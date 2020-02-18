/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    var GruppoAttivitaIva = (function() {
        // Per lo sviluppo
        "use strict";
        var alertErrori = $("#ERRORI");

        var exports = {};

        /**
         * Gestione del click sul pulsante elimina.
         *
         * @param gruppo (Object) il gruppo da annullare
         */
        function clickOnElimina(gruppo) {
            var modal = $("#modaleElimina");
            // Imposto il codice del gruppo
            $("#SPAN_elimina").html(gruppo.codice);
            $("#pulsanteNoElimina").substituteHandler("click", function() {
                // Chiudo il modale
                modal.modal("hide");
            });
            $("#pulsanteSiElimina").substituteHandler("click", function() {
                // Redirigo verso la action di eliminazione
                document.location = "risultatiRicercaGruppoAttivitaIva_elimina.do?uidDaEliminare=" + gruppo.uid;
            });
            // Apro il modale
            modal.modal("show");
        }

        /**
         * Gestione del click sul pulsante aggiorna.
         *
         * @param gruppo (Object) il gruppo da annullare
         */
        function clickOnAggiorna(gruppo) {
            // Redirigo verso la action di aggiornamento
            document.location = "risultatiRicercaGruppoAttivitaIva_aggiorna.do?uidDaAggiornare=" + gruppo.uid;
        }

        /**
         * Gestione del click sul pulsante consulta.
         *
         * @param td     (Node)   il campo su cui e' stata effettuata la selezione
         * @param gruppo (Object) il gruppo da annullare
         */
        function clickOnConsulta(td, gruppo) {
            var overlay = $(td).closest("tr").overlay("show");
            $.post("risultatiRicercaGruppoAttivitaIva_consulta.do", {"gruppoAttivitaIva.uid": gruppo.uid}, function(data) {
                if(typeof data === "object") {
                    // Ho l'errore. Lo loggo ed esco
                    impostaDatiNegliAlert(data.errori, alertErrori);
                    return;
                }
                // Ho il risultato. Lo imposto nel div
                $("#placeholderConsultazione").html(data);
                $("#modaleConsultazioneGruppoAttivitaIva").modal("show");
            }).always(function() {
                overlay.overlay("hide");
            });
        }

        function doFormatMoney(val) {
            if(typeof val === 'number') {
                return val.formatMoney();
            }
            return val;
        }
        /**
         * Caricamento via Ajax della tabella e visualizzazione della stessa.
         */
        exports.visualizzaTabella = function() {
            var options = {
                bServerSide: true,
                sAjaxSource: "risultatiRicercaGruppoAttivitaIvaAjax.do",
                sServerMethod: "POST",
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
                // Chiamata al termine della creazione della tabella
                fnDrawCallback: function () {
                    var records = this.fnSettings().fnRecordsTotal();
                    var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                    $('#id_num_result').html(testo);
                },
                aoColumnDefs: [
                    {aTargets: [0], mData: defaultPerDataTable('codice')},
                    {aTargets: [1], mData: defaultPerDataTable('descrizione')},
                    {aTargets: [2], mData: defaultPerDataTable('tipoChiusura._name')},
                    {aTargets: [3], mData: defaultPerDataTable('tipoAttivita._name')},
                    {aTargets: [4], mData: defaultPerDataTable('percentualeProRata', '', doFormatMoney), fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }},
                    {aTargets: [5], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                        $(nTd).find(".aggiornaGruppoAttivitaIva")
                                .substituteHandler("click", function(e) {
                                    e.preventDefault();
                                    clickOnAggiorna(oData);
                                })
                                .end()
                            .find(".consultaGruppoAttivitaIva")
                                .substituteHandler("click", function(e) {
                                    e.preventDefault();
                                    clickOnConsulta(nTd, oData);
                                })
                                .end()
                            .find(".eliminaGruppoAttivitaIva")
                                .substituteHandler("click", function(e) {
                                    e.preventDefault();
                                    clickOnElimina(oData);
                                });
                    }}
                ]
            };

            var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
            if(!isNaN(startPos)) {
                $.extend(true, options, {"iDisplayStart" : startPos});
            }

            $("#risultatiRicercaGruppoAttivitaIva").dataTable(options);
        };


        return exports;
    }());

    $(function() {
        GruppoAttivitaIva.visualizzaTabella();
    });
}(jQuery, window));