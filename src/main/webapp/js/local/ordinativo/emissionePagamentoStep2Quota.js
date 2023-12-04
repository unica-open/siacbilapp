/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var $table = $("#tabellaQuote").overlay({rebind: true, loader: true});
    var disabled = !!$("#DISABLED").length;
    var selectedDatas = {};
    var isOverlayIn = false;
	var enteGestioneContiVincolati = $('#enteGestioneContiVincolati') && $('#enteGestioneContiVincolati').length > 0;

	//SIAC-5933
	function impostaDefaultConto(){
		 $('#contoTesoreria option').filter(function() { 
	        	return /^0000100/.test($(this).html());
	     }).attr('selected', 'selected');
	}
    
    
    /**
     * gestisce l'eventuale disabilitazione della select
     * */
    function gestisciIdsElaborati(){
    	//SIAC-8200 $table e' gia' stata dichiarata
//    	var $table = $('#tabellaQuote');
    	$table.overlay('show');
    	return $.postJSON('emissioneOrdinativiPagamentoQuota_ottieniIdsElementiElaborati.do', {}).then(function(data){
    		var uidsElaborati = data.uidsElaborati;
    		uidsElaborati.forEach(function(value){
    			$('input[type="checkbox"][name="uidQuota"][data-uid-quota="' + value + '"]').attr('disabled', 'disabled');    			
    		});
    	}).always($table.overlay.bind($table,'hide'));
    }
    
    /**
     * Caricamento dei dati nella tabella.
     */
    function loadDataIntoTable() {
        var opts = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaQuoteDaEmettereSpesaAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
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
                if(!isOverlayIn){
                    $table.overlay('show');
                    isOverlayIn = true;
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (opts) {
                // Nascondo il div del processing
                defaultDrawCallback(opts);
                gestisciIdsElaborati();
                // Attivo i popover
                $table.find("a[rel='popover']").popover();
                $table.overlay('hide');
                isOverlayIn = false;
                ricalcolaTotali();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' name='uidQuota' data-uid-quota='" + source.uid + "'value='" + source.uid + "'";
                    if(disabled || source.inibisciSelezione) {
                        res += " disabled";
                    }
                    if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
                        res += ' checked';
                    }
                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalQuota", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('domStringInfoSelezione')},
                {aTargets: [2], mData: defaultPerDataTable('domStringAttoAmministrativo')},
                	
                {aTargets: [3], mData: defaultPerDataTable('domStringElenco')},
                {aTargets: [4], mData: defaultPerDataTable('domStringSoggetto')},
                {aTargets: [5], mData: defaultPerDataTable('domStringModalitaPagamentoSoggetto')},
                {aTargets: [6], mData: defaultPerDataTable('domStringDocumento'), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [7], mData: defaultPerDataTable('domStringProvvisorio')},
                {aTargets: [8], mData: defaultPerDataTable('domStringLiquidazione')},
                {aTargets: [9], mData: defaultPerDataTable('domStringCapitolo')},
                {aTargets: [10], mData: defaultPerDataTable('domStringMovimento')},
                {aTargets: [11], mData: defaultPerDataTable('domStringDistinta')},
                {aTargets: [12], mData: defaultPerDataTable('domStringContoTesoreria')},
                {aTargets: [13], mData: function(source) {
                    return source.dataScadenza ? formatDate(source.dataScadenza) : "";
                }},
                {aTargets: [14], mData: function(source) {
                    // Rigetto i valori 'undefined' e 'null', accetto 0
                    return source.domStringImporto != undefined ? source.domStringImporto.formatMoney() : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        $table.dataTable(opts);
    }

    /**
     * Selezione di tutti i checkbox
     */
    function selectAll() {
        var $this = $(this);
        var table = $this.closest("table");
        var checkboxes = table.find("tbody").find("input[type='checkbox']");
        var toCheck = $this.prop("checked");

        checkboxes.prop("checked", toCheck).each(function(idx, el) {
            selectedDatas[+el.value] = {isSelected: toCheck, data: $(el).data('originalQuota')};
        });
        ricalcolaTotali();
    }

    /**
     * Salvataggio della singola riga nell'elenco dei selezionati
     */
    function saveRiga() {
        var $this = $(this);
        var isChecked;
        if($this.attr('id') === 'selezionaTuttiQuote') {
            return;
        }
        isChecked = $this.prop('checked');
        selectedDatas[+$this.val()] = {isSelected: isChecked, data: $this.data('originalQuota')};
        ricalcolaTotali();
    }
	//SIAC-8784
	function gestisciContiVincolati(checkedNum, idsSubdocumenti){
		var selectContoTesoreria = $('#contoTesoreria');
		if(!enteGestioneContiVincolati){
			$("#pulsanteEmissione")[checkedNum ? "show" : "hide"]();
			return;	
		}
		
		 if(idsSubdocumenti.length === 0){
			// SIAC-5933
	        impostaDefaultConto();
        	$("#pulsanteEmissione")[checkedNum ? "show" : "hide"]();
			return;
        }
		selectContoTesoreria.overlay('show');
		$.postJSON('emissioneOrdinativiPagamentoQuota_ottieniUidContoDaSelezionare.do', qualify({'uidsSubdocumentiSelezionati' : idsSubdocumenti})).then(function(data){
			var uidConto;
			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
				selectContoTesoreria.overlay('hide');
				return;
			}
			uidConto = data.uidContoDaSelezionare;
			if(uidConto){
				selectContoTesoreria.val(uidConto).change();	
			}
			
			$("#pulsanteEmissione")[checkedNum ? "show" : "hide"]();
			selectContoTesoreria.overlay('hide');
		});
		//andrebbe fatto per un solo subdoc, vediamo
		//$('#pulsanteControlloDisponibilitaConto')[checkedNum /*&& checkedNume===1 */ && $('#contoTesoreria').find('option:selected').data('vincolato') ? "show" : "hide"]();
	}
    /**
     * Ricalcolo dei totali.
     */
    function ricalcolaTotali() {
        var totale = 0;
        var checkedNum = 0;
        var i;
        var data;
		 var idsSubdocumenti = []; 
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                checkedNum++;
                data = selectedDatas[i].data;
				idsSubdocumenti.push(i);
                if(data) {
                    totale += data.domStringImporto;
                }
            }
        }
        $("#totaleQuoteSelezionate").html(totale.formatMoney());
		gestisciContiVincolati(checkedNum, idsSubdocumenti);
		$('#pulsanteControlloDisponibilitaCassa')[checkedNum ? "show" : "hide"]();
       
        $("#confermaEmissioneNumeroSpan").html(checkedNum + (checkedNum === 1 ? " ordinativo" : " ordinativi"));
        $("#confermaEmissioneImportoSpan").html(totale.formatMoney());
    }

    /**
     * Conferma dell'emissione.
     */
    function confermaEmissione() {
        var $form = $("form");
        var str = '';
        var idx = 0;
        var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                str += '<input type="hidden" name="listSubdocumenti[' + idx + '].uid" value="' + i + '" />';
                idx++;
            }
        }
        $form.append(str);
        $form.submit();
    }
    
    /**
     * Conferma dell'emissione.
     */
    function controlloDisponibilitaCassa() {
        var idsSubdocumenti = [];
        var idx = 0;
        var i;
        var pulsante = $('#pulsanteControlloDisponibilitaCassa').overlay({usePosition: true});
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	idsSubdocumenti.push(i);
                idx++;
            }
        }
        pulsante.overlay('show');
        if(idsSubdocumenti.length === 0){
        	return;
        }
        return $.postJSON('emissioneOrdinativiPagamentoQuota_controllaDisponibilitaDiCassaCapitoli.do', qualify({'idsSubdocumentiSpesa' : idsSubdocumenti})).then(function(data){
        	if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
        		return;
        	}
        	impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
        		
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }

	 /**
     * Conferma dell'emissione.
     */
    function controlloDisponibilitaSottoConto() {
        var idsSubdocumenti = [];
        var idx = 0;
        var i;
        var pulsante = $('#pulsanteControlloDisponibilitaConto').overlay({usePosition: true});
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	idsSubdocumenti.push(i);
                idx++;
            }
        }
        pulsante.overlay('show');
        if(idsSubdocumenti.length === 0){
        	return;
        }
        return $.postJSON('emissioneOrdinativiPagamentoQuota_controllaDisponibilitaSottoContoVincolato.do', qualify({'idsSubdocumentiSpesa' : idsSubdocumenti, 'contoTesoreria.uid' : $('#contoTesoreria').val()})).then(function(data){
        	if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
        		return;
        	}
			//ALTERNATIVA
			//impostaDatiNegliAlert(data.messaggi, $('#INFORMAZIONI'));
			if(data.messaggi){
				var messaggio = "";
				jQuery.each(data.messaggi, function(key, value) {
		            // Se ho un oggetto con codice e descrizione, lo spezzo; in caso contrario utilizzo direttamente il valore in entrata
		            if (this.codice !== undefined) {
		               messaggio = messaggio + this.codice + " - " + this.descrizione + "</br>"
		            } 
		        });
				bootboxAlert(messaggio, "Disponibilita sottoconto", "dialogWarn");	
			}
        	
        		
        }).always(pulsante.overlay.bind(pulsante, 'hide'));
    }


    /**
     * Gestione del flag 'daTrasmettere'
     */
    function handleFlagDaTrasmettere() {
        if (this.checked) {
            $("#HIDDEN_flagDaTrasmettere").remove();
        } else {
            $(this).after('<input type="hidden" name="flagDaTrasmettere" id="HIDDEN_flagDaTrasmettere" value="false" />');
        }
    }

    $(function() {
        var annoDatepicker = parseInt($("#HIDDEN_anno_datepicker").val(), 10);
        var now = new Date();
        // Selezione di tutti gli elenchi
        $("#selezionaTuttiQuote").substituteHandler("change", selectAll);
        $table.on("change", "input[name='uidQuota'][type='checkbox']", saveRiga);
        $("#confermaEmissioneModalConferma").substituteHandler("click", confermaEmissione);

        // Caricamento dei dati
        loadDataIntoTable();
        
        $('#dataScadenza').datepicker('setStartDate',new Date(annoDatepicker, now.getMonth(), now.getDate()));
        $('#dataScadenza').datepicker('setEndDate',new Date(annoDatepicker, 11, 31));
        //SIAC-5252
        $('#pulsanteControlloDisponibilitaCassa').substituteHandler('click', controlloDisponibilitaCassa);
        
        //SIAC-6029, secondo punto
        if(!disabled){
        	// SIAC-5933
	        impostaDefaultConto();
	        $("#flagDaTrasmettere").substituteHandler("click", handleFlagDaTrasmettere);
    	}

		//SIAC-8017-CMTO
		$('#pulsanteControlloDisponibilitaConto').substituteHandler('click', controlloDisponibilitaSottoConto);
		$('#contoTesoreria').substituteHandler('change', function(){
			var contoTesoreriaVincolato = $('#contoTesoreria').find('option:selected').data('vincolato');
			var i;
			if(!contoTesoreriaVincolato){
				$('#pulsanteControlloDisponibilitaConto').hide();
				return;
			}
			for(i in selectedDatas) {
           		if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                	$('#pulsanteControlloDisponibilitaConto').show();
					return;
            	}
        	}
			$('#pulsanteControlloDisponibilitaConto').hide();
		});
    
    });
}(jQuery);