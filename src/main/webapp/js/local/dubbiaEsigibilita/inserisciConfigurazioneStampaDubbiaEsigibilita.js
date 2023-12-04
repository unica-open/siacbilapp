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
    var erroriModaleCapitolo = $('#ERRORI_MODALE_CAPITOLO');

    // FUNZIONI UTILITA

    // MODALE CAPITOLI

    /**
     * Ricerca il capitolo Entrata Previsione
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

        return $.postJSON('selezionaCapitolo_capitoloEntrataPrevisioneFondiDubbiaEsigibilita.do', arrayToSend)
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
            sAjaxSource : 'risultatiRicercaCapitoloEntrataPrevisioneModaleAjax.do',
            bAutoWidth:false,
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#divRisultatiRicercaCapitolo").addClass("hide");
                $("#risultatiRicercaCapitoloEntrataPrevisione_processing").parent("div")
                    .show();
            },
            fnDrawCallback: function () {
                // Mostro il div del processing
                $("#risultatiRicercaCapitoloEntrataPrevisione_processing").parent("div")
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
    	return "<input type='hidden' name='listaCapitoloEntrataPrevisione[" + idX + "].uid' value='"+ obj.uid + "'/>";
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
			'inserisciConfigurazioneStampaDubbiaEsigibilita_simulaPopolaDaPrevisione.do', 
			'Conferma capitoli da precedente previsione',
			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
				'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
            'listaAccantonamentoFondiDubbiaEsigibilitaTemp'
    	);

    	return false;
    }
    
    function popolaDaAnnoPrecedenteGestione(event) {
    	Fcde.popolaDa(event, 
    			'inserisciConfigurazioneStampaDubbiaEsigibilita_simulaPopolaDaGestione.do', 
    			'Conferma capitoli da precedente assestamento',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' +
					'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaTemp' 
        	);

    	return false;
    }
    
    function popolaDaAnnoPrecedenteRendiconto(event) {
    	Fcde.popolaDa(event, 
    			'inserisciConfigurazioneStampaDubbiaEsigibilita_simulaPopolaDaRendiconto.do', 
    			'Conferma capitoli da precedente rendiconto',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'ultima fase di bilancio e ' + 
    				'partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaTemp' 
        	);
    	
        return false;
    }
    
    function popolaDaAnnoPrecedenteAnagraficaCapitoli(event) {
    	Fcde.popolaDa(event, 
    			'inserisciConfigurazioneStampaDubbiaEsigibilita_simulaPopolaDaAnagraficaCapitoli.do', 
    			'Conferma capitoli da anagrafica',
    			'Confermando l\'operazione verranno copiati tutti i capitoli contrassegnati per calcolo FCDE in anagrafica ' + 
    				'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaTemp' 
    	);
    	
        return false;
    }
    
    function popolaDaElaborazionePrecedente(event) {
        var uid = $('#listaElaborazionePrecedente').val();
        if(!uid) {
            return false;
        }
        
    	Fcde.popolaDa(event,
    			'inserisciConfigurazioneStampaDubbiaEsigibilita_simulaPopolaDaElaborazione.do',
    			'Conferma capitoli da elaborazione precedente',
    			'Confermando l\'operazione verranno copiati tutti i capitoli presenti nell\'elaborazione selezionata ' +
    				'e partirà l\'elaborazione del calcolo FCDE che potrebbe durare alcuni minuti; si vuole proseguire?',
                'listaAccantonamentoFondiDubbiaEsigibilitaTemp',
    			{'accantonamentoFondiDubbiaEsigibilitaAttributiBilancioCopiaDa.uid': uid}
    	);
    	
        return false;
    }

    function confermaCapitoliPopolaDa() {
        var $modal = $('#modale-capitolo-popola-da');
        $modal.modal('hide');
        var inputs = $modal.find('input[name="risultatiRicercaCapitoloPopolaDaUid"]:checked').map(function(idx, el) {
            return '<input type="hidden" name="listaCapitoloEntrataPrevisione[' + idx + '].uid" value="' + el.value + '"/>';
        }).get().join('');
        $('#formCapitoliPopolaDa_hidden').append(inputs).submit();
    }
    
    /**
     * Salva i capitoli modificati
     */
    function salvaCapitoli() {
    	$(this).prop('disabled', true);
    	
    	var form = $('#form-salvaCapitoli');
    	
        var salvataggio = '';

    	$('.capitolo.modificato').each(function(){
            salvataggio += $(this).data('uid') + ';';
            salvataggio += Fcde.creaStringaPerSalvataggio(this, Fcde.fieldsAsTuple);
       	});
    	
        form.append('<input type="hidden" name="salvataggio" value="'+ salvataggio +'" />');

    	form.submit();
    }

    function modificaStatoInBozza() {
    	$('#formModificaStatoInBozza_hidden')
    		.empty();
    	
        FcdeUtils.confirm('Ritorno dell\'elaborazione a stato bozza', 'Confermando l\'operazione la versione tornerà in stato bozza e potrà essere modificata; si vuole proseguire?', function() { $('#formModificaStatoInBozza_hidden').submit() });
    	
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

    function passaggioInDefinitivaOperations() {
        $.getJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_contaAccantonamentiDefinitivi.do')
        .then(function(data) {
            var errori = data.errori;
            var msg = '';

            if(impostaDatiNegliAlert(errori, erroriModaleCapitolo, true, true)){
                deferred.reject();
                return;
            }

            msg = data.numeroAccantonamentiDefinitivi > 0
                ? 'Esiste già una versione definitiva. Se si conferma l\'operazione l\'altra versione sarà portata in bozza mentre la presente diventerà definitiva e non potrà più essere modificata. Si vuole proseguire?'
                : 'Se si conferma l\'operazione la versione diventerà definitiva e non potrà più essere modificata. Si vuole proseguire?';

            return FcdeUtils.confirm('Passaggio a stato definitivo dell\'elaborazione',
                msg,
                function() {
                    $('#formModificaStatoInDefinitivo_hidden').submit()
                });
        });
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

        $('#pulsanteConfermaCapitoliPopolaDa').substituteHandler('click', confermaCapitoliPopolaDa);
        FcdeUtils.passaggioInDefinitivaOperations = passaggioInDefinitivaOperations;

        $('#pulsanteModificaStatoInDefinitivo').substituteHandler('click', modificaStatoInDefinitivo);
        $('#pulsanteModificaStatoInBozza').substituteHandler('click', modificaStatoInBozza);
    }

    $(init);
    
})(jQuery, this);