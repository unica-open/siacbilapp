/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var QuadroEconomico = (function($){
	
	var $alertErrori = $('#ERRORI');
	var $alertErroriModal = $('#ERRORI_modaleEditQuadroEconomico');
	var $alertInformazioni = $('#INFORMAZIONI');
	var exports = {};
	var urlTabellaQuadroEconomico ='risultatiRicercaQuadroEconomicoAjax.do';
	var dataTable;
	var optionsBase = {
		        bServerSide: true,
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
		            sInfo: '_START_ - _END_ di _MAX_ risultati',
		            sInfoEmpty: '0 risultati',
		            sProcessing: 'Attendere prego...',
		            oPaginate: {
		                sFirst: 'inizio',
		                sLast: 'fine',
		                sNext: 'succ.',
		                sPrevious: 'prec.',
		                sEmptyTable: 'Nessun dato disponibile'
		            }
		        }
		    };
	
	exports.cercaQuadroEconomico = cercaQuadroEconomico;
	exports.apriModaleInserimento = apriModaleInserimento;
	exports.apriModaleAggiornamento = apriModaleAggiornamento;
	exports.apriModaleAnnullamento = apriModaleAnnullamento;
	
	function ricaricaTabella(){
       if(!dataTable) {
    	   return $.Deferred().resolve().promise();
       }	
       var settings = dataTable.fnSettings();
       var obj = {
	       forceRefresh: true,
	       iTotalRecords: settings._iRecordsTotal,
	       iTotalDisplayRecords: settings._iRecordsDisplay,
	       iDisplayStart: settings._iDisplayStart,
	       iDisplayLength: settings._iDisplayLength
	   };
	   return $.postJSON(urlTabellaQuadroEconomico, obj); 	
	}
	
	function cercaQuadroEconomico(){
		var $fieldsetRicerca = $('#fieldsetRicerca');
		var obj = $fieldsetRicerca.serializeObject();
		var accordionQuadroEconomico = $('#accordionQuadroEconomico');
		$alertErrori.slideUp();
		$fieldsetRicerca.overlay('show');
		
		accordionQuadroEconomico.slideUp();
		 
		return $.postJSON('gestisciQuadroEconomico_cercaQuadroEconomico.do', obj)
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori,$alertErrori)){
				return;
			}
			creaTabellaQuadroEconomicoTrovati();
			accordionQuadroEconomico.slideDown();
		})
		.always(function(){$fieldsetRicerca.overlay('hide');});
	}
	
	
	
	function creaTabellaQuadroEconomicoTrovati(){
	    var isOverlay = false;
        var table = $('#tabellaElencoQuadroEconomico').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaQuadroEconomicoAjax.do',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quadroEconomico associate'
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('domString')}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        dataTable = table.dataTable(opts);
	}
	
	function inserisciQuadroEconomico(uidQuadroEconomicoPadre){
		var $fieldsetEdit = $('#fieldsetInserimentoAggiornamento');
		var modale = $('#editQuadroEconomico');
		var obj = $fieldsetEdit.serializeObject();
		var url = 'gestisciQuadroEconomico_inserisciQuadroEconomicoPadre.do';
		$fieldsetEdit.overlay('show');
		$alertErrori.hide();
		$alertInformazioni.hide();
		if(uidQuadroEconomicoPadre){
			obj['uidQuadroEconomicoPadre'] = uidQuadroEconomicoPadre;
			url = 'gestisciQuadroEconomico_inserisciQuadroEconomicoFiglio.do';
		}
		$.postJSON(url, qualify(obj))
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori,$alertErroriModal)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni,$alertInformazioni);
			if(dataTable){
				return ricaricaTabella()
					.then(dataTable.fnDraw.bind(dataTable))
					.then(modale.modal.bind(modale, 'hide'));
			}
			modale.modal('hide');
		})
		.always($fieldsetEdit.overlay.bind( $fieldsetEdit,'hide'));
	}
	
	function aggiornaQuadroEconomico(uidQuadroEconomico,uidQuadroEconomicoPadre){
		var $fieldsetEdit = $('#fieldsetInserimentoAggiornamento');
		var obj = $fieldsetEdit.serializeObject();
		var modale = $('#editQuadroEconomico');
		var url = 'gestisciQuadroEconomico_aggiornaQuadroEconomico.do';
		obj['quadroEconomico.uid'] = uidQuadroEconomico;
		
		$fieldsetEdit.overlay('show');
		$alertErrori.hide();
		$alertInformazioni.hide();
		if(uidQuadroEconomicoPadre){
			obj['uidQuadroEconomicoPadre'] = uidQuadroEconomicoPadre;
		}
		$.postJSON(url, qualify(obj))
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori,$alertErroriModal)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni,$alertInformazioni);
			return ricaricaTabella()
			.then(dataTable.fnDraw.bind(dataTable))
			.then(modale.modal.bind(modale, 'hide'));
		})
		.always($fieldsetEdit.overlay.bind( $fieldsetEdit,'hide'));
	}
	
	function annullaQuadroEconomico(uidQuadroEconomico){
		var obj = {};
		var quadroEconomico ={};
		var modale = $('#editQuadroEconomico');
		quadroEconomico.uid=uidQuadroEconomico;
		obj.quadroEconomico = quadroEconomico;
		$('#modaleAnnullamento').modal('hide');
		$alertErrori.hide();
		$alertInformazioni.hide();
		$.postJSON('gestisciQuadroEconomico_annullaQuadroEconomico.do',qualify(obj)).then(function(data){
			if(impostaDatiNegliAlert(data.errori,$alertErrori)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni,$alertInformazioni);
			return ricaricaTabella()
			.then(dataTable.fnDraw.bind(dataTable))
			.then(modale.modal.bind(modale, 'hide'));
		})
		.always();
	}
	
	function apriModaleInserimento(e){
		var $modal = $('#editQuadroEconomico');
		var $buttonChiamante = e  && $(e.target);
		var uidQuadroEconomicoPadre = $buttonChiamante.data('uidPadre');
		var $pulsanteInserimentoModal = $('#EDIT_inserisci');
		$modal.find('.conceal-on-show').hide();
		var codiceDaPreimpostare = !uidQuadroEconomicoPadre && $('#codiceQuadroEconomico').val() ? $('#codiceQuadroEconomico').val() : '';
		var descrizioneDaPreimpostare = !uidQuadroEconomicoPadre && $('#descrizioneQuadroEconomico').val() ? $('#descrizioneQuadroEconomico').val() : '';
		$('#statoOperativiQuadroEconomicoModal').val('VALIDO');
		$('#codiceQuadroEconomicoModal').val(codiceDaPreimpostare);
		$('#descrizioneQuadroEconomicoModal').val(descrizioneDaPreimpostare);
		$modal.find('button[type="button"].btn-primary').hide();
		$pulsanteInserimentoModal.show();
		$pulsanteInserimentoModal.substituteHandler('click', inserisciQuadroEconomico.bind(undefined,uidQuadroEconomicoPadre));
		
		$modal.modal('show');
	}
	/*

	function apriModaleAggiornamento(e){
		var $modal = $('#editClassifGSA');
		var $buttonChiamante = e  && $(e.target);
		var uidClassificatore = $buttonChiamante.data('uidClassificatore');
		var uidClassifPadre = $buttonChiamante.data('uidPadre');
		var codiceDaPreimpostare = $('#codiceClassificatore_' + uidClassificatore).val();
		var descrizioneDaPreimpostare = $('#descrizioneClassificatore_' + uidClassificatore).val();
		var statoDaPreimpostare = $('#statoClassificatore_' + uidClassificatore).val();
		var $pulsanteAggiornamentoModal = $('#EDIT_aggiorna');
		$modal.find('.conceal-on-show').hide();
		$('#statoOperativiClassificatoreGSAModal').val(statoDaPreimpostare);
		$('#HIDDEN_statoOperativiClassificatoreGSAModal').val(statoDaPreimpostare);
		$('#codiceClassificatoreModal').val(codiceDaPreimpostare);
		$('#descrizioneClassificatoreModal').val(descrizioneDaPreimpostare);
		
		$pulsanteAggiornamentoModal.show();
		$pulsanteAggiornamentoModal.substituteHandler('click', aggiornaClassifGSA.bind(undefined,uidClassificatore,uidClassifPadre));
		
		$modal.modal('show');
	}
	
	
	
	 */
	
	function apriModaleAggiornamento(e){
		var $modal = $('#editQuadroEconomico');
		var $buttonChiamante = e  && $(e.target);
		var uidQuadroEconomico = $buttonChiamante.data('uidQuadroEconomico');
		var uidQuadroEconomicoPadre = $buttonChiamante.data('uidPadre');
		var codiceDaPreimpostare = $('#codiceQuadroEconomico_' + uidQuadroEconomico).val();
		var descrizioneDaPreimpostare = $('#descrizioneQuadroEconomico_' + uidQuadroEconomico).val();
		
		var parteQeDaPreimpostare = $('#parteQuadroEconomico_' + uidQuadroEconomico).val();
		

		var statoDaPreimpostare = $('#statoQuadroEconomico_' + uidQuadroEconomico).val();		
		//var parteDaPreimpostare = $('#parteQuadroEconomico_' + uidQuadroEconomico).val();
		
		var $pulsanteAggiornamentoModal = $('#EDIT_aggiorna');
		$modal.find('.conceal-on-show').hide();
		$('#statoOperativiQuadroEconomicoModal').val(statoDaPreimpostare);
		$('#HIDDEN_statoOperativiQuadroEconomicoModal').val(statoDaPreimpostare);
		
		$('#parteQuadroEconomico').val(parteQeDaPreimpostare);
		//$('#HIDDEN_parteQuadroEconomicoModal').val(parteDaPreimpostare);
		
		
		$('#codiceQuadroEconomicoModal').val(codiceDaPreimpostare);
		$('#descrizioneQuadroEconomicoModal').val(descrizioneDaPreimpostare);
		
		$pulsanteAggiornamentoModal.show();
		$pulsanteAggiornamentoModal.substituteHandler('click', aggiornaQuadroEconomico.bind(undefined,uidQuadroEconomico,uidQuadroEconomicoPadre));
		
		$modal.modal('show');
	}
	/*
	 * function apriModaleAnnullamento(e){
		var $modal = $('#modaleAnnullamento');
		var $buttonChiamante = e  && $(e.target);
		var uidClassificatore = $buttonChiamante.data('uidClassificatore');
		var $spanDescrizioneElemento = $('#modaleAnnullamentoElementoSelezionato');
		var codiceClDaAnnullare = $('#codiceClassificatore_' + uidClassificatore).val();
		var descrizioneClDaAnnullare = $('#descrizioneClassificatore_' + uidClassificatore).val();
		var descrClassificatoreGSA = ' classificatore GSA ' + codiceClDaAnnullare + ' - ' + descrizioneClDaAnnullare;
		$spanDescrizioneElemento.html(descrClassificatoreGSA);
		$('#modaleAnnullamentoPulsanteSalvataggio').substituteHandler('click', annullaClassificatoreGSA.bind(undefined,uidClassificatore));
		$modal.modal('show');
	}
	 */
	function apriModaleAnnullamento(e){
		var $modal = $('#modaleAnnullamento');
		var $buttonChiamante = e  && $(e.target);
		var uidQuadroEconomico = $buttonChiamante.data('uidQuadroEconomico');
		var $spanDescrizioneElemento = $('#modaleAnnullamentoElementoSelezionato');
		var codiceClDaAnnullare = $('#codiceQuadroEconomico_' + uidQuadroEconomico).val();
		var descrizioneClDaAnnullare = $('#descrizioneQuadroEconomico_' + uidQuadroEconomico).val();
		var descrQuadroEconomico = ' Quadro Economico ' + codiceClDaAnnullare + ' - ' + descrizioneClDaAnnullare;
		$spanDescrizioneElemento.html(descrQuadroEconomico);
		$('#modaleAnnullamentoPulsanteSalvataggio').substituteHandler('click', annullaQuadroEconomico.bind(undefined,uidQuadroEconomico));
		$modal.modal('show');
	}
	
	return exports;
} (jQuery));

$(function(){
	//on page load
	
	$('#pulsanteRicercaQuadroEconomico').substituteHandler('click',QuadroEconomico.cercaQuadroEconomico);
	
	$('#pulsanteInserisciQuadroEconomicoPadre').substituteHandler('click',QuadroEconomico.apriModaleInserimento);
	
	$(document).on('click', '.inserisciFiglio', QuadroEconomico.apriModaleInserimento);
	$(document).on('click', '.aggiornaQuadroEconomico', QuadroEconomico.apriModaleAggiornamento);
	$(document).on('click', '.annullaQuadroEconomico', QuadroEconomico.apriModaleAnnullamento);
	
});