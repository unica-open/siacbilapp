/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    var divsMayHideNatura = $('[data-hidden-natura]', '#inserimentoTipoOnere');
    var divsMayHideSplitReverse = $('[data-hidden-split-reverse]', 'form');
    var divSelectSplitReverse = $("#tipoIvaSplitReverseTipoOnereDiv");
    var selectSplitReverse = $('#tipoIvaSplitReverseTipoOnere');
    var selectNaturaOnere = $('#naturaOnere');
    var optionEsente = selectSplitReverse.find('option[data-esente]');

    var codiceEsenzione = $('#hidden_codiceEsenzione').val();
    var codiceSplitReverse = $('#hidden_codiceSplitReverse').val();
    var codice;

    /**
     * Effettua il cambio dei dati della natura, controllando quali ragonamenti debba effettuare lo split/reverse
     */
    function cambioNaturaSplitReverse() {
        var selectedOption = $('option:selected', selectNaturaOnere);
        var codiceSplitReverseSelezionato = $('option:selected', selectSplitReverse).data('codice');
        var divsToHide;
        if(!selectedOption.length || !selectedOption.val()) {
            // Nulla da effettuare: esco
            return;
        }

        codice = selectedOption.data('codice');
        divsToHide = divsMayHideNatura.filter('[data-hidden-natura~="' + codice + '"]');
        divsMayHideSplitReverse.slideDown();
        showDivs(divsMayHideNatura.not(divsToHide));
        hideDivs(divsToHide);

        // Gestione della select
        if(codice === codiceEsenzione) {
            divSelectSplitReverse.slideUp();
            // Esente: seleziono direttamente il campo 'esente'
            selectSplitReverse.removeAttr('disabled');
            optionEsente.prop('selected', true);
            return;
        }
        if(codice === codiceSplitReverse) {
            divSelectSplitReverse.slideDown();
            selectSplitReverse.removeAttr('disabled');
            if (optionEsente.is(':checked')) {
                selectSplitReverse.val('');
            }
            $("label[for='aliquotaCaricoSoggetto']").html(codiceSplitReverseSelezionato === "SI" || codiceSplitReverseSelezionato === "SC"? "Aliquota": "Aliquota a carico soggetto");
            return;
        }
        divSelectSplitReverse.slideUp();
        $("label[for='aliquotaCaricoSoggetto']").html("Aliquota a carico soggetto");
        selectSplitReverse.attr('disabled', true);
    }

    /**
     * Effettua il cambio dello split/reverse con la gestione dei campi
     */
    function cambioSplitReverse() {
        var selectedOption = $('option:selected', selectSplitReverse);
        var divsToHide;
        if(!selectedOption.length || !selectedOption.val()) {
            // Nulla da effettuare: esco
            return;
        }
        codice = selectedOption.data('codice');
        divsToHide = divsMayHideSplitReverse.filter('[data-hidden-split-reverse~="' + codice + '"]');
        
        $("label[for='aliquotaCaricoSoggetto']").html(codice === "SI" || codice === "SC" ? "Aliquota" : "Aliquota a carico soggetto");
        
        showDivs(divsMayHideSplitReverse.not(divsToHide));
        hideDivs(divsToHide);
    }

    /**
     * Mostra i div forniti.
     *
     * @param divs (jQuery) i div da mostrare
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function showDivs(divs) {
        return applyOnDivs(divs, 'slideDown', 'removeAttr');
    }

    /**
     * Nasconde i div forniti.
     *
     * @param divs (jQuery) i div da nascondere
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function hideDivs(divs) {
        return applyOnDivs(divs, 'slideUp', 'attr');
    }

    /**
     * Applica le funzioni fornite ai div.
     *
     * @param divs     (jQuery) i div su cui applicare le funzioni
     * @param slideFnc (String) il nome della funzione di slide da applicare (slideUp / slideDown)
     * @param attrFnc  (String) il nome della funzione di attributi da applicare (attr / removeAttr)
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function applyOnDivs(divs, slideFnc, attrFnc) {
        return divs[slideFnc]()
            .filter(':input')[attrFnc]('disabled', true).end()
            .find(':input')[attrFnc]('disabled', true).end()
            .find('.chosen-select').trigger('chosen:updated').end();
    }

    $(function() {
        selectNaturaOnere.substituteHandler('change', cambioNaturaSplitReverse).change();
        selectSplitReverse.substituteHandler('change', cambioSplitReverse).change();
                
        $('form').on('reset', function() {
            $('.chosen-select').trigger('chosen:updated');
        });
    });
}(jQuery);