/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RisultatiRicerca = (function($) {
	"use strict";
	var alertErrori = $('#ERRORI');
	var alertInfo = $('#INFORMAZIONI');
	var datatable;
	var urlRicaricaTabella ='risultatiRicercaCategoriaCespitiAjax.do';
	var exports = {};
	exports.impostaTabella = impostaTabella;
	
	function eliminaCategoria(uidCategoria){
		var tabellaDatatable = $('#tabellaRisultatiRicercaCategoriaCespiti').dataTable();
		var spinner = $('#SPINNER_modaleEliminazionePulsanteSalvataggio').addClass('activated');
		return $.postJSON('risultatiRicercaCategoriaCespiti_eliminaCategoria.do',{'uidCategoriaCespiti': uidCategoria})
		    	.then(function(data){
					if(impostaDatiNegliAlert(data.errori, alertErrori)){
						$('#modaleEliminazione').modal('hide');
							 return $.Deferred().reject().promise();
						}
						impostaDatiNegliAlert(data.informazioni, alertInfo);
						$('#modaleEliminazione').modal('hide');
					})
				.always(spinner.removeClass.bind(spinner,"activated"))
		        .then(tabellaDatatable.fnDraw.bind(tabellaDatatable));
	}
	
    function impostaEliminazioneCategoria(categoria){
    	var modale = $('#modaleEliminazione');
    	var button = modale.find('#modaleEliminazionePulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleEliminazioneElementoSelezionato');
    	var spanInformazioniAggiuntive = modale.find('#modaleEliminazioneInformazioniAggiuntive');
    	spanInformazioni.html('categoria cespiti ' + categoria.codiceCategoria);
    	spanInformazioniAggiuntive.html("L'eliminazione comporta la cancellazione del dato anche per gli altri anni di bilancio.");
    	button.substituteHandler('click', eliminaCategoria.bind(undefined, categoria.uid));
    	modale.modal('show');
    }
    
    function annullaCategoria(uidCategoria){
        var tabellaDatatable = $('#tabellaRisultatiRicercaCategoriaCespiti').dataTable();
        var spinner = $('#SPINNER_modaleAnnullamentoPulsanteSalvataggio').addClass('activated');
        
        // SIAC-6377
        return annullaCategoriaAjaxCall(uidCategoria, false, true)
        .then(tabellaDatatable.fnDraw.bind(tabellaDatatable))
        .always(spinner.removeClass.bind(spinner,"activated"));
    }
    
    function annullaCategoriaAjaxCall(uid, force, mayAsk) {
        var params = {'uidCategoriaCespiti': uid, 'force': !!force};
        return $.postJSON('risultatiRicercaCategoriaCespiti_annullaCategoria.do', params)
        .then(function(data) {
            $('#modaleAnnullamento').modal('hide');
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            if(mayAsk && data.messaggi && data.messaggi.length) {
                return askProsecuzione(uid, data.messaggi);
            }
            impostaDatiNegliAlert(data.informazioni, alertInfo);
        });
    }
    
    /**
     * Richiesta per la prosecuzione
     * @param uid l'uid
     * @param msg i messaggi per l'utente
     * @returns Promise
     */
    function askProsecuzione(uid, msg) {
        var deferred = $.Deferred();
        var message = msg.map(function(el) {
            return el.codice + ' - ' + el.descrizione;
        }).join('<br/>');
        bootboxAlert(message, 'Attenzione', 'dialogWarn', [
            {label: 'No, indietro', 'class': 'btn', callback: deferred.reject.bind(deferred)},
            {label: 'S&igrave; prosegui', 'class': 'btn', callback: function(){
                annullaCategoriaAjaxCall(uid, true, false)
                .then(deferred.resolve.bind(deferred), deferred.reject.bind(deferred));
                return true;
            }}
        ]);
        return deferred.promise();
    }
	
    function impostaAnnullamentoCategoria(categoria){
    	var modale = $('#modaleAnnullamento');
    	var button = modale.find('#modaleAnnullamentoPulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleAnnullamentoElementoSelezionato');
    	
    	spanInformazioni.html('categoria cespiti ' + categoria.codiceCategoria);
    	button.substituteHandler('click', annullaCategoria.bind(undefined, categoria.uid));
    	modale.modal('show');
    }
	
	 function impostaTabella() {
		 var tabella = $('#tabellaRisultatiRicercaCategoriaCespiti').overlay('show');
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
	                $('#tabellaRisultatiRicercaCategoriaCespiti_processing').parent('div')
	                    .hide();
	                tabella.overlay('hide');
	                $("a[rel='popover']", "#tabellaRisultatiRicercaCategoriaCespiti").popover();
	            },
	            aoColumnDefs: [
	                {aTargets: [0], mData: defaultPerDataTable('codiceCategoria')},
	                {aTargets: [1], mData: defaultPerDataTable('statoCategoria')},
	                {aTargets: [2], mData: defaultPerDataTable('descrizioneCategoria')},
	                {aTargets: [3], mData: defaultPerDataTable('aliquotaCategoria')},
	                {aTargets: [4], mData: defaultPerDataTable('tipoCalcoloCategoria')},
	                {aTargets: [5], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
	                    $(nTd).find('a.eliminaCategoria')
	                        .substituteHandler('click', impostaEliminazioneCategoria.bind(undefined,oData,nTd));
	                    $(nTd).find('a.annullaCategoria')
                        .substituteHandler('click', impostaAnnullamentoCategoria.bind(undefined,oData,nTd));
	                }}
	            ]
	        };
	        
	        datatable = tabella.dataTable(opts);
	    }
	
	
	
	
    return exports;
}(jQuery));

$(function() {
	RisultatiRicerca.impostaTabella();
	
});