/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var cassaInStatoValido = $("#cassaInStatoValido").val() === "true";

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var divOperazione = $("#divOperazioneCassa");
    var pulsanteInserimento = $("#pulsanteInserisci");
    var spinnerPulsanteInserimento = $("#SPINNER_pulsanteInserisci");
    var divRisultati = $("#risultatiRicercaTipoOperazioniCassa");

    /**
     * Ricerca il tipo di operazione di cassa.
     */
    function ricercaTipoOperazioneCassa() {
        var obj = $("#fieldsetRicerca").serializeObject();
        var spinner = $("#SPINNER_pulsanteRicercaTipoOperazioneCassa").addClass("activated");

        divRisultati.slideUp();
        // Effettuo la ricerca
        $.postJSON("cassaEconomaleGestioneTabelleTipiOperazioniDiCassaRicerca.do", obj, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            impostaTabellaTipoOperazioniCassa();
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Impostazione della tabella dei tipi operazione di cassa.
     */
    function impostaTabellaTipoOperazioniCassa() {
        var tableId = "#tabellaTipoOperazioneCassa";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaTipoOperazioneCassaAjax.do",
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
                    sEmptyTable: "Nessun tipo operazione disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.codice || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.statoOperativoTipoOperazioneCassa && source.statoOperativoTipoOperazioneCassa.descrizione || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return booleanToString(source.inclusoInGiornale);
                }},
                {aTargets: [4], mData: function(source) {
                    return booleanToString(source.inclusoInRendiconto);
                }},
                {aTargets: [5], mData: function(source) {
                    var res = "";
                    if(cassaInStatoValido && source.statoOperativoTipoOperazioneCassa.descrizione === "Valido") {
                        res = "<div class='btn-group'>"
                            +     "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>"
                            +     "<ul class='dropdown-menu pull-right'>"
                            +         "<li><a class='aggiornaCassa'>aggiorna</a></li>"
                            +         "<li><a class='annullaCassa'>annulla</a></li>"
                            +     "</ul>"
                            + "</div>";
                    }
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaCassa")
                            .substituteHandler("click", function() {
                                apriCollapseAggiornamento(oData);
                            })
                            .end()
                        .find("a.annullaCassa")
                            .substituteHandler("click", function() {
                                apriModaleAnnullamento(oData);
                            });
                }}
            ]
        };
        divRisultati.slideDown();
        $(tableId).dataTable(opts);
    }

    /**
     * Trasforma il boolean fornito in una stringa.
     *
     * @param bool (Boolean) il boolean da controllare
     *
     * @returns 'S' se il boolean e' true; 'N' altrimenti
     */
    function booleanToString(bool) {
        return bool ? "S" : "N";
    }

    /**
     * Apertura del collapse di aggiornamento delle operazioni di cassa.
     *
     * @param tipo (Object) il tipo da aggiornare
     */
    function apriCollapseAggiornamento(tipo) {
        var obj = {'tipoOperazioneCassa.uid': tipo.uid};
        caricamentoDivOperazione("cassaEconomaleGestioneTabelleTipiOperazioniDiCassaInizioAggiornamento.do", obj,
            "cassaEconomaleGestioneTabelleTipiOperazioniDiCassaAggiornamento.do");
    }

    /**
     * Apertura del modale di annullamento della cassa economale
     */
    function apriModaleAnnullamento(tipo) {
        $("#elementoSelezionatoModaleAnnullamentoElemento").html(tipo.codice + " - " + tipo.descrizione);
        $("#confermaModaleAnnullamentoElemento").data("uid", tipo.uid);
        $("#modaleAnnullamentoElemento").modal("show");
    }

    /**
     * Apertura del collapse di inserimento delle operazioni di cassa.
     */
    function apriCollapseInserimento() {
        caricamentoDivOperazione("cassaEconomaleGestioneTabelleTipiOperazioniDiCassaInizioInserimento.do", {},
            "cassaEconomaleGestioneTabelleTipiOperazioniDiCassaInserimento.do");
    }

    /**
     * Caricamento dei dati nel div dell'operazione di cassa.
     *
     * @param urlInizio (String) l'URL per l'inizio dell'invocazione
     * @param obj       (Object) l'oggetto da passare all'invocazione della chiamata AJAX
     * @param urlFine   (String) l'URL per la fine dell'invocazione
     */
    function caricamentoDivOperazione(urlInizio, obj, urlFine) {
        spinnerPulsanteInserimento.addClass("activated");
        // Invocazione del servizio e caricamento della pagina
        divOperazione.load(urlInizio, obj, function() {
            $("#salvaOperazioneCassa").substituteHandler("click", function() {
                salvaOperazioneCassa(urlFine);
            });
            $("#annullaOperazioneCassa").substituteHandler("click", annullaOperazioneCassa);

            // Mostro il div
            divOperazione.fadeIn("fast", "linear");
            spinnerPulsanteInserimento.removeClass("activated");
            pulsanteInserimento.prop("disabled", true);
        });
    }

    /**
     * Salvataggio del tipo.
     *
     * @param url (String) l'URL da invocare
     */
    function salvaOperazioneCassa(url) {
        var obj = $("#fieldsetOperazioneCassa, #fieldsetRicerca").serializeObject();

        // Attivo l'overlay
        divOperazione.overlay("show");

        // Chiamata
        $.postJSON(url, obj, function(data) {
            // Se ho errori, esco subito
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Imposto il messaggio di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico il dataTable
            impostaTabellaTipoOperazioniCassa();
            // Nascondo il div
            divOperazione.fadeOut("fast", "linear");
            pulsanteInserimento.removeProp("disabled");
        }).always(function() {
            divOperazione.overlay("hide");
        });
    }

    /**
     * Chiusura del collapse.
     */
    function annullaOperazioneCassa() {
        divOperazione.fadeOut("fast", "linear");
        pulsanteInserimento.removeProp("disabled");
    }

    /**
     * Annulla il tipo di operazione di cassa.
     */
    function annullaTipoOperazioneCassa() {
        var uid = $("#confermaModaleAnnullamentoElemento").data("uid");
        var spinner;
        var obj;
        var objTipo;
        var objRicerca;
        // Controllo di avere l'uid
        if(!uid) {
            return;
        }

        objTipo = {"tipoOperazioneCassa.uid": uid};
        objRicerca = $("#fieldsetRicerca").serializeObject();
        obj = $.extend(true, {}, objTipo, objRicerca);
        // Attivo lo spinner
        spinner = $("#SPINNER_confermaModaleAnnullamentoElemento").addClass("activated");
        $.postJSON("cassaEconomaleGestioneTabelleTipiOperazioniDiCassaAnnullamento.do", obj, function(data) {
            // Se ho errori, esco subito
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Imposto il messaggio di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico il dataTable
            impostaTabellaTipoOperazioniCassa();
        }).always(function() {
            spinner.removeClass("activated");
            $("#modaleAnnullamentoElemento").modal("hide");
        });
    }

    $(function() {
        $("#pulsanteRicercaTipoOperazioneCassa").substituteHandler("click", ricercaTipoOperazioneCassa);
        pulsanteInserimento.substituteHandler("click", apriCollapseInserimento);
        $("#confermaModaleAnnullamentoElemento").substituteHandler("click", annullaTipoOperazioneCassa);

        // Per evitare che persistano stati incoerenti
        pulsanteInserimento.removeProp("disabled");
    });
}(jQuery);