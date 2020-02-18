/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");

    /**
     * Caricamento dei dati dell'ultima stampa definitiva
     */
    function caricaUltimaStampaDefinitiva() {
        var uidCassaEconomale = +$('#cassaEconomale').val();
        var fieldsToOverlay;

        if(!uidCassaEconomale) {
            return;
        }
        fieldsToOverlay = $('#periodoDataInizioRendiconto, #periodoDataFineRendiconto, #numeroRendicontoDisabled, #ultimoNumeroRendiconto')
            .overlay('show');

        $.postJSON('cassaEconomaleStampe_caricaUltimaDefinitivaRendiconto.do', {uidCassaEconomale: uidCassaEconomale})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)){
                return;
            }
            popolaDatiStampaRendiconto(data.stampaRendiconto);
        })
        .always(fieldsToOverlay.overlay.bind(fieldsToOverlay, 'hide'));
    }
    /**
     * Popola i campi con il stampaRendiconto nuovo
     * @param stampaRendiconto (any) la stampa
     */
    function popolaDatiStampaRendiconto(stampaRendiconto){
        var periodoInizioRendiconto = formatDate(stampaRendiconto.periodoDataInizio);
        var periodoFineRendiconto = formatDate(stampaRendiconto.periodoDataFine);
        var numeroRendiconto = stampaRendiconto.numeroRendiconto || 0;
        var numeroRendicontoPiuUno = numeroRendiconto + 1;

        $("#periodoDataInizioRendiconto").val(periodoInizioRendiconto);
        $("#periodoDataFineRendiconto").val(periodoFineRendiconto);
        $("#ultimoNumeroRendiconto").val(numeroRendiconto);
        $("#numeroRendiconto").val(numeroRendicontoPiuUno);
        $('#formStampaRendicontoCassa_numeroRendiconto').val(numeroRendicontoPiuUno);
        $("#numeroRendicontoDisabled").val(numeroRendicontoPiuUno);
    }

    $(function() {
        caricaUltimaStampaDefinitiva();
        $("#cassaEconomale").substituteHandler("change", caricaUltimaStampaDefinitiva);
    });

}(jQuery);