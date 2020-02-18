/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    'use strict';
    
    $(init);
    
    function gestisciChangeTipoBene(){
        var inputContoPatrimoniale = $('#contoPatrimonialeCespiteTipoBene');
        var contoPatrimoniale = $('#cespiteTipoBene').find('option:selected').data('conto-patrimoniale') || "";
        inputContoPatrimoniale.val(contoPatrimoniale);
    }
    function submitToUrl(e) {
        var $el = $(e.target);
        var newUrl = $el.data('submitUrl');
        var $form = $('form').prop('action', newUrl);
        $form.submit();
    }

    function annulla(){
   	 var $form = $('form');
   	 $form.attr('action', 'inserisciCespite.do');
   	 $form.submit();
   }

    function init() {
        $('#cespiteTipoBene').substituteHandler('change', gestisciChangeTipoBene).change();
        $('button[data-submit-url]').substituteHandler('click', submitToUrl);       
        $('#pulsanteAnnulla').substituteHandler('click', annulla);
    }
}(jQuery);

