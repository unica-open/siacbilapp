/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function ($, global) {

    "use strict";

    var alertErrori = $("#ERRORI");
    var exports = {};

    /**
     * Carica la select con la lista di entita fornita.
     *
     * @param list       (Array)  la lista da caricare (ATTENZIONE! Necessita dei campi uid, codice e descrizione)
     * @param select     (jQuery) l'oggetto jQuery rappresentante la select da popolare
     * @param emptyValue (String) il valore da impostare nella option vuota (Optional - default: '')
     *
     * @returns (jQuery) l'oggetto jQuery rappresentante la select popolata
     */
    function caricaSelectEntita(list, select, /* Optional */ emptyValue) {
        var i;
        var el;
        var len = list.length;
        var str = "<option value='" + (emptyValue || "") + "'></option>";
        for (i = 0; i < len; i++) {
            el = list[i];
            str += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + "</option>";
        }
        return select.append(str);
    }

    /**
     * Carica la select con la lista di enum fornita.
     *
     * @param list       (Array)  la lista da caricare (ATTENZIONE! Necessita dei campi _name, codice e descrizione)
     * @param select     (jQuery) l'oggetto jQuery rappresentante la select da popolare
     * @param emptyValue (String) il valore da impostare nella option vuota (Optional - default: '')
     *
     * @returns (jQuery) l'oggetto jQuery rappresentante la select popolata
     */
    function caricaSelectEnum(list, select, /* Optional */ emptyValue) {
        var i;
        var el;
        var len = list.length;
        var str = "<option value='" + (emptyValue || "") + "'></option>";
        for (i = 0; i < len; i++) {
            el = list[i];
            str += "<option value='" + el._name + "'>" + el.codice + " - " + el.descrizione + "</option>";
        }
        return select.append(str);
    }

    /**
     * Riformatta il periodo annuale per la visualizzazione.
     */
    function riformattaPeriodo(listaPeriodo) {
        var res = [];
        var i;
        var len = listaPeriodo.length;
        var el;
        var annoEsercizio = $("#annoEsercizio").val();
        for (i = 0; i < len; i++) {
            el = $.extend(true, {}, listaPeriodo[i]);
            if ("SY" === el.codice) {
                el.descrizione = annoEsercizio;
            }
            res.push(el);
        }
        return res;
    }

    /**
     * Gestione dei flags del tipo registro. La selezione di un tipo abilita o nasconde dati flags.
     *
     * @param selectTipoRegistro (jQuery) la select del registro (Optional - default: #tipoRegistroIva)
     */
    exports.gestioneFlagsTipoRegistro = function (/* Optional */selectTipoRegistro) {
        var select = selectTipoRegistro || $("#tipoRegistroIva");
        var selectedValue = select.val();
        var spanDocumentiPagati = $("#spanDocumentiPagati");
        var spanDocumentiIncassati = $("#spanDocumentiIncassati");
        var checkboxDocumentiPagati = $("#flagDocumentiPagati");
        var checkboxDocumentiIncassati = $("#flagDocumentiIncassati");

        if ("ACQUISTI_IVA_DIFFERITA" === selectedValue) {
            // CASO 1: Tipo Registro Iva = 'ACQUISTI IVA ESIGIBILITA' DIFFERITA'
            // Viene visualizzato il campo 'documenti pagati' e reso editabile.
            // Viene nascosto il campo 'documenti incassati'.
            spanDocumentiIncassati.removeClass("in");
            checkboxDocumentiIncassati.attr("disabled", true);

            checkboxDocumentiPagati.removeAttr("disabled");
            spanDocumentiPagati.addClass("in");
        } else if ("VENDITE_IVA_DIFFERITA" === selectedValue) {
            // CASO 2:  Tipo Registro Iva = 'VENDITE IVA ESIGIBILITA’ DIFFERITA'
            // Viene visualizzato il campo 'documenti incassati' e reso editabile.
            // Viene nascosto il campo 'documenti pagati'.
            spanDocumentiPagati.removeClass("in");
            checkboxDocumentiPagati.attr("disabled", true);

            checkboxDocumentiIncassati.removeAttr("disabled");
            spanDocumentiIncassati.addClass("in");
        } else {
            // CASO 3: Tipo Registro Iva = 'ACQUISTI IVA IMMEDIATA' o 'VENDITE IVA IMMEDIATA' o 'CORRISPETTIVI'
            // Viene nascosto il campo 'documenti pagati'.
            // Viene nascosto il campo 'documenti incassati'.
            spanDocumentiPagati.removeClass("in");
            spanDocumentiIncassati.removeClass("in");
            checkboxDocumentiPagati.attr("disabled", true);
            checkboxDocumentiIncassati.attr("disabled", true);
        }
    };

    /**
     * Carica il registro iva a partire dal servizio.
     *
     * @param selectTipoRegistro      (jQuery) la select del registro (Optional - default: #tipoRegistroIva)
     * @param selectGruppoAttivitaIva (jQuery) la select del registro (Optional - default: #gruppoAttivitaIva)
     * @param selectRegistro          (jQuery) la select del registro (Optional - default: #registroIva)
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione AJAX
     */
    exports.caricaRegistroIva = function (/* Optional */selectTipoRegistro, /* Optional */selectGruppoAttivitaIva, /* Optional */selectRegistro) {
        var selectTipoRegistroIva = selectTipoRegistro || $("#tipoRegistroIva");
        var selectGruppoAttivita = selectGruppoAttivitaIva || $("#gruppoAttivitaIva");
        var selectRegistroIva = selectRegistro || $("#registroIva");
        var tipoRegistroIva = selectTipoRegistroIva.val();
        var gruppoAttivitaIva = selectGruppoAttivita.val();
        var oggettoPerChiamataAjax = {};

        // Svuoto la select e inizio l'overlay
        selectRegistroIva.empty()
            .overlay("show");

        // Devo aver selezionato entrambi i campi. In caso contrario, me ne esco
        if (!tipoRegistroIva || !gruppoAttivitaIva) {
            selectRegistroIva.overlay("hide")
                .attr("disabled", true)
                .val("");
            return;
        }
        // Imposto l'oggetto da passare al servizio
        oggettoPerChiamataAjax["registroIva.tipoRegistroIva"] = tipoRegistroIva;
        oggettoPerChiamataAjax["registroIva.gruppoAttivitaIva.uid"] = gruppoAttivitaIva;

        return $.postJSON("ajax/registroIvaAjax.do", oggettoPerChiamataAjax, function (data) {
            // Se ho errori, esco subito
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Carico la select del registro
            caricaSelectEntita(data.listaRegistroIva, selectRegistroIva);
            selectRegistroIva.removeAttr("disabled");
        }).always(function () {
            selectRegistroIva.overlay("hide");
        });
    };

    /**
     * Carica il tipo chiusura e il periodo associati al gruppo.
     *
     * @param url                     (String) l'URL da invocare
     * @param selectGruppoAttivitaIva (jQuery) la select del registro (Optional - default: #gruppoAttivitaIva)
     * @param selectTipoChiusuraIva   (jQuery) la select del registro (Optional - default: #tipoChiusura)
     * @param hiddenTipoChiusuraIva   (jQuery) la select del registro (Optional - default: #hiddenTipoChiusura)
     * @param selectPeriodoIva        (jQuery) la select del registro (Optional - default: #periodo)
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione AJAX
     */
    exports.caricaTipoChiusuraEPeriodoEIvaACredito = function (url, /* Optional */selectGruppoAttivitaIva, /* Optional */selectTipoChiusuraIva, /* Optional */hiddenTipoChiusuraIva, /* Optional */selectPeriodoIva) {
        var selectGruppo = selectGruppoAttivitaIva || $("#gruppoAttivitaIva");
        var selectTipoChiusura = selectTipoChiusuraIva || $("#tipoChiusura");
        var hiddenTipoChiusura = hiddenTipoChiusuraIva || $("#hiddenTipoChiusura");
        var selectPeriodo = selectPeriodoIva || $("#periodo");

        var gruppoAttivitaIva = selectGruppo.val();
        var oggettoPerChiamataAjax = {};

        // Overlay sulle select
        selectTipoChiusura.overlay("show");
        selectPeriodo.empty()
            .overlay("show");

        // Se non ho selezionato il gruppo, esco
        if (!gruppoAttivitaIva) {
            selectTipoChiusura.overlay("hide")
                .val("");
            selectPeriodo.overlay("hide")
                .attr("disabled", true)
                .val("");
            hiddenTipoChiusura.val("");
            return;
        }

        oggettoPerChiamataAjax["gruppoAttivitaIva.uid"] = gruppoAttivitaIva;

        return $.postJSON(url, oggettoPerChiamataAjax, function (data) {
            var listaPeriodo = riformattaPeriodo(data.listaPeriodo);

            // Carico le select
            selectTipoChiusura.val(data.tipoChiusura ? data.tipoChiusura._name : "");
            caricaSelectEnum(listaPeriodo, selectPeriodo);

            // Carico il campo hidden
            hiddenTipoChiusura.val(data.tipoChiusura ? data.tipoChiusura._name : "");

            // Permetto l'editabilita' della select del periodo
            selectPeriodo.removeAttr("disabled");
        }).always(function () {
            // Chiudo gli overlay
            selectTipoChiusura.overlay("hide");
            selectPeriodo.overlay("hide");
        });
    };

    /**
     * Gestione dell'apertura del modale di conferma della stampa.
     *
     * @param select    (jQuery) la select da cui ottenere i valori per la gestione del modale
     * @param fieldName (String) il nome del campo
     */
    exports.aperturaModaleConfermaStampa = function (select, fieldName, allowedEmpty) {
        var selectedOption = select.find("option:selected");
        var strongTemplateStampa = $("#templateStampa");
        var template = $("#hiddenTemplateStampa").val();
        var compiledTemplate;
        var html;

        // Chiudo l'alert degli errori
        alertErrori.slideUp();

        // Se non ho selezionato la option, esco
        if (!allowedEmpty && !selectedOption.val()) {
            impostaDatiNegliAlert(["Necessario selezionare il " + fieldName], alertErrori);
            return;
        }

        html = selectedOption.html() || '';
        compiledTemplate = template.replace("___", html);
        strongTemplateStampa.html(compiledTemplate);

        // Apertura del modale
        $("#modaleConfermaStampaIva").modal("show");
    };

    // Export delle funzionalita'
    global.StampeIva = exports;
}(jQuery, this));