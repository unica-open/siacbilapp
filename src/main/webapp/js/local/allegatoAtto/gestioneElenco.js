/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";

    var exports = {};
    // Alerts
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModaleRicercaElenco = $("#ERRORI_ElencoDocumentiAllegato");
    // Variabili varie
    var statoOperativoAllegatoAtto = $('#statoOperativoAllegatoAtto').val();
    var zero = 0;
    // Le opzioni di base
    var baseOpts = {
        bServerSide: false,
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
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    var baseOptsDettaglioQuoteElenco = {
        bServerSide: true,
        sServerMethod: "POST",
        sAjaxSource : "risultatiRicercaQuoteElencoDocumentiAllegato.do",
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 10,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: true,
        bDestroy:true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    function baseAoColumnDefsDettaglio() {
        // SIAC-5021: aggiunta domStringModalitaPagamentoSoggetto
        return [
            {aTargets: [0], mData: defaultPerDataTable('domStringElenco')},
            {aTargets: [1], mData: defaultPerDataTable('domStringDocumento')},
            {aTargets: [2], mData: defaultPerDataTable('domStringModalitaPagamentoSoggetto')},
            {aTargets: [3], mData: defaultPerDataTable('domStringSoggetto')},
            {aTargets: [4], mData: defaultPerDataTable('domStringMovimento')},
            {aTargets: [5], mData: defaultPerDataTable('domStringCapitolo')},
            {aTargets: [6], mData: defaultPerDataTable('domStringAttoAmministrativoMovimento')},
            {aTargets: [7], mData: defaultPerDataTable('ivaSplitReverse')},
            {aTargets: [8], mData: defaultPerDataTable('domStringAnnotazioni')},
            {aTargets: [9], mData: function(source) {
                var importoInAtto = source.importoInAtto || zero;
                return importoInAtto.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }}
        ];
    }

    /**
     * Formattazione dell'importo
     * @param val (any) il valore da formattare
     * @return (string) il valore formattato
     */
    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return '';
    }

    /**
     * Aggiunge la classe tab_Right all'elemento
     * @param nTd (Node) l'elemento su cui aggiungere la classe
     */
    function tabRight(nTd) {
        $(nTd).addClass('tab_Right');
    }

    /**
     * Generatore per le definizioni base delle colonne.
     *
     * @param suffix (String) il suffisso da apporre
     */
    function baseAoColumnDefsElenchi(suffix) {
        var innerSuffix = suffix || "";
        return [
            {aTargets: [0], mData: function(source) {
                return "<input type='radio' class='checkboxElencoDocumentiAllegato" + innerSuffix + "' name='uidElenco" + innerSuffix + "' value='" + source.uid + "'>";
            }, fnCreatedCell: function(nTd, sData, oData) {
                $("input", nTd).data("originalElenco", oData)
                    .substituteHandler("change", function(e) {
                        var evt = $.Event('elencoSelected');
                        evt.elenco = oData;
                        evt.originalEvent = e;
                        $(this).trigger(evt);
                    });
            }},
            {aTargets: [1], mData: function(source) {
                return source.anno && source.numero ? (source.anno + "/" + source.numero) : "";
            }},
            {aTargets: [2], mData: function(source) {
                if(!source.statoOperativoElencoDocumenti) {
                    return '';
                }
                return '<a href="#" rel="popover" data-original-title="Stato" data-trigger="hover" data-content="'
                    + escapeHtml(source.statoOperativoElencoDocumenti.descrizione) + '">' + source.statoOperativoElencoDocumenti.codice + '</a>';
            }},
            {aTargets: [3], mData: function(source) {
                if(!source.annoSysEsterno || !source.numeroSysEsterno) {
                    return '';
                }
                return '<a href="#" rel="popover" data-original-title="Fonte dati" data-trigger="hover" data-content=" ' + source.sysEsterno + '">'
                    + escapeHtml(source.annoSysEsterno) + '/' + source.numeroSysEsterno + '</a>';
            }},
            {aTargets: [4], mData: defaultPerDataTable('dataTrasmissione', '', formatDate)},
            {aTargets: [5], mData: defaultPerDataTable('numeroQuoteInElenco', 0)},
            {aTargets: [6], mData: defaultPerDataTable('totaleQuoteEntrateNotNull', 0, formatMoney), fnCreatedCell: tabRight},
            {aTargets: [7], mData: defaultPerDataTable('totaleQuoteSpeseNotNull', 0, formatMoney), fnCreatedCell: tabRight},
            {aTargets: [8], mData: defaultPerDataTable('totaleQuoteNetti', 0, formatMoney), fnCreatedCell: tabRight}
        ];
    }

    /**
     * Carica la tabella dell'elencoDocumentiAllegato.
     *
     * @param selTabella  (String)  il selettore CSS della tabella
     * @param moreOpts    (Object)  le ulteriori opzioni da utilizzare
     * @param colDefs     (Array)   le definizioni delle colonne (Optional, default: undefined)
     * @param suffix      (String)  il suffisso per il radio (Optional, default: "")
     * @param checkUnique (Boolean) se sia da checkare l'elemento nel caso sia unico (Optional, default: false)
     */
    function caricaTabellaElenco(selTabella, moreOpts, colDefs, suffix, checkUnique) {
        var innerCheckUnique = !!checkUnique;
        var table = $(selTabella);
        var opts = {
            oLanguage: {
                sZeroRecords: "Nessun elenco associato"
            },
            fnDrawCallback: function () {
                var totalRecords = this.fnSettings().fnRecordsTotal();
                table.find('a[rel="popover"]')
                    .eventPreventDefault("click")
                    .popover();
                if(totalRecords === 1 && innerCheckUnique) {
                    // Se ho solo un record
                    this.$("input").prop("checked", true);
                }
            },
            aoColumnDefs: colDefs || baseAoColumnDefsElenchi(suffix)
        };
        var options = $.extend(true, {}, baseOpts, opts, moreOpts);
        table.dataTable(options);
    }

    /**
     * Elimina l'elenco dall'allegato atto.
     *
     * @param elenco          (any)    l'elenco da eliminare
     * @param urlEliminazione (string) l'url da invocare per l'eliminazione del dato
     * @param urlOriginale    (string) l'URL invocante
     * @param sel             (string) il selettore CSS della tabella
     * @param e               (string) l'evento scatenante
     */
    function eliminaElenco(elenco, urlEliminazione, urlOriginale, sel, e) {
        var modal;
        // Blocco l'evento scatenante
        e.preventDefault();
        // Imposto la denominazione dell'elenco
        $('#spanElementoSelezionatoModaleEliminazione').html(elenco.anno + '/' + elenco.numero);
        $("#pulsanteSiModaleEliminazione").substituteHandler("click", function() {
            var spinner = $("#SPINNER_pulsanteSiModaleEliminazione").addClass("activated");
            $.postJSON(urlEliminazione, {'uidElencoDaEliminare': elenco.uid})
            .then(function(data) {
            	var evt;
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);

                evt = $.Event('elencoEliminato');
                exports.caricaElenchiAggiornamento(urlOriginale, sel, urlEliminazione, evt);
                modal.modal("hide");
            }).always(spinner.removeClass.bind(spinner, "activated"));
        });
        modal = $("#modaleEliminazione").modal("show");
    }

    /**
     * Gestore dell'elenco
     *
     * @param pulsanteApertura   (String)  il selettore CSS del pulsante di apertura
     * @param urlAssociazione    (String)  l'URL di associazione dell'elenco
     * @param annoSelector       (String)  il selettore CSS dell'anno elenco
     * @param numeroSelector     (String)  il selettore CSS del numero elenco
     * @param descriptorSelector (String)  il selettore CSS del descrittore dell'elenco
     * @param sendData           (Boolean) se inviare o meno i dati ottenuti alla action
     * @param formToSubmit       (Boolean) se il form sia da inviare a seguito dell'associazione
     * @param formSelector       (String)  il selettore CSS del form
     * @param tableSelector      (String)  il selettore CSS della tabella degli elenchi
     * @param urlEliminazione    (Boolean) l'url di eliminazione
     */
    var GestoreElenco = function(pulsanteApertura, urlAssociazione, annoSelector, numeroSelector, descriptorSelector, sendData, formToSubmit, formSelector, tableSelector, urlEliminazione) {
        this.urlAssociazione = urlAssociazione;
        this.formToSubmit = formToSubmit;
        this.sendData = sendData;
        this.tableSelector = tableSelector;
        this.urlEliminazione = urlEliminazione;

        this.$pulsanteApertura = $(pulsanteApertura);
        this.$anno = $(annoSelector);
        this.$numero = $(numeroSelector);
        this.$descriptor = $(descriptorSelector);
        this.$form = $(formSelector);
        this.$pulsanteRicerca = $("#pulsanteRicercaElencoDocumentiAllegato_modale");
        this.$pulsanteConferma = $("#pulsanteConfermaElencoDocumentiAllegato_modale");
        this.$spinnerConferma = $("#SPINNER_pulsanteConfermaElencoDocumentiAllegato_modale");
    };

    /** Costruttore */
    GestoreElenco.prototype.constructor = GestoreElenco;
    GestoreElenco.prototype.ricercaElenchi = ricercaElenchi;
    GestoreElenco.prototype.ricercaElenchiCallback = ricercaElenchiCallback;
    GestoreElenco.prototype.associaElenco = associaElenco;
    GestoreElenco.prototype.associaElencoCallback = associaElencoCallback;
    GestoreElenco.prototype.apriModaleAssociaElenco = apriModaleAssociaElenco;
    GestoreElenco.prototype.copiaElenco = copiaElenco;

    /**
     * Ricerca gli elenchi.
     */
    function ricercaElenchi() {
        var oggettoDaInviare = unqualify($("#fieldsetElencoDocumentiAllegato_modale").serializeObject(), 1);
        var spinner = $("#iconaRicercaElencoDocumentiAllegato_modale").removeClass("icon-search")
            .addClass("icon-refresh icon-spin spinner active");
        var divRisultati = $("#divRisultatiElencoDocumentiAllegato_modale").removeClass("in");

        alertErroriModaleRicercaElenco.slideUp();
        $.postJSON("ricercaElencoDocumentiAllegato.do", oggettoDaInviare)
        .then(this.ricercaElenchiCallback.bind(this, divRisultati))
        .always(function() {
            spinner.removeClass("icon-refresh icon-spin spinner active")
                .addClass("icon-search");
        });
    }
    /**
     * Callback di ricerca elenchi
     * @param divRisultati (jQuery) il div dei risultati
     * @param data         (any) i dati di response del servizio
     */
    function ricercaElenchiCallback(divRisultati, data) {
        if(impostaDatiNegliAlert(data.errori, alertErroriModaleRicercaElenco)) {
            // Ho degli errori: esco
            return;
        }
        divRisultati.addClass("in");
        caricaTabellaElenco("#tabellaRisultatiElencoDocumentiAllegato_modale", {
                bServerSide: true,
                sAjaxSource: "risultatiRicercaElencoDocumentiAllegato.do",
                iDisplayLength: 5,
                sServerMethod: "POST"
            }, undefined, "_modale", true);
    }

    /**
     * Associa l'elenco all'allegato.
     */
    function associaElenco() {
        var self = this;
        var selectedRadio = $(".checkboxElencoDocumentiAllegato_modale").filter(":checked");
        var oggettoPerChiamataAjax = {};
        // Se non ho selezionato nulla, comunico l'errore ed esco
        if(!selectedRadio.length) {
            impostaDatiNegliAlert(["Necessario selezionare un elenco da associare"], alertErroriModaleRicercaElenco);
            return;
        }
        oggettoPerChiamataAjax['elencoDocumentiAllegato.uid'] = selectedRadio.val();

        self.$spinnerConferma.addClass("activated");
        $.postJSON(self.urlAssociazione, oggettoPerChiamataAjax)
        .then(this.associaElencoCallback.bind(this))
        .always(this.$spinnerConferma.removeClass.bind(this.$spinnerConferma, "activated"));
    }
    /**
     * Callback di associazione elenco
     * @param data (any) i dati di response del servizio
     */
    function associaElencoCallback(data) {
        var cols;
        // Se ho errori, esco
        if(impostaDatiNegliAlert(data.errori, alertErroriModaleRicercaElenco)) {
            return;
        }
        impostaDatiNegliAlert(data.informazioni, $("INFORMAZIONI"));
        // Chiudo il modale
        $("#modaleAssociaElencoDocumentiAllegato").modal("hide");
        if(this.urlEliminazione) {
            cols = augmentCols(this.urlEliminazione, this.urlAssociazione, this.tableSelector);
        }
        // Carico la tebella, se i dati sono stati forniti
        data.listaElencoDocumentiAllegato && caricaTabellaElenco(this.tableSelector, {aaData: data.listaElencoDocumentiAllegato}, cols);
        // Redirigo verso l'aggiornamento
        this.formToSubmit && this.$form.submit();
    }

    /**
     * Aumenta le colonne per l'aggiornamento.
     *
     * @param urlEliminazione (String) l'URL di eliminazione
     * @param url (String) l'url da cui ottenere la lista
     * @param sel (String) il selettore CSS della tabella
     */
    function augmentCols(urlEliminazione, url, sel) {
        var cols =  baseAoColumnDefsElenchi().slice();
        cols.push(
            {aTargets: [9], mData: function(source) {
                var azioni = [];
                azioni.push(creaAzioneElimina(source));
                azioni.push(creaAzioneSospendiPagamentoSoggetto(source));
                // Prendo solo le azioni vere
                azioni = azioni.filter(function(el) {
                    return el !== undefined;
                });
                return creaAzioni(azioni);
            }, fnCreatedCell: function(nTd, sData, oData) {
                var $nTd = $(nTd);
                $nTd.find('.eliminaElenco').substituteHandler('click', eliminaElenco.bind(undefined, oData, urlEliminazione, url, sel));
                $nTd.find('.sospendiTutto').substituteHandler('click', sospendiTutto.bind(undefined, oData.uid, nTd));
            }});
        return cols;
    }

    /**
     * Creazione l'azione di eliminazione
     * @param el (any) l'elemento
     */
    function creaAzioneElimina(el) {
        var statoElenco = el.statoOperativoElencoDocumenti.codice;
        return statoOperativoAllegatoAtto === 'D' && (statoElenco === "B" || statoElenco === "R")
            ? '<li><a href="#" class="eliminaElenco">elimina</a></li>'
            : undefined;
    }

    /**
     * Creazione l'azione di sospensione pagamento soggetto
     * @param el (any) l'elemento
     */
    function creaAzioneSospendiPagamentoSoggetto(el) {
        return statoOperativoAllegatoAtto === 'D' || statoOperativoAllegatoAtto === 'C'
            ? '<li><a href="#" class="sospendiTutto">sospendi tutto</a></li>'
            : undefined;
    }
    /**
     * Creazione delle azioni
     * @param azioni (string[]) le azioni da usare
     * @return (string) il DOM delle azioni
     */
    function creaAzioni(azioni) {
        var str = '';
        if(!azioni.length) {
            // Non ho azioni: restituisco una stringa vuota
            return '';
        }
        str += '<div class="btn-group">';
        str += '<button class="btn dropdown-toggle" data-placement="left" data-toggle="dropdown" href="#">Azioni <span class="caret"></span></button>';
        str += '<ul class="dropdown-menu pull-right">';
        str += azioni.join('');
        str += '</ul></div>';
        return str;
    }
    /**
     * Sospensione di tutto per l'elenco
     * @param uid (number) l'uid dell'elenco
     * @param td  (Node)   il nodo su cui si e' verificato l'evento
     * @param e   (Event)  l'evento scatenante
     */
    function sospendiTutto(uid, td, e) {
        var evt = $.Event('sospendiTuttoElenco');
        evt.uidElenco = uid;
        // Blocco l'evento
        e.preventDefault();
        $(td).trigger(evt);
    }

    /**
     * Apertura del modale di associazione dell'elenco.
     */
    function apriModaleAssociaElenco() {
        var collapse = $("#divRisultatiElencoDocumentiAllegato_modale");
        var anno = $("#annoElencoDocumentiAllegato_modale");
        var numero = $("#numeroElencoDocumentiAllegato_modale");

        // Chiudo il collapse
        collapse.removeClass("in");

        // Svuoto i campi
        $("#fieldsetElencoDocumentiAllegato_modale").find(":input")
            .val("");

        // Popolo i campi da quelli presenti in maschera
        this.$anno.length && anno.val(this.$anno.val());
        this.$numero.length && numero.val(this.$numero.val());

        // Apro il modale
        $("#modaleAssociaElencoDocumentiAllegato").modal("show");
    }

    /**
     * Copia i dati dell'elenco selezionato.
     */
    function copiaElenco() {
        var selectedRadio = $(".checkboxElencoDocumentiAllegato_modale").filter(":checked");
        var elenco;
        // Se non ho selezionato nulla, comunico l'errore ed esco
        if(!selectedRadio.length) {
            impostaDatiNegliAlert(["Necessario selezionare un elenco"], alertErroriModaleRicercaElenco);
            return;
        }
        this.$spinnerConferma.addClass("activated");
        elenco = selectedRadio.data("originalElenco");
        // Copio i dati
        this.$descriptor.html(": " + elenco.anno + "/" + elenco.numero);
        this.$anno.val(elenco.anno);
        this.$numero.val(elenco.numero);
        this.$spinnerConferma.removeClass("activated");
        // Chiudo il modale
        $("#modaleAssociaElencoDocumentiAllegato").modal("hide");
    }

    /**
     * Inizializza la gestione dell'Elenco.
     *
     * @param pulsanteApertura   (String)  il selettore CSS del pulsante di apertura
     * @param urlAssociazione    (String)  l'URL di associazione dell'elenco
     * @param annoSelector       (String)  il selettore CSS dell'anno elenco (Optional, default: "")
     * @param numeroSelector     (String)  il selettore CSS del numero elenco (Optional, default: "")
     * @param descriptorSelector (String)  il selettore CSS del descrittore dell'elenco (Optional, default: "")
     * @param sendData           (Boolean) se inviare o meno i dati ottenuti alla action (Optional, default: false)
     * @param formToSubmit       (Boolean) se il form sia da inviare a seguito dell'associazione (Optional, default: false)
     * @param formSelector       (String)  il selettore CSS del form (Optional, default: "")
     * @param tableSelector      (String)  il selettore CSS della tabella degli elenchi (Optional, default: "")
     * @param urlEliminazione    (String)  l'url di eliminazione (Optional, default: undefined)
     */
    exports.inizializza = function(pulsanteApertura, urlAssociazione, annoSelector, numeroSelector, descriptorSelector, sendData, formToSubmit, formSelector, tableSelector, urlEliminazione) {
        // Creo un'istanza del gestore
        var gestoreElenco = new GestoreElenco(pulsanteApertura, urlAssociazione, annoSelector || "", numeroSelector || "", descriptorSelector || "",
                !!sendData, !!formToSubmit, formSelector || "", tableSelector || "", urlEliminazione);

        gestoreElenco.$pulsanteApertura.substituteHandler("click", gestoreElenco.apriModaleAssociaElenco.bind(gestoreElenco));
        gestoreElenco.$pulsanteRicerca.substituteHandler("click", gestoreElenco.ricercaElenchi.bind(gestoreElenco));
        gestoreElenco.$pulsanteConferma.substituteHandler("click", gestoreElenco.sendData ? gestoreElenco.associaElenco.bind(gestoreElenco) : gestoreElenco.copiaElenco.bind(gestoreElenco));
    };

    /**
     * Carica gli elenchi dell'allegato.
     *
     * @param url (String) l'url da cui ottenere la lista
     * @param sel (String) il selettore CSS della tabella
     *
     * @returns (Promise) the promise corresponding to the invocation
     */
    exports.caricaElenchi = function(url, sel) {
        return $.postJSON(url)
        .then(function(data) {
            caricaTabellaElenco(sel, {aaData: data.listaElencoDocumentiAllegato}, undefined);
            return data;
        });
    };

    /**
     * Carica gli elenchi dell'allegato, per l'aggiornamento (con una colonna in piu').
     *
     * @param url             (String) l'url da cui ottenere la lista
     * @param sel             (String) il selettore CSS della tabella
     * @param urlEliminazione (String) l'URL per l'eliminazione
     * @param evt             (Event)  l'eventuale evento da triggerare
     *
     * @returns (Promise) the promise corresponding to the invocation
     */
    exports.caricaElenchiAggiornamento = function(url, sel, urlEliminazione, evt) {
        return $.postJSON(url)
        .then(function(data) {
            var cols = augmentCols(urlEliminazione, url, sel);
            caricaTabellaElenco(sel, {aaData: data.listaElencoDocumentiAllegato}, cols);
            if(evt) {
                evt.totaleEntrataListaElencoDocumentiAllegato = data.totaleEntrataListaElencoDocumentiAllegato || 0;
                evt.totaleSpesaListaElencoDocumentiAllegato = data.totaleSpesaListaElencoDocumentiAllegato || 0;
                evt.totaleNettoListaElencoDocumentiAllegato = data.totaleNettoListaElencoDocumentiAllegato || 0;
                evt.listaElencoDocumentiAllegato = data.listaElencoDocumentiAllegato;
                $(document).trigger(evt);
            }
            return data;
        });
    };

    /**
     * Carica i dati dell'elenco.
     *
     * @param elenco     (Object) l'elenco da caricare
     * @param url        (String) l'url da invocare
     * @param selTabella (String) il selettore CSS della tabella da popolare
     * @param overlayDiv (String) il selettore del div per l'overlay
     * @param openDiv    (String) il selettore del div da aprire
     * @param moreCols   (Array)  le colonne aggiuntive (Optional, default: undefined)
     * @param subCols    (Object) le colonne da sostituire, della forma {colNumber: {newColumn}} (Optional, default: undefined)
     * @param startCol   (number) la colonna iniziale (Optional, default: 0)
     */
    exports.caricaDettaglioElenco = function(elenco, url, selTabella, overlayDiv, openDiv, moreCols, subCols, startCol) {
        var obj, div, $openDiv;
        if(!elenco) {
            // Se non ho l'elenco, esco
            return;
        }
        // Definisco gli oggetti dopo. Leggero miglioramento delle performance in caso di non selezione
        obj = {'elencoDocumentiAllegato.uid': elenco.uid};

        div = $(overlayDiv);
        div.length && div.overlay("show");
        $openDiv = $(openDiv).slideUp();
        $.postJSON(url, obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            exports.impostaDettaglioElenco(data.listaElementoElencoDocumentiAllegato, selTabella, moreCols, subCols, startCol);
            $openDiv.slideDown();
            chiudiFiltroQuote();
            $('#pulsanteRicercaSoggettoInElenco').substituteHandler('click', impostaDettaglioElencoFiltrato.bind(undefined, elenco.uid, moreCols, subCols));
        }).always(function() {
            div.length && div.overlay("hide");
        });
    };

    /**
     * Imposta il dettaglio dell'elenco.
     *
     * @param list     (Array)  la lista tramite cui popolare la tabella
     * @param selector (String) il selettore CSS della tabella
     * @param moreCols (Array)  le colonne aggiuntive (Optional, default: undefined)
     * @param subCols  (Object) le colonne da sostituire, della forma {colNumber: {newColumn}} (Optional, default: undefined)
     * @param startCol (number) la colonna iniziale (Optional, default: 0)
     */
    exports.impostaDettaglioElenco = function(list, selector, moreCols, subCols, startCol, sAjaxSource) {
        var cols = baseAoColumnDefsDettaglio().slice();
        var i;
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun subdocumento associato"
            },
            fnDrawCallback: function () {
                // Distruggo gli eventuali vecchi popover ancora aperti
                $(".popover.in").remove();
                // Applico la funzionalita' ai nuovi popover
                $("a[rel='popover']", selector)
                    .eventPreventDefault("click")
                    .popover();
            },
            aoColumnDefs: cols
        };
        var options;
        if(moreCols && moreCols.length) {
            // Aggiungo le azioni
            for(i = 0; i < moreCols.length; i++) {
                cols.push(moreCols[i]);
            }
        }
        if(subCols) {
            for(i in subCols) {
                if(Object.prototype.hasOwnProperty.call(subCols, i)) {
                    cols[i] = subCols[i];
                }
            }
        }
        
        if(startCol !== undefined) {
            opts.iDisplayStart = parseInt(startCol, 10);
        }
        if(sAjaxSource){
        	opts.sAjaxSource = sAjaxSource;
        }
        options = $.extend(true, {}, baseOptsDettaglioQuoteElenco, opts);
        $(selector).dataTable(options);
    };
    
   
    function impostaDettaglioElencoFiltrato(uidElenco, moreCols, subCols){
    	var url = 'ricercaDettaglioElencoDocumentiAllegato_caricaQuoteElencoFiltrate.do';
    	var soggettoCode = $('#codiceSoggetto').val();
    	var obj;
    	var divRicerca = $('#collapse_ricerca_in_elenco');
    	if(!uidElenco || !soggettoCode){
    		//non ho impostato il filtro di ricerca, non ha senso che io filtri
    		return;
    	}
    	divRicerca.overlay('show');
    	$('#dettaglioElementiCollegatiFiltrati').hide();
    	obj = {'elencoDocumentiAllegato.uid': uidElenco, 'soggetto.codiceSoggetto': soggettoCode};
    	$.postJSON(url, obj)
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, alertErrori)){
    			return;
    		}
    		exports.impostaDettaglioElenco(data.listaElementoElencoDocumentiAllegatoFiltrati, '#tabellaDettaglioElementiCollegati' + 'Filtrati' , moreCols, subCols,undefined, 'risultatiRicercaSinteticaQuoteElencoDocumentiAllegato.do');
    	})
    	.then(function(){
    		$('#dettaglioElementiCollegatiFiltrati').slideDown();
    	})
    	.always(divRicerca.overlay.bind(divRicerca, 'hide'));
    }

    function chiudiFiltroQuote(){
    	 var collapseToggle = $('#pulsanteApriRicercaQuoteInelenco');
         var collapse = $('#collapse_ricerca_in_elenco');
         if(collapse.hasClass('in')) {
             collapseToggle.click();
         }
         $('#dettaglioElementiCollegatiFiltrati').hide();
    }
    
    // Esporto i dati
    global.ElencoDocumentiAllegato = exports;
}(jQuery, this);