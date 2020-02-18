/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
 function annulla(){
	 var $form = $('form');
	 $form.attr('action', 'aggiornaCategoriaCespiti_ricaricaDaAnnulla.do');
	 $form.submit();
 }

$(function() {
	
	$('#pulsanteAnnulla').substituteHandler('click', annulla);
/*	
	$("form").substituteHandler("reset", function() {
        document.location = 'url';
    });
*/
});