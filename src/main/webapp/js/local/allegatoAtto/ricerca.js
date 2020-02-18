/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    "use strict";

    /**
     * Apre il modale dell'atto amministrativo copiando i dati forniti dall'utente.
     */
    function apriModaleAttoAmministrativo() {
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm_modale");

        $("#annoProvvedimento_modale").val($("#annoAttoAmministrativo").val());
        $("#numeroProvvedimento_modale").val($("#numeroAttoAmministrativo").val());
        $("#tipoAttoProvvedimento_modale").val($("#tipoAtto").val());

        ZTreePreDocumento.selezionaNodoSeApplicabile(tree.innerZTree, $("#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid").val());

        $("#modaleGuidaProvvedimento").modal("show");
    }

    
  
    function apriModaleImpegno() {
        $('#annoImpegnoModale').val($('#annoMovimentoMovimentoGestione').val());
        $('#numeroImpegnoModale').val($('#numeroMovimentoGestione').val());
        $('#modaleImpegno').modal('show');
    }


  /*
    $("#pulsanteCompilazioneGuidataMovimentoGestione").click(PreDocumento.apriModaleImpegno);
    Impegno.inizializza();
  */  

    $(function() {
    	var idSoggetto;
        // Attivo le funzionalita'
        $("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);

        // Inizializzo il modale
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
                "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
                "AttoAmministrativo", undefined, true);
        
        ElencoDocumentiAllegato.inizializza("#pulsanteApriModaleCompilazioneGuidataElencoDocumentiAllegato", "",
                "#annoElencoDocumentiAllegato", "#numeroElencoDocumentiAllegato", "#descrizioneCompletaElenco", false, false, undefined);
        
        
	    $("#pulsanteCompilazioneGuidataMovimentoGestione").click(apriModaleImpegno);
	    Impegno.inizializza();

	    idSoggetto = Soggetto.inizializza("#codiceSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_soggettoDenominazione", "#descrizioneCompletaSoggetto", "#pulsanteApriModaleSoggetto");
        Soggetto.bindCaricaDettaglioSoggetto(idSoggetto, true);
        preSelezionaSeUnicaOpzione($('#statoOperativoAllegatoAttoAllegatoAtto'));
    });
} (jQuery));
