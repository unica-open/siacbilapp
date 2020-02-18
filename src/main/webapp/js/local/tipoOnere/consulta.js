/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    /**
     * Imposta e inizializza il dataTable.
     *
     * @param tableSelector (String) il selettore CSS della tabella
     * @param moreOpts      (Object) ulteriori opzioni (Optional, default: undefined)
     */
    function impostaDataTable(tableSelector, moreOpts) {
        var opts = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnDrawCallback: function() {
                $(tableSelector + "_processing").parent()
                    .addClass("hide");
            }
        };
        var options = $.extend(true, {}, opts, moreOpts || {});
        $(tableSelector).dataTable(options);
    }

    $(function() {
        // Inizializzazione delle tabelle
        impostaDataTable("#tabellaTipoOnere", {oLanguage: {sZeroRecords: "Non sono presenti tipi onere"}});
        impostaDataTable("#tabellaCausaleEntrata", {oLanguage: {sZeroRecords: "Non sono presenti accertamenti collegati"}});
        impostaDataTable("#tabellaCausaleSpesa", {oLanguage: {sZeroRecords: "Non sono presenti impegni collegati"}});
        impostaDataTable("#tabellaSoggetto", {oLanguage: {sZeroRecords: "Non sono presenti soggetti collegati"}});
    });
}(jQuery);