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
    
    /**
     * gestisce l'eventuale disabilitazione della select
     * */
    function gestisciIdsElaborati(){
    	var $table = $('#tabellaQuote');
    	$table.overlay('show');
    	return $.postJSON('emissioneOrdinativiIncassoQuota_ottieniIdsElementiElaborati.do', {}).then(function(data){
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
            sAjaxSource: "risultatiRicercaQuoteDaEmettereEntrataAjax.do",
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
            	gestisciIdsElaborati();
                // Nascondo il div del processing
                defaultDrawCallback(opts);
                // Attivo i popover
                $table.find("a[rel='popover']").popover();
                $table.overlay('hide');
                isOverlayIn = false;
                ricalcolaTotali();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' name='uidQuota' data-uid-quota='" + source.uid + "' value='" + source.uid + "'";
                    if(disabled) {
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
                {aTargets: [1], mData: function(source) {
                    return source.domStringAttoAmministrativo || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.domStringElenco || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return source.domStringSoggetto || "";
                }},
                {aTargets: [4], mData: defaultPerDataTable('domStringDocumento')},/*, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},*/
                {aTargets: [5], mData: defaultPerDataTable('domStringProvvisorio'), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Left");
                }},
                {aTargets: [6], mData: defaultPerDataTable('domStringCapitolo'), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [7], mData: defaultPerDataTable('domStringMovimento'), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: function(source) {
                    return source.dataScadenza ? formatDate(source.dataScadenza) : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [9], mData: function(source) {
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

    /**
     * Ricalcolo dei totali.
     */
    function ricalcolaTotali() {
        var totale = 0;
        var checkedNum = 0;
        var i;
        var data;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                checkedNum++;
                data = selectedDatas[i].data;
                if(data) {
                    totale += data.domStringImporto;
                }
            }
        }
        $("#totaleQuoteSelezionate").html(totale.formatMoney());
        $("#pulsanteEmissione")[checkedNum ? "show" : "hide"]();
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
     * Gestione del flag 'daTrasmettere'
     */
    function handleFlagDaTrasmettere() {
        if (this.checked) {
            $("#HIDDEN_flagDaTrasmettere").remove();
        } else {
            $(this).after('<input type="hidden" name="flagDaTrasmettere" id="HIDDEN_flagDaTrasmettere" value="false" />');
        }
    }

    //PER PAGINARE LA TABELLA SUL DOM
    function paginaTable() {
    	
		    $('#tabellaRiepilogoProvdicassa').dataTable({
			bPaginate: true,
			bLengthChange: false,
			iDisplayLength: 3,
			bSort: false,
			bInfo: true,
			bAutoWidth: true,
			bFilter: false,
			bDestroy: true,
			bProcessing: false,
			oLanguage: {
				sInfo: '_START_ - _END_ di _MAX_ risultati',
				sInfoEmpty: '0 risultati',
				sProcessing: 'Attendere prego...',
				sZeroRecords: 'Non sono presenti provvisori di cassa corrispondenti ai parametri inseriti',
				oPaginate: {
					sFirst: 'inizio',
					sLast: 'fine',
					sNext: 'succ.',
					sPrevious: 'prec.',
					sEmptyTable: 'Nessun dato disponibile'
				}
			},
			fnDrawCallback: function () {
				// Attivo i popover
				 $('#tabellaRiepilogoProvdicassa').find("a[rel='popover']").popover().eventPreventDefault("click");
			}
		})
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
        paginaTable();
        
        $('#dataScadenza').datepicker('setStartDate',new Date(annoDatepicker, now.getMonth(), now.getDate()));
        $('#dataScadenza').datepicker('setEndDate',new Date(annoDatepicker, 11, 31));
        
        //SIAC-6029
        if(!disabled){
	        // SIAC-5933
	        $('#contoTesoreria option').filter(function() {
	            return /^0000100/.test($(this).html());
	        }).attr('selected', 'selected');
	        $("#flagDaTrasmettere").substituteHandler("click", handleFlagDaTrasmettere);
        }
        
    });
    
    

     

    
    
}(jQuery);