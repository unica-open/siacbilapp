/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *******************************
 **** Funzioni del soggetto ****
 *******************************
 */
!function (w, $) {
    "use strict";
    var exports = {};
    var instance;

    //creo la variabile provvedimento
    var Soggetto = function() {
        Base.call(this);
    };

    window.interazioni = window.interazioni || {};

    Soggetto.prototype = Object.create(Base.prototype);
    Soggetto.prototype.constructor = Soggetto;
    Soggetto.prototype.ricercaSoggetto = ricercaSoggetto;

    // Variabile di esportazione
    exports.init = init;
    exports.onPageLoad = onPageLoad;
    exports.puliziaResetForm = puliziaResetForm;
    exports.destroy = destroy;


    /**
     * Inizializzazione del capitolo
     * <br>
     * lego gli handlers e gli eventi
     */
    function init(){
        var $form = $('#formSelezionaEntitaDiPartenza_soggetto');
        instance = new Soggetto();
        //se c'&egrave; qualcosa di aperto nel div a destra, lo voglio chiudere
        $('#gestioneRisultatiConsultazioneEntitaCollegate .hide').slideUp();
        $('#formSelezionaEntitaDiPartenza_soggetto').show();
        //gestione handlers del soggetto
        $('#pulsanteRicercaEntitaDiPartenzaSoggetto').on('click', instance.ricercaSoggetto.bind(instance));
        $('#gestioneRisultatiConsultazioneEntitaCollegate').on('init.dt', instance.riabilitaElemento.bind(instance, $form));

        instance.riabilitaElemento($form);
    }

    /**
     * funzionalit&agrave relative al soggetto da richiamare al load della pagina
     * */
    function onPageLoad(){
        //implementazione vuota di default
    }
    /**
     * pulisce il form del soggetto
     * */
    function puliziaResetForm() {
        if(!instance) {
            return;
        }
        $("#formSelezionaEntitaDiPartenza_soggetto input").not(".btn").val("");
    }

    /**
     * Funzione che slega eventi e handlers
     * */
    function destroy(){
        if(!instance) {
            return;
        }
        $('#pulsanteRicercaEntitaDiPartenzaSoggetto').off('click');
        $('#tabellaRisultatiConsultazioneEntitaCollegate .button').off('click');
        $('#gestioneRisultatiConsultazioneEntitaCollegate').off( 'init.dt');
        instance = undefined;
    }

    /**
     * Validazione del form.
     * <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare a livello client l'inserimento dei dati all'interno del form.
     * Questa funzione contiene i campi presenti globalmente.
     *
     * @returns la stringa degli errori comuni
     */

    function validaForm () {
       var errori =[];
       var isFormValido = $('#codiceSoggetto').val() ||
                             $('#denominazioneSoggetto').val() ||
                             $('#codiceFiscaleSoggetto').val() ||
                             $('#partitaIvaSoggetto').val();
       if(!isFormValido){
           errori.push("Selezionare almeno un criterio di ricerca");
       }
       return errori;
    }

    /**
     * Fa partire la ricerca del soggetto consultabile
     * @param the event l'evento che ha scatento la ricerca
     * */
    function ricercaSoggetto(event){

        var errori, $form, arrayToSend;

        this.disabilitaElemento($('#formSelezionaEntitaDiPartenza_soggetto'));

        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        $form = $(event.target).parents('form');
        arrayToSend = $form.serializeArray();

        errori = validaForm();

        if (errori.length>0){
            bootboxAlert(errori);
            this.riabilitaElemento($('#formSelezionaEntitaDiPartenza_soggetto'));
            return;
        }

        this.creaECaricaTabellaElencoEntita('ricercaSoggettoConsultabileAjax', arrayToSend, 'SOGGETTO');

    }

    window.interazioni.SOGGETTO = exports;
}(this, jQuery);