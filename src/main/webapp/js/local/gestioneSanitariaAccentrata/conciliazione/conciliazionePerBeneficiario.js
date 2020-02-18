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

    // Uses
    $(onPageLoad);

    // Definitions
    /**
     * Funzione attivata al caricamento della pagina: legame delle funzionalita' base
     */
    function onPageLoad() {
        $('#buttonRicercaConciliazioniPerBeneficiario').click(ricercaConciliazione);
        $('#buttonNuovaConciliazione').click(apriCollapseNuovaConciliazione);
        $('#inserimento_buttonRicercaCapitolo').click(ricercaPuntualeCapitoloInserimento);

        $('#inserimento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'inserimento', 'gestioneConciliazionePerBeneficiario_inserisci.do', true));
        $('#aggiornamento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'aggiornamento', 'gestioneConciliazionePerBeneficiario_aggiornamento.do', false));

        definisciPulsantiAnnullamento();
        inizializzaCompilazioneGuidataSoggetto();
    }

    /**
     * Ricerca delle conciliazioni.
     */
    function ricercaConciliazione() {
        var fieldset = $('#fieldsetRicercaBeneficiario');
        var obj = fieldset.serializeObject();
        var divRisultati = $('#divRisultatiRicerca').slideUp();

        fieldset.overlay('show');
        return $.postJSON('gestioneConciliazionePerBeneficiario_ricercaSintetica.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $('#filtroRicerca').html(data.filtroRicerca);
            inizializzaTabellaRisultati();
            divRisultati.slideDown();
            alertErrori.slideUp();
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
    }

    /**
     * Gestione dei pulsanti di annullamento
     */
    function definisciPulsantiAnnullamento() {
        ['inserimento', 'aggiornamento'].forEach(function(prefix) {
            $('#' + prefix + '_buttonAnnulla').click(closeCollapse.bind(undefined, '#divConciliazione_' + prefix));
        });
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
     * Inizializzazione della tabella dei risultati.
     */
    function inizializzaTabellaRisultati() {
        var tableId = 'tabellaRisultatiRicerca';
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'risultatiRicercaConciliazionePerBeneficiarioAjax.do',
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
                {aTargets: [0], mData: 'stringaEntSpe'},
                {aTargets: [1], mData: 'stringaCapitolo'},
                {aTargets: [2], mData: 'stringaClasseDiConciliazione'},
                {aTargets: [3], mData: 'stringaClasse'},
                {aTargets: [4], mData: 'stringaConto'},
                {aTargets: [5], mData: function(source) {
                    return faseBilancioChiuso ? '' : source.azioni;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    // Se la fase di bilancio e' CHIUSO, non faccio nemmeno finta di legare le azioni
                    if(faseBilancioChiuso) {
                        return;
                    }
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
     * Apertura del collapse per la nuova conciliazione.
     *
     * @returns (jQuery) il collapse di inserimento
     */
    function apriCollapseNuovaConciliazione() {
        impostaDatiNegliAlert([], alertErrori);
        impostaDatiNegliAlert([], alertInformazioni);
        return $('#divConciliazione_inserimento')
            .find(':input')
                .removeAttr('readonly')
                .not('[data-maintain]')
                    .val('')
                    .end()
                .end()
            .find('#inserimento_divConciliazione')
                .find('select')
                    .attr('disabled', true)
                    .find('option')
                    	.remove()
                    .end()
                .end()
            .end()
            .find('#inserimento_datiSoggetto')
                .html('')
                .end()
        .slideDown();
    }

    /**
     * Ricerca puntuale del capitolo.
     */
    function ricercaPuntualeCapitoloInserimento() {
        var fieldset = $('#inserimento_fieldsetRicercaCapitolo');
        var fieldsetConferma = $('#inserimento_divConciliazione').slideUp();
        var obj = fieldset.serializeObject();
        var divSpesaEntrata = fieldsetConferma.find('[data-tipo]').find('select, input[type="hidden"]').attr('disabled', true).end().slideUp();

        fieldset.overlay('show');
        return $.postJSON('gestioneConciliazionePerBeneficiario_ricercaCapitoloInserimento.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#inserimento_uidCapitolo').val(data.capitolo.uid);
            fieldsetConferma.slideDown();
            divSpesaEntrata.filter('[data-tipo="' + data.tipoCapitolo + '"]').find('select, input[type="hidden"]').removeAttr('disabled').end().slideDown();
            return data;
        }).always(function(data) {
            fieldset.overlay('hide');
            return data;
        }).then(function(data) {
            var uid;
            var values = [1, 2];
            if(!data.capitolo) {
                return $.Deferred().reject().promise();
            }
            uid = data.capitolo.macroaggregato && data.capitolo.macroaggregato.uid || (data.capitolo.categoriaTipologiaTitolo && data.capitolo.categoriaTipologiaTitolo.uid) || 0;
            values.forEach(function(val) {
                var classe = $('#inserimento_hidden_classeDiConciliazioneConciliazionePerBeneficiario' + val + '_' + data.tipoCapitolo).val();
                var obj = {'conciliazionePerTitolo.classificatoreGerarchico.uid': uid, 'conciliazionePerTitolo.classeDiConciliazione': classe};
                caricaSelectConti(obj, '#inserimento_contoConciliazionePerBeneficiario' + val + '_' + data.tipoCapitolo);
            });
        });
    }

    /**
     * Caricamento della select dei conti a partire dal servizio.
     *
     * @param obj      (Object) l'oggetto per la chiamata AJAX
     * @param id       (String) l'id della select
     * @param uidToSet (Number) l'uid da auto-selezionare (Optional)
     * @returns (Promise) la Promise legata all'invocazione
     */
    function caricaSelectConti(obj, id, uidToSet) {
        var select = $(id).overlay('show')
        	.find('option')
        	.remove()
        	.end();
        return $.postJSON('ricercaContiByConciliazionePerTitolo.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            populateOptions(select, data.listaConto, uidToSet);
        }).always(select.overlay.bind(select, 'hide'));
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
     * Gestione della conciliazione: inserimento e aggiornamento.
     *
     * @param suffix         (String)  il suffisso
     * @param url            (String)  l'URL da invocare
     * @param shouldCopyData (Boolean) se i dati debbano essere copiati o meno
     */
    function gestisciConciliazione(suffix, url, shouldCopyData) {
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
            if(!!shouldCopyData) {
                copiaDatiPerRicerca(suffix, data.soggetto);
            }
            ricercaConciliazione();
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
    }

    /**
     * Copia dei dati per poter rieffettuare la ricerca.
     *
     * @param prefix   (String) il prefisso da usare
     * @param soggetto (Object) il soggetto da cui reperire i dati
     */
    function copiaDatiPerRicerca(prefix, soggetto) {
        var codiceSoggetto = $('#' + prefix + '_codiceSoggettoSoggetto').val();
        var datiSoggetto = soggetto && soggetto.denominazione || '';
        var tipoCapitolo = $('#' + prefix + '_tipoCapitolo_hidden').val() || $('input[type="radio"][name="tipoCapitolo"]', '#' + prefix + '_fieldsetConciliazioni').filter(':checked').val();
        var numeroCapitolo = $('#' + prefix + '_hidden_numeroCapitoloCapitolo').val() || $('#' + prefix + '_numeroCapitoloCapitolo').val();
        var numeroArticolo = $('#' + prefix + '_hidden_numeroArticoloCapitolo').val() || $('#' + prefix + '_numeroArticoloCapitolo').val();
        var numeroUEB = $('#' + prefix + '_hidden_numeroUEBCapitolo').val() || $('#' + prefix + '_numeroUEBCapitolo').val();

        // Popolo i dati piatti
        $('#codiceSoggettoSoggettoRicerca').val(codiceSoggetto);
        $('#containerDatiBeneficiarioRicerca').html(datiSoggetto);
        $('input[type="radio"][name="tipoCapitoloRicerca"][value="' + tipoCapitolo + '"]', '#fieldsetRicercaConciliazioni').prop('checked', true);
        $('#numeroCapitoloCapitoloRicerca').val(numeroCapitolo);
        $('#numeroArticoloCapitoloRicerca').val(numeroArticolo);
        $('#numeroUEBCapitoloRicerca').val(numeroUEB);
    }

    /**
     * Apertura del div per l'annullamento della conciliazione.
     *
     * @param conciliazione la conciliazione da annullare
     */
    function aperturaAnnullamentoConciliazione(conciliazione) {
        var stringaConto = estraiContoDaStringa(conciliazione.stringaConto);
        $('#spanElementoSelezionatoModaleEliminazione').html(conciliazione.stringaClasseDiConciliazione + ' - ' + conciliazione.stringaEntSpe + ' - ' + conciliazione.stringaCapitolo + ' - ' + stringaConto);
        $('#pulsanteSiModaleEliminazione').substituteHandler('click', annullaConciliazione.bind(undefined, conciliazione));
        $('#modaleEliminazione').modal('show');
    }

    /**
     * Estrazione del codice conto dalla stringa contenente il popover.
     *
     * @param str (String) la stringa da cui ottenere i dati del conto
     */
    function estraiContoDaStringa(str) {
        var executed = regexTagContent.exec(str);
        return executed && executed[1] || '';
    }

    /**
     * Annullamento effettivo della conciliazione
     *
     * @param conciliazione (Object) la conciliazione da annullare
     */
    function annullaConciliazione(conciliazione) {
        var spinner = $('#SPINNER_pulsanteSiModaleEliminazione').addClass('activated');
        return $.postJSON('gestioneConciliazionePerBeneficiario_elimina.do', {'conciliazionePerBeneficiario1.uid': conciliazione.uid})
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
     * Apertura del div per l'aggiornamento della conciliazione.
     *
     * @param td            (Node)   il nodo contente l'informazione
     * @param conciliazione (Object) la conciliazione da aggiornare
     */
    function aperturaAggiornamentoConciliazione(td, conciliazione) {
        var tr = $(td).closest('tr').overlay('show');
        $('#ERRORI').slideUp();
        $('#divConciliazione_aggiornamento').slideUp();
        // Ricerca dettaglio
        $.postJSON('gestioneConciliazionePerBeneficiario_ricercaDettaglio.do', {'conciliazionePerBeneficiario1.uid': conciliazione.uid})
        .then(function(data) {
            // Effettuata ricerca dettaglio
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Imposto il collapse
            return apriCollapseAggiornaConciliazione(data.elementoConciliazionePerBeneficiario);
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Apertura del collapse per l'aggiornamento della conciliazione.
     *
     * @param conciliazione (Object) la conciliazione tramite cui popolare i dati
     */
    function apriCollapseAggiornaConciliazione(conciliazione) {
        var tipoCapitolo;
        var uid;
        var obj;
        var div = $('#divConciliazione_aggiornamento').slideUp();
        if(!conciliazione) {
            return;
        }
        tipoCapitolo = conciliazione.entrata ? 'E' : conciliazione.spesa ? 'S' : '';
        uid = conciliazione.uidClassificatore;
        obj = {'conciliazionePerTitolo.classificatoreGerarchico.uid': uid, 'conciliazionePerTitolo.classeDiConciliazione': conciliazione.nameClasseDiConciliazione};

        $('#aggiornamento_codiceSoggettoSoggetto').val(conciliazione.codiceSoggetto);
        $('#aggiornamento_datiSoggetto').html(conciliazione.denominazioneSoggetto);
        $('#aggiornamento_hidden_uidSoggetto').val(conciliazione.uidSoggetto);
        $('#aggiornamento_hidden_uidConciliazionePerBeneficiario').val(conciliazione.uid);
        $('#aggiornamento_hidden_uidCapitolo').val(conciliazione.uidCapitolo);
        $('#aggiornamento_hidden_tipoCapitolo').val(tipoCapitolo);
        $('input[type="radio"][name="tipoCapitolo"][value="' + tipoCapitolo + '"]', '#aggiornamento_fieldsetConciliazioni').prop('checked', true);
        // Dati capitolo
        ['numeroCapitolo', 'numeroArticolo', 'numeroUEB'].forEach(function(value) {
            $('#aggiornamento_' + value + 'Capitolo').val(conciliazione[value]);
            $('#aggiornamento_hidden_' + value + 'Capitolo').val(conciliazione[value]);
        });

        $('#aggiornamento_classeDiConciliazioneConciliazionePerBeneficiario1').val(conciliazione.descrizioneClasseDiConciliazione);
        $('#aggiornamento_hidden_classeDiConciliazioneConciliazionePerBeneficiario1').val(conciliazione.nameClasseDiConciliazione);
        // Caricamento conto
        caricaSelectConti(obj, '#aggiornamento_contoConciliazionePerBeneficiario1', conciliazione.uidConto);
        div.slideDown();
    }

    /**
     * Inizializzazione della compilazione guidata del soggetto
     */
    function inizializzaCompilazioneGuidataSoggetto() {
        // Ricerca
        Soggetto.inizializza('#codiceSoggettoSoggettoRicerca', '', '#containerDatiBeneficiarioRicerca', '', '#buttonCompilazioneGuidataSoggettoRicerca', '', '', false);

        // Inserimento
        Soggetto.inizializza('#inserimento_codiceSoggettoSoggetto', '', '#inserimento_datiSoggetto', '', '#inserimento_buttonCompilazioneGuidataSoggetto');
    }
}(jQuery);