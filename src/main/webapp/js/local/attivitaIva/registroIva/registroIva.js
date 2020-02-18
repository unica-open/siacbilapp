/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    var RegistroIva = (function() {
        // Per lo sviluppo
        "use strict";

        var exports = {};

        /**
         * Copia la descrizione dal sorgente alla destinazione.
         *
         * @param selSource (String) il selettore della select sorgente
         * @param selDest   (String) il selettore della textarea destinazione
         */
        exports.copiaDescrizione = function(selSource, selDest) {
            // Selezione della option selezionata nel filtro. Cfr. http://api.jquery.com/selected-selector/
            var source = $(selSource).find("option")
                .filter(":selected");
            var dest = $(selDest);

            dest.html(source.data("descrizione"));

            if(source.val()) {
                dest.removeAttr("disabled");
            } else {
                dest.attr("disabled", true);
            }
        };

        return exports;
    }());

    w.RegistroIva = RegistroIva;
}(jQuery, window));