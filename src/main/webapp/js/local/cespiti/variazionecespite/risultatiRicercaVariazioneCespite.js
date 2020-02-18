/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!(function($) {
    'use strict';
    var $alertErrori = $('#ERRORI');
    var $alertInfo = $('#INFORMAZIONI');
    var $modaleEliminazione = $('#modaleEliminazione');
    var baseUrl = $('#HIDDEN_baseUrl').val();
    var urlRicaricaTabella = 'risultatiRicercaVariazioneCespiteAjax.do';
    var $nuovoValoreAttualeVariazioneCespite = $('#fieldsetDatiVariazione #nuovoValoreAttualeVariazioneCespite');
    var $modaleAggiornamento = $('#modaleAggiornaVariazioneCespite');
    
    function recomputeNuovoValoreAttualeVariazioneCespite(valoreAttualeCespite, importoOriginaleVariazione, isIncremento) {
    	var $importo = $('#importoVariazioneCespite')
        var newImporto = +parseLocalNum($importo.val());
        var oldImporto = +importoOriginaleVariazione; 
        var diff = isIncremento ? (newImporto - oldImporto) : (oldImporto - newImporto);
        var oldValore = valoreAttualeCespite;
        // Verificare il segno
        var newValore = oldValore + diff;
        $nuovoValoreAttualeVariazioneCespite.val(formatMoney(newValore));
    }
    
    /**
     * Eliminazione della variazione del cespite
     * @param uid l'uid della variazione
     */
    function eliminaVariazioneCespite(uid){
        var $spinner = $('#SPINNER_modaleEliminazionePulsanteSalvataggio').addClass('activated');
        var tabellaDatatable = $('#tabellaRisultatiRicercaVariazioneCespite').dataTable();
        return $.postJSON(baseUrl + '_elimina.do',{'variazioneCespite.uid': uid})
            .then(function(data){
                if(impostaDatiNegliAlert(data.errori, $alertErrori)){
                    return $.Deferred().reject().promise();
                }
                impostaDatiNegliAlert(data.informazioni, $alertInfo);
            })
            .always($modaleEliminazione.modal.bind($modaleEliminazione, 'hide'))
            .always($spinner.removeClass.bind($spinner, 'activated'))
            .then(tabellaDatatable.fnDraw.bind(tabellaDatatable));
    }
    
    function aggiornaVariazione(cespite){
    	var $tabella = $('#tabellaRisultatiRicercaVariazioneCespite');
    	var tabellaDatatable = $tabella.dataTable();
    	var $spinner = $('#SPINNER_pulsanteAggiornaVariazioneModale').addClass('activated');
    	var obj = $(fieldsetDatiVariazione).serializeObject();
    	obj.cespite = cespite;
    	return $.postJSON(baseUrl + '_aggiorna.do', qualify(obj))
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, $('#erroriVariazioneModale'))){
    			return;
    		}
    		$tabella.overlay('show');
    		impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
    		$modaleAggiornamento.modal('hide');
    	})
    	.then(tabellaDatatable.fnDraw.bind(tabellaDatatable))
    	.always($spinner.removeClass.bind($spinner, 'activated'));
    }
    
    /**
     * Impostazione della funzionalita' di eliminazione della variazione cespite
     * @param uid l'uid della variazione
     */
    function impostaEliminazioneVariazioneCespite(uid, e){
        var $button = $modaleEliminazione.find('#modaleEliminazionePulsanteSalvataggio');
        e.preventDefault();
        $button.substituteHandler('click', eliminaVariazioneCespite.bind(undefined, uid));
        $modaleEliminazione.modal('show');
    }
    
    function popolaFormAggiornaVariazione(variazione, cespite){
    	$('#uidVariazioneCespite').val(variazione.uid);
    	$('#fieldsetDatiVariazione input[name="variazioneCespite.annoVariazione"]').val(variazione.annoVariazione);
    	$('#fieldsetDatiVariazione input[name="variazioneCespite.dataVariazione"]').val(formatDate(variazione.dataVariazione));
    	$('#fieldsetDatiVariazione #descrizioneVariazione').val(variazione.descrizione);
    	$('#fieldsetDatiVariazione #importoVariazioneCespite').val(formatMoney(variazione.importo));
    	$('#fieldsetDatiVariazione #nuovoValoreAttualeVariazioneCespite').val(cespite.importoAttuale);
    }
    
    /**
     * Impostazione dei dati per l'aggiornamento della variazione del cespite
     * @param uid l'uid della variazione
     */
    function impostaAggiornamentoVariazioneCespite(uid, nTd, e) {
    	var row = $(nTd).closest("tr").overlay("show");
    	var variazioneCespite = {'uid':uid}
    	return $.postJSON(baseUrl + '_caricaVariazione.do', qualify({'variazioneCespite':variazioneCespite}))
    		.then(function(data){
    			var variazione = data.variazioneCespite || {};
    	    	var cespite = variazione.cespite || {};
    	    	
    			if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
    				return;
    			}
    			
    			popolaFormAggiornaVariazione(variazione, cespite);
    			
    			$('#fieldsetDatiVariazione #importoVariazioneCespite')
    				.substituteHandler('change',recomputeNuovoValoreAttualeVariazioneCespite.bind(undefined,cespite.valoreAttuale, variazione.importo, variazione.flagTipoVariazioneIncremento))
    				.change()
    			$modaleAggiornamento.modal('show');
    			$('#pulsanteAggiornaVariazioneModale').substituteHandler('click', aggiornaVariazione.bind(undefined,cespite));
    			
    		})
    		.always(row.overlay.bind(row,'hide'));
    	
    }
    
     /**
      * Consultazione della variazione del cespite
      * @param uid l'uid della variazione del cespite
      */
    function consultaVariazioneCespite(uid, e) {
        e.preventDefault();
        document.location = baseUrl + '_consulta.do?variazioneCespite.uid=' + uid;
    }
    
     function impostaTabella() {
        var tabella = $('#tabellaRisultatiRicercaVariazioneCespite').overlay('show');
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: urlRicaricaTabella,
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
            fnPreDrawCallback: defaultPreDraw,
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#tabellaRisultatiRicercaVariazioneCespite_processing').parent('div')
                    .hide();
                tabella.find('a[rel="popover"]').popover();
                tabella.overlay('hide');
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('anno')},
                {aTargets: [1], mData: defaultPerDataTable('dataInserimento')},
                {aTargets: [2], mData: defaultPerDataTable('tipoVariazione')},
                {aTargets: [3], mData: defaultPerDataTable('descrizione')},
                {aTargets: [4], mData: defaultPerDataTable('importo')},
                {aTargets: [5], mData: defaultPerDataTable('stato')},
                {aTargets: [6], mData: defaultPerDataTable('cespite')},
                {aTargets: [7], mData: defaultPerDataTable('tipoBene')},
                {aTargets: [8], mData: defaultPerDataTable('azioni'), fnCreatedCell: function(nTd, sData, oData) {
                    var $nTd = $(nTd);
                    $nTd.find('a[data-elimina-variazione-cespite]').substituteHandler('click', impostaEliminazioneVariazioneCespite.bind(undefined, oData.uid));
                    $nTd.find('a[data-aggiorna-variazione-cespite]').substituteHandler('click', impostaAggiornamentoVariazioneCespite.bind(undefined, oData.uid, nTd));
                    $nTd.find('a[data-consulta-variazione-cespite]').substituteHandler('click', consultaVariazioneCespite.bind(undefined, oData.uid));
                }}
            ]
        };
        tabella.dataTable(opts);
    }
     
    
    $(function() {
        impostaTabella();
//        $importoVariazioneCespite.change(recomputeNuovoValoreAttualeVariazioneCespite);
    });
}(jQuery));
