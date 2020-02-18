/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';

    /**
     * Callback al click del pulsante conferma della modale di richiesta conferma per adeguamento disponibilita' importo.
     **/
    function confermaAdeguamentoDisponibilitaAccertamento(){
        //aggiungo il parametro che permette di sfondare la competenza
        $('input[name="forzaDisponibilitaAccertamento"]').val("true");
        //forzo il submit
        $('#formAggiornamentoPreDocumentoEntrata').submit();
    }
    
    $(function() {

        var originalCausale = $("#originalCausale").val();
        var first = true;
        var isDecentrato = $('#utenteDecentrato').val() === 'true';

        var typeaheads = $("input[data-typeahead]");
        var idProvvedimento;

        // Inizializzo i typeahead
        PreDocumento.impostaTypeahead(typeaheads);
        PreDocumento.calculateProvincia(typeaheads);

        $("#dataCompetenzaPreDocumento").not('[disabled]').on("click changeDate", PreDocumento.impostaPeriodoCompetenza);
        $("#tipoCausale").not('[disabled]').change(PreDocumento.caricaListaCausaleEntrataNonAnnullate(false));
        $("#causaleEntrata").not('[disabled]').change(PreDocumento.impostaDatiCausaleEntrata);

        $(document).on("struttureAmministrativeCaricate", PreDocumento.caricaStruttureAmministrativoContabili)
            .on("selezionataStrutturaAmministrativaContabile", function(e) {
                var caus;
                if(e.treeId === "treeStruttAmmAttoAmministrativo") {
                    // Non e' un evento che mi interessi
                    return;
                }

                caus = first ? originalCausale : undefined;
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
        $("#codiceSoggettoSoggetto").on("codiceSoggettoCaricato", PreDocumento.caricaSedeSecondariaEModalitaPagamento);

        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

        // SIAC-4382
        if(!isDecentrato) {
            // CR-2543
            $("#modale_hidden_tipoProvvisorioDiCassa").val("E");
            ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassa", "", "#annoProvvisorioCassa", "#numeroProvvisorioCassa");
        }

        $("form").on("reset", function() {
            // NATIVO
            this.reset();
        });
        
        //CR-4310
        var inputRichiestaConferma = $('#HIDDEN_richiediConfermaUtente');
        if(inputRichiestaConferma.length > 0 && $('input[name="forzaDisponibilitaAccertamento"]').val() === "false"){
            impostaRichiestaConfermaUtente(inputRichiestaConferma.data('messaggio-conferma'), confermaAdeguamentoDisponibilitaAccertamento);
        }
        $(document).on('accertamentoCaricato', function(){
            //TODO: valutare se metterlo anche al change di anno e numero
            //Ho caricato un nuovo accertamento
            $('input[name="forzaDisponibilitaAccertamento"]').val("false");
        });

    });
}(jQuery);