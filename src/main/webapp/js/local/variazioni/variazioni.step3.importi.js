/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 3 delle variazioni - variazione di importi senza gestione UEB */
var Variazioni = Variazioni || {};

var VariazioniImporti = (function (varImp) {
	"use strict";
	//variables
	var $pulsanteRegistraImportiNellaVariazione = $("#button_registraVariazione");
	var $datiOttenuti = ""; //<-- solo per SIAC 6881 aggiornamento dinamico tabelle
	var $wrapApplicazione = ""; //<-- solo per SIAC 6881 aggiornamento dinamico tabelle
	var $wrapTipoCapitoloApplicazioneMacroTipologia;
	var $annoAttualeCompetenza;
	var $selectElement = $('#dropdown-select-com');
	varImp.impostaValoreCassaSeApplicabile = impostaValoreCassaSeApplicabile;
	varImp.cercaCapitoloNellaVariazione = cercaCapitoloNellaVariazione;
	varImp.leggiCapitoliNellaVariazione = leggiCapitoliNellaVariazione;
	varImp.cercaCapitolo = cercaCapitolo;
	varImp.redirezioneNuovoCapitolo = redirezioneNuovoCapitolo;
	varImp.annullaCapitolo = annullaCapitolo;
	varImp.registraImportiNellaVariazione = registraImportiNellaVariazione;
	varImp.effettuaNuovaRicerca = effettuaNuovaRicerca;
	varImp.salvaEProsegui = salvaEProsegui;
	varImp.apriModaleInserisciStanziamenti = apriModaleInserisciStanziamenti;


	/* ************************************************************* ***/
	/* ****************  UTILITIES  ********************************** ***/
	/* ************************************************************* ***/
	/**
	 * Imposta gli stanziamenti nella tabella.
	 *
	 * @param importo l'oggetto contente gli importi da inserire
	 * @param tabella la tabella in cui inserire i dati
	 */

	function impostaStanziamenti(importo, tabella) {

		var anno = importo.annoCompetenza,
			tabellaStanziamenti = tabella.first().children("th").slice(1);
		if (anno === parseInt(tabellaStanziamenti.eq(0).html(), 10)) {
			impostaDati(importo, 0);
		} else if (anno === parseInt(tabellaStanziamenti.eq(1).html(), 10)) {
			impostaDati(importo, 1);
		} else if (anno === parseInt(tabellaStanziamenti.eq(2).html(), 10)) {
			impostaDati(importo, 2);
		}
	}

	/**
	 * Imposta i dati nella tabella, relativamente all'indice selezionato.
	 *
	 * @param importo l'oggetto contente gli importi da inserire
	 * @param indice  l'indice relativo alla colonna in cui inserire i dati
	 */

	function impostaDati(importo, indice) {

		varImp.impostaValutaEAllineaADestra("#totaleCompetenzaTrovatoAnno" + indice, importo.stanziamento);
		varImp.impostaValutaEAllineaADestra("#totaleResiduiTrovatoAnno" + indice, importo.stanziamentoResiduo);
		varImp.impostaValutaEAllineaADestra("#totaleCassaTrovatoAnno" + indice, importo.stanziamentoCassa);



	}

	/**
	 * Imposta il totale degli importi della variazione.
	 */

	//TODO: valutare, potrebbe essere da togliere
	function impostaTotaliImporti(totaliImporti) {

		varImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totaliImporti.totaleEntrataCompetenza);
		varImp.impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totaliImporti.totaleEntrataResiduo);
		varImp.impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totaliImporti.totaleEntrataCassa);

		varImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totaliImporti.totaleSpesaCompetenza);
		varImp.impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totaliImporti.totaleSpesaResiduo);
		varImp.impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totaliImporti.totaleSpesaCassa);

		varImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", totaliImporti.differenzaCompetenza);
		varImp.impostaValutaEAllineaADestra("#differenzaResiduoVariazione", totaliImporti.differenzaResiduo);
		varImp.impostaValutaEAllineaADestra("#differenzaCassaVariazione", totaliImporti.differenzaCassa);
	}

	//CR-3403

	function impostaTotaliInDataTable(totaleStanziamentiEntrata, totaleStanziamentiCassaEntrata, totaleStanziamentiResiduiEntrata,
		totaleStanziamentiSpesa, totaleStanziamentiCassaSpesa, totaleStanziamentiResiduiSpesa) {


		var totaleStanziamentiEntrataNotUndefined = totaleStanziamentiEntrata || 0;
		var totaleStanziamentiCassaEntrataNotUndefined = totaleStanziamentiCassaEntrata || 0;
		var totaleStanziamentiResiduiEntrataNotUndefined = totaleStanziamentiResiduiEntrata || 0;

		var totaleStanziamentiSpesaNotUndefined = totaleStanziamentiSpesa || 0;
		var totaleStanziamentiCassaSpesaNotUndefined = totaleStanziamentiCassaSpesa || 0;
		var totaleStanziamentiResiduiSpesaNotUndefined = totaleStanziamentiResiduiSpesa || 0;

		varImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totaleStanziamentiEntrataNotUndefined);
		varImp.impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totaleStanziamentiResiduiEntrataNotUndefined);
		varImp.impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totaleStanziamentiCassaEntrataNotUndefined);

		varImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totaleStanziamentiSpesaNotUndefined);
		varImp.impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totaleStanziamentiResiduiSpesaNotUndefined);
		varImp.impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totaleStanziamentiCassaSpesaNotUndefined);

		varImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", (totaleStanziamentiEntrataNotUndefined - totaleStanziamentiSpesaNotUndefined));
		varImp.impostaValutaEAllineaADestra("#differenzaResiduoVariazione", (totaleStanziamentiResiduiEntrataNotUndefined - totaleStanziamentiResiduiSpesaNotUndefined));
		varImp.impostaValutaEAllineaADestra("#differenzaCassaVariazione", (totaleStanziamentiCassaEntrataNotUndefined - totaleStanziamentiCassaSpesaNotUndefined));
	}

	/**
	 * Imposta il form di aggiornamento della variazione.
	 *
	 * @param riga la riga considerata
	 */

	function apriEPopolaModaleModificaImporti(riga) {

		// Pulsante per il salvataggio
		var pulsanteSalvataggio = $("#EDIT_aggiorna");
		// Dati per l'editabilità dei campi

		// Pulisco il form
		$("#editStanziamenti fieldset :input").val("");
		// Nascondo l'eventuale alert di errore già presente
		$("#ERRORI_modaleEditStanziamenti").slideUp();
		var tabellaSorgente = $("#tabellaGestioneVariazioniVB");
		var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);

		apriEPopolaModaleModificaImporti(datiNellaRiga);


		//SIAC-6881
		// $("#competenzaVariazioneModal").val(datiNellaRiga.competenza.formatMoney());
		// $("#residuoVariazioneModal").val(datiNellaRiga.residuo.formatMoney());
		// $("#cassaVariazioneModal").val(datiNellaRiga.cassa.formatMoney());

		// $("#titoloModaleVariazioneStanziamenti").html("Modifica Stanziamenti Capitolo " + datiNellaRiga.numeroCapitolo + " / " + datiNellaRiga.numeroArticolo);

		// // Aggiornamento
		// pulsanteSalvataggio.substituteHandler("click", function(e) {
		// 	e.preventDefault();
		// 	aggiornaCapitoloNellaVariazione( datiNellaRiga);
		// });

		// Apro il modale per la modifica degli importi
		$("#collapse_ricerca").addClass("in");
		window.location.hash = "collapse_ricerca";

	}

	/**
	 * Imposta il valore presente nella variazione di competenza anche come variazione della cassa, nel caso in cui
 	* la variazione degli importi di cassa sia possibile.
 	*/

	function impostaValoreCassaSeApplicabile() {

		var inputCompetenza = $("#competenzaVariazione");
		var inputCassa = $("#cassaVariazione");

		if (!inputCassa.attr("disabled") && inputCassa.val() === "") {
			inputCassa.val(inputCompetenza.val());
		}
	}

	function rimuoviOverlay() {
		$("#tabellaGestioneVariazioniVB").overlay('hide');
		$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
	}

	function richiamoServizioPersistenzaImporti(url, ajaxParam) {
		$("#tabellaGestioneVariazioniVB").overlay('show');
		$pulsanteRegistraImportiNellaVariazione.attr('disabled', '');
		return $.postJSON(url, ajaxParam)
			.then(function (data) {
				var errori = data.errori;
				var messaggi = data.messaggi;
				var cassaIncongruente = data.specificaImporti && data.specificaImporti.cassaIncongruente;
				var cassaIncongruenteDopoDefinizione = data.specificaImporti && data.specificaImporti.cassaIncongruenteDopoDefinizione;

				if (impostaDatiNegliAlert(errori, $("#ERRORI"))) {
					$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
					return;
				}

				if (messaggi && messaggi.length > 0) {
					gestioneRichiestaProsecuzione(messaggi, url, ajaxParam, cassaIncongruente, cassaIncongruenteDopoDefinizione);
					return;
				}

				return varImp.leggiCapitoliNellaVariazione();
			}).always($("#tabellaGestioneVariazioniVB").overlay.bind($("#tabellaGestioneVariazioniVB"), 'hide'));
	}

	/* ************************************************************* ***/
	/* ************  GESTIONE RICHIESTA CONFERME UTENTE  ********** ***/
	/* ************************************************************* ***/

    /**
    *  Apre e imposta la modale di conferma prosecuzione dell'azione
    * */
	function gestioneRichiestaProsecuzione(array, url, ajaxParam, isCassaIncongruente, isCassaIncongruenteDopoDefinizione) {

		ajaxParam["specificaImporti.ignoraValidazione"] = true;
		ajaxParam["specificaImporti.ignoraValidazioneImportiDopoDefinizione"] = !!isCassaIncongruenteDopoDefinizione;

		impostaRichiestaConfermaUtente(array[0].descrizione, richiamoServizioPersistenzaImporti.bind(undefined, url, ajaxParam), rimuoviOverlay);
	}


	/* ***************************************************************** ***/
	/* ****************  GESTIONE DEI CAPITOLI NELLA VARIAZIONE  ******* ***/
	/* ***************************************************************** ***/
	/**
	 * Chiamata Ajax per ottenere la lista dei Capitoli associati alla variazione
	 * e inizializzare dataTable
	 */
	function leggiCapitoliNellaVariazione() {
		$('#tabellaGestioneVariazioniVB').overlay('show');
		return $.postJSON("leggiCapitoliNellaVariazione.do", {}, function (data) {
			var errori = data.errori;
			var messaggi = data.messaggi;
			var informazioni = data.informazioni;
			var alertErrori = $('#ERRORI');
			var alertMessaggi = $("#MESSAGGI");
			var alertInformazioni = $("#INFORMAZIONI");

			// Controllo gli eventuali errori, messaggi, informazioni
			if (impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
			if (impostaDatiNegliAlert(messaggi, alertMessaggi)) {
				return;
			}
			if (impostaDatiNegliAlert(informazioni, alertInformazioni)) {
				return;
			}

			$('form').removeClass('form-submitted');
			//chiamo dataTable
			impostaDataTableCapitoliNellaVariazione($("#tabellaGestioneVariazioniVB"));

			impostaTotaliInDataTable(data.totaleStanziamentiEntrata, data.totaleStanziamentiCassaEntrata, data.totaleStanziamentiResiduiEntrata,
				data.totaleStanziamentiSpesa, data.totaleStanziamentiCassaSpesa, data.totaleStanziamentiResiduiSpesa);
		}).always(rimuoviOverlay.bind(this));
	}

	/**
	 * Imposta il dataTable per l'elenco deli capitoli nella variazione.
     *
	 * @param tabella       la tabella da paginare
	 * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
	*/

	function impostaDataTableCapitoliNellaVariazione(tabella) {
		var isEditable = $("input[name='specificaImporti.tipoCapitolo']").length !== 0;

		var opzioniNuove = {
			bServerSide: true,
			sServerMethod: "POST",
			sAjaxSource: "leggiCapitoliNellaVariazioneImportiAjax.do",
			bPaginate: true,
			bLengthChange: false,
			iDisplayLength: 10,
			bSort: false,
			bInfo: true,
			bAutoWidth: true,
			bFilter: false,
			bProcessing: true,
			bDestroy: true,
			oLanguage: {
				sZeroRecords: "Non sono presenti capitoli associati alla variazione test"
			},
			// Definizione delle colonne
			aoColumnDefs: [
				{
					aTargets: [0],
					mData: function (source) {
						var result = source.descrizione ? "<a rel='popover' href='#'>" + source.descrizione + "</a>" : source.denominazione;
						if (source.datiAccessorii) {
							result += " <em>(" + source.datiAccessorii + ")</em>";
						}
						return result;
					},
					fnCreatedCell: function (nTd, sData, oData) {
						if (oData.descrizione !== null && oData.descrizione !== undefined) {
							// Settings del popover
							var settings = {
								"content": oData.descrizione,
								"title": "Descrizione",
								"trigger": "hover"
							};
							// Importante : attivare il popover sull'elemento anchor
							$("a", nTd).off("click")
								.on("click", function (event) { event.preventDefault(); })
								.popover(settings);
						}
					}
				},
				{
					aTargets: [1],
					mData: function (source) {
						var result = '';
						var table = " <table class='table table-condensed  custom-table-components'>";
						//var descHead =  "<tr><tr>";

						result += table;
						if (typeof (source.componentiCapitolo) != 'undefined' && source.componentiCapitolo.length > 0) {

							for (var i = 0; i < source.componentiCapitolo.length; i++) {
								var descHead = "<tr><tr><th rowspan='2' style='vertical-align : middle; background-color: white;'>";
								result += descHead;
								result += (((((source || "N/A").componentiCapitolo || []))[i] || "N/A").descrizione) || "N/A";
								var descFoot = "</th><td>Stanziamento</td>"
								result += descFoot;
								var stanzFoot = "</tr><tr><td>Impegnato</td>";
								result += stanzFoot;
								var impFoot = "</tr></tr> "
								result += impFoot;
							}

						} else {
							result = "";
						}
						result += "</table>";
						return result;
					},
					fnCreatedCell: function (nTd) {
						$(nTd).addClass("custom-td-components");
						$(nTd).addClass("text-right");
					}
				},
				{
					aTargets: [2],
					mData: function (source) {
						/********SIAC 6881 */
						var result = '';
						var table = " <table id='customTable' class='table table-condensed custom-table-components'>";
						var descHead = "<tr><tr>";
						result += table;
						if (typeof (source.componentiCapitolo) != 'undefined' && source.componentiCapitolo.length > 0) {

							for (var i = 0; i < source.componentiCapitolo.length; i++) {
								result += descHead;
								var descFoot = "<td style='text-align:right;'>"
								result += descFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 0) ? source.componentiCapitolo[i].listaImportiComponente[0].stanziamento.formatMoney() : "0";
								//result+=200;   
								var stanzFoot = "</td></tr><tr><td style='text-align:right;'>";
								result += stanzFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 0) ? source.componentiCapitolo[i].listaImportiComponente[0].impegnato.formatMoney() : "0";
								var impFoot = "</td></tr></tr> "
								result += impFoot;
							}
						} else {
							result = "";
						}
						result += "</table>";
						return result;
					},
					fnCreatedCell: function (nTd) {
						$(nTd).addClass("custom-td-components");
						$(nTd).addClass("text-right");
					}
				},
				{
					aTargets: [3],
					mData: function (source) {
						var result = '';
						var table = " <table id='customTable' class='table table-condensed custom-table-components'>";
						//var descHead =  "<tr><tr>";
						var descHead = "<tr><tr>";
						result += table;
						if (typeof (source.componentiCapitolo) != 'undefined' && source.componentiCapitolo.length > 0) {

							for (var i = 0; i < source.componentiCapitolo.length; i++) {
								result += descHead;
								var descFoot = "<td style='text-align:right;'>"
								result += descFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 1) ? source.componentiCapitolo[i].listaImportiComponente[1].stanziamento.formatMoney() : "0";
								var stanzFoot = "</td></tr><tr><td style='text-align:right;'>";
								result += stanzFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 1) ? source.componentiCapitolo[i].listaImportiComponente[1].impegnato.formatMoney() : "0";
								var impFoot = "</td></tr></tr> "
								result += impFoot;
							}
						} else {
							result = "";
						}
						result += "</table>";
						return result;
					},
					fnCreatedCell: function (nTd) {
						$(nTd).addClass("custom-td-components");
						$(nTd).addClass("text-right");
					}
				},
				{
					bVisible: isEditable,
					aTargets: [4],
					mData: function (source) {
						var result = '';
						var table = " <table id='customTable' class='table table-condensed custom-table-components'>";
						//var descHead =  "<tr><tr>";
						var descHead = "<tr><tr>";
						result += table;
						if (typeof (source.componentiCapitolo) != 'undefined' && source.componentiCapitolo.length > 0) {

							for (var i = 0; i < source.componentiCapitolo.length; i++) {
								result += descHead;
								var descFoot = "<td style='text-align:right;'>"
								result += descFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 2) ? source.componentiCapitolo[i].listaImportiComponente[2].stanziamento.formatMoney() : "0";
								var stanzFoot = "</td></tr><tr><td style='text-align:right;'>";
								result += stanzFoot;
								result += (source.componentiCapitolo[i].listaImportiComponente.length > 2) ? source.componentiCapitolo[i].listaImportiComponente[2].impegnato.formatMoney() : "0";
								var impFoot = "</td></tr></tr> "
								result += impFoot;
							}
						} else {
							result = "";
						}
						result += "</table>";
						return result;
					},
					fnCreatedCell: function (nTd) {
						$(nTd).addClass("custom-td-components");
						$(nTd).addClass("text-right");
					}
				},
				{
					bVisible: isEditable,
					aTargets: [5],
					mData: function () {
						return "<a href='#editStanziamenti' title='modifica gli importi' role='button' data-toggle='modal'>" +
							"<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
							"</a>";
					},
					fnCreatedCell: function (nTd, sData, oData, iRow) {
						if (isEditable) {
							$(nTd).addClass("text-center")
								.find("a")
								.substituteHandler("click", function () {
									//apriEPopolaModaleModificaImporti(iRow);
									apriEPopolaModaleModificaImportiDaTabella(iRow);
								});
						}
					}
				},
				{
					bVisible: isEditable,
					aTargets: [6],
					mData: function () {
						return "<a href='#msgElimina' title='elimina' role='button' data-toggle='modal'>" +
							"<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
							"</a>";
					},
					fnCreatedCell: function (nTd, sData, oData, iRow) {
						if (isEditable) {
							$(nTd).addClass("text-center")
								.find("a")
								.off("click")
								.on("click", function () {
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
				$('#tabellaGestioneVariazioniVB_wrapper').overlay('show');
			},
			// Chiamata al termine della creazione della tabella
			fnDrawCallback: function () {

				tabella.parent().find('div.dataTables_processing').parent("div").hide();
				$('#tabellaGestioneVariazioniVB_wrapper').overlay('hide');
			}
		};
		var opzioniClone = $.extend(true, {}, varImp.opzioniBaseDataTable);
		var opzioni = $.extend(true, opzioniClone, opzioniNuove);
		var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

		if ($(tabelleGiaInDataTable).filter(tabella).length > 0) {
			tabella.dataTable().fnClearTable(false);
			tabella.dataTable().fnDestroy();
		}

		tabella.dataTable(opzioni);
	}


	/*
	 * SIAC-6881
	 * Funzione per il build dei campi della tabella 
	 * Stanziamento
	 */
	// function impostaDatiTabellaStanziamenti(data2, wrapTipoCapitoloApplicazione){

	// 	var objTypeArr = [];
	// 	if(wrapTipoCapitoloApplicazione == 'UscitaGestione' && data2.capitoloUscitaGestione!= null && typeof(data2.capitoloUscitaGestione)!= 'undefined' &&
	// 			data2.capitoloUscitaGestione.listaImportiCapitolo!= null && typeof(data2.capitoloUscitaGestione.listaImportiCapitolo)!= 'undefined'){
	// 		objTypeArr = data2.capitoloUscitaGestione.listaImportiCapitolo;
	// 	}else if(wrapTipoCapitoloApplicazione == 'UscitaPrevisione' && data2.capitoloUscitaPrevisione!= null && typeof(data2.capitoloUscitaPrevisione)!= 'undefined' &&
	// 			data2.capitoloUscitaPrevisione.listaImportiCapitolo!= null && typeof(data2.capitoloUscitaPrevisione.listaImportiCapitolo)!= 'undefined'){
	// 			objTypeArr = data2.capitoloUscitaPrevisione.listaImportiCapitolo;
	// 	}else if(wrapTipoCapitoloApplicazione == 'EntrataGestione' && data2.capitoloEntrataGestione!= null && typeof(data2.capitoloEntrataGestione)!= 'undefined' &&
	// 			data2.capitoloEntrataGestione.listaImportiCapitolo!= null && typeof(data2.capitoloEntrataGestione.listaImportiCapitolo)!= 'undefined'){
	// 		objTypeArr = data2.capitoloEntrataGestione.listaImportiCapitolo;
	// 	}else if(wrapTipoCapitoloApplicazione == 'EntrataPrevisione' && data2.capitoloEntrataPrevisione!= null && typeof(data2.capitoloEntrataPrevisione)!= 'undefined' &&
	// 			data2.capitoloEntrataPrevisione.listaImportiCapitolo!= null && typeof(data2.capitoloEntrataPrevisione.listaImportiCapitolo)!= 'undefined'){
	// 		objTypeArr = data2.capitoloEntrataPrevisione.listaImportiCapitolo;
	// 	}





	// 	if(	objTypeArr.length>2){
	// 		for(var i=0;i<objTypeArr.length;i++){
	// 			var objType= objTypeArr[i];
	// 			var campo;
	// 			for (campo in objType) {
	// 				$('#importiGestione'+i+campo).text(objType[campo]);
	// 				}
	// 		}
	// 	}
	// }

	//SIAC-6881
	function buildTabellaRicercaCapitolo(data2, wrapTipoCapitoloApplicazione) {

		$("#tabellaStanziamentiTotali").html("");
		var componentiCapitolo = [];
		
		if (wrapTipoCapitoloApplicazione == 'UscitaGestione' && data2.capitoloUscitaGestione != null && typeof (data2.capitoloUscitaGestione) != 'undefined' &&
			data2.capitoloUscitaGestione.componentiCapitolo != null && typeof (data2.capitoloUscitaGestione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloUscitaGestione.componentiCapitolo;
			
		} else if (wrapTipoCapitoloApplicazione == 'UscitaPrevisione' && data2.capitoloUscitaPrevisione != null && typeof (data2.capitoloUscitaPrevisione) != 'undefined' &&
			data2.capitoloUscitaPrevisione.componentiCapitolo != null && typeof (data2.capitoloUscitaPrevisione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloUscitaPrevisione.componentiCapitolo;
			
		} else if (wrapTipoCapitoloApplicazione == 'EntrataGestione' && data2.capitoloEntrataGestione != null && typeof (data2.capitoloEntrataGestione) != 'undefined' &&
			data2.capitoloEntrataGestione.componentiCapitolo != null && typeof (data2.capitoloEntrataGestione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloEntrataGestione.componentiCapitolo;
			
		} else if (wrapTipoCapitoloApplicazione == 'EntrataPrevisione' && data2.capitoloEntrataPrevisione != null && typeof (data2.capitoloEntrataPrevisione) != 'undefined' &&
			data2.capitoloEntrataPrevisione.componentiCapitolo != null && typeof (data2.capitoloEntrataPrevisione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloEntrataPrevisione.componentiCapitolo;
			
		}

		if (componentiCapitolo.length > 0) {
			var htmlString = '';
			//ANNI 
			var anno0 = 0;
			var anno1 = 0;
			var anno2 = 0;
			if (componentiCapitolo[0].listaImportiComponente && componentiCapitolo[0].listaImportiComponente.length == 3) {
				anno0 = componentiCapitolo[0].listaImportiComponente[0].annoCompetenza;
				anno1 = componentiCapitolo[0].listaImportiComponente[1].annoCompetenza;
				anno2 = componentiCapitolo[0].listaImportiComponente[2].annoCompetenza;
				$annoAttualeCompetenza = anno0; //questo mi serve per cotruire la modale e gli anni di competenza
			}
			var annoPrecedente = anno0 - 1;
			//HEAD
			htmlString = htmlString + '<tr><th >&nbsp;</th><th >&nbsp;</th><th  class="text-right">' + annoPrecedente + '</th><th  class="text-right">Residui</th><th  class="text-right">' + anno0 + '</th><th  class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th><th  class="text-right">>' + anno2 + '</th></tr>';
			//COMPONENTI
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli"><a id="competenzaTotale" href="#tabellaStanziamentiTotaliComponentiIns"  class="disabled" >Competenza</a></th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';

			//htmlString = htmlString +'<tbody>';
			for (var i = 0; i < componentiCapitolo.length; i++) {
				if (componentiCapitolo[i].listaImportiComponente && componentiCapitolo[i].listaImportiComponente.length == 3) {

					var rowspan = 1;
					if (componentiCapitolo[i].listaImportiComponente[0].impegnato) {
						rowspan = 2;
					}

					if (componentiCapitolo[i].listaImportiComponente[0].stanziamento) {
						htmlString = htmlString + '<tr class="componentiCompetenzaRowIns">';
						htmlString = htmlString + '<td rowspan="' + rowspan + '">' + componentiCapitolo[i].descrizione + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">Stanziamento</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + 'ANNO -1' + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + 'RESIDUI  ' + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[0].stanziamento + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[1].stanziamento + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[2].stanziamento + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + '' + '</td>';
						htmlString = htmlString + '</tr>';
					}

					if (componentiCapitolo[i].listaImportiComponente[0].impegnato) {
						htmlString = htmlString + '<tr class="componentiCompetenzaRowIns">';
						htmlString = htmlString + '<td style="text-align:center;">Impegnato</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + 'ANNO -1' + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + 'RESIDUI  ' + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[0].impegnato + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[1].impegnato + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[2].impegnato + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + '' + '</td>';
						htmlString = htmlString + '</tr>';
					}

				}

			}

			//htmlString = htmlString +'</tbody>';
			//RESIDUI
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli">Residuo</th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			//CASSA
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli">Cassa</th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';

			$("#tabellaStanziamentiTotaliComponenti").html(htmlString);
			$(".componentiCompetenzaRowIns").hide();
			$("#competenzaTotale").click(function () {
				$(".componentiCompetenzaRowIns").slideToggle();
			});

		}

	}


	function aggiornaCapitoloNellaVariazione(oggettoOriginale) {
		var url = "aggiornaCapitoliNellaVariazione.do";
		var ajaxParam;
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaAggiornare = {};
		var competenza = $("#competenzaVariazioneModal").val();
		var residuo = $("#residuoVariazioneModal").val();
		var cassa = $("#cassaVariazioneModal").val();
		var $overlayModale = $('#editStanziamenti').find('.overlay-modale').overlay('show');

		oggettoDaAggiornare.uid = oggettoOriginale.uid;
		oggettoDaAggiornare.annoImporti = oggettoOriginale.annoImporti;
		oggettoDaAggiornare.competenza = competenza;
		oggettoDaAggiornare.residuo = residuo;
		oggettoDaAggiornare.cassa = cassa;
		oggettoDaAggiornare.competenzaOriginale = oggettoOriginale.competenzaOriginale || 0;
		oggettoDaAggiornare.residuoOriginale = oggettoOriginale.residuoOriginale || 0;
		oggettoDaAggiornare.cassaOriginale = oggettoOriginale.cassaOriginale || 0;
		oggettoDaAggiornare.daAnnullare = oggettoOriginale.daAnnullare;
		oggettoDaAggiornare.daInserire = oggettoOriginale.daInserire;


		// Qualifico correttamente l'oggetto da inserire
		oggettoDaAggiornare = qualify(oggettoDaAggiornare, "specificaImporti.elementoCapitoloVariazione");
		ajaxParam = $.extend(true, {}, oggettoDaAggiornare/*, oggettoOld*/);

		return $.postJSON(url, ajaxParam)
			.then(function (data) {
				var errori = data.errori;
				var alertErrori = $("#ERRORI_modaleEditStanziamenti");
				var modale = $("#editStanziamenti");
				// Nascondo gli alert
				alertErrori.slideUp();
				if (impostaDatiNegliAlert(errori, alertErrori)) {
					return;
				}
				if (data.messaggi && data.messaggi.length) {
					$('#editStanziamenti').modal('hide');
					gestioneRichiestaProsecuzione(data.messaggi, url, ajaxParam, data.specificaImporti && data.specificaImporti.cassaIncongruente, data.specificaImporti && data.specificaImporti.cassaIncongruenteDopoDefinizione);
					return;
				}
				modale.modal('hide');

				varImp.leggiCapitoliNellaVariazione();
			}).always($overlayModale.overlay.bind($overlayModale, 'hide'));
	}

	/**
	 * Aggiorna il capitolo nella variazione
	*/

	function aggiornaCapitoloNellaVariazioneDaTabella(riga) {
		// Nothing to do?
	}

	/**
	 * Scollega il capitolo dalla variazione eliminando gli importi
	 */
	function eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga) {

		var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);
		var $overlayModale = $('#msgElimina').find('.overlay-modale').overlay('show');
		oggettoDaEliminare.uid = oggettoOriginale.uid;

		// Qualifico correttamente l'oggetto da inserire
		oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaImporti.elementoCapitoloVariazione");
		return $.postJSON("eliminaCapitoliNellaVariazione.do", oggettoDaEliminare)
			.then(function () {
				impostaDatiNegliAlert(["COR_INF_0006 - Operazione effettuata correttamente"], $('#INFORMAZIONI'));

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
	function registraImportiNellaVariazione(capitolo, annoVariazione) {
		var oggettoDaInviare = {};

		var url = "aggiungiCapitoliNellaVariazione.do";
		var ajaxParam;
		var tabella;
		var listaImporti = capitolo.listaImportiCapitolo;
		var importiCapitolo = listaImporti.find(function (imp) {
			return annoVariazione && imp.annoCompetenza == annoVariazione;
		});
		var importi = importiCapitolo || {};

		$("#divRicercaCapitolo").slideUp();
		tabella = $('#tabellaGestioneVariazioniVB').overlay('show');

		oggettoDaInviare.uid = capitolo.uid;
		oggettoDaInviare.statoOperativoElementoDiBilancio = capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "";


		oggettoDaInviare.competenza = $("#competenzaVariazione").val() || 0;
		oggettoDaInviare.residuo = $("#residuoVariazione").val() || 0;
		oggettoDaInviare.cassa = $("#cassaVariazione").val() || 0;
		oggettoDaInviare.competenzaOriginale = importi.stanziamento || 0;
		oggettoDaInviare.residuoOriginale = importi.stanziamentoResiduo || 0;
		oggettoDaInviare.cassaOriginale = importi.stanziamentoCassa || 0;


		oggettoDaInviare = qualify(oggettoDaInviare, "specificaImporti.elementoCapitoloVariazione");

		ajaxParam = $.extend(true, {}, oggettoDaInviare/*, oggettoOld*/);

		return $.postJSON(url, ajaxParam)
			.then(function (data) {
				var errori = data.errori;
				var messaggi = data.messaggi;
				// Alert errori
				var alertErrori = $("#ERRORI");
				if (impostaDatiNegliAlert(errori, alertErrori)) {
					$pulsanteRegistraImportiNellaVariazione.removeAttr('disabled');
					return;
				}

				if (messaggi && messaggi.length) {
					gestioneRichiestaProsecuzione(messaggi, url, ajaxParam, data.specificaImporti && data.specificaImporti.cassaIncongruente, data.specificaImporti && data.specificaImporti.cassaIncongruenteDopoDefinizione);
					return;
				}

				return varImp.leggiCapitoliNellaVariazione();
			}).always(tabella.overlay.bind(tabella, 'hide'));
	}

	/**
	 * Imposta l'uid del capitolo da eliminare dalla variazione.
	 *
	 * @param riga      la riga considerata
	 */

	function apriModaleEliminazioneCapitoloNellaVariazione(riga) {

		var tabellaSorgente = $("#tabellaGestioneVariazioniVB");
		var tabellaDestinazione = $("#tabellaGestioneVariazioniVB");
		var pulsanteEliminazione = $("#EDIT_elimina");

		// Eliminazione
		pulsanteEliminazione.off("click").on("click", function () {
			eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga);
		});
	}

	function ottieniTipoApplicazioneCapitolo(tipoApplicazione) {
		var tipoApplicazioneNotUndefined = tipoApplicazione || $("#HIDDEN_tipoApplicazione").val();
		return (tipoApplicazioneNotUndefined === "Previsione" || tipoApplicazioneNotUndefined === "PREVISIONE" ? "Previsione" : "Gestione");
	}

	/* ***************************************************************** ***/
	/* **********  GESTIONE ASSOCIAZIONE CAPITOLI E VARIAZIONI  ******* ***/
	/* **************************************************************** ***/

	/**
     * Controllo che i campi della ricerca del capitolo siano compilati correttamente
     * */
	function controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo, numeroArticolo, tipoCapitolo) {
		var erroriArray = [];
		// Controllo che i campi siano compilati correttamente
		if (annoCapitolo === "") {
			erroriArray.push("Il campo Anno deve essere compilato");
		}
		if (numeroCapitolo === "" || !$.isNumeric(numeroCapitolo)) {
			erroriArray.push("Il campo Capitolo deve essere compilato");
		}
		if (numeroArticolo === "" || !$.isNumeric(numeroArticolo)) {
			erroriArray.push("Il campo Articolo deve essere compilato");
		}
		if (tipoCapitolo === undefined) {
			erroriArray.push("Il tipo di capitolo deve essere selezionato");
		}
		return erroriArray;
	}


	/**
		* Effettua la ricerca del capitolo per creare una variazione.
		*/
	function cercaCapitoloNellaVariazione() {
		var prefissoOggetto = 'specificaImporti.capitolo';
		var oggettoPerChiamataAjax = {};
		//Campi hidden da leggere
		var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
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
		var erroriArray = controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo, numeroArticolo, tipoCapitolo);
		var url = 'effettuaRicercaNellaVariazioneCap' + wrapTipoCapitoloApplicazione + '.do';

		if (impostaDatiNegliAlert(erroriArray, $('#ERRORI'))) {
			return;
		}
		//i campi sono tutti stati compilati correttamente
		$('#ERRORI').slideUp();
		spinner.addClass("activated");

		//creo oggetto da passare al server e.g. capitoloDaRichiamare = "capitoloEntrataGestione"
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroUEB"] = 1;
		oggettoPerChiamataAjax.annoImporti = annoVariazione;

		return $.postJSON(url, oggettoPerChiamataAjax).then(function (data) {
			var errori = data.errori;
			var elementoCapitolo = data.specificaImporti && data.specificaImporti.elementoCapitoloVariazioneTrovatoNellaVariazione;
			if (impostaDatiNegliAlert(errori, $('#ERRORI'))) {
				return;
			}
			apriEPopolaModaleModificaImporti(elementoCapitolo);

			$('#editStanziamenti').modal('show');
		}).always(spinner.removeClass.bind(spinner, "activated"));

	}

	/**
	 * Effettua la ricerca del capitolo per creare una variazione.
	 */

	function cercaCapitolo() {
		var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
		//input selezionati dall'utente
		var tipoCapitolo = $("input[name='specificaImporti.tipoCapitolo']:checked").val();
		var annoCapitolo = $("#annoCapitolo").val();
		var numeroCapitolo = $("#numeroCapitolo").val();
		var numeroArticolo = $("#numeroArticolo").val();
		//SIAC-6705
		var statoCapitolo = $('#statoCapitolo').val();

		// gestione degli errori
		var alertErrori = $("#ERRORI");
		var erroriArray = [];

		//Wrapper per il tipo capitolo e tipo applicazione: e.g. SpesaGestione, reduced:UG
		var wrapTipoCapitoloApplicazione = tipoCapitolo + ottieniTipoApplicazioneCapitolo(tipoApplicazione);
		
		//Per rendere globale la tipologia di applicazione <--SIAC 6881 modifica dinamica tabella
		$wrapApplicazione = wrapTipoCapitoloApplicazione; 
		
		var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "P" : "G");
		
		//Wrapper per il tipo di applicazione Previsione o Gestione // <-- SIAC 
		//Questo valore verrà utilizzato per disabiluitare/abilitare le righe di inserimento componente in base al tipo di applicazione
		$wrapTipoCapitoloApplicazioneMacroTipologia = ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "P" : "G";
		
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

		// Controllo che i campi siano compilati correttamente
		if (annoCapitolo === "") {
			erroriArray.push("Il campo Anno deve essere compilato");
		}
		if (numeroCapitolo === "" || !$.isNumeric(numeroCapitolo)) {
			erroriArray.push("Il campo Capitolo deve essere compilato");
		}
		if (numeroArticolo === "" || !$.isNumeric(numeroArticolo)) {
			erroriArray.push("Il campo Articolo deve essere compilato");
		}
		if (tipoCapitolo === undefined) {
			erroriArray.push("Il tipo di capitolo deve essere selezionato");
		}

		//SIAC-6705
		if (!statoCapitolo || statoCapitolo === "") {
			erroriArray.push("Stato capitolo non selezionato");
		}

		// Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
		if (impostaDatiNegliAlert(erroriArray, alertErrori)) {
			return;
		}
		// La validazione JavaScript è andata a buon fine. Proseguire
		alertErrori.slideUp();

		//creo oggetto da passare al server e.g. capitoloDaRichiamare = "capitoloEntrataGestione"
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroUEB"] = 1;
		//SIAC-6705
		oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = statoCapitolo;
		oggettoPerChiamataAjax.annoImporti = annoVariazione;

		spinner.addClass("activated");

		// Controllo i pulsanti per inserimento ed annullamento
		return $.when(
			$.postJSON("controllaAzioniConsentiteAllUtente.do", { "specificaImporti.elementoCapitoloVariazione.tipoCapitolo": tipoArticoloEnum }),
			$.postJSON(ajaxSource, oggettoPerChiamataAjax)
		).then(function (jq1, jq2) {
			var data1 = jq1[0];
			var data2 = jq2[0];
			$datiOttenuti = jq2[0]; //<--solo per SIAC 6881 aggiornamento dinamico tabella mock
			//SIAC-6881
			
			//impostaDatiTabellaStanziamenti(data2,wrapTipoCapitoloApplicazione);
			buildTabellaRicercaCapitolo(data2, wrapTipoCapitoloApplicazione);
			buildTabellaInserisciNuovoComponente(data2, wrapTipoCapitoloApplicazione);


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

			inserisci ? buttonNuovoCapitolo.show() : buttonNuovoCapitolo.hide();
			annulla ? buttonAnnullaCapitolo.show() : buttonAnnullaCapitolo.hide();

			if (impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
			if (impostaDatiNegliAlert(messaggi, alertMessaggi)) {
				return;
			}
			if (impostaDatiNegliAlert(informazioni, alertInformazioni)) {
				return;
			}
			// Nascondo gli alert
			alertErrori.slideUp();
			alertMessaggi.slideUp();
			alertInformazioni.slideUp();

			categoriaCapitoloString = capitolo.categoriaCapitolo ? (capitolo.categoriaCapitolo.codice + "-" + capitolo.categoriaCapitolo.descrizione) : "";

			// Costruzione dei testi da injettare
			$("#annoCapitoloTrovato").html(capitolo.annoCapitolo);
			$("#numeroCapitoloArticoloTrovato").html(capitolo.numeroCapitolo + " / " + capitolo.numeroArticolo);
			$("#categoriaCapitoloTrovato").html(categoriaCapitoloString);
			$("#descrizioneCapitoloTrovato").html(capitolo.descrizione);
			$("#HIDDEN_uidCapitolo").val(capitolo.uid);
			$("#HIDDEN_statoOperativoElementoDiBilancio").val(capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "");

			// Imposto gli importi nella tabella corrispondente
			$.each(listaImporti, function () {
				impostaStanziamenti(this, tabellaStanziamenti);
			});

			// Pulisco i campi degli importi
			$("#fieldset_inserimentoVariazioneImporti :input").val("");

			$pulsanteRegistraImportiNellaVariazione.off().on(
				"click",
				function (e) {
					e.preventDefault();
					if ($pulsanteRegistraImportiNellaVariazione.attr('disabled') === 'disabled') {
						return;
					}
					$pulsanteRegistraImportiNellaVariazione.attr('disabled', 'disabled');
					$("#divRicercaCapitolo").slideUp();
					varImp.registraImportiNellaVariazione(capitolo, annoVariazione);
				}
			);

			$("#divRicercaCapitolo").slideDown();
		}).always(function () {
			spinner.removeClass("activated");
		});

	}

	/**
	 * Effettua una nuova ricerca.
	 */
	function effettuaNuovaRicerca() {

		var divRicerca = $("#divRicercaCapitolo");
		var numeroCapitolo = $("#numeroCapitolo");
		var numeroArticolo = $("#numeroArticolo");
		var tipoCapitolo = $("input[name='specificaImporti.tipoCapitolo']");

		// Nascondo il div
		divRicerca.slideUp();

		// Svuoto i campi per la ricerca
		numeroCapitolo.val("");
		numeroArticolo.val("");
		tipoCapitolo.removeProp("checked");

		// Imposto il focus sul tipoCapitolo
		tipoCapitolo.focus();
	}

	/**
	 * Redirige verso un nuovo capitolo se e solo se il tipo di capitolo &eacute; stato valorizzato.
	 */
	function redirezioneNuovoCapitolo() {

		var form = $("#formVariazioneStep3_VariazioneBilancio");
		var alertErrori = $('#ERRORI');

		var tipoCapitoloSelezionato = $("input[name='specificaImporti.tipoCapitolo']:checked").val();
		var applicazione = ottieniTipoApplicazioneCapitolo($("#HIDDEN_tipoApplicazione").val());
		var tipoCapitoloEnum;
		// Se non è stato selezionato un tipo di capitolo, annullare l'azione
		if (!tipoCapitoloSelezionato) {
			impostaDatiNegliAlert(["Il tipo di capitolo deve essere selezionato"], alertErrori);
			return;
		}
		tipoCapitoloEnum = "CAPITOLO_" + (tipoCapitoloSelezionato === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (applicazione === "Previsione" ? "PREVISIONE" : "GESTIONE");
		$('#redirezioneNuovoCapitoloButton').overlay('show');
		$.postJSON("controllaAzioniConsentiteAllUtente.do", { "specificaImporti.elementoCapitoloVariazione.tipoCapitolo": tipoCapitoloEnum })
			.then(function (data) {
				var inserisci = data.specificaImporti.utenteAbilitatoAdInserimento;
				if (!inserisci && impostaDatiNegliAlert(['COR_ERR_0044 : Operazione non consentita: inserimento Capitolo' + ' ' + tipoCapitoloSelezionato + ' ' + applicazione], alertErrori)) {
					return;
				}
				form.attr("action", "redirezioneVersoNuovoCapitolo.do").submit();

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
		var tipoCapitoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");

		var form = $('form').addClass('form-submitted');

		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.uid"] = $("#HIDDEN_uidCapitolo").val();
		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.tipoCapitolo"] = tipoCapitoloEnum;
		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.annoCapitolo"] = $("#annoCapitolo").val();
		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.numeroCapitolo"] = $("#numeroCapitolo").val();
		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.numeroArticolo"] = $("#numeroArticolo").val();
		oggettoDaInserire["specificaImporti.elementoCapitoloVariazione.statoOperativoElementoDiBilancio"] = $("#HIDDEN_statoOperativoElementoDiBilancio").val();

		return $.postJSON("annullaCapitoli.do", oggettoDaInserire)
			.then(function (data) {
				var errori = data.errori;
				var messaggi = data.messaggi;
				var informazioni = data.informazioni;
				// Alerts
				var alertErrori = $("#ERRORI");
				var alertMessaggi = $("#MESSAGGI");
				var alertInformazioni = $("#INFORMAZIONI");

				// Controllo che non vi siano errori
				if (impostaDatiNegliAlert(errori, alertErrori)) {
					return;
				}
				if (impostaDatiNegliAlert(messaggi, alertMessaggi)) {
					return;
				}
				if (impostaDatiNegliAlert(informazioni, alertInformazioni)) {
					return;
				}
				$("#divRicercaCapitolo").slideUp();
				varImp.leggiCapitoliNellaVariazione();
			}).always(form.removeClass.bind(form, 'form-submitted'));
	}
	/**
	 * Gestisce la chiamata asincrona al servizio di aggiornamento dell'anagrafica
	 * */
	function salvaEProsegui() {
		var form = $('form');
		var obj = qualify(form.serializeObject());
		$(document.body).overlay('show');
		$('#spinner_salvaEProseguiButton').addClass('activated');

		return $.postJSON('esecuzioneStep3InserimentoVariazioniImporti.do', obj)
			.then(function (data) {
				var alertErrori = $('#ERRORI');
				alertErrori.slideUp();
				if (impostaDatiNegliAlert(data.errori, alertErrori)) {
					resettaSpinnerFormSubmitted();
					return;
				}
				ottieniResponse(10, 10000);
			});
	}

	function resettaSpinnerFormSubmitted() {
		$(document.body).overlay('hide');
		$('[data-spinner-async]').removeClass('activated');
	}


	function ottieniResponse(tentativiRimanenti, timeout) {
		var url = 'inserisciVariazioneImportiAction_ottieniResponseExecuteStep3Async.do';
		return $.postJSON(url)
			.then(function (data) {
				var alertErrori = $('#ERRORI');

				alertErrori.slideUp();
				if (impostaDatiNegliAlert(data.errori, alertErrori)) {
					resettaSpinnerFormSubmitted();
					return;
				}

				if (data.isAsyncResponsePresent === undefined) {
					impostaDatiNegliAlert(['COR_ERR_0001 - Errore di sistema: impossibile ottenere la risposta asincrona.'], alertErrori);
					resettaSpinnerFormSubmitted();
					return;
				}


				if (!data.isAsyncResponsePresent) {
					if (tentativiRimanenti <= 0) {
						//se i tentativi rimanenti sono azzerati chiedo se vuoi continuare ricorsione se sì continuo, altrimenti return.
						//$('#collapseVariazioni').overlay('hide');                	
						return;
					}

					setTimeout(ottieniResponse, timeout, --tentativiRimanenti, timeout);
					return;


				}

				document.location = 'enterStep4InserimentoVariazioniImporti.do';
			});
	}

	/* ****************************************************************** ***/
	/* ****************  CALLBACK VARI DOPO CHIAMATA AL SERVIZIO  ******* ***/
	/* ****************************************************************** ***/

	/**
	 *
	 * */
	function completaInserimentoNellaVariazione(tabellaDestinazione, listaCapitoliDaInserireNellaVariazione, totaliImporti, modale) {

		var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

		if ($(tabelleGiaInDataTable).filter(tabellaDestinazione).length > 0) {
			tabellaDestinazione.dataTable().fnClearTable();
			tabellaDestinazione.dataTable().fnDestroy();
		}

		leggiCapitoliNellaVariazione();
		// Chiudo il modale
		modale.modal("hide");

	}
	/*********
	 * 
	 * 
	 * 
	 * SIAC 6881
	 */
	/**
     * Imposta il form di aggiornamento della variazione.
     *
     * @param riga la riga considerata
     */
	function apriEPopolaModaleModificaImportiDaTabella(riga) {
		var tabellaSorgente = $("#tabellaGestioneVariazioniVB");
		var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);
		var collapseRicerca = $("#collapse_ricerca");

		apriEPopolaModaleModificaImporti(datiNellaRiga);
		// Apro il modale per la modifica degli importi
		collapseRicerca.addClass("in");
		window.location.hash = "collapse_ricerca";
	}
	/******
	 * 
	 * 
	 * 
	*/

	//SIAC 6881
	function apriEPopolaModaleModificaImporti(oggettoOriginale) {

		// Dati per l'editabilità dei campi
		// Pulsante per il salvataggio
		var pulsanteSalvataggio = $("#EDIT_aggiorna");

		// Pulisco il form
		$("#editStanziamenti fieldset :input").val("");
		// Nascondo l'eventuale alert di errore già presente
		$("#ERRORI_modaleEditStanziamenti").slideUp();
		$("#idBodyModal").html("");
		if (oggettoOriginale && oggettoOriginale.componentiCapitolo && oggettoOriginale.componentiCapitolo.length > 0) {
			var htmlString = '';
			//ANNI 
			var anno0;
			var anno1;
			var anno2;
			if (oggettoOriginale.componentiCapitolo[0].listaImportiComponente && oggettoOriginale.componentiCapitolo[0].listaImportiComponente.length == 3) {
				anno0 = oggettoOriginale.componentiCapitolo[0].listaImportiComponente[0].annoCompetenza;
				anno1 = oggettoOriginale.componentiCapitolo[0].listaImportiComponente[1].annoCompetenza;
				anno2 = oggettoOriginale.componentiCapitolo[0].listaImportiComponente[2].annoCompetenza;
			}
			htmlString = htmlString + '<table class="table table-condensed table-hover table-bordered">';
			htmlString = htmlString + '<tr><th class="text-center" scope="col">Componente</th><th class="text-center" scope="col">Tipo</th><th class="text-center" scope="col">' + anno0 + '</th><th class="text-center" scope="col">' + anno1 + '</th><th class="text-center" scope="col">' + anno2 + '</th>';
			htmlString = htmlString + '</tr>';

			for (var i = 0; i < oggettoOriginale.componentiCapitolo.length; i++) {
				if (oggettoOriginale.componentiCapitolo[i].listaImportiComponente && oggettoOriginale.componentiCapitolo[i].listaImportiComponente.length == 3) {
					if (oggettoOriginale.componentiCapitolo[i].listaImportiComponente[0].stanziamento) {
						htmlString = htmlString + '<tr>';
						htmlString = htmlString + '<th rowspan="2">' + oggettoOriginale.componentiCapitolo[i].descrizione + '</th>';
						htmlString = htmlString + '<th>Stanziamento</th>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri"  style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[0].stanziamento + '" /></td>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri"  style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[1].stanziamento + '" /></td>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri"  style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[2].stanziamento + '" /></td>';
						htmlString = htmlString + '</tr>';
					}
					if (oggettoOriginale.componentiCapitolo[i].listaImportiComponente[0].impegnato) {
						htmlString = htmlString + '<tr>';
						//htmlString = htmlString +'<th></th>';
						htmlString = htmlString + '<th>Impegnato</th>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri" readonly style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[0].impegnato + '" /></td>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri" readonly style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[1].impegnato + '" /></td>';
						htmlString = htmlString + '<td style="text-align:center;"><input type="text" cssClass="lbTextSmall span6 text-right decimale soloNumeri" readonly style="text-align:right;" name="" value="' + oggettoOriginale.componentiCapitolo[i].listaImportiComponente[2].impegnato + '" /></td>';
						htmlString = htmlString + '</tr>';
					}


				}
			}

			htmlString = htmlString + '</table>';


			$("#idBodyModal").html(htmlString)
		}

		$("#titoloModaleVariazioneStanziamenti").html("Modifica Stanziamenti Capitolo " + oggettoOriginale.numeroCapitolo + " / " + oggettoOriginale.numeroArticolo);

		pulsanteSalvataggio.substituteHandler("click", function () {
			aggiornaCapitoliNellaVariazione(oggettoOriginale);
		}); // Aggiornamento

	}

	//SIAC-6881 modale inserisci modifica
	/**
	 * @author Manuel Ricevuto
	 */
	function apriModaleInserisciStanziamenti() {
		$("#idBodyInsertModal").html("");
		var pulsanteInserisci = $("#INSERT_modifica");
		var htmlString = '';
		
		//TEST COMPONENTI SCOLPITI -> QUesti, dovranno provenire da una ajax
		var listaOperazioni = [
			{ name: 'AVANZO', value: '1' },
			{ name: 'FRESCO', value: '2' },
			{ name: 'FPV BILANCIO', value: '3' },
			{ name: 'FPV CUMULATO', value: '4' },
			{ name: 'FPV APPLICATO', value: '5' }
		] //prova

		/**Costruzione dinamica della tabella alla response */
		$("#ERRORI_modaleInsertStanziamenti").slideUp();
		htmlString = htmlString + '<table class="table table-condensed table-bordered">';
		htmlString = htmlString + '<tr><th class="text-center" scope="col" width="20%">Componente</th><th class="text-center" scope="col" width="20%"></th><th class="text-center" scope="col" width="20%">' + $annoAttualeCompetenza + '</th><th class="text-center" scope="col" width="20%">' + ($annoAttualeCompetenza+1) + '</th><th class="text-center" scope="col" width="20%">' + ($annoAttualeCompetenza+2) + '</th></tr>';
		htmlString = htmlString + '<tr>';
		htmlString = htmlString + '<th rowspan="2" class="valore-stanziamento"><select id="dropdown-select-com" name="componentSelezionato"><option disabled="disabled" selected="true">-- Seleziona --</option></select></th>';
		htmlString = htmlString + '<td class="text-center">Stanziamento</td><td style="position: relative;"><input type="number" step="0.01" id="anno0s" name="anno0s" value="" class="custom-new-component"  required/></td><td style="position: relative;" ><input type="number" step="0.01" id="anno1s" name="anno1s" value="" class="custom-new-component" required/></td><td style="position: relative;"><input type="number" step="0.01" id="anno2s" name="anno2s" value="" class="custom-new-component" required /></td></tr>';
		htmlString = htmlString + '<tr id="valore-impegnato"><td class="text-center">Impegnato</td><td style="position: relative;"><input type="number" step="0.01" id="anno0i" name="anno0i" value="" class="custom-new-component" required /></td><td style="position: relative;"><input type="number" step="0.01" id="anno1i" name="anno1i" value="" class="custom-new-component" required /></td><td style="position: relative;"><input type="number" step="0.01" id="anno2i" name="anno2i" value="" class="custom-new-component" required/></td></tr>';
		htmlString = htmlString + '</table>';
	
		$("#idBodyInsertModal").html(htmlString);

		

		$("select").prop('required', true); //setto la proprietà della select a required
		pulsanteInserisci.attr('disabled', true);
		
		//VALIDAZIONI BASATE SU SELECT
		/***
		 * Se nessun componente selezionato, il pulsante inserisci viene disabilitato e tutti gli input non editabili
		 * 
		*/
		var listaInputValori = [
			$("#anno0i"),
			$("#anno0s"),
			$("#anno1i"),
			$("#anno1s"),
			$("#anno2i"),
			$("#anno2s"),
		]
		//ridondanza di codice. Suddivido gli  id input in due liste separate
		var rigaStanziamento = [$("#anno0s"), $("#anno1s"), $("#anno2s")];
		var rigaImpegnato = [$("#anno0i"), $("#anno1i"), $("#anno2i")];
		


		

		//qui viene gestito l'evento di inserimento il disabled/enabled del button di inserimento modifica in base ai valori obbligatori
		$('.custom-new-component').keyup(function () {
			var empty = false;
			$('.custom-new-component').each(function () {
				if($wrapTipoCapitoloApplicazioneMacroTipologia==='G' && $('#dropdown-select-com').val() == 1 &&  $(this).val().length == 0 && $(this).attr('id')=="anno0s"){
					empty = true;
				}else if($wrapTipoCapitoloApplicazioneMacroTipologia==='G' && 
							$('#dropdown-select-com').val() == 2 && 
							$(this).val().length == 0 && $(this).attr('id').endsWith('s')){
					empty = true;
				}else if($wrapTipoCapitoloApplicazioneMacroTipologia==='G' && 
							$('#dropdown-select-com').val() == 5 && 
							$(this).val().length == 0){
					empty = true;
				}else if($wrapTipoCapitoloApplicazioneMacroTipologia==='P' && 
							$('#dropdown-select-com').val() == 2 && 
							$(this).val().length == 0 && $(this).attr('id').endsWith('s')){
					empty = true;
				}else if($wrapTipoCapitoloApplicazioneMacroTipologia==='P' && 
							$('#dropdown-select-com').val() == 3 && 
							$(this).val().length == 0 && $(this).attr('id').endsWith('s') &&
							($(this).attr('id')=="anno1s" || $(this).attr('id')=="anno2s")){
					empty = true;

				}
			});
			if (empty) {
				pulsanteInserisci.attr('disabled', 'disabled');
			} else {
				pulsanteInserisci.removeAttr('disabled');
			}
		});


		$('#dropdown-select-com').on('change', function () {
			var selectElementVal = $(this).val();
			
			//reset delle form: possibile pensare anche ad un reset SOLO dei campi non editabili 
			listaInputValori.forEach(function (item) {
				item.val("");
			});			
			
			//in base alla tipologia si devono abilitare o disabilitare gli input; utilizzo la variabile globale -->wrapTipoCapitoloApplicazioneMacroTipologia<--
			//Effettuo uno switch case in base alla tipologia di componente selezionata dopo aver verificato la tipologia
			if($wrapTipoCapitoloApplicazioneMacroTipologia==='G'){
				switch(selectElementVal){
					case "1": //AVANZO
						disabledCustom(true);
						break;
					case "2": //FRESCO
						disabledImpegnato(true);
						break;
					case "3": //FPV BILANCIO
						disabledImpegnato(true);
						break;
					case "5": //FPV APPLICATO
						disabledImpegnato(false);
						break;
					default:
						disabledImpegnato();
						break;
				} 
			}else{
				switch(selectElementVal){					
					case "2": //FRESCO
						disabledImpegnato(true);
						break;					
					case "3": //FPV BILANCIO ad eccezione anno N
						disabledCustom(false);												
						break;	
					default:
						disabledImpegnato();
						break;
				}
			}
			pulsanteInserisci.prop('disabled', !$(this).val()); //anche se tutti gli input sono pieni da mettere il controllo

			
			//Al momento faccio il controllo sull'item 4 in quanto si tratta dell'item alla posizione 4
			//la posizione da scegliere quando la lista delle componenti disponibili arriva dal server
			/**
			 * Questo blocco di codice viene lasciato commentato per gestire una eventuale logica di inserimento del componente FPV CUMULATO (con una sola riga)
			 */
			/*

			var valoreImpegnato = $("#valore-impegnato");
			var valoreStanziamento = $("#valore-stanziamento");
			
			if (selectElement.val() == 4) {
				//in questo caso, devo settare il required a false, in quanto nascondo la riga
				$("#anno0i").removeAttr('required');
				$("#anno1i").removeAttr('required');
				$("#anno2i").removeAttr('required');
				pulsanteInserisci.prop('disabled', false);
				valoreImpegnato.hide();
				valoreStanziamento.attr('rowspan', '1');
			} else {
				//in questo caso, devo settare il required a true, in quanto nascondo la riga
				$("#anno0i").prop("required", true);
				$("#anno1i").prop("required", true);
				$("#anno2i").prop("required", true);
				pulsanteInserisci.prop('disabled', false);
				valoreImpegnato.show();
				valoreStanziamento.attr('rowspan', '2');
			}*/
			var empty = false;
			$('.custom-new-component').each(function () {
				if ($(this).val().length == 0) {
					empty = true;
					if (selectElementVal == 4 && $(this).attr('id').endsWith('i')) {
						empty = false;
					}
				}
			});
			if (empty) {
				pulsanteInserisci.attr('disabled', 'disabled');
			} else {
				pulsanteInserisci.removeAttr('disabled');
			}
		}).trigger('change');

		//FUNZIONI DI VALIDAZIONE INPUT BOX
		function disabledImpegnato(flag){ //funzione per abilitare/disabilitare inputbox in base alla scelta/tipo di applicazione
			if(flag===true){
				rigaImpegnato.forEach(function(item){ //TRUE - disabilita solo la riga impegnato
					item.prop('disabled', true);
					item.css('background-color', '#f2f2f2');
				});
				rigaStanziamento.forEach(function(item){
					item.prop('disabled', false);
					item.css('background-color', 'white');
				});
			}else if(flag===false){
				listaInputValori.forEach(function(item){ //FALSE - abilita tutti gli input 
					item.prop('disabled', false);
					item.css('background-color', 'white');
				});
			}else{
				listaInputValori.forEach(function (item) { // UNDEFINED - disabilita tutti gli input
					item.prop('disabled', true);
					item.css('background-color', '#f2f2f2');
				});
			}
		}

		//Validazioni custom in base a selezione
		function disabledCustom(flag){
			if(flag===true){
				listaInputValori.forEach(function (item, index) {
					if(index!=1){
						item.prop('disabled', true);
						item.css('background-color', '#f2f2f2');	
					}else{
						item.prop('disabled', false);
						item.css('background-color', 'white');
					}
				});

			}else if(flag===false){
				rigaImpegnato.forEach(function(item){
					item.prop('disabled', true);
					item.css('background-color', '#f2f2f2');
				});
				rigaStanziamento[0].prop('disabled', true);
				rigaStanziamento[0].css('background-color', '#f2f2f2');
				for(var i=1; i<rigaStanziamento.length; i++){
					rigaStanziamento[i].prop('disabled', false);
					rigaStanziamento[i].css('background-color', 'white');
				}
			}
		}



		$("#insertEdit").modal('show')
		//riempimento dinamico delle option della select in base alla modalità GESTIONE/PREVISIONE
		$.each(listaOperazioni, function (index, item) {
			if($wrapTipoCapitoloApplicazioneMacroTipologia==='G'){ //SE GESTIONE
				if(item.value!=="3" && item.value!=="4" ){
					$("#dropdown-select-com").append($('<option />').val(item.value).text(item.name));
				}	
			}else{ //SE PREVISIONE
				if(item.value!=="1" && item.value!=="4" &&item.value!=="5"){
					$("#dropdown-select-com").append($('<option />').val(item.value).text(item.name));
				}

			}
				
		})

		pulsanteInserisci.substituteHandler('click', function () { //Evento per il pulsante dell'inserimento. Andrà fatta una reale chiamata ajax

			var componenteCapitolo = {}

			componenteCapitolo['descrizione'] = $("#dropdown-select-com option:selected").text();
			componenteCapitolo['anno'] = '2020';

			var listaImportiComponente = [];
			var listaImporti0 = {}
			listaImporti0['annoCompetenza'] = '2020';
			listaImporti0['impegnato'] = $("#anno0i").val() || 0;
			listaImporti0['stanziamento'] = $("#anno0s").val() || 0;
			listaImportiComponente.push(listaImporti0);
			//TEST SIAC 6881 ERRORI



			var listaImporti1 = {};
			listaImporti1['annoCompetenza'] = '2021';
			listaImporti1['impegnato'] = $("#anno1i").val() || 0;
			listaImporti1['stanziamento'] = $("#anno1s").val() || 0;
			listaImportiComponente.push(listaImporti1);

			var listaImporti2 = {};
			listaImporti2['annoCompetenza'] = '2022';
			listaImporti2['impegnato'] = $("#anno2i").val() || 0;
			listaImporti2['stanziamento'] = $("#anno2s").val() || 0;
			listaImportiComponente.push(listaImporti2);
			componenteCapitolo['listaImportiComponente'] = listaImportiComponente;



			buildTabellaInserisciNuovoComponente($datiOttenuti, $wrapApplicazione, componenteCapitolo);

		})



	}




	//SIAC-6881 tabella inserisci nuovo componente
	function buildTabellaInserisciNuovoComponente(data2, wrapTipoCapitoloApplicazione, nuovoComponente) {

		$("#tabellaInserimentoNuovoComponente").html("");
		var componentiCapitolo = [];
		if (wrapTipoCapitoloApplicazione == 'UscitaGestione' && data2.capitoloUscitaGestione != null && typeof (data2.capitoloUscitaGestione) != 'undefined' &&
			data2.capitoloUscitaGestione.componentiCapitolo != null && typeof (data2.capitoloUscitaGestione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloUscitaGestione.componentiCapitolo;
		} else if (wrapTipoCapitoloApplicazione == 'UscitaPrevisione' && data2.capitoloUscitaPrevisione != null && typeof (data2.capitoloUscitaPrevisione) != 'undefined' &&
			data2.capitoloUscitaPrevisione.componentiCapitolo != null && typeof (data2.capitoloUscitaPrevisione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloUscitaPrevisione.componentiCapitolo;
		} else if (wrapTipoCapitoloApplicazione == 'EntrataGestione' && data2.capitoloEntrataGestione != null && typeof (data2.capitoloEntrataGestione) != 'undefined' &&
			data2.capitoloEntrataGestione.componentiCapitolo != null && typeof (data2.capitoloEntrataGestione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloEntrataGestione.componentiCapitolo;
		} else if (wrapTipoCapitoloApplicazione == 'EntrataPrevisione' && data2.capitoloEntrataPrevisione != null && typeof (data2.capitoloEntrataPrevisione) != 'undefined' &&
			data2.capitoloEntrataPrevisione.componentiCapitolo != null && typeof (data2.capitoloEntrataPrevisione.componentiCapitolo) != 'undefined') {
			componentiCapitolo = data2.capitoloEntrataPrevisione.componentiCapitolo;
		}
		if (nuovoComponente !== null && nuovoComponente !== undefined) {

			componentiCapitolo.push(nuovoComponente);
		}

		if (componentiCapitolo.length > 0) {
			//TEST SIAC 6881 per modifica dinamica

			var htmlString = '';
			//ANNI 
			var anno0 = 0;
			var anno1 = 0;
			var anno2 = 0;
			if (componentiCapitolo[0].listaImportiComponente && componentiCapitolo[0].listaImportiComponente.length == 3) {
				anno0 = componentiCapitolo[0].listaImportiComponente[0].annoCompetenza;
				anno1 = componentiCapitolo[0].listaImportiComponente[1].annoCompetenza;
				anno2 = componentiCapitolo[0].listaImportiComponente[2].annoCompetenza;
			}
			//HEAD
			htmlString = htmlString + '<tr><th >&nbsp;</th><th >&nbsp;</th><th  class="text-right">' + anno0 + '</th><th  class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th></tr>';
			//COMPONENTI
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli"><a id="competenzaTotaleInserimento" href="#tabellaComponentiInserimento"  class="disabled" >Competenza</a></th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';

			//htmlString = htmlString +'<tbody>';
			for (var i = 0; i < componentiCapitolo.length; i++) {
				if (componentiCapitolo[i].listaImportiComponente && componentiCapitolo[i].listaImportiComponente.length == 3) {

					var rowspan = 1;
					if (componentiCapitolo[i].listaImportiComponente[0].impegnato) {
						rowspan = 2;
					}

					if (componentiCapitolo[i].listaImportiComponente[0].stanziamento) {
						htmlString = htmlString + '<tr class="componentiCompetenzaModIns">';
						htmlString = htmlString + '<td rowspan="' + rowspan + '">' + componentiCapitolo[i].descrizione + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">Stanziamento</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[0].stanziamento + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[1].stanziamento + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[2].stanziamento + '</td>';
						htmlString = htmlString + '</tr>';
					}

					if (componentiCapitolo[i].listaImportiComponente[0].impegnato) {
						htmlString = htmlString + '<tr class="componentiCompetenzaModIns">';
						htmlString = htmlString + '<td style="text-align:center;">Impegnato</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[0].impegnato + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[1].impegnato + '</td>';
						htmlString = htmlString + '<td style="text-align:center;">' + componentiCapitolo[i].listaImportiComponente[2].impegnato + '</td>';
						htmlString = htmlString + '</tr>';
					}

				}

			}

			//htmlString = htmlString +'</tbody>';
			//RESIDUI
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli">Residuo</th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			//CASSA
			htmlString = htmlString + '<tr><th rowspan = "2" class="stanziamenti-titoli">Cassa</th>';
			htmlString = htmlString + '<td class="text-center">Stanziamento</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';
			htmlString = htmlString + '<tr><td class="text-center">Impegnato</td><td class="text-right"></td><td class="text-right"></td><td class="text-right"></td></tr>';

			$("#tabellaInserimentoNuovoComponente").html(htmlString);

			//chiudi modale se aperta test siac 6881
			$("#insertEdit").modal('hide')
			$(".componentiCompetenzaModIns").hide();
			$("#competenzaTotaleInserimento").click(function () {
				$(".componentiCompetenzaModIns").slideToggle();
			});

		}





		/*pulsanteSalvataggio.substituteHandler("click", function() {
			aggiornaCapitoliNellaVariazione(oggettoOriginale);
		}); // Aggiornamento*/

	}
	//////////////////////////////////////////////////






	/**
	 * **/
	function completaRegistrazione(tabelleGiaInDataTable, tabella, listaImporti, totaliImporti) {
		// Se la tabella è già inizializzata, la pulisco e la distruggo

		if ($(tabelleGiaInDataTable).filter(tabella).length > 0) {
			tabella.dataTable().fnClearTable();
			tabella.dataTable().fnDestroy();
		}

		// Imposto i dati nella tabella
		impostaTotaliImporti(totaliImporti);

		$("#divRicercaCapitolo").slideUp();
	}

	/**
	 * Callback per la chiamata AJAX nell'inserimento per una nuova variazione.
	 */

	function postChiamataAjaxNellaVariazione(data, textStatus, jqXHR, tabellaDestinazione, idModale, idAlert) {

		var errori = data.errori;
		var totaliImporti = data.specificaImporti.elementoImportiVariazione;
		var alertErrori = (idAlert !== undefined ? $("#" + idAlert) : undefined);

		// Controllo gli eventuali errori, messaggi, informazioni
		if (idAlert !== undefined) {
			// Nascondo gli alert
			alertErrori.slideUp();
			if (impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
		}

		impostaTotaliImporti(totaliImporti);
		// Chiudo il modale
		$("#" + idModale).modal("hide");

	}



	return varImp;


}(Variazioni || {}));

$(function () {
	//lego gli handlers
	$("#pulsanteRicercaCapitolo").on("click", VariazioniImporti.cercaCapitolo);
	//SIAC-5016
	$("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", VariazioniImporti.cercaCapitoloNellaVariazione);
	$("#redirezioneNuovoCapitoloButton").on("click", VariazioniImporti.redirezioneNuovoCapitolo);
	$("#annullaCapitoloButton").on("click", VariazioniImporti.annullaCapitolo);
	$("#competenzaVariazione").on("blur", VariazioniImporti.impostaValoreCassaSeApplicabile).gestioneDeiDecimali();
	$("#nuovaRicerca").on("click", VariazioniImporti.effettuaNuovaRicerca);
	$("#salvaEProseguiButton").on("click", VariazioniImporti.salvaEProsegui);

	VariazioniImporti.leggiCapitoliNellaVariazione();

	$("form").substituteHandler("reset", function () {
		document.location = 'enterStep3InserimentoVariazioniImportiSenzaUEB.do';
	});

	// SIAC-5016
	$('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'inserisciVariazioneImporti_download.do', false));
	$('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'inserisciVariazioneImporti_download.do', true));


	//SIAC-6881
	$("#componentiCompetenza").hide();
	$("#competenzaTotale").click(function () {
		$("#componentiCompetenza").slideToggle();
	});

	$("#button_inserisciModifica").click(function () {
		VariazioniImporti.apriModaleInserisciStanziamenti();
	})

});
