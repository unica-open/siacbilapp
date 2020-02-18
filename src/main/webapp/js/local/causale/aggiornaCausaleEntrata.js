/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function() {
    var idProvvedimento;

    $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili);

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

    $("form").on("reset", function() {
        // NATIVO
        this.reset();
    });
});