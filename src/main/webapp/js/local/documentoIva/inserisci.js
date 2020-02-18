/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function ($, w, dociva) {
    "use strict";
     
    /**
     * Gestisce la modifica del tipo di registrazione IVA.
     */
    function modificaTipoRegistrazioneIva() {
        var selectedOption = $(this).find("option:selected");
        var selectedEsigibilita = selectedOption.data("tipoEsigibilitaIva");
        var divs = $("#gruppoProtocolloProvvisorio, #gruppoProtocolloDefinitivo");
        var divProvvisorio = $("#gruppoProtocolloProvvisorio");
        var divDefinitivo = $("#gruppoProtocolloDefinitivo");
        var regexDifferita = /DIFFERITA/;
        var regexImmediata = /IMMEDIATA/;

        if (regexDifferita.test(selectedEsigibilita) && !divProvvisorio.is(":visible")) {
            // Se ho selezionato il valore 'DIFFERITO' e il div e' ancora chiuso, lo apro
            divs.slideUp();
            divProvvisorio.slideDown();
        } else if (regexImmediata.test(selectedEsigibilita) && !divDefinitivo.is(":visible")) {
            // Se ho selezionato il valore 'IMMEDIATO' e il div e' ancora chiuso, lo apro
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
     *
     * @param obj (Object/Event) l'oggetto o evento scatenante l'invocazione
     */
    function ricalcoloRegistro(obj) {
        var suffix = (obj && obj.suffix) || "";
        var tipoRegistroIva = $("#tipoRegistroIva" + suffix).val();
        var attivitaIva = $("#attivitaIva"  + suffix).find("option:selected");
        var selectRegistro = $("#registroIvaSubdocumentoIva"  + suffix);
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
            oggettoPerChiamataAjax["registroIva.gruppoAttivitaIva.listaAttivitaIva[0].uid"] = attivitaIva.val();
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
            preSelezionaSeUnicaOpzione(selectRegistro);
        }).always(function () {
            selectRegistro.overlay("hide");
        });
    }

    /**
     * Controlla se il flag IRAP sia selezionato o meno.
     */
    function checkFlagIrap() {
        var rilevanteIrap = $("#attivitaIva").find("option:selected")
            .data("flagRilevanteIrap");
        $("#flagRilevanteIRAPSubdocumentoIva").attr("checked", !!rilevanteIrap);
    }

    $(function () {
        var tipo = $("#HIDDEN_tipoSubdocumentoIva").val();
        var $attivitaIva = $("#attivitaIva");
        var $attivitaIvaIntracomunitarioDocumento = $("#attivitaIvaIntracomunitarioDocumento");

        // Gestione dei campi della maschera principale
        $("#tipoRegistroIva").substituteHandler("change", function (e) {
            // Funzionalita' interne. Wrappo l'evento in un evento jQuery
            var evGestioneDiv = $.extend(true, {}, $.event.fix(e));
            var evCalcoloRegistro = $.extend(true, {}, $.event.fix(e));
            // Modifico il tipo dell'evento
            evGestioneDiv.type = "gestioneDiv";
            evCalcoloRegistro.type = "calcoloRegistro";
            evCalcoloRegistro.suffix = "";
            // Rilancio i due eventi sintetici
            $(this).trigger(evGestioneDiv).trigger(evCalcoloRegistro);
        })
            .on("gestioneDiv", modificaTipoRegistrazioneIva)
            .on("calcoloRegistro", ricalcoloRegistro)            
            .trigger("change");
        $attivitaIva.substituteHandler("change", function () {
            checkFlagIrap();
            // Ricalcolo il registro applicando il nuovo filtro
            ricalcoloRegistro({suffix: ""});
        });
        $attivitaIvaIntracomunitarioDocumento.substituteHandler("change", function() {
            ricalcoloRegistro({suffix: "IntracomunitarioDocumento"});
        });
        
        dociva.gestioneMovimentiIva("inserisciDocumentoIva" + tipo);
        dociva.gestioneIntracomunitario();
        
        if(preSelezionaSeUnicaOpzione($attivitaIva) || preSelezionaSeUnicaOpzione($attivitaIvaIntracomunitarioDocumento)){
        	$("#tipoRegistroIva").trigger("change");
        }
        
       preSelezionaSeUnicaOpzione($("#tipoRegistrazioneIvaSubdocumentoIva"));

        $("form").on("reset", function() {
            // Oltre al default, lego anche questo
            var attivitaIvaIntracomunitario = $("#attivitaIvaIntracomunitarioDocumento");
            var registroIvaIntracomunitario = $("#registroIvaIntracomunitarioDocumento");

            // Se sono disabilitati, non li tocco. Altrimenti li cancello
            attivitaIvaIntracomunitario.prop("disabled") || attivitaIvaIntracomunitario.val("");
            registroIvaIntracomunitario.prop("disabled") || registroIvaIntracomunitario.val("");
        });
    });
}(jQuery, window, DocumentoIva || {}));