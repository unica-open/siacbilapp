/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {
//	$('.checklist > label').each(function() {
//		$('span', this).html($('input', this).val());
//	});
	
	chiudi();
	
	$('#entrataVincolataFalse').click(chiudi);
	$('#entrataVincolataNon').click(chiudi);
	$('#entrataVincolataTrue').click(apri);
	
	function apri() {
		$('#accertamento').closest('div.checklist').show();
		$('#incasso').closest('div.checklist').show()
		}
	function chiudi() {
		$('#accertamento').closest('div.checklist').hide();
		$('#incasso').closest('div.checklist').hide()
	}
});

