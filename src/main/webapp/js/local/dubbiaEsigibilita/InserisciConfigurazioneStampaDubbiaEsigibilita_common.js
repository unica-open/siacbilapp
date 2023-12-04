/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w) {
    'use strict';

    var exports = {};
    //UTILS
    var FcdeUtils = w.FcdeUtils;

    //SIAC-8513
    var DECIMAL_PLACES_ALLOWED = FcdeUtils.MAX_DECIMA_PLACES;
	//SIAC-8710
	var DECIMAL_PLACES_RAW_VALUES=5;
	
    var baseOptionsDataTable = {
        bPaginate: true,
        bServerSide:true,
        sServerMethod: 'POST',
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: true,
        bDestroy: true,
        oLanguage: {
            sInfo: '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty: '0 risultati',
            sProcessing: 'Attendere prego...',
            sZeroRecords: 'Nessun elemento disponibile',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun dato disponibile'
            }
        }
    };

    // Campi di default
    var fieldsAsTuple = [
        ['numeratore', '.numeratore'],
        ['numeratore1', '.numeratore1'],
        ['numeratore2', '.numeratore2'],
        ['numeratore3', '.numeratore3'],
        ['numeratore4', '.numeratore4'],
        ['denominatore', '.denominatore'],
        ['denominatore1', '.denominatore1'],
        ['denominatore2', '.denominatore2'],
        ['denominatore3', '.denominatore3'],
        ['denominatore4', '.denominatore4'],
        ['mediaUtente', '.media-utente'],
        ['accantonamento', '.accantonamento.accFcde'],
        ['accantonamento1', '.accantonamento.accFcde1'],
        ['accantonamento2', '.accantonamento.accFcde2']
    ];

    var arrLink = [            
        '#pulsanteReset',
        '#pulsanteModificaStatoInDefinitivo',
        '#pulsanteModificaStatoInBozza',
        '#pulsanteRedirezioneIndietro'
    ];


    // var tipologiaFcde = $('#fcde-tipo').val();
    var erroriModaleCapitolo = $('#ERRORI_MODALE_CAPITOLO');
    var componenteACento = new BigNumber(100);

    // MODALE CAPITOLI

    /**
     * Apre la modale di selezione del capitolo
     * @returns (jQuery) la modale
     */
    function apriModaleCapitoloDubbiaEsigibilita() {
        var $modale = $('#modaleGuidaCapitolo');
        $('#fieldsetRicercaGuidataCapitolo').find('select').val('').change();
        $('#HIDDEN_ElementoPianoDeiContiUid').val('');
        $('#HIDDEN_StrutturaAmministrativoContabileUid').val('');
        $('#numeroCapitolo_modale').val('');
        $('#numeroArticolo_modale').val('');
        if($('#HIDDEN_gestioneUEB').val() === 'true'){
            $('#numeroUEB_modale').val('');
        }
        $('#divRisultatiRicercaCapitolo').slideUp();
        erroriModaleCapitolo.slideUp();
        $modale.modal('show');
        return $modale;
    }

    // ========================================================== UTILITA' ====================================================

    /**
     * Gestione del checkbox di selezione globale
     * @param tabella (jQuery) la tabella da considerare
     * @param isPopolaDa (boolean) per decidere su quale selettore scalare
     */
    function gestisciCheckedSelezionaTutti(tabella, isPopolaDa){
        var $checkboxes = tabella.find('tbody').find('input[type="checkbox"]').not(':disabled');
        var $checkedCheckboxes = $checkboxes.filter(':checked');
        var $selezionaTutti = tabella.find('.check-all');
        var method = ($checkboxes.length > 0 && ($checkboxes.length === $checkedCheckboxes.length) ? 'attr' : 'removeAttr');
        $selezionaTutti[method]('checked', 'checked');

        //26.07.2021 gestione della selezione di tutti i checkbox in pagina
        tabella.find('input[class*="tooltip-test check-all"]').on('change', function(){
            //prendo tutti i checkbox dei capitoli nella pagina
            var $checkboxCapitoli = isPopolaDa ? $('input[name*="risultatiRicercaCapitoloPopolaDaUid"]') : $('input[name*="capitolo.uid"]');
            //se non e' selezionato esco resettando i checkbox
            if(!$(this).is(':checked')) {
                gestisciSelezioneTuttiCapitoliInPagina($checkboxCapitoli, false);
                return;
            }
            //applico la selezione
            gestisciSelezioneTuttiCapitoliInPagina($checkboxCapitoli, true);
        });
    }

    /**
     * 26.07.2021
     *
     * Gestisci gli elementi della tabella che permette di marcarli tutti come selezionati
     * o meno a seconda dei parametri, in qualunque caso verra' eseguito
     * il trigger del 'change' per permettere l'esecuzione di eventuali altri listener
     *
     * @param (jQuery selector) $checkboxes
     * @param (boolean) seleziona
     */
    function gestisciSelezioneTuttiCapitoliInPagina($checkboxes, seleziona){
        //li segno o meno come selezionati
        $checkboxes.prop('checked', seleziona)
        //forzo l'innesco del 'change'
        .trigger('change');
    }

	/**
     * SIAC-8222 refactoring per gestire i cookie
     * Callback per la gestione del download.
     */
    function endDownload() {
        var cookieRegex = /siac_ajax_print=(.+?)(?:;|$)/;
        var regexResult = cookieRegex.exec(document.cookie);

        if(regexResult) {
            rimuoviCookie();
            $('body').overlay('hide')
            //Download completato correttamente. Esco.
            return;
        }
   	    setTimeout(endDownload, 250);
    }
    
    function rimuoviCookie(){
    	document.cookie = 'siac_ajax_print=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    }

    //SIAC-8512
    function abilitaPrevisione($this){
        $(".modificabile.numeratore").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore1").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore2").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore3").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore4").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore1").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore2").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore3").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore4").prop('readonly', false).prop('disabled', false);
        $(".modificabile.media-utente").prop('readonly', false).prop('disabled', false);
        $(".applica-media.applica-media-semplice-totali").prop('disabled', false);
        $(".applica-media.applica-media-semplice-rapporti").prop('disabled', false);
        $(".applica-media.applica-media-utente").prop('disabled', false);
        $(".applica-tutti-media").prop('disabled', false);
        $this.addClass('hidden');
        $("#pulsante-salva-importi").removeClass('hidden');
    }

    function abilitaPrevisioneRiscossioneVirtuosa($this){
        $(".modificabile.numeratore").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore1").prop('readonly', false).prop('disabled', false);
        $(".modificabile.numeratore2").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore1").prop('readonly', false).prop('disabled', false);
        $(".modificabile.denominatore2").prop('readonly', false).prop('disabled', false);
        $(".modificabile.media-utente").prop('readonly', false).prop('disabled', false);
        $(".applica-media, .applica-tutti-media").prop('disabled', false);
        $this.addClass('hidden');
        $("#pulsante-salva-importi").removeClass('hidden');
    }

    /**
     * Abilita le modifiche degli importi
     * @param event (Event) l'evento scatenante l'invocazione
     */
    function abilitaModificaImporti() {
        // Blocco tutti i pulsanti che non sono inerenti al salvataggio degli accantonamenti
        bloccaPulsantiModificaAccantonamento();
        var $this = $(this);
        
        // Abilito solo gli anni -1, -2 e -3 e la media utente per la riscossione virtuosa
        if(riscossioneVirtuosaAbilitata() && FcdeUtils.isNotGestione()){
            abilitaPrevisioneRiscossioneVirtuosa($this);
            return;
        }

        //SIAC-8512
        if(FcdeUtils.isPrevisione()){
            abilitaPrevisione($this);
            return;
        }
        
        $(".modificabile").prop('readonly', false).prop('disabled', false);
        $(".applica-media, .applica-tutti-media").prop('disabled', false);
        $this.addClass('hidden');
        $("#pulsante-salva-importi").removeClass('hidden');
    }

    function riscossioneVirtuosaAbilitata(){
        return $('#riscossioneVirtuosaTrue').is(':checked');
    }

    /**
     * Abilita la modifica parametri
     */
    function modificaParametri() {
        bloccaPulsantiModificaParametri();
        $('#pulsante-modifica-parametri').addClass('hidden');
        $('#pulsante-seleziona-capitoli, #alert-selezione-parametri').removeClass('hidden');
        $('.parametri-editabili').removeClass('hidden').prop('readonly', false).prop('disabled', false)
        $('.parametri-non-editabili').addClass('hidden').prop('readonly', true).prop('disabled', true)
    }
    
    function modificaParametriConfirm(){
        FcdeUtils.confirm('Modifica Parametri di Configurazione', 
                'Modificando i parametri i valori dei capitoli verranno ricalcolati e l\'elaborazione potrebbe durare alcuni minuti: si vuole proseguire? ', 
                modificaParametri);

        return false;
    }

    function handleBlurImporto() {
        var $this = $(this);
        var res = handleImportoModificato($this, true);
        if(res === true) {
            var capitolo = $this.closest(".capitolo");
            capitolo.addClass('modificato');
            ricalcola(capitolo);
        }
        return res;
    }

    function handleImportoModificato($this, reformat) {
        if ($this.prop('readonly')) {
            return false;
        }

        var thisParsed = parseLocalNum($this.val().trim());
        var original = $this.data('original');
        $this.removeClass('modificato');

        if (isNaN(thisParsed)) {
            return handleErrorImporto($this[0]);
        }
        if(reformat) {
            $this.val(formatMoney(thisParsed, DECIMAL_PLACES_ALLOWED));
        }

       	if ((original !== '' ^ thisParsed !== '') || new BigNumber(original || 0).comparedTo(new BigNumber(thisParsed || 0)) !== 0) {
            $this.addClass('modificato');
       	}
        return true;
    }

    function handleChangeMediaUtente($this) {
        toggleWarningsUserPercentage($this);

		//SIAC-8710
		$this.attr('data-media-utente-raw', $this.val());

        // Must happen after the parsing... Delay via a timeout
        setTimeout(function() {
            var res = handleImportoModificato($this, true); //SIAC-8513 si passa true per avere due decimali
            if(res === true) {
                var capitolo = $this.closest(".capitolo");
                capitolo.addClass('modificato');
                checkLimitUserPercentage($this);
                ricalcola(capitolo);
            }
            return res;
        }, 10);
    }

    function handleErrorImporto(obj) {
        $(obj).focus();
    }
    
    function setPrevChoose($this) {
        $this.parent().parent().find('.prevChoose').html($this.attr('class'));
    }
    
    function selezionaElaborazione() {
        var uid = +$('#listaElaborazionePrecedente').val(); 
        $('#formSelezionaVersione_hidden').append('<input type="hidden" name="accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.uid" value="' + uid + '" />').submit();
    }
    
    function nuovaVersione() {
        $('#formNuovaVersione_hidden').submit();
    }

    function filterRows() {
        $('tbody').filter('[data-filtro-capitolo]').removeClass('hide').removeClass('hidden-no-save');
        if(!$('#filtroCapitoliAttivo').prop('checked')) {
            return;
        }
        $('[data-filtro]').each(function(i,e) {
            var $this = $(e);
            var tipoFiltro = $this.data('filtro');
            var valore = $this.val();
            if(valore === '') {
                return;
            }
            $('tbody').filter('[data-filtro-capitolo]').filter(function(idx,el) {
                return el.dataset['filtro' + tipoFiltro].indexOf(valore) === -1;
            }).addClass('hide')
            // non permetto la modifica dei valori
            .addClass('hidden-no-save');
        });
    }

    function ricalcola(capitolo) {
        BigNumber.config({ DECIMAL_PLACES: DECIMAL_PLACES_ALLOWED, ROUNDING_MODE: BigNumber.ROUND_HALF_DOWN });

        // Riscossione virtuosa
        if(riscossioneVirtuosaAbilitata() && FcdeUtils.isNotGestione()){
            calcolaPercentualiRiscossioneVirtuosa(capitolo);
            return;
        }

		var importiIncassiObj = $('.incassi .importo', capitolo);
		var importiAccertamentiObj = $('.accertamenti .importo', capitolo);
		var percIncassi = $('.perc-incassi .perc', capitolo);
        var rigaCompleta = true;

		percIncassi.each(function(){
	    	var indiceAnno = $(this).data('indice-anno');
	    	var numeratore = $('.numeratore' + indiceAnno, capitolo).val();
	    	var denominatore = $('.denominatore' + indiceAnno, capitolo).val();
	    	
		    $(this).html(numeratore === '' || denominatore === '' ?
		    		'' : 
			    	new BigNumber(parseLocalNum(denominatore)).eq(0) ? 
			    		'0,00' :
			    		formatMoney(BigNumber.min(100, new BigNumber(parseLocalNum(numeratore)).times(100)
                        .dividedBy(new BigNumber(parseLocalNum(denominatore)))), DECIMAL_PLACES_ALLOWED)
			);

            // Controllo se una riga e' completa
            if(rigaCompleta && (numeratore === '' || denominatore === '')){
                rigaCompleta = false;
            }

            checkWarningTotaleAccertato(capitolo, indiceAnno, parseLocalNum(numeratore), parseLocalNum(denominatore));
	    });
	
        // Gestisco le medie semplici e ponderate
	    FcdeUtils.gestioneMedie(rigaCompleta, capitolo, 
            { 
                'importiIncassiObj': importiIncassiObj, 
                'importiAccertamentiObj': importiAccertamentiObj, 
                'percIncassi': percIncassi 
            }
        );
	    
	    // Ricalcolo la percentuale FCDE
	    calcolaPercentualeFCDE(capitolo);

	    // Impostazione accantonamento
	    $('.accantonamento', capitolo).each(impostaAccantonamento.bind(undefined, capitolo));
    }

    function calcolaPercentualiRiscossioneVirtuosa(capitolo){
        // Prendo le prima tre annualita' (-1, -2 e -3)
        var importiIncassiObj = $('.incassi .importo.riscossioneVirtuosa ', capitolo);
		var importiAccertamentiObj = $('.accertamenti .importo.riscossioneVirtuosa', capitolo);
        var percIncassi = $('.perc-incassi .perc.riscossioneVirtuosa', capitolo);
        var rigaCompleta = false;

		percIncassi.each(function(){
	    	var indiceAnno = $(this).data('indice-anno');
            var numeratore = $('.numeratore' + indiceAnno, capitolo).val();
            var denominatore = $('.denominatore' + indiceAnno, capitolo).val();
            
            $(this).html(numeratore === '' || denominatore === '' ?
                    '' : 
                    new BigNumber(parseLocalNum(denominatore)).eq(0) ? 
                        '0,00' :
                        formatMoney(BigNumber.min(100, new BigNumber(parseLocalNum(numeratore)).times(100)
                        .dividedBy(new BigNumber(parseLocalNum(denominatore)))), DECIMAL_PLACES_ALLOWED)
            );

            checkWarningTotaleAccertato(capitolo, indiceAnno, parseLocalNum(numeratore), parseLocalNum(denominatore));

	    });
	
        // Gestisco le medie semplici e ponderate
	    FcdeUtils.gestioneMedie(rigaCompleta, capitolo, 
            { 
                'importiIncassiObj': importiIncassiObj, 
                'importiAccertamentiObj': importiAccertamentiObj, 
                'percIncassi': percIncassi 
            }
        );

        // Ricalcolo la percentuale FCDE
	    calcolaPercentualeFCDE(capitolo);

	    // Impostazione accantonamento
	    $('.accantonamento', capitolo).each(impostaAccantonamento.bind(undefined, capitolo));
    }

    function checkWarningTotaleAccertato(capitolo, indiceAnno, numeratore, denominatore){
        var $divWarning = capitolo.find('.incassi').find('.div-tooltip-medie');
        var messaggioTooltip = FcdeUtils.estraiNuovoMessaggioTotaleAccertato(indiceAnno);
        var $tooltip = $divWarning.children('a');
        
        //il servizio potrebbe non aver ritornato errori quindi devo creare l'html
        if(!$tooltip || $tooltip.length <= 0){
            $divWarning.html(addWarningsUserPercentage);
            //forzo la rigenerazione dei listener sui tooltip
            $('.tooltip-test').tooltip();
        }
        
        var previousMessage = $tooltip.attr('title') || $tooltip.attr('data-original-title') || '';

        if(numeratore !== undefined && denominatore !== undefined && 
            new BigNumber(numeratore.length > 0 ? numeratore : 0)
                .comparedTo(new BigNumber(denominatore.length > 0 ? denominatore : 0)) > 0){
            //aggiungo al tooltip la stringa del messaggio
            //se il messaggio e' gia' presente esco
            if(previousMessage.indexOf(messaggioTooltip) !== -1 || previousMessage.indexOf(messaggioTooltip + '<br/>') !== -1){
                return;
            }
            //aggiungo il messaggio
            previousMessage = previousMessage.concat(messaggioTooltip + '<br/>');
            $tooltip.attr('title', previousMessage);
            $tooltip.attr('data-original-title', previousMessage);
        } else {
            //se il messaggio non e' presente esco
            if(previousMessage.indexOf(messaggioTooltip) === -1){
                return;
            }
            //rimuovo il messaggio, controllo se possiede un break in coda e nel caso rimuovo anche quello
            previousMessage = previousMessage.replace(previousMessage.indexOf(messaggioTooltip + '<br/>') === -1 ? messaggioTooltip : messaggioTooltip + '<br/>', '');
            $tooltip.attr('title', previousMessage.trim());
            $tooltip.attr('data-original-title', previousMessage.trim());
        }

        if($tooltip.attr('title') !== undefined && $tooltip.attr('title').length > 0) {
            $divWarning.removeClass('hide');
            $tooltip.removeClass('hide');
        } else {
            $divWarning.addClass('hide');
            $tooltip.addClass('hide');
        }
    }
    
    function calcolaPercentualeFCDE(capitolo) {
        var $checkbox = $('input.applica-media:checked', capitolo);
        var media = new BigNumber(0);
        var perc;
		if($checkbox.length) {
           media = new BigNumber(parseLocalNum($('.media-' + $checkbox.data('media'), capitolo).val() || $('.media-' + $checkbox.data('media'), capitolo).html() || '0,00'));
        }
		perc = new BigNumber(100).minus(media);
        $('.perc-accantonamento', capitolo).html(formatMoney(perc, DECIMAL_PLACES_ALLOWED)); //SIAC-8513
		//SIAC-8017
	    $('.perc-accantonamento', capitolo).attr('data-perc-acc-raw', formatMoney(perc, DECIMAL_PLACES_RAW_VALUES));
    }

    function handleApplicaMedia(e) {
        var capitolo = $(this).closest(".capitolo");
        var isMediaPonderata = $(this).hasClass("applica-media-ponderata-totali") || $(this).hasClass("applica-media-ponderata-rapporti");
        // non permetto il click sul radio se, la riscossione virtuosa e' abilitata o non abbiamo la riga degli importi completa 
        // e stiamo usando in entrambi casi una media ponderata
        if((riscossioneVirtuosaAbilitata() && isMediaPonderata) || (!rigaImportiCompleta(capitolo) && isMediaPonderata)
            //SIAC-8512 disabilitare la possibilita' di selezione delle medie ponderate per la previsione
            || (FcdeUtils.isPrevisione() && isMediaPonderata)) {
            e && e.preventDefault();
            return;
        }
        capitolo.addClass('modificato');
        // applico la classe anche sul radio
        $(this).addClass('modificato');
        // gestisco la selezione per poterla usare successivamente
        if(FcdeUtils.isRendiconto()){
            setPrevChoose($(this));
        }
    	ricalcola(capitolo);
    }

    function rigaImportiCompleta(capitolo){
		var rigaCompleta = true;
        $('.perc-incassi .perc', capitolo).each(function(){
	    	var indiceAnno = $(this).data('indice-anno');
	    	var numeratore = $('.numeratore' + indiceAnno, capitolo).val();
	    	var denominatore = $('.denominatore' + indiceAnno, capitolo).val();

            // Controllo se una riga e' completa
            if(rigaCompleta && (numeratore === '' || denominatore === '')) rigaCompleta = false;

	    });
        return rigaCompleta;
    }

    function handleApplicaTuttiMedia() {
        var tipoMedia = $(this).data('media');
        //SIAC-8551
        if(FcdeUtils.isMediaPonderata(tipoMedia) && FcdeUtils.isPrevisione()) return;

    	$('.applica-media-' + tipoMedia).each(function() {
            var $this = $(this);
            // cerco le celle contenenti lo span con le percentuali
            var $children = $this.closest('tbody').children('tr[class="perc-incassi"]').find('span[class^="perc"]');
            var applicareCambioMedia = true;

            // controllo che il capitolo sia tra quelli filtrati altrimenti esco
            if($this.closest('tbody').filter('[data-filtro-capitolo]').hasClass('hidden-no-save')){
                return;
            }

            $children.each(function(){
                // per le medie ponderate non posso applicarle se non ho tutti i valori
                if($(this) && $(this).html() === '' && FcdeUtils.isMediaPonderata(tipoMedia)){
                    applicareCambioMedia = false;
                }
            });

            if(applicareCambioMedia){
                $this.attr('checked', true).trigger('click');
            }
    	});
    }

    function impostaAccantonamento(capitolo, idx, el) {
        var $el = $(el);
        var capitoloRow = capitolo || $el.closest(".capitolo");
        // Gestiamo in con uno switch i vari casi di accantonamento all'FCDE
        FcdeUtils.operazioniAccantonamentoFCDE(capitoloRow, $el);
    }

    function estraiExcelCopiaDa() {
        var $tr = $('input[type=checkbox]:checked', '#risultati-capitolo-popola-da').closest('tr');
        var afde = $tr.map(function(idx, e) {
            return $(e).data('afde');
        }).get();
        estraiExcel(afde, 'capitoli', [{
            title: 'Capitoli',
            columns: [
                { header: 'Anno', extract: function(datum) { return datum.capitolo.annoCapitolo; } },
                { header: 'Capitolo', extract: function(datum) { return datum.capitolo.numeroCapitolo; } },
                { header: 'Articolo', extract: function(datum) { return datum.capitolo.numeroArticolo; } },
                { header: 'Titolo', extract: extractCodiceDescrizione('capitolo.titoloEntrata') },
                { header: 'Tipologia', extract: extractCodiceDescrizione('capitolo.tipologiaTitolo') },
                { header: 'Categoria', extract: extractCodiceDescrizione('capitolo.categoriaTipologiaTitolo') },
                { header: 'SAC', extract: extractCodiceDescrizione('capitolo.strutturaAmministrativoContabile') },
            ]
        }]);
    }

    function extractCodiceDescrizione(path) {
        return function(el) {
            var codice = deepValue(el, path + '.codice');
            var descrizione = deepValue(el, path + '.descrizione');
            if(!codice) {
                return;
            }
            return codice + ' - ' + descrizione;
        }
    }

    exports.popolaDa = function popolaDa(event, simulaAction, titoloPopolaDa, messaggioPopolaDa, ajaxDataProperty, params) {
        var $modale = $('#modale-capitolo-popola-da');
        
        $('.messaggio-popola-da', $modale).html(messaggioPopolaDa);
        $('[data-titolo-popola-da]', $modale).html(titoloPopolaDa);
        $('[data-elaborazione-riferimento-popola-da]', $modale).html('');
        
        var $errori = $('#errori-modale-capitolo-popola-da');
        var divRisultatiRicerca = $('#div-risultati-capitolo-popola-da');
        divRisultatiRicerca.slideUp();
        $errori.slideUp();
        $modale.modal('show');
        
        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();
        
        var opts = {
            sAjaxSource : simulaAction,
            bAutoWidth:false,
            bPaginate: false,
            oLanguage: {
            	sInfo: '', 
            	sInfoFiltered: '',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Nessun elemento disponibile'
            },
            sAjaxDataProp: ajaxDataProperty,
            fnServerData: function (sSource, aoData, fnCallback, oSettings) {
                oSettings.jqXHR = $.ajax({
                    dataType: 'json',
                    type: 'POST',
                    url: sSource,
                    data: $.extend(true, {}, aoData, params || {}),
                    success: function(data) {
                        if(data.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa) {
                            $('[data-elaborazione-riferimento-popola-da]', $modale).html('Elaborazione di riferimento: #<strong>'
                                + data.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.versione + '</strong> del bilancio <strong>'
                                + data.accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.bilancio.anno + '</strong>');
                        }
                        return fnCallback.apply(this, arguments);
                    }
                });
            },
            fnPreDrawCallback: function () {
                $("#div-risultati-capitolo-popola-da").hide();
                $("#risultati-capitolo-popola-da_processing").parent("div").show();
            },
            fnDrawCallback: function () {
                $("#risultati-capitolo-popola-da_processing").parent("div").hide();
                gestisciCheckedSelezionaTutti($("#div-risultati-capitolo-popola-da"), true);
            	$('#div-risultati-capitolo-popola-da').show();
            	$('a[data-popover]', '#risultati-capitolo-popola-da').popover();
            },
            fnCreatedRow: function(nTr, oData) {
                $(nTr).data('afde', oData);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: $('#HIDDEN_gestioneUEB').val() === 'true' ? defaultPerDataTable('capitolo.annoNumeroArticoloUEB') : defaultPerDataTable('capitolo.annoNumeroArticolo')},
                {aTargets: [1], mData: defaultPerDataTable('capitolo.descrizione')},
                {aTargets: [2], mData: codicePopoverDescrizione('capitolo.titoloEntrata')},
                {aTargets: [3], mData: codicePopoverDescrizione('capitolo.tipologiaTitolo')},
                {aTargets: [4], mData: codicePopoverDescrizione('capitolo.categoriaTipologiaTitolo')},
                {aTargets: [5], mData: codicePopoverDescrizione('capitolo.strutturaAmministrativoContabile')},
                {aTargets: [6], mData: function(el) {
                    return '<input type="checkbox" name="risultatiRicercaCapitoloPopolaDaUid" value="' + el.capitolo.uid + '" checked />';
                }}
            ]
        };

        var tabella = $('#risultati-capitolo-popola-da');
        var opzioniTabellaCapitoli = $.extend(true, {},baseOptionsDataTable, opts );
        if ( $.fn.DataTable.fnIsDataTable( tabella.get(0) ) ) {
           tabella.dataTable().fnDestroy();
        }
        tabella.dataTable(opzioniTabellaCapitoli);
        
        return $modale;
    }

    function codicePopoverDescrizione(path) {
        return function(el) {
            var codice = deepValue(el, path + '.codice');
            var descrizione = deepValue(el, path + '.descrizione');
            if(!codice) {
                return '';
            }
            return '<a href="#" data-popover data-placement="left" data-trigger="hover" data-title="descrizione" data-content="' + descrizione + '">' + codice + '</a>';
        }
    }

    // SIAC-8646
    function checkStanziamentoForPercentualeAccantonamento(stanziamento){
        if(eval(stanziamento) === 0){
            bootboxAlert('Impossibile calcolare la percentuale dell\'accantonamento in quanto lo stanziamento del capitolo e\' a zero.');
            return true;
        }
        return false;
    }

    function adeguaNuovoImportoEMediaUtente($this, capitoloRow){
        // var deferred = $.Deferred();
        var accantonamento = new BigNumber(parseLocalNum($this.val()));
        var stanziamento = parseLocalNum(FcdeUtils.getStanziamento($this));
        // controllo su stanziamenti a zero
        if(checkStanziamentoForPercentualeAccantonamento(stanziamento)){
            return;
        }
        var nuovaPercentualeAccantonamento = accantonamento.times(100).dividedBy(new BigNumber(stanziamento));
        // adeguo la percentuale
        $('span[class*="perc-accantonamento"]', capitoloRow).val(nuovaPercentualeAccantonamento);
        var nuovaMediaUtente = componenteACento.minus(nuovaPercentualeAccantonamento);
        // salvo il nuovo importo dato dall'utente
        FcdeUtils.salvaVecchioAccantonamento(formatMoney(accantonamento), capitoloRow);
        // adeguo la media utente
        $this.addClass('modificato');
        //SIAC-8462 
        var $mediaUtente = $('input[class^="media-utente modificabile soloNumeri"]', capitoloRow);
        $mediaUtente.val(formatMoney(nuovaMediaUtente, DECIMAL_PLACES_ALLOWED)); //SIAC-8513
		//SIAC-8710: per i calcoli debbo utilizzare questo valore altrimenti propago errore
		$mediaUtente.attr('data-media-utente-raw', formatMoney(nuovaMediaUtente, DECIMAL_PLACES_RAW_VALUES));
        $mediaUtente.addClass('modificato');
        toggleWarningsUserPercentage($mediaUtente);

        $('.perc-accantonamento', capitoloRow).html(formatMoney(nuovaPercentualeAccantonamento, DECIMAL_PLACES_ALLOWED)); //SIAC-8513
        $('.applica-media.applica-media-utente', capitoloRow).attr('checked', true);
		
		//SIAC-8710: per i calcoli debbo utilizzare questo valore altrimenti propago errore
		$mediaUtente.attr('data-perc-acc-raw', formatMoney(nuovaPercentualeAccantonamento, DECIMAL_PLACES_RAW_VALUES));
		
        $this.val(formatMoney(accantonamento));
        //SIAC-8615
        getTabellaCapitolo($this).addClass('modificato');
        //
        // deferred.resolve();
        // return deferred.promise();
    }

    function getTabellaCapitolo($this){
        return $this.parent().parent().parent();
    }

    /**
     * SIAC-8393
     * funzione di controllo principale per il controllo dell'importo al blur
     */
    function controlloImportoAccantonamento() {
        var $this = $(this);
        var capitoloRow = FcdeUtils.getRigaAccantonamentoDaCella($this);
        // se gli importi sono uguali non c'e' nulla da adeguare
        if(new BigNumber(parseLocalNum($this.val())).comparedTo(new BigNumber(FcdeUtils.getVecchioAccantonamento(capitoloRow))) == 0) {
            return;
        }
        // con when() creo un blocco di controllo sulle varie promise passate 3
        // gestendo automaticamente la catena del $.Deferred() 
        $.when(
            accantonamentoMaggioreDiStanziamento($this, capitoloRow),
            accantonamentoNuovoMinoreDiVecchio($this, capitoloRow)
        )
        // il bind() serve anche per creare il riferimento di una funzione (non solo per passare il contesto ed i parametri)
        // analogalmente a creare una funzione anonima che invoca la funzione
        // es: => function() { callback(); }
        .then(adeguaNuovoImportoEMediaUtente.bind(this, $this, capitoloRow));
    }

    function accantonamentoMaggioreDiStanziamento($this, capitoloRow){
        var deferred = $.Deferred();
        var accantonamento = new BigNumber(parseLocalNum($this.val()));
        // controllo se l'accantonamento supera lo stanziamento
        if(accantonamento.comparedTo(new BigNumber(parseLocalNum(FcdeUtils.getStanziamento($this)))) > 0) {
            FcdeUtils.confirm(
                'Modifica accantonamento fondo crediti dubbia esibilit&agrave;', 
                FcdeUtils.getMessaggioConfermaSfondamentoAccantonamento(),
                function() { return deferred.resolve(); },
                'S&igrave;',
                function(){ return deferred.rejectWith(ripristinaVecchioAccantonamento($this, capitoloRow)); }
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }

    function accantonamentoNuovoMinoreDiVecchio($this, capitoloRow){
        var deferred = $.Deferred();
        var accantonamento = new BigNumber(parseLocalNum($this.val()));
        var accantonamentoPrecedente = new BigNumber(FcdeUtils.getVecchioAccantonamento(capitoloRow));
        // controllo se l'accantonamento impostato e' minore di quello precedente
        if(accantonamento.comparedTo(accantonamentoPrecedente) < 0) {
            FcdeUtils.confirm(
                'Modifica accantonamento fondo crediti dubbia esibilit&agrave;', 
                'Il valore dell\'accantonamento inserito &eacute; minore dell\'accantonamento calcolato. Si vuole comunque proseguire?', 
                function() { return deferred.resolve(); },
                'S&igrave;',
                function(){ return deferred.rejectWith(ripristinaVecchioAccantonamento($this, capitoloRow)); }
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }

    function ripristinaVecchioAccantonamento($this, capitoloRow, trigger = true){
        var vecchioAccantonamento = new BigNumber(FcdeUtils.getVecchioAccantonamento(capitoloRow));
        FcdeUtils.salvaVecchioAccantonamento(formatMoney(vecchioAccantonamento), capitoloRow);
        var vecchiaPercentualeAccantonamento = vecchioAccantonamento.times(100).dividedBy(new BigNumber(parseLocalNum(FcdeUtils.getStanziamento($this))));
        // adeguo la percentuale
        $('span[class*="perc-accantonamento"]', capitoloRow).val(vecchiaPercentualeAccantonamento);
        var vecchiaMediaUtente = componenteACento.minus(vecchiaPercentualeAccantonamento);
        // adeguo la media utente
        $('input[class^="media-utente modificabile soloNumeri"]', capitoloRow).val(formatMoney(vecchiaMediaUtente, DECIMAL_PLACES_ALLOWED)); //SIAC-8513
        // eseguo il trigger dell'evento per far ripartire il ricalcolo
        if(trigger) { $('input[class^="applica-media applica-media-utente"]', capitoloRow).trigger('click'); }
        ritornaSceltaPrecedente($this);
    }

    function ripristinaAccantonamento() {
    	$('#formRipristinaAccantonamento_hidden')
    		.empty()
    		.append('<input type="hidden" name="indiceAccantonamento" value="'+$(this).closest('.capitolo').data('index')+'" />');
    	
        FcdeUtils.confirm('Ripristina valori precedenti', 'Confermando l\'operazione verranno ripristinati i valori del capitolo al salvataggio precedente; si vuole proseguire?', function() { $('#formRipristinaAccantonamento_hidden').submit() });
    	
        return false;
    }

    function eliminaAccantonamento() {
    	$('#formEliminaAccantonamento_hidden')
    		.empty()
    		.append('<input type="hidden" name="indiceAccantonamento" value="'+$(this).closest('.capitolo').data('index')+'" />');
    	
        FcdeUtils.confirm('Elimina Capitolo da calcolo FCDE', 'Confermando l\'operazione il capitolo selezionato verrà eliminato dall\'elaborazione; si vuole proseguire?', function() { $('#formEliminaAccantonamento_hidden').submit() });
    	
        return false;
    }

    function toggleWarningsUserPercentage($this){
        var $divWarning = $this.parent().parent().parent().find('.incassi').find('.div-tooltip-medie');

        var messaggioTooltip = FcdeUtils.getTitleWarningMessage();
        
        //il servizio potrebbe non aver ritornato errori quindi devo creare l'html
        if(!$divWarning.children('a') || $divWarning.children('a').length <= 0){
            $divWarning.html(addWarningsUserPercentage);
            //forzo la rigenerazione dei listener sui tooltip
            $('.tooltip-test').tooltip();
        }

        var $tooltip = $divWarning.children('a');
        var previousMessage = $tooltip.attr('title') || $tooltip.attr('data-original-title') || '';
        
        if(userPercentageLowerThanOtherPercentages($this)){
            //aggiungo al tooltip la stringa del messaggio
            //se il messaggio e' gia' presente esco
            if(previousMessage.indexOf(messaggioTooltip) !== -1 || previousMessage.indexOf(messaggioTooltip + '<br/>') !== -1){
                return;
            }
            //aggiungo il messaggio
            previousMessage = previousMessage.concat(previousMessage.length > 0 ? messaggioTooltip + '<br/>' : messaggioTooltip);
            $tooltip.attr('title', previousMessage);
            $tooltip.attr('data-original-title', previousMessage);
        } else {
            //se il messaggio non e' presente esco
            if(previousMessage.indexOf(messaggioTooltip) === -1){
                return;
            }
            //rimuovo il messaggio, controllo se possiede un break in coda e nel caso rimuovo anche quello
            previousMessage = previousMessage.replace(previousMessage.indexOf(messaggioTooltip + '<br/>') === -1 ? messaggioTooltip : messaggioTooltip + '<br/>', '');
            $tooltip.attr('title', previousMessage.trim());
            $tooltip.attr('data-original-title', previousMessage.trim());
        }

        if(!$tooltip.attr('title') || $tooltip.attr('title').length <= 0 || $tooltip.attr('data-original-title').length <= 0) {
            $divWarning.addClass('hide');
            $tooltip.addClass('hide');
        } else {
            $divWarning.removeClass('hide');
            $tooltip.removeClass('hide');
        }
    }

    function addWarningsUserPercentage(){
        return '<a class="tooltip-test hide" data-html="true" data-placement="right" title="" href="#"><i class="icon-warning-sign icon-2x purple"></i></a>';
    }

    function checkUserPercentageOnClick(e) {
        var $this = $(this);
        var $inputMediaUtente = $this.parent().find('input.media-utente.modificabile');
        //SIAC-8402 non si puo' selezionare la media utente se questa non e' valorizzata
        if(FcdeUtils.isNonValorizzato($inputMediaUtente.val())){
            e && e.preventDefault();
            ritornaSceltaPrecedente($this);
            return false;
        }
        //SIAC-8444: passaggio di parametri aggiuntivi per far gestire a valle della richiesta utente la selezione del radio
        $inputMediaUtente.trigger('change', [true, $this]);
    }

    function checkLimitUserPercentage($this){
        // SIAC-8513 passaggio da 5 a 2 decimali
        if($this.val()){
            var oneHundred = new BigNumber(100);
            var value = new BigNumber(parseLocalNum($this.val()));
            var result = formatMoney(value.comparedTo(oneHundred) > 0 ? oneHundred : value, DECIMAL_PLACES_ALLOWED); //SIAC-8583
        }
        return $this.val(result);
    }

    /**
     * SIAC-8402 se non e' valorizzato il campo devo togliere la spunta e passarla al radio di default
     */
    function userPercentageEmptyAndSelected($this, e){
        if(FcdeUtils.isNonValorizzato($this.val())){
            e && e.preventDefault();
            // giro gestione
            if(FcdeUtils.isGestione()) {
                var capitoloRow = $this.parent().parent().parent();
                FcdeUtils.operazioniAccantonamentoFCDE(capitoloRow, $this);
                return true;
            }
            // giro previsione e rendiconto
            var applicaMediaSemplice = $this.parent().parent().find('input[class^="applica-media applica-media-semplice-totali"]');
            applicaMediaSemplice.attr('checked', true);
            applicaMediaSemplice.trigger('click');
            return true;
        }
        return false;
    }

    //SIAC-8505
    function getUserPercentageRadio($this){
        return $('input[class^="applica-media applica-media-utente"]', $this.parent().parent());
    }

    function checkUserPercentage(e, doSelectRadioButton = true, $radio) {
        var $this = $(this);
        //SIAC-8505
        var radio = $radio || getUserPercentageRadio($this);
        checkLimitUserPercentage($this);
        // SIAC-8402 se non e' valorizzato il campo devo togliere la spunta e passarla al radio di default
        if(userPercentageEmptyAndSelected($this, e)) return;
        // modal warning only for the PREVISIONE and RENDICONTO cases
        // user percentage < the lowest of the percentages
        if(userPercentageLowerThanOtherPercentages($this)){
            FcdeUtils.confirm(
                //modal title
                FcdeUtils.getTitleWarningMessage(),
                //modal message
                FcdeUtils.getModalWarningMessage(), 
                //yes callback
                function(){
                    // SIAC-8444 selezione del radio solo se richiesto dall'utente
                    if(doSelectRadioButton) { radio.attr('checked', true); }
                    handleChangeMediaUtente($this);
                }, 
                //yes button
                'S&igrave;',
                //no callback
                function(){ ritornaSceltaPrecedente($this); }
            );
            return false;
        }
        // SIAC-8444 selezione del radio (in timeout in quanto deve avvenire DOPO il prevent dell'evento originale)
        if(doSelectRadioButton) { setTimeout(radio.attr.bind(radio, 'checked', true), 10); }
        handleChangeMediaUtente($this);
    }

    function bloccaPulsantiModificaParametri(){
        // blocco tutti i pulsanti se sono in modifica
        FcdeUtils.bloccaPulsanti([
            '#pulsante-calcola-campi-report',
            '#pulsante-modifica-campi-report',
            '#pulsante-modifica-importi',
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
        ], true);

        // blocco anche gli elementi che non sono considerati dei pulsanti
        FcdeUtils.bloccaLink(arrLink, true);
    }

    function bloccaPulsantiModificaAccantonamento(){
        // blocco tutti i pulsanti se sono in modifica
        FcdeUtils.bloccaPulsanti([
            '#pulsante-calcola-campi-report',
            '#pulsante-modifica-campi-report',
            '#pulsante-conferma-campi-report',
            '#pulsante-modifica-parametri',
            '#pulsante-modifica-importi',
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
        ], true);

        // blocco anche gli elementi che non sono considerati dei pulsanti
        FcdeUtils.bloccaLink(arrLink, true);
    }

    function bloccaPulsanteInStatoDefinitivo(){
        var versioneDefinitiva = $('#statoAccantonamento').val() === 'DEFINITIVA' ? true : false;
        // array di pulsanti da bloccare
        FcdeUtils.bloccaPulsanti([
            '#pulsante-modifica-parametri',
            '#pulsante-modifica-importi',
            '#pulsanteCaricaElaborazioneSelezionata',
            '#pulsanteApriModaleCapitoloDubbiaEsigibilita',
            '#pulsantePopolaDaAnnoPrecedentePrevisione',
            '#pulsantePopolaDaAnnoPrecedenteGestione',
            '#pulsantePopolaDaAnnoPrecedenteRendiconto',
            '#pulsantePopolaDaAnnoPrecedenteAnagraficaCapitoli',
            '.ripristinaAccantonamento',
            '.eliminaAccantonamento',
        ], versioneDefinitiva);

        // blocco anche gli elementi che non sono considerati dei pulsanti
        if(versioneDefinitiva){
            FcdeUtils.bloccaLink([
                '#pulsanteReset'
            ], versioneDefinitiva);
        }
    }

    /**
     * Utilty method for obtaining an array of percentages
     * @returns (Array) array of percentages
     */
     function handlePercentages($this){
        var $tr = $this.parents('tr');
        // percentages to return
        var percentages = [];

        // searching the percentages by class
        ['.media-semplice-totali', 
            '.media-semplice-rapporti', 
            '.media-ponderata-totali', 
            '.media-ponderata-rapporti'
        ].forEach(function(percentageClass) {
            var $percentage = $tr.find(percentageClass);
            // excluding the undefined value
            if($percentage.html() !== undefined){
                percentages.push(parseLocalNum($percentage.html()));
            }
        });

        //filter the array to get an array with no empty values
        return percentages.filter(function(percentage) {
            return percentage && percentage.length > 0;
        }).map(function(percentage) {
            return new BigNumber(percentage);
        });
    }

    /**
     * Utility method for determining the presentation of the modal
     * Rendiconto and Previsione have the percentages array enhanced
     * Gestione have an empty array of percentages so we check that and pass a new value
     */
    function userPercentageLowerThanOtherPercentages($this){
        var percentages = handlePercentages($this);
        var $confronto = $this.parents('tr').find('.confronto .media-confronto');
        //SIAC-8400 controllo che ci sia almeno una percentuale valorizzata
        if(percentages.length === 0 && ($confronto.html() === undefined || $confronto.html() === '')){
            // return false if no value was found
            return false;
        }
        // SIAC-8382 user percentage > the greatest of the percentages
        return getUserPercentage($this).comparedTo(BigNumber.max.apply(null, percentages.length > 0 
            ? percentages : [getMediaConfrontoPerGestione($confronto)])) > 0;
    }

    function getUserPercentage($this){
        return new BigNumber(parseLocalNum($this.val()));
    }

    function getMediaConfrontoPerGestione($confronto){
        return new BigNumber(parseLocalNum($confronto.html() || '0'));
    }

    /**
     * Metodo comune per la creazione del form di salvataggio delle modfiche
     * 
     * @param {this} thiz - [RICHIESTO] - contesto della riga su cui si opera relativa al capitolo
     * @param {array[]} tuple - [RICHIESTO] - array di tuple [[nomeCampo, classeCampo]...]
     */
    function creaStringaPerSalvataggio(thiz, tuple){
        var riga = '';

        // tupleFld => [nomeCampo, classeCampo]
        tuple.forEach(function(tupleFld){
            // uso il selettore usando la classe
            riga += (parseLocalNum($(tupleFld[1], thiz).val()) || '') + ';' ;
        });

        // gestisco il campo tipoMediaPrescelta
        return riga += $('.applica-media:checked', thiz).data('media').replace('-', '_').toUpperCase() + ':';
    }

    function ritornaSceltaPrecedente($this){
        //cerco la riga su cui poggia il radio cliccato
        var row = $this.parent().parent();
        //SIAC-8421
        if(FcdeUtils.isGestione()){
            $this.val($('.prev-media.soloNumeri.w-55.text-right.hide', row).html());
            return;
        }
        //trovo la selezione precedente dell'utente
        var selettore = row.find('td > span[class*="prevChoose hide"]').html() || 'applica-media applica-media-semplice-totali';
        //forzo l'innesco per ritornare alla situazione precedente
        row.find('td > input[class*="' + selettore + '"]').attr('checked', true).trigger('click'); 
    }

    function initVecchioAccantonamento(){
        var $this = $(this);
        var capitoloRow = FcdeUtils.getRigaAccantonamentoDaCella($this);
        FcdeUtils.salvaVecchioAccantonamento($this.val(), capitoloRow);
    }

    function initVecchiaMediaUtente(){
        var $this = $(this);
        $('.prev-media.soloNumeri.w-55.text-right.hide', FcdeUtils.getRigaAccantonamentoDaCella($this)).html($this.val());
    }

    exports.gestisciCheckedSelezionaTutti = gestisciCheckedSelezionaTutti;
    exports.baseOptionsDataTable = baseOptionsDataTable;
    exports.fieldsAsTuple = fieldsAsTuple;
    exports.endDownload = endDownload;
    exports.creaStringaPerSalvataggio = creaStringaPerSalvataggio;
    w.Fcde = exports;

    /**
     * Inizializzazione
     */
    function init(){
    	$('[data-popover]').popover();

        // Gestione modale
        $('#pulsanteApriModaleCapitoloDubbiaEsigibilita').on('click', apriModaleCapitoloDubbiaEsigibilita);
        $('#titoloEntrata').on('change', CapitoloEntrata.caricaTipologia);
        $('#tipologiaTitolo').on('change', CapitoloEntrata.caricaCategoria);
        $('#categoriaTipologiaTitolo').on('change', function() {
            $('#bottonePdC').attr('href','#PDCfin');
            Capitolo.caricaPianoDeiConti(this, true);
        });
        $('#bottonePdC').on('click', Capitolo.controllaPdC);
        Capitolo.caricaStrutturaAmministrativoContabile();
		
		if(FcdeUtils.isNotRendiconto()){
			$('.accantonamento').each(impostaAccantonamento.bind(undefined, null));	
		}
        

        $('#pulsanteEstraiCapitoliPopolaDa').substituteHandler('click', estraiExcelCopiaDa);

        $('#pulsante-modifica-parametri').substituteHandler('click', modificaParametriConfirm);
        $('.importo.modificabile').substituteHandler('change', handleBlurImporto);
        $('.media-utente.modificabile').substituteHandler('change', checkUserPercentage);

        $('#pulsante-modifica-importi').substituteHandler('click', abilitaModificaImporti);

        $('#filtroCapitoliAttivo, [data-filtro]').change(filterRows);
        $('[data-filtro="Capitolo"]').keyup(filterRows);
        filterRows();

        bloccaPulsanteInStatoDefinitivo();

        $('.media-utente.modificabile').on('focus', initVecchiaMediaUtente);

        //SIAC-8393 e SIAC-8394 questo e' da rimuovere se si predispone un precaricamento dell'accantonamento
        $('.accantonamento.modificabile').on('focus', initVecchioAccantonamento);
        //SIAC-8393 e SIAC-8394
        $('.accantonamento.modificabile').on('blur', controlloImportoAccantonamento);

        $('.ripristinaAccantonamento').substituteHandler('click', ripristinaAccantonamento);
        $('.eliminaAccantonamento').substituteHandler('click', eliminaAccantonamento);

        $('.applica-tutti-media').substituteHandler('click', handleApplicaTuttiMedia);
        $('.applica-media').substituteHandler('click', handleApplicaMedia);

        $('.applica-media-utente').substituteHandler('click', checkUserPercentageOnClick);

        $('#pulsanteSelezionaElaborazione').substituteHandler('click', selezionaElaborazione);
        $('#creaNuovaVersione').substituteHandler('click', nuovaVersione);

        $('#eseguiStampa').substituteHandler('click', endDownload);

    }

    $(init);

})(jQuery, this);