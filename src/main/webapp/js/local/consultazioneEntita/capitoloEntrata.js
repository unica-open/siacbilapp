/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function (w, $) {
    "use strict";

    // Gli exports della funzione
    var exports = {};
    var CapitoloEntrata = function() {
        Capitolo.call(this);
    };
    var instance;

    window.interazioni = window.interazioni || {};

    CapitoloEntrata.prototype = Object.create(Capitolo.prototype);
    CapitoloEntrata.prototype.constructor = CapitoloEntrata;
    CapitoloEntrata.prototype.ricercaCapitoloEntrata = ricercaCapitoloEntrata;

    exports.init = init;
    exports.onPageLoad = onPageLoad;
    exports.puliziaResetForm = puliziaResetForm;
    exports.caricaTipologia = caricaTipologia;
    exports.caricaCategoria = caricaCategoria;
    exports.destroy = destroy;


    function puliziaResetForm(){
        if(!instance) {
            return;
        }
        instance.puliziaResetForm();
    }

   function init(){
       var $form = $('#formSelezionaEntitaDiPartenza_capitolo');
       instance = new CapitoloEntrata();
       instance.initCapitolo();

       $('#gestioneRisultatiConsultazioneEntitaCollegate .hide').not("#formSelezionaEntitaDiPartenza_capitolo").slideUp();
       $('#formSelezionaEntitaDiPartenza_capitolo').show();

       $("#campiSelezioneCapitoloSpesa").fadeOut();
       $("#campiSelezioneCapitoloEntrata").fadeIn();

       $('#pulsanteRicercaEntitaDiPartenzaCapitolo').substituteHandler("click", instance.ricercaCapitoloEntrata.bind(instance));
       $("#titoloEntrata").on("change", caricaTipologia);
       $("#tipologiaTitolo").on("change",caricaCategoria);
       $('#gestioneRisultatiConsultazioneEntitaCollegate').on( 'init.dt', instance.riabilitaElemento.bind(instance, $form));

       instance.riabilitaElemento($form);
   }

   /**
    * funzionalit&agrave relative al capitolo di Entrata da richiamare al load della pagina
    * */

   function onPageLoad(){
       caricaTitoloEntrata();
   }

    /**
     * Ricerca il capitolo Entrata
     */
    function ricercaCapitoloEntrata(event) {
        var errori, $form, arrayToSend;
        //per prima cosa, disabilito il div
        this.disabilitaElemento($('#formSelezionaEntitaDiPartenza_capitolo'));

        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        errori = this.validaForm();
        if (errori.length >0) {
            bootboxAlert(errori);
            this.riabilitaElemento($('#formSelezionaEntitaDiPartenza_capitolo'));
            return;
        }

        $form = $(event.target).parents('form');
        arrayToSend = $form.serializeArray();

        this.creaECaricaTabellaElencoEntita('ricercaCapitoloEntrataConsultabileAjax', arrayToSend, 'CAPITOLOENTRATA');
    }


   function destroy () {
       if(!instance) {
           return;
       }
       instance.destroyCapitolo();
       $("#titoloEntrata").off("change");
       $("#tipologiaTitolo").off("change");
       instance = undefined;
   }


    /* ***** Funzioni per le chiamate AJAX *****/

    /**
     * Carica i dati nella select del titolo da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    function caricaTitoloEntrata() {

        // Effettuo la chiamata JSON
        return $.postJSON("titoloEntrataAjax.do", {})
        .then(function (data) {
            var listaTitoloEntrata = data.listaTitoloEntrata;
            var errori = data.errori;
            var options = $("#titoloEntrata");

            options.find('option').remove().end();
            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                return;
            }

            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            popolaSelect(options, listaTitoloEntrata, createOptionAsValueUidStringCodiceDescrizione);
        }).always(function() {
        // Chiudo l'overlay
            $("#titoloEntrata").removeClass("loading-data");
            $("#titoloEntrata").removeAttr("disabled");
        });
    }


    /**
     * Carica i dati nella select della Tipologia da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    function caricaTipologia () {
        var idTitoloEntrata = $("#titoloEntrata").val();
        var codiceTitolo = $("#titoloEntrata option:selected").html().split("-")[0];
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
        //Pulisco la select della categoria
        $("#categoriaTipologiaTitolo").val("");

        // Imposto il codice nel campo hidden
        $("#HIDDEN_codiceTitolo").val(codiceTitolo);

        return $.postJSON("ajax/tipologiaTitoloAjax.do", {"id" : idTitoloEntrata})
        .then(function (data) {
            var listaTipologiaTitolo = (data.listaTipologiaTitolo);
            var errori = data.errori;
            var options = $("#tipologiaTitolo");
            var selectCategoria = $("#categoriaTipologiaTitolo");
            var bottonePdC = $("#bottonePdC");

            options.find('option').remove().end();

            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                selectCategoria.attr("disabled", "disabled");
                bottonePdC.attr("disabled", "disabled");
                return;
            }
            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            if (selectCategoria.attr("disabled") !== "disabled") {
                selectCategoria.attr("disabled", "disabled");
            }
            if (bottonePdC.attr("disabled") !== "disabled") {
                bottonePdC.attr("disabled", "disabled");
            }
            popolaSelect(options, listaTipologiaTitolo, createOptionAsValueUidStringCodiceDescrizione);
        });
    }

    /**
     * Carica i dati nella select della Categoria da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
     function caricaCategoria() {
        var idTipologiaTitolo = $("#tipologiaTitolo").val();

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");

        return $.postJSON("ajax/categoriaTipologiaTitoloAjax.do", {"id" : idTipologiaTitolo})
        .then(function (data) {
            var listaCategoriaTipologiaTitolo = (data.listaCategoriaTipologiaTitolo);
            var errori = data.errori;
            var options = $("#categoriaTipologiaTitolo");
            var bottonePdC = $("#bottonePdC");

            options.find('option').remove().end();

            if(errori.length > 0) {
                options.attr("disabled", "disabled");
                return;
            }

            if (options.attr("disabled") === "disabled") {
                options.removeAttr("disabled");
            }
            if (bottonePdC.attr("disabled") !== "disabled") {
                bottonePdC.attr("disabled", "disabled");
            }
            popolaSelect(options, listaCategoriaTipologiaTitolo, createOptionAsValueUidStringCodiceDescrizione);
        });
    }

    window.interazioni.CAPITOLOENTRATA = exports;
}(this, jQuery);