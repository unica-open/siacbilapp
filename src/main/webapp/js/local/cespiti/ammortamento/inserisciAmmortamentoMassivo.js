/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var AmmortamentoMassivo = function($,global) {
	"use strict";
	var exports = {};
	var selectedDatas = {};
	var urlBaseEffettuaAmmortamento = 'inserisciAmmortamentoMassivo_effettuaAmmortamento';
	var $alertErrori = $('#ERRORI');
	
	exports.gestisciChangeTipoBene = gestisciChangeTipoBene;
	exports.cercaSchedeCespiteDaElaborare = cercaSchedeCespiteDaElaborare;
	exports.effettuaAmmortamentoSelezionati = effettuaAmmortamentoSelezionati;
	exports.saveRiga = saveRiga;
	exports.selezionaTutti = selezionaTutti;
	exports.effettuaAmmortamentoTutti = effettuaAmmortamentoTutti;
	

	 function gestisciChangeTipoBene(){
	        var inputContoPatrimoniale = $('#contoPatrimonialeCespiteTipoBene');
	        var contoPatrimoniale = $('#cespiteTipoBene').find('option:selected').data('conto-patrimoniale') || "";
	        inputContoPatrimoniale.val(contoPatrimoniale);
	    }
	 
	 function impostaTabellaCespiti(){
		 var $table=$('#tabellaRisultatiRicercaCespite').overlay('show');
		 var opts = {
		            bServerSide: true,
		            sAjaxSource: 'risultatiRicercaCespiteAjax.do',
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
		                $table.find("a[rel='popover']").popover();
		                $table.overlay('hide');
		            },
		            aoColumnDefs: [
		                {aTargets: [0], mData: function(source) {
		                    var res = "<input type='checkbox' name='uidCespite' data-uid-cespite='" + source.uid + "' value='" + source.uid + "'";
//		                    if(disabled) {
//		                        res += " disabled";
//		                    }
		                    if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
		                        res += ' checked';
		                    }
		                    res += "/>";
		                    return res;
		                }, fnCreatedCell: function(nTd, sData, oData) {
		                    $("input", nTd).data("originalCespite", oData);
		                }},
		                {aTargets: [1], mData: defaultPerDataTable('codice')},
		                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
		                {aTargets: [3], mData: defaultPerDataTable('tipoBene')},
		                {aTargets: [4], mData: defaultPerDataTable('inventario')},
		                {aTargets: [5], mData: defaultPerDataTable('dataAccessoInventario')},
		                {aTargets: [6], mData: defaultPerDataTable('valoreAttuale')},
		            ]
		        };
		        $table.dataTable(opts);
	 }
	 
	 
	 function cercaSchedeCespiteDaElaborare(){
		 var $fieldsetRicerca = $('#campiRicercaCespite').overlay('show');
		 var $divRisultati = $('#risultatiRicercaCespitiDaAmmortare').slideUp();
		 return $.postJSON('inserisciAmmortamentoMassivo_ricercaCespitiDaAmmortare.do', qualify($fieldsetRicerca.serializeObject()))
		 .then(function (data){
			 if(impostaDatiNegliAlert(data.errori, $alertErrori)){
				 return;
			 }
			 $divRisultati.slideDown();
			 impostaTabellaCespiti();
		 })
		 .always($fieldsetRicerca.overlay.bind($fieldsetRicerca, 'hide'));
	 }
	 
	    /**
	     * Selezione di tutti i checkbox
	     */
	    function selezionaTutti() {
	        var $this = $(this);
	        var table = $this.closest("table");
	        var checkboxes = table.find("tbody").find("input[type='checkbox']");
	        var toCheck = $this.prop("checked");

	        checkboxes.prop("checked", toCheck).each(function(idx, el) {
	            selectedDatas[+el.value] = {isSelected: toCheck, data: $(el).data('originalCespite')};
	        });
	    }

	    /**
	     * Salvataggio della singola riga nell'elenco dei selezionati
	     */
	    function saveRiga() {
	        var $this = $(this);
	        var isChecked;
	        if($this.attr('id') === 'selezionaTuttiCespiti') {
	            return;
	        }
	        isChecked = $this.prop('checked');
	        selectedDatas[+$this.val()] = {isSelected: isChecked, data: $this.data('originalCespite')};
	    }
	    
	    /**
	     * Conferma dell'emissione.
	     */
	    function effettuaAmmortamentoSelezionati() {
	    	var $form = $('form').prop('action', urlBaseEffettuaAmmortamento + 'Selezionati.do');
	        var str = '';
	        var idx = 0;
	        var i;
	        for(i in selectedDatas) {
	            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
	                str += '<input type="hidden" name="listaIdCespiti[' + idx + ']" value="' + i + '" />';
	                idx++;
	            }
	        }
	        $form.append(str);
	        $form.submit();
	    }
	    
	    function effettuaAmmortamentoTutti() {
	    	var $form = $('form').prop('action', urlBaseEffettuaAmmortamento + 'Tutti.do');
	    	 $form.submit();
	    }
	
	return exports;

	
}(jQuery, this);

$(function() {
	ContoBase.inizializza('', '', '#codiceContoPatrimoniale', '#descrizioneContoPatrimoniale', '#pulsanteCompilazioneGuidataContoPatrimoniale');
	$('#cespiteTipoBene').substituteHandler('change', AmmortamentoMassivo.gestisciChangeTipoBene).change();
	$('#pulsanteRicercaCespiti').substituteHandler('click', AmmortamentoMassivo.cercaSchedeCespiteDaElaborare);
	$('#tabellaRisultatiRicercaCespite').on("change", "input[name='uidCespite'][type='checkbox']", AmmortamentoMassivo.saveRiga);
	$('#selezionaTuttiCespiti').substituteHandler("change", AmmortamentoMassivo.selezionaTutti);
	$('#calcolaAmmortamentoSuSelezionati').substituteHandler('click', AmmortamentoMassivo.effettuaAmmortamentoSelezionati);
	$('#calcolaAmmortamentoSuTutti').substituteHandler('click', AmmortamentoMassivo.effettuaAmmortamentoTutti);
});