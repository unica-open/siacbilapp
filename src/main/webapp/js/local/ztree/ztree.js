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
    
    var Ztree = function(codificaName,suffix, messaggioNessunElementoSelezionato, nomeSottoElementi){
    	this.suffix = suffix || '';
    	this.zTreeId = '#tree'+ codificaName + this.suffix;
    	this.modaleCodifica = $('#' + lowerFirstChar(codificaName) + this.suffix);
    	this.selectorUidHidden = "#HIDDEN_" + codificaName + "Uid" + this.suffix;
    	this.selectorStringaHidden = "#HIDDEN_" + codificaName + "Stringa";
    	this.spanStringaHidden = "#SPAN_" + codificaName + this.suffix;
    	this.messaggioNoSelezione = messaggioNessunElementoSelezionato || "Nessun elemento selezionato";
    	this.selectorPulsanteDeselezione = '#deseleziona' + codificaName + this.suffix;
    	this.idList = 'tree' + codificaName  + this.suffix;
    	this.selectorCampoCodice = '#HIDDEN_'+ codificaName +'Codice' + this.suffix;
    	this.selectorCampoDesc ='#HIDDEN_' + codificaName + 'Descrizione' + this.suffix;
    	
    	var childrenName = nomeSottoElementi || "sottoElementi";
    	
    	//qui come segnaposto, verra' impostato piu' avanti
    	this.innerZtreeObj = {};
    	/** I settings base dello ZTree */
        this.SettingsBase = {
            check: {enable: true, chkStyle: "radio", radioType: "all"},
            data: {key: {name: "testo", children: childrenName}, simpleData: {enable: true}},
            callback: {onCheck: impostaValueZtree.bind(this)}
        };
    };
    Ztree.prototype.constructor = Ztree;
    Ztree.prototype.inizializza = inizializza;
    Ztree.prototype.destroy = destroy;
    Ztree.prototype.impostaValueZtree = impostaValueZtree;
    Ztree.prototype.deselezionaNodiEAbilitaAlberatura = deselezionaNodiEAbilitaAlberatura;
    Ztree.prototype.selezionaNodoSeApplicabile = selezionaNodoSeApplicabile;
    
    w.Ztree = Ztree;
    
    function deseleziona (event) {
    	event.preventDefault();
    	
        var tree = this.innerZtreeObj; 
        var nodo = tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
        
        this.modaleCodifica.hasClass('modal') && this.modaleCodifica.modal('hide');
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
    function valorizzaCampi (treeNode, stringa) {
        if(treeNode.checked) {
            $(this.selectorUidHidden).val(treeNode.uid);
        } else {
            $(this.selectorUidHidden).val("");
        }
        $(this.selectorStringaHidden).val(stringa);
        $(this.spanStringaHidden).html(stringa);
    }

    /**
     * Imposta il valore della Struttura.
     *
     * @param event      (Event)  l'evento scatenante
     * @param treeId     (Number) l'uid dello zTree
     * @param treeNode   (Object) il nodo
     */
    function impostaValueZtree(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, this.messaggioNoSelezione, true);
        valorizzaCampi.bind(this, treeNode, stringa)();
        $(this.selectorPulsanteDeselezione).on('click',deseleziona.bind(this));
        $(this.zTreeId).trigger('ztreecheck');
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
    	var idList = this.idList;
        var list = $(this.zTreeId);
        var uid = parseInt($(this.selectorUidHidden).val(), 10);
        this.innerZtreeObj= $.fn.zTree.init(list, this.SettingsBase, jsonVariable);
        var containingForm = list.closest("form");

        // Selezione subito il nodo, nel caso in cui cio' sia possibile
        selezionaNodoSeApplicabile.call(this, uid);

        // Prendo il form che contiene l'elemento o un form generico
        $(containingForm.length && containingForm || "form").on('reset',deselezionaNodiEAbilitaAlberatura.bind(this));
    }

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     *
     * @param idList (String) l'id della lista in cui apporre la zTree
     * @param uid    (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    function selezionaNodoSeApplicabile(uid) {
        var tree = this.innerZtreeObj;
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
        var tree =this.innerZtree;
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

        selezionaNodoSeApplicabile.call(this, uid);

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