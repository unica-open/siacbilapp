/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    
    var divsMayHideSplitReverse = $('[data-hidden-split-reverse]', 'form');
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: true,
        bDestroy: true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    
    /**
     * Effettua il cambio dello split/reverse con la gestione dei campi
     */
    function cambioSplitReverse() {
    	var selectSplitReverse = $('#tipoIvaSplitReverseTipoOnere');
        var selectedOption = $('option:selected', selectSplitReverse);
        var codice;
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
    
    /**
     * Calcola la stringa del movimento di gestione
     * @param movgestName  (any) il movimento di gestione
     * @param subName      (any) il submovimento
     * @param capitoloName (any) il capitolo
     * @param source       (any) i dati sorgente
     * @return (string) la stringa del movimento di gestione
     */
    function computeStringMovimentoGestione(movgestName, subName, capitoloName, source) {
        var res = '';
        var movgest = source[movgestName];
        var sub = source[subName];
        var capitolo = source[capitoloName];

        if(capitolo) {
            res += (capitolo.bilancio ? capitolo.bilancio.anno : capitolo.annoCapitolo) + '/';
        }
        if(movgest) {
            res += movgest.annoMovimento + '/' + movgest.numero;
        }
        if(sub) {
            res += '-' + sub.numero;
        }
        return res;
    }

    /**
     * Formattazione della valuta
     * @param value (any) il valore da formattare
     * @return (string) il valore formattato
     */
    function formatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return '';
    }
    /**
     * Legge i dati da possibili valori distinti
     * @param possibleFields      (string[]) i possibili campi
     * @param fieldToRead         (string) il campo da leggere
     * @param defaultValueToApply (any) il valore di default
     * @param fncToApply          (function) la funzione da applicare
     * @return (function) al function di lettura del dato
     */
    function readData(possibleFields, fieldToRead, defaultValueToApply, fncToApply) {
        var fnc = fncToApply || passThrough;
        var defaultValue = defaultValueToApply || '';
        return function(source) {
            var el = possibleFields.reduce(function(acc, val) {
                return acc !== undefined ? acc : source[val];
            }, undefined);
            var field = el && el[fieldToRead] || defaultValue;
            return fnc(field);
        };
    }
    
    /**
     * Aggiunge il tab_Right al nodo
     * @param nTd (Node) il nodo
     * @return (Node) il nodo originale
     */
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
        return nTd;
    }

    /**
     * Impostazione delle causali di entrata.
     *
     * @param list (Array) la lista delle causali
     */
    function impostaCausaliEntrata (list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun accertamento associato"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('distinta.descrizione')},
                {aTargets: [1], mData: computeStringMovimentoGestione.bind(undefined, 'accertamento', 'subAccertamento', 'capitoloEntrataGestione')},
                {aTargets: [2], mData: readData(['subAccertamento', 'accertamento'], 'descrizione')},
                {aTargets: [3], mData: readData(['subAccertamento', 'accertamento'], 'importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [4], mData: readData(['subAccertamento', 'accertamento'], 'disponibilitaIncassare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        $("#tabellaMovimentiEntrata").dataTable(options);
    }

    /**
     * Impostazione delle causali di spesa.
     *
     * @param list (Array) la lista delle causali
     */
    function impostaCausaliSpesa (list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun impegno associato"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: computeStringMovimentoGestione.bind(undefined, 'impegno', 'subImpegno', 'capitoloUscitaGestione')},
                {aTargets: [1], mData: readData(['subImpegno', 'impegno'], 'descrizione')},
                {aTargets: [2], mData: readData(['subImpegno', 'impegno'], 'importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [3], mData: readData(['subImpegno', 'impegno'], 'disponibilitaPagare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        $("#tabellaMovimentiSpesa").dataTable(options);
    }

    /**
     * Caricamento delle causali.
     */
    function caricaCausali() {
        $.postJSON("aggiornaTipoOnere_ottieniListaCausali.do", {})
        .then(function(data) {
            impostaCausaliSpesa(data.listaCausaleSpesa || []);
            impostaCausaliEntrata(data.listaCausaleEntrata || []);
        });
    }

    $(function() {
    	cambioSplitReverse();
        // Caricamento causali
        caricaCausali();
    });
}(jQuery);