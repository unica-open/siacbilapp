/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ***************************
 **** Risultati Ricerca ****
 ***************************
 */

/** Container per le funzioni dei Risultati di Ricerca */
var RisultatiRicerca = (function() {
    var exports = {};
    var zero = 0;

    /**
     * Imposta l'uid del capitolo da annullare.
     *
     * @param uid {Number} uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Imposta l'uid del capitolo da eliminare.
     *
     * @param uid {Number} uid da impostare
     */
    function clickOnElimina(uid) {
        $("#HIDDEN_UidDaEliminare").val(uid);
    }

    /**
     * Aggiornamento del totale degli stanziamenti.
     * 
     * @param json (Object) il JSON della risposta
     * @returns (Object) il JSON della risposta
     */
    function updateTotaleStanziamenti(json) {
        return json;
    }

    /**
     * Wrapper per l'invocazione del DataTables. Riunisce le opzioni comuni utilizzate.
     *
     * @param sAjaxSource  {String}  l'URL della sorgente AJAX dei dati (comprende il suffisso)
     * @param actionName   {String}  il nome dell'action verso cui redirigere
     * @param startBlocco  {Number}  il valore che corrisponde al numero del blocco da visualizzare nella paginazione
     * @param tabella      {String}  l'id della tabella da popolare
     * @param updateTotali {Boolean} se aggiornare i totali (optional - default: false)
     *
     * @returns {Object} la DataTable creata
     */
    exports.wrapperDataTable = function (sAjaxSource, actionName, startPos, tabella, updateTotali) {

        // Controlla che siano inseriti i dati
        if ($.type(sAjaxSource) === "undefined") {
            // Se i dati non sono inseriti, si lanci un'eccezione
            throw new Error("Dati inseriti non conformi a quelli attesi");
        }

        // Valore di default per la tabella
        if(tabella === undefined) {
            tabella = "risultatiricerca";
        }

        // Le opzioni base del dataTable
        var optionsBase = {
            // Gestione della paginazione
            bPaginate: true,
            // Impostazione del numero di righe
            bLengthChange: false,
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
            // Configurazione per il processing server-side dei dati
            bServerSide: true,
            // Sorgente AJAX dei dati
            sAjaxSource: sAjaxSource,
            // Metodo HTTP per la chiamata AJAX
            sServerMethod: "POST",

            // Internazionalizzazione
            oLanguage: {
                // Informazioni su quanto mostrato nella pagina
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella e' vuota
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
                    // Quando la tabella e' vuota
                    sEmptyTable: "Nessun dato disponibile"
                }
            },

            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
                $('a[rel="popover"]', '#' + tabella).popover();
            },

            fnServerData : function(sSource, aoData, fnCallback){
                var fncArray = [fnCallback];
                if(updateTotali) {
                    fncArray.unshift(updateTotaleStanziamenti);
                }
                $.ajax({
                    dataType: 'json',
                    type: 'GET',
                    url: sSource,
                    data: aoData,
                    success: fncArray
                });
            },

            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: function (source) {
                    return "<a rel='popover' href='#' data-original-title='Descrizione' data-trigger='hover' data-content='" + escapeHtml(source.descrizione) + "'>" + source.capitolo + "</a>";
                }},
                {aTargets: [1], mData: "stato"},
                {aTargets: [2], mData: "classificazione"},
                {aTargets: [3], mData: function(source) {
                    return (source.stanziamentoCompetenza || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [4], mData: function(source) {
                    return (source.stanziamentoResiduo || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [5], mData: function (source) {
                    return (source.stanziamentoCassa || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [6], mData: "struttAmmResp"},
                {aTargets: [7], mData: function (source) {
                    return "<a rel='popover' href='#' data-original-title='Voce' data-trigger='hover' data-placement='left' data-content='" + escapeHtml(source.pdcVoce) + "'>" + source.pdcFinanziario + "</a>";
                }},
                {aTargets: [8], mData: "azioni", fnCreatedCell: function (nTd, sData, oData) {
                    $(nTd).find("a[href='#msgAnnulla']")
                            .substituteHandler("click", function() {
                                clickOnAnnulla(oData.uid);
                            })
                            .end()
                        .find("a[href='#msgElimina']")
                            .substituteHandler("click", function() {
                                clickOnElimina(oData.uid);
                            })
                            .end()
                        .find(".dropdown-toggle")
                            .dropdown();
                }}
            ]
        };

        // Per i rientri da annulla e cancella e' necessario riposizionarsi nella pagina giusta del blocco
        if($.type(startPos) === "string") {
            var startPosition = parseInt(startPos, 10);
            $.extend(true, optionsBase, {iDisplayStart: startPosition});
        }

        // Chiamata alla funzione dataTable per la generazione della tabella
        return $('#' + tabella).dataTable(optionsBase);
    };

    /**
     * Wrapper per l'invocazione del DataTables nel caso massivo. Riunisce le opzioni comuni utilizzate.
     *
     * @param sAjaxSource {String} l'URL della sorgente AJAX dei dati (comprende il suffisso)
     * @param actionName  {String} il nome dell'action verso cui redirigere
     * @param startPos    {number} il valore che corrisponde al numero del blocco da visualizzare nella paginazione
     * @param tabella     {String} l'id della tabella da impostare come dataTable
     *
     * @returns {Object} la DataTable creata
     */
    exports.wrapperMassivoDataTable = function (sAjaxSource, actionName, startPos, tabella) {

        // Controlla che siano inseriti i dati
        if ($.type(sAjaxSource) === "undefined" || $.type(actionName) === "undefined") {
            // Se i dati non sono inseriti, si lanci un'eccezione
            throw new Error("Dati inseriti non conformi a quelli attesi");
        }

        // Imposto il valore di default per la tabella
        if(tabella === undefined) {
            tabella = "risultatiricerca";
        }

        // Le opzioni base del dataTable
        var optionsBase = {
            // Gestione della paginazione
            bPaginate: true,
            // Impostazione del numero di righe
            bLengthChange: false,
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
            // Configurazione per il processing server-side dei dati
            bServerSide: true,
            // Sorgente AJAX dei dati
            sAjaxSource: sAjaxSource,
            // Metodo HTTP per la chiamata AJAX
            sServerMethod: "POST",

            // Internazionalizzazione
            oLanguage: {
                // Informazioni su quanto mostrato nella pagina
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella e' vuota
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
                    // Quando la tabella e' vuota
                    sEmptyTable: "Nessun dato disponibile"
                }
            },

            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
                $('a[rel="popover"]', '#' + tabella).popover();
            },

            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: function (source) {
                    return "<a rel='popover' href='#' data-original-title='Descrizione' data-trigger='hover' data-content='" + escapeHtml(source.descrizione) + "'>" + source.capitolo + "</a>";
                }},
                {aTargets: [1], mData: "classificazione"},
                {aTargets: [2], mData: function(source) {
                    return (source.stanziamentoCompetenza || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [3], mData: function(source) {
                    return (source.stanziamentoResiduo || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [4], mData: function(source) {
                    return (source.stanziamentoCassa || zero).formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [5], mData: "azioni", fnCreatedCell: function(nTd) {
                    $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };

        // Per i rientri da annulla e cancella e' necessario riposizionarsi nella pagina giusta del blocco
        if($.type(startPos) === "string") {
            var startPosition = parseInt(startPos, 10);
            $.extend(true, optionsBase, {iDisplayStart: startPosition});
        }

        // Chiamata alla funzione dataTable per la generazione della tabella
        return $('#' + tabella).dataTable(optionsBase);
    };

    return exports;

}());