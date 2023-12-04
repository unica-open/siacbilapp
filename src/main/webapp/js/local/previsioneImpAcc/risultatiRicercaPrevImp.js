/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
*******************************************************
**** Risultati Ricerca Capitolo Di Uscita Gestione ****
*******************************************************
*/
!function(global, $) {
	"use strict"
	
	
	function apriModalePrevisioneImpegnatoAccertato(e){
		var pulsante = e && $(e.target);
		if(!pulsante){
			return;
		}
		var uidCapitolo = pulsante.data("uid");
		var $tr = pulsante.closest('tr').overlay("show");;
		var obj = {"uidCapitolo": uidCapitolo};
		$.post('risultatiRicercaCapUGPrevisioneImpegnatoAccertato_caricaTabellaImporti.do', obj)
        .then(function(data) {
			$('#tabellaImportiCapitolo').html(data);
		$("#msgPrevisioneImpegnatoAccertatoLabel").modal("show");
		}).always($tr.overlay.bind($tr, "hide"));
	}
	
	function aggiornaPrevisioneImpegnatoAccertatp(){
		var importoPrevAnno1 = $("#previsioneAnno1").val() || "0,00";
		var importoPrevAnno2 = $("#previsioneAnno2").val() || "0,00";
		var importoPrevAnno3 = $("#previsioneAnno3").val() || "0,00";
		var uid=$("#HIDDEN_UidPrevisioneImpegnatoAccertato").val();
		var note =$('#previsioneNote').val() || "";
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
					return;
				}
			$("#msgPrevisioneImpegnatoAccertatoLabel").modal("show");
		}).always();
	}
	
	$(document).ready(
	    function () {
	        /* Attiva il DataTable */
	        var startPos = $("#HIDDEN_startPosition").val();
			var risultatiRicercaCapitoloSpesa = new RisultatiRicercaCapitolo();
	        risultatiRicercaCapitoloSpesa.inizializza("risultatiRicercaCapUscitaGestionePrevisioneImpegnatoAjax.do", "#risultatiricerca", startPos);
			$('#risultatiricerca').on( 'draw', function () {
			   $('.gestionePrevisioneImpegnatoAccertato').substituteHandler("click", apriModalePrevisioneImpegnatoAccertato);
			} );
			
			$('#pulsanteAggiornaPrevisioneImpegnatoAccertato').substituteHandler("click", aggiornaPrevisioneImpegnatoAccertatp);
			
	    }
	);

}(this, jQuery);
