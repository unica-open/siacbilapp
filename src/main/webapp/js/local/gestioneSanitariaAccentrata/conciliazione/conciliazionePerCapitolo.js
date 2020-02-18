/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    // Variables
    var alertErrori = $('#ERRORI');
    var alertInformazioni = $('#INFORMAZIONI');
    var faseBilancioChiuso = $('#hidden_faseBilancioChiuso').val() === 'true';

    // Uses
    $(onPageLoad);

    // Definitions
    /**
     * Funzione attivata al caricamento della pagina: legame delle funzionalita' base
     */
    function onPageLoad() {
        $('#buttonRicercaCapitolo').click(ricercaPuntualeCapitolo);
        $('#buttonRicercaConciliazioniPerCapitolo').click(ricercaConciliazione);

        definisciPulsantiAnnullamento();

        // Inserimento
        $('#buttonNuovaConciliazione').click(apriCollapseNuovaConciliazione);
        $('#inserimento_buttonRicercaCapitolo').click(ricercaPuntualeCapitoloInserimento);
        $('#inserimento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'inserimento', 'gestioneConciliazionePerCapitolo_inserisci.do', true));
        $('#aggiornamento_buttonSalva').click(gestisciConciliazione.bind(undefined, 'aggiornamento', 'gestioneConciliazionePerCapitolo_aggiornamento.do', false));
    }

    /**
     * Ricerca puntuale del capitolo.
     */
    function ricercaPuntualeCapitolo() {
        var fieldset = $('#fieldsetRicercaCapitolo');
        var fieldsetConferma = $('#fieldsetConfermaCapitolo').slideUp();
        var obj = fieldset.serializeObject();

        fieldset.overlay('show');
        return $.postJSON('gestioneConciliazionePerCapitolo_ricercaCapitolo.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $('#containerDatiCapitolo').html(data.datiCapitoloRicerca);
            $('#filtroRicerca').html(data.filtroRicerca);
            $('#uidCapitoloRicerca').val(data.capitoloRicerca.uid);            
            fieldsetConferma.slideDown();            
            $('#divRisultatiRicerca').slideUp();            
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
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
        return $.postJSON('gestioneConciliazionePerCapitolo_ricercaCapitoloInserimento.do', obj)
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
                var classe = $('#inserimento_hidden_classeDiConciliazioneConciliazionePerCapitolo' + val + '_' + data.tipoCapitolo).val();
                var obj = {'conciliazionePerTitolo.classificatoreGerarchico.uid': uid, 'conciliazionePerTitolo.classeDiConciliazione': classe};
                caricaSelectConti(obj, '#inserimento_contoConciliazionePerCapitolo' + val + '_' + data.tipoCapitolo);
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
        var select = $(id).overlay('show').val('');
        return $.postJSON('ricercaContiByConciliazionePerTitolo.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            populateOptions(select, data.listaConto, uidToSet);
        }).always(select.overlay.bind(select, 'hide'));
    }

    /**
     * Ricerca delle conciliazioni.
     */
    function ricercaConciliazione() {
        var fieldset = $('#fieldsetConfermaCapitolo');
        var obj = fieldset.serializeObject();
        var divRisultati = $("#divRisultatiRicerca").slideUp();

        fieldset.overlay('show');
        return $.postJSON('gestioneConciliazionePerCapitolo_ricercaSintetica.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
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
            sAjaxSource: 'risultatiRicercaConciliazionePerCapitoloAjax.do',
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
                {aTargets: [0], mData: 'stringaClasseDiConciliazione'},
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
        $.postJSON('gestioneConciliazionePerCapitolo_ricercaDettaglio.do', {'conciliazionePerCapitolo1.uid': conciliazione.uid})
        .then(function(data) {
            // Effettuata ricerca dettaglio
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Imposto il collapse
            return apriCollapseAggiornaConciliazione(data.elementoConciliazionePerCapitolo);
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Apertura del div per l'annullamento della conciliazione.
     *
     * @param conciliazione la conciliazione da annullare
     */
    function aperturaAnnullamentoConciliazione(conciliazione) {
        $('#spanElementoSelezionatoModaleEliminazione').html(conciliazione.stringaClasseDiConciliazione + " - " + conciliazione.stringaConto);
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
        return $.postJSON('gestioneConciliazionePerCapitolo_elimina.do', {'conciliazionePerCapitolo1.uid': conciliazione.uid})
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

        $('#aggiornamento_hidden_uidConciliazionePerCapitolo').val(conciliazione.uid);
        $('#aggiornamento_hidden_uidCapitolo').val(conciliazione.uidCapitolo);
        $('#aggiornamento_hidden_tipoCapitolo').val(tipoCapitolo);
        $('input[type="radio"][name="tipoCapitolo"][value="' + tipoCapitolo + '"]', '#aggiornamento_fieldsetConciliazioni').prop('checked', true);
        // Dati capitolo
        ['numeroCapitolo', 'numeroArticolo', 'numeroUEB'].forEach(function(value) {
            $('#aggiornamento_' + value + 'Capitolo').val(conciliazione[value]);
            $('#aggiornamento_hidden_' + value + 'Capitolo').val(conciliazione[value]);
        });

        $('#aggiornamento_classeDiConciliazioneConciliazionePerCapitolo1').val(conciliazione.descrizioneClasseDiConciliazione);
        $('#aggiornamento_hidden_classeDiConciliazioneConciliazionePerCapitolo1').val(conciliazione.nameClasseDiConciliazione);
        // Caricamento conto
        caricaSelectConti(obj, '#aggiornamento_contoConciliazionePerCapitolo1', conciliazione.uidConto);
        div.slideDown();
    }

    /**
     * Apertura del collapse per la nuova conciliazione.
     *
     * @returns (jQuery) il collapse di inserimento
     */
    function apriCollapseNuovaConciliazione() {
        return $('#divConciliazione_inserimento')
            .find(':input')
                .not('[data-maintain]')
                    .val('')
                    .end()
                .end()
            .find('#inserimento_divConciliazione')
                .find('select')
                    .attr('disabled', true)
                .end()
                .slideUp()
            .end()
        .slideDown();
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
        select.html(str);
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
                copiaDatiPerRicerca(suffix);
            }
            ricercaConciliazione();
        }).always(fieldset.overlay.bind(fieldset, 'hide'));
    }

    /**
     * Copia dei dati per poter rieffettuare la ricerca.
     *
     * @param prefix (String) il prefisso da usare
     */
    function copiaDatiPerRicerca(prefix) {
        var tipoCapitolo = $('#' + prefix + '_tipoCapitolo_hidden').val() || $('input[type="radio"][name="tipoCapitolo"]', '#' + prefix + '_fieldsetConciliazioni').filter(':checked').val();
        var numeroCapitolo = $('#' + prefix + '_hidden_numeroCapitoloCapitolo').val() || $('#' + prefix + '_numeroCapitoloCapitolo').val();
        var numeroArticolo = $('#' + prefix + '_hidden_numeroArticoloCapitolo').val() || $('#' + prefix + '_numeroArticoloCapitolo').val();
        var numeroUEB = $('#' + prefix + '_hidden_numeroUEBCapitolo').val() || $('#' + prefix + '_numeroUEBCapitolo').val();
        var uidCapitolo = $('#' + prefix + '_uidCapitolo').val();

        // Popolo i dati piatti
        $('input[type="radio"][name="tipoCapitoloRicerca"][value="' + tipoCapitolo + '"]', '#fieldsetRicercaCapitolo').prop('checked', true);
        $('#numeroCapitoloCapitoloRicerca').val(numeroCapitolo);
        $('#numeroArticoloCapitoloRicerca').val(numeroArticolo);
        $('#numeroUEBCapitoloRicerca').val(numeroUEB);
        $('#uidCapitoloRicerca').val(uidCapitolo);
    }

}(jQuery);