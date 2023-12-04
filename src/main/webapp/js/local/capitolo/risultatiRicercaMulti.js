/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ***************************
 **** Risultati Ricerca ****
 ***************************
 */

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Gestione ad oggetti dello ztree. Permette una gestione generica di uno ztree tramite un suffisso opzionale 
 * che puo' essere o meno fornito
 * 
 * */
;(function (w, $) {
	"use strict";
    var exports = {};
	var zero = 0;
    
    var RisultatiRicercaCapitolo = function(cssSelectorTabella){
		this.cssSelectorTabella = cssSelectorTabella;
    };

    RisultatiRicercaCapitolo.prototype.constructor = RisultatiRicercaCapitolo;
    RisultatiRicercaCapitolo.prototype.inizializza = inizializza;
	RisultatiRicercaCapitolo.prototype.wrapperDataTable = wrapperDataTable;
    RisultatiRicercaCapitolo.prototype.destroy = destroy;

	function updateTotaleStanziamenti(json) {
        return json;
    }


	function wrapperDataTable (sAjaxSource, startPos, tabella, updateTotali) {

        // Controlla che siano inseriti i dati
        if ($.type(this.ajaxSource) === "undefined") {
            // Se i dati non sono inseriti, si lanci un'eccezione
            throw new Error("Dati inseriti non conformi a quelli attesi");
        }

        // Valore di default per la tabella
        if(this.cssSelectorTabella === undefined) {
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
			// Sorting delle colonne
            bDestroy: true,
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
            sAjaxSource: this.ajaxSource,
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
                $('a[rel="popover"]', this.cssSelectorTabella).popover();
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
        if($.type(this.startPos) === "string") {
            var startPosition = parseInt(this.startPos, 10);
            $.extend(true, optionsBase, {iDisplayStart: startPosition});
        }

        // Chiamata alla funzione dataTable per la generazione della tabella
        return $(this.cssSelectorTabella).dataTable(optionsBase);
    };

    /**
     * Imposta lo ZTree.
     *
     * @param jsonVariable  (Array)  la lista da impostare
     */
    function inizializza(ajaxSource, startPos) {
        this.ajaxSource = ajaxSource;
		this.startPos = startPos;
		this.wrapperDataTable();
    }
    
    function destroy(){
		var tabellaInDatatable = $($.fn.dataTable.fnTables(true)).filter(this.cssSelectorTabella);
		if(tabellaInDatatable.length >0){
			tabellaInDatatable.dataTable().fnClearTable();	
			tabellaInDatatable.dataTable().fnDestroy();
		}
    	 
         
    }
 	
	w.RisultatiRicercaCapitolo = RisultatiRicercaCapitolo;

}(this, $));
