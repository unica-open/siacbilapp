/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var PreDocumentoEntrata = (function($) {

    var exports = {};

    /**
     * Gestisce la selezione del checkbox del mancante sulla Causale Entrata.
     */
    exports.gestioneMancanteCausaleEntrata = function() {
        var select = $("#causaleEntrata");
        var optsNum = select.find("option").length;
        ($(this).attr("checked") || optsNum === 1) ? select.attr("disabled", true) : select.removeAttr("disabled");
    };

    /**
     * Gestisce la selezione del checkbox del mancante sul Conto Corrente.
     */
    exports.gestioneMancanteContoCorrente = function() {
        var select = $("#contoCorrente");
        $(this).attr("checked") ? select.attr("disabled", true) : select.removeAttr("disabled");
    };

    /**
     * Gestisce la selezione del checkbox del mancante sul Soggetto.
     */
    exports.gestioneMancanteSoggetto = function() {
        var divs = $("#div_flagSoggettoMancante");
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

    var originalCausale = $("#causaleEntrata").val();
    var first = true;
    var idProvvedimento;

    $("#dataCompetenzaPreDocumento").on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
    $("#tipoCausale").change(PreDocumento.caricaListaCausaleEntrata);

    $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
        .on("selezionataStrutturaAmministrativaContabile", function() {
            var caus = first ? originalCausale : undefined;
            first = false;
            PreDocumento.caricaListaCausaleEntrata(caus);
        });

    // Capitolo
    $("#pulsanteCompilazioneGuidataCapitolo").click(PreDocumento.apriModaleCapitolo);
    Capitolo.inizializza("EntrataGestione");

    // Impegno
    $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleAccertamento);
    Accertamento.inizializza();

    // Soggetto
    $("#pulsanteCompilazioneSoggetto").click(PreDocumento.apriModaleSoggetto);

    Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

    // Atto Amministrativo
    idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
    Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

    $("#flagCausaleEntrataMancante").on("controlloFlag", PreDocumentoEntrata.gestioneMancanteCausaleEntrata);
    $("#flagContoCorrenteMancante").on("controlloFlag", PreDocumentoEntrata.gestioneMancanteContoCorrente);
    $("#flagSoggettoMancante").on("controlloFlag", PreDocumentoEntrata.gestioneMancanteSoggetto);
    $("#flagAttoAmministrativoMancante").on("controlloFlag", PreDocumentoEntrata.gestioneMancanteAttoAmministrativo);

    $("input[type='checkbox'][name^='flag']").on("click", function(e) {
        var event = $.Event("controlloFlag");
        event.originalEvent = e;
        $(this).trigger(event);
    }).trigger("controlloFlag");

    $("form").on("reset", PreDocumento.puliziaReset);
});