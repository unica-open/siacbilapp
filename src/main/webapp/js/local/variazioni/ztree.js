/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* **** Gestione degli ZTree **** */

Variazioni.ZTree = (function() {
    var exports = {};

    // Settings base per lo zTree
    var zTreeSettingsBase = {
        check: {enable : true, chkStyle: "radio",radioType: "all"},
        data: {
            key: {name: "testo", children: "sottoElementi"},
            simpleData: {enable: true}
        }
    };
    // Settings per lo zTree per la Struttura Amministrativo Contabile.
    var zTreeSettingStrutturaAmministrativoContabile = $.extend(true, {}, zTreeSettingsBase, {
            callback: {onCheck: impostaValueStrutturaAmministrativoContabile}
        }
    );
    // Settings per lo zTree per il SIOPE.
    var zTreeSettingSIOPE = $.extend(true, {}, zTreeSettingsBase,{
            callback: {onCheck: impostaValueSIOPE}
        }
    );
    // Settings per lo zTree per l'Elemento del Piano Dei Conti
    var zTreeSettingElementoPianoDeiConti = $.extend(true, {}, zTreeSettingsBase,{
            callback: {beforeCheck: controllaLivelloPianoDeiConti, onCheck: impostaValueElementoPianoDeiConti}
        }
    );

    /**
     * Carica i dati nello zTree del SIOPE da chiamata AJAX.
     *
     * @param elementoCapitoloCodifiche {Object} l'elemento della codifica
     */
    function caricaSIOPE(elementoCapitoloCodifiche) {
        var tipoSiope = (exports.tipoCapitolo === "Uscita") ? "Spesa" : "Entrata";
        var nomeCampo = "siope" + tipoSiope + "Capitolo" + exports.tipoCapitolo;
        var spinner = $("#" + nomeCampo + "Spinner");
        var urlDaChiamare = "ajax/siope" + tipoSiope + "Ajax.do";
        var uidChiamante = elementoCapitoloCodifiche ? elementoCapitoloCodifiche.elementoPianoDeiConti.uid : $("#elementoPianoDeiContiCapitolo" + Variazioni.ZTree.tipoCapitolo).val();
        var oggettoPerChiamataAjax = {};

        if(elementoCapitoloCodifiche && elementoCapitoloCodifiche["siope" + tipoSiope] && !elementoCapitoloCodifiche["siope" + tipoSiope].dataFineValidita) {
            $("#" + nomeCampo).val(elementoCapitoloCodifiche["siope" + tipoSiope].uid);
        } else {
            $("#" + nomeCampo).val("");
            $("#" + nomeCampo + "Span").html("Nessun SIOPE selezionato");
        }

        // Popolo l'oggetto per la chiamata AJAX
        oggettoPerChiamataAjax.id = uidChiamante;
        oggettoPerChiamataAjax["daInjettare" + exports.tipoAzione +"Variazione"] = true;

        /* Attiva lo spinner */
        spinner.addClass("activated");

        $.postJSON(
            urlDaChiamare,
            oggettoPerChiamataAjax,
            function (data) {
                var listaSIOPE = (data.listaElementoCodifica);
                var options = $("#" + nomeCampo + "Pulsante");

                impostaZTree(nomeCampo + "Tree", zTreeSettingSIOPE, listaSIOPE);
                // Se il bottone è disabilitato, lo si riabiliti
                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                options.attr("href", "#" + nomeCampo + "Modale");
            }
        ).always(
            function() {
                // Disattiva lo spinner anche in caso di errore
                spinner.removeClass("activated");
            }
        );
    }

    /**
     * Funzione per l'impostazione dello zTree.
     *
     * @param idList       {String} l'id dell'elemento in cui sar&agrave; posto lo zTree
     * @param setting      {Object} le impostazioni dello Variazioni.ZTree
     * @param jsonVariable {Object} la variabile con i dati JSON per il popolamento dello zTree
     */
    function impostaZTree(idList, setting, jsonVariable) {
        var tree = $.fn.zTree.init($("#" + idList), setting, jsonVariable);
        // Estraggo il nome del campo togliendo la stringa Tree
        var idCampo = idList.slice(0, -4);
        var uid = parseInt($("#" + idCampo).val(), 10);
        var node;
        var idPulsante = idCampo + "Deseleziona";

        // Lego l'evento di deselezione al pulsante
        $("#" + idPulsante).on("click", function(event) {
            event.preventDefault();
            deseleziona(idList);
        });

        // Se l'uid è selezionato l'elemento corrispondente
        if(!isNaN(uid)) {
            node = tree.getNodeByParam("uid", uid);
            // Evito il check nel caso in cui il nodo sia null
            !!node && tree.checkNode(node, true, true, true);
        }
    }

    /**
     * Funzione per la creazione di una stringa contenente le informazioni gerarchiche della selezione. Genera solo il codice degli elementi non-foglia.
     *
     * @param treeNode       {Object} il nodo selezionato
     * @param messaggioVuoto {String} messaggio da impostare qualora non sia stato selezionato nulla
     * @param regressione    {boolean} indica se effettuare una regressione sui vari codici
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
     * @param idCampoHidden {String} l'id del campo hidden
     * @param stringa       {String} la stringa contenente la descrizione estesa (ovvero comprendente la descrizione degli elementi superiori in gerarchia) della selezione
     */
    function valorizzaCampi(treeNode, idCampoHidden, stringa) {
        var idCampo = idCampoHidden + "Capitolo" + exports.tipoCapitolo;
        treeNode.checked ? $("#" + idCampo).val(treeNode.uid) : $("#" + idCampo).val("");
        $("#" + idCampo + "Span").html(stringa);
    }

    /**
     * Calcola il livello del piano dei conti.
     *
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Integer} il livello del piano dei conti
     */
    function calcolaLivelloPianoDeiConti(treeNode) {
        var array = treeNode.codice.split(".");
        var index;
        for(index = 1; index < array.length && array[index] > 0; index++) {/* Empty */}
        return --index;
    }

    /**
     * Controllare che la selezione dell'elemento del Piano dei Conti corrisponda almeno al quarto livello dello stesso.
     *
     * @param treeId   {String} l'id univoco dello Variazioni.ZTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Boolean} <code>true</code> qualora il livello selezionato sia
     *          valido, e scatena pertanto l'evento onClick; <code>false</code> in
     *          caso contrario, e inibisce lo scatenarsi dell'evento onclick.
     */
    function controllaLivelloPianoDeiConti(treeId, treeNode) {
        var livello = calcolaLivelloPianoDeiConti(treeNode);

        // Se il livello non è almeno pari a 4, segnalo l'errore
        if (livello <= 3) {
            bootboxAlert("Selezionare almeno il quarto livello del Piano dei Conti. Livello selezionato: " + livello);
        }
        return livello > 3;
    }

    /**
     * Imposta il valore della Struttura Amministrativo Contabile in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello Variazioni.ZTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        var pulsante = $("#strutturaAmministrativaContabileCapitolo" + exports.tipoCapitolo + "Deseleziona");
        valorizzaCampi(treeNode, "strutturaAmministrativoContabile", stringa);
        treeNode.checked ? pulsante.removeAttr("disabled") : pulsante.attr("disabled", "disabled");
    }

    /**
     * Imposta il valore del SIOPE in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello Variazioni.ZTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueSIOPE(event, treeId, treeNode) {
        var tipoSiope = exports.tipoCapitolo === "Entrata" ? "Entrata" : "Spesa";
        var stringa = creaStringaGerarchica(treeNode, "Nessun SIOPE selezionato", true);
        var pulsante = $("#siope" + tipoSiope + "Capitolo" + exports.tipoCapitolo + "Deseleziona");
        valorizzaCampi(treeNode, "siope" + tipoSiope, stringa);
        treeNode.checked ? pulsante.removeAttr("disabled") : pulsante.attr("disabled", "disabled");
    }

    /**
     * Imposta il valore dell'Elemento del Piano Dei Conti in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello Variazioni.ZTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueElementoPianoDeiConti(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessun P.d.C. finanziario selezionato", false);
        var pulsante = $("#elementoPianoDeiContiCapitolo" + exports.tipoCapitolo + "Deseleziona");
        valorizzaCampi(treeNode, "elementoPianoDeiConti", stringa);
        treeNode.checked ? pulsante.removeAttr("disabled") : pulsante.attr("disabled", "disabled");

        // Chiamata per il caricamento del SIOPE
        caricaSIOPE();
    }

    /**
     * Deseleziona la struttura Amministrativa Contabile, nel caso selezionata.
     *
     * @param zTreeId {String} l'id dell'elemento contenente lo Variazioni.ZTree
     */
    function deseleziona(zTreeId) {
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }

    /** Il caso d'uso della variazione, entrata o uscita */
    exports.tipoCapitolo = undefined;
    /** Il tipo di azione, di aggiornamento o di inserimento */
    exports.tipoAzione = undefined;

    /**
     * Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     * @param elementoCapitoloCodifiche {Object} l'elemento della codifica
     */
    exports.caricaPianoDeiConti = function (elementoCapitoloCodifiche) {
        var nomeCampo = "elementoPianoDeiContiCapitolo" + exports.tipoCapitolo;
        var spinner = $("#" + nomeCampo + "Spinner");
        var casoDiUscita = exports.tipoCapitolo === "Uscita";
        if((casoDiUscita && !$("#macroaggregatoCapitoloUscita").val() ) || (!casoDiUscita && !$("#categoriaTipologiaTitoloCapitoloEntrata").val())){
        	return;
        }
        var uidChiamante = elementoCapitoloCodifiche ?
            (casoDiUscita ? elementoCapitoloCodifiche.macroaggregato.uid : elementoCapitoloCodifiche.categoriaTipologiaTitolo.uid) :
            (casoDiUscita ? $("#macroaggregatoCapitoloUscita").val() : $("#categoriaTipologiaTitoloCapitoloEntrata").val());
        var oggettoPerChiamataAjax = {};

        if(elementoCapitoloCodifiche && elementoCapitoloCodifiche.elementoPianoDeiConti && !elementoCapitoloCodifiche.elementoPianoDeiConti.dataFineValidita) {
            $("#" + nomeCampo).val(elementoCapitoloCodifiche.elementoPianoDeiConti.uid);
        } else {
            $("#" + nomeCampo).val("");
            $("#" + nomeCampo + "Span").html("Nessun P.d.C. finanziario selezionato");
        }

        // Popolo l'oggetto per la chiamata AJAX
        oggettoPerChiamataAjax.id = uidChiamante;
        oggettoPerChiamataAjax["daInjettare" + exports.tipoAzione + "Variazione"] = true;

        /* Attiva lo spinner */
        spinner.addClass("activated");

        $.postJSON(
            "ajax/elementoPianoDeiContiAjax.do",
            oggettoPerChiamataAjax,
            function (data) {
                var listaElementoPianoDeiConti = (data.listaElementoCodifica);
                var options = $("#" + nomeCampo + "Pulsante");
                var codiceTitolo;
                var albero;
                if(exports.tipoCapitolo === "Entrata") {
                    codiceTitolo = $("#titoloEntrataCapitoloEntrataCodice").val();
                }

                impostaZTree(nomeCampo + "Tree", zTreeSettingElementoPianoDeiConti, listaElementoPianoDeiConti);
                // Se il bottone è disabilitato, lo si riabiliti
                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }

                // Controllo se sono nel caso d'uso di entrata: in tal caso, effettuo il check del primo elemento nel caso in cui non vi sia un altro elemento selezionato
                if(codiceTitolo && codiceTitolo <= 2 && listaElementoPianoDeiConti && listaElementoPianoDeiConti.length > 0) {
                    albero = $.fn.zTree.getZTreeObj("treePDC");
                    albero.checkNode(albero.getNodes()[0], true, true, true);
                }
                options.attr("href", "#" + nomeCampo + "Modale");
                caricaSIOPE(elementoCapitoloCodifiche);
            }
        ).always(
            function() {
                // Disattiva lo spinner anche in caso di errore
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Carica i dati nello zTree della Struttura Amministrativo Contabile da chiamata AJAX.
     *
     * @param elementoCapitoloCodifiche {Object} l'elemento della codifica
     */
    exports.caricaStrutturaAmministrativoContabile = function (elementoCapitoloCodifiche) {
        var nomeCampo = "strutturaAmministrativoContabileCapitolo" + exports.tipoCapitolo;
        var spinner = $("#" + nomeCampo + "Spinner");
        var oggettoPerChiamataAjax = {};

        if(elementoCapitoloCodifiche && elementoCapitoloCodifiche.strutturaAmministrativoContabile && !elementoCapitoloCodifiche.strutturaAmministrativoContabile.dataFineValidita) {
            $("#" + nomeCampo).val(elementoCapitoloCodifiche.strutturaAmministrativoContabile.uid);
        } else {
            $("#" + nomeCampo).val("");
            $("#" + nomeCampo + "Span").html("Nessuna Struttura Amministrativa Responsabile selezionata");
        }

        // Popolo l'oggetto per la chiamata AJAX
        oggettoPerChiamataAjax["daInjettare" + exports.tipoAzione +"Variazione"] = true;

        /* Attiva lo spinner */
        spinner.addClass("activated");

        $.postJSON(
            "ajax/strutturaAmministrativoContabileAjax.do",
            oggettoPerChiamataAjax,
            function (data) {
                var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
                var options = $("#" + nomeCampo + "Pulsante");

                impostaZTree(nomeCampo + "Tree", zTreeSettingStrutturaAmministrativoContabile, listaStrutturaAmministrativoContabile);
                // Se il bottone è disabilitato, lo si riabiliti
                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                options.attr("href", "#" + nomeCampo + "Modale");
            }
        ).always(
            function() {
                // Disattiva lo spinner anche in caso di errore
                spinner.removeClass("activated");
            }
        );
    };

    return exports;
}());

$(function() {
    // Carico una volta sola il tipo di azione: inserimento o aggiornamento
    Variazioni.ZTree.tipoAzione = $("#tipoAzione").val();
});