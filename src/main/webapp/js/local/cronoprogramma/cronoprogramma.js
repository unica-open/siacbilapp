/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Cronoprogramma = (function() {
    'use strict';
    var exports = {};

    var alertErrori = $('#ERRORI');
    var baseUrl = $('#HIDDEN_baseUrl').val();
    var itemsPerPage = 5;
    var caricaClassificatori = {};
    var trees = {};
    
    var treeQuadroEconomico = {};
    
    var tipoProgettoCollegato = $('#tipoProgettoAssociato').html();
    var stringaTipoCapitolo = "PREVISIONE" === tipoProgettoCollegato? "Previsione" : "Gestione";
    var suffissoTipoCapitolo = "PREVISIONE" === tipoProgettoCollegato? "p" : "g";
    
    var campiToCheck = {
        'Entrata': [
            {sel: '#descrizioneEntrata', msg: 'Descrizione Entrata'},
            {sel: '#annoEntrata', msg: 'Anno'},
            {sel: '#valorePrevistoEntrata', msg: 'Valore previsto'},
            {sel: 'input[name="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione"]:checked', msg: 'Avanzo di amministrazione'},
            {sel: '#titoloEntrataEntrata', msg: 'Titolo', excludeCheckFnc: isAvanzoAmministrazioneOrEsistente},
            {sel: '#tipologiaTitoloEntrata', msg: 'Tipologia', excludeCheckFnc: isAvanzoAmministrazioneOrEsistente}
        ], 'Uscita': [
            {sel: '#descrizioneUscita', msg: 'Descrizione Spesa'},
            {sel: '#annoUscita', msg: 'Anno spesa', excludeCheckFnc: isDaDefinire},
            {sel: '#annoUscitaEntrata', msg: 'Anno entrata', excludeCheckFnc: isDaDefinire},
            {sel: '#valorePrevistoUscita', msg: 'Valore previsto'},
            {sel: '#missioneUscita', msg: 'Missione'},
            {sel: '#programmaUscita', msg: 'Programma'},
            {sel: '#titoloSpesaUscita', msg: 'Titolo'}
        ]
    };

    // Options per il dataTable
    var optionsComuni = {
        bPaginate : true,
        bLengthChange : false,
        bSort : false,
        bInfo : true,
        bAutoWidth : true,
        bFilter : false,
        bProcessing : true,
        bServerSide : false,
        bDestroy : true,
        iDisplayLength : itemsPerPage,
        oLanguage : {
            sInfo : '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty : '0 risultati',
            sProcessing : 'Attendere prego...',
            oPaginate : {
                sFirst : 'inizio',
                sLast : 'fine',
                sNext : 'succ.',
                sPrevious : 'prec.',
                sEmptyTable : 'Nessun dettaglio disponibile'
            }
        }
    };

    var optionsEntrata = {
        oLanguage : {
            sZeroRecords : 'Non sono presenti dettagli di entrata'
        },
        aoColumnDefs : [
            {aTargets: [0], mData: defaultPerDataTable('descrizioneCapitolo')},
            {aTargets: [1], mData: defaultPerDataTable('annoCompetenza')},
            {aTargets: [2], mData: printCodes('titoloEntrata', 'tipologiaTitolo')},
            {aTargets: [3], mData: generaStringaCapitoloArticoloUEBAvanzoAmministrazione},
            {aTargets: [4], mData: defaultPerDataTable('stanziamento', 0, doFormatMoney), fnCreatedCell: addClass('text-right')},
            {aTargets: [5], bVisible: $('#pulsanteApriInserisciDettaglioEntrata').length >0, mData: writePulsantiRiga, fnCreatedCell: bindFunzioniPressionePulsanti(aggiornaEntrata, eliminaEntrata)}
        ]
    };

    var optionsUscita = {
        oLanguage : {
            sZeroRecords : 'Non sono presenti dettagli di spesa'
        },
        aoColumnDefs : [
            {aTargets: [0], mData: defaultPerDataTable('descrizioneCapitolo')},
            {aTargets: [1], mData: defaultPerDataTable('annoCompetenza')},
            {aTargets: [2], mData: defaultPerDataTable('annoEntrata')},
            {aTargets: [3], mData: printCodes('missione', 'programma', 'titoloSpesa')},
            {aTargets: [4], mData: generaStringaCapitoloArticoloUEB},
            {aTargets: [5], mData: defaultPerDataTable('stanziamento', 0, doFormatMoney), fnCreatedCell : addClass('text-right')},
            {aTargets: [6], bVisible: $('#pulsanteApriInserisciDettaglioUscita').length >0, mData: writePulsantiRiga, fnCreatedCell: bindFunzioniPressionePulsanti(aggiornaUscita, eliminaUscita)}
        ]
    };

    exports.gestioneUEB = undefined;
    exports.apriInserisciDettaglioEntrata = apriInserisciDettaglioEntrata;
    exports.apriInserisciDettaglioUscita = apriInserisciDettaglioUscita;
    exports.consultaTotaliEntrata = consultaTotaliEntrata;
    exports.consultaTotaliUscita = consultaTotaliUscita;
    exports.caricaTabelleDettagli = caricaTabelleDettagli;

    caricaClassificatori.caricaClassificatoriEntrata = caricaClassificatoriEntrata;
    caricaClassificatori.caricaClassificatoriUscita = caricaClassificatoriUscita;

    $(onload);
    return exports;
    
    /**
     * Stampa l'HTML relativo ai pulsanti della riga
     * @returns (string) l'HTML della riga
     */
    function writePulsantiRiga() {
        return '<div class="btn-group">' +
            '<button data-toggle="dropdown" class="btn dropdown-toggle">Azioni <span class="caret"></span></button>' +
            '<ul class="dropdown-menu pull-right">' +
                '<li><a data-toggle="modal" data-operazione="aggiorna">aggiorna</a></li>' +
                '<li><a data-toggle="modal" data-operazione="elimina">elimina</a></li>' +
            '</ul>' +
        '</div>';
    }
    
    /**
     * Effettua il binding delle funzioni fornite con la pressione dei pulsanti
     * @param fncAggiorna (function) la funzione da utilizzare in aggiornamento
     * @param fncElimina (function) una funzione da utilizzare in eliminazione
     * @returns (function) la funzione da utilizzare con dataTables
     */
    function bindFunzioniPressionePulsanti(fncAggiorna, fncElimina) {
        return function(nTd, sData, oData, iRow) {
            $(nTd).find('a[data-operazione="aggiorna"]')
                .on('click', fncAggiorna.bind(undefined, nTd, oData, iRow))
                .end()
            .find('a[data-operazione="elimina"]')
                .on('click', fncElimina.bind(undefined, iRow));
        };
    }
    
	/**
     * Disabilita e abilita il salvataggio generale
     *
	 * @param disabled (boolean) true o false a seconda dell'azione da effettuare
     */
    function disabilitaSalvaGenerale(disabled){
		$('#salvaCrono').attr('disabled', disabled);		 
    }

    /**
     * Stampa i codici richiesti se presenti
     * @returns (function) una funzione che stampa i codici richiesti
     */
    function printCodes() {
        var fields = Array.prototype.slice.call(arguments, 0);
        return function(source) {
            var arr = [];
            if(!source) {
                return '';
            }
            fields.forEach(function(el) {
                if(source[el] && source[el].codice) {
                    arr.push(source[el].codice);
                }
            });
            return arr.join(' - ');
        };
    }
    
    /**
     * Controlla se il dettaglio sia riferito a un capitolo esistente o sia un avanzo di amministrazione
     * @returns (boolean) true se e' da un capitolo esistente o un avanzo di amministrazione; false altrimenti
     */
    function isAvanzoAmministrazioneOrEsistente() {
        var hasUid = $('#uidCapitoloEntrata').val() !== '';
        var isAvanzoAmministrazione = $('input[name="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione"]')
            .filter(function(idx, el) {
                return el.checked;
            }).val() === 'true';
        return hasUid || isAvanzoAmministrazione;
    }
    
    /**
     * Controlla se il cronoprogramma sia da definire
     * @returns (boolean) true se e' da definire; false altrimenti
     */
    function isDaDefinire() {
        return $('input[name="cronoprogramma.cronoprogrammaDaDefinire"]:checked').val() === 'true';
    }

    /**
     * Effettua la formattazione dell'importo
     * @param el l valore da formattare
     * @returns il valore formattato
     */
    function doFormatMoney(el) {
        if(typeof el === 'number') {
            return el.formatMoney();
        }
        return '';
    }

    /**
     * Aggiunge la classe all'elemento
     * @param className la classe da aggiungere
     * @returns una funzione che aggiunge la classe all'elemento fornito
     */
    function addClass(className) {
        return function(el) {
            $(el).addClass(className);
        };
    }

    /**
     * Gestione della classificazione: se da esistente sara' disabilitata.
     *
     * @param fieldsId  (Array)   gli id dei campi
     * @param esistente (Boolean) se i dati siano per capitolo esistente o meno
     */
    function gestioneClassificazione(fieldsId, esistente) {
        fieldsId.forEach(function(val) {
            $('#' + val)[esistente ? 'attr' : 'removeAttr']('disabled', true);
            $('#HIDDEN_' + val)[!esistente ? 'attr' : 'removeAttr']('disabled', true);
        });
    }

    /**
     * Inserimento del dettaglio di entrata.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function apriInserisciDettaglioEntrata(e) {
        e.preventDefault();
        apriCollapseDettaglio(baseUrl + '_apriInserisciDettaglioEntrata.do', undefined, '#collapseDettaglioEntrata')
        .then(function() {
            gestioneClassificazione(['titoloEntrataUscita'], false);
            $('#HIDDEN_tipologiaTitoloEntrata').attr('disabled', true);
            Capitolo.inizializza('Entrata' + stringaTipoCapitolo, '#HIDDEN_annoEsercizio', '#numeroCapitoloEntrata', '#numeroArticoloEntrata', '#numeroUEBEntrata', '#datiRiferimentoCapitoloEntrataSpan', '#daEsistenteEntrataSi', '_ce' + suffissoTipoCapitolo);
        });
    }
    
    //SIAC-6255
    function caricaQuadroEconomicoCallback(data){
    	 var listaQuadroEconomico = (data.listaQuadroEconomico);
    	 var $accordion = $('#accordionPadreQuadroEconomico')
    	 treeQuadroEconomico = new Ztree('QuadroEconomico',undefined, "Nessun servizio selezionato", 'listaQuadroEconomicoFigli');
    	 
    	 treeQuadroEconomico.inizializza(listaQuadroEconomico);
         //ZTree.imposta('treeStruttAmm' + this.suffix, ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
         // Ripristina l'apertura del modale
         $accordion.data('loaded', true);
    }
    
    
    function caricaQuadroEconomico(){
    	 // Pulsante per il modale della Struttura Amministrativo Contabile
        var spinner = $('#SPINNER_');
        var $accordion = $('#accordionPadreQuadroEconomico');
        var datiPresenti = $accordion.data('loaded');
        if(datiPresenti){
//        	treeQuadroEconomico.selezionaNodoSeApplicabile(parseInt($(HIDDEN_QuadroEconomicoUid).val(), 10));
        	return;
        }
        // Non permettere di accedere al modale finche' il caricamento non e' avvenuto
        $accordion.overlay('show');
        // Attiva lo spinner

        $.postJSON(baseUrl + '_caricaListaQuadroEconomico.do',{})
        .then(caricaQuadroEconomicoCallback)
        .always($accordion.overlay.bind($accordion, 'hide'));
    }
    
    function gestioneQuadroEconomico(e){
    	var cronoGestioneQuadroEconomico=$('input[name="cronoprogramma.gestioneQuadroEconomico"]:checked').val();
    	var collapseUscitaAperto = $('#fieldsetDettaglioUscita').length >0 && $('#collapseDettaglioUscita').hasClass('in');
    	var $quadroEconomico = $('#quadroEconomico');
    	
    	e && e.preventDefault();
    	if(!collapseUscitaAperto){
    		return;
    	}
    	
    	if(cronoGestioneQuadroEconomico === 'true'){
    		$('#accordionPadreQuadroEconomico').attr('href', '#quadroEconomico');
    		$('#importoQuadroEconomicoUscita').removeAttr('disabled');
    		caricaQuadroEconomico();
    		return;
    	}
    	$('#accordionPadreQuadroEconomico').removeAttr('href');
		$('#importoQuadroEconomicoUscita').attr('disabled', 'disabled').val('');
		if($quadroEconomico.hasClass('in')){
			$('#quadroEconomico').collapse('hide');
		}
		
		if(treeQuadroEconomico && typeof treeQuadroEconomico.deselezionaNodiEAbilitaAlberatura === 'function'){
			treeQuadroEconomico.deselezionaNodiEAbilitaAlberatura();
		}
		
		$('#HIDDEN_QuadroEconomicoUid').val('0');
		
    		
    		
    }

    /**
     * Inserimento del dettaglio di uscita.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function apriInserisciDettaglioUscita(e) {
        e.preventDefault();
        apriCollapseDettaglio(baseUrl + '_apriInserisciDettaglioUscita.do', undefined, '#collapseDettaglioUscita')
        .then(function() {
            gestioneClassificazione(['missioneUscita', 'titoloSpesaUscita'], false);
            $('#HIDDEN_programmaUscita').attr('disabled', true);
            Capitolo.inizializza('Uscita' + stringaTipoCapitolo, '#HIDDEN_annoEsercizio', '#numeroCapitoloUscita', '#numeroArticoloUscita', '#numeroUEBUscita', '#datiRiferimentoCapitoloUscitaSpan', '#daEsistenteUscitaSi', '_cu' + suffissoTipoCapitolo);
            gestioneQuadroEconomico();
        });
    }

    /**
     * Apertura del collapse del dettaglio.
     *
     * @param url        (String) l'URL da invocare
     * @param obj        (Object) l'oggetto per la chiamata
     * @param collapseId (String) l'id del collapse da caricare
     */
    function apriCollapseDettaglio(url, obj, collapseId) {
		disabilitaSalvaGenerale(true);
		
        return $.post(url, obj)
        .then(function(data) {
            $(collapseId).html(data)
                .find('.soloNumeri')
                    .allowedChars({numeric: true})
                    .end()
                .find('.decimale')
                    .gestioneDeiDecimali(true)
                    .end()
                .collapse('show');
        });
    }

    /**
     * Chiusura del collapse.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function closeCollapse(e) {
		var target = $(e.target);
        var dataTarget = $(target.data('closeCollapse'));
        e.preventDefault();
        dataTarget.collapse('hide');
		disabilitaSalvaGenerale(false);
    }

    /**
     * Salvataggio a partire dal collapse
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function saveCollapse(e) {
        var target = $(e.target);
        var closeCollapseId = target.data('closeCollapse');
        var urlFragment = target.data('urlFragment');
        var tipo = /(Entrata|Uscita)/.exec(urlFragment)[0];
        var campi = campiToCheck[tipo];
        var spinner = target.find('i.spinner');
        var fieldset = $('#fieldsetDettaglio' + tipo);

        e.preventDefault();
        e.stopPropagation();
        e.stopImmediatePropagation();

        salvaDettaglio(spinner, campi, tipo === 'Uscita', urlFragment, $(closeCollapseId), fieldset);
    }

    /**
     * Salvataggio del dettaglio
     *
     * @param spinner            (jQuery)  lo spinner da attivare per la segnalazione all'utente
     * @param campiDaControllare (Object)  i campi da controllare nel form
     * @param isUscita           (Boolean) se sia un'uscita
     * @param urlFragment        (String)  il frammento dell'url da invocare
     * @param collapse           (jQuery)  il collapse da chiudere
     * @param fieldset           (jQuery)  il fieldset da inviare come parametro nella chiamata
     */
    function salvaDettaglio(spinner, campiDaControllare, isUscita, urlFragment, collapse, fieldset) {
        var oggettoPerChiamataAjax = {};
        var listaErrori = controllaCampi(campiDaControllare, []);
        var url = baseUrl + urlFragment + '.do';

        if(isUscita) {
            controllaAnnoEntrataPerUscita(listaErrori);
        }

        // Chiudo l'alert degli errori
        alertErrori.slideUp();

        if(impostaDatiNegliAlert(listaErrori, alertErrori)) {
            return;
        }

        oggettoPerChiamataAjax = fieldset.serializeObject();
        oggettoPerChiamataAjax.cronoprogrammaDaDefinire = isDaDefinire();

        spinner.addClass('activated');

        $.postJSON(url, oggettoPerChiamataAjax)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Ricarico entrambe le tabelle
            caricaTabellaDettagli(data.listaDettaglioEntrataCronoprogramma, 'tabellaEntrata', optionsEntrata);
            caricaTabellaDettagli(data.listaDettaglioUscitaCronoprogramma, 'tabellaUscita', optionsUscita);

            collapse.collapse('hide');
        }).always(spinner.removeClass.bind(spinner, 'activated'));
		
		disabilitaSalvaGenerale(false);
    }

    /**
     * Elimina il dettaglio di entrata.
     *
     * @param row (Number) la riga
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     */
    function eliminaEntrata(row, e) {
        e.preventDefault();
        $('#pulsanteProseguiElimina').substituteHandler('click', function(e) {
            e.preventDefault();
            eliminaRiga('Entrata', row);
        });
        $('#modaleElimina').modal('show');
    }

    /**
     * Elimina il dettaglio di uscita.
     *
     * @param row (Number) la riga
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     */
    function eliminaUscita(row, e) {
        e.preventDefault();
        $('#pulsanteProseguiElimina').substituteHandler('click', function(e) {
            e.preventDefault();
            eliminaRiga('Uscita', row);
        });
        $('#modaleElimina').modal('show');
    }

    /**
     * Verifica la quadratura del Cronoprogramma.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function verificaQuadratura (e) {
        var spinner = $(this).find('spinner');
        e.preventDefault();
        spinner.addClass('activated');

        $.postJSON(baseUrl + 'VerificaQuadratura.do', {})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            $('#quadraturaEntrata').html(data.quadratura.entrata.formatMoney());
            $('#quadraturaUscita').html(data.quadratura.uscita.formatMoney());
            $('#quadraturaDifferenza').html(data.quadratura.differenza.formatMoney());

            $('#modaleVerificaQuadratura').modal('show');
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }
    
    function impostaInputValue(inputSelector, value){
    	if(!inputSelector || !value){
    		return;
    	}
    	$(inputSelector).val(formatDate(value));
    }
    
    function copiaDatiAnagraficaCronoprogramma(campiDaCopiare){
    	if(!campiDaCopiare){
    		return;
    	}
    	impostaInputValue('input[name="cronoprogramma.dataApprovazioneFattibilita"]', campiDaCopiare.dataApprovazioneFattibilita);
    	impostaInputValue('input[name="cronoprogramma.dataApprovazioneProgettoDefinitivo"]', campiDaCopiare.dataApprovazioneProgettoDefinitivo);
    	impostaInputValue('input[name="cronoprogramma.dataAvvioProcedura"]', campiDaCopiare.dataAvvioProcedura);
    	impostaInputValue('input[name="cronoprogramma.dataApprovazioneProgettoEsecutivo"]', campiDaCopiare.dataApprovazioneProgettoEsecutivo);
    	impostaInputValue('input[name="cronoprogramma.dataAggiudicazioneLavori"]', campiDaCopiare.dataAggiudicazioneLavori);
    	impostaInputValue('input[name="cronoprogramma.dataInizioLavori"]', campiDaCopiare.dataInizioLavori);
    	impostaInputValue('input[name="cronoprogramma.dataFineLavori"]', campiDaCopiare.dataFineLavori);
     	$('input[name="cronoprogramma.durataInGiorni"]').val(campiDaCopiare.durataInGiorni);
    	impostaInputValue('input[name="cronoprogramma.dataCollaudo"]', campiDaCopiare.dataCollaudo);
    }

    /**
     * Copia i dati dal cronoprogramma.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function copiaDaCronoprogramma (e) {
        var uidCronoprogramma = $('#listaCronoprogrammaDaCopiare').val();
        var listaErrori = [];
        var spinner = $(this).find('.spinner');
        var oggettoPerChiamataAjax = {};

        if(!uidCronoprogramma) {
            listaErrori.push('Necessario impostare un Cronoprogramma da cui copiare');
        }
        if(impostaDatiNegliAlert(listaErrori, alertErrori)) {
            return;
        }

        e.preventDefault();

        spinner.addClass('activated');
        oggettoPerChiamataAjax.uidCronoprogrammaDaCopiare = uidCronoprogramma;

        return $.postJSON(baseUrl + 'CopiaDaCronoprogramma.do', oggettoPerChiamataAjax)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            // Popolo le liste
            caricaTabellaDettagli(data.listaDettaglioEntrataCronoprogramma, 'tabellaEntrata', optionsEntrata);
            caricaTabellaDettagli(data.listaDettaglioUscitaCronoprogramma, 'tabellaUscita', optionsUscita);
            
            copiaDatiAnagraficaCronoprogramma(data.cronoprogramma);

            $('#checkCopiaDaCronoprogramma').removeAttr('checked');
            $('#divSelezionaCronoprogramma').slideUp();
            $('#campiRicercaCronoprogramma').slideUp();
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }
    
    function popolaSelect(options, tipoProgettoString){
    	var opts = "<option value=''> Scegli </option>";
    	var $select = $("#listaCronoprogrammaDaCopiare");
        options.forEach(function(el) {
        	var stringaisFpv = el.usatoPerFpv ? 'Si' : 'No';
            opts += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + " - Usato Per FPV : " + stringaisFpv +  "</option>";
        });
        $select.html(opts);
    }
    
    function toggleDivSelezioneCronoprogramma(){
    	var checked = $(this).is(':checked');
        var funzioneDaInvocare = checked ? 'slideDown' : 'slideUp';
        $('#campiRicercaCronoprogramma')[funzioneDaInvocare]();
        if(checked){
        	$('#caricaCronoprogrammiDaCopiare').substituteHandler('click', cercaCronoprogrammi).click();
        }
    }

    /**
     * Effettua un toggle del div di selezione del Cronoprogramma.
     */
    function cercaCronoprogrammi() {
        var button = $('#caricaCronoprogrammiDaCopiare').overlay({usePosition: true});
        var oggettoPerChiamataAjax = {};
        var tipoProgetto = $('#tipoProgettoRicercaCrono').val();
        button.overlay('show');
        $('#selezionaCronoprogramma').val('');
        $('#divSelezionaCronoprogramma').slideUp();
        //task-170
        oggettoPerChiamataAjax.tipoProgettoRicerca = $('#tipoProgettoRicercaCrono').val();
        oggettoPerChiamataAjax.annoRicerca = $('#annoRicercaCrono').val();
      	oggettoPerChiamataAjax.prova = $('#tipoProgettoRicercaCrono').val();
        oggettoPerChiamataAjax.progetto = {};
        //task-170
        oggettoPerChiamataAjax.tipoProgettoRicerca = {};
        oggettoPerChiamataAjax.tipoProgettoStr = $('#tipoProgettoRicercaCrono').val();
        oggettoPerChiamataAjax.progetto.codice = $('#codiceProgettoRicercaCrono').val();
        
        
        return $.postJSON(baseUrl + 'CaricaCronoprogrammiDaCopiare.do', qualify(oggettoPerChiamataAjax))
        .then(function(data) {
        	if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
        	popolaSelect(data.listaCronoprogrammiDaCopiare,tipoProgetto);
        	$('#divSelezionaCronoprogramma').slideDown();
        }).always(button.overlay.bind(button, 'hide'));
        
    }

    /**
     * Consulta il totale dei dettagli di entrata.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function consultaTotaliEntrata(e) {
        e.preventDefault();
        consultaTotali(baseUrl + 'ConsultaTotaliEntrata.do', 'Totali Entrata', $('#SPINNER_consultaTotaliEntrata'));
    }

    /**
     * Consulta il totale dei dettagli di uscita.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function consultaTotaliUscita(e) {
        e.preventDefault();
        consultaTotali(baseUrl + 'ConsultaTotaliUscita.do', 'Totali Spesa', $('#SPINNER_consultaTotaliUscita'));
    }

    /**
     * Consulta i totali dei dettagli.
     *
     * @param url     (String) l'URL da invocare
     * @param header  (String) il testo da apporre nel titolo
     * @param spinner (jQuery) lo spinner da azionare
     */
    function consultaTotali(url, header, spinner) {
        spinner.addClass('activated');
        $.postJSON(url, {cronoprogrammaDaDefinire: isDaDefinire()})
        .then(function(data) {
            var anno;
            var tableHead = $('#tabellaTotali').find('thead').empty();
            var row;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            
            $('#titoloTotali').html(header);
            for(anno in data.mappaTotali) {
                if(data.mappaTotali.hasOwnProperty(anno)) {
                    row = '<tr class="borderBottomLight">' +
                            '<th scope="col">Totale</th>' +
                            '<th scope="col" class="tab_Right">' + (+anno === -1 ? 'Senza anno' : anno) + '</th>' +
                            '<th scope="col">&nbsp;</th>' +
                            '<th scope="col">&nbsp;</th>' +
                            '<th class="tab_Right text-right" scope="col">' + data.mappaTotali[anno].formatMoney() + '</th>' +
                            '<th scope="col">&nbsp;</th>' +
                        '</tr>';
                    tableHead.append(row);
                }
            }
            $('#modaleConsultaTotali').modal('show');
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }

    /**
     * Carica le tabelle dei dettagli.
     *
     * @param data (Object) i dati ottenuti tramite cui caricare le tabelle
     */
    function caricaTabelleDettagli(data) {
    	if(!data){
    		return;
    	}
        caricaTabellaDettagli(data.listaDettaglioEntrataCronoprogramma, 'tabellaEntrata', optionsEntrata);
        caricaTabellaDettagli(data.listaDettaglioUscitaCronoprogramma, 'tabellaUscita', optionsUscita);
    }

    /**
     * Carica la lista dei Programmi.
     *
     * @param e    (Event) l'evento scatenante l'invocazione
     * @param args (Array) gli argomenti ulteriori
     *
     * @return (Deferred) l'oggetto rappresentante l'invocazione AJAX
     */
    function caricaProgramma(e, args) {
        var uidProgramma = args && args[0];
        var mayRemoveDisabled = args && args[1];
        return caricaListaDaAjax($('#missioneUscita').val(), 'programmaUscita', 'ajax/programmaAjax.do', 'listaProgramma', 'Seleziona il programma', uidProgramma, mayRemoveDisabled);
    }

    /**
     * Carica la lista delle Tipologie Titolo.
     *
     * @param e    (Event) l'evento scatenante l'invocazione
     * @param args (Array) gli argomenti ulteriori
     *
     * @return (Deferred) l'oggetto rappresentante l'invocazione AJAX
     */
    function caricaTipologiaTitolo(e, args) {
        var uidTipologia = args && args[0];
        var mayRemoveDisabled = args && args[1];
        return caricaListaDaAjax($('#titoloEntrataEntrata').val(), 'tipologiaTitoloEntrata', 'ajax/tipologiaTitoloAjax.do', 'listaTipologiaTitolo', 'Seleziona la tipologia', uidTipologia, mayRemoveDisabled);
    }

    /**
     * Carica i classificatori per il capitolo di uscita.
     *
     * @param capitolo (Object) il capitolo da cui ottenere i classificatori
     */
    function caricaClassificatoriUscita(capitolo) {
        var uidMissione = capitolo.missione.uid;
        var uidProgramma = capitolo.programma.uid;
        var uidTitoloSpesa = capitolo.titoloSpesa.uid;

        $('#missioneUscita').val(uidMissione).trigger('change', uidProgramma);
        $('#titoloSpesaUscita').val(uidTitoloSpesa);
    }

    /**
     * Carica i classificatori per il capitolo di entrata.
     *
     * @param capitolo (Object) il capitolo da cui ottenere i classificatori
     */
    function caricaClassificatoriEntrata (capitolo) {
        var uidTitoloEntrata = capitolo && capitolo.titoloEntrata && capitolo.titoloEntrata.uid || 0;
        var uidTipologiaTitolo = capitolo && capitolo.tipologiaTitolo && capitolo.tipologiaTitolo.uid || 0;

        $('#titoloEntrataEntrata').val(uidTitoloEntrata).trigger('change', uidTipologiaTitolo);
    }

    /**
     * Controlla l'anno di entrata per il capitolo di uscita.
     *
     * @param listaErrori (Array)   la lista degli errori da popolare
     */
    function controllaAnnoEntrataPerUscita(listaErrori) {
        var annoUscita = $('#annoUscita').val();
        var annoEntrata = $('#annoUscitaEntrata').val();
        
        if(isDaDefinire() && annoUscita && annoEntrata && annoUscita < annoEntrata) {
            listaErrori.push('BIL_ERR_101 - L\'anno di competenza della spesa non puo\' essere antecedente l\'anno di competenza del finanziamento');
        }
    }

    /**
     * Controlla i campi e appone un messaggio di errore nel caso in cui i valori non siano impostati correttamente.
     *
     * @param campiDaControllare (Object) i campi da controllare
     * @param listaErrori        (Array)  la lista degli errori da popolare
     *
     * @return (Array) la lista degli errori popolata
     */
    function controllaCampi(campiDaControllare, listaErrori) {
        campiDaControllare.forEach(function(el) {
            if((!el.excludeCheckFnc || !el.excludeCheckFnc()) && !$(el.sel).val()) {
                listaErrori.push('COR_ERR_0002 - Dato obbligatorio omesso: ' + el.msg);
            }
        });
        return listaErrori;
    }

    /**
     * Carica la tabella dei dettagli del Cronoprogramma.
     *
     * @param lista               (Array)  la lista da caricare nella tabella
     * @param idTabella           (String) l'id della tabella
     * @param optionsDaUtilizzare (Object) le opzioni per il dataTable
     */
    function caricaTabellaDettagli (lista, idTabella, optionsDaUtilizzare) {
        var tabella = $('#' + idTabella);
        // Ottengo la pagina selezionata
        var selectedPageString = tabella.next()
            .find('.pagination')
                .find('.active')
                    .find('a')
                        .html();
        var selectedPage = parseInt(selectedPageString, 10);
        var opts = $.extend(true, {}, optionsComuni, optionsDaUtilizzare, {aaData: lista});
        if(!isNaN(selectedPage)) {
            $.extend(true, opts, {iDisplayStart: ((selectedPage - 1) * itemsPerPage)});
        }

        tabella.dataTable(opts);
        $('#' + idTabella + '_wrapper').find('div:first').hide();
    }

    /**
     * Elimina la riga del dettaglio.
     *
     * @param tipoDettaglio (String) il tipo del dettaglio
     * @param row           (Number) la riga
     */
    function eliminaRiga(tipoDettaglio, row) {
        var url = baseUrl + 'CancellaDettaglioCronoprogramma' + tipoDettaglio + '.do';
        var oggettoPerChiamataAjax = {};
        var spinner = $('#SPINNER_eliminaDettaglio');
        oggettoPerChiamataAjax.indiceDettaglioNellaLista = row;

        spinner.addClass('activated');

        $.postJSON(url, oggettoPerChiamataAjax)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaTabellaDettagli(data.listaDettaglioEntrataCronoprogramma, 'tabellaEntrata', optionsEntrata);
            caricaTabellaDettagli(data.listaDettaglioUscitaCronoprogramma, 'tabellaUscita', optionsUscita);

            // Chiudo il modale
            $('#modaleElimina').modal('hide');
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }

    /**
     * Carica il form di aggiornamento.
     *
     * @param td   (Node)   la cella della tabella
     * @param obj  (Object) l'oggetto da inviare nella chiamata AJAX
     * @param tipo (String) il tipo di aggiornamento (Entrata|Spesa)
     */
    function caricaFormAggiornamento(td, obj, tipo) {
        var tr = $(td).closest('tr').overlay('show');
        return apriCollapseDettaglio(baseUrl + '_apriAggiornaDettaglio' + tipo + '.do', obj, '#collapseDettaglio' + tipo)
        .then(function() {
            var suffix = tipo === 'Entrata' ? ('_ce' + suffissoTipoCapitolo) : ('_cu' + suffissoTipoCapitolo);
            Capitolo.inizializza(tipo + stringaTipoCapitolo, '#HIDDEN_annoEsercizio', '#numeroCapitolo' + tipo, '', '', '#datiRiferimentoCapitolo' + tipo + 'Span', '#daEsistente' + tipo + 'Si', suffix);
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Aggiorna il dettaglio di entrata.
     *
     * @param td  (Node)   la cella della tabella
     * @param obj (Object) l'oggetto contenuto nella tabella
     * @param row (Number) la riga
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     */
    function aggiornaEntrata(td, obj, row, e) {
        var oggettoDaInviare = {indiceDettaglioNellaLista: row};

        e.preventDefault();
        caricaFormAggiornamento(td, oggettoDaInviare, 'Entrata');
    }

    /**
     * Aggiorna il dettaglio di uscita.
     *
     * @param td  (Node)   la cella della tabella
     * @param obj (Object) l'oggetto contenuto nella tabella
     * @param row (Number) la riga
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     */
    function aggiornaUscita(td, obj, row, e) {
        var oggettoDaInviare = {indiceDettaglioNellaLista: row};

        e.preventDefault();
        caricaFormAggiornamento(td, oggettoDaInviare, 'Uscita')
        .then(gestioneQuadroEconomico);
    }

    /**
     * Carica una lista a partire da una chiamata AJAX e popola la relativa select.
     *
     * @param uidPadre       (String)  l'uid del campo padre
     * @param idSelect       (String)  l'id della select da popolare
     * @param url            (String)  l'URL da invocare
     * @param campoAjax      (String)  il nome del campo nella response AJAX
     * @param htmlSelect     (String)  l'HTML da apporre come option vuota della select
     * @param uidFiglio      (Number)  l'uid del figlio (Optional - default: undefined)
     * @param removeDisabled (Boolean) se sia possibile toglire il disabled (Optional - default: true)
     *
     * @return (Deferred) l'oggetto rappresentante l'invocazione AJAX
     */
    function caricaListaDaAjax(uidPadre, idSelect, url, campoAjax, htmlSelect, uidFiglio, removeDisabled) {
        var selectFiglio = $('#' + idSelect);
        var hiddenFiglio = $('#HIDDEN_' + idSelect);
        var mayRemoveDisabled = removeDisabled !== false;
        if(!uidPadre) {
            // Svuoto la lista
            selectFiglio.val('')
                .empty()
                // Appendo la option vuota
                .append('<option value="0">' + htmlSelect + '</option>')
                .attr('disabled', 'disabled');
            return;
        }
        selectFiglio.overlay('show');

        return $.postJSON(url, {id: uidPadre})
        .then(function(data) {
            var str;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            str = data[campoAjax].reduce(function(acc, el) {
                return acc + '<option value="' + el.uid + '">' + el.codice + '-' + el.descrizione + '</option>';
            }, '<option value="0">' + htmlSelect + '</option>');

            selectFiglio.html(str);
            if(mayRemoveDisabled) {
                selectFiglio.removeAttr('disabled');
            }
            if(uidFiglio) {
                selectFiglio.val(uidFiglio);
                hiddenFiglio.val(uidFiglio);
            }
        }).always(selectFiglio.overlay.bind(selectFiglio, 'hide'));
    }

    /**
     * Genera la stringa relativa al capitolo ed alle informazioni.
     *
     * @param src (Object) la sorgente
     */
    function generaStringaCapitoloArticoloUEB(src) {
        var arr = [];
        if(!src) {
            return '';
        }
        
        pushIfCond(arr, src.numeroCapitolo, notEmpty(src.numeroCapitolo));
        pushIfCond(arr, src.numeroArticolo, notEmpty(src.numeroArticolo));
        pushIfCond(arr, src.numeroUEB, notEmpty(src.numeroUEB) && exports.gestioneUEB);
        return arr.join(' / ');
    }
    
    /**
     * Genera la stringa relativa al capitolo ed alle informazioni.
     *
     * @param src (Object) la sorgente
     */
    function generaStringaCapitoloArticoloUEBAvanzoAmministrazione(src) {
        var arr = [];
        if(!src) {
            return '';
        }
        
        pushIfCond(arr, src.numeroCapitolo, notEmpty(src.numeroCapitolo));
        pushIfCond(arr, src.numeroArticolo, notEmpty(src.numeroArticolo));
        pushIfCond(arr, src.numeroUEB, notEmpty(src.numeroUEB) && exports.gestioneUEB);
        pushIfCond(arr, 'Avanzo di Amm.', (!src['capitoloEntrata' + stringaTipoCapitolo] || !src['capitoloEntrata' + stringaTipoCapitolo].uid) && src.isAvanzoAmministrazione);
        return arr.join(' / ');
    }
    
    /**
     * Controlla che il campo non sia vuoto
     * @param field (any) il campo da controllare
     * @returns (boolean) <code>true</code> se il campo non e' vuoto; <code>false</code> altrimenti
     */
    function notEmpty(field) {
        return field !== null && field !== undefined && field !== '';
    }
    
    function pushIfCond(arr, field, cond) {
        if(cond) {
            arr.push(field);
        }
    }

    /**
     * Pulizia del capitolo di entrata.
     */
    function cleanCapitoloEntrata(e) {
        var esistente = /Si$/.test(e.target.id);
        var fieldsId = ['titoloEntrataEntrata'];
        $('input[name="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione"]').prop('disabled', !!esistente)
            .filter('[value="true"]').prop('checked', false).end()
            .filter('[value="false"]').prop('checked', true).end();
        $('#HIDDEN_tipologiaTitoloEntrata')[!esistente ? 'attr' : 'removeAttr']('disabled', true);
        $('#titoloEntrataEntrata, #tipologiaTitoloEntrata').val('');
		//SIAC-8791
		$('#HIDDEN_titoloEntrataEntrata', '#HIDDEN_tipologiaTitoloEntrata').val('');
        cleanCapitolo('Entrata', esistente, fieldsId);
    }

    /**
     * Pulizia del capitolo di uscita.
     */
    function cleanCapitoloUscita(e) {
        var esistente = /Si$/.test(e.target.id);
        var fieldsId = ['missioneUscita', 'titoloSpesaUscita'];
        cleanCapitolo('Uscita', esistente, fieldsId);
        $('#HIDDEN_programmaUscita')[!esistente ? 'attr' : 'removeAttr']('disabled', true);
        $('#missioneUscita, #programmaUscita, #titoloSpesaUscita').val('');
		//SIAC-8791
		$('#HIDDEN_missioneUscita', '#HIDDEN_programmaUscita', '#HIDDEN_titoloSpesaUscita').val('');
    }

    /**
     * Pulizia del capitolo.
     * 
     * @param tipo      (String)  il tipo di capitolo
     * @param esistente (Boolean) se sia esistente o meno
     * @param fieldsId  (Array)   gli id dei field da trattare
     */
    function cleanCapitolo(tipo, esistente, fieldsId) {
        $('#numeroCapitolo' + tipo + ', #numeroArticolo' + tipo + ', #numeroUEB' + tipo).removeAttr('readonly').val('');
        //$('#uidCapitoloEntrata').val('');
        //SIAC-8791 
		$('#uidCapitolo' + tipo).val('');
		$('#datiRiferimentoCapitolo' + tipo + 'Span').html('');

        gestioneClassificazione(fieldsId, esistente);
    }

    /**
     * Caricamento del capitolo di entrata.
     *
     * @param e    (Event)  l'evento scatenante l'invocazione
     * @param data (Object) i dati dell'invocazione
     */
    function loadCapitoloEntrata(e, data) {
        loadCapitolo('Entrata', data.capitolo);
        $('#titoloEntrataEntrata, #HIDDEN_titoloEntrataEntrata').val(data.capitolo.titoloEntrata && data.capitolo.titoloEntrata.uid || '');
        caricaTipologiaTitolo(null, [data.capitolo.tipologiaTitolo && data.capitolo.tipologiaTitolo.uid, false]);
    }

    /**
     * Caricamento del capitolo di uscita.
     *
     * @param e    (Event)  l'evento scatenante l'invocazione
     * @param data (Object) i dati dell'invocazione
     */
    function loadCapitoloUscita(e, data) {
        loadCapitolo('Uscita', data.capitolo);
        $('#missioneUscita, #HIDDEN_missioneUscita').val(data.capitolo.missione && data.capitolo.missione.uid || '');
        $('#titoloSpesaUscita, #HIDDEN_titoloSpesaUscita').val(data.capitolo.titoloSpesa && data.capitolo.titoloSpesa.uid || '');
        caricaProgramma(null, [data.capitolo.programma && data.capitolo.programma.uid, false]);
    }

    /**
     * Caricamento del capitolo.
     *
     * @param tipo (String) il tipo del capitolo
     * @param cap  (Object) il capitolo
     */
    function loadCapitolo(tipo, cap) {
        $('#numeroArticolo' + tipo).val(cap.numeroArticolo);
        $('#numeroUEB' + tipo).val(cap.numeroUEB);
    }

    /**
     * Caricamento della SAC.
     *
     * @param suffix (String) il suffisso dei campi da attivare
     * @returns (Promise) la promise del caricamento delle SAC
     */
    function gestisciStrutturaAmministrativoContabile(e) {
       
        var listaStrutturaAmministrativoContabile = (e && e.lista) || [];

        ['_ce' + suffissoTipoCapitolo, '_cu' + suffissoTipoCapitolo].forEach(function(value) {
        	trees[value] =  new Ztree('_modale' + value);
        	trees[value].inizializza(listaStrutturaAmministrativoContabile);
        });
    }

    /**
     * Pulizia del modale di entrata
     */
    function cleanModaleEntrata() {
        cleanModaleCapitolo('_ce' + suffissoTipoCapitolo);
        gestioneClassificazione(['titoloEntrataEntrata', 'tipologiaTitoloEntrata'], true);
    }

    /**
     * Pulizia del modale di uscita
     */
    function cleanModaleUscita() {
        cleanModaleCapitolo('_cu' + suffissoTipoCapitolo);
        gestioneClassificazione(['missioneUscita', 'programmaUscita', 'titoloSpesaUscita'], true);
    }

    /**
     * Pulizia del modale del capitolo.
     *
     * @param suffix (String) il suffisso del capitolo
     */
    function cleanModaleCapitolo(suffix) {
        // TODO: chiudere il collapse
//    	trees[suffix].deselezionaNodiEAbilitaAlberatura(trees[suffix]);
    }
    
    /**
     * Impostazione dell'uid del capitolo di entrata
     * @param e (Event) l'evento scatenante
     * @param capitolo (Object) il capitolo
     */
    function impostaUidCapitoloEntrata(e, capitolo) {
        $('#uidCapitoloEntrata').val(capitolo && capitolo.capitolo && capitolo.capitolo.uid || '');
    }

    /**
     * Impostazione dell'uid del capitolo di uscita
     * @param e (Event) l'evento scatenante
     * @param capitolo (Object) il capitolo
     */
    function impostaUidCapitoloUscita(e, capitolo) {
        $('#uidCapitoloUscita').val(capitolo && capitolo.capitolo && capitolo.capitolo.uid || '');
    }

    //SIAC-6255
    function calcolaDataFineLavori(){
    	var dataInizioLavori = $('#dataInizioLavori').val();
    	var durataInGiorni =  $('#giorniDurata').val();
    	var dataFineLavori =  $('#dataFineLavori');
    	var obj = {};
    	if(!dataInizioLavori || !durataInGiorni){
    		dataFineLavori.val("");
    		dataFineLavori.overlay("hide");
    		return;
    	}
    	dataFineLavori.overlay('show');
    	obj.durataInGiorni  = durataInGiorni;
    	obj.dataInizioLavori = dataInizioLavori;
    	return $.postJSON(baseUrl + '_calcolaDataFineLavori.do', qualify(obj))
    		.then(function(data){
    			if(impostaDatiNegliAlert(data.errori, alertErrori)){
    				return;
    			}
    			dataFineLavori.val(data.dataFineLavoriString);
    		})
    		.always(dataFineLavori.overlay.bind(dataFineLavori,"hide"));
    }
    
    /**
     * Funzione invocata al caricamento della pagina
     */
    function onload() {
        var $document = $(document);
        ['Entrata', 'Uscita'].forEach(function(val) {
            $('#pulsanteApriInserisciDettaglio' + val).on('click', exports['apriInserisciDettaglio' + val]);
            $('#pulsanteApriConsultaTotali' + val).on('click', exports['consultaTotali' + val]);
        });

        $document.on('change', '#missioneUscita', caricaProgramma);
        $document.on('change', '#titoloEntrataEntrata', caricaTipologiaTitolo);
        $document.on('click', ' [data-save-button]', saveCollapse);
        $document.on('click', '#daEsistenteEntrataSi, #daEsistenteEntrataNo', cleanCapitoloEntrata);
        $document.on('click', '#daEsistenteUscitaSi, #daEsistenteUscitaNo', cleanCapitoloUscita);
        $document.on('click', '[data-close-collapse]', closeCollapse);

        $document.on('loadedCapitolo_ce' + suffissoTipoCapitolo, loadCapitoloEntrata);
        $document.on('loadedCapitolo_cu' + suffissoTipoCapitolo, loadCapitoloUscita);

        $document.on('openedModalCapitolo_ce' + suffissoTipoCapitolo, cleanModaleEntrata);
        $document.on('openedModalCapitolo_cu' + suffissoTipoCapitolo, cleanModaleUscita);

        $('#checkCopiaDaCronoprogramma').on('click', toggleDivSelezioneCronoprogramma);
        $('#pulsanteCopiaDaCronoprogramma').on('click', copiaDaCronoprogramma);

        $('#pulsanteApriVerificaQuadratura').on('click', verificaQuadratura);
//        caricaStrutturaAmministrativoContabile();
        $(document).on("struttureAmministrativeCaricate", gestisciStrutturaAmministrativoContabile);

        $document.on('loadedCapitolo_ce' + suffissoTipoCapitolo, impostaUidCapitoloEntrata);
        $document.on('loadedCapitolo_cu' + suffissoTipoCapitolo, impostaUidCapitoloUscita);
        
        $('#giorniDurata, #dataInizioLavori').substituteHandler('change', calcolaDataFineLavori);
        $('input[name="cronoprogramma.gestioneQuadroEconomico"]').substituteHandler('change', gestioneQuadroEconomico);

        exports.gestioneUEB = $('#HIDDEN_gestioneUEB').val() === 'true';
    }
}());