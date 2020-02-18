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
        // Seleziono dallo zTree
        ZTreePreDocumento.selezionaNodoSeApplicabile(tree.innerZTree, $("#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid").val());
        // Apro il modale
        $("#modaleGuidaProvvedimento").modal("show");
    }
    
    /**
     * - se si seleziona Atto automatico, il sistema visualizza della sezione provvedimento 
     * solo la sac ed ai fini dell'inserimento considera l'anno provvedimento = anno di bilancio
	 *- si si seleziona Atto da sistema esterno, il sistama visualizza la sezione provvedimento 
	 * così come fatta oggi (Anno - numero - tipo - sac) e non permette più di indicare solo l'anno 
	 * ai fini dell'inserimento automatico. 
     */
    //CR 2704
    function gestisciDefinizioneAtto() {
    	var $this =  $("input[name='attoAutomatico']").filter(':checked');
        var automatico = $this.val();
        if(automatico === "true"){
        	$("#datiProvvedimento").addClass("hide");
        	$("#annoAttoAmministrativo").val("");
        	$("#numeroAttoAmministrativo").val("");
        	$("#tipoAtto").val(0);
        	
        }else{
        	$("#datiProvvedimento").removeClass("hide");
        }
    }

    $(function() {
        // Attivo le funzionalita'
        $("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);
        // Inizializzo il modale
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
        gestisciDefinizioneAtto();
        $("input[name='attoAutomatico']").on("change", gestisciDefinizioneAtto);
    });
} (jQuery));
