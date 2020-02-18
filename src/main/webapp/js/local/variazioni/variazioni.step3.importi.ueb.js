/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 3 delle variazioni - variazione di importi con gestione UEB */

var VariazioniImportiUEB = (function(varImp) {

	varImp.leggiCapitoliNellaVariazione = leggiCapitoliNellaVariazione;
	varImp.cercaCapitolo = cercaCapitolo;
	varImp.cercaCapitoloNellaVariazione = cercaCapitoloNellaVariazione;
	varImp.redirezioneNuovoCapitolo = redirezioneNuovoCapitolo;
	varImp.annullaCapitolo = annullaCapitolo;
	varImp.redirezioneNuovoUEB = redirezioneNuovoUEB;
	varImp.effettuaNuovaRicerca = effettuaNuovaRicerca;
	varImp.impostaValoreCassaSeApplicabile = impostaValoreCassaSeApplicabile;
	varImp.salvaEProsegui = salvaEProsegui;
	
	function ottieniTipoApplicazioneCapitolo(tipoApplicazione){
		var tipoApplicazioneNotUndefined = tipoApplicazione || $("#HIDDEN_tipoApplicazione").val();
		return (tipoApplicazioneNotUndefined === "Previsione" || tipoApplicazioneNotUndefined === "PREVISIONE" ? "Previsione" : "Gestione");
	}
	
	
    /**
     * Imposta i dati nella tabella, relativamente all'indice selezionato.
     *
     * @param importo l'oggetto contente gli importi da inserire
     * @param indice  l'indice relativo alla colonna in cui inserire i dati
     */
    function impostaDati(importo, indice) {
        Variazioni.impostaValutaEAllineaADestra("#totaleCompetenzaTrovatoAnno" + indice, importo.stanziamento);
        Variazioni.impostaValutaEAllineaADestra("#totaleResiduiTrovatoAnno" + indice, importo.stanziamentoResiduo);
        Variazioni.impostaValutaEAllineaADestra("#totaleCassaTrovatoAnno" + indice, importo.stanziamentoCassa);
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
	 * Gestisce la chiamata asincrona al servizio di aggiornamento dell'anagrafica
	 * */
	function salvaEProsegui(){
		var form = $('form');
        var obj = qualify(form.serializeObject());
        $(document.body).overlay('show');
        $('#spinner_salvaEProseguiButton').addClass('activated');

        return $.postJSON('esecuzioneStep3InserimentoVariazioniImportiConUEB.do', obj)
        .then(function(data) {
            var alertErrori = $('#ERRORI');
            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	resettaSpinnerFormSubmitted();
                return;
            }
            ottieniResponse(10, 10000);
        });
	}
	
	function resettaSpinnerFormSubmitted(){
		$(document.body).overlay('hide');
		
        $('[data-spinner-async]').removeClass('activated');
    }

	
    function ottieniResponse(tentativiRimanenti, timeout){
    	var url = 'inserisciVariazioneImportiUEBAction_ottieniResponseExecuteStep3Async.do';
        return $.postJSON(url)
        .then(function(data) {
            var alertErrori = $('#ERRORI');

            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	resettaSpinnerFormSubmitted();        	
                return;
            }

            if(data.isAsyncResponsePresent === undefined){
                impostaDatiNegliAlert(['COR_ERR_0001 - Errore di sistema: impossibile ottenere la risposta asincrona.'], alertErrori);
                resettaSpinnerFormSubmitted();
                return;
            }


            if(!data.isAsyncResponsePresent){
                if(tentativiRimanenti<=0){
                	$(document.body).overlay('hide');             	
                    return;
                }

                setTimeout(ottieniResponse, timeout, --tentativiRimanenti, timeout);
                return;


            }


            document.location = 'enterStep4InserimentoVariazioniImportiUEB.do';
        });
    }

	/**
	 * Chiamata Ajax per ottenere la lista dei Capitoli associati alla variazione
	 * e inizializzare dataTable
	 * */
	function leggiCapitoliNellaVariazione(){
			
			$('#tabellaGestioneVariazioni').overlay('show');
			$.postJSON(
					"leggiCapitoliNellaVariazione_importiUeb.do",
					{},
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

						$('form').removeClass('form-submitted');
						$("#EDIT_salva").removeAttr('disabled');
						//chiamo dataTable
						impostaDataTableCapitoliNellaVariazione($("#tabellaGestioneVariazioni"));					

						impostaTotaliInDataTable(data.totaleStanziamentiEntrata, data.totaleStanziamentiCassaEntrata, data.totaleStanziamentiResiduiEntrata,
									data.totaleStanziamentiSpesa, data.totaleStanziamentiCassaSpesa, data.totaleStanziamentiResiduiSpesa);
						}
				);
	}
	
	function apriEPopolaModaleModificaImporti( riga) {
		
			var tabellaSorgente = $("#tabellaGestioneVariazioni");
			// Pulsante per il salvataggio
			var pulsanteSalvataggio = $("#EDIT_aggiorna");

			var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);

			// Pulisco il form
			$("#editStanziamenti fieldset :input").val("");
			// Nascondo l'eventuale alert di errore già presente
			$("#ERRORI_modaleEditStanziamenti").slideUp();

		
			$("#competenzaVariazioneModal").val(datiNellaRiga.competenza.formatMoney());
			$("#residuoVariazioneModal").val(datiNellaRiga.residuo.formatMoney());
			$("#cassaVariazioneModal").val(datiNellaRiga.cassa.formatMoney());

			$("#titoloModaleVariazioneStanziamenti").html("Modifica Stanziamenti Capitolo " + datiNellaRiga.numeroCapitolo + " / " + datiNellaRiga.numeroArticolo);

			pulsanteSalvataggio.substituteHandler("click", function(e) {
				e.preventDefault();
				$('form').addClass('form-submitted');			
			}); // Aggiornamento

			// Apro il modale per la modifica degli importi
			$("#collapse_ricerca").addClass("in");
			window.location.hash = "collapse_ricerca";
	}
	
	
	/**
	 * Imposta l'uid del capitolo da eliminare dalla variazione.
	 *
	 * @param riga      la riga considerata
	 */
	
	function apriModaleEliminazioneCapitoloNellaVariazione(riga) {
		
	    var tabellaSorgente = $("#tabellaGestioneVariazioni");
	    var tabellaDestinazione = $("#tabellaGestioneVariazioni");
	    var pulsanteEliminazione = $("#EDIT_elimina");

	    pulsanteEliminazione.off("click").on("click", function() {
	        eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga);
	    }); // Eliminazione
	}
	
	function eliminaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, riga) {

		var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);
		var $overlayModale = $('#msgElimina').find('.overlay-modale').overlay('show');
		
		oggettoDaEliminare.uid = oggettoOriginale.uid;

		// Qualifico correttamente l'oggetto da inserire
		oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaUEB.elementoCapitoloVariazione");
		return $.postJSON("eliminaCapitoliNellaVariazione_importiUeb.do", oggettoDaEliminare)
		.then(function() {
			impostaDatiNegliAlert(["COR_INF_0006 - Operazione effettuata correttamente"],$('#INFORMAZIONI'));
			$('form').addClass('form-submitted');
			leggiCapitoliNellaVariazione();
			$('#msgElimina').modal('hide');
		})
		.always($overlayModale.overlay.bind($overlayModale, 'hide'));
	}

	
	/**
	 * Imposta il dataTable per l'elenco deli capitoli nella variazione.
     *
	 * @param tabella       la tabella da paginare
	 * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
	*/

	function impostaDataTableCapitoliNellaVariazione(tabella) {
			
			var isEditable = $("input[name='specificaUEB.tipoCapitolo']").length !== 0;
			
			
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
							sZeroRecords : "Non sono presenti capitoli associati alla variazione"
						},
						// Definizione delle colonne
						aoColumnDefs : [
						                {
						                	aTargets : [ 0 ],
						                	mData : function (source) {
						                				var result = source.descrizione ? "<a rel='popover' href='#'>" + source.denominazione + "</a>" : source.denominazione;
						                				if(source.datiAccessorii) {
						                					result += " <em>(" + source.datiAccessorii + ")</em>";
						                				}
						                				return result;
						                	},
						                	fnCreatedCell : function (nTd, sData, oData) {
						                		if(oData.descrizione !== null && oData.descrizione !== undefined) {
						                			// Settings del popover
						                			var settings = {
						                					"content" : oData.descrizione,
						                					"title" : "Descrizione",
						                					"trigger" : "hover"
						                			};
						                			// Importante : attivare il popover sull'elemento anchor
						                			$("a", nTd).off("click")
						                			.on("click", function(event) {event.preventDefault();})
						                			.popover(settings);
						                		}
						                	}
						                },
						                {
						                	aTargets : [ 1 ],
						                	mData : function(source) {
						                			return source.competenza.formatMoney();
						                	},
						                	fnCreatedCell : function(nTd) {
						                		$(nTd).addClass("text-right");
						                	}
						                },
						                {
						                	aTargets : [ 2 ],
						                	mData : function(source) {
						                		return source.residuo.formatMoney();
						                	},
						                	fnCreatedCell : function(nTd) {
						                		$(nTd).addClass("text-right");
						                	}
						                },
						                {
						                	aTargets : [ 3 ],
						                	mData : function(source) {
						                		return source.cassa.formatMoney();
						                	},
						                	fnCreatedCell : function(nTd) {
						                		$(nTd).addClass("text-right");
						                	}
						                },
						                {
						                	bVisible: isEditable,
						                	aTargets : [ 4 ],
						                	mData : function() {
						                		return "<a href='#editStanziamenti' title='modifica gli importi' role='button' data-toggle='modal'>" +
						                		"<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
						                		"</a>";
						                	},
						                	fnCreatedCell : function (nTd, sData, oData, iRow, iCol) {
						                			// Imposta l'evento di onClick
						                		if(isEditable){
						                			$(nTd).addClass("text-center")
						                			.find("a").off("click")
						                			.on("click", function() {
						                				impostaFormVariazioneImportoAggiornamento(false,iRow);
						                			});
						                		}
						                	}
						                },
						                {
						                	bVisible: isEditable,
						                	aTargets : [ 5 ],
						                	mData : function() {
						                		return "<a href='#msgElimina' title='elimina' role='button' data-toggle='modal'>" +
						                		"<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
						                		"</a>";
						                	},
						                	fnCreatedCell : function(nTd, sData, oData, iRow) {
						                		if(isEditable){
						                			$(nTd).addClass("text-center")
						                			.find("a")
						                			.off("click")
						                			.on("click", function() {
						                				apriModaleEliminazioneCapitoloNellaVariazione(iRow);
						                			});
						                			
						                		}
						                	}
						                }
						                ],
						                fnPreDrawCallback: function () {
						                	// Mostro il div del processing
						                	tabella.parent().find('div.dataTables_processing').parent("div")
						                	.show();
						                	$('#SPINNER_leggiCapitoli').removeClass("activated");
						                	$('#tabellaGestioneVariazioni').overlay('hide');
						                	$('#tabellaGestioneVariazioni_wrapper').overlay('show');
						                },
						                // Chiamata al termine della creazione della tabella
						                fnDrawCallback: function () {
						                	
						                	tabella.parent().find('div.dataTables_processing').parent("div")
						                	.hide();
						                	$('#tabellaGestioneVariazioni_wrapper').overlay('hide');
						                }
			};
    		var opzioniClone = $.extend(true, {}, varImp.opzioniBaseDataTable);
			var opzioni = $.extend(true, opzioniClone, opzioniNuove);
			var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

			if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
					tabella.dataTable().fnClearTable(false);
					tabella.dataTable().fnDestroy();
			}

			tabella.dataTable(opzioni);
	}
    /**
     * Imposta il valore dei totali nella tabella di gestione delle variazioni
     *
     * **/
	function impostaTotaliInDataTable(totaleStanziamentiEntrata, totaleStanziamentiCassaEntrata, totaleStanziamentiResiduiEntrata,
			totaleStanziamentiSpesa, totaleStanziamentiCassaSpesa, totaleStanziamentiResiduiSpesa){
		
			
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

	

    function completaInserimentoNellaVariazione(tabellaDestinazione, listaCapitoliDaInserireNellaVariazione, modale){
         impostaDataTableUEBDaInserireNellaVariazione(tabellaDestinazione, listaCapitoliDaInserireNellaVariazione);
            // Chiudo il modale
         modale.modal("hide");
    }

    function rimuoviOverlay(){
    	$("#tabellaGestioneVariazioni").overlay('hide');
    	$('form').overlay('hide');
    }

    function richiamoServizioPersistenzaImporti(url, ajaxParam){
    	$("#tabellaGestioneVariazioni").overlay('show');
    	$("#EDIT_salva").attr('disabled','');
    	return $.postJSON(url, ajaxParam)
			.then(function(data) {
				var errori = data.errori;
    			var messaggi = data.messaggi;
    			// Alert errori
    			var alertErrori = $("#ERRORI");
				if(impostaDatiNegliAlert(errori, alertErrori)) {
				    $('form').overlay('hide');
				    $("#EDIT_salva").removeAttr('disabled');
					return;
				}
				if(messaggi && messaggi.length >0) {
        			gestioneRichiestaProsecuzione(messaggi, url, ajaxParam,data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);	
        			return;
				}
				
    			return varImp.leggiCapitoliNellaVariazione();
		}).always($("#tabellaGestioneVariazioni").overlay.bind($("#tabellaGestioneVariazioni"), 'hide'));
    }

	
	/**
     *  Apre e imposta la modale di conferma prosecuzione dell'azione
     */
	function gestioneRichiestaProsecuzione(array, url, ajaxParam, isCassaIncongruente, isCassaIncongruenteDopoDefinizione){
		ajaxParam["specificaUEB.ignoraValidazione"] = true;
		ajaxParam["specificaUEB.ignoraValidazioneImportiDopoDefinizione"] = !!isCassaIncongruenteDopoDefinizione;

		impostaRichiestaConfermaUtente(array[0].descrizione, richiamoServizioPersistenzaImporti.bind(undefined, url, ajaxParam), rimuoviOverlay);
	}


    /**
     * Imposta il form di aggiornamento o di inserimento della variazione.
     *
     * @param nuovo     se l'UEB &eacute; nuova o meno
     * @param riga      la riga considerata
     */
    function impostaFormVariazioneImportoAggiornamento(nuovo, riga) {
        var tabellaSorgente = nuovo ? $("#tabellaUEBTrovate") : $("#tabellaGestioneVariazioni");

        var tabellaDestinazione = $("#oggettiDaInserireNellaVariazione");
        // Pulsante per il salvataggio

        // Dati per l'editabilità dei campi
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);

        // Nascondo l'eventuale alert di errore già presente
        $("#ERRORI_modaleEditStanziamenti").slideUp();
        if(nuovo) {
        	impostaModalePerCapitoloAncoraDaCollegareAVariazione(datiNellaRiga, annoVariazione);        	
        } else {        	
        	impostaModalePerCapitoloPrecedentementeCollegatoAVariazione(datiNellaRiga, annoVariazione, tabellaSorgente, tabellaDestinazione);
        }
    }

    function impostaModalePerCapitoloAncoraDaCollegareAVariazione(datiNellaRiga, annoVariazione){
    	var pulsanteSalvataggio = $("#EDIT_salva");
    	var capitolo = {};
            	
    	$('#EDIT_salva').html("registra nella variazione");
    	$("#competenzaVariazioneUEB").val("");
		$("#residuoVariazioneUEB").val("");
		$("#cassaVariazioneUEB").val("");
		capitolo.uidCapitolo = datiNellaRiga.uid;
        capitolo.statoOperativoElementoDiBilancio = datiNellaRiga.statoOperativoElementoDiBilancio;
        capitolo.daAnnullare = datiNellaRiga.daAnnullare;
        capitolo.daInserire = datiNellaRiga.daInserire;
		capitolo.cassaOriginale = datiNellaRiga.cassa;
	    capitolo.residuoOriginale =  datiNellaRiga.residuo;
	    capitolo.competenzaOriginale = datiNellaRiga.competenza;
        pulsanteSalvataggio.off("click").on("click", function() {
        	$('#EDIT_salva').attr('disabled','disabled');
        	registraImportiNellaVariazione(capitolo, annoVariazione);
        }); // Salvataggio
    }

    function impostaModalePerCapitoloPrecedentementeCollegatoAVariazione(datiNellaRiga, annoVariazione, tabellaSorgente, tabellaDestinazione){
    	var pulsanteSalvataggio = $("#EDIT_salva");
    	var capitolo = {};
    	// Popolo i dati del form con quelli già presenti nell'oggetto
        $("#competenzaVariazioneUEB").val(datiNellaRiga.competenza.formatMoney());
        $("#residuoVariazioneUEB").val(datiNellaRiga.residuo.formatMoney());
        $("#cassaVariazioneUEB").val(datiNellaRiga.cassa.formatMoney());

        capitolo.uidCapitolo = datiNellaRiga.uid;
        capitolo.statoOperativoElementoDiBilancio = datiNellaRiga.statoOperativoElementoDiBilancio;
        capitolo.annoImporti = datiNellaRiga.annoImporti;
        capitolo.daAnnullare = datiNellaRiga.daAnnullare;
        capitolo.daInserire = datiNellaRiga.daInserire;
        capitolo.cassaOriginale = datiNellaRiga.cassaOriginale;
	    capitolo.residuoOriginale =  datiNellaRiga.residuoOriginale;
	    capitolo.competenzaOriginale = datiNellaRiga.competenzaOriginale;
	
    	$('#EDIT_salva').html("inserisci modifiche");
        pulsanteSalvataggio.off("click").on("click", function() {
        	aggiornaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, capitolo);
        });
    }

	/**
	 * Aggiorna il capitolo nella variazione
	*/

	function aggiornaCapitoloNellaVariazione(tabellaSorgente, tabellaDestinazione, capitolo) {

		var oggettoOriginale = $.extend(true, {}, capitolo);
		var url = "aggiornaCapitoliNellaVariazione_importiUeb.do";
		var ajaxParam;
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaAggiornare = {};
		var competenza = $("#competenzaVariazioneUEB").val();
		var residuo = $("#residuoVariazioneUEB").val();
		var cassa = $("#cassaVariazioneUEB").val();
		var form;

		oggettoDaAggiornare.uid = oggettoOriginale.uidCapitolo;
		oggettoDaAggiornare.statoOperativoElementoDiBilancio = oggettoOriginale.statoOperativoElementoDiBilancio;
		oggettoDaAggiornare.annoImporti = oggettoOriginale.annoImporti;
		oggettoDaAggiornare.competenza = competenza;
		oggettoDaAggiornare.residuo = residuo;
		oggettoDaAggiornare.cassa = cassa;
		oggettoDaAggiornare.competenzaOriginale = oggettoOriginale.competenzaOriginale;
		oggettoDaAggiornare.residuoOriginale = oggettoOriginale.residuoOriginale;
		oggettoDaAggiornare.cassaOriginale = oggettoOriginale.cassaOriginale;
		oggettoDaAggiornare.daAnnullare = oggettoOriginale.daAnnullare;
		oggettoDaAggiornare.daInserire = oggettoOriginale.daInserire;

		// Qualifico correttamente l'oggetto da inserire
		oggettoDaAggiornare = qualify(oggettoDaAggiornare, "specificaUEB.elementoCapitoloVariazione");
		ajaxParam = $.extend(true, {}, oggettoDaAggiornare/*, oggettoOld*/);
		form = $('form').addClass('form-submitted');
		return $.postJSON(url, ajaxParam)
		.then(function(data) {
			var errori = data.errori;
			var alertErrori = $("#ERRORI_modaleEditStanziamenti");
			var modale = $("#editStanziamenti");
			// Nascondo gli alert
			alertErrori.slideUp();
			if(impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
			if(data.messaggi && data.messaggi.length) {
				modale.modal('hide');
				gestioneRichiestaProsecuzione(data.messaggi, url, ajaxParam,data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);
				return;
			}
			modale.modal('hide');

			leggiCapitoliNellaVariazione();
		}).always(form.removeClass.bind(form, 'form-submitted'));
	}


    /**
     * Imposta il dataTable per l'elenco delle UEB.
     *
     * @param tabella       la tabella da paginare
     * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
     */
    function impostaDataTableElencoUEB(tabella, listaCapitoli) {
        if(listaCapitoli === null || listaCapitoli === undefined) {
            listaCapitoli = [];
        }
        var opzioniNuove = {
            aaData : listaCapitoli,
            bDestroy : true,
            // Definizione delle colonne
            aoColumnDefs : [
                {
                    aTargets : [ 0 ],
                    mData : function (source) {
                        var result = source.descrizione ? "<a rel='popover' href='#'>" + source.denominazione + "</a>" : source.denominazione;
                        if(source.datiAccessorii) {
                            result += " <em>(" + source.datiAccessorii + ")</em>";
                        }
                        return result;
                    },
                    fnCreatedCell : function (nTd, sData, oData) {
                        if(oData.descrizione !== null && oData.descrizione !== undefined) {
                            // Settings del popover
                            var settings = {
                                "content" : oData.descrizione,
                                "title" : "Descrizione",
                                "trigger" : "hover"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                        return source.competenza.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        return source.residuo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        return source.cassa.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function() {
                        return "<a href='#editStanziamenti' title='modifica gli importi' role='button' data-toggle='modal'>" +
                                   "<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function (nTd, sData, oData, iRow) {
                        // Imposta l'evento di onClick
                        $(nTd).addClass("text-center")
                            .find("a")
                                .off("click")
                                .on("click", function() {
                                    impostaFormVariazioneImportoAggiornamento(true, iRow);
                                });
                    }
                }
            ]
        };
        var opzioni = $.extend(true, {}, Variazioni.opzioniBaseDataTable, opzioniNuove);
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables();


        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable();
            tabella.dataTable().fnDestroy();
        }

        tabella.dataTable(opzioni);
    }

    /**
     * Elimina la nuova variazione.
     *
     * @param tabellaSorgente     {Object} la tabella sorgente
     * @param tabellaDestinazione {Object} la tabella di destinazione
     * @param riga                {Number} il numero della riga della variazione
     */
    function eliminaVariazioneDaAggiungere(tabellaSorgente, tabellaDestinazione, riga) {
        var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
        // Clone dell'oggetto originale, per effettuare la chiamata
        var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);

        // Qualifico correttamente l'oggetto da inserire
        oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaUEB.elementoCapitoloVariazione");

        $.postJSON(
            "eliminaCapitoliDaInserireNellaVariazione_importiUeb.do",
            oggettoDaEliminare,
            function(data) {
                postChiamataAjaxInserireNellaVariazioneElimina(data, tabellaDestinazione, "msgElimina");
            }
        );
    }

    /**
     * Imposta l'uid del capitolo da eliminare dalla variazione.
     *
     * @param riga la riga considerata
     */
    function impostaFormVariazioneImportoEliminazione(riga) {
        var tabellaSorgente = $("#oggettiDaInserireNellaVariazione");
        var tabellaDestinazione = $("#oggettiDaInserireNellaVariazione");
        var pulsanteEliminazione = $("#EDIT_elimina");

        pulsanteEliminazione.off("click").on("click", function() {
            eliminaVariazioneDaAggiungere(tabellaSorgente, tabellaDestinazione, riga);
        }); // Eliminazione
    }

    /**
     * Aggiorna la nuova variazione.
     *
     * @param tabellaSorgente     {Object} la tabella sorgente
     * @param tabellaDestinazione {Object} la tabella di destinazione
     * @param riga                {Number} il numero della riga della variazione
     */
    function aggiornaVariazioneNellaVariazione() {
        $.postJSON(
            "aggiornaCapitoliNellaVariazione_importiUeb.do",
            {},
            function(data) {
                var listaUEBNellaVariazioneCollassate = data.specificaUEB.listaUEBNellaVariazioneCollassate;
                var totaliImporti = data.specificaUEB.elementoImportiVariazione;
                var tabella = $("#tabellaGestioneVariazioni");
                var tabelleGiaInDataTable = $.fn.dataTable.fnTables();


                // Se la tabella è già inizializzata, la pulisco e la distruggo
                if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
                    tabella.dataTable().fnClearTable();
                    tabella.dataTable().fnDestroy();
                }

                impostaDataTableUEBNellaVariazione(tabella, listaUEBNellaVariazioneCollassate);
                // Imposto i dati nella tabella
                impostaTotaliImporti(totaliImporti);
                // Svuoto la dataTable delle UEB da inserire nella variazione
                $("#oggettiDaInserireNellaVariazione").dataTable().fnClearTable();
                $("#oggettiDaInserireNellaVariazione").dataTable().fnDestroy();
                // Chiudo il collapse

            }
        );
    }

    /**
     * Imposta il form di aggiornamento della variazione.
     *
     * @param riga la riga considerata
     */
    function impostaFormVariazioneImportoAggiornamentoRegistrato(riga) {
        var tabellaUEBTrovate = $("#tabellaUEBTrovate");
        var tabellaDaInserireInVariazione = $("#oggettiDaInserireNellaVariazione");
        var tabellaSorgente = $("#tabellaGestioneVariazioni");
        // Pulsante per l'aggiornamento
        var pulsanteRegistrazione = $("#registra");
        // Dati per l'editabilità dei campi
        var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);
        var tabelleInDataTable = $.fn.dataTable.fnTables();
        var elencoUEBDaModificare = datiNellaRiga.listaSottoElementi.slice();
        var oggettoDaInserire = {};
        var p;
        var arrayPerIlDataTable = [];

        // Qualifica l'elenco per l'injezione nell'AJAX
        $.each(elencoUEBDaModificare, function(index, value) {
            // Imposto eventualmente i campi cassa e residuo a 0
            if(value.cassa === undefined || value.cassa === null) {
                value.cassa = 0;
            }
            if(value.residuo === undefined || value.residuo === null) {
                value.residuo = 0;
            }
            elencoUEBDaModificare[index] = qualify(value, "specificaUEB.listaUEBDaInserireNellaVariazione[" + index + "]");
        });

        for(p in elencoUEBDaModificare) {
            if(elencoUEBDaModificare.hasOwnProperty(p)) {
                $.extend(true, oggettoDaInserire, elencoUEBDaModificare[p]);
            }
        }

        // Invocare via AJAX la action per injettare l'elenco delle UEB da modificare nella lista delle UEB da inserire nella variazione
        $.postJSON(
            "popolaListaAggiornamentoImportiNellaVariazione_importiUeb.do",
            oggettoDaInserire,
            function() {
                var q;
                var $collapse = $('#collapseUEB');
                var collapseOpen = $collapse.hasClass('in');

                $("#div_elencoUEB").slideUp();
                $("#span_elencoUEBDaInserire").html("Elenco UEB da aggiornare nella variazione");

                if($(tabelleInDataTable).filter(tabellaUEBTrovate).length > 0) {
                    tabellaUEBTrovate.dataTable().fnClearTable();
                    tabellaUEBTrovate.dataTable().fnDestroy();
                }
                if($(tabelleInDataTable).filter(tabellaDaInserireInVariazione).length > 0) {
                    tabellaDaInserireInVariazione.dataTable().fnClearTable();
                    tabellaDaInserireInVariazione.dataTable().fnDestroy();
                }

                // Dequalifica l'elenco per l'utilizzo di dataTable
                $.each(elencoUEBDaModificare, function(index, value) {
                    elencoUEBDaModificare[index] = unqualify(value, 2);
                });

                for(q in elencoUEBDaModificare) {
                    if(elencoUEBDaModificare.hasOwnProperty(q)) {
                        arrayPerIlDataTable.push(elencoUEBDaModificare[q]);
                    }
                }

                // Imposto staticamente il dataTable delle modifiche da effettuare
                impostaDataTableUEBDaInserireNellaVariazione(tabellaDaInserireInVariazione, arrayPerIlDataTable);

                pulsanteRegistrazione.off("click").on("click", aggiornaVariazioneNellaVariazione);

                // Mostro il div per la modifica del capitolo
                $("#divRicercaCapitolo").slideDown();

                if(collapseOpen){
                	$collapse.trigger('click');
                }

                window.location.hash = "collapseUEB";
            }
        );
    }

    /**
     * Callback per la chiamata AJAX per i dati nella variazione.
     */
    function postChiamataAjaxNellaVariazione(data, tabellaDestinazione, idModale, idAlert) {
        var errori = data.errori;
        var listaCapitoliNellaVariazione = data.specificaUEB.listaUEBNellaVariazioneCollassate;
        var alertErrori = (idAlert !== undefined ? $("#" + idAlert) : undefined);
        var totaliImporti = data.specificaUEB.elementoImportiVariazione;

        // Controllo gli eventuali errori, messaggi, informazioni
        if(idAlert !== undefined) {
            // Nascondo gli alert
            alertErrori.slideUp();
            if(impostaDatiNegliAlert(errori, alertErrori)) {
                return;
            }
        }
        impostaDataTableUEBNellaVariazione(tabellaDestinazione, listaCapitoliNellaVariazione);
        // Imposto gli importi
        impostaTotaliImporti(totaliImporti);
        // Chiudo il modale
        $("#" + idModale).modal("hide");
    }

    /**
     * Elimina la nuova variazione.
     *
     * @param tabellaSorgente     {Object} la tabella sorgente
     * @param tabellaDestinazione {Object} la tabella di destinazione
     * @param riga                {Number} il numero della riga della variazione
     */
    function eliminaVariazioneNellaVariazione(tabellaSorgente, tabellaDestinazione, riga) {
        var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
        // Clone dell'oggetto originale, per effettuare la chiamata
        var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);

        // Qualifico correttamente l'oggetto da inserire
        oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaUEB.elementoCapitoloVariazione");

        $.postJSON(
            "eliminaCapitoliNellaVariazione_importiUeb.do",
            oggettoDaEliminare,
            function(data) {
                postChiamataAjaxNellaVariazione(data, tabellaDestinazione, "msgElimina");
            }
        );
    }

    /**
     * Imposta l'uid del capitolo da eliminare dalla variazione.
     *
     * @param riga la riga considerata
     */
//    function impostaFormVariazioneImportoEliminazioneRegistrato(riga) {
//        var tabellaSorgente = $("#tabellaGestioneVariazioni");
//        var tabellaDestinazione = $("#tabellaGestioneVariazioni");
//        var pulsanteEliminazione = $("#EDIT_elimina");
//
//        pulsanteEliminazione.off("click").on("click", function() {
//            eliminaVariazioneNellaVariazione(tabellaSorgente, tabellaDestinazione, riga);
//        }); // Eliminazione
//    }

    /**
     * Registra nella variazione quanto presente nella tabella.
     */
    function registraImportiNellaVariazione(capitolo, annoVariazione) {

        var oggettoDaInviare = {};
		
		var url = "aggiungiCapitoliNellaVariazione_importiUeb.do";
		var $overlayModale = $('#editStanziamenti').find('.overlay-modale').overlay('show');

		$("#divRicercaCapitolo").slideUp();
		
		oggettoDaInviare.uid = capitolo.uidCapitolo;
		oggettoDaInviare.statoOperativoElementoDiBilancio = capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "";


		oggettoDaInviare.competenza = $("#competenzaVariazioneUEB").val() || 0;
		oggettoDaInviare.residuo = $("#residuoVariazioneUEB").val() || 0;
		oggettoDaInviare.cassa = $("#cassaVariazioneUEB").val() || 0;
		oggettoDaInviare.competenzaOriginale = capitolo.competenzaOriginale;
		oggettoDaInviare.residuoOriginale = capitolo.residuoOriginale;
		oggettoDaInviare.cassaOriginale = capitolo.cassaOriginale;
		
		
		oggettoDaInviare = qualify(oggettoDaInviare, "specificaUEB.elementoCapitoloVariazione");

        return $.postJSON(
            url,
            oggettoDaInviare).then(function(data) {
                var messaggi = data.messaggi;
                var errori = data.errori;
                var alertErrori = $('#ERRORI_modaleEditStanziamenti');
                var $collapseUEB = $("#collapseGestioneUEB");

                if(impostaDatiNegliAlert(errori, alertErrori)) {
                	$("#EDIT_salva").removeAttr('disabled');
    				return;
    			}

                if(messaggi && messaggi.length) {
                	$("#editStanziamenti").modal("hide");
    				gestioneRichiestaProsecuzione(messaggi, url, oggettoDaInviare, data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);
    				return;
    			}

        		$('#tabellaGestioneVariazioni').overlay('show');
        		

                leggiCapitoliNellaVariazione();

                // Chiudo il collapse
                if(!$collapseUEB.hasClass('collapsed')){
                	$collapseUEB.trigger("click");
                }
                $("#editStanziamenti").modal("hide");
            }
        ).always($overlayModale.overlay.bind($overlayModale, 'hide'));
    }
    
    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     *
     * @param e              (Event)   l'evento scatenante l'invocazione
     * @param chiudereAlertErrori (Boolean) se chiudere l'alert degli errori (Optional - default: undefined)
     */
    function cercaCapitoloNellaVariazione(e, /* Optional */ chiudereAlertErrori) {
        var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
        var tipoCapitolo = $("input[name='specificaUEB.tipoCapitolo']:checked").val();
        var annoCapitolo = $("#annoCapitolo").val();
        var numeroCapitolo = $("#numeroCapitolo").val();
        var numeroArticolo = $("#numeroArticolo").val();
            // alert degli errori
        var alertErrori = $("#ERRORI");
            // Array degli errori
        var erroriArray = [];
            // Wrapper per l'injezione di tipo capitolo e tipo applicazione
        var wrapTipoCapitoloApplicazione = tipoCapitolo + (ottieniTipoApplicazioneCapitolo(tipoApplicazione)==="Previsione" ? "Previsione" : "Gestione");
        var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione)==="Previsione" ? "P" : "G");
            // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
        var oggettoPerChiamataAjax = {};
        var ajaxSource = "effettuaRicercaCap" + wrapTipoCapitoloApplicazione + "NellaVariazione.do";
            // Spinner
        var spinner = $("#SPINNER_CapitoloSorgente");
            // Pulsante registrazione nella variazione
        var pulsanteRegistrazione = $("#registra");
            // Per la ricerca delle autorizzazioni dell'utente
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        var divRicercaCapitolo = $("#divRicercaCapitolo");
        var chiusuraAlert = chiudereAlertErrori === undefined ? true : chiudereAlertErrori;

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

        // Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
        if(impostaDatiNegliAlert(erroriArray, alertErrori, true, chiusuraAlert)) {
            return;
        }
        // La validazione JavaScript è andata a buon fine. Proseguire
        !!chiusuraAlert && alertErrori.slideUp();

        divRicercaCapitolo.slideUp();

        oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";
        oggettoPerChiamataAjax.annoImporti = annoVariazione;

        // Disabilito l'onclick presente sul pulsante e imposto il valore di default
        pulsanteRegistrazione.off("click").on("click", registraImportiNellaVariazione);

        spinner.addClass("activated");
       $.postJSON(
            ajaxSource,
            oggettoPerChiamataAjax,
            function(data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;
                var capitoloMassivo = data.capitoloMassivo;
                var listaCapitoli = data.listaCapitoloVariazione;
                var maxUEB = data.maxUEB;
                var listaImporti;
                    // Tabella degli stanziamenti
                var tabellaStanziamenti = $("#tabellaStanziamentiTotali tbody tr");
                    // Tabelle in dataTable
                var tabellaUEB = $("#tabellaUEBTrovate");
                    // Alerts
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                var categoriaCapitoloString;

                // Controllo gli eventuali errori, messaggi, informazioni
                if(impostaDatiNegliAlert(errori, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                if (impostaDatiNegliAlert(messaggi, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                if( impostaDatiNegliAlert(informazioni, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                // Nascondo gli alert
                !!chiusuraAlert && alertErrori.slideUp();
                !!chiusuraAlert && alertMessaggi.slideUp();
                !!chiusuraAlert && alertInformazioni.slideUp();
                // popolo la lista degli importi
                listaImporti = capitoloMassivo["listaImportiCapitolo" + wrapTipoCapitoloApplicazioneReduced];
                categoriaCapitoloString = capitoloMassivo.categoriaCapitolo ? (capitoloMassivo.categoriaCapitolo.codice + "-" + capitoloMassivo.categoriaCapitolo.descrizione) : "";

                // Non vi sono errori, né messaggi, né informazioni.
                // Costruzione dei testi da injettare
                $("#annoCapitoloTrovato").html(capitoloMassivo.annoCapitolo);
                $("#numeroCapitoloArticoloTrovato").html(capitoloMassivo.numeroCapitolo + " / " + capitoloMassivo.numeroArticolo);
                $("#categoriaCapitoloTrovato").html(categoriaCapitoloString);
                $("#descrizioneCapitoloTrovato").html(capitoloMassivo.descrizione);
                $("#maxNumeroUEB").val(maxUEB);

                $("#titoloElencoUEB").html("Elenco UEB capitolo selezionato " + (tipoCapitolo === "Entrata" ? "E" : "S") + "-" + annoCapitolo + "/" + numeroCapitolo + "/" + numeroArticolo);

                // Imposto gli importi nella tabella corrispondente
                $.each(listaImporti, function() {
                    impostaStanziamenti(this, tabellaStanziamenti);
                });

                // Popolamento del dataTable
                impostaDataTableElencoUEB(tabellaUEB, listaCapitoli);

                $("#span_elencoUEBDaInserire").html("Elenco UEB da inserire nella variazione");
                $("#div_elencoUEB").slideDown();
                divRicercaCapitolo.slideDown();
            }
        ).always(
            function() {
            	
                spinner.removeClass("activated");
            }
        );
    }

    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     *
     * @param e              (Event)   l'evento scatenante l'invocazione
     * @param chiudereAlertErrori (Boolean) se chiudere l'alert degli errori (Optional - default: undefined)
     */
    function cercaCapitolo(e, /* Optional */ chiudereAlertErrori) {
        var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
        var tipoCapitolo = $("input[name='specificaUEB.tipoCapitolo']:checked").val();
        var annoCapitolo = $("#annoCapitolo").val();
        var numeroCapitolo = $("#numeroCapitolo").val();
        var numeroArticolo = $("#numeroArticolo").val();
            // alert degli errori
        var alertErrori = $("#ERRORI");
            // Array degli errori
        var erroriArray = [];
            // Wrapper per l'injezione di tipo capitolo e tipo applicazione
        var wrapTipoCapitoloApplicazione = tipoCapitolo + (ottieniTipoApplicazioneCapitolo(tipoApplicazione)==="Previsione" ? "Previsione" : "Gestione");
        var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione)==="Previsione" ? "P" : "G");
            // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
        var oggettoPerChiamataAjax = {};
        var ajaxSource = "effettuaRicercaDettaglioMassivaCap" + wrapTipoCapitoloApplicazione + ".do";
            // Spinner
        var spinner = $("#SPINNER_CapitoloSorgente");
            // Pulsante registrazione nella variazione
        var pulsanteRegistrazione = $("#registra");
            // Per la ricerca delle autorizzazioni dell'utente
        var tipoArticoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        var divRicercaCapitolo = $("#divRicercaCapitolo");
        var chiusuraAlert = chiudereAlertErrori === undefined ? true : chiudereAlertErrori;

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

        // Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
        if(impostaDatiNegliAlert(erroriArray, alertErrori, true, chiusuraAlert)) {
            return;
        }
        // La validazione JavaScript è andata a buon fine. Proseguire
        !!chiusuraAlert && alertErrori.slideUp();

        divRicercaCapitolo.slideUp();

        oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";
        oggettoPerChiamataAjax.annoImporti = annoVariazione;

        // Disabilito l'onclick presente sul pulsante e imposto il valore di default
        pulsanteRegistrazione.off("click").on("click", registraImportiNellaVariazione);

        spinner.addClass("activated");

        // Controllo i pulsanti per inserimento ed annullamento
        $.postJSON(
            "controllaAzioniConsentiteAllUtente_Ueb.do",
            {"specificaUEB.elementoCapitoloVariazione.tipoCapitolo" : tipoArticoloEnum},
            function(data) {
                var inserisci = data.specificaUEB.utenteAbilitatoAdInserimento;
                var annulla = data.specificaUEB.utenteAbilitatoAdAnnullamento;
                var buttonNuovoCapitolo = $("#redirezioneNuovoCapitoloButton");
                var buttonNuovaUEB = $("#redirezioneNuovaUEBButton");
                var buttonAnnullaCapitolo = $("#annullaCapitoloButton");

                if(inserisci) {
                    buttonNuovoCapitolo.show();
                    buttonNuovaUEB.show();
                } else {
                    buttonNuovoCapitolo.hide();
                    buttonNuovaUEB.hide();
                }
                if(annulla) {
                    buttonAnnullaCapitolo.show();
                } else {
                    buttonAnnullaCapitolo.hide();
                }
            }
        );

        $.postJSON(
            ajaxSource,
            oggettoPerChiamataAjax,
            function(data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;
                var capitoloMassivo = data.capitoloMassivo;
                var listaCapitoli = data.listaCapitoloVariazione;
                var maxUEB = data.maxUEB;
                var listaImporti;
                    // Tabella degli stanziamenti
                var tabellaStanziamenti = $("#tabellaStanziamentiTotali tbody tr");
                    // Tabelle in dataTable
                var tabellaUEB = $("#tabellaUEBTrovate");
                    // Alerts
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                var categoriaCapitoloString;

                // Controllo gli eventuali errori, messaggi, informazioni
                if(impostaDatiNegliAlert(errori, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                if (impostaDatiNegliAlert(messaggi, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                if( impostaDatiNegliAlert(informazioni, alertErrori, true, chiusuraAlert)) {
                    return;
                }
                // Nascondo gli alert
                !!chiusuraAlert && alertErrori.slideUp();
                !!chiusuraAlert && alertMessaggi.slideUp();
                !!chiusuraAlert && alertInformazioni.slideUp();
                // popolo la lista degli importi
                listaImporti = capitoloMassivo["listaImportiCapitolo" + wrapTipoCapitoloApplicazioneReduced];
                categoriaCapitoloString = capitoloMassivo.categoriaCapitolo ? (capitoloMassivo.categoriaCapitolo.codice + "-" + capitoloMassivo.categoriaCapitolo.descrizione) : "";

                // Non vi sono errori, né messaggi, né informazioni.
                // Costruzione dei testi da injettare
                $("#annoCapitoloTrovato").html(capitoloMassivo.annoCapitolo);
                $("#numeroCapitoloArticoloTrovato").html(capitoloMassivo.numeroCapitolo + " / " + capitoloMassivo.numeroArticolo);
                $("#categoriaCapitoloTrovato").html(categoriaCapitoloString);
                $("#descrizioneCapitoloTrovato").html(capitoloMassivo.descrizione);
                $("#maxNumeroUEB").val(maxUEB);

                $("#titoloElencoUEB").html("Elenco UEB capitolo selezionato " + (tipoCapitolo === "Entrata" ? "E" : "S") + "-" + annoCapitolo + "/" + numeroCapitolo + "/" + numeroArticolo);

                // Imposto gli importi nella tabella corrispondente
                $.each(listaImporti, function() {
                    impostaStanziamenti(this, tabellaStanziamenti);
                });

                // Popolamento del dataTable
                impostaDataTableElencoUEB(tabellaUEB, listaCapitoli);

                $("#span_elencoUEBDaInserire").html("Elenco UEB da inserire nella variazione");
                $("#div_elencoUEB").slideDown();
                divRicercaCapitolo.slideDown();
            }
        ).always(
            function() {
            	
                spinner.removeClass("activated");
            }
        );
    }

    /**
     * Imposta il dataTable per l'elenco delle UEB.
     *
     * @param tabella       la tabella da paginare
     * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
     */
    function impostaDataTableUEBDaInserireNellaVariazione(tabella, listaCapitoli) {
        if(listaCapitoli === null || listaCapitoli === undefined) {
            listaCapitoli = [];
        }
        var opzioniNuove = {
            aaData : listaCapitoli,
            bDestroy : true,
            // Definizione delle colonne
            aoColumnDefs : [
                {
                    aTargets : [ 0 ],
                    mData : function (source) {
                        var result = source.descrizione ? "<a rel='popover' href='#'>" + source.denominazione + "</a>" : source.denominazione;
                        if(source.datiAccessorii) {
                            result += " <em>(" + source.datiAccessorii + ")</em>";
                        }
                        return result;
                    },
                    fnCreatedCell : function (nTd, sData, oData) {
                        if(oData.descrizione !== null && oData.descrizione !== undefined) {
                            // Settings del popover
                            var settings = {
                                "content" : oData.descrizione,
                                "title" : "Descrizione",
                                "trigger" : "hover"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                        return source.competenza.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        return source.residuo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        return source.cassa.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function() {
                        return "<a href='#editStanziamenti' title='modifica gli importi' role='button' data-toggle='modal'>" +
                                   "<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function (nTd, sData, oData, iRow) {
                        // Imposta l'evento di onClick
                        $(nTd).addClass("text-center")
                            .find("a")
                                .off("click")
                                .on("click", function() {
                                    impostaFormVariazioneImportoAggiornamento(false, iRow);
                                });
                    }
                },
                {
                    aTargets : [ 5 ],
                    mData : function() {
                        return "<a href='#msgElimina' title='elimina' role='button' data-toggle='modal'>" +
                                   "<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function(nTd, sData, oData, iRow) {
                        $(nTd).addClass("text-center")
                            .find("a")
                                .off("click")
                                .on("click", function() {
                                    impostaFormVariazioneImportoEliminazione(iRow);
                                });
                    }
                }
            ]
        };
        var opzioni = $.extend(true, {}, Variazioni.opzioniBaseDataTable, opzioniNuove);
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables();

        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable();
            tabella.dataTable().fnDestroy();
        }

        tabella.dataTable(opzioni);
    }

    /**
     * Imposta il dataTable per l'elenco delle UEB nella variazione.
     *
     * @param tabella       la tabella da paginare
     * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
     */
    function impostaDataTableUEBNellaVariazione(tabella, listaCapitoli) {
        if(listaCapitoli === null || listaCapitoli === undefined) {
            listaCapitoli = [];
        }
        var opzioniNuove = {
            aaData : listaCapitoli,
            bDestroy : true,
            // Definizione delle colonne
            aoColumnDefs : [
                {
                    aTargets : [ 0 ],
                    mData : function (source) {
                        var result = source.descrizione ? "<a rel='popover' href='#'>" + source.denominazione + "</a>" : source.denominazione;
                        if(source.datiAccessorii) {
                            result += " <em>(" + source.datiAccessorii + ")</em>";
                        }
                        return result;
                    },
                    fnCreatedCell : function (nTd, sData, oData) {
                        if(oData.descrizione !== null && oData.descrizione !== undefined) {
                            // Settings del popover
                            var settings = {
                                "content" : oData.descrizione,
                                "title" : "Descrizione",
                                "trigger" : "hover"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                        var importo = source.competenza || 0;
                        return importo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        var importo = source.residuo || 0;
                        return importo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        var importo = source.cassa || 0;
                        return importo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function() {
                        return "<a href='#' title='modifica gli importi'>" +
                                   "<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function (nTd, sData, oData, iRow) {
                        // Imposta l'evento di onClick
                        $(nTd).addClass("text-center")
                            .find("a")
                                .off("click")
                                .on("click", function(e) {
                                    e.preventDefault();
                                    impostaFormVariazioneImportoAggiornamentoRegistrato(iRow);
                                });
                    }
                },
                {
                    aTargets : [ 5 ],
                    mData : function() {
                        return "<a href='#msgElimina' title='elimina' role='button' data-toggle='modal'>" +
                                   "<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
                               "</a>";
                    },
                    fnCreatedCell : function(nTd, sData, oData, iRow) {
                        $(nTd).addClass("text-center")
                            .find("a")
                                .off("click")
                                .on("click", function() {
                                    impostaFormVariazioneImportoEliminazioneRegistrato(iRow);
                                });
                    }
                }
            ]
        };
        var opzioni = $.extend(true, {}, Variazioni.opzioniBaseDataTable, opzioniNuove);
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables();

        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable();
            tabella.dataTable().fnDestroy();
        }

        tabella.dataTable(opzioni);
    }

    /**
     * Imposta il totale degli importi della variazione.
     *
     * @param totaliImporti {Object} il totale degli importi
     */
    function impostaTotaliImporti(totaliImporti) {
        Variazioni.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totaliImporti.totaleEntrataCompetenza);
        Variazioni.impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totaliImporti.totaleEntrataResiduo);
        Variazioni.impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totaliImporti.totaleEntrataCassa);

        Variazioni.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totaliImporti.totaleSpesaCompetenza);
        Variazioni.impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totaliImporti.totaleSpesaResiduo);
        Variazioni.impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totaliImporti.totaleSpesaCassa);

        Variazioni.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", totaliImporti.differenzaCompetenza);
        Variazioni.impostaValutaEAllineaADestra("#differenzaResiduoVariazione", totaliImporti.differenzaResiduo);
        Variazioni.impostaValutaEAllineaADestra("#differenzaCassaVariazione", totaliImporti.differenzaCassa);
    }

    /**
     * Effettua una nuova ricerca.
     */
    function effettuaNuovaRicerca() {
        var divRicerca = $("#divRicercaCapitolo");
        var numeroCapitolo = $("#numeroCapitolo");
        var numeroArticolo = $("#numeroArticolo");
        var tipoCapitolo = $("input[name='specificaUEB.tipoCapitolo']");
        
        $("#tabellaUEBTrovate").dataTable();
        $("#oggettiDaInserireNellaVariazione").dataTable();

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
       
    	var form = $("#formVariazioneStep3_VariazioneBilancioConUEB");
        var alertErrori = $("#ERRORI");

	    var tipoCapitoloSelezionato = $("input[name='specificaUEB.tipoCapitolo']:checked").val();
        var applicazione = ottieniTipoApplicazioneCapitolo($("#HIDDEN_tipoApplicazione").val()) ;
        var tipoCapitoloEnum; 
        // Se non è stato selezionato un tipo di capitolo, annullare l'azione
        if(!tipoCapitoloSelezionato) {
            impostaDatiNegliAlert(["Il tipo di capitolo deve essere selezionato"], alertErrori);
            return;
        }
        tipoCapitoloEnum = "CAPITOLO_" + (tipoCapitoloSelezionato === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (applicazione === "Previsione" ? "PREVISIONE" : "GESTIONE");
        $('#redirezioneNuovoCapitoloButton').overlay('show');
        $.postJSON("controllaAzioniConsentiteAllUtente.do", {"specificaUEB.elementoCapitoloVariazione.tipoCapitolo" : tipoCapitoloEnum})
        .then(function(data){
        	 var inserisci = data.specificaImporti.utenteAbilitatoAdInserimento;
        	 if(!inserisci && impostaDatiNegliAlert(['COR_ERR_0044 : Operazione non consentita: inserimento Capitolo' + ' ' + tipoCapitoloSelezionato + ' ' + applicazione], alertErrori)){
        		 return;
        	 }
        	 form.attr("action", "redirezioneVersoNuovoCapitolo_importiUeb.do").submit();

        }).always($('#redirezioneNuovoCapitoloButton').overlay.bind($('#redirezioneNuovoCapitoloButton'), 'hide'));
    }

    /**
     * Redirige verso una nuova UEB.
     */
    function redirezioneNuovoUEB() {
    	var form = $("#formVariazioneStep3_VariazioneBilancioConUEB");
    	var alertErrori = $('#ERRORI');
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
        $.postJSON("controllaAzioniConsentiteAllUtente.do", {"specificaImporti.elementoCapitoloVariazione.tipoCapitolo" : tipoCapitoloEnum})
        .then(function(data){
        	 var inserisci = data.specificaImporti.utenteAbilitatoAdInserimento;
        	 if(!inserisci && impostaDatiNegliAlert(['COR_ERR_0044 : Operazione non consentita: inserimento Capitolo' + ' ' + tipoCapitoloSelezionato + ' ' + applicazione], alertErrori)){
        		 return;
        	 }
        	 form.attr("action", "redirezioneVersoNuovoCapitolo.do").submit();

        }).always($('#redirezioneNuovoCapitoloButton').overlay.bind($('#redirezioneNuovoCapitoloButton'), 'hide'));
       

        // Modifico l'action del form e faccio un submit
        form.attr("action", "redirezioneVersoNuovaUEB_importiUeb.do")
            .submit();
    }

    /**
     * Annulla il capitolo e tutte le ueb corrispondenti
     */
     function annullaCapitolo() {
        var varieUEB = $("#tabellaUEBTrovate").dataTable().fnGetData();
        var clone = $.extend(true, {}, varieUEB);
        var oggettoDaInserire = {};
        var form = $('form');
        form.addClass('form-submitted');

        // Carico ogni UEB nella lista in sessione
        $.each(clone, function(index, value) {
            var qualifiedValue = qualify(value, "specificaUEB.listaUEBDaAnnullare[" + index + "]");
            $.extend(true, oggettoDaInserire, qualifiedValue);
        });

        $.postJSON(
            "annullaCapitoli_importiUeb.do",
            oggettoDaInserire,
            function(data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;
                var tabella = $("#tabellaGestioneVariazioni");
                // Alerts
                var alertErrori = $("#ERRORI");
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                var tabelleGiaInDataTable = $.fn.dataTable.fnTables();

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

                // Se la tabella è già inizializzata, la pulisco e la distruggo
                if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
                    tabella.dataTable().fnClearTable(false);
                    tabella.dataTable().fnDestroy();
                }

                leggiCapitoliNellaVariazione();
                // Chiudo il collapse

            }
        ).always(form.removeClass.bind(form, 'form-submitted'));
    }

    /**
     * Imposta il valore presente nella variazione di competenza anche come variazione della cassa, nel caso in cui
     * la variazione degli importi di cassa sia possibile.
     */
    function impostaValoreCassaSeApplicabile() {
        var inputCompetenza = $("#competenzaVariazioneUEB");
        var inputCassa = $("#cassaVariazioneUEB");

        if(!inputCassa.attr("disabled") && inputCassa.val() === "") {
            inputCassa.val(inputCompetenza.val());
        }
    }

    return varImp;
} (Variazioni || {}));

$(
    function() {
        var pulsanteRicercaCapitolo = $("#pulsanteRicercaCapitolo");

        pulsanteRicercaCapitolo.on("click", VariazioniImportiUEB.cercaCapitolo);
        $("#redirezioneNuovoCapitoloButton").on("click", VariazioniImportiUEB.redirezioneNuovoCapitolo);
        $("#annullaCapitoloButton").on("click", VariazioniImportiUEB.annullaCapitolo);
        $("#redirezioneNuovaUEBButton").on("click", VariazioniImportiUEB.redirezioneNuovoUEB);
        $("#nuovaRicerca").on("click", VariazioniImportiUEB.effettuaNuovaRicerca);
        $("#salvaEProseguiButton").on("click", VariazioniImportiUEB.salvaEProsegui);

        $("#competenzaVariazioneUEB").on("blur", VariazioniImportiUEB.impostaValoreCassaSeApplicabile);

        VariazioniImportiUEB.leggiCapitoliNellaVariazione();

        $("#collapseGestioneUEB").substituteHandler("click", function() {
            var $collapse = $('#collapseUEB');
        	var collapseOpen = $collapse.hasClass('in');
            $collapse[collapseOpen? 'slideUp' : 'slideDown']()[collapseOpen? 'removeClass' : 'addClass']('in');
        });
        
        // SIAC-5016
        $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'inserisciVariazioneImportiUEB_download.do', false));
        $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'inserisciVariazioneImportiUEB_download.do', true));
      //SIAC-5016
        $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", VariazioniImportiUEB.cercaCapitoloNellaVariazione);
    }
);