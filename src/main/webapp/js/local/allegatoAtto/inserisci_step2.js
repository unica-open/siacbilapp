/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';
    $(function() {
        // Caricamento elenchi
        ElencoDocumentiAllegato.caricaElenchi("inserisciAllegatoAtto_ottieniListaElenchi.do", "#tabellaElencoDocumentiAllegato");
        // Inizializzo la gestione dell'elenco
        ElencoDocumentiAllegato.inizializza("#pulsanteApriModaleAssociaElencoDocumentiAllegato", "inserisciAllegatoAtto_associaElenco.do",
            undefined, undefined, undefined, true, true, "#formInserimentoAllegatoAttoStep2");
    });
}(jQuery));