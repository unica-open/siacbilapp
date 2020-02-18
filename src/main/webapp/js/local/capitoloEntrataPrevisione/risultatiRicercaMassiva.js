/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
******************************************************************
**** Risultati Ricerca Massiva Capitolo Di Entrata Previsione ****
******************************************************************
*/

$(document).ready(
    function () {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
        RisultatiRicerca.wrapperMassivoDataTable("risultatiRicercaMassivaCapEntrataPrevisioneAjax.do",
                "risultatiRicercaCapEntrataPrevisione",
                startPos);
    }
);