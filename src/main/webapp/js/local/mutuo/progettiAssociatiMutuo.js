/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

$(function() {
	$('.idProgetto').on('click', function() {
		$('#eliminaAssociazione').prop('disabled', $('.idProgetto:checked').length == 0);
	});
});