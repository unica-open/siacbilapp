/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    // Variables
    var alertErrori = $('#ERRORI');
    var alertInformazioni = $('#INFORMAZIONI');
    var regexTagContent = /<.*?>(.*?)<\/.*?>/;
    var faseBilancioChiuso = $('#hidden_faseBilancioChiuso').val() === 'true';
    var selects =  {
        'titoloSpesa': {
            url: 'ajax/macroaggregatoAjax.do', jsonName: 'listaMacroaggregato', figlioName: 'macroaggregato', nipoteName: '', data: [
                {prefix: '', suffix: 'Ricerca', suffixFiglio: 'Ricerca', suffixNipote: ''},
                {prefix: 'inserimento_', suffix: '', suffixFiglio: '', suffixNipote: ''},
                {prefix: 'aggiornamento_', suffix: '', suffixFiglio: '', suffixNipote: ''}
            ]
        },
        'titoloEntrata': {
            url: 'ajax/tipologiaTitoloAjax.do', jsonName: 'listaTipologiaTitolo', figlioName: 'tipologiaTitolo', nipoteName: 'categoriaTipologiaTitolo', data: [
                {prefix: '', suffix: 'Ricerca', suffixFiglio: 'Ricerca', suffixNipote: 'Ricerca'},
                {prefix: 'inserimento_', suffix: '', suffixFiglio: '', suffixNipote: ''},
                {prefix: 'aggiornamento_', suffix: '', suffixFiglio: '', suffixNipote: ''}
            ]
        },
        'tipologiaTitolo': {
            url: 'ajax/categoriaTipologiaTitoloAjax.do', jsonName: 'listaCategoriaTipologiaTitolo', figlioName: 'categoriaTipologiaTitolo', nipoteName: '', data: [
                {prefix: '', suffix: 'Ricerca', suffixFiglio: 'Ricerca', suffixNipote: ''},
                {prefix: 'inserimento_', suffix: '', suffixFiglio: '', suffixNipote: ''},
                {prefix: 'aggiornamento_', suffix: '', suffixFiglio: '', suffixNipote: ''}
            ]
        }
    };
    var thClassificatore = {E: 'Tipologia - Categoria', S: 'Macroaggregato'};

    // Uses
    $(onPageLoad);

    // Definitions
    /**
     * Funzione attivata al caricamento della pagina: legame delle funzionalita' base
     */
    function onPageLoad() {
        $('button[data-button-cerca]').click(ricercaConciliazione.bind(undefined, false));
        $("#buttonNuovaConciliazione").click(apriCollapseNuovaConciliazione);
        $('#inserimento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'inserimento', 'gestioneConciliazionePerTitolo_inserisci.do'));
        $('#aggiornamento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'aggiornamento', 'gestioneConciliazionePerTitolo_aggiornamento.do'));

        definisciRadioEntrataSpesa();
        definisciCaricamentoSelectViaAjax();
        definisciPulsantiAnnullamento();
        definisciRicercaGuidataConto();

        $(document).on('contoCaricato', gestioneContoCaricato);
    }

    /**
     * Gestione dei radio di entrata e di spesa
     */
    function definisciRadioEntrataSpesa() {
        ['fieldsetRicercaConciliazioni', 'inserimento_fieldsetConciliazioni', 'aggiornamento_fieldsetConciliazioni'].forEach(function(val) {
            $('input[type="radio"][name^="entrataSpesa"]', '#' + val).change(showHideCampiRicerca.bind(undefined, '#' + val)).filter(':checked').change();
        });
    }

    /**
     * Gestione dei pulsanti di annullamento
     */
    function definisciPulsantiAnnullamento() {
        ['inserimento', 'aggiornamento'].forEach(function(prefix) {
            $('#' + prefix + '_buttonAnnulla').click(closeCollapse.bind(undefined, '#divConciliazione_' + prefix));
        });
    }

    function definisciRicercaGuidataConto() {
        ['inserimento', 'aggiornamento'].forEach(function(prefix) {
            Conto.inizializza('#' + prefix + '_classePiano', undefined, '#' + prefix + '_contoConciliazionePerTitolo', '', '#' + prefix + '_pulsanteCompilazioneGuidataConto', true);
        });
    }

    /**
     * Controlla se mostrare o nascondere i campi di ricerca.
     *
     * @param fieldsetId (String) l'id del fieldset
     * @param event      (Event)  l'evento scatenante l'invocazione
     * @param data       (Object) i dati da utilizzare (Optional)
     */
    function showHideCampiRicerca(fieldsetId, event, data) {
        var value = $(event.target).val();
        var divTipoRicerca = $('div[data-tipo]', fieldsetId);
        var tipoToUse = divTipoRicerca.filter('[data-tipo="' + value + '"]');
        var tipoNotToUse = divTipoRicerca.not(tipoToUse);
        var dataToEnable = tipoToUse.find(':input').filter(':first, [data-button-cerca]');
        var preventEnabling = data && data.doNotEnable;

        tipoNotToUse.find(':input').not('[type="hidden"]').val('').attr('disabled', true).end().end().slideUp();
        tipoToUse.find(':input').val('').end().slideDown();
        if(!preventEnabling) {
            dataToEnable.removeAttr('disabled');
        }
    }

    /**
     * Chiusura del collapse.
     *
     * @param collapseId (String) l'id del collape
     */
    function closeCollapse(collapseId) {
        return $(collapseId).slideUp();
    }

    /**
     * Definizioni dei caricamenti delle select via chiamata AJAX
     */
    function definisciCaricamentoSelectViaAjax() {
        $.each(selects, function(key, value) {
            value.data.forEach(function(val) {
                var fnc = caricaSelect.bind(undefined, $('#' + val.prefix + key + val.suffix), $('#' + val.prefix + value.figlioName + val.suffixFiglio),
                        $('#' + val.prefix + value.nipoteName + val.suffixNipote), value.url, value.jsonName);
                $('#' + val.prefix + key + val.suffix).change(fnc).change();
            });
        });
    }

    /**
     * Caricamento della select.
     *
     * @param selectPadre  (jQuery) la select padre
     * @param selectFiglio (jQuery) la select figlia
     * @param selectNipote (jQuery) le select nipoti
     * @param url          (String) l'URL da invocare
     * @param jsonName     (String) il nome del parametro JSON da leggere
     * @param event        (Event)  l'evento scatenante l'invocazione
     * @param data         (Object) gli eventuali dati aggiuntivi (Optional)
     */
    function caricaSelect(selectPadre, selectFiglio, selectNipote, url, jsonName, event, data) {
        var uid = selectPadre.val();
        var uidToSet = data && +data.uid;
        var preventEnabling = data && data.doNotEnable;

        event.preventDefault();
        selectNipote.val('').attr('disabled', true);
        selectFiglio.attr('disabled', true);
        if(!uid) {
            selectFiglio.val('');
            return $.Deferred().resolve().promise();
        }
        selectFiglio.overlay('show');

        return $.postJSON(url, {id: uid})
        .then(function(data) {
            var list = data[jsonName];
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            if(!preventEnabling) {
                selectFiglio.removeAttr('disabled');
            }
            selectFiglio.find('option').remove();
            populateOptions(selectFiglio, list, uidToSet);
            selectPadre.trigger('dataLoaded');
        }).always(selectFiglio.overlay.bind(selectFiglio, 'hide'));
    }

    /**
     * Popolamento delle option della select.
     *
     * @param select   (jQuery) la select da popolare
     * @param list     (Array)  la lista da cui ottenere le option
     * @param uidToSet (Number) l'uid da auto-selezionare (Optional)
     */
    function populateOptions(select, list, uidToSet) {
        var str = list.reduce(function(acc, val) {
            var opt = '<option value="' + val.uid + '"';
            if(uidToSet && uidToSet === val.uid) {
                opt += ' selected';
            }
            opt += '>' + val.codice + ' - ' + val.descrizione + '</option>';
            return acc + opt;
        }, '<option></option>');
        select.append(str);
    }

    /**
     * Ricerca della conciliazione.
     *
     * @param waitForSelects (Boolean) se sia necessario aspettare il caricamento delle select
     */
    function ricercaConciliazione(waitForSelects) {
        var fieldset = $('#fieldsetRicercaConciliazioni');
        var divRisultati = $('#divRisultatiRicerca').slideUp();
        var selectToWait;
        $('#divConciliazione_inserimento, #divConciliazione_aggiornamento').slideUp();
        if(waitForSelects) {
            selectToWait = $('input[type="radio"][name="entrataSpesaRicerca"]', '#fieldsetRicercaConciliazioni').filter(':checked') === 'E' ? '#titoloSpesaRicerca' : '#tipologiaTitoloRicerca';
            return $(selectToWait).one('dataLoaded', effettuaRicerca.bind(undefined, divRisultati, fieldset));
        }

        return effettuaRicerca(divRisultati, fieldset);
    }

    /**
     * Effettiva ricerca.
     *
     * @param divRisultati (jQuery) il div dei risultati di ricerca
     * @param fieldset     (jQuery) il fieldset da cui recuperare i dati
     */
    function effettuaRicerca(divRisultati, fieldset) {
        var obj = fieldset.serializeObject();
        fieldset.overlay('show');
        return $.postJSON('gestioneConciliazionePerTitolo_ricercaSintetica.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $('#filtroRicerca').html(data.filtroRicerca);
            $('#classificazioneRisultato').html(thClassificatore[data.entrataSpesaRicerca] || '');
            // Caricamento tabella
            inizializzaTabellaRisultati();
            divRisultati.slideDown();
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
    }

    /**
     * Inizializzazione della tabella dei risultati.
     */
    function inizializzaTabellaRisultati() {
        var tableId = 'tabellaRisultatiRicerca';
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'risultatiRicercaConciliazionePerTitoloAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: false,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
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
                $('#' + tableId + '_processing').parent('div').show();
            },
            fnDrawCallback: function () {
                $('#' + tableId + '_processing').parent('div').hide();
                $('a[rel="popover"]', '#' + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: 'stringaClassificatore'},
                {aTargets: [1], mData: 'stringaClasse'},
                {aTargets: [2], mData: 'stringaConto'},
                {aTargets: [3], mData: 'stringaDescrizioneConto'},
                {aTargets: [4], mData: function(source) {
                    return faseBilancioChiuso ? '' : source.azioni;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).find('a.aggiornaConciliazione')
                            .eventPreventDefault('click', aperturaAggiornamentoConciliazione.bind(undefined, nTd, oData))
                            .end()
                        .find('a.annullaConciliazione')
                            .eventPreventDefault('click', aperturaAnnullamentoConciliazione.bind(undefined, oData));
                }}
            ]
        };
        return $('#' + tableId).dataTable(opts);
    }

    /**
     * Apertura del div per l'aggiornamento della conciliazione.
     *
     * @param td            (Node)   il nodo contente l'informazione
     * @param conciliazione (Object) la conciliazione da aggiornare
     */
    function aperturaAggiornamentoConciliazione(td, conciliazione) {
        var tr = $(td).closest('tr').overlay('show');
        $('#divConciliazione_aggiornamento').slideUp();
        // Ricerca dettaglio
        $.postJSON('gestioneConciliazionePerTitolo_ricercaDettaglio.do', {'conciliazionePerTitolo.uid': conciliazione.uid})
        .then(function(data) {
            // Effettuata ricerca dettaglio
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Imposto il collapse
            return apriCollapseAggiornaConciliazione(data.conciliazionePerTitolo);
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Apertura del div per l'annullamento della conciliazione.
     *
     * @param conciliazione la conciliazione da annullare
     */
    function aperturaAnnullamentoConciliazione(conciliazione) {
        $('#spanElementoSelezionatoModaleEliminazione').html(regexTagContent.exec(conciliazione.stringaClassificatore)[1]);
        $('#pulsanteSiModaleEliminazione').substituteHandler('click', annullaConciliazione.bind(undefined, conciliazione));
        $('#modaleEliminazione').modal('show');
    }

    /**
     * Annullamento effettivo della conciliazione
     *
     * @param conciliazione (Object) la conciliazione da annullare
     */
    function annullaConciliazione(conciliazione) {
        var spinner = $('#SPINNER_pulsanteSiModaleEliminazione').addClass('activated');
        return $.postJSON('gestioneConciliazionePerTitolo_elimina.do', {'conciliazionePerTitolo.uid': conciliazione.uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $('#modaleEliminazione').modal('hide');
            // Ricerco le conciliazioni
            ricercaConciliazione();
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }

    /**
     * Apertura del collapse per la nuova conciliazione.
     */
    function apriCollapseNuovaConciliazione() {
        var entrataSpesa = $('input[name="entrataSpesaRicerca"]').filter(':checked').val() || '';
        var classeDiConciliazione = $('#classeDiConciliazione').val() || '';
        var titoloSpesa = $('#titoloSpesaRicerca').val() || '';
        var macroaggregato = $('#macroaggregatoRicerca').val() || '';
        var titoloEntrata = $('#titoloEntrataRicerca').val() || '';
        var tipologiaTitolo = $('#tipologiaTitoloRicerca').val() || '';
        var categoriaTipologiaTitolo = $('#categoriaTipologiaTitoloRicerca').val() || '';
        var collapse = cleanCollapse('#divConciliazione_inserimento');

        // Popolo i dati piatti
        $('#inserimento_classeDiConciliazioneConciliazionePerTitolo').val(classeDiConciliazione);
        $('input[type="radio"][value="' + entrataSpesa + '"]', '#inserimento_fieldsetConciliazioni').prop('checked', true).trigger('change');
        $('#inserimento_titoloSpesa').val(titoloSpesa).trigger('change', {uid: macroaggregato});
        $('#inserimento_titoloEntrata').val(titoloEntrata).trigger('change', {uid: tipologiaTitolo});
        // Popolamento hidden
        $('#inserimento_classeDiConciliazioneConciliazionePerTitolo_hidden').val(classeDiConciliazione);
        $('#inserimento_entrataSpesa_hidden').val(entrataSpesa);
        $('#inserimento_titoloSpesa_hidden').val(titoloSpesa);
        $('#inserimento_titoloEntrata_hidden').val(titoloEntrata);
        $('#inserimento_tipologiaTitolo_hidden').val(tipologiaTitolo);
        $('#inserimento_macroaggregato_hidden').val(macroaggregato);
        $('#inserimento_categoriaTipologiaTitolo_hidden').val(categoriaTipologiaTitolo);
        if(!!titoloEntrata) {
            $('#inserimento_titoloEntrata').one('dataLoaded', function() {
                $('#inserimento_tipologiaTitolo').val(tipologiaTitolo).trigger('change', {uid: categoriaTipologiaTitolo});
            });
        }
        return collapse.slideDown();
    }

    /**
     * Pulizia del collapse.
     *
     * @param id (String) l'id del collapse
     */
    function cleanCollapse(id) {
        return $(id)
        .find(':input')
            .not('[data-maintain]')
                .val('')
                .end()
            .removeProp('checked')
            .end()
        .find('div[data-tipo]')
            .find(':input')
                .not('[type="hidden"]')
                    .attr('disabled', true)
                    .end()
                .end()
            .slideUp()
            .end()
        .find('span[data-descrizione-conto]')
            .html('')
            .end();
    }

    /**
     * Apertura del collapse per l'aggiornamento della conciliazione.
     *
     * @param conciliazione (Object) la conciliazione tramite cui popolare i dati
     */
    function apriCollapseAggiornaConciliazione(conciliazione) {
        var entrataSpesa = conciliazione.macroaggregato && conciliazione.macroaggregato.uid ? 'S' : 'E';
        var classeDiConciliazione = conciliazione.classeDiConciliazione && conciliazione.classeDiConciliazione._name || '';
        var titoloSpesa = conciliazione.titoloSpesa && conciliazione.titoloSpesa.uid || '';
        var macroaggregato = conciliazione.macroaggregato && conciliazione.macroaggregato.uid || '';
        var titoloEntrata = conciliazione.titoloEntrata && conciliazione.titoloEntrata.uid || '';
        var tipologiaTitolo = conciliazione.tipologiaTitolo && conciliazione.tipologiaTitolo.uid || '';
        var categoriaTipologiaTitolo = conciliazione.categoriaTipologiaTitolo && conciliazione.categoriaTipologiaTitolo.uid || '';
        var classePiano = conciliazione.conto && conciliazione.conto.pianoDeiConti && conciliazione.conto.pianoDeiConti.classePiano && conciliazione.conto.pianoDeiConti.classePiano.uid || '';
        var conto = conciliazione.conto && conciliazione.conto.codice || '';
        var descrizioneConto = conciliazione.conto && conciliazione.conto.descrizione || '';
        var collapse = cleanCollapse('#divConciliazione_aggiornamento');

        // Popolo i dati piatti
        $('#aggiornamento_classeDiConciliazioneConciliazionePerTitolo').val(classeDiConciliazione);
        $('input[type="radio"][value="' + entrataSpesa + '"]', '#aggiornamento_fieldsetConciliazioni').prop('checked', true).trigger('change', {doNotEnable: true});
        $('#aggiornamento_titoloSpesa').val(titoloSpesa).trigger('change', {uid: macroaggregato, doNotEnable: true});
        $('#aggiornamento_titoloEntrata').val(titoloEntrata).trigger('change', {uid: tipologiaTitolo, doNotEnable: true});
        $('#aggiornamento_classePiano').val(classePiano);
        $('#aggiornamento_contoConciliazionePerTitolo').val(conto);
        $('#aggiornamento_descrizioneConto').html(descrizioneConto);

        // Popolamento hidden
        $('#aggiornamento_uidConciliazionePerTitolo_hidden').val(conciliazione.uid);
        $('#aggiornamento_classeDiConciliazioneConciliazionePerTitolo_hidden').val(classeDiConciliazione);
        $('#aggiornamento_entrataSpesa_hidden').val(entrataSpesa);
        $('#aggiornamento_titoloSpesa_hidden').val(titoloSpesa);
        $('#aggiornamento_titoloEntrata_hidden').val(titoloEntrata);
        $('#aggiornamento_tipologiaTitolo_hidden').val(tipologiaTitolo);
        $('#aggiornamento_macroaggregato_hidden').val(macroaggregato);
        $('#aggiornamento_categoriaTipologiaTitolo_hidden').val(categoriaTipologiaTitolo);

        // Carico la categoria DOPO che ho caricato la tipologia
        $('#aggiornamento_titoloEntrata').one('dataLoaded', function() {
            $('#aggiornamento_tipologiaTitolo').val(tipologiaTitolo).trigger('change', {uid: categoriaTipologiaTitolo, doNotEnable: true});
        });

        // Caricamenti AJAX
        return collapse.slideDown();
    }

    /**
     * Gestione della conciliazione: inserimeto e aggiornamento.
     *
     * @param suffix         (String)  il suffisso
     * @param url            (String)  l'URL da invocare
     */
    function gestisciConciliazione(suffix, url) {
        var fieldset = $('#' + suffix + '_fieldsetConciliazioni');
        var obj = fieldset.serializeObject();
        fieldset.overlay('show');
        return $.postJSON(url, obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $('#divConciliazione_' + suffix).slideUp();
            ricercaConciliazione(false);
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
    }

    /**
     * Copia dei dati per poter rieffettuare la ricerca.
     *
     * @param prefix (String) il prefisso da usare
     */
    function copiaDatiPerRicerca(prefix) {
        var entrataSpesa = $('#' + prefix + '_entrataSpesa_hidden').val() || $('input[type="radio"][name="entrataSpesa"]', '#' + prefix + '_fieldsetConciliazioni').filter(':checked').val();
        var classeDiConciliazione = $('#' + prefix + '_classeDiConciliazioneConciliazionePerTitolo_hidden').val() || $('#' + prefix + '_classeDiConciliazioneConciliazionePerTitolo').val();
        var titoloSpesa = $('#' + prefix + '_titoloSpesa_hidden').val() || $('#' + prefix + '_titoloSpesa').val();
        var macroaggregato = $('#' + prefix + '_macroaggregato_hidden').val() || $('#' + prefix + '_macroaggregato').val();
        var titoloEntrata = $('#' + prefix + '_titoloEntrata_hidden').val() || $('#' + prefix + '_titoloEntrata').val();
        var tipologiaTitolo = $('#' + prefix + '_tipologiaTitolo_hidden').val() || $('#' + prefix + '_tipologiaTitolo').val();
        var categoriaTipologiaTitolo = $('#' + prefix + '_categoriaTipologiaTitolo_hidden').val() || $('#' + prefix + '_categoriaTipologiaTitolo').val();

        // Popolo i dati piatti
        $('#classeDiConciliazione').val(classeDiConciliazione);
        $('input[type="radio"][value="' + entrataSpesa + '"]', '#fieldsetRicercaConciliazioni').prop('checked', true).trigger('change');
        $('#titoloSpesaRicerca').val(titoloSpesa).trigger('change', {uid: +macroaggregato});
        $('#titoloEntrataRicerca').val(titoloEntrata).trigger('change', {uid: +tipologiaTitolo});

        // Carico la categoria DOPO che ho caricato la tipologia
        $('#titoloEntrataRicerca').one('dataLoaded', function() {
            $('#tipologiaTitoloRicerca').val(tipologiaTitolo).trigger('change', {uid: +categoriaTipologiaTitolo});
        });
    }

    /**
     * Gestione del conto caricato dalla compilazione guidata.
     *
     * @param event (Event)  l'evento scatenante l'invocazione
     * @param conto (Object) il conto ottenuto
     */
    function gestioneContoCaricato(event, conto) {
        var regex = /(.*?)_.*/;
        var target = event.target;
        var descrizione = conto && conto.descrizione || '';
        var executed = regex.exec(target.id);
        var value = executed && executed[1] || '';
        var campo = $('#' + value + "_descrizioneConto");

        event.stopPropagation();
        campo.html(descrizione);
    }
}(jQuery);