/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var TipoBene = function($,global) {
	"use strict";
	var exports = {};
	var arrayConti = ['Patrimoniale','Ammortamento', 'FondoAmmortamento', 'Plusvalenza', 'MinusValenza', 'Incremento', 'Decremento', 'Alienazione', 'Donazione'];
	var arrayOggettiConto = [];
	var alertErrori = $('#ERRORI');
	
	exports.inizializzaCompilazioniGuidateConti = inizializzaCompilazioniGuidateConti;
	
	function inizializzaCompilazioniGuidateConti(){	
		var obj = ContoBase.inizializza('', '', '#codiceContoPatrimoniale', '#descrizioneContoPatrimoniale', '#pulsanteCompilazioneGuidataContoPatrimoniale');
		
	}
	
	return exports;

	
}(jQuery, this);

$(function() {
	TipoBene.inizializzaCompilazioniGuidateConti();
	
});