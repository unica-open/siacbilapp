/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, RichiestaEconomale) {
    "use strict";

    $(function() {
        // Inizializzo la tabella
        RichiestaEconomale.impostaTabellaTipoOperazioniCassa("risultatiRicercaAnticipoSpesePerMissioneCassaEconomaleAjax.do", "risultatiRicercaAnticipoSpesePerMissioneCassaEconomale_annulla.do", "risultatiRicercaAnticipoSpesePerMissioneCassaEconomale_aggiorna.do","risultatiRicercaAnticipoSpesePerMissioneCassaEconomale_stampaRicevuta.do");
    });
}(jQuery, RichiestaEconomale);