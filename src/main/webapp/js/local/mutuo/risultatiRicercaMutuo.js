/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Mutuo = (function () {
    var exports = {};
    var selectedDatas = {};
    var $alertErrori = $('#ERRORI');
    var $alertInformazioni = $('#INFORMAZIONI');

	exports.gestioneCheckSuMutuo = gestioneCheckSuMutuo;
	exports.gestioneCompletamentoMultiplo = gestioneCompletamentoMultiplo;
	exports.getIdMutuiSelezionati = getIdMutuiSelezionati;
	
	function gestioneCheckSuMutuo() {
        var $this = $(this);

        selectedDatas[+$this.val()] = {
        	isSelected: !$this.attr('disabled') && $this.prop('checked'), 
        	data: $this.data('originalMutuo')
        };
    
        $('#variazioneTassoMutuiSelezionati').prop('disabled', ! mutuiChecked());
    }
	
	function mutuiChecked() {
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
    		impostaCompletamentoMutuiSelezionati();
    		return;
    	}
    }
    
    function impostaCompletamentoMutuiSelezionati(e){
    	var $modaleVariazioneTassoMutuiSelezionati = $('#modaleVariazioneTassoMutuiSelezionati');
    	var idsMutui = getIdMutuiSelezionati();

    	$modaleVariazioneTassoMutuiSelezionati.find('#spanModaleVariazioneTassoMutuiSelezionati').html(idsMutui.length);
    	$modaleVariazioneTassoMutuiSelezionati.modal('show'); 	
		$('#buttonVariazioneTassoMutuiSelezionati').substituteHandler('click', chiamaServizioConvalidaCompleta);
    }
   
    
    function getIdMutuiSelezionati(){
    	var idMutuiSelezionati = [];
    	var listaMutui = [];
    	var i; 
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
            	listaMutui.push(selectedDatas[i].data);
            }
        }
        idMutuiSelezionati = listaMutui.filter(filterOutUndefined).map(mapToUid);
        return idMutuiSelezionati;
    }
    
     function chiamaServizioConvalidaCompleta() {
 		$('#buttonVariazioneTassoMutuiSelezionati').prop('disabled', true);
		
 		var $modal = $('#modaleVariazioneTassoMutuiSelezionati');

 		$.postJSON('risultatiRicercaMutuo_effettuaVariazioneTassoMutuiSelezionati.do', 
 				qualify({'elencoIdMutui': getIdMutuiSelezionati(), 'tassoInteresseEuribor': $('#tassoInteresseEuribor').val()}))
    	.then(function(data){
    		if(impostaDatiNegliAlert(data.errori, $alertErrori)){
    			return;
    		}
    		
    		impostaDatiNegliAlert(data.informazioni, $alertInformazioni);
    	})
    	.always(function(){
    		$modal && $modal.modal('hide');
    		$('#buttonVariazioneTassoMutuiSelezionati').prop('disabled', false);
    		$("#risultatiRicercaMutuo").dataTable().fnClearTable();
    	});
    }
    
    
	
    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Gestione del click sul pulsante riattiva.
     *
     * @param uid l'uid da impostare
     */
    function clickOnRiattiva(uid) {
        $("#HIDDEN_UidDaRiattivare").val(uid);
    }

    function checkAbilitaVariazioneTasso() {
    	if ($('#abilitaVariazioneTasso').length === 0) {
	        $('#risultatiRicercaMutuo tr th').first().remove();
	        $('.idMutuo').parent('td').remove();
    	}
    }
    
    /**
     * Caricamento via Ajax della tabella dei progetti e visualizzazione.
     */
    exports.visualizzaTabellaMutui = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaMutuoAjax.do",
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
                
                checkAbilitaVariazioneTasso();
            },
            "aoColumnDefs" : [
				{aTargets: [0], mData: function(source) {
            		var uidMutuo = source.uid;
                    var res = "<input type='checkbox' class='idMutuo' name='uidMutuo' data-uid-mutuo='" + uidMutuo + "' value='" + uidMutuo + "'";
                    if(selectedDatas[uidMutuo] && selectedDatas[uidMutuo].isSelected){
                	   res += " checked ";
                    }

                    if(source.tipoTasso === 'Fisso' ||
                       source.codiceStatoMutuo !== 'D') {
						res += " disabled ";
					}
                    
                    res += "/>";
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd)
	                    .data("originalMutuo", oData);
                }},
                {"aTargets" : [ 1 ], "mData" : "numero"},
                {"aTargets" : [ 2 ], "mData" : "tipoTasso"},
                {"aTargets" : [ 3 ], "mData" : "descrizioneStatoMutuo"},
                {"aTargets" : [ 4 ], "mData" : "periodoRimborso"},
                {"aTargets" : [ 5 ], "mData" : "tassoInteresse"},
                {"aTargets" : [ 6 ], "mData" : "euribor"},
                {"aTargets" : [ 7 ], "mData" : "spread"},
                {"aTargets" : [ 8 ], "mData" : "provvedimento"},
                {"aTargets" : [ 9 ], "mData" : "tipo"},
                {"aTargets" : [ 10 ], "mData" : "strutturaAmministrativa"},
                {"aTargets" : [ 11 ], "mData" : "soggetto"},
                {"aTargets" : [ 12 ], "mData" : "sommaMutuataIniziale"},
                {"aTargets" : [ 13 ], "mData" : "azioni", 
                    			      "fnCreatedCell" : function (nTd, sData, oData) {
					                        $("a[href='#msgAnnulla']", nTd).off("click")
					                            .on("click", function() {
					                                clickOnAnnulla(oData.uid);
					                            });
					                        $('.dropdown-toggle', nTd).dropdown();
					                   }
                    }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#risultatiRicercaMutuo").dataTable(options);
    };

    return exports;
}());

$(
    function() {
        Mutuo.visualizzaTabellaMutui();
        $('form').on('change', 'input[type="checkbox"]', Mutuo.gestioneCheckSuMutuo);	
        $('#variazioneTassoMutuiSelezionati').substituteHandler('click', Mutuo.gestioneCompletamentoMultiplo);	
    }
);