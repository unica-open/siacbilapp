/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Progetto = (function () {
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
     * Gestione del click sul pulsante riattiva.
     *
     * @param uid l'uid da impostare
     */
    function clickOnRiattiva(uid) {
        $("#HIDDEN_UidDaRiattivare").val(uid);
    }

    /**
     * Caricamento via Ajax della tabella dei progetti e visualizzazione.
     */
    exports.visualizzaTabellaProgetti = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaProgettoAjax.do",
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
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
                {
                    "aTargets" : [ 0 ],
                    "mData" : function(source) {
                    	var tipoProgettoNotUndefined = source.codiceTipoProgetto? source.codiceTipoProgetto : "N.D."; 
                        return "<a rel='popover' href='#'>" + source.codice  + " - " + tipoProgettoNotUndefined + "</a>";
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.descrizione,
                            "title" : "Descrizione",
                            "trigger" : "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).off("click")
                            .on("click", function(event) {event.preventDefault();})
                            .popover(settings);
                    }
                },
                {"aTargets" : [ 1 ], "mData" : "statoOperativoProgetto"},
                {"aTargets" : [ 2 ], "mData" : "provvedimento"},
                {"aTargets" : [ 3 ], "mData" : "ambito"},
                {
                    "aTargets" : [ 4 ],
                    "mData" : "azioni",
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        $(nTd).find("a[href='#msgAnnulla']")
                                .off("click")
                                .on("click", function() {
                                    clickOnAnnulla(oData.uid);
                                })
                                .end()
                            .find("a[href='#msgRiattiva']")
                                .off("click")
                                .on("click", function() {
                                    clickOnRiattiva(oData.uid);
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

        $("#risultatiRicercaProgetto").dataTable(options);
    };

    return exports;
}());

$(
    function() {
        // Carico la dataTable
        Progetto.visualizzaTabellaProgetti();
    }
);