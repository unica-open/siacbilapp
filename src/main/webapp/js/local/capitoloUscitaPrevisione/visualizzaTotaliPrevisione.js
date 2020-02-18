/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function() {
    'use strict';
    function bindOpenComponenti(year) {
        $('#competenzaTotaleAnno' + year).eventPreventDefault('click', function(){
            $('.componentiCompetenzaRow' + year).toggle();
        });
    }

    $(function() {
        bindOpenComponenti(0);
        bindOpenComponenti(1);
        bindOpenComponenti(2);
    });
}();
