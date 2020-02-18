/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");

    /**
     * Imposta e inizializza il dataTable.
     *
     * @param tableSelector (String) il selettore CSS della tabella
     * @param ajaxURL       (String) l'URL per l'invocazione dell'AJAX
     */
    function impostaDataTable(tableSelector, ajaxURL) {
        var options = {
            bServerSide: true,
            sAjaxSource: ajaxURL,
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $("#risultatiRicercaPreDocumento_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.tipoOnere && source.tipoOnere.codice || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.tipoOnere && source.tipoOnere.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    var natura = source.tipoOnere && source.tipoOnere.naturaOnere;
                    return natura && (natura.codice + " - " + natura.descrizione) || "";
                }},
                {aTargets: [3], mData: function(source) {
                    var res = "";
                    var tipoIvaSplitReverse = source.tipoOnere && source.tipoOnere.tipoIvaSplitReverse;
                    var natura = source.tipoOnere && source.tipoOnere.naturaOnere;
                    if(natura && natura.codice === 'SP' && tipoIvaSplitReverse) {
                        res = tipoIvaSplitReverse.codice + " - " + tipoIvaSplitReverse.descrizione;
                    }
                    return res;
                }},
                {aTargets: [4], mData: function(source) {
                    return source.tipoOnere && source.tipoOnere.dataCreazione && formatDate(source.tipoOnere.dataCreazione) || "";
                }},
                {aTargets: [5], mData: function(source) {
                    return source.tipoOnere && source.tipoOnere.dataFineValidita && formatDate(source.tipoOnere.dataFineValidita) || "";
                }},
                {aTargets: [6], mData: function(source) {
                    return source.azioni;
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        $(tableSelector).dataTable(options);
    }

    /**
     * Effettua una ricerca per il tipo onere.
     */
    function ricercaTipoOnere() {
        var div = $("#divElencoTipoOnere").slideUp();
        var fieldset = $("#fieldsetRicerca").overlay("show");
        var obj = fieldset.serializeObject();
        var spinner = $("#SPINNER_cercaTipoOnere").addClass("activated");
        var url = "effettuaRicercaTipoOnere.do";

        $.postJSON(url, obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Ho degli errori: esco subito
                return;
            }
            // Mostro il div
            div.slideDown();
            // Creo la dataTable
            impostaDataTable("#tabellaTipoOnere", "risultatiRicercaTipoOnereAjax.do");
        }).always(function() {
            fieldset.overlay("hide");
            spinner.removeClass("activated");
        });
    }

    /**
     * Chiusura del div della ricerca onere.
     */
    function chiudiRicercaTipoOnere() {
        // Chiudo il div
        $("#divElencoTipoOnere").slideUp();
    }

    $(function() {
        $("#cercaTipoOnere").substituteHandler("click", ricercaTipoOnere);
        $("form").on("reset", chiudiRicercaTipoOnere);
    });
}(jQuery);