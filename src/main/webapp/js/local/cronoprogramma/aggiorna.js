/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function() {
    'use strict';
    var baseUrl = $('#HIDDEN_baseUrl').val();

    $(function() {
        // Caricamento liste dei dettagli
        $.postJSON(baseUrl + 'OttieniListeDettaglio.do', Cronoprogramma.caricaTabelleDettagli);
        var provvedimento = new Provvedimento(undefined, 'provvedimento', 'provvedimento: ');
    	provvedimento.inizializza();
    });

}();