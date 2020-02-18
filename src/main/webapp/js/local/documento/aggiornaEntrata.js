/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($) {
    var exports = {};
    var _oldImpostaDatiNegliAlert = impostaDatiNegliAlert;

    /**
     * Sovrscrive i dati originali presenti nei campi di input e riprova l'invocazione dell'evento.
     *
     * @param anchor         (jQuery) l'ancora su cui ichiamare l'evento
     * @param inputContainer (jQuery) gli input presenti nella pagina
     * @param modale         (jQuery) il modale da chiudere
     */
    function sovrascriviModificheERiprova(anchor, inputContainer, modale) {
        // Sovrascrivo il valore originale
        inputContainer.keepOriginalValues();
        modale.modal("hide");
        anchor.trigger("click");
    }

    /**
     * Apre il modale per la conferma del cambiamento di tab.
     *
     * @param anchor         (jQuery) l'ancora su cui ichiamare l'evento
     * @param inputContainer (jQuery) gli input presenti nella pagina
     *
     * @returns (jQuery) il modale aperto
     */
    function apriModaleConfermaCambioTab(anchor, inputContainer) {
        var modale = $("#modaleConfermaProsecuzione");
        return modale
            .find("#modaleConfermaPulsanteSi")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    sovrascriviModificheERiprova(anchor, inputContainer, modale);
                })
                .end()
            .find("#modaleConfermaPulsanteNo")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    modale.modal("hide");
                })
                .end()
            .find(".alert")
                .slideDown()
                .end()
            .modal("show");
    }

    /**
     * Gestisce il cambiamento del tab: blocca tale azione nel caso in cui vi siano delle modifiche non salvate.
     *
     * @param e (Event) l'evento scatenante, da bloccare nel caso in cui vi siano modifiche non salvate
     *
     * @returns (Boolean) <code>true</code> nel caso in cui l'invocazione possa proseguire; <code>false</code> in caso contrario
     */
    exports.gestioneCambioTab = function(e) {
        var inputContainer = $(".tab-content").children(".active")
            .find("*:input:not(.modal *:input)")
                .not("button");
        var changedInputs = inputContainer.findChangedValues();
        var result = true;
        if(changedInputs.length > 0) {
            e.preventDefault();
            // Apri modale conferma cambio, passandogli il this
            apriModaleConfermaCambioTab($(this), inputContainer);
            result = false;
        } else {
            // Posso cambiare il tab: chiudo i messaggi di errore
            $("#ERRORI, #INFORMAZIONI").slideUp();
        }
        return result;
    };

    /**
     * Gestisce il submit del form. Controlla che il checkbox sia deselezionato: in tal caso, FORZA il valore 'false'.
     */
    exports.gestioneSubmitPerCheckbox = function() {
        var checkbox = $("#debitoreMultiplo");
        if(!checkbox.is(":checked")) {
            checkbox.after(
                $("<input/>", {
                    "type": "hidden",
                    "name": checkbox.attr("name"),
                    "value": false
                })
            );
        }
    };

    /**
     * Salva i vecchi valori di netto ed arrotondamento.
     */
    exports.salvaVecchiNettoArrotondamento = function() {
        $("#HIDDEN_oldArrotondamento").val($("#arrotondamentoDocumento").val()).keepOriginalValues();
        $("#HIDDEN_oldNetto").val($("#nettoDocumento").val()).keepOriginalValues();
    };

    /**
     * Pulisce il modale del soggetto.
     */
    exports.puliziaMascheraSoggetto = function() {
        $("#fieldsetRicercaGuidateSoggetto").find(":input")
                .val("");
        $("#divTabellaSoggetti").slideUp();
    };

    exports.alertErrori = $("#ERRORI");
    exports.alertMessaggi = $("#MESSAGGI");
    exports.alertInformazioni = $("#INFORMAZIONI");
    exports.alerts = $("#ERRORI, #INFORMAZIONI");

    /**
     * Wrappo la funzionalità vecchia aggiungendo la chiusura dei vecchi alerts.
     */
    window.impostaDatiNegliAlert = function() {
        var chiudereGliAlerts = arguments[3];
        var innerChiusura = chiudereGliAlerts === undefined ? true : chiudereGliAlerts;
        if(!!innerChiusura) {
            exports.alertErrori.slideUp();
            exports.alertInformazioni.slideUp();
        }
        return _oldImpostaDatiNegliAlert.apply(this, arguments);
    };

    return exports;
}(jQuery));

$(function() {
    //jira 1900 e' stato richiesto di  togliere la descrizione ' È possibile associare all'impegno un soggetto o una classe di soggetti'
    $('#descrizioneSoggetto_modale_seleziona_soggetto').addClass('hide');

    // Gestione del cambio tab
    $("*:input:not(.modal *:input)").not("button")
        .keepOriginalValues();
    $(".nav.nav-tabs").find("a")
        .on("click", Documento.gestioneCambioTab);

    $("#formAggiornamentoDatiDocumento").submit(Documento.gestioneSubmitPerCheckbox);
    $("form").on("reset", function() {
        // NATIVO
        this.reset();
        Documento.puliziaMascheraSoggetto();
    });

    Documento.salvaVecchiNettoArrotondamento();
});