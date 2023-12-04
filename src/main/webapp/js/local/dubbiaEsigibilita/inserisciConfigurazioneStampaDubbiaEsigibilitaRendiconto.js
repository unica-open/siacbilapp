/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    'use strict';
    //COMMON
    var Fcde = w.Fcde;
    //UTILS
    var FcdeUtils = w.FcdeUtils;

    // VARIABILI GENERICHE UTILIZZATE NELLO SCOPE
    var capitoliModaleSelezionati = [];
    var capitoliInTemp = [];
    var baseOptionsDataTable = Fcde.baseOptionsDataTable;
    var alertErrori = $('#ERRORI');
    var alertInformazioni = $('#INFORMAZIONI');
    var erroriModaleCapitolo = $('#ERRORI_MODALE_CAPITOLO');

    // FUNZIONI UTILITA

    // MODALE CAPITOLI

    /**
     * Ricerca il capitolo Entrata Gestione
     * @param event (Event) l'evento scatenante l'invocazione
     * @returns (Promise) la Promise collegata alla ricerca del capitolo
     */
    function ricercaCapitoliModale(event) {
        var $form, arrayToSend;
        var $spinner = $('#SPINNER_RicercaCapitolo');
        // per prima cosa, disabilito il div
        var divRisultatiRicerca = $('#divRisultatiRicercaCapitolo').slideUp();
        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        $form = $(event.target).parents('form');
        arrayToSend = unqualify($form.serializeObject(), 1);
        $spinner.addClass('activated');

        return $.postJSON('selezionaCapitolo_capitoloEntrataGestioneFondiDubbiaEsigibilita.do', arrayToSend)
            .then(function(data) {
                var errori = data.errori;

                if(impostaDatiNegliAlert(errori, erroriModaleCapitolo, true, true)){
                    return;
                }
                gestioneTabellaCapitoliModale();
                divRisultatiRicerca.slideDown();

                $('.modal-body').scrollTop(divRisultatiRicerca.position().top);
            }).always($spinner.removeClass.bind($spinner,'activated'));
    }

    /**
     * Crea e gestisce le chiamate ajax per la tabella di risultati
     */
    function gestioneTabellaCapitoliModale() {
        var opts = {
            sAjaxSource : 'risultatiRicercaCapitoloEntrataGestioneModaleAjax.do',
            bAutoWidth:false,
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#divRisultatiRicercaCapitolo").addClass("hide");
                $("#risultatiRicercaCapitoloEntrataGestione_processing").parent("div")
                    .show();
            },
            fnDrawCallback: function () {
                // Mostro il div del processing
                $("#risultatiRicercaCapitoloEntrataGestione_processing").parent("div")
                    .hide();
                $("#divRisultatiRicercaCapitolo").removeClass("hide");
                Fcde.gestisciCheckedSelezionaTutti($("#divRisultatiRicercaCapitolo"), false);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(src){
                    return '<input type="checkbox" name="capitolo.uid" value="' + src.uid + '" '
                        + (capitoliInTemp[src.uid] ? 'disabled="disabled"' : '') + (capitoliModaleSelezionati[src.uid] ? 'checked' : '') +'  />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('capitolo', oData).substituteHandler('change', function(){
                        gestisciSelezioneCapitoliModale($(this), oData);
                    });
                } },
                {aTargets: [1], mData: defaultPerDataTable('capitolo'), fnCreatedCell: setCssClass('tab_Right')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('classificazione')},
                {aTargets: [4], mData: defaultPerDataTable('struttAmmResp')},
                {aTargets: [5], mData: defaultPerDataTable('pdcFinanziario')}
            ]
        };

        var tabella = $('#risultatiRicercaCapitolo');
        var opzioniTabellaCapitoli = $.extend(true, {}, baseOptionsDataTable, opts );
        if ( $.fn.DataTable.fnIsDataTable( tabella.get(0) ) ) {
           tabella.dataTable().fnDestroy();
        }
        tabella.dataTable(opzioniTabellaCapitoli);
    }

    /**
     * Crea una funzione che imposta una classe CSS
     * @param cssClass (string) la classe CSS da impostare
     * @returns (function(Node) => any) la funzione che imposta la classe CSS
     */
    function setCssClass(cssClass) {
        return function(nTd) {
            nTd.className = cssClass;
        };
    }

    /**
     * Gestione della selezione del capitolo nella modale
     * @param checkbox (jQuery) il checkbox
     * @param capitolo (any)    il capitolo
     */
    function gestisciSelezioneCapitoliModale(checkbox, capitolo){
        if(!checkbox.is(':checked')) {
            capitoliModaleSelezionati[capitolo.uid] = undefined;
            return;
        }
        capitoliModaleSelezionati[capitolo.uid] = {
            capitolo: capitolo
        };
    }

    //SIAC-7858 CM 09/06/2021 Inizio
    function submitForm() {
        var listaCapitoli = capitoliModaleSelezionati.filter(filterOutUndefined).map(mapToObject).map(projectToInput);

        $('#formRicercaCapitoli_hidden')
        	.append(listaCapitoli.join(""))
            .submit();
    }

    function projectToInput(obj, idX){
    	return "<input type='hidden' name='listaCapitoloEntrataGestione[" + idX + "].uid' value='"+ obj.uid + "'/>";
    }

    //SIAC-7858 CM 09/06/2021 Fine
    /**
     * Filtra gli elementi undefined
     * @param el (any) l'elemento da controllare
     * @returns (boolean) se l'elemento non e' undefined
     */
    function filterOutUndefined(el) {
        return el !== undefined;
    }

    /**
     * Mappa l'elemento in un altro elemento
     * @param elem (any) l'elemento da mappare
     * @return (any) l'elemento wrappato
     */
    function mapToObject(elem) {
        return {
            uid: elem.capitolo.uid,
            annoCapitolo: elem.capitolo.annoCapitolo,
            numeroCapitolo: elem.capitolo.numeroCapitolo,
            numeroArticolo: elem.capitolo.numeroArticolo,
            numeroUEB: elem.capitolo.numeroUEB || 1
        };
    }

    function popolaDaAnnoPrecedentePrevisione(event) {
    	Fcde.popolaDa(event,
			'inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_simulaPopolaDaPrevisione.do',
			'Conferma capitoli da precedente previsione',
			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
				'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp'
    	);

    	return false;
    }

    function popolaDaAnnoPrecedenteGestione(event) {
    	Fcde.popolaDa(event,
    			'inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_simulaPopolaDaGestione.do',
    			'Conferma capitoli da precedente assestamento',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
					'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp'
        	);

    	return false;
    }

    function popolaDaAnnoPrecedenteRendiconto(event) {
    	Fcde.popolaDa(event,
    			'inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_simulaPopolaDaRendiconto.do',
    			'Conferma capitoli da precedente rendiconto',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
    				'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp'
        	);

        return false;
    }

    function popolaDaAnnoPrecedenteAnagraficaCapitoli(event) {
    	Fcde.popolaDa(event,
    			'inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_simulaPopolaDaAnagraficaCapitoli.do',
    			'Conferma capitoli da anagrafica',
    			'Confermando l\'operazione verranno copiati tutti i capitoli contrassegnati per calcolo FCDE in anagrafica ' +
    				'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp'
    	);

        return false;
    }

    function popolaDaElaborazionePrecedente(event) {
        var uid = $('#listaElaborazionePrecedente').val();
        if(!uid) {
            return false;
        }

    	Fcde.popolaDa(event,
    			'inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_simulaPopolaDaElaborazione.do',
    			'Conferma capitoli da elaborazione precedente',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'elaborazione selezionata ' +
    				'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp',
    			{'accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.uid': uid}
    	);

        return false;
    }

    function confermaCapitoliPopolaDa() {
        var $modal = $('#modale-capitolo-popola-da');
        $modal.modal('hide');
        var inputs = $modal.find('input[name="risultatiRicercaCapitoloPopolaDaUid"]:checked').map(function(idx, el) {
            return '<input type="hidden" name="listaCapitoloEntrataGestione[' + idx + '].uid" value="' + el.value + '"/>';
        }).get().join('');
        $('#formCapitoliPopolaDa_hidden').append(inputs).submit();
    }

	//SIAC-8710
	function creaStringaPerSalvataggioDataAttribute(thiz, tuple){
        var riga = '';

        // tupleFld => [nomeCampo, classeCampo]
        tuple.forEach(function(tupleFld){
			if(tupleFld[2]){
				var num = $("[data-"+tupleFld[1]+"]", thiz).data(tupleFld[1]);
				riga += (parseLocalNum(num) || '') + ';' ;
			}else{
				// uso il selettore usando la classe
            	riga += (parseLocalNum($(tupleFld[1], thiz).val()) || '') + ';' ;	
			}
            
        });

        // gestisco il campo tipoMediaPrescelta
        return riga += $('.applica-media:checked', thiz).data('media').replace('-', '_').toUpperCase() + ':';
    }

    /**
     * Salva i capitoli modificati
     */
    function salvaCapitoli() {
    	$(this).prop('disabled', true);

    	var form = $('#form-salvaCapitoli');
    	// var toInputHidden = function(i, n, v) {
    	// 	return '<input type="hidden" name="listaAccantonamentoFondiDubbiaEsigibilitaRendicontoTemp['+i+'].'+n+'" value="'+v+'" />';
    	// }

        var salvataggio = '';

    	$('.capitolo.modificato').each(function(){
            salvataggio += $(this).data('uid') + ';';
    		// form.append(toInputHidden(i, 'uid', $(this).data('uid')));
            salvataggio += creaStringaPerSalvataggioDataAttribute(this, [
                ['numeratore', '.numeratore', false],
                ['numeratore1', '.numeratore1', false],
                ['numeratore2', '.numeratore2', false],
                ['numeratore3', '.numeratore3', false],
                ['numeratore4', '.numeratore4', false],
                ['denominatore', '.denominatore', false],
                ['denominatore1', '.denominatore1', false],
                ['denominatore2', '.denominatore2', false],
                ['denominatore3', '.denominatore3', false],
                ['denominatore4', '.denominatore4', false],
                ['mediaUtente', 'media-utente-raw', true],
                ['accantonamento', '.accantonamento.accFcde', false],
                ['residuo', '.residuo.soloNumeri', false]
            ]);
       	});

        form.append('<input type="hidden" name="salvataggio" value="'+ salvataggio +'" />');

        form.submit();
    }

    function bloccaPulsantiModificaParametriReport(blocca){
        // blocco tutti i pulsanti se sono in modifica
        FcdeUtils.bloccaPulsanti([
            '#pulsante-calcola-campi-report',
            '#pulsante-modifica-parametri',
            '#pulsante-seleziona-capitoli',
            '#pulsante-modifica-importi',
            '#pulsante-salva-importi',
            '#pulsanteCaricaElaborazioneSelezionata',
            '#pulsanteApriModaleCapitoloDubbiaEsigibilita',
            '#pulsantePopolaDaAnnoPrecedentePrevisione',
            '#pulsantePopolaDaAnnoPrecedenteGestione',
            '#pulsantePopolaDaAnnoPrecedenteRendiconto',
            '#pulsantePopolaDaAnnoPrecedenteAnagraficaCapitoli',
            '.ripristinaAccantonamento',
            '.eliminaAccantonamento',
            '#pulsanteSelezionaElaborazione',
            '#creaNuovaVersione',
            '#eseguiStampa',
            '#eseguiStampaCreditiStralciati',
            '#eseguiStampaAccertame',
        ], blocca);

        // Se non devo bloccare allora esco e ricarico la pagina per riabilitare i link
        if(!blocca){
            location.reload();
            return;
        }

        // blocco anche gli elementi che non sono considerati dei pulsanti
        FcdeUtils.bloccaLink([
            '#pulsanteReset',
            '#pulsanteModificaStatoInDefinitivo',
            '#pulsanteModificaStatoInBozza',
            '#pulsanteRedirezioneIndietro',
        ], blocca);
    }

    /**
     * Esegue il calcolo dei crediti stralciati per il report arconet
     */
     function calcolaCampiReport() {
        
        // Devo cercare i capitoli associati alla versione
        var objChiamataAjax = {
            'accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.uid' : $('#inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_salvaAttributi_accantonamentoFondiDubbiaEsigibilitaAttributiBilancio_uid').val(),
            'accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.versione' : $('#inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_salvaAttributi_accantonamentoFondiDubbiaEsigibilitaAttributiBilancio_versione').val()
        };

        // metto l'overlay della pagina
        $('body').overlay('show');

        $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_calcolaCreditiStralciati.do', objChiamataAjax)
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)){
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                impostaValoriResponse(data);
            }).always(function() {
                // resetPulsantiECampiReport();
                // tolgo l'overlay della pagina
                $('body').overlay('hide');
            });

        return false;
    }

    /**
     * Abilita la modifica campi report
     */
     function modificaCampiReport() {
        bloccaPulsantiModificaParametriReport(true);
        $('#pulsante-modifica-campi-report').addClass('hidden');
        $('#pulsante-conferma-campi-report').removeClass('hidden');
        $('.parametri-editabili-campi-report').prop('readonly', false).prop('disabled', false);
    }

    /**
     * Reset pulsanti e campi report
     */
     function resetPulsantiECampiReport() {
        bloccaPulsantiModificaParametriReport(false);
        $('#pulsante-modifica-campi-report').removeClass('hidden');
        $('#pulsante-conferma-campi-report').addClass('hidden');
        $('.parametri-editabili-campi-report').prop('readonly', true).prop('disabled', true);
    }

    /**
     * Metodo per la gestione dei campi per le creazione dell'oggetto request
     */
    function creaOggettoRequest(objChiamataAjax){
        // Array dei campi per il report 
        [
            // campi report
            'creditiStralciati',
            'creditiStralciatiFcde',
            'accertamentiAnniSuccessivi',
            'accertamentiAnniSuccessiviFcde',
            // campi attributi di bilancio
            'accantonamentoGraduale',
            'quinquennioRiferimento',
            'accertamentiAnniSuccessiviFcde',
        ].forEach(function(field){
            objChiamataAjax['accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.' + field] = $('#' + field).val();
        });

        //riscossione virtuosa
        objChiamataAjax['accantonamentoFondiDubbiaEsigibilitaAttributiBilancio.riscossioneVirtuosa'] = $('#riscossioneVirtuosaTrue').prop('cheked') ? true : false;

        return objChiamataAjax;
    }
    
    function impostaValoriResponse(data){
        var accFCDEAttrBil = data.accantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
        if(accFCDEAttrBil !== undefined || accFCDEAttrBil !== null){
            $('#creditiStralciati').val(formatMoney(accFCDEAttrBil.creditiStralciati, 2));
            $('#creditiStralciatiFcde').val(formatMoney(accFCDEAttrBil.creditiStralciatiFcde, 2));
            $('#accertamentiAnniSuccessivi').val(formatMoney(accFCDEAttrBil.accertamentiAnniSuccessivi, 2));
            $('#accertamentiAnniSuccessiviFcde').val(formatMoney(accFCDEAttrBil.accertamentiAnniSuccessiviFcde, 2));
        }
        $('#eseguiStampaCreditiStralciati').attr('disabled', campiCreditiStralciatiNonValorizzati());
        $('#eseguiStampaAccertamentiAnniSuccessivi').attr('disabled', campiAccertamentiAnniSuccessiviNonValorizzati());
    }

    function checkCreditiStralciati(){
        var deferred = $.Deferred();
        // se i valori non sono uguali non salvo i risultati
        if(new BigNumber(parseLocalNum($('#creditiStralciati').val() || '0'))
            .comparedTo(new BigNumber(parseLocalNum($('#creditiStralciatiFcde').val() || '0'))) !== 0) {
            FcdeUtils.presaVisione(
                'Operazione non consentita', 
                'I valori dei campi Crediti stralciati e Crediti stralciati FCDE non corrispondono, assicurarsi che presentino un valore uguale.'
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }
    
    function checkAccertamentiAnniSuccessivi(){
        var deferred = $.Deferred();
        // se i valori non sono uguali non salvo i risultati
        if(new BigNumber(parseLocalNum($('#accertamentiAnniSuccessivi').val() || '0'))
            .comparedTo(new BigNumber(parseLocalNum($('#accertamentiAnniSuccessiviFcde').val() || '0'))) !== 0) {
            FcdeUtils.presaVisione(
                'Operazione non consentita', 
                'I valori dei campi Accertamenti anni successivi e Accertamenti anni successivi FCDE non corrispondono, assicurarsi che presentino un valore uguale.'
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }
    
    // SIAC-8463
    function controlloParametriReportCorrettamenteValorizzati(){
        $.when(
            checkCreditiStralciati(),
            checkAccertamentiAnniSuccessivi()
        )
        .then(function() { salvaParametriReport(); });
    }

    /**
     * Chiamata ajax per il salvataggio dei parametri del report
     */
    function salvaParametriReport() {
        var objChiamataAjax = {};

        objChiamataAjax = creaOggettoRequest(objChiamataAjax);

        // metto l'overlay della pagina
        $('body').overlay('show');

        $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilitaRendiconto_salvaCampiReport.do', objChiamataAjax)
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)){
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                impostaValoriResponse(data);
            }).always(function() {
                resetPulsantiECampiReport();
                // tolgo l'overlay della pagina
                $('body').overlay('hide');
            });

        return false;
    }

    /**
     * 
     */
    function bloccaPulsanteInStatoDefinitivoRendiconto(){
        // array di pulsanti da bloccare
        [
            '#pulsante-calcola-campi-report',
            '#pulsante-modifica-campi-report',
            '#pulsante-conferma-campi-report',
        ].forEach(function(buttonSelector){
            FcdeUtils.abilitaDisabilitaPulsante(buttonSelector, $('#statoAccantonamento').val() === 'DEFINITIVA' ? true : false);
        })
    }

    function campiCreditiStralciatiNonValorizzati(){
        return FcdeUtils.isNonValorizzato($('#creditiStralciati').val()) 
                && FcdeUtils.isNonValorizzato($('#creditiStralciatiFcde').val());
    }

    function campiAccertamentiAnniSuccessiviNonValorizzati(){
        return FcdeUtils.isNonValorizzato($('#accertamentiAnniSuccessivi').val()) 
                && FcdeUtils.isNonValorizzato($('#accertamentiAnniSuccessiviFcde').val());
    }

    function campiAllegatoCNonValorizzati(){
        // controllo che tutti i valori siano valorizzati
        return FcdeUtils.isNonValorizzato($('#creditiStralciati').val()) 
                || FcdeUtils.isNonValorizzato($('#creditiStralciatiFcde').val()) 
                || FcdeUtils.isNonValorizzato($('#accertamentiAnniSuccessivi').val()) 
                || FcdeUtils.isNonValorizzato($('#accertamentiAnniSuccessiviFcde').val());
    }

    function controllaCampiAllegatoC(){
        // impedisco il cambio stato durante la modifica di una versione in bozza
        if($('#pulsante-modifica-importi').is(':disabled')){
            return false;
        }

        //SIAC-8395 controllo che i parametri del report arconet siano valorizzati
        if(campiAllegatoCNonValorizzati()){
            FcdeUtils.confirm('Passaggio a stato definitivo dell\'elaborazione', 'Per la versione in Definizione non sono stati indicati i i valori nei campi report per l\'allegato - C; si vuole proseguire?', function() { modificaStatoInDefinitivo() });
            return false;
        }

        modificaStatoInDefinitivo();
        return false;
    }

    function modificaStatoInDefinitivo() {
        // impedisco il cambio stato durante la modifica di una versione in bozza
        if($('#pulsante-modifica-importi').is(':disabled')){
            return false;
        }

        $('#formModificaStatoInDefinitivo_hidden').empty();
        // SIAC-8426
        FcdeUtils.passaggioInDefinitivaOperations();
        return false;
    }

    function modificaStatoInBozza() {
    	$('#formModificaStatoInBozza_hidden')
    		.empty();
    	
        FcdeUtils.confirm('Ritorno dell\'elaborazione a stato bozza', 'Confermando l\'operazione la versione tornerà in stato bozza e potrà essere modificata; si vuole proseguire?', function() { $('#formModificaStatoInBozza_hidden').submit() });
    	
        return false;
    }

    /**
     * Inizializzazione
     */
    function init() {

        bloccaPulsanteInStatoDefinitivoRendiconto();

        $('#pulsanteRicercaCapitolo').substituteHandler('click', ricercaCapitoliModale);

        $('button[data-submit]').substituteHandler('click', submitForm);

        $('#pulsante-salva-importi').substituteHandler('click', salvaCapitoli);

        $('#pulsantePopolaDaAnnoPrecedentePrevisione').substituteHandler('click', popolaDaAnnoPrecedentePrevisione);
        $('#pulsantePopolaDaAnnoPrecedenteGestione').substituteHandler('click', popolaDaAnnoPrecedenteGestione);
        $('#pulsantePopolaDaAnnoPrecedenteRendiconto').substituteHandler('click', popolaDaAnnoPrecedenteRendiconto);
        $('#pulsantePopolaDaAnnoPrecedenteAnagraficaCapitoli').substituteHandler('click', popolaDaAnnoPrecedenteAnagraficaCapitoli);
        $('#pulsanteCaricaElaborazioneSelezionata').substituteHandler('click', popolaDaElaborazionePrecedente);

        $('#pulsanteConfermaCapitoliPopolaDa').substituteHandler('click', confermaCapitoliPopolaDa);

        $('#pulsanteModificaStatoInDefinitivo').substituteHandler('click', controllaCampiAllegatoC);
        $('#pulsanteModificaStatoInBozza').substituteHandler('click', modificaStatoInBozza);

        // REPORT ARCONET
        $('#pulsante-calcola-campi-report').substituteHandler('click', calcolaCampiReport);
        $('#pulsante-modifica-campi-report').substituteHandler('click', modificaCampiReport);
        $('#pulsante-conferma-campi-report').substituteHandler('click', controlloParametriReportCorrettamenteValorizzati);
        // FOGLI DI CALCOLO SECONDARI
        $('#eseguiStampaCreditiStralciati').substituteHandler('click', Fcde.endDownload());
        $('#eseguiStampaAccertamentiAnniSuccessivi').substituteHandler('click', Fcde.endDownload());
    }

    $(init);

})(jQuery, this);