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
	
	
	function disabledFieldPerStanziamentiNegativi(){
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
	
	function determinaSeCapitoloFondino(){
		var categoria = $("#categoriaCapitolo").val();
		var budget = $.trim($.trim($("#classificatoreGenerico3 option:selected").text()).split("-")[0])  === '01';
		var checkImpegnabile = $("#flagImpegnabile").prop("checked");
		return categoria && budget && !checkImpegnabile;
	}
	
	function impostaHandlersCapitoloFondino(){
		//SIAC- 8256: ma questo non va in conflitto ocn la gestione della risorsa accantonata?
					//lo lascio qui per ora					
					$("#flagImpegnabile").change(function(){
							var classificatoreGenerico3Fondino = $("#classificatoreGenerico3");
							var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
							tastoStanziamenti.attr("disabled", true);
							if($(this).prop('checked')){
								$('#fondinoHiddenValue').val(false);
								classificatoreGenerico3Fondino.val("0"); //clear della select. 
				            	classificatoreGenerico3Fondino.attr("disabled", true);
								$('.nascondi-se-fondino').show();	
							}else{
								//$('#fondinoHiddenValue').val(true);
								var nonFreschiAssociati = $("#nonFreschiHidden").val() === 'true' ? true :false;
								if(nonFreschiAssociati){
									//se non ci sono freschi associati, non posso selezionare il fondino
									classificatoreGenerico3Fondino.val("0"); //clear della select. 
				            		classificatoreGenerico3Fondino.attr("disabled", true);	
									$('.nascondi-se-fondino').hide();
								}else{
									classificatoreGenerico3Fondino.removeAttr("disabled");
									$('.nascondi-se-fondino').show();
								}
							}
				
						});
				
						$("#categoriaCapitolo").change(function(){
							var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
							tastoStanziamenti.attr("disabled", true);
							var categoriaSTD = $.trim($.trim($(this).find('option:selected').text()).split("-")[0]) === "STD";			
							if(!categoriaSTD){
								$('.nascondi-se-fondino').show();
							}
						});



		//SIAC-6884 inizialmente disabilito lo stanziamenti. da abilitare solo dopo il salvataggio del capitolo aggiornato

	}
	
	
	function caricaTabellaImportiComponenti(){
			var overlay = $('#tabellaStanziamentiPerComponenti').overlay({usePosition: true});
			overlay.overlay('show');
			var uidCapitolo = $('#uidCapitoloDaAggiornare').val();
			var capitoloUscitaPrevisione={uid: uidCapitolo};
			var ciSonoComponentiNonFresco;
			$("#flagImpegnabile").parent().overlay('show');
			return $.postJSON(
				"aggiornaCapUscitaPrevisione_caricaImporti.do",
				qualify({'capitoloUscitaPrevisione' : capitoloUscitaPrevisione}),
				function(data) {
					var righeComp;
					var righeImporti;
					var stanziamentiNegativiPresenti;
					// Controllo gli eventuali errori, messaggi, informazioni
					if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {return;	}
					if(impostaDatiNegliAlert( data.messaggi, $('#MEAASGGI'))) {return;	}
					if(impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'))) {return;	}
					righeComp = data.righeComponentiTabellaImportiCapitolo;
					righeImporti = data.righeImportiTabellaImportiCapitolo;
					
					TabellaImportiComponenteCapitolo.creaEPosizionaRigheImporti(righeImporti, false);
					$('#competenzaCella').substituteHandler('click', TabellaImportiComponenteCapitolo.creaEPosizionaRigheComponenti.bind(undefined, righeComp, false, true));
					stanziamentiNegativiPresenti = data.stanziamentiNegativiPresenti;
					
					ciSonoComponentiNonFresco = data.presentiComponentiNonFresco;
					//1 - se ci sono stanziamenti negativi, disabilito alcuni campi così:'
					if(stanziamentiNegativiPresenti){
						disabledFieldPerStanziamentiNegativi();
					}
					if(determinaSeCapitoloFondino()){
						$('#fondinoHiddenValue').val(true);
						//SIAC-8256, lascio qui gli handlers nonostante alcuni dubbi sul funzionamento
						impostaHandlersCapitoloFondino();	
					}
					$("#nonFreschiHidden").val(ciSonoComponentiNonFresco);
					
					
				}
			).always(function(){
				overlay.overlay('hide');	
				$("#flagImpegnabile").parent().overlay('hide');
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
        
		caricaTabellaImportiComponenti();
		
		//SIAC-6884 Validazione con stanziamenti negativi e altre funzioni 
		$("#classificatoreGenerico3").change(function(){
			var tastoStanziamenti = $('a#tastoStanziamenti');//.attr("disabled", true);
			tastoStanziamenti.attr("disabled", true);
			var fondino = $.trim($.trim($(this).find('option:selected').text()).split("-")[0]) ==="01";
			if(fondino){
				$('#fondinoHiddenValue').val(true);
			}else{
				$('#fondinoHiddenValue').val(false);
			}
		});
							
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
		
		//SIAC-7192
		cap.gestisciRisorsaAccantonata(true);

		
    });
}(jQuery, window, Capitolo, CapitoloUscita))
