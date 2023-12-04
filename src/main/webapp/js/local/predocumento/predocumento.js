/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var PreDocumento = (function($) {
    'use strict';

    var exports = {};

    // L'alert degli errori
    var alertErrori = $('#ERRORI');

    // Opzioni base per il dataTable
    var optionsBaseDataTable = {
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

    var datiCausale = {
        'spesa':   {'causale': 'causaleSpesa',   'capitolo': 'capitoloUscitaGestione',  'movimento': 'impegno',      'submovimento': 'subImpegno'},
        'entrata': {'causale': 'causaleEntrata', 'capitolo': 'capitoloEntrataGestione', 'movimento': 'accertamento', 'submovimento': 'subAccertamento'}
    };
    
    exports.apriModaleSoggetto = apriModaleSoggetto;
    exports.impostaPeriodoCompetenza = impostaPeriodoCompetenza;
    exports.caricaListaCausaleSpesa = caricaListaCausaleSpesa;
    exports.caricaListaCausaleEntrata = caricaListaCausaleEntrata;
    exports.caricaListaCausaleEntrataNonAnnullate = caricaListaCausaleEntrataNonAnnullate;
    exports.caricaSedeSecondariaEModalitaPagamento = caricaSedeSecondariaEModalitaPagamento;
    exports.precaricaSedeSecondariaEModalitaPagamento = precaricaSedeSecondariaEModalitaPagamento;
    exports.apriModaleImpegno = apriModaleImpegno;
    exports.apriModaleAccertamento = apriModaleAccertamento;
    exports.caricaStruttureAmministrativoContabili = caricaStruttureAmministrativoContabili;
    exports.apriModaleCapitolo = apriModaleCapitolo;
    exports.puliziaReset = puliziaReset;
    exports.impostaDatiCausaleSpesa = impostaDatiCausaleSpesa;
    exports.impostaDatiCausaleEntrata = impostaDatiCausaleEntrata;
    exports.impostaTypeahead = impostaTypeahead;
    exports.calculateProvincia = calculateProvincia;
    exports.apriModaleProvvisorio = apriModaleProvvisorio;

    /**
     * Imposta la tabella delle sedi secondarie del soggetto.
     *
     * @param lista                    (Array)  la lista delle sedi
     * @param tabella                  (jQuery) la tabella da popolare
     */
    function impostaTabellaSedeSecondariaSoggetto(lista, tabella) {
        var oldUid = parseInt($('#HIDDEN_sedeSecondariaSoggettoUid').val(), 10);
        var options = {
            aaData: lista,
            oLanguage: {
                sZeroRecords: 'Non sono presenti sedi associate'
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var campo = '<input type="radio" name="sedeSecondariaSoggetto.uid" value="' + source.uid + '" ';
                    if(source.uid === oldUid) {
                        campo += 'checked ';
                    }
                    campo += ' />';
                    return campo;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalData', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('denominazione')},
                {aTargets: [2], mData: function(source) {
                    var indirizzo = source.indirizzoSoggettoPrincipale;
                    var result = '';
                    if(indirizzo) {
                        result = indirizzo.sedime + ' ' + indirizzo.denominazione + ', ' + indirizzo.numeroCivico;
                    }
                    return result;
                }},
                {aTargets: [3], mData: defaultPerDataTable('indirizzoSoggettoPrincipale.comune')},
                {aTargets: [4], mData: defaultPerDataTable('statoOperativoSedeSecondaria')}
            ]
        };

        var opts = $.extend(true, {}, optionsBaseDataTable, options);
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
        var res = '<input type="radio" name="modalitaPagamentoSoggetto.uid" value="' + uid + '" ';
        if(uid === oldUid) {
            res += 'checked ';
        }
        // Accetto anche la stringa vuota
        if(codSog != undefined) {
            res += 'data-cessione-cod-soggetto="' + codSog + '" ';
        }
        res += '/>';
        return res;
    }

    /**
     * Imposta la cessione del codiceSoggetto
     */
    function impostaCessioneCodSoggetto() {
        var modalitaSelezionata = $('input[name="modalitaPagamentoSoggetto.uid"]').find(':selected');
        var cessioneCodSoggetto;
        var hiddenField;

        // Rimuovo il campo
        $('#__cessioneCodSoggetto__modalitaPagamentoSoggetto__').remove();
        if(!modalitaSelezionata.length || modalitaSelezionata.data('cessioneCodSoggetto') == undefined) {
            // Esco
            return;
        }
        // Creo il campo
        cessioneCodSoggetto = modalitaSelezionata.data('cessioneCodSoggetto');
        hiddenField = $('<input />', {type: 'hidden', value: cessioneCodSoggetto, id: '__cessioneCodSoggetto__modalitaPagamentoSoggetto__'});
        hiddenField.insertAfter(modalitaSelezionata);
    }

    /**
     * Imposta la tabella delle modalita' di pagamento del soggetto.
     *
     * @param lista   (Array)  la lista delle modalita'
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaModalitaPagamentoSoggetto(lista, tabella) {
        var oldUid = parseInt($('#HIDDEN_modalitaPagamentoSoggettoUid').val(), 10);
        var options = {
            aaData: lista,
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

        var opts = $.extend(true, {}, optionsBaseDataTable, options);
        tabella.dataTable(opts);
    }

    /**
     * Carica la lista della causale.
     *
     * @param url          (string)  l'url da invocare per il caricamento della lista
     * @param select       (string)  il selettore CSS della select da popolare
     * @param uid          (string)  l'uid della causale, se presente (Optional)
     * @param doAutoselect (boolean) se effettuare l'autoselect (Optional, default: false)
     * @param firstExec    (boolean) se sia la prima esecuzione
     */
    function caricaListaCausale(url, select, uid, doAutoselect, firstExec) {
        var tipoCausale = parseInt($('#tipoCausale').val(), 10);
        var strutturaAmministrativoContabile = $('#HIDDEN_StrutturaAmministrativoContabileUid').val();
        var selectCausale = $(select).empty();
        var oggettoPerChiamataAjax = {};
        var fieldsetOriginale = $('#fieldset_causaleOriginale');

        selectCausale.overlay('show');

        if(!tipoCausale) {
            selectCausale.attr('disabled', true);
            selectCausale.overlay('hide');
            return;
        }

        oggettoPerChiamataAjax.uidTipoCausale = tipoCausale;
        if(strutturaAmministrativoContabile) {
            oggettoPerChiamataAjax.uidStrutturaAmministrativoContabile = strutturaAmministrativoContabile;
        }
        // SIAC-4492
        if(fieldsetOriginale.length) {
            $.extend(true, oggettoPerChiamataAjax, fieldsetOriginale.serializeObject());
        }

        $.postJSON(url, oggettoPerChiamataAjax)
        .then(function(data) {
            var str;
            var hasErrori = false;
            if(firstExec) {
                hasErrori = data.errori && data.errori.length && impostaDatiNegliAlert(data.errori, alertErrori, true, false);
            } else {
                hasErrori = impostaDatiNegliAlert(data.errori, alertErrori);
            }
            
            if(hasErrori) {
                return;
            }
            // Option vuota
            str = data.listaCausale.reduce(function(acc, el) {
                return acc + '<option value="' + el.uid + '">' + el.codice + ' - ' + el.descrizione + '</option>';
            }, '<option></option>');

            selectCausale.html(str);
            selectCausale.removeAttr('disabled');
            selectCausale.val(uid);
        }).always(function() {
            selectCausale.overlay('hide');
            autoselectCombo(selectCausale, firstExec);
        });
    }

    /**
     * Imposta i dati del capitolo a partire da quelli forniti dalla causale.
     *
     * @param capitolo (Object) il capitolo presente nella causale
     */
    function impostaCapitoloDaCausale(capitolo) {
        var numeroCapitolo = '';
        var numeroArticolo = '';
        var numeroUEB = '';
        var datiCapitolo = '';
        // Se ho il capitolo, popolo i campi correttamente
        if(capitolo) {
            numeroCapitolo = capitolo.numeroCapitolo;
            numeroArticolo = capitolo.numeroArticolo;
            numeroUEB = capitolo.numeroUEB;
        }

        // Imposto i valori
        $('#numeroCapitolo').val(numeroCapitolo);
        $('#numeroArticolo').val(numeroArticolo);
        $('#numeroUEB').val(numeroUEB);
        $('#datiRiferimentoCapitoloSpan').html(datiCapitolo);
    }

    /**
     * Imposta i dati del movimento di gestione a partire da quelli forniti dalla causale.
     *
     * @param movimentoGestione    (Object) il movimento di gestione presente nella causale
     * @param submovimentoGestione (Object) il submovimento di gestione presente nella causale
     */
    function impostaMovimentoGestioneDaCausale(movimentoGestione, subMovimentoGestione) {
        var annoMovimento = '';
        var numero = '';
        var numeroSubmovimento = '';
        var datiMovimento = '';
        // Se ho il movimento di gestione, popolo i campi correttamente
        if(movimentoGestione) {
            annoMovimento = movimentoGestione.annoMovimento;
            numero = movimentoGestione.numero;
        }
        // Se ho il submovimento di gestione, popolo i campi correttamente
        if(subMovimentoGestione) {
            numeroSubmovimento = subMovimentoGestione.numero;
        }

        // Imposto i campi
        $('#annoMovimentoMovimentoGestione').val(annoMovimento);
        $('#numeroMovimentoGestione').val(numero);
        $('#numeroSubMovimentoGestione').val(numeroSubmovimento);
        $('#datiRiferimentoImpegnoSpan').html(datiMovimento);
    }

    /**
     * Imposta i dati del soggetto a partire da quelli forniti dalla causale.
     *
     * @param soggetto (Object) il soggetto presente nella causale
     */
    function impostaSoggettoDaCausale(soggetto) {
        var codiceSoggetto = '';
        // Se ho il soggetto, popolo il campo correttamente
        if(soggetto) {
            codiceSoggetto = soggetto.codiceSoggetto;
        }

        // Imposto il dato e ricerco le sedi secondarie
        $('#codiceSoggettoSoggetto').val(codiceSoggetto)
            .trigger('change');
    }

    /**
     * Imposta i dati dell'atto amministrativo a partire da quelli forniti dalla causale.
     *
     * @param attoAmministrativo (Object) l'atto amministrativo presente nella causale
     */
    function impostaAttoAmministrativoDaCausale(attoAmministrativo) {
        var anno;
        var numero;
        var tipoAtto;
        var sac;
        var datiAttoAmministrativo;

        // Se ho l'atto, popolo i campi correttamente
        if(attoAmministrativo) {
            anno = attoAmministrativo.anno;
            numero = attoAmministrativo.numero;
            if(attoAmministrativo.tipoAtto) {
                tipoAtto = attoAmministrativo.tipoAtto.uid;
            }
            if(attoAmministrativo.strutturaAmmContabile) {
                sac = attoAmministrativo.strutturaAmmContabile.uid;
            }
        }

        $('#annoAttoAmministrativo').val(anno);
        $('#numeroAttoAmministrativo').val(numero);
        $('#tipoAtto').val(tipoAtto);
        $('#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid').val(sac);
        Ztree.selezionaNodoSeApplicabile('treeStruttAmmAttoAmministrativo', sac);
        $('#datiRiferimentoProvvedimentoSpan').html(datiAttoAmministrativo);
    }

    /**
     * Imposta i dati della causale.
     *
     * @param url         (String) l'URL da invocare
     * @param tipoCausale (String) il tipo di causale
     */
    function impostaDatiCausale(url, tipoCausale) {
        var self = $(this);
        var datiDellaCausale = datiCausale[tipoCausale];
        var obj = {};

        // Se non ho selezionato nulla, non chiamo il servizio
        if(!self.val()) {
            return;
        }

        // Imposto i dati dell'oggetto
        obj[datiDellaCausale.causale + '.uid'] = self.val();

        self.overlay('show');
        $.postJSON(url, obj)
        .then(function(data) {
            var causale;
            var capitolo;
            var movimento;
            var submovimento;
            var soggetto;
            var attoAmministrativo;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Ho degli errori: esco
                return;
            }
            // Popolo i campi corretti
            causale = data[datiDellaCausale.causale];
            capitolo = causale[datiDellaCausale.capitolo];
            movimento = causale[datiDellaCausale.movimento];
            submovimento = causale[datiDellaCausale.submovimento];
            soggetto = causale.soggetto;
            attoAmministrativo = causale.attoAmministrativo;

            // Popolo il capitolo
            impostaCapitoloDaCausale(capitolo);
            // Popolo il movimento
            impostaMovimentoGestioneDaCausale(movimento, submovimento);
            // Popolo il soggetto
            impostaSoggettoDaCausale(soggetto);
            // Popolo l'atto amministrativo
            impostaAttoAmministrativoDaCausale(attoAmministrativo);
        }).always(self.overlay.bind(self, 'hide'));
    }

    /**
     * Apre il modale del soggetto e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriModaleSoggetto(e) {
        e.preventDefault();
        $('#codiceSoggetto_modale').val($('#codiceSoggettoSoggetto').val());
        $('#modaleGuidaSoggetto').modal('show');
    }

    /**
     * Apre il modale del provvisorio e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriModaleProvvisorio(e) {
        e.preventDefault();
        $('#modaleRicercaProvvisorioDiCassa').modal('show');
    }

    /**
     * Imposta il periodo della competenza per il preDocumento.
     */
    function impostaPeriodoCompetenza() {
        var splitSelfVal = $(this).val().split('/');
        if(splitSelfVal.length !== 3) {
            // Non ho il numero corretto di pezzi: la data non è valida
            return;
        }
        $('#periodoCompetenzaPreDocumento').val(splitSelfVal[2] + '' + splitSelfVal[1]);
    }

    /**
     * Carica la lista delle Causali di Spesa e popola la select relativa
     * @param doAutoselect (boolean) se effettuare l'autoselect
     * @param appendError  (boolean) se appendere gli errori
     * @return (function(number) => void) la funzione occupantesi del caricamento delle causali
     */
    function caricaListaCausaleSpesa(doAutoselect, appendError) {
        return function(uid) {
            caricaListaCausale.call(this, 'ajax/leggiCausaliPreDocumentoSpesa.do', '#causaleSpesa', uid, doAutoselect, appendError);
        };
    }

    /**
     * Carica la lista delle Causali di Entrata e popola la select relativa
     *
     * @param uid l'uid della causale, se presente (Optional)
     */
    function caricaListaCausaleEntrata(uid) {
        caricaListaCausale.call(this, 'ajax/leggiCausaliPreDocumentoEntrata.do', '#causaleEntrata', uid, false, false);
    }

    /**
     * Carica la lista delle Causali di Entrata non annullate e popola la select relativa
     * @param uid doAutoselect (boolean) se effettuare l'autoselect
     * @return (function(number) => void) la funzione occupantesi del caricamento delle causali
     */
    function caricaListaCausaleEntrataNonAnnullate(doAutoselect) {
        return function(uid) {
            caricaListaCausale.call(this, 'ajax/leggiCausaliPreDocumentoEntrata_nonAnnullate.do', '#causaleEntrata', uid, doAutoselect, false);
        };
    }

    /**
     * Carica sede secondaria e modalità di pagamento del soggetto a partire dal codice.
     */
    function caricaSedeSecondariaEModalitaPagamento() {
        var codiceSoggetto = $(this).val();

        $.postJSON('caricaSedeSecondariaModalitaPagamentoDaSoggetto.do', {'codiceSoggetto': codiceSoggetto})
        .then(function(data) {
            var accordionSedeSecondaria = $('#accordionSedeSecondariaSoggetto');
            var accordionModalitaPagamento = $('#accordionModalitaPagamentoSoggetto');
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            accordionSedeSecondaria.slideDown();
            accordionModalitaPagamento.slideDown();

            impostaTabellaSedeSecondariaSoggetto(data.listaSedeSecondariaSoggetto, accordionSedeSecondaria.find('table'));
            impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggetto, accordionModalitaPagamento.find('table'));
        });
    }

    /**
     * Precarica sede secondaria e modalità di pagamento del soggetto.
     *
     * @param url (String) l'URL da invocare
     */
    function precaricaSedeSecondariaEModalitaPagamento(url) {
        $.postJSON(url)
        .then(function(data) {
            var accordionSedeSecondaria = $('#accordionSedeSecondariaSoggetto');
            var accordionModalitaPagamento = $('#accordionModalitaPagamentoSoggetto');
            if(data.listaSedeSecondariaSoggetto.length === 0 && data.listaModalitaPagamentoSoggetto.length === 0) {
                // Non ho ancora le liste
                return;
            }

            accordionSedeSecondaria.slideDown();
            accordionModalitaPagamento.slideDown();

            impostaTabellaSedeSecondariaSoggetto(data.listaSedeSecondariaSoggetto, accordionSedeSecondaria.find('table'));
            impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggetto, accordionModalitaPagamento.find('table'));
        });
    }

    /**
     * Apre il modale dell'impegno copiando i dati forniti dall'utente.
     */
    function apriModaleImpegno() {
        $('#annoImpegnoModale').val($('#annoMovimentoMovimentoGestione').val());
        $('#numeroImpegnoModale').val($('#numeroMovimentoGestione').val());
        $('#modaleImpegno').modal('show');
    }

    /**
     * Apre il modale dell'accertamento copiando i dati forniti dall'utente.
     */
    function apriModaleAccertamento() {
        $('#annoAccertamentoModale').val($('#annoMovimentoMovimentoGestione').val());
        $('#numeroAccertamentoModale').val($('#numeroMovimentoGestione').val());
        $('#modaleAccertamento').modal('show');
    }

    /**
     * Carica le Strutture all'interno dello ZTree.
     *
     * @param e (Event) l'evento scatenante
     */
    function caricaStruttureAmministrativoContabili(e) {
        var lista = e.lista;
        var ztree = new Ztree('', 'treeStruttAmm');
        ztree.inizializza(lista);
    }

    /**
     * Apre il modale del capitolo copiando i dati forniti dall'utente.
     */
    function apriModaleCapitolo() {
        $('#annoCapitolo_modale').val($('#annoCapitoloHidden').val());
        $('#annoCapitolo_modaleHidden').val($('#annoCapitoloHidden').val());
        $('#numeroCapitolo_modale').val($('#numeroCapitolo').val());
        $('#numeroArticolo_modale').val($('#numeroArticolo').val());
        $('#numeroUEB_modale').val($('#numeroUEB').val());

        $('#ERRORI_MODALE_CAPITOLO').slideUp();
        $('#divTabellaCapitoli').slideUp();
        $('#divVisualizzaDettaglio').slideUp();
        $('#collapseDettaglioCapitolo').removeClass('in');

        $('#modaleGuidaCapitolo').modal('show');
    }

    /**
     * Effettua una pulizia completa dei campi al reset.
     */
    function puliziaReset() {
        // Pulisco i field
        $(this).add('.modal')
            .find('*:input')
                .not('*[data-maintain], input[type="hidden"], input[type="button"], input[type="submit"], input[type="reset"], input[type="radio"], button')
                    .val('');

        // Pulisco gli span
        $('.datiRIFCapitolo, .datiRIFImpegno, .datiRIFSoggetto, .datiRIFProvvedimento, .datiRIFProvvisorioCassa').html('');

        // Ripristino i campi readonly
        $('.imputazioniContabiliCapitolo, .imputazioniContabiliMovimentoGestion, .imputazioniContabiliSoggetto, .imputazioniContabiliProvvedimento')
            .find('input[readOnly]')
                .removeAttr('readOnly');

        // Ripristino la select
        $('#HIDDEN_tipoAttoUid').remove();
        $('#tipoAtto').removeAttr('disabled');

        // Pulisco i modali (solo quello del soggetto)
        $('#divTabellaSoggetti, #divImpegniTrovati, #divContenitoreTabellaProvvedimento, #divTabellaCapitoli').slideUp();

        // Ripristino la selezionabilità dello zTree
        $('.ztree').each(function() {
            var tree = $.fn.zTree.getZTreeObj(this.id);
            // Scongiurare i problemi dovuti alla non-inizializzazione del campo
            tree && Ztree.deselezionaNodiEAbilitaAlberatura($.fn.zTree.getZTreeObj(this.id));
        }).closest('.collapse')
            .each(function() {
                var self = $(this);
                self.is('.in:visible') && self.collapse('hide');
            });
        $('#accordionSedeSecondariaSoggetto').slideUp();
        $('#accordionModalitaPagamentoSoggetto').slideUp();

    }

    /**
     * Imposta i dati della causale di spesa.
     */
    function impostaDatiCausaleSpesa() {
        impostaDatiCausale.call(this, 'ricercaDettaglioCausaleSpesa.do', 'spesa');
    }

    /**
     * Imposta i dati della causale di spesa.
     */
    function impostaDatiCausaleEntrata() {
        impostaDatiCausale.call(this, 'ricercaDettaglioCausaleEntrata.do', 'entrata');
    }

    /**
     * Impostazione dei typeahead.
     *
     * @param elts (jQuery) gli elementi su cui attivare la funzionalita'.
     */
    function impostaTypeahead(elts) {
        var baseOpts = {
            ajax: {
                url: '/siacbilapp/listaComuni.do',
                // Quanto tempo debba intercorrere tra le pressioni dei tasti
                timeout: 300,
                triggerLength: 3,
                method: 'POST',
                displayField: 'descrizione',
                valueField: 'descrizione'
            },
            dataMap: {
                'provincia': 'siglaProvincia'
            }
        };
        elts
        .not('[disabled]')
        .each(function(idx, elt) {
            var $el = $(elt);
            var opts = $.extend(true, {}, baseOpts);
            var spanProvincia = $el.siblings('span[data-provincia]').find('span');

            // Cancello il dato della provincia al premere di un tasto (per permetterne il ricalcolo)
            $el.keypress(function(e) {
                if(e.key === 'Enter' || e.keyCode === 13 || e.which === 13) {
                    // Non catturo l'evento ENTER
                    return;
                }
                spanProvincia.html('');
            });

            // Aggiungo le funzionalita' AJAX
            opts.ajax.preDispatch = function(query) {
                if(!this.spinner) {
                    this.spinner = $el.siblings('span[data-has-spinner]')
                        .find('.spinner');
                }
                this.spinner.addClass('activated');
                return {descrizione: query};
            };
            opts.ajax.preProcess = function(data) {
                this.spinner.removeClass('activated');
                return data.listaComuni;
            };
            // Aggiungo il callback di selezione
            opts.onSelect = function(arg) {
                var data = arg.data || {};
                var siglaProvincia = data.siglaProvincia || '';
                if(!this.spanProvincia) {
                    this.spanProvincia = $el.siblings('span[data-provincia]')
                        .find('span');
                }
                this.spanProvincia.html(siglaProvincia);
            };
            $el.typeahead(opts);
        });
    }

    /**
     * Calcolo della provincia.
     *
     * @param elts (jQuery) gli elementi su cui attivare la funzionalita'.
     */
    function calculateProvincia(elts) {
        elts.each(function(idx, elt) {
            var $el = $(elt);
            var descrizione = $el.val();
            var spinner;
            var spanProvincia;

            // Se non ho la descrizione, esco subito
            if(!descrizione) {
                return;
            }
            spinner = $el.siblings('span[data-has-spinner]').find('.spinner').addClass('activated');
            spanProvincia = $el.siblings('span[data-provincia]').find('span');

            // Chiamo il servizio
            $.postJSON('/siacbilapp/listaComuni.do', {descrizione: descrizione})
            .then(function(data) {
                var comune;
                // Se non ho comuni o non ne ho esattamente uno, non so quale selezionare
                if(!data.listaComuni || data.listaComuni.length !== 1) {
                    return;
                }
                comune = data.listaComuni[0] || {};
                // Aggiungo un breve timeout per evitare che si sovrapponga con altri eventi (change)
                setTimeout(function() {
                    spanProvincia.html(comune.siglaProvincia || '');
                }, 20);
            }).always(spinner.removeClass.bind(spinner, 'activated'));
        });
    }

    return exports;
} (jQuery));