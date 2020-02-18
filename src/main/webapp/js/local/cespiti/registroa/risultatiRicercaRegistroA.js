/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global) {
    'use strict';
    var alertErrori = $('#ERRORI');
    var datatable;
    $(init);
    
    function init() {
        impostaTabella();
    }
    
    
    function checkOperazioneConsentita(primaNota, nTd, urlCheck, urlOperazione, operazione, e){
    	var obj = {};
    	var $nTr = $(nTd).closest('tr').overlay("show");
    	var operazioneRichiesta = operazione? operazione : " elaborare "
    	obj.primaNota = {};
    	e && e.preventDefault();
    	if(!primaNota){
    		return;
    	}
    	
    	obj.primaNota.uid = primaNota.uid;
    	
    	$.postJSON(urlCheck, qualify(obj)).then(function(data){
    		var messaggi;
    		var stringaMessaggi = "";;
    		if(impostaDatiNegliAlert(data.errori, alertErrori)){
    			return;
    		}
    		messaggi = data.messaggi;
    		
    		if(messaggi && messaggi.length >0 ){
    			jQuery.each(messaggi,function(key, value){
    				if(this.descrizione){
    					stringaMessaggi += this.descrizione;
    				}
    			});
    			impostaRichiestaConfermaUtente(stringaMessaggi + ' . Sei sicuro di voler proseguire?',
	        			handleModalSubmit.bind(undefined, primaNota, urlOperazione));
    			return;
    		}
    		impostaRichiestaConfermaUtente('Stai per '+ operazioneRichiesta + ' l\'elemento selezionato: sei sicuro di voler proseguire?',
        			handleModalSubmit.bind(undefined, primaNota, urlOperazione))
    		
    	}).always($nTr.overlay.bind($nTr, 'hide'));
    	
    	
    }
      
    
    function impostaTabella() {
        var $tabella = $('#tabellaPrimeNoteRegistroA').overlay({usePosition: true});
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource : 'risultatiRicercaRegistroACespiteAjax.do',
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            iDisplayStart: $('#HIDDEN_savedDisplayStart').val() || 0,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
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
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnPreDrawCallback: function() {
                $tabella.overlay('show');
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#tabellaRisultatiRicercaCespite_processing').parent('div')
                    .hide();
                $tabella.find('a[rel="popover"]').popover();
                $tabella.find('a.tooltip-test').tooltip();
                $tabella.overlay('hide');
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numeroPrimaNota')},
                {aTargets: [1], mData: defaultPerDataTable('contoPatrimoniale')},
                {aTargets: [2], mData: defaultPerDataTable('dataDefinizionePrimaNota')},
                //{aTargets: [3], mData: defaultPerDataTable('numeroInventario')},
                {aTargets: [3], mData: defaultPerDataTable("infoCespite"), fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).click([oData, nTd], apriModaleDettagliCespiti);
                }},

                
                
                {aTargets: [4], mData: defaultPerDataTable('statoInventario')},
                {aTargets: [5], mData: defaultPerDataTable('statoCoGe')},
                {aTargets: [6], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    var $ntd = $(nTd);
                    $ntd.find('a[data-operazione="rifiutaRegistroA"]').substituteHandler('click', checkOperazioneConsentita.bind(undefined, oData, nTd, "risultatiRicercaRegistroACespite_verificaRifiutoPossibileSuPrimaNotaRegistroA.do", 'risultatiRicercaRegistroACespite_rifiuta.do', 'rifiutare'));
                    $ntd.find('a[data-operazione="integraRegistroA"]')
                    	.substituteHandler('click',   checkOperazioneConsentita.bind(undefined, oData, nTd, "risultatiRicercaRegistroACespite_verificaIntegraPossibilePrimaNotaRegistroA.do", 'risultatiRicercaRegistroACespite_integra.do', 'integrare'));                 		
                    $ntd.find('a[data-operazione="aggiornaRegistroA"]')
                    	.substituteHandler('click', handleModalSubmit.bind(undefined, oData, 'risultatiRicercaRegistroACespite_aggiorna.do'));
                    
                    $ntd.find('a[data-operazione="consultaRegistroA"]')
                    	.substituteHandler('click', handleModalSubmit.bind(undefined, oData, 'risultatiRicercaRegistroACespite_consulta.do'));
                }}
            ]
        };
        alertErrori.slideUp();
        datatable = $tabella.dataTable(opts);
    }
    
    /**
     * Gestione del submit da modale
     * @param primaNota (any)    la prima nota
     * @param url       (string) l'URL da invocare
     */
    function handleModalSubmit(primaNota, url,e) {
        var $body = $('body');
        var inputPrimaNotaInventario = primaNota.uidPrimaNotaInventario? '<input type="hidden" name="primaNota.primaNotaInventario.uid" value="' + primaNota.uidPrimaNotaInventario + '" />' : '';
        var $form = $('<form class="hide" action="' + url + '" method="POST"><input type="hidden" name="primaNota.uid" value="' + primaNota.uid + '" />'+ inputPrimaNotaInventario + '</form>');
        e && e.preventDefault;
        e && e.stopPropagation;
        $body.append($form);
        $form.submit();
    }
    
    /**
     * Apertura del modale dei dettagli.
     */
    function apriModaleDettagliCespiti (e) {
        var primaNota = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");
        e.preventDefault();
        $.postJSON("dettaglioCespitePrimaNotaLiberaAction_caricaCespitiDaPrimaNota.do", {"primaNota.primaNotaInventario.uid": primaNota.uidPrimaNotaInventario , "tipoEvento.codice": 'COGE_INV'})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaTabellaModaleCespite(data.listaCespite);

            $("#modaleDettaglioCespiti").modal("show");
        }).always(row.overlay.bind(row, "hide"));
    }
    
    
    /**
     * Impostazione della tabella della modale.
     *
     * @param list (Array) la lista da impostare
     *
     * @returns (jQuery) il wrapper della tabella impostata
     */
    function impostaTabellaModaleCespite(list) {
        var tableId = "#modaleDettaglioCespitiTabella";
        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti cespiti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun cespite disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('codiceDescrizione')},
                {aTargets: [1], mData: defaultPerDataTable('tipoBene')},
                {aTargets: [2], mData: defaultPerDataTable('valoreIniziale')},
                {aTargets: [3], mData: defaultPerDataTable('valoreAttuale')},
                {aTargets: [4], mData: defaultPerDataTable('stato')},
            ]
        };
        
        return $(tableId).dataTable(opts);
    }
    
}(this);
