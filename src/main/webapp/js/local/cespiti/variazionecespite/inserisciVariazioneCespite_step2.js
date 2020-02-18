/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    
    $(init);
    
    function impostaTabella() {
        var tabella = $('#tabellaCespiti').overlay('show');
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'risultatiRicercaCespiteAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            iDisplayStart: $('#HIDDEN_savedDisplayStart').val() || 0,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnPreDrawCallback: defaultPreDraw,
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                // Nascondo il div del processing
                $('#tabellaRisultatiRicercaCespite_processing').parent('div').hide();
                tabella.find('a[rel="popover"]').popover();
                tabella.overlay('hide');
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('codice')},
                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
                {aTargets: [2], mData: defaultPerDataTable('tipoBene')},
                {aTargets: [3], mData: defaultPerDataTable('classificazione')},
                {aTargets: [4], mData: defaultPerDataTable('inventario')},
                {aTargets: [5], mData: function() {return ''}},
                {aTargets: [6], mData: createSelezionaButton}
            ]
        };
        tabella.dataTable(opts);
    }
    
    /**
     * Creazione del pulsante di selezione
     * @param cespite il cespite
     * @returns il pulsante di selezione
     */
    function createSelezionaButton(cespite) {
        return '<button type="button" class="btn btn-primary" data-seleziona="' + cespite.uid + '">Seleziona</button>';
    }
    
    function handleSelezioneCespite(e) {
        var $this = $(this);
        var uid = $this.data('seleziona');
        $('form')
            .append('<input type="hidden" name="cespite.uid" value="' + uid + '" />')
            .submit();
    } 
    /**
     * Initialization
     */
    function init() {
        $('#tabellaCespiti').substituteHandler('click', 'button[data-seleziona]', handleSelezioneCespite);
        impostaTabella();
    }
}(jQuery);