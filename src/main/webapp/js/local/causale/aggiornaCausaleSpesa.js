/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function() {

    // Campi soggetto
    var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
    var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
    var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
    var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
    var idProvvedimento;

    $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili);

    // Capitolo
    $("#pulsanteCompilazioneGuidataCapitolo").click(PreDocumento.apriModaleCapitolo);
    Capitolo.inizializza("UscitaGestione");

    // Impegno
    $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleImpegno);
    Impegno.inizializza();

    // Soggetto
    $("#pulsanteCompilazioneSoggetto").click(PreDocumento.apriModaleSoggetto);
    campoCodiceSoggetto.on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento)
    .change(function(e, doNotCloseAlert) {
    	var alertErrori = $('#ERRORI');
        var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione, undefined,undefined, undefined, true);        	
        if(!doNotCloseAlert){
        	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){ 
            	impostaDatiNegliAlert(errori, alertErrori);
            	return errori;
            });
        }
    })
    // Forzo la chiamata al metodo
	.trigger("change", true);
    accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
        var tabella = accordionModalitaPagamento.find("table");
        var uid = $(this).filter(":checked").val();
        Soggetto.filterModalitaPagamentoSoggetto(uid, tabella);
    });

    PreDocumento.precaricaSedeSecondariaEModalitaPagamento("aggiornaCausaleSpesa_ottieniListaSedeSecondariaEModalitaPagamento.do");
    Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

    // Atto Amministrativo
    idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
    Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

    $("form").on("reset", function() {
        // NATIVO
        this.reset();
    });
});