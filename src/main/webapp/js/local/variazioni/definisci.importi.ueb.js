/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ************************************
 **** Funzioni di ricerca Variazioni ****
 ************************************
 */

var Variazioni = Variazioni || {};

var DefinisciVariazioni = (function(varImp) {
    "use strict";
	var exports = {};

    exports.leggiCapitoliNellaVariazione = leggiCapitoliNellaVariazione;
    exports.definisciVariazione = definisciVariazione;
    exports.cercaCapitoloNellaVariazione = cercaCapitoloNellaVariazione;

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
        
        //Wrapper per il tipo capitolo e tipo applicazione: e.g. SpesaGestione, reduced:UG
        var wrapTipoCapitoloApplicazione = tipoCapitolo + ottieniTipoApplicazioneCapitolo(tipoApplicazione);
        // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = prefissoOggetto + wrapTipoCapitoloApplicazione + 'NellaVariazione';
        
        //validazione dei campi
        var erroriArray = controllaCampiRicercaCapitolo(annoCapitolo, numeroCapitolo,numeroArticolo,tipoCapitolo);
        var url = 'effettuaRicercaNellaVariazioneCap' + wrapTipoCapitoloApplicazione + 'importiUeb_definizione.do';
        
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
        
        return $.postJSON(url, oggettoPerChiamataAjax).then(function(data){
        	var errori = data.errori;
            var elementoCapitolo = data.elementoCapitoloVariazioneTrovatoNellaVariazione;
            if(impostaDatiNegliAlert(errori,$('#ERRORI'))){
            	return;
            }
            apriEPopolaModaleModificaImporti( elementoCapitolo);
        }).always(spinner.removeClass.bind(spinner, "activated"));
        
    }


    function impostaDataTableCapitoliNellaVariazione(tabella) {

        var isEditable = $("input[name='specificaImporti.tipoCapitolo']").length !== 0;
    	//var processingDiv = tabella.parent().find('div.dataTables_processing');

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
                sZeroRecords : " "
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
                        	.find("a")//.find("input")
                        	//.substituteHandler("click", varImp.apriEPopolaModaleModificaImporti.bind(undefined, iRow, oData.uid));
                            	.off("click")
                            	.on("click", function() {
                            		apriEPopolaModaleModificaImporti(iRow);
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
            }],
            fnPreDrawCallback: function () {
                // Mostro il div del processing
            	tabella.parent().find('div.dataTables_processing').parent("div")
                      .show();
            	$('#SPINNER_leggiCapitoli').removeClass("activated");
            	$('#elencoCapitoli').overlay('hide');
            	$('#elencoCapitoli_wrapper').overlay('show');
            },
                // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {

            	tabella.parent().find('div.dataTables_processing').parent("div")
                    .hide();
            	$('#elencoCapitoli_wrapper').overlay('hide');
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

    function impostaTotaliInDataTable(totaleStanziamentiEntrata, totaleStanziamentiCassaEntrata, totaleStanziamentiResiduiEntrata,
    		totaleStanziamentiSpesa, totaleStanziamentiCassaSpesa, totaleStanziamentiResiduiSpesa){


    	var totStanziamentiEntrata = totaleStanziamentiEntrata || 0;
        var totStanziamentiCassaEntrata = totaleStanziamentiCassaEntrata || 0;
        var totStanziamentiResiduiEntrata = totaleStanziamentiResiduiEntrata || 0;

        var totStanziamentiSpesa = totaleStanziamentiSpesa || 0;
        var totStanziamentiCassaSpesa = totaleStanziamentiCassaSpesa || 0;
        var totStanziamentiResiduiSpesa = totaleStanziamentiResiduiSpesa || 0;

        impostaValutaEAllineaADestra("#totaleEntrateCompetenzaVariazione", totStanziamentiEntrata);
        impostaValutaEAllineaADestra("#totaleEntrateResiduoVariazione", totStanziamentiResiduiEntrata);
        impostaValutaEAllineaADestra("#totaleEntrateCassaVariazione", totStanziamentiCassaEntrata);

        impostaValutaEAllineaADestra("#totaleSpeseCompetenzaVariazione", totStanziamentiSpesa);
        impostaValutaEAllineaADestra("#totaleSpeseResiduoVariazione", totStanziamentiResiduiSpesa);
        impostaValutaEAllineaADestra("#totaleSpeseCassaVariazione", totStanziamentiCassaSpesa);

        impostaValutaEAllineaADestra("#differenzaCompetenzaVariazione", (totStanziamentiEntrata - totStanziamentiSpesa));
        impostaValutaEAllineaADestra("#differenzaResiduoVariazione", (totStanziamentiResiduiEntrata - totStanziamentiResiduiSpesa));
        impostaValutaEAllineaADestra("#differenzaCassaVariazione", (totStanziamentiCassaEntrata - totStanziamentiCassaSpesa));
    }
    /**
     * Carica la tabella con i capitoli associati ad una variazione
     * **/
    function leggiCapitoliNellaVariazione(){
    	var elencoCapitoli = $('#elencoCapitoli').overlay('show');
    	$.postJSON("leggiCapitoliNellaVariazioneUEB_definizione.do", {})
    	.then(function(data) {

			var errori = data.errori;
			var alertErrori = $('#ERRORI');

			// Controllo gli eventuali errori, messaggi, informazioni
			if(impostaDatiNegliAlert(errori, alertErrori)) {
				return;
			}

				//chiamo dataTable SI
			impostaDataTableCapitoliNellaVariazione($("#elencoCapitoli"));

			impostaTotaliInDataTable(data.totaleStanziamentiEntrata, data.totaleStanziamentiCassaEntrata, data.totaleStanziamentiResiduiEntrata,
					data.totaleStanziamentiSpesa, data.totaleStanziamentiCassaSpesa, data.totaleStanziamentiResiduiSpesa);
		}).always(elencoCapitoli.overlay.bind(elencoCapitoli, 'hide'));
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
                	$(document.body).overlay('hide');            		
                    setTimeout(ottieniResponse,30000,operazione, 50, 30000);
                }
            }]);
    }

    function ottieniResponse(operazione, tentativiRimanenti, timeout){
        var url = operazione + 'AggiornamentoVariazioneImportiUEBAsyncResponse.do';
        return $.postJSON(url)
        .then(function(data) {
            var alertErrori = $('#ERRORI');
            var alertInformazioni = $('#INFORMAZIONI');

            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	alertInformazioni.slideUp();
            	alertInformazioni.find("ul").find("li").remove();
            	$(document.body).overlay('hide');
                $('#bottoni').find('button').removeAttr('disabled');
                $("#spanPulsanteDefinisciVariazione").removeClass("hide");
                return;
            }

            if(data.isAsyncResponsePresent === undefined){
            	 alertInformazioni.slideUp();
            	 alertInformazioni.find("ul").find("li").remove();
            	 $(document.body).overlay('hide');
                impostaDatiNegliAlert(['COR_ERR_0001 - Errore di sistema: impossibile ottenere la risposta asincrona.'], alertErrori);
                $("#spanPulsanteDefinisciVariazione").removeClass("hide");
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
           
            alertInformazioni.slideUp();
            alertInformazioni.find("ul").find("li").remove();
            $(document.body).overlay('hide');
    		
            $('#bottoni').find('button').removeAttr('disabled');

            var alertMessaggi = $('#MESSAGGI');
            impostaDatiNegliAlert(data.messaggi, alertMessaggi);
            impostaDatiNegliAlert(data.informazioni, alertInformazioni,undefined, false);

        });
    }

    function definisciVariazione(){
        
        $("#spanPulsanteDefinisciVariazione").addClass("hide");
        impostaDatiNegliAlert(["COR_INF_0019 - L'elaborazione e' stata attivata."], $("#INFORMAZIONI"));
        $(document.body).overlay('show');
        
        return $.postJSON('effettuaDefinisciVariazioneImportiUEB.do', qualify($('#effettuaDefinisciVariazioneImporti').serializeObject()), function(data) {
            var alertErrori = $('#ERRORI');
            alertErrori.slideUp();
            if (impostaDatiNegliAlert(data.errori,alertErrori)) {
            	var alertInformazioni = $('#INFORMAZIONI');
            	$("#spanPulsanteDefinisciVariazione").removeClass("hide");
            	alertInformazioni.slideUp();
            	alertInformazioni.find("ul").find("li").remove();
                return;
            }
            ottieniResponse("definisci", 10, 10000);
        });
    }

    return exports;
}(Variazioni || {}));

/* Document Ready */

$(function() {

    DefinisciVariazioni.leggiCapitoliNellaVariazione();
    $('#buttonDefinisciVariazione').on("click",DefinisciVariazioni.definisciVariazione);
    // SIAC-5016
    $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'definisceVariazioneImportiUEB_download.do', false));
    $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [], 'definisceVariazioneImportiUEB_download.do', true));
    $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", Variazioni.cercaCapitoloNellaVariazione);
});