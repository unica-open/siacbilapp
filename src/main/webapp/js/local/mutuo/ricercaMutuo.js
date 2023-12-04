/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';

        $("#formRicercaMutuo").on("reset", function() {
            $("#datiRiferimentoAttoAmministrativoSpan").empty();
            $("#descrizioneCompletaSoggetto").empty();
        });

        $(".tipoTasso").mousedown(function() {
       		$(this).data('was-checked', $(this).prop('checked'));
        }).click(function(){
        	if ($(this).data('was-checked')) {
        		$(this).prop('checked', false);
        	}
        });
        
 }(jQuery);