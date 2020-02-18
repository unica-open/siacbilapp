/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    "use strict";

    var alertErrori = $("#ERRORI");

    /**
     * Gestisce la modifica del tipo di registrazione IVA.
     */
    function modificaTipoRegistrazioneIva() {
        var tipoEsigibilitaIva = $(this).find("option:selected").data("tipoEsigibilitaIva");
        var divs = $("#ricercaProtocolloProvvisorio, #ricercaDataProtocolloProvvisorio, #ricercaProtocolloDefinitivo, #ricercaDataProtocolloDefinitivo");
        var divsProvvisorio = $("#ricercaProtocolloProvvisorio, #ricercaDataProtocolloProvvisorio");
        var divsDefinitivo = $("#ricercaProtocolloDefinitivo, #ricercaDataProtocolloDefinitivo");

        if ("DIFFERITA" === tipoEsigibilitaIva && divsDefinitivo.is(':visible')) {
            // Se ho selezionato il valore 'DIFFERITO' e il div e' ancora chiuso, lo apro
            divs.slideDown();
            // SIAC-4462: per il definitivo devo comunque mostrare il protocollo definitivo
        } else if ("IMMEDIATA" === tipoEsigibilitaIva && divsProvvisorio.is(":visible")) {
            // Se ho selezionato il valore 'IMMEDIATO' e il div e' ancora chiuso, lo apro
            divs.slideDown();
            divsProvvisorio.slideUp();
        } else if (!tipoEsigibilitaIva) {
            // Se non ho selezionato null, apro tutti i div
            divs.slideDown();
        }
        // Se i div sono gia' aperti, non modifico nulla
    }

    /**
     * Ricalcola la combo dei registri.
     */
    function ricalcoloRegistro() {
        var tipoRegistroIva = $("#tipoRegistroIva").val();
        var attivitaIva = $("#attivitaIva").find("option:selected");
        var selectRegistro = $("#registroIvaSubdocumentoIva");
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

        $.postJSON("ajax/registroIvaAjax.do", oggettoPerChiamataAjax)
        .then(function(data) {
            // Se ho errori, esco
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Pulisco la select del registro
            selectRegistro.val("")
                .find("option")
                    .not(":first")
                        .remove();

            // Popolo la select
            $.each(data.listaRegistroIva, function() {
                selectRegistro.append($("<option>").val(this.uid).html(this.codice + " - " + this.descrizione));
            });

            selectRegistro.removeAttr("disabled");
        }).always(selectRegistro.overlay.bind(selectRegistro, 'hide'));
    }

    /**
     * Controlla se il flag IRAP sia selezionato o meno.
     */
    function checkFlagIrap() {
        var rilevanteIrap = $("#attivitaIva").find("option:selected")
            .data("flagRilevanteIrap");

        $("#flagRilevanteIRAP").attr("checked", !!rilevanteIrap);
    }


    /**
     * Apre il modale del soggetto e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriModaleSoggetto(e) {
        e.preventDefault();
        $("#codiceSoggetto_modale").val($("#codiceSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    }

    $(function() {

        // Gestione dei campi della maschera principale
        $("#tipoRegistroIva").substituteHandler("change", function(e) {
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
        $("#attivitaIva").substituteHandler("change", function() {
            checkFlagIrap();
            // Ricalcolo il registro applicando il nuovo filtro
            ricalcoloRegistro();
        });

        $("#attivitaIva").substituteHandler("change", function() {
            checkFlagIrap();
            // Ricalcolo il registro applicando il nuovo filtro
            ricalcoloRegistro();
        });

        $("#pulsanteCompilazioneSoggetto").click(apriModaleSoggetto);
    });
}(jQuery));