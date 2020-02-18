/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RateiRisconti = (function($) {
    var exports = {};
	
    var baseUrlRateiRisconti = "gestioneRateiERiscontiPrimaNotaIntegrata";
    exports.inserimentoRateo = inserimentoRateo;
    exports.aggiornamentoRateo  = aggiornamentoRateo;
    exports.gestioneRisconti = gestioneRisconti;
     
     
   /**
    * preparzione e apertura modale per inserimento rateo
    */
  function inserimentoRateo(e){
   	var primaNota = e.data[0];
   	var documentLocation = $(e.target).data('href-documento'); 
   	var uidDocumento = $(e.target).data('uid-documento'); 
   	if(documentLocation){
   		gestisciRedirezioneDocumento(documentLocation, uidDocumento);
   		return;
   	}
   	var annoBilacio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
   	RateiRiscontiBase.impostaModaleRateo(primaNota,undefined, annoBilacio-1, 'inserisciRateo');
   }
   
      
   /**
    * preparzione e apertura modale per aggiornamento rateo
    */
   function aggiornamentoRateo(e){
   	var primaNota = e.data[0];
   	var rateo = primaNota.rateo;
   	RateiRiscontiBase.impostaModaleRateo(primaNota,rateo, rateo.anno, 'aggiornaRateo');
   }
   
   function gestisciRedirezioneDocumento(documentLocation, uidDocumento){
	  $.postJSON(baseUrlRateiRisconti + '_controlloNotaCredito.do', {'uidDocumento' : uidDocumento}).then(function(data){
		   if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
			   return;
		   }
		   if(data.esisteNCDCollegataADocumento){
			   impostaRichiestaConfermaUtente("Il documento relativo alla prima nota risulta essere collegato ad una nota di credito. Proseguire ugualmente?", redirezioneToDocumentLocation.bind(undefined,documentLocation));
			   return;
		   }
		   redirezioneToDocumentLocation(documentLocation);
	   });
	  
   }
   
   function redirezioneToDocumentLocation(documentLocation){
	   $('body').overlay('show');
	   document.location = documentLocation;
   }
   
   
   /**
    * preparzione e apertura modale per inserimento/aggiornamento risocnti
    */
   function gestioneRisconti(e){
	   	$("#tabellaRisultatiRicerca").overlay("show");
	   	var primaNota = e.data[0];
	   	var documentLocation = $(e.target).data('href-documento'); 
	   	var uidDocumento = $(e.target).data('uid-documento'); 
	   	if(documentLocation){
	   		gestisciRedirezioneDocumento(documentLocation, uidDocumento);
	   		return;
	   	}
	   	RateiRiscontiBase.impostaModaleRisconti(primaNota)
	   		.always(function(){
			$("#tabellaRisultatiRicerca").overlay("hide");
	    });
   }
   
   return exports;
   
}(jQuery));    

$(function() {
	$('#tabellaRisultatiRicerca').overlay({rebind: true});
	
	RateiRiscontiBase.onLoadPage();
	
    $("#pulsanteChiudiModaleRisconti, #buttonCloseModal").click(function(){
    	var spinner = $("#SPINNER_pulsanteChiudiModaleRisconti").addClass("activated");
    	RisultatiRicercaPrimeNote.impostaTabellaPrimaNota()
         .then(function() {
        	 spinner.removeClass("activated");
        	 $("#collapseInserimentoAggiornamentoRisconto").slideUp();
        	 $("#modaleInserimentoAggiornamentoRisconti").modal("hide");
         });
    });
   
    
});
