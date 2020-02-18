/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ************************************
 **** Funzioni di ricerca Variazioni ****
 ************************************
 */

/* Document Ready */
$(function() {
    var uid = $('#uidVariazione').val();
    var annoCompetenza = $('#annoCompetenza').val();
    var numeroVariazione = $('#numeroVariazione').val();
	VariazioneImporti.leggiCapitoliNellaVariazione("consultaVariazioneImportiUEB_leggiCapitoliNellaVariazione.do", {uidVariazione: $('#uidVariazione').val()});
	// SIAC-5016
    $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [{name: 'uidVariazione', value: $('#uidVariazione').val()}, {name: 'annoCompetenza', value: annoCompetenza}, {name: 'numeroVariazione', value: numeroVariazione}], 'consultaVariazioneImportiUEB_download.do', false));
    $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [{name: 'uidVariazione', value: $('#uidVariazione').val()}, {name: 'annoCompetenza', value: annoCompetenza}, {name: 'numeroVariazione', value: numeroVariazione}], 'consultaVariazioneImportiUEB_download.do', true));
    $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", VariazioneImporti.cercaCapitoloNellaVariazione);
});