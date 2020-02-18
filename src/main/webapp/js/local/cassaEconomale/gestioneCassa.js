/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    "use strict";
    var form = $("#formGestioneCassaEconomale");
    var alertMessaggi = $("#MESSAGGI");
    var alertMessaggiModale = $("#alertModaleConfermaProsecuzioneSuAzione");
    var modaleConferma = $("#modaleConfermaProsecuzioneSuAzione");

    /**
     * Copia dei messaggi tra gli alert.
     *
     * @param start (jQuery) l'alert di partenza
     * @param end   (jQuery) l'alert di arrivo
     */
    function copyBetweenAlerts(start, end) {
        var isShown = !start.is(".hide");
        var content;
        if(!isShown) {
            // Non ho nulla da fare: esco subito
            return;
        }
        // Copio i messaggi
        content = start.find("ul").html();
        end.find("ul").html(content);
        // Chiudo l'alert originale e apro il nuovo
        start.slideUp();
        end.slideDown();
    }

    /**
     * Conferma la prosecuzione dell'azione.
     */
    function confermaProsecuzione() {
        form.append("<input type='hidden' name='proseguireConElaborazione' value='true'/>").submit();
    }

    /**
     * Annullamento della cassa economale.
     */
    function annullamentoCassaEconomale() {
        // Cambio la action al form e lo invio
        form.attr("action", "cassaEconomaleCassaGestioneAnnulla.do").submit();
    }

    // Lego delle funzionalita' al caricamento della pagina
    $(function() {
    	var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        var campoUidSoggetto = $("#HIDDEN_soggettoUid");
    	
        $("#pulsanteProsecuzioneModaleAnnullamentoCassaEconomale").substituteHandler("click", annullamentoCassaEconomale);
        $("#modaleConfermaProsecuzioneSuAzionePulsanteSi").substituteHandler("click", confermaProsecuzione);
        
        // Carica il dettaglio del soggetto e apre gli accordion
        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	    var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, $(), $(),spanDescrizione,false,campoUidSoggetto, undefined,true);
        	    if(!params || !params.doNotCloseAlerts) {
        	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
            			impostaDatiNegliAlert(errori, alertErrori);
            			return errori;
            		});
        	    }
        		
            })
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});

        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan", "#pulsanteAperturaCompilazioneGuidataSoggetto", "","#HIDDEN_soggettoUid");
       
        // Controllo se devo aprire il modale di richiesta prosecuzione
        if(alertMessaggi.find("li").length) {
            copyBetweenAlerts(alertMessaggi, alertMessaggiModale);
            modaleConferma.modal("show");
        }
    });

}(jQuery);