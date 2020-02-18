/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";
    var checkedData = {};
    var hasSubmit = $('button[data-submit]').length !== 0;
    var importoDaRegolarizzare;
    
    /**
     * Effettua l'escape del doppio apice
     * @param str (string) la stringa per cui effettuare l'escape
     * @returns (string) la stringa con l'escape corretto
     */
    function escapeQuote(str) {
        if(str === null || str === undefined) {
            return '';
        }
        return str.replace(/"/, "'");
    }

    /**
     * Popola la tabella.
     *
     * @param url               (String)  l'URL di invocazione
     * @param tableSelector     (String)  il selettore CSS della tabella
     */
    function populateTable(url) {
        var zeroRecords = "Non sono presenti quote corrispondenti ai parametri inseriti";
        var table = $("#tabellaSubdocumenti");
        var opts = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaQuoteEntrataDaAssociareAProvvisorioAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 50,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            sScrollY: "300px",
            bScrollCollapse: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: zeroRecords,
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#tabellaSubdocumenti_processing").show();
            },
            fnDrawCallback: function () {
                // Attivo i popover
                table.find("a[rel='popover']")
                    .popover()
                    .eventPreventDefault("click");
                $("#tabellaSubdocumenti_processing").hide();
                // SIAC-6060: inizializzo il calcolo dei totali
                ricalcolaTotali();
                $(".check-all").prop('checked', false);
            },
            
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var str = '';
                    if(!hasSubmit) {
                        return str;
                    }
                    str = "<input type='checkbox' name='listaUidSubdocumenti' value='" + source.uid + "'";
                    if(checkedData[source.uid] && checkedData[source.uid].checked === true) {
                        str += ' checked';
                    }
                    return str + "/>";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalSubdocumento", oData);
                }},
                {aTargets: [1], mData: function(source) {
                    return '<a href="#" rel="popover" data-original-title="Descrizione" data-content="' + escapeQuote(source.descrizioneDocumentoPadre) + '" data-trigger="hover">' + source.documentoPadre + '</a>';
                }},
                {aTargets: [2], mData: function(source) {
                    return source.dataEmissione ? formatDate(source.dataEmissione) : "";
                }},
                {aTargets: [3], mData: function(source) {
                    return '<a href="#" rel="popover" data-original-title="Stato" data-content="' + escapeQuote(source.descrizioneStato) + '" data-trigger="hover">' + source.codiceStato + "</a>";
                }},
                {aTargets: [4], mData: "soggetto"},
                {aTargets: [5], mData: "numero"},
                {aTargets: [6], mData: "movimento"},
                {aTargets: [7], mData: function(source) {
                    return '<a href="#" rel="popover" data-original-title="Dettagli" data-content="importo IVA: ' + source.importoIvaSplitReverse.formatMoney() + '" data-trigger="hover">' +  source.ivaSplitReverse + "</a>";
                }},
                {aTargets: [8], mData: function(source) {
                    return '<a href="#" rel="popover" data-original-title="Dettagli" data-content="' + escapeQuote(source.dettagliNote) + '" data-trigger="hover">' + source.note + "</a>";
                }},
                {aTargets: [9], mData: function(source) {
                    return source.provvisorioCassa;
                }},
                {aTargets: [10], mData: function(source) {
                    return (source.importo - source.importoDaDedurre).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        table.dataTable(opts);
    }

    /**
     * Seleziona tutti i checkbox nella tabella contente il pulsante chiamante.
     *
     * @returns i checkbox su cui si &eacute; agito.
     */
    function checkAllInTable() {
        var $this = $(this);
        var tableId = "#tabellaSubdocumenti";
        var isChecked = $this.prop("checked");
        // Seleziono tutti i checkboxes e li rendo come il globale
        $(tableId)
            .find("tbody input[type='checkbox']")
            .prop("checked", isChecked)
            .each(function(idx, el) {
                registerCheckbox.bind(el)(undefined);
            });
        ricalcolaTotali();
    }
    
    /**
     * Registrazione del checkbox
     * @param e (Event) l'evento scatenante
     */
    function registerCheckbox(e) {
        var $this = $(this);
        var subdoc = $this.data("originalSubdocumento");
        checkedData[subdoc.uid] = {checked: $this.prop('checked'), subdoc: subdoc};
        if(e !== undefined) {
            ricalcolaTotali();
        }
    }
    
    /**
     * Elimina i dati non checkati
     * @param el (any) l'elemento da controllare
     */
    function filterOutNotChecked(el) {
        return el !== undefined && el !== null && el.checked === true;
    }
    
    /**
     * Submit del form
     */
    function submitForm() {
        var str = '<input type="hidden" name="totaleSubdocumentiEntrata" value="' + $('#totaleSubdocumentiEntrata').val() + '"/>';
        str = Object
            .keys(checkedData)
            .reduce(function(acc, uid) {
                return acc + '<input type="checkbox" name="listaUidSubdocumenti" value="' + uid + '" checked/>';
            }, str);
        
        $('#formAssociaDocumentoProvvisorioStep2_hidden')
            .append(str)
            .submit();
    }

    /**
     * Ricalcolo dei totali.
     */
    function ricalcolaTotali() {
        // SIAC-6060: utilizzo il BigNumber per evitare problemi di arrotondamento
        var totaleEntrata = new BigNumber(0);
        Object
            .keys(checkedData)
            .map(function(key) {
                return checkedData[key];
            })
            .filter(filterOutNotChecked)
            .forEach(function(el) {
                var importo = new BigNumber(el.subdoc.importo);
                var importoDaDedurre = new BigNumber(el.subdoc.importoDaDedurre);
                totaleEntrata = totaleEntrata.plus(importo).sub(importoDaDedurre);
            });
        
        $("#spanTotaleSubdocumentiEntrata").html(formatMoney(totaleEntrata));
        // SIAC-6060: verificare se sia possibile scrivere il dato in maniera differente
        $("#totaleSubdocumentiEntrata").val(totaleEntrata.toNumber());
        
        // SIAC-6077
        $('#spanImportoDaRegolarizzare').html(formatMoney(importoDaRegolarizzare.sub(totaleEntrata)));
    }

    $(function() {
        var daRegolarizzareString = $('#HIDDEN_importoDaRegolarizzare').val()
            .replace(/\./g, '')
            .replace(/,/g, '.');
        
        // Creo la funzionalita' di selezione multipla
        $(".check-all").change(checkAllInTable);
        // Lego l'aggiornamento degli importi
        $(document).on("change", "tbody input[type='checkbox']", registerCheckbox);
        $('button[data-submit]').substituteHandler('click', submitForm);
        // Popolo le due tabelle
        populateTable();
        
        // SIAC-6077
        importoDaRegolarizzare = new BigNumber(daRegolarizzareString);
    });
}(jQuery));