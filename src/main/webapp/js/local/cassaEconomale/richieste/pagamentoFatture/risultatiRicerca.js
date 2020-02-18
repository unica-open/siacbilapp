/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, RichiestaEconomale) {
    "use strict";

    $(function() {
        // Inizializzo la tabella
        RichiestaEconomale.impostaTabellaTipoOperazioniCassa("risultatiRicercaPagamentoFattureCassaEconomaleAjax.do", "risultatiRicercaPagamentoFattureCassaEconomale_annulla.do","risultatiRicercaPagamentoFattureCassaEconomale_aggiorna.do","risultatiRicercaPagamentoFattureCassaEconomale_stampaRicevuta.do");
    });
}(jQuery, RichiestaEconomale);