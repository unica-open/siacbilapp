/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");

    /**
     * Popolamento della select.
     *
     * @param select  (jQuery) la select da popolare
     * @param options (Array)  l'array delle opzioni
     */
    function popolaSelect(select, options) {
        var opts = "<option></option>";
        $.each(options, function() {
            opts += "<option value='" + this.uid + "'>" + this.codice + " - " + this.descrizione + "</option>";
        });
        select.html(opts);
    }

    /**
     * Caricamento degli eventi.
     */
    function caricaEventi() {
        var selectTipoEvento = $("#uidTipoEvento");
        var selectEvento = $("#uidEvento");
        var tipoEvento = selectTipoEvento.val();
        var overlayElements = $("#uidTipoEvento, #uidEvento");

        // Attivo gli overlay
        overlayElements.overlay("show");

        // Se non ho selezionato il l'evento, svuoto la select ed esco
        if(!tipoEvento) {
            popolaSelect(selectEvento, []);
            overlayElements.overlay("hide");
            return;
        }

        // Chiamata AJAX
        $.postJSON("ricercaEventoByTipoEvento.do", {"tipoEvento.uid": tipoEvento}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectEvento, []);
                return;
            }
            popolaSelect(selectEvento, data.listaEvento);
        }).always(function() {
            // Nasconfdo gli overlay
            overlayElements.overlay("hide");
        });

    }

    /**
     * Caricamento del dettaglio del soggetto.
     */
    function caricaDettaglioSoggetto() {
        Soggetto.caricaDettaglioSoggetto($("#codiceSoggettoSoggetto"), $(), $(), $("#descrizioneCompletaSoggetto"));
    }
    /*********************************************************AGGIUNTI IL 13/01/2015 CR 2865 BEGIN************************************/

    /**
     * Caricamento delle liste del tipo evento.
     */
    function caricaListaTipoEvento(callback) {
        var select = $('#uidTipoEvento').overlay('show');
        var innerCallback = callback && typeof callback === 'function' ? callback : $.noop;
        var selectEvento = $("#evento");

        $.postJSON('ricercaCausaleEPFI_caricaListaTipoEvento.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
         caricaSelect(select, data.listaTipoEvento, true);
        }).then(innerCallback)
        .always(select.overlay.bind(select, 'hide'));
        selectEvento.removeAttr("disabled").change();
        

    }
    
    /**
     * Controlla se la select ha piu' di un dato numero di opzioni.
     *
     * @param select    (jQuery) la select da controllare
     * @param threshold (Number) la soglia da superare
     * @returns true se la select ha pi' du threshold opzioni; false altrimenti
     */
    function selectHasOptions(select, threshold) {
        return select.find('option').length > threshold;
    }
    
    /**
     * Caricamento della select tramite la lista fornita.
     *
     * @param select           (jQuery)  la select da popolare
     * @param list             (Array)   la lista tramite cui popolare la select
     * @param addEmptyOption   (Boolean) se aggiungere una option vuota (optional - default - false)
     * @param emptyOptionValue (String)  il valore da impostare per l'opzione vuota (optional - default - '')
     * @param emptyOptionText  (String)  il testo da impostare per l'opzione vuota (optional - default - '')
     * @return (jQuery) la select su cui si e' agito
     */
    function caricaSelect(select, list, addEmptyOption, emptyOptionValue, emptyOptionText) {
        var initialValue = !!addEmptyOption
            ? '<option value="' + (emptyOptionValue || '') + '">' + (emptyOptionText || '') + '</option>'
            : '';
        var str = list.reduce(function(acc, val) {
            return acc + '<option value="' + val.uid + '">' + val.codice + ' - ' + val.descrizione + '</option>';
        }, initialValue);
        return select.html(str);
    }
    
    /**
     * Caricamento delle liste della classe.
     */
    function caricaListaClasse() {
        // classePianoDeiConti_modale
        var select = $('#classePianoDeiConti_modale');
        // Se sono gia' popolate, non ricarico
        if(selectHasOptions(select, 1)) {
            return;
        }
        select.overlay('show');
        $.postJSON('ricercaCausaleEPFIN_caricaListaClassi.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect(select, data.listaClassi, true);
        }).always(select.overlay.bind(select, 'hide'));
    }
    
    /**
     * Caricamento delle liste della classe.
     */
    function caricaListaClasseSoggetto() {
        // classePianoDeiConti_modale
        var select = $('#classificatoreSoggetto_modale');

        // Se sono gia' popolate, non ricarico
        if(selectHasOptions(select, 1)) {
            return;
        }
        select.overlay('show');
        $.postJSON('ricercaCausaleEPFI_caricaListaClasseSoggetto.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect(select, data.listaClasseSoggetto, true);
        }).always(select.overlay.bind(select, 'hide'));
    }

    /**
     * Caricamento delle liste dei titoli.
     */
    function caricaListaTitolo() {
        var selectEntrataFIN = $('#titoloEntrataFIN');
        var selectSpesaFIN = $('#titoloSpesaFIN');
        var selectEntrata = $('#titoloEntrata');
        var selectSpesa = $('#titoloSpesa');

        var selects = selectEntrata.add(selectSpesa).add(selectEntrataFIN).add(selectSpesaFIN);
        // Se sono gia' popolate, non ricarico
        if(selectHasOptions(selectEntrata, 1) && selectHasOptions(selectSpesa, 1) && selectHasOptions(selectEntrataFIN, 1) && selectHasOptions(selectSpesaFIN, 1)) {
            return;
        }
        selects.overlay('show');
        $.postJSON('ricercaCausaleEPFI_caricaListaTitoli.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect(selectEntrata, data.listaTitoloEntrata, true);
            caricaSelect(selectEntrataFIN, data.listaTitoloEntrata, true);
            caricaSelect(selectSpesa, data.listaTitoloSpesa, true);
            caricaSelect(selectSpesaFIN, data.listaTitoloSpesa, true);
        }).always(selects.overlay.bind(selects, 'hide'));
    }
    
    
    /**
     * Caricamento degli eventi.
     */
    function caricaEvento(mayTriggerChange) {
        var tipoEvento = $("#tipoEvento").val();
        var selectEvento = $("#evento");
        var oldValue = selectEvento.val();
        var eventoDaRicerca = $("#uidEventoDaRicerca");
        var tipoEventoDaRicerca = $("#uidTipoEventoDaRicerca");
        $('#causale').val('').attr('disabled', true);
        gestisciMovimentoFinanziario();

        if(!tipoEvento) {
            // Se non ho selezionato il tipo di evento, disabilito l'evento ed esco
            selectEvento.attr("disabled", true)
                .val("");
            if(mayTriggerChange) {
            	selectEvento.change();
            }
            return;
        }

        // Attivo l'overlay
        selectEvento.overlay("show");
        $.postJSON("ricercaEventoByTipoEvento.do", {'tipoEvento.uid': tipoEvento}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            	if (!!eventoDaRicerca.val()) {
            		selectEvento.val(eventoDaRicerca.val());
                    if (tipoEventoDaRicerca.val()=="") {
                    	//lo azzero solamente se e' azzerato anche l'altro 
                    	eventoDaRicerca.val("");
                    }
                }
                return;
            }
            // Non ho errori: carico la select
            caricaSelect(selectEvento, data.listaEvento, true);
            impostaValoreOld(selectEvento, data.listaEvento, !!eventoDaRicerca.val() ? eventoDaRicerca.val() :oldValue);
            if (!!eventoDaRicerca.val() && (tipoEventoDaRicerca.val()=="")) {
            	//lo azzero solamente se e' azzerato anche l'altro 
            	eventoDaRicerca.val("");
        }
            // Tolgo il disabled
            selectEvento.removeAttr("disabled") .change();
        }).always(function() {
        	
            
            selectEvento.overlay("hide");
        });
    }

    
    /**
     * Abilita o disabilita la gestione dell'evento finanziario
     */
    function gestisciMovimentoFinanziario() {
        $("#annoMovimento, #numeroMovimento, #numeroSubmovimento")[$("#tipoEvento").val() ? "removeAttr" : "attr"]("disabled", true);
    }
   
    /*********************************************************AGGIUNTI IL 13/01/2015 CR 2865 FINE ************************************/


    $(function () {
    	var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
    	// aggiunti in data 13/01/2015 in seguito alla CR 2868
    	var selectTipoEvento = $("#uidTipoEvento");
    	
        Conto.inizializza("#uidClassePianoPianoDeiContiContoContoTipoOperazione", undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
        ContoFIN.inizializza("#codiceElementoPianoDeiConti","#pulsanteCompilazioneGuidataElementoPianoDeiConti");
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_denominazioneSoggetto", "#descrizioneCompletaSoggetto","#pulsanteCompilazioneGuidataSoggetto");
        $("#uidTipoEvento").change(caricaEventi).change();
        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	var def = Soggetto.caricaDettaglioSoggetto( campoCodiceSoggetto, $(), $(), $("#descrizioneCompletaSoggetto"),undefined,undefined, undefined,true);
        	if(!params || !params.doNotCloseAlerts) {
        		def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
        			impostaDatiNegliAlert(errori, alertErrori);
            		return errori;
            	});
        	}
        })
        // Forzo la chiamata al metodo
        	.trigger('change', {doNotCloseAlerts: true});

        caricaListaTitolo();
        caricaListaClasse();
        caricaListaClasseSoggetto();
        caricaListaTipoEvento(function() {

            if(selectTipoEvento.val()) {
            	caricaEvento(false);
            }
        });
    });
}(jQuery);