/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

$(function() {

	$('.dettaglio').on('click', function(){

		var tr = $(this).closest('tr');
		var movId = tr.find('.idMovimentoGestione').val();
		$('#annoMovimentoGestioneDettaglio').val(tr.find('.annoMovimentoGestione').html());
		$('#numeroMovimentoGestioneDettaglio').val(tr.find('.numeroMovimentoGestione').html());
		
		$("#tabellaDettaglioMovimentoGestioneMutuo tbody").hide();
        $("#tabellaDettaglioMovimentoGestioneMutuo").dataTable(
    		{
                "bServerSide" : true,
                "sAjaxSource" : "dettaglioImpegnoMutuoAjax.do",
                "bProcessing" : true,
                "sServerMethod" : "POST",
                "fnServerParams": function (aoData) {
                    aoData.push({ name: "impegno.uid", value: movId });
                },
                "bPaginate" : false,
                "bLengthChange" : false,
                "bSort" : false,
                "bInfo" : false,
                "bDestroy" : true,
                "bAutoWidth" : true,
                "bFilter" : false,
                "oLanguage" : {
                    "sProcessing" : "Attendere prego..."
                 },
                "fnDrawCallback" : function () { 		$("#tabellaDettaglioMovimentoGestioneMutuo tbody").show(); },
                "aoColumnDefs" : [
                    {"aTargets" : [ 0 ], "mData" : "annoBilancio"},
                    {"aTargets" : [ 1 ], "mData" : "importoIniziale"},
                    {"aTargets" : [ 2 ], "mData" : "importoAttuale"},
                    {"aTargets" : [ 3 ], "mData" : "importoLiquidato"},
                ]
            }
		);
	});
	
	$('.dettaglioAccertamento').on('click', function(){
		
		var tr = $(this).closest('tr');
		var movId = tr.find('.idMovimentoGestione').val();
		$('#annoAccertamentoDettaglio').val(tr.find('.annoMovimentoGestione').html());
		$('#numeroAccertamentoDettaglio').val(tr.find('.numeroMovimentoGestione').html());
		
		$("#tabellaDettaglioAccertamentoMutuo tbody").hide();
		$("#tabellaDettaglioAccertamentoMutuo").dataTable(
				{
					"bServerSide" : true,
					"sAjaxSource" : "dettaglioAccertamentoMutuoAjax.do",
					"bProcessing" : true,
					"sServerMethod" : "POST",
					"fnServerParams": function (aoData) {
						aoData.push({ name: "accertamento.uid", value: movId });
					},
					"bPaginate" : false,
					"bLengthChange" : false,
					"bSort" : false,
					"bInfo" : false,
					"bDestroy" : true,
					"bAutoWidth" : true,
					"bFilter" : false,
					"oLanguage" : {
						"sProcessing" : "Attendere prego..."
					},
					"fnDrawCallback" : function () { 		$("#tabellaDettaglioAccertamentoMutuo tbody").show(); },
					"aoColumnDefs" : [
						{"aTargets" : [ 0 ], "mData" : "annoBilancio"},
						{"aTargets" : [ 1 ], "mData" : "importoIniziale"},
						{"aTargets" : [ 2 ], "mData" : "importoAttuale"},
						{"aTargets" : [ 3 ], "mData" : "importoIncassato"},
						]
				}
		);
	});
	
	
	$('.idMovimentoGestione').on('click', function() {
		$('#eliminaAssociazione').prop('disabled', $('.idMovimentoGestione:checked').length == 0);
	});
});