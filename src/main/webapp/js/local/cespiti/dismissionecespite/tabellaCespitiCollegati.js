/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var CespitiCollegatiDismissione = function($,global) {
	"use strict";
	var exports = {};
	var urlRicaricaTabella ='risultatiRicercaCespiteDaDismissioneAjax.do';
	
	exports.impostaTabella = impostaTabella;
	
	function impostaTabella(fncEdit) {
		var tabella = $('#tabellaCespitiCollegati').overlay('show');
		var editabile = fncEdit !== undefined && typeof fncEdit ==="function";
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
		    bProcessing: false,
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
		    	defaultDrawCallbackRisultatiRicerca('#id_num_result');
		        tabella.overlay('hide');
		    },
		    aoColumnDefs: [
		        {aTargets: [0], mData: defaultPerDataTable('codice')},
		        {aTargets: [1], mData: defaultPerDataTable('inventario')},
		        {aTargets: [2], mData: defaultPerDataTable('descrizione')},
		        {aTargets: [3], mData: defaultPerDataTable('tipoBene')},
		        {aTargets: [4], mData: defaultPerDataTable('valoreAttuale'), fnCreatedCell: doAddClassToElement('text-right')},
		        {aTargets: [5], mData: defaultPerDataTable('fondoAmmortamento'), fnCreatedCell: doAddClassToElement('text-right')},
		        {aTargets: [6], mData: defaultPerDataTable('residuoAmmortamento'), fnCreatedCell: doAddClassToElement('text-right')},
		        {aTargets: [7], bVisible: editabile, mData: defaultPerDataTable('azioneScollegamento'), fnCreatedCell: function(nTd, sData, oData) {
		           if(editabile){
		        	$(nTd).find('a.scollegaCespite')
		                .substituteHandler('click', fncEdit.bind(undefined,oData,nTd));
		           }
		        }}
		    ]
		};

	    tabella.dataTable(opts);
	}
	
	
	return exports;
	
}(jQuery, this);