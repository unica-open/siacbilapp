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
    function impostaTabellaTipoOperazioniCassa() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaPrimaNotaIntegrataManualeGSAAjax.do",
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
                    var $nTd = $(nTd).addClass("tab_Right");
                    $nTd.find("a.validaPrimaNotaLibera")
                        .click([oData], validazionePrimaNota);
                    $nTd.find("a.aggiornaPrimaNotaLibera")
                        .click([oData], aggiornamentoPrimaNota);
                    $nTd.find("a.consultaPrimaNotaLibera")
                        .click([oData], consultazionePrimaNota);
                    $nTd.find("a.annullaPrimaNotaLibera")
                        .click([oData], annullamentoPrimaNota);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }

    /**
     * Validazione dellla prima nota.
     * @param e (Event) l'evento scatenante
     */
    function validazionePrimaNota(e){
        var primaNota = e.data[0];
        var href = $(this).data("href");
        $("#modaleValidazioneElementoSelezionato").html(primaNota.numero + " - " + primaNota.descrizione);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            // SIAC-5336
            var classificatoreGSA = $('#HIDDEN_classificatoreGSAUid').val();
            // SIAC-5853
            var dataRegistrazione = $('#dataRegistrazionePrimaNotaLibera').val();
            // Aggiungo l'uid specificato nell'URL
            href += '&classificatoreGSA.uid=' + encodeURIComponent(classificatoreGSA)
                + '&primaNotaLibera.dataRegistrazioneLibroGiornale=' + encodeURIComponent(dataRegistrazione);
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        // SIAC-5741
        GSAClassifZtree.cleanZTree(classifGSATreeId);
        $('#modaleValidazione').one('shown', GSAClassifZtree.closeClassificatoreGSACollapse);
        $("#modaleValidazione").modal("show");
    }

    /**
     * Aggiornamento della prima nota.
     */
    function aggiornamentoPrimaNota(){
         var href = $(this).data("href");
         document.location = href;
    }

    /**
     * Consultazione della prima nota.
     */
    function consultazionePrimaNota(){
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Annullamento della prima nota.
     * @param e (Event) l'evento scatenante
     */
    function annullamentoPrimaNota(e){
        var primaNota = e.data[0];
        var href = $(this).data("href");
        $("#modaleAnnullamentoElementoSelezionato").html(primaNota.numero + " - " + primaNota.descrizione);
        $("#modaleAnnullamentoPulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleAnnullamentoPulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleAnnullamento").modal("show");
    }

    /**
     * Apertura del modale dei dettagli.
     * @param e (Event) l'evento scatenante
     */
    function apriModaleDettagli (e) {
        var primaNota = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");

        $.postJSON("dettaglioMovimentiPrimaNotaIntegrataManualeGSAAction_caricaMovimenti.do", {"primaNotaLibera.uid": primaNota.uid}, function(data) {
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
        }).always(row.overlay.bind(row, "hide"));
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
                {aTargets: [4], mData: defaultPerDataTable('importoDare', 0, doFormatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('importoAvere', 0, doFormatMoney), fnCreatedCell: tabRight}
            ]
        };
        return $(tableId).dataTable(opts);
    }
    
    /**
     * Formattazione del dato in valuta
     * @param val (any) il valore da formattare
     * @returns (any) il valore formattato
     */
    function doFormatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return val;
    }
    
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
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
        // SIAC-5336
        // SIAC-5741
        classifGSATreeId = GSAClassifZtree.initClassificatoreGSAZtree();

    });
}(jQuery, this);