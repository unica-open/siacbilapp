/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var fatturaUid = parseInt($("#HIDDEN_uidFattura").val());

    var azioniTemplate = "<div class='btn-group'>" +
                             "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                             "<ul class='dropdown-menu pull-right'>" +
                                 "<li><a class='aggiornaGiustificativo'>aggiorna</a></li>" +
                                 "<li><a class='eliminaGiustificativo'>elimina</a></li>" +
                                 "<li><a class='escludiPagamento'>escludi pagamento</a></li>" +
                             "</ul>" +
                         "</div>";

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModale = $("#ERRORI_modaleGiustificativo");
    var baseUrl = $("#HIDDEN_baseUrl").val();
    var restituzioneTotaleUrl = $("#HIDDEN_urlRestituzioneTotale").val();
    var restituzioneAltroUfficioUrl = $("#HIDDEN_urlRestituzioneAltroUfficio").val();
    var flagRestituzioneTotale = $("#HIDDEN_flagRestituzioneTotale").val();
    /**
     * Apertura del modale del nuovo giustificativo.
     */
    function apriModaleNuovoGiustificativo() {
    	
        var modale = $("#modaleGiustificativo");
        // Chiudo l'alert di errore
        alertErroriModale.slideUp();
        // Cancello tutti i campi
        modale.find(":input").not("*[data-maintain]").val("");
        //azzero label e tasti nel caso fosse già passsato da un aggiornamento.
        $("#labelModaleGiustificativo").html("Inserimento nuovo giustificativo");
        $("#confermaModaleGiustificativo").substituteHandler("click", function() {
        	inserisciGiustificativo();
        });
        // Disabilito i campi
        $("#annoProtocolloGiustificativo, #numeroProtocolloGiustificativo").attr("disabled", true);
        // Apro il modale
        modale.modal("show");
    }
    
    /**
     * verifica se c'e stato una restituzione totale :
     * 1-se si -> mostra messaggio per confermare l'annullamento dopo di che apre la modale di inserimento giustificativi
     * 2-se no -> esce
     */
    function mostraMessaggioDiConferma(){
    	if(flagRestituzioneTotale==="true"){
            bootbox.dialog(
                    "Se si procede con l'inserimento dei giustificativi la restituzione totale verra annullata. Si desidera comunque proseguire?",
                    [
                        {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
                        {"label" : "Si, prosegui", "class" : "btn", "callback":apriModaleNuovoGiustificativo }
                    ],
                    {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'}
                );
    	}else{
    		apriModaleNuovoGiustificativo();
    	}
    }

    /**
     * Caricamento della lista dei giustificativi.
     */
    function caricaListaGiustificativi() {
        return $.postJSON(baseUrl + "_ottieniListaGiustificativi.do", {}, function(data) {
            $("#totaleGiustificativi").html(data.totaleImportiGiustificativi.formatMoney());
            $("#importoDaRestituire").html(data.importoDaRestituire.formatMoney());
            $("#importoDaIntegrare").html(data.importoDaIntegrare.formatMoney());
            // Impostazione dei giustificativi
            impostaTabellaGiustificativi(data.listaGiustificativo);
        });
    }

    /**
     * Impostazione della tabella dei giustificativi.
     *
     * @param list (Array) la lista da impostare
     */
    function impostaTabellaGiustificativi(list) {
        var tableId = "#tabellaGiustificativi";
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
                sZeroRecords: "Nessun giustificativo associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $(tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $(tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                // Inizializzazione dei datepicker
                $(tableId).find(".tooltip-test").tooltip();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.tipoGiustificativo && (source.tipoGiustificativo.codice + " - " + source.tipoGiustificativo.descrizione) || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.annoProtocollo || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.numeroProtocollo || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return formatDate(source.dataEmissione) || "";
                }},
                {aTargets: [4], mData: function(source) {
                    return source.numeroGiustificativo || "";
                }},
                {aTargets: [5], mData: function(source) {
                    return !source.flagInclusoNelPagamento
                        ? "<a data-original-title='Escluso da pagamento' class='tooltip-test'><span class='icon-warning-sign icon-red alRight'></span></a>"
                        : "";
                }},
                {aTargets: [6], mData: function(source) {
                    return source.importoGiustificativo !== undefined && source.importoGiustificativo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [7], mData: function() {
                    return azioniTemplate;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).find("a.aggiornaGiustificativo")
                            .click([oData, iRow], aggiornaGiustificativo)
                            .end()
                        .find("a.eliminaGiustificativo")
                            .click([oData, iRow], eliminaGiustificativo)
                            .end()
                        .find("a.escludiPagamento")
                            .click([oData, iRow], escludiPagamento);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Selezione della option nella lista corrispondente al valore fornito. L'uguaglianza tra i termini sara' debole.
     *
     * @param select (jQuery) la select da analizzare
     * @param value  (Object) il valore da considerare per la selezione
     *
     * @param (jQuery) la select modificata
     */
    function selectCorrectOption(select, value) {
        return select.find("option")
            .each(function() {
                var $this = $(this);
                var fnc = $this.val() == value ? "attr" : "removeAttr";
                $this[fnc]("selected", true);
            });
    }

    /**
     * Aggiornamento del giustificativo.
     *
     * @param e (Event) l'eveno scatenante l'invocazione della funzione
     */
    function aggiornaGiustificativo(e) {
        var fieldset = $("#fieldsetModaleGiustificativo");
        var giustificativo = e.data[0];
        var row = +e.data[1];

        // Ciclo sui campi per impostarne i valori
        fieldset.find(":input[name^='giustificativo.']").each(function(idx, el) {
            var $el = $(el);
            var name = $el.attr("name").split("\.").slice(1);
            var value = giustificativo;
            // Ricorsivamente sull'oggetto
            for(var i in name) {
                if(name.hasOwnProperty(i)) {
                    value = value ? value[name[i]] : "";
                }
            }
            if($el.hasClass('decimale')) {
                value = formatMoney(value);
            } else if($el.data("date")) {
                value = formatDate(value);
            }
            $el.val(value).blur();
        });

        alertErroriModale.slideUp();
        $("#labelModaleGiustificativo").html("Aggiornamento giustificativo numero " + (row + 1));
        $("#confermaModaleGiustificativo").substituteHandler("click", function() {
            effettuaAggiornamentoGiustificativo(row);
        });
        $("#modaleGiustificativo").modal("show");
    }

    /**
     * Formattazione in valuta
     * @param el (any) il campo da formattare
     * @returns (any) il numero formattato, o il campo in input se non numerico
     */
    function formatMoney(el) {
        return typeof el === 'number' ? el.formatMoney() : el;
    }

    /**
     * Effettua l'aggiornamento del giustificativo.
     *
     * @param row (Number) il numero di riga
     */
    function effettuaAggiornamentoGiustificativo(row) {
        var obj = $("#fieldsetModaleGiustificativo").serializeObject();
        obj.rowNumber = row;

        // Chiudo l'alert degli errori
        alertErroriModale.slideUp();

        // Chiamata al servizio
        $.postJSON(baseUrl + "_updateGiustificativo.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Chiusura modale
            $("#modaleGiustificativo").modal("hide");
            // Ricaricamento tabella
            caricaListaGiustificativi();
        });
    }

    /**
     * Inizio dell'eliminazione del giustificativo.
     *
     * @param e (Event) l'evento scatenante l'invocazione della funzione
     */
    function eliminaGiustificativo(e) {
        // TODO: diversificare le stringhe
        var str = e.data[0].numeroGiustificativo
            ? 'giustificativo numero ' + e.data[0].numeroGiustificativo
            : 'giustificativo numero ' + (e.data[1] + 1);
        $("#elementoSelezionatoModaleAnnullamentoElemento").html(str);
        $("#confermaModaleAnnullamentoElemento").off("click").click(e.data, eliminazioneGiustificativo);
        $("#modaleAnnullamentoElemento").modal("show");
    }

    /**
     * Eliminazione del giustificativo.
     *
     * @param e (Event) l'eveno scatenante l'invocazione della funzione
     */
    function eliminazioneGiustificativo(e) {
        var row = e.data[1];
        var spinner = $("#SPINNER_confermaModaleAnnullamentoElemento").addClass("activated");
        $.postJSON(baseUrl + "_removeGiustificativo.do", {rowNumber: row}, function(data) {
            impostaDatiNegliAlert(data.errori, alertErrori) || impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricaricamento tabella
            caricaListaGiustificativi();
        }).always(function() {
            $("#modaleAnnullamentoElemento").modal("hide");
            spinner.removeClass("activated");
        });
    }

    /**
     * Esclusione del giustificativo dal pagamento.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function escludiPagamento(e) {
        var tr = $(e.currentTarget).closest("tr");
        var row = e.data[1];
        tr.overlay("show");
        $.postJSON(baseUrl + "_escludiPagamento.do", {rowNumber: row}, function(data) {
            impostaDatiNegliAlert(data.errori, alertErrori) || impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricaricamento tabella
            caricaListaGiustificativi();
        }).always(function() {
            tr.overlay("hide");
        });
    }

    /**
     * Inserimento del giustificativo
     */
    function inserisciGiustificativo() {
        var obj = $("#fieldsetModaleGiustificativo").serializeObject();

        // Chiudo l'alert degli errori
        alertErroriModale.slideUp();

        // Chiamata al servizio
        $.postJSON(baseUrl + "_addGiustificativo.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Chiusura modale
            $("#modaleGiustificativo").modal("hide");
            // Ricaricamento tabella
            caricaListaGiustificativi();
        });
    }

    /**
     * Abilitazione e disabilitazione dei campi fattura.
     */
    function abilitazioneCampiFattura() {
        // Prendo il contenitore
        var $this = $(this);
        var container = $this.closest("#fieldsetModaleGiustificativo, tr");
        var cambiDaAbilitareDisabilitare = container.find("input[data-fattura]");
        // Doppio uguale in quanto possono insorgere problemi di cast (jQuery.fn.val ritorna una stringa)
        var isFattura = $this.val() == fatturaUid;
        cambiDaAbilitareDisabilitare.each(function() {
            var $this = $(this);
            var originalValue = $this.data("originalValue");
            $this[!isFattura ? "attr" : "removeAttr"]("disabled", true)
                // Reimposto i precedenti valori, se presenti. Altrimenti cancello il dato
                .val(isFattura ? originalValue : "");
        });
    }

    /**
     * Effettua la restituzione globale dell'importo della richiesta.
     */
    function effettuaRestituzioneGlobale() {
        $("form").attr("action", restituzioneTotaleUrl).submit();
    }

    /**
     * Gestione della restituzione totale.
     */
    function gestioneRestituzioneTotale() {
        var datatable = $("#tabellaGiustificativi").dataTable();
        if(datatable.$("tr").length) {
            bootbox.dialog(
                "Sono gi&agrave; stati compilati alcuni dati del rendiconto, se si procede con la restituzione totale non verranno salvati. Si desidera comunque proseguire?",
                [
                    {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
                    {"label" : "Si, prosegui", "class" : "btn", "callback": effettuaRestituzioneGlobale}
                ],
                {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'}
            );
        } else {
            effettuaRestituzioneGlobale();
        }
    }
    
    /**
     * Effettua la restituzione altro ufficio dell'importo della richiesta.
     */
    function effettuaRestituzioneAltroUfficio() {
        $("form").attr("action", restituzioneAltroUfficioUrl).submit();
    }

    /**
     * Gestione della restituzione altro ufficio.
     */
    function gestioneRestituzioneAltroUfficio() {
        var datatable = $("#tabellaGiustificativi").dataTable();
        if(datatable.$("tr").length) {
            bootbox.dialog(
                "Sono gi&agrave; stati compilati alcuni dati del rendiconto, se si procede con la restituzione altro ufficio non verranno salvati. Si desidera comunque proseguire?",
                [
                    {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
                    {"label" : "Si, prosegui", "class" : "btn", "callback": effettuaRestituzioneAltroUfficio}
                ],
                {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'}
            );
        } else {
            effettuaRestituzioneAltroUfficio();
        }
    }

    /**
     * Svuota tutti i campi del form
     */
    function puliziaForm() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }

    $(function() {
        $("#pulsanteInserimentoDati").click(mostraMessaggioDiConferma);
        $("#confermaModaleGiustificativo").click(inserisciGiustificativo);
        $("#pulsanteConfermaModaleRestituzioneTotale").click(gestioneRestituzioneTotale);
        $("#pulsanteConfermaModaleRestituzioneAltroUfficio").click(gestioneRestituzioneAltroUfficio);
        $("#pulsanteAnnullaStep1").click(puliziaForm);
        $(document).on("change", "select[data-tipo-giustificativo]", abilitazioneCampiFattura);

        // Carica la lista dei giustificativi dalla action
        caricaListaGiustificativi();
    });
}(jQuery);