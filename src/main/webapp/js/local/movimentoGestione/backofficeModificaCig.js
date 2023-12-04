/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
	"use strict";
	
	var alertErrori = $('#ERRORI');
	var alertWarning = $('#MESSAGGI');
	var alertSuccess = $('#INFORMAZIONI');
	var $document = $(document);
	
	/**
	 * Elimina i messaggi di errore o di successo di un'azione
	 */
	function eliminaMessaggi() {
		alertErrori.slideUp();
		alertWarning.slideUp();
		alertSuccess.slideUp();
	}

	/**
	 * Apre il modale dell'impegno copiando i dati forniti dall'utente.
	 */
	function apriModaleImpegno() {
		$('#annoImpegnoModale').val($('#annoMovimentoMovimentoGestione').val());
		$('#numeroImpegnoModale').val($('#numeroMovimentoGestione').val());
		$('#modaleImpegno').modal('show');
	}
	
	/**
     * Imposta nella tabella Dettagli Impegno/Subimpegno selezionato i dati dell'impegno o del subimpegno caricato 
     * @param impegno (any) l'impegno o il subimpegno
     */
    function impostaImpegnoNellaTabella(impegno) {
        var disponibile = impegno.disponibilitaLiquidare || 0;
        
        $('#tabellaModaleImpegnoCaricato_tdCapitolo').html(Impegno.computeStringCapitolo(impegno.capitoloUscitaGestione));
        $('#tabellaModaleImpegnoCaricato_tdProvvedimento').html(Impegno.computeStringProvvedimento(impegno.attoAmministrativo));
        $('#tabellaModaleImpegnoCaricato_tdSoggetto').html(Impegno.computeStringSoggetto(impegno.soggetto, impegno.classeSoggetto));
        $('#tabellaModaleImpegnoCaricato_tdImporto').html(Impegno.computeStringImporto(impegno.importoAttuale, impegno.importoIniziale));
        $('#tabellaModaleImpegnoCaricato_tdDisponibile').html(disponibile.formatMoney());
    }
    
    function impostaSubImpegnoNellaTabella(subimpegno) {
    	$('#tabellaModaleSubImpegnoCaricato_tdDescrizione').html(subimpegno.descrizione);
    	$('#tabellaModaleSubImpegnoCaricato_tdSoggetto').html(subimpegno.soggetto.denominazione);
    	$('#tabellaModaleSubImpegnoCaricato_tdImporto').html(subimpegno.importoAttuale.formatMoney());
    	$('#tabellaModaleSubImpegnoCaricato_tdDisponibilitaLiquidare').html(subimpegno.disponibilitaLiquidare.formatMoney());
    }
	
	function caricaImpegno(e) {
		var annoImpegno = $('#annoMovimentoMovimentoGestione').val();
		var numeroImpegno = $('#numeroMovimentoGestione').val();
		var numeroSubImpegno = $('#numeroSubMovimentoGestione').val();
		var oggettoPerChiamataAjax;
        var arrayErrori = [];
        var $fieldset = $('.imputazioniContabiliMovimentoGestione');
        
        if(isNaN(parseInt(annoImpegno, 10))) {
            arrayErrori.push('COR_ERR_0002 - Dato obbligatorio omesso: Anno');
        }
        if(isNaN(parseInt(numeroImpegno, 10))) {
            arrayErrori.push('COR_ERR_0002 - Dato obbligatorio omesso: Numero');
        }
        if(impostaDatiNegliAlert(arrayErrori, alertErrori, true)) {
            return;
        }
        
        oggettoPerChiamataAjax = {
    		'impegno.annoMovimento': annoImpegno,
			'impegno.numero': numeroImpegno,
			//SIAC-7661
			//permetto la ricerca di tutti i sub, in questo modo verranno mostrati tutti i sub che non sono in stato annullato
			'bko': true
        };
		
        // resettiamo i campi e li nascondiamo all'utente
		annulla();
		
		$fieldset.overlay('show');
        $.postJSON('ricercaImpegnoPerChiaveOttimizzato.do', oggettoPerChiamataAjax)
        	.then( function(response){
        		//la funzione anononima esegue quando il server risponde ma il numeroSubImpegno è quello del momento in cui ho cliccato
        		gestioneRisposta(response,numeroSubImpegno);
			})
    		.always($fieldset.overlay.bind($fieldset, 'hide'));
	}
	

	function gestioneRisposta(response, numeroSubImpegno){
		var arrayErrori = [];
		var impegno;
		var subimpegno;
		var elencoSubImpegni;
		var event;
		if(response.errori.length>0){
			impostaDatiNegliAlert(response.errori, alertErrori, true);
			return;
		}
		
		impegno = response.impegno;
		elencoSubImpegni = response.impegno.elencoSubImpegni || [];
		subimpegno;
		
			
		if(!numeroSubImpegno) {
			// Ignoro subimpegno
			event = $.Event('impegnoCaricato', {'impegno': impegno});
			$document.trigger(event);
			//Da qui in poi continua il giro 'normale' (dalla funzione gestisciImpegnoCaricato)
			return;
		}
		
		// TODO filtro per numero sub
		subimpegno = elencoSubImpegni.find(function(el) {
			return +el.numero === +numeroSubImpegno;
		});

		if(!subimpegno){
			impostaDatiNegliAlert(['COR_ERR_0000 - Nessun subimpegno corrispondente al numero ' + numeroSubImpegno], alertErrori, true);
			return;
		}
		
		//i dati di impegno sono in response.impegno
		//i dati dei sub-impegni sono response.impegno.elencoSubimpegni
		//in base al fatto che nel form ci sia il sub impegno occorre cercarlo nel campo elencoImpegni
		
		event = $.Event('impegnoCaricato', {'impegno': impegno, 'subimpegno': subimpegno});
		//Da qui in poi continua il giro 'normale' (dalla funzione gestisciImpegnoCaricato)
		$document.trigger(event);
	}
	
	function annulla(){
		eliminaMessaggi();
		$("#divBloccoSiope, #divCommerciale, #divTipoModifica, #divImpegnoCaricato").addClass("hide");
		$("#pulsanteCompilazioneGuidataMovimentoGestione").removeAttr('disabled');
		$("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione, #numeroSubMovimentoGestione").removeAttr('readonly');
		$('#uidImpegno, #uidSubImpegno').val('');
		$("#divImpegniTrovati").slideUp();
	}
	
	function gestisciSiopeTipoDebito() {
    	var $this = $('input[type="radio"][name="siopeTipoDebito.uid"]').filter(':checked');
        var siopeTipoDebito = $this.data('descrizione');
        
        $("#divCommerciale").removeClass("hide");
        if(siopeTipoDebito !== "COMMERCIALE") {
        	$("#bloccoMotivazioneAssenzaCig").hide();
        	$("#siopeAssenzaMotivazione").attr('disabled', true);
        } else {
        	$("#bloccoMotivazioneAssenzaCig").show();
        	$("#siopeAssenzaMotivazione").removeAttr('disabled');
        }
    }
	
	function gestisciImpegnoCaricato(e) {
		var siopeTipoDebito;
		var radio;
		var movgest = e.subimpegno ? e.subimpegno : e.impegno;
		var siopeTipoDebito = movgest.siopeTipoDebito;
		$("#divBloccoSiope").removeClass("hide");
		$("#divTipoModifica").removeClass("hide");
		if(e.subimpegno){
			siopeTipoDebito= e.subimpegno.siopeTipoDebito;
			$('#uidSubImpegno').val(e.subimpegno.uid);
		} else {
			siopeTipoDebito= e.impegno.siopeTipoDebito;
			$('#uidImpegno').val(e.impegno.uid);
		}
		
		eliminaMessaggi();
		impostaImpegnoNellaTabella(e.impegno);
		
		$('#siopeAssenzaMotivazione').val(movgest.siopeAssenzaMotivazione ? movgest.siopeAssenzaMotivazione.uid : '');
		$('#cig').val(movgest.cig);
		$("#divImpegnoCaricato, #divBloccoSiope, #divTipoModifica").removeClass("hide");
		$('#uidSubImpegno, #uidImpegno').val('');
		
		if(e.subimpegno){
			$('#uidSubImpegno').val(movgest.uid);
			$("#divTabellaModaleSubImpegnoCaricato").removeClass("hide");
			impostaSubImpegnoNellaTabella(e.subimpegno);
		} else {
			$('#uidImpegno').val(movgest.uid);
			$("#divTabellaModaleSubImpegnoCaricato").addClass("hide");
		}
		
		if(siopeTipoDebito.codice === 'CO'){
			radio = $('input[type="radio"][name="siopeTipoDebito.uid"][data-descrizione="COMMERCIALE"]');
		} else {
			radio = $('input[type="radio"][name="siopeTipoDebito.uid"][data-descrizione="NON COMMERCIALE"]');
		}
		
		radio.attr('checked',true);
		radio.change();
		$("#pulsanteCompilazioneGuidataMovimentoGestione").attr('disabled',true);
	}
	
	$(function() {
		// Inizializzo il modale
		$('#pulsanteSelezionaImpegno').click(caricaImpegno);
		$("#pulsanteCompilazioneGuidataMovimentoGestione").click(apriModaleImpegno);
		Impegno.inizializza();
		$(document).on('impegnoCaricato', gestisciImpegnoCaricato);
		$('input[type="radio"][name="siopeTipoDebito.uid"]').on('change', gestisciSiopeTipoDebito);
		
		//Gestione custom evento annulla
		$('#formBackofficeModificaCig').on('reset',annulla);
		
		//Gestione impegno selezionato precedentemente al caricamento della pagina
		var impegnoPrec = $('#uidSubImpegno').val() != '' || $('#uidImpegno').val() != '';
		
		if(impegnoPrec) {
			$("#divBloccoSiope, #divTipoModifica, #divImpegnoCaricato").removeClass("hide");
			gestisciSiopeTipoDebito();
		}
	});
	
}(jQuery));