/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Implementazione della funzionalita' di ricerca del provvedimento da collapse utilizzando il prototype.
 * Questo permette, come nelle variazioni, la gestione di due diverse compilazioni guidate del provvedimento.
 * DA UTILIZZARE, A TENDERE, AL POSTO DEL VECCHIO ricerca_collapse.js
 * **/
;(function (w, $) {
	'use strict';
	var alertErrori = $('#ERRORI');
	var alertMessaggi = $('#MESSAGGI');
    var alertInformazioni = $('#INFORMAZIONI');
    var Provvedimento = function(suffix, infoNoProvvedimento, prefissoInfoProvvedimento, infoVariazioneProvvedimento, caricamentoSACEsterno){
    	this.suffix = suffix || '';
    	this.informazioniNoProvvedimento = infoNoProvvedimento || 'provvedimento';
    	this.prefissoInformazioniProvvedimento = prefissoInfoProvvedimento || 'provvedimento: ';
    	this.informazioniVariazioneProvvedimento = infoVariazioneProvvedimento || this.informazioniNoProvvedimento;
    	//lo metto qui, vuoto, solo per segnalare che verra' poi impostato successivamente e lo posso utilizzare
    	this.ztreeSAC = {};
    	this.caricamentoSACEsterno = caricamentoSACEsterno;
    };
    
    Provvedimento.prototype.constructor = Provvedimento;
    Provvedimento.prototype.inizializza = inizializza;
    Provvedimento.prototype.destroy = destroy;
    Provvedimento.prototype.svuotaFormRicerca = svuotaFormRicerca;
    Provvedimento.prototype.pulisciCampiProvvedimentoSelezionato = pulisciCampiProvvedimentoSelezionato;
    Provvedimento.prototype.impostaDataTableProvvedimentoSenzaOperazioni = impostaDataTableProvvedimentoSenzaOperazioni;
    Provvedimento.prototype.cercaProvvedimento = cercaProvvedimento;
    Provvedimento.prototype.cercaProvvedimentoCallback = cercaProvvedimentoCallback;
    Provvedimento.prototype.impostaUid = impostaUid;
    Provvedimento.prototype.deseleziona = deseleziona;
    Provvedimento.prototype.deselezionaSAC = deselezionaSAC;
    Provvedimento.prototype.pulisciENascondiTabellaProvvedimento = pulisciENascondiTabellaProvvedimento;
    Provvedimento.prototype.caricaStrutture = caricaStrutture;
    Provvedimento.prototype.caricaStruttureDaEvento = caricaStruttureDaEvento;
    Provvedimento.prototype.caricaStruttureCallback = caricaStruttureCallback;
    Provvedimento.prototype.onFormReset = onFormReset;
    
    w.Provvedimento = Provvedimento;
    
    var optionsBase = {
        bPaginate: true,
        bLengthChange: false,
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
            sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun provvedimento corrisponde ai criteri di ricerca'
            }
        }
    };

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per il Provvedimento. Caso della ricerca senza operazioni.
     *
     * @param aaData {Array} i dati da impostare nel dataTable
     */
    function impostaDataTableProvvedimentoSenzaOperazioni(lista) {
        // Le opzioni base del dataTable
    	var suffisso = this.suffix;
    	var aaData = lista || [];
        var optionsNuove = {
            aaData: aaData,
            iDisplayLength: 3,
            bSort: false,
            aoColumnDefs: [
                {aTargets: [0], mData: function(source, type, val) {
                    return '<input type="radio" name="uidProvvedimento' + suffisso + 'Radio" value="' + source.uid + '" />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalProvvedimento', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('anno')},
                {aTargets: [2], mData: defaultPerDataTable('numero')},
                {aTargets: [3], mData: function(source){
                	return source.codiceTipo + ' - ' + source.tipo;
                }},
                {aTargets: [4], mData: defaultPerDataTable('oggetto')},
                {aTargets: [5], mData: defaultPerDataTable('strutturaAmministrativoContabile')},
                {aTargets: [6], mData: defaultPerDataTable('stato')}
            ]
        };
        var options = $.extend(true, {}, optionsBase, optionsNuove);
        var tabellaProvvedimentoGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($('#risultatiRicercaProvvedimento' +  this.suffix));
        // Pulisco il dataTable prima di inizializzarne uno nuovo
        if(tabellaProvvedimentoGiaInDataTable.length > 0) {
            // Distruggo la dataTable
            tabellaProvvedimentoGiaInDataTable.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        $('#risultatiRicercaProvvedimento' + this.suffix).dataTable(options);
    }

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     *
     * @param doNotPopulateErrori (Boolean) se gli errori siano da popolare o meno (Optional, default: false)
     */
    function cercaProvvedimento(e) {
        // Valori per il check javascript
        var annoProvvedimento = $('#annoRicercaProvvedimento' + this.suffix).val();
        var numeroProvvedimento = $('#numeroRicercaProvvedimento' + this.suffix).val();
        var tipoAtto = $('#tipoAttoRicercaProvvedimento' + this.suffix).val();
        var uidTipoProvvedimento = $('#tipoAttoRicercaProvvedimento' + this.suffix).val();
        var uidStrutturaAmministrativoContabile = $('#HIDDEN_StrutturaAmministrativoContabileUidRicercaProvvedimento' + this.suffix).val();
        var oggettoDaInviare = {};
        // Errori
        var erroriArray = [];
        // Spinner
        var spinner = $('#SPINNER_Provvedimento' + this.suffix);

        e && e.preventDefault();

        // Controllo che non vi siano errori
        if(annoProvvedimento === '') {
            erroriArray.push('Il campo Anno deve essere selezionato');
        }
        if(numeroProvvedimento === '' && uidTipoProvvedimento === '') {
            erroriArray.push('Almeno uno tra i campi Numero e Tipo deve essere compilato');
        }

        // se arrivo dal ricaricamento della pagina, e' accettabile che i campi anno e numero non siano compilati
        if((e && impostaDatiNegliAlert(erroriArray, alertErrori))){
            this.pulisciENascondiTabellaProvvedimento();            
            return;
        }
        
        spinner.addClass('activated');
        
        oggettoDaInviare.attoAmministrativo = {};
        oggettoDaInviare.strutturaAmministrativoContabile = {};
        oggettoDaInviare.tipoAtto = {};
        
        oggettoDaInviare.attoAmministrativo.anno = annoProvvedimento;
        oggettoDaInviare.attoAmministrativo.numero = numeroProvvedimento;
        oggettoDaInviare.tipoAtto.uid = uidTipoProvvedimento;
//        oggettoDaInviare.tipoAtto.descrizione = tipoAtto;
        oggettoDaInviare.attoAmministrativo.oggetto= $('#oggettoRicercaProvvedimento' + this.suffix).val();
        oggettoDaInviare.strutturaAmministrativoContabile.uid = uidStrutturaAmministrativoContabile;
        
        // Chiamata AJAX
        $.postJSON('effettuaRicercaSenzaOperazioniProvvedimento.do', qualify(oggettoDaInviare))
        .then(this.cercaProvvedimentoCallback.bind(this))
        .always(spinner.removeClass.bind(spinner, 'activated'));
    }
    
    function cercaProvvedimentoCallback(data) {
        var listaProvvedimenti = data.listaElementoProvvedimento;
        // Tabella gia' in dataTable
        var tabellaProvvedimentoGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($('#risultatiRicercaProvvedimento' + this.suffix));

        alertErrori.slideUp();
        alertMessaggi.slideUp();
        alertInformazioni.slideUp();

        tabellaProvvedimentoGiaInDataTable.length > 0 && tabellaProvvedimentoGiaInDataTable.dataTable().fnDestroy();

        // Non vi sono errori, ne' messaggi, ne' informazioni. Proseguire con la creazione del dataTable
        this.impostaDataTableProvvedimentoSenzaOperazioni(listaProvvedimenti);

        $('#tabellaProvvedimento' + this.suffix).slideDown();
        // Imposto come selezionato un provvedimento, qualora si abbia il ripopolamento della pagina
        if($('input[name="uidProvvedimento' + this.suffix + 'Radio"]:checked').length > 0) {
            impostaUid.bind(this);
        }
    }

    /**
     * Imposta l'uid nel campo hidden.
     * @param e (Event) l'eventuale evento scatenante
     */
    function impostaUid(e) {
        var radioButton = $('input[name="uidProvvedimento' + this.suffix + 'Radio"]:checked');
        var valoreDelRadio = radioButton.val();
            // Il collapse, se presente
        var divCollapse = $('#collapseProvvedimento' + this.suffix);
        var spanInformazioni = $('#SPAN_InformazioniProvvedimento' + this.suffix);
            // Dati del radio
        var annoProvvedimento;
        var numeroProvvedimento;
        var codiceTipoAtto;
        var descrizioneTipoAtto;
        var statoProvvedimento;
        var uidStrutturaAmministrativoContabile;
        var obj;
        var event;

        if(e && typeof e.preventDefault === 'function') {
            e.preventDefault();
        }

        obj = radioButton.data('originalProvvedimento');
        // Controllo che non vi siano errori
        if(valoreDelRadio === undefined || !obj) {
            impostaDatiNegliAlert(['Necessario selezionare Provvedimento'], alertErrori, undefined, true);
            return;
        }
        alertErrori.slideUp();
        
        // Determino anno e numero del Provvedimento
        //perche' fare cosi?
        annoProvvedimento = obj.anno;
        numeroProvvedimento = obj.numero;
        statoProvvedimento = obj.stato;
        codiceTipoAtto = obj.codiceTipo;
        descrizioneTipoAtto = obj.tipo;
        uidStrutturaAmministrativoContabile = obj.uidStrutturaAmministrativoContabile;

        $('#HIDDEN_uidProvvedimento' + this.suffix).val(valoreDelRadio);
        $('#HIDDEN_uidProvvedimento' + this.suffix + 'Injettato').val(valoreDelRadio);
        $('#HIDDEN_numeroProvvedimento' + this.suffix).val(numeroProvvedimento);
        $('#HIDDEN_tipoAttoDescrizione' + this.suffix).val(descrizioneTipoAtto);
        $('#HIDDEN_tipoAttoCodice' + this.suffix).val(codiceTipoAtto);
        $('#HIDDEN_annoProvvedimento' + this.suffix).val(annoProvvedimento);
        $('#HIDDEN_statoProvvedimento' + this.suffix).val(statoProvvedimento);
        // Chiudo il collapse, qualora presente
        //TODO: valutare, forse sono sempre dalla ricerca
        if(divCollapse.length > 0 && divCollapse.hasClass('in')) {
            $('[data-toggle="collapse"][href="#collapseProvvedimento' + this.suffix + '"]').trigger('click');
        }
        // Imposto le informazioni del provvedimento, qualora possibile
        if(spanInformazioni.length > 0) {
            spanInformazioni.html(this.prefissoInformazioniProvvedimento + ' ' + annoProvvedimento + ' / ' + numeroProvvedimento + ' / ' + codiceTipoAtto + ' - ' + descrizioneTipoAtto);
        }
        event = $.Event('selectedProvvedimento');
        event.provvedimento = obj;
        $(document).trigger(event);
    }

    /**
     * Deseleziona il provvedimento.
     */
    function deseleziona() {
        this.svuotaFormRicerca();
        this.pulisciCampiProvvedimentoSelezionato();
        alertErrori.slideUp();
        $('#SPAN_InformazioniProvvedimento' + this.suffix).html(this.informazioniVariazioneProvvedimento);
        $('#HIDDEN_uidProvvedimento' + this.suffix).val('0');
        $('#HIDDEN_uidProvvedimento' + this.suffix + 'Injettato').val('0');
    }

    function pulisciCampiProvvedimentoSelezionato(){
    	//pulisco i campi del provvedimento che ho, eventualmente, selezionato precedentemente
    	
    	$('HIDDEN_tipoAttoUid'  + this.suffix).val('');
    	$('HIDDEN_tipoAttoCodice'  + this.suffix).val('');
    	$('HIDDEN_tipoAttoDescrizione'  + this.suffix).val('');
    	$('HIDDEN_numeroProvvedimento' + this.suffix).val('');
    	$('HIDDEN_annoProvvedimento'  + this.suffix).val('');
    	$('HIDDEN_statoProvvedimento'  + this.suffix).val('');
    	
        $('#SPAN_InformazioniProvvedimento' + this.suffix).html(this.informazioniVariazioneProvvedimento);
    }
    
    /**
     * Svuota il form di ricerca del Provvedimento.
     */
    function svuotaFormRicerca() {
        this.pulisciENascondiTabellaProvvedimento();
        //pulisco i campi utilizzati dalla ricerca
        $('#HIDDEN_StrutturaAmministrativoContabile' + this.suffix + 'Uid').val('');
        $('#HIDDEN_StrutturaAmministrativoContabile' + this.suffix + 'Codice').val('');
        $('#HIDDEN_StrutturaAmministrativoContabile' + this.suffix + 'Descrizione').val('');
        $('#SPAN_StrutturaAmministrativoContabile' + this.suffix).text('Nessuna Struttura Amministrativa Responsabile selezionata');
        $('#formRicercaProvvedimento' + this.suffix).find(':input').val('');
        if($('#collapseProvvedimento' + this.suffix).hasClass('in')){
            $('[data-toggle="collapse"][href="#collapseProvvedimento' + this.suffix + '"]').trigger('click');
        }
    }

    /**
     * Pulisce e nasconde la tabella del provvedimento
     */
    function pulisciENascondiTabellaProvvedimento(){
        var tabelleInDataTable =$.fn.dataTable.fnTables(true);
        var tabellaProvvedimento = $('#risultatiRicercaProvvedimento' + this.suffix);
        var tabelleDaIgnorare = $(tabelleInDataTable).not(tabellaProvvedimento);
        var arrayDaIgnorare = [];
        for(var i=0; i<tabelleDaIgnorare.size(); i++){
          arrayDaIgnorare.push(tabelleDaIgnorare.get(i));
        }
        cleanDataTables(arrayDaIgnorare);
        $('#tabellaProvvedimento' + this.suffix).slideUp();
    }
    
    function caricaStruttureDaEvento(e){
        var data = {};
        data.listaElementoCodifica = (e && e.lista);
        this.caricaStruttureCallback.call(this,data);
        
    }
    
    /**
     * Carico lo ztree delle strutture Amministrativo Contabili
     */
    function caricaStrutture(){
        // Pulsante per il modale della Struttura Amministrativo Contabile
        var spinner = $('#SPINNER_StrutturaAmministrativoContabileRicercaProvvedimento' + this.suffix);

        // Non permettere di accedere al modale finche' il caricamento non e' avvenuto
        $('#bottoneSACRicercaProvvedimento' + this.suffix).removeAttr('href');
        // Attiva lo spinner
        spinner.addClass('activated');
        

        $.postJSON('ajax/strutturaAmministrativoContabileAjax.do',{})
        .then(this.caricaStruttureCallback.bind(this))
        .always(spinner.removeClass.bind(spinner, 'activated'));

    }

    function caricaStruttureCallback(data) {
    	if(!data){
    		return;
    	}
        var listaStrutturaAmministrativoContabile  = (data.listaElementoCodifica);
        var suffixZtreeStruttura = 'RicercaProvvedimento' + this.suffix;
//        this.ztreeSAC = new Ztree(suffixZtreeStruttura);
//        this.ztreeSAC.inizializza(listaStrutturaAmministrativoContabile);
        this.ztreeSAC = new Ztree('StrutturaAmministrativoContabile', suffixZtreeStruttura, 'Seleziona la Struttura amministrativa');
        this.ztreeSAC.inizializza(listaStrutturaAmministrativoContabile);
        // Ripristina l'apertura del modale
        $('#bottoneSACRicercaProvvedimento' + this.suffix).attr('href', '#strutturaAmministrativoContabile' + suffixZtreeStruttura);
        
        if(this.caricamentoSACEsterno){
        	return;
        }
        
        $(document).trigger({
            type : 'struttureAmministrativeCaricate',
            lista : listaStrutturaAmministrativoContabile
        });
        
    }
    
    function inizializza(){
    	if(this.caricamentoSACEsterno){
    		$(document).on("struttureAmministrativeCaricate", this.caricaStruttureDaEvento.bind(this));
    	}else{
    		this.caricaStrutture();
    	}
    	
	    this.svuotaFormRicerca();
	
	    // Lego le azioni ai pulsanti
	   $('#pulsanteRicercaProvvedimento' + this.suffix).substituteHandler('click', this.cercaProvvedimento.bind(this));
	   $('#pulsanteSelezionaProvvedimento' + this.suffix).substituteHandler('click', this.impostaUid.bind(this));
	   $('#pulsanteDeselezionaProvvedimento' + this.suffix).substituteHandler('click', this.deseleziona.bind(this));
	   submitFormOnEnterPress('#formRicercaConOperazioniProvvedimento' + this.suffix);
	
	   $('#deselezionaStrutturaAmministrativaResponsabileRicercaProvvedimento' + this.suffix).substituteHandler('click', this.deselezionaSAC.bind(this));
	   // Cancella la tabella dei provvedimenti quando si effettua un reset del form
	   $('form').on('reset', this.onFormReset.bind(this));
    }
    
    function onFormReset() {
    	$('#tabellaProvvedimento' + this.suffix).slideUp();
    	this.deseleziona();
    	
    	if($('#collapseProvvedimento').hasClass('in')){
    		$('[data-toggle="collapse"][href="#collapseProvvedimento' + this.suffix + '"]').trigger('click');
    	}
    }
    
    function deselezionaSAC(){
    	$('#struttAmmRicercaProvvedimento' + this.suffix).modal('hide');
    	this.ztreeSAC.deselezionaNodiEAbilitaAlberatura();
    }

   function destroy(){
         $('#tabellaProvvedimento' + this.suffix).slideUp();
         deseleziona();
    }
   
}(this, $));
