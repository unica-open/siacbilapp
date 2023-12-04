/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

$(function() {
  
  $('#mutuo').addClass('active');
  
  
  $("#tabellaRateMutuoConsulta td").filter(function() {
	    return $(this).text().trim() === '-0,00';
	}).text('0,00');

  
});