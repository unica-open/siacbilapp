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
    	Fcde.popolaDa(
            event, 
			'inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_simulaPopolaDaPrevisione.do', 
			'Conferma capitoli da precedente previsione',
			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
				'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp'
    	);

    	return false;
    }
    
    function popolaDaAnnoPrecedenteGestione(event) {
    	Fcde.popolaDa(
            event, 
            'inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_simulaPopolaDaGestione.do', 
            'Conferma capitoli da precedente assestamento',
            'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
                'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp'
        );

    	return false;
    }
    
    function popolaDaAnnoPrecedenteRendiconto(event) {
    	Fcde.popolaDa(
            event, 
            'inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_simulaPopolaDaRendiconto.do', 
            'Conferma capitoli da precedente rendiconto',
            'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' + 
                'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp'
        );
    	
        return false;
    }
    
    function popolaDaAnnoPrecedenteAnagraficaCapitoli(event) {
    	Fcde.popolaDa(
            event, 
            'inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_simulaPopolaDaAnagraficaCapitoli.do', 
            'Conferma capitoli da anagrafica',
            'Confermando l\'operazione verranno copiati tutti i capitoli contrassegnati per calcolo FCDE in anagrafica ' + 
                'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp'
    	);
    	
        return false;
    }
    
    function popolaDaElaborazionePrecedente(event) {
        var uid = $('#listaElaborazionePrecedente').val();
        if(!uid) {
            return false;
        }
        
    	Fcde.popolaDa(
            event,
            'inserisciConfigurazioneStampaDubbiaEsigibilitaGestione_simulaPopolaDaElaborazione.do',
            'Conferma capitoli da elaborazione precedente',
            'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'elaborazione selezionata ' +
                'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp',
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
    
    /**
     * Salva i capitoli modificati
     */
    function salvaCapitoli() {
    	$(this).prop('disabled', true);
    	
    	var form = $('#form-salvaCapitoli');
    	// var toInputHidden = function(i, n, v) { 
    	// 	return '<input type="hidden" name="listaAccantonamentoFondiDubbiaEsigibilitaGestioneTemp['+i+'].'+n+'" value="'+v+'" />';
    	// }

        var salvataggio = '';
    	
    	$('.capitolo.modificato').each(function(){
            salvataggio += $(this).data('uid') + ';';
    		// form.append(toInputHidden(i, 'uid', $(this).data('uid')));
            salvataggio += Fcde.creaStringaPerSalvataggio(this, [
                ['numeratore', '.numeratore'],
                ['denominatore', '.denominatore'],
                ['mediaUtente', '.media-utente'],
                //SIAC-8513 devo portare a salvataggio anche la media semplice dei totali
                ['mediaSempliceTotali', '.perc-effettiva'],
                ['accantonamento', '.accantonamento.accFcde'],
                ['accantonamento1', '.accantonamento.accFcde1'],
                ['accantonamento2', '.accantonamento.accFcde2']
            ]);
       	});
        
        form.append('<input type="hidden" name="salvataggio" value="'+ salvataggio +'" />');

        form.submit();
    }

    function projectToInput(obj, idX){
    	return "<input type='hidden' name='listaCapitoloEntrataGestione[" + idX + "].uid' value='"+ obj.uid + "'/>";
    }

    function appendOptionToForm($form, name, value){
        if(!$form) return; 
        $form.append('<input type="text" class="hide" name="'+ name +'" value="' + value + '" />');
    }

    function dueDefinitivePresenti(versioniDefinitive){
        var deferred = $.Deferred();
        if(versioniDefinitive.length >= 2){
            bootboxAlert(
                "Esistono gi&agrave; " + versioniDefinitive.length + " calcoli FCDE in assestamento definitivi. Operazione non consentita",
                "Passaggio a stato definitivo dell'elaborazione",
                undefined,
                {
                    "label": "Ok",
                    "class": "btn-primary",
                    "callback": function() { deferred.reject(); }
                }
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }

    function nessunaVersioneDefinitiva(versioniDefinitive, passaggioInBozza){
        var deferred = $.Deferred();
        if(versioniDefinitive.length === 0){
            if(passaggioInBozza){                
                FcdeUtils.confirm(
                    'Ritorno in bozza dell\'elaborazione',
                    'Confermando l\'operazione la versione torner&agrave in stato Bozza; si vuole proseguire?',
                    function() { deferred.resolve(); },
                    'S&igrave;',
                    function() { deferred.reject(); },
                    'No'
                );
                return deferred.promise();
            }
            FcdeUtils.confirm(
                'Passaggio a stato definitivo dell\'elaborazione',
                'Confermando l\'operazione la versione diventer&agrave definitiva e non potr&agrave più essere modificata, eventuali altre versioni definitive saranno portate in stato di bozza; si vuole proseguire?',
                function() { deferred.resolve(); },
                'S&igrave;',
                function() { deferred.reject(); },
                'No'
            );
            return deferred.promise();
        }
        deferred.resolve();
        return deferred.promise();
    }

    // DEFINTIVA
    function controlloSeVersioneSuccessiva(versioniDefinitive, numeroVersioneInPassaggio){
        var deferred = $.Deferred();
        if(versioniDefinitive.length !== 1){
            deferred.resolve();
            return deferred.promise();
        }
        // numero versione pattern: 'Vers. #<numero> del <data> in stato <stato>' 
        // numeroVersioneDef = [uidVersione, numeroVersione]
        var tupleVersioneDefinitiva = [versioniDefinitive[0].val(), versioniDefinitive[0].html().slice(versioniDefinitive[0].html().indexOf('#'), versioniDefinitive[0].html().indexOf('del')).trim()];
        // Se la versione in passaggio e' precedente all'altra versione definitiva non permetto il proseguimento
        if(parseInt(numeroVersioneInPassaggio, 10) < parseInt(tupleVersioneDefinitiva[1].slice(1), 10)){
            FcdeUtils.presaVisione(
                'Passaggio a stato definitivo dell\'elaborazione',
                'E gi&agrave; presente la versione ' + tupleVersioneDefinitiva[1] + ' in stato Definitivo con numerazione successiva. Passaggio in stato Definitivo non possibile.',
                function() { deferred.reject(); }
            );
            return deferred.promise();
        }
        
        // Se la versione in passaggio e'successiva all'altra versione definitiva chiedo all'utente il comportamento
        if(parseInt(numeroVersioneInPassaggio, 10) > parseInt(tupleVersioneDefinitiva[1].slice(1), 10)){
            FcdeUtils.confirmWithOption(
                'Passaggio a stato definitivo dell\'elaborazione',
                'E gi&agrave; presente la versione ' + tupleVersioneDefinitiva[1] + ' in stato Definitivo con numerazione precedente. Si vuole sovrascrivere (la versione precedente tornerà in stato Bozza) oppure si intende inserire una seconda versione in stato Definitivo?',
                function() { 
                    appendOptionToForm($('#formModificaStatoInDefinitivo_hidden'), 'sceltaUtente', 'SOVRASCRIVI'); 
                    deferred.resolve(); 
                },
                'Sovrascrivi',
                function() { 
                    appendOptionToForm($('#formModificaStatoInDefinitivo_hidden'), 'sceltaUtente', 'RENDI_DEFINITIVA'); 
                    // appendOptionToForm($('#formModificaStatoInDefinitivo_hidden'), 'uidVersioneDaNonSovrascrivere', tupleVersioneDefinitiva[0]); 
                    deferred.resolve(); 
                },
                'Rendi definitiva',
                function() { deferred.reject(); },
                'Torna indietro'
            );
            return deferred.promise();
        }

        deferred.resolve();
        return deferred.promise();
    }

    // BOZZA
    function controlloSeVersionePrecedente(versioniDefinitive, numeroVersioneInPassaggio){
        var deferred = $.Deferred();
        if(versioniDefinitive.length !== 1){
            deferred.resolve();
            return deferred.promise();
        }
        // numero versione pattern: 'Vers. #<numero> del <data> in stato <stato>' 
        // numeroVersioneDef = [uidVersione, numeroVersione]
        var tupleVersioneDefinitiva = [versioniDefinitive[0].val(), versioniDefinitive[0].html().slice(versioniDefinitive[0].html().indexOf('#'), versioniDefinitive[0].html().indexOf('del')).trim()];
        // Se la versione in passaggio in BOZZA ha un numero successivo alla versione definitiva non permetto il proseguimento
        if(parseInt(numeroVersioneInPassaggio, 10) < parseInt(tupleVersioneDefinitiva[1].slice(1), 10)){
            FcdeUtils.presaVisione(
                'Ritorno in bozza dell\'elaborazione',
                'Esiste un secondo Calcolo FCDE  in stato Definitivo che referenzia la Media di Confronto calcolata in questa versione. Non è possibile riportarla in stato Bozza.',
                function() { deferred.reject(); }
            );
            return deferred.promise();
        }
        
        // Se la versione in passaggio in BOZZA e' precedente alla versione definitiva chiedo all'utente il comportamento
        if(parseInt(numeroVersioneInPassaggio, 10) > parseInt(tupleVersioneDefinitiva[1].slice(1), 10)){
            FcdeUtils.confirm(
                'Ritorno dell\'elaborazione a stato bozza', 
                'Confermando l\'operazione la versione tornerà in stato bozza e potrà essere modificata; si vuole proseguire?',
                function() { $('#formModificaStatoInBozza_hidden').submit() },
                'S&igrave;',
                function() { deferred.reject(); },
                'No'
            );
            return deferred.promise();
        }

        deferred.resolve();
        return deferred.promise();
    }

    function trovaVersioniDefinitive(){
        var versioniDefinitive = [];

        $('#listaElaborazionePrecedente').children().each(function(){
            if($(this).html() && $(this).html().indexOf('DEFINITIVA') != -1) versioniDefinitive.push($(this));
        }); 

        return versioniDefinitive;
    }
    
    function modificaStatoInDefinitivo() {
        // impedisco il cambio stato durante la modifica di una versione in bozza
        if($('#pulsante-modifica-importi').is(':disabled')){
            return false;
        }

        $('#formModificaStatoInDefinitivo_hidden').empty();
        // // SIAC-8426
        // FcdeUtils.passaggioInDefinitivaOperations();
        controlloPassaggioDefinitivoVersione();
        return false;
    }

    function controlloPassaggioDefinitivoVersione(){
        var versioniDefinitive = trovaVersioniDefinitive();

        $.when(
            dueDefinitivePresenti(versioniDefinitive),
            nessunaVersioneDefinitiva(versioniDefinitive, false),
            controlloSeVersioneSuccessiva(versioniDefinitive, $('#n_versione').val())
        )
        // se ricevo solo conferme proseguo con il submit 
        .then(function() { $('#formModificaStatoInDefinitivo_hidden').submit(); });
    }

    function modificaStatoInBozza() {
    	$('#formModificaStatoInBozza_hidden')
    		.empty();
    	//SIAC-8421
        controlloPassaggioBozzaVersione();    	
        return false;
    }

    function controlloPassaggioBozzaVersione(){
        var versioniDefinitive = trovaVersioniDefinitive();

        $.when(
            nessunaVersioneDefinitiva(versioniDefinitive, true),
            controlloSeVersionePrecedente(versioniDefinitive, $('#n_versione').val())
        )
        // se ricevo solo conferme proseguo con il submit 
        .then(function() { $('#formModificaStatoInBozza_hidden').submit(); });
    }
    
    /**
     * Inizializzazione
     */
    function init() {
        $('#pulsanteRicercaCapitolo').substituteHandler('click', ricercaCapitoliModale);

        $('button[data-submit]').substituteHandler('click', submitForm);

        $('#pulsante-salva-importi').substituteHandler('click', salvaCapitoli);
        
        $('#pulsantePopolaDaAnnoPrecedentePrevisione').substituteHandler('click', popolaDaAnnoPrecedentePrevisione);
        $('#pulsantePopolaDaAnnoPrecedenteGestione').substituteHandler('click', popolaDaAnnoPrecedenteGestione);
        $('#pulsantePopolaDaAnnoPrecedenteRendiconto').substituteHandler('click', popolaDaAnnoPrecedenteRendiconto);
        $('#pulsantePopolaDaAnnoPrecedenteAnagraficaCapitoli').substituteHandler('click', popolaDaAnnoPrecedenteAnagraficaCapitoli);
        $('#pulsanteCaricaElaborazioneSelezionata').substituteHandler('click', popolaDaElaborazionePrecedente);

        $('#pulsanteModificaStatoInDefinitivo').substituteHandler('click', modificaStatoInDefinitivo);
        $('#pulsanteModificaStatoInBozza').substituteHandler('click', modificaStatoInBozza);

        $('#pulsanteConfermaCapitoliPopolaDa').substituteHandler('click', confermaCapitoliPopolaDa);
    }

    $(init);

})(jQuery, this);