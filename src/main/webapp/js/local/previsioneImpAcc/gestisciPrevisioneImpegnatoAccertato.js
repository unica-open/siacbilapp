/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *******************************************************************
 **** Risultati Ricerca Capitoli Previsione Impegnato Accertato ****
 *******************************************************************
 */

(function($) {

    "use strict";

    var alertErrori = $('#ERRORI');
	var alertInformazioni = $('#INFORMAZIONI');
    var divRisultatiRicercaCapitoli = $('#risultatiDiRicercaCapitoli');
	var divTabellaUscita = $('#tabellaUscita');
	var divTabellaEntrata = $('#tabellaEntrata');
	var urlRicercaBase ="ricercaPrevisioneImpegnatoAccertato_ricercaCapitoli";
	var risultatiRicercaCapitoloSpesa = new RisultatiRicercaCapitolo("#risultatiRicercaCapitoloUscitaGestione");
	var risultatiRicercaCapitoloEntrata = new RisultatiRicercaCapitolo("#risultatiRicercaCapitoloEntrataGestione");
	var overalyRicerca = $('#collapseRicerca');
	
    function postRicercaCapitoliSpesa() {
        /* Attiva il DataTable */
		var startPos = $("#HIDDEN_startPosition").val();
		risultatiRicercaCapitoloSpesa.destroy();
        risultatiRicercaCapitoloSpesa.inizializza("risultatiRicercaCapUscitaGestionePrevisioneImpegnatoAjax.do",  startPos);
		divTabellaEntrata.hide();
		divTabellaUscita.show();
		divRisultatiRicercaCapitoli.slideDown();
		overalyRicerca.overlay("hide");
    }

    function postRicercaCapitoliEntrata() {
        /* Attiva il DataTable */
        var startPos = $("#HIDDEN_startPosition").val();
		risultatiRicercaCapitoloEntrata.destroy();
        risultatiRicercaCapitoloEntrata.inizializza("risultatiRicercaCapEntrataGestionePrevisioneAccertatoAjax.do", startPos);
		divTabellaEntrata.show();
		divTabellaUscita.hide();
		divRisultatiRicercaCapitoli.slideDown();
		overalyRicerca.overlay("hide");
    }

	function effettuaRicerca(url, callbackFunction){
		var objectToSend = $("#fieldsetRicerca").serializeObject();
		
		divRisultatiRicercaCapitoli.slideUp();
		$("#HIDDEN_startPosition").val("0");
		
		return $.postJSON(url, qualify(objectToSend))
            .then(function(data) {
                var errori = data.errori;

                if(impostaDatiNegliAlert(errori, alertErrori)){
					overalyRicerca.overlay("hide");
                    return;
                }
                divTabellaUscita.hide();
				divTabellaEntrata.hide();
				if(callbackFunction && typeof callbackFunction == "function"){
					callbackFunction();
				}
                
            });
	} 

    function ricercaCapitoli(){
    	var isEntrata = $('#tipoCapitoloRadioEntrata').prop("checked");
		overalyRicerca.overlay("show");
		if(isEntrata){
			return effettuaRicerca(urlRicercaBase + "Entrata.do", postRicercaCapitoliEntrata );
		}
		return effettuaRicerca(urlRicercaBase + "Spesa.do", postRicercaCapitoliSpesa );
	}
	
	function resetCampiRicerca(){
		var tree = $.fn.zTree.getZTreeObj("treeStruttAmm");
        var nodo = tree && tree.getCheckedNodes(true)[0];
		var fieldset = $('#fieldsetRicerca');
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
		fieldset.find('input:not([type="radio"]):not([type="hidden"])').val("");
		fieldset.find('input[type="radio"]').prop("checked", false);
	}
	
	function apriModalePrevisioneImpegnatoAccertato(e){
		e && e.preventDefault() && e.stopPropagation; 
		var pulsante = e && $(e.target);
		var isEntrata;
		var uidCapitolo;
		var $tr;
		var obj;
		var url;
		var descrizione;
		if(!pulsante){
			return;
		}
		uidCapitolo = pulsante.data("uid");
		obj = {"uidCapitolo": uidCapitolo};
		isEntrata = $('#tipoCapitoloRadioEntrata').prop("checked");
		url = isEntrata? 'risultatiRicercaCapEGPrevisioneImpegnatoAccertato_caricaTabellaImporti.do' : 'risultatiRicercaCapUGPrevisioneImpegnatoAccertato_caricaTabellaImporti.do';
		descrizione = (isEntrata? " Accertato " : " Impegnato ") + pulsante.data("chiave-logica");
		$tr = pulsante.closest('tr').overlay("show");
		$.post(url, obj)
        .then(function(data) {
			//pulisco eventuali errori precedenti
			$('#ERRORI_modale').hide();
			
			$('#tabellaImportiCapitolo').html(data);
			$('#descrizionePrevisioneImpAcc').html(descrizione);
			$('.decimale').off("blur").gestioneDeiDecimali();
			$("#msgPrevisioneImpegnatoAccertatoLabel").modal("show");
		}).always($tr.overlay.bind($tr, "hide"));
	}
	
	function aggiornaPrevisioneImpegnatoAccertato(){
		var importoPrevAnno1 = $("#previsioneAnno1").val() || "0,00";
		var importoPrevAnno2 = $("#previsioneAnno2").val() || "0,00";
		var importoPrevAnno3 = $("#previsioneAnno3").val() || "0,00";
		var uid=$("#HIDDEN_UidPrevisioneImpegnatoAccertato").val();
		var note =$('#previsioneNote').val() || "";
		var pulsanteConferma = $('#pulsanteAggiornaPrevisioneImpegnatoAccertato').overlay("show");
		var previsioneImpegnatoAccertato = {
			uid: uid,
			importoPrevAnno1: importoPrevAnno1,
			importoPrevAnno2: importoPrevAnno2,
			importoPrevAnno3: importoPrevAnno3,
			note: note
		};
		$.postJSON("risultatiRicercaCapUGPrevisioneImpegnatoAccertato_aggiorna.do", qualify(previsioneImpegnatoAccertato, "previsioneImpegnatoAccertato"))
			.then(function(data){
				if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modale'))){
					pulsanteConferma.overlay("hide");
					return;
				}
				impostaDatiNegliAlert(data.informazioni, alertInformazioni);
				$("#msgPrevisioneImpegnatoAccertatoLabel").modal("hide");
				$('#tabellaImportiCapitolo').html("");
		}).always(pulsanteConferma.overlay.bind(pulsanteConferma, "hide"));
	}
	
	function postRicerca(){
		var isEntrata = $('#tipoCapitoloRadioEntrata').prop("checked");
		overalyRicerca.overlay("show");
		if(isEntrata){
			return postRicercaCapitoliEntrata();
		}
		postRicercaCapitoliSpesa();
	}
	

    $(function () {
		$('#cercaCapitoliPerPrevisione').substituteHandler('click', ricercaCapitoli);
		$('#risultatiRicercaCapitoloUscitaGestione').on( 'draw', function () {
		   $('.gestionePrevisioneImpegnatoAccertato').substituteHandler("click", apriModalePrevisioneImpegnatoAccertato);
		} );
		
		$('#risultatiRicercaCapitoloEntrataGestione').on( 'draw', function () {
		   $('.gestionePrevisioneImpegnatoAccertato').substituteHandler("click", apriModalePrevisioneImpegnatoAccertato);
		} );
		
		$('#pulsanteAggiornaPrevisioneImpegnatoAccertato').substituteHandler("click", aggiornaPrevisioneImpegnatoAccertato);
		
		$('#pulisciCampiRicerca').substituteHandler('click', resetCampiRicerca);;
		
		var forzaRicerca = $('#HIDDEN_forzaRicerca'); 
		if(forzaRicerca.val() === "true"){
			postRicerca();
	     }else {
			Capitolo.caricaStrutturaAmministrativoContabile();	
		}
		
    });

}(jQuery));