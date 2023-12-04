/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
**********************************************
**** Aggiorna Capitolo Di Uscita Gestione ****
**********************************************
*/
;(function($, w, c, cap) {
    "use strict";

    /**
     * Ricarica le select gerarchiche
     */
    function ricaricamentoSelectGerarchiche(e) {
        e.target.reset();

        /*
         * 1. cosa caricare
         * 2. da cosa carico
         * 3. che funzione invoco per caricare
         * 4. che evento deve essere triggerato per far si' che io invochi la funzione
         * 5. che evento invoco
         * 6. il namespace
         */
        c.ricaricaSelectGerarchica("programma", "missione", "caricaProgramma", "missioneCaricata", "programmaCaricato", cap);
        c.ricaricaSelectGerarchica("classificazioneCofog", "programma", "caricaCofog", "programmaCaricato", "cofogCaricato",cap);
        c.ricaricaSelectGerarchica("titoloSpesa", "programma", "caricaTitoloSpesa", "programmaCaricato", "titoloSpesaCaricato",cap);
        c.ricaricaSelectGerarchica("macroaggregato", "titoloSpesa", "caricaMacroaggregato", "titoloSpesaCaricato", "macroaggregatoCaricato", cap);
        c.ricaricaTreeElementoPianoDeiConti("macroaggregato");
        c.ricaricaTreeSiope();
        c.ricaricaTreeStrutturaAmministrativoContabile();

        // Inizio del triggering degli eventi
        c.ricaricaSelectBase("missione", "missioneCaricata");
//        c.ricaricaSelectBase("titoloSpesa", "titoloSpesaCaricato");

        
    }
	
	//validazione con stanziamenti negativi SIAC-6884
	function validazioneWithFondino(){
		
		var stanziamentiNegativiPresenti = ($("#stanziamentiNegativiPresentiHidden").val()==='true' ? true : false);
		var diversiDaFresco = ($("#componentiDiversiDaFrescoHidden").val()==='true' ? true : false);
		//var checkImpegnabile = $("#flagImpegnabile").prop('checked') ? true : false;
		var categoriaCapitolo = $.trim($.trim($("#categoriaCapitolo option:selected").text()).split("-")[0]) === "STD";
		var capitoloFondinoValue = $.trim($.trim($("#classificatoreGenerico3 option:selected").text()).split("-")[0]) === "01";
		var fondino = categoriaCapitolo && capitoloFondinoValue;
		//DA COMPLETARE DURANTE L'INTEGRAZIONE
		if(stanziamentiNegativiPresenti){
			$("#flagImpegnabile").attr("disabled", true);
			$("#categoriaCapitolo").attr("disabled", true);
			$("#classificatoreGenerico3").attr("disabled", true);
            $("#classificatoreGenerico3Hidden").removeAttr("disabled");
            //SIAC-7608 MR 28/04/2020
            $("#tipoCapitoloHidden").removeAttr("disabled");
            $("#flagImpegnabileHidden").removeAttr("disabled");
            //END
            $("#classificatoreGenerico3Hidden").val($("#classificatoreGenerico3 option:selected").val());
            //SIAC-7608 MR 28/04/2020
            $("#tipoCapitoloHidden").val($("#categoriaCapitolo option:selected").val());
            $("#flagImpegnabileHidden").val($("#flagImpegnabile").val());
            //END

			
		}else{
            if($("#flagImpegnabile").is(':disabled')) {
                $("#flagImpegnabile").removeAttr("disabled");
            }
            $("#classificatoreGenerico3Hidden").attr("disabled", true);
            //SIAC-7608 MR 28/04/2020
            $("#tipoCapitoloHidden").attr("disabled", true);
            $("#flagImpegnabileHidden").attr("disabled", true);
            //END
        }
        
        if(!fondino && diversiDaFresco){
            $("#flagImpegnabile").attr("disabled", true);
			$("#categoriaCapitolo").attr("disabled", true);
			$("#classificatoreGenerico3").attr("disabled", true);
			$("#classificatoreGenerico3Hidden").removeAttr("disabled");
            $("#classificatoreGenerico3Hidden").val($("#classificatoreGenerico3 option:selected").val());
            //SIAC-7608 MR 28/04/2020
            $("#tipoCapitoloHidden").removeAttr("disabled");
            $("#flagImpegnabileHidden").removeAttr("disabled");
            //END
            //SIAC-7608 MR 28/04/2020
            $("#tipoCapitoloHidden").val($("#categoriaCapitolo option:selected").val());
            $("#flagImpegnabileHidden").val($("#flagImpegnabile").val());
            //END

        } 
	}


	function caricaTabellaImportiComponenti(){
			var overlay = $('#tabellaStanziamentiPerComponenti').overlay({usePosition: true});
			overlay.overlay('show');
			$("#flagImpegnabile").parent().overlay('show');
			var uidCapitolo = $('#uidCapitoloDaAggiornare').val();
			var capitoloUscitaGestione={uid: uidCapitolo};
			$.postJSON(
				"aggiornaCapUscitaGestione_caricaImporti.do",
				qualify({'capitoloUscitaGestione' : capitoloUscitaGestione}),
				function(data) {
					var righeComp;
					var righeImporti;
					// Controllo gli eventuali errori, messaggi, informazioni
					if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {return;	}
					if(impostaDatiNegliAlert( data.messaggi, $('#MEAASGGI'))) {return;	}
					if(impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'))) {return;	}
					righeComp = data.righeComponentiTabellaImportiCapitolo;
					righeImporti = data.righeImportiTabellaImportiCapitolo;
					
					TabellaImportiComponenteCapitolo.creaEPosizionaRigheImporti(righeImporti, false);
					$('#competenzaCella').substituteHandler('click', TabellaImportiComponenteCapitolo.creaEPosizionaRigheComponenti.bind(undefined, righeComp, false, false));
				
				}
			).always(function(){
				overlay.overlay('hide');
				//SIAC-8347	
				$("#flagImpegnabile").parent().overlay('hide');
			});
		}

    $(function () {
        var macroaggregato = $("#macroaggregato");
        var editabilitaElementoPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiEditabile").val();
        var editabilitaStrutturaAmministrativaContabile = $("#HIDDEN_StrutturaAmministrativoContabileEditabile").val();
        var editabilitaSiope = $("#HIDDEN_SIOPEEditabile").val();
        var caricamentoPianoDeiConti = $.Deferred().resolve();

        /**
         * Funzioni necessarie alla tabella, sezione componenti competenza. Permettono di settare il rowspan a 2
         * all descrizione se il componente ha sia Stanziamento che Impegnato
         */

        var listTypeComponent=[];
        var rows = $("tbody#componentiCompetenzaAgg").find("tr");
        
    
    
        function getDescriptions(){
            var rows = $("tbody#componentiCompetenzaAgg").find("tr");
            for (var index = 0; index < rows.length; index++) {
                var typeComponent = {};
                var tableCell = {};
                var item = rows[index];
                var description = ($(item).find('td:first-child').text());
                var type = ($(item).find('td:eq(1)').text());
                typeComponent[description.trim()]=type.trim();
                tableCell[item] = typeComponent;
                listTypeComponent.push(tableCell);
            }
            getRowSpan();
        }
    
        function getValuesFromObject(item){
            var values;
            for(var k in item){
                values = item[k];
            }
            return values;
        }
        function getKeyFromObject(item){
            var key;
            for(var k in item){
                key = k;
            }
            return key;
        }
    
        
    
        function getRowSpan(){
            var i=0;
            while(i<listTypeComponent.length){
                
                if(i==listTypeComponent.length){
                    //set del rowspan a 1
                    break;
                }
                
                //oggettoni contenenti i td
                var obj=listTypeComponent[i]; 
                var obj2 = listTypeComponent[i+1];
                //oggetti contententi descrizione - tipo
                var keyObj1=getKeyFromObject(obj); //td
                var keyObj2=getKeyFromObject(obj2) //td
                var valuesObj1 = getValuesFromObject(obj); //{}
                var valuesObj2 = getValuesFromObject(obj2);//{}
                
                //confronto le chiavi e i valori (descr - type)
                var key1 = getKeyFromObject(valuesObj1) //fresco1
                var key2 = getKeyFromObject(valuesObj2) //fresco1
                var value1 = getValuesFromObject(valuesObj1);
                var value2 = getValuesFromObject(valuesObj2);
                
                if(key1===key2 && value1!==value2){
                    $(rows[i]).find('.componenti-competenza').attr("rowspan", "2");
                    $(rows[i+1]).find('.componenti-competenza').remove();
                    i+=2;
                }else if(key1!==key2){
                    i+=1;
                }else{
                    i+=1;
                }
                
    
            }
        }

        // Lego le azioni
        $("#missione").on("change", CapitoloUscita.gestisciChangeMissione.bind(null, false));
        $("#programma").on("change", CapitoloUscita.caricaCofogTitolo.bind(null, false));
        $("#titoloSpesa").on("change", CapitoloUscita.caricaMacroaggregato);
        $("#macroaggregato").on("change", function() {
            Capitolo.caricaPianoDeiConti(this, false);
        });
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        // CR SIAC-2559
//        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);
        $("#pulsanteStanziamentoAnnoPrecedente").on("click", Capitolo.stanziamentoAnnoPrecedente);

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

        // CR SIAC-2559
//        // Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti
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
        
        
        //SIAC-6881

        $("#componentiCompetenzaAgg").hide();

        $("#competenzaTotale").click(function(){
            $("#componentiCompetenzaAgg").slideToggle();
        });

        getDescriptions();

        //alert($("#stanziamentiNegativiPresentiHidden").val());
        
    	//SIAC-6884 Validazione con stanziamenti negativi e altre funzioni
		/*$("#classificatoreGenerico3").change(function(){
			var capitoloFondinoValue = $.trim($.trim($("#classificatoreGenerico3 option:selected").text()).split("-")[0]) === "01";
			var uidCapitolo = $("#uidCapitoloDaAggiornare").val();
			if(capitoloFondinoValue){
				caricaTabella(true)
			}else{
				caricaTabella(false);
			}

		});*/

		caricaTabellaImportiComponenti();
		validazioneWithFondino();
		
		$("#flagImpegnabile").change(function(){
			if($(this).prop('checked')){
				caricaTabellaImportiComponenti(false);
				
			}

		});

		$("#categoriaCapitolo").change(function(){
			var categoriaSTD = $.trim($.trim($(this).find('option:selected').text()).split("-")[0]) === "STD";
			if(!categoriaSTD){
				caricaTabellaImportiComponenti(false);
			}
        });	
        
        //ALLA LOAD DELLA PAGINA
		var classificatoreGenerico3Fondino = $("#classificatoreGenerico3");
		if($("#flagImpegnabile").prop("checked")){
			classificatoreGenerico3Fondino.attr("disabled", true);
		}

		var categoriaCapitolo = $.trim($.trim($("#categoriaCapitolo option:selected").text()).split("-")[0]) === "STD";
		if(!categoriaCapitolo){
			$("#classificatoreGenerico3").attr("disabled", true);
		}
		
		cap.gestisciRisorsaAccantonata(true);
        
        
        
    });
    
    
    
   
    
    
    
}(jQuery, window, Capitolo, CapitoloUscita));