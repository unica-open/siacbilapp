/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function ($, w, dociva) {
    "use strict";

    /**
     * Gestisce la tabella delle note di credito
     */
    function gestioneTabellaNoteCredito() {
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: false,
            bFilter: false,
            bProcessing: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti note collegate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };
        $("#tabellaNoteCredito").dataTable(opts);
    }

    /**
     * Gestisce la modifica del tipo di registrazione IVA.
     */
    function modificaTipoRegistrazioneIva() {
        var selectedOption = $(this).find("option:selected");
        var selectedEsigibilita = selectedOption.data("tipoEsigibilitaIva");
        var divs = $("#nota_gruppoProtocolloProvvisorio, #nota_gruppoProtocolloDefinitivo");
        var divProvvisorio = $("#nota_gruppoProtocolloProvvisorio");
        var divDefinitivo = $("#nota_gruppoProtocolloDefinitivo");
        var regexDifferita = /DIFFERITA/;
        var regexImmediata = /IMMEDIATA/;

        if (regexDifferita.test(selectedEsigibilita) && !divProvvisorio.is(":visible")) {
            // Se ho selezionato il valore 'DIFFERITO' e il div Ã¨ ancora chiuso, lo apro
            divs.slideUp();
            divProvvisorio.slideDown();
        } else if (regexImmediata.test(selectedEsigibilita) && !divDefinitivo.is(":visible")) {
            // Se ho selezionato il valore 'IMMEDIATO' e il div Ã¨ ancora chiuso, lo apro
            divs.slideUp();
            divDefinitivo.slideDown();
        } else if (!selectedEsigibilita) {
            // Se non ho selezionato null, chiudo i div
            divs.slideUp();
        }
        // Se i div sono gia'  aperti, non modifico nulla
    }

    /**
     * Ricalcola la combo dei registri.
     */
    function ricalcoloRegistro() {
        var tipoRegistroIva = $("#nota_tipoRegistroIva").val();
        var attivitaIva = $("#nota_attivitaIva").find("option:selected");
        var selectRegistro = $("#nota_registroIvaSubdocumentoIva");
        var oggettoPerChiamataAjax = {};

        if (!tipoRegistroIva) {
            // Se non ho il tipo, disabilito e pulisco la select dei registri
            selectRegistro.val("")
                .attr("disabled", true)
                .find("option")
                    .not(":first")
                        .remove();
            return;
        }
        // Creo l'oggetto per la chiamata AJAX
        oggettoPerChiamataAjax["registroIva.tipoRegistroIva"] = tipoRegistroIva;
        if (attivitaIva.length && attivitaIva.val() !== "0") {
            oggettoPerChiamataAjax["registroIva.gruppoAttivitaIva.uid"] = attivitaIva.data("gruppoAttivitaIva");
        }

        selectRegistro.overlay("show");

        $.postJSON("ajax/registroIvaAjax.do", oggettoPerChiamataAjax, function (data) {
            // Se ho errori, esco
            if (impostaDatiNegliAlert(data.errori, dociva.alertErrori)) {
                return;
            }

            // Pulisco la select del registro
            selectRegistro.val("")
                .find("option")
                    .not(":first")
                        .remove();

            // Popolo la select
            $.each(data.listaRegistroIva, function () {
                selectRegistro.append($("<option>").val(this.uid).html(this.codice + " - " + this.descrizione));
            });

            selectRegistro.removeAttr("disabled");
            
          //CR 2515:preseleziono gli elementi delle combo box se l'elemento selezionabile è uno solo
            preSelezionaSeUnicaOpzione(selectRegistro);
            
        }).always(function () {
            selectRegistro.overlay("hide");
        });
    }

    /**
     * Controlla se il flag IRAP sia selezionato o meno.
     */
    function checkFlagIrap() {
        var rilevanteIrap = $("#nota_attivitaIva").find("option:selected")
            .data("flagRilevanteIrap");
        $("#nota_flagRilevanteIRAPSubdocumentoIva").attr("checked", !!rilevanteIrap);
    }

    $(function () {
        var tipo = $("#HIDDEN_tipoSubdocumentoIva").val();
        // Gestione dei campi della maschera principale
        if(!$("#nota_tipoRegistroIva").is(':disabled')){
	        $("#nota_tipoRegistroIva").substituteHandler("change", function (e) {
	            // Funzionalita' interne. Wrappo l'evento in un evento jQuery
	            var evGestioneDiv = $.extend(true, {}, $.event.fix(e));
	            var evCalcoloRegistro = $.extend(true, {}, $.event.fix(e));
	            // Modifico il tipo dell'evento
	            evGestioneDiv.type = "gestioneDiv";
	            evCalcoloRegistro.type = "calcoloRegistro";
	            // Rilancio i due eventi sintetici
	            $(this).trigger(evGestioneDiv).trigger(evCalcoloRegistro);
	        })
	            .on("gestioneDiv", modificaTipoRegistrazioneIva)
	            .on("calcoloRegistro", ricalcoloRegistro)
	            .trigger("gestioneDiv");
            
        	$("#nota_attivitaIva").substituteHandler("change", function () {
            	checkFlagIrap();
            	// Ricalcolo il registro applicando il nuovo filtro
            	ricalcoloRegistro();
        	});
        	
        	if(preSelezionaSeUnicaOpzione($("#nota_attivitaIva"))){
            	$("#nota_tipoRegistroIva").trigger("change");
            }
            preSelezionaSeUnicaOpzione($("#nota_tipoRegistrazioneIvaSubdocumentoIva"));
            $("#nota_registroIvaSubdocumentoIva").attr("disabled", true);
        } 
        
        gestioneTabellaNoteCredito();

        dociva.gestioneMovimentiIva("aggiornaDocumentoIva" + tipo, "nota_", "", "Nota", "Nota");
        
        
    });
}(jQuery, window, DocumentoIva || {}));