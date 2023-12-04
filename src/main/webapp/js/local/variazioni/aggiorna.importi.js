/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 3 delle variazioni - variazione di importi senza gestione UEB */
var Variazioni = Variazioni || {};

var VariazioniImporti = (function(varImp){
	"use strict";
	var alertMessaggi = $("#MESSAGGI");
	var alertInformazioni = $("#INFORMAZIONI");
	var $divPerOverlay = $('.overlay-on-submit');
	//utilities
	varImp.chiediConfermaAnnullamento = chiediConfermaAnnullamento;
	//capitoli
	varImp.salvaVariazione = salvaVariazione;
	varImp.concludiAggiornamentoVariazione = concludiAggiornamentoVariazione;
	varImp.chiudiPropostaAggiornamentoVariazione = chiudiPropostaAggiornamentoVariazione;
       
    function callbackRichiestaProsecuzioneSuSquadraturaCassa(ulterioreCheck){
    	 $('form').append('<input type= "hidden" name ="saltaCheckStanziamentoCassa" value ="true" >');
    	 if(ulterioreCheck){
    		 return impostaConfermaProvvedimento();
    	 }
    	 aggiornaAnagraficaVariazioneAsync('concludi');
    }
    
    //SIAC-7629 inizio FL 
    function callbackRichiestaProsecuzioneSuSquadraturaImporti(ulterioreCheck){
   	 $('form').append('<input type= "hidden" name ="saltaCheckStanziamentoCassa" value ="true" >');
 		$('form').append('<input type= "hidden" name ="saltaCheckStanziamento" value ="true" >');
   	 if(ulterioreCheck){
   		 return impostaConfermaProvvedimento();
   	 }
   	 aggiornaAnagraficaVariazioneAsync('chiudiProposta');
   }
   //SIAC-7629 fine FL

    /**
     * Se il servizio viene implementato in modo asincrono permette all'utente di scegliere se rimanere
     * sulla pagina oppure tornare alla home
     */
    function showDialogAbbandonoPaginaSuServizioAsincrono(operazione) {
        bootboxAlert('L\'elaborazione non &egrave ancora terminata. &Egrave possibile rimanere sulla pagina oppure tornare alla home.', 'Attenzione', 'dialogWarn', [
            {
                "label" : "Torna alla home"
                , "class" : "btn"
                , "callback": function(){
                    document.location="/siacbilapp/redirectToCruscotto.do";
                }
            }
            , {
                "label" : "Rimani sulla pagina"
                , "class" : "btn"
                , "callback" : function() {
                	$divPerOverlay.overlay('show');
                    setTimeout(ottieniResponse,30000,operazione, 50, 30000);
                }
            }]);
    }
    
	/**
	 * Gestisce la conferma di prosecuzione Per il provvedimento della variazione di bilancio.
	 * */
    function callbackConfermaRichiestaProsecuzionePerProvvedimentoVariazioneBilancio(){
    	$('form').append('<input type= "hidden" name ="saltaCheckProvvedimentoVariazioneBilancio" value ="true" >');
        aggiornaAnagraficaVariazioneAsync('concludi');
    }

    /* ***************************************************************** ***/
    /* **********  GESTIONE ASSOCIAZIONE CAPITOLI E VARIAZIONI  ******* ***/
    /* **************************************************************** ***/

    /* ****************************************************************** ***/
    /* ****************  CALLBACK VARI DOPO CHIAMATA AL SERVIZIO  ******* ***/
    /* ****************************************************************** ***/
    function chiediConfermaAnnullamento(){
    	$('#msgAnnulla').modal('show');
    	$('#EDIT_annulla').substituteHandler("click",annullaAnagraficaVariazioneAsync);
    }
    function salvaVariazione(){
        aggiornaAnagraficaVariazioneAsync('salva');
    }

    function concludiAggiornamentoVariazione(){
        aggiornaAnagraficaVariazioneAsync('concludi');
    }
    
    function chiudiPropostaAggiornamentoVariazione(){
    	chiudiPropostaAsync('chiudiProposta');
    }
    
    

    function annullaAnagraficaVariazioneAsync(){
    	$('#msgAnnulla').modal('hide');
        aggiornaAnagraficaVariazioneAsync('annulla');
    }

    function aggiornaAnagraficaVariazioneAsync(operazione){
        var form = $('form');
        var obj = qualify(form.serializeObject());
        $divPerOverlay.overlay('show');
        $('#spinner_' + operazione).addClass('activated');

        return $.postJSON(operazione + 'AggiornamentoVariazioneImporti.do', obj)
        .then(function(data) {
            var alertErrori = $('#ERRORI');
            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	resettaSpinnerFormSubmitted();
                return;
            }
			//SIAC-8261
			impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            ottieniResponse(operazione, 10, 10000);
        });
    }
    
    
    
    function chiudiPropostaAsync(operazione){
        var form = $('form');
        var obj = qualify(form.serializeObject());
        $divPerOverlay.overlay('show');
        $('#spinner_' + operazione).addClass('activated');
        return $.postJSON(operazione + 'AggiornamentoVariazioneImporti.do', obj)
        .then(function(data) {
            var alertErrori = $('#ERRORI');
            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	resettaSpinnerFormSubmitted();
                return;
            }
            ottieniResponse(operazione, 10, 10000);
        });
    }
    

    function resettaSpinnerFormSubmitted(){
        $divPerOverlay.overlay('hide');
        $('[data-spinner-async]').removeClass('activated'); 
    } 
    
    function ottieniResponse(operazione, tentativiRimanenti, timeout){
        var url = operazione + 'AggiornamentoVariazioneImportiAsyncResponse.do';
        return $.postJSON(url)
        .then(function(data) {
            var alertErrori = $('#ERRORI');
            var richiediConfermaProvvedimento;
            
            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	
                $divPerOverlay.overlay('hide');
            	redirezioneAPaginaDisabilitata();
                return;
            }

            if(data.isAsyncResponsePresent === undefined){
            	impostaDatiNegliAlert(['COR_ERR_0001 - Errore di sistema: impossibile ottenere la risposta asincrona.'], alertErrori);
                resettaSpinnerFormSubmitted();
                return;
            }


            if(!data.isAsyncResponsePresent){
               /* SIAC-8261
				 if(tentativiRimanenti<=0){
                    //se i tentativi rimanenti sono azzerati chiedo se vuoi continuare ricorsione se sì continuo, altrimenti return.
                	$divPerOverlay.overlay('hide');

                	showDialogAbbandonoPaginaSuServizioAsincrono(operazione);
                    return;
                }*/
                                                                 //SIAC-8261
                setTimeout(ottieniResponse, timeout, operazione, /*--*/ tentativiRimanenti, timeout);
                return;
            }
            
            richiediConfermaProvvedimento = data.richiediConfermaMancanzaProvvedimentoVariazioneBilancio;
            
            if(data.richiediConfermaQuadratura){
            	var fnc_callback_indietro = richiediConfermaProvvedimento ? impostaConfermaProvvedimento : $.noop;
            	resettaSpinnerFormSubmitted();
            	return impostaRichiestaConfermaUtente('Non vi &eacute; quadratura sulla cassa. Proseguire ugualmente con il salvataggio?', 
    						callbackRichiestaProsecuzioneSuSquadraturaCassa.bind(undefined , richiediConfermaProvvedimento),
    						fnc_callback_indietro);
            }
            
            
            //SIAC-7629 inizio FL 
            if(data.richiediConfermaQuadraturaCP){
            	var fnc_callback_indietro = richiediConfermaProvvedimento ? impostaConfermaProvvedimento : $.noop;
            	resettaSpinnerFormSubmitted();
            	return impostaRichiestaConfermaUtente('Quadratura entrate-spese non corretta. Proseguire ugualmente con il salvataggio?', 
    						callbackRichiestaProsecuzioneSuSquadraturaImporti.bind(undefined , richiediConfermaProvvedimento),
    						fnc_callback_indietro);
            }
            //SIAC-7629 fine FL
            
            if(richiediConfermaProvvedimento){
            	resettaSpinnerFormSubmitted();
            	return impostaConfermaProvvedimento();
            }
            
            redirezioneAPaginaDisabilitata();
            //$divPerOverlay.overlay('hide');
        });

    }

    function gestioneConfermaUtente(richiediConfermaQuadratura, richiediConfermaProvvedimento){
    	return richiediConfermaQuadratura ?
    			true
    			: impostaConfermaProvvedimento();
    }
    
    function impostaConfermaProvvedimento(){
    	return impostaRichiestaConfermaUtente('Non &egrave; stato selezionato il provvedimento di variazione di bilancio definitivo: sei sicuro di voler proseguire?',
				callbackConfermaRichiestaProsecuzionePerProvvedimentoVariazioneBilancio);
    }
    function redirezioneAPaginaDisabilitata(){
    	var redirect=$("#redirectAction").val();
    	var redirectAction = "/siacbilapp/" + redirect + ".do"
    	document.location = redirectAction;

    }
	return varImp;

}(Variazioni || {}));

$(function() {
	
	CapitoloInVariazione.init();
    //lego gli handlers: variazioni
    $("#aggiornaVariazioneButton").substituteHandler("click", VariazioniImporti.salvaVariazione);
    $("#concludiVariazioneButton").substituteHandler("click", VariazioniImporti.concludiAggiornamentoVariazione);
    $("#chiudiPropostaButton").substituteHandler("click", VariazioniImporti.chiudiPropostaAggiornamentoVariazione);
    
    $('#annullaVariazioneButton').substituteHandler('click', VariazioniImporti.chiediConfermaAnnullamento);
    if (typeof stampaVariazione === 'function') {
    	$('#stampaVariazioneButton').substituteHandler('click', stampaVariazione);
    }
    
    
    $("form").substituteHandler("reset", function() {
        document.location = 'aggiornaVariazioneImporti.do';
    });


    // Se non sono nella pagina disabilitata, svuoto il form del provvedimento e inizializzo la gestione
    if(!$("#DISABLED").length && Provvedimento) {
    	var provvedimento = new Provvedimento(undefined, 'Nessun provvedimento variazione di PEG selezionato', 'provvedimento variazione di PEG',undefined, true);
    	//questo provvedimento non carica la sac, per non andare in concurrent modification exception, ma attende che la chiamata venga fatta dall'altro provvedimento
    	provvedimento.inizializza();
    	
    	var provvedimentoVariazioneDiBilancio = new Provvedimento('Aggiuntivo', 'Nessun provvedimento variazione di bilancio selezionato', 'provvedimento variazione di bilancio');
    	provvedimentoVariazioneDiBilancio.inizializza();
    }
    
    // SIAC-5016
    $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'aggiornaVariazioneImporti_download.do', false));
    $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'aggiornaVariazioneImporti_download.do', true));
    
});
