/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    "use strict";

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Permette la visualizzazione del dettaglio quote.
     *
     * @param td  (Node)   il td della tabella
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param obj (Object) l'oggetto contenuto nella tabella
     */
    function dettaglioQuote(td, e, uid) {
        var tr = $(td).closest('tr').overlay('show');
        e.preventDefault();
        $.postJSON("risultatiRicercaTestataDocumentoEntrataDettaglioQuote.do", {uidDaConsultare : uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_QUOTA"))) {
                return;
            }
            popolaTabellaQuote();
            // SPAN dei totali
            $("#totaleQuote").html(data.totaleQuote.formatMoney());
            $("#modaleDettaglioQuote").modal("show");
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Caricamento via Ajax della tabella delle quote
     */
    function popolaTabellaQuote() {
        var isOverlay = false;
        var table = $("#tabellaDettaglioQuoteDocumentoEntrata").overlay({rebind: true, loader: true, usePosition: true});
        var opts = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            bServerSide: true,
            sAjaxSource: "risultatiRicercaSinteticaQuoteDocumentoEntrataAjax.do",
            sServerMethod: "POST",
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti quote associate",
                oPaginate : {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: defaultPerDataTable('movimento')},
                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
                {aTargets: [3], mData: defaultPerDataTable('ordinativo')},
                {aTargets: [4], mData: defaultPerDataTable('provvisorio')},
                {aTargets: [5], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }}
            ]
        };

        table.dataTable(opts);
    }

    /**
     * Caricamento via Ajax della tabella dei documenti e visualizzazione.
     */
    function visualizzaTabellaDocumenti() {
        var options = {
            // Configurazione per il processing server-side dei dati
            bServerSide: true,
            // Sorgente AJAX dei dati
            sAjaxSource: "risultatiRicercaTestataDocumentoEntrataAjax.do",
            // Metodo HTTP per la chiamata AJAX
            sServerMethod: "POST",
            // Gestione della paginazione
            bPaginate: true,
            // Impostazione del numero di righe
            bLengthChange: false,
            // Numero base di righe
            iDisplayLength: 10,
            // Sorting delle colonne
            bSort: false,
            // Display delle informazioni
            bInfo: true,
            // Calcolo automatico della larghezza delle colonne
            bAutoWidth: true,
            // Filtro dei dati
            bFilter: false,
            // Abilita la visualizzazione di 'Processing'
            bProcessing: true,
            // Internazionalizzazione
            oLanguage: {
                // Informazioni su quanto mostrato nella pagina
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                sInfoEmpty: "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                sProcessing: "Attendere prego...",
                // Testo quando non vi sono dati
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                // Definizione del linguaggio per la paginazione
                oPaginate: {
                    // Link alla prima pagina
                    sFirst: "inizio",
                    // Link all'ultima pagina
                    sLast: "fine",
                    // Link alla pagina successiva
                    sNext: "succ.",
                    // Link alla pagina precedente
                    sPrevious: "prec.",
                    // Quando la tabella è vuota
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "documento"},
                {aTargets: [1], mData: "data"},
                {
                    aTargets: [2],
                    mData: function(source) {
                        return source.statoOperativoDocumentoCode + " / "+ source.statoOperativoDocumentoDesc;
                    }
                },
                {aTargets: [3], mData: "soggetto"},
                {
                    aTargets: [4],
                    mData: function(source) {
                        return source.importo.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets: [ 5 ],
                    mData: "azioni",
                    fnCreatedCell: function (nTd, sData, oData) {
                        $(nTd).find("a[href='#msgAnnulla']")
                                .off("click")
                                .on("click", function() {
                                    clickOnAnnulla(oData.uid);
                                })
                                .end()
                            .find("a.dettaglioQuoteDocumento")
                                .off("click")
                                .on("click", function(e) {
                                    dettaglioQuote(nTd, e, oData.uid);
                                })
                                .end();
                        $('.dropdown-toggle', nTd).dropdown();
                    }
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }
        $("#risultatiRicercaDocumento").dataTable(options);
    }

    $(function() {
        // Carico la tabella
        visualizzaTabellaDocumenti();
    });
}(jQuery));