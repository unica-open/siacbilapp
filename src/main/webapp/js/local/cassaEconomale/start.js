/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    "use strict";
    var necessarioSelezionareCassa = $("#necessarioSelezionareCassa").val() === "true";
    var modalOpeningOpts = {
        "true": {
            backdrop: true,
            keyboard: true,
            show:     true
        },
        "false": {
            backdrop: "static",
            keyboard: false,
            show:     true
        }
    };
    var modal = $("#modaleSelezioneCassaEconomale");
    var errori = $("#ERRORI");
    var erroriModal = $("#erroriModaleSelezioneCassaEconomale");

    /**
     * Apertura del modale per la selezione della cassa.
     */
    function openModalSelezioneCassa(closeable) {
        modal.modal(modalOpeningOpts[closeable]);
    }

    /**
     * Copia dei messaggi di errore.
     *
     * @param start (jQuery) l'alert di partenza
     * @param end   (jQuery) l'alert di arrivo
     */
    function copyErrorMessages(start, end) {
        var isShown = !start.is(".hide");
        var content;
        if(!isShown) {
            // Non ho nulla da fare: esco subito
            return;
        }
        // Copio i messaggi
        content = start.find("ul").html();
        end.find("ul").html(content);
        // Chiudo l'alert originale e apro il nuovo
        start.slideUp();
        end.slideDown();
    }

    // Eseguo subito
    if(necessarioSelezionareCassa) {
        // Copio eventuali messaggi di errore
        copyErrorMessages(errori, erroriModal);
        // Apro il modale
        openModalSelezioneCassa(false);
    }

    // Lego delle funzionalita' al caricamento della pagina
    $(function() {
        // Disabilito tutti i pulsanti 'disableCas'
        $(".disableCas, .disabCs a").eventPreventDefault("click");
        $("#changeCassaEconomale").substituteHandler("click", function() {
            openModalSelezioneCassa(true);
        });
    });

}(jQuery);