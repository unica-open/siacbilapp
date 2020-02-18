/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    $(function() {
        ProvvisorioDiCassa.inizializzazione("#pulsanteAperturaCompilazioneGuidataProvvisorioCassa",
            "input[name='provvisorioDiCassa.tipoProvvisorioDiCassa']", "#annoProvvisorioDiCassa", "#numeroProvvisorioDiCassa");
    });
}(jQuery);