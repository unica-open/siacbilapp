/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Funzioni JavaScript comuni per le variazioni di importi */

var VariazioneImporti = (function() {
	var exports = {};
	exports.leggiCapitoliNellaVariazione = leggiCapitoliNellaVariazione;
	exports.cercaCapitoloNellaVariazione = cercaCapitoloNellaVariazione;

    /**
     * Toglie il primo valore per l'injezione da Struts2.
     *
     * @param array l'array di cui modificare i dati
     *
     * @returns l'array modificato
     */
    exports.togliPrimaInjezione = function(array) {
        var i;
        // Ciclo sugli elementi in array
        for(i = 0; i < array.length; i++) {
            // Imposta il dato nell'array togliendo il primo valore dal nome
            array[i].name = array[i].name.split(".").slice(1).join(".");
        }
        return array;
    };

    /** Opzioni base per il dataTable */
    var opzioniBaseDataTable = {
        // Gestione della paginazione
        bPaginate : true,
        // Impostazione del numero di righe
        bLengthChange : false,
        // Numero base di righe
        iDisplayLength : 10,
        // Sorting delle colonne
        bSort : false,
        // Display delle informazioni
        bInfo : true,
        // Calcolo automatico della larghezza delle colonne
        bAutoWidth : true,
        // Filtro dei dati
        bFilter : false,
        // Abilita la visualizzazione di 'Processing'
        bProcessing : true,
        // Internazionalizzazione
        oLanguage : {
            // Informazioni su quanto mostrato nella pagina
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            // Informazioni per quando la tabella è vuota
            sInfoEmpty : "0 risultati",
            // Testo mostrato quando la tabella sta processando i dati
            sProcessing : "Attendere prego...",
            // Testo quando non vi sono dati
            sZeroRecords : "Non sono presenti capitoli corrispondenti alla variazione",
            // Definizione del linguaggio per la paginazione
            oPaginate : {
                // Link alla prima pagina
                sFirst : "inizio",
                // Link all'ultima pagina
                sLast : "fine",
                // Link alla pagina successiva
                sNext : "succ.",
                // Link alla pagina precedente
                sPrevious : "prec.",
                // Quando la tabella è vuota
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    /**
     * Imposta il campo come valuta e lo allinea a destra.
     *
     * @param id     {String} l'id del campo, comprensivo di #
     * @param valuta {Number} la valuta da impostare
     */
    function impostaValutaEAllineaADestra(id, valuta) {
        return $(id)
            .html(valuta.formatMoney())
            .parent()
                .addClass("text-right");
    }
    
    function impostaTabellaComponenti(lista){
    	var opzioniNuove = {
        		aaData: lista,
        		//sDom: "t<'row-fluid'<'span4'i><'span8'p>>",
                bPaginate: false,
                bLengthChange: false,
                iDisplayLength: 10,
                bSort: false,
                bInfo: true,
                bAutoWidth: true,
                bFilter: false,
                bProcessing: true,
                bDestroy : true,
                oLanguage : {
                    sZeroRecords : "Non sono presenti componenti associate alla variazione."
                },
                // Definizione delle colonne
                aoColumnDefs : [
                	{aTargets : [ 0 ], mData : defaultPerDataTable('descrizioneDettaglioInVariazione')},
                    {aTargets : [ 1 ], mData : defaultPerDataTable('descrizioneTipoDettaglioComponenteCapitolo')},
                    {aTargets : [ 2 ], mData : function(source) {
                        		return  source.eliminaSuTuttiGliAnni? "S&igrave;" : "No" ;
                    		}
                    },
                    {aTargets : [ 3 ], mData : defaultPerDataTable('htmlImportoAnno0'), fnCreatedCell: doAddClassToElement('text-right')},
                    {aTargets : [ 4 ], mData : defaultPerDataTable('htmlImportoAnno1'), fnCreatedCell: doAddClassToElement('text-right')},
                    {aTargets : [ 5 ], mData : defaultPerDataTable('htmlImportoAnno2'), fnCreatedCell: doAddClassToElement('text-right')}
                ]
            };

            var opzioniClone = $.extend(true, {}, opzioniBaseDataTable);

            var opzioni = $.extend(true, opzioniClone, opzioniNuove);
            
            $('#tabellaStanziamentiTotaliPerComponenteModale').dataTable(opzioni);

    }
    
	function apriEPopolaModaleModificaImporti( oggettoOriginale){
    	
        // Pulisco il form
        $("#editStanziamenti fieldset :input").val("");
        // Nascondo l'eventuale alert di errore già presente
        $("#ERRORI_modaleEditStanziamenti").slideUp();

        $("#competenzaVariazioneAnno0Modale").val(oggettoOriginale.competenza.formatMoney());
        //SIAC-6883
        $("#competenzaVariazioneAnno1Modale").val(oggettoOriginale.competenza1.formatMoney());
        $("#competenzaVariazioneAnno2Modale").val(oggettoOriginale.competenza2.formatMoney());
        $("#residuoVariazioneModale").val(oggettoOriginale.residuo.formatMoney());
        $("#cassaVariazioneModale").val(oggettoOriginale.cassa.formatMoney());
        $("#titoloModaleVariazioneStanziamenti").html("Capitolo " + oggettoOriginale.numeroCapitolo + " / " + oggettoOriginale.numeroArticolo);

        $('#componentiCapitolo').hide();
        $('.stanziamentiCapitolo').show();
        
        if(isCapitoloConGestioneComponenti(oggettoOriginale.tipoCapitolo)){
        	$('#componentiCapitolo').show();
            caricaComponentiByCapitolo(oggettoOriginale, $('#componentiCapitolo').find('table'));
        }
        
        $('#modaleConsultazioneComponenti').modal('show');
    }
    
    function apriModaleConsultazioneComponenti(capitoloConComponenti, nTd){
    	$('#componentiCapitolo').show();
    	$('.stanziamentiCapitolo').hide();
    	$("#titoloModaleVariazioneStanziamenti").html("Capitolo " + capitoloConComponenti.numeroCapitolo + " / " + capitoloConComponenti.numeroArticolo);
    	return caricaComponentiByCapitolo(capitoloConComponenti, $(nTd).closest('tr'))
    		.then($('#modaleConsultazioneComponenti').modal.bind($('#modaleConsultazioneComponenti'),'show'));
    }
    
    function caricaComponentiByCapitolo(capitolo, containingObject){
    	var uidVariazione = $('#uidVariazione').val();
    	var applicazione = $('#HIDDEN_tipoApplicazione').val();
    	containingObject.overlay('show');
    	return $.postJSON('consultaVariazioneImporti_ricercaComponentiCapitoloInVariazione.do',{'uidCapitoloConComponenti': capitolo.uid, 'uidVariazione': uidVariazione, 'applicazione':applicazione})
    		.then(function(data){
    			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
    				return;
    			}
    			impostaTabellaComponenti(data.componentiCapitolo);
    		})
    		.always(containingObject.overlay.bind(containingObject,'hide'));
    }
    
  //CR-3403
    function impostaTotaliInDataTable(data){
    	if(!data){
    		return;
    	}

    	var totaleStanziamentiEntrataNotUndefined = data.totaleStanziamentiEntrata || 0;
        var totaleStanziamentiCassaEntrataNotUndefined = data.totaleStanziamentiCassaEntrata || 0;
        var totaleStanziamentiResiduiEntrataNotUndefined = data.totaleStanziamentiResiduiEntrata || 0;

        var totaleStanziamentiSpesaNotUndefined = data.totaleStanziamentiSpesa || 0;
        var totaleStanziamentiCassaSpesaNotUndefined = data.totaleStanziamentiCassaSpesa || 0;
        var totaleStanziamentiResiduiSpesaNotUndefined = data.totaleStanziamentiResiduiSpesa || 0;
        
        //SIAC-6883
        var totStanziamentiEntrataAnno1 = data.totaleStanziamentiEntrata1 || 0;
        var totStanziamentiEntrataAnno2 = data.totaleStanziamentiEntrata2 || 0;
        
        var totStanziamentiSpesaAnno1 = data.totaleStanziamentiSpesa1 || 0;
		//SIAC-8166
        var totStanziamentiSpesaAnno2 = data.totaleStanziamentiSpesa2 || 0;
        

        impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totaleStanziamentiEntrataNotUndefined);
        impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totaleStanziamentiResiduiEntrataNotUndefined);
        impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totaleStanziamentiCassaEntrataNotUndefined);
        
        //SIAC-6883
        impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazioneAnnoPiuUno", totStanziamentiEntrataAnno1);
        impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazioneAnnoPiuDue", totStanziamentiEntrataAnno2);

        impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totaleStanziamentiSpesaNotUndefined);
        impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totaleStanziamentiResiduiSpesaNotUndefined);
        impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totaleStanziamentiCassaSpesaNotUndefined);
        
        //SIAC-6883
        impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazioneAnnoPiuUno", totStanziamentiSpesaAnno1);
        impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazioneAnnoPiuDue", totStanziamentiSpesaAnno2);

        impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", (totaleStanziamentiEntrataNotUndefined - totaleStanziamentiSpesaNotUndefined));
        impostaValutaEAllineaADestra("#differenzaResiduoVariazione", (totaleStanziamentiResiduiEntrataNotUndefined - totaleStanziamentiResiduiSpesaNotUndefined));
        impostaValutaEAllineaADestra("#differenzaCassaVariazione", (totaleStanziamentiCassaEntrataNotUndefined - totaleStanziamentiCassaSpesaNotUndefined));
        
        //SIAC-6883
        impostaValutaEAllineaADestra("#differenzaCompetenzaVariazioneAnnoPiuUno", (totStanziamentiEntrataAnno1 - totStanziamentiSpesaAnno1));
        impostaValutaEAllineaADestra("#differenzaCompetenzaVariazioneAnnoPiuDue", (totStanziamentiEntrataAnno2 - totStanziamentiSpesaAnno2));
    }
    
    function isCapitoloConGestioneComponenti(tipoCapitolo){
    	return tipoCapitolo && tipoCapitolo._name && (tipoCapitolo._name === 'CAPITOLO_USCITA_PREVISIONE' || tipoCapitolo._name === 'CAPITOLO_USCITA_GESTIONE')
    }
    
    function isCapitoloConGestioneComponenti(tipoCapitolo){
    	return tipoCapitolo && tipoCapitolo._name && (tipoCapitolo._name === 'CAPITOLO_USCITA_PREVISIONE' || tipoCapitolo._name === 'CAPITOLO_USCITA_GESTIONE')
    }
    
    /**
     * Imposta il dataTable per l'elenco deli capitoli nella variazione.
     *
     * @param tabella       la tabella da paginare
     * @param listaCapitoli la lista dei capitoli da cui ottenere la tabella
     */
    function impostaDataTableCapitoliNellaVariazione(tabella) {

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
                sZeroRecords : "Non sono presenti capitoli associati alla variazione."
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
                            $("a", nTd).substituteHandler("click", function(event) {
                        		event.preventDefault();
                        	}).popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                    	var stringaComponenti = "<a href='#' title='componenti' role='button' data-toggle='modal'>" +
                        "<i class='icon-zoom-in icon-2x'><span class='nascosto'>componenti</span></i>" +
                        "</a>"; 
                        return isCapitoloConGestioneComponenti(source.tipoCapitolo) ? stringaComponenti : '';
                    },
                    fnCreatedCell : function(nTd, sData, oData, iRow) {
                    	$(nTd).addClass("text-center")
                            .find("a")
                            .off('click')
                                .eventPreventDefault("click", function() {
                                	apriModaleConsultazioneComponenti(oData, nTd);
                                });

                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        return source.competenza.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        return source.residuo.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function(source) {
                        return source.cassa.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                  
                },
                {
                    aTargets : [ 5 ],
                    mData : function(source) {
                        return source.competenza1.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                  
                },
                {
                    aTargets : [ 6 ],
                    mData : function(source) {
                        return source.competenza2.formatMoney();
                    },
                    fnCreatedCell : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                  
                }],
            fnPreDrawCallback: function () {
                // Mostro il div del processing
            	tabella.parent().find('div.dataTables_processing').parent("div").show();
            	$('#SPINNER_leggiCapitoli').removeClass("activated");
            	$('#tabellaGestioneVariazioni').overlay('hide');
            	$('#tabellaGestioneVariazioni_wrapper').overlay('show');
            },
                // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
            	tabella.parent().find('div.dataTables_processing').parent("div").hide();
            	$('#tabellaGestioneVariazioni_wrapper').overlay('hide');
            }
        };

        var opzioniClone = $.extend(true, {}, opzioniBaseDataTable);

        var opzioni = $.extend(true, opzioniClone, opzioniNuove);

        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable(false);
            tabella.dataTable().fnDestroy();
        }

        tabella.dataTable(opzioni);
    }
    
  //CR-3403
    function leggiCapitoliNellaVariazione(url, objToSend, tabellaInput){
    	//arguments: url per il posta JSON e tabella da popolare
    	if(!url){
    		return;
    	}
    	var tabella = tabella || $("#tabellaGestioneVariazioni");
    	var obj = objToSend || {};
    	tabella.overlay('show');
    	return $.postJSON(url,obj)
    	.then(function(data) {
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
			//chiamo dataTable SI
			impostaDataTableCapitoliNellaVariazione(tabella);

			impostaTotaliInDataTable(data);
		}).always(tabella.overlay.bind(tabella, 'hide'));
    }
    
    function ottieniTipoApplicazioneCapitolo(tipoApplicazione){
		var tipoApplicazioneNotUndefined = tipoApplicazione || $("#HIDDEN_tipoApplicazione").val();
		return (tipoApplicazioneNotUndefined === "Previsione" || tipoApplicazioneNotUndefined === "PREVISIONE" ? "Previsione" : "Gestione");
	}
    /**
     * Controllo che i campi della ricerca del capitolo siano compilati correttamente
     * */
    function controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo){
    	var erroriArray = []; 
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
        return erroriArray;
    }
    
   
	
 /**
     * Effettua la ricerca del capitolo per creare una variazione.
     */
    function cercaCapitoloNellaVariazione(){
    	var prefissoOggetto = 'capitolo';
    	var oggettoPerChiamataAjax = {};
    	//Campi hidden da leggere
        var  tipoApplicazione   = $("#HIDDEN_tipoApplicazione").val();
        var annoVariazione = $("#HIDDEN_annoVariazione").val();
        // Spinner
        var spinner = $("#SPINNER_CapitoloNellaVariazione");

        //input selezionati dall'utente
        var tipoCapitolo = $('input[name="specificaImporti.tipoCapitoloNellaVariazione"]:checked').val();
        var annoCapitolo = $('#annoCapitoloNellaVariazione').val();
        var numeroCapitolo = $('#numeroCapitoloNellaVariazione').val();
        var numeroArticolo = $('#numeroArticoloNellaVariazione').val();
        var campoUidVariazione = $('#uidVariazione');
        
        //Wrapper per il tipo capitolo e tipo applicazione: e.g. SpesaGestione, reduced:UG
        var wrapTipoCapitoloApplicazione = tipoCapitolo + ottieniTipoApplicazioneCapitolo(tipoApplicazione);
        // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = prefissoOggetto + wrapTipoCapitoloApplicazione + 'NellaVariazione';
        
        //validazione dei campi
        var erroriArray = controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo);
        var url = 'effettuaRicercaNellaVariazioneCap' + wrapTipoCapitoloApplicazione + '_consultazione.do';
        
        if(impostaDatiNegliAlert(erroriArray, $('#ERRORI'))){
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
        
        if(campoUidVariazione.length) {
        	oggettoPerChiamataAjax["uidVariazione"] = campoUidVariazione.val();
        }
        
        return $.postJSON(url, oggettoPerChiamataAjax).then(function(data){
        	var errori = data.errori;
            var elementoCapitolo = data.elementoCapitoloVariazioneTrovatoNellaVariazione;
            if(impostaDatiNegliAlert(errori,$('#ERRORI'))){
            	return;
            }
            apriEPopolaModaleModificaImporti( elementoCapitolo);
        }).always(spinner.removeClass.bind(spinner, "activated"));
        
    }

    return exports;
}());