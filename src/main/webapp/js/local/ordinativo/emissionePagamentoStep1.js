/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var divRicercaQuote = $("#datiRicercaQuote");
    var radioTipoEmissione = $("input[name='tipoEmissioneOrdinativo']");

    /**
     * Gestione del tipo di emissione per l'ordinativo.
     */
    function gestioneTipoEmissione() {
        var value = $(this).val();
        divRicercaQuote[value === "QUOTE" ? "slideDown" : "slideUp"]();
    }

    /**
     * Gestione del submit del form.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     *
     * @returns <code>true</code> se il form possa essere submittato; <code>false</code> in caso contrario
     */
    function gestioneSubmit(e) {
        var checkedRadio = radioTipoEmissione.filter(":checked");
        var action;

        if(!checkedRadio.length) {
            // Non ho ancora selezionato nulla. Esco e blocco il submit
            impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: Tipo emissione"], alertErrori);
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        // Ho il tipo. Determino l'azione e la eseguo
        action = checkedRadio.data("action");
        $(this).attr("action", action);
        return true;
    }

    $(function() {
        radioTipoEmissione.change(gestioneTipoEmissione);
        $("form").substituteHandler("submit", gestioneSubmit);

        // Inizializzazione delle funzionalita' della ricerca provvedimento
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
        // Inizializzazione delle funzionalita' della ricerca capitolo
        Capitolo.inizializza('UscitaGestione', "#annoCapitolo", "#numeroCapitolo", "#numeroArticolo", "#numeroUEB", "#datiRiferimentoCapitoloSpan",
            "#pulsanteCompilazioneGuidataCapitolo", "");
        // Inizializzazione delle funzionalita' della ricerca soggetto
        Soggetto.inizializza("#codiceSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_soggettoDenominazione", "#descrizioneCompletaSoggetto",
            "#pulsanteAperturaCompilazioneGuidataSoggetto");
        // Inizializzazione delle funzionalita' della ricerca elenco
        ElencoDocumentiAllegato.inizializza("#pulsanteApriModaleCompilazioneGuidataElencoDocumentiAllegato", "", "#annoElencoDocumentiAllegato",
            "#numeroElencoDa, #numeroElencoA", "#descrizioneCompletaElenco");
    });
}(jQuery);