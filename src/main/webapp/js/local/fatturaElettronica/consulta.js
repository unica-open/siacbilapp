/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 3,
        bSort: false,
        bInfo: true,
        bAutoWidth: false,
        bFilter: false,
        bProcessing: false,
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
        }
    };

    /**
     * Attivazione del dataTable delle fatture collegate.
     */
    function attivaDataTableFattureCollegate() {
        var opts = $.extend(true, {}, baseOpts);
        opts.oLanguage.sZeroRecords = "Non sono presenti fatture collegate";
        $("#fattureCollegateTable").dataTable(opts);
    }

    /**
     * Attivazione del dataTable dei dati iva.
     */
    function attivaDataTableDatiIva() {
        var opts = $.extend(true, {}, baseOpts);
        opts.oLanguage.sZeroRecords = "Non sono presenti dati iva";
        $("#datiIvaTable").dataTable(opts);
    }
    
    /**
     * Attivazione del dataTable dei dati ritenute.
     */
    function attivaDataTableDatiRitenuta() {
        var opts = $.extend(true, {}, baseOpts);
        opts.oLanguage.sZeroRecords = "Non sono presenti dati ritenuta";
        $("#datiRitenutaTable").dataTable(opts);
    }
    
    

    $(function() {
        attivaDataTableFattureCollegate();
        attivaDataTableDatiIva();
        attivaDataTableDatiRitenuta();
    });
}(jQuery);