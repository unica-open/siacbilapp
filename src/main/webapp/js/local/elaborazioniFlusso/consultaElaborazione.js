/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function () {
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
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
            oPaginate : {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }};
    //var collapseOrdine = $("#divOrdine_modaleDettaglioOrdini");
    //var tabellaOrdini = $("#tabella_modaleDettaglioOrdini");
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
//    var dataTable;
    
    function openErrorModal(td, e, uid, ricId) {
    	console.log("ricId="+ricId);
    	 var tr = $(td).closest('tr').overlay('show');
         e.preventDefault();

         $.postJSON("risultatiRicercaErroriRiconciliazioni.do", {uidDaConsultare : uid, riconciliazioneId:ricId})
         .then(function(data) {
            
        	 console.log(data);
        	 if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 return;
             }
        	
        	 if(data.tipoFattura){
        		 popolaTabellaErroriFattura();
        		 $("#erroriRiconciliazioniFattureModal").modal("show");
        	 }else{
        		 popolaTabellaErrori(data.tipoFattura); 
                 $("#erroriRiconciliazioniModal").modal("show"); 
        	 }
         })
         .always(tr.overlay.bind(tr, 'hide'));
    }
    
    //SIAC-8046 CM 19-30/03/-12/04/2021 Task 2.1/2.2 Inizio
    function openAggiornaAccertamentoModal(td, e, infoAcc, uid, ricId ) {
    	 $("#infoAccertamentoModale").val(infoAcc);
    	 $("#aggiornaAccertamentoModal").modal("show");
    	 var alertErroriModale = $("#ERRORI_ACCERTAMENTO_MODALE");
    	 var alertInformazioni = $("#INFORMAZIONI");
    	 
    	 //prima off e poi on
         $("#pulsanteAggiornaModaleAccertamento").substituteHandler("click", function(event) {
        	 
	         var anno = $("#annoaccertamentonuovo").val();
	         var numero = $("#numeroaccertamentonuovo").val();
	         var arrayErrori = [];
	         var arrayInformazioni = [];

	         if(isNaN(parseInt(anno, 10))) {
	             arrayErrori.push("COR_ERR_0002 - Dato obbligatorio omesso: Anno");
	         }
	         if(isNaN(parseInt(numero, 10))) {
	             arrayErrori.push("COR_ERR_0002 - Dato obbligatorio omesso: Numero");
	         }

	         if(impostaDatiNegliAlert(arrayErrori, alertErroriModale)) {
	             return;
	         }

	         $.postJSON(
	             "aggiornaAccertamentoModale.do", {annoAcc : anno, numAcc : numero, riconciliazioneId:ricId},
	             function(data){
	                 if(data.resRicercaAccertamento) {
	                	 if(data.resAggiornaAccertamento){
		                	 //chiama il servizio di update e poi esci e aggiorna la pagina
		                	 $("#aggiornaAccertamentoModal").modal('hide');
		                	 //aggiunto per ricaricare i dati in tabella
		                	 visualizzaTabellaRiconciliazioni();
		                	 arrayInformazioni.push(data.descrizioneResAggiornaAccertamento);
		                	 impostaDatiNegliAlert(arrayInformazioni, alertInformazioni);
		                     return;
	                	 }else{
		                	 arrayErrori.push(data.descrizioneResAggiornaAccertamento);
		        	         if(impostaDatiNegliAlert(arrayErrori, alertErroriModale)) {
		        	             return;
		        	         }
	                	 }
	                 }else{
	                	 arrayErrori.push("L'accertamento non esiste o lo stato non è definitivo");
	        	         if(impostaDatiNegliAlert(arrayErrori, alertErroriModale)) {
	        	             return;
	        	         }
	                 }
	             }
	         ).always(function() {
	         });	            	         	 
         });
    }
    
    function overlayForWait(){
        document.getElementById("overlay").style.display = "block";
    }
    //SIAC-8046 CM 19-30/03-12/04/2021 Task 2.1/2.2 Fine
    /**
     * Caricamento via Ajax della tabella errori con Fattura.
     * 
     */
    function popolaTabellaErroriFattura() {
        var options = {
            bServerSide: true,
            // SIAC-6769
            bDestroy: true,
            sAjaxSource: "risultatiRiconciliazioneDocAjax.do",
            sServerMethod: "POST",
            iDisplayLength: 10,
            oLanguage: {
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            fnServerData : function (sSource, aoData, fnCallback, oSettings) {
            	oSettings.jqXHR = $.ajax({
            		dataType: 'json',
            		type: 'POST',
            		url: sSource,
            		data: aoData,
            		success: function(data) {
            			return fnCallback.apply(this, arguments);
            		}
            	});
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "descrizione", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                ,
                {aTargets: [1], mData: "tipoOperazioneDocumento", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                ,
                {  aTargets: [2],  mData: "codiceFiscale", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                ,
                { aTargets: [3], mData: "ragioneSociale", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                { aTargets: [4], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                { aTargets: [5], mData: "iuv", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition_errori_fatture").val(), 10);
        var opts;
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }
        opts = $.extend(true, {}, baseOpts, options);

        $("#risultatiRiconciliazioneFattureErrori").dataTable(opts);
    }
    
    
    
    
    /**
     * Caricamento via Ajax della tabella errori sintetica.
     * 
     */
    function popolaTabellaErrori() {
        var options = {
            bServerSide: true,
            // SIAC-6769
            bDestroy: true,
            sAjaxSource: "risultatiRiconciliazioneDocAjax.do",
            sServerMethod: "POST",
            iDisplayLength: 10,
            oLanguage: {
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            fnServerData : function (sSource, aoData, fnCallback, oSettings) {
            	oSettings.jqXHR = $.ajax({
            		dataType: 'json',
            		type: 'POST',
            		url: sSource,
            		data: aoData,
            		success: function(data) {
            			return fnCallback.apply(this, arguments);
            		}
            	});
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "descrizione", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                ,
                {aTargets: [1], mData: "tipoOperazioneDocumento", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition_errori").val(), 10);
        var opts;
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }
        opts = $.extend(true, {}, baseOpts, options);

        $("#risultatiRiconciliazioneErrori").dataTable(opts);
    }
    
    
    
    
    
    


    /**
     * Caricamento via Ajax della tabella riconciliazione.
     */
    function visualizzaTabellaRiconciliazioni() {
        var options = {
            bServerSide: true,
            bDestroy: true,
            sAjaxSource: "risultatiRicercaRiconciliazioniAjax.do",
            sServerMethod: "POST",
            iDisplayLength: 10,
            oLanguage: {
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            fnServerData : function (sSource, aoData, fnCallback, oSettings) {
            	oSettings.jqXHR = $.ajax({
            		dataType: 'json',
            		type: 'POST',
            		url: sSource,
            		data: aoData,
            		success: function(data) {
            			return fnCallback.apply(this, arguments);
            		}
            	});
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "codiceVoce", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }}
                ,
                {aTargets: [1], mData: "descrizioneVoce", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [2], mData: "codiceSottovoce", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [3], mData: "infoAccertamento", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                //SIAC-8046 CM 16/03/2021 Task 2.0 e 18/03/2021 Task 2.1 Inizio
                {aTargets: [4], mData: "infoAccertamentoNuovo", fnCreatedCell: function(nTd, sData, oData) {
                	$(nTd).addClass("text-center")
                	.find("a.aggiornaAccertamento")
                    .off("click")
                    .on("click", function(e) {
                    	openAggiornaAccertamentoModal(nTd, e, oData.infoAccertamento, oData.riconciliazioneDoc.elaborazione.uid, oData.riconciliazioneDoc.riconciliazione.uid); 
                    })
                    .end()
                    $('.dropdown-toggle', nTd).dropdown();
                }},
                //SIAC-8046 CM 16-18/03/2021 Fine
                {aTargets: [5], mData: defaultPerDataTable('riconciliazioneDoc.importoRigaRiconciliazione', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [6], mData: "esito", fnCreatedCell: function(nTd, sData, oData) {
                    //$(nTd).addClass("text-center");
                    
                    $(nTd).addClass("text-center")
                    .find("a.erroriRiconciliazione")
                        .off("click")
                        .on("click", function(e) {
                        	openErrorModal(nTd, e, oData.riconciliazioneDoc.elaborazione.uid, oData.riconciliazioneDoc.riconciliazione.uid);
                        })
                        .end()
                $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        var opts;
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }
        opts = $.extend(true, {}, baseOpts, options);

        $("#risultatiRicercaRiconciliazioni").dataTable(opts);
    }
       

    $(function() {
        // Carico la dataTable
        visualizzaTabellaRiconciliazioni();
    });
}();