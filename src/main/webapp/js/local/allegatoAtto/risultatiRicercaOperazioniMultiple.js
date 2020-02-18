/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RisultatiRicercaOperazioniMultiple = function($,w){
    // Per lo sviluppo
    'use strict';
    var exports = {};
    var selectedDatas = {};
    var baseUrlCompletamento = "risultatiRicercaAllegatoAttoOperazioniMultiple_completa";
    var baseUrlConvalida = "risultatiRicercaAllegatoAttoOperazioniMultiple_convalida";
    var $alertErrori = $('#ERRORI');
    var $alertInformazioni = $('#INFORMAZIONI');
    
    exports.gestioneTabella = gestioneTabella;
    exports.gestioneCompletamentoMultiplo = gestioneCompletamentoMultiplo;
    exports.gestioneConvalidaMultiplo = gestioneConvalidaMultiplo;
    exports.gestioneCheckSuAllegato = gestioneCheckSuAllegato;
    exports.controlloImporti = controlloImporti;

    // *********************************************************
    //                FUNZIONI DI UTILITA' DI BASE
    //**********************************************************
    function filterOutUndefined(el) {
        return el !== undefined;
    }
    
    function mapToUid(el){
    	return el.uid;
    }
    
    // *********************************************************
    //                FUNZIONI DI UTILITA' SPECIFICHE
    //**********************************************************
    function gestioneCheckSuAllegato() {
        var $this = $(this);
        var isChecked;
        isChecked = !$this.attr('disabled') && $this.prop('checked');
        selectedDatas[+$this.val()] = {isSelected: isChecked, data: $this.data('originalAllegato')};
    }
    
    function creaListaUidsAllegatiSelezionati(){
    	var idsAllegatiSelezionati = [];
    	var listaAllegati = [];
    	var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	listaAllegati.push(selectedDatas[i].data);
            }
        }
        idsAllegatiSelezionati = listaAllegati.filter(filterOutUndefined).map(mapToUid);
        return idsAllegatiSelezionati;
    }
    
    // *********************************************************
    //                FUNZIONI DI BASE
    //**********************************************************
    function chiamaServizioConvalidaCompleta(obj, url, $modal){
    	$.postJSON(url,obj)
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori,$alertErrori)){
    			return;
    		}
    		
    		impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
    	})
    	.always(function(){
    		$modal && $modal.modal('hide');
    	});
    }
    
    // *********************************************************
    //               COMPLETA
    //**********************************************************
    function impostaCompletamentoPerAttiDaRicerca(){
    	var $modaleCompletamentoMultiplo = $('#modaleCompletamentoMultiplo');
    	var $bottoneConferma = $('#buttonConfermaModaleCompletamento');
        
    	$modaleCompletamentoMultiplo.find('#spanModaleCompletamentoElementiSelezionati').html('tutti gli elementi ricercati');
    	$modaleCompletamentoMultiplo.modal('show');
    	$bottoneConferma.substituteHandler('click', chiamaServizioConvalidaCompleta.bind(undefined,{}, baseUrlCompletamento + 'Tutti.do', $modaleCompletamentoMultiplo));
    	
    }
    
    function impostaCompletamentoAttiSelezionati(){
    	var $modaleCompletamentoMultiplo = $('#modaleCompletamentoMultiplo');
    	var $bottoneConferma = $('#buttonConfermaModaleCompletamento');
    	var idsAllegati = creaListaUidsAllegatiSelezionati();
        if(idsAllegati.lenght == 0){
        	impostaDatiNegliAlert(['Nessun allegatoSelezionato'], $alertErrori);
        	return;
        }
    	$modaleCompletamentoMultiplo.find('#spanModaleCompletamentoElementiSelezionati').html(idsAllegati.length);
    	$modaleCompletamentoMultiplo.modal('show');
    	$bottoneConferma.substituteHandler('click', chiamaServizioConvalidaCompleta.bind(undefined,qualify({'uidsAllegatiAtto' : idsAllegati}), baseUrlCompletamento + 'Selezionati.do', $modaleCompletamentoMultiplo));
    	
    }
    
    // *********************************************************
    //               CONVALIDA
    //**********************************************************
    function impostaTipoConvalidaEConvalida(obj, url){
    	var $modaleConvalida = $('#modaleTipoConvalida');
    	var checkedTipoConvalida = $modaleConvalida.find('input:checked').val();
    	if(!obj){
    		return;
    	}
    	obj.convalidaManuale = checkedTipoConvalida;
    	chiamaServizioConvalidaCompleta(qualify(obj),url,$modaleConvalida);
    	
    }
    
    function impostaConvalidaPerAttiDaRicerca(){
    	var $modaleConvalida = $('#modaleTipoConvalida');
    	var $bottoneConferma = $('#pulsanteConfermaModaleTipoConvalida');
        
    	$('#labelModaleTipoConvalida').find('h4').html('Elementi da convalidare selezionati: tutti gli elementi ricercati');
    	$modaleConvalida.modal('show');
    	$bottoneConferma.substituteHandler('click', impostaTipoConvalidaEConvalida.bind(undefined,{}, baseUrlConvalida + 'Tutti.do'));
    	
    }
    
    function impostaConvalidaAttiSelezionati(){
    	var $modaleConvalida = $('#modaleTipoConvalida');
    	var $bottoneConferma = $('#pulsanteConfermaModaleTipoConvalida');
    	
    	var idsAllegati = creaListaUidsAllegatiSelezionati();
        if(idsAllegati.lenght == 0){
        	impostaDatiNegliAlert(['Nessun allegatoSelezionato'], $alertErrori);
        	return;
        }
        $('#labelModaleTipoConvalida').find('h4').html('Elementi da convalidare selezionati: ' + idsAllegati.length);
        $modaleConvalida.modal('show');
    	$bottoneConferma.substituteHandler('click', impostaTipoConvalidaEConvalida.bind(undefined,{'uidsAllegatiAtto' : idsAllegati}, baseUrlConvalida+ 'Selezionati.do'));
    	
    }
    
    // *********************************************************
    //                FUNZIONI  RICHIAMATE DA ESTERNO
    //**********************************************************
    
   /**
     * Popola la tabella.
     */
    function gestioneTabella() {
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaAllegatoAttoOperazioniMultipleAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
                $('#tabellaRisultatiRicercaAllegatoAtto_processing').parent('div')
                    .hide();
            },
            aoColumnDefs: [
            	{aTargets: [0], mData: function(source) {
            		var uidAllegato = source.uid;
                    var res = "<input type='checkbox' name='uidAllegato' data-uid-allegato='" + uidAllegato + "' value='" + uidAllegato + "'";
                    if(selectedDatas[uidAllegato] && selectedDatas[uidAllegato].isSelected){
                	   res += " checked ";
                    }
                    if(source.inibisciSelezione){
                    	res += " disabled ";
                    } 
                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalAllegato", oData);
                }},
                {aTargets: [1],	mData: defaultPerDataTable('stringaAttoAmministrativo')},
                {aTargets: [2], mData: defaultPerDataTable('stringaRitenute')},
                {aTargets: [3], mData: defaultPerDataTable('stringaSospensione')},
                {aTargets: [4], mData: defaultPerDataTable('stringaCausale')},
                {aTargets: [5], mData: defaultPerDataTable('stringaDataScadenza')},
                {aTargets: [6], mData: defaultPerDataTable('stringaDomRichDurc')},
                {aTargets: [7], mData: defaultPerDataTable('stringaDataFineValiditaDurc')},
                {aTargets: [8], mData: defaultPerDataTable('stringaOrdinativi')},
                {aTargets: [9], mData: defaultPerDataTable('stringaStatoOperativo')}
            ]
        };
        $('#tabellaRisultatiRicercaAllegatoAtto').dataTable(opts);
    }

    function gestioneCompletamentoMultiplo(e){
    	var pulsante;
    	
    	e && e.preventDefault;
    	
    	pulsante = $(e.target);
    	
    	if(pulsante.hasClass("selezionati")){
    		impostaCompletamentoAttiSelezionati();
    		return;
    	}
    	
    	impostaCompletamentoPerAttiDaRicerca();
    }
    
    function gestioneConvalidaMultiplo(e){
    	var pulsante;
    	
    	e && e.preventDefault;
    	
    	pulsante = $(e.target);
    	
    	if(pulsante.hasClass("selezionati")){
    		impostaConvalidaAttiSelezionati();
    		return;
    	}
    	
    	impostaConvalidaPerAttiDaRicerca();
    }
    
    /////////////////// SIAC-6688
    function controlloImporti(allegato, nTd){
    	
  	  var listaAllegati = []; //lista allegati
	  var uidsAllegatiAtto = []; //listaIDAllegati
      //var idx = 0;
      var i;
      var pulsante = $('#pulsanteControlloDisponibilitaCassa').overlay({usePosition: true});
      for(i in selectedDatas) {
          if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
        	  listaAllegati.push(selectedDatas[i].data);
          }
      }
      
      uidsAllegatiAtto = listaAllegati.filter(filterOutUndefined).map(mapToUid);

      
      pulsante.overlay('show');
      
      if(uidsAllegatiAtto.length === 0){
      	return;
      }
      
      // uidsAllegatiAtto parametro passato presente nel model
    	return $.postJSON('risultatiRicercaAllegatoAttoOperazioniMultiple_controlloImportiImpegniVincolati.do', qualify({'uidsAllegatiAtto' : uidsAllegatiAtto}))
         .then(controlloImportiCallback.bind(undefined,allegato))
         .always(pulsante.overlay.bind(pulsante, 'hide'));
    }
    
    /**
     * Callback di sospensione di tutti i dati
     * @param modal (jQuery) la modale
     * @param data  (any)    i dati del servizio
     */
    function controlloImportiCallback(allegato, data) {       
    	if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
            return $.Deferred().reject().promise();
        }
    	if(impostaDatiNegliAlert(data.messaggi, $('#MESSAGGI'))) {
            return $.Deferred().reject().promise();
        }
    	if(impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'))) {
            return $.Deferred().reject().promise();
        }
    }
    ///////////////////////////////////////
    
 
    return exports;
    
}(jQuery, this);
$(function() {
	RisultatiRicercaOperazioniMultiple.gestioneTabella();
	$('form').on('change', 'input[type="checkbox"]', RisultatiRicercaOperazioniMultiple.gestioneCheckSuAllegato);	
	$('#pulsanteControllaImporti').substituteHandler('click',RisultatiRicercaOperazioniMultiple.controlloImporti );	
	$('#pulsanteCompletaTutti, #pulsanteCompletaSelezionati').substituteHandler('click', RisultatiRicercaOperazioniMultiple.gestioneCompletamentoMultiplo);
	$('#pulsanteConvalidaTutti, #pulsanteConvalidaSelezionati').substituteHandler('click', RisultatiRicercaOperazioniMultiple.gestioneConvalidaMultiplo);
});