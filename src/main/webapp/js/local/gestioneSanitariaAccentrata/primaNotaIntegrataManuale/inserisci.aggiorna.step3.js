/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var baseUrl = $("#HIDDEN_baseUrl").val();

    /**
     * Popolamento della tabella.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     * @param importoDaReg (Numeric) l'importo da registrare se i conti sono a zero (optionale default = 0)
     */
    function popolaTabella(list) {
        var tableId = "tabellaScritture";


        var opts = {
            bServerSide: false,
            aaData: list,
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
                sZeroRecords: "Nessun conto associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('movimentoDettaglio.conto.codice')},
                {aTargets: [1], mData: defaultPerDataTable('movimentoDettaglio.conto.descrizione')},
                {aTargets: [2], mData: defaultPerDataTable("domStringMissione")},
                {aTargets: [3], mData: defaultPerDataTable("domStringProgramma")},
                {aTargets: [4], mData: defaultPerDataTable("dare"), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable("avere"), fnCreatedCell: tabRight}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }
    
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
    }

    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaelencoScrittture() {
        $.postJSON(baseUrl + "_ottieniListaConti.do", {}, function(data) {
            var listaMovimentiPerTabella;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                return;
            }

            if (data.isContiCausale) {
                listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
                popolaTabella(listaMovimentiPerTabella);
                $("#totaleDare").html(data.totaleDare);
                $("#totaleAvere").html(data.totaleAvere);

            }
        });
    }



    $(function () {

    	GSAClassifZtree.initClassificatoreGSAZtree();
    });
    }(jQuery);