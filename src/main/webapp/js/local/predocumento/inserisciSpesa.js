/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    var first = true;
    var originalCausale = $("#causaleSpesa").val();
    // Campi soggetto
    var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
    var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
    var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
    var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
    $(init);

    /**
     * Gestione della selezione della SAC
     * @param e (event) l'evento
     */
    function handleSelezioneStrutturaAmministrativoContabile(e) {
        var caus;
        if(e.treeId === "treeStruttAmmAttoAmministrativo") {
            // Non e' un evento che mi interessi
            return;
        }

        caus = first ? originalCausale : undefined;
        PreDocumento.caricaListaCausaleSpesa(true, e.isFirst)(caus);
        first = false;
    }

    /**
     * Gestione del cambio di codice soggetto
     * @param e      (event) l'evento
     * @param params (any)   i parametri aggiuntivi
     */
    function handleChangeCodiceSoggetto(e, params) {
        var alertErrori = $('#ERRORI');
        var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione,undefined,undefined, undefined,true);
        if(!params || !params.doNotCloseAlerts) {
            def.then(alertErrori.slideUp.bind(alertErrori), function(errori){
                impostaDatiNegliAlert(errori, alertErrori);
                return errori;
            });
        }
    }

    /**
     * Gestione del cambio di sede secondaria
     */
    function handleChangeSedeSecondaria() {
        var tabella = accordionModalitaPagamento.find("table");
        var uid = $(this).filter(":checked").val();
        Soggetto.filterModalitaPagamentoSoggetto(uid, tabella);
    }

    /**
     * Inizializzazione
     */
    function init() {

        var typeaheads = $("input[data-typeahead]");
        var idProvvedimento;

        // Inizializzo i typeahead
        PreDocumento.impostaTypeahead(typeaheads);
        PreDocumento.calculateProvincia(typeaheads);

        $("#dataCompetenzaPreDocumento").on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
        $("#tipoCausale").change(PreDocumento.caricaListaCausaleSpesa(true, false));
        $("#causaleSpesa").change(PreDocumento.impostaDatiCausaleSpesa);

        $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
            .on("selezionataStrutturaAmministrativaContabile", handleSelezioneStrutturaAmministrativoContabile);

        // Capitolo
        $("#pulsanteCompilazioneGuidataCapitolo").click(PreDocumento.apriModaleCapitolo);
        Capitolo.inizializza("UscitaGestione");

        // Impegno
        $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleImpegno);
        Impegno.inizializza();

        // Soggetto
        $("#pulsanteCompilazioneSoggetto").click(PreDocumento.apriModaleSoggetto);
        campoCodiceSoggetto.on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento)
            .change(handleChangeCodiceSoggetto)
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});
        accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", handleChangeSedeSecondaria);

        PreDocumento.precaricaSedeSecondariaEModalitaPagamento("inserisciPreDocumentoSpesa_ottieniListaSedeSecondariaEModalitaPagamento.do");
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

        // CR-2543
        $("#modale_hidden_tipoProvvisorioDiCassa").val("S");
        ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassa", "", "#annoProvvisorioCassa", "#numeroProvvisorioCassa");

        $("form").on("reset", PreDocumento.puliziaReset);

        // SIAC-4623
        autoselectCombo('#tipoCausale');
        autoselectCombo('#contoTesoreria');
        autoselectCombo('#nazioneIndirizzoDatiAnagraficiPreDocumentoSpesaPreDocumento');
        autoselectCombo('#nazioneNascitaDatiAnagraficiPreDocumentoSpesaPreDocumento');
        autoselectCombo('#tipoAtto');
        autoselectCombo('#tipoFin');
    }
}(jQuery);