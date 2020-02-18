/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var tipoDocumentoRendiconto = $('#HIDDEN_tipoDocumentoRendiconto').val() === 'true';

    /**
     * Popola la tabella.
     */
    function gestioneTabella() {

        var dataTable;
        var tableId = "#tabellaStampeCassaEconomale";

        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaStampaCassaEconomaleAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                dataTable.$("a[rel='popover']").popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable("tipoStampa")},
                {aTargets: [1], mData: defaultPerDataTable("periodoDa")},
                {aTargets: [2], mData: defaultPerDataTable("periodoA")},
                {aTargets: [3], mData: defaultPerDataTable("nomeFile")},
                {aTargets: [4], mData: defaultPerDataTable("dataCreazione")},
                {aTargets: [5 + +tipoDocumentoRendiconto], mData: defaultPerDataTable("azioni")}
            ]
        };
        if(tipoDocumentoRendiconto) {
            opts.aoColumnDefs.push({
                aTargets:[5], mData: defaultPerDataTable('allegatoAtto')
            });
        }

        dataTable = $(tableId).dataTable(opts);
    }

    $(function() {
        gestioneTabella();
    });
}(jQuery);