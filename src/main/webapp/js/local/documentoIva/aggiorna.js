/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function ($, w, dociva) {
    "use strict";

    /**
     * Gestisce il reset del form.
     */
    function gestioneResetForm () {
        // Oltre al default, lego anche questo
        var attivitaIvaIntracomunitario = $("#attivitaIvaIntracomunitarioDocumento_aggiornamento");
        var registroIvaIntracomunitario = $("#registroIvaIntracomunitarioDocumento_aggiornamento");
        var campiConValoreOriginario = $("[data-original-value]");

        // Se sono disabilitati, non li tocco. Altrimenti li cancello
        attivitaIvaIntracomunitario.prop("disabled") || attivitaIvaIntracomunitario.val("");
        registroIvaIntracomunitario.prop("disabled") || registroIvaIntracomunitario.val("");

        // Mantengo i valori originali
        campiConValoreOriginario.each(function(idx, el) {
            var $this = $(el);
            var isDate = $this.is("[data-date]");
            var originalValue = $this.data("originalValue");
            var derivedValue = originalValue;
            if(isDate) {
                // Sono una data. Dato che ci sono problemi di conversione con Java, prendo direttamente il time (come numero) dal campo e lo converto in data
                derivedValue = formatDate(new Date(parseInt(originalValue, 10)));
            }
            $this.val(derivedValue);
        });
    }

    $(function () {
        var tipo = $("#HIDDEN_tipoSubdocumentoIva").val();
        dociva.gestioneMovimentiIva("aggiornaDocumentoIva" + tipo, undefined, "_aggiornamento");
        // Funge anche per le note
        dociva.gestioneIntracomunitario("_aggiornamento");

        $("form").on("reset", gestioneResetForm);
    });
}(jQuery, window, DocumentoIva || {}));