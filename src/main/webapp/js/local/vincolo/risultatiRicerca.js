/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Vincolo = (function () {
    'use strict';
    var exports = {};

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Caricamento via Ajax della tabella dei vincoli e visualizzazione.
     */
    exports.visualizzaTabellaVincoli = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaVincoloAjax.do",
            // Metodo HTTP per la chiamata AJAX
            "sServerMethod" : "POST",
            // Gestione della paginazione
            "bPaginate" : true,
            // Impostazione del numero di righe
            "bLengthChange" : false,
            // Numero base di righe
            "iDisplayLength" : 10,
            // Sorting delle colonne
            "bSort" : false,
            // Display delle informazioni
            "bInfo" : true,
            // Calcolo automatico della larghezza delle colonne
            "bAutoWidth" : true,
            // Filtro dei dati
            "bFilter" : false,
            // Abilita la visualizzazione di 'Processing'
            "bProcessing" : true,
            // Internazionalizzazione
            "oLanguage" : {
                // Informazioni su quanto mostrato nella pagina
                "sInfo" : "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                "sInfoEmpty" : "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                "sProcessing" : "Attendere prego...",
                // Testo quando non vi sono dati
                "sZeroRecords" : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                // Definizione del linguaggio per la paginazione
                "oPaginate" : {
                    // Link alla prima pagina
                    "sFirst" : "inizio",
                    // Link all'ultima pagina
                    "sLast" : "fine",
                    // Link alla pagina successiva
                    "sNext" : "succ.",
                    // Link alla pagina precedente
                    "sPrevious" : "prec.",
                    // Quando la tabella è vuota
                    "sEmptyTable" : "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            "fnDrawCallback" : function () {
                $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
            },
            "aoColumnDefs" : [
                {
                    "aTargets" : [ 0 ],
                    "mData" : function(source) {
                        return "<a rel='popover' href='#'>" + source.codice + "</a>";
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.descrizione,
                            "title" : "Descrizione",
                            "trigger" : "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).on("click", function(event) {
                            event.preventDefault();
                        }).popover(settings);
                    }
                },
                {"aTargets" : [ 1 ], "mData" : "bilancio"},
                {"aTargets" : [ 2 ], "mData" : "trasferimentiVincolati"},
                {"aTargets" : [ 3 ], "mData" : "numeroCapitoliEntrata"},
                {"aTargets" : [ 4 ], "mData" : "numeroCapitoliUscita"},
                {
                    "aTargets" : [ 5 ],
                    "mData" : "azioni",
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        $("a[href='#msgAnnulla']", nTd).off("click")
                            .on("click", function() {
                                clickOnAnnulla(oData.uid);
                            });
                        $('.dropdown-toggle', nTd).dropdown();
                    }
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#risultatiRicercaVincolo").dataTable(options);
    };

    return exports;
}());

$(
    function() {
        // Carico la dataTable
        Vincolo.visualizzaTabellaVincoli();
    }
);