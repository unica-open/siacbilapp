/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";

    var st = global.StampeIva || {};

    /**
     * Gestisce l'apertura dei div delle stampe a seconda della selezione operata sul tipo
     */
    function gestioneAperturaDivStampa(event) {

        var $this = $("#tipoStampaIvaStampaIva");
        var tipoSelezionato = $this.val();
        var divCampi = $("#campiStampaIva");
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
        if(selected === "liquidazione"){
            $("#labelPeriodo").html("Periodo *");
        }else{
            $("#labelPeriodo").html("Periodo");
        }
        divCampi.children("div")
                .not("div[data-" + selected + "]")
                    .fadeOut()
                .invert()
                    .fadeIn()
                    .end()
                .end()
            .fadeIn();
    }

    $(function() {
        var selectTipoRegistro = $("#tipoRegistroIvaRegistroIva");
        var selectGruppoAttivitaIva = $("#gruppoAttivitaIvaRegistroIva");
        var selectRegistro = $("#registroIva");
        var hiddenTipoChiusura = $("#hiddenTipoChiusura");
        var selectPeriodo = $("#periodoStampaIva");

        $("#tipoStampaIvaStampaIva").change(gestioneAperturaDivStampa);
        gestioneAperturaDivStampa();

        st.caricaRegistroIva(selectTipoRegistro, selectGruppoAttivitaIva, selectRegistro);
        st.caricaTipoChiusuraEPeriodoEIvaACredito("stampaRegistroIva_ottieniTipoChiusuraEPeriodoEIvaACreditoDaGruppo.do",
                selectGruppoAttivitaIva, undefined, hiddenTipoChiusura, selectPeriodo);

        selectTipoRegistro.substituteHandler("change", function() {
            // Gestisco i flag
            st.gestioneFlagsTipoRegistro(selectTipoRegistro);
            // Carico il registro
            st.caricaRegistroIva(selectTipoRegistro, selectGruppoAttivitaIva, selectRegistro);
        });

        selectGruppoAttivitaIva.substituteHandler("change", function() {
            // Carico il registro
            st.caricaRegistroIva(selectTipoRegistro, selectGruppoAttivitaIva, selectRegistro);
            // Carica Periodo
            st.caricaTipoChiusuraEPeriodoEIvaACredito("stampaRegistroIva_ottieniTipoChiusuraEPeriodoEIvaACreditoDaGruppo.do",
                selectGruppoAttivitaIva, undefined, hiddenTipoChiusura, selectPeriodo);
        });
    });
}(jQuery, this);