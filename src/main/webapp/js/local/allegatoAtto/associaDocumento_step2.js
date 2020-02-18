/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";
    var disabilitataGestioneResidui = $('#HIDDEN_gestioneResiduiDisabilitata').length > 0;
    var selectedDatas = {};
    window.selectedDatas = selectedDatas;

    /**
     * Popola la tabella.
     *
     * @param url               (String)  l'URL di invocazione
     * @param tableSelector     (String)  il selettore CSS della tabella
     */
    function populateTable(url) {
        var zeroRecords = "Non sono presenti quote corrispondenti ai parametri inseriti";
        var table = $("#tabellaSubdocumenti");
        var opts = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaQuoteDaAssociareAllegatoAttoAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 50,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            sScrollY: "300px",
            bScrollCollapse: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: zeroRecords,
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            // SIAC-6232: aggiunto l'overlay sulla tabella
            fnPreDrawCallback: preDraw,
            fnDrawCallback: drawCallback,
            aoColumnDefs: [
                {aTargets: [0], mData: computaCheckbox, fnCreatedCell: function(nTd) {
                    $('input', nTd).substituteHandler('change', updateSelectedData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('domStringDocumento')},
                {aTargets: [2], mData: defaultPerDataTable('dataEmissione', '', formatDate)},
                {aTargets: [3], mData: defaultPerDataTable('domStringStato')},
                {aTargets: [4], mData: defaultPerDataTable('domStringSoggetto')},
                {aTargets: [5], mData: defaultPerDataTable('numeroQuota')},
                {aTargets: [6], mData: defaultPerDataTable('domStringMovimento')},
                {aTargets: [7], mData: defaultPerDataTable('domStringIva')},
                {aTargets: [8], mData: defaultPerDataTable('domStringAnnotazioni')},
                {aTargets: [9], mData: defaultPerDataTable('importoQuota', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        table.dataTable(opts);
    }
    
    /**
     * Funzione di pre-disegno
     * @param opts (any) le opzioni
     */
    function preDraw(opts) {
        $('#' + opts.nTable.id + '_processing').parent('div').show();
        $('#' + opts.nTable.id + '_wrapper').overlay('show');
    }
    
    /**
     * Calcolo del checkbox per la tabella
     * @param source (any) la sorgente dei dati
     * @return (string) l'html da injettare
     */
    function computaCheckbox(source) {
        var strDisabilita = disabilitataGestioneResidui && source.associatoAMovgestResiduo? "disabled" : '';
        return '<input type="checkbox" name="listaUidSubdocumenti" ' + strDisabilita + ' data-famiglia="' + source.famiglia + '" data-importo="'
            + source.importoQuota + '" value="' + source.uid + '"/>';
    }

    /**
     * Aggiornamento dei dati selezionati
     */
    function updateSelectedData() {
        var $this = $(this);
        var selected = $this.prop('checked');
        selectedDatas[+$this.val()] = {isSelected: selected, famiglia: $this.data('famiglia'), importo: +$this.data('importo')};
        ricalcolaTotali();
    }
    
    /**
     * Callback al disegno della tabella
     * @param options (any) le opzioni
     */
    function drawCallback(options) {
        var unselectedCheckboxes = $(options.nTBody).find('input[type="checkbox"]').not(':checked');
        $(".check-all")[unselectedCheckboxes.length ? 'removeProp' : 'prop']('checked', true);
        // Attivo i popover
        $("a[rel='popover']", options.nTBody).eventPreventDefault("click") .popover();
        $("#tabellaSubdocumenti_processing").parent('div').hide();
        $('#' + options.nTable.id + '_wrapper').overlay('hide');
    }

    /**
     * Formattazione della valuta
     * @param val (any) il valore da formattare
     * @returns (string|any) il valore formattato
     */
    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return val;
    }
    /**
     * Seleziona tutti i checkbox nella tabella contente il pulsante chiamante.
     *
     * @returns i checkbox su cui si &eacute; agito.
     */
    function checkAllInTable() {
        var $this = $(this);
        var $checkboxes = $('#tabellaSubdocumenti').find('tbody').find('input[type="checkbox"]');
        var toCheck = $this.prop('checked');
        
        $checkboxes.prop("checked", toCheck).each(function(idx, el) {
            var $el = $(el);
            selectedDatas[+el.value] = {isSelected: toCheck, famiglia: $el.data('famiglia'), importo: +$el.data('importo')};
        });
        ricalcolaTotali();
    }

    /**
     * Ricalcolo dei totali.
     */
    function ricalcolaTotali() {
        var totaleSpesa = 0;
        var totaleEntrata = 0;
        var i;
        var importo;
        var famiglia;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                importo = selectedDatas[i].importo || 0;
                famiglia = selectedDatas[i].famiglia;
                if(famiglia === 'S') {
                    totaleSpesa += importo;
                } else {
                    totaleEntrata += importo;
                }
            }
        }
        $("#spanTotaleSubdocumentiSpesa").html(totaleSpesa.formatMoney());
        $("#spanTotaleSubdocumentiEntrata").html(totaleEntrata.formatMoney());
    }
    
    /**
     * Gestione del submit del form
     */
    function gestisciSubmitForm() {
        var i;
        var str;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                str += '<input type="hidden" name="mappaUidSubdocumenti[' + i + ']" value="' + selectedDatas[i].famiglia + '" />';
            }
        }
        $('form').append(str);
    }

    $(function() {
        // Creo la funzionalita' di selezione multipla
        $(".check-all").change(checkAllInTable);
        // Lego l'aggiornamento degli importi
        $(document).on("change", "tbody input[type='checkbox']", ricalcolaTotali);
        // Popolo le due tabelle
        populateTable();
        $('form').substituteHandler('submit', gestisciSubmitForm);
    });
}(jQuery));