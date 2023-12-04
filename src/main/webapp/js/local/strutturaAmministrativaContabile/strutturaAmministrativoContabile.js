/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $){
	'use strict'
	
	/* 
	 * SIAC-8280
	 * 
	 * scorporo della SIAC-8237 
	 */
	var StrutturaAmministrativaContabile = {};
	
	function inizializzaZtreeStrutturaAmministrativaContabile(suffix, $hiddenUidField){
	    var spinner = $('#SPINNER_StrutturaAmministrativoContabile');

	    // Attiva lo spinner
	    spinner.addClass('activated');
	    //SIAC-8153 controllo le SAC per azione
		var oggettoPerChiamataAjax = {};
		if($('#nomeAzioneSAC').val() !== undefined && $('#nomeAzioneSAC').val() !== null){
			oggettoPerChiamataAjax['nomeAzioneDecentrata'] = $('#nomeAzioneSAC').val();
		}
		
	    $.postJSON('ajax/strutturaAmministrativoContabileAjax.do', oggettoPerChiamataAjax)
	    .then(function (data) {
	        var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
	        var zTree = new ZtreeSAC(suffix);
			// devo assicurarmi di avere solo strutture di RESPONSABILITA' a livello 1
	        zTree.inizializza(filtraCentriDiResponsabilita(listaStrutturaAmministrativoContabile));
	    })
	    .always(spinner.removeClass.bind(spinner, 'activated'));

		// Gestisco la possibilita' di deselezionare la struttura, sara' compito del backend gestire un eventuale errore
		$hiddenUidField.on("change", function(e){
			e && e.preventDefault();
			$this = $(this).value();
			if(!$this){
				$(this).value(0);
			}
		});
	}

	function filtraCentriDiResponsabilita(listaStrutturaAmministrativoContabile){
		var list = [];
		if(listaStrutturaAmministrativoContabile && listaStrutturaAmministrativoContabile.length > 0){
			for(var i = 0; i < listaStrutturaAmministrativoContabile.length; i++) {
				if(listaStrutturaAmministrativoContabile[i].codiceTipo === 'CDR'){
					list.push(listaStrutturaAmministrativoContabile[i]);
				}
			}
		}
		return list;
	}

	StrutturaAmministrativaContabile.inizializzaZtreeStrutturaAmministrativaContabile = inizializzaZtreeStrutturaAmministrativaContabile;
	
    global.StrutturaAmministrativaContabile = StrutturaAmministrativaContabile;
	
}(this, jQuery);