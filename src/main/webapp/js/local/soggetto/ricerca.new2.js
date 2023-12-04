/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function (w, $) {
    'use strict';

    var exports = {};
    var counter = 0;
    var instances = {};
    var Soggetto;
    var baseOpts = {
        bServerSide: false,
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
            sInfo: '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty: '0 risultati',
            sProcessing: 'Attendere prego...',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun dato disponibile'
            }
        }
    };

    // Export sul namespace 'Soggetto'
    w.Soggetto = exports;

    /**
     * Costruttore per il soggetto
     *
     * @param campoCodiceSoggetto        (string)  il campo ove &eacute; presente il codice del soggetto
     * @param campoCodiceFiscale         (string)  il campo ove &eacute; presente il codice fiscale
     * @param campoDenominazione         (string)  il campo ove &eacute; presente la denominazione
     * @param campoDescrizione           (string)  il campo ove &eacute; presente la descrizione
     * @param campoUid                   (string)  il campo ove &eacute; presente l'uid
     * @param campoAmbito                (string)  il campo ove &eacute; presente l'ambito
     * @param pulsanteApertura           (string)  il campo rappresentante il pulsante di apertura
     * @param accordionSedeSecondaria    (string)  l'accordion della sede secondaria
     * @param accordionModalitaPagamento (string)  l'accordion della modalita' di pagamento
     * @param blockCodiceSoggetto        (boolean) se il codice del soggetto debba essere bloccato alla modifica
     */
    Soggetto = function(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, campoUid, campoAmbito, pulsanteApertura,
            accordionSedeSecondaria, accordionModalitaPagamento, blockCodiceSoggetto) {
        this.$codice = $(campoCodiceSoggetto || '#codiceSoggetto');
        this.$codiceFiscale = $(campoCodiceFiscale || '#HIDDEN_soggettoCodiceFiscale');
        this.$denominazione = $(campoDenominazione || '#HIDDEN_soggettoDenominazione');
        this.$descrizione = $(campoDescrizione || '#descrizioneCompletaSoggetto');
        this.$uid = $(campoUid || '#HIDDEN_soggettoUid');
        this.$pulsanteApertura = $(pulsanteApertura || '#pulsanteApriModaleSoggetto');
        this.$sedeSecondaria = $(accordionSedeSecondaria || '');
        this.$modalitaPagamento = $(accordionModalitaPagamento || '');

        this.blockCodiceSoggetto = blockCodiceSoggetto === undefined ? true : !!blockCodiceSoggetto;

        // Campi modale
        this.$codiceSoggetto = $('#codiceSoggetto_modale');
        this.$pulsanteCerca = $('#pulsanteCercaSoggetto');
        this.$pulsanteConferma = $('#pulsanteConfermaSoggetto');
        this.$campoCodiceFiscale = $('#codiceFiscaleSoggetto_modale');
        this.$tableSoggetti = $('#risultatiRicercaSoggetti');
        this.$fieldsetRicerca = $('#fieldsetRicercaGuidateSoggetto');
        this.$divTabella = $('#divTabellaSoggetti');
        this.$spinner = $('#SPINNER_RicercaSoggetto');
        this.$modale = $('#modaleGuidaSoggetto');

        // Dati di utilita'
        this.ambito = $(campoAmbito || '').val() || '';
        this.$alertErrori = $('#ERRORI_MODALE_SOGGETTO');

        // Iniziailzzazione
        this.init();
    };

    Soggetto.prototype.constructor = Soggetto;
    Soggetto.prototype.init = init;
    Soggetto.prototype.cleanForm = cleanForm;
    Soggetto.prototype.delayControlloCodiceFiscale = delayControlloCodiceFiscale;
    Soggetto.prototype.initModale = initModale;
    Soggetto.prototype.tabellaSoggetti = tabellaSoggetti;
    Soggetto.prototype.cercaSoggetti = cercaSoggetti;
    Soggetto.prototype.callbackRicercaSoggetti = callbackRicercaSoggetti;
    Soggetto.prototype.impostaSoggetto = impostaSoggetto;
    Soggetto.prototype.calcoloCodiceFiscale = calcoloCodiceFiscale;
    Soggetto.prototype.apriModaleSoggetto = apriModaleSoggetto;
    Soggetto.prototype.copiaDatiApertura = copiaDatiApertura;
    Soggetto.prototype.caricaDettaglioSoggettoInner = caricaDettaglioSoggettoInner;
    Soggetto.prototype.caricaDettaglioSoggettoInnerCallback = caricaDettaglioSoggettoInnerCallback;

    exports.inizializza = inizializza;
    exports.bindCaricaDettaglioSoggetto = bindCaricaDettaglioSoggetto;

    /**
     * Composizione della denominzione del soggetto.
     * @param soggetto (any) il soggetto da cui ottenere i dati
     * @returns (string) la denominazione
     */
    function componiDenominazione(soggetto) {
        var arr = [];
        if(!soggetto) {
            return '';
        }
        pushInArray(arr, soggetto.codiceSoggetto);
        pushInArray(arr, soggetto.denominazione);
        pushInArray(arr, soggetto.codiceFiscale);
        if(!arr.length) {
            return '';
        }
        return ': ' + arr.join(' - '); 
    }
    /**
     * Aggiunge il dato nell'array se valorizzato.
     * @param arr (string[])  l'array da popolare
     * @param val (string) il valore con cui popolare l'array
     */
    function pushInArray(arr, val) {
        if(val) {
            arr.push(val);
        }
    }

    /**
     * Imposta la tabella delle sedi secondarie del soggetto.
     * @param lista   (any[])  la lista delle sedi
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaSedeSecondariaSoggetto (lista, tabella) {
        var oldUid = +$('#HIDDEN_sedeSecondariaSoggettoUid').val();
        var options = {
            aaData: lista || [],
            oLanguage: {
                sZeroRecords: 'Non sono presenti sedi associate'
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var campo = '<input type="radio" name="sedeSecondariaSoggetto.uid" value="' + source.uid + '"';
                    if(source.uid === oldUid) {
                        campo += ' checked';
                    }
                    return campo + ' />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalData', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('denominazione')},
                {aTargets: [2], mData: calcolaIndirizzoSoggetto},
                {aTargets: [3], mData: defaultPerDataTable('indirizzoSoggettoPrincipale.comune')},
                {aTargets: [4], mData: defaultPerDataTable('statoOperativoSedeSecondaria')}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        tabella.dataTable(opts);
    }
    /**
     * Calcolo dell'indirizzo del soggetto
     * @param source (any) la sede
     * @return (string) l'indirizzo
     */
    function calcolaIndirizzoSoggetto(source) {
        var indirizzo = source.indirizzoSoggettoPrincipale;
        if(!indirizzo) {
            return '';
        }
        return indirizzo.sedime + ' ' + indirizzo.denominazione + ', ' + indirizzo.numeroCivico;
    }

    /**
     * Imposta la tabella delle modalita' di pagamento del soggetto.
     *
     * @param lista   (any[])  la lista delle modalita'
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaModalitaPagamentoSoggetto(lista, tabella) {
        var oldUid = +$('#HIDDEN_modalitaPagamentoSoggettoUid').val();
        var options = {
            aaData: lista || [],
            oLanguage: {
                sZeroRecords: 'Non sono presenti modalit&agrave; associate'
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return creaRadioModalitaPagamentoSoggettoByUid(source.modalitaPagamentoSoggettoCessione2 ? source.modalitaPagamentoSoggettoCessione2.uid : source.uid, oldUid, source.cessioneCodSoggetto);
                }, fnCreatedCell: function(nTd, sData, oData) {
                    var self = $('input', nTd).data('originalData', oData);
                    if(oData.modalitaAccreditoSoggetto) {
                        self.data('codiceModalitaPagamento', oData.modalitaAccreditoSoggetto.codice);
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
     * Crea il campo radio per la modalita di pagamento del soggetto per l'uid fornito, fornendo il check del campo nel caso in cui sia pari al precedente valore.
     * @param uid    (number) l'uid per cui creare il campo
     * @param oldUid (number) il precedente uid
     * @param codSog (string) il codice del soggetto di cessione (Optional - default: undefined)
     * @return (string) il radio
     */
    function creaRadioModalitaPagamentoSoggettoByUid(uid, oldUid, codSog) {
        var res = '<input type="radio" name="modalitaPagamentoSoggetto.uid" value="' + uid + '" ';
        if(uid === oldUid) {
            res += 'checked ';
        }
        // Accetto anche la stringa vuota
        if(codSog !== undefined && codSog !== null) {
            res += 'data-cessione-cod-soggetto="' + codSog + '" ';
        }
        return res + '/>';
    }

    /**
     * Controlla se il codice fiscale sia valido.
     * @param cf (string) il codice fiscale da controllare
     * @returns (boolean) <code>true</code> se il codice fiscele e' corretto; <code>false</code> altrimenti
     */
    function isCodiceFiscaleValido(cf) {
        return checkCodiceFiscale !== undefined ? checkCodiceFiscale(cf) : true;
    }

    /** Inizializzazione dei campi */
    function init() {
        this.$pulsanteApertura.substituteHandler('click', this.apriModaleSoggetto.bind(this));
        this.$campoCodiceFiscale.substituteHandler('keypress', this.delayControlloCodiceFiscale.bind(this));
        $('form').on('reset', this.cleanForm.bind(this));
    }

    /** Pulizia del form */
    function cleanForm() {
        this.$modale
            .find(':input').val('').end()
            .find('#divTabellaSoggetti').slideUp().end()
            .find('#ERRORI_MODALE_SOGGETTO').slideUp();
    }

    /**
     * Delay per il calcolo del codice fiscale
     * @return (number) l'id del timeout
     */
    function delayControlloCodiceFiscale() {
        return setTimeout(this.calcoloCodiceFiscale.bind(this), 1);
    }

    /** Inizializzazione della modale */
    function initModale() {
        this.$pulsanteCerca.substituteHandler('click', this.cercaSoggetti.bind(this));
        this.$pulsanteConferma.substituteHandler('click', this.impostaSoggetto.bind(this));
    }

    /**
     * Caricamento via Ajax della tabella dei soggetti e visualizzazione.
     * @param lista (any[]) la lista dei soggetti da utilizzare per la creazione della tabella
     */
    function tabellaSoggetti(lista) {
        var options = {
            bSort: true,
            aaData: lista || [],
            oLanguage: {
                sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti'
            },
            aoColumnDefs: [
                {aTargets: [0], mData : function() {
                    return '<input type="radio" name="checkSoggetto"/>';
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalSoggetto', oData);
                }},
                {aTargets: [1], mData : defaultPerDataTable('codiceSoggetto')},
                {aTargets: [2], mData : defaultPerDataTable('codiceFiscale'), bSortable: false},
                {aTargets: [3], mData : defaultPerDataTable('partitaIva'), bSortable: false},
                {aTargets: [4], mData : defaultPerDataTable('denominazione')},
                {aTargets: [5], mData : defaultPerDataTable('statoOperativo'), bSortable: false}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);

        if($.fn.DataTable.fnIsDataTable(this.$tableSoggetti[0])) {
            this.$tableSoggetti.dataTable().fnDestroy();
        }
        this.$tableSoggetti.dataTable(opts);
    }

    /**
     * Richiama l'esecuzione della ricerca Soggetto.
     * @param e (Event) l'evento invocante la funzione
     * @returns (Promise) la promise legata all'invocazione della ricerca soggetto
     */
    function cercaSoggetti(e) {
        var oggettoPerChiamataAjax = unqualify(this.$fieldsetRicerca.serializeObject(), 1);
        oggettoPerChiamataAjax.codiceAmbito = this.ambito;

        e.preventDefault();
        this.$divTabella.slideUp();
        this.$alertErrori.slideUp();

        this.$spinner.addClass('activated');

        return $.postJSON('ricercaSinteticaSoggetto.do', oggettoPerChiamataAjax)
        .then(this.callbackRicercaSoggetti.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, 'activated'));
    }

    /**
     * Callback per la ricerca dei soggetti.
     * @param data (any) i dati della risposta della chiamata
     */
    function callbackRicercaSoggetti(data) {
        if(impostaDatiNegliAlert(data.errori, this.$alertErrori, false)) {
            return;
        }

        // Carico i dati in tabella
        this.tabellaSoggetti(data.listaSoggetti);
        this.$divTabella.slideDown();
    }

    /**
     * Imposta i dati del soggetto all'interno dei campi hidden.
     * @param e (Event) l'evento scatenante la funzione
     */
    function impostaSoggetto(e) {
        var checkedSoggetto = this.$tableSoggetti.find('[name="checkSoggetto"]:checked');
        var soggetto;
        var codiceSoggetto;
        var codiceFiscaleSoggetto;
        var denominazioneSoggetto;
        var operazioneDenominazione;

        e.preventDefault();
        if(checkedSoggetto.length === 0) {
            impostaDatiNegliAlert(['Necessario selezionare un soggetto'], this.$alertErrori, false);
            return;
        }

        operazioneDenominazione = this.$denominazione.is(':input') ? 'val' : 'html';
        soggetto = checkedSoggetto.data('originalSoggetto');

        codiceSoggetto = soggetto.codiceSoggetto;
        codiceFiscaleSoggetto = soggetto.codiceFiscale;
        denominazioneSoggetto = soggetto.denominazione;

        // Copio i dati
        this.$codice.val(codiceSoggetto);
        this.$codiceFiscale.val(codiceFiscaleSoggetto);
        this.$denominazione[operazioneDenominazione](denominazioneSoggetto);
        this.$uid.val(soggetto.uid);
        
        this.$descrizione.html(componiDenominazione(soggetto));

        // Blocco il campo del codice se necessario
        if(this.blockCodiceSoggetto) {
            this.$codice.attr('readonly', 'readonly');
        }
        this.$modale.modal('hide');
        this.$codice.trigger('codiceSoggettoCaricato').trigger('change');
    }

    /** Calcola il codice fiscale. */
    function calcoloCodiceFiscale () {
        var codiceFiscale = this.$campoCodiceFiscale.val().toUpperCase();
        var codiceFiscaleValido = true;
        if(codiceFiscale.length === 16) {
            codiceFiscaleValido = isCodiceFiscaleValido(codiceFiscale);
        }

        this.$campoCodiceFiscale.val(codiceFiscale);

        if(codiceFiscaleValido) {
            this.$alertErrori.slideUp();
        } else {
            impostaDatiNegliAlert(['Il codice fiscale inserito non è sintatticamente corretto'], this.$alertErrori, false);
        }
    }

    /**
     * Apre il modale del soggetto
     * @param e (Event) l'evento invocante la funzione
     */
    function apriModaleSoggetto(e) {
        e.preventDefault();
        this.$alertErrori.slideUp();
        this.$divTabella.slideUp();
        this.copiaDatiApertura();

        this.initModale();
        // Apro il modale
        this.$modale.modal('show');
    }

    /** Copia dei dati per l'apertura del modale */
    function copiaDatiApertura() {
        this.$codiceSoggetto.val(this.$codice.val());
    }

    /**
     * Caricamento del dettaglio del soggetto
     * @returns (Promise) l'oggetto deferred corrispondente all'invocazione del servizio
     */
    function caricaDettaglioSoggettoInner() {
        var campiOverlay = this.$codice.add(this.$sedeSecondaria).add(this.$modalitaPagamento);
        // Inizializzo l'Overlay
        if(!this.$codice.val()) {
            // Nascondere i div degli accordion
            this.$sedeSecondaria.slideUp();
            this.$modalitaPagamento.slideUp();
            // Svuotare la descrizione
            this.$descrizione.html('');
            return $.Deferred().reject().promise();
        }
        // Inizializzo gli altri Overlay
        this.$sedeSecondaria.slideDown();
        this.$modalitaPagamento.slideDown();
        
        campiOverlay.overlay('show');
        return $.postJSON('ricercaSoggettoPerChiave.do', {'codiceSoggetto': this.$codice.val()})
        .then(this.caricaDettaglioSoggettoInnerCallback.bind(this))
        .always(campiOverlay.overlay.bind(campiOverlay, 'hide'));
    }

    /**
     * Callback del caricamento soggetto
     * @param data (any) i dati del soggetto
     */
    function caricaDettaglioSoggettoInnerCallback(data) {
        var alertErroriSoggetto = $('#ERRORI');
        var tabellaSedeSecondaria;
        var tabellaModalitaPagamento;
        if(impostaDatiNegliAlert(data.errori, alertErroriSoggetto, true, true)) {
            // Se ho errori, esco
            // Svuoto la descrizione del soggetto
            this.$descrizione.html('');
            // Chiudo gli accordion
            this.$sedeSecondaria.slideUp();
            this.$modalitaPagamento.slideUp();
            return $.Deferred().reject().promise();
        }
        // Carico il soggetto
        this.$descrizione.html(componiDenominazione(data.soggetto));
        if (this.$uid) {
            this.$uid.val(data.soggetto.uid);
        }
        // Carico gli accordion
        if(this.$sedeSecondaria.length) {
            tabellaSedeSecondaria = this.$sedeSecondaria.find('table');
            impostaTabellaSedeSecondariaSoggetto(data.listaSedeSecondariaSoggettoValide, tabellaSedeSecondaria);
        }
        if(this.$modalitaPagamento.length) {
            tabellaModalitaPagamento = this.$modalitaPagamento.find('table');
            impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggettoValide, tabellaModalitaPagamento);
        }
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoCodiceSoggetto        (string)  il campo ove impostare il codice del soggetto (Optional - default: #codiceSoggetto)
     * @param campoCodiceFiscale         (string)  il campo ove impostare il codice fiscale (Optional - default: #HIDDEN_soggettoCodiceFiscale)
     * @param campoDenominazione         (string)  il campo ove impostare la denominazione (Optional - default: #HIDDEN_soggettoDenominazione)
     * @param campoDescrizione           (string)  il campo ove impostare la descrizione completa del soggetto (Optional - default: #descrizioneCompletaSoggetto)
     * @param pulsanteApertura           (string)  il pulsante di apertura modale (Optional - default: #pulsanteApriModaleSoggetto)
     * @param campoAmbito                (string)  il campo da cui reperire l'ambito (Optional - default: '')
     * @param campoUid                   (string)  il campo da cui reperire uid (Optional - default: '')
     * @param accordionSedeSecondaria    (string)  l'accordion della sede secondaria (Optional - default: '')
     * @param accordionModalitaPagamento (string)  l'accordion della modalita' di pagamento (Optional - default: '')
     * @param blockCodiceSoggetto        (boolean) se il codice soggetto debba essere bloccato (Optional - default: true)
     * @return (number) l'id dell'istanza
     */
    function inizializza(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, pulsanteApertura, campoAmbito, campoUid,
            accordionSedeSecondaria, accordionModalitaPagamento, blockCodiceSoggetto) {
        var id = counter++;
        var soggetto = new Soggetto(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, campoUid, campoAmbito, pulsanteApertura,
                accordionSedeSecondaria, accordionModalitaPagamento, blockCodiceSoggetto);
        // Salvo l'istanza
        instances[id] = soggetto;
        return id;
    }
    /**
     * Lega il caricamento del dettaglio del soggetto e l'apposizione dei dati nella pagina al pulsante.
     * @param id            (number) l'id dell'istanza
     * @param triggerChange (boolean) se effettuare il primo change
     */
    function bindCaricaDettaglioSoggetto(id, initialTrigger) {
        var soggetto = instances[id];
        if(!soggetto) {
            return;
        }
        
        if (soggetto.$codice.data('carica-dettaglio') === false) {
        	return;
        }
        
        soggetto.$codice.substituteHandler('change', soggetto.caricaDettaglioSoggettoInner.bind(soggetto));
        if(initialTrigger) {
            soggetto.$codice.trigger('change');
        }
    }

}(this, jQuery);