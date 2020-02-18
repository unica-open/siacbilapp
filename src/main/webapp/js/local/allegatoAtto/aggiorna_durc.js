/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    // Variabili pseudo-globali
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");

//    var moreCols = [
//        {aTargets: [10], mData: defaultPerDataTable('domStringAzioni'), fnCreatedCell: function(nTd, sData, oData) {
//            var $nTd = $(nTd).addClass("tab_Right");
//            $nTd.find(".aggiornaQuotaElenco")
//                .substituteHandler("click", aggiornaQuotaElenco.bind(undefined, nTd, oData, "aggiornaAllegatoAtto_aggiornaSubdocumentoElenco.do"));
//            $nTd.find(".eliminaQuotaElenco")
//                .substituteHandler("click", eliminaQuotaElenco.bind(undefined, oData, "aggiornaAllegatoAtto_eliminaSubdocumentoElenco.do"));
//            $nTd.find(".sospendiPagamentoSoggetto")
//                .substituteHandler("click", sospendiPagamentoSoggetto.bind(undefined, oData, "aggiornaAllegatoAtto_sospendiPagamentoSoggetto.do"));
//            $nTd.find(".riattivaPagamentoSoggetto")
//                .substituteHandler("click", riattivaPagamentoSoggetto.bind(undefined, oData, "aggiornaAllegatoAtto_riattivaSoggettoSospeso.do"));
//            $nTd.find(".spezzaQuotaElenco")
//                .substituteHandler("click", spezzaQuotaElenco.bind(undefined, nTd, oData, "aggiornaAllegatoAtto_spezzaSubdocumentoElenco.do"));
//        }}
//    ];

    /**
     * Imposta i dati negli alert, e ricarica l'elenco.
     *
     * @param data               (Object) i dati dell'invocazione asincrona
     * @param elemento           (Object) l'elemento originale
     * @param modal              (jQuery) il modale da chiudere (Optional, default: undefined)
     * @param alertErroriDaUsare (jQuery) l'alert degli errori da utilizzare (Optional, default: $("#ERRORI"))
     * @return (any) un oggetto che specifica se ci sono errori
     */
    function impostaDatiERicaricaElenco(data, elemento, modal, alertErroriDaUsare) {
        var evt;
        var alt = alertErroriDaUsare || alertErrori;
        if(impostaDatiNegliAlert(data.errori, alt)) {
            return {errori: true};
        }
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        evt = $.Event("elencoSelected");
        evt.elenco = {uid: elemento.uidElencoDocumentiAllegato};
        evt.startPage = getPageTabellaCollegati();
        $("#tabellaElencoDocumentiAllegato").trigger(evt);
        modal && modal.modal("hide");
        return {errori: false};
    }
    
    function caricaTab() {
    	caricaElencoSoggetti();
    	caricaStrutture();
    }
    

    
    function caricaElencoSoggetti() {
        $.postJSON('aggiornaAllegatoAtto_ottieniListaSoggettiDurc.do', {'uidAllegatoAtto': $('#HIDDEN_uidAllegatoAtto').val()})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }

            initDataTable(data.allegatoAtto.elencoSoggettiDurc);
        }).always(function() {
        });
    }
   
    
	function initDataTable(soggetti) {
		
		var dt = $('#tabellaElencoSoggetti');
		
        var opts = {
                aaData: soggetti,
                bPaginate: true,
                bLengthChange: false,
                bSort: false,
                bInfo: true,
                bAutoWidth: false,
                bFilter: false,
                bProcessing: true,
                bDestroy: true,
                iDisplayLength: 5,
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
                aoColumnDefs: [
//                    {aTargets: [0], mData : function(src) { 
//                    	return '<input class="idSoggetti" name="soggUid" type="radio" value="'+src.uid+'"/>'; }},
                    {aTargets: [0], mData : 'codiceSoggetto'},
                    {aTargets: [1], mData : 'denominazione'},
                    {aTargets: [2], mData : 'codiceFiscale'},
                    {aTargets: [3], mData : defaultPerDataTable('partitaIva')},
                    {aTargets: [4], mData : defaultPerDataTable('dataFineValiditaDurcStr', '')},
                    {aTargets: [5], mData : defaultPerDataTable('loginModificaDurc')},
                    {aTargets: [6], mData : defaultPerDataTable('descrizioneFonteDurc')},
                    {aTargets: [7], mData : function(src) { 
                    	return  src.tipoFonteDurc=='A' ? '' : '<button '
                    	+'data-id-soggetto="' + (src.uid||'') + '" '
                    	+'data-note-durc="' + (src.noteDurc||'') + '" '
                    	+'data-classif-id-durc="' + (src.fonteDurcClassifId||'') + '" '
                    	+'data-fine-validita-durc="' + (src.dataFineValiditaDurcStr||'') + '"' 
                    	+ 'class="btn dropdown-toggle aggiorna-durc" data-placement="left" href="#" tabindex="0" >Aggiorna DURC</button>'; }},
                ]
        };
        if($.fn.DataTable.fnIsDataTable(dt[0])) {
            dt.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        dt.dataTable(opts);
        
        $('.aggiorna-durc').on('click', mostraAggiornaDettaglioSoggetto);
	}
    
    function caricaStrutture() {
        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do')
        .then(function (data) {
            if(data.listaElementoCodifica.length === 1) {
                $('#HIDDEN_StrutturaAmministrativoContabileUid').val(data.listaElementoCodifica[0].uid);
            }
            ZTreeDocumento.imposta('treeStruttAmm', ZTreeDocumento.SettingsBase, data.listaElementoCodifica, 'HIDDEN_StrutturaAmministrativoContabileUid');
        });
    }
    
    function mostraAggiornaDettaglioSoggetto(evt) {
    	evt.preventDefault();
    	
    	var obj = $(this);
    	
    	$('#dataFineValiditaDurc').val(obj.data('fine-validita-durc'));
    	if (obj.data('classif-id-durc')) {
    		ZTreeDocumento.selezionaNodoSeApplicabile('treeStruttAmm', obj.data('classif-id-durc'));
    	}
    	$('#noteDurc').val(obj.data('note-durc'));
    	$('#idSoggetto').val(obj.data('id-soggetto'));
    	
    	
    	$('#dettaglioSoggetto').slideDown().scrollToSelf();
    }
    
    function salvaDurc(evt) {
    	var errori = [];
    	
    	if (!$('#dataFineValiditaDurc').val().length) {   
    		errori.push({codice: 'COR_ERR_0002', descrizione: 'Dato obbligatorio omesso: data fine validit&agrave; DURC' });
    	}
    	
    	if (!$('#HIDDEN_StrutturaAmministrativoContabileUid').val().length) {
    		errori.push({codice: 'COR_ERR_0002', descrizione: 'Dato obbligatorio omesso: fonte DURC' });
    	}
    	
    	if (impostaDatiNegliAlert(errori, alertErrori)) {
        	evt.preventDefault();
    		return false;
    	}    		
    	
    	$(this).closest('form').attr('action', '/siacbilapp/aggiornaAllegatoAtto_salvaDurc.do')
    }
    
    $(function() {
        $('#navDurc').one('click', caricaTab);
        
        $('#salvaDurc').on('click', salvaDurc);
    });
}(jQuery);