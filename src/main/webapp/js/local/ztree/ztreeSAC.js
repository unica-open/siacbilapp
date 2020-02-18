/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Gestione ad oggetti dello ztree. Permette una gestione generica di uno ztree tramite un suffisso opzionale 
 * che puo' essere o meno fornito
 * 
 * */
;(function (w, $) {
	"use strict";
    var exports = {};
    
    var Ztree = function(suffix){
    	this.suffix = suffix || '';
    	//qui come segnaposto, verra' impostato piu' avanti
    	this.innerZtreeObj = {};
    	/** I settings base dello ZTree */
        this.SettingsBase = {
            check: {enable: true, chkStyle: "radio", radioType: "all"},
            data: {key: {name: "testo", children: "sottoElementi"}, simpleData: {enable: true}},
            callback: {onCheck: impostaValueStrutturaAmministrativoContabile.bind(this)}
        };
    };
    Ztree.prototype.constructor = Ztree;
    Ztree.prototype.inizializza = inizializza;
    Ztree.prototype.destroy = destroy;
    Ztree.prototype.impostaValueStrutturaAmministrativoContabile = impostaValueStrutturaAmministrativoContabile;
    Ztree.prototype.deselezionaNodiEAbilitaAlberatura = deselezionaNodiEAbilitaAlberatura;
    
    w.Ztree = Ztree;
    
    function deseleziona (event) {
    	event.preventDefault();
    	var zTreeId = 'treeStruttAmm' + this.suffix;
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
       
        var nodo = tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
        $('#struttAmm' + this.suffix).modal('hide');
    }
    /**
     * Crea una stringa gerarchica per il nodo.
     *
     * @param treeNode       (Object)  il nodo
     * @param messaggioVuoto (String)  il messaggio nel caso in cui non sia stato selezionato nulla
     * @param regressione    (Boolean) se sia necessaria una regressione sui nodi
     *
     * @returns (String) la stringa gerarchica ottenuta
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        // Il nodo corrente
        var nodes = treeNode;
        // Il nodo padre
        var parent;
        // La stringa da comporre
        var string;
        if(!nodes) {
            throw new Error("Nessun nodo su cui effettuare la creazione della stringa");
        }
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
     * Valorizza i campi hidden, pulendo il campo dell'uid nel caso in cui il nodo sia stato deselezionato.
     *
     * @param treeNode      (Object) il nodo
     * @param idCampoHidden (String) l'uid del campo hidden
     *      formato standard per i campi hidden: HIDDEN_#idCampoHidden#Uid, HIDDEN_#idCampoHidden#Stringa, SPAN_#idCampoHidden#
     * @param stringa       (String) la stringa da apporre nei campi testuali
     */
    function valorizzaCampi (treeNode, idCampoHidden, suffix, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "Uid" + suffix).val(treeNode.uid);
        } else {
            $("#HIDDEN_" + idCampoHidden + "Uid" + suffix).val("");
        }
        $("#HIDDEN_" + idCampoHidden + "Stringa").val(stringa);
        $("#SPAN_" + idCampoHidden + suffix).html(stringa);
    }

    /**
     * Imposta il valore della Struttura.
     *
     * @param event      (Event)  l'evento scatenante
     * @param treeId     (Number) l'uid dello zTree
     * @param treeNode   (Object) il nodo
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        valorizzaCampi(treeNode, "StrutturaAmministrativoContabile", this.suffix, stringa);
        $('#deselezionaStrutturaAmministrativaResponsabile' + this.suffix).on('click',deseleziona.bind(this));
        $('#' + treeId).trigger('ztreecheck');
    }

    /**
     * Deseleziona i nodi all'interno dello zTree.
     * <br />
     * Inoltre, abilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     */
    function deselezionaNodiEAbilitaAlberatura() {
        var nodeArray;
        var node;
        var index;
        var tree = this.innerZtreeObj;

        nodeArray = tree.getNodes();

        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                tree.setChkDisabled(node, false, true, true);
            }
        }
        // Deseleziono i nodi checkati
        nodeArray = tree.getCheckedNodes(true);
        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                tree.checkNode(node, false, true, true);
            }
        }
    };

    /**
     * Imposta lo ZTree.
     *
     * @param jsonVariable  (Array)  la lista da impostare
     */
    function inizializza(jsonVariable) {
    	var idList = 'treeStruttAmm' + this.suffix;
        var list = $('#' + idList);
        var uid = parseInt($('#HIDDEN_StrutturaAmministrativoContabileUid' + this.suffix ).val(), 10);
        this.innerZtreeObj= $.fn.zTree.init(list, this.SettingsBase, jsonVariable);
        var containingForm = list.closest("form");

        // Selezione subito il nodo, nel caso in cui cio' sia possibile
        exports.selezionaNodoSeApplicabile(idList, uid);

        // Prendo il form che contiene l'elemento o un form generico
        $(containingForm.length && containingForm || "form").on('reset',deselezionaNodiEAbilitaAlberatura.bind(this));
    }

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     *
     * @param idList (String) l'id della lista in cui apporre la zTree
     * @param uid    (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.selezionaNodoSeApplicabile = function(idList, uid) {
        var tree = $.fn.zTree.getZTreeObj(idList);
        var node;
        if(isNaN(parseInt(uid, 10)) || !tree) {
            return;
        }
        node = tree.getNodeByParam("uid", uid);
        if(node) {
            tree.refresh();
            // Evito il check nel caso in cui il nodo sia null
            tree.checkNode(node, true, true, true);
        }
    };

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     * <br>
     * Inoltre, disabilita l'intera alberatura
     *
     * @param idList (String) l'id della lista in cui apporre la zTree
     * @param uid    (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.selezionaNodoSeApplicabileEDisabilitaAlberatura = function(idList, uid) {
        var tree = $.fn.zTree.getZTreeObj(idList);
        var nodeArray;
        var node;
        if(!tree) {
            return;
        }

        // Abilito temporaneamente i nodi e li dechecko
        nodeArray = tree.getNodes();
        for(node in nodeArray) {
            if(nodeArray.hasOwnProperty(node)) {
                tree.setChkDisabled(nodeArray[node], false, true, true);
                tree.checkNode(nodeArray[node], false, true, true);
            }
        }

        exports.selezionaNodoSeApplicabile(idList, uid);

        // Disabilito temporaneamente i nodi
        nodeArray = tree.getNodes();
        for(node in nodeArray) {
            if(nodeArray.hasOwnProperty(node)) {
                tree.setChkDisabled(nodeArray[node], true, true, true);
            }
        }
    };
    
    function destroy(){
    	
    }

}(this, $));