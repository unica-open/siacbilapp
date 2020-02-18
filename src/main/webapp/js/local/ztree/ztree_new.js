/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function (w, $) {
	'use strict';
    var settingsBase = {
        check: {enable: true, chkStyle: 'radio', radioType: 'all'},
        data: {key: {name: 'testo', children: 'sottoElementi'}, simpleData: {enable: true}}
    };
    var $document = $(document);
    
    var Ztree = function(suffix, treeId, settings, uidField) {
        this.suffix = suffix || '';
        this.treeId = (treeId || 'treeStruttAmm') + this.suffix;
        this.settings = $.extend(true, {}, settingsBase, {callback: {onCheck: impostaValueStrutturaAmministrativoContabile.bind(this)}}, settings || {});
        this.uidField = uidField || ('#HIDDEN_StrutturaAmministrativoContabile'+ this.suffix +'Uid');
        this.innerTree = undefined;
    };

    Ztree.prototype.constructor = Ztree;
    Ztree.prototype.inizializza = inizializza;
    Ztree.prototype.destroy = destroy;
    Ztree.prototype.impostaValueStrutturaAmministrativoContabile = impostaValueStrutturaAmministrativoContabile;
    Ztree.prototype.deseleziona = deseleziona;
    Ztree.prototype.valorizzaCampi = valorizzaCampi;

    Ztree.deselezionaNodiEAbilitaAlberatura = deselezionaNodiEAbilitaAlberatura;
    Ztree.selezionaNodoSeApplicabile = selezionaNodoSeApplicabile;
    Ztree.deselezionaNodoSeApplicabile = deselezionaNodoSeApplicabile;
    Ztree.selezionaNodoSeApplicabileEDisabilitaAlberatura = selezionaNodoSeApplicabileEDisabilitaAlberatura;
    Ztree.deselezionaNodoSeApplicabileEAbilitaAlberatura = deselezionaNodoSeApplicabileEAbilitaAlberatura;
    Ztree.creaStringaGerarchica = creaStringaGerarchica;

    w.Ztree = Ztree;

    function deseleziona (event) {
        var tree = $.fn.zTree.getZTreeObj(this.treeId);
        var nodo = tree.getCheckedNodes(true)[0];
        event.preventDefault();

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
            throw new Error('Nessun nodo su cui effettuare la creazione della stringa');
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
            string = nodes.codice + ' - ' + string;
            parent = nodes.getParentNode();
        }

        return string;
    }

    /**
     * Valorizza i campi hidden, pulendo il campo dell'uid nel caso in cui il nodo sia stato deselezionato.
     *
     * @param treeNode      (Object) il nodo
     * @param stringa       (String) la stringa da apporre nei campi testuali
     */
    function valorizzaCampi (treeNode, stringa) {
        $('#HIDDEN_StrutturaAmministrativoContabileUid' + this.suffix).val(treeNode.checked ? treeNode.uid : '');
        $('#SPAN_StrutturaAmministrativoContabile' + this.suffix).html(stringa);
    }

    /**
     * Imposta il valore della Struttura.
     *
     * @param event      (Event)  l'evento scatenante
     * @param treeId     (Number) l'uid dello zTree
     * @param treeNode   (Object) il nodo
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, 'Nessuna Struttura Amministrativa Responsabile selezionata', true);
        this.valorizzaCampi(treeNode, stringa);
        var evt = $.Event("selezionataStrutturaAmministrativaContabile");
        $('#deselezionaStrutturaAmministrativaResponsabile' + this.suffix).on('click',deseleziona.bind(this));
        $('#' + treeId).trigger('ztreecheck');
        // Lancio un evento sul DOM
        evt.treeId = treeId;
        evt.treeNode = treeNode;
        $document.trigger(evt);
    }

    /**
     * Deseleziona i nodi all'interno dello zTree.
     * <br />
     * Inoltre, abilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     */
    function deselezionaNodiEAbilitaAlberatura(tree) {
        var nodeArray;
        var node;
        var index;
        if(!tree.innerTree) {
            return;
        }

        nodeArray = tree.innerTree.getNodes();

        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                tree.innerTree.setChkDisabled(node, false, true, true);
            }
        }
        // Deseleziono i nodi checkati
        nodeArray = tree.innerTree.getCheckedNodes(true);
        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                tree.innerTree.checkNode(node, false, true, true);
            }
        }
    }

    /**
     * Imposta lo ZTree.
     *
     * @param jsonVariable  (Array)  la lista da impostare
     */
    function inizializza(jsonVariable) {
        var list = $('#' + this.treeId);
        var uid = parseInt($(this.uidField).val(), 10);
        var tree = $.fn.zTree.init(list, this.settings, jsonVariable);
        var containingForm = list.closest('form');
        this.innerTree = tree;

        // Selezione subito il nodo, nel caso in cui cio' sia possibile
        selezionaNodoSeApplicabile(this.treeId, uid);

        // Prendo il form che contiene l'elemento o un form generico
        $(containingForm.length && containingForm || 'form').on('reset', function() {
            deselezionaNodiEAbilitaAlberatura(tree);
        });
    }

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     *
     * @param idList (String) l'id della lista in cui apporre la zTree
     * @param uid    (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    function selezionaNodoSeApplicabile(idList, uid) {
        var tree = $.fn.zTree.getZTreeObj(idList);
        var node;
        if(isNaN(parseInt(uid, 10)) || !tree) {
            return;
        }
        node = tree.getNodeByParam('uid', uid);
        if(node) {
            tree.refresh();
            // Evito il check nel caso in cui il nodo sia null
            tree.checkNode(node, true, true, true);
        }
    }
    
    /**
     * Deseleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     * @param tree (Ztree) lo ZTree
     * @param uid  (number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    function deselezionaNodoSeApplicabile(tree, uid) {
        var node;
        if (isNaN(parseInt(uid, 10)) || !tree) {
            return;
        }

        node = tree.getNodeByParam('uid', uid);
        // Evito il check nel caso in cui il nodo sia null
        if(node) {
            tree.refresh();
            tree.checkNode(node, false, true, true);
        }
    }

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     * <br>
     * Inoltre, disabilita l'intera alberatura
     *
     * @param idList (String) l'id della lista in cui apporre la zTree
     * @param uid    (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    function selezionaNodoSeApplicabileEDisabilitaAlberatura(idList, uid) {
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

        selezionaNodoSeApplicabile(idList, uid);

        // Disabilito temporaneamente i nodi
        nodeArray = tree.getNodes();
        for(node in nodeArray) {
            if(nodeArray.hasOwnProperty(node)) {
                tree.setChkDisabled(nodeArray[node], true, true, true);
            }
        }
    }
    
    function destroy(){
    	
    }
    
    /**
     * Deseleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile. <br>
     * Inoltre, abilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     * @param uid  (number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    function deselezionaNodoSeApplicabileEAbilitaAlberatura (tree, uid) {
        deselezionaNodoSeApplicabile(tree, uid);

        tree.getNodes().forEach(function(el) {
            tree.setChkDisabled(el, false, true, true);
        });
    }

}(this, $));