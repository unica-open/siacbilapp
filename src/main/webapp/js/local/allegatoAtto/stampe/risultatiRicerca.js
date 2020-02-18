/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * 
 */
!function($) {
    // Per lo sviluppo
    "use strict";
  
    /**
     * Popola la tabella.
     */
    function gestioneTabella() {
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaStampaAllegatoAttoAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: defaultPreDraw,
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: defaultDrawCallbackRisultatiRicerca('#id_num_result'),
            aoColumnDefs: [
                {aTargets: [0], mData: "tipoStampa"},
                {aTargets: [1], mData: "stringaAttoAmministrativo"},
                {aTargets: [2], mData: "versione"},
                {aTargets: [3], mData: "dataCreazione"},
                {aTargets: [4], mData: "azioni"} 
            ]
        };
        $("#tabellaRisultatiRicercaStampaAllegatoAtto").dataTable(opts);
    }
    
    $(function() {
        gestioneTabella();
    });
    
    
}(jQuery);