/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

var Impegno = (function () {
    var exports = {};
    var selectedDatas = {};
    var $alertErrori = $('#ERRORI');
    var $alertInformazioni = $('#INFORMAZIONI');

	exports.gestioneCheckSuImpegno = gestioneCheckSuImpegno;
	exports.gestioneCompletamentoMultiplo = gestioneCompletamentoMultiplo;
	exports.getIdImpegniSelezionati = getIdImpegniSelezionati;
	
	function gestioneCheckSuImpegno() {
        var $this = $(this);

        selectedDatas[+$this.val()] = {
        	isSelected: !$this.attr('disabled') && $this.prop('checked'), 
        	data: $this.data('originalImpegno')
        };
        $('#associaMovimentiSelezionati').prop('disabled', ! impegniChecked());
    }
	
	function impegniChecked() {
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                return true;
            }
        }
        
        return false;
	}
    
    function mapToUid(el){
    	return el.uid;
    }
    
     function filterOutUndefined(el) {
        return el !== undefined;
    }
    
    function gestioneCompletamentoMultiplo(e){
    	var pulsante;
    	
    	e && e.preventDefault;
    	
    	pulsante = $(e.target);
    	
    	if(pulsante.hasClass("btn selezionati")){
    		chiamaServizioConvalidaCompleta();
    		return;
    	}
    }
    
    function getIdImpegniSelezionati(){
    	var idImpegniSelezionati = [];
    	var listaImpegni = [];
    	var i; 
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	listaImpegni.push(selectedDatas[i].data);
            }
        }
        idImpegniSelezionati = listaImpegni.filter(filterOutUndefined).map(mapToUid);
        return idImpegniSelezionati;
    }
    
     function chiamaServizioConvalidaCompleta() {
 		$('#associaMovimentiSelezionati').prop('disabled', true);
 		$('body').overlay('show');
		
 		$.postJSON('risultatiRicercaImpegnoMutuo_associaMovimentiGestioneSelezionati.do', 
 				qualify({'elencoIdMovimentiGestione': getIdImpegniSelezionati()}))
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, $alertErrori)){
    			return;
    		}
    		
    		impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
    	})
    	.always(function(){
    		$('#associaMovimentiSelezionati').prop('disabled', false);
    		$('.idMovimentoGestione:checked').prop('checked', false);
     		selectedDatas = {};
    		//$("#risultatiRicercaImpegno").dataTable().fnClearTable();
     		$('body').overlay('hide');
    	});
    }
    
    
	
    /**
	 * Gestione del click sul pulsante riattiva.
	 * 
	 * @param uid
	 *            l'uid da impostare
	 */
    function clickOnRiattiva(uid) {
        $("#HIDDEN_UidDaRiattivare").val(uid);
    }

    /**
	 * Caricamento via Ajax della tabella dei progetti e visualizzazione.
	 */
    exports.visualizzaTabellaImpegni = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaImpegnoMutuoAjax.do",
            // Metodo HTTP per la chiamata AJAX
            "sServerMethod" : "POST",
            // Gestione della paginazione
            "bPaginate" : true,
            // Impostazione del numero di righe
            "bLengthChange" : false,
            // Numero base di righe
            "iDisplayLength" : 10,
            // Sorting delle colonne
            "bSort" : false,
            // Display delle informazioni
            "bInfo" : true,
            // Calcolo automatico della larghezza delle colonne
            "bAutoWidth" : true,
            // Filtro dei dati
            "bFilter" : false,
            // Abilita la visualizzazione di 'Processing'
            "bProcessing" : true,
            // Internazionalizzazione
            "oLanguage" : {
                // Informazioni su quanto mostrato nella pagina
                "sInfo" : "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                "sInfoEmpty" : "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                "sProcessing" : "Attendere prego...",
                // Testo quando non vi sono dati
                "sZeroRecords" : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                // Definizione del linguaggio per la paginazione
                "oPaginate" : {
                    // Link alla prima pagina
                    "sFirst" : "inizio",
                    // Link all'ultima pagina
                    "sLast" : "fine",
                    // Link alla pagina successiva
                    "sNext" : "succ.",
                    // Link alla pagina precedente
                    "sPrevious" : "prec.",
                    // Quando la tabella è vuota
                    "sEmptyTable" : "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            "fnDrawCallback" : function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? ("Impegni: "+records + " Risultati trovati") : ("Impegni: 1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
				{aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' class='idMovimentoGestione' name='uidImpegno' data-uid-impegno='" + source.uid + "' value='" + source.uid + "'";
                    if(selectedDatas[source.uid] && selectedDatas[source.uid].isSelected){
                	   res += " checked ";
                    }

                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd)
	                    .data("originalImpegno", oData);
                }},
                {"aTargets" : [ 1 ], "mData" : "anno"},
                {"aTargets" : [ 2 ], "mData" : "numero"},
                {"aTargets" : [ 3 ], "mData" : "stato"},
                {"aTargets" : [ 4 ], "mData" : "missione"},
                {"aTargets" : [ 5 ], "mData" : "programma"},
                {"aTargets" : [ 6 ], "mData" : "capitolo"},
                {"aTargets" : [ 7 ], "mData" : "tipoFinanziamento"},
                {"aTargets" : [ 8 ], "mData" : "componenteBilancio"},
                {"aTargets" : [ 9 ], "mData" : "provvedimento"},
                {"aTargets" : [ 10 ], "mData" : "tipoAtto"},
                {"aTargets" : [ 11 ], "mData" : "strutturaAmministrativa"},
                {"aTargets" : [ 12 ], "mData" : "soggetto"},
                {"aTargets" : [ 13 ], "mData" : "cig"},
                {"aTargets" : [ 14 ], "mData" : "cup"},
                {"aTargets" : [ 15 ], "mData" : "sub"},
                {"aTargets" : [ 16 ], "mData" : "importoIniziale"}
            ]
        };


        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#tabellaRisultatiRicercaImpegnoMutuo").dataTable(options);
    };

    return exports;
}());

$(
    function() {
        Impegno.visualizzaTabellaImpegni();
        $('form').on('change', 'input[type="checkbox"]', Impegno.gestioneCheckSuImpegno);	
        $('#associaMovimentiSelezionati').substituteHandler('click', Impegno.gestioneCompletamentoMultiplo);	
    }
);