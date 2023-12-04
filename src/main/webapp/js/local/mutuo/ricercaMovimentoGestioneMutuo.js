/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
	
    var tipoMovimentoClick = function(){
    	var tipoMovimentoEntrataChecked = $('.tipoMovimentoEntrata').is(':checked');
		$('#tipoComponente').prop('disabled', tipoMovimentoEntrataChecked);
		$('.componenteCapitolo').toggle(! tipoMovimentoEntrataChecked);
		
		Capitolo.inizializza(tipoMovimentoEntrataChecked ? 'EntrataGestione' :'UscitaGestione', "#annoCapitolo", "#numeroCapitolo", "#numeroArticolo", "#numeroUEB", "#datiRiferimentoCapitoloSpan",
				"#pulsanteCompilazioneGuidataCapitolo", "");
	};
    
    $('.tipoMovimento').click(tipoMovimentoClick);
	
	
	tipoMovimentoClick();
     
 }(jQuery);