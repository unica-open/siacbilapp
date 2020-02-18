/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** Inserisci Stanziamenti per Capitolo Di Uscita Previsione ****
*************************************************
*/
var Variazioni = Variazioni || {};
var VariazioniImporti = (function (varImp) {
	"use strict";
	
	var $pulsanteRegistraImportiNellaVariazione = $("#button_registraVariazione");
	var $datiOttenuti = ""; //<-- solo per SIAC 6881 aggiornamento dinamico tabelle
	var $wrapApplicazione = ""; //<-- solo per SIAC 6881 aggiornamento dinamico tabelle
	var $wrapTipoCapitoloApplicazioneMacroTipologia;
	var $annoAttualeCompetenza;
	varImp.apriModaleInserisciStanziamenti = apriModaleInserisciStanziamenti;
	
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
	
	return varImp;
	
	
	}(Variazioni || {}));


/* Document ready */
$(
    function () {
       $("#button_inserisciModifica").click(function () {
   		VariazioniImporti.apriModaleInserisciStanziamenti();
   	})
    }
);