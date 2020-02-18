/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    /**
     * Caricamento via Ajax della tabella delle richieste e visualizzazione.
     */
    function visualizzaTabelleRicheste() {
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaRegistrazioneMassivaMovFinGSAAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                var testoConferma = (records === 0 || records > 1) ? (records + " elementi") : ("1 elemento");
                $("#spanDatiModaleConfermaRegistrazioneMassiva").html(testoConferma);
                $('#id_num_result').html(testo);
            },
            'aoColumnDefs' : [
                {aTargets: [0], mData: 'stringaMovimento'},
                {aTargets: [1], mData: "stringaEvento"},
                {aTargets: [2], mData: 'stringaStatoOperativoRegistrazioneMovFin'},
                {aTargets: [3], mData: 'stringaDataRegistrazione'}
            ]
        };

        var startPos = parseInt($('#HIDDEN_startPosition').val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {'iDisplayStart' : startPos});
        }

        $('#risultatiRicercaRegistrazioniMovfin').dataTable(options);
    }

    /**
     * Registrazione massiva della prima nota.
     */
    function effettuaRegistrazioneMassiva(){
        $("#SPINNER_confermaModaleConfermaRegistrazioneMassiva").addClass("activated");
        document.location = "risultatiRicercaRegistrazioneMassivaMovFinGSA_registraTutto.do";
    }

    $(function() {
       visualizzaTabelleRicheste();
       $("#confermaModaleConfermaRegistrazioneMassiva").substituteHandler("click", effettuaRegistrazioneMassiva);
    });

}(jQuery);