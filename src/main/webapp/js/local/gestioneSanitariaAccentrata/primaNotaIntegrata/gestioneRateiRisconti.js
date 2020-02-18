/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RateiRiscontiGSA = (function($) {
    var exports = {};
	
    var baseUrlRateiRisconti = "gestioneRateiERiscontiPrimaNotaIntegrata";
    
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
     * salvataggio del rateo 
     */
    exports.salvaRateo = function() {
        var fieldset = $("#fieldsetModaleRateo");
        var obj = fieldset.serializeObject();
        var suffix = $("#HIDDEN_suffixRateo").val();

        // Attivo l'overlay
        fieldset.overlay("show");
        return $.postJSON(baseUrlRateiRisconti + "_" + suffix + ".do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_modaleRateo"))) {
            	fieldset.overlay("hide");
                return;
            }
            impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"));
            RisultatiRicercaPrimeNoteGSA.impostaTabellaPrimaNota();
            fieldset.overlay("hide");
            $("#modaleInserimentoAggiornamentoRateo").modal("hide");
            
        });
    };
    
    /**
     * preparzione e apertura collapse per inserimento nuovo risocnto
     */
    exports.iniziaInserimentoRisconto = function(){
   	 var annoBilacio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
   	 $("#annoRisconto_modale").val(annoBilacio+1);
     $("#importoRisconto_modale").val("");
     $("#HIDDEN_suffixRisconto").val("inserisciRisconto");
     $("#HIDDEN_idxRisconto").val("");
        
     $("#collapseInserimentoAggiornamentoRisconto").slideDown();
   };
   
   /**
    * salvataggio del risconto
    */
   exports.salvaRisconto = function() {
       var fieldset = $("#fieldsetModaleRisconti");
       var obj = fieldset.serializeObject();
       var suffix = $("#HIDDEN_suffixRisconto").val();

       // Attivo l'overlay
       fieldset.overlay("show");
       $.postJSON(baseUrlRateiRisconti + "_" + suffix + ".do", obj, function(data) {
           if(impostaDatiNegliAlert(data.errori, $("#ERRORI_modaleRisconti"))) {
           	fieldset.overlay("hide");
               return;
           }
           impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI_modaleRisconti"));
           popolaTabellaRiscontiInseriti(data.riscontiGiaInseriti);
           $("#collapseInserimentoAggiornamentoRisconto").slideUp();
           }).always(function(){
           	fieldset.overlay("hide");
           });
   };
   
    /**
     * preparzione e apertura modale per inserimento rateo
     */
   exports.inserimentoRateo = function(e){
	   	var primaNota = e.data[0];
	   	var documentLocation = $(e.target).data('href-documento'); 
	   	var uidDocumento = $(e.target).data('uid-documento'); 
	   	if(documentLocation){
	   		gestisciRedirezioneDocumento(documentLocation, uidDocumento);
	   		return;
	   	}
	   	var annoBilacio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
	   	RateiRiscontiBase.impostaModaleRateo(primaNota,undefined, annoBilacio-1, 'inserisciRateo');
//	   };    	
    };
    
   
    /**
     * preparzione e apertura modale per aggiornamento rateo
     */
    exports.aggiornamentoRateo = function(e){
    	var primaNota = e.data[0];
    	var rateo = primaNota.rateo;
       	var documentLocation = $(e.target).data('href-documento'); 
       	var uidDocumento = $(e.target).data('uid-documento'); 
       	if(documentLocation){
       		gestisciRedirezioneDocumento(documentLocation, uidDocumento);
       		return;
       	}
       	RateiRiscontiBase.impostaModaleRateo(primaNota,rateo, rateo.anno, 'aggiornaRateo');
    };
    
    /**
     * preparzione e apertura modale per inserimento/aggiornamento risocnti
     */
    exports.gestioneRisconti = function(e){
    	var primaNota = e.data[0];
    	var documentLocation = $(e.target).data('href-documento'); 
    	var uidDocumento = $(e.target).data('uid-documento');
    	
    	$("#tabellaRisultatiRicerca").overlay("show");
    	$("#INFORMAZIONI_modaleRisconti").slideUp();
    	$("#ERRORI_modaleRisconti").slideUp();
    	$("#HIDDEN_primaNotaRisconto").val(primaNota.uid);
    	
       	if(documentLocation){
       		gestisciRedirezioneDocumento(documentLocation, uidDocumento);
       		return;
       	}
       	RateiRiscontiBase.impostaModaleRisconti(primaNota)
       		.always(function(){
    		$("#tabellaRisultatiRicerca").overlay("hide");
        });
    };
    
   return exports;
   
}(jQuery));    

$(function() {
	$('#tabellaRisultatiRicerca').overlay({rebind: true});
	
	RateiRiscontiBase.onLoadPage();
    $("#pulsanteChiudiModaleRisconti, #buttonCloseModal").click(function(){
    	var spinner = $("#SPINNER_pulsanteChiudiModaleRisconti").addClass("activated");
    	RisultatiRicercaPrimeNoteGSA.impostaTabellaPrimaNota()
         .then(function() {
        	 spinner.removeClass("activated");
        	 $("#collapseInserimentoAggiornamentoRisconto").slideUp();
        	 $("#modaleInserimentoAggiornamentoRisconti").modal("hide");
         });
    });

});
