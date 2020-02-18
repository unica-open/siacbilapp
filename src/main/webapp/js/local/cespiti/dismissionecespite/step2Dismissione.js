/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Dismissione = function($,global) {
	"use strict";
	var alertErrori = $('#ERRORI');
	var alertInfo = $('#INFORMAZIONI');
	var prefisso = $('#HIDDEN_baseUrl').val();
	var alertInformazioni = $('#INFORMAZIONI');
	var exports = {};
	exports.chiamaCollegaCespite = chiamaCollegaCespite;
	exports.effettuaScritture = effettuaScritture;
	exports.impostaScollegamentoCespite = impostaScollegamentoCespite;
	
	function ricaricaTabella(){
		var tabellaDatatable = $('#tabellaCespitiCollegati')
			.overlay('show')
			.dataTable()
			.fnDraw();
	}
	
	function chiamaCollegaCespite(evento){
		var arrayUid = evento.uidsCespiti;
		var url
		var obj = {};
		var $form = $('form').overlay('show');
		
		obj.uidsCespitiDaCollegare = arrayUid;
		return $.postJSON(prefisso + '_collegaCespite.do', qualify(obj))
			.then(function(data){
			if(impostaDatiNegliAlert(data.errori, alertErrori)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni, alertInformazioni, undefined, true);
		}).always($form.overlay.bind($form,'hide'))
		.then(ricaricaTabella);
		
	}
	
	function scollegaCespite(uidCespite, nTd){
		var row = $(nTd).closest("tr").overlay("show");
		var obj = {};
		obj.uidCespiteDaScollegare = uidCespite;
		return $.postJSON(prefisso + '_scollegaCespite.do', qualify(obj))
			.then(function(data){
			if(impostaDatiNegliAlert(data.errori, alertErrori, undefined, true)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni, alertInformazioni);
			$('#modaleEliminazione').modal('hide');
			}).always(row.overlay.bind(row,'hide'))
			.then(ricaricaTabella);
	}
	
	function impostaScollegamentoCespite(cespite, nTd){
		var modale = $('#modaleEliminazione');
    	var button = modale.find('#modaleEliminazionePulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleEliminazioneElementoSelezionato');
    	spanInformazioni.html('cespite ' + cespite.codice);
    	button.substituteHandler('click', scollegaCespite.bind(undefined, cespite.uid,nTd));
    	modale.modal('show');
	}
	
	function effettuaScritture(e){
		var pulsante = $('form').overlay('show');
		return $.postJSON(prefisso + '_effettuaScritture.do', {})
			.then(function(data){
			if(impostaDatiNegliAlert(data.errori, alertErrori)){
				return;
			}
			impostaDatiNegliAlert(data.informazioni, alertInformazioni);
		}).always($('form').overlay.bind($('form'),"hide"));
	}
	
	return exports;
	
}(jQuery, this);

$(function() {
	CespitiCollegatiDismissione.impostaTabella(Dismissione.impostaScollegamentoCespite);
	Cespite.inizializza(true);
	$(document).on('cespitiCaricati', Dismissione.chiamaCollegaCespite);
	$('#pulsanteEffettuascritture').substituteHandler('click', impostaRichiestaConfermaUtente.bind(undefined,'Si stanno per effettuare le scritture sulla dismissione. Si desidera proseguire?',Dismissione.effettuaScritture));
});