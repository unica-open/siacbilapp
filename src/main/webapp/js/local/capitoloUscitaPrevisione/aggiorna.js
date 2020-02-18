/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
************************************************
**** Aggiorna Capitolo Di Uscita Previsione ****
************************************************
*/
;(function($, w, c, cap) {
    "use strict";

    /**
     * Ricarica le select gerarchiche
     */

	//  //SIAC-6884 creo una variabile privata settandola a false. Tengo traccia dei componenti associati
	//  var freschiAssociati = false;
	 

	//  var checkNonFreschiAssociati = function(tipologieComponenti){
	// 	 var componentiDiversiDaFresco = 0;		 
	// 	 freschiAssociati = tipologieComponenti;
	// 	 if(freschiAssociati instanceof Array && freschiAssociati.length>0){			
	// 		freschiAssociati.forEach(function(importo) {				
	// 			if(!(importo.tipoComponenteImportiCapitolo.macrotipoComponenteImportiCapitolo.descrizione === 'FRESCO')){
	// 				componentiDiversiDaFresco++;
	// 			}
	// 		});
	// 		if(componentiDiversiDaFresco>0){
	// 			// $("#flagImpegnabile").attr("disabled", true)
	// 			// $("#categoriaCapitolo").attr("disabled", true)
	// 			// $("#classificatoreGenerico3").attr("disabled", true)
	// 		}			
	// 	 }
	//  }
	// //  function getFreschiAssociati(){
	// // 	return this.fresciAssociati;
	// // }






    function ricaricamentoSelectGerarchiche(e) {
        e.target.reset();

        c.ricaricaSelectGerarchica("programma", "missione", "caricaProgramma", "missioneCaricata", "programmaCaricato", cap);
        c.ricaricaSelectGerarchica("classificazioneCofog", "programma", "caricaCofog", "programmaCaricato", "cofogCaricato",cap);
        c.ricaricaSelectGerarchica("macroaggregato", "titoloSpesa", "caricaMacroaggregato", "titoloSpesaCaricato", "macroaggregatoCaricato", cap);
        c.ricaricaTreeElementoPianoDeiConti("macroaggregato");
        c.ricaricaTreeSiope();
        c.ricaricaTreeStrutturaAmministrativoContabile();

        // Inizio del triggering degli eventi
        c.ricaricaSelectBase("missione", "missioneCaricata");
        c.ricaricaSelectBase("titoloSpesa", "titoloSpesaCaricato");
	}
	
	
    
    /**
	 * CARICAMENTO TABELLA
	 */
	function caricaTabella (seFondino){
		var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
		overlay.overlay('show');
		var oggettoDaInviare = {};
		oggettoDaInviare.uidCapitolo = $('#uidCapitoloDaAggiornare').val();
		//oggettoDaInviare.isCapitolo = $('#fondinoHiddenValue').val();
		//ALLA LOAD
		//IS FONDINO RICARICA LE COMPONENTI
		if(seFondino==true){
			oggettoDaInviare.isCapitoloFondino = true;
		}else if(seFondino==false){
			oggettoDaInviare.isCapitoloFondino = false;
		}else{
			oggettoDaInviare.isCapitoloFondino = $('#fondinoHiddenValue').val();
		}
		$.postJSON(
				"aggiornaComponenteImportoCapitolo_ricerca.do",
				oggettoDaInviare,
				function(data) {
					var errori = data.errori;
					var messaggi = data.messaggi;
					var informazioni = data.informazioni;
					var alertErrori = $('#ERRORI');
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
					//SIAC-6884
					checkStanziamentiNegativi(data.importiComponentiCapitolo);
					checkNonFreschiAssociati(data.importiComponentiCapitolo);
					//SIAC 6881
					//BUILDTABLE
					buildTabellaInserisciNuovoComponente(data);
					}
			).always(overlay.overlay.bind(overlay, 'hide'));
}


	//SIAC 6884 controlla stanziamenti negativi
	function checkStanziamentiNegativi(importiCapitolo){		
		if(importiCapitolo instanceof Array && importiCapitolo.length>0){

			for (var i = 0; i < importiCapitolo.length; i++) {
				const importo = importiCapitolo[i];
				for(var prop in importo){
					if(importo.hasOwnProperty(prop)){
						if(importo[prop].importo && importo[prop].importo<0){
							disabledField();
							break;
							
						}
					}
				}
				
			}

		}
	}

	//SIAC-6884 controlla componenti non freschi associati
	function checkNonFreschiAssociati(importiCapitolo){	
		$("#nonFreschiHidden").removeAttr("disabled");
		$("#nonFreschiHidden").attr("value", false);	
		if(importiCapitolo instanceof Array && importiCapitolo.length>0){
			for (var i = 0; i < importiCapitolo.length; i++) {
				const importo = importiCapitolo[i];
				var descrizione = (((((importo.tipoComponenteImportiCapitolo) || undefined).macrotipoComponenteImportiCapitolo) || undefined).descrizione ) || undefined;
				var propostaDefault = (importo.propostaDefault) ? true : false;
				if(descrizione && !(descrizione === "FRESCO") && !propostaDefault){
					$("#nonFreschiHidden").removeAttr("disabled");
					$("#nonFreschiHidden").attr("value", true);
					disabledFieldForNotFreschi();
					break;

				}
			}
		}
	}


	function disabledField(){
		var categoria = $("#categoriaCapitolo").val();
		var budget = $("#classificatoreGenerico3 option:selected").val();
		var checkImpegnabile = $("#flagImpegnabile").prop("checked");
			
		
		$("#categoriaCapitolo").attr("disabled", true);
		$("#flagImpegnabile").attr("disabled", true);
		$("#classificatoreGenerico3").attr("disabled", true);
		
		
		$("#categoriaCapitoloHidden").removeAttr("disabled");
		$("#flagImpegnabileHidden").removeAttr("disabled");
		$("#classificatoreGenerico3Hidden").removeAttr("disabled");
		
		$("#categoriaCapitoloHidden").attr("value", categoria);
		$("#flagImpegnabileHidden").attr("value", checkImpegnabile);
		$("#classificatoreGenerico3Hidden").attr("value", budget);
		
	}

	function disabledFieldForNotFreschi(){
		//mi conservo un hidden nella jsp per recuperare il dato
		var categoriaCapitoloSTD = $.trim($.trim($("#categoriaCapitolo option:selected").text()).split("-")[0]) === "STD";
		var checkImpegnabile = $("#flagImpegnabile").prop("checked");
		var nonFreschiAssociati = $("#nonFreschiHidden").val();
		if(nonFreschiAssociati && categoriaCapitoloSTD && !checkImpegnabile){
			$("#classificatoreGenerico3").attr("disabled", true);
		}
		
	}
	/**
	 * BUILD CAMPI TABELLA
	 */
	function buildTabellaInserisciNuovoComponente(data) {
		$("#tabellaStanziamentiTotaliComponenti").html("");
			var htmlString = '';
			var anno0=data.annoEsercizioInt;
			var annoPrece=anno0-1;
			var anno1=anno0+1;
			var anno2=anno0+2;
			//HEAD
			htmlString = htmlString + '<tr><th >&nbsp;</th><th >&nbsp;</th><th class="text-right">'+ annoPrece +'</th><th class="text-right">Residui '+anno0+'</th><th  class="text-right">' + anno0 + '</th><th class="text-right">' + anno1 + '</th><th class="text-right">' + anno2 + '</th><th class="text-right"> >' + anno2 + '</th></tr>';
			//COMPETENZA
			if(data.cassaComponenti && data.competenzaComponenti.length>=3){
				for(var i=0;i<data.competenzaComponenti.length;i++){
					// SIAC-7185
					if(i==0){
						htmlString = htmlString + '<tr  class="componentiRowFirst" >';
						htmlString = htmlString + '<th rowspan = "3" class="stanziamenti-titoli"><a id="competenzaTotaleInserimento" href="#tabellaComponentiInserimento"  class="disabled" >Competenza</a></th>';
					}else{
						htmlString = htmlString + '<tr  class="componentiRowOther" >';
					}
					htmlString = htmlString + '<td class="text-center">'+data.competenzaComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno0.importo).formatMoney()+'</td>';
					htmlString = htmlString + '<td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right">'+(data.competenzaComponenti[i].dettaglioAnniSucc.importo).formatMoney()+'</td>';
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
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioResidui.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno0.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno1.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnno2.importo).formatMoney() + '</td>';
					htmlString = htmlString + '<td style="text-align:right;">' + (data.importiComponentiCapitolo[i].dettaglioAnniSucc.importo).formatMoney() + '</td>';
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
					htmlString = htmlString + '<td class="text-center">'+data.residuoComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+'0,00'+'</td>';
					htmlString = htmlString + '<td class="text-right">'+(data.residuoComponenti[i].dettaglioAnno1.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnno2.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnniSucc.importo).formatMoney()+'</td>';
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
    	    $("#competenzaTotaleInserimento").click(function(){
    	        $(".componentiCompetenzaRow").slideToggle();
    	    });
	}
	
    
    

    $(function () {
        var macroaggregato = $("#macroaggregato");
        var editabilitaElementoPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiEditabile").val();
        var editabilitaStrutturaAmministrativaContabile = $("#HIDDEN_StrutturaAmministrativoContabileEditabile").val();
        var editabilitaSiope = $("#HIDDEN_SIOPEEditabile").val();
		var caricamentoPianoDeiConti = $.Deferred().resolve();
		
        // Lego le azioni agli elementi
        $("#missione").on("change", CapitoloUscita.caricaProgramma.bind(null, false));
        $("#programma").on("change", CapitoloUscita.caricaCofogTitolo.bind(null, false));
        $("#titoloSpesa").on("change", CapitoloUscita.caricaMacroaggregato);
        $("#macroaggregato").on("change", function() {
            Capitolo.caricaPianoDeiConti(this, false);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);
        $("#pulsanteCalcoloImportoCassa").on("click", Capitolo.calcolaImportoCassa);

        /*
         * Controlla se il campo FPV sia stato selezionato. In caso contrario, disabilita l'editazione dei campi
         * corrispondenti agli importi.
         */
        if ($("#FPV").attr("checked") !== "checked") {
            $("#fpv0").attr("disabled", "disabled");
            $("#fpv1").attr("disabled", "disabled");
            $("#fpv2").attr("disabled", "disabled");
        }

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        if(editabilitaStrutturaAmministrativaContabile === "true") {
            Capitolo.caricaStrutturaAmministrativoContabile();
        }

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        if (editabilitaElementoPianoDeiConti === "true" && macroaggregato.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(macroaggregato, false, true);
        }

        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (editabilitaSiope === "true" && !isNaN(elementoPdC) && elementoPdC) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }
        // CR SIAC-2559
        if (editabilitaSiope === "true") {
            $('#siopeSpesa').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa',
                '#HIDDEN_descrizioneSiopeSpesa', '', 'ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do'));
            RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa','#HIDDEN_descrizioneSiopeSpesa','#HIDDEN_SIOPECodiceTipoClassificatore');
        }

        $("form").substituteHandler("reset", ricaricamentoSelectGerarchiche);
        
		caricaTabella();
		
		//SIAC-6884 Validazione con stanziamenti negativi e altre funzioni 
		$("#classificatoreGenerico3").change(function(){
			var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
			tastoStanziamenti.attr("disabled", true);
			var fondino = $.trim($.trim($(this).find('option:selected').text()).split("-")[0]) ==="01";
			if(fondino){
				$('#fondinoHiddenValue').val(true);
				caricaTabella(true);
			}else{
				$('#fondinoHiddenValue').val(false);
				caricaTabella(false);
			}
		});

		
		$("#flagImpegnabile").change(function(){
			var classificatoreGenerico3Fondino = $("#classificatoreGenerico3");
			var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
			tastoStanziamenti.attr("disabled", true);
			if($(this).prop('checked')){
				$('#fondinoHiddenValue').val(false);
				classificatoreGenerico3Fondino.val("0"); //clear della select. 
            	classificatoreGenerico3Fondino.attr("disabled", true);
				caricaTabella(false);	
			}else{
				//$('#fondinoHiddenValue').val(true);
				var nonFreschiAssociati = $("#nonFreschiHidden").val() === 'true' ? true :false;
				if(nonFreschiAssociati){
					//se non ci sono freschi associati, non posso selezionare il fondino
					classificatoreGenerico3Fondino.val("0"); //clear della select. 
            		classificatoreGenerico3Fondino.attr("disabled", true);	
				}else{
					classificatoreGenerico3Fondino.removeAttr("disabled");
				}
			}

		});

		$("#categoriaCapitolo").change(function(){
			var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
			tastoStanziamenti.attr("disabled", true);
			var categoriaSTD = $.trim($.trim($(this).find('option:selected').text()).split("-")[0]) === "STD";			
			if(!categoriaSTD){
				caricaTabella(false);
			}
		});


		//SIAC-6884 inizialmente disabilito lo stanziamenti. da abilitare solo dopo il salvataggio del capitolo aggiornato
		
		// var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
		// tastoStanziamenti.attr("disabled", true);
		

		//ALLA LOAD DELLA PAGINA
		var classificatoreGenerico3Fondino = $("#classificatoreGenerico3");
		if($("#flagImpegnabile").prop("checked")){
			classificatoreGenerico3Fondino.attr("disabled", true);
		}

		var categoriaCapitolo = $.trim($.trim($("#categoriaCapitolo option:selected").text()).split("-")[0]) === "STD";
		if(!categoriaCapitolo){
			$("#classificatoreGenerico3").attr("disabled", true);
		}
		

		
    });
}(jQuery, window, Capitolo, CapitoloUscita))
