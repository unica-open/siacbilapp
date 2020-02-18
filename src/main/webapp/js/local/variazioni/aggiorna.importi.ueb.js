/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per l'aggiornamento della variazione di importi con gestione UEB */

var VariazioniImporti = (function(varImp){

	varImp.impostaValoreCassaSeApplicabile = impostaValoreCassaSeApplicabile;
	varImp.gestioneRichiestaProsecuzioneSuSquadraturaCassa = gestioneRichiestaProsecuzioneSuSquadraturaCassa;
	varImp.chiediConfermaAnnullamento = chiediConfermaAnnullamento;
	//capitoli
	varImp.leggiCapitoliNellaVariazione = leggiCapitoliNellaVariazione;
	varImp.cercaCapitolo = cercaCapitolo;
	varImp.cercaCapitoloNellaVariazione = cercaCapitoloNellaVariazione;
	varImp.redirezioneNuovoCapitolo = redirezioneNuovoCapitolo;
	varImp.annullaCapitolo = annullaCapitolo;
	varImp.registraImportiNellaVariazione = registraImportiNellaVariazione;
	varImp.salvaVariazione = salvaVariazione;
	varImp.concludiAggiornamentoVariazione = concludiAggiornamentoVariazione;
	varImp.redirezioneNuovoUEB = redirezioneNuovoUEB;

	function ottieniTipoApplicazioneCapitolo(tipoApplicazione){
		var tipoApplicazioneNotUndefined = tipoApplicazione || $("#HIDDEN_tipoApplicazione").val();
		return (tipoApplicazioneNotUndefined === "Previsione" || tipoApplicazioneNotUndefined === "PREVISIONE" ? "Previsione" : "Gestione");
	}
	
	/**
	 * Effettua la ricerca del capitolo per creare una variazione.
	 */
	function cercaCapitoloNellaVariazione() {
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
	    var wrapTipoCapitoloApplicazione = tipoCapitolo + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "Previsione" : "Gestione");
	    var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "P" : "G");
	        // Dati per la creazione della chiamata AJAX
	    var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione + "NellaVariazione";
	    var oggettoPerChiamataAjax = {};
	    var ajaxSource = "effettuaRicercaDettaglioMassivaCap" + wrapTipoCapitoloApplicazione + ".do";
	        // Spinner
	    var spinner = $("#SPINNER_CapitoloSorgente");
	        // Pulsante registrazione nella variazione
	        // Per la ricerca delle autorizzazioni dell'utente
	    var tipoArticoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
	    var annoVariazione = $("#HIDDEN_annoVariazione").val();

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
	    if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
	        return;
	    }
	    // La validazione JavaScript è andata a buon fine. Proseguire
	    !!arguments[0] && alertErrori.slideUp();

	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";
	    oggettoPerChiamataAjax.annoImporti = annoVariazione;

	    spinner.addClass("activated");

	    // Controllo i pulsanti per inserimento ed annullamento
	    $.postJSON(
	        "controllaAzioniConsentiteAllUtente_Ueb_aggiornamento.do",
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
	            //var tabellaDaInserireNellaVariazione = $("#oggettiDaInserireNellaVariazione");
	                // Alerts
	            var alertMessaggi = $("#MESSAGGI");
	            var alertInformazioni = $("#INFORMAZIONI");
	            var categoriaCapitoloString;
	
	            var $collapse = $('#collapseUEB');
                var collapseOpen = $collapse.hasClass('in');

	            // Controllo gli eventuali errori, messaggi, informazioni
	            if(impostaDatiNegliAlert(errori, alertErrori)) {
	                return;
	            }
	            if(impostaDatiNegliAlert(messaggi, alertErrori)) {
	                return;
	            }
	            if(impostaDatiNegliAlert(informazioni, alertErrori)) {
	                return;
	            }
	            // Nascondo gli alert
	            alertErrori.slideUp();
	            alertMessaggi.slideUp();
	            alertInformazioni.slideUp();

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
	            $("#divRicercaCapitolo").slideDown();
	            if(collapseOpen){
                	$collapse.trigger('click');
                }
	        }
	    ).always(
	        function() {
	            spinner.removeClass("activated");
	
	        }
	    );

	}
	
	/**
	 * Effettua la ricerca del capitolo per creare una variazione.
	 */
	function cercaCapitolo() {
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
	    var wrapTipoCapitoloApplicazione = tipoCapitolo + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "Previsione" : "Gestione");
	    var wrapTipoCapitoloApplicazioneReduced = (tipoCapitolo === "Entrata" ? "E" : "U") + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "P" : "G");
	        // Dati per la creazione della chiamata AJAX
	    var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
	    var oggettoPerChiamataAjax = {};
	    var ajaxSource = "effettuaRicercaDettaglioMassivaCap" + wrapTipoCapitoloApplicazione + ".do";
	        // Spinner
	    var spinner = $("#SPINNER_CapitoloSorgente");
	        // Pulsante registrazione nella variazione
	        // Per la ricerca delle autorizzazioni dell'utente
	    var tipoArticoloEnum = "CAPITOLO_" + (tipoCapitolo === "Entrata" ? "ENTRATA" : "USCITA") + "_" + (ottieniTipoApplicazioneCapitolo(tipoApplicazione) === "Previsione" ? "PREVISIONE" : "GESTIONE");
	    var annoVariazione = $("#HIDDEN_annoVariazione").val();

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
	    if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
	        return;
	    }
	    // La validazione JavaScript è andata a buon fine. Proseguire
	    !!arguments[0] && alertErrori.slideUp();

	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
	    oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";
	    oggettoPerChiamataAjax.annoImporti = annoVariazione;

	    spinner.addClass("activated");

	    // Controllo i pulsanti per inserimento ed annullamento
	    $.postJSON(
	        "controllaAzioniConsentiteAllUtente_Ueb_aggiornamento.do",
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
	            //var tabellaDaInserireNellaVariazione = $("#oggettiDaInserireNellaVariazione");
	                // Alerts
	            var alertMessaggi = $("#MESSAGGI");
	            var alertInformazioni = $("#INFORMAZIONI");
	            var categoriaCapitoloString;
	
	            var $collapse = $('#collapseUEB');
                var collapseOpen = $collapse.hasClass('in');

	            // Controllo gli eventuali errori, messaggi, informazioni
	            if(impostaDatiNegliAlert(errori, alertErrori)) {
	                return;
	            }
	            if(impostaDatiNegliAlert(messaggi, alertErrori)) {
	                return;
	            }
	            if(impostaDatiNegliAlert(informazioni, alertErrori)) {
	                return;
	            }
	            // Nascondo gli alert
	            alertErrori.slideUp();
	            alertMessaggi.slideUp();
	            alertInformazioni.slideUp();

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
	            $("#divRicercaCapitolo").slideDown();
	            if(collapseOpen){
                	$collapse.trigger('click');
                }
	        }
	    ).always(
	        function() {
	            spinner.removeClass("activated");
	
	        }
	    );

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
	function impostaTotaliImportis(totaliImporti) {
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
	
	function rimuoviOverlay(){
    	$("#tabellaGestioneVariazioni").overlay('hide');
    	$('form').overlay('hide');
    }

    function richiamoServizioPersistenzaImporti(url, ajaxParam){
    	$("#tabellaGestioneVariazioni").overlay('show');
    	return $.postJSON(url, ajaxParam)
			.then(function(data) {
				var errori = data.errori;
    			var messaggi = data.messaggi;
    			// Alert errori
    			var alertErrori = $("#ERRORI");
				if(impostaDatiNegliAlert(errori, alertErrori)) {
				    $('form').overlay('hide');
					return;
				}
				if(messaggi && messaggi.length >0) {
        			gestioneRichiestaProsecuzione(messaggi, url, ajaxParam,data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);	
        			return;
				}
				$('#pulsanteApriRicercaCapitolo').trigger('click');
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
     * Richiede la conferma di prosecuzione nel caso in cui il servizio abbia
     * riscontrato una squadratura sulla cassa
     * */
    function gestioneRichiestaProsecuzioneSuSquadraturaCassa (){
        bootboxAlert('Non vi &eacute; quadratura sulla cassa. Proseguire ugualmente con il salvataggio?', 'Attenzione', 'dialogWarn', [
            {
                "label" : "No, indietro"
                , "class" : "btn"
                , "callback": function(){
                }
            }
            , {
                "label" : "si, prosegui"
                , "class" : "btn"
                , "callback" : function() {
                    $('form').append('<input type= "hidden" name ="saltaCheckStanziamentoCassa" value ="true" >');
                    aggiornaAnagraficaVariazioneAsync('concludi');
                }
            }]);
    }

    /**
     * Se il servizio viene implementato in modo asincrono permette all'utente di scegliere se rimanere
     * sulla pagina oppure tornare alla home
     */
    function showDialogAbbandonoPaginaSuServizioAsincrono(operazione) {
        bootboxAlert('L\'elaborazione non &egrave ancora terminata. &Egrave possibile rimanere sulla pagina oppure tornare alla home.', 'Attenzione', 'dialogWarn', [
            {
                "label" : "Torna alla home"
                , "class" : "btn"
                , "callback": function(){
                    document.location="/siacbilapp/redirectToCruscotto.do";
                }
            }
            , {
                "label" : "Rimani sulla pagina"
                , "class" : "btn"
                , "callback" : function() {
                    setTimeout(ottieniResponse,30000,operazione, 50, 30000);
                }
            }]);
    }


	/**
	 * Associa alla variazione un capitolo
	 */
	function registraImportiNellaVariazione(capitolo, annoVariazione) {

	    var oggettoDaInviare = {};

		var url = "aggiungiCapitoliNellaVariazione_importiUeb_aggiornamento.do";
		var $overlayModale = $('#editStanziamenti').find('.overlay-modale').overlay('show');
		
		$('#tabellaGestioneVariazioni').overlay('show');
		
		
		
		oggettoDaInviare.uid = capitolo.uidCapitolo;
		oggettoDaInviare.statoOperativoElementoDiBilancio = capitolo.statoOperativoElementoDiBilancio ? capitolo.statoOperativoElementoDiBilancio._name : "";


		oggettoDaInviare.competenza = $("#competenzaVariazioneUEB").val() || 0;
		oggettoDaInviare.residuo = $("#residuoVariazioneUEB").val() || 0;
		oggettoDaInviare.cassa = $("#cassaVariazioneUEB").val() || 0;
		oggettoDaInviare.competenzaOriginale = capitolo.competenzaOriginale;
		oggettoDaInviare.residuoOriginale = capitolo.residuoOriginale;
		oggettoDaInviare.cassaOriginale = capitolo.cassaOriginale;


		oggettoDaInviare = qualify(oggettoDaInviare, "specificaUEB.elementoCapitoloVariazione");

	    $.postJSON( url, oggettoDaInviare)
	    	.then(function(data) {
	            var messaggi = data.messaggi;
	            var errori = data.errori;
	            var alertErrori = $('#ERRORI_modaleEditStanziamenti');
	
	            if(impostaDatiNegliAlert(errori, alertErrori)) {
	            	$overlayModale.overlay('hide');
	            	$("#EDIT_salva").removeAttr('disabled');
					return;
				}

	            if(messaggi && messaggi.length) {
	            	$("#editStanziamenti").modal("hide");
	            	$overlayModale.overlay('hide');	 
	            	$("#EDIT_salva").removeAttr('disabled');
					//gestioneRichiestaProsecuzioneRegistrazione(messaggi, url, oggettoDaInviare, tabelleGiaInDataTable, tabella, alertErrori);
	            	gestioneRichiestaProsecuzione(messaggi, url, oggettoDaInviare, data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);
	            	return;
				}

                leggiCapitoliNellaVariazione();

                $overlayModale.overlay('hide');
                $("#EDIT_salva").removeAttr('disabled');
                $('#pulsanteApriRicercaCapitolo').trigger('click');
                $("#editStanziamenti").modal("hide");
	        }
	    );
	}


	
	/**
	 * Imposta il dataTable per l'elenco delle UEB all'interno del collapse. la tabella che viene popolata risulta essere la tabella #tabellaUEBtrovate.
	 * (rifacimento variazioni)
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
	                                impostaModaleModificaImportoUEB(true, iRow);
	                            });
	                }
	            }
	        ]
	    };
	    var opzioni = $.extend(true, {}, varImp.opzioniBaseDataTable, opzioniNuove);
	    var tabelleGiaInDataTable = $.fn.dataTable.fnTables();


	    if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
	        tabella.dataTable().fnClearTable();
	        tabella.dataTable().fnDestroy();
	    }

	    tabella.dataTable(opzioni);
	}

	/**
	 * Imposta il dataTable delle singole UEB che sono state associate alla variazione.
	 *
	 * @param tabella       la tabella da paginare
	 * (rifacimento variazioni)
	 */

	function impostaDataTableUEBNellaVariazione() {

				var isEditable = $("input[name='specificaUEB.tipoCapitolo']").length !== 0;
				var tabella = $("#tabellaGestioneVariazioni");

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
							                	fnCreatedCell : function (nTd, sData, oData, iRow) {
							                			// Imposta l'evento di onClick
							                		if(isEditable){
							                			$(nTd).addClass("text-center")
							                			.find("a").off("click")
							                			.on("click", function() {
							                				impostaModaleModificaImportoUEB(false,iRow);
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
							                				impostaFormVariazioneImportoEliminazioneRegistrato(iRow);
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
	 * Imposta il form di aggiornamento o di inserimento degli importi di una UEB della variazione.
	 *
	 * @param nuovo     se l'UEB &eacute; nuova o meno
	 * @param riga      la riga considerata
	 */
	function impostaModaleModificaImportoUEB(nuovo, riga) {
		var tabellaSorgente = nuovo ? $("#tabellaUEBTrovate") : $("#tabellaGestioneVariazioni");

	    var tabellaDestinazione = $("#oggettiDaInserireNellaVariazione");
	    // Pulsante per il salvataggio

	    // Dati per l'editabilità dei campi
	    var annoVariazione = $("#HIDDEN_annoVariazione").val();
	    var datiNellaRiga = tabellaSorgente.dataTable().fnGetData(riga);

	    // Nascondo l'eventuale alert di errore già presente
	    $("#ERRORI_modaleEditStanziamenti").slideUp();
	    //FIXME: questa cosa si puo' semplificare
	    if(nuovo) {
	    	impostaModaleModificaImportoUEBDaCollegareAVariazione(datiNellaRiga, annoVariazione);
	    } else {
	    	impostaModaleModificaImportoUEBCollegataAVariazione(datiNellaRiga, annoVariazione, tabellaSorgente, tabellaDestinazione);
	    }

	}
	/***
	 * Imposta la modale per la modifica degli importi della singola UEB
	 *
	 * */
	function impostaModaleModificaImportoUEBDaCollegareAVariazione(datiNellaRiga, annoVariazione){
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
	    	pulsanteSalvataggio.attr('disabled','');
	    	registraImportiNellaVariazione(capitolo, annoVariazione);
	    }); // Salvataggio
	}

	function impostaModaleModificaImportoUEBCollegataAVariazione(datiNellaRiga, annoVariazione, tabellaSorgente, tabellaDestinazione){
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
		var url = "aggiornaCapitoliNellaVariazione_importiUeb_aggiornamento.do";
		var ajaxParam;
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaAggiornare = {};
		var competenza = $("#competenzaVariazioneUEB").val();
		var residuo = $("#residuoVariazioneUEB").val();
		var cassa = $("#cassaVariazioneUEB").val();
		var $overlayModale = $('#editStanziamenti').find('.overlay-modale').overlay('show');

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
		
		return $.postJSON(url, ajaxParam)
		.then(function(data) {
			var errori = data.errori;
			var alertErrori = $("#ERRORI_modaleEditStanziamenti");
			var modale = $("#editStanziamenti");
			var messaggi = data.messaggi;
			// Nascondo gli alert
			alertErrori.slideUp();
			if(impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}
			if(messaggi && messaggi.length) {
				modale.modal('hide');
				//gestioneRichiestaProsecuzione(data.messaggi, url, ajaxParam, tabellaDestinazione, alertErrori, modale);
				gestioneRichiestaProsecuzione(messaggi, url, ajaxParam,data.specificaUEB && data.specificaUEB.cassaIncongruente, data.specificaUEB && data.specificaUEB.cassaIncongruenteDopoDefinizione);
				return;
			}
			modale.modal('hide');

			leggiCapitoliNellaVariazione();
		}).always($overlayModale.overlay.bind($overlayModale, 'hide'));
	}

	/**
	 * Imposta l'uid del capitolo da eliminare dalla variazione.
	 *
	 * @param riga      la riga considerata
	 */
	//TODO: verificare, probabilmente si può togliere
	function impostaFormVariazioneImportoEliminazione(riga) {
	    var tabellaSorgente = $("#oggettiDaInserireNellaVariazione");
	    var tabellaDestinazione = $("#oggettiDaInserireNellaVariazione");
	    var pulsanteEliminazione = $("#EDIT_elimina");

	    pulsanteEliminazione.off("click").on("click", function() {
	        eliminaVariazioneDaAggiungere(tabellaSorgente, tabellaDestinazione, riga);
	    }); // Eliminazione
	}

	/**
	 * Imposta l'uid del capitolo da eliminare dalla variazione.
	 *
	 * @param riga la riga considerata
	 */
	function impostaFormVariazioneImportoEliminazioneRegistrato(riga) {
	    var tabellaSorgente = $("#tabellaGestioneVariazioni");
	    var pulsanteEliminazione = $("#EDIT_elimina");

	    pulsanteEliminazione.off("click").on("click", function() {
	        eliminaCapitoloNellaVariazione(tabellaSorgente, riga);
	    }); // Eliminazione
	}

	function eliminaCapitoloNellaVariazione(tabellaSorgente, riga) {

		var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
		// Clone dell'oggetto originale, per effettuare la chiamata
		var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);
		var $overlayModale = $('#msgElimina').find('.overlay-modale').overlay('show');

		oggettoDaEliminare.uid = oggettoOriginale.uid;

		// Qualifico correttamente l'oggetto da inserire
		oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaUEB.elementoCapitoloVariazione");
		return $.postJSON("eliminaCapitoliNellaVariazione_importiUeb_aggiornamento.do", oggettoDaEliminare)
		.then(function() {
			impostaDatiNegliAlert(["COR_INF_0006 - Operazione effettuata correttamente"],$('#INFORMAZIONI'));
			$('form').overlay('show');
			leggiCapitoliNellaVariazione();
			$('#msgElimina').modal('hide');
		}).always($overlayModale.overlay.bind($overlayModale, 'hide'));
	}


	/**
	 * Elimina la nuova variazione.
	 */
	function eliminaVariazioneDaAggiungere(tabellaSorgente, tabellaDestinazione, riga) {
	    var oggettoOriginale = tabellaSorgente.dataTable().fnGetData(riga);
	    // Clone dell'oggetto originale, per effettuare la chiamata
	    var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);
	    var $overlayModale = $('#msgElimina').find('.overlay-modale').overlay('show');
	    // Qualifico correttamente l'oggetto da inserire
	    oggettoDaEliminare = qualify(oggettoDaEliminare, "specificaUEB.elementoCapitoloVariazione");

	    $.postJSON(
	        "eliminaCapitoliDaInserireNellaVariazione_importiUeb_aggiornamento.do",
	        oggettoDaEliminare)
	        .then( function(data) {
	            postChiamataAjaxInserireNellaVariazione(data, tabellaDestinazione, "msgElimina");
	        }).always($overlayModale.overlay.bind($overlayModale, 'hide'));
	}




	/**
	 * Effettua una nuova ricerca.
	 */
	function effettuaNuovaRicerca() {
	    var divRicerca = $("#divRicercaCapitolo");
	    var numeroCapitolo = $("#numeroCapitolo");
	    var numeroArticolo = $("#numeroArticolo");
	    var tipoCapitolo = $("input[name='specificaUEB.tipoCapitolo']");
	    
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

	function impostaTotaliInDataTable(totaleStanziamentiEntrata, totaleStanziamentiCassaEntrata, totaleStanziamentiResiduiEntrata,
			totaleStanziamentiSpesa, totaleStanziamentiCassaSpesa, totaleStanziamentiResiduiSpesa){

		var totStanziamentiEntrata = totaleStanziamentiEntrata || 0;
	    var totStanziamentiCassaEntrata = totaleStanziamentiCassaEntrata || 0;
	    var totStanziamentiResiduiEntrata = totaleStanziamentiResiduiEntrata || 0;

	    var totStanziamentiSpesa = totaleStanziamentiSpesa || 0;
	    var totStanziamentiCassaSpesa = totaleStanziamentiCassaSpesa || 0;
	    var totStanziamentiResiduiSpesa = totaleStanziamentiResiduiSpesa || 0;

	    varImp.impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totStanziamentiEntrata);
	    varImp.impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totStanziamentiResiduiSpesa);
	    varImp.impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totStanziamentiCassaEntrata);

	    varImp.impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totStanziamentiSpesa);
	    varImp.impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totStanziamentiResiduiSpesa);
	    varImp.impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totStanziamentiCassaSpesa);

	    varImp.impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", (totStanziamentiEntrata - totStanziamentiSpesa));
	    varImp.impostaValutaEAllineaADestra("#differenzaResiduoVariazione", (totStanziamentiResiduiEntrata - totStanziamentiResiduiSpesa));
	    varImp.impostaValutaEAllineaADestra("#differenzaCassaVariazione", (totStanziamentiCassaEntrata - totStanziamentiCassaSpesa));
	}


	/**
	 * Chiamata Ajax per ottenere la lista dei Capitoli associati alla variazione
	 * e inizializzare dataTable
	 * */

	// Richiamo i capitoli presenti nella variazione
	function leggiCapitoliNellaVariazione(){

		$('#tabellaGestioneVariazioni').overlay('show');
		$.postJSON("leggiCapitoliNellaVariazione_importiUeb_aggiornamento.do",{})
			.then(function(data) {

				var errori = data.errori;
				var messaggi = data.messaggi;
				var informazioni = data.informazioni;
				var alertErrori = $('#ERRORI');
				var alertMessaggi = $("#MESSAGGI");
				var alertInformazioni = $("#INFORMAZIONI");

				// Controllo gli eventuali errori, messaggi, informazioni
				if(impostaDatiNegliAlert(errori, alertErrori)) {
					$("#EDIT_salva").removeAttr('disabled');
					return;
				}
				if(impostaDatiNegliAlert(messaggi, alertMessaggi)) {
					$("#EDIT_salva").removeAttr('disabled');
						return;
				}
				if(impostaDatiNegliAlert(informazioni, alertInformazioni)) {
					$("#EDIT_salva").removeAttr('disabled');
					return;
				}

				$('form').overlay('hide');
				$("#EDIT_salva").removeAttr('disabled');
				//chiamo dataTable
				impostaDataTableUEBNellaVariazione();

				impostaTotaliInDataTable(data.totaleStanziamentiEntrata, data.totaleStanziamentiCassaEntrata, data.totaleStanziamentiResiduiEntrata,
							data.totaleStanziamentiSpesa, data.totaleStanziamentiCassaSpesa, data.totaleStanziamentiResiduiSpesa);
				}
			);
		}


	/**
	 * Redirige verso un nuovo capitolo se e solo se il tipo di capitolo &eacute; stato valorizzato.
	 */
	function redirezioneNuovoCapitolo() {
	    var form = $("#aggiornaVariazioneImportiConUEB");
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
        	 if(!inserisci && impostaDatiNegliAlert(['COR_ERR_0044 : Operazione non consentita: inserimento capitolo' + tipoCapitoloSelezionato + ' ' + applicazione], alertErrori)){
        		 return;
        	 }
        	 form.attr("action", "redirezioneVersoNuovoCapitolo_importiUeb_aggiornamento.do").submit();

        }).always($('#redirezioneNuovoCapitoloButton').overlay.bind($('#redirezioneNuovoCapitoloButton'), 'hide'));
	    // Modifico l'action del form e faccio un submit
	}

	/**
	 * Redirige verso una nuova UEB.
	 */
	function redirezioneNuovoUEB() {
	    var form = $("#aggiornaVariazioneImportiConUEB");

	    // Modifico l'action del form e faccio un submit
	    form.attr("action", "redirezioneVersoNuovaUEB_importiUeb_aggiornamento.do")
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
	
	    form.overlay('show');
	
	    // Carico ogni UEB nella lista in sessione
	    $.each(clone, function(index, value) {
	        var qualifiedValue = qualify(value, "specificaUEB.listaUEBDaAnnullare[" + index + "]");
	        $.extend(true, oggettoDaInserire, qualifiedValue);
	    });
	
	    $.postJSON(
	        "annullaCapitoli_importiUeb_aggiornamento.do",
	        oggettoDaInserire,
	        function(data) {
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
		
	            leggiCapitoliNellaVariazione();
	            	
	            $('#pulsanteApriRicercaCapitolo').trigger('click');
	        }
	    //).always(form.removeClass.bind(form, 'form-submitted'));
	    ).always(form.overlay.bind(form, 'hide'));
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

	function salvaVariazione(){
	    aggiornaAnagraficaVariazioneAsync('salva');
	}

	function concludiAggiornamentoVariazione(){
	    aggiornaAnagraficaVariazioneAsync('concludi');
	}

	function chiediConfermaAnnullamento(){
    	$('#msgAnnulla').modal('show');
    	$('#EDIT_annulla').substituteHandler("click",annullaAnagraficaVariazioneAsync);
    }

	function annullaAnagraficaVariazioneAsync(){
		$('#msgAnnulla').modal('hide');
	    aggiornaAnagraficaVariazioneAsync('annulla');
	}

	function aggiornaAnagraficaVariazioneAsync(operazione){

	    var obj = qualify($('form').serializeObject());

	    $(document.body).overlay('show');

	    return $.postJSON(operazione + 'AggiornamentoVariazioneImportiUEB.do', obj)
	    .then(function(data) {
	        var alertErrori = $('#ERRORI');
	        alertErrori.slideUp();
	        if (impostaDatiNegliAlert(data.errori,alertErrori)) {
	        	resettaSpinnerFormSubmitted();
	            return;
	        }
	        ottieniResponse(operazione, 10, 10000);
	    });
	}


	function ottieniResponse(operazione, tentativiRimanenti, timeout){
	    var url = operazione + 'AggiornamentoVariazioneImportiUEBAsyncResponse.do';
	
	    return $.postJSON(url)
	    .then(function(data) {
	        var alertErrori = $('#ERRORI');

	        alertErrori.slideUp();
	        if (impostaDatiNegliAlert(data.errori,alertErrori)) {
	        	$(document.body).overlay('hide');
	        	redirezioneAPaginaDisabilitata();
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
	            	showDialogAbbandonoPaginaSuServizioAsincrono(operazione);
	                return;
	            }

	            setTimeout(ottieniResponse, timeout, operazione, --tentativiRimanenti, timeout);
	            return;


	        }

	        if(data.richiediConfermaQuadratura){
	        	$('#collapseVariazioni').overlay('hide');
	        	$(document.body).overlay('hide');
	        	gestioneRichiestaProsecuzioneSuSquadraturaCassa();
	        	return;
	        }
	        $(document.body).overlay('hide');
	        redirezioneAPaginaDisabilitata();
	
	    });

	}

	function redirezioneAPaginaDisabilitata(){
		document.location = "/siacbilapp/redirectToDisabledAggiornamentoVariazioneImportiUEB.do";
	}
	
	 function resettaSpinnerFormSubmitted(){
		$(document.body).overlay('hide');
		 $('[data-spinner-async]').removeClass('activated');
	 }

	return varImp;
}(Variazioni || {}));

$(
    function() {

        VariazioniImporti.leggiCapitoliNellaVariazione();

        $("#pulsanteApriRicercaCapitolo").substituteHandler("click", function() {
            var collapse = $("#collapse_ricerca");
            $('#divRicercaCapitolo').slideUp();
            if(collapse.is(".in")) {
                // Svuoto i campi
                collapse.find("fieldset")
                    .find("input")
                        .not("[data-maintain]")
                            .not("[type='hidden']")
                                .val("")
                                .prop("checked", false)
                                .end()
                            .end()
                        .find("*[name='specificaUEB.tipoCapitolo']")
                            .prop("checked", false)
                            .end()
                        .end()
                    .end()
                .removeClass("in")
                .trigger("hidden");
                $('#collapseUEB').slideUp();
            } else {
                collapse.find("fieldset")
                    .find("input")
                        .not("[data-maintain]")
                            .val("")
                            .prop("checked", false)
                            .end()
                        .end()
                    .end()
                .addClass("in")
                    .trigger("shown");
            }
        });


        $("#pulsanteRicercaCapitolo").substituteHandler("click", VariazioniImporti.cercaCapitolo);
      //SIAC-5016
        $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", VariazioniImporti.cercaCapitoloNellaVariazione);
        $("#redirezioneNuovoCapitoloButton").substituteHandler("click", VariazioniImporti.redirezioneNuovoCapitolo);
        $("#annullaCapitoloButton").substituteHandler("click", VariazioniImporti.annullaCapitolo);
        $("#redirezioneNuovaUEBButton").substituteHandler("click", VariazioniImporti.redirezioneNuovoUEB);

        $("#competenzaVariazioneUEB").on("blur", VariazioniImporti.impostaValoreCassaSeApplicabile);

        $("#concludiVariazioneButton").substituteHandler("click", VariazioniImporti.concludiAggiornamentoVariazione);
        // Svuoto il form del provvedimento
        if(!$('a[href="#collapseProvvedimento"]').hasClass('disabled') && Provvedimento) {
        	var provvedimento = new Provvedimento(undefined, 'Nessun provvedimento variazione di PEG selezionato', 'provvedimento variazione di PEG');
        	provvedimento.inizializza();
        	
        	var provvedimentoVariazioneDiBilancio = new Provvedimento('Aggiuntivo', 'Nessun provvedimento variazione di bilancio selezionato', 'provvedimento variazione di bilancio');
        	provvedimentoVariazioneDiBilancio.inizializza();
        }



        // Imposto il cambio del nome del pulsante di ricerca capitolo a seguito dell'apertura del collapse
        $("#collapse_ricerca").on(
            "shown",
            function() {
                $("#pulsanteApriRicercaCapitolo").html("chiudi ricerca");
            }
        ).on(
            "hidden",
            function() {
                $("#pulsanteApriRicercaCapitolo").html("ricerca capitolo");
            }
        );



        $("form").on("reset", function() {
        	document.location = 'aggiornaVariazioneImportiUEB.do';
        });

      //lego gli handlers: variazioni
        $("#aggiornaVariazioneButton").substituteHandler("click", VariazioniImporti.salvaVariazione);
        $("#concludiVariazioneButton").substituteHandler("click", VariazioniImporti.concludiAggiornamentoVariazione);
        $('#annullaVariazioneButton').substituteHandler('click', VariazioniImporti.chiediConfermaAnnullamento);


        $("#collapseGestioneUEB").substituteHandler("click", function() {
            var $collapse = $('#collapseUEB');
        	var collapseOpen = $collapse.hasClass('in');
            $collapse[collapseOpen? 'slideUp' : 'slideDown']()[collapseOpen? 'removeClass' : 'addClass']('in');
        });
        // SIAC-5016
        $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'aggiornaVariazioneImportiUEB_download.do', false));
        $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'aggiornaVariazioneImportiUEB_download.do', true));
    }
);