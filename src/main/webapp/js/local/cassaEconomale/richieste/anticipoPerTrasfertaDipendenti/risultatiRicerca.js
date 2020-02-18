/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, RichiestaEconomale) {
    "use strict";

    $(function() {
        // Inizializzo la tabella
        RichiestaEconomale.impostaTabellaTipoOperazioniCassa("risultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomaleAjax.do", "risultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomale_annulla.do","risultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomale_aggiorna.do","risultatiRicercaAnticipoPerTrasfertaDipendentiCassaEconomale_stampaRicevuta.do");
    });
}(jQuery, RichiestaEconomale);