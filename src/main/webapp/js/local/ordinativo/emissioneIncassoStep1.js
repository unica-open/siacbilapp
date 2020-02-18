/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var divRicercaQuote = $("#datiRicercaQuote");
    var divRicercaElenco = $("#datiRicercaElenco");
    var divRicercaProvCassa = $("#datiRicercaProvCassa");
    var divRicercaQuoteElencoProvCassa = $('#datiRicercaQuote,#datiRicercaElenco,#datiRicercaProvCassa')
    var radioTipoEmissione = $("input[name='tipoEmissioneOrdinativo']");
    var pdcId;
    var pdc;

   	//////////////////////
    /**
     * Gestione del tipo di emissione per l'ordinativo.
     */
    function gestioneTipoEmissione() {
        var value = radioTipoEmissione.filter(':checked').val();
        divRicercaQuoteElencoProvCassa.slideUp();
 
        if (value === "QUOTE"){
        	divRicercaElenco.slideDown();
        	divRicercaQuote.slideDown();
        }else if(value === "ELENCO"){
        	divRicercaElenco.slideDown();
        }else if(value === "PROVCASSA"){
        	divRicercaProvCassa.slideDown();
        }else{
    	    divRicercaElenco.slideDown();
        }
    }

    /**
     * Gestione del submit del form.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     *
     * @returns <code>true</code> se il form possa essere submittato; <code>false</code> in caso contrario
     */
    function gestioneSubmit(e) {
    	var $this = $(this);
        var checkedRadio = radioTipoEmissione.filter(":checked");
        var action;
        var hiddenString;
        var pdcValue = radioTipoEmissione.filter(':checked').val();
        if(!checkedRadio.length) {
            // Non ho ancora selezionato nulla. Esco e blocco il submit
            impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: Tipo emissione"], alertErrori);
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        // Ho il tipo. Determino l'azione e la eseguo
        action = checkedRadio.data("action");
        $this.attr("action", action);
        

        if (pdcValue === "PROVCASSA"){
            hiddenString = Object.keys(pdc.selectedDatas)
	        .reduce(function(acc, key, idx) {
	        	var value = pdc.selectedDatas[key];
	        	var data = [
	        		value.data.uid || 0,
	        		value.data.anno || 0,
	        		value.data.numero || 0,
	        		value.data.importo || 0,
	        		value.data.importoDaRegolarizzare || 0
	        	];
	        	var str = '<input type="hidden" name="listProvvisorioDiCassa[' + idx + '].causale" value="' + data.join('|') + '" />';
	        	return acc + str;
	        }, '');
	        $this.append(hiddenString);
    	}
        

        $('input[data-checkbox-provvisorio-cassa]').attr('disabled', true);
        return true;
    }

    $(function() {
    	var pdcObj;
        radioTipoEmissione.change(gestioneTipoEmissione);
        gestioneTipoEmissione();
        
        //radioTipoEmissione.filter(':checked').change();
        
        //divRicercaQuoteElencoProvCassa.slideUp();
        
        $("form").substituteHandler("submit", gestioneSubmit);

        // Inizializzazione delle funzionalita' della ricerca provvedimento
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
        // Inizializzazione delle funzionalita' della ricerca capitolo
        Capitolo.inizializza('EntrataGestione', "#annoCapitolo", "#numeroCapitolo", "#numeroArticolo", "#numeroUEB", "#datiRiferimentoCapitoloSpan",
            "#pulsanteCompilazioneGuidataCapitolo", "");
        // Inizializzazione delle funzionalita' della ricerca soggetto
        Soggetto.inizializza("#codiceSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_soggettoDenominazione", "#descrizioneCompletaSoggetto",
            "#pulsanteAperturaCompilazioneGuidataSoggetto");
        // Inizializzazione delle funzionalita' della ricerca elenco
        ElencoDocumentiAllegato.inizializza("#pulsanteApriModaleCompilazioneGuidataElencoDocumentiAllegato", "", "#annoElencoDocumentiAllegato",
            "#numeroElencoDa", "#descrizioneCompletaElenco");
        pdcObj = ProvvisorioDiCassaInline.inizializzazione();
        pdcId = pdcObj.idx;
        pdc = pdcObj.instance;
    });
}(jQuery);