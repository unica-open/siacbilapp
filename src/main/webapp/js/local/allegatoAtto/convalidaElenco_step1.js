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

    $(function() {
        // Attivo le funzionalita'
        $("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);
        // Inizializzo il modale
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
    });
} (jQuery));
