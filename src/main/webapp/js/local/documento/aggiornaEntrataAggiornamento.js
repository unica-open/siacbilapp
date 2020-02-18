/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function(global, $, doc){
    var exports = {};
    var _oldPreSubmit = global.preSubmit;
    
    global.preSubmit = preSubmit;
    
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
     * Effettua la chiamata per l'attivazione della registrazione GEN/PCC.
     *
//     */
//    function effettuaAttivazioneGENPCC( ) {
//    	var spinner = $("#SPINNER_pulsanteAttivaRegistrazioniContabili");
//    	spinner.addClass("activated");
//        $.postJSON("aggiornamentoDocumentoEntrata_attivaRegistrazioniContabili.do", {})
//        .then(function(data) {
//            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
//                return;
//            }
//            // E' andato a buon fine: carico l'informazione di successo
//            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
//
//        }).always(function() {
//        	spinner.removeClass("activated");
//        });
//    }

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
     * Gestisce la data di scadenza del documento.
     */
    exports.gestioneDataScadenza = function() {
        var terminePagamento = parseInt($("#termineIncassoDocumento").val(), 10);
        var dataScadenzaField = $("#dataScadenzaDocumento");
        var dataEmissione = $("#dataRepertorioDocumento").val() || $("#dataEmissioneDocumento").val();
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
    		.attr("action", "aggiornamentoDocumentoEntrata_attivaRegistrazioniContabili.do")
    		.submit();
    }
    
    function callbackValidazionePrimaNota(){
    	var $body = $('body');
    	$("#formAggiornamentoDatiDocumento").append('<input type="hidden" name="validazionePrimaNotaDaDocumento" value="true"/>');
    	//SIAC-6045
    	$body.overlay('remove');
    	$body.overlay({useMessage: true});
    	submitFormPerAttivaRegistrazioniContabili();
    }
    
    function callbackConfermaAttivaRegistrazioniContabili(){
    	$('body').overlay('show');
    	$.postJSON('aggiornamentoDocumentoEntrata_controllaNecessitaRichiestaUlterioreConferma.do', {})
    	.then(function(data){
    		if(data && data.abilitatoPrimaNotaDaFinanziaria && !data.esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota){
    			impostaRichiestaConfermaUtente('&Egrave; possibile validare la prima nota ora o successivamente.',
    					callbackValidazionePrimaNota,
    					submitFormPerAttivaRegistrazioniContabili,
    					'valida ora',
    					'valida in seguito'
    					);
    			return;
    		}
    		submitFormPerAttivaRegistrazioniContabili();
    	})
    	.always($('body').overlay.bind($('body'), 'hide'));
    }
    
    /**
     * Attiva le registrazioni contabili del documento
     *
     */
    exports.attivaRegistrazioniContabili = function(elt) {
    	impostaRichiestaConfermaUtente( 'Si stanno per attivare le registrazioni GEN e PCC sul documento . Proseguire?', callbackConfermaAttivaRegistrazioniContabili, $.noop);
    };
    
    doc.Aggiornamento = exports;
    return doc;
}(this, jQuery, Documento || {}));


$(function(){
    $("#pulsanteAperturaCompilazioneGuidataSoggetto").on("click", Documento.Aggiornamento.apriModaleSoggetto);
    $("#pulsanteAttivaRegistrazioniContabili").click(Documento.Aggiornamento.attivaRegistrazioniContabili);
    
    $("#termineIncassoDocumento, #dataRepertorioDocumento, #dataEmissioneDocumento").on("change", Documento.Aggiornamento.gestioneDataScadenza);
    $("#importoDocumento, #arrotondamentoDocumento").change(Documento.Aggiornamento.valorizzaNetto);

    $("#importoDocumento").data("importoBase", $("#importoDocumento").val());
    $("#arrotondamentoDocumento").data("importoBase", $("#arrotondamentoDocumento").val());
    $("#nettoDocumento").data("importoBase", $("#nettoDocumento").val());
    
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

