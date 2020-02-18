/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Dismissione = function($,global) {
	var exports = {};
	var $modaleEliminazione = $('#modaleEliminazione');
	var datatable;
	
	exports.impostaTabella = impostaTabella;

	function eliminaDismissione(uidDismissione){
		var tabellaDatatable = $('#tabellaRisultatiRicercaDismissione').dataTable();
		 
		var spinner = $('#SPINNER_modaleEliminazionePulsanteSalvataggio').addClass('activated');
		return $.postJSON('risultatiRicercaDismissioneCespite_elimina.do',{'uidDismissioneCespite': uidDismissione})
		    	.then(function(data){
		    		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
						$modaleEliminazione.modal('hide');
						$('#tabellaRisultatiRicercaDismissione').overlay('show');
						return $.Deferred().reject().promise();
					}
		    		impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'),undefined, true);
					$modaleEliminazione.modal('hide');
					$('#tabellaRisultatiRicercaDismissione').overlay('show');
				})
				.then(tabellaDatatable.fnDraw.bind(tabellaDatatable))
				.always(spinner.removeClass.bind(spinner,"activated"));
		        
	}
	
	function impostaEliminazioneDismissione(dismissione){
    	var button = $modaleEliminazione.find('#modaleEliminazionePulsanteSalvataggio');
    	var spanInformazioni = $modaleEliminazione.find('#modaleEliminazioneElementoSelezionato');
    	spanInformazioni.html('dismissione ' + dismissione.elenco);
    	button.on('click', eliminaDismissione.bind(undefined, dismissione.uid));
    	$modaleEliminazione.modal('show');
	}
	
	function effettuaScritture(dismissione){
		var pulsante = $('form').overlay('show');
		return $.postJSON('risultatiRicercaDismissioneCespite_effettuaScritture.do', {'uidDismissioneCespite': dismissione.uid})
			.then(function(data){
			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
				return;
			}
			impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
		}).always($('form').overlay.bind($('form'),"hide"));
	}
	
	function impostaTabella() {
		 var tabella = $('#tabellaRisultatiRicercaDismissione').overlay('show');
	        var opts = {
	            bServerSide: true,
	            sServerMethod: 'POST',
	            sAjaxSource : 'risultatiRicercaDismissioneCespiteAjax.do',
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
	                $('#tabellaRisultatiRicercaCespite_processing').parent('div')
	                    .hide();
	                tabella.find('a[rel="popover"]').popover();
	                tabella.overlay('hide');
	            },
	            aoColumnDefs: [
	                {aTargets: [0], mData: defaultPerDataTable('elenco')},
	                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
	                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
	                {aTargets: [3], mData: defaultPerDataTable('dataCessazione')},
	                {aTargets: [4], mData: defaultPerDataTable('causaleDismissione')},
	                {aTargets: [5], mData: defaultPerDataTable('numeroCespitiCollegati')},
	                {aTargets: [6], mData: defaultPerDataTable('statoMovimento')},
	                {aTargets: [7], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
	                    $(nTd).find('a.eliminaDismissioneCespite')
	                        	.substituteHandler('click', impostaEliminazioneDismissione.bind(undefined,oData,nTd))
	                    $(nTd).find('a.effettuaScritture')
	                    		.substituteHandler('click', 
	                    			impostaRichiestaConfermaUtente.bind(undefined,'Si stanno per effettuare le scritture sulla dismissione. Si desidera proseguire?',effettuaScritture.bind(undefined,oData)));
	                    
	                    
	                }}
	            ]
	        };
	        
	        datatable = tabella.dataTable(opts);
	    }
	
	
	
	
   return exports;
	
}(jQuery, this);

$(function() {
	
	Dismissione.impostaTabella();
});