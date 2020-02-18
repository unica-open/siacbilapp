/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* **** Gestione dei classificatori **** */

Variazioni.Classificatori = (function() {
    var exports = {};

    /** Il tipo di azione: inserimento o aggiornamento */
    exports.tipoAzione = undefined;

    /**
     * Carica il classificatore a partire dalla chiamata AJAX.
     *
     * @param classificatore    {String} il classificatore da cercare
     * @param idChiamante       {Number} l'id del classificatore chiamante
     * @param urlAJAX           {String} l'URL da chiamare via AJAX
     * @param idComboAttuale    {String} l'id della combo da popolare
     * @param idComboSuccessiva {String} l'id della combo successiva [OPZIONALE]
     */
    function caricaClassificatore(classificatore, idChiamante, urlAJAX, idComboAttuale, /* Optional */idComboSuccessiva) {
        // Pulisco la combo successiva
        var comboSuccessiva = $("#" + idComboSuccessiva);
        var oggettoPerChiamataAjax = {};
        comboSuccessiva.length > 0 && comboSuccessiva.val("");

        oggettoPerChiamataAjax.id = idChiamante;
        oggettoPerChiamataAjax["daInjettare" + exports.tipoAzione + "Variazione"] = true;

        $.postJSON(
            urlAJAX,
            oggettoPerChiamataAjax,
            function(data) {
                var listaRisultati = data[classificatore];
                var comboAttuale = $("#" + idComboAttuale);

                // Cancello le options presenti
                comboAttuale.find("option").remove().end();
                // Se la combo è disabilitata, tolgo l'attributo
                if(comboAttuale.attr("disabled") === "disabled") {
                    comboAttuale.removeAttr("disabled");
                }

                // Se la combo successiva esiste ed è modificabile, la disabilito
                if (comboSuccessiva.length > 0 && comboSuccessiva.attr("disabled") !== "disabled") {
                    comboSuccessiva.attr("disabled", "disabled");
                }

                comboAttuale.append($("<option />"));
                $.each(listaRisultati, function (idx, value) {
                    comboAttuale.append($("<option />").val(value.uid).text(value.codice + '-' + value.descrizione));
                    }
                );
            }
        );
    }

    /**
     * Carica il Programma.
     */
    exports.caricaProgramma = function() {
        caricaClassificatore("listaProgramma", $("#missioneCapitoloUscita").val(), "ajax/programmaAjax.do",
            "programmaCapitoloUscita", "classificazioneCofogCapitoloUscita");
    };

    /**
     * Carica la Classificazione Cofog.
     */
    exports.caricaClassificazioneCofog = function() {
        caricaClassificatore("listaClassificazioneCofog", $("#programmaCapitoloUscita").val(), "ajax/cofogAjax.do",
            "classificazioneCofogCapitoloUscita");
    };

    /**
     * Carica il Macroaggregato.
     */
    exports.caricaMacroaggregato = function() {
        caricaClassificatore("listaMacroaggregato", $("#titoloSpesaCapitoloUscita").val(), "ajax/macroaggregatoAjax.do",
            "macroaggregatoCapitoloUscita");
    };

    /**
     * Carica la Tipologia Titolo.
     */
    exports.caricaTipologiaTitolo = function() {
        caricaClassificatore("listaTipologiaTitolo", $("#titoloEntrataCapitoloEntrata").val(), "ajax/tipologiaTitoloAjax.do",
            "tipologiaTitoloCapitoloEntrata", "categoriaTipologiaTitoloCapitoloEntrata");
    };

    /**
     * Carica la Categoria Tipologia Titolo.
     */
    exports.caricaCategoriaTipologiaTitolo = function() {
        caricaClassificatore("listaCategoriaTipologiaTitolo", $("#tipologiaTitoloCapitoloEntrata").val(),
            "ajax/categoriaTipologiaTitoloAjax.do", "categoriaTipologiaTitoloCapitoloEntrata");
    };

    return exports;
}());

$(
    function() {
        // Carico una volta sola il tipo di azione: inserimento o aggiornamento
        Variazioni.Classificatori.tipoAzione = $("#tipoAzione").val();
    }
);