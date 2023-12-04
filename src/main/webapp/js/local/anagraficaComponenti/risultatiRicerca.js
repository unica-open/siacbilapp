/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    'use strict';
    var spanExtractors = {};
    
    spanExtractors.extractorBase = extractorBase;
    
    /**
     * Estrattore base per l'allegato.
     *
     * @param wrapper (any) l'allegato da cui estrarre i dati
     * @returns (string) i dati di base per l'allegato
     */
    function extractorBase(wrapper) {
        return  wrapper.stringaMacrotipo + ' ' +  wrapper.stringaSottotipo +' ' + wrapper.stringaDescrizione;
    }


    /**
     * Popola la tabella.
     */
    function gestioneTabella() {
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaComponenteCapitoloAjax.do',
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
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#tabellaRisultatiRicercaComponenteCapitolo_processing').parent('div')
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('stringaMacrotipo')},
                {aTargets: [1], mData: defaultPerDataTable('stringaSottotipo')},
                {aTargets: [2], mData: defaultPerDataTable('stringaDescrizione')},
                {aTargets: [3], mData: defaultPerDataTable('stringaStato')},
                {aTargets: [4], mData: defaultPerDataTable('stringaAmbito')},
                {aTargets: [5], mData: defaultPerDataTable('stringaFonte')},
                {aTargets: [6], mData: defaultPerDataTable('stringaMomento')},
                {aTargets: [7], mData: defaultPerDataTable('stringaAnno')},
                {aTargets: [8], mData: defaultPerDataTable('stringaImpegnabile')},
                {aTargets: [9], mData: defaultPerDataTable('stringaDefault')},
                {aTargets: [10], mData: defaultPerDataTable('stringaDataInizioValidita')},
                {aTargets: [11], mData: defaultPerDataTable('stringaDataFineValidita')},
                {aTargets: [12], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    var $nTd = $(nTd);
                    $nTd.find('a.annullaTipoComponenteImportiCapitolo')
                        .substituteHandler('click', gestioneModale.bind(undefined, oData, '#buttonConfermaModaleAnnullamentoRisultatiRicerca', '#modaleAnnullamentoRisultatiRicerca', undefined));
                }}
                
            ]
        };
        $('#tabellaRisultatiRicercaComponenteCapitolo').dataTable(opts);
    }

    /**
     * Gestione del dell'utilizzo dei modali.
     *
     * @param wrapper        (any)    il wrapper
     * @param buttonSelector (string) il selettore CSS del pulsante
     * @param modalSelector  (string) il selettore CSS del modale
     * @param extractorType  (string) il tipo di estrattore della stringa della descrizione (Default: extractorBase)
     * @param ev             (Event) l'evento
     */
    function gestioneModale(allegato, buttonSelector, modalSelector, extractorType, warnings, ev) {
        var pulsante = $(buttonSelector);
        var modale = $(modalSelector);
        var labelledby = modale.attr('aria-labelledby');
        var span = $('#' + labelledby).find('span');
        var str = spanExtractors[extractorType || 'extractorBase'](allegato);
        var alertMessaggiModale = modale.find('.alert-warning');
        
        alertMessaggiModale && warnings && impostaDatiNegliAlert(warnings, alertMessaggiModale)
        // Blocco l'evento
        ev && ev.preventDefault();
        // Popolo lo span
        span.html(str);
        pulsante.substituteHandler('click', function() {
            var href = pulsante.data('href');
            $('#hiddenUidComponenteCapitolo').val(allegato.uid);
            $('#formRisultatiRicercaComponenteCapitolo').attr('action', href).submit();
            pulsante.attr('disabled', 'disabled');
        });
        modale.modal('show');
    }
    
    $(function() {
        gestioneTabella();
    });
}(jQuery);