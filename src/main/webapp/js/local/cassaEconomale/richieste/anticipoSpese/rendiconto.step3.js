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
    
    /**
     * Impostazione della tabella dei giustificativi.
     */
    function impostaTabellaGiustificativi() {
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessun giustificativo associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").addClass("hide");
            }
        };
        $("#tabellaGiustificativi").dataTable(opts);
    }

    $(function() {
        // Impostazione della paginazione della tabella dei giustificativi
        impostaTabellaGiustificativi();
        //AGGIUNTI IN DATA 01/09/2015 
        $("#pulsanteConfermaStampaRicevuta").substituteHandler("click", confermaStampaRicevuta);
        $("#pulsanteStampaRicevuta").substituteHandler("click", mostraModaleConfermaStampaRicevuta);
    });
}(jQuery);