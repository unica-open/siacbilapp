/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    "use strict";
    var exports = {};
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 3,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: false,
        bDestroy: true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec."
            }
        }
    };

    exports.activateDataTableOnTable = activateDataTableOnTable;
    
    /**
     * Attivazione del dataTable sulla tabella.
     *
     * @param tableSelector (String) il selettore css della tabella
     * @param extraOpts     (Object) le opzioni aggiuntive (opzionale, default = {})
     */
    function activateDataTableOnTable(tableSelector, extraOpts) {
        var opts = $.extend(true, {}, baseOpts, extraOpts || {});
        var popoverOpts = {                
                title: "Descrizione Esito",
                trigger: "hover"
            };
        $('a[rel="popover"]', 'td ' + tableSelector).eventPreventDefault('click')
        .popover(popoverOpts);
        $(tableSelector).dataTable(opts);
    }

    global.RegistroComunicazioniPCC = exports;
}(this, jQuery);