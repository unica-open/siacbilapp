/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErroriSoggetto = $("#ERRORI_MODALE_SOGGETTO");

    /**
     * Crea un form a partire da un oggetto: i campi dell'oggetto saranno i campi del form, utilizzando la chiave come name e il valore come value.
     *
     * @param formAction (String) l'azione del form
     * @param obj        (Object) l'oggetto di popolamento del form
     *
     * @returns (JQuery) l'oggetto jQuery corrispondente al form
     */
    function createFormFromObject(formAction, obj) {
        var str = "<form action=\"" + formAction + "\" method=\"POST\" novalidate=\"novalidate\">";
        $.each(obj, function(idx, val) {
            str += "<input type=\"hidden\" name=\"" + idx + "\" value=\"" + val + "\" />";
        });
        str += "</form>";
        return $(str);
    }

    /**
     * Impostazione della tabella.
     */
    function impostaTabellaFatturaElettronica() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaFatturaElettronicaAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna fattura elettronica disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                $("a[rel='popover']", tableId).popover();
                $("a.tooltip-test", tableId).tooltip();
            },
            aoColumnDefs: [
                {aTargets: [0], sWidth: "20%", mData: "fornitore"},
                {aTargets: [1], mData: "dataEmissione"},
                {aTargets: [2], mData: "dataRicezione"},
                {aTargets: [3], mData: "numeroDocumento"},
                {aTargets: [4], mData: "tipoDocumentoFEL"},
                {aTargets: [5], mData: "dataAcquisizione"},
                {aTargets: [6], mData: "statoAcquisizione"},
                {aTargets: [7], mData: "importoLordo", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.consultaFatturaElettronica")
                            .click([oData], consultazioneFatturaElettronica)
                            .end()
                        .find("a.importaFatturaElettronica")
                            .click([oData], importazioneFatturaElettronica)
                            .end()
                        .find("a.sospendiFatturaElettronica")
                            .click([oData], sospensioneFatturaElettronica);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }


    /**
     * Consultazione della fattura elettronica.
     */
    function consultazioneFatturaElettronica(){
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Importazione della fattura elettronica.
     */
    function importazioneFatturaElettronica(e){
        var obj = e.data[0] || {};
        var href = $(this).data("href");
        var formAction = href.split("?")[0];
        //SIAC-7571
        var $overlay = $(document.body).overlay("show");
        // var table = $("#tabellaRisultatiRicerca").overlay("show");
        
        //ricercaSinteticaSoggetto
        var oggettoPerChiamataAjax = {"soggetto.codiceFiscale": obj.codiceFiscale || "", "soggetto.partitaIva": obj.partitaIva || ""};
        alertErroriSoggetto.slideUp();
        $.postJSON("ricercaSinteticaSoggetto.do", oggettoPerChiamataAjax, function(data) {
        	if(impostaDatiNegliAlert(data.errori, alertErroriSoggetto)) {
        		proseguiImportazioneConModaleSoggetto(obj, formAction);
            }
        	if(data.listaSoggetti.length === 1){
                var soggetto = data.listaSoggetti[0];
                var oggettoPerChiamataAjax = {"fatturaFEL.idFattura": obj.uid, "soggetto.uid": soggetto.uid, "soggetto.codiceSoggetto": soggetto.codiceSoggetto};
        		var frm = createFormFromObject(formAction, oggettoPerChiamataAjax);
                frm.appendTo("body");
                //SIAC-8273 eseguo la chiamata ajax per controllare se possiede importo totale
                checkImportoValorizzatoAsync(frm, oggettoPerChiamataAjax, $overlay);
        	}else{
        		proseguiImportazioneConModaleSoggetto(obj, formAction); 
            }
        });
        
    }
    
    function proseguiImportazioneConModaleSoggetto(obj, formAction){
        //SIAC-7718 creo una continuita' con l'overlay
        $(document.body).overlay("hide");
        
    	// Svuoto il form di ricerca soggetto
        $("#fieldsetRicercaGuidateSoggetto").find(":input")
            .val("");
        // Imposto codice fiscale e partita iva
        $("#codiceFiscaleSoggetto_modale").val(obj.codiceFiscale || "");
        $("#partitaIvaSoggetto_modale").val(obj.partitaIva || "");

        // Imposto l'azione per il submit
        $("#pulsanteConfermaSoggetto").substituteHandler("click", function() {
            var checkedSoggetto = $("#risultatiRicercaSoggetti").find("[name='checkSoggetto']:checked");
            var soggetto;
            var frm;
            // Controllo di aver impostato il soggetto
            if(!checkedSoggetto.length) {
                impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: soggetto"], alertErroriSoggetto, false);
                return;
            }
            // Ho il soggetto e l'id della fattura. Invio i dati
            soggetto = checkedSoggetto.data("originalSoggetto") || {};
            // Creazione del form di invios
            var oggettoPerChiamataAjax = {"fatturaFEL.idFattura": obj.uid, "soggetto.uid": soggetto.uid, "soggetto.codiceSoggetto": soggetto.codiceSoggetto};
            frm = createFormFromObject(formAction, oggettoPerChiamataAjax);
            // Invio del form
            frm.appendTo("body");
            $("#modaleGuidaSoggetto").modal("hide")
            //SIAC-8497 in caso di importazione fatture con importo negativo il passaggio non era eseguito
            checkImportoValorizzatoAsync(frm, oggettoPerChiamataAjax, $(document.body).overlay("show"));
        });
        // Apro il modale
        $("#modaleGuidaSoggetto").modal("show");
    }

    /**
     * Gestisce la richiesta di conferma prosecuzione su una certa azione.
     *
     *@param $form il form da cui fare il submit
     *@param oggettoPerChiamataAjax l'oggetto da inviare 
     *		prema il pulsante "conferma" (se non presente, viene solo chiuso il modale)
     *@param callbackIndietro (function) la function da eseguire nel caso l'utente 
     *		prema il pulsante "No, indietro" (se non presente, viene solo chiuso il modale)
     *@param etichettaConferma1 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
     *@param etichettaConferma2 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
     *
     *@param etichettaIndietro(stringa) l'etichetta da mostrare sul pulsante che chiama la function fncIndietro
     *
     * */
    function checkImportoValorizzatoAsync($form, oggettoPerChiamataAjax, $overlay){
    	// var $overlay = $(document.body);
    	var $alertWarning = $("#modaleConfermaProsecuzioneSuAzione");
    	// var $form = $('#formInserimentoDocumentoEntrata');
    	var url = $form.attr('action').slice(0, -10) + 'cercaFatturaAsincrono.do';
    	
    	$overlay.overlay('show');
    	$alertWarning.slideUp();
    	
    	$.postJSON(url, oggettoPerChiamataAjax, function(data) {
    		if(data.errori && data.errori.length) {
    			//se l'overlay è ancora mostrato lo rimuovo
    			$overlay.overlay('hide');
    			impostaDatiNegliAlert(data.errori, $('#ERRORI'));
    			return;
    		}
    		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
    			//se l'overlay è ancora mostrato lo rimuovo
    			$overlay.overlay('hide');
    			return;
    		}
    		if(data.messaggi && data.messaggi.length) {
    			//tolgo l'overlay per mostrare la modale del messaggio
    			$overlay.overlay('hide');
    			var messaggioConfermaFatturaSenzaImporto = '';
    			for(var i = 0; i < data.messaggi.length; i++){
    				messaggioConfermaFatturaSenzaImporto += data.messaggi[i].descrizione;
    			}
    			impostaConfermaUtentePerFEL(messaggioConfermaFatturaSenzaImporto
    			, function() {
    				//CALLBACK NOTA DI CREDITO
    				//passo la conferma
    				$form.append('<input type="hidden" id="HIDDEN_adeguaImportoNonValorizzato" name="adeguaImportoNonValorizzato" value="ADEGUAMENTO" />');
    				$overlay.overlay('show');
    				//parto con una nuova chiamata
                    checkImportoNegativoAsync($form, oggettoPerChiamataAjax, $overlay);
    			}, undefined, "continua", "indietro");
    			return;
    		}
    		//non eseguo il submit e proseguo con il successivo controllo
    		checkImportoNegativoAsync($form, oggettoPerChiamataAjax, $overlay);
    	});
    }
    
    /**
     * Gestisce la richiesta di conferma prosecuzione su una certa azione.
     *
     *@param messaggioRichiestaConferma il messaggio da mostrare all'utente
     *@param callbackConferma (function) la function da eseguire nel caso l'utente 
     *		prema il pulsante "conferma" (se non presente, viene solo chiuso il modale)
     *@param callbackIndietro (function) la function da eseguire nel caso l'utente 
     *		prema il pulsante "No, indietro" (se non presente, viene solo chiuso il modale)
     *@param etichettaConferma (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
     *@param etichettaIndietro(stringa) l'etichetta da mostrare sul pulsante che chiama la function fncIndietro
     *
     * */
    function impostaConfermaUtentePerFEL(messaggioRichiestaConferma, callbackConferma, callbackIndietro, etichettaConferma, etichettaIndietro) {
    	var fncConferma = callbackConferma && typeof callbackConferma === "function" ? callbackConferma : $.noop;
    	var fncIndietro = callbackIndietro && typeof callbackIndietro === "function" ? callbackIndietro : $.noop;
    	var messaggioDaMostrare = messaggioRichiestaConferma || "";
    	var siConferma = etichettaConferma ? etichettaConferma : 'Conferma';
    	var noIndietro = etichettaIndietro ? etichettaIndietro : 'Indietro';
    	
    	bootboxAlert(messaggioDaMostrare, 'Attenzione', 'dialogWarn', [
    		{
    			"label" : noIndietro
    			, "class" : "btn"
    				, "callback": fncIndietro
    		}
    		, {
    			"label" : siConferma
    			, "class" : "btn"
    				, "callback" : fncConferma
    		}
    		]);
    }
    
    /**
    * Gestisce la richiesta di conferma prosecuzione su una certa azione.
    *
    *@param $form il form da cui fare il submit
    *@param oggettoPerChiamataAjax l'oggetto da inviare 
    *		prema il pulsante "conferma" (se non presente, viene solo chiuso il modale)
    *@param callbackIndietro (function) la function da eseguire nel caso l'utente 
    *		prema il pulsante "No, indietro" (se non presente, viene solo chiuso il modale)
    *@param etichettaConferma1 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
    *@param etichettaConferma2 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
    *
    *@param etichettaIndietro(stringa) l'etichetta da mostrare sul pulsante che chiama la function fncIndietro
    *
    * */
    function checkImportoNegativoAsync($form, oggettoPerChiamataAjax, $overlay){
        // var $overlay = $(document.body);
        var $alertWarning = $("#modaleConfermaProsecuzioneSuAzione");
        // var $form = $('#formInserimentoDocumentoEntrata');
        var url = $form.attr('action').slice(0, -3) + 'Asincrono.do';

        $overlay.overlay('show');
        $alertWarning.slideUp();
        
        //controllo la chiamata precedente se ho un risultato
        if($('#HIDDEN_adeguaImportoNonValorizzato') !== undefined){
        	oggettoPerChiamataAjax['adeguaImportoNonValorizzato'] = $('#HIDDEN_adeguaImportoNonValorizzato').val();
        }

        $.postJSON(url, oggettoPerChiamataAjax, function(data) {
            if(data.errori && data.errori.length) {
                //se l'overlay è ancora mostrato lo rimuovo
                $overlay.overlay('hide');
                impostaDatiNegliAlert(data.errori, $('#ERRORI'));
                return;
            }
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                //se l'overlay è ancora mostrato lo rimuovo
                $overlay.overlay('hide');
                return;
            }
            if(data.messaggi && data.messaggi.length) {
                //tolgo l'overlay per mostrare la modale del messaggio
                $overlay.overlay('hide');
                var messaggioConfermaFatturaNegativa = '';
                for(var i = 0; i < data.messaggi.length; i++){
                    messaggioConfermaFatturaNegativa += data.messaggi[i].descrizione;
                }
                impostaRichiestaConfermaUtentePerFEL(messaggioConfermaFatturaNegativa
                , function() {
                    //CALLBACK NOTA DI CREDITO
                    //passo la conferma
                    $form.append('<input type="hidden" id="#HIDDEN_sceltaUtente" name="sceltaUtente" value="NCD" />');
                    $overlay.overlay('show');
                    //submit del form
                    $form.submit();
                }
                , function() {
                    //CALLBACK FATTURA ATTIVA
                    //passo la conferma
                    $form.append('<input type="hidden" id="#HIDDEN_sceltaUtente" name="sceltaUtente" value="FSN" />');
                    $overlay.overlay('show');
                    //submit del form
                    $form.submit();
                }, undefined, "nota di credito", "fattura attiva", "indietro");
                return;
            }
            //eseguo il submit del form solo se non ho avuto errori o messagi in risposta
            if(!(data.errori && data.errori.length)) {
                $form.submit();
            }
        });
    }

    /**
    * Gestisce la richiesta di conferma prosecuzione su una certa azione.
    *
    *@param messaggioRichiestaConferma il messaggio da mostrare all'utente
    *@param callbackConferma (function) la function da eseguire nel caso l'utente 
    *		prema il pulsante "conferma" (se non presente, viene solo chiuso il modale)
    *@param callbackIndietro (function) la function da eseguire nel caso l'utente 
    *		prema il pulsante "No, indietro" (se non presente, viene solo chiuso il modale)
    *@param etichettaConferma1 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
    *@param etichettaConferma2 (stringa) l'etichetta da mostrare sul pulsante che chiama la function fncConferma
    *
    *@param etichettaIndietro(stringa) l'etichetta da mostrare sul pulsante che chiama la function fncIndietro
    *
    * */
    function impostaRichiestaConfermaUtentePerFEL(messaggioRichiestaConferma, callbackConferma1,  callbackConferma2, callbackIndietro, etichettaConferma1, etichettaConferma2, etichettaIndietro) {
        var fncConferma1 = callbackConferma1 && typeof callbackConferma1 === "function" ? callbackConferma1 : $.noop;
        var fncConferma2 = callbackConferma2 && typeof callbackConferma2 === "function" ? callbackConferma2 : $.noop;
        var fncIndietro = callbackIndietro && typeof callbackIndietro === "function" ? callbackIndietro : $.noop;
        var messaggioDaMostrare = messaggioRichiestaConferma || "";
        var siNotaCredito = etichettaConferma1 ? etichettaConferma1 : 'Nota di credito';
        var siFatturaAttiva = etichettaConferma2 ? etichettaConferma2 : 'Fattura attiva';
        var noIndietro = etichettaIndietro ? etichettaIndietro : 'Indietro';
        
        bootboxAlert(messaggioDaMostrare, 'Attenzione', 'dialogWarn', [
            {
                "label" : noIndietro
                , "class" : "btn"
                , "callback": fncIndietro
            }
            , {
                "label" : siNotaCredito
                , "class" : "btn"
                , "callback" : fncConferma1
            }
            , {
                "label" : siFatturaAttiva
                , "class" : "btn"
                , "callback" : fncConferma2
            }
        ]);
    }

    /**
     * Sospensione della fattura elettronica
     */
    function sospensioneFatturaElettronica(e){
        var obj = e.data[0] || {};
        // Imposto i dati nella modale
        $("#spanModaleSospensione").html(obj.numeroDocumento || "");
        $("#idFatturaFatturaFELModaleSospensione").val(obj.uid || "");
        $("#noteFatturaFELModaleSospensione").val(obj.note || "");
        $("#modaleSospensione").modal("show");
    }

    $(function() {
        // Inizializzazione del datatable
        impostaTabellaFatturaElettronica();
    });
}(jQuery, this);