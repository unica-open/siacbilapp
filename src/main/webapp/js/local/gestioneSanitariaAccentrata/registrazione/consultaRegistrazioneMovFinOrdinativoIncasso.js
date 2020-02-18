/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {

    var optionsBase = {
        bServerSide : false,
        bPaginate : true,
        bLengthChange : false,
        iDisplayLength : 5,
        bSort : false,
        bInfo : true,
        bAutoWidth : true,
        bFilter : false,
        bProcessing : true,
        bDestroy : true,
        oLanguage : {
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty : "0 risultati",
            sProcessing : "Attendere prego...",
            oPaginate : {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec.",
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    function popolaTabellaSubOrdinativi(){
        var options = {
             oLanguage : {
                 sZeroRecords : "Non sono presenti subordinativi associati"
             }
         };
         var opts = $.extend(true, {}, optionsBase, options);
         $("#").dataTable(opts);
    }

    $(function() {
        popolaTabellaSubOrdinativi();
    });


}(jQuery, this);