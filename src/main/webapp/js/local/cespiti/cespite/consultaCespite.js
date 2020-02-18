/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ConsultaCespite = (function($){	
	'use strict';
	var exports = {};
	var uidCespite = $('#uidCespite').val();
	var baseOpts = { //  var options = $.extend(true, {}, baseOpts, opts);
		bServerSide: true,
        sServerMethod: 'POST',
	    bPaginate: true,
	    bLengthChange: false,
	    iDisplayLength: 10,
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
          sZeroRecords: "Nessun risultato presente",
          oPaginate: {
              sFirst: "inizio",
              sLast: "fine",
              sNext: "succ.",
              sPrevious: "prec.",
              sEmptyTable:  "Nessun risultato presente" 
          }
	    },  
		fnPreDrawCallback: defaultPreDraw,
		fnDrawCallback: defaultDrawCallback
	}
	
	var baseOptsDatiFinanziari = { 
            sAjaxSource : "risultatiRicercaDatiFinanziariPrimaNotaIntegrataFINAjax.do",
            fnDrawCallback: function () {
            	$('#collapseDatiFinanziariPrimeNoteCoge').overlay('hide');
            }
        };
	
	var buttonDettaglioString = '<button type="button" class="btn btn-secondary">dettaglio</button>';
	
	exports.apriCollapsePianoAmmortamento = apriCollapsePianoAmmortamento;
	exports.apriCollapseDatiContabiliPrimeNote = apriCollapseDatiContabiliPrimeNote;
	exports.apriCollapseDismissioni = apriCollapseDismissioni;
	exports.apriCollapseRivalutazioni = apriCollapseRivalutazioni;
	exports.apriCollapseSvalutazioni = apriCollapseSvalutazioni;
	exports.apriCollapseDonazioniRivenimenti = apriCollapseDonazioniRivenimenti;
	
	/***
	 * Funzione generica per l'apertiura di tutti i collapse.
	 * @param $anchorEl 	il nodo su cui l'utente ha cliccato che scatena l'apertura del collapse
	 * @param $referencedEl il nodo che deve essere aperto
	 * @param url           la stringa per il caricamento dei dati via JSON da mostrare nel collpase
	 * @param thenfunc      la function che viene eseguita all'interno del then (default: $.noop)
	 * 
	 * @return la promise
	 * */
	function apriCollapse(e, $anchorEl, $referencedEl, url, thenFunct) {
	    var obj;
	    var fnc = thenFunct && typeof thenFunct ==="function" ? thenFunct : $.noop;
	    if($anchorEl.data('loaded')) {
	        return  $.Deferred().resolve();
	    }
	    e.preventDefault();
	    e.stopPropagation();
	    e.stopImmediatePropagation();
	    obj = {'uidCespite': uidCespite};
	    $anchorEl.overlay('show');
	    
	    return $.post(url, obj)
	    .then(function(data) {
	        if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
	            return;
	        }
	        fnc(data);
	        $anchorEl.data('loaded', true);
	        $referencedEl.collapse('show');            
	    }).always($anchorEl.overlay.bind($anchorEl, 'hide'));
	}
	
	
	/**
	 * Funzione generica di impostazione delle prime note.
	 * 
	 * @param primeNote        la lista di prime note che devono essere mostrate
	 * @param $divContenitore  il div in cui inserire le tabelle create
	 * */
	function impostaPrimeNote(primeNote,$divContenitore){
		var opts;
		var primeNoteNotNull = primeNote || [];
		
		opts = {
				bServerSide: false,
				aoColumnDefs: [
					{aTargets: [0], mData: defaultPerDataTable("codiceConto")},
					{aTargets: [1], mData: defaultPerDataTable('descrizioneConto')},
					{aTargets: [2], mData: defaultPerDataTable('dare'), fnCreatedCell: function(nTd) {
	                    $(nTd).addClass("tab_Right");
	                }},
					{aTargets:[3], mData: defaultPerDataTable('avere'), fnCreatedCell: function(nTd) {
	                    $(nTd).addClass("tab_Right");
	                }},
				]
		}
		
		$divContenitore.html("");
		
		primeNoteNotNull.forEach(function(element){
			var options = $.extend(true, {},baseOpts, opts, {aaData: element.listaElementoScritturaPrimaNotaLibera});
			$divContenitore.append(element.intestazione + element.tabella);
			$('#tabellaScritture' + element.suffixSelettoreTabella).dataTable(options);
		});
	}
	
	 function attivaDataTableSubdocSpesa() {
		 var optsDocumentoSpesa = {
				 aoColumnDefs : [
					 {aTargets : [ 0 ], mData: defaultPerDataTable('numero')},
					 {aTargets : [ 1 ], mData: defaultPerDataTable('importo')},
					 {aTargets : [ 2 ], mData: defaultPerDataTable('movimentoGestione')},
					 {aTargets : [ 3 ], mData: defaultPerDataTable('pianoDeiConti')},
					 {aTargets : [ 4 ], mData: defaultPerDataTable('liquidazione')},
					 {aTargets : [ 5 ], mData: defaultPerDataTable('ordinativo')}
				]
	        };
	        
	        var opzioniTabella = $.extend(true, {}, baseOpts,  baseOptsDatiFinanziari, optsDocumentoSpesa);
	        $('#collapseDatiFinanziariPrimeNoteCoge').find('table').dataTable(opzioniTabella);
	    }
	    
	    
	    function attivaDataTableSubdocEntrata() {	    	
	    	var optsDocumentoEntrata = {
					 aoColumnDefs : [
						 {aTargets : [ 0 ], mData: defaultPerDataTable('numero')},
						 {aTargets : [ 1 ], mData: defaultPerDataTable('importo')},
						 {aTargets : [ 2 ], mData: defaultPerDataTable('movimentoGestione')},
						 {aTargets : [ 3 ], mData: defaultPerDataTable('pianoDeiConti')},
						 {aTargets : [ 4 ], mData: defaultPerDataTable('ordinativo')}
					]
		        };
	        var opzioniTabella = $.extend(true, {},baseOpts,  baseOptsDatiFinanziari, optsDocumentoEntrata);
	        $('#collapseDatiFinanziariPrimeNoteCoge').find('table').dataTable(opzioniTabella);
	    }
	    
	    
	    /*******************************************************************************************/
	    /*******************************************************************************************/
	    /**********************   POPOLAMENTO DEI COLLAPSE            ******************************/
	    /*******************************************************************************************/
	    /*******************************************************************************************/
	    
	    function popolaCollapseDatiContabili(primaNota) {
	        var header = $('#anchorDatiFinanziariPrimeNoteCoge');
	        var div = $('#collapseDatiFinanziariPrimeNoteCoge');
	        var tipoCollegamento  = primaNota.nameTipoCollegamentoDatiFinanziari;
	        var obj;
	        
	        
	        if(header.data('loaded') || !primaNota.integrata) {
	            return;
	        }
	        obj = {'primaNotaContabilitaFinanziaria.uid': primaNota.uid, 'tipoCollegamento': primaNota.nameTipoCollegamentoDatiFinanziari};
	        header.overlay('show');
	        
	        return $.post('consultaCespite_ottieniDatiFinanziari.do', obj)
	        .then(function(data) {
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
	
	
	
	// **************************************************************** 
	//  CARICAMENTO PRIME NOTE INVENTARIO
	//*****************************************************************
    function popolaCollapsePrimeNotePiamoAmmortamento($fieldsetDettaglio, dettaglioAmmortamento, nTd, e){
    	var $row = $(nTd).closest("tr");
    	
		if(!dettaglioAmmortamento || !dettaglioAmmortamento.uid){
			return;
		}
		$row.overlay("show");
		return $.postJSON('consultaCespite_ottieniPrimeNoteAmmortamento.do', {'uidEntitaCollegata': dettaglioAmmortamento.uid }).then(function(data){
			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
				return;
			}
			impostaPrimeNote(data.listaPrimeNote,$('#divPrimeNoteAmmortamento'));
        	$fieldsetDettaglio.collapse('show');
			
		}).always($row.overlay.bind($row, 'hide'));
		//Perche' aveva il collapse nel 
//		return apriCollapse(e, $(this), $('#collapsePrimeNoteCogeAmmortamento'), 'consultaCespite_ottieniPrimeNoteAmmortamento.do', callbackPrimeNotePianoAmmortamento);
	}
    
	function caricaPrimeNoteVariazioni($fieldsetDettaglio, variazione, nTd){
		var $row = $(nTd).closest("tr");
		var xhr;
		if(!variazione || !variazione.uid){
			return;
		}
		$row.overlay("show");
		xhr = $.postJSON('consultaCespite_ottieniPrimeNoteVariazione.do', {'uidEntitaCollegata': variazione.uid });
		$fieldsetDettaglio.data('xhr', xhr);
		return xhr.then(function(data){
		    $fieldsetDettaglio.removeData('xhr');
			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
				return;
			}
			impostaPrimeNote(data.listaPrimeNote,$fieldsetDettaglio);
        	$fieldsetDettaglio.slideDown();
			
		}).always($row.overlay.bind($row, 'hide'));
	}
	
	function caricaPrimeNoteDismissione(nRow){
		var $row = $(nRow).overlay('show');
		var uidDismissione = $row.data('originalDismissione') && $row.data('originalDismissione').uid;
		return $.postJSON('consultaCespite_ottieniPrimeNoteDismissione.do', {'uidEntitaCollegata': uidDismissione, 'uidCespite': uidCespite })
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
				return;
			}
			impostaPrimeNote(data.listaPrimeNote,$('#primaNotaDismissione'));
			
		}).always($row.overlay.bind($row, 'hide'));
	}
	
	
	// **************************************************************** 
	//  POPOLAMENTO DEI COLLAPSE
	//*****************************************************************
	
	function popolaCollapsePianoAmmortamento(data){
		var $spanimporto = $('#importiAmmortamentoAccumulati');
		var $tabella = $('#tabellaPianoAmmortamento').overlay('show');
		var opts = {
				sAjaxSource: 'risultatiRicercaDettaglioAmmortamentoAnnuoCespiteAjax.do',
				aoColumnDefs: [
	                {aTargets: [0], mData: defaultPerDataTable('anno')},
	                {aTargets: [1], mData: defaultPerDataTable('importo')},
	                {aTargets: [2], mData: defaultPerDataTable('stringaPrimaNotaDefinitiva')},
	                {aTargets: [3], mData: function(source){
		            	return buttonDettaglioString;
 		            }, fnCreatedCell: function(nTd, sData, oData) {	                    
		                $(nTd).find('button').substituteHandler('click', popolaCollapsePrimeNotePiamoAmmortamento.bind(undefined,$('#collapsePrimeNoteCogeAmmortamento'), oData, nTd) );
 		            }}
	            ],
		        fnDrawCallback: function(settings){
	              $tabella.overlay('hide');
		        }
			}
		var options = $.extend(true, {}, baseOpts, opts);
		if(data.totaleImportoAmmortato){
			$spanimporto.html(formatMoney(data.totaleImportoAmmortato));
		}
		$tabella.dataTable(options);
	}
	
	
	
	function popolaCollapseDatiContabiliPrimeNoteCoge(data){
		var $tabella = $('#tabellaPrimeNoteRegistroA').overlay('show');
		var opts = {
				sAjaxSource: 'risultatiRicercaScrittureRegistroAByCespiteAjax.do',
				aoColumnDefs: [
					{aTargets: [0], mData: defaultPerDataTable('numeroPrimaNota')},
	                {aTargets: [1], mData: defaultPerDataTable('contoPatrimoniale')},
	                {aTargets: [2], mData: defaultPerDataTable('dataDefinizionePrimaNota')},
	                {aTargets: [3], mData: defaultPerDataTable('numeroInventario')},
	                {aTargets: [4], mData: defaultPerDataTable('statoInventario')},
	                {aTargets: [5], mData: defaultPerDataTable('statoCoGe')},
		            {aTargets: [6], bVisible:false, mData: defaultPerDataTable('azioni')}
	            ],
	            fnCreatedRow: function(nRow, aData, iDataIndex){
	            	$('#anchorDatiFinanziariPrimeNoteCoge').substituteHandler('click', popolaCollapseDatiContabili.bind($('#anchorDatiFinanziariPrimeNoteCoge'), aData));
	            },
		        fnDrawCallback: function(settings){
	              $tabella.overlay('hide');
	              $tabella.find('a[rel="popover"]').popover();
	              $tabella.find('a.tooltip-test').tooltip();
		        }
			}
		var options = $.extend(true, {}, baseOpts, opts);
		$tabella.dataTable(options);
	}
	
	function popolaCollapseDismissioni(data){
		var $tabella = $('#tabellaDismissioniCespite');
		var datatableDismissioni;		
		var opts = {
				sAjaxSource: 'risultatiRicercaDismissioneCespiteAjax.do',
				aoColumnDefs: [
	                {aTargets: [0], mData: defaultPerDataTable('elenco')},
	                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
	                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
	                {aTargets: [3], mData: defaultPerDataTable('dataCessazione')},
	                {aTargets: [4], mData: defaultPerDataTable('causaleDismissione')},
	                {aTargets: [5], mData: defaultPerDataTable('numeroCespitiCollegati')},
	                {aTargets: [6], mData: defaultPerDataTable('statoMovimento')},
	                {aTargets: [7], bVisible:false,  mData: defaultPerDataTable('azioni')}
	            ],
	            fnCreatedRow: function(nRow, aData, iDataIndex){
	            	$(nRow).data('originalDismissione', aData);
	            },
		        fnDrawCallback: function(settings){
		        	 var row = settings.oInstance.fnGetNodes(0);
		        	 caricaPrimeNoteDismissione(row);
		        	 $tabella.find('a[rel="popover"]').popover();
		             $tabella.overlay('hide');
		        }
			}
		var options = $.extend(true, {}, baseOpts, opts);
		datatableDismissioni = $tabella.dataTable(options);
	}
	
	
	
	function popolaCollapseVariazioni($tabella, urlCaricaTabella, $fieldsetDettaglio){
		var opts = {
				sAjaxSource: urlCaricaTabella,
		        aoColumnDefs: [
		            {aTargets: [0], mData: defaultPerDataTable('anno')},
		            {aTargets: [1], mData: defaultPerDataTable('dataInserimento')},
		            {aTargets: [2], mData: defaultPerDataTable('tipoVariazione')},
		            {aTargets: [3], mData: defaultPerDataTable('descrizione')},
		            {aTargets: [4], mData: defaultPerDataTable('importo')},
		            {aTargets: [5], mData: defaultPerDataTable('stato')},
		            {aTargets: [6], mData: defaultPerDataTable('cespite')},
		            {aTargets: [7], mData: defaultPerDataTable('tipoBene')},
		            {aTargets: [8], mData: function(source){
		            	return buttonDettaglioString;
 		            }, fnCreatedCell: function(nTd, sData, oData) {
		                $(nTd).find('button').substituteHandler('click', caricaPrimeNoteVariazioni.bind(undefined,$fieldsetDettaglio, oData, nTd) );
 		            }}
		        ],
		        fnDrawCallback: function(settings){
	        	  $tabella.find('a[rel="popover"]').popover();
	        	  $tabella.substituteHandler('page.dt',function(){
	        	      var xhr = $fieldsetDettaglio.data('xhr');
	        		  $fieldsetDettaglio.slideUp();
	        		  if(xhr) {
	        		      xhr.abort();
	        		  }
	        	  });
	              $tabella.overlay('hide');
		        }
			}
			var options = $.extend(true, {}, baseOpts, opts);
			return $tabella.dataTable(options);
	}
	
	function popolaCollapseRivalutazioni(data){
		var $tabella = $('#tabellaRivalutazioniCespite');
		var urlCaricaTabella = 'risultatiRicercaRivalutazioneCespiteAjax.do';
		return popolaCollapseVariazioni($tabella, urlCaricaTabella, $('#primaNotaRivalutazione'));
	}
	
	
	
	function popolaCollapseSvalutazioni(data){
		var $tabella = $('#tabellaSvalutazioniCespite');
		var urlCaricaTabella = 'risultatiRicercaSvalutazioneCespiteAjax.do';
		return popolaCollapseVariazioni($tabella, urlCaricaTabella, $('#primaNotaSvalutazione') );
	}
	
	function popolaCollapseDonazioniRinvenimenti(data){
		impostaPrimeNote(data.listaPrimeNote, $('#primaNotaDonazioneRinvenimento'));
		var $collapse = $('#primaNotaDonazioneRinvenimento').slideDown();
	}
	
	// **************************************************************** 
	//  APERTURA COLLAPSE
	//*****************************************************************
	
	function apriCollapsePianoAmmortamento(e){
		var $accordionPrimeNoteAmmortamento  = $('#anchorPrimeNoteAmmortamento');
		return apriCollapse(e, $('#anchorPianoAmmortamento'), $('#collapsePianoAmmortamento'), 'consultaCespite_ottieniPianoAmmortamento.do', popolaCollapsePianoAmmortamento);
//			.then($accordionPrimeNoteAmmortamento.substituteHandler.bind($accordionPrimeNoteAmmortamento,'click',popolaCollapsePrimeNotePiamoAmmortamento));
	}
	
	function apriCollapseDatiContabiliPrimeNote(e){
		var $accordionPrimeNoteCoge  = $('#anchorDatiFinanziariPrimeNoteCoge');
		return apriCollapse(e, $('#anchorPrimeNoteCoge'), $('#collapsePrimeNoteCoge'), 'consultaCespite_ottieniPrimeNoteContabilitaGenerale.do', popolaCollapseDatiContabiliPrimeNoteCoge)
			.then($accordionPrimeNoteCoge.substituteHandler.bind($accordionPrimeNoteCoge,'click'));
	}
	
	function apriCollapseDismissioni(e){
		return apriCollapse(e, $('#anchorDismissioni'), $('#collapseDismissioni'), 'consultaCespite_ottieniDismissioni.do', popolaCollapseDismissioni);
	}

	function apriCollapseRivalutazioni(e){
		return apriCollapse(e, $('#anchorRivalutazioni'), $('#collapseRivalutazioni'), 'consultaCespite_ottieniRivalutazioni.do', popolaCollapseRivalutazioni);
	}
	
	function apriCollapseSvalutazioni(e){
		return apriCollapse(e, $('#anchorSvalutazioni'), $('#collapseSvalutazioni'), 'consultaCespite_ottieniSvalutazioni.do',
				popolaCollapseSvalutazioni);
	}
	
	function apriCollapseDonazioniRivenimenti(e){
		return apriCollapse(e, $('#anchorDonazioniRinvenimenti'), $('#collapseDonazioniRinvenimenti'), 'consultaCespite_ottieniDonazioniRinvenimenti.do', popolaCollapseDonazioniRinvenimenti);
	}
	
	return exports;
	
}(jQuery));


$(function() {
	$("#anchorPianoAmmortamento").substituteHandler("click" , ConsultaCespite.apriCollapsePianoAmmortamento);
	$("#anchorPrimeNoteCoge").substituteHandler("click" , ConsultaCespite.apriCollapseDatiContabiliPrimeNote);
	$("#anchorDismissioni").substituteHandler("click" , ConsultaCespite.apriCollapseDismissioni);
	$("#anchorRivalutazioni").one("click" , ConsultaCespite.apriCollapseRivalutazioni);
	$("#anchorSvalutazioni").one("click" , ConsultaCespite.apriCollapseSvalutazioni);
	$("#anchorDonazioniRinvenimenti").substituteHandler("click" , ConsultaCespite.apriCollapseDonazioniRivenimenti);

});