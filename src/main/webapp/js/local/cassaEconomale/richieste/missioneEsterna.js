/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    'use strict';
    var exports = {};
    var alertErrori = $('#ERRORI');
    var alertErroriModale = $('#ERRORI_modaleMissioneEsterna');
    var $document = $(document);

    var MissioneEsterna = function(idMissioneEsternaField, pulsanteApertura) {
        this.$idMissioneEsterna = $(idMissioneEsternaField);
        this.$pulsanteApertura = $(pulsanteApertura);

        // Dati della modale
        this.$table = $('#tabellaModaleMissioneEsterna');
        this.$pulsanteConferma = $('#pulsanteConfermaModaleMissioneEsterna');
        this.$modal = $('#modaleMissioneEsterna');
        // Dati derivati
        this.$spinnerApertura = this.$pulsanteApertura.find('i.spinner');
        this.$spinnerConferma = this.$pulsanteConferma.find('i.spinner');
        // DataTable
        this.dtTable;
    };

    // Definizione degli export
    exports.inizializza = inizializza;

    /** Costruttore */
    MissioneEsterna.prototype.constructor = MissioneEsterna;

    /**
     * Selezione della missione.
     */
    MissioneEsterna.prototype.selezionaMissione = function() {
        var radio = this.dtTable.$('input[type="radio"]').filter(':checked');
        var id;
        if(!radio.length) {
            // Errore, devo selezionare qualcosa
            impostaDatiNegliAlert(['Necessario selezionare una missione da caricamento esterno'], alertErroriModale);
            return;
        }
        id = radio.val();
        this.$idMissioneEsterna.val(id);
        this.$spinnerConferma.addClass('activated');
        $document.trigger('selectedMissioneEsterna', {idMissioneEsterna: id, callback: this.closeModal.bind(this)});
    };

    /**
     * Chiusura del modale.
     */
    MissioneEsterna.prototype.closeModal = function() {
        this.$spinnerConferma.removeClass('activated');
        this.$modal.modal('hide');
    };

    /**
     * Inizializzazione della funzionalita'.
     */
    MissioneEsterna.prototype.init = function() {
        this.$pulsanteApertura.addClass('disabled').attr('disabled', true);
        this.$spinnerApertura.addClass('activated');
        // Caricamento AJAX
        $.postJSON('ricercaMissioneDaEsterno.do')
        .then(this.loadedMissioni.bind(this))
        .then(this.successPostInit.bind(this))
        .always(this.postInit.bind(this));
    };

    /**
     * Callback di caricamento delle missioni.
     *
     * @param data       (Object) i dati ottenuti dal servizio
     * @param textStatus (String) lo status della risposta
     * @param jqXHR      (Object) l'oggetto XHR utilizzato nell'invocazione asincrona
     */
    MissioneEsterna.prototype.loadedMissioni = function(data, textStatus, jqXHR) {
        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            return $.Deferred().reject(jqXHR, data, 'Errors').promise();
        }
        this.populateTable(data.listaRichiestaEconomale);
    };

    /**
     * Callback di successo dell'init.
     */
    MissioneEsterna.prototype.successPostInit = function() {
        this.$pulsanteApertura.removeClass('disabled').removeAttr('disabled').substituteHandler('click', this.openModal.bind(this));
    };

    /**
     * Callback della funzione init;
     */
    MissioneEsterna.prototype.postInit = function() {
        this.$spinnerApertura.removeClass('activated');
    };

    /**
     * Apertura del modale
     */
    MissioneEsterna.prototype.openModal = function() {
        alertErroriModale.slideUp();
        this.$pulsanteConferma.substituteHandler('click', this.selezionaMissione.bind(this));
        this.$modal.modal('show');
    };

    /**
     * Popolamento della tabella.
     */
    MissioneEsterna.prototype.populateTable = function(list) {
        var opts = {
            aaData: list,
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: false,
            bDestroy: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti missioni da caricamento esterno',
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
                    return '<input type="radio" name="listaIdMissione" value="' + source.idMissioneEsterna + '"/>';
                }},
                {aTargets: [1], mData: defaultPerDataTable('idMissioneEsterna')},
                {aTargets: [2], mData: defaultPerDataTable('soggetto.matricola')},
                {aTargets: [3], mData: defaultPerDataTable('soggetto.denominazione')},
                {aTargets: [4], mData: function(source) {
                    var tmp = [];
                    if(source.dataMissioneEsternaDa) {
                        tmp.push(source.dataMissioneEsternaDa);
                    }
                    if(source.dataMissioneEsternaA) {
                        tmp.push(source.dataMissioneEsternaA);
                    }
                    return tmp.join(' - ');
                }}
            ]
        };
        this.dtTable = this.$table.dataTable(opts);
    };

    /**
     * Inizializzazione della missione esterna.
     *
     * @param idMissioneEsternaField (String) il selettore del campo per l'id della missione esterna
     * @param pulsanteApertura       (String) il selettore del pulsante di apertura
     */
    function inizializza(idMissioneEsternaField, pulsanteApertura) {
        var m = new MissioneEsterna(idMissioneEsternaField || '', pulsanteApertura || '');
        m.init();
    }


    global.MissioneEsterna = exports;
}(jQuery, this);