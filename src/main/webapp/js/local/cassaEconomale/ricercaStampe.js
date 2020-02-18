/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    /**
     * Gestisce l'apertura dei div delle stampe a seconda della selezione operata sul tipo
     */
    function gestioneAperturaDivStampa(event) {

        var $this = $("#tipoStampa");
        var tipoSelezionato = $this.val();
        var divCampi = $("#campiStampaCassa");
        var split;
        var selected;

        if(event) {
            $("#ERRORI").slideUp();
        }

        // Se non ho scelto nulla, nascondo il div ed esco
        if(!tipoSelezionato) {
            divCampi.fadeOut();
            return;
        }
        // Ho selezionato qualcosa. Vedo cosa ho selezionato
        split = tipoSelezionato.toLowerCase().split("_");
        selected = split[0];
        divCampi.children("div")
                .not("div[data-" + selected + "]")
                    .attr("disabled", true)
                    .fadeOut()
                .invert()
                    .fadeIn()
                    .removeAttr("disabled")
                    .end()
                .end()
            .fadeIn();

    }


    $(function() {

        $("#tipoStampa").change(gestioneAperturaDivStampa);
        gestioneAperturaDivStampa();
        $("form").on("reset", function() {
            setTimeout(function() {
                $("#tipoStampa").change();
            }, 10);
        });

    });

}(jQuery);