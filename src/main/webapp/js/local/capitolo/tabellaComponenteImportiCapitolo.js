/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** GESTIONE COMPONENTE IMPORTI CAPITOLO ****
*************************************************
*/
var TabellaImportiComponenteCapitolo = (function (cic) {
	"use strict";
	
	var exports = {};
	var selectorTabella = '#tabellaStanziamentiPerComponenti';
	
	exports.creaEPosizionaRigheComponenti = creaEPosizionaRigheComponenti;
	exports.creaEPosizionaRigheImporti = creaEPosizionaRigheImporti;
	exports.creaEPosizionaRigheComponentiConCallback  = creaEPosizionaRigheComponentiConCallback;
	exports.creaEPosizionaRigheImportiConCallback = creaEPosizionaRigheImportiConCallback;
	exports.creaEPosizionaRigheTabellaNuovaComponente = creaEPosizionaRigheTabellaNuovaComponente;
	exports.creaTabellaPerModificaImportoPerTipoComponente = creaTabellaPerModificaImportoPerTipoComponente;

	//si potrebbe dinamizzare anche questo..per ora no.
	var listaDettaglioComponenteImportiCapitolo=[
		{
			tipoDettaglioComponenteImportiCapitolo: {descrizione: 'Stanziamento'}, 
			editabile: true
		}, 
		{
			tipoDettaglioComponenteImportiCapitolo: {descrizione: 'Impegnato'},
			 editabile: false
		}];
	
	function getTd(tdContent, stile){ // :alignment
		var result = '<td ';
		if(stile){
			result += 'style="' + stile + '"';
		}
		
		return result + '>' + tdContent + '</td>';
	}
	
	function getTdInput(id, cssClass, value, editabile){

		var inputHtml =  '<input '
		+ getAttrIfNotUndefined(id, 'id="', '"') + '" type="text" '+ getReadOnlyAttr(editabile)+' name="" value="' + value + '"'
		+ ' class="custom-new-component soloNumeri decimale text-right ' + getAttrIfNotUndefined(cssClass) + '" required="" />';
		return getTd(inputHtml, 'position: relative;');
	}
	
	function getAttrIfNotUndefined(attrStr, beforeStr, afterStr){
		if(!attrStr){
			return '';
		}
		return beforeStr && afterStr? 
			(beforeStr + attrStr + afterStr) : attrStr;
	}
	
	function getReadOnlyAttr(editabile){
		return editabile? '' : 'readonly =""';
	}
	
	function getTdEdit(riga, dettaglio, cssEditClass, index, firstRow){
		
		if(firstRow){
			var tdStringa = '';
			tdStringa += ('<td style="text-align:left;"' + ' rowspan="'+ riga.rowspanEdit + ' ">');
			if(riga.rigaModificabilePerTipoImporto && dettaglio.tipoDettaglioEditabile ){
				//SIAC-8003
				if(capitoloCat.value.includes('true')){//categoria==AAM o categoria == FPV
					// task-236 
					//if(riga.firstCellString.includes('Residuo')||riga.firstCellString.includes('Da attribuire')){//riga =residuo o da attribuire
					if(!riga.firstCellString.includes('Cassa')){//riga =residuo o da attribuire
						tdStringa += ('<a href="#editStanziamenti" title="modifica gli importi" role="button" class="' + cssEditClass +'" data-index-element="' + index + '">' + 
				'	<i class="icon-pencil icon-2x">' + 
				'		<span class="nascosto">modifica</span>' + 
				'	</i>' + 
				' </a>');	
					}
				}else{//categoria != AMM o categoria != FPV
					tdStringa += ('<a href="#editStanziamenti" title="modifica gli importi" role="button" class="' + cssEditClass +'" data-index-element="' + index + '">' + 
				'	<i class="icon-pencil icon-2x">' + 
				'		<span class="nascosto">modifica</span>' + 
				'	</i>' + 
				' </a>');	
				}
			}
						
			if(riga.rigaEliminabile && dettaglio.tipoDettaglioEliminabile){
				tdStringa += '<a href="#msgElimina" title="elimina" class="eliminaComponente" data-index-element="' + index + '"" style="padding-left:15px;">'
					+ '     <i class="icon-trash icon-2x">'
					+ '       <span class="nascosto">elimina</span>'
					+ '     </i>'
					+ '</a>';	
			}
						
			tdStringa += '</td>';
			return tdStringa;	
		}
		
		if(riga.rowspanEdit !== riga.sottoRigheSize){
			//questa proprieta' del background-color messo cosi, non e' bella
			// ma e' meglio della soluzione precedente per la quale la riga aveva un td in meno'
			return '<td style="background-color:white;"></td>';
		}
	}
	
		
	
	function creaStringTabella(righe, visualizzaImportoIniziale, cssEditClass){
		var table= "";
		for(var j in righe){
			var riga = righe[j];
			var firstRow = true;
			var size = riga.sottoRigheSize -1;
			for (var i in riga.sottoRighe){
				var rd = riga.sottoRighe[i];
				var trClass = rd.trCssClass + " " + riga.trCssClass;
				if(i ==size){
					trClass += " " + riga.lastSottoRigaCssClass;
				}
				var rigaTabella  = '<tr class="' + trClass + '">';
				if(firstRow){
					rigaTabella += riga.firstCellString;
				}
				rigaTabella += getTd(rd.tipoDettaglioComponenteImportiCapitolo.descrizione, 'text-align:center;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoInizialeAnniPrecedenti, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoAnniPrecedenti, 'text-align:right;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoResiduoInizialeAnno0, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoResiduoAnno0, 'text-align:right;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoInizialeAnno0, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoAnno0, 'text-align:right;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoInizialeAnno1, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoAnno1, 'text-align:right;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoInizialeAnno2, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoAnno2, 'text-align:right;');
				visualizzaImportoIniziale && (rigaTabella += getTd(rd.formattedImportoInizialeAnniSuccessivi, 'text-align:right;'));
				rigaTabella += getTd(rd.formattedImportoAnniSuccessivi, 'text-align:right;');
				if(cssEditClass){
					rigaTabella += getTdEdit(riga, rd, cssEditClass, j, firstRow);
				}
				rigaTabella += '</tr>';
				
				firstRow=false;
				table += rigaTabella;
			}
			
		}
		return table;
	}
	
	function creaRigheComponentiSeNecessario(righeComponenti, visualizzaImportoIniziale, cssClass, impostaCallBack, callbackModificaImporti, callbackElimina ){
		var rowCompetenza = $('#competenzaCella').closest('tr');
		var lastRowCompetenza = $("tr.competenzaLast");
		
		if(!rowCompetenza.data('loaded')){
			var callbackEdit;
			var callbackDelete;
			var table = creaStringTabella(righeComponenti,visualizzaImportoIniziale, cssClass);
			
			lastRowCompetenza.after(table);
			
			if(impostaCallBack){
				callbackEdit = callbackModificaImporti && typeof callbackModificaImporti == 'function'? callbackModificaImporti : $.noop;
				callbackDelete = callbackElimina && typeof callbackElimina == 'function'? callbackElimina : $.noop;
				$(selectorTabella).find('a.editComponenti').substituteHandler('click', callbackEdit.bind(undefined,righeComponenti));
				$(selectorTabella).find('a.eliminaComponente').substituteHandler('click',callbackDelete.bind(undefined,righeComponenti));
			}
			
			rowCompetenza.data('loaded', true);	
			
		}
	}
	
	
	function creaStringaTabellaPerModificaImportoSuTipoComponente(righe){
		var table= "";
		for(var j in righe){
			var riga = righe[j];
			var firstRow = true;
			for (var i in riga.sottoRighe){
				var rd = riga.sottoRighe[i];
				var rigaTabella  = '<tr>';
				var descTipoDettaglio = rd && rd.tipoDettaglioComponenteImportiCapitolo && rd.tipoDettaglioComponenteImportiCapitolo.descrizione;
				if(firstRow){
					rigaTabella += '<td class="componenti-competenza" rowspan="'+ riga.sottoRigheSize + '">' + riga.descrizioneImporto+ '</td>'
				}
				firstRow=false;
				
				rigaTabella += getTd(descTipoDettaglio, 'text-align:left;');
				rigaTabella += getTdInput('annoPrec' + descTipoDettaglio,'annoPrec', rd.formattedImportoAnniPrecedenti, false);
				rigaTabella += getTdInput('residuo' + descTipoDettaglio, 'residuo',rd.formattedImportoResiduoAnno0 , riga.importoEditabileCellaResiduo && rd.tipoDettaglioEditabile);
				rigaTabella += getTdInput('anno0' + descTipoDettaglio,'anno0', rd.formattedImportoAnno0 , riga.importoEditabileCellaAnnoBilancio && rd.tipoDettaglioEditabile);
				rigaTabella += getTdInput('anno1' + descTipoDettaglio, 'anno1', rd.formattedImportoAnno1, riga.importoEditabileCelleAnnoBilancioPiuUnoPiuDue && rd.tipoDettaglioEditabile);
				rigaTabella += getTdInput('anno2' + descTipoDettaglio, 'anno2', rd.formattedImportoAnno2, riga.importoEditabileCelleAnnoBilancioPiuUnoPiuDue && rd.tipoDettaglioEditabile);
				rigaTabella += getTdInput('anniSucc'+ descTipoDettaglio , 'anniSucc', rd.formattedImportoAnniSuccessivi, false);
				rigaTabella += '</tr>'
				
				table += rigaTabella;
			}
			
		}
		return table;
	}
	
	function creaEPosizionaRigheImporti(righeImporti, visualizzaImportoIniziale){
		
		var trs = creaStringTabella(righeImporti,visualizzaImportoIniziale);
		$(selectorTabella).find('tbody').html(trs);
		
	}
	
	function creaEPosizionaRigheImportiConCallback(righeImporti, callbackModificaImporti, paramCallback){
		var param;
		var callback;
		
		var trs = creaStringTabella(righeImporti, false, 'editImporti' );
		$(selectorTabella).find('tbody').html(trs);
		
		callback = callbackModificaImporti && typeof callbackModificaImporti == 'function'? callbackModificaImporti : $.noop;
		param = paramCallback? paramCallback : righeImporti;
		
		$(selectorTabella).find('a.editImporti').substituteHandler('click', callbackModificaImporti.bind(undefined,param));
	}
	
	function creaTabellaPerModificaImportoPerTipoComponente(righe, pulsante){
		var index;
		var riga;
		var table;
		var $table = $('#tabellaModificaImporti');

		if(!pulsante){
			//...trovo un modo per caricarmi il dato
		}
		index = pulsante.data('index-element');
		riga = righe[index];
		
		table = creaStringaTabellaPerModificaImportoSuTipoComponente([riga]);
		$table.find('tbody').html(table);
		return riga;
	}

	
	function visualizzaNascondiRigheComponenti(gestioneDinamicaRigheFondino){
		//e && e.preventDefault() && e.stopPropagation() && e.stopImmediatePropagation();
		var rows = $('.componentiCompetenzaRow');
		var cssClass = 'componenti-shown';
		if(rows.hasClass(cssClass)){
			rows.hide();
			rows.removeClass(cssClass);
			return;
		}
		if(gestioneDinamicaRigheFondino && determinaSeCapitoloFondino()){
			rows.not('.nascondi-se-fondino').show();
			rows.filter('.nascondi-se-fondino').hide();
		}else{
			rows.show();	
		}
		
		rows.addClass(cssClass);
	}
	
	function determinaSeCapitoloFondino(){
		var $categoriaElement = $("#categoriaCapitolo"); 
		var $classificatore3 = $("#classificatoreGenerico3"); 
		var $checkImpegnabile = $("#flagImpegnabile");
		if($categoriaElement.length === 0 || $classificatore3.length === 0 ||  $checkImpegnabile === 0){
			return;
		}
		var categoria = $categoriaElement.val();
		var budget = $.trim($.trim($("#classificatoreGenerico3 option:selected").text()).split("-")[0])  === '01';
		var checkImpegnabile = $checkImpegnabile.prop("checked");
		return categoria && budget && !checkImpegnabile;
	}
	
	function creaEPosizionaRigheComponenti(righeComponenti,visualizzaImportoIniziale, gestioneDinamicaRigheFondino, e){
		
		e && e.preventDefault() && e.stopPropagation() && e.stopImmediatePropagation();
		//TODO: migliorare questa selezione
		creaRigheComponentiSeNecessario(righeComponenti,visualizzaImportoIniziale);
		
		visualizzaNascondiRigheComponenti(gestioneDinamicaRigheFondino);
		
	}
	
	function creaEPosizionaRigheComponentiConCallback(righeComponenti, callbackModificaImporti, callbackElimina, e){
	
		e && e.preventDefault() && e.stopPropagation() && e.stopImmediatePropagation();
		//TODO: migliorare questa selezione
		creaRigheComponentiSeNecessario(righeComponenti, false,'editComponenti',true, callbackModificaImporti, callbackElimina);
		
		visualizzaNascondiRigheComponenti();
		
	}
	
	function creaEPosizionaRigheTabellaNuovaComponente(){
		var trs = '';
		for(var a in listaDettaglioComponenteImportiCapitolo){
			var dettaglio = listaDettaglioComponenteImportiCapitolo[a];
			var descrizioneDettaglio = dettaglio && dettaglio.tipoDettaglioComponenteImportiCapitolo && dettaglio.tipoDettaglioComponenteImportiCapitolo.descrizione; 
			
			trs += '<tr>'
			trs += getTd(descrizioneDettaglio, 'text-align:left;');
			trs += getTdInput('annoPrecNuovaComponente' + descrizioneDettaglio,'', '', false);
			trs += getTdInput('residuoNuovaComponente' + descrizioneDettaglio,'', '', false);
			trs += getTdInput('anno0NuovaComponente'+ descrizioneDettaglio, 'anno0NuovaComponente', '', dettaglio.editabile);
			trs += getTdInput('anno1NuovaComponente'+ descrizioneDettaglio,'anno1NuovaComponente', '', dettaglio.editabile);
			trs += getTdInput('anno2NuovaComponente'+ descrizioneDettaglio, 'anno2NuovaComponente', '', dettaglio.editabile);
			trs += getTdInput('anniSuccNuovaComponente' + descrizioneDettaglio, '','',  false);
			trs += '</tr>'	
		}
		
		$('#tabellaInserimentoImportiNuovaComponente').find('tbody').html(trs);
		// SIAC-7132
		$("input.soloNumeri", '#tabellaInserimentoImportiNuovaComponente').allowedChars({
		    numeric: true
		});
		$("input.decimale", '#tabellaInserimentoImportiNuovaComponente').gestioneDeiDecimali();

	}
	
	
	return exports;
	
	}({}));