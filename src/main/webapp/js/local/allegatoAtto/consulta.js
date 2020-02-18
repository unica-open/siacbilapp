/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    var isConvalidato = $('#HIDDEN_allegatoAttoConvalidato').val() === 'true';
    var colonnaAttoAmministrativo = {aTargets: [6], mData: defaultPerDataTable('domStringAttoAmministrativoMovimento')};
    var colonnaOrdinativo = {aTargets: [10], mData: defaultPerDataTable('domStringOrdinativo')};
    // SIAC-5171
    var colonnaLiquidazione = {aTargets: [9], mData: defaultPerDataTable('domStringLiquidazione')};
    var colonnaOrdinativoReduced = {aTargets: [10], mData: defaultPerDataTable('domStringOrdinativoReduced')};
    var colonnaStatoOrdinativo = {aTargets: [11], mData: defaultPerDataTable('domStringStatoOrdinativo')};
    var colonnaImporto = {aTargets: [12], mData: defaultPerDataTable('importoInAtto', 0, formatMoney), fnCreatedCell: tabRight};

    $(init);

    /**
     * Formattazione degli importi
     * @param value (any) il valore da formattare
     * @return (string) il valore formattato
     */
    function formatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return '';
    }
    /**
     * Aggiunge la classe tabRight all'elemento
     * @param nTd (Node) il nodo su cui impostare la classe
     * @return (Node) il nodo
     */
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
        return nTd;
    }

    /**
     * Gestione del change sull'elenco documenti
     * @param e (Event) l'evento scatenante
     */
    function handleChangeElencoDocumento(e) {
        var evt = $.Event("elencoSelected");
        var value = $(e.target).val();
        evt.originalEvent = e;
        evt.elenco = {uid: value};
        $(this).trigger(evt);
    }

    /**
     * Inizializzazione
     */
    function init() {
        var substitutionObject = {'6': colonnaAttoAmministrativo};
        var addArray = [];

        if(isConvalidato) {
            substitutionObject['9'] = colonnaLiquidazione;
            substitutionObject['10'] = colonnaOrdinativoReduced;
            substitutionObject['11'] = colonnaStatoOrdinativo;
            substitutionObject['12'] = colonnaImporto;
        } else {
            addArray = [colonnaOrdinativo];
        }
        $("#tabellaElencoDocumentiAllegato").on("change", ".checkboxElencoDocumentiAllegato", handleChangeElencoDocumento);

        // Lego l'azione relativa al caricamento del dettaglio dell'elenco
        $(document).on("elencoSelected", "#tabellaElencoDocumentiAllegato", function(e) {
            ElencoDocumentiAllegato.caricaDettaglioElenco(e.elenco, "ricercaDettaglioElencoDocumentiAllegato.do", "#tabellaDettaglioElementiCollegati",
                "#divElenchi", "#dettaglioElementiCollegati", addArray, substitutionObject);
        });
    }
}(jQuery);