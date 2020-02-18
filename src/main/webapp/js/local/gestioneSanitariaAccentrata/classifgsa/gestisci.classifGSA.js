/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ClassificatoreGSA = (function($){
	
	var $alertErrori = $('#ERRORI');
	var $alertErroriModal = $('#ERRORI_modaleEditClassifGSA');
	var $alertInformazioni = $('#INFORMAZIONI');
	var exports = {};
	var urlTabellaClassificatori ='risultatiRicercaClassificatoreGSAAjax.do';
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
	
	exports.cercaClassificatoriGSA = cercaClassificatoriGSA;
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
	   return $.postJSON(urlTabellaClassificatori, obj); 	
	}
	
	function cercaClassificatoriGSA(){
		var $fieldsetRicerca = $('#fieldsetRicerca');
		var obj = $fieldsetRicerca.serializeObject();
		$fieldsetRicerca.overlay('show');
		$.postJSON('gestisciClassificatoriGSA_cercaClassificatoreGSA.do', obj)
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori,$alertErrori)){
				return;
			}
			creaTabellaClassificatoriTrovati();
		})
		.always($fieldsetRicerca.overlay.bind($fieldsetRicerca,'hide'));
	}
	
	function creaTabellaClassificatoriTrovati(){
	    var isOverlay = false;
        var table = $('#tabellaElencoClassificatoreGSA').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaClassificatoreGSAAjax.do',
            oLanguage: {
                sZeroRecords: 'Non sono presenti classificatoriGSA associate'
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
	
	function inserisciClassifGSA(uidClassifPadre){
		var $fieldsetEdit = $('#fieldsetInserimentoAggiornamento');
		var modale = $('#editClassifGSA');
		var obj = $fieldsetEdit.serializeObject();
		var url = 'gestisciClassificatoriGSA_inserisciClassificatoreGSAPadre.do';
		$fieldsetEdit.overlay('show');
		$alertErrori.hide();
		$alertInformazioni.hide();
		if(uidClassifPadre){
			obj['uidClassificatorePadre'] = uidClassifPadre;
			url = 'gestisciClassificatoriGSA_inserisciClassificatoreGSAFiglio.do';
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
	
	function aggiornaClassifGSA(uidClassificatore,uidClassifPadre){
		var $fieldsetEdit = $('#fieldsetInserimentoAggiornamento');
		var obj = $fieldsetEdit.serializeObject();
		var modale = $('#editClassifGSA');
		var url = 'gestisciClassificatoriGSA_aggiornaClassificatoreGSA.do';
		obj['classificatoreGSA.uid'] = uidClassificatore;
		
		$fieldsetEdit.overlay('show');
		$alertErrori.hide();
		$alertInformazioni.hide();
		if(uidClassifPadre){
			obj['uidClassificatorePadre'] = uidClassifPadre;
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
	
	function annullaClassificatoreGSA(uidClassificatore){
		var obj = {};
		var classificatoreGSA ={};
		var modale = $('#editClassifGSA');
		classificatoreGSA.uid=uidClassificatore;
		obj.classificatoreGSA = classificatoreGSA;
		$('#modaleAnnullamento').modal('hide');
		$alertErrori.hide();
		$alertInformazioni.hide();
		$.postJSON('gestisciClassificatoriGSA_annullaClassificatoreGSA.do',qualify(obj)).then(function(data){
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
		var $modal = $('#editClassifGSA');
		var $buttonChiamante = e  && $(e.target);
		var uidClassifPadre = $buttonChiamante.data('uid-padre');
		var $pulsanteInserimentoModal = $('#EDIT_inserisci');
		$modal.find('.conceal-on-show').hide();
		var codiceDaPreimpostare = !uidClassifPadre && $('#codiceClassificatore').val() ? $('#codiceClassificatore').val() : '';
		var descrizioneDaPreimpostare = !uidClassifPadre && $('#descrizioneClassificatore').val() ? $('#descrizioneClassificatore').val() : '';
		$('#statoOperativiClassificatoreGSAModal').val('VALIDO');
		$('#codiceClassificatoreModal').val(codiceDaPreimpostare);
		$('#descrizioneClassificatoreModal').val(descrizioneDaPreimpostare);
		$modal.find('button[type="button"].btn-primary').hide();
		$pulsanteInserimentoModal.show();
		$pulsanteInserimentoModal.substituteHandler('click', inserisciClassifGSA.bind(undefined,uidClassifPadre));
		
		$modal.modal('show');
	}
	
	function apriModaleAggiornamento(e){
		var $modal = $('#editClassifGSA');
		var $buttonChiamante = e  && $(e.target);
		var uidClassificatore = $buttonChiamante.data('uid-classificatore');
		var uidClassifPadre = $buttonChiamante.data('uid-padre');
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
	
	function apriModaleAnnullamento(e){
		var $modal = $('#modaleAnnullamento');
		var $buttonChiamante = e  && $(e.target);
		var uidClassificatore = $buttonChiamante.data('uid-classificatore');
		var $spanDescrizioneElemento = $('#modaleAnnullamentoElementoSelezionato');
		var codiceClDaAnnullare = $('#codiceClassificatore_' + uidClassificatore).val();
		var descrizioneClDaAnnullare = $('#descrizioneClassificatore_' + uidClassificatore).val();
		var descrClassificatoreGSA = ' classificatore GSA ' + codiceClDaAnnullare + ' - ' + descrizioneClDaAnnullare;
		$spanDescrizioneElemento.html(descrClassificatoreGSA);
		$('#modaleAnnullamentoPulsanteSalvataggio').substituteHandler('click', annullaClassificatoreGSA.bind(undefined,uidClassificatore));
		$modal.modal('show');
	}
	
	return exports;
} (jQuery));

$(function(){
	//on page load
	
	$('#pulsanteRicercaClassificatoreGSA').substituteHandler('click',ClassificatoreGSA.cercaClassificatoriGSA);
	
	$('#pulsanteInserisciClassificatorePadreGSA').substituteHandler('click',ClassificatoreGSA.apriModaleInserimento);
	
	$(document).on('click', '.inserisciFiglio', ClassificatoreGSA.apriModaleInserimento);
	$(document).on('click', '.aggiornaClassificatore', ClassificatoreGSA.apriModaleAggiornamento);
	$(document).on('click', '.annullaClassificatore', ClassificatoreGSA.apriModaleAnnullamento);
	
});