/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var tipoCausaleIntegrata = $("#HIDDEN_tipoCausaleIntegrata").val();

    /**
     * Popolamento della select.
     *
     * @param select  (jQuery) la select da popolare
     * @param options (Array)  l'array delle opzioni
     */
    function popolaSelect(select, options) {
        var opts = "";
        $.each(options, function() {
            opts += "<option value='" + this.uid + "'>" + this.codice + " - " + this.descrizione + "</option>";
        });
        select.html(opts);
        select.multiSelect("refresh");
    }
    /**
     * Popolamento della select.
     *
     * @param select  (jQuery) la select da popolare
     * @param options (Array)  l'array delle opzioni
     */
    function popolaSelectSingola(select, options) {
        var opts = "<option></option>";
        $.each(options, function() {
            opts += "<option value='" + this.uid + "'>" + this.codice + " - " + this.descrizione + "</option>";
        });
        select.html(opts);
        
    }

    /**
     * Caricamento degli eventi.
     */
    function caricaEventi() {
        var selectTipoEvento = $("#uidTipoEvento");
        var selectEvento = $("#uidEvento");
        var tipoEvento = selectTipoEvento.val();
        var overlayElements = $("#uidTipoEvento, #ms-uidEvento");

        // Attivo gli overlay
        overlayElements.overlay("show");

        // Se non ho selezionato il l'evento, svuoto la select ed esco
        if(!tipoEvento) {
            popolaSelect(selectEvento, []);
            overlayElements.overlay("hide");
            return;
        }

        // Chiamata AJAX
        $.postJSON("ricercaEventoByTipoEvento.do", {"tipoEvento.uid": tipoEvento}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectEvento, []);
                return;
            }
            popolaSelect(selectEvento, data.listaEvento);
        }).always(function() {
            // Nasconfdo gli overlay
            overlayElements.overlay("hide");
        });
    }

    /**
     * Mostra o nasconde gli attributi della causale di raccordo.
     */
    function gestisciAttributiCausaleRaccordo() {
        var selectTipoEvento = $("#uidTipoEvento");
        var selectEvento = $("#uidEvento");
        var value = $("#tipoCausaleCausaleEP").val();
        var overlayElements = $("#uidTipoEvento, #ms-uidEvento");
        $("#divCampiRaccordo")[value === tipoCausaleIntegrata ? "slideDown" : "slideUp"]();
        // Rifiltro il tipo evento..
        var obj = $("#tipoCausaleCausaleEP").serializeObject();
        // Chiamata AJAX
        $.postJSON("inserisciCausaleEPGSA_ottieniListaTipoEventoFiltrata.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                popolaSelectSingola(selectTipoEvento, []);
                popolaSelect(selectEvento, []);
                return;
            }
            popolaSelect(selectEvento, []);
            popolaSelectSingola(selectTipoEvento, data.listaTipoEventoFiltrata);
        }).always(function() {
            // Nasconfdo gli overlay
            overlayElements.overlay("hide");
        });
    }

    /**
     * Caricamento del dettaglio del soggetto.
     */
    function caricaDettaglioSoggetto() {
        Soggetto.caricaDettaglioSoggetto($("#codiceSoggettoSoggetto"), $(), $(), $("#descrizioneMatricola"));
    }

    $(function () {
        
        $("#uidTipoEvento").change(caricaEventi);
        $("#tipoCausaleCausaleEP").change(gestisciAttributiCausaleRaccordo);
        $("#codiceSoggettoSoggetto").change(function(e, params) {
        	var alertErrori = $('#ERRORI');
    	    var def = Soggetto.caricaDettaglioSoggetto($("#codiceSoggettoSoggetto"), $(), $(), $("#descrizioneCompletaSoggetto"),undefined,undefined, undefined,true);
    	    if(!params || !params.doNotCloseAlerts) {
    	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
        			impostaDatiNegliAlert(errori, alertErrori);
        			return errori;
        		});
    	    }
    		
        })
        // Forzo la chiamata al metodo
        .trigger('change', {doNotCloseAlerts: true});
        // Inizializzazione dei modali
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_denominazioneSoggetto", "#descrizioneCompletaSoggetto","#pulsanteCompilazioneGuidataSoggetto");
     //   ContoFIN.inizializza("#codiceElementoPianoDeiConti", "#pulsanteCompilazioneGuidataElementoPianoDeiConti");
    });
}(jQuery);