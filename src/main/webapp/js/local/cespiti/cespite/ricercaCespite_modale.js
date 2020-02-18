/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function (w, $) {
    'use strict';

    var exports = {};
   
    var urlCaricaTabella ='risultatiRicercaCespiteAjax.do';
   
    var instance;
    var $document = $(document);
    var selectedDatas = {};
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
       **************                                                                              **************
          ***********                  FUNZIONALITA' RELATIVE ALL'ISTANZA CESPITE                  ***********
            *********                                                                              *********
       ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    
    var Cespite = function(fieldsetSelector, divRisultatiSelector, spinnerRicercaSelector, spinnerConfermaSelector, tabellaRisultatiSelector, pulsanteRicercaSelector, pulsanteConfermaSelector, pulsanteAperturaSelector){
    	this.$fieldset         = fieldsetSelector? $(fieldsetSelector) : $('#fieldset_modaleCespite') ;
    	this.$divRisultati     = divRisultatiSelector? $(divRisultatiSelector) : $('#divCespitiTrovati') ;
    	this.$spinnerConferma  = spinnerConfermaSelector? $(spinnerConfermaSelector) : $('#pulsanteConfermaCespiteModale') ;
    	this.$spinnerRicerca   = spinnerRicercaSelector? $(spinnerRicercaSelector) : $('#SPINNER_pulsanteRicercaCespiteModale');
    	
    	this.$tabellaRisultati = tabellaRisultatiSelector? $(tabellaRisultatiSelector) : $('#tabellaCespiteModale') ;
    	this.$pulsanteRicerca  = pulsanteRicercaSelector? $(pulsanteRicercaSelector) : $('#pulsanteRicercaCespiteModale') ;   
    	this.$pulsanteConferma = pulsanteConfermaSelector? $(pulsanteConfermaSelector) : $('#pulsanteConfermaCespiteModale') ; 
    	this.$pulsanteApertura = pulsanteAperturaSelector? $(pulsanteAperturaSelector) : $('#pulsanteAperturaModaleCespite') ; 
    	
    	this.$alertErrori = $('#erroriCespiteModale');
    	this.$modale = $('#modaleRicercaCespite');
    	this.$pulsanteSelezionaTutto = $('#selezionaTuttiCespiti');
    	this.$inputHiddenStatoBene =$('input[name="flagStatoBene"]');
    	
    }
    
    Cespite.prototype.ricercaCespite = ricercaCespite;
    Cespite.prototype.ricercaCespiteCallback = ricercaCespiteCallback;
    Cespite.prototype.confermaCespite = confermaCespite;
    Cespite.prototype.drawCallBak = drawCallBak;
    Cespite.prototype.apriModale = apriModale;
    Cespite.prototype.saveRiga = saveRiga;
    Cespite.prototype.impostaCespitiNellaTabella = impostaCespitiNellaTabella;
    Cespite.prototype.selectAll = selectAll;

    exports.inizializza = inizializza;
    exports.destroy = destroy;
    w.Cespite = exports;
    
    function inizializza(defaultStatoBene, pulsanteAperturaSelector, fieldsetSelector, divRisultatiSelector, spinnerRicercaSelector, spinnerConfermaSelector, tabellaRisultatiSelector, pulsanteRicercaSelector, pulsanteConfermaSelector){
    	var cespite = new Cespite(fieldsetSelector, divRisultatiSelector, spinnerRicercaSelector, spinnerConfermaSelector, tabellaRisultatiSelector, pulsanteRicercaSelector, pulsanteConfermaSelector, pulsanteAperturaSelector);
            
//            impegno._fncRicercaImpegno = impegno.ricercaImpegno.bind(impegno);
    	cespite._fncRicercaCespite = cespite.ricercaCespite.bind(cespite);
        cespite._fncConfermaCespite = cespite.confermaCespite.bind(cespite);
        cespite._fncApriModale = cespite.apriModale.bind(cespite, defaultStatoBene);
        cespite._fncSaveRiga = cespite.saveRiga.bind(cespite);
        
        cespite.$pulsanteRicerca.substituteHandler('click', cespite._fncRicercaCespite);
        cespite.$pulsanteConferma.substituteHandler('click',  cespite._fncConfermaCespite);
        cespite.$pulsanteApertura.substituteHandler('click', cespite._fncApriModale);
        cespite.$pulsanteSelezionaTutto.substituteHandler('click', cespite.selectAll);
        
        instance = cespite;
    }
    
    function destroy(){
    	if(!instance) {
    		return;
    	}
        //tolgo gli eventi che ho messo su instance
    	instance.$pulsanteRicerca.off('click', instance._fncRicercaCespite);
        instance.$pulsanteConferma.off('click', instance._fncConfermaCespite);
        instance && instance.$table && instance.$table.off("change", "input[name='uidCespite'][type='checkbox']");
        instance.$pulsanteApertura.off('click');
    	
    	//toglio gli eventi su document
    	instance = undefined;
    }
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     **************                                                                              **************
        ***********                  FUNZIONALITA' DELLA TABELLA                                 ***********
          *********                                                                              *********
     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    function drawCallBak(opzioni){
    	defaultDrawCallback(opzioni);
    	this.$tabellaRisultati.find("input[name='uidCespite'][type='checkbox']").substituteHandler("change", this.saveRiga);
    	this.$tabellaRisultati.find("a[rel='popover']").popover();
    }
    
    function impostaCespitiNellaTabella(data){
	   	 var opts = {	   			
	        bServerSide: true,
	        sAjaxSource: urlCaricaTabella,
	        sServerMethod: 'POST',
	        bPaginate: true,
	        bLengthChange: false,
	        iDisplayLength: 5,
	        bSort: false,
	        bInfo: true,
	        bFilter: false,
	        bAutoWidth: true,
	        bProcessing: true,
	        bDestroy: true,
	        oLanguage: {
	            sInfo : '_START_ - _END_ di _MAX_ risultati',
	            sInfoEmpty : '0 risultati',
	            sZeroRecords: 'Non sono presenti cespiti',
	            sProcessing : 'Attendere prego...',
	            oPaginate: {
	                sFirst : 'inizio',
	                sLast : 'fine',
	                sNext : 'succ.',
	                sPrevious : 'prec.',
	                sEmptyTable: 'Nessun cespite disponibile'
	            }
	        },
	        fnPreDrawCallback: defaultPreDraw,
	        fnDrawCallback:this.drawCallBak.bind(this),
	        aoColumnDefs: [
	            {aTargets: [0], mData: function(source) {
	            	var res = "<input type='checkbox' name='uidCespite' data-uid-Cepsite='" + source.uid + "' value='" + source.uid + "'";
	            	if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
	                    res += ' checked';
	                }
	                return res + "/>";
	            }, fnCreatedCell: function(nTd, sData, oData) {
	                $('input', nTd).data('originalCespite', oData);
	            }},
	            {aTargets: [1], mData: defaultPerDataTable('codice')},
	            {aTargets: [2], mData: defaultPerDataTable('tipoBene')},
	            {aTargets: [3], mData: defaultPerDataTable('inventario')},
	            {aTargets: [4], mData: defaultPerDataTable('attivo')}
	        ]
	     };
	   	 if($.fn.DataTable.fnIsDataTable( this.$tabellaRisultati[0])) {
	   		 this.$tabellaRisultati.dataTable().fnDestroy();
	        }
	   	 
	   	 this.$tabellaRisultati.dataTable(opts);
   }
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     **************                                                                              **************
        ***********                  FUNZIONALITA' SELEZIONE MULTIPLA                            ***********
          *********                                                                              *********
     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /**
     * Selezione di tutti i checkbox
     */
    function selectAll() {
        var $this = $(this);
        var table = $this.closest("table");
        var checkboxes = table.find("tbody").find("input[type='checkbox']");
        var toCheck = $this.prop("checked");

        checkboxes.prop("checked", toCheck).each(function(idx, el) {
            selectedDatas[+el.value] = {isSelected: toCheck, data: $(el).data('originalCespite')};
        });
    }
    
    /**
     * Salvataggio della singola riga nell'elenco dei selezionati
     */
    function saveRiga() {
        var $this = $(this);
        var isChecked;
        if($this.attr('id') === 'selezionaTuttiCespiti') {
            return;
        }
        isChecked = $this.prop('checked');
        selectedDatas[+$this.val()] = {isSelected: isChecked, data: $this.data('originalCespite')};
    }
    /**
     * Conferma dell'emissione.
     */
    function filtraCespitiSelezionati() {
    	 var res = [];
         var i;
         var arrayUid = [];
         var oggettoPerInvocazioneAsincrona = {};
         
         for(i in selectedDatas) {
             if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                 res.push(selectedDatas[i].data);
             }
         }
    	 if(!res.length) {
            return arrayUid;
         }
        	 
         res.forEach(function(el) {
             arrayUid.push(el.uid);
         });
         
         return arrayUid;
    }
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     **************                                                                              **************
        ***********                  GESTIONE INTERAZIONE UTENTE                                 ***********
          *********                                                                              *********
     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
    /*
     * Gestisce l'apertura della modale collega cespite
     * 
     */
    function apriModale(defaultStatoBene){
    	this.$fieldset.find('input').val('');
    	if(defaultStatoBene){
    		this.$inputHiddenStatoBene.val('S');
    	}
    	this.$alertErrori.slideUp();
    	this.$divRisultati.slideUp();
    	this.$modale.modal('show');
    }
    
    /*
     * Gestisce la pressione della ricerca del cepsite
     * 
     */
    function ricercaCespite(){
    	var oggettoPerChiamataAjax = unqualify(this.$fieldset.serializeObject());
        this.$alertErrori.slideUp();
        this.$divRisultati.slideUp();
        this.$spinnerRicerca.addClass('activated');
        selectedDatas = {};
        this.$pulsanteSelezionaTutto.removeProp('checked');
        
        return $.postJSON('ricercaCespite_effettuaRicercaModale.do', oggettoPerChiamataAjax)
        .then(this.ricercaCespiteCallback.bind(this))
        .always(this.$spinnerRicerca.removeClass.bind(this.$spinnerRicerca, 'activated'));
    }
    
    /*
     * Gestisce la pressione del pulsante collega cespite
     * 
     */
    function ricercaCespiteCallback(data){
    	if(impostaDatiNegliAlert(data.errori, this.$alertErrori, false)) {
            return;
        }
        this.impostaCespitiNellaTabella(data);

        this.$divRisultati.slideDown();
        return data;
    }
    
    /*
     * Gestisce la pressione del pulsante collega cespite
     * 
     */
    function confermaCespite(){
    	var uids = 	filtraCespitiSelezionati.bind(this)();
    	var numeroCespiti = uids.length;
    	var event;

        // Se non ho selezionato sufficienti predocumenti, fornisco un messaggio di errore ed esco
        if(!numeroCespiti) {
            impostaDatiNegliAlert(['Necessario selezionare almeno un cespite da collegare.'], this.$alertErrori);
            return;
        }
        this.$alertErrori.slideUp();
	    event = $.Event('cespitiCaricati', {'uidsCespiti': uids});
        $document.trigger(event);
        this.$modale.modal('hide');
    }
   

}(this, jQuery));