/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function (w, $) {
    'use strict';
    
    //VARIABLES
    var exports = {};
    var $document = $(document);
    var instanceId = 0;
    var instances = [];
    var baseOptionsDataTable = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            fnDrawCallBack: defaultPreDraw,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Nessun elemento disponibile',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            }
        };
    
    //INSTANCE 
    
    var GestioneRegistroA = function(baseUrl, abilitaModificheCespiti, cssSelectorTabella, fncAggiornaCespite, fncEliminaCespite){
    	var selectorTabella = cssSelectorTabella || '#tabellaMovimentiDettaglioRegistroA';
    	this.baseUrl = baseUrl;
    	this.id = instanceId++;
    	this.$tabellaMovimentoDettaglio = $(selectorTabella);
    	this.abilitaModificheCespiti = abilitaModificheCespiti;
    	this.aggiornaCespite = fncAggiornaCespite && typeof fncAggiornaCespite ==="function" ? fncAggiornaCespite : defaultPerDataTable("");
    	this.eliminaCespite = fncEliminaCespite && typeof fncEliminaCespite ==="function" ? fncEliminaCespite : defaultPerDataTable("");
    	
    	this.urlCarimentoTabella = 'risultatiRicercaMovimentoDettaglioRegistroACespiteAjax.do';
    	this.datatable = undefined;
    };
    
    GestioneRegistroA.prototype.constructor = GestioneRegistroA;
    GestioneRegistroA.prototype.gestioneAperturaRigaInterna = gestioneAperturaRigaInterna;
    GestioneRegistroA.prototype.gestioneChiusuraRigaInterna = gestioneChiusuraRigaInterna;
    GestioneRegistroA.prototype.ricaricaDatiTabellaCespite = ricaricaDatiTabellaCespite;
    GestioneRegistroA.prototype.callbackCaricamentoCespiti = callbackCaricamentoCespiti;
    GestioneRegistroA.prototype.caricaTabellaCespitiDelMovimentoDettaglio = caricaTabellaCespitiDelMovimentoDettaglio;
    GestioneRegistroA.prototype.caricaTabellaCespitiDelMovimentoDettaglioCallback = caricaTabellaCespitiDelMovimentoDettaglioCallback;
    GestioneRegistroA.prototype.impostaEventiModificaLegameCespitiPrimaNota = impostaEventiModificaLegameCespitiPrimaNota;
    GestioneRegistroA.prototype.redirectToInserisciCespiti = redirectToInserisciCespiti;
    GestioneRegistroA.prototype.collegaCespite = collegaCespite;
    GestioneRegistroA.prototype.scollegaCespite = scollegaCespite;
    GestioneRegistroA.prototype.modificaCespite = modificaCespite;
    GestioneRegistroA.prototype.apriModaleModificaImportoCespite = apriModaleModificaImportoCespite;
    GestioneRegistroA.prototype.ricaricaTabellaMovimentiDettaglio = ricaricaTabellaMovimentiDettaglio;
    GestioneRegistroA.prototype.impostaTabella = impostaTabella;
    
    exports.inizializza = inizializza;
    exports.destroy = destroy;
    
    w.GestioneRegistroA = exports;
    
    /**
     * Inizializza la gestione.
     *
     * @param campoAnnoMovimento      (String) il campo ove impostare l'anno del movimento (Optional - default: #annoMovimentoMovimentoGestione)
     * @param campoNumeroMovimento    (String) il campo ove impostare il numero del movimento (Optional - default: #numeroMovimentoGestione)
     * @param campoNumeroSubMovimento (String) il campo ove impostare la denominazione (Optional - default: #numeroSubMovimentoGestione)
     * @param campoDescrizione        (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #datiRiferimentoImpegnoSpan)
     * @param spanDisponibilita       (String) il campo ove impostare la disponibilita (Optional - default: '')
     * @param alertErrori             (Jquery) oggetto jQuery che specifica quale alertErroriModale utilizzare
     * @param pulsanteApertura        (String) il pulsante di apertura (Optional - default: '')
     */
    function inizializza(baseUrl, abilitaModificheCespiti, cssSelectorTabella, fncAggiornaCespite, fncEliminaCespite) {
    	
        var inner = new GestioneRegistroA(baseUrl, abilitaModificheCespiti, cssSelectorTabella, fncAggiornaCespite, fncEliminaCespite);
        instances[inner.id] = inner;
        inner.impostaTabella();
        
        return inner;
    }
    
    
    /* CARICAMENTO CESPITI COLEGATI **/
    function ricaricaTabellaMovimentiDettaglio(movimentoDettaglio){
    	var settings;
        var obj;
        this.$tabellaMovimentoDettaglio.overlay('show');
    	if(!this.datatable){
    		this.datatable = this.$tabellaMovimentoDettaglio.dataTable();
    	}
    	settings = this.datatable.fnSettings();
        obj = {
            forceRefresh: true,
            iTotalRecords: settings._iRecordsTotal,
            iTotalDisplayRecords: settings._iRecordsDisplay,
            iDisplayStart: settings._iDisplayStart,
            iDisplayLength: settings._iDisplayLength
        };
        return $.postJSON(this.urlCarimentoTabella, obj).then(callBackRicaricaTabellaMovimentoDettaglio.bind(this, movimentoDettaglio));
    }
    
    function callBackRicaricaTabellaMovimentoDettaglio(movimentoDettaglio){
    	var selectorDettaglio = 'button[data-uid-movimento-det="'+ movimentoDettaglio.uid +'"]';
    	var $tr;
    	this.datatable.fnDraw();
    	$tr = $(selectorDettaglio).closest('tr').overlay('show');
    	this.caricaTabellaCespitiDelMovimentoDettaglio(movimentoDettaglio, $tr);
    	this.$tabellaMovimentoDettaglio.overlay('hide');
    }
    
    function impostaEventiModificaLegameCespitiPrimaNota(movimentoDettaglio, tr, innerTabella){
    	var $tr = $(tr);
    	var $buttonChiudiCespite = $tr.find('button.chiudiCespite');
    	var $buttonInserisciCespite = $tr.find('button.inserisciCespite');
    	var self = this;
    	$buttonChiudiCespite.substituteHandler('click',this.gestioneChiusuraRigaInterna.bind(this));
    	if(!this.abilitaModificheCespiti){
    		$buttonInserisciCespite.attr('disabled', true);
    		$('button.cercaCespite').attr('disabled', true);
    		return;
    	}
    	
    	$buttonInserisciCespite.substituteHandler('click', self.redirectToInserisciCespiti.bind(self,movimentoDettaglio));
    	Cespite.destroy();
    	Cespite.inizializza(true, '#tabellaMovimentiDettaglioRegistroA button.cercaCespite');
    	$(document).substituteHandler('cespitiCaricati', self.collegaCespite.bind(self,movimentoDettaglio,tr, innerTabella));
    }
    
    function redirectToInserisciCespiti(movimentoDettaglio) {
    	
  	    var $body = $('body').overlay('show');
  	    var uidPrimaNota = $('#uidPrimaNota').val();
  	    var stringForm = '<form class="hide" action="aggiornaRegistroACespite_redirectToInserisciCespite.do" method="POST">' +
  	        '<input type="hidden" name="movimentoDettaglio.uid" value="' + movimentoDettaglio.uid + '" />' + 
  	        '<input type="hidden" name="uidPrimaNota" value="' + uidPrimaNota + '" /> ' +
  	        '<input type="hidden" name="importosuRegistroA" value="' + movimentoDettaglio.importoDaInventariareAlienare + '" /> ' + 
  	        '</form>';
  	    var $form = $(stringForm);
  	    if(!this.abilitaModificheCespiti){
  	    	return;
  	    }
        $body.append($form);
        $form.submit();
     }
   
  
  function redirectToAggiornaCespite(movimentoDettaglio, cespite){
	  var $body = $('body').overlay('show');
	  var uidPrimaNota = $('#uidPrimaNota').val();
	  var $form = $('<form class="hide" action="' + this.baseUrl + '_redirectToAggiornaCespite.do" method="POST"><input type="hidden" name="movimentoDettaglio.uid" value="' + movimentoDettaglio.uid + '" /><input type="hidden" name="uidPrimaNota" value="' + uidPrimaNota + '" /><input type="hidden" name="uidCespite" value="' + cespite.uid + '" /></form>');
	  if(!this.abilitaModificheCespiti){
	    	return;
	  }	  
	  $body.append($form);
	  $form.submit();
  }
  
  function apriModaleModificaImportoCespite(movimentoDettaglio, cespite){
  	var $modale = $('#editCespiteRegistroA');
  	if(!this.abilitaModificheCespiti){
    	return;
  	}
  	$('#importoSuPrimaNotaModale').val(cespite.valoreAttualeSuRegistroA);
  	$('#confermaAggiornaCespiteRegistroAModale').substituteHandler('click', modificaImportoCespite.bind(this, movimentoDettaglio, cespite));
  	$modale.modal('show');
  }
  
  function modificaImportoCespite(movimentoDettaglio, cespite){
  	var objToSend = {};
  	var $pulsante = $('#confermaAggiornaCespiteRegistroAModale').overlay('show');
  	var self = this;
  	objToSend.cespite = {};
  	objToSend.cespite.uid = cespite.uid;
  	objToSend.movimentoDettaglio = {};
  	objToSend.movimentoDettaglio.uid = movimentoDettaglio.uid;
  	objToSend.importosuRegistroA = $('#importoSuPrimaNotaModale').val();
  	
  	return $.postJSON(this.baseUrl +'_aggiornaImportoCespite.do', qualify(objToSend))
  	.then(function(data){
  		if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleEditCespiteRegistroA'))){
  			return;
  		}
  		$('#editCespiteRegistroA').modal('hide');
  		self.ricaricaTabellaMovimentiDettaglio(movimentoDettaglio);
  	})
  	.always($pulsante.overlay.bind($pulsante,'hide'));
  }
  
  function modificaCespite(movimentoDettaglio, cespite, nTd){
  	if(!cespite.inserimentoContestualeRegistroA){
  		this.apriModaleModificaImportoCespite(movimentoDettaglio, cespite);
  		return;
  	}
  	impostaRichiestaConfermaUtente("Il cespite risulta essere stato inserito tramite prima nota. &Egrave; possibile aggiornare l'importo su prima nota oppure l'intera scheda cespite",
				apriModaleModificaImportoCespite.bind(this, movimentoDettaglio, cespite),
				redirectToAggiornaCespite.bind(this, movimentoDettaglio, cespite),
				"aggiorna importo",
				"aggiorna scheda"
		);
  	
  }
    
  
  function scollegaCespite(movimentoDettaglio, cespite, nTd){
  	var $row = $(nTd).closest('tr').overlay('show');
  	var objToSend = {};
  	var self = this;
  	var $tabella = $row.closest('table');
  	if(!cespite){
  		return;
  	}
  	
  	objToSend.cespite = {};
  	objToSend.cespite.uid = cespite.uid;
  	objToSend.uidMovimentoDettaglio = movimentoDettaglio.uid;
  	$.postJSON(this.baseUrl +'_scollegaCespiteDaPrimaNota.do',qualify(objToSend))
  	.then(function(data){
  		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
  			return;
  		}
  		impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
  		self.ricaricaTabellaMovimentiDettaglio(movimentoDettaglio);
  		
  	})
  	.always($row.overlay.bind($row,'hide'));
  }
  
  function collegaCespite(movimentoDettaglio,tr, innerTabella, e){
  	var uidsCespiti = e.uidsCespiti;
  	var self = this;
  	var tabellaDataTable = this.$tabellaMovimentoDettaglio.dataTable();
  	var objToSend = {};
  	this.$tabellaMovimentoDettaglio.overlay('show');
  	if(!uidsCespiti || uidsCespiti.length === 0){
  		return;
  	}
  	objToSend.cespite = {};
  	objToSend.cespite.uid = uidsCespiti[0];
  	objToSend.uidCespitiDaCollegare = uidsCespiti;
  	objToSend.uidMovimentoDettaglio = movimentoDettaglio.uid;
  	$.postJSON(this.baseUrl +'_collegaCespiteEsistente.do',qualify(objToSend))
  	.then(function(data){
  		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
  			return;
  		}
  		impostaDatiNegliAlert(data.messaggi, $('#MESSAGGI'));
  		impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'), undefined, false);
  		self.ricaricaTabellaMovimentiDettaglio(movimentoDettaglio);
  	})
  	.always(this.$tabellaMovimentoDettaglio.overlay.bind(this.$tabellaMovimentoDettaglio,'hide'));
  	
  }
  
    /**
     * Pulisce gli handler precedentemente settati per l'aggiornamento
     * */
    function destroy(instance){
    	if(!instance || instance.id === undefined) {
    		return;
    	}
    	instances.splice(instance.id, 1);
    }
    
    
    //funzionalita
    
    function gestioneAperturaRigaInterna(movimentoDettaglio, e){
    	var $tr = $(e.target).closest('tr').overlay('show');
    	var tr = $tr[0];
    	e.preventDefault();
    	if(this.datatable.fnIsOpen(tr)) {
    		this.datatable.fnClose(tr);
    		$tr.overlay('hide');
    	} else {
    		this.caricaTabellaCespitiDelMovimentoDettaglio(movimentoDettaglio, $tr);
    	}
    }
    
    function gestioneChiusuraRigaInterna(e){
    	var trDettaglio = $(e.target).closest('tr');
    	var dataTableDettaglio = trDettaglio.find('table').dataTable();
    	var trToClose = trDettaglio.prev()[0];
    	e.preventDefault();
    	if(dataTableDettaglio){
    		dataTableDettaglio.fnDestroy();
    	}
    	if(this.datatable.fnIsOpen(trToClose)) {
    		this.datatable.fnClose(trToClose);
    	}
    }
    
    /* CARICAMENTO CESPITI COLEGATI **/
    function ricaricaDatiTabellaCespite(movimentoDettaglio, $tabellaCespitiCollegati){
    	var objToSend = {};
    	var objOverlay = $('form').overlay('show');
    	objToSend.movimentoDettaglio = {};
    	objToSend.movimentoDettaglio.uid = movimentoDettaglio.uid;
    	return $.postJSON( this.baseUrl + '_ottieniListaCespitiCollegati.do', qualify(objToSend))
    	.then(this.callbackCaricamentoCespiti.bind(this, $tabellaCespitiCollegati))
    	.always(objOverlay.overlay.bind(objOverlay, 'hide'));
    	
    }
    
    function callbackCaricamentoCespiti(movimentoDettaglio, $tabellaCespitiCollegati, dataLista){
    	var self = this;
    	var opzioniTabellaInner;
    	var opzioniDataTable;
    	 if(impostaDatiNegliAlert(dataLista.errori, $('#ERRORI'))) {
             return;
         }
    	 opzioniTabellaInner = {
     		aaData: dataLista.listaCespitiCollegatiAMovimentoEP,
     		aoColumnDefs: [
     			{aTargets: [0], mData: defaultPerDataTable('inventario')},
     			{aTargets: [1], mData: defaultPerDataTable('codice')},
     			{aTargets: [2], mData: defaultPerDataTable('tipoBene')},
     			{aTargets: [3], mData: defaultPerDataTable('descrizione')},
     			{aTargets: [4], mData: defaultPerDataTable('dataAccessoInventario')},
     			{aTargets: [5], mData: defaultPerDataTable('valoreAttuale'), fnCreatedCell: doAddClassToElement('text-right')},
     			{aTargets: [6], mData: defaultPerDataTable('valoreAttualeSuRegistroA'), fnCreatedCell: doAddClassToElement('text-right')},
				{aTargets: [7], bVisible: this.abilitaModificheCespiti, mData: function(source){
		         	return '<a href="#" class="scollegaCespite" ><i class="icon icon-trash icon-2x"></i></a>';
		            },fnCreatedCell: function(nTd, sData, oData) {
		            	var stringaCespiteContestuale = oData.inserimentoContestualeRegistroA ?
		            			' ed eliminato dal sistema perch&egrave; risulta essere stato inserito tramite la prima nota.'
		            			: '.' ;
		            	if(!self.abilitaModificheCespiti){
			        		  return;
			        	}
		            	$(nTd).find('a.scollegaCespite').substituteHandler('click', 
		            			 impostaRichiestaConfermaUtente.bind(undefined, "Il cespite verr&agrave; scollegato dalla prima nota " + stringaCespiteContestuale + "  Proseguire con l'operazione?", self.scollegaCespite.bind(self,movimentoDettaglio, oData, nTd)));
		        }},
		        {aTargets: [8], bVisible: this.abilitaModificheCespiti, mData: function(source){
		          	return '<a href="#" class="modificaCespite" ><i class="icon-pencil icon-2x"></i></a>';
		          },fnCreatedCell: function(nTd, sData, oData) {		        	  
		        	  if(!self.abilitaModificheCespiti){
		        		  return;
		        	  }
		          	  $(nTd).find('a.modificaCespite').substituteHandler('click', 
		               		self.modificaCespite.bind(self, movimentoDettaglio, oData, nTd));
		        }}
     		]
    	 };
    	      
         opzioniDataTable = $.extend(true,{}, baseOptionsDataTable, opzioniTabellaInner);
         $tabellaCespitiCollegati.dataTable(opzioniDataTable);
    }
    
    function caricaTabellaCespitiDelMovimentoDettaglio(movimentoDettaglio, $tr) {
    	var objCaricaTabella = {};
    	var objCaricaListaCespiti = {};
    	var self = this;
    	var movimentoEP = movimentoDettaglio.movimentoEP;
    	if(!movimentoEP){
    		impostaDatiNegliAlert(['COR_ERR_0001 - Errore di sistema: impossibile reperire i dati del movimento ep'], $('#ERRORI'));
    		return;
    	}
    	objCaricaTabella.codiceEventoCespite = movimentoDettaglio.codiceEventoCespite;
    	objCaricaTabella.tipoCausale = movimentoDettaglio.tipoCausalePrimaNota._name;
    	objCaricaListaCespiti.movimentoDettaglio = {};
    	objCaricaListaCespiti.movimentoDettaglio.uid = movimentoDettaglio.uid;
    	return $.when(
    			$.post(this.baseUrl + '_ottieniTabellaCespiti.do', qualify(objCaricaTabella)),
    			$.postJSON(this.baseUrl + '_ottieniListaCespitiCollegati.do', qualify(objCaricaListaCespiti))
        ).then(function(jqTabella, jqLista){
        	self.caricaTabellaCespitiDelMovimentoDettaglioCallback.bind(self, jqTabella, jqLista, $tr, movimentoDettaglio)();
        })
        .always($tr.overlay.bind($tr, 'hide'));
    }
    
    function caricaTabellaCespitiDelMovimentoDettaglioCallback(jqTabella, jqLista, $tr, movimentoDettaglio) {
    	var dataTabella = jqTabella[0];
        var dataLista = jqLista[0];
    	var innerTr;
    	var tabellaCespitiCollegati;
    	
        if(impostaDatiNegliAlert(dataTabella.errori, $('#ERRORI')) || impostaDatiNegliAlert(dataLista.errori, $('#ERRORI'))) {
            return;
        }
        innerTr = this.datatable.fnOpen($tr[0], dataTabella, 'details');
        tabellaCespitiCollegati = $(innerTr).find('table');
        $(innerTr).find('button.chiudiCespite').substituteHandler('click', this.gestioneChiusuraRigaInterna.bind(this));
        this.impostaEventiModificaLegameCespitiPrimaNota(movimentoDettaglio, innerTr, tabellaCespitiCollegati);
        this.callbackCaricamentoCespiti(movimentoDettaglio,tabellaCespitiCollegati, dataLista);
    }
    
    /** TABELLA MOVIMENTI EP*/
    function impostaTabella() {
        this.$tabellaMovimentoDettaglio.overlay({usePosition: true});
        var self = this;
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : this.urlCarimentoTabella,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            iDisplayStart: $('#HIDDEN_savedDisplayStart').val() || 0,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
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
            fnPreDrawCallback: function() {
                self.$tabellaMovimentoDettaglio.overlay('show');
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#tabellaRisultatiRicercaCespite_processing').parent('div')
                    .hide();
                self.$tabellaMovimentoDettaglio.find('a[rel="popover"]').popover();
                self.$tabellaMovimentoDettaglio.find('a.tooltip-test').tooltip();
                self.$tabellaMovimentoDettaglio.overlay('hide');
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('movimentoFinanziario')},
                {aTargets: [1], mData: defaultPerDataTable('ivaCommerciale')},
                {aTargets: [2], mData: defaultPerDataTable('importoFinanziario'), fnCreatedCell: doAddClassToElement('text-right')},
                {aTargets: [3], mData: defaultPerDataTable('codiceContoCespite')},
                {aTargets: [4], mData: defaultPerDataTable('descrizioneContoCespite')},
                
                {aTargets: [5], mData: defaultPerDataTable('importoDaInventariareAlienare'), fnCreatedCell: doAddClassToElement('text-right')},
                {aTargets: [6], mData: defaultPerDataTable('importoInventariatoString'), fnCreatedCell: doAddClassToElement('text-right')},
                
                {aTargets: [7], mData: defaultPerDataTable('numeroBeni'), fnCreatedCell: doAddClassToElement('text-right')},
                {aTargets: [8], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    var $ntd = $(nTd);
                    $ntd.find('button').substituteHandler('click', self.gestioneAperturaRigaInterna.bind(self, oData));
                }}
            ]
        };
        
        this.datatable = this.$tabellaMovimentoDettaglio.dataTable(opts);
    }
    
    return exports;
}(this, $));

