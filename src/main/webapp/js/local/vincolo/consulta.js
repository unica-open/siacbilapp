/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Vincolo = (function(){
    'use strict';
    var exports = {};

    /**
     * Caricamento via Ajax della tabella dei vincoli e visualizzazione.
     *
     * @param idTabella {String} l'id della tabella
     */
    exports.visualizzaTabellaCapitoli = function(idTabella) {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : false,
            // Gestione della paginazione
            "bPaginate" : true,
            // Impostazione del numero di righe
            "bLengthChange" : false,
            // Numero base di righe
            "iDisplayLength" : 5,
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
                "sInfo" : "_START_ - _END_ di _MAX_ capitoli",
                // Informazioni per quando la tabella è vuota
                "sInfoEmpty" : "0 capitoli",
                // Testo mostrato quando la tabella sta processando i dati
                "sProcessing" : "Attendere prego...",
                // Testo quando non vi sono dati
                "sZeroRecords" : "Non sono presenti capitoli associati",
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
            }
        };

        $("#" + idTabella).dataTable(options);
        $("#" + idTabella + "_wrapper").children(":first").hide();
    };

    return exports;
}());

$(
    function() {
        // Carico le dataTable
        Vincolo.visualizzaTabellaCapitoli("tabellaCapitoliEntrata");
        Vincolo.visualizzaTabellaCapitoli("tabellaCapitoliSpesa");
    }
);