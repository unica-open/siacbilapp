/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';
    var $overlay;
	
	/**
     * Inizializzazione delle SAC
     * @return (Promise) una promise legata al caricamento delle SAC
     */	   
	function inizializzaSAC() {
		$overlay = $('div.struttAmm div.accordion-heading').overlay({usePosition: true});
		$overlay.overlay('show');
        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do')
        .then(inizializzaSACCallback);
    }
    /**
     * Caricamento delle SAC come callback del servizio
     * @param data (any) la response del servizio
     */
    function inizializzaSACCallback(data) {
        var ztree = new Ztree('', 'treeStruttAmm');
        ztree.inizializza(data.listaElementoCodifica);
        $overlay.overlay('hide');
    }
    
    
    $(function() {
    	inizializzaSAC();
    });
    
})(jQuery);