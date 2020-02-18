/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(
    function () {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
        ConsultaMassiva.dataTable = RisultatiRicerca.wrapperDataTable("risultatiRicercaConsultazioneMassivaCapitoloUscitaGestione.do", "risultatiRicercaCapUscitaGestione", startPos, "risultatiricerca");

        // Lego le azioni
        $("#pulsanteAnnulla").on("click", function() {
            ConsultaMassiva.annulla("annullaCapUscitaGestioneAjax.do");
        });
        $("#pulsanteElimina").on("click", function() {
            ConsultaMassiva.elimina("eliminaCapUscitaGestioneAjax.do");
        });
    }
);