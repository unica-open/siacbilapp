/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    'use strict';
    var alertErroriModale = $('#ERRORI_modale_ProvvisorioDiCassa');
    // Progressivo
    var idx = 0;
    var instances = {};
    // Funzionalita' da pubblicare
    var exports = {};

    /**
     * Popolamento della tabella del provvisorio.
     *
     * @param table (jQuery) la tabella da popolare
     */
    function popolaTabella(table) {
        var id = table.attr('id');
        var opts = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaProvvisorioCassaAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            bProcessing: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti provvisori di cassa corrispondenti ai parametri inseriti',
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
                $('#' + id + '_processing').show();
            },
            fnDrawCallback: function () {
                // Attivo i popover
                table.find("a[rel='popover']")
                    .popover()
                    .eventPreventDefault("click");
                $('#' + id + '_processing').hide();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return '<input type="radio" name="modale.provvisorioDiCassa.uid">';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalProvvisorioDiCassa', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('anno')},
                {aTargets: [2], mData: defaultPerDataTable('numero')},
                {aTargets: [3], mData: defaultPerDataTable('dataEmissione', '', formatDate)},
                {aTargets: [4], mData: function(source) {
                    var result = '';
                    if(source.causale) {
                        result = '<a href="#" rel="popover" data-trigger="hover" data-original-title="SubCausale" data-content="' + escapeHtml(source.subCausale) + '">' + source.causale + '</a>';
                    }
                    return result;
                }},
                {aTargets: [5], mData: defaultPerDataTable('denominazioneSoggetto')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }},
                {aTargets: [7], mData: defaultPerDataTable('importoDaRegolarizzare', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }}
            ]
        };

        // Distruggo la vecchia tabella se presente
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            // Sembra che non ricarichi correttamente, senza questa istruzione
        	self.$tabella.dataTable().fnDestroy();
        }

        table.dataTable(opts);
    }

    /**
     * Formattazione dell'importo
     * @param value (number) il valore da formattare
     * @return (string) l'importo formattato
     */
    function formatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return '';
    }

    /**
     * Crea l'oggetto per la gestione del Provvisorio di cassa.
     *
     * @param pulsanteApertura       (String) il selettore CSS del pulsante di apertura del modale
     * @param fieldTipoProvvisorio   (String) il selettore CSS del campo per il tipoProvvisorio
     * @param fieldAnnoProvvisorio   (String) il selettore CSS del campo per l'annoProvvisorio
     * @param fieldNumeroProvvisorio (String) il selettore CSS del campo per il numeroProvvisorio
     * @param spanCausale            (String) il selettore CSS dello span in cui impostare la causale
     */
    var ProvvisorioDiCassa = function(pulsanteApertura, fieldTipoProvvisorio, fieldAnnoProvvisorio, fieldNumeroProvvisorio, spanCausale) {
        this.$tipoProvvisorio = $(fieldTipoProvvisorio);
        this.$annoProvvisorio = $(fieldAnnoProvvisorio);
        this.$numeroProvvisorio = $(fieldNumeroProvvisorio);
        this.$pulsanteApertura = $(pulsanteApertura);
        this.$spanCausale = $(spanCausale);

        this.$pulsanteRicerca = $('#modale_pulsanteRicercaProvvisorioCassa');
        this.$pulsanteConferma = $('#modale_pulsanteConfermaProvvisorioCassa');
        this.$tabella = $('#modale_tabellaProvvisorioCassa');
        this.$modale = $('#modaleRicercaProvvisorioDiCassa');
        this.$divRicerca = $('#campiProvvisorio');
        this.$divElenco = $('#modale_divElencoProvvisorioCassa');
    };

    /** Costruttore */
    ProvvisorioDiCassa.prototype.constructor = ProvvisorioDiCassa;

    /**
     * Apre il modale e copia i dati forniti in input.
     */
    ProvvisorioDiCassa.prototype.apriModale = function() {
        this.$divRicerca.find('input').not('[data-maintain]').val('');
        this.$divElenco.slideUp();
        // Nothing to do
        this.$modale.modal('show');
    };

    /**
     * Inizializzazione delle funzionalit&agrave;.
     */
    ProvvisorioDiCassa.prototype.inizializza = function() {
        this.$pulsanteApertura.substituteHandler('click', this.apriModale.bind(this));
        this.$pulsanteRicerca.substituteHandler('click', this.ricercaProvvisorio.bind(this));
        this.$pulsanteConferma.substituteHandler('click', this.confermaProvvisorio.bind(this));
    };

    /**
     * Distruzione della funzionalita'
     */
    ProvvisorioDiCassa.prototype.destroy = function() {
        this.$pulsanteApertura.off('click');
        this.$pulsanteRicerca.off('click');
        this.$pulsanteConferma.off('click');
    };

    /**
     * Ricerca del provvisorio di cassa.
     */
    ProvvisorioDiCassa.prototype.ricercaProvvisorio = function() {
        var self = this;
        var obj = unqualify(this.$divRicerca.serializeObject(), 1);
        var spinner = this.$pulsanteRicerca.find('i');
        // Forzo il flag da regolarizzare
        obj.flagDaRegolarizzare = 'S';

        // Distruggo la vecchia tabella se presente
        if($.fn.DataTable.fnIsDataTable(self.$tabella[0])) {
            // Sembra che non ricarichi correttamente, senza questa istruzione
        	self.$tabella.dataTable().fnDestroy();
        	 self.$divElenco.slideUp();
        }

        // Attivo lo spinner sul pulsante
        spinner.toggleClass('icon-search icon-spin icon-refresh spinner activated');
        $.postJSON('ricercaProvvisorioCassa.do', obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            alertErroriModale.slideUp();
            popolaTabella(self.$tabella);
            // Apro il div degli elenchi
            self.$divElenco.slideDown();
        }).always(spinner.toggleClass.bind(spinner, 'icon-search icon-spin icon-refresh spinner activated'));
    };

    /**
     * Conferma del provvisorio di cassa.
     */
    ProvvisorioDiCassa.prototype.confermaProvvisorio = function() {
        var selectedRadio;
        var dt;
        var provvisorio;
        if(!$.fn.DataTable.fnIsDataTable(this.$tabella[0])) {
            // Non ho nemmeno inizializzato la tabella. Esco
            return;
        }
        dt = this.$tabella.dataTable();
        selectedRadio = dt.$('input[name="modale.provvisorioDiCassa.uid"]')
            .filter(':checked');
        if(!selectedRadio.length) {
            impostaDatiNegliAlert(['Necessario selezionare un provvisorio'], alertErroriModale, false);
            return;
        }
        provvisorio = selectedRadio.data('originalProvvisorioDiCassa');

        // Imposto i dati nel form originale
        this.$tipoProvvisorio.filter('[value="' + provvisorio.tipoProvvisorioDiCassa._name + '"]')
            .prop('checked', true);
        this.$annoProvvisorio.val(provvisorio.anno);
        this.$numeroProvvisorio.val(provvisorio.numero);
        this.$spanCausale.html(provvisorio.causale);
        
        // SIAC-6780
        var riepilogo = $('#HIDDEN_provvisorioCompletaDefinisci').val();
        if(riepilogo !== undefined && riepilogo === "true"){
            var $importoDaRegolaizzareProvvisorio = $('#importoDaRegolarizzareProvvisorioCassa');
            var $importoProvvisorio = $('#importoProvvisorioCassa');
            $importoDaRegolaizzareProvvisorio.val(formatMoney(provvisorio.importoDaRegolarizzare));
            $importoProvvisorio.val(formatMoney(provvisorio.importo));
        }
        
        // Chiudo il modale
        this.$modale.modal('hide');
    };

    /**
     * Inizalizzazione della gestione.
     *
     * @param pulsanteApertura       (string) il selettore CSS del pulsante di apertura del modale (Optional, default: '')
     * @param fieldTipoProvvisorio   (string) il selettore CSS del campo per il tipoProvvisorio    (Optional, default: '')
     * @param fieldAnnoProvvisorio   (string) il selettore CSS del campo per l'annoProvvisorio     (Optional, default: '')
     * @param fieldNumeroProvvisorio (string) il selettore CSS del campo per il numeroProvvisorio  (Optional, default: '')
     * @param spanCausale            (string) il selettore CSS dello span per la causale           (Optional, default: '')
     * @return (number) l'id dell'elemento
     */
    exports.inizializzazione = function(pulsanteApertura, fieldTipoProvvisorio, fieldAnnoProvvisorio, fieldNumeroProvvisorio, spanCausale) {
        var pdc = new ProvvisorioDiCassa(pulsanteApertura || '#pulsanteCompilazioneGuidataProvvisorioCassa', 
            fieldTipoProvvisorio || '', 
            fieldAnnoProvvisorio || '', 
            fieldNumeroProvvisorio || '', 
            spanCausale || '');
        var index = idx++;
        pdc.inizializza();
        
        instances[index] = pdc;
        return index;
    };
    
    /**
     * Distrugge il gestore per id
     * @param index (number) l'indice del gestore
     */
    exports.destroy = function(index) {
        var obj;
        if(!instances[index]) {
            return;
        }
        obj = instances[index];
        instances[index] = undefined;
        obj.destroy();
        obj = undefined;
    };

    // Esporto le funzionalita' sulla variabile globale
    global.ProvvisorioDiCassa = exports;
}(jQuery, this);