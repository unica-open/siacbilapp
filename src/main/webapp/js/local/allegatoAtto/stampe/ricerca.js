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
    
    /**
     * Gestisce il label di anno e atto amministrativo a seconda se sono o meno valorizzati
     */
    function gestioneLabelAnnoNumeroAtto(event) {
    	var anno = $("#annoAttoAmministrativo").val();
    	var numero = $("#numeroAttoAmministrativo").val();
    	var valorizzato = anno || numero;
    	if(valorizzato){
    		$("label[for='annoAttoAmministrativo']").html("Anno *");
    		$("label[for='numeroAttoAmministrativo']").html("Numero *");
    	}else{
    		$("label[for='annoAttoAmministrativo']").html("Anno");
    		$("label[for='numeroAttoAmministrativo']").html("Numero ");
    	}
    	
    }
    
    
    /**
     * Gestisce l'apertura dei div delle stampe a seconda della selezione operata sul tipo
     */
    function gestioneAperturaDivStampa(event) {

        var $this = $("#tipoStampaAllegatoAtto");
        var tipoSelezionato = $this.val();
        var divCampi = $("#campiStampaAllegatoAtto");
        var selected;

        if(event) {
            $("#ERRORI").slideUp();
        }

        // Se non ho scelto nulla, nascondo il div ed esco
        if(!tipoSelezionato) {
            divCampi.fadeOut();
            return;
        }        
        divCampi.children("div")
                .not("div[data-" + selected + "]")
                    .fadeOut()
                .invert()
                    .fadeIn()
                    .end()
                .end()
            .fadeIn();
        $("#annoAttoAmministrativo").change(gestioneLabelAnnoNumeroAtto);
        $("#numeroAttoAmministrativo").change(gestioneLabelAnnoNumeroAtto);
    }
    
    $(function() { 
    	$("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);
    	$("#tipoStampaAllegatoAtto").change(gestioneAperturaDivStampa);
    	gestioneAperturaDivStampa();
    	Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
    
    });

} (jQuery));