/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    'use strict';

    var alertErrori = $('#ERRORI');
    var alertInformazioni  = $('#INFORMAZIONI');
    var ricercheModale = {};
    var tipiRicercheModale = ['Capitolo', 'Accertamento', 'Soggetto', 'Provvedimento','ProvvisorioDiCassa'];
    // SIAC-4957 : per permettere la selezione multipagina
    var selectedDatas = {};

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param preDoc (Object) il preDocumento da annullare
     */
    function clickOnAnnulla(preDoc) {
        $('#HIDDEN_UidDaAnnullare').val(preDoc.uid);
        $('#elementoSelezionatoAnnullamento').html(preDoc.numero);
        $('#modaleAnnullaPreDocumento').modal('show');
    }

    /**
     * Associa le imputazioni contabili ai preDocumenti.
     *
     * @param param (Object) il parametro per la chiamata asincrona
     * @param modal (jQuery) il modale da chiudere
     */
    function associaImputazioniContabili(param, modal) {
        $.postJSON('risultatiRicercaPreDocumentoEntrataAssocia.do', param).then(function(data){
            //impostoErrorineglialert se ci sono errori
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            //altrimenti, imposto il messaggio operazione asincrona avviata
            impostaDatiNegliAlert(['COR_INF_0037 - L\'elaborazione asincrona e\' stata avviata e sarà disponibile nel cruscotto una volta conclusa'], alertInformazioni);
        });
        modal.modal('hide');
    }

    /**
     * Definisce la predisposizione di incasso per i preDocumenti.
     *
     * @param param (Object) il parametro per la chiamata asincrona
     * @param modal (jQuery) il modale da chiudere
     */
    function completaDefinisciPredisposizioneIncasso(param, modal) {
        var $form = $('#risultatiRicercaPreDocEntrataForm');
        var paramsKeys = Object.keys(param);
        if(param.inviaTutti === true){
            $('#HIDDEN_inviaTutti').val(param.inviaTutti);
        } else {
            for(var i = 0; i < paramsKeys.length; i++) {
                $form.append('<input type="hidden" name="' + paramsKeys[i] + '" value="' + param[paramsKeys[i]] + '" />');
            }
        }
        $('#HIDDEN_riepilogoCompletaDefinisci').val('true');
        $form.attr('action','riepilogoCompletaDefinisciPreDocumentoEntrata.do');
        $form.submit();

        modal.modal('hide');
    };

    /**
     * Definisce la predisposizione di incasso per i preDocumenti.
     *
     * @param param (Object) il parametro per la chiamata asincrona
     * @param modal (jQuery) il modale da chiudere
     */
    function definisciPredisposizioneIncasso(param, modal) {
        $.postJSON('risultatiRicercaPreDocumentoEntrataDefinisci.do', param).then(function(data){
            //impostoErrorineglialert se ci sono errori
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            //altrimenti, imposto il messaggio operazione asincrona avviata
            impostaDatiNegliAlert(['COR_INF_0037 - L\'elaborazione asincrona e\' stata avviata e sarà disponibile nel cruscotto una volta conclusa'], alertInformazioni);
        });
        modal.modal('hide');
    }

    /**
     * Funzione di callback per la chiamata asincrona.
     *
     * @param data (Object) i dati ottenuti dall'invocazione del servizio
     *
     * @returns (Object) i parametri per il polling
     */
    function callbackAsync(data) {
        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            throw Error('Errore nell\'invocazione del servizio');
        }

        return {idOperazioneAsync: data.idAzioneAsync};
    }

    /**
     * Funzione di callback per il polling.
     *
     * @param data (Object) i dati ottenuti dall'invocazione del servizio
     *
     * @returns (Boolean) se lo stato sia pari a concluso o ad errore
     */
    function callbackPolling(data) {
        var stato = data.stato;
        var messaggi = [data.messaggio];

        stato && impostaDatiNegliAlert(messaggi, alertInformazioni);
        return Async.checkConclusaOErrore(stato);
    }

    /**
     * Ottiene i preDocumenti selezionati.
     * @returns (any[]) l'array dei dati selezionati
     */
    function getPreDocumentiSelezionati() {
        var res = [];
        var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                res.push(selectedDatas[i].data);
            }
        }
        return res;
    }

    /**
     * Effettua un'invocazione asincrona con i predocumenti selezionati.
     *
     * @param e            (Event)    l'evento scatenante l'invocazione
     * @param suffissoTipo (String)   il tipo di suffisso da utilizzare (Definisci / AssociaImputazioniContabili)
     * @param msg          (String)   il messaggio da impostare
     * @param suffissoSpan (String)   il suffisso per gli span da utilizzare (Definire / Associare)
     * @param callback     (Function) la funzione da invocare alla conferma
     * @param fncTransform (Function) la funzione di trasformazione ulteriore sui dati (Optional - default: $.noop)
     */
    function lavoraConSelezionati(e, suffissoTipo, msg, suffissoSpan, callback, fncTransform) {
        var predisposizioniAssociate = getPreDocumentiSelezionati();
        var numeroPredisposizioni = predisposizioniAssociate.length;
        var importo = 0;
        var modale = $('#modale' + suffissoTipo);
        var oggettoPerInvocazioneAsincrona = {};
        var arrayUid = [];
        $('#hidden_inviaTutti').val('false');

        e.preventDefault();

        // Se non ho selezionato sufficienti predocumenti, fornisco un messaggio di errore ed esco
        if(!numeroPredisposizioni) {
            impostaDatiNegliAlert(['Necessario selezionare almeno una predisposizione da ' + msg], alertErrori);
            return;
        }
        // Chiudo l'alert degli errori
        alertErrori.slideUp();
        predisposizioniAssociate.forEach(function(el) {
            importo += el.importo;
            arrayUid.push(el.uid);
        });

        $('#pulsanteAnnulla' + suffissoTipo).substituteHandler('click', modale.modal.bind(modale, 'hide'));
        $('#pulsanteConferma' + suffissoTipo).substituteHandler('click', function() {
            oggettoPerInvocazioneAsincrona.listaUid = arrayUid;
            if(fncTransform && typeof fncTransform === 'function') {
                fncTransform(oggettoPerInvocazioneAsincrona);
            }
            callback(qualify(oggettoPerInvocazioneAsincrona), modale);
        });
        $('#pulsanteModifica' + suffissoTipo).substituteHandler('click', openModificaImputazioniContabili.bind(undefined, modale));

        $('#numeroPreDocumentiDa' + suffissoSpan).html(numeroPredisposizioni);
        $('#importoTotalePreDocumentiDa' + suffissoSpan).html(importo.formatMoney());

        modale.modal('show');
    }
    
    /**
     * Effettua un'invocazione asincrona con tutti i predocumenti.
     *
     * @param e            (Event)    l'evento scatenante l'invocazione
     * @param suffissoTipo (String)   il tipo di suffisso da utilizzare (Definisci / AssociaImputazioniContabili)
     * @param suffissoSpan (String)   il suffisso per gli span da utilizzare (Definire / Associare)
     * @param callback     (Function) la funzione da invocare alla conferma
     * @param fncTransform (Function) la funzione di trasformazione ulteriore sui dati (Optional - default: $.noop)
     */
    function lavoraConTutti(e, suffissoTipo, suffissoSpan, callback, fncTransform) {
        var modale = $('#modale' + suffissoTipo);
        var oggettoPerInvocazioneAsincrona = {};
        var totaleRisultatiTrovati = /\d*/.exec($('#id_num_result').html())[0];
        var importoTotale = $('#importoTotale').val();
        $('#hidden_inviaTutti').val('true');

        e.preventDefault();

        $('#pulsanteAnnulla' + suffissoTipo).substituteHandler('click', modale.modal.bind(modale, 'hide'));
        $('#pulsanteConferma' + suffissoTipo).substituteHandler('click', function() {
            oggettoPerInvocazioneAsincrona.inviaTutti = true;
            if(fncTransform && typeof fncTransform === 'function') {
                fncTransform(oggettoPerInvocazioneAsincrona);
            }
            callback(oggettoPerInvocazioneAsincrona, modale);
        });
        $('#pulsanteModifica' + suffissoTipo).substituteHandler('click', openModificaImputazioniContabili.bind(undefined, modale));

        $('#numeroPreDocumentiDa' + suffissoSpan).html(totaleRisultatiTrovati);
        $('#importoTotalePreDocumentiDa' + suffissoSpan).html(importoTotale);

        modale.modal('show');
    }
    
    /**
     * Apertura della modifica delle imputazioni contabili
     */
    function openModificaImputazioniContabili(modale) {
        var collapseImputazioniContabili = $('#collapseImputazioniContabili').slideUp();
        var input = modale.find('input[name="chooseAdeguaDisponibilitaAccertamento"]');
        var adeguaDisponibilita = input.attr('type') === 'radio'
            ? input.filter(':checked')
            : input;
        
        tipiRicercheModale.forEach(function(val) {
            if(ricercheModale[val] && global[val]) {
                global[val].destroy(ricercheModale[val]);
            }
        });
        
        $.post('risultatiRicercaPreDocumentoEntrata_apriAssociaConModifiche.do')
        .then(function(data) {
            console.log('SIAC-7423 data', data);
            var idCapitolo;
            var idProvvedimento;
            var idProvvisorio;

            $('#containerImputazioni').html(data);
            $('input[name="forzaDisponibilitaAccertamento"]').val(adeguaDisponibilita.length>0? adeguaDisponibilita.val() : 'false');
            
            // Inizializzazione delle ricerche
            idCapitolo = Capitolo.inizializza('EntrataGestione', '#annoCapitolo', '#numeroCapitolo', '#numeroArticolo', '#numeroUEB', '#datiRiferimentoCapitoloSpan', '#pulsanteCompilazioneGuidataCapitolo');
            idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
            Accertamento.inizializza('#annoMovimentoMovimentoGestione', '#numeroMovimentoGestione', '#numeroSubMovimentoGestione', '#datiRiferimentoImpegnoSpan', undefined, undefined, '#pulsanteCompilazioneGuidataMovimentoGestione');
            Soggetto.inizializza('#codiceSoggettoSoggetto', '#HIDDEN_codiceFiscaleSoggetto', '#HIDDEN_denominazioneSoggetto', '#datiRiferimentoSoggettoSpan', '#pulsanteCompilazioneSoggetto');
            
            Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

            // Provvisorio
            $('#modale_hidden_tipoProvvisorioDiCassa').val('E');
            idProvvisorio = ProvvisorioDiCassa.inizializzazione('#pulsanteCompilazioneGuidataProvvisorioCassa','#HIDDEN_TipoProvvisorioCassa','#annoProvvisorioCassa','#numeroProvvisorioCassa','#HIDDEN_CausaleProvvisorioCassa');
            // ProvvisorioDiCassa.inizializza;
            // $('#pulsanteCompilazioneGuidataProvvisorioCassa').substituteHandler('click', ProvvisorioDiCassa.apriModale);


            // Imposto i valori a 1 per permetterne la distruzione: viene ignorato dalla funzionalita'
            ricercheModale.Capitolo = idCapitolo;
            ricercheModale.Provvedimento = idProvvedimento;
            ricercheModale.Accertamento = 1;
            ricercheModale.Soggetto = 1;
            ricercheModale.ProvvisorioDiCassa = idProvvisorio;

            collapseImputazioniContabili.slideDown();
            modale.modal('hide');
            collapseImputazioniContabili.scrollToSelf();
        });
    }

    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return val;
    }

    /**
     * Caricamento via Ajax della tabella dei preDocumenti e visualizzazione della stessa.
     */
    function visualizzaTabellaDocumenti() {
        var table = $('#risultatiRicercaPreDocumento');
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaPreDocumentoEntrataAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 50,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            sScrollY: '300px',
            bScrollCollapse: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $('#risultatiRicercaPreDocumento_processing').parent('div')
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#risultatiRicercaPreDocumento_processing').parent('div')
                    .hide();
                // Attivo i popover
                table.find('a[rel="popover"]')
                    .popover()
                    .eventPreventDefault('click');
                handleSelezionaTutti(settings);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var res = '<input type="checkbox" name="uidPreDocumento" value="' + source.uid + '"';
                    if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
                        res += ' checked';
                    }
                    return res + '>';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalPreDocumento', oData)
                        .substituteHandler('change', handlePredocCheckboxChange.bind(undefined, nTd, oData));
                }},
                {aTargets: [1], mData: function(source) {
                    return source.descrizione
                        ? '<a data-original-title="Causale estesa" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.descrizione) + '" href="#">' + source.numero + '</a>'
                        : source.numero;
                }},
                {aTargets: [2], mData: function(source) {
                    var sac = source.strutturaAmministrativoContabile;
                    return sac
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(sac.descrizione) + '">' + sac.codice + '</a>'
                        : '';
                }},
                {aTargets: [3], mData: function(source) {
                    var causale = source.causale;
                    var tipoCausale = causale && causale.tipoCausale;
                    return causale
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(causale.descrizione) + '">' + (tipoCausale ? tipoCausale.codice + ' - ' : '') + causale.codice + '</a>'
                        : '';
                }},
                {aTargets: [4], mData: function(source) {
                    return source.contoCorrente
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.contoCorrente.descrizione) + '">' + source.contoCorrente.codice + '</a>'
                        : '';
                }},
                {aTargets: [5], mData: defaultPerDataTable('dataCompetenza', '', formatDate)},
                {aTargets: [6], mData: function(source) {
                    return source.statoOperativoDocumento
                        ? '<a data-original-title="Documento" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.documento) + '">' + source.statoOperativoDocumento.codice + '</a>'
                        : '';
                }},
                {aTargets: [7], mData: defaultPerDataTable('ragioneSocialeCognomeNome')},
                {aTargets: [8], mData: defaultPerDataTable('soggetto')},
                {aTargets: [9], mData: defaultPerDataTable('statoOperativoPreDocumento.codice')},
                {aTargets: [10], mData: defaultPerDataTable('dataDocumento', '', formatDate)},
                {aTargets: [11], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }},
                {aTargets: [12], mData: 'azioni', fnCreatedCell: function(nTd, sData, oData) {
                    $('.annullaPreDocumento', nTd).eventPreventDefault('click', clickOnAnnulla.bind(undefined, oData));
                }}
            ]
        };

        var startPos = parseInt($('#HIDDEN_startPosition').val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {'iDisplayStart' : startPos});
        }

        table.dataTable(options);
    }

    /**
     * Handling della variazione del checkbox
     * @param nTd    (Node) il td
     * @param predoc (any)  il predoc
     */
    function handlePredocCheckboxChange(nTd, predoc) {
        var input = $('input', nTd);
        selectedDatas[+input.val()] = {isSelected: input.prop('checked'), data: predoc};
    }

    /**
     * Gestione della selezione di tutti gli elementi. Preseleziona il checkbox all'apertura della pagina
     * @param settings (any) i settings del dataTable
     */
    function handleSelezionaTutti(settings) {
        var inputNonChecked = $('input', settings.nTBody).not(':checked');
        $('#checkboxSelezionaTutti').prop('checked', inputNonChecked.length === 0);
    }

    /**
     * Definisce la predisposizione di incasso per i preDocumenti selezionati.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function definisciPredisposizioneIncassoSelezionati(e) {
        lavoraConSelezionati(e, 'Definisci', 'definire', 'Definire', definisciPredisposizioneIncasso);
    }

    /**
     * Definisce la predisposizione di incasso per tutti i preDocumenti.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function definisciPredisposizioneIncassoTutti(e) {
        lavoraConTutti(e, 'Definisci', 'Definire', definisciPredisposizioneIncasso);
    }

    //SIAC-6780
    /**
     * Definisce la predisposizione di incasso per i preDocumenti selezionati.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function completaDefinisciPredisposizioneIncassoSelezionati(e) {
        lavoraConSelezionati(e, 'CompletaDefinisci', 'definire', 'CompletaDefinisci', completaDefinisciPredisposizioneIncasso);
    }

    /**
     * Definisce la predisposizione di incasso per tutti i preDocumenti.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function completaDefinisciPredisposizioneIncassoTutti(e) {
        lavoraConTutti(e, 'CompletaDefinisci', 'CompletaDefinisci', completaDefinisciPredisposizioneIncasso);
    }
    //

    /**
     * Associa le imputazioni contabili ai preDocumenti selezionati.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function associaImputazioniContabiliSelezionati(e) {
        lavoraConSelezionati(e, 'AssociaImputazioniContabili', 'associare', 'Associare', associaImputazioniContabili, adeguaDisponibilitaTransform);
    }

    /**
     * Associa le imputazioni contabili a tutti i preDocumenti.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function associaImputazioniContabiliTutti(e) {
        lavoraConTutti(e, 'AssociaImputazioniContabili', 'Associare', associaImputazioniContabili, adeguaDisponibilitaTransform);
    }

    /**
     * Gestione dell'azione di inserimento di un nuovo preDocumento.
     */
    function inserisciPreDocumento() {
        document.location = 'risultatiRicercaPreDocumentoEntrataInserisci.do';
    }

    /**
     * Seleziona tutti i preDocumenti visibili sulla pagina.
     */
    function selezionaTutti() {
        var checked = !!$(this).prop('checked');
        $('input[name="uidPreDocumento"]')
            .prop('checked', checked)
            .each(function(idx, el) {
                selectedDatas[+el.value] = {isSelected: checked, data: $(this).data('originalPreDocumento')};
            });
    }

    /**
     * Refresha la pagina, dopo aver atteso n secondi.
     *
     * @param time (Number) il tempo in ms da attendere prima del refresh.
     */
    function gestioneRefreshTabella(time) {
        refreshTable(time, alertInformazioni, '#risultatiRicercaPreDocumento');
        $('#checkboxSelezionaTutti').attr('checked', false)
            .trigger('selezionaTutti');
    }
    
    function handlePollingDone(e) {
        if(e.pollingData.stato === 'ERRORE') {
            //impostaDatiNegliAlert(); COMUNICARE ERRORE in qualche modo... Ma come mi arriva l'errore?
        } else if(e.pollingData.stato !== 'CONCLUSA') {
            // Impostare negli alert l'informazione di operazione ancora in esecuzione
            impostaDatiNegliAlert(['L\'elaborazione e\' stata presa in carico, i risultati verrano mostrati sul cruscotto nella sezione dedicata, quando l\'operazione sara\' CONCLUSA'], $('#INFORMAZIONI'));
        } else {
            gestioneRefreshTabella(5000);
        }
    }

    function modificaImputazioniContabili() {
        var collapseImputazioni = $('#collapseImputazioniContabili');
        var oggettoPerInvocazioneAsincrona = collapseImputazioni.serializeObject();
        var arrayUid;

        alertErrori.slideUp();

        if(oggettoPerInvocazioneAsincrona.inviaTutti !== 'true') {
            arrayUid = getPreDocumentiSelezionati().map(function(el) {
                return el.uid;
            });
            if(!arrayUid.length) {
                impostaDatiNegliAlert(['Necessario selezionare almeno una predisposizione da associare'], alertErrori);
                return;
            }
            oggettoPerInvocazioneAsincrona.listaUid = arrayUid;
        }
        collapseImputazioni.overlay('show');
        

        $.postJSON('risultatiRicercaPreDocumentoEntrata_associaConModifiche.do', qualify(oggettoPerInvocazioneAsincrona))
        .then(function(data){
            //impostoErrorineglialert se ci sono errori
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            //altrimenti, imposto il messaggio operazione asincrona avviata
            impostaDatiNegliAlert(['COR_INF_0037 - L\'elaborazione asincrona e\' stata avviata e sarà disponibile nel cruscotto una volta conclusa'], alertInformazioni);
        })
        .then(collapseImputazioni.slideUp.bind(collapseImputazioni))
        .always(collapseImputazioni.overlay.bind(collapseImputazioni, 'hide'));
    }

    //SIAC-6780
    function completaDefinisci() {
        var collapseCompletaDefinisci = $('#collapseCompletaDefinisci');
        var oggettoPerInvocazioneAsincrona = collapseCompletaDefinisci.serializeObject();
        var arrayUid;

        alertErrori.slideUp();

        if(oggettoPerInvocazioneAsincrona.inviaTutti !== 'true') {
            arrayUid = getPreDocumentiSelezionati().map(function(el) {
                return el.uid;
            });
            if(!arrayUid.length) {
                impostaDatiNegliAlert(['Necessario selezionare almeno una predisposizione da associare'], alertErrori);
                return;
            }
            oggettoPerInvocazioneAsincrona.listaUid = arrayUid;
        }
        collapseCompletaDefinisci.overlay('show');
        

        $.postJSON('risultatiRicercaPreDocumentoEntrata_completaDefinisciPreDoc.do', qualify(oggettoPerInvocazioneAsincrona))
        .then(function(data){
            //impostoErrorineglialert se ci sono errori
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            // //altrimenti, imposto il messaggio operazione asincrona avviata
            // impostaDatiNegliAlert(['COR_INF_0037 - L\'elaborazione asincrona e\' stata avviata e sarà disponibile nel cruscotto una volta conclusa'], alertInformazioni);
        })
        .then(collapseCompletaDefinisci.slideUp.bind(collapseCompletaDefinisci))
        .always(collapseCompletaDefinisci.overlay.bind(collapseCompletaDefinisci, 'hide'));
    }
    //

    // SIAC-4384
    /**
     * Aggiorna la data di trasmissione per tutti i predoc
     * @param e (Event) l'evento scatenante
     */
    function dataTrasmissionePredisposizioneIncassoTutti(e) {
        lavoraConTutti(e, 'DataTrasmissione', 'DataTrasmissione', dataTrasmissionePredisposizioneIncasso, dataTrasmissioneTransform);
    }
    /**
     * Aggiorna la data di trasmissione per i predoc selezionati
     * @param e (Event) l'evento scatenante
     */
    function dataTrasmissionePredisposizioneIncassoSelezionati(e) {
        lavoraConSelezionati(e, 'DataTrasmissione', 'aggiornare', 'DataTrasmissione', dataTrasmissionePredisposizioneIncasso, dataTrasmissioneTransform);
    }
    /**
     * Definisce la predisposizione di incasso per i preDocumenti.
     *
     * @param param (Object) il parametro per la chiamata asincrona
     * @param modal (jQuery) il modale da chiudere
     */
    function dataTrasmissionePredisposizioneIncasso(param, modal) {
        $.postJSON('risultatiRicercaPreDocumentoEntrata_aggiornaDataTrasmissione.do', param).then(function(data){
            //impostoErrorineglialert se ci sono errori
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            //altrimenti, imposto il messaggio operazione asincrona avviata
            impostaDatiNegliAlert(['COR_INF_0037 - L\'elaborazione asincrona e\' stata avviata e sarà disponibile nel cruscotto una volta conclusa'], alertInformazioni);
        });
        modal.modal('hide');
    }
    /**
     * Trasformazione sull'oggetto per la data di trasmissione
     * @param obj l'oggetto da trasformare
     */
    function dataTrasmissioneTransform(obj) {
        obj.dataTrasmissione = $('#dataTrasmissione').val();
    }
    
    //SIAC-4310
    function adeguaDisponibilitaTransform(obj){
        var input = $('input[name="chooseAdeguaDisponibilitaAccertamento"]');
        // SIAC-5041
        var res = input.length === 0
            ? 'false'
            : input.attr('type') === 'radio'
                ? input.filter(':checked').val()
                : input.val();

        obj.forzaDisponibilitaAccertamento = res;
    }

    //SIAC-6780
    function checkIsfromConvalidaDefinisci(){
        var hiddenVal = $('#HIDDEN_fromConvalidaDefinisci').val();
        if(hiddenVal){
            $('#risultatiRicercaPreDocEntrataForm').attr('action','');
        }
    }

    $(function() {
        //SIAC-7440
        if($('#fromRiepilogoCompletaDefinisci').val() === 'true'){
            $('#divAzioni').find('a.accordion-toggle').prop('disabled', true);
        }

        var collapseImputazioni = $('#collapseImputazioniContabili');
        $('#checkboxSelezionaTutti').change(function() {
            $(this).trigger('selezionaTutti');
        }).on('selezionaTutti', selezionaTutti);

        // Al termine del polling, effettua un refresh della pagina
        $(document).on('pollingDone', handlePollingDone);

        $('#pulsanteInserisciPreDocumento').click(inserisciPreDocumento);

        $('#pulsanteAssociaImputazioniContabiliTutti').click(associaImputazioniContabiliTutti);
        $('#pulsanteAssociaImputazioniContabiliSelezionati').click(associaImputazioniContabiliSelezionati);

        $('#pulsanteDefinisciTutti').click(definisciPredisposizioneIncassoTutti);
        $('#pulsanteDefinisciSelezionati').click(definisciPredisposizioneIncassoSelezionati);

        //SIAC-6780
        checkIsfromConvalidaDefinisci();
        $('#pulsanteCompletaDefinisciTutti').click(completaDefinisciPredisposizioneIncassoTutti);
        $('#pulsanteCompletaDefinisciSelezionati').click(completaDefinisciPredisposizioneIncassoSelezionati);
        //
        
        $('#pulsanteDataTrasmissioneTutti').click(dataTrasmissionePredisposizioneIncassoTutti);
        $('#pulsanteDataTrasmissioneSelezionati').click(dataTrasmissionePredisposizioneIncassoSelezionati);
        
        $('#pulsanteAnnullaImputazioniContabili').substituteHandler('click', collapseImputazioni.slideUp.bind(collapseImputazioni));
        $('#pulsanteConfermaImputazioniContabili').substituteHandler('click', modificaImputazioniContabili);

        visualizzaTabellaDocumenti();
    });
})(jQuery, this);
