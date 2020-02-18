/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Gestisce le funzionalita' per la selezione del capitolo da cui iniziare la consultazione
 */
var ConsultazioneEntita = (function($) {
    "use strict";
    var InnerConsultazioneEntita = function() {
        Base.call(this);
    };

    var exports = {};

    window.interazioni = window.interazioni || {};

    InnerConsultazioneEntita.prototype = Object.create(Base.prototype);
    InnerConsultazioneEntita.prototype.constructor = InnerConsultazioneEntita;

    exports.ricercaEntitaConsultabile = ricercaEntitaConsultabile;
    exports.gestisciInterazioneCambiata = gestisciInterazioneCambiata;
    exports.impostaCambioInterazione = impostaCambioInterazione;
    exports.removeOverlaySelezione = removeOverlaySelezione;

    /**
     * Ottiene l'array con i parametri della request cosi' come voluto da dataTables
     * @param l'elemento cliccato
     * @returns l'array di oggetti con i parametri della request
     * */
    function creaArrayOfRequestParameters(tipoEntitaSelezionata, tipoEntitaPadre, uidEntitaPadre){
        var array = [{},{},{}];
        array[0].name = 'tipoEntitaConsultabile';
        array[0].value = tipoEntitaSelezionata;

        array[1].name = 'tipoEntitaConsultabilePadre';
        array[1].value = tipoEntitaPadre;

        array[2].name = 'uidEntitaConsultabilePadre';
        array[2].value = uidEntitaPadre;
        return array;
    }

    /**
     * Imposta la selezione dell'entita (non di partenza)
     * **/
    function ricercaEntitaConsultabile(tipoEntitaSelezionata, tipoEntitaPadre, uidEntitaPadre){
        var urlRicercaEntita = 'ricercaEntitaConsultabileAjax';
        var arrayOfRequestParameters = creaArrayOfRequestParameters(tipoEntitaSelezionata, tipoEntitaPadre, uidEntitaPadre);

        $('#gestioneRisultatiConsultazioneEntitaCollegate').addClass('loading-data');
        $('#selezioneConsultazioneEntitaCollegate')
            .addClass('loading-data')
            .off('click', 'li');
        InnerConsultazioneEntita.prototype.creaECaricaTabellaElencoEntita(urlRicercaEntita, arrayOfRequestParameters, tipoEntitaSelezionata);
    }

    /**
     * Quando l'interazione e' cambiata (al click su uno dei folders delle entita selezionabili del div centrale)
     * lancia le funzionalita' dell'entita di partenza o dell'entita' Generica nel div a destra dei risultati della ricerca
     * @param e l'evento che ha scatenato il cambio di interazione
     * */
    function gestisciInterazioneCambiata(e){
        var interazioni= window.interazioni || {};
        var interazione = interazioni[e.interazione];
        var $elementoScatenante = $(e.elementoScatenante);
        var tipoEntitaSelezionata;
        var tipoEntitaPadre;
        var uidEntitaPadre;
        var it;

        if(interazione){
            for( it in interazioni) {
                if(Object.prototype.hasOwnProperty.call(interazioni, it)){
                    interazioni[it].destroy();
                    interazioni[it].puliziaResetForm();
                }
            }
            interazione.init();
            return;
        }

        tipoEntitaSelezionata = $elementoScatenante.data('tipo-entita');
        tipoEntitaPadre = $elementoScatenante.data('tipo-entita-padre');
        uidEntitaPadre = $elementoScatenante.data('uid-padre');

        ricercaEntitaConsultabile(tipoEntitaSelezionata, tipoEntitaPadre, uidEntitaPadre);
    }

    /**
     * Quando l'interazione e' cambiata (al click su uno dei folders delle entita selezionabili del div centrale)
     * lancia le funzionalita' dell'entita di partenza o dell'entita' Generica nel div a destra dei risultati della ricerca
     * */
    function impostaCambioInterazione(e){
        var customEvt = $.Event('changeInterazione');
        var tipoInterazione = $(e.currentTarget).data('tipo-entita');
        var $elementoSelezionato = $(e.currentTarget);
        customEvt.elementoScatenante = e.currentTarget;
        customEvt.interazione = tipoInterazione;

        //seleziono l'icona dell'entita precedentemente selezionata e la cambio
        $elementoSelezionato.parent().find('span.button.ico_open').toggleClass('ico_close ico_open');



        //seleziono l'icona dell'entita selezionata e la cambio
        $elementoSelezionato.find('span.button.ico_close').toggleClass('ico_close ico_open');

        $('#gestioneRisultatiConsultazioneEntitaCollegate').trigger(customEvt);
    }
    
    function removeOverlaySelezione() {
        if($('#selezioneConsultazioneEntitaCollegate').hasClass('loading-data')) {
            $('#selezioneConsultazioneEntitaCollegate')
                .removeClass('loading-data')
                .on('click', 'li', ConsultazioneEntita.impostaCambioInterazione);
        }
    }

    return exports;
}(jQuery));

//onload functions
$(function() {
    var it;
    var interazioni = window.interazioni || {};

    ZTreeSAC.caricaStrutturaAmministrativoContabile();
    for( it in interazioni) {
        if(Object.prototype.hasOwnProperty.call(interazioni, it)){
            interazioni[it].onPageLoad();
        }
      }
    $('#selezioneConsultazioneEntitaCollegate').on('click', 'li', ConsultazioneEntita.impostaCambioInterazione);
    $('#gestioneRisultatiConsultazioneEntitaCollegate').on('changeInterazione', ConsultazioneEntita.gestisciInterazioneCambiata);

    $.fn.zTree.init($("#treeConsultaEntitaCollegate"), Riepilogo.settingTree,Riepilogo.home).expandAll(true);
    $('#riepilogoConsultazioneEntitaCollegate').on('entitaSelezionata', Riepilogo.aggiungiNodo);

    $(document).on('table-data-loaded', ConsultazioneEntita.removeOverlaySelezione);
});