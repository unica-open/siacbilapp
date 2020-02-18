/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var alertErrori = $("#ERRORI");
    var alertErroriModale = $("#ERRORI_modaleFattura");
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
     * Apertura del modale per la compilazione guidata della fattura
     */
    function apriModaleRicercaFattura() {
        var modale = $("#modaleRicercaFattura");
        // Chiudo l'alert di errore
        alertErroriModale.slideUp();
        // Cancello tutti i campi
        modale.find(":input").val("");
        // Apro il modale
        modale.modal("show");
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
     * Caricamento della lista dei giustificativi.
     */
    function caricaFatturaAssociata() {
        return $.postJSON(baseUrl + "_ottieniFatturaAssociata.do", {}, function(data) {

            if (data.documentoSpesa && data.documentoSpesa.uid !== 0) {
                impostaDatiFatturaAssociata(null, {params: {fattura: data.documentoSpesa, quote: data.listaSubdocumentoSpesa}});
            }
        });
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

    /**
     * Impostazione della fattura.
     *
     * @param e      (Event)  l'evento scatenante l'invocazione
     * @param params (Object) i parametri opzionali dell'evento
     */
    function impostaDatiFatturaAssociata(e, params) {
        var documento = params && params.fattura;
        var quote = params && params.quote;
        if (documento) {
            calcolaDenominazioneSpan(documento, quote);
        }
    }

    /**
     * Calcolo della denominazione per lo span del documento
     *
     * @param documento (Object) il documento da cui ottenere i dati
     * @param quote     (Array)  le quote
     */
    function calcolaDenominazioneSpan(documento, quote) {
        var denominazione = ": " + documento.descAnnoNumeroTipoDoc + "/" + documento.soggetto.codiceSoggetto
            + "/" + documento.soggetto.denominazione + "/" + formatDate(documento.dataEmissione);
        var testoQuote = "";
        if(quote && quote.length) {
            if(quote.length === 1) {
                testoQuote = " - Quota " + quote[0].numero;
            } else {
                testoQuote = " - Quote " + $.map(quote, function(quota) {
                    return quota.numero;
                }).join(", ");
            }
        }
        denominazione += testoQuote;

        // Imposto dati negli hidden per ricaricare correttamente la denominazione fattura
        $("#HIDDEN_annoDocumentoSpesa").val(documento.anno);
        $("#HIDDEN_numeroDocumentoSpesa").val(documento.numero);
        $("#HIDDEN_tipoDocumentoUidDocumentoSpesa").val(documento.tipoDocumento.uid);
        $("#HIDDEN_tipoDocumentoDescrizioneDocumentoSpesa").val(documento.tipoDocumento.descrizione);

        // Imposto i dati nello span
       $("#descrizioneFatturaAssociataSpan").html(denominazione);
    }

    /**
     * Associazione della fattura.
     *
     * @param e     (Event)  L'evento scatenante l'invocazione
     * @param param (Object) i parametri dell'invocazione
     */
    function associazioneFattura(e, param) {
        var fattura;
        var obj = {};
        var alertToUse;
        var alertRedirect;
        if(!param || !param.fattura) {
            // Non associo alcunche'
            return;
        }

        fattura = param.fattura;
        obj.documentoSpesa = fattura;
        $.each(param.quote, function(idx, el) {
            obj['listaSubdocumentoSpesa[' + idx + ']'] = el;
        });
        obj = qualify(obj);

        alertToUse = param.alert || alertErrori;
        alertRedirect = param.alertRedirect !== undefined ? param.alertRedirect : true;
        if (!fattura.statoOperativoDocumento || fattura.statoOperativoDocumento.codice !== "V"){
             impostaDatiNegliAlert(["CEC_ERR_0014, Fattura non completata dalla finanziaria"], alertToUse, alertRedirect);
             return;
        }
        // Effettuo l'associazione
        $.postJSON(baseUrl + "_associaFattura.do", obj)
        .then(function(data) {
            var collapseFattura;
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertToUse, alertRedirect)) {
                return;
            }

            collapseFattura = $("#collapseFattura");
            // Non ho errori. Imposto il titolo
            $(document).trigger("fatturaCaricata", {fattura: fattura, quote: param.quote});

            // Carico la descrizione proposta
            $("#descrizionedellaRichiestaRichiestaEconomale").val(data.descrizioneDellaSpesaProposta);
            if(collapseFattura.is(".in")) {
                collapseFattura.collapse("toggle");
            }
            if(param.callback && typeof param.callback === "function") {
                param.callback();
            }
        });
    }

    $(function() {
        $("#pulsanteCopiaRichiestaEconomale").click(copiaRichiestaEconomale);
        $("#pulsanteAnnullaStep1").click(puliziaForm);

        $(document).on("associaFattura", associazioneFattura);
        $(document).on("fatturaCaricata", impostaDatiFatturaAssociata);

        // Carica la lista dei giustificativi dalla action
        caricaFatturaAssociata();
        // Inizializzazione della gestione della matricola
        Matricola.inizializza("#matricolaSoggettoRichiestaEconomale", "#descrizioneMatricola", "#pulsanteCompilazioneGuidataMatricola", "#HIDDEN_codiceAmbito", "#HIDDEN_maySearchHR");
        $("#pulsanteVisualizzaImporti").substituteHandler("click", visualizzaImportiCassa);
    });

}(jQuery);