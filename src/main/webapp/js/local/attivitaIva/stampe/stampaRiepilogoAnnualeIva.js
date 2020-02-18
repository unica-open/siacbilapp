/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    "use strict";

    var st = global.StampeIva || {};

    $(function() {
        $("#pulsanteStampa").substituteHandler("click", function() {
            st.aperturaModaleConfermaStampa($("#gruppoAttivitaIva"), "Gruppo Attivit&agrave; Iva");
        });
    });
}(jQuery, this));