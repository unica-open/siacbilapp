/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";

    /**
     * Imposta l'anno per il datepicker della data di emissione in maniera coerente con l'anno del documento
     */
    function impostaAnnoPerDataEmissione() {
        var anno = parseInt($(this).val(), 10);
        var dataOdierna;
        var datepicker;
        var oldValue;
        if(isNaN(anno)) {
            // L'anno non e' valido: esco
            return;
        }
        // Imposto la data
        dataOdierna = new Date();
        // Imposto la data iniziale al giorno corrente dell'anno selezionato
        dataOdierna.setFullYear(anno);

        // Imposto i dati del datepicker
        datepicker = $("#dataEmissioneDocumento");
        oldValue = datepicker.val();

        // Imposto la data nel datepicker
        datepicker.datepicker("update", formatDate(dataOdierna));
        if(!oldValue) {
            datepicker.val(oldValue);
        }
    }

    /**
     * Apre il modale del soggetto copiando i dati forniti dall'utente.
     */
    function apriModaleSoggetto() {
        $("#codiceSoggetto_modale").val($("#codiceSoggettoSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    }

	/**
     * Popola lo span con i dati del soggetto appena caricato
     */
    function popolaSpanDescrizione () {
    	
    	var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
        var campoUidSoggetto = $("#HIDDEN_soggettoUid");
        
        // Inizializzo l'Overlay
        campoCodiceSoggetto.overlay("show");
        if(!campoCodiceSoggetto.val()) {
            // Svuotare la descrizione
        	spanDescrizione.html("");
            campoCodiceSoggetto.overlay("hide");
            campoUidSoggetto && campoUidSoggetto.val && campoUidSoggetto.val('');
            return $.Deferred().resolve('Nessun soggetto selezionato');
        }
        //setto semaforo soggetto a true (=chiamata in corso)
        window.semaphore.soggetto=true;
        return $.postJSON("ricercaSoggettoPerChiave.do", {"codiceSoggetto": campoCodiceSoggetto.val()}, function(data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                // Se ho errori, esco
                // Svuoto la descrizione del soggetto
            	spanDescrizione.html("");
                campoUidSoggetto.val("");
                return;
            }
            // Carico il soggetto
            impostaDescrizioneSoggetto(data.soggetto, spanDescrizione);
            if (campoUidSoggetto.length) {
                campoUidSoggetto.val(data.soggetto.uid);
            }
        }).always(function() {
            campoCodiceSoggetto.overlay("hide");
            //setto semaforo soggetto a false
            window.semaphore.soggetto=false;
        });
    }
    
    /**
     * Imposta la descrizione del soggetto.
     *
     * @param soggetto (Object) il soggetto da impostare
     * @param element  (jQuery) l'elemetno in cui impostare la descrizione
     */
    function impostaDescrizioneSoggetto(soggetto, element) {
        var denominazione;
        // Se non ho il soggetto, esco
        if(!soggetto) {
            return;
        }
        denominazione = ": " + soggetto.codiceSoggetto + " - " + ( soggetto.denominazione != null ? soggetto.denominazione : "" ) + " - " + ( soggetto.codiceFiscale != null ? soggetto.codiceFiscale : "" );
        // Imposto i dati nello span
        element.html(denominazione);
    }
    
    /**
     * Svuota i campi per il tasto "annulla'
     *
     */
    function svuotaCampiNascosti() {
       $("#HIDDEN_soggettoUid").val("");
       $("#HIDDEN_codiceFiscaleSoggetto").val("");
       $("#HIDDEN_codiceFiscaleSoggetto").val("");
       $("#datiRiferimentoSoggettoSpan").html("");
    }


    $(function() {
        // Campi del soggetto (caricati un'unica volta)
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        
        $("#annoDocumento").on("change blur", impostaAnnoPerDataEmissione);
        $("#btnAnnulla").on("click", svuotaCampiNascosti);


        // Attivo l'apertura del modale dell'atto amministrativo
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(apriModaleSoggetto);
        

        // Inizializzazione dei modali
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#datiRiferimentoSoggettoSpan");

        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	    var def = Soggetto.caricaDettaglioSoggetto($("#codiceSoggettoSoggetto"), $(), $(), $("#datiRiferimentoSoggettoSpan"),false, $("#HIDDEN_soggettoUid"), undefined,true);
        	    if(!params || !params.doNotCloseAlerts) {
        	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
            			impostaDatiNegliAlert(errori, alertErrori);
            			return errori;
            		});
        	    }
        		
            })
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});
    });
}(jQuery));