/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    $(init);

    /**
     * Caricamento della tabella con i dati
     */
    function caricaTabella() {
        var tableId = '#risultatiRicerca';
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'risultatiRicercaRendicontoCassaDaStampareAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayStart: +$('#HIDDEN_startPage').val() || 0,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
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
                    sEmptyTable: 'Nessun rendiconto disponibile'
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + '_processing').parent('div')
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
            	$("a[rel='popover']", tableId).popover();
                // Nascondo il div del processing
                $(tableId + '_processing').parent('div')
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return '<input type="checkbox" disabled>';
                }},
                {aTargets: [1], mData: defaultPerDataTable('capitolo')},
                {aTargets: [2], mData: defaultPerDataTable('impegno')},
                {aTargets: [3], mData: defaultPerDataTable('numeroMovimentoConStato'), fnCreatedCell: textRight},
                {aTargets: [4], mData: defaultPerDataTable('tipo')},
                {aTargets: [5], mData: defaultPerDataTable('dataRegistrazione')},
                {aTargets: [6], mData: defaultPerDataTable('beneficiario')},
                {aTargets: [7], mData: defaultPerDataTable('oggetto')},
                {aTargets: [8], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: textRight}
            ]
        };
        $(tableId).dataTable(opts);
    }
    /**
     * Formattazione della valuta.
     * @param val (number) il valore da formattare
     * @returns (string) il valore formattato
     */
    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return '';
    }
    /**
     * Imposta la classe text-right
     * @param nTd (Node) il nodo
     */
    function textRight(nTd) {
        nTd.className += 'text-right';
    }
    /**
     * Apertura del modale di stampa
     */
    function apriModaleStampa() {
        $('#modaleConfermaStampaRendicontoCassa').modal('show');
    }
    /**
     * Effettua la stampa
     */
    function stampa() {
        $('form').submit();
    }
    /**
     * Inizializzazione
     */
    function init() {
        caricaTabella();
        $('#pulsanteStampa').substituteHandler('click', apriModaleStampa);
        $('#pulsanteConfermaStampaRendicontoCassa').substituteHandler('click', stampa);
    }

}(jQuery);