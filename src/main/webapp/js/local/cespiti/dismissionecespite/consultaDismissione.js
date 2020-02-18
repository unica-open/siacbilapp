/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
	'use strict';
	
	function caricaCespitiCollegati(){
		var $fieldset = $('#fieldsetTabellaCespiti').overlay('show');
		var uidDismissione = $('#uidDismissioneCespite').val();
		return $.postJSON('consultaDismissioneCespite_ottieniCespitiCollegati.do',{'uidDismissioneCespite': uidDismissione})
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori,$('#ERRORI'))){
				return;
			}
			CespitiCollegatiDismissione.impostaTabella();
		}).always($fieldset.overlay.bind($fieldset, 'hide'));
	}
	
	$(function() {
		caricaCespitiCollegati();
	});
	
}(jQuery, this);

