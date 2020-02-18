/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Provvedimento = (function(exports) {
    'use strict';
    var stringaInformazioniNoProvvedimento = 'provvedimento';
    exports.inizializza= inizializza;
    exports.destroy = destroy;
    exports.svuotaFormRicerca = svuotaFormRicerca;
    
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
    function impostaDataTableProvvedimentoSenzaOperazioni(aaData) {
        // Le opzioni base del dataTable
        var optionsNuove = {
            aaData: aaData,
            iDisplayLength: 3,
            bSort: false,
            aoColumnDefs: [
                {aTargets: [0], mData: function(source, type, val) {
                    return '<input type="radio" name="uidProvvedimentoRadio" value="' + source.uid + '" />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalProvvedimento', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('anno')},
                {aTargets: [2], mData: defaultPerDataTable('numero')},
                {aTargets: [3], mData: defaultPerDataTable('tipo')},
                {aTargets: [4], mData: defaultPerDataTable('oggetto')},
                {aTargets: [5], mData: defaultPerDataTable('strutturaAmministrativoContabile')},
                {aTargets: [6], mData: defaultPerDataTable('stato')}
            ]
        };
        var options = $.extend(true, {}, optionsBase, optionsNuove);
        var tabellaProvvedimentoGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($('#risultatiRicercaProvvedimento'));
        // Pulisco il dataTable prima di inizializzarne uno nuovo
        if(tabellaProvvedimentoGiaInDataTable.length > 0) {
            // Distruggo la dataTable
            tabellaProvvedimentoGiaInDataTable.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        $('#risultatiRicercaProvvedimento').dataTable(options);
    }

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     *
     * @param doNotPopulateErrori (Boolean) se gli errori siano da popolare o meno (Optional, default: false)
     */
    function cercaProvvedimento(e) {
        // Workaround per la gestione del fieldset su Explorer
        var formDiRicerca = $('#formRicercaProvvedimento :input');
        // Valori per il check javascript
        var annoProvvedimento = $('#annoProvvedimento').val();
        var numeroProvvedimento = $('#numeroProvvedimento').val();
        var tipoProvvedimento =$('#tipoAttoProvvedimento').val();
        // Errori
        var erroriArray = [];
        // Alert per gli errori
        var alertErrori = $('#ERRORI');
        // Spinner
        var spinner = $('#SPINNER_Provvedimento');

        e && e.preventDefault();

        // Controllo che non vi siano errori
        if(annoProvvedimento === '') {
            erroriArray.push('Il campo Anno deve essere selezionato');
        }
        if(numeroProvvedimento === '' && tipoProvvedimento === '') {
            erroriArray.push('Almeno uno tra i campi Numero e Tipo deve essere compilato');
        }

        // se arrivo dal ricaricamento della pagina, e' accettabile che i campi anno e numero non siano compilati
        if((e && impostaDatiNegliAlert(erroriArray, alertErrori))){
            pulisciENascondiTabellaProvvedimento();            
            return;
        }
        
        spinner.addClass('activated');

        // Chiamata AJAX
        $.postJSON('effettuaRicercaSenzaOperazioniProvvedimento.do', formDiRicerca.serializeArray())
        .then(function (data) {
            var listaProvvedimenti = data.listaElementoProvvedimento;
            // Alerts
            var alertMessaggi = $('#MESSAGGI');
            var alertInformazioni = $('#INFORMAZIONI');
            // Tabella gia' in dataTable
            var tabellaProvvedimentoGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($('#risultatiRicercaProvvedimento'));

            alertErrori.slideUp();
            alertMessaggi.slideUp();
            alertInformazioni.slideUp();

            tabellaProvvedimentoGiaInDataTable.length > 0 && tabellaProvvedimentoGiaInDataTable.dataTable().fnDestroy();

            // Non vi sono errori, ne' messaggi, ne' informazioni. Proseguire con la creazione del dataTable
            impostaDataTableProvvedimentoSenzaOperazioni(listaProvvedimenti);

            $('#tabellaProvvedimento').slideDown();
            // Imposto come selezionato un provvedimento, qualora si abbia il ripopolamento della pagina
            if($('input[name="uidProvvedimentoRadio"]:checked').length > 0) {
                impostaUid();
            }
        }).always(spinner.removeClass.bind(spinner, 'activated'));
    }

    /**
     * Imposta l'uid nel campo hidden.
     * @param e (Event) l'eventuale evento scatenante
     */
    function impostaUid(e) {
        var alertErrori = $('#ERRORI');
        var radioButton = $('input[name="uidProvvedimentoRadio"]:checked');
        var valoreDelRadio = radioButton.val();
            // Il collapse, se presente
        var divCollapse = $('#collapseProvvedimento');
        var spanInformazioni = $('#SPAN_InformazioniProvvedimento');
            // Dati del radio
        var annoProvvedimento;
        var numeroProvvedimento;
        var statoProvvedimento;
        var obj;
        var event;

        if(e && typeof e.preventDefault === 'function') {
            e.preventDefault();
        }

        // Controllo che non vi siano errori
        if(valoreDelRadio === undefined) {
            impostaDatiNegliAlert(['Necessario selezionare Provvedimento'],alertErrori, undefined, true);
            return;
        }
        alertErrori.slideUp();
        obj = radioButton.data('originalProvvedimento');
        // Determino anno e numero del Provvedimento
        annoProvvedimento = radioButton.parent().next().html();
        numeroProvvedimento = radioButton.parent().next().next().html();
        statoProvvedimento = radioButton.parent().siblings(':last').html();

        $('#HIDDEN_uidProvvedimento').val(valoreDelRadio);
        $('#HIDDEN_uidProvvedimentoInjettato').val(valoreDelRadio);
        $('#HIDDEN_numeroProvvedimento').val(numeroProvvedimento);
        $('#HIDDEN_annoProvvedimento').val(annoProvvedimento);
        $('#HIDDEN_statoProvvedimento').val(statoProvvedimento);
        // Chiudo il collapse, qualora presente
        //TODO: valutare, forse sono sempre dalla ricerca
        if(divCollapse.length > 0 && divCollapse.hasClass('in')) {
            $('[data-toggle="collapse"][href="#collapseProvvedimento"]').trigger('click');
        }
        // Imposto le informazioni del provvedimento, qualora possibile
        if(spanInformazioni.length > 0) {
            spanInformazioni.html('provvedimento: ' + annoProvvedimento + ' / ' + numeroProvvedimento);
        }
        event = $.Event('selectedProvvedimento');
        event.provvedimento = obj;
        $(document).trigger(event);
    }

    /**
     * Deseleziona il provvedimento.
     */
    function deseleziona() {
        svuotaFormRicerca();
        $('#ERRORI').slideUp();
        $('#SPAN_InformazioniProvvedimento').html(stringaInformazioniNoProvvedimento);
        $('#HIDDEN_uidProvvedimento').val('0');
        $('#HIDDEN_uidProvvedimentoInjettato').val('0');
    }

    /**
     * Svuota il form di ricerca del Provvedimento.
     */
    function svuotaFormRicerca() {
        pulisciENascondiTabellaProvvedimento();

        $('#HIDDEN_numeroProvvedimento').val('');
        $('#HIDDEN_annoProvvedimento').val('');
        $('#HIDDEN_statoProvvedimento').val('');
        $('#HIDDEN_StrutturaAmministrativoContabileUid').val('');
        $('#HIDDEN_StrutturaAmministrativoContabileCodice').val('');
        $('#HIDDEN_StrutturaAmministrativoContabileDescrizione').val('');
        $('#SPAN_StrutturaAmministrativoContabile').text('Nessuna Struttura Amministrativa Responsabile selezionata');
        $('#formRicercaProvvedimento').find(':input').val('');
        if($('#collapseProvvedimento').hasClass('in')){
            $('[data-toggle="collapse"][href="#collapseProvvedimento"]').trigger('click');
        }
    }

    /**
     * Pulisce e nasconde la tabella del provvedimento
     */
    function pulisciENascondiTabellaProvvedimento(){
        var tabelleInDataTable =$.fn.dataTable.fnTables(true);
        var tabellaProvvedimento = $('#risultatiRicercaProvvedimento');
        var tabelleDaIgnorare = $(tabelleInDataTable).not(tabellaProvvedimento);
        var arrayDaIgnorare = [];
        for(var i=0; i<tabelleDaIgnorare.size(); i++){
          arrayDaIgnorare.push(tabelleDaIgnorare.get(i));
        }
        cleanDataTables(arrayDaIgnorare);
        $('#tabellaProvvedimento').slideUp();
    }
    
    /**
     * Carico lo ztree delle strutture Amministrativo Contabili
     */
    function caricaStrutture(){
        // Pulsante per il modale della Struttura Amministrativo Contabile
        var pulsante = $('#bottoneSAC');
        var spinner = $('#SPINNER_StrutturaAmministrativoContabile');

        // Non permettere di accedere al modale finche' il caricamento non e' avvenuto
        pulsante.removeAttr('href');
        // Attiva lo spinner
        spinner.addClass('activated');

        $.postJSON('ajax/strutturaAmministrativoContabileAjax.do',{})
        .then(function (data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
            ZTree.imposta('treeStruttAmm', ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
            // Ripristina l'apertura del modale
            pulsante.attr('href', '#struttAmm');
        })
        .always(spinner.removeClass.bind(spinner, 'activated'));

    }

    function inizializza(infoNessunProvvedimento){
        if(infoNessunProvvedimento){
            stringaInformazioniNoProvvedimento = infoNessunProvvedimento;
        }
        caricaStrutture();
        if(($('#annoProvvedimento').val())&&($('#numeroProvvedimento').val()|| $('#tipoAttoProvvedimento').val())){
            svuotaFormRicerca();
        }

        // Lego le azioni ai pulsanti
        $('#pulsanteRicercaProvvedimento').substituteHandler('click', cercaProvvedimento);
        $('#pulsanteSelezionaProvvedimento').substituteHandler('click', impostaUid);
        $('#pulsanteDeselezionaProvvedimento').substituteHandler('click', deseleziona);
        submitFormOnEnterPress('#formRicercaConOperazioniProvvedimento');

        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $('form').on('reset', function() {
            $('#tabellaProvvedimento').slideUp();
            deseleziona();

            if($('#collapseProvvedimento').hasClass('in')){
                $('[data-toggle="collapse"][href="#collapseProvvedimento"]').trigger('click');
            }
        });
    }

   function destroy(){
         $('#tabellaProvvedimento').slideUp();
         deseleziona();
    }
   
    return exports;
} (Provvedimento || {}));
