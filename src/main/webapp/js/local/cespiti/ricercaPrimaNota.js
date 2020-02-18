/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
    "use strict";
    var exports = {};
    var PrimaNota;
    var alertErroriModale = $("#ERRORI_modaleRicercaPrimaNota");

    function selectVuota (objectVal) {
        return !objectVal;
    }

    /**
     * Impostazione della tabella con i conti figli ottenuti dalla ricerca.
     */
    function impostaTabellaRisultati() {
        var tableId = "#tabellaRisultatiRicercaPrimaNota";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaPrimeNoteAjax.do",
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
                sZeroRecords: "Non sono presenti prime note per i parametri selezionati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna prima nota disponibile"
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
                var testo = (records === 0 || records > 1) ? (records + " Prime note trovate") : ("1 Prima nota trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: [
                 {aTargets : [ 0 ], mData : function() {
                    return "<input type='radio' name='checkPrimaNota'/>";
                 	},
                 	bSortable: false, 
                 	fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalPrimaNota", oData);
                 	}
                 },
                {aTargets: [1], mData: "tipo"},
                {aTargets: [2], mData: "anno"},
                {aTargets: [3], mData: "numeroLibroGiornale"},
                {aTargets: [4], mData: "tipoEvento"},
                {aTargets: [5], mData: "numeroMovimento"}
            ]
        };
        $(tableId).dataTable(opts);
    }

   
    /**
     * Costruttore per il conto.
     *
     * @param selectorClasse          (String)  il selettore CSS della classe del pianod ei conti
     * @param selectorCodificaInterna (String)  il selettore CSS del codice interno del conto
     * @param selectorCodiceConto     (String)  il selettore CSS del codice  del conto
     * @param automaticSelectConto    (Boolean) se la gestione della select del conto sia da effettuarsi in automatico (Optional - default: true)
     */
    PrimaNota = function(selectorAnno, selectorNumero, selectorTipo, selectorAmbito, modaleDaChiudere) {
        // Campi della pagina
        this.$anno = $(selectorAnno);
        this.$numero = $(selectorNumero);
        this.$tipo = $(selectorTipo);
        this.$ambito = $(selectorAmbito);
        this.$altraModale = $(modaleDaChiudere);

        // I campi del modale
        this.$fieldsetRicerca = $("#fieldsetModaleRicercaPrimaNota");
        this.$annoModale = $("#annoPrimaNota_modale");
        this.$numeroModale = $("#numeroPrimaNota_modale");
        this.$tipoModale = $("#tipoPrimaNota_modale");
        this.$ambitoModale = $("#HIDDEN_ambitoModale");
        
        this.$divRisultati = $("#risultatiRicercaPrimaNota");
        this.$spinnerRicerca = $("#spinnerModaleRicercaPrimaNota");
        this.$tabellaRisultatiRicercaPrimaNota = $("#risultatiRicercaPrimaNota");
        this.$bottoneCercaModaleRicercaPrimaNotaModale = $("#bottoneCercaModaleRicercaPrimaNota");
        this.$pulsanteConfermaModaleRicercaPrimaNota = $("#pulsanteConfermaModaleRicercaPrimaNota");
        this.$modal = $("#comp-PrimaNota");
        
        this.$bottoneCercaModaleRicercaPrimaNotaModale.substituteHandler("click", $.proxy(this.ricercaPrimaNota, this));
        this.$pulsanteConfermaModaleRicercaPrimaNota.substituteHandler("click", $.proxy(this.impostaPrimaNota, this));
        $("#tipoEvento_modale").substituteHandler("change", $.proxy(this.abilitaCampi, this)).change();
        
    };

    PrimaNota.prototype.abilitaCampi = function() {
    	var tipoEvento = $("#tipoEvento_modale").val();
    	if(!!tipoEvento){
    		$("#annoMovimento_modale").removeAttr("disabled");
    		$("#numeroMovimento_modale").removeAttr("disabled");
    		$("#numeroSubmovimento_modale").removeAttr("disabled");
    		$("#codiceContoFinanziario_modale").removeAttr("disabled");
    	}else{
    		$("#annoMovimento_modale").attr("disabled", "disabled").val("");
    		$("#numeroMovimento_modale").attr("disabled", "disabled").val("");
    		$("#numeroSubmovimento_modale").attr("disabled", "disabled").val("");
    		$("#codiceContoFinanziario_modale").attr("disabled", "disabled").val("");
    		
    	}
    };
    /**
     * Ricerca il piano dei conti.
     */
    PrimaNota.prototype.ricercaPrimaNota = function() {
        alertErroriModale.slideUp();
        var obj = this.$fieldsetRicerca.serializeObject();
        var spinner = this.$spinnerRicerca.addClass("activated");

        this.$divRisultati.slideUp();
        var divRisultatiRicercaPrimaNota = this.$divRisultati;
        // Effettuo la ricerca
        $.postJSON("ricercaPrimeNote_effettuaRicercaModale.do", obj, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            impostaTabellaRisultati();
            divRisultatiRicercaPrimaNota.slideDown();
        }).always(function() {
            spinner.removeClass("activated");
        });
    };
    
    
    PrimaNota.prototype.apriModale = function() {
        this.$fieldsetRicerca.find(":input").not("[data-maintain]").val("");
        //Popolo campi modale
        this.$annoModale.val(this.$anno.val());
        this.$numeroModale.val(this.$numero.val());
        this.$tipoModale.val(this.$tipo.val());
        this.$ambitoModale.val(this.$ambito.val());
       
        this.$divRisultati.slideUp();
        alertErroriModale.slideUp();
        if(this.$altraModale){
        	this.$altraModale.modal("hide");	
        }
        this.$modal.modal("show");
    };
  

    /**
     * Imposta i dati della conto all'interno dei campi selezionati.
     *
     * @returns (Conto) l'oggetto su cui e' atata effettuata l'invocazione
     */
    PrimaNota.prototype.impostaPrimaNota = function() {
        var checkedPrimaNota = this.$tabellaRisultatiRicercaPrimaNota.find("[name='checkPrimaNota']:checked");

        // I campi da popolare
        var primaNota;

        // Se non ho selezionato nulla, esco subito
        if(checkedPrimaNota.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare una prima nota"], alertErroriModale, false);
            return;
        }

        // Chiudo il modale e distruggo il datatable
        this.$modal.modal("hide");
        if(this.$altraModale){
        	this.$altraModale.modal("show");	
        }

        primaNota = checkedPrimaNota.data("originalPrimaNota");

        // Copio i campi ove adeguato
        this.$anno.val(primaNota.anno);
        this.$numero.val(primaNota.numeroLibroGiornale);
        this.$tipo.val(primaNota.tipoCausale._name);
        
    };

    exports.inizializza = function(selectorAnno, selectorNumero, selectorTipo, selectorPulsante, selectorAmbito, modaleDaChiudere) {
        var datiPrimaNota = new PrimaNota(selectorAnno, selectorNumero, selectorTipo, selectorAmbito, modaleDaChiudere);
        $(selectorPulsante).click($.proxy(datiPrimaNota.apriModale, datiPrimaNota));
    };

    // Esportazione delle funzionalita'
    global.PrimaNota = exports;

}(jQuery, this);