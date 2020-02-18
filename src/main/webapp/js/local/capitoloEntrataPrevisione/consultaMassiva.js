/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(
    function () {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
        ConsultaMassiva.dataTable = RisultatiRicerca.wrapperDataTable("risultatiRicercaConsultazioneMassivaCapitoloEntrataPrevisione.do", "risultatiRicercaCapEntrataPrevisione", startPos, "risultatiricerca");

        // Lego le azioni
        $("#pulsanteAnnulla").on("click", function() {
            ConsultaMassiva.annulla("annullaCapEntrataPrevisioneAjax.do");
        });
        $("#pulsanteElimina").on("click", function() {
            ConsultaMassiva.elimina("eliminaCapEntrataPrevisioneAjax.do");
        });
    }
);