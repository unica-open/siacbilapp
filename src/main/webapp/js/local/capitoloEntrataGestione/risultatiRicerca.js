/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *********************************************************
 **** Risultati Ricerca Capitolo Di Entrata Gestione ****
 *********************************************************
 */

$(document).ready(
    function () {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
        RisultatiRicerca.wrapperDataTable("risultatiRicercaCapEntrataGestioneAjax.do", "risultatiRicercaCapEntrataGestione", startPos);
    }
);