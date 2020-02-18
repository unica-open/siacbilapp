/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    'use strict';
    var alertErrori = $('#ERRORI');
    var id = 0;
    var instances = [];
    var exports = {};
    var GSAClassifZtreeHandler;

    // Esportazione della funzionalita' in un namespace univoco
    exports.initClassificatoreGSAZtree = initClassificatoreGSAZtree;
    exports.cleanZTree = cleanZTree;
    exports.closeClassificatoreGSACollapse = closeClassificatoreGSACollapse;
    global.GSAClassifZtree = exports;

    // Definizione della classe
    GSAClassifZtreeHandler = function(treeId, uidField) {
        this.id = id++;
        this.treeId = treeId;
        this.uidField = uidField;
        this.$uidField = $(this.uidField);
        this.$deseleziona = $('button[data-deseleziona-ztree="' + this.treeId + '"]');
        // Parametrizzare?
        this.$span = $('#SPAN_classificatoreGSA');
        
        this.ztree = undefined;
        this.opts = {
            callback: {
                onCheck: this.onCheckClassificatoreGSA.bind(this)
            },
            data: {
                key: {
                    children: 'listaClassificatoriGSAFigli'
                }
            }
        };
    };
    
    GSAClassifZtreeHandler.prototype.constructor = GSAClassifZtreeHandler;
    GSAClassifZtreeHandler.prototype.init = init;
    GSAClassifZtreeHandler.prototype.initCallback = initCallback;
    GSAClassifZtreeHandler.prototype.onCheckClassificatoreGSA = onCheckClassificatoreGSA;
    GSAClassifZtreeHandler.prototype.deselezionaNodo = deselezionaNodo;
    
    /**
     * Inizializzazione
     * @returns (Promise<void>) nulla
     */
    function init() {
        this.$deseleziona.substituteHandler('click', this.deselezionaNodo.bind(this));
        
        return $.postJSON('elencoClassificatoreGSA.do')
        .then(this.initCallback.bind(this));
    }
    
    /**
     * Callback di inizializzazione
     * @param data (any) il dato ottenuto dal servizio
     */
    function initCallback(data) {
        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            return $.Deferred().reject().promise();
        }
        this.ztree = new Ztree('', this.treeId, this.opts, this.uidField);
        this.ztree.inizializza(data.listaClassificatoreGSA);
    }
    
    /**
     * Imposta il valore della Struttura.
     *
     * @param event    (event)  l'evento scatenante
     * @param treeId   (number) l'uid dello zTree
     * @param treeNode (any)    il nodo
     */
    function onCheckClassificatoreGSA(event, treeId, treeNode) {
        var stringa = Ztree.creaStringaGerarchica(treeNode, 'Nessun Classificatore selezionato', true);
        this.$uidField.val(treeNode.checked ? treeNode.uid : '');
        this.$span.html(stringa);
        $('#' + treeId).trigger('ztreecheck');
    }
    
    /**
     * Deselezione del nodo
     */
    function deselezionaNodo() {
        if(!this.ztree) {
            // Ztree non ancora inizializzato
            return;
        }
        Ztree.deselezionaNodiEAbilitaAlberatura(this.ztree);
    }
    
    /**
     * Inizializzazione dello ZTree del classificatore GSA
     * @param treeId   (string) l'id dell'elemento che conterra' l'albero
     * @param uidField (string) il selettore del campo che contiene l'uid dell'albero
     * @returns (number) l'id dell'istanza
     */
    function initClassificatoreGSAZtree(treeId, uidField) {
        var paramTreeId = treeId || 'classGSATree';
        var paramUidField = uidField || '#HIDDEN_classificatoreGSAUid';
        var tree = new GSAClassifZtreeHandler(paramTreeId, paramUidField);
        
        tree.init();
        
        instances[tree.id] = tree;
        return tree.id;
    }
    
    /**
     * Pulizia dello ZTree
     * @param id (number) l'id dell'albero da pulire
     * @returns (boolean) se la pulizia sia andata a buon fine
     */
    function cleanZTree(id) {
        var tree = instances[id];
        if(tree === undefined || tree.ztree === undefined) {
            // Albero non presente: esco
            return false;
        }
        tree.deselezionaNodo();
        return true;
    }
    
    /**
     * Chiusura del collapse del classificatore GSA
     */
    function closeClassificatoreGSACollapse() {
        var collapseToggle = $('#classGSAParent').find('[data-toggle="collapse"]');
        var collapse = $(collapseToggle.attr('href'));
        if(collapse.hasClass('in')) {
            collapseToggle.click();
        }
    }
}(jQuery, this);