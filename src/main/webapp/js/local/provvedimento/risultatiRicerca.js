/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var exports = global.Provvedimento || {};

    /**
     * Inizializzazione della tabella.
     */
    function inizializzaTabella() {
        var options = {
            bPaginate: true,
            bLengthChange: false,
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
            iDisplayLength: 10,
            aoColumnDefs: [
                {aTargets: [ 6 ], fnCreatedCell: function (nTd) {
                    $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };
        var posizioneStart = $("#HIDDEN_startPosition").val();
        var startPos = parseInt(posizioneStart, 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }
        $("#risultatiRicercaProvvedimento").dataTable(options);
    }

    /**
     * Imposta la pagina nel campo hidden per la comunicazione con Struts2.
     *
     * @param element l'elemento da considerare
     */
    exports.impostaPagina = function(element) {
        // Modificare se la lista sara' paginata
        var paginaScelta = (parseInt($(".dataTables_paginate .active a").text(), 10) - 1) * 10;
        var elementoJQuery = $(element);
        // Ottengo l'href dell'elemento, lo modifico e lo re-injetto
        var hrefElemento = elementoJQuery.attr("href");
        hrefElemento += "&iDisplayStart=" + paginaScelta;
        elementoJQuery.attr("href", hrefElemento);
        $("#HIDDEN_startPosition").val(paginaScelta);
    };

    global.Provvedimento = exports;

    $(function() {
        // Imposto le azioni sui campi ancora
        $("a", "#risultatiRicercaProvvedimento").on("click", function() {
            exports.impostaPagina(this);
        });

        // Inizializzazione del dataTable
        inizializzaTabella();
    });
}(jQuery, this);