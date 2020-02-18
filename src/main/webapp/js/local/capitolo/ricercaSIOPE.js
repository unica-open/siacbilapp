/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, g) {
    'use strict';
    var exports = {};
    var $document = $(document);
    var baseOpts = {
        bServerSide: true,
        sServerMethod: 'POST',
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
            sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun dato disponibile'
            }
        },
        aoColumnDefs: [
            {aTargets: [0], mData: function(source) {
                return '<input type="radio" name="modale.classificatore.uid" value="' + source.uid + '" />';
            }, fnCreatedCell: function(nTd, sData, oData) {
                $('input', nTd).data('classificatore', oData);
            }},
            {aTargets: [1], mData: function(source) {
                return source.codice || '';
            }},
            {aTargets: [2], mData: function(source) {
                return source.descrizione || '';
            }}
        ]
    };
    var RicercaSiope;

    // Definizione degli exports
    exports.inizializzazione = inizializzazione;

    /**
     * Crea una classe per la ricerca del SIOPE.
     *
     * @param buttonApertura    (String) il selettore CSS del pulsante di apertura
     * @param campoCodice       (String) il selettore CSS del campo in cui impostare il codice
     * @param campoDescrizione  (String) il selettore CSS del campo in cui impostare la descrizione
     * @param hiddenUid         (String) il selettore CSS del campo hidden in cui impostare l'uid
     * @param hiddenDescrizione (String) il selettore CSS del campo hidden in cui impostare la descrizione
     * @param hiddenTipo        (String) il selettore CSS del campo hidden in cui impostare il tipo di classificatore
     * @param suffix            (String) la stringa da appendere per modificare i selettori css precedenti ("Entrata" o "Spesa")
     * 
     */
    RicercaSiope = function(buttonApertura, campoCodice, campoDescrizione, hiddenUid, hiddenDescrizione, hiddenTipo, suffix) {
        this.$buttonApertura = $(buttonApertura + suffix);
        this.$campoCodice = $(campoCodice + suffix);
        this.$campoDescrizione = $(campoDescrizione + suffix);
        this.$hiddenUid = $(hiddenUid + suffix);
        this.$hiddenDescrizione = $(hiddenDescrizione + suffix);
        this.$hiddenTipo = $(hiddenTipo + suffix);

        this.$fieldsetRicerca = $('#modaleCompilazioneSIOPEFieldset' + suffix);
        this.$campoCodiceRicerca = $('#modaleCompilazioneSIOPE' + suffix + '_codice');
        this.$campoDescrizioneRicerca = $('#modaleCompilazioneSIOPE' + suffix + '_descrizione');
        this.$divTabella = $('#modaleCompilazioneSIOPE' + suffix + 'DivTabella');
        this.$table = $('#modaleCompilazioneSIOPE' + suffix + 'Tabella');
        this.$pulsanteConferma = $('#modaleCompilazioneSIOPE' + suffix + 'PulsanteConferma');
        this.$modal =$('#modaleCompilazioneSIOPE' + suffix);
        this.$alertErrori = $('#ERRORI_modaleCompilazioneSIOPE' + suffix);
        // Campi derivati
        this.$buttonRicerca = this.$fieldsetRicerca.find('button');
        this.$spinnerRicerca = this.$buttonRicerca.find('i.spinner');
        this.ricercaUrl = this.$buttonRicerca.data('ricercaUrl');
        this.ajaxUrl = this.$buttonRicerca.data('ajaxUrl');

        this.opts = $.extend(true, baseOpts, {
            sAjaxSource: this.ajaxUrl,
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#modaleCompilazioneSIOPETabella_processing").parent("div").show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $("#modaleCompilazioneSIOPETabella_processing").parent("div").hide();
            }
        });
    };

    /** Costruttore */
    RicercaSiope.prototype.constructor = RicercaSiope;

    /**
     * Inizializzazione delle funzionalita'.
     */
    RicercaSiope.prototype.init = function() {
        this.$buttonApertura.substituteHandler('click', this.apriModale.bind(this));
        this.$buttonRicerca.substituteHandler('click', this.ricerca.bind(this));
        this.$pulsanteConferma.substituteHandler('click', this.conferma.bind(this));
    };

    /**
     * Apertura della modale.
     */
    RicercaSiope.prototype.apriModale = function() {
        this.$campoCodiceRicerca.val(this.$campoCodice.val());
        this.$campoDescrizioneRicerca.val('');
        this.$divTabella.slideUp();
        this.$modal.modal('show');
    };

    /**
     * Ricerca.
     */
    RicercaSiope.prototype.ricerca = function() {
        // De-qualifico per togliere il dato 'modale.'
        var obj = unqualify(this.$fieldsetRicerca.serializeObject(), 1);

        this.$divTabella.slideUp();
        this.$spinnerRicerca.addClass('activated');

        $.postJSON(this.ricercaUrl, obj)
        .then(this.ajaxCallback.bind(this))
        .always(this.alwaysCallback.bind(this));
    };

    /**
     * Callback per la chiamata ajax.
     *
     * @param data       (Object) i dati dell'invocazione
     * @param textStatus (String) lo status della chiamata
     * @param jqXHR      (XHR)    l'oggetto XHR per la chiamata AJAX
     * @returns (Promise) la promise associata all'invocazione AJAX
     */
    RicercaSiope.prototype.ajaxCallback = function(data, textStatus, jqXHR) {
        if(impostaDatiNegliAlert(data.errori, this.$alertErrori)) {
            return $.Deferred().reject(jqXHR, data, 'Errors').promise();
        }
        // Tutto ok
        this.$divTabella.slideDown();
        this.initDataTable();
        return data;
    };

    /**
     * Callback per l'always.
     */
    RicercaSiope.prototype.alwaysCallback = function() {
        this.$spinnerRicerca.removeClass('activated');
    };

    /**
     * Inizializzazione del datatable.
     */
    RicercaSiope.prototype.initDataTable = function() {
        if($.fn.DataTable.fnIsDataTable(this.$table[0])) {
            this.$table.dataTable().fnDestroy();
        }
        this.$table.dataTable(this.opts);
    };

    /**
     * Conferma
     */
    RicercaSiope.prototype.conferma = function() {
        var selectedRadio;
        var classificatore;
        if(!$.fn.DataTable.fnIsDataTable(this.$table[0])) {
            impostaDatiNegliAlert(['Necessario effettuare una ricerca'], this.$alertErrori);
            return;
        }
        selectedRadio = this.$table.dataTable().$('input:checked');
        if(!selectedRadio.length) {
            impostaDatiNegliAlert(['Necessario selezionare un classificatore'], this.$alertErrori);
            return;
        }
        classificatore = selectedRadio.data('classificatore');
        this.$campoCodice.val(classificatore.codice);
        this.$campoDescrizione.html(classificatore.descrizione);
        this.$hiddenDescrizione.val(classificatore.descrizione);
        this.$hiddenUid.val(classificatore.uid);
        this.$hiddenTipo.val(classificatore.tipoClassificatore.codice);
        $document.trigger('selectedClassificatore', {classificatore: classificatore});
        this.closeModal();
    };

    /**
     * Chiusura del modale.
     */
    RicercaSiope.prototype.closeModal = function() {
        this.$modal.modal('hide');
    };

    /**
     * Inizializzazione.
     *
     * @param buttonApertura    (String) il selettore CSS del pulsante di apertura
     * @param campoCodice       (String) il selettore CSS del campo in cui impostare il codice (Optional - default: '')
     * @param campoDescrizione  (String) il selettore CSS del campo in cui impostare la descrizione (Optional - default: '')
     * @param hiddenUid         (String) il selettore CSS del campo hidden in cui impostare l'uid (Optional - default: '')
     * @param hiddenDescrizione (String) il selettore CSS del campo hidden in cui impostare la descrizione (Optional - default: '')
     * @param hiddenTipo        (String) il selettore CSS del campo hidden in cui impostare il tipo di classificatore (Optional - default: '')
     * @param suffix            (String) la stringa da appendere per modificare i selettori css precedenti ("Entrata" o "Spesa") (Optional- default:'')
     */
    function inizializzazione(buttonApertura, campoCodice, campoDescrizione, hiddenUid, hiddenDescrizione, hiddenTipo, suffix) {
        var rs = new RicercaSiope(buttonApertura, campoCodice || '', campoDescrizione || '', hiddenUid || '', hiddenDescrizione || '', hiddenTipo || '', suffix || '');
        rs.init();
    }

    // Export delle funzionalita'
    g.RicercaSiope = exports;
}(jQuery, this);