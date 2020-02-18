/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
	"use strict";
	
	function gestisciChangeTipoBene(){
		var inputContoPatrimoniale = $('#contoPatrimonialeCespiteTipoBene');
		var contoPatrimoniale = $('#cespiteTipoBene').find('option:selected').data('conto-patrimoniale') || "";
		inputContoPatrimoniale.val(contoPatrimoniale);
	}
	
	$(function(){
		$('#cespiteTipoBene').substituteHandler('change', gestisciChangeTipoBene).trigger('change');
	});

}(jQuery);


