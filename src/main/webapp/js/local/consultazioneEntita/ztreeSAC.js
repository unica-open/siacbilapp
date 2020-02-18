/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
 var ZTreeSAC = (function($) {
     "use strict";
     var exports = {};
     exports.caricaStrutturaAmministrativoContabile = caricaStrutturaAmministrativoContabile;
     exports.deseleziona = deseleziona;
    /**
     * Funzione per la creazione di una stringa contenente le informazioni gerarchiche della selezione. Genera solo il codice degli elementi non-foglia.
     *
     * @param treeNode       {Object}  il nodo selezionato
     * @param messaggioVuoto {String}  messaggio da impostare qualora non sia stato selezionato nulla
     * @param regressione    {Boolean} indica se effettuare una regressione sui vari codici
     *
     * @returns {String} la stringa gerarchica creata
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        var nodes = treeNode;
        var parent;
        var string;

        if (!nodes.checked) {
            return messaggioVuoto;
        }
        if (!regressione) {
            return nodes.testo;
        }

        string = nodes.testo;
        parent = nodes.getParentNode();
        while (parent !== null) {
            nodes = parent;
            string = nodes.codice + " - " + string;
            parent = nodes.getParentNode();
        }

        return string;
    }

    /**
     * Metodo di utilit&agrave; per l'impostazione dei dati da zTree a un campo hidden.
     *
     * @param treeNode      {Object} l'oggetto JSON corrispondente alla selezione
     * @param idCampoHidden {String} l'id del campo hidden, senza la sotto-stringa HIDDEN_
     * @param stringa       {String} la stringa contenente la descrizione estesa (ovvero comprendente la descrizione degli elementi superiori in gerarchia) della selezione
     */
    function valorizzaCampi(treeNode, idCampoHidden, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "uid").val(treeNode.uid);
        } else {
            $("#HIDDEN_" + idCampoHidden + "uid").val("");
        }

        $("#SPAN_" + idCampoHidden).html(stringa);
    }

    /**
     * Imposta il valore della Struttura.
     *
     * @param event    {Event}  l'evento scatenato
     * @param treeId   {Number} l'uid dello zTree
     * @param treeNode {Object} il nodo
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode, suffix) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        var pulsante = $("#deselezionaStrutturaAmministrativaResponsabile");
        //TODO: questa cosa e' orribile!!! CAMBIARE!!
        //if(treeId==="treeStruttAmmProvvedimento"){
        valorizzaCampi(treeNode, "StrutturaAmministrativoContabile_"+ treeId + '_', stringa);
        pulsante = $("#deselezionaStrutturaAmministrativaResponsabile_treeStruttAmmProvvedimento");
        /*}else {
        valorizzaCampi(treeNode, "StrutturaAmministrativoContabileCapitolo", stringa);
        pulsante = $("#deselezionaStrutturaAmministrativaResponsabileCapitolo");
        }*/
        if(treeNode.checked) {
            pulsante.removeAttr("disabled");
        } else {
            pulsante.attr("disabled", "disabled");
        }
    }

    //Settings di base per la gestione dello ztree
    var zTreeSettingStrutturaAmministrativoContabile = {
        check: {enable: true, chkStyle: "radio", radioType: "all"},
        data: {
            key: {name: "testo", children: "sottoElementi"},
            simpleData: {enable: true}
        },
        callback: {onCheck: impostaValueStrutturaAmministrativoContabile}
    };





    /**
     * Funzione per l'impostazione dello zTree.
     *
     * @param idList       {String} l'id dell'elemento in cui sar&agrave; posto lo zTree
     * @param setting      {Object} le impostazioni dello zTree
     * @param jsonVariable {Object} la variabile con i dati JSON per il popolamento dello zTree
     * @param suffix        {String} la stringa per la selezione di diversi alberi ("Capitolo" o "Provvedimento")
     */
    function popolaAlbero(idList, setting, jsonVariable, suffix) {

        var tree = $.fn.zTree.init($("#" + idList + suffix), setting, jsonVariable);
        var node;
        var uid = parseInt($("#HIDDEN_StrutturaAmministrativoContabile" + suffix + "Uid").val(), 10);

        // Lego l'evento di deselezione al pulsante
        $("#pulsanteDeselezionaStrutturaAmministrativoContabile_treeStruttAmm"+ suffix).on("click", function(event) {
            event.preventDefault();
            deseleziona("treeStruttAmm" + suffix);
        });

        // Se l'uid selezionato l'elemento corrispondente
        if(!isNaN(uid)) {
            node = tree.getNodeByParam("uid", uid);
            // Evito il check nel caso in cui il nodo sia null
            !!node && tree.checkNode(node, true, true, true);
        }
    }

    /**
     * Deseleziona la struttura Amministrativa Contabile, nel caso selezionata.
     *
     * @param zTreeId {String} l'id dell'elemento contenente lo zTree
     */
    function deseleziona(zTreeId) {
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree && tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }



    /**
     * Ricarica lo zTree della Struttura Amministrativo Contabile.
     */
    function ricaricaTreeStrutturaAmministrativoContabile() {
        var editabile = $("#HIDDEN_StrutturaAmministrativoContabileEditabile").val() === "true";
        if(editabile) {
            caricaStrutturaAmministrativoContabile()
            .then(function() {
                var hiddenField = $("#HIDDEN_StrutturaAmministrativoContabileUid");
                var uid = hiddenField.data("originalUid");
                var albero = $.fn.zTree.getZTreeObj("treeStruttAmm");
                var node;

                hiddenField.val(uid);
                if(albero) {
                    node = albero.getNodeByParam("uid", uid);
                    node && !node.checked && albero.checkNode(node, true, true, true);
                }
                $(document).trigger("strutturaAmministrativoContabileCaricato");
            });
        }
    }

    /**
     * Chiamata Ajax per il caricamento delle SAC
     *
     */
     function caricaStrutturaAmministrativoContabile(){
        /* Pulsante per il modale della Struttura Amministrativo Contabile */

        var spinner = $("#SPINNER_StrutturaAmministrativoContabile");
        /* Non permettere di accedere al modale finchÃ© il caricamento non Ã¨ avvenuto */

        /* Attiva lo spinner */
        spinner.addClass("activated");

        return $.postJSON("ajax/strutturaAmministrativoContabileAjax.do", {})
        .then(function (data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);

            popolaAlbero("treeStruttAmm", zTreeSettingStrutturaAmministrativoContabile, listaStrutturaAmministrativoContabile, "Provvedimento");
            popolaAlbero("treeStruttAmm", zTreeSettingStrutturaAmministrativoContabile, listaStrutturaAmministrativoContabile, "Capitolo");

        }).always(spinner.removeClass.bind(spinner, "activated"));
    }

    return exports;
 }(jQuery));