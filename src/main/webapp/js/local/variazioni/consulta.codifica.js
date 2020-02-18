/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ************************************
 **** Funzioni di ricerca Variazioni ****
 ************************************
 */


var ConsultaVariazioni = (function() {
    var exports = {};

    /** Opzioni base per il dataTable capitoli */
    exports.dataTableBaseOptions = {
        // Gestione della paginazione
        bPaginate : true,
        // Impostazione del numero di righe
        bLengthChange : false,
        // Numero base di righe
        iDisplayLength : 5,
        // Sorting delle colonne
        bSort : false,
        // Display delle informazioni
        bInfo : true,
        // Calcolo automatico della larghezza delle colonne
        bAutoWidth : true,
        // Filtro dei dati
        bFilter : false,
        // Abilita la visualizzazione di 'Processing'
        bProcessing : true,
        // Internazionalizzazione
        oLanguage : {
            // Informazioni su quanto mostrato nella pagina
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            // Informazioni per quando la tabella è vuota
            sInfoEmpty : "0 risultati",
            // Testo mostrato quando la tabella sta processando i dati
            sProcessing : "Attendere prego...",
            // Testo quando non vi sono dati
            sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
            // Definizione del linguaggio per la paginazione
            oPaginate : {
                // Link alla prima pagina
                sFirst : "inizio",
                // Link all'ultima pagina
                sLast : "fine",
                // Link alla pagina successiva
                sNext : "succ.",
                // Link alla pagina precedente
                sPrevious : "prec.",
                // Quando la tabella è vuota
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    return exports;
}());

/* Document Ready */

$(function() {
    var optionsBase = {
        // Chiamata al termine della creazione della tabella
        "fnDrawCallback" : function () {
            $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
        }
    };
    var options = $.extend(true, {}, ConsultaVariazioni.dataTableBaseOptions, optionsBase);

    $("#elencoCapitoli").dataTable(options);

});