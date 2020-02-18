/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RicercaFattura = (function($, exports) {
    "use strict";

    var alertErrori = $("#ERRORI");
    var divRisultati = $("#tabellaFattureDIV");
    var tableFatture = $("#tabellaFatture");
    var tableSubdocumento = $("#tabellaRicercaDocumentoSpesaSubdocumento");
    var divTabellaSubdocumento = $("#divRicercaDocumentoSpesaSubdocumento");

    var baseOpts = {
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
            sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun elemento disponibile"
            }
        }
    };

    /**
     * Ricerca le fatture
     */
    function ricercaFatture() {
        var obj = $("#fieldsetRicercaFatture").serializeObject();
        if(!window.semaphore.soggetto){
	        divRisultati.slideUp();
	        var spinner = $("#SPINNER_pulsanteCercaFattura").addClass("activated");

	        var oggettoPerChiamataAjax = unqualify(obj, 1);
	
	        oggettoPerChiamataAjax.collegatoCEC = true;
	        oggettoPerChiamataAjax.contabilizzaGenPcc = true;
	        // Effettuo la ricerca /
	        $.postJSON("ricercaDocumentoSpesaAjax_ricercaFattureDaAssociareCEC.do", oggettoPerChiamataAjax, function(data) {
	            // Se ho errori, esco
	            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
	                return;
	            }
	            // Non ho errori. Mostro la tabella e calcolo i risultati e chiudo il collapse
	            $("#collapseFattura.in").collapse("hide");
	            	impostaTabellaFatture();
	        }).always(function() {
	            spinner.removeClass("activated");
	        });
        }
    }
    
    /**
     * Impostazione della tabella delle fatture
     */
    function impostaTabellaFatture() {
        var tableId = "#" + tableFatture.attr("id");
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDocumentoSpesaAjax.do",
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            },
            aoColumnDefs: [
               {aTargets : [0], mData : function() {
                   return "<input type='radio' name='checkDocumento'/>";
               }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                   $("input", nTd).data("originalDatiDocumento", oData);
               }},
                {aTargets: [1], mData: defaultPerDataTable('documento')},
                {aTargets: [2], mData: defaultPerDataTable('data')},
                {aTargets: [3], mData: defaultPerDataTable('statoOperativoDocumentoDesc')},
                {aTargets: [4], mData: defaultPerDataTable('soggetto')},
                {aTargets: [5], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [6], mData: defaultPerDataTable('documentoSpesa.importoDaPagareNonPagatoInCassaEconomale', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        if($.fn.DataTable.fnIsDataTable(tableFatture[0])) {
            tableFatture.dataTable().fnDestroy();
        }
        tableFatture.dataTable(options);
        divTabellaSubdocumento.slideUp();
        divRisultati.slideDown();
    }

    /**
     * Caricamento della tabella delle quote da visualizzare.
     *
     * @param table (jQuery) la tabella da popolare
     * @param list  (Array)  la lista tramite cui popolare la tabella
     */
    function tabellaDatiSubdocumentoSpesa(table, list) {
        var options = {
            bServerSide: false,
            aaData: list,
            fnDrawCallback: function () {
                var tableId = "#" + table.attr("id");
                $(tableId + "_processing").parent("div").hide();
            },
            aoColumnDefs: [
               {aTargets: [0], mData: function() {
                   return "<input type='checkbox' name='checkSubdocumento'/>";
               }, fnCreatedCell: function(nTd, sData, oData) {
                   $("input", nTd).data("originalDatiSubdocumento", oData);
               }},
                {aTargets: [1], mData: defaultPerDataTable('numero')},
                {aTargets: [2], mData: defaultPerDataTable('importo', 0, formatMoney)},
                {aTargets: [3], mData: defaultPerDataTable('importoSplitReverse', 0, formatMoney)},
                {aTargets: [4], mData: function(source) {
                    return gestioneBoolean(source.pagatoInCEC, "Si", "No");
                }},
                {aTargets: [5], mData: function(source) {
                    return formatDate(source.dataPagamentoCEC);
                }}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        // DIsattivo eventualmente il datatable
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        table.dataTable(opts);
    }

    /**
     * Associa la fattura selezionata
     */
    function associaFattura() {
        var checkedDocumento = $("#tabellaFatture").find("[name='checkDocumento']:checked");
        var checkedSubdocumentoSpesa = tableSubdocumento.find("[name='checkSubdocumento']:checked");
        var datiDocumentoSpesa;
        var datiSubdocumentoSpesa = [];

         // Se non ho selezionato nulla, esco subito
        if(checkedDocumento.length === 0 || checkedSubdocumentoSpesa.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare uno dei dati proposti"], alertErrori, false);
            return;
        }
        // Pruning dei dati
        datiDocumentoSpesa = pruneDocumento(checkedDocumento.data("originalDatiDocumento").documentoSpesa);
        datiSubdocumentoSpesa = checkedSubdocumentoSpesa.map(function(idx, el) {
            return pruneSubdocumento($(el).data("originalDatiSubdocumento"));
        }).get();

        $(document).trigger('associaFattura', {fattura: datiDocumentoSpesa, quote: datiSubdocumentoSpesa, alert: alertErrori, alertRedirect: true, callback: function() {
            divRisultati.slideUp();
        }});
    }

    /**
     * Effettua un pruning del documento, mantenendo solo i dati necessarii.
     *
     * @param doc (Object) il documento
     */
    function pruneDocumento(doc) {
        var res = {};
        // Dati che mi interessano: uid, anno, tipoDocumento (uid, codice, descrizione, codiceGruppo), numero, soggetto (uid, codice soggetto, denominazione), data emissione
        // Aggiungo descAnnoNumeroTipoDoc per comodita'
        var propertiesToClone = ['uid', 'anno', 'tipoDocumento.uid', 'tipoDocumento.codiceGruppo', 'tipoDocumento.codice', 'tipoDocumento.descrizione', 'numero',
            'soggetto.uid', 'soggetto.codiceSoggetto', 'soggetto.denominazione', 'dataEmissione', 'statoOperativoDocumento', 'descAnnoNumeroTipoDoc'];

        cloneProperties(doc, res, propertiesToClone);
        return res;
    }

    /**
     * Effettua un pruning del subdocumento, mantenendo solo i dati necessarii.
     *
     * @param subdoc (Object) il subdocumento
     */
    function pruneSubdocumento(subdoc) {
        var res = {};
        // Dati che mi interessano: uid, numero
        var propertiesToClone = ['uid', 'numero', 'flagRilevanteIVA', 'importo', 'impegno.uid', 'impegno.annoMovimento', 'impegno.numero',
            'subImpegno.uid', 'subImpegno.annoMovimento', 'subImpegno.numero', 'pagatoInCEC', 'importoSplitReverse', 'modalitaPagamentoSoggetto'];

        cloneProperties(subdoc, res, propertiesToClone);
        return res;
    }

    /**
     * Cerca i dati del subdocumento di spesa.
     */
    function cercaDatiSubdocumentoSpesa() {
        var checkedRadio = tableFatture.find("input[type='radio'][name='checkDocumento']").filter(":checked");
        var documento;
        var row;
        // Se nulla e' stato selezionato, esco
        if(!checkedRadio.length) {
            return this;
        }
        divTabellaSubdocumento.slideUp();
        alertErrori.slideUp();

        documento = checkedRadio.data("originalDatiDocumento").documentoSpesa;
        row = checkedRadio.closest("tr").overlay("show");
        
        $.postJSON("ricercaDocumentoSpesaAjax_ricercaQuoteDocumentoSpesa.do", {"documentoSpesa.uid": documento.uid}, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori, false)) {
                return;
            }

            // Carico i dati in tabella
            tabellaDatiSubdocumentoSpesa(tableSubdocumento, data.listaSubdocumentoSpesa);
            divTabellaSubdocumento.slideDown();
        }).always(function() {
            // Disattivo lo spinner
            row.overlay("hide");
        });

        return this;
    }
    
    function gestioneSoggetto(campoCodiceSoggetto, spanDescrizione, campoUidSoggetto) {
    	 return Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, $(), $(), spanDescrizione,undefined,campoUidSoggetto, undefined,true)
    	 .then(function() {
    		 var pulsanteCercaFattura = $("#pulsanteCercaFattura");
        	 var soggettoNonTrovato = campoCodiceSoggetto.val() != "" && campoUidSoggetto.val() === "";
        	 soggettoNonTrovato ? pulsanteCercaFattura.attr("disabled","disabled") : pulsanteCercaFattura.removeAttr("disabled");
    	 });
    }

    $(function() {
        var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        var campoUidSoggetto = $("#HIDDEN_soggettoUid");

        $("#pulsanteCercaFattura").substituteHandler("click", ricercaFatture);
        $("#pulsanteAssociaFattura").substituteHandler("click", associaFattura);
        // Carica il dettaglio del soggetto e apre gli accordion
        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	    var def = gestioneSoggetto(campoCodiceSoggetto, spanDescrizione, campoUidSoggetto);
        	    if(!params || !params.doNotCloseAlerts) {
        	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){
            			impostaDatiNegliAlert(errori, alertErrori);
            			return errori;
            		});
        	    }
        		
            })
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});

        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan", "#pulsanteAperturaCompilazioneGuidataSoggetto", "","#HIDDEN_soggettoUid");
        DatiDocumentoSpesa.inizializza("#tipoDocumentoDocumentoSpesaRic", "#annoDocumentoSpesaRic", "#numeroDocumentoSpesaRic", "#pulsanteCompilazioneGuidataFattura");

        // Lotto M
        tableFatture.substituteHandler("change", "input[type='radio'][name='checkDocumento']", cercaDatiSubdocumentoSpesa);
    });

    return exports;
}(jQuery, RicercaFattura || {}));