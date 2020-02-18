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
            sAjaxSource : "risultatiRicercaPrimaNotaLiberaINVAjax.do",
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
                {aTargets: [0], mData: defaultPerDataTable("evento")},
                {aTargets: [1], mData: defaultPerDataTable("statoOperativo")},
                {aTargets: [2], mData: defaultPerDataTable("primaNotaProvvisoria")},
                {aTargets: [3], mData: defaultPerDataTable("primaNotaDefinitiva")},
                
                {aTargets: [4], mData: defaultPerDataTable("infoCespite"), fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).click([oData, nTd], apriModaleDettagliCespiti);
                }},
                
                {aTargets: [5], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                    	.find("a.validaPrimaNotaLibera")
                            .click([oData], validazionePrimaNotaLibera)
                            .end()
                        .find("a.consultaPrimaNotaLibera")
                            .click([oData], consultazionePrimaNotaLibera)
                            .end()
                        .find("a.rifiutaPrimaNotaLibera")
                            .click([oData], rifiutoPrimaNotaLibera)
                            .end()
                        .find("a.annullaPrimaNotaLibera")
                            .click([oData], annullamentoPrimaNotaLibera);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Apertura del modale dei dettagli.
     */
    function apriModaleDettagliCespiti (e) {
        var primaNota = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");
        e.preventDefault();
        $.postJSON("dettaglioCespitePrimaNotaLiberaAction_caricaCespitiDaPrimaNota.do", {"primaNota.uid": primaNota.uid,"tipoEvento.codice": 'INV_COGE'})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaTabellaModaleCespite(data.listaCespite);

            $("#modaleDettaglioCespiti").modal("show");
        }).always(row.overlay.bind(row, "hide"));
    }
    
    
    /**
     * Impostazione della tabella della modale.
     *
     * @param list (Array) la lista da impostare
     *
     * @returns (jQuery) il wrapper della tabella impostata
     */
    function impostaTabellaModaleCespite(list) {
        var tableId = "#modaleDettaglioCespitiTabella";
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
                sZeroRecords: "Non sono presenti cespiti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun cespite disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('codiceDescrizione')},
                {aTargets: [1], mData: defaultPerDataTable('tipoBene')},
                {aTargets: [2], mData: defaultPerDataTable('valoreIniziale')},
                {aTargets: [3], mData: defaultPerDataTable('valoreAttuale')},
                {aTargets: [4], mData: defaultPerDataTable('stato')},
            ]
        };
        
        return $(tableId).dataTable(opts);
    }
    
    
    
    /**
     * Validazione dellla prima nota libera.
     */
    function validazionePrimaNotaLibera(e){
        var primaNotaLibera = e.data[0];
        var href = $(this).data("href");
        $("#modaleValidazioneElementoSelezionato").html(primaNotaLibera.numero);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleValidazione").modal("show");
    }

    /**
     * Validazione dellla prima nota libera.
     */
    function rifiutoPrimaNotaLibera(e){
        var primaNotaLibera = e.data[0];
        var href = $(this).data("href");
        $("#modaleRifiutoElementoSelezionato").html(primaNotaLibera.numero) ;
        $("#modaleRifiutoPulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleRifiutoPulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleRifiuto").modal("show");
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

    $(function() {
        // Inizializzazione del datatable
        impostaTabellaTipoOperazioniCassa();

    });
}(jQuery, this);