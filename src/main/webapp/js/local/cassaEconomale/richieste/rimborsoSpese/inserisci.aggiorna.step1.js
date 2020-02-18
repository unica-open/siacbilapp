/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var euroUid = parseInt($("#HIDDEN_uidValutaEuro").val());
    var isAggiornamento = $("#HIDDEN_isAggiornamento").val() === "true";
    var tipoFatturaUid = parseInt($("#HIDDEN_uidTipoGiustificativoFattura").val());

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModale = $("#ERRORI_modaleGiustificativo");
    var baseUrl = $("#HIDDEN_baseUrl").val();

    /**
     * Visualizzazione degli importi di cassa.
     */
    function visualizzaImportiCassa() {
        var spinner = $("#SPINNER_pulsanteVisualizzaImporti").addClass("activated");
        $("#divImportiCassa").load(baseUrl +"_visualizzaImporti.do", {}, function() {
            spinner.removeClass("activated");
            $("#modaleImportiCassa").modal("show");
        });
    }
    
    /**
     * Apertura del modale del nuovo giustificativo.
     */
    function apriModaleNuovoGiustificativo() {
        var modale = $("#modaleGiustificativo");
        // Chiudo l'alert di errore
        alertErroriModale.slideUp();
        // Cancello tutti i campi
        modale.find(":input").val("");
        // Imposto l'euro
        $("#valutaGiustificativo").val(euroUid);

        $("#labelModaleGiustificativo").html("Inserimento nuovo giustificativo");
        $("#confermaModaleGiustificativo").substituteHandler("click", inserisciGiustificativo);
        // Apro il modale
        modale.modal("show");
    }

    /**
     * Caricamento della lista dei giustificativi.
     */
    function caricaListaGiustificativi() {
        return $.postJSON(baseUrl + "_ottieniListaGiustificativi.do", {}, function(data) {
            // Impostazione totale
            $("#totaleGiustificativi").html(data.totaleImportiGiustificativi.formatMoney());
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
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").addClass("hide");
                // Inizializzazione dei tooltip
                $("#tabellaGiustificativi").find(".tooltip-test").tooltip();
            },

            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.tipoGiustificativo && (source.tipoGiustificativo.codice + " - " + source.tipoGiustificativo.descrizione) || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.annoProtocollo  || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.numeroProtocollo || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return formatDate(source.dataEmissione);
                }},
                {aTargets: [4], mData: function(source) {
                    return source.numeroGiustificativo || "";
                }},
                {aTargets: [5], mData: function(source) {
                    return source.importoGiustificativo !== undefined && source.importoGiustificativo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };

        if(!isAggiornamento) {
            opts.aoColumnDefs.push({aTargets: [6], mData: function() {
                return "<a title='aggiorna'><i class='icon-pencil icon-2x'><span class='nascosto'>aggiorna</span></i></a>";
            }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                $("a", nTd).click([oData, iRow], aggiornaGiustificativo);
            }});
            opts.aoColumnDefs.push({aTargets: [7], mData: function() {
                return "<a title='elimina'><i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i></a>";
            }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                $("a", nTd).click([oData, iRow], eliminaGiustificativo);
            }});
        }
        $("#tabellaGiustificativi").dataTable(opts);
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
        $("#valutaGiustificativo").val(euroUid);
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
     * Abilitazione e disabilitazione dei campi valuta
     */
    function abilitazioneCampiFattura() {
        // Prendo il contenitore
        var $this = $(this);
        var container = $this.closest("#fieldsetModaleGiustificativo, tr");
        var campiDaAbilitareDisabilitare = container.find("input[data-fattura]");
        // Doppio uguale in quanto possono insorgere problemi di cast (jQuery.fn.val ritorna una stringa)
        var isFattura = $this.val() == tipoFatturaUid;
        campiDaAbilitareDisabilitare[isFattura ? "removeAttr" : "attr"]("disabled", true);
    }

    /**
     * Copia della richiesta economale.
     */
    function copiaRichiestaEconomale() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
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
        $("#pulsanteCopiaRichiestaEconomale").click(copiaRichiestaEconomale);
        $("#pulsanteInserimentoDati").click(apriModaleNuovoGiustificativo);
        $("#pulsanteAnnullaStep1").click(puliziaForm);
        $(document).on("change", "select[data-tipo-giustificativo]", abilitazioneCampiFattura);

        // Carica la lista dei giustificativi dalla action
        caricaListaGiustificativi();
        // Inizializzazione della gestione della matricola
        Matricola.inizializza("#matricolaSoggettoRichiestaEconomale", "#descrizioneMatricola", "#pulsanteCompilazioneGuidataMatricola", "#HIDDEN_codiceAmbito", "#HIDDEN_maySearchHR");
        $("#pulsanteVisualizzaImporti").substituteHandler("click", visualizzaImportiCassa);
    
    });
}(jQuery);