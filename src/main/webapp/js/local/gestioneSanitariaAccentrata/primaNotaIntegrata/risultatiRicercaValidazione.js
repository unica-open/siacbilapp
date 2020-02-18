/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var classifGSATreeId;

    /**
     * Impostazione della tabella.
     */
    function impostaTabellaPrimaNota() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaValidazionePrimaNotaIntegrataGSAAjax.do",
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
                var testoConferma = (records === 0 || records > 1) ? (records + " elementi") : ("1 elemento");
                $('#num_result').html(testo);
                $("#spanDatiModaleConfermaValidazioneMassiva").html(testoConferma);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                $("a[rel='popover']", tableId).popover();
                $("a.tooltip-test", tableId).tooltip();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "numeroMovimento"},
                {aTargets: [1], mData: "infoENumero", fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).click([oData, nTd], apriModaleDettagli);
                }},
                {aTargets: [2], mData: "descrizione"},
                {aTargets: [3], mData: "stato"},
                {aTargets: [4], mData: "numeroLibroGiornale"},
                {aTargets: [5], mData: "dataRegistrazioneDefinitiva"},
                {aTargets: [6], mData: "causaleEPWithPopover"}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Apertura del modale dei dettagli.
     */
    function apriModaleDettagli (e) {
        var primaNota = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");

        $.postJSON("dettaglioMovimentiPrimaNotaIntegrataAction_caricaMovimenti.do", {"primaNota.uid": primaNota.uid}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaTabellaModale(data.listaMovimentoDettaglio);
            // Imposto i campi aggiuntivi
            $("#modaleDettaglioMovimentiSpan").html(primaNota.numero);
            $("#modaleDettaglioMovimentiDescrizione").html("Descrizione: " + primaNota.descrizione);
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
                {aTargets: [0], mData: function(source) {
                    return source.conto && source.conto.codice || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.conto && source.conto.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.segno && "DARE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [3], mData: function(source) {
                    return source.segno && "AVERE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        return $(tableId).dataTable(opts);
    }

    /**
     * Validazione massiva della prima nota.
     */
    function effettuaValidazioneMassiva(){
        // SIAC-5336
        var classificatoreGSA = $('#HIDDEN_classificatoreGSAUid').val();
        // SIAC-5853
        var dataRegistrazioneLibroGiornale = $('#dataRegistrazioneLibroGiornale').val();
        $('#invio_classificatoreGSA').val(classificatoreGSA);
        $('#invio_dataRegistrazioneLibroGiornale').val(dataRegistrazioneLibroGiornale);
        $("#SPINNER_confermaModaleConfermaValidazioneMassiva").addClass("activated");
        // Invio il form
        $('#formInvio').submit();
    }
    
    /**
     * Apertura del modale di conferma validazione
     */
    function openModaleConfermaValidazione() {
        // SIAC-5741
        GSAClassifZtree.cleanZTree(classifGSATreeId);
        $('#modaleConfermaValidazioneMassiva').one('shown', GSAClassifZtree.closeClassificatoreGSACollapse);
        $('#modaleConfermaValidazioneMassiva').modal('show');
    }

    $(function() {
        // Lego l'azione di validazione al pulsante
        $("#confermaModaleConfermaValidazioneMassiva").substituteHandler("click", effettuaValidazioneMassiva);
        // Inizializzazione del datatable
        impostaTabellaPrimaNota();
        // SIAC-5336
        // SIAC-5741
        classifGSATreeId = GSAClassifZtree.initClassificatoreGSAZtree();
        $('#buttonValidaTutto').substituteHandler('click', openModaleConfermaValidazione);

    });
}(jQuery, this);