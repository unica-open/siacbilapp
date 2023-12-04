/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    'use strict';
    var spanExtractors = {};

    spanExtractors.extractorBase = extractorBase;
    spanExtractors.extractorInvioFirma = extractorInvioFirma;

    /**
     * Estrattore base per l'allegato.
     *
     * @param wrapper (any) l'allegato da cui estrarre i dati
     * @returns (string) i dati di base per l'allegato
     */
    function extractorBase(wrapper) {
        return wrapper.stringaAttoAmministrativo;
    }

    /**
     * Estrattore per l'invio in firma dell'allegato.
     *
     * @param wrapper (any) l'allegato da cui estrarre i dati
     * @returns (string) i dati di invio firma per l'allegato
     */
    function extractorInvioFirma(wrapper) {
        var versioneInvioFirma = wrapper.versioneInvioFirma + 1;
        return wrapper.stringaAttoAmministrativo + ' in versione ' + versioneInvioFirma;
    }

    /**
     * Popola la tabella.
     */
    function gestioneTabella() {
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaAllegatoAttoAjax.do',
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
                $('#tabellaRisultatiRicercaAllegatoAtto_processing').parent('div')
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('stringaAttoAmministrativo')},
                {aTargets: [1], mData: defaultPerDataTable('stringaRitenute')},
                {aTargets: [2], mData: defaultPerDataTable('stringaSospensione')},
                {aTargets: [3], mData: defaultPerDataTable('stringaCausale')},
                {aTargets: [4], mData: defaultPerDataTable('stringaDataScadenza')},
                {aTargets: [5], mData: defaultPerDataTable('stringaDomRichDurc')},
                {aTargets: [6], mData: defaultPerDataTable('stringaDataFineValiditaDurc')},
                {aTargets: [7], mData: defaultPerDataTable('stringaOrdinativi')},
                {aTargets: [8], mData: defaultPerDataTable('stringaStatoOperativo')},
                {aTargets: [9], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    var $nTd = $(nTd);
                    $nTd.find('a.annullaAllegatoAtto')
                        .substituteHandler('click', gestioneModale.bind(undefined, oData, '#buttonConfermaModaleAnnullamentoRisultatiRicerca', '#modaleAnnullamentoRisultatiRicerca', undefined));
                    $nTd.find('a.completaAllegatoAtto')
                        .substituteHandler('click', controlloImporti.bind(undefined, oData, nTd));
                    //SIAC-6775
                    $nTd.find('a.completaAllegatoAttoByPassVerifiche')
                    	.substituteHandler('click', gestioneModale.bind(undefined, oData, '#buttonConfermaModaleCompletamentoRisultatiRicerca', '#modaleCompletamentoRisultatiRicerca', undefined));
                    $nTd.find('a.inviaAllegatoAtto')
                        .substituteHandler('click', gestioneModale.bind(undefined, oData, '#buttonConfermaModaleInvioRisultatiRicerca', '#modaleInvioRisultatiRicerca', 'extractorInvioFirma'));
                }}
            ]
        };
        $('#tabellaRisultatiRicercaAllegatoAtto').dataTable(opts);
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
            $('#hiddenUidAllegatoAtto').val(allegato.uid);
            $('#formRisultatiRicercaAllegatoAtto').attr('action', href).submit();
            pulsante.attr('disabled', 'disabled');
        });
        
        if (modale.has('#descrAllegatoAtto').length) {
        	$('#descrAllegatoAtto', modale).val(str);
        }
        
        modale.modal('show');
    }

    
    function controlloImporti(allegato, nTd){
    	var $nTrow = $(nTd).closest('row');
    	if(!allegato){
    		return;
    	}
    	$nTrow.overlay('show');
    	return $.postJSON('risultatiRicercaAllegatoAtto_controlloImportiImpegniVincolati.do', qualify({uidAllegatoAtto: allegato.uid}))
         .then(controlloImportiCallback.bind(undefined,allegato))
         //.always($nTrow.overlay.bind($nTrow, 'hide'));
    }
    
    /**
     * Callback di sospensione di tutti i dati
     * @param modal (jQuery) la modale
     * @param data  (any)    i dati del servizio
     */
    function controlloImportiCallback(allegato, data) {
        
    	if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
            return $.Deferred().reject().promise();
        }
       	gestioneModale(allegato, '#buttonConfermaModaleCompletamentoRisultatiRicerca', '#modaleCompletamentoRisultatiRicerca',undefined, data.messaggi);
    }
    
    
    
    
    
    
    
    
    
    
    $(function() {
        gestioneTabella();
    });
}(jQuery);