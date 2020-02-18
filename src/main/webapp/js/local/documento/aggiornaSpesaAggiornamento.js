/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function(global, $, doc){
    var exports = {};
    var $body = $(document.body);
    var _oldPreSubmit = global.preSubmit;
    
    global.preSubmit = preSubmit;
    
    exports.handleTipoDocumentoSiope = handleTipoDocumentoSiope;
    
    /**
     * Pre-submit dei dati
     * @param e (Event) l'evento scatenante
     * @return (boolean) se proseguire con il submit
     */
    function preSubmit(e) {
        var res = _oldPreSubmit(e);
        var fields = $(e.currentTarget)
            .find('#registroRepertorioDocumento, #annoRepertorioDocumento, #numeroRepertorioDocumento, #dataRepertorioDocumento');
        var campiValorizzati;
        if(fields.length === 0 || e.forceSubmit === true) {
            // Non ho i campi: permetto il submit
            return res;
        }
        campiValorizzati = fields.filter(function(idx, el) {
            return el.value !== undefined && el.value !== '';
        }).length;
        if(campiValorizzati === 0 || campiValorizzati === fields.length) {
            // Tutti i campi sono correttamente valorizzati
            return res;
        }
        // Chiedo di proseguire
        askProsecuzione();
        e.preventDefault();
        // Forzo il false
        return false;
    }

    /**
     * Richiesta per la prosecuzione con il submit del form
     */
    function askProsecuzione() {
        var msg = 'COR_ERR_0009 - Il formato del parametro Dati repertorio/protocollo non &egrave; valido: Registro, Anno, Numero e Data devono essere tutti valorizzati o tutti non valorizzati. Proseguire?';
        bootboxAlert(msg, 'Attenzione', 'dialogWarn', [
            {label: 'No, indietro', 'class': 'btn', callback: $.noop},
            {label: 'S&igrave;, prosegui', 'class': 'btn', callback: function(){
                // Forzo il submit
                var e = $.Event('submit');
                e.forceSubmit = true;
                $('form').trigger(e);
            }}
        ]);
    }

    
    /**
     * Valorizza il netto per l'importo.
     */
    exports.valorizzaNetto = function() {
        var campoImporto = $("#importoDocumento").blur();
        var campoArrotondamento = $("#arrotondamentoDocumento").blur();
        var campoNetto = $("#nettoDocumento");
        var importoStr = campoImporto.val();
        var arrotondamentoStr = campoArrotondamento.val();
        var importo = parseFloat(parseLocalNum(importoStr));
        var arrotondamento = parseFloat(parseLocalNum(arrotondamentoStr));

        // I valori originali per ottenere gli extra da aggiungere
        var valoriOriginaliImporto = parseFloat(parseLocalNum(campoImporto.data("importoBase")));
        var valoriOriginaliArrotondamento = parseFloat(parseLocalNum(campoArrotondamento.data("importoBase")));
        var valoriOriginaliNetto = parseFloat(parseLocalNum(campoNetto.data("importoBase")));

        // L'extra da aggiungere alla somma di importo e arrotondamento
        var extra = valoriOriginaliNetto - (valoriOriginaliImporto + valoriOriginaliArrotondamento);

        if(isNaN(importo) || isNaN(arrotondamento)) {
            // Almeno una stringa non è corretta: ritorno
            return;
        }

        // Importo il netto
        campoNetto.val((importo + arrotondamento + extra).formatMoney());
    };

    /**
     * Calcola il codice fiscale del soggetto pignorato.
     *
     * @param elt (jQuery) l'elemento da cui ottenere il codice fiscale
     */
    exports.calcoloCodiceFiscalePignorato = function(elt) {
        var codiceFiscale = elt.val().toUpperCase();
        var codiceFiscaleValido = true;
        if(codiceFiscale.length === 16) {
            codiceFiscaleValido = checkCodiceFiscale(codiceFiscale);
        }

        elt.val(codiceFiscale);

        if(codiceFiscaleValido) {
            doc.alertErrori.slideUp();
        } else {
            impostaDatiNegliAlert(["Il codice fiscale inserito non è sintatticamente corretto"], doc.alertErrori, false);
        }
    };

    /**
     * Gestisce l'attivazione o la disattivazione della data di sospensione del documento.
     */
    exports.gestioneDataRiattivazione = function() {
        var campoDataRiattivazione = $("#dataRiattivazioneDocumento");
        var campoDataSospensione = $("#dataSospensioneDocumento");
        if(campoDataSospensione.val()) {
            campoDataRiattivazione.removeAttr("disabled");
        } else {
            campoDataRiattivazione.val("")
                .attr("disabled", "disabled");
        }
    };

    /**
     * Gestisce la data di scadenza del documento.
     */
    exports.gestioneDataScadenza = function() {
        var terminePagamento = parseInt($("#terminePagamentoDocumento").val(), 10);
        var dataScadenzaField = $("#dataScadenzaDocumento");
        var dataEmissione = $("#dataRicezioneDocumento").val() || $("#dataRepertorioDocumento").val() || $("#dataEmissioneDocumento").val();
        var dataEmissioneSplit = dataEmissione.split("/");
        var dataScadenzaDate;
        var newString;

        // Controllo se abbia sufficienti dati
        if(isNaN(terminePagamento) || dataEmissioneSplit.length !== 3) {
            return;
        }

        // Costruisco la data
        dataScadenzaDate = new Date(dataEmissioneSplit[2], dataEmissioneSplit[1] - 1, dataEmissioneSplit[0]);
        dataScadenzaDate.setDate(dataScadenzaDate.getDate() + terminePagamento);
        // Costruisco la nuova stringa
        newString = ('0' + dataScadenzaDate.getDate()).slice(-2) + '/'
                + ('0' + (dataScadenzaDate.getMonth() + 1)).slice(-2) + '/'
                + dataScadenzaDate.getFullYear();
        dataScadenzaField.val(newString);
    };

    /**
     * Gestisce il click sul campo di riattivazione, bloccando l'evento nel caso in cui il campo sia readonly.
     *
     * @param e (Event) l'evento scatenante
     *
     * @returns (Boolean) se l'evento debba propagarsi o meno
     */
    exports.gestioneClickRiattivazione = function(e) {
        if($(this).attr("disabled")) {
            e.stopPropagation();
            e.preventDefault();
            return false;
        }
        return true;
    };

    /**
     * Apre il modale del soggetto e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.apriModaleSoggetto = function(e) {
        e.preventDefault();
        $("#codiceSoggetto_modale").val($("#codiceSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    };

    function submitFormPerAttivaRegistrazioniContabili(){
    	$("#formAggiornamentoDatiDocumento")
    		.attr("action", "aggiornamentoDocumentoSpesa_attivaRegistrazioniContabili.do")
    		.submit();
//    	alert('chiamata servizio attivazione registrazioni');
    }
    function callbackValidazionePrimaNota(){
    	var $body = $('body');
    	$("#formAggiornamentoDatiDocumento").append('<input type="hidden" name="validazionePrimaNotaDaDocumento" value="true"/>');
    	//SIAC-6045
    	$body.overlay('remove');
    	$body.overlay({useMessage: true});
    	//per i tests
    	submitFormPerAttivaRegistrazioniContabili();
    }
    
    function callbackConfermaAttivaRegistrazioniContabili(){
    	$.postJSON('aggiornamentoDocumentoSpesa_controllaNecessitaRichiestaUlterioreConferma.do', {})
    	.then(function(data){
    		if(data && data.abilitatoPrimaNotaDaFinanziaria){
    			impostaRichiestaConfermaUtente('&Egrave; possibile validare la prima nota ora o successivamente.',
    					callbackValidazionePrimaNota,
    					submitFormPerAttivaRegistrazioniContabili,
    					'valida ora',
    					'valida in seguito'
    					);
    			return;
    		}
    		submitFormPerAttivaRegistrazioniContabili();
    	});
    }
    
    /**
     * Attiva le registrazioni contabili del documento
     *
     */
    exports.attivaRegistrazioniContabili = function(elt) {    	
    	impostaRichiestaConfermaUtente('Si stanno per attivare le registrazioni GEN e PCC sul documento . Proseguire?', callbackConfermaAttivaRegistrazioniContabili, $.noop);
    };
    
    
    /**
     * Prepopolamento della causale di sospensione
     */
    exports.prepopolaCausaleSospensione = function() {
        var $option = $('option:selected', '#causaleSospensione');
        var template;
        if($option.val() === '0') {
            // Non faccio alcunche'
            return;
        }
        template = $option.data('template');
        $('#causaleSospensioneDocumento').val(template);
    };
    
    /**
     * Caricamento delle SAC
     */
    exports.caricaStrutture = function() {
        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do', {nomeAzioneDecentrata: $('#nomeAzioneSAC').val()})
        .then(function (data) {
            ZTreeDocumento.imposta('treeStruttAmm_Doc', ZTreeDocumento.SettingsBase, data.listaElementoCodifica, 'HIDDEN_StrutturaAmministrativoContabile_DocUid', '_Doc');
        });
    };
    
    /**
     * Gestione dei dati della fattura pagata/incassata
     * @param e (Event) l'evento scatenante l'invocazione
     * @returns (boolean) se proseguire con il submit
     */
    exports.handleSubmitFatturaPagata = function(e) {
        var res = preSubmit(e);
        var flagPagataIncassata = $('#flagPagataIncassataDatiFatturaPagataIncassata').is(':checked');
        if(res === false) {
            // Esco subito
            return false;
        }
        if(!flagPagataIncassata || e.forceSubmitFatturaPagata) {
            $body.overlay('show');
            return res;
        }
        // Chiedo conferma all'utente
        bootbox.dialog(
            "Si stanno per inserire i dati di pagamento sul documento. Il documento diverr&agrave; di stato EMESSO. Proseguire?",
            [
                {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
                {"label" : "Si, prosegui", "class" : "btn", "callback": forceFormSubmitFatturaPagata}
            ],
            {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'}
        );
        // Forzo il false
        e.preventDefault();
        return false;
    };
    
    /**
     * Forza il submit del form
     */
    function forceFormSubmitFatturaPagata() {
        var ev = $.Event('submit');
        ev.forceSubmit = true;
        ev.forceSubmitFatturaPagata = true;
        $("#formAggiornamentoDatiDocumento").trigger(ev);
    }
    
    /**
     * Gestione del tipo SIOPE
     * @param e (Event) l'evento scatenante
     */
    function handleTipoDocumentoSiope(e) {
        var $siopeTipoDocumento = $(e.target).find('option:selected');
        var codice = $siopeTipoDocumento.data('codice');
        var $analogico = $('div[data-siope-analogico]');
        
        if(codice === 'A') {
            $analogico.slideDown();
        } else {
            $analogico.slideUp();
            $analogico.find('select')
                .val(0);
        }
    }
    
    /**
     * Gestisce il prepopolamento della select del codice PCC
     */
    
    /**
     *Inibisce una select disabilitandola ed aggiungendovi dopo un campo hidden 
     */
    function inibisciSelect($selectDaInibire, modelName, idName){
    	var chunksInput = ['<input name="',
    		'" value="',
    		'" id="',
    		'" type="hidden">'];
    	var chunksSelect; 
    	var $oldHidden;
    	var strHidden='';
    	var uidSelezionato =  $selectDaInibire.val();
    	if(!uidSelezionato){
    		return;
    	}
    	
    	$selectDaInibire.attr('disabled','disabled');
		
		$oldHidden = $('input[type="hidden"][name="'+modelName+'"]');
		if($oldHidden.length >0){
			$oldHidden.val(uidSelezionato);
			return;
		}
		chunksSelect = [modelName ? modelName : '',
    			uidSelezionato,
    			idName? idName : ''];
		strHidden = chunksInput.reduce(function(acc, el, index) {
	    	var valueSelect = chunksSelect[index]? chunksSelect[index] : '';
	        return acc + el + valueSelect;
	    }, '');
		$selectDaInibire.after(strHidden);
    }
    
    //CR-2888: Preselezionare l'elemento se ne esiste solo uno se il campo è obbligatorio per quel tipo documento. 
    exports.inibisciModificaCodicePCCSeNecessario = function(inibisciModifica){
    	if(!inibisciModifica){
    		//non devo disabilitare le select
    		return;
    	}
    	//disabilito la select del codice pcc
    	inibisciSelect($('#codicePCCDocumento'), 'documento.codicePCC.uid');
    	//disabilito la select del codice ufficio destinatario documento
    	inibisciSelect($('#codiceUfficioDestinatarioDocumento'), 'documento.codiceUfficioDestinatario.uid');
    
    };
    
    exports.gestisciCodicePCC = function(){
    	var $selectCodice = $('#codicePCCDocumento');
    	var $selectUfficio = $('#codiceUfficioDestinatarioDocumento');
    	var uidUfficio = $selectUfficio.val();
    	if(!uidUfficio){
    		// Non ho un ufficio selezionato, non posso filtrare: esco.
    		return;
    	}
    	$selectCodice.overlay('show');
    	return $.postJSON('aggiornamentoDocumentoSpesa_obtainListaCodicePCCByCodiceUfficioDestinatario.do', qualify({uidCodiceUfficioDestinatarioPccToFilter: uidUfficio}))
	    	.then(function(data){
	    		var str="";
	    		var inibisciModificaCodicePCC = $selectUfficio.data('disabilita-pcc');
	    		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
	    			return;
	    		}
	    		
	    		//tolgo la disabilitazione sulla select del codice: non posso sapere se a quell'ufficio corrisponde uno ed un solo codice 
	    		$selectCodice.removeAttr('disabled');
	    		$selectCodice.parent().find('input[type="hidden"]').remove();
	    		$selectUfficio.parent().find('input[type="hidden"]').remove();
	    		
	    		//popolo la select
	    		str = data.listaCodicePCCFiltered.reduce(function(acc, el) {
	                return acc + '<option value="' + el.uid + '">' + el.codice + ' - ' + el.descrizione + '</option>';
	            }, '<option></option>');
	    		
	    		$selectCodice.html(str);
	    		
	    		if((inibisciModificaCodicePCC || $selectCodice.data("pcc-obbligatorio")) && preSelezionaSeUnicaOpzione($selectCodice)){
	    			inibisciModificaCodicePCCSeNecessario(inibisciModificaCodicePCC);
	    		}
	    	}).always($selectCodice.overlay.bind($selectCodice, 'hide'));
        	
    };
    doc.Aggiornamento = exports;
    return doc;
    
    
}(this, jQuery, Documento || {}));


$(function(){
    $("#terminePagamentoDocumento, #dataRepertorioDocumento, #dataEmissioneDocumento, #dataRicezioneDocumento").on("change", Documento.Aggiornamento.gestioneDataScadenza);
    $("#dataRiattivazioneDocumento").click(Documento.Aggiornamento.gestioneClickRiattivazione);
    $("#dataSospensioneDocumento").change(Documento.Aggiornamento.gestioneDataRiattivazione);
    $("#importoDocumento, #arrotondamentoDocumento").change(Documento.Aggiornamento.valorizzaNetto);
    $("#codiceFiscalePignoratoDocumento").on("keypress", function() {
        var self = $(this);
        setTimeout(function() {
            Documento.Aggiornamento.calcoloCodiceFiscalePignorato(self);
        }, 1);
    });

    $("#importoDocumento").data("importoBase", $("#importoDocumento").val());
    $("#arrotondamentoDocumento").data("importoBase", $("#arrotondamentoDocumento").val());
    $("#nettoDocumento").data("importoBase", $("#nettoDocumento").val());

    $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(Documento.Aggiornamento.apriModaleSoggetto);
    $("#pulsanteAttivaRegistrazioniContabili").click(Documento.Aggiornamento.attivaRegistrazioniContabili);
    // SIAC-4679
    $('#causaleSospensione').substituteHandler('change', Documento.Aggiornamento.prepopolaCausaleSospensione);
    // SIAC-4680
    Documento.Aggiornamento.caricaStrutture();
    // SIAC-4749
    if($('#datiFatturaPagataIncassataEditabili').val() === 'true') {
        $('#formAggiornamentoDatiDocumento').substituteHandler('submit', Documento.Aggiornamento.handleSubmitFatturaPagata);
    }
    // SIAC-5311 SIOPE+
    $('#siopeDocumentoTipo').substituteHandler('change', Documento.Aggiornamento.handleTipoDocumentoSiope)
        .change();
    
    $('#codiceUfficioDestinatarioDocumento').on('change',Documento.Aggiornamento.gestisciCodicePCC);
    Documento.Aggiornamento.inibisciModificaCodicePCCSeNecessario($('#codiceUfficioDestinatarioDocumento').data('disabilita-pcc'));
    
    
    //SIAC 6677
    $("#codAvvisoPagoPA").change(function(){
        if($(this).val().length > 0 && $(this).val().length < 18 ){
         	var diff = 18 - $(this).val().length;
             var codice = '';
             for(var i=0; i<diff; i++){
             	codice = codice + '0';
             }
             var newcodice = codice + $(this).val();
         	$(this).val(newcodice)
         }
       });
    
});

