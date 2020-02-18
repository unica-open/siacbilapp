/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function (w, $) {
    'use strict';
    var exports = {};
    var Soggetto;
    var baseOpts = {
        bServerSide : false,
        bPaginate : true,
        bLengthChange : false,
        iDisplayLength : 5,
        bSort : false,
        bInfo : true,
        bAutoWidth : true,
        bFilter : false,
        bProcessing : true,
        bDestroy : true,
        oLanguage : {
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty : "0 risultati",
            sProcessing : "Attendere prego...",
            oPaginate : {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec.",
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    /**
     * Imposta la tabella delle sedi secondarie del soggetto.
     *
     * @param lista   (Array)  la lista delle sedi
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaSedeSecondariaSoggetto (lista, tabella) {
        var oldUid = parseInt($("#HIDDEN_sedeSecondariaSoggettoUid").val(), 10);
        var options = {
            aaData: lista || [],
            oLanguage: {
                sZeroRecords: "Non sono presenti sedi associate"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var campo = "<input type='radio' name='sedeSecondariaSoggetto.uid' value='" + source.uid + "'";
                    if(source.uid === oldUid) {
                        campo += " checked";
                    }
                    campo += " />";
                    return campo;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalData", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('denominazione')},
                {aTargets: [2], mData: function(source) {
                    var indirizzo = source.indirizzoSoggettoPrincipale;
                    if(!indirizzo) {
                        return "";
                    }
                    return indirizzo.sedime + " " + indirizzo.denominazione + ", " + indirizzo.numeroCivico;
                }},
                {aTargets: [3], mData: defaultPerDataTable('indirizzoSoggettoPrincipale.comune')},
                {aTargets: [4], mData: defaultPerDataTable('statoOperativoSedeSecondaria')}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        tabella.dataTable(opts);
    }

    /**
     * Crea il campo radio per la modalita di pagamento del soggetto per l'uid fornito, fornendo il check del campo nel caso in cui sia pari al precedente valore.
     *
     * @param uid    (Number) l'uid per cui creare il campo
     * @param oldUid (Number) il precedente uid
     * @param codSog (String) il codice del soggetto di cessione (Optional - default: undefined)
     */
    function creaRadioModalitaPagamentoSoggettoByUid(uid, oldUid, codSog) {
        var res = "<input type='radio' name='modalitaPagamentoSoggetto.uid' value='" + uid + "' ";
        if(uid === oldUid) {
            res += "checked ";
        }
        // Accetto anche la stringa vuota
        if(codSog !== undefined && codSog !== null) {
            res += "data-cessione-cod-soggetto=\"" + codSog + "\" ";
        }
        res += "/>";
        return res;
    }

    /**
     * Imposta la tabella delle modalita' di pagamento del soggetto.
     *
     * @param lista   (Array)  la lista delle modalita'
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaModalitaPagamentoSoggetto(lista, tabella) {
        var oldUid = parseInt($("#HIDDEN_modalitaPagamentoSoggettoUid").val(), 10);
        var options = {
            aaData: lista || [],
            oLanguage: {
                sZeroRecords: "Non sono presenti modalit&agrave; associate"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return creaRadioModalitaPagamentoSoggettoByUid(source.modalitaPagamentoSoggettoCessione2 ? source.modalitaPagamentoSoggettoCessione2.uid : source.uid, oldUid, source.cessioneCodSoggetto);
                }, fnCreatedCell: function(nTd, sData, oData) {
                    var self = $("input", nTd).data("originalData", oData);
                    if(oData.modalitaAccreditoSoggetto) {
                        self.data("codiceModalitaPagamento", oData.modalitaAccreditoSoggetto.codice);
                    }
                }},
                {aTargets: [1], mData: defaultPerDataTable('codiceModalitaPagamento')},
                {aTargets: [2], mData: defaultPerDataTable('descrizioneInfo.descrizioneArricchita')},
                {aTargets: [3], mData: defaultPerDataTable('associatoA')},
                {aTargets: [4], mData: defaultPerDataTable('descrizioneStatoModalitaPagamento')}
            ]
        };

        var opts = $.extend(true, {}, baseOpts, options);
        tabella.dataTable(opts);
    }

    /**
     * Imposta la descrizione del soggetto.
     *
     * @param soggetto (Object) il soggetto da impostare
     * @param element  (jQuery) l'elemento in cui impostare la descrizione
     */
    function impostaDescrizioneSoggetto(soggetto, element) {
        var denominazione;
        // Se non ho il soggetto, esco
        if(!soggetto) {
            return;
        }
        denominazione = componiDenominazione(soggetto);
        // Imposto i dati nello span
        element.html(denominazione);
    }

    /**
     * Composizione della denominzione del soggetto.
     *
     * @param soggetto (Object) il soggetto da cui ottenere i dati
     * @returns (String) la denominazione
     */
    function componiDenominazione(soggetto) {
        var arr = [];
        if(!soggetto) {
            return "";
        }
        pushInArray(soggetto.codiceSoggetto);
        pushInArray(soggetto.denominazione);
        pushInArray(soggetto.codiceFiscale);
        if(!arr.length) {
            return "";
        }
        return ": " + arr.join(" - "); 
    }

    /**
     * Aggiunge il dato nell'array se valorizzato.
     *
     * @param arr (Array)  l'array da popolare
     * @param val (String) il valore con cui popolare l'array
     */
    function pushInArray(arr, val) {
        if(val) {
            arr.push(val);
        }
    }
    /**
     * Controlla se il codice fiscale sia valido.
     *
     * @param cf (String) il codice fiscale da controllare
     * @returns (Boolean) <code>true</code> se il codice fiscele e' corretto; <code>false</code> altrimenti
     */
    function isCodiceFiscaleValido(cf) {
        return checkCodiceFiscale !== undefined ? checkCodiceFiscale(cf) : true;
    }

    /**
     * Costruttore per il soggetto
     *
     * @param campoCodiceSoggetto (jQuery)  il campo ove &eacute; presente il codice del soggetto
     * @param campoCodiceFiscale  (jQuery)  il campo ove &eacute; presente il codice fiscale
     * @param campoDenominazione  (jQuery)  il campo ove &eacute; presente la denominazione
     * @param campoDescrizione    (jQuery)  il campo ove &eacute; presente la descrizione
     * @param campoUid            (jQuery)  il campo ove &eacute; presente l'uid
     * @param campoAmbito         (jQuery)  il campo ove &eacute; presente l'ambito
     * @param pulsanteApertura    (jQuery)  il campo rappresentante il pulsante di apertura
     * @param blockCodiceSoggetto (Boolean) se il codice del soggetto debba essere bloccato alla modifica
     */
    Soggetto = function(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, campoUid, campoAmbito, pulsanteApertura, blockCodiceSoggetto) {
        this.$codice = $(campoCodiceSoggetto);
        this.$codiceFiscale = $(campoCodiceFiscale);
        this.$denominazione = $(campoDenominazione);
        this.$descrizione = $(campoDescrizione);
        this.$uid = $(campoUid);
        this.$pulsanteApertura = $(pulsanteApertura);
        this.blockCodiceSoggetto = !!blockCodiceSoggetto;

        // Campi modale
        this.$codiceSoggetto = $("#codiceSoggetto_modale");
        this.$pulsanteCerca = $("#pulsanteCercaSoggetto");
        this.$pulsanteConferma = $("#pulsanteConfermaSoggetto");
        this.$campoCodiceFiscale = $("#codiceFiscaleSoggetto_modale");
        this.$tableSoggetti = $("#risultatiRicercaSoggetti");
        this.$fieldsetRicerca = $("#fieldsetRicercaGuidateSoggetto");
        this.$divTabella = $("#divTabellaSoggetti");
        this.$spinner = $("#SPINNER_RicercaSoggetto");
        this.$modale = $("#modaleGuidaSoggetto");

        // Dati di utilita'
        this.ambito = $(campoAmbito).val() || "";
        this.$alertErrori = $("#ERRORI_MODALE_SOGGETTO");

        // Iniziailzzazione
        this.init();
    };

    /** Costruttore */
    Soggetto.prototype.constructor = Soggetto;

    /** Inizializzazione dei campi */
    Soggetto.prototype.init = function() {
        this.$pulsanteApertura.substituteHandler("click", this.apriModaleSoggetto.bind(this));
        this.$campoCodiceFiscale.substituteHandler("keypress", this.delayControlloCodiceFiscale.bind(this));
        $("form").on("reset", this.cleanForm.bind(this));
    };

    /** Pulizia del form */
    Soggetto.prototype.cleanForm = function() {
        this.$modale.find(":input").val("").end()
        .find("#divTabellaSoggetti").slideUp().end()
        .find("#ERRORI_MODALE_SOGGETTO").slideUp();
    };

    /** Delay per il calcolo del codice fiscale */
    Soggetto.prototype.delayControlloCodiceFiscale = function() {
        return setTimeout(this.calcoloCodiceFiscale.bind(this), 1);
    };

    /** Inizializzazione della modale */
    Soggetto.prototype.initModale = function() {
        this.$pulsanteCerca.substituteHandler("click", this.cercaSoggetti.bind(this));
        this.$pulsanteConferma.substituteHandler("click", this.impostaSoggetto.bind(this));
    };

    /**
     * Caricamento via Ajax della tabella dei soggetti e visualizzazione.
     *
     * @param lista (Array) la lista dei soggetti da utilizzare per la creazione della tabella
     */
    Soggetto.prototype.tabellaSoggetti = function(lista) {
        var options = {
            bSort : true,
            aaData : lista || [],
            oLanguage : {
                sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            aoColumnDefs : [
                {aTargets : [ 0 ], mData : function() {
                    return "<input type='radio' name='checkSoggetto'/>";
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalSoggetto", oData);
                }},
                {aTargets : [ 1 ], mData : defaultPerDataTable("codiceSoggetto")},
                {aTargets : [ 2 ], mData : defaultPerDataTable("codiceFiscale"), bSortable: false},
                {aTargets : [ 3 ], mData : defaultPerDataTable("partitaIva"), bSortable: false},
                {aTargets : [ 4 ], mData : defaultPerDataTable("denominazione")},
                {aTargets : [ 5 ], mData : defaultPerDataTable("statoOperativo"), bSortable: false}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);

        if($.fn.DataTable.fnIsDataTable(this.$tableSoggetti[0])) {
            this.$tableSoggetti.dataTable().fnDestroy();
        }
        this.$tableSoggetti.dataTable(opts);
    };

    /**
     * Richiama l'esecuzione della ricerca Soggetto.
     *
     * @param e (Event) l'evento invocante la funzione
     * @returns (Promise) la promise legata all'invocazione della ricerca soggetto
     */
    Soggetto.prototype.cercaSoggetti = function(e) {
        var oggettoPerChiamataAjax = unqualify(this.$fieldsetRicerca.serializeObject(), 1);
        oggettoPerChiamataAjax.codiceAmbito = this.ambito;

        e.preventDefault();
        this.$divTabella.slideUp();
        this.$alertErrori.slideUp();

        this.$spinner.addClass("activated");
        return $.postJSON("ricercaSinteticaSoggetto.do", oggettoPerChiamataAjax)
        .then(this.callbackRicercaSoggetti.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, "activated"));
    };

    /**
     * Callback per la ricerca dei soggetti.
     *
     * @param data (Object) i dati della risposta della chiamata
     */
    Soggetto.prototype.callbackRicercaSoggetti = function(data) {
        if(impostaDatiNegliAlert(data.errori, this.$alertErrori, false)) {
            return;
        }

        // Carico i dati in tabella
        this.tabellaSoggetti(data.listaSoggetti);
        this.$divTabella.slideDown();
    };

    /**
     * Imposta i dati del soggetto all'interno dei campi hidden.
     *
     * @param e (Event) l'evento scatenante la funzione
     */
    Soggetto.prototype.impostaSoggetto = function(e) {
        var checkedSoggetto = this.$tableSoggetti.find("[name='checkSoggetto']:checked");
        var soggetto;
        var codiceSoggetto;
        var codiceFiscaleSoggetto;
        var denominazioneSoggetto;
        var operazioneDenominazione;

        e.preventDefault();
        if(checkedSoggetto.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un soggetto"], this.$alertErrori, false);
            return;
        }

        operazioneDenominazione = this.$denominazione.is(':input') ? 'val' : 'html';
        soggetto = checkedSoggetto.data("originalSoggetto");

        codiceSoggetto = soggetto.codiceSoggetto;
        codiceFiscaleSoggetto = soggetto.codiceFiscale;
        denominazioneSoggetto = soggetto.denominazione;

        // Copio i dati
        this.$codice.val(codiceSoggetto);
        this.$codiceFiscale.val(codiceFiscaleSoggetto);
        this.$denominazione[operazioneDenominazione](denominazioneSoggetto);
        this.$uid.val(soggetto.uid);
        this.$descrizione.html(": " + codiceSoggetto + " - " + denominazioneSoggetto + " - " + codiceFiscaleSoggetto);

        // Blocco il campo del codice se necessario
        if(this.blockCodiceSoggetto) {
            this.$codice.attr("readonly", "readonly");
        }
        this.$modale.modal("hide");
        this.$codice.trigger("codiceSoggettoCaricato");
    };

    /**
     * Calcola il codice fiscale.
     */
    Soggetto.prototype.calcoloCodiceFiscale = function() {
        var codiceFiscale = this.$campoCodiceFiscale.val().toUpperCase();
        var codiceFiscaleValido = true;
        if(codiceFiscale.length === 16) {
            codiceFiscaleValido = isCodiceFiscaleValido(codiceFiscale);
        }

        this.$campoCodiceFiscale.val(codiceFiscale);

        if(codiceFiscaleValido) {
            this.$alertErrori.slideUp();
        } else {
            impostaDatiNegliAlert(["Il codice fiscale inserito non è sintatticamente corretto"], this.$alertErrori, false);
        }
    };

    /**
     * Apre il modale del soggetto
     *
     * @param e (Event) l'evento invocante la funzione
     */
    Soggetto.prototype.apriModaleSoggetto = function(e) {
        e.preventDefault();
        this.$alertErrori.slideUp();
        this.$divTabella.slideUp();
        this.copiaDatiApertura();

        this.initModale();
        // Apro il modale
        this.$modale.modal("show");
    };

    /** Copia dei dati per l'apertura del modale */
    Soggetto.prototype.copiaDatiApertura = function() {
        this.$codiceSoggetto.val(this.$codice.val());
    };

    /**
     * Inizializza la gestione.
     *
     * @param campoCodiceSoggetto (String)  il campo ove impostare il codice del soggetto (Optional - default: #codiceSoggetto)
     * @param campoCodiceFiscale  (String)  il campo ove impostare il codice fiscale (Optional - default: #HIDDEN_soggettoCodiceFiscale)
     * @param campoDenominazione  (String)  il campo ove impostare la denominazione (Optional - default: #HIDDEN_soggettoDenominazione)
     * @param campoDescrizione    (String)  il campo ove impostare la descrizione completa del soggetto (Optional - default: #descrizioneCompletaSoggetto)
     * @param pulsanteApertura    (String)  il pulsante di apertura modale (Optional - default: #pulsanteApriModaleSoggetto)
     * @param campoAmbito         (String)  il campo da cui reperire l'ambito (Optional - default: '')
     * @param campoUid            (String)  il campo da cui reperire uid (Optional - default: '')
     * @param blockCodiceSoggetto (Boolean) se il codice soggetto debba essere bloccato (Optional - default: true)
     */
    exports.inizializza = function(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, pulsanteApertura, campoAmbito, campoUid, blockCodiceSoggetto) {
        var codiceSoggetto = campoCodiceSoggetto || "#codiceSoggetto";
        var codiceFiscale = campoCodiceFiscale || "#HIDDEN_soggettoCodiceFiscale";
        var denominazione = campoDenominazione || "#HIDDEN_soggettoDenominazione";
        var descrizione = campoDescrizione || "#descrizioneCompletaSoggetto";
        var uid = campoUid || "#HIDDEN_soggettoUid";
        var ambito = campoAmbito || "";
        var apertura = pulsanteApertura || "#pulsanteApriModaleSoggetto";
        var block = blockCodiceSoggetto === undefined ? true : blockCodiceSoggetto;

        // Inizializzazione
        var soggetto = new Soggetto(codiceSoggetto, codiceFiscale, denominazione, descrizione, uid, ambito, apertura, block);
        $(apertura).data('compilazioneGuidataSoggetto', soggetto);
    };

    /**
     * Carica il dettaglio del soggetto e appone i dati nella pagina.
     *
     * @param campoCodiceSoggetto         (jQuery)  il campo ove &eacute; presente il codice del soggetto
     * @param accordionSedeSecondaria     (jQuery)  l'accordion delle sedi secondarie
     * @param accordionModalitaPagamento  (jQuery)  l'accordion delle modalita di pagamento
     * @param elementoDescrizioneSoggetto (jQuery)  l'elemento ove impostare la descrizione
     * @param doNotCloseModals            (boolean) se i modali non debbano essere chiusi (optional - default: false)
     * @param campoUidSoggetto            (jQuery)  il campo ove &eacute; presente l'uid (optional - default: '')
     *
     * @returns (Promise) l'oggetto deferred corrispondente all'invocazione del servizio
     */
    exports.caricaDettaglioSoggetto = function(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento,
            elementoDescrizioneSoggetto, /* Optional */ doNotCloseModals, /* Optional */campoUidSoggetto, /* Optional */alert) {

        var alertErroriSoggetto = alert || $("#ERRORI");
        // Inizializzo l'Overlay
        campoCodiceSoggetto.overlay("show");
        if(!campoCodiceSoggetto.val()) {
            // Nascondere i div degli accordion
            accordionSedeSecondaria.slideUp();
            accordionModalitaPagamento.slideUp();
            // Svuotare la descrizione
            elementoDescrizioneSoggetto.html("");
            campoCodiceSoggetto.overlay("hide");
            return $.Deferred().reject().promise();
        }
        // Inizializzo gli altri Overlay
        accordionSedeSecondaria.slideDown().overlay("show");
        accordionModalitaPagamento.slideDown().overlay("show");
        return $.postJSON("ricercaSoggettoPerChiave.do", {"codiceSoggetto": campoCodiceSoggetto.val()})
        .then(function(data) {
            var tabellaSedeSecondaria;
            var tabellaModalitaPagamento;
            if(impostaDatiNegliAlert(data.errori, alertErroriSoggetto, true, !doNotCloseModals)) {
                // Se ho errori, esco
                // Svuoto la descrizione del soggetto
                elementoDescrizioneSoggetto.html("");
                // Chiudo gli accordion
                accordionSedeSecondaria.slideUp();
                accordionModalitaPagamento.slideUp();
                return;
            }
            // Carico il soggetto
            impostaDescrizioneSoggetto(data.soggetto, elementoDescrizioneSoggetto);
            if (campoUidSoggetto) {
                campoUidSoggetto.val(data.soggetto.uid);
            }
            // Carico gli accordion
            if(accordionSedeSecondaria.length || accordionModalitaPagamento.length) {
                tabellaSedeSecondaria = accordionSedeSecondaria.find("table");
                tabellaModalitaPagamento = accordionModalitaPagamento.find("table");
                impostaTabellaSedeSecondariaSoggetto(data.listaSedeSecondariaSoggettoValide, tabellaSedeSecondaria);
                impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggettoValide, tabellaModalitaPagamento);
            }
        }).always(function() {
            campoCodiceSoggetto.overlay("hide");
            accordionSedeSecondaria.overlay("hide");
            accordionModalitaPagamento.overlay("hide");
        });
    };

    /**
     * Filtra la modalita di pagamento.
     *
     * @param uid       (Number) l'uid per cui filtrare
     * @param tabella   (jQuery) la tabella da popolare
     * @param accordion (jQuery) l'accordion su cui fare overlay (Optional, default: undefined)
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione del servizio
     */
    exports.filterModalitaPagamentoSoggetto = function(uid, tabella, accordion) {
        var acc = accordion || $('');
        acc.overlay("show");
        return $.postJSON("filtraModalitaPagamentoRispettoSedeSecondaria.do", {uidSedeSecondariaSoggetto: uid})
        .then(function(data) {
            impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggettoValide, tabella);
        }).always(acc.overlay.bind(acc, false));
    };

    /**
     * Carica la vecchia sede secondaria.
     *
     * @param accordion (jQuery) l'accordion
     * @param uid       (Number) l'uid per cui filtrare
     */
    exports.caricaOldSedeSecondaria = function(accordion, uid) {
        if(!uid) {
            return;
        }
        accordion.find("table")
            .dataTable()
            .$("input[type='radio'][name='sedeSecondariaSoggetto.uid'][value='" + uid + "']")
                .prop("checked", true)
                .trigger("change");
    };

    /**
     * Carica la vecchia modalita di pagamento.
     *
     * @param accordion (jQuery) l'accordion
     * @param uid       (Number) l'uid per cui filtrare
     */
    exports.caricaOldModalitaPagamentoSoggetto = function(accordion, uid) {
        if(!uid) {
            return;
        }
        accordion.find("table")
            .dataTable()
            .$("input[type='radio'][name='modalitaPagamentoSoggetto.uid'][value='" + uid + "']")
                .prop("checked", true);
    };

    w.Soggetto = exports;
}(this, jQuery);