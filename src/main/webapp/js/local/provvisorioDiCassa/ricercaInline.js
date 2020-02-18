/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    'use strict';
    // Progressivo
    var idx = 0;
    var instances = {};
    var alertErrori = $('#ERRORI');
    // Funzionalita' da pubblicare
    var exports = {};
    
    //SIAC-7116
    //array di appoggio per i provvedimenti in stato :checked
    var provvSelect = [];
    
    
    /**
     * Crea l'oggetto per la gestione del Provvisorio di cassa.
     */
    var ProvvisorioDiCassa = function() {    	
        this.$pulsanteRicercaProvCassa = $('#ricInline_pulsanteRicercaProvvisorioCassa');
        this.$selezionaTuttiProvCassa = $('#ricInline_selezionaTuttiProvCassa');        
        this.$tabella = $('#ricInline_tabellaProvvisorioCassa');
        this.$divRicerca = $('#ricInline_campiProvvisorio');
        this.$divElenco = $('#ricInline_divElencoProvvisorioCassa');        
        this.selectedDatas = {};
    };

    /** Costruttore */
    ProvvisorioDiCassa.prototype.constructor = ProvvisorioDiCassa;

    /**
     * Inizializzazione delle funzionalit&agrave;.
     */
    ProvvisorioDiCassa.prototype.inizializza = function() {
        this.$pulsanteRicercaProvCassa.substituteHandler('click', this.ricercaProvvisorio.bind(this));
        // Selezione di tutti gli elenchi
        this.$selezionaTuttiProvCassa.substituteHandler("change", this.selectAll.bind(this));
    };

    /**
     * Distruzione della funzionalita'
     */
    ProvvisorioDiCassa.prototype.destroy = function() {
        this.$pulsanteRicercaProvCassa.off('click');
    };

    /**
     * Ricerca del provvisorio di cassa.
     */
    ProvvisorioDiCassa.prototype.ricercaProvvisorio = function() {
        var self = this;
        var obj = unqualify(this.$divRicerca.serializeObject(), 1);
        var spinner = this.$pulsanteRicercaProvCassa.find('i');
        this.$divElenco.slideUp();
        this.selectedDatas = {};
        // Attivo lo spinner sul pulsante
        spinner.toggleClass('icon-search icon-spin icon-refresh spinner activated');
        $.postJSON('ricercaProvvisorioCassa.do', obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            alertErrori.slideUp();
            self.popolaTabella();
            // Apro il div degli elenchi
            self.$divElenco.slideDown();
        }).always(spinner.toggleClass.bind(spinner, 'icon-search icon-spin icon-refresh spinner activated'));
    };
    
    /**
     * Popolamento della tabella del provvisorio.
     */
    ProvvisorioDiCassa.prototype.popolaTabella = function() {
    	var table = this.$tabella;
        var id = table.attr('id');
        var self = this;
        var opts = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaProvvisorioCassaAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            bProcessing: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti provvisori di cassa corrispondenti ai parametri inseriti',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $('#' + id + '_processing').show();
            },
            fnDrawCallback: function () {
                var checkboxes = table.find('tbody input[type="checkbox"]');
                var checkedCheckboxes = checkboxes.filter(':checked');
                // Attivo i popover
                table.find("a[rel='popover']")
                    .popover()
                    .eventPreventDefault("click");
                $('#' + id + '_processing').hide();
                self.$selezionaTuttiProvCassa[checkboxes.length === checkedCheckboxes.length ? 'prop' : 'removeProp']('checked', true);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(src) {
                	return '<input type="checkbox" value="' + src.uid + '" name="ricercaInline.provvisorioDiCassa.uid" data-checkbox-provvisorio-cassa '
                		+ (self.selectedDatas[+src.uid] && self.selectedDatas[+src.uid].isSelected ? 'checked' : '') + ' />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd)
                    .data('originalProvvisorioDiCassa', oData)
                	.change(function() {
                		var $this = $(this);
                        self.selectedDatas[+oData.uid] = {isSelected: $this.is(':checked'), data: oData};
                        //SIAC-7116
                        //rimuovo un elemento dalla mappa se presente in caso la selezione sia false
                        if(self.selectedDatas[oData.uid].isSelected === false){
                            delete self.selectedDatas[oData.uid];
                        }
                        //controllo che al termine della selezione tutti i checkbox siano flaggati
                        //altrimenti rimuovo il flag dal checkbox 'seleziona tutti' e lo rendo di nuovo disponibile
                        controlloSelezionaTutto();
                	});
                }},
                
                {aTargets: [1], mData: defaultPerDataTable('anno')},
                {aTargets: [2], mData: defaultPerDataTable('numero')},
                {aTargets: [3], mData: defaultPerDataTable('dataEmissione', '', formatDate)},
                {aTargets: [4], mData: function(source) {
                    var result = '';
                    if(source.causale) {
                        result = '<a href="#" rel="popover" data-trigger="hover" data-original-title="SubCausale" data-content="' + escapeHtml(source.subCausale) + '">' + source.causale + '</a>';
                    }
                    return result;
                }},
                {aTargets: [5], mData: defaultPerDataTable('denominazioneSoggetto')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }},
                {aTargets: [7], mData: defaultPerDataTable('importoDaEmettere', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }}
            ]
        };
        // Distruggo la vecchia tabella se presente
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            // Sembra che non ricarichi correttamente, senza questa istruzione
            table.dataTable().fnDestroy();
        }
        table.dataTable(opts);
    };

    /**
     * Selezione di tutti i checkbox
     */
    ProvvisorioDiCassa.prototype.selectAll = function(e) {
    	var self = this;
        var $this = $(e.target);
        var table = $this.closest("table");
        var checkboxes = table.find("tbody").find("input[type='checkbox']");
        var toCheck = $this.prop("checked");

        checkboxes.prop("checked", toCheck).each(function(idx, el) {
            self.selectedDatas[+el.value] = {isSelected: toCheck, data: $(el).data('originalProvvisorioDiCassa')};
            //SIAC-7116
            //rimuovo un elemento dalla mappa in caso la selezione sia false
            if(self.selectedDatas[el.value].isSelected === false){
                delete self.selectedDatas[el.value];
            }
        });
        //controllo che al termine della selezione tutti i checkbox siano flaggati
        //altrimenti rimuovo il flag dal checkbox 'seleziona tutti' e lo rendo di nuovo disponibile
        controlloSelezionaTutto();
        //ricalcolaTotali();
    }

    //SIAC-7116
    function controlloSelezionaTutto(){
        var checkboxes = $('tbody').find('input[type="checkbox"]');
        var checkedCheckboxes = checkboxes.filter(':checked');
        var selectAllButt = $('#ricInline_selezionaTuttiProvCassa');
        selectAllButt[checkboxes.length === checkedCheckboxes.length ? 'prop' : 'removeProp']('checked', true);
    }

//    /**
//     * Ricalcolo dei totali.
//     */
//    function ricalcolaTotali() {
//        var totale = 0;
//        var checkedNum = 0;
//        var i;
//        var data;
//        for(i in selectedDatas) {
//            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
//                checkedNum++;
//                data = selectedDatas[i].data;
//                if(data) {
//                    totale += data.domStringImporto;
//                }
//            }
//        }
//        $("#totaleQuoteSelezionate").html(totale.formatMoney());
//        $("#pulsanteEmissione")[checkedNum ? "show" : "hide"]();
//        $('#pulsanteControlloDisponibilitaCassa')[checkedNum ? "show" : "hide"]();
//        $("#confermaEmissioneNumeroSpan").html(checkedNum + (checkedNum === 1 ? " ordinativo" : " ordinativi"));
//        $("#confermaEmissioneImportoSpan").html(totale.formatMoney());
//    }
    
//    /**
//     * Formattazione dell'importo
//     * @param value (number) il valore da formattare
//     * @return (string) l'importo formattato
//     */
//    function formatMoney(value) {
//        if(typeof value === 'number') {
//            return value.formatMoney();
//        }
//        return '';
//    }

    /**
     * Inizalizzazione della gestione.
     * @return (number) l'id dell'elemento
     */
    exports.inizializzazione = function() {
        var pdc = new ProvvisorioDiCassa();
        var index = idx++;
        pdc.inizializza();
        instances[index] = pdc;
        return {idx: index, instance: pdc};
    };
    
    /**
     * Distrugge il gestore per id
     * @param index (number) l'indice del gestore
     */
    exports.destroy = function(index) {
        var obj;
        if(!instances[index]) {
            return;
        }
        obj = instances[index];
        instances[index] = undefined;
        obj.destroy();
        obj = undefined;
    };

    // Esporto le funzionalita' sulla variabile globale
    global.ProvvisorioDiCassaInline = exports;
}(jQuery, this);