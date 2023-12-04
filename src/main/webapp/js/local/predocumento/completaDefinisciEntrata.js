/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    var originalCausale = $('#causaleEntrata').val();
    var first = true;
    var $document = $(document);
    var $sacUid = $('#HIDDEN_StrutturaAmministrativoContabileUidAttoAmministrativo');
    var $alertErrori = $('#ERRORI');
    
    $(init);
    
    /**
     * Caricamento dei dati per la modifica della SAC
     */
    function loadDataPerSAC(e) {
        var caus = first ? originalCausale : undefined;
        if(e.treeId === "treeStruttAmmAttoAmministrativo") {
            // Non e' un evento che mi interessi
            return;
        }

        first = false;
        PreDocumento.caricaListaCausaleEntrata(caus);
    }
    
    /**
     * Caricamento delle SAC
     * @param e (Event) l'evento scatenante
     */
    function caricaSAC(e) {
        PreDocumento.caricaStruttureAmministrativoContabili(e);
        Ztree.selezionaNodoSeApplicabile('treeStruttAmmAttoAmministrativo', $sacUid.val());
    }
    
    /**
     * Caricamento dei totali
     */
    function caricaTotali() {
        var $fieldsetRicerca = $('#fieldsetRicercaPredisposizione');
        var obj = $fieldsetRicerca.serializeObject();
        var $divTotali = $('#totaliElencoPredocumenti');
        //var $overlayDivs = $('#fieldsetRicercaPredisposizione, #totaliElencoPredocumenti');
        $alertErrori.slideUp();
        $divTotali.find(':input').val('');
        
        return $divTotali.slideUp()
            .promise()
            .then($fieldsetRicerca.overlay.bind($fieldsetRicerca, 'show'))
            .then($.postJSON.bind(undefined, 'completaDefinisciPreDocumentoEntrata_cercaTotali.do', obj))
            .then(function(data) {
               if(impostaDatiNegliAlert(data.errori, $alertErrori)) {
                   return $.Deferred().reject().promise();
               }
               // Impostazione dati nei campi
               $('#numeroPredisposizioniTotale').val(data.numeroPreDocumentiTotale);
               $('#numeroPredisposizioniIncomplete').val(data.numeroPreDocumentiIncompleti);
               $('#numeroPredisposizioniAnnullateDefinite').val(data.numeroPreDocumentiAnnullatiDefiniti);
               $('#numeroPredisposizioniComplete').val(data.numeroPreDocumentiCompleti);
               
               $('#importoPredisposizioniTotale').val(formatMoney(data.importoPreDocumentiTotale));
               $('#importoPredisposizioniIncomplete').val(formatMoney(data.importoPreDocumentiIncompleti));
               $('#importoPredisposizioniAnnullateDefinite').val(formatMoney(data.importoPreDocumentiAnnullatiDefiniti));
               $('#importoPredisposizioniComplete').val(formatMoney(data.importoPreDocumentiCompleti));

               //SIAC-6780
               $('#numeroPredisposizioniNoCassaTotale').val(data.numeroPreDocumentiNoCassaTotale);
               $('#numeroPredisposizioniNoCassaIncomplete').val(data.numeroPreDocumentiNoCassaIncompleti);
               $('#numeroPredisposizioniNoCassaAnnullateDefinite').val(data.numeroPreDocumentiNoCassaAnnullatiDefiniti);
               $('#numeroPredisposizioniNoCassaComplete').val(data.numeroPreDocumentiNoCassaCompleti);
               
               $('#importoPredisposizioniNoCassaTotale').val(formatMoney(data.importoPreDocumentiNoCassaTotale));
               $('#importoPredisposizioniNoCassaIncomplete').val(formatMoney(data.importoPreDocumentiNoCassaIncompleti));
               $('#importoPredisposizioniNoCassaAnnullateDefinite').val(formatMoney(data.importoPreDocumentiNoCassaAnnullatiDefiniti));
               $('#importoPredisposizioniNoCassaComplete').val(formatMoney(data.importoPreDocumentiNoCassaCompleti));
               //
               $divTotali.slideDown()
           })
           .always($fieldsetRicerca.overlay.bind($fieldsetRicerca, 'hide'));
    }

    function changeFormAction(){
        $('#completaDefinisciForm').attr('action','completaDefinisciPreDocumentoEntrata_completaDefinisci.do');
        $('#completaDefinisciForm').submit();
    }
    
    // Implementazione
    function init() {
        var idProvvedimento;
        var idSoggetto;

        $('#tipoCausale').substituteHandler('change', PreDocumento.caricaListaCausaleEntrata);

        $document.substituteHandler('struttureAmministrativeCaricate', caricaSAC);
        $document.substituteHandler('selezionataStrutturaAmministrativaContabile', loadDataPerSAC);

        $('#pulsanteCercaTotali').substituteHandler('click', caricaTotali);

        // Impegno
        $('#pulsanteCompilazioneGuidataMovimentoGestione').substituteHandler('click', PreDocumento.apriModaleAccertamento);
        Accertamento.inizializza();

        // Soggetto
        idSoggetto = Soggetto.inizializza('#codiceSoggettoSoggetto', '#HIDDEN_codiceFiscaleSoggetto', '#HIDDEN_denominazioneSoggetto', '#datiRiferimentoSoggettoSpan', '#pulsanteCompilazioneSoggetto');
        Soggetto.bindCaricaDettaglioSoggetto(idSoggetto, false);

        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

        // Provvisorio
        $('#HIDDEN_TipoProvvisorioCassa').val('E');
        ProvvisorioDiCassa.inizializzazione('#pulsanteCompilazioneGuidataProvvisorioCassa','#HIDEEN_TipoProvvisorioCassa','#annoProvvisorioCassa','#numeroProvvisorioCassa','#HIDDEN_CausaleProvvisorioCassa');
        $('#pulsanteCompilazioneGuidataProvvisorioCassa').substituteHandler('click', PreDocumento.apriModaleProvvisorio);

        // Workaround per Struts2
        if($sacUid.val() === '') {
            $sacUid.val(0);
        }

        //SIAC-6780
        $('form').on('reset', PreDocumento.puliziaReset);
    }
}(jQuery);