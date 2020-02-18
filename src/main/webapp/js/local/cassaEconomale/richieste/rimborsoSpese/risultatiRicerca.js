/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, RichiestaEconomale) {
    "use strict";

    $(function() {
        // Inizializzo la tabella
        RichiestaEconomale.impostaTabellaTipoOperazioniCassa("risultatiRicercaRimborsoSpeseCassaEconomaleAjax.do", "risultatiRicercaRimborsoSpeseCassaEconomale_annulla.do","risultatiRicercaRimborsoSpeseCassaEconomale_aggiorna.do","risultatiRicercaRimborsoSpeseCassaEconomale_stampaRicevuta.do");
    });
}(jQuery, RichiestaEconomale);