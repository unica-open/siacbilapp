/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Gestisce le funzionalit&agrave; del Provvedimento (entit&agrave di partenza)
 */
!function (w, $) {
    "use strict";

    //creo la variabile provvedimento
    var exports = {};
    var Provvedimento = function() {
        Base.call(this);
    };
    var instance;

    window.interazioni = window.interazioni || {};

    Provvedimento.prototype = Object.create(Base.prototype);
    Provvedimento.prototype.constructor = Provvedimento;
    Provvedimento.prototype.ricercaProvvedimento = ricercaProvvedimento;
    Provvedimento.prototype.validaForm = validaForm;

    exports.init = init;
    exports.onPageLoad = onPageLoad;
    exports.puliziaResetForm = puliziaResetForm;
    exports.destroy = destroy;

    function puliziaResetForm() {
        if(!instance) {
            return;
        }
        // Pulisco i vari campi
        $("#formSelezionaEntitaDiPartenza_provvedimento input").not(".btn").val("");
        $("#formSelezionaEntitaDiPartenza_provvedimento select").val("");
        ZTreeSAC.deseleziona("treeStruttAmmCapitolo");
    }

    function init() {
        var $form = $('#formSelezionaEntitaDiPartenza_provvedimento');
        instance = new Provvedimento();
        $('#gestioneRisultatiConsultazioneEntitaCollegate .hide').slideUp();

        $('#formSelezionaEntitaDiPartenza_provvedimento').show();

        //lego gli handlers
        $('#pulsanteRicercaEntitaDiPartenzaProvvedimento').substituteHandler("click", instance.ricercaProvvedimento.bind(instance));
        $('#gestioneRisultatiConsultazioneEntitaCollegate').on( 'init.dt', instance.riabilitaElemento.bind(instance, $form));

        instance.riabilitaElemento($form);
    }

    /**
     * Carica i dati nella select del titolo da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    function caricaListaTipoAttoProvvedimento() {

        // Effettuo la chiamata JSON
        return $.postJSON("tipoAttoAjax.do", {})
        .then(function(data) {
            var listaTipoAtto = (data.listaTipoAtto);
            var errori = data.errori;
            var options = $("#tipoAttoProvvedimento");

            options.find('option').remove().end();
            if (errori.length > 0) {
                options.attr("disabled", "disabled");
                return;
            }

            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            popolaSelect(options, listaTipoAtto, createOptionAsValueUidStringCodiceDescrizione);
        }).always(function() {
            // Chiudo l'overlay
            $("#tipoAttoProvvedimento").removeClass("loading-data");
            $("#tipoAttoProvvedimento").removeAttr("disabled");
        });
    }

    /**
     * Validazione del form.
     * <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare a livello client l'inserimento dei dati all'interno del form.
     *
     * @returns l'array degli errori comuni
     */
    //TODO quali sono i campi obbligatori?
    function validaForm() {
        // Definizione dei campi da controllare
        var annoProvvedimento = $('#annoProvvedimento').val();
        var numeroProvvedimento = $('#numeroProvvedimento').val();
        var tipoProvvedimento = $('#tipoAttoProvvedimento').val();

        // definizione del vettore di errori
        var errori = [];

        if (annoProvvedimento === "") {
            errori.push("Il campo Anno deve essere selezionato");
        }
        if (numeroProvvedimento === "" && tipoProvvedimento === "") {
            errori.push("Almeno uno tra i campi Numero e Tipo deve essere compilato");
        }
        // Restituisco il vettore di errori
        return errori;
    }

    function ricercaProvvedimento(event) {

        var errori, $form, arrayToSend;

        Provvedimento.prototype.disabilitaElemento($('#formSelezionaEntitaDiPartenza_provvedimento'));

        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        errori = Provvedimento.prototype.validaForm();

        if (errori.length >0) {
            Provvedimento.prototype.riabilitaElemento($('#formSelezionaEntitaDiPartenza_provvedimento'));
            bootboxAlert(errori);
            return;
        }

        $form = $(event.target).parents('form');
        arrayToSend = $form.serializeArray();
        Provvedimento.prototype.creaECaricaTabellaElencoEntita('ricercaProvvedimentoConsultabileAjax', arrayToSend, 'PROVVEDIMENTO');

    }


    /**
     * funzionalit&agrave relative al soggetto da richiamare al load della pagina
     * */
    function onPageLoad(){
        caricaListaTipoAttoProvvedimento();
    }

    function destroy() {
        if(!instance) {
            return;
        }
        $('#pulsanteRicercaEntitaDiPartenzaProvvedimento').off("click");
        $('#selezionaEntitaDiPartenza_provvedimento').hide();
        $('#gestioneRisultatiConsultazioneEntitaCollegate').off( 'init.dt');
        instance = undefined;
    }

    window.interazioni.PROVVEDIMENTO = exports;
}(this, jQuery);