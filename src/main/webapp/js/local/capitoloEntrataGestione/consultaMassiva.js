/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(
    function () {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
        ConsultaMassiva.dataTable = RisultatiRicerca.wrapperDataTable("risultatiRicercaConsultazioneMassivaCapitoloEntrataGestione.do", "risultatiRicercaCapEntrataGestione", startPos, "risultatiricerca");

        // Lego le azioni
        $("#pulsanteAnnulla").on("click", function() {
            ConsultaMassiva.annulla("annullaCapEntrataGestioneAjax.do");
        });
        $("#pulsanteElimina").on("click", function() {
            ConsultaMassiva.elimina("eliminaCapEntrataGestioneAjax.do");
        });
    }
);