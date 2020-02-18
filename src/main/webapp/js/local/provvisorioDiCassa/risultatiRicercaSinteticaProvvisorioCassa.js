/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';

    /**
     * Visualizzazione della tabella dei documenti.
     */
    function visualizzaTabellaProvvisori() {
        var table = $("#risultatiRicercaProvvisorioCassa");
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaSinteticaProvvisorioDiCassaAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: defaultPreDraw,
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $("#risultatiRicercaProvvisorioCassa_processing").parent("div").hide();
                // Attivo i popover
                table.find("a[rel='popover']").popover().eventPreventDefault("click");
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "<input type='checkbox' name='listaUidProvvisorio' value='" + source.uid + "'>";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalProvvisorio", oData);
                }},
                {aTargets: [1], mData: "tipo"},
                {aTargets: [2], mData: "numero"},
                {aTargets: [3], mData: "dataEmissione"},
                {aTargets: [4], mData: "dataAnnullamento"},
                {aTargets: [5], mData: "descrizioneVersante"},
                {aTargets: [6], mData: "descrizioneCausale"},
                {aTargets: [7], mData: "importo", fnCreatedCell: setTabRight},
                {aTargets: [8], mData: "importoDaRegolarizzare", fnCreatedCell: setTabRight},
                {aTargets: [9], mData: "importoDaEmettere", fnCreatedCell: setTabRight},
                {aTargets: [10], mData: "azioni",
                	fnCreatedCell: function (nTd, sData, oData) {
                    $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };
        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }
        table.dataTable(options);
    }
    
    /**
     * Seleziona tutti i checkbox nella tabella contente il pulsante chiamante.
     *
     * @returns i checkbox su cui si &eacute; agito.
     */
    function checkAllInTable() {
        var $this = $(this);
        var tableId = "#risultatiRicercaProvvisorioCassa";
        var isChecked = $this.prop("checked");
        // Seleziono tutti i checkboxes e li rendo come il globale
        return $(tableId).find("tbody")
            .find("input[type='checkbox']")
                .prop("checked", isChecked)
	        // Invoco il 'change' sul primo elemento della lista per ricalcolare i totali
	        .first()
	            .change()
	            .end();
    }
    
    /**
     * Ricalcolo il totale dei provvisori selezionati
     */
    function ricalcolaTotaleProvvisori() {
        var table = $("#risultatiRicercaProvvisorioCassa");
        var totaleProvvisori = 0;
        table.find("tbody").find("input[type='checkbox']")
            .filter(":checked")
                .each(function() {
                	totaleProvvisori = totaleProvvisori + $(this).data("originalProvvisorio").importoDaRegolarizzareNumeric;
                });
        $("#spanTotaleProvvisoriSelezionati").html(totaleProvvisori.formatMoney());
        $("#totaleProvvisoriSelezionati").val(totaleProvvisori);
    }

    /**
     * Imposta la classe tabRight sul nodo fornito
     * @param node (Node) il nodo a cui applicare la classe
     * @returns il nodo wrappato da jQuery
     */
    function setTabRight(node) {
        return $(node).addClass('tab_Right');
    }
    
    $(function() {
        $(".check-all").change(checkAllInTable);
        visualizzaTabellaProvvisori();
        ricalcolaTotaleProvvisori();
        $(document).on("change", "tbody input[type='checkbox']", ricalcolaTotaleProvvisori);
        
    });
})(jQuery);