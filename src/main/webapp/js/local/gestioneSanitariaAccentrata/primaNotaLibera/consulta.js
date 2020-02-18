/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";

    /**
     * Impostazione della tabella.
     */
    function attivaDataTable() {
        var tableId = "#tabellaScritture";
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
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
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        $(tableId).dataTable(opts);
    }
    
    /**
     * Impostazione della tabella.
     */
    function attivaDataTablePrimeNoteCollegate() {
        var tableId = "#tabellaPrimeNoteCollegate";
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti prime note collegate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        $(tableId).dataTable(opts);
    }

    $(function() {
        // Inizializzazione del datatable
        attivaDataTable();
        attivaDataTablePrimeNoteCollegate();
    });
}(jQuery, this);