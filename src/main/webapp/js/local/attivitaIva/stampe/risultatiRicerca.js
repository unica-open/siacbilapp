/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var isRegistroIvaVisibile = $("#HIDDEN_registroIvaVisibile").val() === "true";
    var isPeriodoVisibile = $("#HIDDEN_periodoVisibile").val() === "true";

    /**
     * Popola la tabella.
     */
    function gestioneTabella() {
        var addRegistroIva = +isRegistroIvaVisibile;
        var addPeriodo = +isPeriodoVisibile;
        var dataTable;
        var tableId = "#tabellaStampaIva";

        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaStampaIvaAjax.do",
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
            fnPreDrawCallback: defaultPreDraw,
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
                {aTargets: [0], mData: "annoEsercizio"},
                {aTargets: [1], mData: "gruppoAttivitaIva"},
                {aTargets: [2 + addRegistroIva + addPeriodo], mData: "nomeFile"},
                {aTargets: [3 + addRegistroIva + addPeriodo], mData: "dataCreazione"},
                {aTargets: [4 + addRegistroIva + addPeriodo], mData: "azioni"}
            ]
        };
        if(isRegistroIvaVisibile) {
            // Aggiungo le colonne del registro
            opts.aoColumnDefs.push({aTargets: [2], mData: "registroIva"});
        }
        if(isPeriodoVisibile) {
            // Aggiungo le colonne del periodo
            opts.aoColumnDefs.push({aTargets: [2 + addRegistroIva], mData:"periodoTipoStampa"});
        }
        dataTable = $(tableId).dataTable(opts);
    }

    $(function() {
        gestioneTabella();
    });
}(jQuery);