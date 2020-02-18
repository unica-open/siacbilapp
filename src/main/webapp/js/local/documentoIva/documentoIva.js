/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function ($, w) {
    "use strict";

    var exports = {};
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var cento = new BigNumber(100);
    var MovimentiIva;

    /**
     * Costruttore per i movimenti iva.
     *
     * @param urlPrefix  (String) il prefisso da anteporre agli URL
     * @param prefix     (String) il prefisso da utilizzare (Optional - default: "")
     * @param suffix     (String) il suffisso da utilizzare (Optional - default: "")
     * @param urlSuffix  (String) il suffisso da apporre agli URL (Optional - default: "")
     * @param jsonSuffix (String) il suffisso da apporre alle proprieta degli oggetti JSON (Optional - default: "")
     */
    MovimentiIva = function (urlPrefix, prefix, suffix, urlSuffix, jsonSuffix) {
        this.urlPrefix = urlPrefix;
        this.prefix = prefix || "";
        this.suffix = suffix || "";
        this.urlSuffix = urlSuffix || "";
        this.jsonSuffix = jsonSuffix || "";
    };

    /**
     * Costruttore.
     */
    MovimentiIva.prototype.constructor = MovimentiIva;

    /**
     * Popola la tabella dei movimenti IVA.
     *
     * @param lista            (Array)  la lista dei movimenti
     * @param totaleImponibile (Number) il totale degli imponibili
     * @param totaleImposta    (Number) il totale delle imposte
     * @param totaleTotale     (Number) il totale dei totali
     */
    MovimentiIva.prototype.popolaTabellaMovimentiIva = function (lista, totaleImponibile, totaleImposta, totaleTotale) {
        var self = this;
        // Opzioni per il dataTable
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            aaData: lista,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti movimenti iva associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function (source) {
                    // Codice e descrizione dell'aliquota
                    return source.aliquotaIva.codice + " - " + source.aliquotaIva.descrizione;
                }},
                {aTargets: [1], mData: function (source) {
                    // Percentuale dell'aliquota
                    return source.aliquotaIva.percentualeAliquota.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [2], mData: function (source) {
                    // Imponibile
                    return source.imponibile.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                { aTargets: [3], mData: function (source) {
                    // Imposta
                    return source.imposta.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], mData: function (source) {
                    // Imposta detraibile
                    return source.impostaDetraibile.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [5], mData: function (source) {
                    // Imposta indetraibile
                    return source.impostaIndetraibile.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [6], mData: function (source) {
                    // Totale
                    return source.totale.formatMoney();
                }, fnCreatedCell: function (nTd) {
                    // Allineo a destra
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [7], mData: function () {
                    return "<div class='btn-group'>" +
                        "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                        "<ul class='dropdown-menu pull-right'>" +
                            "<li><a href='#' class='aggiornaMovimentoIva'>aggiorna</a></li>" +
                            "<li><a href='#' class='eliminaMovimentoIva'>elimina</a></li>" +
                        "</ul>" +
                    "</div>";
                }, fnCreatedCell: function (nTd, sData, oData, iRow) {
                    $(nTd).find(".aggiornaMovimentoIva")
                            .substituteHandler("click", self.apriModaleAggiornaMovimentoIva.bind(self, iRow))
                            .end()
                        .find(".eliminaMovimentoIva")
                            .substituteHandler("click", self.apriModaleEliminaMovimentoIva.bind(self, oData, iRow));
                }}
            ]
        };

        self.getElementPrefixed("tabellaMovimentiIva").dataTable(opts);
        // Popolo i th dei totali
        self.getElementPrefixed("totaleImponibileMovimentiIva").html(totaleImponibile.formatMoney());
        self.getElementPrefixed("totaleImpostaMovimentiIva").html(totaleImposta.formatMoney());
        self.getElementPrefixed("totaleMovimentiIva").html(totaleTotale.formatMoney());
    };

    /**
     * Apre il modale dell'aggiornamento del movimento iva.
     *
     * @param riga (Number) la riga
     * @param e    (Event)  l'evento scatenante
     */
    MovimentiIva.prototype.apriModaleAggiornaMovimentoIva = function (riga, e) {
        var self = this;
        var oggettoPerChiamataAjax = {
            riga: riga,
            suffisso: self.suffix
        };
        var url = self.urlPrefix + "_apriModaleMovimentiIva" + self.urlSuffix + ".do";
        e.preventDefault();
        self.getElementPrefixed("divModaleMovimentiIva").load(url, oggettoPerChiamataAjax, function () {
            var imponibile, imposta, totale, pulsante;
            var imponibileFnc = self.calcolaImportiDaImponibile.bind(self);
            var impostaFnc = self.calcolaImportiDaImposta.bind(self);
            var totaleFnc = self.calcolaImportiDaTotale.bind(self);

            // Modifico i name
            self.getElementPrefixed("fieldsetAliquotaSubdocumentoIva_modale").find("*[name]").each(function () {
                var $this = $(this);
                $this.attr("name", "modale." + $this.attr("name"));
            });
            // Lego le azioni
            self.getElementPrefixed("aliquotaIvaAliquotaSubdocumentoIva_modale").substituteHandler("change", {suffix: "_modale"}, function (e) {
                self.impostaAliquota($(this), e);
            });
            self.getElementPrefixed("pulsanteConfermaAliquota_modale").substituteHandler("click", {suffix: "_modale"}, self.aggiornaMovimentoIva.bind(self));
            imponibile = self.getElementPrefixed("imponibileAliquotaSubdocumentoIva_modale").on("change", {suffix: "_modale"}, imponibileFnc);

            imposta = self.getElementPrefixed("impostaAliquotaSubdocumentoIva_modale")
                .on("change", {suffix: "_modale"}, impostaFnc)
                .on("impostaAggiornata", {suffix: "_modale"}, self.aggiornaImposteDetraibiliIndetraibili.bind(self));
            totale = self.getElementPrefixed("totaleAliquotaSubdocumentoIva_modale").on("change", {suffix: "_modale"}, totaleFnc);

            // Pulsante gestione manuale
            pulsante = self.getElementPrefixed("pulsanteGestioneManualeAliquotaSubdocumentoIva_modale").removeClass('disabled');
            pulsante.substituteHandler('click', gestioneManuale.bind(null, pulsante, imponibile, imposta, totale, imponibileFnc, impostaFnc, totaleFnc));
            // Apro il modale
            self.getElementPrefixed("modaleAggiornamentoMovimentiIva").modal("show");
        });
    };

    /**
     * Metodo di utilit&agrave; per ottenere un elemento prefissato e suffissato.
     *
     * @param base  (String) l'id di base
     * @param infix (String) l'eventuale stringa da frapporre tra base e suffix (default: '') 
     * @return (jQuery) l'elemento wrappato da jQuery
     */
    MovimentiIva.prototype.getElementPrefixed = function(base, infix) {
        var inf = infix || '';
        return $("#" + this.prefix + base + inf + this.suffix);
    };

    /**
     * Aggiorna il movimento iva.
     */
    MovimentiIva.prototype.aggiornaMovimentoIva = function () {
        var self = this;
        var fieldset = self.getElementPrefixed("fieldsetAliquotaSubdocumentoIva_modale");
        var oggettoPerChiamataAjax = unqualify(fieldset.serializeObject(), 1);
        var spinner = self.getElementPrefixed("SPINNER_pulsanteConfermaAliquota_modale").addClass("activated");
        var url = self.urlPrefix + "_aggiornaMovimentiIva" + self.urlSuffix + ".do";

        $.postJSON(url, oggettoPerChiamataAjax).then(function (data) {
            var alertErrorModale = self.getElementPrefixed("modaleAggiornamentoMovimentiIva").find(".alert.alert-error");
            if (impostaDatiNegliAlert(data.errori, alertErrorModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricalcolo la tabella
            self.popolaTabellaMovimentiIva(
                data["listaAliquotaSubdocumentoIva" + self.jsonSuffix],
                data["totaleImponibileMovimentiIva" + self.jsonSuffix],
                data["totaleImpostaMovimentiIva" + self.jsonSuffix],
                data["totaleTotaleMovimentiIva" + self.jsonSuffix]);
            // Chiudo il modale
            self.getElementPrefixed("modaleAggiornamentoMovimentiIva").modal("hide");
        }).always(spinner.removeClass.bind(spinner, "activated"));
    };

    /**
     * Apre il modale dell'eliminazione del movimento iva.
     *
     * @param aliquota (Object) l'aliquota
     * @param riga     (Number) la riga nella tabella
     * @param e        (Event) l'evento scatenante l'invocazione
     */
    MovimentiIva.prototype.apriModaleEliminaMovimentoIva = function (aliquota, riga, e) {
        var self = this;
        e.preventDefault();
        self.getElementPrefixed("SPAN_elementoSelezionato").html(aliquota.aliquotaIva.codice);
        self.getElementPrefixed("pulsanteSiElimina").substituteHandler("click", self.eliminaMovimentoIva.bind(self, riga));
        self.getElementPrefixed("modaleElimina").find(".alert")
                .slideDown()
                .end()
            .modal("show");
    };

    /**
     * Elimina il movimento iva.
     *
     * @param riga (Number) la riga nella tabella
     */
    MovimentiIva.prototype.eliminaMovimentoIva = function (riga) {
        var self = this;
        var spinner = self.getElementPrefixed("SPINNER_pulsanteSiElimina").addClass("activated");
        var url = self.urlPrefix + "_eliminaMovimentiIva" + self.urlSuffix + ".do";

        $.postJSON(url, {riga: riga}).then(function (data) {
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricalcolo la tabella
            self.popolaTabellaMovimentiIva(
                data["listaAliquotaSubdocumentoIva" + self.jsonSuffix],
                data["totaleImponibileMovimentiIva" + self.jsonSuffix],
                data["totaleImpostaMovimentiIva" + self.jsonSuffix],
                data["totaleTotaleMovimentiIva" + self.jsonSuffix]);
            $("#" + self.prefix + "modaleElimina" + self.suffix).modal("hide");
        }).always(spinner.removeClass.bind(spinner, "activated"));
    };

    /**
     * Ottiene la lista dei Movimenti Iva.
     */
    MovimentiIva.prototype.obtainListaMovimentiIva = function () {
        var self = this;
        var url = self.urlPrefix + "_caricaMovimentiIva" + self.urlSuffix + ".do";
        $.postJSON(url).then(function (data) {
            self.popolaTabellaMovimentiIva(
                data["listaAliquotaSubdocumentoIva" + self.jsonSuffix],
                data["totaleImponibileMovimentiIva" + self.jsonSuffix],
                data["totaleImpostaMovimentiIva" + self.jsonSuffix],
                data["totaleTotaleMovimentiIva" + self.jsonSuffix]);
        });
    };

    /**
     * Carica il collapse del nuovo movimento IVA e lo apre.
     */
    MovimentiIva.prototype.caricaCollapseNuovoMovimentoIva = function () {
        var self = this;
        var url = self.urlPrefix + "_apriCollapseMovimentiIva" + self.urlSuffix + ".do";
        var oggettoPerChiamataAjax = {
            suffisso: self.suffix
        };
        // Attivo lo spinner
        var spinner = self.getElementPrefixed("SPINNER_bottoneInserisciNuovoMovimentoIva").addClass("activated");
        // Carico il form nel div
        $("#" + self.prefix + "collapseInserisciNuovoMovimentoIva" + self.suffix).load(url, oggettoPerChiamataAjax, function () {
            var $this = $(this);
            var $selectAliquotaIva = $("#" + self.prefix + "aliquotaIvaAliquotaSubdocumentoIva" + self.suffix);
            var imponibile, imposta, totale, pulsanteManuale;
            var imponibileFnc = self.calcolaImportiDaImponibile.bind(self);
            var impostaFnc = self.calcolaImportiDaImposta.bind(self);
            var totaleFnc = self.calcolaImportiDaTotale.bind(self);

            preSelezionaSeUnicaOpzione($selectAliquotaIva);
            // Attivo le funzionalita dei pulsanti
            self.getElementPrefixed("pulsanteAnnullaInserisciMovimentiIva").substituteHandler("click", $this.collapse.bind($this, "hide"));

            // Caricamento funzionalita sugli elementi
            self.getElementPrefixed("pulsanteConfermaInserisciMovimentiIva").substituteHandler("click", self.inserisciMovimentoIva.bind(self));
            self.getElementPrefixed("aliquotaIvaAliquotaSubdocumentoIva").substituteHandler("change", function (e) {
                self.impostaAliquota($(this), e);
            });

            imponibile = self.getElementPrefixed("imponibileAliquotaSubdocumentoIva").on("change", imponibileFnc);
            imposta = self.getElementPrefixed("impostaAliquotaSubdocumentoIva")
                .on("change", impostaFnc)
                .on("impostaAggiornata", self.aggiornaImposteDetraibiliIndetraibili.bind(self));
            totale = self.getElementPrefixed("totaleAliquotaSubdocumentoIva").on("change", totaleFnc);

            // Pulsante gestione manuale
            pulsanteManuale = self.getElementPrefixed("pulsanteGestioneManualeAliquotaSubdocumentoIva").removeClass('disabled');
            pulsanteManuale.substituteHandler('click', gestioneManuale.bind(null, pulsanteManuale, imponibile, imposta, totale, imponibileFnc, impostaFnc, totaleFnc));
            // Apro il collapse
            $this.collapse("show");
            // Disattivo lo spinner
            spinner.removeClass("activated");
        });
    };

    /**
     * Gestione manuale dei campi.
     *
     * @param pulsanteManuale (jQuery)   il pulsante di attivazione della funzionalita
     * @param imponibile      (jQuery)   il campo dell'imponibile
     * @param imposta         (jQuery)   il campo dell'imposta
     * @param totale          (jQuery)   il campo del totale
     * @param imponibileFnc   (Function) la funzione dell'imponibile
     * @param impostaFnc      (Function) la funzione dell'imposta
     * @param totaleFnc       (Function) la funzione del totale
     * @param e               (Event)    l'evento scatanante
     */
    function gestioneManuale(pulsanteManuale, imponibile, imposta, totale, imponibileFnc, impostaFnc, totaleFnc, e) {
        e.preventDefault();
        pulsanteManuale.addClass('disabled');
        imponibile.off('change', imponibileFnc).gestioneDeiDecimali();
        imposta.off('change', impostaFnc).gestioneDeiDecimali().on('change', imposta.trigger.bind(imposta, 'impostaAggiornata'));
        totale.off('change', totaleFnc).gestioneDeiDecimali();
    }

    /**
     * Carica il collapse del nuovo movimento IVA e lo apre.
     */
    MovimentiIva.prototype.inserisciMovimentoIva = function () {
        var self = this;
        var oggettoPerChiamataAjax = self.getElementPrefixed("fieldsetAliquotaSubdocumentoIva").serializeObject();
        var url = self.urlPrefix + "_inserisciMovimentiIva" + self.urlSuffix + ".do";
        var spinner = self.getElementPrefixed("SPINNER_pulsanteConfermaInserisciMovimentiIva").addClass("activated");

        $.postJSON(url, oggettoPerChiamataAjax).then(function (data) {
            if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricalcolo la tabella
            self.popolaTabellaMovimentiIva(
                data["listaAliquotaSubdocumentoIva" + self.jsonSuffix],
                data["totaleImponibileMovimentiIva" + self.jsonSuffix],
                data["totaleImpostaMovimentiIva" + self.jsonSuffix],
                data["totaleTotaleMovimentiIva" + self.jsonSuffix]);
            self.getElementPrefixed("collapseInserisciNuovoMovimentoIva").collapse("hide");
        }).always(spinner.removeClass.bind(spinner, "activated"));
    };

    /**
     * Imposta la percentuale dell'aliquota selezionata nel campo corretto.
     *
     * @param $this (jQuery) l'oggetto jQuery su cui la funzione è stata invocata
     * @param e     (Event)  l'evento scatenante l'invocazione
     */
    MovimentiIva.prototype.impostaAliquota = function ($this, e) {
        var option = $this.find("option:selected");
        var evtSuffix = e && e.data && e.data.suffix ? e.data.suffix : "";
        var imponibile = this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", evtSuffix);
        var imposta = this.getElementPrefixed("impostaAliquotaSubdocumentoIva", evtSuffix);
        var totale = this.getElementPrefixed("totaleAliquotaSubdocumentoIva", evtSuffix);
        var fields = imponibile.add(imposta).add(totale);

        // Carico la percentuale
        this.getElementPrefixed("percentualeAliquotaIva", evtSuffix).val(option.data("percentualeAliquota"));
        this.getElementPrefixed("percentualeIndetraibilitaAliquotaIva", evtSuffix).val(option.data("percentualeIndetraibilita"));

        if (!!$this.val()) {
            // Se ho selezionato qualcosa, abilito i campi
            fields.removeAttr("disabled");
        } else {
            // Disabilito i campi
            fields.attr("disabled", true);
        }
        // Ricalcolo
        this.calcolaImportiDaImponibile(e);
    };

    /**
     * Ricalcola gli importi a seguito della modifica dell'imponibile.
     *
     * @param e(Event) l'evento scatenante
     */
    MovimentiIva.prototype.calcolaImportiDaImponibile = function (e) {
        var eventSuffix = e && e.data && e.data.suffix ? e.data.suffix : "";
        var percentualeStr = parseLocalNum(this.getElementPrefixed("percentualeAliquotaIva", eventSuffix).val()) || '0';
        var imponibileStr = parseLocalNum(this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", eventSuffix).val()) || '0';
        var percentuale = new BigNumber(percentualeStr);
        var imponibile = new BigNumber(parseLocalNum(formatMoney(imponibileStr)));
        var imposta;
        var totale;
        
        // Imposta = imponibile * percentuale / 100
        imposta = new BigNumber(parseLocalNum(formatMoney(imponibile.times(percentuale).dividedBy(cento))));
        // Totale = imponibile + imposta
        totale = imponibile.add(imposta);

        //this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", eventSuffix).val(imponibile.formatMoney());
        this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imponibile));
        this.getElementPrefixed("impostaAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imposta))
            .trigger("impostaAggiornata");
        this.getElementPrefixed("totaleAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(totale));
    };

    /**
     * Ricalcola gli importi a seguito della modifica dell'imposta.
     *
     * @param e (Event) l'evento scatenante
     */
    MovimentiIva.prototype.calcolaImportiDaImposta = function (e) {
        var eventSuffix = e && e.data && e.data.suffix ? e.data.suffix : "";
        var percentualeStr = parseLocalNum(this.getElementPrefixed("percentualeAliquotaIva", eventSuffix).val()) || '0';
        var percentuale = new BigNumber(percentualeStr);
        var impostaStr;
        var imponibile;
        var imposta;
        var totale;
        
        if(percentuale.equals(0)){
        	return;
        }
        impostaStr = parseLocalNum(this.getElementPrefixed("impostaAliquotaSubdocumentoIva", eventSuffix).val()) || '0';
        imposta = new BigNumber(parseLocalNum(formatMoney(impostaStr)));
        
        // Imponibile = (imposta / percentuale) * 100
        imponibile = new BigNumber(parseLocalNum(formatMoney(imposta.dividedBy(percentuale).times(cento))));
        imponibile = 
        // Totale = imponibile + imposta
        totale = imponibile.add(imposta);
        
        this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imponibile));
        this.getElementPrefixed("impostaAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imposta))
            .trigger("impostaAggiornata");
        this.getElementPrefixed("totaleAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(totale));
    };

    /**
     * Ricalcola gli importi a seguito della modifica del totale.
     *
     * @param e (Event) l'evento scatenante
     */
    MovimentiIva.prototype.calcolaImportiDaTotale = function (e) {
        var eventSuffix = e && e.data && e.data.suffix ? e.data.suffix : "";
        var percentualeStr = parseLocalNum(this.getElementPrefixed("percentualeAliquotaIva", eventSuffix).val()) || '0';
        var percentuale = new BigNumber(percentualeStr);
        var totaleStr = parseLocalNum(this.getElementPrefixed("totaleAliquotaSubdocumentoIva", eventSuffix).val()) || '0';
        var totale = new BigNumber(parseLocalNum(formatMoney(totaleStr)));

        // Imposta = (percentuale * totale) / (100 + percentuale)
        var imposta = new BigNumber(parseLocalNum(formatMoney(percentuale.times(totale).dividedBy(cento.add(percentuale)))));
        // Imponibile = totale - imposta
        var imponibile = totale.minus(imposta);

        this.getElementPrefixed("imponibileAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imponibile));
        this.getElementPrefixed("impostaAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(imposta))
            .trigger("impostaAggiornata");
        this.getElementPrefixed("totaleAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(totale));
    };

    /**
     * Aggiorna le imposte detraibili ed indetraibili
     */
    MovimentiIva.prototype.aggiornaImposteDetraibiliIndetraibili = function (e) {
        var eventSuffix = e && e.data && e.data.suffix ? e.data.suffix : "";
        var percentuale = new BigNumber(parseLocalNum(this.getElementPrefixed("percentualeIndetraibilitaAliquotaIva", eventSuffix).val()) || '0');
        var impostaStr = parseLocalNum(this.getElementPrefixed("impostaAliquotaSubdocumentoIva", eventSuffix).val()) || '0';
        var imposta = new BigNumber(parseLocalNum(formatMoney(impostaStr)));
        
        // detraibile = imposta * (100 - percentuale) / 100
        var impostaDetraibile = new BigNumber(parseLocalNum(formatMoney(imposta.times(cento.minus(percentuale)).dividedBy(cento))));
        // indetraibile = imposta * percentuale / 100
        var impostaIndetraibile = imposta.times(percentuale).dividedBy(cento);

        this.getElementPrefixed("impostaDetraibileAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(impostaDetraibile));
        this.getElementPrefixed("impostaIndetraibileAliquotaSubdocumentoIva", eventSuffix).val(formatMoney(impostaIndetraibile));
    };

    /**
     * Inizializza il tutto.
     */
    MovimentiIva.prototype.init = function () {
        var self = this;
        self.getElementPrefixed("bottoneInserisciNuovoMovimentoIva").substituteHandler("click", self.caricaCollapseNuovoMovimentoIva.bind(self));
        self.obtainListaMovimentiIva();
    };

    /**
     * Gestisce i movimenti Iva.
     *
     * @param urlPrefix  (String) il prefisso da anteporre agli URL
     * @param prefix     (String) il prefisso da utilizzare (Optional - default: "")
     * @param suffix     (String) il suffisso da utilizzare (Optional - default: "")
     * @param urlSuffix  (String) il suffisso da apporre agli URL (Optional - default: "")
     * @param jsonSuffix (String) il suffisso da apporre alle proprieta degli oggetti JSON (Optional - default: "")
     */
    exports.gestioneMovimentiIva = function (urlPrefix, prefix, suffix, urlSuffix, jsonSuffix) {
        var mov = new MovimentiIva(urlPrefix, prefix || "", suffix || "", urlSuffix || "", jsonSuffix || "");
        mov.init();
    };

    /**
     * Gestisce il documento intracomunitario.
     *
     * @param sfx (String) il suffisso dei campi (Opzionale, default: "")
     */
    exports.gestioneIntracomunitario = function(sfx) {
        var suffix = sfx || "";
        // Imposto il check sul documento intracomunitario
        $(".flagIntracomunitario").substituteHandler("click", function(e) {
            $(this).trigger({
                type: "flagIntracomunitarioCliccato",
                originalEvent: e
            });
        }).on("flagIntracomunitarioCliccato", function() {
            var self = $(this);
            $("#campiDocumentoIntracomunitario" + suffix)[self.prop("checked") ? "addClass" : "removeClass"]("in");
        }).trigger("flagIntracomunitarioCliccato");

        $(".pulsanteSalvaFormIva").on("click", function() {
            var tipoRegistro = $("#tipoRegistroIva" + suffix).val();
            var index = tipoRegistro.indexOf("DIFFERITA");
            var id = $(this).attr("id");
            if($("#HIDDEN_isMovimentoResiduo").val() === "true" && index !== -1){
                $("#modaleMovimentoResiduo").modal("show");
            } else{
                // Se non ho checkato il flag intracomunitario, effettuo il submit. Altrimenti apro il modale
                // Dovrebbe gia' essere false in questi casi, ma lo risetto per sicurezza
                proseguiConSubmit(false, suffix, id);
            }
        });

        $("#pulsanteProseguiSenzaProtocollo").on("click", proseguiConSubmit.bind(null, true, suffix, "pulsanteSalvaForm"));
        $("#pulsanteProseguiMovimentoResiduo").on("click", proseguiConSubmit.bind(null, false, suffix, "pulsanteSalvaForm"));
    };
    
    function proseguiConSubmit(value, suffix, id){
        var checkedFlag = $("#flagIntracomunitario" + suffix).prop("checked");
        $("#HIDDEN_isSenzaProtocollo").val(!!value);
        if(checkedFlag) {
            // Mostro il modale
            $("#modaleMovimentoResiduo").modal("hide");
            $("#modaleDocumentoIntracomunitarioDocumento" + suffix).modal("show");
        } else {
            // Invio il form
            $("#"+id).parents("form")
                // Per sicurezza prendo solo il primo form che contiene l'elemento
                .first()
                    .submit();
        }
    }

    exports.alertErrori = alertErrori;
    exports.alertInformazioni = alertInformazioni;
    w.DocumentoIva = exports;
}(jQuery, window));