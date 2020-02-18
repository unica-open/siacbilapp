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
    var QuadroEconomicoZtreeHandler;

    // Esportazione della funzionalita' in un namespace univoco
    exports.initQuadroEconomicoZtree = initQuadroEconomicoZtree;
    exports.cleanZTree = cleanZTree;
    exports.closeQuadroEconomicoCollapse = closeQuadroEconomicoCollapse;
    global.QuadroEconomicoZtree = exports;

    // Definizione della classe
    QuadroEconomicoZtreeHandler = function(treeId, uidField) {
        this.id = id++;
        this.treeId = treeId;
        this.uidField = uidField;
        this.$uidField = $(this.uidField);
        this.$deseleziona = $('button[data-deseleziona-ztree="' + this.treeId + '"]');
        // Parametrizzare?
        this.$span = $('#SPAN_QuadroEconomico');
        
        this.ztree = undefined;
        this.opts = {
            callback: {
                onCheck: this.onCheckQuadroEconomico.bind(this)
            },
            data: {
                key: {
                    children: 'listaQuadroEconomicoFigli'
                }
            }
        };
    };
    
    QuadroEconomicoZtreeHandler.prototype.constructor = QuadroEconomicoZtreeHandler;
    QuadroEconomicoZtreeHandler.prototype.init = init;
    QuadroEconomicoZtreeHandler.prototype.initCallback = initCallback;
    QuadroEconomicoZtreeHandler.prototype.onCheckQuadroEconomico = onCheckQuadroEconomico;
    QuadroEconomicoZtreeHandler.prototype.deselezionaNodo = deselezionaNodo;
    
    /**
     * Inizializzazione
     * @returns (Promise<void>) nulla
     */
    function init() {
        this.$deseleziona.substituteHandler('click', this.deselezionaNodo.bind(this));
        
        return $.postJSON('elencoQuadroEconomico.do')
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
        this.ztree.inizializza(data.listaQuadroEconomico);
    }
    
    /**
     * Imposta il valore della Struttura.
     *
     * @param event    (event)  l'evento scatenante
     * @param treeId   (number) l'uid dello zTree
     * @param treeNode (any)    il nodo
     */
    function onCheckQuadroEconomico(event, treeId, treeNode) {
        var stringa = Ztree.creaStringaGerarchica(treeNode, 'Nessun Quadro Economico selezionato', true);
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
     * Inizializzazione dello ZTree del QuadroEconomico
     * @param treeId   (string) l'id dell'elemento che conterra' l'albero
     * @param uidField (string) il selettore del campo che contiene l'uid dell'albero
     * @returns (number) l'id dell'istanza
     */
    function initQuadroEconomicoZtree(treeId, uidField) {
        var paramTreeId = treeId || 'quadroEconomicoTree';
        var paramUidField = uidField || '#HIDDEN_quadroEconomicoUid';
        var tree = new QuadroEconomicoZtreeHandler(paramTreeId, paramUidField);
        
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
     * Chiusura del collapse del Quadro Economico
     */
    function closeQuadroEconomicoCollapse() {
        var collapseToggle = $('#quadroEconomicoParent').find('[data-toggle="collapse"]');
        var collapse = $(collapseToggle.attr('href'));
        if(collapse.hasClass('in')) {
            collapseToggle.click();
        }
    }
}(jQuery, this);