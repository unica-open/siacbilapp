/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RateiRisconti = (function($) {
    var exports = {};
    
    exports.attivaDataTablePrimeNoteCollegate = attivaDataTablePrimeNoteCollegate;
    exports.ottieniDatiFinanziari = ottieniDatiFinanziari;
    exports.caricaElencoScritture = caricaElencoScritture;
    exports.attivaDataTableQuote = attivaDataTableQuote;
    exports.impostaRateoDopoAggiornamento = impostaRateoDopoAggiornamento;
	
   
    var baseOptsDataTable = {
    		bPaginate: true,
            bLengthChange: false,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
              	sInfo: "_START_ - _END_ di _MAX_ risultati",
              	sInfoEmpty: "0 risultati",
               	sProcessing: "Attendere prego...",
               	sZeroRecords: "Nessun dato disponibile",
               	oPaginate: {
               		sFirst: "inizio",
               		sLast: "fine",
               		sNext: "succ.",
               		sPrevious: "prec.",
               		sEmptyTable: "Nessun dato disponibile"
               	}
            },
            aoColumnDefs : []
    };

    var optsComuniDatiFinanziari = {
        	bServerSide:true,
        	sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDatiFinanziariPrimaNotaIntegrataFINAjax.do",
            iDisplayLength: 10,
            fnDrawCallback: function () {
            	$('#divDatiFinanziari').overlay('hide');
            }
        };

    var optsComuniSenzaPaginazione= {
    		bServerSide: false,
            iDisplayLength: 5
        };

    var baseOpts = $.extend(true, {}, baseOptsDataTable, optsComuniSenzaPaginazione);

    var baseOptsDatiFinanziari =  $.extend(true, {}, baseOptsDataTable, optsComuniDatiFinanziari);

    /**
     * Estrae l'importo di dato tipo (DARE O AVERE)
     * */
    function estraiImportoDareAvere(tipo) {
    	return function(source) {
    		if(source.movimentoDettaglio.segno && source.movimentoDettaglio.segno._name === tipo) {
                var res = source.movimentoDettaglio.importo || 0;
                return res.formatMoney();
            }
            return "";
    	};
    }


    function attivaDataTableQuote(){
    	var optsQuote = {
                fnDrawCallback: function () {
                    // Nascondo il div del processing
                	$("#tabellaQuote").find('a.btn').substituteHandler('click', caricaElencoScritture);
                }
            };
    	var opts = $.extend(true, {}, baseOpts, optsQuote);
    	$("#tabellaQuote").dataTable(opts);
    	$("#elencoScrittureQuota").slideUp();

    }



    /**
     * Impostazione della tabella.
     */
    function attivaDataTablePrimeNoteCollegate() {
        var tableId = "#tabellaPrimeNoteCollegate";
        var optsNoteCollegate = {
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        var opts = $.extend(true, {}, baseOpts, optsNoteCollegate);
        $(tableId).dataTable(opts);
    }

    function attivaDataTableSubdocSpesa() {
        var optsDocumentoSpesa = {
        	aoColumnDefs : [
        		{aTargets: [0], mData: defaultPerDataTable('numero')},
    			{aTargets: [1], mData: defaultPerDataTable('importo')},
    			{aTargets: [2], mData: defaultPerDataTable('movimentoGestione')},
    			{aTargets: [3], mData: defaultPerDataTable('pianoDeiConti')},
    			{aTargets: [4], mData: defaultPerDataTable('liquidazione')},
    			{aTargets: [5], mData: defaultPerDataTable('ordinativo')}
    			]
        };

        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoSpesa);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }


    function attivaDataTableSubdocEntrata() {
        var optsDocumentoEntrata = {
    		aoColumnDefs: [
    			{aTargets: [0], mData: defaultPerDataTable('numero')},
    			{aTargets: [1], mData: defaultPerDataTable('importo')},
    			{aTargets: [2], mData: defaultPerDataTable('movimentoGestione')},
    			{aTargets: [3], mData: defaultPerDataTable('pianoDeiConti')},
    			{aTargets: [4], mData: defaultPerDataTable('ordinativo')}
    		]
        };
        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoEntrata);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }

    function attivaDataTableScritture(list) {
        var tableId = "tabellaScritture";
        var optsScritture = {
        	aaData: list,
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
            	{aTargets: [0], mData: defaultPerDataTable('movimentoDettaglio.conto.codice')},
            	{aTargets: [1], mData: defaultPerDataTable('movimentoDettaglio.conto.descrizione')},
                {aTargets: [2], mData: estraiImportoDareAvere('DARE'), fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");

                }},
                {aTargets: [3], mData: estraiImportoDareAvere('AVERE'),  fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], mData: function(source) {
                    return "";
                }}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, optsScritture);
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Ottiene i dati finanziari
     */
    function ottieniDatiFinanziari(e) {
        var header = $('#headingAccordionDatiFinanziari');
        var div = $('#divDatiFinanziari');
        var obj;

        if(header.data('loaded')) {
            return;
        }
        e.preventDefault();
        e.stopPropagation();
        obj = {'primaNota.uid': $('#uidPrimaNotaIntegrata').val(), 'tipoCollegamento': $('#tipoCollegamentoDatiFinanziari').val()};
        header.overlay('show');

        return $.post('gestioneRateiRiscontiPrimaNotaIntegrataDocumentoFIN_ottieniDatiFinanziari.do', obj)
        .then(function(data) {
        	var tipoCollegamento = $('#tipoCollegamentoDatiFinanziari').val();
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                return;
            }
            div.html(data);
            header.data('loaded', true);
            div.collapse('show');
            if(tipoCollegamento==='SUBDOCUMENTO_SPESA'){
            	div.overlay('show');
            	attivaDataTableSubdocSpesa();
            }else if(tipoCollegamento==='SUBDOCUMENTO_ENTRATA'){
            	div.overlay('show');
            	attivaDataTableSubdocEntrata();
            }

        }).always(header.overlay.bind(header, 'hide'));
    }
    
    function aggiornaDatiScritture (data) {
        var listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
        attivaDataTableScritture(listaMovimentiPerTabella);
        $("#totaleDare").html(data.totaleDare.formatMoney());
        $("#totaleAvere").html(data.totaleAvere.formatMoney());
    }
     
    
    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaElencoScritture(e) {
    	 var overlay = $("#tabellaScritture").overlay("show");
    	 e.preventDefault();
    	 var clickedButton = e && e.target;
     	var uidMovimentoEP = $(clickedButton).data('mov');
     	$("#elencoScrittureQuota").slideDown();
         $.postJSON("gestioneRateiRiscontiPrimaNotaIntegrataDocumentoFIN_ottieniListaConti.do", {uidMovimentoEPPerScritture: uidMovimentoEP})
         .then(function(data) {
        	 var rateoPrimaNota = data.rateoPrimaNota;
        	 if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                 // Svuoto la select ed esco
                 return;
             }
             aggiornaDatiScritture(data);
             $('#pulsanteInserimentoAggiornamentoRisconti').substituteHandler('click', smistaRateiRisconti.bind(undefined, rateoPrimaNota));
         }).always(function() {
             overlay.overlay("hide");
         });
    }
    
    function popolaPulsante(rateoPrimaNota){
    	$('#pulsanteInserimentoAggiornamentoRisconti').data();
    }
    
    function smistaRateiRisconti(rateoPrimaNota){
    	var primaNota = {};
    	var annoBilancio = parseInt($("#HIDDEN_annoBilancio").val(), 10);
   	 	var stringaAzioneRateo = $('#pulsanteInserimentoAggiornamentoRisconti').data('azione');
     	primaNota.uid = $('#uidPrimaNotaIntegrata').val();
     	$('#elencoScrittureQuota').overlay('show');
     	
     	if(stringaAzioneRateo && stringaAzioneRateo != ''){
     		
     		RateiRiscontiBase.impostaModaleRateo(primaNota, rateoPrimaNota, annoBilancio-1, stringaAzioneRateo);
     		
         	return $('#elencoScrittureQuota').overlay('hide');
         	
     	}
     	
     	
     	return RateiRiscontiBase.impostaModaleRisconti(primaNota)
     	.always(function(){
     		$('#elencoScrittureQuota').overlay('hide');
     	});
    }

     
   /**
    * preparzione e apertura modale per inserimento rateo
    */
  function inserimentoRateo(e){
   	var primaNota = e.data[0];
   	var documentLocation = $(e.target).data('href-documento'); 
   	if(documentLocation){
   		document.location = documentLocation;
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
   	var documentLocation = $(e.target).data('href-documento'); 
   	if(documentLocation){
   		document.location = documentLocation;
   		return;
   	}
   	RateiRiscontiBase.impostaModaleRateo(primaNota,rateo, rateo.anno, 'aggiornaRateo');
   }

   function impostaRateoDopoAggiornamento(e){
	   var rateo = e.rateo;
	   var obj = qualify(rateo,'rateoPrimaNota');
	   $('#pulsanteInserimentoAggiornamentoRisconti').attr('disabled');
	   $('#elencoScrittureQuota').slideUp();
	   return $.postJSON('gestioneRateiRiscontiPrimaNotaIntegrataDocumentoFIN_impostaRateoDopoAggiornamento.do',obj).then(function(data){
		   impostaDatiNegliAlert(data.errori, $('#ERRORI'));
	   }).always( $('#pulsanteInserimentoAggiornamentoRisconti').removeAttr.bind($('#pulsanteInserimentoAggiornamentoRisconti'),'disabled'));
   }
   
   return exports;
   
}(jQuery));    

$(function() {
		
	RateiRiscontiBase.onLoadPage();
	$("#pulsanteChiudiModaleRisconti, #buttonCloseModal").click(function(){
		 $("#collapseInserimentoAggiornamentoRisconto").slideUp();
	     $("#modaleInserimentoAggiornamentoRisconti").modal("hide");
	});
 
    RateiRisconti.attivaDataTablePrimeNoteCollegate();
    RateiRisconti.attivaDataTableQuote();
    $('#headingAccordionDatiFinanziari').on('click', RateiRisconti.ottieniDatiFinanziari);
    $(document).on('rateoSalvato', RateiRisconti.impostaRateoDopoAggiornamento);
    
});
