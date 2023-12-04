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
    var dataTable;

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

  
    

   


    /**
     * Caricamento via Ajax della tabella dei documenti e visualizzazione.
     */
    function visualizzaTabellaDocumenti() {
        var options = {
            bServerSide: true,
            // SIAC-6769
            bDestroy: true,
            sAjaxSource: "risultatiRicercaElaborazioniFlussoAjax.do",
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
            			if(data.moreData.importoTotale !== undefined) {
            				$('#importoTotale').html(data.moreData.importoTotale.formatMoney());
            			}
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
                {aTargets: [0], mData: "elaborazione.numeroProvvisorio", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [1], mData: "dataEmissioneStr", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
//                {aTargets: [2], mData: function(source) {
//                    return source.statoOperativoDocumentoCode + " / "+ source.statoOperativoDocumentoDesc;
//                }},
                {aTargets: [2], mData: "elaborazione.versante", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [3], mData: "elaborazione.flusso", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
//                {aTargets: [4], mData: "contabilizzato"},
                {aTargets: [4], mData: defaultPerDataTable('elaborazione.importoProvvisorio', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [5], mData: "dataElaborazioneStr", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                {aTargets: [6], mData: "esitoElaborazioneStato", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-center");
                }},
                
                {aTargets: [7], mData: "azioni", fnCreatedCell: function (nTd, sData, oData) {
                   
                    $('.dropdown-toggle', nTd).dropdown();
                }
                
                
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        var opts;
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }
        opts = $.extend(true, {}, baseOpts, options);

        $("#risultatiRicercaDocumento").dataTable(opts);
    }

   
   

   

    /**
     * Chiusura del modale degli ordini.
     *
     * @param id (String) l'ide del modale da chiudere
     */
    function chiudiModale(id) {
        $(id).modal("hide");
    }

    $(function() {
        // Carico la dataTable
        visualizzaTabellaDocumenti();

       
    });
}();