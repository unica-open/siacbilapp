/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var modaleConferma = $("#modaleConfermaStampaRicevuta");
    /*
     * mostro la modale di conferma stampa
     */
    function mostraModaleConfermaStampaRicevuta(){

         modaleConferma.modal("show");
    }

    /**
     * tasto prosegui ---->stampo la ricevuta
     */
    function confermaStampaRicevuta(){
        var form = $("#formRichiestaEconomale");

        form.submit();
        modaleConferma.modal('hide');

    }
    $(function() {

          $("#pulsanteConfermaStampaRicevuta").substituteHandler("click", confermaStampaRicevuta);
          $("#pulsanteStampaRicevuta").substituteHandler("click", mostraModaleConfermaStampaRicevuta);
    });

}(jQuery);