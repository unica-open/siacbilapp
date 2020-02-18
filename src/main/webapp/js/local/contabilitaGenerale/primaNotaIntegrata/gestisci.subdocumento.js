/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var alertErrori = $("#ERRORI");
    var alertErroriModale = $("#ERRORI_modale");
    var alertInformazioni = $("#INFORMAZIONI");
    var baseUrl = $("#HIDDEN_baseUrlSubdocumento").val();

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
                {aTargets: [0], mData: "codiceConto"},
                {aTargets: [1], mData: "descrizioneConto"},
                {aTargets: [2], mData: "dare", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [3], mData: "avere", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                //Azioni
                {aTargets: [4], mData: function(source) {
                    var temp = "";
                    var res = "";
                    if(!source.utilizzoImportoNonModificabile) {
                        // L'importo e' modificabile dall'operatore
                        temp += "<li><a class=\"aggiornaConto\">aggiorna</a></li>";
                    }
                    if(!source.utilizzoContoObbligatorio) {
                        // Posso eliminare il conto
                        temp += "<li><a class=\"eliminaConto\">elimina</a></li>";
                    }

                    if(temp) {
                        res = "<div class=\"btn-group\">" +
                                "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                                "<ul class=\"dropdown-menu pull-right\">" +
                                    temp +
                                "</ul>" +
                            "</div>";
                    }
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaConto")
                            .click([oData, iRow], aggiornaConto)
                            .end()
                        .find("a.eliminaConto")
                            .click([oData, iRow], eliminaConto);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Aggiornamento del conto.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaConto(e) {
        var elementoStruttura = e.data[0];
        var idx = e.data[1];
        var importo = elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.importo || 0;
        var dareAvere = elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name;

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Popolo i dati del conto
        $("input[type='radio'][data-" + dareAvere + "]").prop('checked', true);
        // Diabilito i radio se e'è da causale
        $("input[type='radio'][data-DARE], input[type='radio'][data-AVERE]").prop('disabled', true);

        $("#segnoConto").val(elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name);
        $("#importoModale").val(importo.formatMoney());

        // Imposto l'indice
        $("#indiceContoModale").val(idx);
        // Apro il modale
        alertErroriModale.slideUp();
        $("#modaleAggiornamentoConto").modal("show");
    }

    /**
     * Eliminazione del conto.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaConto(e) {
        var elementoStruttura = e.data[0];
        var idx = e.data[1];
        var str = "";

        if(elementoStruttura.movimentoDettalgio && elementoStruttura.movimentoDettaglio.conto && elementoStruttura.movimentoDettaglio.conto.descrizione) {
            str = " " + elementoStruttura.movimentoDettaglio.conto.descrizione;
        }
        $("#modaleEliminazioneElementoSelezionato").html(str);
        // Apro il modale
        $("#modaleEliminazione").modal("show");
        // Lego l'azione di cancellazione
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", function() {
            eliminazioneConto(idx);
        });
    }

    /**
     * Eliminazione del conto.
     *
     * @param index l'indice del conto da eliminare
     */
    function eliminazioneConto(index) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_eliminaConto.do", {indiceConto: index}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 $("#modaleEliminazione").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            aggiornaDatiScritture (data);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        });
    }

    /**
     * Aggiornamento dei dati delle scritture
     */
    function aggiornaDatiScritture(data) {
        $("#totaleDare").html(data.totaleDare.formatMoney());
        $("#totaleAvere").html(data.totaleAvere.formatMoney());
        $("#daRegistrare").html(data.daRegistrare.formatMoney());

        popolaTabella(data.listaElementoScritturaPerElaborazione);
    }

    /**
     * Carica la lista del conto tipo operazione.
     *
     * @returns (Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    function caricaElencoScritture() {
        return  $.postJSON(baseUrl + "_ottieniListaConti.do", {}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            aggiornaDatiScritture(data);
        });
    }

    /**
     * Aggiornamento del conto da modale.
     */
    function aggiornamentoContoDaModale() {
        var fieldset = $("#fieldsetModaleAggiornamentoConto");
        var radio = fieldset.find("input[type='radio']:checked");
        var serialized = fieldset.serializeObject();
        var obj;
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

        if(radio.is(":disabled")) {
            // Il radio e' disabilitato. Prendo comunque il valore
            serialized[radio.attr("name")] = radio.val();
        }

        obj = unqualify(serialized, 1);
        $("#segnoModale").val($("input[type='radio'][data-AVERE]").prop('checked'));

        alertErroriModale.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_aggiornaConto.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#modaleAggiornamentoConto").modal("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);

        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Proposta dei conti
     */
    function proponiConti() {
        var causaleEPSelect = $("#causaleEPmovimentoEP");
        causaleEPSelect.overlay("show");
        $.postJSON(baseUrl + "_proponiContiDaCausaleEP.do", {"causaleEP.uid": causaleEPSelect.val()}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errore: esco
                return;
            }
            aggiornaDatiScritture(data);
        }).always(function() {
            causaleEPSelect.overlay("hide");
        });
    }

    $(function () {
        $("#causaleEPmovimentoEP").on("change elencoScrittureCaricato", proponiConti);
        $("#pulsanteSalvaAggiornamentoConto").click(aggiornamentoContoDaModale);
        caricaElencoScritture().always(function() {
            $("#causaleEPmovimentoEP").trigger("elencoScrittureCaricato");
        });
    });
}(jQuery);