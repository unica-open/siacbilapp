/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 3 delle variazioni - variazione di importi senza gestione UEB */
var Variazioni = Variazioni || {};

var ComponenteInVariazione = ( function(compInVar){
	"use strict";
})(Variazioni);


var CapitoloInVariazione = (function(capVarImp){
	"use strict";
	//variables
	var $pulsanteRegistraImportiNellaVariazione = $("#button_registraVariazioneNuovoDettaglio");
	var alertErrori = $('#ERRORI');
	//capitoli
	capVarImp.init = init;
	
	/* ****************************************************************************** ***/
    /* ****************  UTILITIES  ************************************************ ***/
    /* ***************************************************************************** ***/
	
	
	
	function getStanziamentoNotUndefined(stanziamento){
		return stanziamento || 0;
	}
	
	function impostaEditabilitaSuInput($input, editabile, defaultVal){
    	if(editabile){
    		$input.removeAttr("disabled");
    		return;
    	}
    	$input.attr("disabled", true);
    	if(defaultVal !== undefined){
    		$input.val(defaultVal);
    	}
    }
	
	function apriEChiudiCollapse(e){
		var $target = e && $(e.target);
		var $collapseInterno = $($target.data('selettore-collapse-interno'));
		if($target.attr("disabled")){
			return;
		}
		if($collapseInterno.hasClass('in')){
			return chiudiCollapse($collapseInterno);
    	}
		$collapseInterno.addClass('in')
		$collapseInterno.slideDown();
	}
	
	function chiudiCollapse($collapseInterno){
		if($collapseInterno.hasClass('in')){
			$collapseInterno.removeClass('in');
			$collapseInterno.slideUp();
    	}
	}
	
	function rimuoviOverlay(){
    	$("#tabellaGestioneVariazioni").overlay('hide');
    	$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
    }
	
	
	function pulisciCampiNuovaComponente(suffix){
		var collapse = $('#collapseNuovaComponente' + suffix);
		collapse.find('input').not("[data-maintain]").val("").attr("disabled", true);
		collapse.find('select').val("");
	}
	
	 /**
     * Imposta il valore presente nella variazione di competenza anche come variazione della cassa, nel caso in cui
     * la variazione degli importi di cassa sia possibile.
     */
    function impostaValoreCassaSeApplicabile(suffix){

    	var innerSuffix = suffix || '';
        var inputCompetenza = $("#competenzaVariazioneAnno0" + innerSuffix);
        var inputCassa = $("#cassaVariazione" + innerSuffix);

        if(!inputCassa.attr("disabled")) {
            inputCassa.val(inputCompetenza.val()).trigger('blur');
        }
    }
    
    /**
     * Imposta i dati nella tabella, relativamente all'indice selezionato.
     *
     * @param importo l'oggetto contente gli importi da inserire
     * @param indice  l'indice relativo alla colonna in cui inserire i dati
     */

    function impostaDati (importo, indice) {
        capVarImp.impostaValutaEAllineaADestra("#totaleCompetenzaTrovatoAnno" + indice, importo.stanziamento);
        capVarImp.impostaValutaEAllineaADestra("#totaleResiduiTrovatoAnno" + indice, importo.stanziamentoResiduo);
        capVarImp.impostaValutaEAllineaADestra("#totaleCassaTrovatoAnno" + indice, importo.stanziamentoCassa);
    }
    
    /**
     * Imposta gli stanziamenti nella tabella.
     *
     * @param importo l'oggetto contente gli importi da inserire
     * @param tabella la tabella in cui inserire i dati
     */
    function impostaStanziamenti(importo, tabella) {
    	var anno = importo.annoCompetenza,
            tabellaStanziamenti = tabella.first().children("th").slice(1);
        if(anno === parseInt(tabellaStanziamenti.eq(0).html(), 10)) {
            impostaDati(importo, 0);
        } else if (anno === parseInt(tabellaStanziamenti.eq(1).html(), 10)) {
            impostaDati(importo, 1);
        } else if (anno === parseInt(tabellaStanziamenti.eq(2).html(), 10)) {
            impostaDati(importo, 2);
        }
    }
    
    /**
     * 
     * 
     * */
    function svuotaFormCapitolo(){
    	$('#collapse_ricerca').find('input[type="radio"]').removeProp('checked');
    	$('#numeroCapitolo').val("");
    	$('#numeroArticolo').val("");
    	//svuoto anche il pezzo delle componenti
    	$('#tipoComponenteVariazione').val('');
    	$('#componenteInVariazioneDaEliminare').val('');
    	$('#importoComponenteInVariazione').val('');
    }
    
    function svuotaRisultatiRicercaCapitolo(){
    	$('#nuovoDettaglioVariazioneImporti').find('input, select').not(':disabled').val('');
    	$('#componenteInVariazioneDaEliminare').removeProp('checked');
    	$('#competenzaVariazioneAnno0NuovoDettaglio').val('0,00');
    	$('#competenzaVariazioneAnno1NuovoDettaglio').val('0,00');
    	$('#competenzaVariazioneAnno2NuovoDettaglio').val('0,00');
    	$('#cassaVariazioneNuovoDettaglio').val('0,00');
    	$('#residuoVariazioneNuovoDettaglio').val('0,00');
    }

    /**
     * 
     * 
     * */
    function ottieniTipoApplicazioneCapitolo(tipoApplicazione){
		var tipoApplicazioneNotUndefined = tipoApplicazione || $("#HIDDEN_tipoApplicazione").val();
		return (tipoApplicazioneNotUndefined === "Previsione" || tipoApplicazioneNotUndefined === "PREVISIONE" ? "Previsione" : "Gestione");
	}
    
    /**
     *  Apre e imposta la modale di conferma prosecuzione dell'azione
     * */
	function gestioneRichiestaProsecuzione(array, url, ajaxParam, isCassaIncongruente, isCassaIncongruenteDopoDefinizione) {
		
		ajaxParam["specificaImporti.ignoraValidazione"] = true;
		ajaxParam["specificaImporti.ignoraValidazioneImportiDopoDefinizione"] = !!isCassaIncongruenteDopoDefinizione;

		impostaRichiestaConfermaUtente(array[0].descrizione, effettuaChiamataAssociaCapitoloAVariazione.bind(undefined, url, ajaxParam), rimuoviOverlay);
	}
	
	   
	  /**
     * Controllo che i campi della ricerca del capitolo siano compilati correttamente
     * */
    function controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo){
    	var erroriArray = []; 
    	// Controllo che i campi siano compilati correttamente
        if(annoCapitolo === "") {
            erroriArray.push("Il campo Anno deve essere compilato");
        }
        if(numeroCapitolo === "" || !$.isNumeric(numeroCapitolo)) {
            erroriArray.push("Il campo Capitolo deve essere compilato");
        }
        if(numeroArticolo === "" || !$.isNumeric(numeroArticolo)) {
            erroriArray.push("Il campo Articolo deve essere compilato");
        }
        if(tipoCapitolo === undefined) {
            erroriArray.push("Il tipo di capitolo deve essere selezionato");
        }
        return erroriArray;
    }

    
    /*********************************************************************************
     **********************************************************************************
     ************************ COMPONENTI **********************************************
     **********************************************************************************
     **********************************************************************************/
   
     function caricaComponentiNuovoDettaglio(capitolo, wrapTipoCapitoloApplicazione){
 	  	var $fieldset = $('#fielsetInserimentoImportiNuovoDettaglio').overlay('show');
 	  	var $fieldsetInserimento = $('#fieldset_inserimentoVariazioneImporti').overlay('show');
 	      var chiaveCapitolo = capitolo.uid;
 	      var containerStanziamentiPerComponenti = $('#containerTabellaStanziamentiTotaliPerComponetnti').html('');
 	      $fieldset.overlay('show');
 	      
 	      $fieldset.find('input').val('0,00');
 	      
 	      return $.post('ricercaComponentiCap' + wrapTipoCapitoloApplicazione + '.do', {uidCapitoloPerRicercaComponenti: chiaveCapitolo})
 	      .then(function(data) {
 	      	var competenzaRow;
 	           if(typeof data !== 'string' && impostaDatiNegliAlert(data.errori, alertErrori)) {
 	               return;
 	           }
 	           
 	           // Imposto l'html nel contenitore
 	           containerStanziamentiPerComponenti.html(data);
 	           containerStanziamentiPerComponenti.slideDown();
 	           competenzaRow = $(".righeComponenti");
 	           $("#competenzaTotaleInserimento").eventPreventDefault('click', competenzaRow.toggle.bind(competenzaRow));
 	           $fieldsetInserimento.overlay('hide');
 	           impostaNuovoDettaglioConComponentiVariazioneImporti(capitolo);
 	           
 	      }).always($fieldset.overlay.bind($fieldset, 'hide'));
 	  }

 	function impostaNuovoDettaglioConComponentiVariazioneImporti(capitolo){
 		var capitoloStandard = capitolo.categoriaCapitolo && (capitolo.categoriaCapitolo.codice === 'STD');
	    var capitoloSelezionatoFondino;
	    var capitoloBudget = false;
	    if(capitolo.classificatoriGenerici){
	    	 $.each(capitolo.classificatoriGenerici, function(cl){
	    		 if(!capitoloBudget && this.codice === '01' && this.tipoClassificatore && this.tipoClassificatore.codice=='CLASSIFICATORE_3'){
	    			capitoloBudget = true;
	    		 }
	    	 });
	      }
	      
	     capitoloSelezionatoFondino = (capitoloStandard && capitoloBudget);
 	  	return $.postJSON("caricaComponentiImportoPerNuovoDettaglioVariazione_aggiornamento.do", {"specificaImporti.uidCapitoloComponenti" : capitolo.uid, "specificaImporti.capitoloFondino" : capitoloSelezionatoFondino })
 	  	.then(function(data){
            //SIAC-7228
 	  		var listaComponenti;
 	  		if(impostaDatiNegliAlert(data.errori, alertErrori)){
 	  			return;
 	  		}
 	  		pulisciCampiNuovaComponente("NuovoDettaglio");
 	  		impostaTabellaComponentiInVariazione(data.specificaImporti.componentiCapitoloNuovoDettaglio,  'NuovoDettaglio', true);
 	    	$('[id^="competenzaVariazioneAnno"][id$="NuovoDettaglio"]').attr('disabled', true);
 	    	$('#listaTipoComponenteNuovoDettaglio').substituteHandler('change', gestisciEditabilitaImportiNuovaComponenteByTipoSelezionato.bind(undefined, 'NuovoDettaglio'));
 	        $('#button_salvaNuovaComponenteNuovoDettaglio').substituteHandler('click', inserisciNuovaComponente.bind(undefined, true, 'NuovoDettaglio'));
 	  		$('#divComponentiInVariazioneNuovoDettaglio').show();
 	  	})
 	  	.always($.noop);
 	  }
   
   
 	function pulisciCampiPerComponenti(suffix){
 		var innerSuffix = suffix || '';
 		var tabellaComponenti = $('#tabellaStanziamentiTotaliPerComponente' + innerSuffix);
 		var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);
 		$('#divComponentiInVariazione' + innerSuffix).hide();
 		$('#fielsetInserimentoImporti' + innerSuffix).overlay('hide');
 		
 		$("#button_registraVariazione" + suffix).removeAttr("disabled");
 		
 		impostaEditabilitaSuInput($('[id^="competenzaVariazione"][id$="' + innerSuffix + '"]'), true);
 		
 		if($(tabelleGiaInDataTable).filter(tabellaComponenti).length > 0) {
 			tabellaComponenti.dataTable().fnClearTable(false);
 			tabellaComponenti.dataTable().fnDestroy();
 		}
 	  }
   

     function creaOggettoDaMandareComponente(dettaglio, $tr){
     	var objToSend = {};
     	var inputs = $tr.find('input[type="text"]');
     	var importoAnno0InTabella = $tr.find('input[type="text"].anno0').val();
     	var importoAnno1InTabella =  $tr.find('input[type="text"].anno1').val();
     	var importoAnno2InTabella =  $tr.find('input[type="text"].anno2').val();
     	
     	objToSend["dettaglioAnno0.componenteImportiCapitolo.uid"] = dettaglio.componenteImportiCapitolo? dettaglio.componenteImportiCapitolo.uid : 0;
     	objToSend["dettaglioAnno0.componenteImportiCapitolo.tipoComponenteImportiCapitolo.uid"] = dettaglio.componenteImportiCapitolo && dettaglio.componenteImportiCapitolo.tipoComponenteImportiCapitolo? dettaglio.componenteImportiCapitolo.tipoComponenteImportiCapitolo.uid : 0;
     	objToSend["eliminaSuTuttiGliAnni"] = $tr.find('input[type="checkbox"]').attr('checked') === 'checked';
     	objToSend["dettaglioAnno0.tipoDettaglioComponenteImportiCapitolo"] = dettaglio.tipoDettaglioComponenteImportiCapitolo ? dettaglio.tipoDettaglioComponenteImportiCapitolo._name : "";
     	objToSend["dettaglioAnno0.importo"] = importoAnno0InTabella === null || importoAnno0InTabella === undefined? dettaglio.importoAnno0 : importoAnno0InTabella;
     	objToSend["dettaglioAnno1.importo"] = importoAnno1InTabella === null || importoAnno1InTabella === undefined? dettaglio.importoAnno1 : importoAnno1InTabella;
     	objToSend["dettaglioAnno2.importo"] = importoAnno2InTabella === null || importoAnno2InTabella === undefined? dettaglio.importoAnno2 : importoAnno2InTabella;
     	return qualify(objToSend, "specificaImporti.elementoComponenteModificata");
     }
     
     
     /**
      * 
      * 
      * */
     function caricaEPopolaTabellaComponentiCapitoloInVariazione(capitolo){
     	var containerComponenti = $('#divComponentiInVariazioneModale');
     	var oggettoPerChiamata = {};
     	oggettoPerChiamata.uidCapitoloAssociatoComponenti = capitolo.uid;
     	return $.postJSON('ricercaComponentiCapitoloInVariazione_aggiornamento.do', qualify(oggettoPerChiamata, 'specificaImporti'))
     		.then(function(data){
     			if(impostaDatiNegliAlert(data.errori, alertErrori)){
     				//in questo caso, lascio l'overlay: non ho caricato le componenti, non voglio che si possa cliccare
     				return;
     			}
     			pulisciCampiNuovaComponente("Modale");
     			$("#button_registraVariazioneModale").removeAttr('disabled');
     			$('#linkCollapseComponentiModale').removeAttr("disabled");
     			
     	    	impostaTabellaComponentiInVariazione(data.specificaImporti.componentiCapitoloDettaglio,  'Modale', false);
     	    	
     	    	$('[id^="competenzaVariazioneAnno"][id$="Modale"]').attr('disabled', true);
     	    	$('#listaTipoComponenteModale').substituteHandler('change', gestisciEditabilitaImportiNuovaComponenteByTipoSelezionato.bind(undefined, 'Modale'));
     	        $('#button_salvaNuovaComponenteModale').substituteHandler('click', inserisciNuovaComponente.bind(undefined, false, 'Modale'));
     	        
     	        containerComponenti.overlay('hide');
     		});
     }
     

     function inserisciNuovaComponente(nuovoDettaglio, suffix){
     	var objToSend = {};
     	var stringaSelettoreNuovoDettaglio = nuovoDettaglio? 'Nuovo' : '';
     	var innerSuffix = suffix || '';
     	objToSend["dettaglioAnno0.componenteImportiCapitolo.tipoComponenteImportiCapitolo.uid"] = $('#listaTipoComponente' + innerSuffix).val();
     	objToSend["dettaglioAnno0.tipoDettaglioComponenteImportiCapitolo"] = 'STANZIAMENTO';
     	objToSend["dettaglioAnno0.importo"] = $('#importoComponenteAnno0InVariazione' + innerSuffix).val();
     	objToSend["dettaglioAnno1.importo"] = $('#importoComponenteAnno1InVariazione' + innerSuffix).val();
     	objToSend["dettaglioAnno2.importo"] = $('#importoComponenteAnno2InVariazione' + innerSuffix).val();
     	return $.postJSON('inserisciNuovaComponente' + stringaSelettoreNuovoDettaglio + 'DettaglioVariazione_aggiornamento.do', qualify(objToSend, "specificaImporti.elementoComponenteModificata"))
     		.then(function(data){
     			var lista;
     			var errori = nuovoDettaglio? alertErrori : $('#ERRORI_modaleEditStanziamenti');
     			if(impostaDatiNegliAlert(data.errori, errori)){
     				return;
     			}
     			lista = nuovoDettaglio? data.specificaImporti.componentiCapitoloNuovoDettaglio : data.specificaImporti.componentiCapitoloDettaglio;
     			aggiornaDatiComponentiPerModificaInLista(nuovoDettaglio, lista, data.specificaImporti['competenzaTotale' + stringaSelettoreNuovoDettaglio +'Anno0'], data.specificaImporti['competenzaTotale' + stringaSelettoreNuovoDettaglio +'Anno1'], data.specificaImporti['competenzaTotale' + stringaSelettoreNuovoDettaglio +'Anno2']);
     			chiudiCollapse($('#collapseNuovaComponente'+ innerSuffix), $('#buttonNuovaComponente' + innerSuffix));
     			pulisciCampiNuovaComponente(innerSuffix);
     		})
     		.always($.noop);
     }
     
     function eliminaComponente(nTd,dettaglio, nuovoDettaglio){
     	var $tr = $(nTd).closest('tr');
     	var stringaSelettoreNuovoDettaglio = nuovoDettaglio  ? 'Nuovo' : '';
     	$tr.overlay('show');
     	return $.postJSON('eliminaComponente' + stringaSelettoreNuovoDettaglio +  'DettaglioVariazione_aggiornamento.do', creaOggettoDaMandareComponente(dettaglio, $tr))
 		.then(function(data){
 			var lista;
 			if(impostaDatiNegliAlert(data.errori, alertErrori)){
 				return;
 			}
 			lista = nuovoDettaglio? data.specificaImporti.componentiCapitoloNuovoDettaglio : data.specificaImporti.componentiCapitoloDettaglio;
 			aggiornaDatiComponentiPerModificaInLista(nuovoDettaglio, lista, data.specificaImporti['competenzaTotale'+ stringaSelettoreNuovoDettaglio +'Anno0'], data.specificaImporti['competenzaTotale'+ stringaSelettoreNuovoDettaglio +'Anno1'], data.specificaImporti['competenzaTotale'+ stringaSelettoreNuovoDettaglio +'Anno2'] );
 		})
 		.always($tr.overlay.bind($tr, 'hide'));
     }
     
     function gestisciChangeEliminaComponente(nTd, dettaglio){
    	 var tr = $(nTd).closest('tr');
    	 var $checkbox = tr.find('input[type="checkbox"]').not(':disabled');
    	 var $input = tr.find('input[type="text"]');
    	 var componenteDaEliminare = $checkbox.prop('checked');
    	 if($checkbox.length == 0){
    		 return;
    	 }
    	 
    	 if( componenteDaEliminare){
    		 tr.find('.anno0').val('-' + dettaglio.importoComponenteOriginaleCapitoloAnno0).blur();
    		 tr.find('.anno1').val('-' + dettaglio.importoComponenteOriginaleCapitoloAnno1).blur();
    		 tr.find('.anno2').val('-' + dettaglio.importoComponenteOriginaleCapitoloAnno2).blur();
    	 }
    	 $input[componenteDaEliminare ? "attr" : "removeAttr" ]("disabled", true);
    	 
     }
     
     function gestisciEditabilitaInputImportoComponenteInTabella(nTd,dettaglio, innerSuffix, nuovoDettaglio){
     	var $nTd = $(nTd);
     	var $inputCheckbox = $nTd.closest('tr').find('input[type="checkbox"]');
     	var $input = $nTd.closest('tr').find('input[type="text"]');
     	var inputDaAbilitare = $inputCheckbox.attr('disabled') === 'disabled';
     	var componenteDaEliminare = $inputCheckbox.attr('checked');
     		
     	$nTd.find('i').removeClass('icon-lock').toggleClass('icon-lock icon-unlock');
     	
     	$inputCheckbox[inputDaAbilitare ? "removeAttr" : "attr" ]("disabled", true);
     	$input[inputDaAbilitare && !componenteDaEliminare ? "removeAttr" : "attr" ]("disabled", true);
     	
     	$('#button_registraVariazione' + innerSuffix)[inputDaAbilitare ? "attr" : "removeAttr" ]('disabled', true);
     	
     	if(!inputDaAbilitare){
     		aggiornaComponente(nTd, dettaglio, nuovoDettaglio);
     	}
     }
     
     function gestisciEditabilitaImportiNuovaComponenteByTipoSelezionato(suffix){
     	var uidTipoComponente = $('#listaTipoComponente' + suffix).val();
     	var importi;
     	var objToSend = {};
     	if(!uidTipoComponente || uidTipoComponente == '0'){
     		return;
     	}
     	importi = $('[id^="importoComponenteAnno"][id$="' + suffix  + '"]').overlay('show');
     	objToSend['tipoComponenteSelezionata.uid'] = uidTipoComponente;
     	objToSend['tipoDettaglioSelezionato'] = 'STANZIAMENTO';
     	$.postJSON('ottieniEditabilitaByTipoComponente_aggiornamento.do', qualify(objToSend, 'specificaImporti'))
     	.then(function(data){
     		if(impostaDatiNegliAlert(data.errori, alertErrori)){
     			return;
     		}
     		impostaEditabilitaSuInput($('#importoComponenteAnno0InVariazione' + suffix), data.specificaImporti.editabileAnno0, '0,00');
     		impostaEditabilitaSuInput($('#importoComponenteAnno1InVariazione' + suffix), data.specificaImporti.editabileAnno1, '0,00');
     		impostaEditabilitaSuInput($('#importoComponenteAnno2InVariazione' + suffix), data.specificaImporti.editabileAnno2, '0,00');
     	}).always(importi.overlay.bind(importi, 'hide'));
     }
     
     function aggiornaComponente(nTd, dettaglio, nuovoDettaglio){
     	var $tr = $(nTd).closest('tr');
     	var stringaNuovoDettaglio = nuovoDettaglio? 'Nuovo' : '' ;  
     	
     	$tr.overlay('show');
     	
     	return $.postJSON('aggiornaComponente' + stringaNuovoDettaglio + 'DettaglioVariazione_aggiornamento.do', creaOggettoDaMandareComponente(dettaglio, $tr))
 		.then(function(data){
 			var lista;
 			var listaAnnoBilancioPiuUno;
 			var listaAnnoBilancioPiuDue;
 			var competenza;
 			var competenzaAnnoBilancioPiuUno;
 			var competenzaAnnoBilancioPiuDue;
 			if(impostaDatiNegliAlert(data.errori, alertErrori)){
 				return;
 			}
 			//importi anno = anno bilancio
 			lista = nuovoDettaglio? data.specificaImporti.componentiCapitoloNuovoDettaglio : data.specificaImporti.componentiCapitoloDettaglio;
 			competenza = data.specificaImporti['competenza' + stringaNuovoDettaglio + 'Anno0'];
 			
 			aggiornaDatiComponentiPerModificaInLista(nuovoDettaglio, lista, data.specificaImporti['competenzaTotale' + stringaNuovoDettaglio + 'Anno0'], data.specificaImporti['competenzaTotale' + stringaNuovoDettaglio + 'Anno1'], data.specificaImporti['competenzaTotale' + stringaNuovoDettaglio + 'Anno2']);
 		})
 		.always($tr.overlay.bind($tr, 'hide'));
     }
     
     function aggiornaDatiComponentiPerModificaInLista(nuovoDettaglio, lista, competenzaTotaleAnno0, competenzaTotaleAnno1, competenzaTotaleAnno2){
     	var innerSuffix = nuovoDettaglio? 'NuovoDettaglio' : 'Modale';
     	
     	//imposto la tabella
     	impostaTabellaComponentiInVariazione(lista,  innerSuffix, nuovoDettaglio);
     	
     	//imposto i campi associati
     	
         $('#competenzaVariazioneAnno0' + innerSuffix).val(formatMoney(competenzaTotaleAnno0) || '0,00').trigger('blur');
         $('#competenzaVariazioneAnno1' + innerSuffix).val(formatMoney(competenzaTotaleAnno1) || '0,00');
         $('#competenzaVariazioneAnno2' + innerSuffix).val(formatMoney(competenzaTotaleAnno2) || '0,00');
         
     }
     
     function impostaTabellaComponentiInVariazione(lista, suffix, nuovoDettaglio){
     	var innerSuffix = suffix || '';
     	var listaComponenti = lista || [];
     	var tabella = $('#tabellaStanziamentiTotaliPerComponente' + innerSuffix);
         var opzioniNuove = {
         		aaData: listaComponenti,
         		//sDom: "t<'row-fluid'<'span4'i><'span8'p>>",
                 bPaginate: false,
                 bLengthChange: false,
                 iDisplayLength: 10,
                 bSort: false,
                 bInfo: true,
                 bAutoWidth: true,
                 bFilter: false,
                 bProcessing: true,
                 bDestroy : true,
                 oLanguage : {
                     sZeroRecords : "Non sono presenti componenti associate alla variazione."
                 },
                 // Definizione delle colonne
                 aoColumnDefs : [
                    {aTargets : [ 0 ], mData : defaultPerDataTable('descrizioneDettaglioInVariazione')},
                    {aTargets : [ 1 ], mData : defaultPerDataTable('descrizioneTipoDettaglioComponenteCapitolo')},
                    {aTargets : [ 2 ], mData : function(source) {
                        		return '<input type="checkbox" disabled ' + (source.eliminaSuTuttiGliAnni? 'checked' : "" ) + '/>' ;
                    		}, fnCreatedCell: function(nTd, sData, oData, iRow, iCol){
                    			$(nTd).addClass("text-center").find('input').substituteHandler('change', gestisciChangeEliminaComponente.bind(undefined, nTd, oData));
                    		}
                    },
                    {aTargets : [ 3 ], mData : defaultPerDataTable('htmlImportoAnno0'), fnCreatedCell: doAddClassToElement('text-right')},
                    {aTargets : [ 4 ], mData : defaultPerDataTable('htmlImportoAnno1'), fnCreatedCell: doAddClassToElement('text-right')},
                    {aTargets : [ 5 ], mData : defaultPerDataTable('htmlImportoAnno2'), fnCreatedCell: doAddClassToElement('text-right')},
                    { aTargets : [ 6 ],
                         mData : function() {
                             return "<a href='#' title='rendi modificabile l'importo' role='button'>" +
                                        "<i class='icon-lock icon-2x'><span class='nascosto'>modifica</span></i>" +
                                    "</a>";
                         },
                         fnCreatedCell : function (nTd, sData, oData, iRow, iCol) {
                             // Imposta l'evento di onClick
                              $(nTd).addClass("text-center")
                             	.find("a")
                             	.eventPreventDefault("click", gestisciEditabilitaInputImportoComponenteInTabella.bind(undefined, nTd, oData, innerSuffix, nuovoDettaglio));
                         }
                    },
                    {aTargets : [ 7 ],
                         mData : function() {
                             return "<a href='#' title='elimina la componente' role='button' data-toggle='modal'>" +
                                        "<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
                                    "</a>";
                         },
                         fnCreatedCell : function (nTd, sData, oData, iRow, iCol) {
                             // Imposta l'evento di onClick
                              $(nTd).addClass("text-center")
                             	.find("a")
                             		.eventPreventDefault("click", eliminaComponente.bind(undefined, nTd, oData,nuovoDettaglio));
                         }
                     } 
                 ],
                 fnCreatedRow: function(nRow){
                	 $(nRow)
                	  .find('.decimale')
                        .gestioneDeiDecimali();
                 }
             };

         	var opzioniClone = $.extend(true, {}, capVarImp.opzioniBaseDataTable);
         
         	var opzioni = $.extend(true, opzioniClone, opzioniNuove);

             
             tabella.dataTable(opzioni);
     }
    

    /* ******************************************************** ********************************** ***/
    /* ****************  MODIFICA DA TABELLA CAPITOLI ASSOCIATI  ******************************** ***/
    /* ****************************************************************************************** ***/
    
    /**
     * Imposta l'uid del capitolo da eliminare dalla variazione.
     *
     * @param riga      la riga considerata
     */
    function apriModaleEliminazioneCapitoloNellaVariazione(riga) {

        var tabellaSorgente = $("#tabellaGestioneVariazioni");
        var tabellaDestinazione = $("#tabellaGestioneVariazioni");
        var pulsanteEliminazione = $("#EDIT_elimina");

        // Eliminazione
        pulsanteEliminazione.off("click").substituteHandler("click", function() {
        	$('msgElimina').modal('hide');
            eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga);
        });
    }
    
    /**
     * Imposta il totale degli importi della variazione.
     */
    function apriEPopolaModaleModificaImporti(oggettoOriginale){
    	
        // Dati per l'editabilità dei campi
        // Pulsante per il salvataggio
        var pulsanteSalvataggio = $("#button_registraVariazioneModale");
        var $modale=$("#editStanziamenti");
        //SIAC-6881
        var abilitaGestioneComponenti = oggettoOriginale && oggettoOriginale.tipoCapitolo && (oggettoOriginale.tipoCapitolo._name === 'CAPITOLO_USCITA_PREVISIONE' || oggettoOriginale.tipoCapitolo._name==='CAPITOLO_USCITA_GESTIONE');
        
        // Pulisco il form
        $("#editStanziamenti fieldset :input").not("[data-maintain]").val("");
        // Nascondo l'eventuale alert di errore già presente
        $("#ERRORI_modaleEditStanziamenti").slideUp();

        $("#competenzaVariazioneAnno0Modale").val(oggettoOriginale.competenza.formatMoney());
        $("#residuoVariazioneModale").val(oggettoOriginale.residuo.formatMoney());
        $("#cassaVariazioneModale").val(oggettoOriginale.cassa.formatMoney());
        $("#competenzaVariazioneAnno1Modale").val(getStanziamentoNotUndefined(oggettoOriginale.competenza1).formatMoney());
        $("#competenzaVariazioneAnno2Modale").val(getStanziamentoNotUndefined(oggettoOriginale.competenza2).formatMoney());
        
        $("#titoloModaleVariazioneStanziamenti").html("Modifica Stanziamenti Capitolo " + oggettoOriginale.numeroCapitolo + " / " + oggettoOriginale.numeroArticolo);

        pulsanteSalvataggio.substituteHandler("click", aggiornaCapitoliNellaVariazione.bind(undefined, oggettoOriginale, abilitaGestioneComponenti));
        
        if(!abilitaGestioneComponenti){
        	pulisciCampiPerComponenti('Modale');
        	$modale.modal("show");
        	return;
        }
        
        pulsanteSalvataggio.attr("disabled", true);
        $('#linkCollapseComponentiModale').attr("disabled", true);
        
    	$('#divComponentiInVariazioneModale').slideDown().promise().then(function() {
    		$('#divComponentiInVariazioneModale').overlay('show');
    	});
    	//chiudo tutti i collapse
    	$modale.modal('show')
    		.find('.gestisci-collapse')
    			.each(function(){
    				var selettoreCollapse= $(this).data('selettore-collapse-interno');
    				chiudiCollapse($(selettoreCollapse));
    				});
    	caricaEPopolaTabellaComponentiCapitoloInVariazione(oggettoOriginale);
    }
    
    /**
     * Imposta il form di aggiornamento della variazione.
     *
     * @param riga la riga considerata
     */
    function apriEPopolaModaleModificaImportiDaTabella(riga) {
    	var tabellaSorgente = $("#tabellaGestioneVariazioni");
    	var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);

    	apriEPopolaModaleModificaImporti(datiNellaRiga);
        // Apro il modale per la modifica degli importi
    }

	/* ****************************************************************************** ***/
    /* ****************  TABELLA CAPITOLI ASSOCIATI  ******************************** ***/
    /* ****************************************************************************** ***/
    
    
    //CR-3403
    function impostaTotaliInDataTable(totaleStanziamentiEntrata, totaleStanziamentiCassaEntrata, totaleStanziamentiResiduiEntrata, totaleStanziamentiEntrata1, totaleStanziamentiEntrata2,
    		totaleStanziamentiSpesa, totaleStanziamentiCassaSpesa, totaleStanziamentiResiduiSpesa, totaleStanziamentiSpesa1, totaleStanziamentiSpesa2){

    	var totaleStanziamentiEntrataNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiEntrata);
        var totaleStanziamentiCassaEntrataNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiCassaEntrata);
        var totaleStanziamentiResiduiEntrataNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiResiduiEntrata);
        //anno = anno bilancio +1
        var totaleStanziamentiEntrata1NotUndefined = getStanziamentoNotUndefined(totaleStanziamentiEntrata1);
        //anno = anno bilancio +2
        var totaleStanziamentiEntrata2NotUndefined = getStanziamentoNotUndefined(totaleStanziamentiEntrata2);
        
        
        var totaleStanziamentiSpesaNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiSpesa);
        var totaleStanziamentiCassaSpesaNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiCassaSpesa);
        var totaleStanziamentiResiduiSpesaNotUndefined = getStanziamentoNotUndefined(totaleStanziamentiResiduiSpesa);
        //anno = anno bilancio +1
        var totaleStanziamentiSpesa1NotUndefined = getStanziamentoNotUndefined(totaleStanziamentiSpesa1);
        //anno = anno bilancio +2
        var totaleStanziamentiSpesa2NotUndefined = getStanziamentoNotUndefined(totaleStanziamentiSpesa2);

        capVarImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totaleStanziamentiEntrataNotUndefined);
        capVarImp.impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totaleStanziamentiResiduiEntrataNotUndefined);
        capVarImp.impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totaleStanziamentiCassaEntrataNotUndefined);
        //anno = anno bilancio +1
        capVarImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazioneAnnoPiuUno", totaleStanziamentiEntrata1NotUndefined);
        //anno = anno bilancio +2
        capVarImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazioneAnnoPiuDue", totaleStanziamentiEntrata2NotUndefined);
        

        capVarImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totaleStanziamentiSpesaNotUndefined);
        capVarImp.impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totaleStanziamentiCassaSpesaNotUndefined);
        capVarImp.impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totaleStanziamentiResiduiSpesaNotUndefined);
        //anno = anno bilancio +1
        capVarImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazioneAnnoPiuUno", totaleStanziamentiSpesa1NotUndefined);
        //anno = anno bilancio +2
        capVarImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazioneAnnoPiuDue", totaleStanziamentiSpesa2NotUndefined);
        

        capVarImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", (totaleStanziamentiEntrataNotUndefined - totaleStanziamentiSpesaNotUndefined));
        capVarImp.impostaValutaEAllineaADestra("#differenzaResiduoVariazione", (totaleStanziamentiResiduiEntrataNotUndefined - totaleStanziamentiResiduiSpesaNotUndefined));
        capVarImp.impostaValutaEAllineaADestra("#differenzaCassaVariazione", (totaleStanziamentiCassaEntrataNotUndefined - totaleStanziamentiCassaSpesaNotUndefined));
        
        //anno = anno bilancio +1
        capVarImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazioneAnnoPiuUno", (totaleStanziamentiEntrata1NotUndefined - totaleStanziamentiSpesa1NotUndefined));
        //anno = anno bilancio +2
        capVarImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazioneAnnoPiuDue", (totaleStanziamentiEntrata2NotUndefined - totaleStanziamentiSpesa2NotUndefined));
    }

    
    /**
     * Imposta il dataTable per l'elenco deli capitoli nella variazione.
     *
     * @param tabella       la tabella da paginare
     * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
     */
    function impostaDataTableCapitoliNellaVariazione(tabella) {
    	
    	var isEditable = $("input[name='specificaImporti.tipoCapitolo']").length !== 0; //  || $('#chiudiPropostaButton').length > 0;
//    	//SIAC 6884
//    	var isDecentratoEditabke =  > 0;//$("input[name='decentratoDisabled']").val();
//    	if(isDecentratoEditabke == "DISABILITA_DA_DECENTRATO"){
//        	isEditable = false;
//        }
        

        var opzioniNuove = {
    		bServerSide:true,
    		sServerMethod: "POST",
            sAjaxSource : "leggiCapitoliNellaVariazioneImportiAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy : true,
            oLanguage : {
                sZeroRecords : "Non sono presenti capitoli associati alla variazione."
            },
            // Definizione delle colonne
            aoColumnDefs : [{aTargets : [ 0 ], mData : function (source) {
                    var result = source.descrizione ? "<a rel='popover' href='#'>" + source.denominazione + "</a>" : source.denominazione;
                    if(source.datiAccessorii) {
                        result += " <em>(" + source.datiAccessorii + ")</em>";
                    }
                    return result;
                 },fnCreatedCell : function (nTd, sData, oData) {
                        if(oData.descrizione !== null && oData.descrizione !== undefined) {
                            // Settings del popover
                            var settings = {
                                "content" : oData.descrizione,
                                "title" : "Descrizione",
                                "trigger" : "hover"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).substituteHandler("click", function(event) {
                        		event.preventDefault();
                        	}).popover(settings);
                        }
                    }
                },
                {aTargets : [ 1 ], mData : function(source) {
                        return source.competenza.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {aTargets : [ 2 ], mData : function(source) {
                        return source.residuo.formatMoney();
                    },fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {aTargets : [ 3 ],mData : function(source) {
                        return source.cassa.formatMoney();
                	},fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                   }
                },
                {aTargets : [ 4 ], mData : function(source) {
                        return source.competenza1.formatMoney();
                    },fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {aTargets : [ 5 ],mData : function(source) {
                        return source.competenza2.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {bVisible: isEditable,
                 aTargets : [ 6 ], mData : function() {
                        return "<a href='#' title='modifica gli importi' role='button' data-toggle='modal'>" +
                                   "<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
                               "</a>";
                    },fnCreatedCell : function (nTd, sData, oData, iRow, iCol) {
                        // Imposta l'evento di onClick
                        if(isEditable){
                         $(nTd).addClass("text-center")
                        	.find("a")
                        	.off("click")
                        	.eventPreventDefault("click", function() {
                            		apriEPopolaModaleModificaImportiDaTabella(iRow);
                            	});
                        }
                    }
                },
                {bVisible: isEditable,
                    aTargets : [ 7 ], mData : function() {
                        return "<a href='#msgElimina' title='elimina' role='button' data-toggle='modal'>" +
                                   "<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function(nTd, sData, oData, iRow) {
                        if(isEditable){
                    	$(nTd).addClass("text-center")
                            .find("a")
                                .substituteHandler("click", function() {
                                    apriModaleEliminazioneCapitoloNellaVariazione(iRow);
                                });

                        }
                    }
                }
            ],
            fnPreDrawCallback: function () {
                // Mostro il div del processing
            	tabella.parent().find('div.dataTables_processing').parent("div").show();
            	$('#SPINNER_leggiCapitoli').removeClass("activated");
            	$('#tabellaGestioneVariazioni').overlay('hide');
            	$('#tabellaGestioneVariazioni_wrapper').overlay('show');
            },
                // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
            	tabella.parent().find('div.dataTables_processing').parent("div").hide();
            	$('#tabellaGestioneVariazioni_wrapper').overlay('hide');
            }
        };

        var opzioniClone = $.extend(true, {}, capVarImp.opzioniBaseDataTable);
        var opzioni = $.extend(true, opzioniClone, opzioniNuove);

        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable(false);
            tabella.dataTable().fnDestroy();
        }

        tabella.dataTable(opzioni);
    }
    
    
    /**
     * Chiamata Ajax per ottenere la lista dei Capitoli associati alla variazione
     * e inizializzare dataTable
     * */
    //CR-3403
    function leggiCapitoliNellaVariazione(){
    	$('#tabellaGestioneVariazioni').overlay('show');

    	return $.postJSON("leggiCapitoliNellaVariazione_aggiornamento.do", {})
    	.then(function(data) {
			var errori = data.errori;
			var messaggi = data.messaggi;
			var informazioni = data.informazioni;
			var alertErrori = alertErrori;
			var alertMessaggi = $("#MESSAGGI");
			var alertInformazioni = $("#INFORMAZIONI");

				// Controllo gli eventuali errori, messaggi, informazioni
			if(impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
			if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {
				return;
			}
			if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {
				return;
			}
			//chiamo dataTable SI
			impostaDataTableCapitoliNellaVariazione($("#tabellaGestioneVariazioni"));

			impostaTotaliInDataTable(data.totaleStanziamentiEntrata, data.totaleStanziamentiCassaEntrata, data.totaleStanziamentiResiduiEntrata, data.totaleStanziamentiEntrata1, data.totaleStanziamentiEntrata2,
					data.totaleStanziamentiSpesa, data.totaleStanziamentiCassaSpesa, data.totaleStanziamentiResiduiSpesa, data.totaleStanziamentiSpesa1, data.totaleStanziamentiSpesa2);
		}).always(rimuoviOverlay.bind(this));
    }


    
    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     */
    function cercaCapitoloNellaVariazione(e){
    	var prefissoOggetto = 'capitolo';
    	var oggettoPerChiamataAjax = {};
    	//Campi hidden da leggere
        var  tipoApplicazione   = $("#HIDDEN_tipoApplicazione").val();
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        // Spinner
        var spinner = $("#SPINNER_CapitoloNellaVariazione");

        //input selezionati dall'utente
        var tipoCapitolo = $('input[name="specificaImporti.tipoCapitoloNellaVariazione"]:checked').val();
        var annoCapitolo = $('#annoCapitoloNellaVariazione').val();
        var numeroCapitolo = $('#numeroCapitoloNellaVariazione').val();
        var numeroArticolo = $('#numeroArticoloNellaVariazione').val();
        
        //Wrapper per il tipo capitolo e tipo applicazione: e.g. SpesaGestione, reduced:UG
        var wrapTipoCapitoloApplicazione = tipoCapitolo + ottieniTipoApplicazioneCapitolo(tipoApplicazione);
        // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = prefissoOggetto + wrapTipoCapitoloApplicazione + 'NellaVariazione';
        
        //validazione dei campi
        var erroriArray = controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo);
        var url = 'effettuaRicercaNellaVariazioneCap' + wrapTipoCapitoloApplicazione + '_aggiornamento.do';
        
        e.preventDefault();
        
        if(impostaDatiNegliAlert(erroriArray, alertErrori)){
        	return;
        }
        //i campi sono tutti stati compilati correttamente
        alertErrori.slideUp();
        spinner.addClass("activated");
        
      //creo oggetto da passare al server e.g. capitoloDaRichiamare = "capitoloEntrataGestione"
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroUEB"] = 1;
        oggettoPerChiamataAjax.annoImporti = annoVariazione;
        
        return $.postJSON(url, oggettoPerChiamataAjax).then(function(data){
        	var errori = data.errori;
            var elementoCapitolo = data.elementoCapitoloVariazioneTrovatoNellaVariazione;
            if(impostaDatiNegliAlert(errori,alertErrori)){
            	return;
            }
            apriEPopolaModaleModificaImporti(elementoCapitolo);
            $('#editStanziamenti').modal('show');
        }).always(spinner.removeClass.bind(spinner, "activated"));
        
    }
    

    /* ******************************************************** ********************************** ***/
    /* ****************  LETTURA DATI CAPITOLO DA ASSOCIARE  ************************************* ***/
    /* ****************************************************************************************** ***/
    function impostaImportiCapitolo(abilitaGestioneComponenti, wrapTipoCapitoloApplicazione, capitolo, listaImporti, tabellaStanziamenti){
    	var containerStanziamentiDefault = $('#containerTabellaStanziamentiTotali').hide();
    	var containerStanziamentiPerComponenti = $('#containerTabellaStanziamentiTotaliPerComponetnti').hide();
    	
        if(!abilitaGestioneComponenti){
	        $.each(listaImporti, function() {
	                 impostaStanziamenti(this, tabellaStanziamenti);
	           });
        	containerStanziamentiDefault.slideDown();
        	$('#fieldset_inserimentoVariazioneImporti').overlay('hide');
        	return pulisciCampiPerComponenti("NuovoDettaglio");
        }
        
        caricaComponentiNuovoDettaglio(capitolo, wrapTipoCapitoloApplicazione);
        
    }

    //SIAC-6884
    function disabledNuovoEliminaCapitoloPerDecentrato(){
        //var  tipoApplicazione   = $("#HIDDEN_tipoApplicazione").val();
        //Un utente decentrato non ha permessi di inserire ne capitoli di Uscita, ne di entrata, quindi di default viene settato il tipo capitolo
        //per effettuare la chiamata delle azioni consentite
        //var tipoCapitolo = "Uscita"

        //var tipoArticoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
        // return $.when(
        //     $.postJSON("controllaAzioniConsentiteAllUtente_aggiornamento.do", {"specificaImporti.elementoCapitoloVariazione.tipoCapitolo" : tipoArticoloEnum})
        // ).then(function(response) {
        //     var data1 = response;
        //     var inserisci = data1.specificaImporti.utenteAbilitatoAdInserimento;
        //     var annulla = data1.specificaImporti.utenteAbilitatoAdAnnullamento;

        //     var buttonNuovoCapitolo = $("#redirezioneNuovoCapitoloButton");
        //     var buttonAnnullaCapitolo = $("#annullaCapitoloButton");

        //     if(!inserisci) buttonNuovoCapitolo.attr("disabled", true);
        //     if(!annulla) buttonAnnullaCapitolo.attr("disabled", true);

            
        // })
        var isDecentrato = $("#HIDDEN_decentrato").val() === 'true' ? true : false;
        var buttonNuovoCapitolo = $("#redirezioneNuovoCapitoloButton");
        var buttonAnnullaCapitolo = $("#annullaCapitoloButton");
        if(isDecentrato) {
            buttonNuovoCapitolo.attr("disabled", true);
            buttonAnnullaCapitolo.attr("disabled", true);
        }
        

    }

    function disabledRadioButtonSpesa() {
        var isDecentrato = $("#HIDDEN_decentrato").val() === 'true' ? true : false;
        var isRegionePiemonte= $("#HIDDEN_regionePiemonte").val() === 'true' ? true : false;
        var  tipoApplicazioneVal   = $("#HIDDEN_tipoApplicazione").val();

        if(isDecentrato && isRegionePiemonte &&(tipoApplicazioneVal==='PREVISIONE' || tipoApplicazioneVal==='GESTIONE')){
            $('#tipoCapitoloRadio1').attr("disabled", true);
        }else if(isDecentrato && !isRegionePiemonte &&(tipoApplicazioneVal==='PREVISIONE' || tipoApplicazioneVal==='GESTIONE')){
            if($('#tipoCapitoloRadio1').prop("disabled")){
                $('#tipoCapitoloRadio1').removeAttr('disabled')
            }
        }

    }
    


    /**
     * Redirige verso un nuovo capitolo se e solo se il tipo di capitolo &eacute; stato valorizzato.
     */
    function redirezioneNuovoCapitolo(){

        var form = $("form");
        var alertErrori = $("#ERRORI");
        var tipoCapitoloSelezionato = $("input[name='specificaImporti.tipoCapitolo']:checked").val();
        var applicazione = ottieniTipoApplicazioneCapitolo($("#HIDDEN_tipoApplicazione").val()) ;
        var tipoCapitoloEnum; 
        // Se non è stato selezionato un tipo di capitolo, annullare l'azione
        if(!tipoCapitoloSelezionato) {
            impostaDatiNegliAlert(["Il tipo di capitolo deve essere selezionato"], alertErrori);
            return;
        }
        tipoCapitoloEnum = "CAPITOLO_" + (tipoCapitoloSelezionato === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (applicazione === "Previsione" ? "PREVISIONE" : "GESTIONE");
        $('#redirezioneNuovoCapitoloButton').overlay('show');
        $.postJSON("controllaAzioniConsentiteAllUtente_aggiornamento.do", {"specificaImporti.elementoCapitoloVariazione.tipoCapitolo" : tipoCapitoloEnum})
        .then(function(data){
        	 var inserisci = data.specificaImporti.utenteAbilitatoAdInserimento;
        	 if(!inserisci && impostaDatiNegliAlert(['COR_ERR_0044 : Operazione non consentita: inserimento capitolo ' + tipoCapitoloSelezionato + ' ' + applicazione], alertErrori)){
        		 return;
        	 }
        	 form.attr("action", "redirezioneVersoNuovoCapitolo_aggiornamento.do").submit();

        }).always($('#redirezioneNuovoCapitoloButton').overlay.bind($('#redirezioneNuovoCapitoloButton'), 'hide'));
    }

    /**
     * Annulla il capitolo e tutte le ueb corrispondenti
     */
    function annullaCapitolo() {

        // Oggetto da injettare nella request
        var oggettoDaInserire = {};
        // Dati dell'oggetto
        var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
        var tipoCapitolo = $("input[name='specificaImporti.tipoCapitolo']:checked").val();
        var tipoCapitoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + ( ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
        var form = $('form').overlay('show');

        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.uid"] = $("#HIDDEN_uidCapitolo").val();
        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.tipoCapitolo"] = tipoCapitoloEnum;
        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.annoCapitolo"] = $("#annoCapitolo").val();
        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.numeroCapitolo"] = $("#numeroCapitolo").val();
        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.numeroArticolo"] = $("#numeroArticolo").val();
        oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.statoOperativoElementoDiBilancio"] = $("#HIDDEN_statoOperativoElementoDiBilancio").val();

        return $.postJSON("annullaCapitoli_aggiornamento.do", oggettoDaInserire)
        .then(function(data) {
            var errori = data.errori;
            var messaggi = data.messaggi;
            var informazioni = data.informazioni;
            // Alerts
            var alertErrori = $("#ERRORI");
            var alertMessaggi = $("#MESSAGGI");
            var alertInformazioni = $("#INFORMAZIONI");

            // Controllo che non vi siano errori
            if(impostaDatiNegliAlert(errori, alertErrori)) {
                return;
            }
            if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {
                return;
            }
            if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {
                return;
            }
            $("#divRicercaCapitolo").slideUp();
            capVarImp.leggiCapitoliNellaVariazione();
        }).always(form.overlay.bind(form, 'hide'));
    }
    
    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     */
    function cercaCapitolo(){

        var  tipoApplicazione   = $("#HIDDEN_tipoApplicazione").val();


        //SIAC-6884-filtraggio capitolo associabili
        var isDecentrato = $("#HIDDEN_decentrato").val() === 'true' ? true : false;
        var isRegionePiemonte= $("#HIDDEN_regionePiemonte").val() === 'true' ? true : false;
        var direzioneProponente= $("#HIDDEN_direzioneProponente").val();
        var uidVariazione = $("#HIDDEN_uidVariazione").val();

        //input selezionati dall'utente
        var tipoCapitolo = $("input[name='specificaImporti.tipoCapitolo']:checked").val();
        var annoCapitolo = $("#annoCapitolo").val();
        var numeroCapitolo = $("#numeroCapitolo").val();
        var numeroArticolo = $("#numeroArticolo").val();
        //SIAC-6705
        var statoCapitolo = $('#statoCapitolo').val();

        // gestione degli errori
        var erroriArray = controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo);
        //SIAC-6705
        if(!statoCapitolo || statoCapitolo === "") {
            erroriArray.push("Stato capitolo non selezionato");
        }
        
        //SIAC-6883
        $('#fieldset_inserimentoVariazioneImporti').overlay('show');
        var $divRisultatiRicerca = $("#divRicercaCapitolo").slideUp();
        
        $('#divRicercaCapitolo').find('.gestisci-collapse')
			.each(function(){
				var selettoreCollapse= $(this).data('selettore-collapse-interno');
				chiudiCollapse($(selettoreCollapse));
				});
        
        //Wrapper per il tipo capitolo e tipo applicazione: e.g. SpesaGestione, reduced:UG
        var wrapTipoCapitoloApplicazione = tipoCapitolo + ottieniTipoApplicazioneCapitolo(tipoApplicazione);
        var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "P" : "G");

        // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
        var oggettoPerChiamataAjax = {};
        
        var ajaxSource = "effettuaRicercaDettaglioCap" + wrapTipoCapitoloApplicazione + ".do";
        // Spinner
        var spinner = $("#SPINNER_CapitoloSorgente");

        // Per la ricerca delle autorizzazioni dell'utente
        var tipoArticoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
        //prenderlo da quello visualizzato?
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        //var annoEsercizio = $("#HIDDEN_annoEsercizio").val();
        //SIAC-6881
        var abilitaGestioneComponenti = tipoCapitolo && tipoCapitolo !== 'Entrata';
        
        // Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
        if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
            return;
        }
        // La validazione JavaScript è andata a buon fine. Proseguire
        alertErrori.slideUp();

        //creo oggetto da passare al server e.g. capitoloDaRichiamare = "capitoloEntrataGestione"
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo.trim();
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroUEB"] = 1;
        //SIAC-6705
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = statoCapitolo;
        oggettoPerChiamataAjax.annoImporti = annoVariazione;
        
        //SIAC-6884
        if(isDecentrato){
            ajaxSource = "effettuaRicercaDettaglioCapDaVariazione" + wrapTipoCapitoloApplicazione + ".do";
            oggettoPerChiamataAjax.regionePiemonte = isRegionePiemonte;
            oggettoPerChiamataAjax.decentrato = isDecentrato;
            oggettoPerChiamataAjax.uidVariazione = uidVariazione;
            oggettoPerChiamataAjax.direzioneProponente = direzioneProponente;
        }
        //END SIAC-6884

        spinner.addClass("activated");

        // Controllo i pulsanti per inserimento ed annullamento
        return $.when(
            $.postJSON("controllaAzioniConsentiteAllUtente_aggiornamento.do", {"specificaImporti.elementoCapitoloVariazione.tipoCapitolo" : tipoArticoloEnum}),
            $.postJSON(ajaxSource, oggettoPerChiamataAjax)
        ).then(function(jq1, jq2) {
            var data1 = jq1[0];
            var data2 = jq2[0];

            var inserisci = data1.specificaImporti.utenteAbilitatoAdInserimento;
            var annulla = data1.specificaImporti.utenteAbilitatoAdAnnullamento;

            var buttonNuovoCapitolo = $("#redirezioneNuovoCapitoloButton");
            var buttonAnnullaCapitolo = $("#annullaCapitoloButton");

            var errori = data2.errori;
            var messaggi = data2.messaggi;
            var informazioni = data2.informazioni;
            var capitolo = data2[capitoloDaRichiamare];
            var listaImporti = capitolo["listaImportiCapitolo" + wrapTipoCapitoloApplicazioneReduced];
                // Tabella degli stanziamenti
            var tabellaStanziamenti = $("#tabellaStanziamentiTotali tbody tr");
                // Alerts
            var alertMessaggi = $("#MESSAGGI");
            var alertInformazioni = $("#INFORMAZIONI");
            var categoriaCapitoloString;
            var urlRegistraImporti;

            //inserisci ? buttonNuovoCapitolo.show() : buttonNuovoCapitolo.hide();
            //annulla ? buttonAnnullaCapitolo.show() : buttonAnnullaCapitolo.hide();

            // Controllo gli eventuali errori, messaggi, informazioni
            if(impostaDatiNegliAlert(errori, alertErrori)) {
                return;
            }
            if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {
                return;
            }
            if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {
                return;
            }
            // Nascondo gli alert
            alertErrori.slideUp();
            alertMessaggi.slideUp();
            alertInformazioni.slideUp();

            categoriaCapitoloString = capitolo.categoriaCapitolo ? (capitolo.categoriaCapitolo.codice + "-" + capitolo.categoriaCapitolo.descrizione) : "";
            
            svuotaRisultatiRicercaCapitolo();
            
            // Non vi sono errori, né messaggi, né informazioni.
            // Costruzione dei testi da injettare
            $("#annoCapitoloTrovato").html(capitolo.annoCapitolo);
            $("#numeroCapitoloArticoloTrovato").html(capitolo.numeroCapitolo + " / " + capitolo.numeroArticolo);
            $("#categoriaCapitoloTrovato").html(categoriaCapitoloString);
            $("#descrizioneCapitoloTrovato").html(capitolo.descrizione);
            $("#HIDDEN_uidCapitolo").val(capitolo.uid);
            $("#HIDDEN_statoOperativoElementoDiBilancio").val(capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "");

            impostaImportiCapitolo(abilitaGestioneComponenti, wrapTipoCapitoloApplicazione, capitolo, listaImporti, tabellaStanziamenti);
            
            urlRegistraImporti = abilitaGestioneComponenti? 'aggiungiCapitoliConComponentiNellaVariazione_aggiornamento.do' : 'aggiungiCapitoliNellaVariazione_aggiornamento.do';
            
            // Pulisco i campi degli importi
             $pulsanteRegistraImportiNellaVariazione.off().substituteHandler(
                "click",
                function(e) {
                	e.preventDefault();
                	if($pulsanteRegistraImportiNellaVariazione.attr('disabled') === 'disabled'){
                		return;
                	}
                    associaCapitoloAllaVariazione(capitolo, urlRegistraImporti);
                }
            );

             $divRisultatiRicerca.slideDown();
        }).always(spinner.removeClass.bind(spinner, "activated"));
    }

//    /**
//     * Effettua una nuova ricerca.
//     */
//    function effettuaNuovaRicerca(){
//
//        var divRicerca = $("#divRicercaCapitolo");
//        var numeroCapitolo = $("#numeroCapitolo");
//        var numeroArticolo = $("#numeroArticolo");
//        var tipoCapitolo = $("input[name='specificaImporti.tipoCapitolo']");
//
//        // Nascondo il div
//        divRicerca.slideUp();
//
//        // Svuoto i campi per la ricerca
//        numeroCapitolo.val("");
//        numeroArticolo.val("");
//        tipoCapitolo.removeProp("checked");
//
//        // Imposto il focus sul tipoCapitolo
//        tipoCapitolo.focus();
//    }

    
    
    
    /* ******************************************************** ********************************** ***/
    /* ****************  ASSOCIA NUOVO CAPITOLO  ************************************* ***/
    /* ****************************************************************************************** ***/
    
    
    function effettuaChiamataAssociaCapitoloAVariazione(url, ajaxParam, $pulsanteRegistrazione){
    	$("#tabellaGestioneVariazioni").overlay('show');
    	$pulsanteRegistraImportiNellaVariazione.attr('disabled','disabled');
    	return $.postJSON(url, ajaxParam)
			.then(function(data) {
				var errori = data.errori;
				var messaggi = data.messaggi;
				
				if(impostaDatiNegliAlert(errori, alertErrori)) {
					$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
					return;
				}
				
				if(messaggi && messaggi.length >0) {
					gestioneRichiestaProsecuzione(messaggi, url, ajaxParam,data.specificaImporti && data.specificaImporti.cassaIncongruente, data.specificaImporti && data.specificaImporti.cassaIncongruenteDopoDefinizione);	
        			return;
				}  
				$("#divRicercaCapitolo").slideUp();
				alertErrori.slideUp();
				$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
				leggiCapitoliNellaVariazione();
				$("#associaCapitoliAllaVariazione").scrollToSelf();
		}).always($("#tabellaGestioneVariazioni").overlay.bind($("#tabellaGestioneVariazioni"),'hide'));
    }
    
    
    /* ***************************************************************** ***/
    /* ****************  GESTIONE DEI CAPITOLI NELLA VARIAZIONE  ******* ***/
    /* ***************************************************************** ***/
    
    /**
     * Scollega il capitolo dalla variazione eliminando gli importi
     */
    function eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga) {

    	var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
        // Clone dell'oggetto originale, per effettuare la chiamata
        var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);
        //var form = $('form').addClass('form-submitted');
        var $overlayModale = $('#msgElimina').find('.overlay-modale').overlay('show');
        oggettoDaEliminare.uid = oggettoOriginale.uid;

        // Qualifico correttamente l'oggetto da inserire
        oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaImporti.elementoCapitoloVariazione");

        $.postJSON("eliminaCapitoliNellaVariazione_aggiornamento.do", oggettoDaEliminare)
        .then(function(data) {
        	impostaDatiNegliAlert(["COR_INF_0006 - Operazione effettuata correttamente"],$('#INFORMAZIONI'));

        	leggiCapitoliNellaVariazione();
        	$('#msgElimina').modal('hide');
        }).always($overlayModale.overlay.bind($overlayModale, 'hide'));
    }

    /**
     * Registra nella variazione quanto presente nella tabella.
     *
     * @param capitolo       il Capitolo da registrare
     * @param annoVariazione l'anno della variazione
     */
    function associaCapitoloAllaVariazione(capitolo, url, $pulsante) {
        var oggettoDaInviare = {};

        var urlNotUndefined = url || "aggiungiCapitoliNellaVariazione_aggiornamento.do";
        var ajaxParam;
		var listaImporti = capitolo.listaImportiCapitolo;
		var annoBilancio = $('#HIDDEN_annoEsercizio').val();
		//questa cosa sono sicura si possa far meglio, ma sono di fretta. Spero che qualcuno la metta a posto...
		var importiCapitolo = listaImporti.find(function(imp){
			return annoBilancio && imp.annoCompetenza == annoBilancio;		
		});
		var importi = importiCapitolo || {};
		
        oggettoDaInviare.uid = capitolo.uid;
        oggettoDaInviare.statoOperativoElementoDiBilancio = capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "";
        oggettoDaInviare.competenza = $("#competenzaVariazioneAnno0NuovoDettaglio").val() || 0;
        oggettoDaInviare.competenza1 = $("#competenzaVariazioneAnno1NuovoDettaglio").val() || 0;
        oggettoDaInviare.competenza2 = $("#competenzaVariazioneAnno2NuovoDettaglio").val() || 0;
        oggettoDaInviare.residuo = $("#residuoVariazioneNuovoDettaglio").val() || 0;
        oggettoDaInviare.cassa = $("#cassaVariazioneNuovoDettaglio").val() || 0;
        //la competenza originale non mi serve se non per 
        oggettoDaInviare.competenzaOriginale = importi.stanziamento || 0;
        
		oggettoDaInviare.residuoOriginale = importi.stanziamentoResiduo || 0;
		oggettoDaInviare.cassaOriginale = importi.stanziamentoCassa || 0;
        
        oggettoDaInviare = qualify(oggettoDaInviare, "specificaImporti.elementoCapitoloVariazione");

        ajaxParam = $.extend(true, {}, oggettoDaInviare/*, oggettoOld*/);

        return effettuaChiamataAssociaCapitoloAVariazione(urlNotUndefined, ajaxParam);
    }
    
    
  function aggiornaCapitoliNellaVariazione(oggettoOriginale, abilitaGestioneComponenti){
   	 var ajaxParam;
   	 var url = "aggiornaCapitoli" + (abilitaGestioneComponenti? 'ConComponenti' : '') + "NellaVariazione_aggiornamento.do";
        // Clone dell'oggetto originale, per effettuare la chiamata
        var oggettoDaAggiornare = {};
        var competenza = $("#competenzaVariazioneAnno0Modale").val();
        var competenza1 = $("#competenzaVariazioneAnno1Modale").val();
        var competenza2 = $("#competenzaVariazioneAnno2Modale").val();
        var residuo = $("#residuoVariazioneModale").val();
        var cassa = $("#cassaVariazioneModale").val();
        
        var $overlayModale = $('#editStanziamenti').find('.overlay-modale').overlay('show');

        oggettoDaAggiornare.uid = oggettoOriginale.uid;
        oggettoDaAggiornare.annoImporti = oggettoOriginale.annoImporti;
        oggettoDaAggiornare.competenza = competenza;
        oggettoDaAggiornare.competenza1 = competenza1;
        oggettoDaAggiornare.competenza2 = competenza2;
        oggettoDaAggiornare.residuo = residuo;
        oggettoDaAggiornare.cassa = cassa;
        oggettoDaAggiornare.competenzaOriginale = oggettoOriginale.competenzaOriginale || 0;
        oggettoDaAggiornare.competenzaOriginale1 = oggettoOriginale.competenzaOriginale1 || 0;
        oggettoDaAggiornare.competenzaOriginale2 = oggettoOriginale.competenzaOriginale2 || 0;
		oggettoDaAggiornare.residuoOriginale = oggettoOriginale.residuoOriginale || 0;
		oggettoDaAggiornare.cassaOriginale = oggettoOriginale.cassaOriginale || 0;
        oggettoDaAggiornare.daAnnullare = oggettoOriginale.daAnnullare;
        oggettoDaAggiornare.daInserire = oggettoOriginale.daInserire;

        // Qualifico correttamente l'oggetto da inserire
        oggettoDaAggiornare = qualify(oggettoDaAggiornare, "specificaImporti.elementoCapitoloVariazione");
        ajaxParam = $.extend(true, {}, oggettoDaAggiornare/*, oggettoOld*/);
       
        return $.postJSON(url, ajaxParam)
        .then(function(data) {
            var errori = data.errori;
            var alertErroriModale = $("#ERRORI_modaleEditStanziamenti");
            var modale = $("#editStanziamenti");

            // Nascondo gli alert
            alertErrori.slideUp();
            if(impostaDatiNegliAlert(errori, alertErroriModale)) {
                return;
            }
            modale.modal('hide');
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, ajaxParam, data.specificaImporti && data.specificaImporti.cassaIncongruente, data.specificaImporti && data.specificaImporti.cassaIncongruenteDopoDefinizione);
                return;
            }
            leggiCapitoliNellaVariazione();
        }).always($overlayModale .overlay.bind($overlayModale , 'hide'));
   }	
    
    function init(){
    	$("#competenzaVariazioneAnno0NuovoDettaglio").off("blur").on("blur", impostaValoreCassaSeApplicabile.bind(undefined, 'NuovoDettaglio')).gestioneDeiDecimali();
    	$("#competenzaVariazioneAnno0Modale").off("blur").on("blur", impostaValoreCassaSeApplicabile.bind(undefined,"Modale")).gestioneDeiDecimali();
    	//carico i capitoli legati alla variazione (se ce ne sono)
        leggiCapitoliNellaVariazione();

        //lego gli handlers: capitolo
        $("#pulsanteRicercaCapitolo").substituteHandler("click", cercaCapitolo);
        //SIAC-5016
        $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", cercaCapitoloNellaVariazione);
        $("#redirezioneNuovoCapitoloButton").substituteHandler("click", redirezioneNuovoCapitolo);
        $("#annullaCapitoloButton").substituteHandler("click", annullaCapitolo);
        
        $('.gestisci-collapse').off('click').eventPreventDefault('click', apriEChiudiCollapse);



        //SIAC-6884
        disabledNuovoEliminaCapitoloPerDecentrato();
        disabledRadioButtonSpesa();

    }
    
	return capVarImp;

}(Variazioni || {}));
