/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    $(function() {
        var uidEquivalente = $("#HIDDEN_UID_EQUIVALENTE").val();

        Capitolo.delayCaricamentoTab('#tabVincoli', '#vincoli', Capitolo.caricaVincoli.bind(null, 'EntrataPrevisione'), Capitolo.postCaricaVincoli);
        if(!!uidEquivalente && uidEquivalente !== "0") {
            Capitolo.ricercaVariazioniEquivalente(uidEquivalente);
        }
    });
}(jQuery);