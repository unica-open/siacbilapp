/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var tableId = "#tabellaElenchi";
    var tbody = $("tbody", tableId);
    var disabled = !!$("#DISABLED").length;
    
    /***
     *  gestione degli id precedentemente elaborati
     * */
    function gestisciIdsElaborati(){
    	var $table = $('#tabellaElenchi');
    	$table.overlay('show');
    	return $.postJSON('emissioneOrdinativiPagamentoElenco_ottieniIdsElementiElaborati.do', {}).then(function(data){
    		var uidsElaborati = data.uidsElenchiElaborati;
    		if(!uidsElaborati){
    			return;
    		}
    		uidsElaborati.forEach(function(value){
    			$('input[type="checkbox"][name="uidElenco"][data-uid-elenco="' + value + '"]').attr('disabled', 'disabled');    			
    		});
    	}).always($table.overlay.bind($table,'hide'));
    }
    
    /**
     * Caricamento dei dati nella tabella.
     */
    function loadDataIntoTable() {
        var opts = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaElencoDocumentiAllegatoDaEmettere.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 50,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            sScrollY: "300px",
            bScrollCollapse: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti elenchi corrispondenti ai parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun elenco disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
            	
            	gestisciIdsElaborati().then(function(tableId){
            		$(tableId + "_processing").parent("div")
                    .hide();
            	});
                // Nascondo il div del processing
            	// Attivo i popover
                $(tableId).find("a[rel='popover']")
                    .popover();
                // Calcolo subito i totali
                $("#selezionaTuttiElenchi").change();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' name='uidElenco' data-uid-elenco='" + source.uid + "' value='" + source.uid + "'";
                    if(disabled || source.inibisciSelezioneSpesa) {
                        res += "disabled";
                    }
                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalElenco", oData.elencoDocumentiAllegato);
                }},
                {aTargets: [1], mData: defaultPerDataTable("anno")},
                {aTargets: [2], mData: defaultPerDataTable("numero")},
                {aTargets: [3], mData: defaultPerDataTable("stato")},
                {aTargets: [4], mData: defaultPerDataTable("dataTrasmissione")},
                {aTargets: [5], mData: defaultPerDataTable("hasImpegnoConConfermaDurcString")},
                {aTargets: [6], mData: defaultPerDataTable("dataFineValiditaDurc")},
                {aTargets: [7], mData: defaultPerDataTable("totaleQuoteSpese"), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: defaultPerDataTable("totaleDaPagareString"), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
    /*
                {aTargets: [1], mData: function(source) {
                    return source.anno || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.numero || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return source.statoOperativoElencoDocumenti && source.statoOperativoElencoDocumenti.codice || "";
                }},
                {aTargets: [4], mData: function(source) {
                    var res = "";
                    if(source.dataTrasmissione) {
                        res = "<a data-original-title='Fonte' data-trigger='hover' rel='popover' data-content='"
                            + escapeHtml(source.sysEsterno) + " - " + source.annoSysEsterno + "/" + source.numeroSysEsterno + "'>"
                            + formatDate(source.dataTrasmissione)
                            + "</a>";
                    }
                    return res;
                }},
                {aTargets: [5], mData: function(source) {
                    return source.hasImpegnoConfermaDurc? "S&igrave;" : "No";
                }},
                {aTargets: [6], mData: function(source) {
                	var stringDurcDaRichiedere = "DURC DA RICHIEDERE";
                    return !source.hasImpegnoConfermaDurc ?
                    		"" :
                    		source.dataFineValiditaDurc?  
                    				formatDate(source.dataFineValiditaDurc) 
                    				: stringDurcDaRichiedere;
                }},
                {aTargets: [7], mData: function(source) {
                    // Rigetto i valori 'undefined' e 'null', accetto 0
                    return source.totaleQuoteSpese != undefined ? source.totaleQuoteSpese.formatMoney() : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: function(source) {
                    return source.totaleDaPagare != undefined ? source.totaleDaPagare.formatMoney() : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}*/
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Selezione di tutti i checkbox
     */
    function selectAll() {
        var checked = $(this).prop("checked");
        tbody.find("input[name='uidElenco'][type='checkbox']")
            .not("[disabled]")
                .prop("checked", checked)
                .first()
                    .change();
    }

    /**
     * Ricalcolo dei totali.
     */
    function ricalcolaTotali() {
        var totaleQuoteSpese = 0;
        var totaleDaPagare = 0;
        var differenza = 0;
        var chks = tbody.find("input[name='uidElenco'][type='checkbox']").filter(":checked");
        chks.each(function() {
            var self = $(this);
            var elenco = self.data("originalElenco");
            if(!elenco) {
                // Non ho i data. Errore?
                return;
            }
            totaleQuoteSpese += elenco.totaleQuoteSpese;
            totaleDaPagare += elenco.totaleDaPagare;
        });
        differenza = totaleQuoteSpese - totaleDaPagare;
        $("#totaleElenchiSelezionati").html(totaleQuoteSpese.formatMoney());
        $("#totaleSpeseCollegateSelezionati").html(totaleDaPagare.formatMoney());
        $("#differenzaSelezionati").html(differenza.formatMoney());

        // Mostro o nascondo il pulsante di emissione
        $("#pulsanteEmissione")[chks.length ? "show" : "hide"]();
        $('#pulsanteControlloDisponibilitaCassa')[chks.length ? "show" : "hide"]();
        $("#confermaEmissioneNumeroSpan").html(chks.length + (chks.length === 1 ? " elemento" : " elementi"));
        $("#confermaEmissioneImportoSpan").html(totaleDaPagare.formatMoney());
    }

    /**
     * Conferma dell'emissione.
     */
    function confermaEmissione() {
        var $form = $("form");
        var str = "";
        tbody.find("input[name='uidElenco'][type='checkbox']").filter(":checked").each(function(idx) {
            var elenco = $(this).data("originalElenco");
            if(!elenco) {
                // Non ho i data. Errore?
                return;
            }
            str += "<input type='hidden' name='listElenchi[" + idx + "].uid' value='" + elenco.uid + "'/>";
            str += "<input type='hidden' name='listElenchi[" + idx + "].anno' value='" + elenco.anno + "'/>";
            str += "<input type='hidden' name='listElenchi[" + idx + "].numero' value='" + elenco.numero + "'/>";
        });
        $form.append(str);
        $form.submit();
    }
    
    /**
     * Conferma dell'emissione.
     */
    function controlloDisponibilitaCassa() {
        var idsElenchi = [];
        var pulsante = $('#pulsanteControlloDisponibilitaCassa').overlay({usePosition: true});
        
        tbody.find("input[name='uidElenco'][type='checkbox']")
        .filter(":checked")
        .each(function(idx) {
            var elenco = $(this).data("originalElenco");
            if(!elenco) {
                // Non ho i data. Errore?
                return;
            }
            idsElenchi.push(elenco.uid);
        });
        if(idsElenchi.length === 0){
        	return;
        }
        pulsante.overlay('show');
        return $.postJSON('emissioneOrdinativiPagamentoQuota_controllaDisponibilitaDiCassaCapitoli.do', qualify({'idsElenchi' : idsElenchi}))
        .then(function(data){
        	if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
        		return;
        	}
        	impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
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
        $("#selezionaTuttiElenchi").substituteHandler("change", selectAll);
        $(document).on("change", tableId + " input[name='uidElenco'][type='checkbox']", ricalcolaTotali);
        $("#confermaEmissioneModalConferma").substituteHandler("click", confermaEmissione);

        // Caricamento dei dati
        loadDataIntoTable();
        
        $('#dataScadenza').datepicker('setStartDate',new Date(annoDatepicker, now.getMonth(), now.getDate()));
        $('#dataScadenza').datepicker('setEndDate',new Date(annoDatepicker, 11, 31));
        
        //SIAC-5252
        $('#pulsanteControlloDisponibilitaCassa').substituteHandler('click', controlloDisponibilitaCassa);
        
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