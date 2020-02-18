/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
 /*******************************
 **** Funzioni del capitolo ****
 *******************************
 */
!function (w, $) {
    "use strict";
    var Capitolo = function() {
        Base.call(this);
    };

    window.interazioni = window.interazioni || {};
    
    Capitolo.prototype = Object.create(Base.prototype);
    Capitolo.prototype.constructor = Capitolo;

    Capitolo.prototype.initCapitolo = initCapitolo;
    Capitolo.prototype.puliziaResetForm = puliziaResetForm;
    Capitolo.prototype.validaForm = validaForm;
    Capitolo.prototype.destroyCapitolo = destroyCapitolo;


    /**
     * Inizializzazione comune del capitolo
     * <br>
     * lego gli handlers e gli eventi
     */
    function initCapitolo() {

        //se c'&eacute  qualcosa di aperto nel div a destra, lo voglio chiudere prima di aprire ci&ograve che mi interessa

        //solo per la primainterazione

    }

    /**
     * Effettua una pulizia completa dei campi al reset.
     */
    function puliziaResetForm() {
        // Pulisco i vari campi
        $("#formSelezionaEntitaDiPartenza_capitolo input[type='radio']").removeAttr("checked");
        $("#formSelezionaEntitaDiPartenza_capitolo input").not("[data-maintain]").not(".btn").val("");
        $("#formSelezionaEntitaDiPartenza_capitolo select").val("");
        $("#formSelezionaEntitaDiPartenza_capitolo textarea").val("");
        ZTreeSAC.deseleziona("treeStruttAmmCapitolo");
    }


    /**
     * Validazione del form.
     * <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare a livello client l'inserimento dei dati all'interno del form.
     * Questa funzione contiene i campi presenti globalmente.
     *
     * @returns la stringa degli errori comuni
     */

    function validaForm() {
        //var hasUnCampoValorizzato = false;
        // Definizione dei campi da controllare
        var tipoBilancioCapitoloSelezionato = $('#faseBilancioCapitolo input:checked').val();

        // definizione del vettore di errori
        var errori = [];

        if (!tipoBilancioCapitoloSelezionato) {
            errori.push("Parametro non inizializzato: Bilancio");
        }

        // Restituisco il vettore di errori
        return errori;
    }

    /**
     * Inizializzazione del capitolo
     * <br>
     * lego gli handlers e gli eventi
     */
    function destroyCapitolo() {

        $('#pulsanteRicercaEntitaDiPartenzaCapitolo').off("click");
        $('#tabellaRisultatiConsultazioneEntitaCollegate .button').off('click');
        $('#formSelezionaEntitaDiPartenza_capitolo').hide();

    }

    //un tentativo finito male...
    function popolaForm(arrayOfRequestParameters){
        var tipoCapitolo = Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('tipoCapitolo');
        $('#tipoCapitolo'+ tipoCapitolo).attr('checked');
        $('#faseBilancio input[value="'+ Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('tipoCapitolo')+'"]').attr('checked');

        $('annoCapitolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('annoCapitolo'));
        $('numeroCapitolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('numeroCapitolo'));
        $('numeroArticolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('numeroArticolo'));
        $('numeroUEB').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('numeroUEB'));
        $('descrizioneCapitolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('numeroCapitolo'));

        $('textarea').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('descrizione'));

        $("#campiSelezioneCapitolo" + tipoCapitoloSelezionato).find('titolo' + tipoCapitolo).val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('titolo' + tipoCapitolo));

        $("#campiSelezioneCapitolo" + tipoCapitoloSelezionato).find('#macroaggregato').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('#macroaggregato'));
        $("#campiSelezioneCapitolo" + tipoCapitoloSelezionato).find('#tipologiaTitolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('#tipologiaTitolo'));
        $("#campiSelezioneCapitolo" + tipoCapitoloSelezionato).find('#categoriaTipologiaTitolo').val(Capitolo.prototype.ottieniValoreParametroFromArrayOfRequestParametersByNomeParametro('#tipologiaTitolo'));
    }


    /* ***** Chiamate AJAX *****/

    w.Capitolo = Capitolo;
}(this, jQuery);