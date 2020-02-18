/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var exports = {};
    var DatiDocumentoSpesa;
    var alertErroriModale = $("#ERRORI_modaleRicercaDocumentoSpesa");

    var baseOpts = {
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 3,
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
                sEmptyTable: "Nessun documento disponibile"
            }
        }
    };


    /**
     * Caricamento della tabella delle fatture da visualizzare.
     *
     * @param table (jQuery) la tabella da popolare
     */
    function tabellaDatiDocumentoSpesa(table) {
        var options = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDocumentoSpesaAjax.do",
            aoColumnDefs: [
               {aTargets: [0], mData: function() {
                   return "<input type='radio' name='checkDocumento'/>";
               }, fnCreatedCell: function(nTd, sData, oData) {
                   $("input", nTd).data("originalDatiDocumento", oData);
               }},
                {aTargets: [1], mData: function(source) {
                    return source.documentoSpesa && source.documentoSpesa.tipoDocumento && source.documentoSpesa.tipoDocumento.descrizione || 0;
                }},
                {aTargets: [2], mData: function(source) {
                    return source.documentoSpesa && source.documentoSpesa.anno || "";
                }},
                {aTargets: [3], mData: function(source) {
                    return source.documentoSpesa && source.documentoSpesa.numero || "";
                }},
                {aTargets: [4], mData: function(source) {
                    return source.statoOperativoDocumentoDesc || "";
                }},
                {aTargets: [5], mData: function(source) {
                    return source.documentoSpesa && source.documentoSpesa.soggetto && (source.documentoSpesa.soggetto.codiceSoggetto + " - " + source.documentoSpesa.soggetto.denominazione) || "";
                }},
                {aTargets: [6], mData: function(source) {
                    var importo = source.documentoSpesa && source.documentoSpesa.importo || 0;
                    return importo.formatMoney();
                }},
                {aTargets: [7], mData: function(source) {
                	var importo = source.documentoSpesa && source.documentoSpesa.importoDaPagareNonPagatoInCassaEconomale || 0;
                	return importo.formatMoney();
                }}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        // DIsattivo eventualmente il datatable
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        table.dataTable(opts);
    }

    /**
     * Caricamento della tabella delle quote da visualizzare.
     *
     * @param table (jQuery) la tabella da popolare
     * @param list  (Array)  la lista tramite cui popolare la tabella
     */
    function tabellaDatiSubdocumentoSpesa(table, list) {
        var options = {
            bServerSide: false,
            aaData: list,
            aoColumnDefs: [
               {aTargets: [0], mData: function() {
                   return "<input type='checkbox' name='checkSubdocumentoModale'/>";
               }, fnCreatedCell: function(nTd, sData, oData) {
                   $("input", nTd).data("originalDatiSubdocumento", oData);
               }},
                {aTargets: [1], mData: function(source) {
                    return source.numero || "";
                }},
                {aTargets: [2], mData: function(source) {
                    var importo = source.importo || 0;
                    return importo.formatMoney();
                }},
                {aTargets: [3], mData: function(source) {
                    return gestioneBoolean(source.pagatoInCEC, "Si", "No");
                }},
                {aTargets: [4], mData: function(source) {
                    return formatDate(source.dataPagamentoCEC);
                }}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        // DIsattivo eventualmente il datatable
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        table.dataTable(opts);
    }

    /**
     * Costruttore per i DatiDocumentoSpesa.
     *
     * @param selectorTipoDocumento (String) il selettore CSS del campo ove impostare il tipo documento (deve rappresentare un select)
     * @param selectorAnno          (String) il selettore CSS del campo ove impostare l'anno del documento
     * @param selectorNumero        (String) il selettore CSS del campo ove impostare l'anno del documento
     */
    DatiDocumentoSpesa = function(selectorTipoDocumento, selectorAnno, selectorNumero) {
        // Campi della pagina
        this.$tipoDocumento = $(selectorTipoDocumento);
        this.$annoDocumento = $(selectorAnno);
        this.$numeroDocumento = $(selectorNumero);

        // I campi del modale
        this.$tipoDocumentoModale = $("#tipoDocumentoDocumentoSpesa_modale");
        this.$annoDocumentoModale = $("#anno_modale");
        this.$numeroDocumentoModale = $("#numero_modale");
        this.$statoOperativoDocumentoModale = $("#statoOperativoDocumento_modale");
        this.$dataEmissioneDocumentoModale = $("#dataEmissione_modale");
        this.$elencoAnnoModale = $("#elenco.anno_modale");
        this.$elencoNumeroModale = $("#elenco.numero_modale");
        this.$movGestioneAnnoModale = $("#movimentoGestione.anno_modale");
        this.$movGestioneNumeroModale = $("#movimentoGestione.numero_modale");

        this.$fieldsetRicerca = $("#fieldsetModaleRicercaDocumentoSpesa");
        this.$divTabella = $("#risultatiRicercaModaleDocumentoSpesa");
        this.$spinner = $("#spinnerModaleCercaDocumentoSpesa");
        this.$table = $("#tabellaModaleRicercaDocumentoSpesa");

        this.$divTabellaSubdocumento = $("#divModaleRicercaDocumentoSpesaSubdocumento");
        this.$tableSubdocumento = $("#tabellaModaleRicercaDocumentoSpesaSubdocumento");

        this.$modal = $("#modaleRicercaDocumentoSpesa");

        this.init();
    };

    /** Costruttore */
    DatiDocumentoSpesa.prototype.constructor = DatiDocumentoSpesa;

    /**
     * Inizializzazione interna delle funzionalita'.
     */
    DatiDocumentoSpesa.prototype.init = function() {
        $("#pulsanteCercaModaleRicercaDocumentoSpesa").click($.proxy(DatiDocumentoSpesa.prototype.cercaDatiDocumentoSpesa, this));
        $("#pulsanteConfermaModaleRicercaDocumentoSpesa").click($.proxy(DatiDocumentoSpesa.prototype.impostaDatiSubdocumentoSpesa, this));

        this.$table.substituteHandler("change", "input[type='radio'][name='checkDocumento']", $.proxy(DatiDocumentoSpesa.prototype.cercaDatiSubdocumentoSpesa, this));
    };

    /**
     * Richiama l'esecuzione della ricerca DatiDocumentoSpesa.
     *
     * @returns (DatiDocumentoSpesa) l'oggetto su cui e' atata effettuata l'invocazione
     */
    DatiDocumentoSpesa.prototype.cercaDatiDocumentoSpesa = function() {
        var self = this;
        var oggettoPerChiamataAjax = unqualify(self.$fieldsetRicerca.serializeObject(), 1);
        oggettoPerChiamataAjax.collegatoCEC = true;
        oggettoPerChiamataAjax.contabilizzaGenPcc = true;
        // Prendo tre pagine
        oggettoPerChiamataAjax.risultatiPerPagina = 9;

        self.$divTabella.slideUp();
        self.$divTabellaSubdocumento.slideUp();
        self.$spinner.addClass("activated");
        // Chiudo l'alert
        alertErroriModale.slideUp();

        $.postJSON("ricercaDocumentoSpesaAjax_ricercaDocumentoModale.do", oggettoPerChiamataAjax, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }

            // Carico i dati in tabella
            tabellaDatiDocumentoSpesa(self.$table);
            self.$divTabella.slideDown();
        }).always(function() {
            // Disattivo lo spinner
            self.$spinner.removeClass("activated");
        });

        return this;
    };

    /**
     * Cerca i dati del subdocumento di spesa.
     *
     * @returns (DatiDocumentoSpesa) l'oggetto su cui e' atata effettuata l'invocazione
     */
    DatiDocumentoSpesa.prototype.cercaDatiSubdocumentoSpesa = function() {
        var self = this;
        var checkedRadio = this.$table.find("input[type='radio'][name='checkDocumento']").filter(":checked");
        var documento;
        var row;
        // Se nulla e' stato selezionato, esco
        if(!checkedRadio.length) {
            return this;
        }
        this.$divTabellaSubdocumento.slideUp();
        alertErroriModale.slideUp();

        documento = checkedRadio.data("originalDatiDocumento").documentoSpesa;
        row = checkedRadio.closest("tr").overlay("show");

        $.postJSON("ricercaDocumentoSpesaAjax_ricercaQuoteDocumentoSpesa.do", {"documentoSpesa.uid": documento.uid}, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }

            // Carico i dati in tabella
            tabellaDatiSubdocumentoSpesa(self.$tableSubdocumento, data.listaSubdocumentoSpesa);
            self.$divTabellaSubdocumento.slideDown();
        }).always(function() {
            // Disattivo lo spinner
            row.overlay("hide");
        });

        return this;
    };

    /**
     * Imposta i dati del subdocumento all'interno dei campi selezionati.
     *
     * @returns (DatiDocumentoSpesa) l'oggetto su cui e' atata effettuata l'invocazione
     */
    DatiDocumentoSpesa.prototype.impostaDatiSubdocumentoSpesa = function() {
        var checkedDatiDocumentoSpesa = this.$table.find("[name='checkDocumento']:checked");
        var checkedDatiSubdocumentoSpesa = this.$tableSubdocumento.find("[name='checkSubdocumentoModale']:checked");

        // I campi da popolare
        var datiDocumentoSpesa;
        var datiSubdocumentoSpesa = [];

        // Se non ho selezionato nulla, esco subito
        if(checkedDatiDocumentoSpesa.length === 0 || checkedDatiSubdocumentoSpesa.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare uno dei dati proposti"], alertErroriModale, false);
            return;
        }

        // Ottengo i dati del soggetto salvati
        datiDocumentoSpesa = pruneDocumento(checkedDatiDocumentoSpesa.data("originalDatiDocumento").documentoSpesa);
        datiSubdocumentoSpesa = checkedDatiSubdocumentoSpesa.map(function(idx, el) {
            return pruneSubdocumento($(el).data("originalDatiSubdocumento"));
        }).get();

        // Associazione della fattura
        $(document).trigger('associaFattura', {fattura: datiDocumentoSpesa, quote: datiSubdocumentoSpesa, alert: alertErroriModale, alertRedirect: false, callback: $.proxy(function() {
            // Copio i campi ove adeguato e lancio le azioni
            this.$tipoDocumento.val(datiDocumentoSpesa.tipoDocumento.uid);
            this.$annoDocumento.val(datiDocumentoSpesa.anno);
            this.$numeroDocumento.val(datiDocumentoSpesa.numero);

            // Chiudo il modale
            this.$modal.modal("hide");
        }, this)});
    };

    /**
     * Effettua un pruning del documento, mantenendo solo i dati necessarii.
     *
     * @param doc (Object) il documento
     */
    function pruneDocumento(doc) {
        var res = {};
        // Dati che mi interessano: uid, anno, tipoDocumento (uid, codice, descrizione), numero, soggetto (uid, codice soggetto, denominazione), data emissione
        // Aggiungo descAnnoNumeroTipoDoc per comodita'
        var propertiesToClone = ['uid', 'anno', 'tipoDocumento.uid', 'tipoDocumento.codice', 'tipoDocumento.descrizione', 'numero',
            'soggetto.uid', 'soggetto.codiceSoggetto', 'soggetto.denominazione', 'dataEmissione', 'statoOperativoDocumento', 'descAnnoNumeroTipoDoc'];

        cloneProperties(doc, res, propertiesToClone);
        return res;
    }

    /**
     * Effettua un pruning del subdocumento, mantenendo solo i dati necessarii.
     *
     * @param subdoc (Object) il subdocumento
     */
    function pruneSubdocumento(subdoc) {
        var res = {};
        // Dati che mi interessano: uid, numero
        var propertiesToClone = ['uid', 'numero', 'flagRilevanteIVA', 'importo', 'impegno.uid', 'impegno.annoMovimento', 'impegno.numero',
            'subImpegno.uid', 'subImpegno.annoMovimento', 'subImpegno.numero'];

        cloneProperties(subdoc, res, propertiesToClone);
        return res;
    }

    /**
     * Apertura del modale
     *
     * @returns (DatiDocumentoSpesa) l'oggetto su cui e' stata effettuata l'invocazione
     */
    DatiDocumentoSpesa.prototype.apriModale = function() {

        this.$fieldsetRicerca.find(":input").val("");
        //Popolo campi modale
        this.$tipoDocumentoModale.val(this.$tipoDocumento.val());
        this.$annoDocumentoModale.val(this.$annoDocumento.val());
        this.$numeroDocumentoModale.val(this.$numeroDocumento.val());

        // Nascondo i campi
        alertErroriModale.slideUp();
        this.$divTabella.slideUp();
        this.$modal.modal("show");
    };


    /**
     * Inizializzazione della gestione dei dati del Documento spesa.
     */

    /**
     * Inizializza la gestione.
     *
     * @param selectorTipoDocumento     (String)    il selettore del campo ove impostare il tipo documento
     * @param selectorAnno              (String)    il selettore del campo ove impostare l'anno del documento
     * @param selectorNumero            (String)    il selettore del campo ove impostare l'anno del documento
     * @param selectorPulsante          (String)    il selettore del pulsante di apertura modale
     */
    exports.inizializza = function(selectorTipoDocumento, selectorAnno, selectorNumero, selectorPulsante) {
        var datiDocumentoSp = new DatiDocumentoSpesa(selectorTipoDocumento, selectorAnno, selectorNumero);
        $(selectorPulsante).click($.proxy(DatiDocumentoSpesa.prototype.apriModale, datiDocumentoSp));
    };

    // Esportazione delle funzionalita'
    global.DatiDocumentoSpesa = exports;

}(jQuery, this);