/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
    "use strict";
    var exports = {};
    var Cespite;
    var alertErroriModale = $("#ERRORI_modaleRicercaCespite");

    function selectVuota (objectVal) {
        return !objectVal;
    }

    /**
     * Impostazione della tabella con i conti figli ottenuti dalla ricerca.
     */
    function impostaTabellaRisultati(tableId) {
    	var $table = $(tableId);
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaCespiteAjax.do",
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
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti cespiti per i parametri selezionati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun cespite disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Conti trovati") : ("1 Cespite trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            
            
            aoColumnDefs: [
                 {aTargets : [0], mData : function() {
                    return "<input type='radio' name='checkCespite'/>";
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalCespite", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('codice')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('tipoBene')},
                {aTargets: [4], mData: defaultPerDataTable('classificazione')},
                {aTargets: [5], mData: defaultPerDataTable('inventario')},
                {aTargets: [6], mData: defaultPerDataTable('stato')},
                {aTargets: [7], mData: defaultPerDataTable('statoIter')}
            ]
        };
        if($.fn.dataTable.fnIsDataTable($table[0])) {
        	$table.dataTable().fnDestroy();
        }
        $table.dataTable(opts);
    }

    /**
     * Costruttore per il cespite.
     * @param selectorCodiceCespite      (String)  il selettore CSS del codice  del cespite
     * @param selectorDescrizioneCespite (String)  il selettore CSS della descrizione del cespite
     */
     Cespite = function(selectorCodiceCespite, selectorDescrizioneCespite) {
    	 
    	 this.urlRicerca = 'ricercaCespite_effettuaRicercaModale.do';
    	 this.tableId = '#tabellaRisultatiRicercaCespite';
    	 
    	 // Campi della pagina
    	 this.$codice = $(selectorCodiceCespite);
    	 this.$descrizioneCespite = $(selectorDescrizioneCespite);
    	 
    	 // Campi modale
    	 this.$codiceModale = $('#codiceCespiteRicerca_modale');
    	 this.$fieldsetRicerca = $("#fieldsetModaleRicercaCespite");
    	 this.$divRisultati = $("#risultatiRicercaCespite");
         this.$spinnerRicerca = $("#spinnerModaleRicercaCespite");
         this.$tabellaRisultatiRicercaCespite = $("#tabellaRisultatiRicercaCespite");
         this.$bottoneCercaModaleRicercaTipoBeneCespiteModale = $("#bottoneCercaModaleRicercaTipoBeneCespite");
         this.$pulsanteConfermaModaleRicercaCespite = $("#pulsanteConfermaModaleRicercaCespite");
         this.$modal = $("#comp-CodCespite");
    };

    Cespite.prototype.apriModale = function() {
        this.init();
        this.$codiceModale.val(this.$codice.val());
        this.$divRisultati.slideUp();
        alertErroriModale.slideUp();
        this.$modal.modal("show");
    };

    Cespite.prototype.init = function() {
    	this.$fieldsetRicerca.find(":input").not("[data-maintain]").val("");
    	// Se servono altre pulizie
    	
    	this.$bottoneCercaModaleRicercaTipoBeneCespiteModale.substituteHandler('click', this.cercaCespite.bind(this));
    	
    	this.$pulsanteConfermaModaleRicercaCespite.substituteHandler("click", this.impostaCespite.bind(this));
    };
    
    Cespite.prototype.cercaCespite = function() {
    	var data = this.$fieldsetRicerca.serializeObject();
    	this.$spinnerRicerca.addClass('activated');
    	// Chiamata ajax
    	return $.post(this.urlRicerca, unqualify(data, 1))
    	.then(this.cercaCespiteCallback.bind(this))
    	.always(this.$spinnerRicerca.removeClass.bind(this.$spinnerRicerca, 'activated'));
    };
    
    Cespite.prototype.cercaCespiteCallback = function(data) {
    	if(impostaDatiNegliAlert(data, alertErroriModale)) {
			return;
		}
    	this.$divRisultati.slideDown();
		impostaTabellaRisultati(this.tableId);
    }

    /**
     * Imposta i dati della cespite all'interno dei campi selezionati.
     *
     * @returns (Cespite) l'oggetto su cui e' atata effettuata l'invocazione
     */
    Cespite.prototype.impostaCespite = function() {
        var checkedCespite = this.$tabellaRisultatiRicercaCespite.find("input[name='checkCespite']:checked");

        // I campi da popolare
        var cespite;

        // Se non ho selezionato nulla, esco subito
        if(checkedCespite.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un cespite"], alertErroriModale, false);
            return;
        }

        // Ottengo i dati del soggetto salvati
        cespite = checkedCespite.data("originalCespite");

        // Copio i campi ove adeguato
        this.$codice.val(cespite.codice);
        this.$descrizioneCespite.html(cespite.descrizione);
        
        
        
        // Chiudo il modale
        this.$modal.modal("hide");
        this.$codice.trigger('cespiteCaricato', cespite);
    };

    exports.inizializza = function(selectorCodiceCespite, selectorDescrizioneCespite, selectorPulsante) {
        //var datiCespite = new Cespite(selectorClasse, selectorCodificaInterna, selectorCodiceCespite, selectorDescrizioneCespite, automaticSelectCespite);
    	var datiCespite = new Cespite(selectorCodiceCespite, selectorDescrizioneCespite);
        $(selectorPulsante).click(datiCespite.apriModale.bind(datiCespite));
    };

    // Esportazione delle funzionalita'
    global.Cespite = exports;

}(jQuery, this);