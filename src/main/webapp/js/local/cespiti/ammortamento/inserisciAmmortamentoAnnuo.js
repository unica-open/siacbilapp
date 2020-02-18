/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var AmmortamentoAnnuo = function($,global) {
	"use strict";
	var exports = {};
	var selectedDatas = {};
	var $alertErrori = $('#ERRORI');
	var $alertInformazioni = $('#INFORMAZIONI');
	var $risultatiRicerca = $('#risultatiRicercaAnteprimaAmmortamento');
	
	exports.caricaDettagliAnteprima = caricaDettagliAnteprima;
	exports.gestisciApriAnteprima = gestisciApriAnteprima;
	exports.inserisciAnteprima = inserisciAnteprima;
	exports.effettuaScritture = effettuaScritture;
	
	function creaObjAnno(){
		var obj={};
		obj.annoAmmortamentoAnnuo = $('#annoAmmortamentoAnnuo').val();
		return qualify(obj);
	}
	
	function impostaTabellaAnteprima(){
		 var $table=$('#tabellaAnteprima').overlay('show');
		 var opts = {
		            bServerSide: true,
		            sAjaxSource: 'risultatiRicercaDettaglioAnteprimaAmmortamentoAnnuoAjax.do',
		            sServerMethod: "POST",
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
		                sZeroRecords: "Non sono presenti quote corrispondenti ai parametri inseriti",
		                oPaginate: {
		                    sFirst: "inizio",
		                    sLast: "fine",
		                    sNext: "succ.",
		                    sPrevious: "prec.",
		                    sEmptyTable: "Nessun elenco disponibile"
		                }
		            },
		            fnPreDrawCallback: function(opts) {
		                defaultPreDraw(opts);
		            },
		            // Chiamata al termine della creazione della tabella
		            fnDrawCallback: function (opts) {
		                // Nascondo il div del processing
		                defaultDrawCallback(opts);
		                // Attivo i popover
//		                $table.find("a[rel='popover']").popover();
		                $table.overlay('hide');
		            },
		            aoColumnDefs: [
						{aTargets: [0], mData: defaultPerDataTable("codiceConto")},
						{aTargets: [1], mData: defaultPerDataTable('descrizioneConto')},
						{aTargets: [2], mData: defaultPerDataTable('numeroCespiti'), fnCreatedCell: function(nTd) {
		                    $(nTd).addClass("tab_Right");
		                }},
						{aTargets: [3], mData: defaultPerDataTable('importoDare'), fnCreatedCell: function(nTd) {
		                    $(nTd).addClass("tab_Right");
		                }},
						{aTargets:[4], mData: defaultPerDataTable('importoAvere'), fnCreatedCell: function(nTd) {
		                    $(nTd).addClass("tab_Right");
		                }},
		                {aTargets:[5], mData: function(){
		                	return  '<button type="button" class="btn btn-secondary">dettaglio</button>';
		                }, fnCreatedCell: function(nTd,sData, oData) {
		                	 $(nTd).find('button').substituteHandler('click', redirectDettaglioCespiti.bind(undefined,oData, nTd) );
		                }},
		               ]
		        };
		        $table.dataTable(opts);
	 }
	
	function redirectDettaglioCespiti(dettaglio){
		var objToSend = {};
		var $form = $('form');
		var str;
		if(!dettaglio){
			return;
		}
		
		str = '<input type="hidden" name="codiceContoDettaglio" value="' + dettaglio.codiceConto + '"/>'
		+ '"<input type="hidden" name="uidDettaglioAnteprima" value="' + dettaglio.uid + '"/>'
		+ '"<input type="hidden" name="segnoDettaglio" value="' + dettaglio.segno._name + '"/>'; 
		$form.append(str);
		
		$form.attr('action', 'inserisciAmmortamentoAnnuo_dettaglioCespiti.do');
		$form.submit();
	}
	 
	 function caricaDettagliAnteprima(){
		 
		 $risultatiRicerca.overlay('show').slideDown();
		 var $divRisultati = $('#fieldsetRisultatiRicercaAnteprimaAmmortamento').slideUp();
		 return $.postJSON('inserisciAmmortamentoAnnuo_ricercaDettagliAnteprimaAmmortamentoCespite.do', {'annoAmmortamentoAnnuo': $('#annoAmmortamentoAnnuo').val()})
		 .then(function (data){
			 if(impostaDatiNegliAlert(data.errori, $alertErrori)){
				 return;
			 }
			 $divRisultati.slideDown();
			 impostaTabellaAnteprima();
		 })
		 .always($risultatiRicerca.overlay.bind($risultatiRicerca, 'hide'));
	 }
	 
	 
	 function inserisciAnteprima(){
		var $fieldset = $('#campiAmmortamentoAnnuo').overlay('show');
		$risultatiRicerca.slideUp();
		impostaDatiNegliAlert(['Inserimento anteprima in corso...'], $alertInformazioni);
		return $.postJSON('inserisciAmmortamentoAnnuo_inserisciAnteprimaAmmortamentoAnnuo.do',creaObjAnno())
		 .then(function(data){
			 if(impostaDatiNegliAlert(data.errori, $alertErrori)){
				 return;
			 }
			 $alertInformazioni.slideUp();
			 caricaDettagliAnteprima();
		 })
		 .always($fieldset.overlay.bind($fieldset, 'hide'));
	 }
	 
	
	 function gestisciApriAnteprima(){
		 var $fieldset = $('#campiAmmortamentoAnnuo').overlay('show');
		 $risultatiRicerca.slideUp();
		 return $.postJSON('inserisciAmmortamentoAnnuo_caricaAnteprimaPrecedente.do', creaObjAnno())
		 .then(function(data){
			 var msg ="";
			 if(impostaDatiNegliAlert(data.errori, $alertErrori)){
				 return;
			 }
			 if(!data.anteprimaAmmortamentoAnnuoCespite || !data.anteprimaAmmortamentoAnnuoCespite.uid){
				 return inserisciAnteprima();
			 }
			 msg = 'Esiste gi&agrave; un\' anteprima nel sistema per l\' anno indicato. &Egrave; possibile caricare i dati precedenti oppure inserirne di nuovi. <br/> Si noti che il pulsante "inserisci nuova anteprima" porta alla cancellazione dell\'anteprima precedente.';
			 impostaRichiestaConfermaUtente(msg, inserisciAnteprima, caricaDettagliAnteprima, "inserisci nuova", "carica dati precedenti");
		 }).always($fieldset.overlay.bind($fieldset, 'hide'));
	 }
	 
	 
	 function effettuaScritture(){
		 var $fieldset = $('body').overlay('show');
			return $.postJSON('inserisciAmmortamentoAnnuo_effettuaScrittureAmmortamento.do',creaObjAnno())
			 .then(function(data){
				 if(impostaDatiNegliAlert(data.errori, $alertErrori)){
					 return;
				 }
				 impostaDatiNegliAlert(data.informazioni, $alertInformazioni)
			 })
			 .always($fieldset.overlay.bind($fieldset, 'hide'));
	 }
	
	return exports;

	
}(jQuery, this);

$(function() {
	$('#pulsanteInserisciAnteprimaCespiti').substituteHandler('click', AmmortamentoAnnuo.gestisciApriAnteprima);
	
	$('#pulsanteEffettuaScritture').substituteHandler('click', AmmortamentoAnnuo.effettuaScritture);
	
});