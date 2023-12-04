/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    'use strict';
    
    var exports = {};
    //SIAC-8513
    var MAX_DECIMA_PLACES = 2;
    
    var tipologiaFcde = $('#fcde-tipo').val();

    function getTipologiaFcde(){
        return tipologiaFcde;
    }

    function isPrevisione(){
        return tipologiaFcde === 'PREVISIONE';
    }

    function isNotPrevisione(){
        return !isPrevisione();
    }

    function isGestione(){
        return tipologiaFcde === 'GESTIONE';
    }

    function isNotGestione(){
        return !isGestione();
    }

    function isRendiconto(){
        return tipologiaFcde === 'RENDICONTO';
    }

    function isNotRendiconto(){
        return !isRendiconto();
    }

    function isMediaPonderata(tipoMedia){
        return tipoMedia === 'ponderata-totali' || tipoMedia === 'ponderata-rapporti';
    }

    function confirm(title, message, yesCbk, yesTxt = 'S&igrave;', noCbk = null, noTxt = 'No') {
        var deferred = $.Deferred();

        bootboxAlert(message, title, 'dialogWarn', [
            {label: yesTxt, 'class': 'btn', callback: yesCbk },
            {label: noTxt, 'class': 'btn', callback: noCbk == null ? deferred.reject.bind(deferred) : noCbk }
        ]);
        return deferred.promise();
    }

    function presaVisione(title, message, yesCbk, yesTxt = 'Chiudi') {
        var deferred = $.Deferred();

        bootboxAlert(message, title, 'dialogWarn', [
            {label: yesTxt, 'class': 'btn', callback: yesCbk == null ? deferred.reject.bind(deferred) : yesCbk}
        ]);
        return deferred.promise();
    }

    function confirmWithOption(title, message, yesCbk, yesTxt = 'S&igrave;', yes2Cbk, yes2Txt = 'S&igrave; {2}', noCbk = null, noTxt = 'No') {
        var deferred = $.Deferred();

        bootboxAlert(message, title, 'dialogWarn', [
            {label: yesTxt, 'class': 'btn', callback: yesCbk },
            {label: yes2Txt, 'class': 'btn', callback: yes2Cbk },
            {label: noTxt, 'class': 'btn', callback: noCbk == null ? deferred.reject.bind(deferred) : noCbk }
        ]);
        return deferred.promise();
    }

    function sumObjVal(obj, peso) {
    	var s = new BigNumber(0);
    	
	    obj.each(function(){
	    	if(this.nodeName === 'SPAN') {
	    		if ($(this).html() !== '') {  
		    		s = new BigNumber(parseLocalNum($(this).html())).times(peso ? $(this).data('peso') : 1).plus(s);
		    	}
	    	} else {
		    	if ($(this).val() !== '') {  
		    		s = new BigNumber(parseLocalNum($(this).val())).times(peso ? $(this).data('peso') : 1).plus(s);
		    	}
	    	}
	    });

	    return s;
    }

    function countNotEmpty(obj, peso = false) {
    	var n = new BigNumber(0);
    	
	    obj.each(function() {
	    	if(this.nodeName === 'SPAN') {
	    		n = n.plus(new BigNumber($(this).html() !== '' ? (peso ? $(this).data('peso') : 1) : 0));
	    	} else {
	    		n = n.plus(new BigNumber($(this).val() !== '' ? (peso ? $(this).data('peso') : 1) : 0));
	    	}
	    });

	    return n;
    }
    
    function limitPercentage(percentage){
        // division by zero return infinite number
        if(!percentage.isFinite()) return '';

        var maxPercetangeAallowed = new BigNumber(100.00);
        return percentage.comparedTo(maxPercetangeAallowed) > 0 ? maxPercetangeAallowed : percentage;
    }

    function calcoloMediaTotali(importiEPercentualiObj){
        return calcoloMediaTotaliConPeso(importiEPercentualiObj, false);
    }

    function calcoloMediaTotaliConPeso(importiEPercentualiObj, peso = true){
        return limitPercentage(sumObjVal(importiEPercentualiObj['importiIncassiObj'], peso).times(100)
            .dividedBy(sumObjVal(importiEPercentualiObj['importiAccertamentiObj'], peso)));
    }

    function calcoloMediaRapporti(importiEPercentualiObj){
        return calcoloMediaRapportiConPeso(importiEPercentualiObj, false);
    }

    function calcoloMediaRapportiConPeso(importiEPercentualiObj, peso = true){
        return sumObjVal(importiEPercentualiObj['percIncassi'], peso)
            .dividedBy(countNotEmpty(importiEPercentualiObj['percIncassi'], peso));
    }

    /**
     * Metodo di gestione dell'abilitazione/totale dei pulsanti 
     * 
     * @param { array } arraySelettori 
     * @param { boolean } disabilitaPulsante 
     */
    function bloccaPulsanti(arraySelettori, disabilitaPulsante){
        // controllo formale
        if(arraySelettori !== undefined && arraySelettori.length > 0){
            arraySelettori.forEach(function(buttonSelector){
                abilitaDisabilitaPulsante(buttonSelector, disabilitaPulsante);
            });
        }
    }

    /**
     * Metodo di gestione dell'abilitazione/totale dei link 
     * 
     * @param { array } arraySelettori 
     * @param { boolean } disabilitaLink
     */
    function bloccaLink(arraySelettori, disabilitaPulsante){
        if(arraySelettori !== undefined && arraySelettori.length > 0){
            arraySelettori.forEach(function(selettore){
                // non e' un button devo gestirne la disabilitazione
                $(selettore).attr('disabled', disabilitaPulsante)
                    .on('click', function(e){
                        e && e.preventDefault();
                    });
            });
        }
    }

    function unicoPulsanteAbilitato(){
        var arrBtn = [
            '.btn-previsione',
            '.btn-gestione',
            '.btn-rendiconto',
            '.btn-anagrafica'
        ];
        var abilitati = [];
        arrBtn.forEach(function(buttonSelector){
            if(!checkPulsanteDisabilitato(buttonSelector)){
                abilitati.push(buttonSelector);
            }
        });
        return !(abilitati.length > 1)
    }

    function checkPulsanteDisabilitato(buttonSelector){
        return $(buttonSelector).is(':disabled');
    }

    function abilitaDisabilitaPulsante(buttonSelector, disabilita) {
        $(buttonSelector).prop('disabled', disabilita);
    }

    function isValorizzato($value){
        // se non e' undefined, null o blank allora e' valorizzato
        return $value !== undefined && $value !== null && ($value !== '' || $value !== "");
    }

    function isZeroValue($value){
        return $value === '0' || $value === '0,00';
    }

    function isNonValorizzato($value){
        return !isValorizzato($value);
    }

    function getRigaAccantonamentoDaCella($this){
        return $this.parent().parent();
    }

    function getStanziamento($this){
        return $this.parent().data("content");
    }

    function getVecchioAccantonamento(capitoloRow){
        return parseLocalNum($('input[class*="oldAcc hidden soloNumeri text-right"]', capitoloRow).val());
    }

    function getMessaggioConfermaSfondamentoAccantonamento(){
        var messaggio = '';
        switch (getTipologiaFcde()) {
            case 'GESTIONE':
                messaggio = 'Il valore dell\'accantonamento inserito &eacute; maggiore dello Stanziamento calcolato del capitolo. Si vuole comunque proseguire?'
                break;
            case 'RENDICONTO':
                messaggio = 'Il valore dell\'accantonamento inserito &eacute; maggiore del Residuo calcolato per il capitolo. Si vuole comunque proseguire?'
                break;
            default:
                break;
        };
        return messaggio;
    }

    function getTitleWarningMessage(){
        // SIAC-8382 non e' piu' la MIN ma la MAX
        return isGestione() ? 'Media utente maggiore alla media di confronto' : 'Media utente maggiore alle medie calcolate';
    }

    function getModalWarningMessage(){
        // SIAC-8382 cambia il messaggio di warning a seguito di superamento della MAX
        return 'La media utente impostata riduce la quota accantonata, secondo il DL n. 118 del 2011. Si intende proseguire comunque con l\'operazione?';
    }

    function estraiNuovoMessaggioTotaleAccertato(indiceAnno) {
        var anno = indiceAnno !== undefined ? $('.a-q' + indiceAnno) : $('.a-q');
        var messaggio = '';
        switch (FcdeUtils.getTipologiaFcde()) {
            case 'PREVISIONE':
                messaggio = 'Totale accertato per l\'anno $ minore del totale incassato'.replace('$', anno.html())
                break;
            case 'GESTIONE':
                messaggio = 'Totale accertato per l\'anno $ minore del totale incassato in conto competenza'.replace('$', anno.html())
                break;
            case 'RENDICONTO':
                messaggio = 'Totale residuo per l\'anno $ minore del totale incassato in conto residui'.replace('$', anno.html())
                break;
            default:
                break;
        }
        return messaggio;
    }

    function getAccertatoCapitolo(capitoloRow){
        return new BigNumber(parseLocalNum($('.importo.modificabile.soloNumeri.denominatore', capitoloRow).val()) || '0');
    }

    function getStanziamentoCapitolo($el){
        return new BigNumber(parseLocalNum($el.data('importo')) || '0');
    }

    function getPercentuaAccantonamentoFCDE(capitoloRow){
        return new BigNumber(parseLocalNum($('.perc-accantonamento', capitoloRow).html()) || '0');
    }

	//task-142
	function getPercentualeMediaUtilizzo(capitoloRow ){
		var percAccantonamento = new BigNumber(parseLocalNum($('.perc-accantonamento', capitoloRow).html()) || '0');
        var mediaConfronto = new BigNumber(100).minus( percAccantonamento );
		var numeratore = new BigNumber(parseLocalNum($('.numeratore', capitoloRow).val()) || '0');
		var stanziamentoCapitolo = new BigNumber(parseLocalNum($('.stanziamento-capitolo', capitoloRow).html()) || '0');
        
		var valoreConfronto1 = stanziamentoCapitolo == 0? new BigNumber(0):numeratore.times(100).dividedBy(stanziamentoCapitolo);
		// media semplice totali in gestione é il contenuto della 4a colonna, ossia l'accantonamento su accertato
		var mediaSempliceTotali = new BigNumber(parseLocalNum($('.perc',capitoloRow).html()) || '100');
		var mediaUtilizzo = BigNumber.max(mediaConfronto, BigNumber.min((isNaN(valoreConfronto1.valueOf())?new BigNumber(0):valoreConfronto1),mediaSempliceTotali));
		
		return mediaUtilizzo;
    }
	//SIAC-8710
	function getPercentuaAccantonamentoFCDEDataAttribute(capitoloRow){
        return new BigNumber(parseLocalNum($('[data-perc-acc-raw]', capitoloRow).attr('data-perc-acc-raw')) || '0');
    }

    function getPercentualeAccantonamentoGraduale(){
        return new BigNumber(parseLocalNum($('#accantonamentoGraduale').val()) || '100');
    }

    function salvaVecchioAccantonamento(importoAccantonamento, capitoloRow){
        $('input[class*="oldAcc hidden soloNumeri text-right"]', capitoloRow).val(importoAccantonamento);
    }

    function setPercetualeFCDE(capitoloRow, mediaAsBigNumber){
        $('.perc-accantonamento', capitoloRow).html(formatMoney(mediaAsBigNumber, MAX_DECIMA_PLACES));
    }

    function setPercetualeSuStanziato(capitoloRow, mediaAsBigNumber){
        $('.perc-stanziamento', capitoloRow).html(formatMoney(mediaAsBigNumber, MAX_DECIMA_PLACES));
    }

    function adeguaERitornaPercentualeACento($selettore, capitoloRow){
        var perc = new BigNumber('100');
        $($selettore, capitoloRow).html(formatMoney(perc, MAX_DECIMA_PLACES));
        return perc;
    }

    function adeguaERitornaPercentualeAZero($selettore, capitoloRow){
        var perc = new BigNumber('0');
        $($selettore, capitoloRow).html(formatMoney(perc, MAX_DECIMA_PLACES));
        return perc;
    }

    // % A
    function calcoloTupleAccertamentiEPercentualeIncassatoSuAccertamenti(capitoloRow){
        var numeratore = $('.numeratore', capitoloRow).val();
        var denominatore = $('.denominatore', capitoloRow).val();
        if(isNonValorizzato(numeratore) || isNonValorizzato(denominatore)){
            return [
                adeguaERitornaPercentualeAZero('.perc', capitoloRow), 
                getAccertatoCapitolo(capitoloRow)
            ];
        }
        // se entrambi i valori sono a 0
        if(isZeroValue(numeratore) && isZeroValue(denominatore)){
            return [
                adeguaERitornaPercentualeACento('.perc', capitoloRow), 
                getAccertatoCapitolo(capitoloRow)
            ];
        }
        var minimaPercIncassoAccertato = BigNumber.min(100, new BigNumber(parseLocalNum(numeratore))
                .times(100)
                .dividedBy(new BigNumber(parseLocalNum(denominatore))));
        // ritorno la tuple con l'accertato e la percentuale di incasso sull'accertato
        return [
            minimaPercIncassoAccertato, 
            getAccertatoCapitolo(capitoloRow)
        ];
    }
    
    // % B
    function calcoloTupleStanziamentoEPercentualeIncassatoSuStanziamento(capitoloRow){
        var numeratore = $('.numeratore', capitoloRow).val();
        var stanziamentoCapitolo = $('.stanziamento-capitolo', capitoloRow).html();
        if(isNonValorizzato(numeratore) || isNonValorizzato(stanziamentoCapitolo)){
            return [
                adeguaERitornaPercentualeAZero('.perc-stanziamento', capitoloRow), 
                new BigNumber(parseLocalNum($('.stanziamento-capitolo' , capitoloRow).html() || '0'))
            ];
        }
        // se ho entrambi i valori a zero 0
        if(isZeroValue(numeratore) && isZeroValue(stanziamentoCapitolo)){
            return [
                adeguaERitornaPercentualeACento('.perc-stanziamento', capitoloRow), 
                new BigNumber(parseLocalNum($('.stanziamento-capitolo' , capitoloRow).html() || '0'))
            ];
        }
        var minimaPercIncassatoStanziamento = BigNumber.min(100, new BigNumber(parseLocalNum(numeratore))
                .times(100)
                .dividedBy(new BigNumber(parseLocalNum(stanziamentoCapitolo))));
        // ritorno la tuple con lo stanziamento e la percentuale di incasso sullo stanziamento
        return [
            minimaPercIncassatoStanziamento, 
            new BigNumber(parseLocalNum($('.stanziamento-capitolo' , capitoloRow).html() || '0'))
        ];
    }

    function getPercentualeMediaDiConfronto(capitoloRow){
        return new BigNumber(parseLocalNum($('span[class="media-confronto"]', capitoloRow).html() || '0,00'));
    }

    /**
     * Il metoto produce un array di valori, se di lunghezza 1 e' la media utente da applicare con lo stanziamento
     * altrimenti se fosse di lunghezza maggiore siamo nel caso delle tuple di valori:
     *      percAccFcde[0] : percentuale di accantonamento
     *      percAccFcde[1] : importo accertato o di stanziamento per accantonamento
     * @param {JQueryElement} capitoloRow 
     * @returns tuple
     */
    function getTuplePercentualeAccantonamentoEAccantonamentoFCDEGestione(capitoloRow){
        // Se non ho un valore valido passo false
        var mediaUtente = $('.media-utente', capitoloRow).val() === '' ? false : $('.media-utente', capitoloRow).val() || false;
        // Se la media utente e' valorizzata esco
        if(mediaUtente){
            $('.prev-media.soloNumeri.w-55.text-right.hide', capitoloRow).html(mediaUtente);
            return [
                new BigNumber(100).minus(new BigNumber(parseLocalNum(mediaUtente) || '0'))
            ];
        }
        var tupleAccertato = calcoloTupleAccertamentiEPercentualeIncassatoSuAccertamenti(capitoloRow);
        var tupleStanziamento = calcoloTupleStanziamentoEPercentualeIncassatoSuStanziamento(capitoloRow);
        setPercetualeSuStanziato(capitoloRow, tupleStanziamento[0]);
        // Min tra la % A e % B
        // task-142 var minimaDellePercentuali = BigNumber.min(tupleAccertato[0], tupleStanziamento[0]);
		var mediaSempliceTotali = new BigNumber(parseLocalNum($('.media-semplice-totali', capitoloRow).val()) || '0');
		var minimaDellePercentuali = BigNumber.min(tupleStanziamento[0], mediaSempliceTotali);

        var nuovaPercAccFcde = new BigNumber(100).minus(confrontoTraPercentualiEMedia(minimaDellePercentuali, capitoloRow));
        // Ritorno una tuple con la nuova percentuale ed il maggiore tra gli importi dell'accertato o stanziamento
        // task-142 return [nuovaPercAccFcde, minimaDellePercentuali.comparedTo(tupleAccertato[0]) === 0 ? tupleAccertato[1] : tupleStanziamento[1]];
		return [nuovaPercAccFcde, BigNumber.max(tupleStanziamento[1],tupleAccertato[1])];
    }

    function confrontoTraPercentualiEMedia(minimaDellePercentuali, capitoloRow){
        var max = BigNumber.max(minimaDellePercentuali, getPercentualeMediaDiConfronto(capitoloRow));
        //salvo la percentuale effettiva utilizzata per il calcolo
        $('.perc-effettiva', capitoloRow).html(formatMoney(max, MAX_DECIMA_PLACES));
        return max;
    }

    function passaggioInDefinitivaOperations() {
        return confirm('Passaggio a stato definitivo dell\'elaborazione',
            'Confermando l\'operazione la versione diventerà definitiva e non potrà più essere modificata, eventuali altre versioni definitive saranno portate in stato di bozza; si vuole proseguire?',
            function() {
                $('#formModificaStatoInDefinitivo_hidden').submit()
            });
    }

    /**
     * SIAC-8421 
     * 
     * nuove richieste rendono impossibile/illeggibile la gestione dell'accantonamento nella sezione comune
     * creaimo uno switch per maggiore leggibilita'.
     * 
     * @param {JQueryElement} capitoloRow 
     * @param {JQueryElement} $el 
     */
     function operazioniAccantonamentoFCDE(capitoloRow, $el) {
        switch (FcdeUtils.getTipologiaFcde()) {
            case 'PREVISIONE':
                impostaAccantonamentoPrevisione(capitoloRow, $el);
                break;
            case 'GESTIONE':
                impostaAccantonamentoGestione(capitoloRow, $el);
                break;
            case 'RENDICONTO':
                impostaAccantonamentoRendiconto(capitoloRow, $el);
                break;
            default:
                break;
        }
    }

    // PREVISIONE
    function impostaAccantonamentoPrevisione(capitoloRow, $el){
        var stanziamentoCapitolo = getStanziamentoCapitolo($el);
        var percAccFcde = getPercentuaAccantonamentoFCDE(capitoloRow);
        //SIAC-8377 si aggiunge al calcolo l'accantonamento graduale
        var percAccGradualeEnte = getPercentualeAccantonamentoGraduale();
        var accantonamento = stanziamentoCapitolo.times(percAccFcde).times(percAccGradualeEnte).dividedBy(10000);
        $el.html(formatMoney(accantonamento));
    }
    
    // GESTIONE
    function impostaAccantonamentoGestione(capitoloRow, $el){
        var stanziamentoCapitolo = getStanziamentoCapitolo($el);
        // percAccFcde[0 => percentuale di accantonamento]
        // percAccFcde[1 => importo accertato o di stanziamento per accantonamento]
        var percAccFcde = getTuplePercentualeAccantonamentoEAccantonamentoFCDEGestione(capitoloRow);
        //SIAC-8377 si aggiunge al calcolo l'accantonamento graduale
        var percAccGradualeEnte = getPercentualeAccantonamentoGraduale();
        setPercetualeFCDE(capitoloRow, percAccFcde[0]);
        
		//task-142
		var percMediaUtilizzo = getPercentualeMediaUtilizzo(capitoloRow);
		var complementoSuMediaUtilizzo = new BigNumber(100).minus(percMediaUtilizzo);		
		// adeguo la nuova percentuale di accantonamento all'FCDE
        if(percAccFcde.length > 1) {
			//$el.html(formatMoney(percAccFcde[1].times(percAccFcde[0]).times(percAccGradualeEnte).dividedBy(10000)));
			$el.html(formatMoney(percAccFcde[1].times(complementoSuMediaUtilizzo).times(percAccGradualeEnte).dividedBy(10000)));
			return;
        }
        //task-142 var accantonamento = stanziamentoCapitolo.times(percAccFcde[0]).times(percAccGradualeEnte).dividedBy(10000);
 		var accertato = new BigNumber(parseLocalNum(percAccFcde[1]) || '0');
		var maxAccertatoStanziamento = BigNumber.max(accertato, stanziamentoCapitolo);
		//var accantonamento = maxAccertatoStanziamento.times(percAccFcde[0]).times(percAccGradualeEnte).dividedBy(10000);
		var accantonamento = maxAccertatoStanziamento.times(complementoSuMediaUtilizzo).times(percAccGradualeEnte).dividedBy(10000);
        $el.html(formatMoney(accantonamento));
    }

    // RENDICONTO
    function impostaAccantonamentoRendiconto(capitoloRow, $el){
        var stanziamentoCapitolo = getStanziamentoCapitolo($el);
		//SIAC-8710
        var percAccFcde = getPercentuaAccantonamentoFCDEDataAttribute(capitoloRow);
        //SIAC-8377 si aggiunge al calcolo l'accantonamento graduale
        var percAccGradualeEnte = getPercentualeAccantonamentoGraduale();
        var accantonamento = stanziamentoCapitolo.times(percAccFcde).times(percAccGradualeEnte).dividedBy(10000);
        $el.val(formatMoney(accantonamento));
        salvaVecchioAccantonamento($el.val(), capitoloRow);
    }

    function gestioneMedieSemplici(capitoloRow, importiEPercentualiObj){
        var mediaSempliceTotali = formatMoney(calcoloMediaTotali(importiEPercentualiObj), MAX_DECIMA_PLACES); //SIAC-8513;
        var mediaSempliceRapporti = formatMoney(calcoloMediaRapporti(importiEPercentualiObj), MAX_DECIMA_PLACES); //SIAC-8513;

        $('.media-semplice-totali', capitoloRow).html(mediaSempliceTotali);
	    $('.media-semplice-rapporti', capitoloRow).html(mediaSempliceRapporti);
    }

    function gestioneMediePonderate(rigaCompleta, capitoloRow, importiEPercentualiObj){
        // Non mostro le medie ponderate se non ho tutti i valori per i 5 anni
        if(rigaCompleta){
            var mediaPonderataTotali = formatMoney(calcoloMediaTotaliConPeso(importiEPercentualiObj), MAX_DECIMA_PLACES); //SIAC-8513;
            var mediaPonderataRapporti = formatMoney(calcoloMediaRapportiConPeso(importiEPercentualiObj), MAX_DECIMA_PLACES); //SIAC-8513;

            $('.media-ponderata-totali', capitoloRow).prop('disabled', false);
            $('.media-ponderata-totali', capitoloRow).html(mediaPonderataTotali);
            $('.media-ponderata-rapporti', capitoloRow).prop('disabled', false);
            $('.media-ponderata-rapporti', capitoloRow).html(mediaPonderataRapporti);
            return;
        }
        gestioneDefaultMediePonderate(capitoloRow);
    }

    function gestioneDefaultMediePonderate(capitoloRow){
        $('.media-ponderata-totali', capitoloRow).prop('disabled', true);
        $('.media-ponderata-totali', capitoloRow).html('');
        $('.media-ponderata-rapporti', capitoloRow).prop('disabled', true);
        $('.media-ponderata-rapporti', capitoloRow).html('');
    }
        
    function gestioneMedie(rigaCompleta, capitoloRow, importiEPercentualiObj){

        if(isNotGestione()){
        	gestioneMedieSemplici(capitoloRow, importiEPercentualiObj);
        }

        switch (getTipologiaFcde()) {
            case 'PREVISIONE':
                gestioneDefaultMediePonderate(capitoloRow);
                break;
            case 'RENDICONTO':
                gestioneMediePonderate(rigaCompleta, capitoloRow, importiEPercentualiObj);
                break;
            default:
                // La GESTIONE possiede solo la media utente
                break;
        }
    }

    exports.getTipologiaFcde = getTipologiaFcde;
    exports.isPrevisione = isPrevisione;
    exports.isNotPrevisione = isNotPrevisione;
    exports.isGestione = isGestione;
    exports.isNotGestione = isNotGestione;
    exports.isRendiconto = isRendiconto;
    exports.isNotRendiconto = isNotRendiconto;
    exports.isMediaPonderata = isMediaPonderata;
    exports.confirm = confirm;
    exports.presaVisione = presaVisione;
    exports.confirmWithOption = confirmWithOption;
    exports.bloccaPulsanti = bloccaPulsanti;
    exports.bloccaLink = bloccaLink;
    exports.unicoPulsanteAbilitato = unicoPulsanteAbilitato;
    exports.abilitaDisabilitaPulsante = abilitaDisabilitaPulsante;
    exports.isValorizzato = isValorizzato;
    exports.isNonValorizzato = isNonValorizzato;
    exports.getRigaAccantonamentoDaCella = getRigaAccantonamentoDaCella;
    exports.getStanziamento = getStanziamento;
    exports.getVecchioAccantonamento = getVecchioAccantonamento;
    exports.getMessaggioConfermaSfondamentoAccantonamento = getMessaggioConfermaSfondamentoAccantonamento;
    exports.getTitleWarningMessage = getTitleWarningMessage;
    exports.getModalWarningMessage = getModalWarningMessage;
    exports.estraiNuovoMessaggioTotaleAccertato = estraiNuovoMessaggioTotaleAccertato;
    exports.passaggioInDefinitivaOperations = passaggioInDefinitivaOperations;
    exports.salvaVecchioAccantonamento = salvaVecchioAccantonamento;
    exports.operazioniAccantonamentoFCDE = operazioniAccantonamentoFCDE;
    exports.gestioneMedie = gestioneMedie;
    exports.MAX_DECIMA_PLACES = MAX_DECIMA_PLACES;

    w.FcdeUtils = exports;
    
})(jQuery, this);
