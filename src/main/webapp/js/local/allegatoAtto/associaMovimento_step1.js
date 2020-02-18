/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    // Per lo sviluppo
    "use strict";
    var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
    var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
    var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
    var spanDescrizione = $("#datiRiferimentoSoggettoSpan");

    /**
     * Apertura del modale del soggetto
     */
    function apriModaleSoggetto() {
        // Copio i dati
        $("#codiceSoggetto_modale").val(campoCodiceSoggetto.val());
        // Chiudo gli errori
        $("#ERRORI_MODALE_SOGGETTO").slideUp();
        // Apro il modale
        $("#modaleGuidaSoggetto").modal("show");
    }

    $(function() {
        // Inizializzazione dei dati del soggetto
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

        // Apertura del modale di compilazione guidata
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(apriModaleSoggetto);
        // Caricamento dettaglio soggetto
        var alertErrori = $('#ERRORI');
        campoCodiceSoggetto.on("change codiceSoggettoCaricato", function(e, doNotCleanModal, doNotCloseAlert) {
            var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione, !!doNotCleanModal,undefined, undefined, true)
	        	.always(function() {
	                // Caricamento vecchi valori
	                Soggetto.caricaOldSedeSecondaria(accordionSedeSecondaria, $("#oldSedeSecondariaSoggetto").val());
	                Soggetto.caricaOldModalitaPagamentoSoggetto(accordionModalitaPagamento, $("#oldModalitaPagamentoSoggetto").val());
	        	});
            if(!doNotCloseAlert){
            	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){ 
                	impostaDatiNegliAlert(errori, alertErrori);
                	return errori;
                });
            }
        })
        	// Forzo la chiamata al metodo
        	.trigger("change", [true, true]);
        // Filtro delle modalita di pagamento
        accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
            var tabella = accordionModalitaPagamento.find("table");
            var uid = $(this).filter(":checked").val();
            Soggetto.filterModalitaPagamentoSoggetto(uid, tabella, accordionModalitaPagamento);

            Soggetto.caricaOldModalitaPagamentoSoggetto(accordionModalitaPagamento, $("#accordionModalitaPagamento").val());
        });
    });
}(jQuery, this));