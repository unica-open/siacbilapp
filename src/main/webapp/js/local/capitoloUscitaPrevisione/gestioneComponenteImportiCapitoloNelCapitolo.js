/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** GESTIONE COMPONENTE IMPORTI CAPITOLO ****
*************************************************
*/
var ComponenteImportiCapitolo = (function (cic) {
	"use strict";
	
	var exports = {};
	exports.caricaTabella = caricaTabella;
	exports.apriModaleNuovaComponente = apriModaleNuovaComponente;
	exports.confermaNuovaComponente = confermaNuovaComponente;
	exports.onChangeTipoComponenteNuovaComponente = onChangeTipoComponenteNuovaComponente;
	// SIAC-8859
	exports.apriModaleConfermaNuovaComponente= apriModaleConfermaNuovaComponente;
	
	var $alertErrori = $('#ERRORI');
	var $alertMessaggi = $("#MESSAGGI");
	var $alertInformazioni = $("#INFORMAZIONI");
	
	
	function onChangeTipoComponenteNuovaComponente(){
		TabellaImportiComponenteCapitolo.creaEPosizionaRigheTabellaNuovaComponente();
		$('#divTabellaInserimentoImportiNuovaComponente').slideDown();
		$("#INSERT_modifica").prop("disabled",false);
		
	}
	
	/***FUNZIONI CONFERMA DELLE MODALI */
	
	function confermaNuovaComponente(){
		var objToSend ={};
		var modal = $('#componenteModel').overlay({usePosition: true}).overlay("show");
		
		// SIAC-8859
		$('#CONFERMA_modaleEditComponenti').hide();
		$('#INSERT_modifica').prop("disabled",false);
		$('#chiudiModaleComp').prop("disabled",false);
		
		
		var url="gestioneComponenteImportoCapitoloNelCapitolo_inserisciNuovaComponente.do";
		
		objToSend.uidTipoComponenteCapitolo = $('#dropdown-select-com').val();
		objToSend.importoStanziamentoAnno0= $(".anno0NuovaComponente:not([readonly])").val();
		objToSend.importoStanziamentoAnno1= $(".anno1NuovaComponente:not([readonly])").val();
		objToSend.importoStanziamentoAnno2= $(".anno2NuovaComponente:not([readonly])").val();
		
		return $.postJSON(url,qualify(objToSend))
			.then(function(data){
				if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleInsertStanziamenti'))){
					return;
				}
				creaTabella(data.righeComponentiTabellaImportiCapitolo, data.righeImportiTabellaImportiCapitolo);
				modal.modal("hide");
			})
			.always(modal.overlay.bind(modal,'hide'));
	}
	
	function confermaModificaStanziamenti(tipoImporto){
		var objToSend ={};
		var modal = $('#editStanziamentiComponente').overlay({usePosition: true}).overlay("show");
		var url="gestioneComponenteImportoCapitoloNelCapitolo_aggiorna" + tipoImporto + ".do";
		
		// SIAC-8859
		$('#INSERT_modificaImporti').prop("disabled",false);		
		$('#chiudiModale').prop("disabled",false);
				
		objToSend.importoStanziamentoModificato = $('#tabellaModificaImporti').find(':input:not([readonly])').val();
		return $.postJSON(url,qualify(objToSend))
			.then(function(data){
				if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleEditStanziamenti'))){
					return;
				}
				creaTabella(data.righeComponentiTabellaImportiCapitolo, data.righeImportiTabellaImportiCapitolo);
				modal.modal("hide");
			})
			.always(modal.overlay.bind(modal,'hide'));
	}
	
	function confermaModificaStanziamentiComponenti(uidTipoComponente){
		var objToSend ={};
		var modal = $('#editStanziamentiComponente').overlay({usePosition: true}).overlay("show");
		var url="gestioneComponenteImportoCapitoloNelCapitolo_aggiornaComponenti.do";
		
		// SIAC-8859
		$('#INSERT_modificaComponenti').prop("disabled",false);		
		$('#chiudiModale').prop("disabled",false);
		
	
		objToSend.uidTipoComponenteCapitolo = uidTipoComponente;
		objToSend.importoStanziamentoAnno0= $('.anno0:not([readonly])').val();
		objToSend.importoStanziamentoAnno1= $('.anno1:not([readonly])').val();
		objToSend.importoStanziamentoAnno2= $('.anno2:not([readonly])').val();	
			
		return $.postJSON(url,qualify(objToSend))
			.then(function(data){
				if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleEditStanziamenti'))){
					return;
				}
				creaTabella(data.righeComponentiTabellaImportiCapitolo, data.righeImportiTabellaImportiCapitolo);
				modal.modal("hide");
			})
			.always(modal.overlay.bind(modal,'hide'));
	}
	
	
	function confermaEliminaStanziamentiComponenti(uidTipoComponente){
		var objToSend ={};
		var modal = $('#msgElimina').overlay({usePosition: true}).overlay("show");
		var url="gestioneComponenteImportoCapitoloNelCapitolo_elimina.do";
		
		objToSend.uidTipoComponenteCapitolo = uidTipoComponente;
		
			
		$.postJSON(url,qualify(objToSend))
			.then(function(data){
				if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleEditStanziamenti'))){
					return;
				}
				creaTabella(data.righeComponentiTabellaImportiCapitolo, data.righeImportiTabellaImportiCapitolo);
				modal.modal("hide");
			})
			.always(modal.overlay.bind(modal,'hide'));
	}
	
	/***FUNZIONI APERTURA MODALI */
	
	function apriModaleNuovaComponente(e){
		var overlay;
		var $select = $('#dropdown-select-com');
		var $modal = $('#componenteModel');
		
		e && e.preventDefault && e.stopPropagation;
		
		//SIAC-8859
		$('#CONFERMA_modaleEditComponenti').hide();
		$('#ERRORI_modaleInsertStanziamenti').hide();
		$('#chiudiModaleComp').prop("disabled",true);
		$('#INSERT_modifica').prop("disabled",true);
	
		$('#divTabellaInserimentoImportiNuovaComponente').slideUp();
		$select.val('0');
		
		if($select.data('filtered')){
			$modal.modal('show');
			return;
		}
				
		overlay = $("#button_inserisciModifica").overlay('show');
				
		$.postJSON(
			"gestioneComponenteImportoCapitoloNelCapitolo_caricaListaTipoComponentiPerNuovaComponente.do",
			{},
			function(data) {
				var listaTipi = [];
				$select.find('option').hide();
				// Controllo gli eventuali errori, messaggi, informazioni
				if(impostaDatiNegliAlert(data.errori, $alertErrori)) {return;	}
				if(impostaDatiNegliAlert( data.messaggi, $alertMessaggi)) {return;	}
				if(impostaDatiNegliAlert(data.informazioni, $alertInformazioni)) {return;	}
				
				listaTipi = data.listaTipoComponentiPerNuovaComponente;
				
				for(var a in listaTipi){
					var tipo = listaTipi[a];
					$select.find('option[value="' + tipo.uid + '"]').show();
				}
				$select.data('filtered', true);
				$modal.modal('show');
				
			}
		).always(overlay.overlay.bind(overlay, 'hide'));
		
	}
	
	// SIAC-8859
	function apriModaleConfermaOperazioneImporti(tipoImporto){
	
		$('#CONFERMA_modaleEditStanziamenti').show();
		$('#chiudiModale').prop("disabled",true);
		$('#INSERT_modificaImporti').prop("disabled",true);
		$('#EDIT_confermaOperazione').substituteHandler('click', confermaModificaStanziamenti.bind(undefined, tipoImporto));	
	}
	
	// SIAC-8859
	function apriModaleConfermaOperazioneComponenti(uuid){
	
		$('#CONFERMA_modaleEditStanziamenti').show();
		$('#chiudiModale').prop("disabled",true);
		$('#INSERT_modificaComponenti').prop("disabled",true);
		$('#EDIT_confermaOperazione').substituteHandler('click', confermaModificaStanziamentiComponenti.bind(undefined, uuid));
	}
	
	// SIAC-8859
	function apriModaleConfermaNuovaComponente(){
	
		$('#CONFERMA_modaleEditComponenti').show();
		$('#chiudiModaleComp').prop("disabled",true);
		$('#INSERT_modifica').prop("disabled",true);
		$('#EDIT_confermaCompOperazione').substituteHandler('click', ComponenteImportiCapitolo.confermaNuovaComponente);
		//$('#EDIT_confermaCompIndietro').substituteHandler('click', $('#CONFERMA_modaleEditComponenti').hide()); 
	}
	
	function apriModaleModificaImporti(righe, event){
		var pulsante = $(event && event.delegateTarget);
		var $modal = $('#editStanziamentiComponente');
		var riga;
		
		$modal.find('.hide-modal-element-componenti').hide();
		$modal.find('.hide-modal-element-importo').show();
		
		$('#editstanziamenti_campiHidden').find('input').val('');
		
		var rigaImporto = TabellaImportiComponenteCapitolo.creaTabellaPerModificaImportoPerTipoComponente(righe, pulsante);
		
		$('#tipoImportoString').val(rigaImporto.tipoImportiCapitoloTabella.descrizioneCella);
		
		$modal.modal('show');
		
		// SIAC-8859 richiesta conferma
		//$modal.find('.button-conferma-importi').substituteHandler('click', confermaModificaStanziamenti.bind(undefined,rigaImporto.tipoImportiCapitoloTabella.descrizioneCella));
		$('#CONFERMA_modaleEditStanziamenti').hide();
		$('#ERRORI_modaleEditStanziamenti').hide();
		$('#INSERT_modificaImporti').prop("disabled",false);
		$('#chiudiModale').prop("disabled",false);
				
		$modal.find('.button-conferma-importi').substituteHandler('click', apriModaleConfermaOperazioneImporti.bind(undefined,rigaImporto.tipoImportiCapitoloTabella.descrizioneCella));
	}
	
	function apriModaleModificaComponenti(righe, event){
		var pulsante = $(event && event.delegateTarget);
		var $modal = $('#editStanziamentiComponente');
		var riga;
		
		$modal.find('.hide-modal-element-importo').hide();
		$modal.find('.hide-modal-element-componenti').show();
		
		$('#editstanziamenti_campiHidden').find('input').val('');
		
		var rigaImporto = TabellaImportiComponenteCapitolo.creaTabellaPerModificaImportoPerTipoComponente(righe, pulsante);
		
		$modal.modal('show');	
		
		// SIAC-8859 richiesta conferma
		//$modal.find('.button-conferma-componenti').substituteHandler('click', confermaModificaStanziamentiComponenti.bind(undefined, rigaImporto.uidTipoComponenteImportiCapitolo));		
		$('#CONFERMA_modaleEditStanziamenti').hide();
		$('#ERRORI_modaleEditStanziamenti').hide();
		$('#INSERT_modificaComponenti').prop("disabled",false);
		$('#chiudiModale').prop("disabled",false);
		
		$modal.find('.button-conferma-componenti').substituteHandler('click', apriModaleConfermaOperazioneComponenti.bind(undefined, rigaImporto.uidTipoComponenteImportiCapitolo));	
	}
	
	function apriModaleEliminaComponente(righe, event){
		var pulsante = $(event && event.delegateTarget);
		var $modal = $('#msgElimina');
		var index = pulsante.data('index-element');
		var rigaImporto = righe[index];
		
		$modal.modal('show');
		$('#EDIT_elimina').substituteHandler('click', confermaEliminaStanziamentiComponenti.bind(undefined, rigaImporto.uidTipoComponenteImportiCapitolo));
	}
	
	/** CHIAMATE AL CARICAMENTO DELLA TABELLA*/
	
	function creaTabella(righeComp, righeImporti){
		
		TabellaImportiComponenteCapitolo.creaEPosizionaRigheImportiConCallback(righeImporti, apriModaleModificaImporti, undefined);
				
		$('#competenzaCella').substituteHandler('click', TabellaImportiComponenteCapitolo.creaEPosizionaRigheComponentiConCallback.bind(undefined, righeComp, apriModaleModificaComponenti, apriModaleEliminaComponente));
	}
	
	function caricaTabella(){
		var overlay = $('#tabellaStanziamentiPerComponenti').overlay({usePosition: true});
		overlay.overlay('show');
		$.postJSON(
			"gestioneComponenteImportoCapitoloNelCapitolo_caricaImporti.do",
			{},
			function(data) {
				var righeComp;
				var righeImporti;
				// Controllo gli eventuali errori, messaggi, informazioni
				if(impostaDatiNegliAlert(data.errori, $alertErrori)) {return;	}
				if(impostaDatiNegliAlert( data.messaggi, $alertMessaggi)) {return;	}
				if(impostaDatiNegliAlert(data.informazioni, $alertInformazioni)) {return;	}
				
				creaTabella(data.righeComponentiTabellaImportiCapitolo, data.righeImportiTabellaImportiCapitolo);
			
			}
		).always(overlay.overlay.bind(overlay, 'hide'));
	}
		
	return exports;
	
	}({}));


/* Document ready */
$(function () {
    ComponenteImportiCapitolo.caricaTabella();
    $("#button_inserisciModifica").substituteHandler('click', ComponenteImportiCapitolo.apriModaleNuovaComponente );
	// SIAC-8859
	//$("#INSERT_modifica").substituteHandler('click',  ComponenteImportiCapitolo.confermaNuovaComponente );
	$("#INSERT_modifica").substituteHandler('click',  ComponenteImportiCapitolo.apriModaleConfermaNuovaComponente );	
	$('#dropdown-select-com').substituteHandler("change", ComponenteImportiCapitolo.onChangeTipoComponenteNuovaComponente);
	
	

	
});