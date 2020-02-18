/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var PreDocumentoSpesa = (function($) {

    var exports = {};

    /**
     * Gestisce la selezione del checkbox del mancante sulla Causale Spesa.
     */
    exports.gestioneMancanteCausaleSpesa = function() {
        var select = $("#causaleSpesa");
        var optsNum = select.find("option").length;
        ($(this).attr("checked") || optsNum === 1) ? select.attr("disabled", true) : select.removeAttr("disabled");
    };

    /**
     * Gestisce la selezione del checkbox del mancante sul Conto Tesoreria.
     */
    exports.gestioneMancanteContoTesoreria = function() {
        var select = $("#contoTesoreria");
        $(this).attr("checked") ? select.attr("disabled", true) : select.removeAttr("disabled");
    };

    /**
     * Gestisce la selezione del checkbox del mancante sul Soggetto.
     */
    exports.gestioneMancanteSoggetto = function() {
        var divs = $("#div_flagSoggettoMancante, #accordionSedeSecondariaSoggetto, #accordionModalitaPagamentoSoggetto");
        $(this).attr("checked") ? divs.slideUp() : divs.slideDown();
    };

    /**
     * Gestisce la selezione del checkbox del mancante sull'Atto Amministrativo.
     */
    exports.gestioneMancanteAttoAmministrativo = function() {
        var div = $("#div_flagAttoAmministrativoMancante");
        $(this).attr("checked") ? div.slideUp() : div.slideDown();
    };

    return exports;
}(jQuery));



$(function() {

    var originalCausale = $("#causaleSpesa").val();
    var first = true;
    var idProvvedimento;

    $("#dataCompetenzaPreDocumento").on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
    $("#tipoCausale").change(PreDocumento.caricaListaCausaleSpesa(false, false));

    $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
        .on("selezionataStrutturaAmministrativaContabile", function(e) {
            var caus = first ? originalCausale : undefined;
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
    $("#codiceSoggettoSoggetto").on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento);

    PreDocumento.precaricaSedeSecondariaEModalitaPagamento("ricercaPreDocumentoSpesa_ottieniListaSedeSecondariaEModalitaPagamento.do");
    Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

    // Atto Amministrativo
    idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
    Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

    $("#flagCausaleSpesaMancante").on("controlloFlag", PreDocumentoSpesa.gestioneMancanteCausaleSpesa);
    $("#flagContoTesoreriaMancante").on("controlloFlag", PreDocumentoSpesa.gestioneMancanteContoTesoreria);
    $("#flagSoggettoMancante").on("controlloFlag", PreDocumentoSpesa.gestioneMancanteSoggetto);
    $("#flagAttoAmministrativoMancante").on("controlloFlag", PreDocumentoSpesa.gestioneMancanteAttoAmministrativo);

    $("input[type='checkbox'][name^='flag']").on("click", function(e) {
        var event = $.Event("controlloFlag");
        event.originalEvent = e;
        $(this).trigger(event);
    }).trigger("controlloFlag");

    $("form").on("reset", PreDocumento.puliziaReset);
});