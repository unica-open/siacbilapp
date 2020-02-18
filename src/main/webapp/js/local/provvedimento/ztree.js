/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * Gestione ZTree
 *
 */

var ZTree = (function() {
    var exports = {};

    /**
     * Deseleziona la struttura Amministrativa Contabile, nel caso selezionata.
     *
     * @param zTreeId {String} l'id dell'elemento contenente lo zTree
     */
    function deseleziona (zTreeId) {
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }

    /**
     * Crea una stringa gerarchica per il nodo.
     *
     * @param treeNode       {Object}  il nodo
     * @param messaggioVuoto {String}  il messaggio nel caso in cui non sia stato selezionato nulla
     * @param regressione    {Boolean} se sia necessaria una regressione sui nodi
     *
     * @returns {String} la stringa gerarchica ottenuta
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
     * Valorizza i campi hidden.
     *
     * @param treeNode      {Object} il nodo
     * @param idCampoHidden {String} l'uid del campo hidden
     * @param stringa       {String} la stringa da apporre
     */
    function valorizzaCampi (treeNode, idCampoHidden, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "Uid").val(treeNode.uid);
        } else {
            $("#HIDDEN_" + idCampoHidden + "Uid").val("");
        }
        $("#HIDDEN_" + idCampoHidden + "Stringa").val(stringa);
        $("#SPAN_" + idCampoHidden).html(stringa);
    }

    /**
     * Imposta il valore della Struttura.
     *
     * @param event    {Event}  l'evento scatenato
     * @param treeId   {Number} l'uid dello zTree
     * @param treeNode {Object} il nodo
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        var pulsante = $("#deselezionaStrutturaAmministrativaResponsabile");

        valorizzaCampi(treeNode, "StrutturaAmministrativoContabile", stringa);
        if(treeNode.checked) {
            pulsante.removeAttr("disabled");
        } else {
            pulsante.attr("disabled", "disabled");
        }
    }

    /**
     * Imposta lo ZTree.
     *
     * @param idList       {String} l'id della lista
     * @param settings     {Object} le impostazioni
     * @param jsonVariable {Array}  la lista da impostare
     */
    exports.imposta = function (idList, setting, jsonVariable, suffix) {
        var tree = $.fn.zTree.init($("#" + idList), setting, jsonVariable);
        var node;
        var uid = parseInt($("#HIDDEN_StrutturaAmministrativoContabileUid").val(), 10);

        // Lego l'evento di deselezione al pulsante
        $("#deselezionaStrutturaAmministrativaResponsabile").on("click", function(event) {
            event.preventDefault();
            deseleziona("treeStruttAmm");
        });

        // Se l'uid è selezionato l'elemento corrispondente
        if(!isNaN(uid)) {
            node = tree.getNodeByParam("uid", uid);
            // Evito il check nel caso in cui il nodo sia null
            !!node && tree.checkNode(node, true, true, true);
        }
    };

    /** I settings base dello ZTree */
    exports.SettingsBase = {
        check: {enable: true, chkStyle: "radio", radioType: "all"},
        data: {
            key: {name: "testo", children: "sottoElementi"},
            simpleData: {enable: true}
        },
        callback: {onCheck: impostaValueStrutturaAmministrativoContabile}
    };

    return exports;
}());

$(
    function() {
        // Controllo se il pulsante di deselezione della struttura debba essere disabilitato o meno
        var uidStrutturaAmministrativoResponsabile = $("#HIDDEN_StrutturaAmministrativoContabileUid").val();
        if(!uidStrutturaAmministrativoResponsabile) {
            $("#deselezionaStrutturaAmministrativaResponsabile").attr("disabled", "disabled");
        }
    }
);
