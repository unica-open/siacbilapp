/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    $(function() {
    
        var originalCausale = $("#originalCausale").val();
        var first = true;
    
        var typeaheads = $("input[data-typeahead]");
        var idProvvedimento;
    
        // Campi soggetto
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
        var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
        var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
    
        // Inizializzo i typeahead
        PreDocumento.impostaTypeahead(typeaheads);
        PreDocumento.calculateProvincia(typeaheads);
    
    
        $("#dataCompetenzaPreDocumento").on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
        $("#tipoCausale").change(PreDocumento.caricaListaCausaleSpesa(false, false));
        $("#causaleSpesa").change(PreDocumento.impostaDatiCausaleSpesa);
    
        $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
            .on("selezionataStrutturaAmministrativaContabile", function(e) {
                var caus;
                if(e.treeId === "treeStruttAmmAttoAmministrativo") {
                    // Non e' un evento che mi interessi
                    return;
                }
    
                caus = first ? originalCausale : undefined;
                first = false;
                PreDocumento.caricaListaCausaleSpesa(false, e.isFirst)(caus);
            });
    
        // Capitolo
        $("#pulsanteCompilazioneGuidataCapitolo").click(PreDocumento.apriModaleCapitolo);
        Capitolo.inizializza("UscitaGestione");
    
        // Impegno
        $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleImpegno);
        Impegno.inizializza();
    
        // Soggetto
        $("#pulsanteCompilazioneSoggetto").click(PreDocumento.apriModaleSoggetto);
        campoCodiceSoggetto.on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento)
            .change(function(e, params) {
            	var alertErrori = $('#ERRORI');
            	    var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione,undefined,undefined, undefined,true);
            	    if(!params || !params.doNotCloseAlerts) {
            	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
                			impostaDatiNegliAlert(errori, alertErrori);
                			return errori;
                		});
            	    }
            		
                })
                // Forzo la chiamata al metodo
                .trigger('change', {doNotCloseAlerts: true});
        accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
            var tabella = accordionModalitaPagamento.find("table");
            var uid = $(this).filter(":checked").val();
            Soggetto.filterModalitaPagamentoSoggetto(uid, tabella);
        });
    
        PreDocumento.precaricaSedeSecondariaEModalitaPagamento("aggiornaPreDocumentoSpesa_ottieniListaSedeSecondariaEModalitaPagamento.do");
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");
    
        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);
    
        // CR-2543
        $("#modale_hidden_tipoProvvisorioDiCassa").val("S");
        ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassa", "", "#annoProvvisorioCassa", "#numeroProvvisorioCassa");
    
        $("form").on("reset", function() {
            // NATIVO
            this.reset();
        });
    });
}(jQuery);