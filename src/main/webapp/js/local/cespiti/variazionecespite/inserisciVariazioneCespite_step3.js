/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    var intestazioneInserimento = $('#HIDDEN_intestazioneInserimento').val();
    var intestazioneAggiornamento = $('#HIDDEN_intestazioneAggiornamento').val();
    var baseUrl = $('#HIDDEN_baseUrl').val();
    var isIncremento = $('#HIDDEN_flagTipoVariazioneIncremento').val() === 'true';

    var $body = $(document.body);
    var $alertErrori = $('#ERRORI');
    var $alertInformazioni = $('#INFORMAZIONI');
    var $divDatiVariazione = $('#divDatiVariazione');
    var $intestazioneDatiVariazione = $('#intestazioneDatiVariazione');
    var $pulsanteSalvataggioVariazione = $('#pulsanteSalvaVariazione');
    var $fieldsetDatiVariazione = $('#fieldsetDatiVariazione');
    var $modaleEliminazione = $('#modaleEliminazione');
    var $modaleEliminazionePulsanteSalvataggio = $('#modaleEliminazionePulsanteSalvataggio');
    var $valoreAttualeCespite = $('#valoreAttualeCespite');
    var $nuovoValoreAttualeVariazioneCespite = $('#nuovoValoreAttualeVariazioneCespite');
    var $importoVariazioneCespite = $('#importoVariazioneCespite');
    
    var datatable;
    
    $(init);
    
    function impostaTabella() {
        var tabella = $('#tabellaVariazioneCespite').overlay('show');
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'risultatiRicercaVariazioneCespiteAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            iDisplayStart: 0,
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
                $('#tabellaVariazioneCespite_processing').parent('div').hide();
                tabella.find('a[rel="popover"]').popover();
                tabella.overlay('hide');
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('anno')},
                {aTargets: [1], mData: defaultPerDataTable('dataInserimento')},
                {aTargets: [2], mData: defaultPerDataTable('tipoVariazione')},
                {aTargets: [3], mData: defaultPerDataTable('descrizione')},
                {aTargets: [4], mData: defaultPerDataTable('importo')},
                {aTargets: [5], mData: defaultPerDataTable('stato')},
                {aTargets: [6], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).find('a[data-elimina-variazione-cespite]').substituteHandler('click', startEliminaVariazioneCespite.bind(undefined, oData.uid));
                    $(nTd).find('a[data-aggiorna-variazione-cespite]').substituteHandler('click', startAggiornaVariazioneCespite.bind(undefined, oData.uid));
                    // Non permettere la consultazione
                    $(nTd).find('a[data-consulta-variazione-cespite]').remove();
                }}
            ]
        };
        datatable = tabella.dataTable(opts);
    }
    
    /**
     * Inizializzazione dell'inserimento
     */
    function inizializzaInserimentoVariazione() {
        $divDatiVariazione.slideUp();
        $intestazioneDatiVariazione.html(intestazioneInserimento);
        $fieldsetDatiVariazione.find(':input').not('[disabled], [readonly]').val('');
        $pulsanteSalvataggioVariazione.substituteHandler('click', salvaVariazione.bind(undefined, 'inserisciVariazione.do'));
        
        $importoVariazioneCespite.data('originalImporto', 0);
        $nuovoValoreAttualeVariazioneCespite.val($valoreAttualeCespite.html());
        
        $divDatiVariazione.slideDown();
    }
    
    function salvaVariazione(urlPart) {
        var obj = $fieldsetDatiVariazione.serializeObject();
        $divDatiVariazione.overlay('show');
        $.postJSON(baseUrl + '_' + urlPart, obj)
        .then(callbackVariazione)
        .then($divDatiVariazione.slideUp.bind($divDatiVariazione))
        .always($divDatiVariazione.overlay.bind($divDatiVariazione, 'hide'));
    }
    
    function startAggiornaVariazioneCespite(uid, e) {
        e.preventDefault();
        $body.overlay('show');
        $divDatiVariazione.slideUp();
        $.postJSON(baseUrl + '_caricaVariazione.do', {'variazioneCespite.uid': uid})
        .then(function(data) {
            var valoreAttuale;
            if(impostaDatiNegliAlert(data.errori, $alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $fieldsetDatiVariazione.find(':input')
                .not('[disabled]')
                .each(function(idx, el) {
                    var $el = $(el);
                    var name = $el.attr('name') || '';
                    var key = name === '' ? '' : name.split('.').slice(1).join('.');
                    var value = data.variazioneCespite[key];
                    if($el.data('importo') !== undefined) {
                        value = (value || 0).formatMoney();
                    } else if ($el.data('date') !== undefined) {
                        value = formatDate(value || '');
                    }
                    el.value = value || '';
                });
            // Salvataggio dei valori per calcolo automatico
            $importoVariazioneCespite.data('originalImporto', data.variazioneCespite.importo);
            $nuovoValoreAttualeVariazioneCespite.val($valoreAttualeCespite.html());
            
            $intestazioneDatiVariazione.html(intestazioneAggiornamento);
            $pulsanteSalvataggioVariazione.substituteHandler('click', salvaVariazione.bind(undefined, 'aggiornaVariazione.do'));
            $divDatiVariazione.slideDown();
        })
        .always($body.overlay.bind($body, 'hide'));
    }
    function startEliminaVariazioneCespite(uid, e) {
        e.preventDefault();
        $modaleEliminazione.modal('show');
        $modaleEliminazionePulsanteSalvataggio.substituteHandler('click', eliminaVariazioneCespite.bind(undefined, uid));
    }
    function eliminaVariazioneCespite(uid) {
        $modaleEliminazione.modal('hide');
        $body.overlay('show');
        $.postJSON(baseUrl + '_eliminaVariazione.do', {'variazioneCespite.uid': uid})
        .then(callbackVariazione)
        .always($body.overlay.bind($body, 'hide'));
    }
    function callbackVariazione(data) {
        if(impostaDatiNegliAlert(data.errori, $alertErrori)) {
            return $.Deferred().reject().promise();
        }
        if(data.variazioneCespite && data.variazioneCespite.cespite && data.variazioneCespite.cespite.valoreAttuale != undefined) {
            $valoreAttualeCespite.html(data.variazioneCespite.cespite.valoreAttuale.formatMoney());
        }
        impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
        datatable.fnDraw();
    }
    
    function recomputeNuovoValoreAttualeVariazioneCespite() {
        var newImporto = +parseLocalNum($importoVariazioneCespite.val());
        var oldImporto = +$importoVariazioneCespite.data('originalImporto');
        var diff = isIncremento ? (newImporto - oldImporto) : (oldImporto - newImporto);
        var oldValore = +parseLocalNum($valoreAttualeCespite.html());
        // Verificare il segno
        var newValore = oldValore + diff;
        $nuovoValoreAttualeVariazioneCespite.val(formatMoney(newValore));
    }
    /**
     * Initialization
     */
    function init() {
        impostaTabella();
        $('#nuovaVariazione').substituteHandler('click', inizializzaInserimentoVariazione);
        $importoVariazioneCespite.change(recomputeNuovoValoreAttualeVariazioneCespite);
    }
}(jQuery);