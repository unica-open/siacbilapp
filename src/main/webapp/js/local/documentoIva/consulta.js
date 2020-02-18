/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {

    "use strict";

    var optionsBase = {
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
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };
    
    /**
     * Caricamento via Ajax della tabella dei preDocumenti e visualizzazione della stessa.
     */
    function dataTableTables(/* Parameters */) {
        var options = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessun dato presente",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }
        var tables = $();
        var i;
        var length = arguments.length;
        for(i = 0; i < length; i++) {
            tables.add("#" + arguments[i]);
        }

        tables.dataTable(options);
    }

    
    /**
     * Carica il dettaglio della registrazione e mostra la modale
     *
     * @param e (Event) l'evento scatenante
     */
    function ottieniDettaglioRegistrazione(e) {
        var $this = $(this);
        var uid = $this.data("uid");
        var tipo = $("#HIDDEN_tipoEntrataSpesa").val();
        $.postJSON("consultaDocumentoIva"+tipo+"_ottieniDettaglioQuotaIvDifferita.do", {uidQuotaIvaDifferita: uid}, function(data){
            caricaDettaglioQuotaIvaDifferita(data.quotaIvaDifferita);
            $("#quotaIvaDifferita").modal("show");
        });
        
    }
    
    /**
     * 
     *
     */
    function caricaDettaglioQuotaIvaDifferita(quotaIva) {
    	var tipoRegistroIva = quotaIva.registroIva.tipoRegistroIva;
    	if(tipoRegistroIva != null){
    		tipoRegistroIva = tipoRegistroIva.split("_").join(" ");
    	}
    	$("#progressivoIVA").html(quotaIva.progressivoIVA);
    	$("#annoEsercizio").html(quotaIva.annoEsercizio);
    	$("#tipoRegistrazioneIva").html(quotaIva.tipoRegistrazioneIva.codice +" - "+ quotaIva.tipoRegistrazioneIva.descrizione);
    	$("#tipoRegistroIva").html(tipoRegistroIva);
    	$("#attivitaIva").html(quotaIva.attivitaIva != null ? quotaIva.attivitaIva.codice + " - " + quotaIva.attivitaIva.descrizione : "");
    	$("#flagRilevanteIRAP").html(quotaIva.flagRilevanteIRAP === "true" ? "Sì" : "No");
    	$("#registroIva").html(quotaIva.registroIva.codice + " - " + quotaIva.registroIva.descrizione);
    	$("#numeroProtocolloDefinitivo").html(quotaIva.numeroProtocolloDefinitivo);
    	$("#dataProtocolloDefinitivo").html(formatDate(quotaIva.dataProtocolloDefinitivo));
    	$("#descrizioneIva").html(quotaIva.descrizioneIva);
    	popolaTabellaMovimentiIva(quotaIva.listaAliquotaSubdocumentoIva);
    	$("#totaleImponibileMovimentiIva").html(quotaIva.totaleImponibileMovimentiIva.formatMoney());
    	$("#totaleImpostaMovimentiIva").html(quotaIva.totaleImpostaMovimentiIva.formatMoney());
    	$("#totaleTotaleMovimentiIva").html(quotaIva.totaleTotaleMovimentiIva.formatMoney());
        
    }
    
    function popolaTabellaMovimentiIva(listaAliquote) {
    	var options = {
                aaData: listaAliquote,
                oLanguage: {
                    sZeroRecords: "Non sono presenti aliquote"
                },
                aoColumnDefs: [
                    {aTargets: [0], mData: function(source){
                        var res = "";
                        if(source.aliquotaIva) {
                            res = source.aliquotaIva.codice + " - " + source.aliquotaIva.descrizione;
                        }
                        return res;
                    }},
                    {aTargets: [1], mData: function(source){
                        var res = "";
                        if(source.aliquotaIva) {
                            res = source.aliquotaIva.percentualeAliquota;
                        }
                        return res;
                    }},
                    {aTargets: [2], mData: function(source){
                        return source.imponibile.formatMoney();
                    }, fnCreatedCell: function(nTd){
                        $(nTd).addClass("tab_Right");
                    }},
                    {aTargets: [3], mData: function(source) {
                    	return source.imposta.formatMoney();
                    }, fnCreatedCell: function(nTd){
                        $(nTd).addClass("tab_Right");
                    }},
                    {aTargets: [4], mData: function(source) {
                    	return source.impostaDetraibile.formatMoney();
                    }, fnCreatedCell: function(nTd){
                        $(nTd).addClass("tab_Right");
                    }},
                    {aTargets: [5], mData: function(source) {
                    	return source.impostaIndetraibile.formatMoney();
                    }, fnCreatedCell: function(nTd){
                        $(nTd).addClass("tab_Right");
                    }},
                    {aTargets: [6], mData:  function(source){
                        var importo = source.totale;
                        return importo.formatMoney();
                    }, fnCreatedCell: function(nTd){
                        $(nTd).addClass("tab_Right");
                    }}
                ]
            };
            var opts = $.extend(true, {}, optionsBase, options);
            $("#tabellaModaleMovimentiIva").dataTable(opts);
    }		

    $(function() {
        dataTableTables("quotePagateTable", "movimentiIvaTable");
        $(".dettaglioRegistrazione").on("click", ottieniDettaglioRegistrazione);
    });

}(jQuery));