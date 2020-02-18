/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function (w, $) {
    "use strict";
    var exports = {};
    var CapitoloSpesa = function() {
        Capitolo.call(this);
    };
    var instance;

    window.interazioni = window.interazioni || {};

    CapitoloSpesa.prototype = Object.create(Capitolo.prototype);
    CapitoloSpesa.prototype.constructor = CapitoloSpesa;
    CapitoloSpesa.prototype.ricercaCapitoloSpesa = ricercaCapitoloSpesa;

    exports.init = init;
    exports.onPageLoad = onPageLoad;
    exports.puliziaResetForm = puliziaResetForm;
    exports.destroy = destroy;

    /**
     * Inizializzo degli handlers del capitolo di spesa
     *
     */
    function init(){
        var $form = $('#formSelezionaEntitaDiPartenza_capitolo');

        instance = new CapitoloSpesa();
        instance.initCapitolo();
        //nascondo tutto cio' che c'era precedentemente nel div di dx
        $('#gestioneRisultatiConsultazioneEntitaCollegate .hide').not("#formSelezionaEntitaDiPartenza_capitolo").slideUp();
        //mostro cio' che mi interessa
        $('#formSelezionaEntitaDiPartenza_capitolo').show();
        $("#campiSelezioneCapitoloEntrata").fadeOut();
        $("#campiSelezioneCapitoloSpesa").fadeIn();

        //lego gli eventi ora e non al loading della pagina per non appesantire il DOM
        $('#pulsanteRicercaEntitaDiPartenzaCapitolo').substituteHandler("click", instance.ricercaCapitoloSpesa.bind(instance));
        $("#titoloSpesa").on("change", caricaMacroaggregato);
        $('#gestioneRisultatiConsultazioneEntitaCollegate').on( 'init.dt', instance.riabilitaElemento.bind(instance, $form));
        //Ri-abilito il div nel caso sia stato erroneamente disabilitato
        instance.riabilitaElemento($form);

    }

    function puliziaResetForm(){
        if(!instance) {
            return;
        }
        instance.puliziaResetForm();
        $('#titoloSpesa').val("");
        $('#macroaggregato').find('option').remove();
        $('#macroaggregato').attr('disabled');
    }

    /**
     * Ricerca il capitolo Spesa
     *
     */

    function ricercaCapitoloSpesa(event) {
        var errori, $form, arrayToSend;
        //per prima cosa, impedisco il doppio click per non avere oSettings is null error
        this.disabilitaElemento($('#formSelezionaEntitaDiPartenza_capitolo'));

        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        //controllo se vi sono errori
        errori = this.validaForm();
        if (errori.length >0) {
            bootboxAlert(errori);
            this.riabilitaElemento($('#formSelezionaEntitaDiPartenza_capitolo'));
            return;
        }

        //non ho errori: procedo alla creazione dei parametri necessari ai servizi
        $form = $(event.target).parents('form');
        arrayToSend = $form.serializeArray();

        this.creaECaricaTabellaElencoEntita('ricercaCapitoloSpesaConsultabileAjax', arrayToSend, 'CAPITOLOSPESA');
    }


    /**
     * funzionalita' che vengono chiamate al caricamento della pagina
     * */
    function onPageLoad(){
        caricaTitoloSpesa();
    }
    /**
     * Funzionalita' che slega gli handlers
     * */
    function destroy(){
        if(!instance) {
            return;
        }
        instance.destroyCapitolo();
        $("#titoloSpesa").off("change");
        $('#gestioneRisultatiConsultazioneEntitaCollegate').off( 'init.dt');
        instance = undefined;
    }


    /* ***** Funzioni per il caricamento delle select *****/

    /**
     * Carica i dati nella select del titolo da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    function caricaTitoloSpesa() {
        var ts = $("#titoloSpesa").startLoadData();

        // Effettuo la chiamata JSON
        return $.postJSON("titoloSpesaAjax.do", {})
            .then(function (data) {
                var listaTitoloSpesa = (data.listaTitoloSpesa);
                var errori = data.errori;
                var options = $("#titoloSpesa");
                options.find('option').remove().end();
                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    return;
                }

                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }

                popolaSelect(options, listaTitoloSpesa, createOptionAsValueUidStringCodiceDescrizione);
            }).always(ts.endLoadData.bind(ts));
    }

    /**
     * Carica i dati nella select del Macroaggregato da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
     function caricaMacroaggregato() {
        var idTitoloSpesa = $("#titoloSpesa").val();
        var selectMacroaggregato = $("#macroaggregato").overlay('show');

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei cMonti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");

        // Effettuo la chiamata JSON
        return $.postJSON("ajax/macroaggregatoAjax.do", {"id" : idTitoloSpesa})
            .then(function (data) {
                var listaMacroaggregato = (data.listaMacroaggregato);
                var errori = data.errori;
                var bottonePdC = $("#bottonePdC");

                selectMacroaggregato.find('option').remove().end();
                if(errori.length > 0) {
                    selectMacroaggregato.attr("disabled", "disabled");
                    return;
                }

                if (selectMacroaggregato.attr("disabled") === "disabled") {
                    selectMacroaggregato.removeAttr("disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }
                popolaSelect(selectMacroaggregato, listaMacroaggregato, createOptionAsValueUidStringCodiceDescrizione);
            }).always(selectMacroaggregato.overlay.bind(selectMacroaggregato, "hide"));
    }

    window.interazioni.CAPITOLOSPESA = exports;
}(this, jQuery);