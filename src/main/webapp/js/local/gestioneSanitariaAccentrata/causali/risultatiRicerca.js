/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, global) {
    "use strict";

    /**
     * Impostazione della tabella.
     */
    function impostaTabellaTipoOperazioniCassa() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaCausaleEPGSAAjax.do",
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
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "tipo"},
                {aTargets: [1], mData: "codice"},
                {aTargets: [2], mData: "descrizione"},
                {aTargets: [3], mData: "conti"},
                {aTargets: [4], mData: "stato"},
                {aTargets: [5], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.validaCausaleEP")
                            .click([oData], validazioneCausaleEP)
                            .end()
                        .find("a.annullaCausaleEP")
                            .click([oData], annullamentoCausaleEP)
                            .end()
                        .find("a.eliminaCausaleEP")
                            .click([oData], eliminazioneCausaleEP)
                            .end()
                        .find("a.aggiornaCausaleEP")
                            .click([oData], aggiornamentoCausaleEP)
                            .end()
                        .find("a.consultaCausaleEP")
                            .click([oData], consultazioneCausaleEP);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Validazione della causale EP.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function validazioneCausaleEP(e) {
        var causale = e.data[0];
        var href = $(this).data("href");
        $("#modaleValidazioneElementoSelezionato").html(causale.codice + " - " + causale.descrizione);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleValidazione").modal("show");
    }

    /**
     * Annullamento della causale EP.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function annullamentoCausaleEP(e) {
        var causale = e.data[0];
        var href = $(this).data("href");
        $("#modaleAnnullamentoElementoSelezionato").html(causale.codice + " - " + causale.descrizione);
        $("#modaleAnnullamentoPulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleAnnullamentoPulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleAnnullamento").modal("show");
    }

    /**
     * Eliminazione della causale EP.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminazioneCausaleEP(e) {
        var causale = e.data[0];
        var href = $(this).data("href");
        $("#modaleEliminazioneElementoSelezionato").html(causale.codice + " - " + causale.descrizione);
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleEliminazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleEliminazione").modal("show");
    }

    /**
     * Aggiornamento della causale EP.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornamentoCausaleEP(e) {
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Consultazione della causale EP.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function consultazioneCausaleEP(e) {
        var href = $(this).data("href");
        document.location = href;
    }

    $(function() {
        // Inizializzazione del datatable
        impostaTabellaTipoOperazioniCassa();
    });

}(jQuery, this);