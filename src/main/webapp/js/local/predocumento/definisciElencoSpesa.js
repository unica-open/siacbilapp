/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";

    // Campi di default
    var pulsanteCompilazioneGuidata = $("#pulsanteCompilazioneGuidataMovimentoGestione");
    var annoMovimento = $("#annoMovimento");
    var numeroMovimento = $("#numeroMovimentoGestione");
    var numeroSubmovimento = $("#numeroSubmovimentoGestione");
    var alertErrori = $("#ERRORI");

     /**
     * Apre il modale dell'impegno.
     */
    function apriModaleImpegno() {
        // Copio i dati
        $("#annoImpegnoModale").val(annoMovimento.val());
        $("#numeroImpegnoModale").val(numeroMovimento.val());
        $("#modaleImpegno").modal("show");
    }
    
    /**
     * Calcola il movimento di gestione se sono stati forniti sufficienti dati.
     */
    function caricaMovimentoGestione() {
    	//controllo la validita' dei dati forniti in input
    	if( !annoMovimento.val() || !numeroMovimento.val()) {
            return;
        }
    	alertErrori.slideUp();
    	
    	return numeroSubmovimento.val() ? computeSubMovimentoGestione() : computeTestataMovimentoGestione();
        
    }


    /**
     * Ottiene i dati della testata del movimento gestione
     * */
    function computeTestataMovimentoGestione(){
    	// Metto il tipo di movimento in lowercase per pigrizia
        var url = "ricercaImpegnoPerChiaveOttimizzato.do";
        // Attivo l'overlay
        var overlayer = $('#fieldsetImpegno').overlay("show");
        var obj = {};
        
        // Creo l'oggetto per inviare i dati
        obj['impegno.annoMovimento'] = annoMovimento.val();
        obj['impegno.numero'] = numeroMovimento.val();
        obj['impegno.caricaSub'] = false;
        $.postJSON(url, obj)
        .then(function(data) {
        	var event;
            var obj = {};
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
            	return;
            }
                        
            obj['impegno'] = data['impegno'];

            // Lancio l'evento di caricamento
            event = $.Event('impegnoCaricato', obj);
            $(document).trigger(event);
        })
        .always(function() {
            // qualunque cosa sia successa, chiudo l'overlay
            overlayer.overlay("hide");
        });
    }
    
    /**
     * Ottiene i dati del submovimento di gestione e della testata
     * */
    function computeSubMovimentoGestione(){
        var url = 'ricercaMovimentoGestione_cercaImpegnoSubImpegno.do';
        var i ; 
        // Attivo l'overlay
        var obj = {};
        var subs;
        var numeroSubmovimentoValue = numeroSubmovimento.val();
        var overlayer = $('#fieldsetImpegno').overlay("show");

        // Creo l'oggetto per inviare i dati
        obj['impegno'] = {};
        obj['impegno' + ".annoMovimento"] = annoMovimento.val();
        obj['impegno.numero'] = numeroMovimento.val();
        obj["numeroSubmovimentoGestione"] =  numeroSubmovimentoValue;
        $.postJSON(url, obj)
        .then(function(data) {
        	var event;
            var obj = {};
            var found = false;
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
            	return;
            }
                       
        	subs = data['impegno']['elencoSubImpegni'] || [];
            for(i = 0; i < subs.length && !found; i++) {
                // Uso il == in vece del === in quanto uno dei due campi e' numerico e l'altro e' una stringa
                // TODO: usare il + su entrambi i campi per forzare il cast?
                if(subs[i] && subs[i].numero == numeroSubmovimentoValue) {
                	obj['subimpegno'] = subs[i];
                    found = true;
                }
            }
            if(!found) {
                impostaDatiNegliAlert(["COR_ERR_0010 - Il valore del parametro subimpegno non e' consentito: non e' presente nell'elenco dei submovimenti"], alertErrori);
                numeroSubmovimento.val("");
                return;
            }
        	
        	
            obj['impegno'] = data['impegno'];

            // Lancio l'evento di caricamento
            event = $.Event('impegnoCaricato', obj);
            $(document).trigger(event);
        })
        .always(function() {
            // Chiudo l'overlay
            overlayer.overlay("hide");
        });
    }
    
    function showDisponibileImpegno(e) {
    	var impegno = e.impegno;
    	var subimpegno = e.subimpegno;
    	var disponibile = subimpegno && subimpegno.numero ? subimpegno.disponibilitaLiquidare : impegno.disponibilitaLiquidare;
    	if(!disponibile){
    		return;
    	}
    	$("#disponibilitaMovimentoGestione").html(disponibile.formatMoney());
    	$("#divDisponibilitaMovimentoGestione").show();
    }
    
    
   function cercaElenco(){
    	var annoElenco = $('#annoElencoDocumentiAllegato').val();
    	var numeroElenco = $('#numeroElencoDocumentiAllegato').val();
    	var obj = {anno: annoElenco, numero:numeroElenco};
    	var form = $('#divRicercaElenco').overlay('show');
    	alertErrori.slideUp();
    	$('#HIDDEN_uidElencoDocumentiAllegato').val('0');
    	$.postJSON('definisciPreDocumentoSpesaPerElenco_cercaElencoTotali.do', qualify(obj, 'elencoDocumentiAllegato')).then(function(data){
    		if(impostaDatiNegliAlert(data.errori, alertErrori)){
    			//non sono riuscita ad ottenere i dati dell'impegno
    			return;
    		}
    		//popolo i campi del numero delle predisposizioni
    		$('#numeroPredisposizioniTotale').val(data.numeroPreDocumentiTotale);
    		$('#numeroPredisposizioniIncomplete').val(data.numeroPreDocumentiIncompleti); 
    		$('#numeroPredisposizioniDefinite').val(data.numeroPreDocumentiDefiniti);

    		//popolo i campi dgli importi delle predisposizioni
    		$('#importoPredisposizioniTotale').val(data.importoPreDocumentiTotale.formatMoney());
    		$('#importoPredisposizioniIncomplete').val(data.importoPreDocumentiIncompleti.formatMoney());
    		$('#importoPredisposizioniDefinite').val(data.importoPreDocumentiDefiniti.formatMoney());
    		//mostro il div appena popolato
    		$('#totaliElencoPredocumenti').slideDown();
    		
    		$('#HIDDEN_uidElencoDocumentiAllegato').val(data.elencoDocumentiAllegato && data.elencoDocumentiAllegato.uid);
    	}).always(form.overlay.bind(form, 'hide'));
    }
    
    $(function() {
    	var annoElenco = $('#annoElencoDocumentiAllegato').val();
     	var numeroElenco = $('#numeroElencoDocumentiAllegato').val();
     	var pulsanteRicercaElenco =  $('#pulsanteRicercaElenco');
      
        //funzionalita' relative al div dell'elenco
        pulsanteRicercaElenco.substituteHandler('click', cercaElenco );
        if(annoElenco && numeroElenco){
    		//sono probabilmente arrivata da un ri-caricamento della pagina. ricarico quindi anche l'elenco.
    		pulsanteRicercaElenco.trigger('clik');
    	}
        
        //funzionalita' relative al div dell'impegno
        Impegno.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined,
            undefined, undefined, undefined, "#disponibilitaMovimentoGestione");
        //funzionalita' particolari non gestite dalla gestione centralizzata
        $(document).on("impegnoCaricato", showDisponibileImpegno);
        // Alla modifica dei campi del tipo movimento, anno, numero e numero subdoc, se valorizzati, ricalcolo i dati del movimenti di gestione
        $("#annoMovimento, #numeroMovimentoGestione, #numeroSubmovimentoGestione").on("change", caricaMovimentoGestione).trigger('change');
        pulsanteCompilazioneGuidata.substituteHandler("click", apriModaleImpegno.bind(apriModaleImpegno, this));
    });
}(jQuery));