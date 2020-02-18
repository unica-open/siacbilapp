/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RisultatiRicerca = (function($) {
	"use strict";
	var alertErrori = $('#ERRORI');
	var alertInfo = $('#INFORMAZIONI');
	var datatable;
	var urlRicaricaTabella ='risultatiRicercaCespiteAjax.do';
	var exports = {};
	exports.impostaTabella = impostaTabella;	 
     
	
	function eliminaCespite(uidCespite){
		var tabellaDatatable = $('#tabellaRisultatiRicercaCespite').dataTable();
		var spinner = $('#SPINNER_modaleEliminazionePulsanteSalvataggio').addClass('activated');
		return $.postJSON('risultatiRicercaCespite_elimina.do',{'uidCespite': uidCespite})
		    	.then(function(data){
					if(impostaDatiNegliAlert(data.errori, alertErrori)){
						$('#modaleEliminazione').modal('hide');
							 return $.Deferred().reject().promise();
						}
						impostaDatiNegliAlert(data.informazioni, alertInfo);
						$('#modaleEliminazione').modal('hide');
						$('#tabellaRisultatiRicercaCespite').overlay('show');
					})
				.always(spinner.removeClass.bind(spinner,"activated"))
		        .then(tabellaDatatable.fnDraw.bind(tabellaDatatable));
	}
	
    function impostaEliminazioneCespite(cespite){
    	var modale = $('#modaleEliminazione');
    	var button = modale.find('#modaleEliminazionePulsanteSalvataggio');
    	var spanInformazioni = modale.find('#modaleEliminazioneElementoSelezionato');
    	spanInformazioni.html('cespite ' + cespite.codice);
    	button.on('click', eliminaCespite.bind(undefined, cespite.uid));
    	modale.modal('show');
    }
    
    function goToDismissioni(cespite, nTd){
    	$(nTd).closest('row').overlay('show');
    	document.location = 'risultatiRicercaCespite_dismissioni.do?uidDismissioneCollegata=' + cespite.uidDismissioneCespiteCollegata + "&uidCespite=" + cespite.uid;
    	
    }
	
	 function impostaTabella() {
		 var tabella = $('#tabellaRisultatiRicercaCespite').overlay('show');
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
	                $('#tabellaRisultatiRicercaCespite_processing').parent('div')
	                    .hide();
	                tabella.find('a[rel="popover"]').popover();
	                tabella.overlay('hide');
	            },
	            aoColumnDefs: [
	                {aTargets: [0], mData: defaultPerDataTable('codice')},
	                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
	                {aTargets: [2], mData: defaultPerDataTable('tipoBene')},
	                {aTargets: [3], mData: defaultPerDataTable('classificazione')},
	                {aTargets: [4], mData: defaultPerDataTable('inventario')},
	                {aTargets: [5], mData: defaultPerDataTable('attivo')},
	                {aTargets: [6], mData: defaultPerDataTable('donazioneRinvenimento')},
	                {aTargets: [7], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
	                    $(nTd).find('a.eliminaCespite')
	                        .substituteHandler('click', impostaEliminazioneCespite.bind(undefined,oData,nTd));
	                    $(nTd).find('a.dismissioni') 
                        .substituteHandler('click', goToDismissioni.bind(undefined,oData,nTd));
	                    
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