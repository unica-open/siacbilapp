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
        $('#variazioneTassoImpegniSelezionati').prop('disabled', ! impegniChecked());
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
    		impostaCompletamentoImpegniSelezionati();
    		return;
    	}
    }
    
    function impostaCompletamentoImpegniSelezionati(e){
    	var $modaleVariazioneTassoImpegniSelezionati = $('#modaleVariazioneTassoImpegniSelezionati');
    	var idsImpegni = getIdImpegniSelezionati();

    	$modaleVariazioneTassoImpegniSelezionati.find('#spanModaleVariazioneTassoImpegniSelezionati').html(idsImpegni.length);
    	$modaleVariazioneTassoImpegniSelezionati.modal('show'); 	
		$('#buttonVariazioneTassoImpegniSelezionati').substituteHandler('click', chiamaServizioConvalidaCompleta);
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
 		$('#buttonVariazioneTassoImpegniSelezionati').prop('disabled', true);
		
 		var $modal = $('#modaleVariazioneTassoImpegniSelezionati');

 		$.postJSON('risultatiRicercaImpegno_effettuaVariazioneTassoImpegniSelezionati.do', 
 				qualify({'elencoIdImpegni': getIdImpegniGestioneSelezionati().val()}))
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, $alertErrori)){
    			return;
    		}
    		
    		impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
    	})
    	.always(function(){
    		$modal && $modal.modal('hide');
    		$('#buttonVariazioneTassoImpegniSelezionati').prop('disabled', false);
    		$("#risultatiRicercaImpegno").dataTable().fnClearTable();
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
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
				{aTargets: [0], mData: function(source) {
                    var res = "<input type='checkbox' class='idImpegno' name='uidImpegno' data-uid-impegno='" + source.uid + "' value='" + source.uid + "'";
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
                {"aTargets" : [ 2 ], "mData" : "numero"}
//                {"aTargets" : [ 3 ], "mData" : "statoImpegno"},
//                {"aTargets" : [ 4 ], "mData" : "periodoRimborso"},
//                {"aTargets" : [ 5 ], "mData" : "tassoInteresse"},
//                {"aTargets" : [ 6 ], "mData" : "euribor"},
//                {"aTargets" : [ 7 ], "mData" : "spread"},
//                {"aTargets" : [ 8 ], "mData" : "provvedimento"},
//                {"aTargets" : [ 9 ], "mData" : "tipo"},
//                {"aTargets" : [ 10 ], "mData" : "strutturaAmministrativa"},
//                {"aTargets" : [ 11 ], "mData" : "soggetto"},
//                {"aTargets" : [ 12 ], "mData" : "sommaMutuataIniziale"},
//                {"aTargets" : [ 3 ],"mData" : "azioni", 
//                    "fnCreatedCell" : function (nTd, sData, oData) {
//                        $("a[href='#msgAnnulla']", nTd).off("click")
//                            .on("click", function() {
//                                $("#HIDDEN_UidDaAnnullare").val(oData.uid);
//                            });
//                        $('.dropdown-toggle', nTd).dropdown();
//                    }
//                    }
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
        $('#variazioneTassoImpegniSelezionati').substituteHandler('click', Impegno.gestioneCompletamentoMultiplo);	
    }
);