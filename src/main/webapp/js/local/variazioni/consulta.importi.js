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
    var uidVariazione = $('#uidVariazione').val();
    var annoCompetenza = $('#annoCompetenza').val();
    var numeroVariazione = $('#numeroVariazione').val();
    VariazioneImporti.leggiCapitoliNellaVariazione("consultaVariazioneImporti_leggiCapitoliNellaVariazione.do", {uidVariazione: uidVariazione});
    // SIAC-5016
    $("#pulsanteRicercaCapitoloNellaVariazione").substituteHandler("click", VariazioneImporti.cercaCapitoloNellaVariazione);
    $('#pulsanteEsportaDati').substituteHandler('click', Variazioni.exportResults.bind(undefined, [{name: 'uidVariazione', value: uidVariazione}, {name: 'annoCompetenza', value: annoCompetenza}, {name: 'numeroVariazione', value: numeroVariazione}], 'consultaVariazioneImporti_download.do', false));
    $('#pulsanteEsportaDatiXlsx').substituteHandler('click', Variazioni.exportResults.bind(undefined, [{name: 'uidVariazione', value: uidVariazione}, {name: 'annoCompetenza', value: annoCompetenza}, {name: 'numeroVariazione', value: numeroVariazione}], 'consultaVariazioneImporti_download.do', true));
});