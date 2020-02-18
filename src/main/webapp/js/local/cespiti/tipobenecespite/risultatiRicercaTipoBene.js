/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RisultatiRicerca = (function($) {
	"use strict";
	var alertErrori = $('#ERRORI');
	var alertInfo = $('#INFORMAZIONI');
	var datatableTipoBene;
	var urlRicaricaTabella ='risultatiRicercaTipoBeneAjax.do';
	var exports = {};
	var tabella = $('#tabellaRisultatiRicercaTipoBeneCespite');
	exports.impostaTabella = impostaTabella;	 
     
	
	function eliminaTipoBene(uidTipoBene){
		var spinner = $('#SPINNER_modaleEliminazionePulsanteSalvataggio').addClass('activated');
		return $.postJSON('risultatiRicercaTipoBene_eliminaTipoBene.do',{'uidTipoBeneCespite': uidTipoBene})
		    	.then(function(data){
					if(impostaDatiNegliAlert(data.errori, alertErrori)){
							$('#modaleEliminazione').modal('hide');
							 return $.Deferred().reject().promise();
						}
						impostaDatiNegliAlert(data.informazioni, alertInfo);
						$('#modaleEliminazione').modal('hide');
					})
				.then(tabella.fnDraw.bind(tabella))
				.always(spinner.removeClass.bind(spinner,"activated"));
	}
    function annullaTipoBene(uidTipoBene){
    	var spinner = $('#SPINNER_modaleAnnullamentoPulsanteSalvataggio').addClass('activated');
    	var annoAnnullamento = $('#annoAnnullamento').val();
		return annullaTipoBeneAjaxCall(uidTipoBene, annoAnnullamento, false, true)
		.then(tabella.fnDraw.bind(tabella))
		.always(spinner.removeClass.bind(spinner,"activated"));
    }
    
    function annullaTipoBeneAjaxCall(uid, annoAnnullamento, force, mayAsk) {
        var params = {'uidTipoBeneCespite': uid, 'annoAnnullamento': annoAnnullamento, 'force': !!force};
        return $.postJSON('risultatiRicercaTipoBene_annullaTipoBene.do', params)
        .then(function(data) {
            $('#modaleAnnullamento').modal('hide');
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            if(mayAsk && data.messaggi && data.messaggi.length) {
                return askProsecuzione(uid, annoAnnullamento, data.messaggi);
            }
            impostaDatiNegliAlert(data.informazioni, alertInfo);
        });
    }
    
    /**
     * Richiesta per la prosecuzione
     * @param uid l'uid
     * @param annoAnnullamento l'anno di annullamento
     * @param msg i messaggi per l'utente
     * @returns Promise
     */
    function askProsecuzione(uid, annoAnnullamento, msg) {
        var deferred = $.Deferred();
        var message = msg.map(function(el) {
            return el.codice + ' - ' + el.descrizione;
        }).join('<br/>');
        bootboxAlert(message, 'Attenzione', 'dialogWarn', [
            {label: 'No, indietro', 'class': 'btn', callback: deferred.reject.bind(deferred)},
            {label: 'S&igrave; prosegui', 'class': 'btn', callback: function(){
                annullaTipoBeneAjaxCall(uid, annoAnnullamento, true, false)
                .then(deferred.resolve.bind(deferred), deferred.reject.bind(deferred));
                return true;
            }}
        ]);
        return deferred.promise();
    }
    
    function impostaAnnullaTipoBene(tipobene){
    	var modale = $('#modaleAnnullamento');
    	var button = modale.find('#modaleAnnullamentoPulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleAnnullamentoElementoSelezionato');
    	spanInformazioni.html('tipo bene ' + tipobene.codice);
    	button.substituteHandler('click', annullaTipoBene.bind(undefined, tipobene.uid));
    	modale.modal('show');    	
    }
    
    function impostaEliminazioneTipoBene(tipobene){
    	var modale = $('#modaleEliminazione');
    	var button = modale.find('#modaleEliminazionePulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleEliminazioneElementoSelezionato');
    	var spanInformazioniAggiuntive = modale.find('#modaleEliminazioneInformazioniAggiuntive');
    	spanInformazioni.html('tipo bene ' + tipobene.codice);
    	spanInformazioniAggiuntive.html("L'eliminazione comporta la cancellazione del dato anche per gli altri anni di bilancio.");
    	button.substituteHandler('click', eliminaTipoBene.bind(undefined, tipobene.uid));
    	modale.modal('show');    	
    }
	
	 function impostaTabella() {
	     var opts = {
	         bServerSide: true,
	         sServerMethod: 'POST',
	         sAjaxSource : urlRicaricaTabella,
	         bPaginate: true,
	         bLengthChange: false,
	         iDisplayLength: 10,
	         iDisplayStart: $('#HIDDEN_savedDisplayStart').val() || 0,
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
	             sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
	             oPaginate: {
	                 sFirst: 'inizio',
	                 sLast: 'fine',
	                 sNext: 'succ.',
	                 sPrevious: 'prec.',
	                 sEmptyTable: 'Nessun dato disponibile'
	             }
	         },
	         fnPreDrawCallback: defaultPreDraw,
	         // Chiamata al termine della creazione della tabella
	         fnDrawCallback: function (settings) {
	             var records = settings.fnRecordsTotal();
	             var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
	             $('#id_num_result').html(testo);
	             // Nascondo il div del processing
	             $('#tabellaRisultatiRicercaTipoBeneCespite_processing').parent('div')
	                 .hide();
	             tabella.overlay('hide');
	             $("a[rel='popover']", "#tabellaRisultatiRicercaTipoBeneCespite").popover();
	         },
	         aoColumnDefs: [
	             {aTargets: [0], mData: defaultPerDataTable('codice')},
	             {aTargets: [1], mData: defaultPerDataTable('stato') },
	             {aTargets: [2], mData: defaultPerDataTable('descrizione')},
	             {aTargets: [3], mData: defaultPerDataTable('categoria')},
	             {aTargets: [4], mData: defaultPerDataTable('statoCategoria') },
	             {aTargets: [5], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
	                 $(nTd).find('a.eliminaTipoBene')
	                     .substituteHandler('click', impostaEliminazioneTipoBene.bind(undefined,oData,nTd));
	                 $(nTd).find('a.annullaTipoBene')
                     	.substituteHandler('click', impostaAnnullaTipoBene.bind(undefined,oData,nTd));
	             }}
	         ]
	    };
	        
	    datatableTipoBene = tabella.dataTable(opts);
    }
	
	
	
	
    return exports;
}(jQuery));

$(function() {
	RisultatiRicerca.impostaTabella();
	
});