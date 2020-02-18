/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var divOperazione = $("#divOperazioneCassa");
    var pulsanteInserimento = $("#pulsanteInserisci");
    var spinnerPulsanteInserimento = $("#SPINNER_pulsanteInserisci");
    var divRisultati = $("#risultatiRicercaOperazioneCassa");
    var azioniAbilitate = $("#azioniAbilitate").val() === "true";
    var messaggioErroreGiaStampato = "COR_ERR_0044 - Operazione non consentita: l'operazione di cassa &eacute; gi&agrave; stata stampata sul giornale di cassa. Non &eacute; possibile effettuare la modifica";

    /**
     * Ricerca l'operazione di cassa.
     */
    function ricercaOperazioneCassa() {
        var obj = $("#fieldsetRicerca").serializeObject();
        var spinner = $("#SPINNER_pulsanteRicercaOperazioneCassa").addClass("activated");

        divRisultati.slideUp();
        // Effettuo la ricerca
        $.postJSON("cassaEconomaleOperazioniCassaGestioneRicerca.do", obj, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            impostaTabellaOperazioniCassa();
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Impostazione della tabella delle operazione di cassa.
     */
    function impostaTabellaOperazioniCassa() {
        var tableId = "#tabellaOperazioneCassa";
        var zero = 0;
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaOperazioneCassaAjax.do",
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
                    sEmptyTable: "Nessuna operazione di cassa disponibile"
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
                    return source.dataOperazione && formatDate(source.dataOperazione) || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.tipoOperazioneCassa && source.tipoOperazioneCassa.tipologiaOperazioneCassa && source.tipoOperazioneCassa.tipologiaOperazioneCassa.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.tipoOperazioneCassa && source.tipoOperazioneCassa.descrizione || "";
                }},
      
                {aTargets: [3], mData: function(source) {
                    return source.modalitaPagamentoCassa && source.modalitaPagamentoCassa.descrizione || "";
                }},
                {aTargets: [4], mData: function(source) {
                    return source.statoOperativoOperazioneCassa && source.statoOperativoOperazioneCassa.descrizione || "";
                }},
                {aTargets: [5], mData: function(source) {
                    var number = source.importo || zero;
                    return number.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [6], mData: function(source) {
                    var res = "";
                    var aggiornaAbilitato = source.statoOperativoOperazioneCassa && (source.statoOperativoOperazioneCassa.codice === "P" || source.statoOperativoOperazioneCassa.codice === "D");
                    var annullaAbilitato = source.statoOperativoOperazioneCassa && source.statoOperativoOperazioneCassa.codice === "P";
                    if(azioniAbilitate && annullaAbilitato) {
                        res = "<div class='btn-group'>"
                            +     "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>"
                            +     "<ul class='dropdown-menu pull-right'>";
                        if(aggiornaAbilitato){
                            res +=   "<li><a class='aggiornaOperazioneCassa'>aggiorna</a></li>";
                        }
                        res +=       "<li><a class='annullaOperazioneCassa'>annulla</a></li>"
                            +    "</ul>"
                            + "</div>";
                    }
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaOperazioneCassa")
                            .substituteHandler("click", function() {
                                apriCollapseAggiornamento(oData);
                            })
                            .end()
                        .find("a.annullaOperazioneCassa")
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
     * Apertura del collapse di aggiornamento delle operazioni di cassa.
     *
     * @param tipo (Object) il tipo da aggiornare
     */
    function apriCollapseAggiornamento(operazione) {
        var obj;
        if(operazione.statoOperativoOperazioneCassa && operazione.statoOperativoOperazioneCassa.codice === "D") {
            return bootboxAlert(messaggioErroreGiaStampato, undefined, "dialogWarn");
        }
        obj = {'operazioneCassa.uid': operazione.uid};
        caricamentoDivOperazione("cassaEconomaleOperazioniCassaGestioneInizioAggiornamento.do", obj,
            "cassaEconomaleOperazioniCassaGestioneAggiornamento.do");
    }

    /**
     * Apertura del modale di annullamento della cassa economale.
     *
     * @param tipo (Object) il tipo da annullare
     */
    function apriModaleAnnullamento(tipo) {
        $("#elementoSelezionatoModaleAnnullamentoElemento").html(tipo.numeroOperazione);
        $("#confermaModaleAnnullamentoElemento").data("uid", tipo.uid);
        $("#modaleAnnullamentoElemento").modal("show");
    }

    /**
     * Apertura del collapse di inserimento delle operazioni di cassa.
     */
    function apriCollapseInserimento() {
        caricamentoDivOperazione("cassaEconomaleOperazioniCassaGestioneInizioInserimento.do", {},
            "cassaEconomaleOperazioniCassaGestioneInserimento.do");
    }

    /**
     * Caricamento dei dati nel div dell'operazione di cassa.
     *
     * @param urlInizio (String) l'URL per l'inizio dell'invocazione
     * @param obj       (Object) l'oggetto da passare all'invocazione della chiamata AJAX
     * @param urlFine   (String) l'URL per la fine dell'invocazione
     */
    function caricamentoDivOperazione(urlInizio, obj, urlFine) {
        // Attivo lo spinner
        spinnerPulsanteInserimento.addClass("activated");
        // Invocazione del servizio e caricamento della pagina
        divOperazione.load(urlInizio, obj, function() {
            $("#salvaOperazioneCassa").substituteHandler("click", function() {
                salvaOperazioneCassa(urlFine);
            });
            $("#annullaOperazioneCassa").substituteHandler("click", annullaOperazioneCassa);

            // Imposto le funzionalita' aggiuntive
            divOperazione.find(".soloNumeri")
                    .allowedChars({numeric : true})
                    .end()
                .find(".decimale")
                    .gestioneDeiDecimali()
                    .end()
                .find(".datepicker")
                    .datepicker({
                        weekStart : 1,
                        language : "it",
                        startDate : "01/01/1901",
                        autoclose : true
                        })
                    .attr("tabindex", -1)
                    .end()
                .find("#pulsanteCompilazioneAttoAmministrativo")
                    .substituteHandler("click", apriModaleAttoAmministrativo);
            // Provvedimento
            Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmm",
                    "#HIDDEN_StrutturaAmministrativoContabileUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoProvvedimentoSpan");
            // Mostro il div
            divOperazione.fadeIn("fast", "linear");
            spinnerPulsanteInserimento.removeClass("activated");
            pulsanteInserimento.prop("disabled", true);
        });
    }

    /**
     * Apre il modale dell'atto amministrativo copiando i dati forniti dall'utente.
     */
    function apriModaleAttoAmministrativo() {
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm_modale");

        $("#annoProvvedimento_modale").val($("#annoAttoAmministrativo").val());
        $("#numeroProvvedimento_modale").val($("#numeroAttoAmministrativo").val());
        $("#tipoAttoProvvedimento_modale").val($("#tipoAtto").val());
        // Seleziono dallo zTree
        ZTreePreDocumento.selezionaNodoSeApplicabile(tree.innerZTree, $("#HIDDEN_StrutturaAmministrativoContabileUid").val());
        // Apro il modale
        $("#modaleGuidaProvvedimento").modal("show");
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
            impostaTabellaOperazioniCassa();
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
    function annullamentoOperazioneCassa() {
        var uid = $("#confermaModaleAnnullamentoElemento").data("uid");
        var spinner;
        var obj;
        var objTipo;
        var objRicerca;
        // Controllo di avere l'uid
        if(!uid) {
            return;
        }

        objTipo = {"operazioneCassa.uid": uid};
        objRicerca = $("#fieldsetRicerca").serializeObject();
        obj = $.extend(true, {}, objTipo, objRicerca);
        // Attivo lo spinner
        spinner = $("#SPINNER_confermaModaleAnnullamentoElemento").addClass("activated");
        $.postJSON("cassaEconomaleOperazioniCassaGestioneAnnullamento.do", obj, function(data) {
            // Se ho errori, esco subito
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Imposto il messaggio di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico il dataTable
            impostaTabellaOperazioniCassa();
        }).always(function() {
            spinner.removeClass("activated");
            $("#modaleAnnullamentoElemento").modal("hide");
        });
    }

    /**
     * Visualizzazione degli importi di cassa.
     */
    function visualizzaImportiCassa() {
        var spinner = $("#SPINNER_pulsanteVisualizzaImporti").addClass("activated");
        $("#divImportiCassa").load("cassaEconomaleOperazioniCassaGestioneImportiCassa.do", {}, function() {
            spinner.removeClass("activated");
            $("#modaleImportiCassa").modal("show");
        });
    }

    $(function() {
        $("#pulsanteRicercaOperazioneCassa").substituteHandler("click", ricercaOperazioneCassa);
        pulsanteInserimento.substituteHandler("click", apriCollapseInserimento);
        $("#confermaModaleAnnullamentoElemento").substituteHandler("click", annullamentoOperazioneCassa);
        $("#pulsanteVisualizzaImporti").substituteHandler("click", visualizzaImportiCassa);

        // Per evitare che persistano stati incoerenti
        pulsanteInserimento.removeProp("disabled");
    });
}(jQuery);