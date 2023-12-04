/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione

$(function() {

    var overlayHide = false;

	
	/*$("#carica-rate-da-file").change(function() {
		$("#carica-rate-da-file").clone().appendTo("#form-carica-rate-da-file");
		$("#form-carica-rate-da-file").submit();
	});
	*/
	$(".aggiungiRipartizione").click(function() {
		$(this).closest('form').prop('action', '/siacbilapp/ripartizioneMutuo_aggiungiRipartizione.do').submit();
	});
	
	$(".eliminaRipartizione").click(function() {
		$("input[name='idx']").val($(this).data("idx"));
		$(this).closest('form').prop('action', '/siacbilapp/ripartizioneMutuo_eliminaRipartizione.do').submit();
	});
/*
	$('.scaricaModelloExcel, .scaricaPianoCsv').click(function(){
		overlayHide = true;
	});
	
	$('body').on('overlayShow', function() {
		if (overlayHide) {
			$('body').overlay('hide');
		}
		
		overlayHide = false;
	});
	
	
	$(".importoRata").on('change', function(evData){
		
		var importoRataHidden = $(this).parents('td').find(".importoRataHidden");
		var change = new BigNumber(parseLocalNum($(this).val() || '0')).minus(importoRataHidden.val() || '0').round(2);
		importoRataHidden.val(new BigNumber(importoRataHidden.val() || '0').plus(change));
		
		var rowTr = $(this).parents('tr');
		
		var importoTotaleHidden = rowTr.find(".importoTotale.importoRataHidden");
		importoTotaleHidden.val(new BigNumber(importoTotaleHidden.val() || '0').plus(change));
		rowTr.find(".importoTotale.importoRata").val(formatMoney(importoTotaleHidden.val()));

		var totaleImportoTotale = $(".totale-importoTotale");
		totaleImportoTotale.html(formatMoney(new BigNumber(parseLocalNum(totaleImportoTotale.html())).plus(change)));
		var totaleImportoQuota = $(".totale-" + importoRataHidden.attr("name"));
		totaleImportoQuota.html(formatMoney(new BigNumber(parseLocalNum(totaleImportoQuota.html())).plus(change)));
	});
	
	$(".salva").click(function() {
		$("input[name='requestStatoMutuo']").val($(this).data("stato-mutuo"));
	});	
	
	$('form').submit(function() {
		var joinValues = function(obj) {
			return obj.toArray().map(x => x.value).join(':');
		}

		$('[name="dataScadenzaStr"]').val(joinValues($(".dataScadenza")));
		$('[name="importoTotaleStr"]').val(joinValues($(".importoTotale.importoRataHidden").prop('disabled', true)));
		$('[name="importoQuotaCapitaleStr"]').val(joinValues($(".importoQuotaCapitale.importoRataHidden").prop('disabled', true)));
		$('[name="importoQuotaInteressiStr"]').val(joinValues($(".importoQuotaInteressi.importoRataHidden").prop('disabled', true)));
		$('[name="importoQuotaOneriStr"]').val(joinValues($(".importoQuotaOneri.importoRataHidden").prop('disabled', true)));
	});
	*/
	
	Capitolo.inizializza('UscitaGestione', "#annoCapitolo", "#numeroCapitolo", "#numeroArticolo", "#numeroUEB", "#datiRiferimentoCapitoloSpan",
			"#pulsanteCompilazioneGuidataCapitolo", "");
});