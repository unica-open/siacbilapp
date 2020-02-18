/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RateiRiscontiBase = (function($) {
    var exports = {};
	
    var alertErrori = $("#ERRORI");
    var baseUrlRateiRisconti = "gestioneRateiERiscontiPrimaNotaIntegrata";
    //ratei
    exports.impostaModaleRateo = impostaModaleRateo;
    exports.salvaRateo = salvaRateo;
    
    
    //risconti
    exports.impostaModaleRisconti = impostaModaleRisconti;
    exports.iniziaInserimentoRisconto = iniziaInserimentoRisconto;
    exports.salvaRisconto =  salvaRisconto;
    
    //load della pagina
    exports.onLoadPage = onLoadPage;
    
    /***
     * Funzioni necessarie al caricamento della pagina.
     * */
    function onLoadPage(){
    	//rateo
    	$("#pulsanteSalvaRateo").click(salvaRateo);
        //risconto
    	$("#pulsanteInserisciRisconto").click(iniziaInserimentoRisconto);
        $("#pulsanteSalvaRisconti").click(salvaRisconto);
        $("#pulsanteAnnullaRisconti").click(function(){
    		$("#collapseInserimentoAggiornamentoRisconto").slideUp();
    	});
    }
    
    
    /* ##########################################################################################################
     * ##########################################################################################################

     *										       RATEI 
     * 
     * ##########################################################################################################
     * */
    
    /**
     * Imposta la modale del rateo.
     * */
    function impostaModaleRateo(primaNota,rateo, annoRateo, suffissoAttivita){
    	$('#INFORMAZIONI_modaleRateo').slideUp();
    	$('#ERRORI_modaleRateo').slideUp();
    	$('#annoRateo_modale').val(annoRateo);
        
        $('#HIDDEN_primaNotaRateo').val(primaNota.uid);
        $('#HIDDEN_suffixRateo').val(suffissoAttivita);
        if(rateo){
        	 $('#HIDDEN_uidRateo').val(rateo.uid);
        	 $('#importoRateo_modale').val(rateo.importo.formatMoney());
        }else{
        	//devo sbiancare il dato
        	$('#importoRateo_modale').val("");
        }
        
       
    	$('#modaleInserimentoAggiornamentoRateo').modal("show");

    }
    
    /**
     * salvataggio del rateo 
     */
    function salvaRateo() {
        var fieldset = $("#fieldsetModaleRateo");
        var obj = fieldset.serializeObject();
        var suffix = $("#HIDDEN_suffixRateo").val();
        var event;

        // Attivo l'overlay
        fieldset.overlay("show");
        return $.postJSON(baseUrlRateiRisconti + "_" + suffix + ".do", obj, function(data) {
        	var rateo;
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_modaleRateo"))) {
            	fieldset.overlay("hide");
                return;
            }
            impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"));
            rateo = data.rateo;
            event = $.Event('rateoSalvato', {'rateo': rateo});
        	$(document).trigger(event);
        	$("#modaleInserimentoAggiornamentoRateo").modal("hide");
        }).then(function() {
            	fieldset.overlay("hide");
        });
    }
    
    /* ##########################################################################################################
     * ##########################################################################################################
     *										       RISCONTI 
     * 
     * ##########################################################################################################
     * */
    
    /**
     * preparzione e apertura collapse per inserimento nuovo risocnto
     */
    function iniziaInserimentoRisconto(){
	   	 var annoBilacio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
	   	 $("#annoRisconto_modale").val(annoBilacio+1);
	     $("#importoRisconto_modale").val("");
	     $("#HIDDEN_suffixRisconto").val("inserisciRisconto");
	     $("#HIDDEN_idxRisconto").val("");	        
	     $("#collapseInserimentoAggiornamentoRisconto").slideDown();
   }
   
   /**
    * salvataggio del risconto
    */
   function salvaRisconto() {
	   var fieldset = $("#fieldsetModaleRisconti");
	   var obj = fieldset.serializeObject();
	   var suffix = $("#HIDDEN_suffixRisconto").val();
	
	   // Attivo l'overlay
	   fieldset.overlay("show");
	   return $.postJSON(baseUrlRateiRisconti + "_" + suffix + ".do", obj, function(data) {
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
   }
   
    
    
    function impostaModaleRisconti(primaNota){
    	$("#INFORMAZIONI_modaleRisconti").slideUp();
    	$("#ERRORI_modaleRisconti").slideUp();
    	$("#HIDDEN_primaNotaRisconto").val(primaNota.uid);
    	$('#collapseInserimentoAggiornamentoRisconto').slideUp();
    	return $.postJSON(baseUrlRateiRisconti + "_impostaRiscontiInseriti.do", {uidPrimaNota: primaNota.uid}, function(data) {
    		 if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 return;
             }
    		popolaTabellaRiscontiInseriti(data.riscontiGiaInseriti);
    		$("#modaleInserimentoAggiornamentoRisconti").modal({backdrop : 'static', keyboard : false, show : true});
    	});
    }
    
    /**
     * Popolamento della tabella delle prime note collegate.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function popolaTabellaRiscontiInseriti(list) {
        var tableId = "tabellaRiscontiInseriti";

        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessun risconto inserito",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source){
                	return source.anno;
                }},
                {aTargets: [1], mData: function(source){
                	return source.importo.formatMoney();
                }},
                //Azioni
                {aTargets: [2], mData: function() {
                    return "<div class=\"btn-group\">" +
                               "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                               "<ul class=\"dropdown-menu pull-right\">" +
                                   "<li><a class=\"aggiornaRisconto\">aggiorna</a></li>" +
                               "</ul>" +
                           "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaRisconto")
                            .click([oData, iRow], iniziaAggiornamentoRisconto);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }
    
    /**
     * preparzione e apertura collapse per aggiornamento risconto
     */
   function iniziaAggiornamentoRisconto(e){
      var idx = e.data[1];
      var risconto = e.data[0];
      $("#annoRisconto_modale").val(risconto.anno);
      $("#importoRisconto_modale").val(risconto.importo.formatMoney());
      $("#HIDDEN_suffixRisconto").val("aggiornaRisconto");
      $("#HIDDEN_idxRisconto").val(idx);
      
      $("#collapseInserimentoAggiornamentoRisconto").slideDown();
   }
   
   /**
    * preparzione e apertura modale per inserimento rateo
    */
  function inserimentoRateo(e){
   	var primaNota = e.data[0];
   	var annoBilacio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
   	impostaModaleRateo(primaNota,undefined, annoBilacio-1, 'inserisciRateo');
   }
   
      
   /**
    * preparzione e apertura modale per aggiornamento rateo
    */
   function aggiornamentoRateo(e){
   	var primaNota = e.data[0];
   	var rateo = primaNota.rateo;
   	impostaModaleRateo(primaNota,rateo, rateo.anno, 'aggiornaRateo');
   }
   
   return exports;
   
}(jQuery));    

