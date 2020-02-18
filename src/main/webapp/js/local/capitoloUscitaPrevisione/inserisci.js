/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*************************************************
**** Inserisci Capitolo Di Uscita Previsione ****
*************************************************
*/


	    /**
		 * CARICAMENTO TABELLA
		 */
		function caricaTabella (){
			var oggettoDaInviare = {};
			oggettoDaInviare.uidCapitolo = $('#uidCapitoloDaAggiornare').val();
			oggettoDaInviare.isCapitoloFondino = $('#fondinoHiddenValue').val();
			if(oggettoDaInviare.uidCapitolo){
			var overlay = $('#tabellaStanziamentiTotaliComponenti').overlay({usePosition: true});
			overlay.overlay('show');
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
							//BUILDTABLE
							buildTabellaInserisciNuovoComponente(data);
							}
					).always(overlay.overlay.bind(overlay, 'hide'));
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
						htmlString = htmlString + '<td class="text-center">'+data.residuoComponenti[i].tipoDettaglioComponenteImportiCapitolo.descrizione+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnnoPrec.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioResidui.importo).formatMoney()+'</td><td class="text-right">'+(data.residuoComponenti[i].dettaglioAnno0.importo).formatMoney()+'</td>';
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
		
		
/* Document ready */

$(
    
		
		
		function () {
        var Macroaggregato = $("#macroaggregato");
        var caricamentoPianoDeiConti = $.Deferred().resolve();
        
        $("#categoriaCapitolo").change();

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
        if(!$("form[data-disabled-form]").length) {
            $("#ModalAltriDati").on("click", CapitoloUscita.validaForm);
        }

        /* Carica lo zTree relativo alla Struttura Amministrativo Contabile */
        Capitolo.caricaStrutturaAmministrativoContabile();

        /*
         * Controlla se il campo FPV sia stato selezionato. In caso contrario, disabilita l'editazione dei campi
         * corrispondenti agli importi.
         */
        if ($("#FPV").attr("checked") !== "checked") {
            $("#fpv0").attr("disabled", "disabled");
            $("#fpv1").attr("disabled", "disabled");
            $("#fpv2").attr("disabled", "disabled");
        }
        /* Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti */
        if (Macroaggregato.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti(Macroaggregato, false, true);
        }
        /* Carica, eventualmente, lo zTree relativo al SIOPE */
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (!isNaN(elementoPdC) && elementoPdC !== 0) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }
        // CR SIAC-2559
        $('#siopeSpesa').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa',
            '#HIDDEN_descrizioneSiopeSpesa', '', 'ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do'));
        RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa','#HIDDEN_descrizioneSiopeSpesa','#HIDDEN_SIOPECodiceTipoClassificatore');

        $("form").on("reset", Capitolo.puliziaReset);
        
        caricaTabella();
    }
);