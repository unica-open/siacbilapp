/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
    "use strict";
    var exports = {};
    var NotaCreditoEntrata;
    var alertErroriModale = $("#ERRORI_modaleRicercaNCV");
    var tabellaRisultati = $("#tabellaRisultatiRicercaEntrataNCV");


    var baseOpts = {
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
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
                sEmptyTable: "Nessun elemento disponibile"
            }
        }
    };
    /**
     * Impostazione della tabella con i conti figli ottenuti dalla ricerca.
     */
    function impostaTabellaRisultati() {
        var tableId = "#tabellaRisultatiRicercaEntrataNCV";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDocumentoEntrataAjax.do",
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div").show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Conti trovati") : ("1 Conto trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            },
            aoColumnDefs: [
               {aTargets : [ 0 ], mData : function() {
                   return "<input type='radio' name='checkDocumento'/>";
               }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                   $("input", nTd).data("originalDatiDocumento", oData);
               }},
               {aTargets: [1], mData: function(source) {
                   return source.documento || "";
               }},
               {aTargets: [2], mData: function(source) {
                   return source.data || "";
               }},
               {aTargets: [3], mData: function(source) {
                   var statoOperativoDocumentoCode = source.statoOperativoDocumentoCode + " / " || "";
                   var statoOperativoDocumento = source.documentoEntrata && source.documentoEntrata.statoOperativoDocumento || "";
                   return statoOperativoDocumentoCode + statoOperativoDocumento.descrizione;
               }},
               {aTargets: [4], mData: function(source) {
                   var imp = source.importo || 0;
                   return imp.formatMoney();
               }, fnCreatedCell: function(nTd) {
                   $(nTd).addClass("tab_Right");
               }}
           ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        if($.fn.DataTable.fnIsDataTable(tabellaRisultati[0])) {
            tabellaRisultati.dataTable().fnDestroy();
        }
        tabellaRisultati.dataTable(options);
    }

    function formatSN(flag) {
        return !!flag ? "S" : "N";
    }

    function popolaTabella(dataconto){
        impostaTabellaRisultati();
    }

    function ricalcolaDati (){
        $("#tabNoteCredito").overlay("show");
        $.postJSON("aggiornamentoDocumentoEntrata_ricalcolaListaDocumentiCollegati.do", function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
                }
            impostaDatiNegliAlert(data.informazioni, Documento.alertInformazioni);
            Documento.NoteCredito.caricaListaNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito);
        }).always(function() {
            $("#tabNoteCredito").overlay("hide");
        });
    }

    /**
     * Costruttore per la nota di credito x entrata.
     */
    NotaCreditoEntrata = function() {
        // Campi della pagina

        // I campi del modale
        this.$fieldsetRicerca = $("#fieldsetModaleRicercaEntrataNCV");
        this.$annoModale = $("#annoNotaCreditoEsistente");
        this.$numeroModale = $("#numeroNotaCreditoEsistente");
        this.$dataModale = $("#dataNotaCreditoEsistente");
        this.$bottoneCercaEntrataNCVModale = $("#pulsanteCercaEntrataNCVModale");
        this.$spinnerRicerca = $("#SPINNER_pulsanteCercaEntrataNCVModale");
        this.$spinnerConferma = $("#SPINNER_pulsanteConfermaEntrataNCVModale");
        this.$pulsanteConfermaEntrataNCVModale = $("#pulsanteConfermaEntrataNCVModale");

        this.$divRisultati = $("#risultatiRicercaEntrataNCV");
        this.$tabellaRisultatiRicercaEntrataNCV = $("#tabellaRisultatiRicercaEntrataNCV");
        this.$modal = $("#comp-EntrataNotaCredito");
        this.$hiddenTipoDocUid = $("#HIDDEN_tipoDocumentoUIDNotaCreditoEsistente");
        this.$importoTotaleDoc = $("#importoTotaleDoc");

        this.init();
    };

    NotaCreditoEntrata.prototype.init = function() {
        var self = this;

        this.$bottoneCercaEntrataNCVModale.substituteHandler("click", $.proxy(self.ricercaListaNCV, self));
        this.$pulsanteConfermaEntrataNCVModale.substituteHandler("click", $.proxy(self.collegaNCV, self));
    };

    NotaCreditoEntrata.prototype.apriModale = function() {
        this.$fieldsetRicerca.find(":input").not("[data-maintain]").val("");
        //pulisco gli altri campi
        this.$annoModale.val("");
        this.$numeroModale.val("");
        this.$dataModale.val("");

        this.$divRisultati.slideUp();
        alertErroriModale.slideUp();
        this.$modal.modal("show");
    };

    /**
     * Richiama l'esecuzione della ricerca Note credito.
     *
     * @returns (NotaCreditoEntrata) l'oggetto su cui e' atata effettuata l'invocazione
     */
    NotaCreditoEntrata.prototype.ricercaListaNCV = function() {
        var self = this;
        var oggettoPerChiamataAjax = unqualify(self.$fieldsetRicerca.serializeObject(), 1);
        //rimappo i campi soggetto e tipo

        self.$divRisultati.slideUp();
        self.$spinnerRicerca.addClass("activated");
        // Chiudo l'alert
        alertErroriModale.slideUp();

        $.postJSON("ricercaDocumentoEntrataAjax.do", oggettoPerChiamataAjax, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }

            // Carico i dati in tabella
            impostaTabellaRisultati();
            self.$importoTotaleDoc.html((data.importoTotale).formatMoney());
            self.$divRisultati.slideDown();
        }).always(function() {
            // Disattivo lo spinner
            self.$spinnerRicerca.removeClass("activated");
        });

        return this;
    };

    /**
     * Imposta i dati della conto all'interno dei campi selezionati.
     *
     * @returns (Conto) l'oggetto su cui e' atata effettuata l'invocazione
     */
    NotaCreditoEntrata.prototype.collegaNCV = function() {
        var checkedDocumento= this.$tabellaRisultatiRicercaEntrataNCV.find("[name='checkDocumento']:checked");
        // I campi da popolare
        var documentoNCV;
        var self = this;

        // Se non ho selezionato nulla, esco subito
        if(checkedDocumento.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare una nota di credito"], alertErroriModale, false);
            return;
        }

        self.$spinnerConferma.addClass("activated");
        // Ottengo i dati del soggetto salvati
        documentoNCV = checkedDocumento.data("originalDatiDocumento");

        $.postJSON("aggiornamentoDocumentoEntrata_collegaNotaCreditoEsistente.do", {uidDocumentoDaCollegare : documentoNCV.uid}, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }
            Documento.NoteCredito.ricalcolaListaDocCollegati();
            self.$modal.modal("hide");
        }).always(function() {
             // Disattivo lo spinner
            self.$spinnerConferma.removeClass("activated");
        });
    };

    exports.inizializza = function(selectorPulsante) {
        var datiNotaCreditoEntrata = new NotaCreditoEntrata();
        $(selectorPulsante).click($.proxy(NotaCreditoEntrata.prototype.apriModale, datiNotaCreditoEntrata));
    };

    // Esportazione delle funzionalita'
    global.NotaCreditoEntrata = exports;

}(jQuery, this);