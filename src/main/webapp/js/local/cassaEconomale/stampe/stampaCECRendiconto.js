/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var modaleConferma = $("#modaleConfermaStampaRendicontoCassa");

    /*
     * mostro la modale di conferma stampa
     */
    function mostraModaleConfermaStampa() {
        modaleConferma.modal("show");
    }
    function caricaUltimaStampaDefinitiva() {
        var uidCassaEconomale = parseInt($("#cassaEconomale").val(), 10);

        if(!uidCassaEconomale) {
            return;
        }

        $("#periodoDataInizioRendiconto").overlay("show");
        $("#periodoDataFineRendiconto").overlay("show");
        $("#numeroRendiconto").overlay("show");
        $("#numeroRendiconto_1").overlay("show");
        $.postJSON("cassaEconomaleStampe_caricaUltimaDefinitivaRendiconto.do",
                {uidCassaEconomale : uidCassaEconomale},
                function(data) {
                    if(impostaDatiNegliAlert(data.errori, alertErrori)){
                        return;
                    }
                    popolaDatiStampaRendiconto(data.stampaRendiconto);


                });
    }
    /**
     * popola i campi con il stampaRendiconto nuovo
     */
    function popolaDatiStampaRendiconto(stampaRendiconto){


        var periodoInizioRendiconto= formatDate(stampaRendiconto.periodoDataInizio);
        var periodoFineRendiconto= formatDate(stampaRendiconto.periodoDataFine);
        var numeroRendiconto= stampaRendiconto.numeroRendiconto || 0;
        var rendiconto_= parseInt(stampaRendiconto.numeroRendiconto || 0, 10)+1;
        var numeroRendiconto_1=rendiconto_ || 0;


        $("#periodoDataInizioRendiconto").val(periodoInizioRendiconto);
        $("#periodoDataFineRendiconto").val(periodoFineRendiconto);
        $("#numeroRendiconto").val(numeroRendiconto);
        $("#numeroRendiconto_1").val(numeroRendiconto_1);

        $("#periodoDataInizioRendiconto").overlay("hide");
        $("#periodoDataFineRendiconto").overlay("hide");
        $("#numeroRendiconto").overlay("hide");
        $("#numeroRendiconto_1").overlay("hide");

    }
    /**
     * tasto prosegui ---->stampo la ricevuta
     */
    function confermaStampaRendicontoCassa() {
        var form = $("#formStampaRendicontoCassa");

        form.submit();
        modaleConferma.modal('hide');
    }

    $(function() {
        caricaUltimaStampaDefinitiva();
        $("#cassaEconomale").substituteHandler("change", caricaUltimaStampaDefinitiva);
        $("#pulsanteConfermaStampaRendicontoCassa").substituteHandler("click", confermaStampaRendicontoCassa);
        $("#pulsanteStampa").substituteHandler("click", mostraModaleConfermaStampa);
    });

}(jQuery);