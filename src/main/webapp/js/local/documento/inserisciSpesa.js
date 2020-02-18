/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    "use strict";
    var MS_PER_DAY = 1000 * 60 * 60 * 24;
    global.preSubmit = Documento.preSubmit;

    /**
     * Gestisce la data di scadenza del documento dopo la sospensione.
     */
    function gestioneDataScadenzaDopoSospensione() {
        var terminePagamento = parseInt($("#terminePagamentoDocumento").val(), 10);
        var dataScadenzaDopoSospensioneField = $("#dataScadenzaDopoSospensioneDocumento");
        var dataScadenza = $("#dataScadenzaDocumento").val() || "";
        var dataSospensione = $("#dataSospensioneDocumento").val() || "";
        var dataRiattivazione = $("#dataRiattivazioneDocumento").val() || "";

        var dataScadenzaSplit = dataScadenza.split("/");
        var dataSospensioneSplit = dataSospensione.split("/");
        var dataRiattivazioneSplit = dataRiattivazione.split("/");

        var dataSospensioneDate;
        var dataRiattivazioneDate;
        var dayDiff;
        var dataScadenzaDate;
        var newString;

        // Controllo se abbia sufficienti dati
        if(isNaN(terminePagamento) || dataSospensioneSplit.length !== 3 || dataRiattivazioneSplit.length !== 3) {
            return;
        }

        // Costruisco le due date per la differenza (uso Date.UTC per evitare problemi con il cambio ora)
        dataSospensioneDate = Date.UTC(dataSospensioneSplit[2], dataSospensioneSplit[1] - 1, dataSospensioneSplit[0]);
        dataRiattivazioneDate = Date.UTC(dataRiattivazioneSplit[2], dataRiattivazioneSplit[1] - 1, dataRiattivazioneSplit[0]);
        dayDiff = Math.floor((dataRiattivazioneDate - dataSospensioneDate) / MS_PER_DAY);

        // Costruisco la data
        dataScadenzaDate = new Date(dataScadenzaSplit[2], dataScadenzaSplit[1] - 1, dataScadenzaSplit[0]);

        dataScadenzaDate.setDate(dataScadenzaDate.getDate() + dayDiff);
        // Costruisco la nuova stringa
        newString = ('0' + dataScadenzaDate.getDate()).slice(-2) + '/'
                + ('0' + (dataScadenzaDate.getMonth() + 1)).slice(-2) + '/'
                + dataScadenzaDate.getFullYear();
        dataScadenzaDopoSospensioneField.val(newString);
    }

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
			//era gia' presente un campo hidden, non lo inserisco piu': ne cambio semplicemente il valore
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
    function inibisciModificaCodicePCCSeNecessario(inibisciModifica){
    	if(!inibisciModifica){
    		return;
    	}
    	//inibisco la select del codice pcc
    	inibisciSelect($('#codicePCCDocumento'), 'documento.codicePCC.uid');
    	//inibisco la select del codice ufficio destinatario
    	inibisciSelect($('#codiceUfficioDestinatarioDocumento'), 'documento.codiceUfficioDestinatario.uid');
    	
    }
    //CR-2888: Preselezionare l'elemento se ne esiste solo uno se il campo è obbligatorio per quel tipo documento.  
    
    function gestisciCodicePCC(){
    	var $selectCodice = $('#codicePCCDocumento');
    	var $selectUfficio = $('#codiceUfficioDestinatarioDocumento');
    	var uidUfficio = $selectUfficio.val();
    	if(!uidUfficio){
    		// Non ho un ufficio selezionato, non posso filtrare: esco.
    		return;
    	}
    	$selectCodice.overlay('show');
    	return $.postJSON('inserisciDocumentoSpesa_obtainListaCodicePCCByCodiceUfficioDestinatario.do', qualify({uidCodiceUfficioDestinatarioPccToFilter: uidUfficio}))
	    	.then(function(data){
	    		var str="";
	    		var inibisciModificaCodicePCC = $selectUfficio.data('disabilita-pcc');
	    		if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
	    			return;
	    		}
	    		
	    		//tolgo le vecchie opzioni
	    		$selectCodice.find('option').remove();
	    		//tolgo la disabilitazione sulla select del codice: non posso sapere se a quell'ufficio corrisponde uno ed un solo codice 
	    		$selectCodice.removeAttr('disabled');
	    		$selectCodice.parent().find('input[type="hidden"]').remove();
	    		
	    		//popolo la select
	    		str = data.listaCodicePCCFiltered.reduce(function(acc, el) {
	                return acc + '<option value="' + el.uid + '">' + el.codice + ' - ' + el.descrizione + '</option>';
	            }, '<option></option>');
	    		
	    		$selectCodice.html(str);
	    		
	    		if((inibisciModificaCodicePCC || $selectCodice.data("pcc-obbligatorio")) && preSelezionaSeUnicaOpzione($selectCodice)){
	    			inibisciModificaCodicePCCSeNecessario(inibisciModificaCodicePCC);
	    		}
	    	}).always($selectCodice.overlay.bind($selectCodice, 'hide'));
        	
        }

    /**
     * Prepopolamento della causale di sospensione
     */
    function prepopolaCausaleSospensione() {
        var $option = $('option:selected', '#causaleSospensione');
        var template;
        if($option.val() === '0') {
            // Non faccio alcunche'
            return;
        }
        template = $option.data('template');
        $('#causaleSospensioneDocumento').val(template);
    }

    /**
     * Carica le strutture amministrative contabili per la modale di compilazione guidata degli impegni  
     */
    function caricaStrutture(){
        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do', {nomeAzioneDecentrata: $('#nomeAzioneSAC').val()})
        .then(function (data) {
            if(data.listaElementoCodifica.length === 1) {
                $('#HIDDEN_StrutturaAmministrativoContabileUid').val(data.listaElementoCodifica[0].uid);
            }
            ZTreeDocumento.imposta('treeStruttAmm', ZTreeDocumento.SettingsBase, data.listaElementoCodifica, 'HIDDEN_StrutturaAmministrativoContabileUid');
        });
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
    

    $(function() {
        $("#importoDocumento, #arrotondamentoDocumento").on("change", Documento.valorizzaNetto);
        $("#codiceFiscalePignoratoDocumento").on("keypress", function() {
            var self = $(this);
            setTimeout(function() {
                Documento.calcoloCodiceFiscalePignorato(self);
            }, 1);
        });
        $("#dataSospensioneDocumento").on("change changeDate", Documento.gestioneDataRiattivazione);
        $("#terminePagamentoDocumento, #dataRepertorioDocumento").on("change", Documento.gestioneDataScadenza)
            .change();
        
        $("#annoDocumento").on("change blur", Documento.impostaAnnoPerDataEmissione);
        $("#dataRiattivazioneDocumento").on("click", Documento.gestioneClickRiattivazione);

        // Gestione della data di scadenza dopo la sospensione
        $("#dataScadenzaDocumento, #dataSospensioneDocumento, #dataRiattivazioneDocumento, #terminePagamentoDocumento").on("change changeDate", gestioneDataScadenzaDopoSospensione);

        Documento.gestioneDataRiattivazione();

        $("#formInserimentoDocumentoSpesa").on("reset", Documento.resetForm);

        // SIAC-4679
        $('#causaleSospensione').substituteHandler('change', prepopolaCausaleSospensione);

        // SIAC-4680
        if($('#nomeAzioneSAC').length) {
            caricaStrutture();
        }
        
        // SIAC-5311 SIOPE+
        $('#siopeDocumentoTipo').substituteHandler('change', handleTipoDocumentoSiope)
            .change();
        
        //SIAC-5346
        $('#codiceUfficioDestinatarioDocumento').on('change', gestisciCodicePCC).trigger('change');
        
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

}(this, jQuery);