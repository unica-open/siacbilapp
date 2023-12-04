/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** GESTIONE COMPONENTE IMPORTI CAPITOLO ****
*************************************************
*/
var Variazioni = Variazioni || {};
var VariazioniImporti = (function (varImp) {
	"use strict";
	varImp.caricaTabella = caricaTabella;
	varImp.apriModaleComponenti = apriModaleComponenti;
	varImp.apriModaleComponentiInserimento = apriModaleComponentiInserimento;
	varImp.eliminaComponentiImportiCapitolo = eliminaComponentiImportiCapitolo;
	varImp.dataObjmodal = {};
	varImp.listaTipoComponentiCapitoli = [];
	varImp.typeModifica = 0;
	varImp.typeInserisci= 1;
	varImp.typeComponentiCapitolo= 2;
	varImp.typeResiduo= 3;
	varImp.typeCassa= 4;
	varImp.annoEsercizioInt=0;
	varImp.tipiCompMap={};
	//SIAC-7349 - Start - MR - 22/04/2020 - Patch Nullpointer Exception per modifica stanziamenti anni precedenti
	varImp.enabledInsertModifica = true;
	//SIAC-7349
	/**
	 * CARICAMENTO TABELLA
	 */
	function caricaTabella (){
		var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
		overlay.overlay('show');
		var oggettoDaInviare = {};
		$.postJSON(
			"aggiornaComponenteImportoCapitolo_ricerca.do",
			oggettoDaInviare,
			function(data) {
				//SIAC-7228
				var errori = data.errori;
				var messaggi = data.messaggi;
				var informazioni = data.informazioni;
				var alertErrori = $('#ERRORI');
				var alertMessaggi = $("#MESSAGGI");
				var alertInformazioni = $("#INFORMAZIONI");
				// Controllo gli eventuali errori, messaggi, informazioni
				if(impostaDatiNegliAlert(errori, alertErrori)) {return;	}
				if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {return;	}
				if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {return;	}
				//BUILDTABLE
				//SE LA LISTA NON HA ELEMENTI COMPONENTI NON CARICHIAMO NULLA 
				if(data.competenzaComponenti && data.competenzaComponenti.length>=3){
					buildTabellaInserisciNuovoComponente(data);
				}
			}
		).always(overlay.overlay.bind(overlay, 'hide'));
	}

	
	/**
	 * Creazione Mappa tipo componente per capitolo
	 */
	function buildTipoCompMap(){
		if(varImp.listaTipoComponentiCapitoli && varImp.listaTipoComponentiCapitoli.length>0){
			for(var z=0;z<varImp.listaTipoComponentiCapitoli.length;z++){
				varImp.tipiCompMap[varImp.listaTipoComponentiCapitoli[z].uid] =varImp.listaTipoComponentiCapitoli[z]; 
			}
		}
	}
	
	
	/**
	 * BUILD CAMPI TABELLA
	 */
	function buildTabellaInserisciNuovoComponente(data) {
		varImp.dataObjmodal = data;
//		varImp.listaTipoComponentiCapitoli = data.listaTipoComponenti;
//		buildTipoCompMap();
		$("#tabellaStanziamentiTotaliComponenti").html("");
		var htmlString = '';
		varImp.annoEsercizioInt= data.annoEsercizioInt;
		var anno0=data.annoEsercizioInt;
		var annoPrece=anno0-1;
		var anno1=anno0+1;
		var anno2=anno0+2;
		//HEAD
		htmlString = htmlString + '<tr>';
		htmlString = htmlString + '<th >&nbsp;</th><th >&nbsp;</th><th class="text-right">'+ annoPrece +'</th><th class="text-right">Residui '+anno0+'</th><th  class="text-right">' + anno0 + '</th> ';
		htmlString = htmlString + '<th class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th><th class="text-right"> >' + anno2 + '</th>';
		htmlString = htmlString + '<th >&nbsp;</th>';
		htmlString = htmlString + '</tr>';
		//COMPETENZA
		if(data.competenzaComponenti && data.competenzaComponenti.length>=3){
			for(var i=0;i<data.competenzaComponenti.length;i++){
				// SIAC-7185
				if(i==0){
					htmlString = htmlString + '<tr  class="componentiRowFirst" >';
					htmlString = htmlString + '<th rowspan = "3" class="stanziamenti-titoli"><a id="competenzaTotaleInserimento" href="#tabellaComponentiInserimento"  class="disabled" >Competenza</a></th>';
				}else{
					htmlString = htmlString + '<tr  class="componentiRowOther" >';
				}
				//SIAC-7349 - SR200 - Start - MR - 20/04/2020 celle vuote per anni prec e anni succ
				if(i==2){
					htmlString = htmlString + '<td class="text-center">'+data.competenzaComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right"></td><td class="text-right"></td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno0.importo).formatMoney()+'</td>';
					htmlString = htmlString + '<td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right"></td>';
				}else{
					htmlString = htmlString + '<td class="text-center">'+data.competenzaComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno0.importo).formatMoney()+'</td>';
					htmlString = htmlString + '<td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnniSucc.importo).formatMoney()+'</td>';
				}
				//SIAC-7349 End
				if(i==0){
					htmlString = htmlString + '<td rowspan = "3" ></td>';
				}
				htmlString = htmlString + '</tr>';
			}
		}
		//COMPONENTI COMPETENZA
		if(data.importiComponentiCapitolo && data.importiComponentiCapitolo.length>0){
			var oldTipoCom='';
			var rowspan = 1;
			var idrowspan ='';
			var rowspanmap ={};
			for (var i = 0; i < data.importiComponentiCapitolo.length; i++) {
				//SIAC-7159 SIAC-7185
				if(data.importiComponentiCapitolo[i].propostaDefault==true){
					if(data.importiComponentiCapitolo[i].tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'){
						htmlString = htmlString + '<tr class="componentiCompetenzaRow previsione-default">';
					}else{
						htmlString = htmlString + '<tr class="componentiCompetenzaRow">';
					}
				}else{
					if(data.importiComponentiCapitolo[i].tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'){
						htmlString = htmlString + '<tr class="componentiCompetenzaRow previsione-default-stanziamento">';
					}else{
						htmlString = htmlString + '<tr class="componentiCompetenzaRow">';
					}
				}
				
				 
					
					if(oldTipoCom!=data.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo.uid){
						rowspan=1;
						htmlString = htmlString + '<td class="componenti-competenza" id="tipoComTD'+i+'" rowspan="' + rowspan + '">' + data.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo.descrizione + '</td>';
						idrowspan = 'tipoComTD'+i;
					}
					htmlString = htmlString + '<td  style="text-align:center;">'+data.importiComponentiCapitolo[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnnoPrec.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioResidui.importo).formatMoney()+ '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno0.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno1.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno2.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnniSucc.importo).formatMoney() + '</td>';
					if(oldTipoCom!=data.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo.uid){
						htmlString = htmlString + '<td style="text-align:left;" rowspan="' + rowspan + '">';
						htmlString = htmlString + '<a href="#editStanziamenti" title="modifica gli importi" role="button" onclick="VariazioniImporti.apriModaleComponenti('+i+','+anno0+', '+varImp.typeComponentiCapitolo+');" data-toggle="modal">';
						htmlString = htmlString + '<i class="icon-pencil icon-2x"><span class="nascosto">modifica</span></i></a>';
						if(data.importiComponentiCapitolo[i].propostaDefault!=true && data.importiComponentiCapitolo[i].nonEliminabile!=true){

							htmlString = htmlString + '<a href="#msgElimina" title="elimina" role="button" onclick="VariazioniImporti.eliminaComponentiImportiCapitolo('+i+');" data-toggle="modal" style="padding-left:15px;">';
							htmlString = htmlString + '<i class="icon-trash icon-2x"><span class="nascosto">elimina</span></i></a>';
							htmlString = htmlString + '</td>';
						}
					}
					
					htmlString = htmlString + '</tr>';
					if(oldTipoCom==data.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo.uid){
						rowspan++;
						rowspanmap[idrowspan]=rowspan;
					}
					
					oldTipoCom=data.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo.uid;
			}
		}
		//RESIDUI
		if(data.cassaComponenti && data.residuoComponenti.length>=2){
			for(var i=0;i<data.residuoComponenti.length;i++){
				//SIAC-7185
				if(i==0){
					htmlString = htmlString + '<tr  class="componentiRowFirst"  >';
					htmlString = htmlString + '<th rowspan = "2" class="stanziamenti-titoli">Residuo</th>';
				}else{
					htmlString = htmlString + '<tr  class="componentiRowOther" >';
				}
				//SIAC-7349 - Start - SR200 - MR/GS - 07/04/2020 - Corretta valorizzazione residuo presunto 
				htmlString = htmlString + '<td class="text-center">'+data.residuoComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+'0,00'+'</td>';
				//SIAC-7349 - End
				htmlString = htmlString + '<td class="text-right">'+(data.residuoComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnniSucc.importo).formatMoney()+'</td>';
				if(i==0){
					htmlString = htmlString + '<td rowspan = "2" >';
					if(data.fromPage == 'MODIFY'){
						htmlString = htmlString + '<a href="#editStanziamenti" title="modifica gli importi" role="button" onclick="VariazioniImporti.apriModaleComponenti('+i+','+anno0+','+varImp.typeResiduo+');"  data-toggle="modal">';
						htmlString = htmlString + '<i class="icon-pencil icon-2x"><span class="nascosto">modifica</span></i></a>';
					}
					htmlString = htmlString + '</td>';
				}
				htmlString = htmlString + '</tr>';
			}
		}
		//CASSA
		if(data.cassaComponenti && data.cassaComponenti.length>=2){
			for(var i=0;i<data.cassaComponenti.length;i++){
				//SIAC-7185
				if(i==0){
					htmlString = htmlString + '<tr  class="componentiRowFirst"  >';
					htmlString = htmlString + '<th rowspan = "2" class="stanziamenti-titoli">Cassa</th>';
				}else{
					htmlString = htmlString + '<tr  class="componentiRowOther"  >';
				}
				htmlString = htmlString + '<td class="text-center">'+data.cassaComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.cassaComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.cassaComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+(data.cassaComponenti[i].dettaglioAnno0.importo).formatMoney()+'</td>';
				htmlString = htmlString + '<td class="text-right">'+(data.cassaComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.cassaComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right">'+(data.cassaComponenti[i].dettaglioAnniSucc.importo).formatMoney()+'</td>';
				if(i==0){
					htmlString = htmlString + '<td rowspan = "2" >';
					htmlString = htmlString + '<a href="#editStanziamenti" title="modifica gli importi" onclick="VariazioniImporti.apriModaleComponenti('+i+','+anno0+', '+varImp.typeCassa+');"  role="button" data-toggle="modal">';
					htmlString = htmlString + '<i class="icon-pencil icon-2x"><span class="nascosto">modifica</span></i></a>';
					htmlString = htmlString + '</td>';
				}
				htmlString = htmlString + '</tr>';
			}
		}
		$("#tabellaStanziamentiTotaliComponenti").html(htmlString);
		//Impostazione rowspan
		for (var k in rowspanmap) {
			$("#"+k).attr('rowspan', rowspanmap[k]);
		}
		//Settaggio ancor sezione competenza
		$(".componentiCompetenzaRow").hide();
		$("#competenzaTotaleInserimento").eventPreventDefault('click', function(){
		    $(".componentiCompetenzaRow").toggle()
		});
	}

	/**
	 * HEAD TABELLA
	 */
	function buildHeadTable(htmlString, annoEsercizioInt){
		var anno0=annoEsercizioInt;
		var annoPrece=anno0-1;
		var anno1=anno0+1;
		var anno2=anno0+2;
		htmlString = htmlString + '<tr>';
		htmlString = htmlString + '<th >Componente</th><th class="text-right">&nbsp;</th><th class="text-right">'+ annoPrece +'</th><th class="text-right">Residui '+anno0+'</th><th  class="text-right">' + anno0 + '</th> ';
		htmlString = htmlString + '<th class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th><th class="text-right"> >' + anno2 + '</th>';
		htmlString = htmlString + '</tr>';
		return htmlString ;
	}
	
	/**
	 * SELECT TIPO COMPONENTI IMPORTO
	 */
	function buiildSelectComponent(){
		var htmlSelect='';
		if(varImp.listaTipoComponentiCapitoli.length>0){
			htmlSelect ='<select id="dropdown-select-com" name="componentSelezionato" required="">';
			htmlSelect = htmlSelect + '<option disabled="disabled" selected="true">-- Seleziona --</option>';
			for(var i=0; i<varImp.listaTipoComponentiCapitoli.length;i++){
				htmlSelect= htmlSelect +'<option value="'+varImp.listaTipoComponentiCapitoli[i].uid+'">'+varImp.listaTipoComponentiCapitoli[i].descrizione+'</option>';
			}
			htmlSelect =htmlSelect +'</select>';
		}
		return htmlSelect;
	}
	
	/**
	 * Righe delle componenti importo del capitolo
	 */
	function buildBodyRow(htmlString,i, arr){
		
		var readonlyAnnoPrec = (arr[i].dettaglioAnnoPrec.editabile) ? '' : 'readonly';
		var readonlyResidui = (arr[i].dettaglioResidui.editabile) ? '' : 'readonly';
		var readonlyAnno0 = (arr[i].dettaglioAnno0.editabile) ? '' : 'readonly';
		var readonlyAnno1 = (arr[i].dettaglioAnno1.editabile) ? '' : 'readonly';
		var readonlyAnno2 = (arr[i].dettaglioAnno2.editabile) ? '' : 'readonly';
		var readonlyAnniSucc = (arr[i].dettaglioAnniSucc.editabile) ? '' : 'readonly';
		//SIAC-7349 - Start - MR - 22/04/2020 - Patch Nullpointer Exception per modifica stanziamenti anni precedenti
		if(i%2==0 && readonlyAnnoPrec!='' && readonlyResidui !='' && readonlyAnno0!='' && readonlyAnno1!='' && readonlyAnno2!='' && readonlyAnniSucc!=''){			
			varImp.enabledInsertModifica = false;	
		}
		//SIAC-7349 End

		// SIAC-7131 + SIAC-7132
		htmlString += '<td style="position:relative"><input id="detAnnoPrecImp'+i+'" type="text" '+readonlyAnnoPrec+' name="" value="'+formatMoney(arr[i].dettaglioAnnoPrec.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative;width: 30px;"><input id="detAnnoResImp'+i+'" type="text" '+readonlyResidui+' name="" value="'+formatMoney(arr[i].dettaglioResidui.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno0Imp'+i+'" type="text" '+readonlyAnno0+' name="" value="'+formatMoney(arr[i].dettaglioAnno0.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno1Imp'+i+'" type="text" '+readonlyAnno1+' name="" value="'+formatMoney(arr[i].dettaglioAnno1.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno2Imp'+i+'" type="text" '+readonlyAnno2+' name="" value="'+formatMoney(arr[i].dettaglioAnno2.importo)+'" class="custom-new-component soloNumeri text-right" required/>/td>';
		htmlString += '<td style="position:relative"><input id="detAnnoSuccImp'+i+'" type="text" '+readonlyAnniSucc+' name="" value="'+formatMoney(arr[i].dettaglioAnniSucc.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		return htmlString;
	}

	//SIAC-7349 - Start - MR - 22/04/2020 - Patch Nullpointer Exception per modifica stanziamenti anni precedenti
	//Metodo per costruire il body della riga riferita alla cassa
	//duplicato per introdurre regressioni
	function buildBodyRowCassa(htmlString,i, arr){
		
		var readonlyAnnoPrec = (arr[i].dettaglioAnnoPrec.editabile) ? '' : 'readonly';
		var readonlyResidui = (arr[i].dettaglioResidui.editabile) ? '' : 'readonly';
		var readonlyAnno0 = (arr[i].dettaglioAnno0.editabile) ? '' : 'readonly';
		var readonlyAnno1 = (arr[i].dettaglioAnno1.editabile) ? '' : 'readonly';
		var readonlyAnno2 = (arr[i].dettaglioAnno2.editabile) ? '' : 'readonly';
		var readonlyAnniSucc = (arr[i].dettaglioAnniSucc.editabile) ? '' : 'readonly';

		// SIAC-7131 + SIAC-7132
		htmlString += '<td style="position:relative"><input id="detAnnoPrecImp'+i+'" type="text" '+readonlyAnnoPrec+' name="" value="'+formatMoney(arr[i].dettaglioAnnoPrec.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative;width: 30px;"><input id="detAnnoResImp'+i+'" type="text" '+readonlyResidui+' name="" value="'+formatMoney(arr[i].dettaglioResidui.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno0Imp'+i+'" type="text" '+readonlyAnno0+' name="" value="'+formatMoney(arr[i].dettaglioAnno0.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno1Imp'+i+'" type="text" '+readonlyAnno1+' name="" value="'+formatMoney(arr[i].dettaglioAnno1.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position:relative"><input id="detAnno2Imp'+i+'" type="text" '+readonlyAnno2+' name="" value="'+formatMoney(arr[i].dettaglioAnno2.importo)+'" class="custom-new-component soloNumeri text-right" required/>/td>';
		htmlString += '<td style="position:relative"><input id="detAnnoSuccImp'+i+'" type="text" '+readonlyAnniSucc+' name="" value="'+formatMoney(arr[i].dettaglioAnniSucc.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		return htmlString;
	}
	
	
	/**
	 * Righe delle componenti importo del capitolo solo per i residui
	 * Sulla colonna residuo mettiamo il valore dell Anno N
	 * mentre in questo caso mettiamo il valore 0 
	 */
	function buildBodyRowResidui(htmlString,i, arr){
		var readonlyAnnoPrec = (arr[i].dettaglioAnnoPrec.editabile) ? '' : 'readonly';
		//var readonlyResidui = (arr[i].dettaglioResidui.editabile) ? '' : 'readonly';
		var readonlyAnno0 = (arr[i].dettaglioAnno0.editabile) ? '' : 'readonly';
		var readonlyAnno1 = (arr[i].dettaglioAnno1.editabile) ? '' : 'readonly';
		var readonlyAnno2 = (arr[i].dettaglioAnno2.editabile) ? '' : 'readonly';
		var readonlyAnniSucc = (arr[i].dettaglioAnniSucc.editabile) ? '' : 'readonly';
		// SIAC-7131 + SIAC-7132
		htmlString += '<td style="position: relative;"><input id="detAnnoPrecImp'+i+'" type="text" '+readonlyAnnoPrec+' name="" value="'+formatMoney(arr[i].dettaglioAnnoPrec.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		//SIAC-7349 - Start - SR200 - MR - 15/04/2020 - Mappo il residuo effettivo finale, in quanto non presente nel primo item editabile
		if(i==1){
			htmlString += '<td style="position: relative;"><input id="detAnno0Imp'+i+'" type="text" readonly name="" value="'+formatMoney(arr[i].dettaglioResidui.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		}else{
			htmlString += '<td style="position: relative;"><input id="detAnno0Imp'+i+'" type="text" '+readonlyAnno0+' name="" value="'+formatMoney(arr[i].dettaglioAnno0.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		}
		//SIAC-7349 - End
		htmlString += '<td style="position: relative;"><input id="'+i+'" type="text" readonly name="" value="'+formatMoney(0)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position: relative;"><input id="detAnno1Imp'+i+'" type="text" '+readonlyAnno1+' name="" value="'+formatMoney(arr[i].dettaglioAnno1.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		htmlString += '<td style="position: relative;"><input id="detAnno2Imp'+i+'" type="text" '+readonlyAnno2+' name="" value="'+formatMoney(arr[i].dettaglioAnno2.importo)+'" class="custom-new-component soloNumeri text-right" required/>/td>';
		htmlString += '<td style="position: relative;"><input id="detAnnoSuccImp'+i+'" type="text" '+readonlyAnniSucc+' name="" value="'+formatMoney(arr[i].dettaglioAnniSucc.importo)+'" class="custom-new-component soloNumeri text-right" required/></td>';
		return htmlString;
	}
	
	
	/**
	 * MODALE IN INSERIMENTO
	 * 
	 */
	function apriModaleComponentiInserimento(){
		$('#ERRORI_modaleInsertStanziamenti').hide();
		$('#descrizioneErrore').text('');
		$("#INSERT_modifica").html('conferma')
		 $("#INSERT_modifica").prop("disabled",true);
		//LOAD SELECT
		$.postJSON(
			'aggiornaComponenteImportoCapitolo_ricercaTipoComponenti.do',
			{},
			function(data) {
				var errori = data.errori;
				var messaggi = data.messaggi;
				var informazioni = data.informazioni;
				var alertErrori = $('#ERRORI');
				var alertMessaggi = $("#MESSAGGI");
				var alertInformazioni = $("#INFORMAZIONI");
				if(impostaDatiNegliAlert(errori, alertErrori)) {return;	}
				if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {return;	}
				if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {return;}
				
				varImp.listaTipoComponentiCapitoli = data.listaTipoComponenti;//lista tipo componenti del customJson
				buildTipoCompMap();
				$('#ERRORI_modaleInsertStanziamenti').hide();
				$('#descrizioneErrore').text('');
				$("#idBodyInsertModal").html("");
				var htmlString = '';
				//RIGA CON LA SELECT
				htmlString = htmlString + buiildSelectComponent();
				htmlString = htmlString + '<div id="contentDiv" style="margin-top:20px;"></div>';
				
				$("#idBodyInsertModal").html(htmlString);
				
				$('#componenteModel').modal('show');
				
				$('#dropdown-select-com').change(function() {
					var listaDettagli = varImp.tipiCompMap[$('#dropdown-select-com').val()].listaDettaglioComponenteImportiCapitolo;
					if(listaDettagli && listaDettagli.length>0){
						//CREIAMO OGGETTO DA INVIARE IN TABELLA
						$("#INSERT_modifica").prop("disabled",false);
						$('#contentDiv').html(buildTabellaInserimentoModale(data.annoEsercizioInt, listaDettagli));
						// SIAC-7132
						$("input.soloNumeri", '#contentDiv').allowedChars({
						    numeric: true
						});
						$("input.decimale", '#contentDiv').gestioneDeiDecimali();
					}
				});
				
				//NESSUNA LISTA
				if(varImp.listaTipoComponentiCapitoli.length==0){
					$('#ERRORI_modaleInsertStanziamenti').show();
					$('#descrizioneErrore').text('Nessun componente da associare');
					 $("#INSERT_modifica").prop("disabled",true);
				}
				
				
			}
		);
		
		//SUBMIT INSERT
		var pulsanteInserisci= $("#INSERT_modifica");
		pulsanteInserisci.off("click").on("click", function () {
			
			if(!($('#detAnno0Imp'+0).val() && $('#detAnno1Imp'+0).val() && $('#detAnno2Imp'+0).val())){
				$('#ERRORI_modaleInsertStanziamenti').show();
				$('#descrizioneErrore').text('Inserire tutti gli importi nel formato corretto');
			}else{
				var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
				overlay.overlay('show');
				/**
				 * INVIAMO SOLO LA RIGA INDEX DOVE CI SARA' lO STANZIAMENTO
				 * Non bellissimo...non riesco a sottometere tutto l'oggetto
				 */
				var index =0;
				var oggettoDaInviare = {};
				var actionToDo = 'aggiornaComponenteImportoCapitolo_inserisciComponenti.do';
				// SIAC-7131
				oggettoDaInviare.importoStanziamentoAnno0 = parseLocalNum($('#detAnno0Imp'+index).val());
				oggettoDaInviare.importoStanziamentoAnno1 = parseLocalNum($('#detAnno1Imp'+index).val());
				oggettoDaInviare.importoStanziamentoAnno2 = parseLocalNum($('#detAnno2Imp'+index).val());
				oggettoDaInviare.uidTipoComponenteImportiCapitolo = varImp.tipiCompMap[$('#dropdown-select-com').val()].uid;
				
				//CHIUDIAMO MODALE
				$.postJSON(
					actionToDo,
					qualify(oggettoDaInviare),
					function(data) {
						if(data.errori && data.errori.length>0){
							$('#ERRORI_modaleInsertStanziamenti').show();
							$('#descrizioneErrore').text(data.errori[0].codice + ' - '+data.errori[0].descrizione);
							
						}else{
							//BUILDTABLE
							$('#componenteModel').modal('hide');
							buildTabellaInserisciNuovoComponente(data);
						}
					}
				).always(overlay.overlay.bind(overlay, 'hide'));
			}
		});
		
	}

	/**
	 * BUILD TAbella per le componenti da inserire
	 */
	function buildTabellaInserimentoModale(annoEsercizioInt, listaDettagli){
		//HEAD
		
		var htmlString='';
		htmlString = htmlString + '<table class="table table-condensed table-bordered">';
		var anno0=annoEsercizioInt;
		var annoPrece=anno0-1;
		var anno1=anno0+1;
		var anno2=anno0+2;
		htmlString = htmlString + '<tr>';
		htmlString = htmlString + '<th class="text-right"></th><th class="text-right">'+ annoPrece +'</th><th class="text-right">Residui '+anno0+'</th><th  class="text-right">' + anno0 + '</th> ';
		htmlString = htmlString + '<th class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th><th class="text-right"> >' + anno2 + '</th>';
		htmlString = htmlString + '</tr>';
		//COMPONENTI CAPITOLO
		for(var z=0; z<listaDettagli.length; z++){
			htmlString += '<tr>';
			var readonlyAnnoPrec =  'readonly';
			var readonlyResidui =  'readonly';
//			var readonlyAnno0 = (z==0) ? '' : 'readonly';
//			var readonlyAnno1 = (z==0) ? '' : 'readonly';
//			var readonlyAnno2 = (z==0) ? '' : 'readonly';
			var readonlyAnno0 = (listaDettagli[z].editabile) ? '' : 'readonly';
			var readonlyAnno1 = (listaDettagli[z].editabile) ? '' : 'readonly';
			var readonlyAnno2 = (listaDettagli[z].editabile) ? '' : 'readonly';
			var readonlyAnniSucc =  'readonly';
			htmlString += '<td class="text-center" style="position: relative;">'+listaDettagli[z].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td>';
			htmlString += '<td style="position: relative;"><input id="detAnnoPrecImp'+z+'" type="text" '+readonlyAnnoPrec+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/></td>';
			htmlString += '<td style="position: relative;"><input id="detAnnoResImp'+z+'" type="text" '+readonlyResidui+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/></td>';
			htmlString += '<td style="position: relative;"><input id="detAnno0Imp'+z+'" type="text" '+readonlyAnno0+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/></td>';
			htmlString += '<td style="position: relative;"><input id="detAnno1Imp'+z+'" type="text" '+readonlyAnno1+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/></td>';
			htmlString += '<td style="position: relative;"><input id="detAnno2Imp'+z+'" type="text" '+readonlyAnno2+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/>/td>';
			htmlString += '<td style="position: relative;"><input id="detAnnoSuccImp'+z+'" type="text" '+readonlyAnniSucc+' name="" value="" class="custom-new-component soloNumeri decimale text-right" required/></td>';
			htmlString += '</tr>';
		}
		htmlString = htmlString + '</table>';






		return htmlString;
	}

	
	
	
	/**
	 * APERTURA MODALE IN MODIFICA 
	 */
	function apriModaleComponenti(index,annoEsercizioInt, tipoRiga){
		$('#ERRORI_modaleInsertStanziamenti').hide();
		$('#descrizioneErrore').text('');
		$("#INSERT_modifica").html('inserisci modifica')
		$("#INSERT_modifica").prop("disabled",false);
		var iccArr = varImp.dataObjmodal.importiComponentiCapitolo;
		var residuoArr = varImp.dataObjmodal.residuoComponenti;
		var cassaArr = varImp.dataObjmodal.cassaComponenti;
		var competenzaArr = varImp.dataObjmodal.competenzaComponenti;
		var importoComponentePerAnno = varImp.dataObjmodal.importiComponentiCapitolo;
		var uidTipoComponenteCapitolo = varImp.dataObjmodal;
		var isPropostaDiDefault = false;
		$("#idBodyInsertModal").html("");
		var htmlString = '';
		var rowspan = 0;
		//HEAD
		htmlString = htmlString + '<table class="table table-condensed table-bordered">';
		htmlString = htmlString + buildHeadTable(htmlString, annoEsercizioInt);
		//COMPONENTI CAPITOLO
		if(tipoRiga ==varImp.typeComponentiCapitolo){
			var codiceComponente=iccArr[index].tipoComponenteImportiCapitolo.uid;
			//7156
			isPropostaDiDefault = iccArr[index].propostaDefault || false;
			uidTipoComponenteCapitolo = codiceComponente;
			for(var i=index; i< iccArr.length; i++){
				if(codiceComponente==iccArr[i].tipoComponenteImportiCapitolo.uid ){
					htmlString = htmlString + '<tr>';
					if(i==index){
						htmlString = htmlString + '<td class="componenti-competenza" id="tipoComModale'+i+'" rowspan="' + rowspan + '">';
						htmlString = htmlString + iccArr[i].tipoComponenteImportiCapitolo.descrizione;
						htmlString = htmlString + '</td>';
					}
					
					htmlString = htmlString + '<td  style="text-align:center;">'+ iccArr[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td>';
					htmlString = buildBodyRow(htmlString, i, iccArr);
					htmlString = htmlString + '</tr>';
					rowspan++;
				}else{
					break;
				}
			}
		}
		//RESIDUO
		if(tipoRiga ==varImp.typeResiduo){
			for(var i=0; i< residuoArr.length; i++){
					htmlString = htmlString + '<tr>';
					if(i==0){
						htmlString = htmlString + '<td class="componenti-competenza" id="tipoComModale'+i+'" rowspan="2">';
						htmlString = htmlString + 'Residuo'  ;
						htmlString = htmlString + '</td>';
					}
					htmlString = htmlString + '<td  style="text-align:center;">'+ residuoArr[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td>';
					htmlString = buildBodyRowResidui(htmlString, i, residuoArr);
					htmlString = htmlString + '</tr>';
					rowspan++;
			}
		}
		//CASSA
		if(tipoRiga ==varImp.typeCassa){
			for(var i=0; i< residuoArr.length; i++){
					htmlString = htmlString + '<tr >';
					if(i==0){
						htmlString = htmlString + '<td class="componenti-competenza" id="tipoComModale'+i+'" rowspan="2">';
						htmlString = htmlString + 'Cassa'  ;
						htmlString = htmlString + '</td>';
					}
					htmlString = htmlString + '<td  style="text-align:center;">'+ cassaArr[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td>';
					//SIAC-7349 - Start - MR - 22/04/2020 - Patch Nullpointer Exception per modifica stanziamenti anni precedenti
					//Chiamo metodo duplicato per retrocompatibilita
					htmlString = buildBodyRowCassa(htmlString, i, cassaArr);
					htmlString = htmlString + '</tr>';
					rowspan++;
			}
		}
		htmlString = htmlString + '</table>';
		if(tipoRiga ==varImp.typeCassa){
			htmlString = htmlString + '<div class="btn-group"><a class="btn dropdown-toggle" data-toggle="dropdown" href="#">calcola <span class="caret"></span>';
			htmlString = htmlString + '</a><ul class="dropdown-menu"><li><a href="#cassa0" id="pulsanteCalcoloImportoCassa">importo cassa</a></li></ul></div>';
			htmlString = htmlString + '<div id="contentDiv" style="margin-top:40px;"></div>';
		}
		
		$("#idBodyInsertModal").html(htmlString);
		$('.soloNumeri', '#idBodyInsertModal').gestioneDeiDecimali();
		$("#tipoComModale"+index).attr('rowspan', rowspan);
		//formattazione jira 
		jQuery(".soloNumeri").allowedChars({
	        numeric : true
		});
	   	$("input.decimale").gestioneDeiDecimali();
		$('#componenteModel').modal('show');
		
		//CALCOLO CASSA
		$("#pulsanteCalcoloImportoCassa").eventPreventDefault("click", function () {
			var competenza = competenzaArr[0].dettaglioAnno0.importo || 0;
	        var residui = residuoArr[0].dettaglioAnno0.importo || 0;
	        var cassa = $("#detAnno0Imp0");
	        var valore = (parseFloat(competenza) + parseFloat(residui));
	        // SIAC-7131
	        cassa.val(formatMoney(valore));
			
		});
		
		
		//AGGIORNA ELEMENTO
		var pulsanteAggiorna = $("#INSERT_modifica");


		if(!varImp.enabledInsertModifica){
			pulsanteAggiorna.prop("disabled",true);
			varImp.enabledInsertModifica=true;
		}

		pulsanteAggiorna.substituteHandler("click", function () {
			if(!($('#detAnno0Imp'+index).val() && $('#detAnno1Imp'+index).val() && $('#detAnno2Imp'+index).val())){
				$('#ERRORI_modaleInsertStanziamenti').show();
				$('#descrizioneErrore').text('Inserire tutti gli importi nel formato corretto');
			}else{
				var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
				overlay.overlay('show');
				
				/**
				 * INVIAMO SOLO LA RIGA INDEX DOVE CI SARA' lO STANZIAMENTO
				 * Non bellissimo...non riesco a sottometere tutto l'oggetto
				 */
//				var oggettoDaInviare = {listaComponenteImportiCapitolo: []};
//				oggettoDaInviare.listaComponenteImportiCapitolo[index] = iccArr[index];

				var oggettoDaInviare = {};
				var actionToDo ='';
				if(tipoRiga ==varImp.typeComponentiCapitolo){
					actionToDo = 'aggiornaComponenteImportoCapitolo_aggiornaComponenti.do';
					// SIAC-7131
					oggettoDaInviare.importoStanziamentoAnno0 = parseLocalNum($('#detAnno0Imp'+index).val());
					oggettoDaInviare.importoStanziamentoAnno1 = parseLocalNum($('#detAnno1Imp'+index).val());
					oggettoDaInviare.importoStanziamentoAnno2 = parseLocalNum($('#detAnno2Imp'+index).val());
					oggettoDaInviare.indexElemento = index;
					//7156
					oggettoDaInviare.uidTipoComponenteCapitolo=uidTipoComponenteCapitolo;
					// oggettoDaInviare.propostaDiDefault = (isPropostaDiDefault===true ? "SIDEF" : "NODEF");
					oggettoDaInviare.propostaDiDefault = isPropostaDiDefault;
				}
				if(tipoRiga ==varImp.typeResiduo){
					oggettoDaInviare.importoResiduoPresunto = parseLocalNum($('#detAnno0Imp'+index).val());
					actionToDo ='aggiornaComponenteImportoCapitolo_aggiornaResiduo.do';
				}
				if(tipoRiga ==varImp.typeCassa){
					oggettoDaInviare.importoCassaStanziamento = parseLocalNum($('#detAnno0Imp'+index).val());
					actionToDo ='aggiornaComponenteImportoCapitolo_aggiornaCassa.do';
				}
				
				//CHIUDIAMO MODALE
				
				$.postJSON(
						actionToDo,
						qualify(oggettoDaInviare),
						function(data) {
							if(data.errori && data.errori.length>0){
								$('#ERRORI_modaleInsertStanziamenti').show();
								$('#descrizioneErrore').text(data.errori[0].codice + ' - '+data.errori[0].descrizione);
								
							}else{
								//BUILDTABLE
								$('#componenteModel').modal('hide');
								buildTabellaInserisciNuovoComponente(data);
							}
							
							}
					).always(overlay.overlay.bind(overlay, 'hide'));
			}
		});
	}
	
	/*
	 * Eliminazione Elemento
	 */
	function eliminaComponentiImportiCapitolo(i){
		var pulsanteEliminazione = $("#EDIT_elimina");
		pulsanteEliminazione.off("click").on("click", function () {
			var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
			overlay.overlay('show');
			var oggettoDaInviare = {};
			oggettoDaInviare.tipoComponenteImportiCapitolo = varImp.dataObjmodal.importiComponentiCapitolo[i].tipoComponenteImportiCapitolo;
			//CHIUDIAMO MODALE
			$('#msgElimina').modal('hide');
			$.postJSON(
					"aggiornaComponenteImportoCapitolo_elimina.do",
					qualify(oggettoDaInviare),
					function(data) {
						var errori = data.errori;
						var messaggi = data.messaggi;
						var informazioni = data.informazioni;
						var alertErrori = $('#ERRORI');
						var alertMessaggi = $("#MESSAGGI");
						var alertInformazioni = $("#INFORMAZIONI");
						// Controllo gli eventuali errori, messaggi, informazioni
						if(impostaDatiNegliAlert(errori, alertErrori)) {return;}
						if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {return;	}
						if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {return;}
						//BUILDTABLE
						buildTabellaInserisciNuovoComponente(data);
						}
				).always(overlay.overlay.bind(overlay, 'hide'));
		});
	}
	

	
	
	return varImp;
	
	}(Variazioni || {}));


/* Document ready */
$(function () {
    VariazioniImporti.caricaTabella();
    $("#button_inserisciModifica").click(function () {
        VariazioniImporti.apriModaleComponentiInserimento();
	});
	

	
});