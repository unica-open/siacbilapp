/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    	
	function apriModaleAttoAmministrativo() {
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm_modale");

        $("#annoProvvedimento_modale").val($("#annoAttoAmministrativo").val());
        $("#numeroProvvedimento_modale").val($("#numeroAttoAmministrativo").val());
        $("#tipoAttoProvvedimento_modale").val($("#tipoAtto").val());

        ZTreePreDocumento.selezionaNodoSeApplicabile(tree.innerZTree, $("#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid").val());
        $("#modaleGuidaProvvedimento").modal("show");
    }
	
	function sommaImportiTassoInteresse() {
		var tassoInteresseEuribor = new BigNumber(parseLocalNum($("#tassoInteresseEuribor").val()) || NaN);
		var tassoInteresseSpread = new BigNumber(parseLocalNum($("#tassoInteresseSpread").val()) || NaN);
		var tassoInteresse = tassoInteresseSpread.add(tassoInteresseEuribor);

		$("#tassoInteresse").val(tassoInteresse.isNaN() ? '' : formatDecimal(tassoInteresse));
	}
	
	function calcAnnoFine() {
		var durataAnni = $("#durataAnni").val();
		var scadenzaPrimaRata = $("#scadenzaPrimaRata").val();
		var numeroMesiPeriodo = $("#listaPeriodoMutuo > option:selected").data("numero-mesi");
		
		if (durataAnni === '' || scadenzaPrimaRata == '' | !numeroMesiPeriodo) {
			return '';
		}
		
		var d = parseDate(scadenzaPrimaRata);
		
		d.setMonth(d.getMonth() + durataAnni * 12 - numeroMesiPeriodo);
		
		$("#annoFine").val(d.getFullYear());
	}

	function resetValTassoInteresse() {
		$("#tassoInteresseEuribor, #tassoInteresseSpread, #tassoInteresse").val("");
	}
	
	function onChangeTipoTasso(isV) {
		$("#tassoInteresse").prop("disabled", isV);
		$("#tassoInteresseSpreadLabel, #tassoInteresseEuriborLabel, #tassoInteresseEuribor, #tassoInteresseSpread").toggle(isV);
	}

	function onChangeScadenzaPrimaRata() {
		var statoMutuo = $('#statoMutuo').val();
		
		if (statoMutuo !== undefined && statoMutuo !== 'Bozza') {
			return;
		}
		
		var annoInizioObj = $('#annoInizio');
		var scadenzaPrimaRata = $("#scadenzaPrimaRata").val() || '';
		
		annoInizioObj.find('> option').not('[value=""]').remove();
		annoInizioObj.find('> option').prop('selected', true);

		if (scadenzaPrimaRata === '') {
			return;
		}
		
		var anno = scadenzaPrimaRata.substring(6);
		
		[parseInt(anno)-1, anno].forEach(a => $('#annoInizio').append('<option value="'+a+'">'+a+'</option>'));
		
		calcAnnoFine();
	}

	$(function() {
	    Soggetto.bindCaricaDettaglioSoggetto(Soggetto.inizializza("#codiceSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_soggettoDenominazione", 
			"#descrizioneCompletaSoggetto", "#pulsanteApriModaleSoggetto"), true);
	
		$("#tipoTassoV").change(function(){		  
			onChangeTipoTasso(true);
			resetValTassoInteresse();
		});
		
		$("#tipoTassoF").change(function(){		  
			onChangeTipoTasso(false);
			resetValTassoInteresse();
		});
			
		$("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);
		Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
		    "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
		    "AttoAmministrativo");
		
		$("#sommaMutuataIniziale")
			.keyup(function(){
				var importo = $("#sommaMutuataIniziale").val();
				$("#debitoResiduo").val(importo);
			})
			.blur(function() {
				$("#debitoResiduo").trigger('blur');
			});
		 
		$("#tassoInteresseEuribor, #tassoInteresseSpread").change(sommaImportiTassoInteresse);
		 
		if (! $("#tipoTassoV, #tipoTassoF").is(':checked')) {
			$("#tassoInteresse").prop("disabled", true);
			$("#tassoInteresseSpreadLabel, #tassoInteresseEuriborLabel, #tassoInteresseEuribor, #tassoInteresseSpread").hide();
		}
    
		onChangeTipoTasso($("#tipoTassoV").is(':checked'));

		if ($("#tipoTassoV").is(':checked')) {
			sommaImportiTassoInteresse();
		}

		var periodoRimborsoUid = $("#periodoRimborsoUid").data('periodo-rimborso-uid');
		$("#listaPeriodoMutuo > option").each(function(){	
			var split = $(this).val().split(":");
			$(this)
				.val(split[0])
				.data('numero-mesi', split[1])
				.prop('selected', periodoRimborsoUid == split[0]);
		});
		
		$("#listaPeriodoMutuo, #durataAnni").change(calcAnnoFine);

		$("#scadenzaPrimaRata").on('changeDate', onChangeScadenzaPrimaRata);
		onChangeScadenzaPrimaRata();
		$('#annoInizio > option[value="'+$('#annoInizio').data('selected')+'"]').prop('selected', true);
		
		if ($('.pianoAmmortamento').prop('disabled')) {
			$('.pianoAmmortamento').attr('title', 'Prima rata scaduta, piano ammortamento non abilitato');
		}
	});
	
 }(jQuery);