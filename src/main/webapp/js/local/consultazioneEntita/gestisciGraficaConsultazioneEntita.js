/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * gestisce il dimensionamento e le funzionalita' grafiche del cruscottino
 */

!function($) {
    "use strict";

    function computeHeight() {
        var w = window;
        var d = document;
        var e = d.documentElement;
        var g = d.getElementsByTagName('body')[0];

        var y = w.innerHeight || e.clientHeight || g.clientHeight;

        var hContenitoreOuter = $('.contentPage').outerHeight(true);
        var hContenitoreOuterNoMargin = $('.contentPage').outerHeight();
        var hContenitore = $('.contentPage').height();
        var hPaggingBorder = hContenitoreOuter - hContenitore;

        var margin = hContenitoreOuter - hContenitoreOuterNoMargin;

        var hSopraOuter = $('.container-fluid-banner').outerHeight(true) + $('.nascosto').outerHeight(true);
        var hSottoOuter = $('#portalFooter').outerHeight(true);

        var hTitoloOuter = $('.contentPage> h3').outerHeight(true);

        var newContainerHeight = Math.max(y - hSopraOuter - hSottoOuter - margin, 0);

        var newRicercaSinteticaHeight = Math.max(newContainerHeight - hTitoloOuter - hPaggingBorder - 10, 0);
        return {'hContainer': newContainerHeight, 'hricerca': newRicercaSinteticaHeight};
    }

    $(function(){
        var resizing;

        $( window ).resize(function() {
            if(resizing) {
                clearTimeout(resizing);
            }

            resizing = setTimeout(function() {
                var heights = computeHeight();
                var y = heights.hContainer;
                var h = heights.hricerca;

                $('.contentPage').outerHeight(y);
                $('.consultazione-entita').outerHeight(h);
                $('.contentPage .row-fluid .step-content').outerHeight(h);
                resizing = undefined;
            }, 300 );
        }).trigger('resize');

    });
}(jQuery);