/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    "use strict";
    var ZERO = 0;
    var st = global.StampeIva || {};

    /**
     * Attiva o disattiva il campo relativo all'iva a credito precedente, e popola il campo con il valore del gruppo.
     */
    function attivazioneIvaPrecedente() {
        var gruppoAttivitaIva = $("#gruppoAttivitaIva");
        var ivaPrecedente = $("#ivaACredito");

        var importoDaImpostare = ZERO;
        var readonlyDaImpostare = true;

        // Controllo di aver selezionato il gruppo: in caso contrario, disabilito l'iva precedente
        if(gruppoAttivitaIva.val()){
            readonlyDaImpostare = false;
            importoDaImpostare = gruppoAttivitaIva.find("option:selected")
                .data("ivaPrecedente");
        }
        ivaPrecedente.val(importoDaImpostare)
            .prop("readonly", readonlyDaImpostare)
            // Attivo subito la formattazione dell'importo
            .blur();
    }

    $(function() {
        $("#gruppoAttivitaIva").substituteHandler("change", function() {
            // Carica Periodo
            st.caricaTipoChiusuraEPeriodoEIvaACredito("stampaLiquidazioneIva_ottieniTipoChiusuraEPeriodoEIvaACreditoDaGruppo.do");
            attivazioneIvaPrecedente();
        });

        $("#pulsanteStampa").substituteHandler("click", function() {
            st.aperturaModaleConfermaStampa($("#gruppoAttivitaIva"), "Gruppo Attivit&agrave; Iva");
        });
    });
}(jQuery, this));