/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErrori = $("#ERRORI");

    /**
     * Impostazione della tabella.
     */
    function impostaTabellaTipoOperazioniCassa() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaPrimaNotaLiberaFINAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                $("a[rel='popover']", tableId).popover();
                $("a.tooltip-test", tableId).tooltip();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "infoENumero", fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).click([oData, nTd], apriModaleDettagli);
                }},
                {aTargets: [1], mData: "dataRegistrazione"},
                {aTargets: [2], mData: "statoOperativo"},
                {aTargets: [3], mData: "numeroLibroGiornale"},
                {aTargets: [4], mData: "dataRegistrazioneDefinitiva"},
                {aTargets: [5], mData: "causaleEPWithTootip"},
                {aTargets: [6], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.validaPrimaNotaLibera")
                            .click([oData], validazionePrimaNotaLibera)
                            .end()
                        .find("a.aggiornaPrimaNotaLibera")
                            .click([oData], aggiornamentoPrimaNotaLibera)
                            .end()
                        .find("a.consultaPrimaNotaLibera")
                            .click([oData], consultazionePrimaNotaLibera)
                            .end()
                        .find("a.annullaPrimaNotaLibera")
                            .click([oData], annullamentoPrimaNotaLibera);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Validazione dellla prima nota libera.
     */
    function validazionePrimaNotaLibera(e){
        var primaNotaLibera = e.data[0];
        var href = $(this).data("href");
        $("#modaleValidazioneElementoSelezionato").html(primaNotaLibera.numero + " - " + primaNotaLibera.descrizione);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleValidazione").modal("show");
    }

    /**
     * Aggiornamento della prima nota libera.
     */
    function aggiornamentoPrimaNotaLibera(){
         var href = $(this).data("href");
         document.location = href;
    }

    /**
     * Consultazione della prima nota libera.
     */
    function consultazionePrimaNotaLibera(){
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Annullamento della prima nota libera.
     */
    function annullamentoPrimaNotaLibera(e){
        var primaNotaLibera = e.data[0];
        var href = $(this).data("href");
        $("#modaleAnnullamentoElementoSelezionato").html(primaNotaLibera.numero + " - " + primaNotaLibera.descrizione);
        $("#modaleAnnullamentoPulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleAnnullamentoPulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleAnnullamento").modal("show");
    }

    /**
     * Apertura del modale dei dettagli.
     */
    function apriModaleDettagli (e) {
        var primaNotaLibera = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");

        $.postJSON("dettaglioMovimentiPrimaNotaLiberaFINAction_caricaMovimenti.do", {"primaNotaLibera.uid": primaNotaLibera.uid}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaTabellaModale(data.listaMovimentoDettaglio);
            // Imposto i campi aggiuntivi
            $("#modaleDettaglioMovimentiSpan").html(primaNotaLibera.numero);
            $("#modaleDettaglioMovimentiDescrizione").html("Descrizione: " + primaNotaLibera.descrizione);
            $("#modaleDettaglioMovimentiTotaleDare").html(data.totaleDare.formatMoney());
            $("#modaleDettaglioMovimentiTotaleAvere").html(data.totaleAvere.formatMoney());

            $("#modaleDettaglioMovimenti").modal("show");
        }).always(function() {
            row.overlay("hide");
        });
    }

    /**
     * Impostazione della tabella della modale.
     *
     * @param list (Array) la lista da impostare
     *
     * @returns (jQuery) il wrapper della tabella impostata
     */
    function impostaTabellaModale(list) {
        var tableId = "#modaleDettaglioMovimentiTabella";
        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti movimenti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun movimento disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('conto.codice')},
                {aTargets: [1], mData: defaultPerDataTable('conto.descrizione')},
                {aTargets: [2], mData: extractCodiceDescrizione('missione')},
                {aTargets: [3], mData: extractCodiceDescrizione('programma')},
                {aTargets: [4], mData: function(source) {
                    return source.segno && "DARE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [5], mData: function(source) {
                    return source.segno && "AVERE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        return $(tableId).dataTable(opts);
    }
    
    function extractCodiceDescrizione(field) {
        return function(source) {
            return source[field] !== null
            && source[field] !== undefined
            && (source[field].codice + ' - ' + source[field].descrizione)
            || '';
        };
    }

    $(function() {
        // Inizializzazione del datatable
        impostaTabellaTipoOperazioniCassa();

    });
}(jQuery, this);