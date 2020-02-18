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
	                {aTargets: [0], mData: defaultPerDataTable('codiceDescrizione')},
//	                {aTargets: [1], mData: defaultPerDataTable('descrizione')},
	                {aTargets: [1], mData: defaultPerDataTable('tipoBene'), fnCreatedCell: doAddClassToElement('text-right')},
	                {aTargets: [2], mData: defaultPerDataTable('inventario'), fnCreatedCell: doAddClassToElement('text-center')},
	                {aTargets: [3], mData: defaultPerDataTable('dataAccessoInventario'), fnCreatedCell: doAddClassToElement('text-right')},
	                {aTargets: [4], mData: defaultPerDataTable('valoreIniziale'), fnCreatedCell: doAddClassToElement('text-right')},
	                {aTargets: [5], mData: defaultPerDataTable('valoreAttuale'), fnCreatedCell: doAddClassToElement('text-right')},
	                {aTargets: [6], mData: defaultPerDataTable('importoAmmortamentoSuSingoloAnno'), fnCreatedCell: doAddClassToElement('text-right')}
	            ]
	        };
	        
	        datatable = tabella.dataTable(opts);
	    }
	
	
	
	
    return exports;
}(jQuery));

$(function() {
	RisultatiRicerca.impostaTabella();
	
});