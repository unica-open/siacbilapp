/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';
    var $document = $(document);
    var first = true;
    var originalCausale = $("#causaleEntrata").val();

    $(init);

    /**
     * Callback al click del pulsante conferma della modale di richiesta conferma per adeguamento disponibilita' importo.
     */
    function confermaAdeguamentoDisponibilitaAccertamento(){
        // aggiungo il parametro che permette di sfondare la competenza
        $('input[name="forzaDisponibilitaAccertamento"]').val("true");
        // forzo il submit
        $('#formInserimentoPreDocumentoEntrata').submit();
    }

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
        first = false;
        PreDocumento.caricaListaCausaleEntrata(caus);
    }

    /**
     * Copia i dati della data di competenza nei dati della data di esecuzione
     */
    function copiaCompetenzaInEsecuzione() {
        $('#dataDocumentoPreDocumento').val($('#dataCompetenzaPreDocumento').val());
    }
    
    function applyChangeOnSelects() {
        $('select').change();
    }

    function init() {
        var inputRichiestaConferma = $('#HIDDEN_richiediConfermaUtente');

        var typeaheads = $("input[data-typeahead]");
        var idProvvedimento;
        var richiestaProsecuzione = false;

        // Inizializzo i typeahead
        PreDocumento.impostaTypeahead(typeaheads);
        PreDocumento.calculateProvincia(typeaheads);

        $("#dataCompetenzaPreDocumento").on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
        $("#tipoCausale").change(PreDocumento.caricaListaCausaleEntrataNonAnnullate(true));
        $("#causaleEntrata").change(PreDocumento.impostaDatiCausaleEntrata);

        $document.on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
            .on("selezionataStrutturaAmministrativaContabile", handleSelezioneStrutturaAmministrativoContabile);

        // Capitolo
        $("#pulsanteCompilazioneGuidataCapitolo").click(PreDocumento.apriModaleCapitolo);
        Capitolo.inizializza("EntrataGestione");

        // Impegno
        $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleAccertamento);
        Accertamento.inizializza();

        // Soggetto
        $("#pulsanteCompilazioneSoggetto").click(PreDocumento.apriModaleSoggetto);
        $("#codiceSoggettoSoggetto").on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento);

        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);
        
        // CR-2543
        $("#modale_hidden_tipoProvvisorioDiCassa").val("E");
        ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassa", "", "#annoProvvisorioCassa", "#numeroProvvisorioCassa");
        
        $("form").on("reset", PreDocumento.puliziaReset);

        //CR-4310
        if(inputRichiestaConferma.length>0 && $('input[name="forzaDisponibilitaAccertamento"]').val() === "false"){
            // SIAC-5318
            richiestaProsecuzione = true;
            impostaRichiestaConfermaUtente(inputRichiestaConferma.data('messaggio-conferma'), confermaAdeguamentoDisponibilitaAccertamento, applyChangeOnSelects);
        }
        $document.on('accertamentoCaricato', function(){
            //TODO: valutare se metterlo anche al change di anno e numero
            //Ho caricato un nuovo accertamento
            $('input[name="forzaDisponibilitaAccertamento"]').val("false");
        });
        // SIAC-4623
        // SIAC-5318: la richiesta di prosecuzione deve impedire il ricaricamento delle select
        autoselectCombo('select', richiestaProsecuzione);
        $('#dataCompetenzaPreDocumento').change(copiaCompetenzaInEsecuzione);

    }

}(jQuery));
