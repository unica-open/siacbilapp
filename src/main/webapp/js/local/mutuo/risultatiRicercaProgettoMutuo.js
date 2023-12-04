/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

var Progetto = (function () {
    var exports = {};
    var selectedDatas = {};
    var $alertErrori = $('#ERRORI');
    var $alertInformazioni = $('#INFORMAZIONI');

	exports.gestioneCheckSuProgetto = gestioneCheckSuProgetto;
	exports.gestioneCompletamentoMultiplo = gestioneCompletamentoMultiplo;
	exports.getIdProgettiSelezionati = getIdProgettiSelezionati;
	
	function gestioneCheckSuProgetto() {
        var $this = $(this);

        selectedDatas[+$this.val()] = {
        	isSelected: !$this.attr('disabled') && $this.prop('checked'), 
        	data: $this.data('originalProgetto')
        };
        $('#associaProgettiSelezionati').prop('disabled', ! progettiChecked());
    }
	
	function progettiChecked() {
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
    
    function getIdProgettiSelezionati(){
    	var idProgettiSelezionati = [];
    	var listaProgetti = [];
    	var i; 
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	listaProgetti.push(selectedDatas[i].data);
            }
        }
        idProgettiSelezionati = listaProgetti.filter(filterOutUndefined).map(mapToUid);
        return idProgettiSelezionati;
    }
    
     function chiamaServizioConvalidaCompleta() {
 		$('#associaProgettiSelezionati').prop('disabled', true);
 		$('body').overlay('show');
		
 		$.postJSON('risultatiRicercaProgettoMutuo_associaProgettiSelezionati.do', 
 				qualify({'elencoIdProgetti': getIdProgettiSelezionati()}))
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, $alertErrori)){
    			return;
    		}
    		
    		impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
    	})
    	.always(function(){
    		$('#associaProgettiSelezionati').prop('disabled', false);
    		$('.idProgetto:checked').prop('checked', false);
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
    exports.visualizzaTabellaProgetti = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaProgettoMutuoAjax.do",
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
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
				{aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' class='idProgetto' name='uidProgetto' data-uid-impegno='" + source.uid + "' value='" + source.uid + "'";
                    if(selectedDatas[source.uid] && selectedDatas[source.uid].isSelected){
                	   res += " checked ";
                    }

                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd)
	                    .data("originalProgetto", oData);
                }},
                {"aTargets" : [ 1 ], "mData" : "codice"},
                {"aTargets" : [ 2 ], "mData" : "ambito"},
                {"aTargets" : [ 3 ], "mData" : "provvedimento"},
                {"aTargets" : [ 4 ], "mData" : "valoreComplessivo"}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#tabellaRisultatiRicercaProgettoMutuo").dataTable(options);
    };

    return exports;
}());

$(
    function() {
        Progetto.visualizzaTabellaProgetti();
        $('form').on('change', 'input[type="checkbox"]', Progetto.gestioneCheckSuProgetto);	
        $('#associaProgettiSelezionati').substituteHandler('click', Progetto.gestioneCompletamentoMultiplo);	
    }
);