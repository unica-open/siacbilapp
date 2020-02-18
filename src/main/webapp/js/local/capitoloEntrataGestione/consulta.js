/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    'use strict';

    $(function() {
        Capitolo.delayCaricamentoTab('#tabVincoli', '#vincoli', Capitolo.caricaVincoli.bind(null, 'EntrataGestione'), Capitolo.postCaricaVincoli);
        Capitolo.caricaTabellaCruscottinoLike('#tabellaAccertamentiCollegati', 'CAPITOLOENTRATA', 'ACCERTAMENTO', ['input[name="filtroAccertamento"]:checked']);
        Capitolo.caricaTabellaCruscottinoLike('#tabellaOrdinativiCollegati', 'CAPITOLOENTRATA', 'REVERSALE', ['input[name="filtroOrdinativo"]:checked']);
        $('button[data-esportazione]').substituteHandler('click', Capitolo.esportaRisultatiCruscottinoLike.bind(undefined, 'CAPITOLOENTRATA'));
        
        $('[data-reload-datatable]').each(function(idx, el) {
            Capitolo.handleReloadTabellaCruscottinoLike($(el));
        });
    });
}(jQuery);