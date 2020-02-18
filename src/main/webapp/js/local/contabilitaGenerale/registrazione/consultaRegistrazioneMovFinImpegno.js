/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {

    var optionsBase = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
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

    function popolaTabellaSubimpegni(listaSubimpegni){
    	var isOverlay = false;
    	var table =  $('#tabellaSubImpegni').overlay({rebind: true, usePosition: true, loader: true});
        var options = {
    		bServerSide: true,            
            sAjaxSource: "risultatiRicercaSubImpegniMovimentoGestioneAjax.do",
            sServerMethod: "POST",
            oLanguage : {
            	sZeroRecords : "Non sono presenti subimpegni associati"
            },
             fnPreDrawCallback: function() {
            	if(isOverlay) {
            		return;
            	}
            	table.overlay('show');
            	isOverlay = true;
            },
            fnDrawCallback: function() {
            	table.overlay('hide');
            	isOverlay = false;
            },
            aoColumnDefs: [
                           {aTargets: [0], mData: function(source) {
                               return source.domStringSubMovimento || "";
                           }},
                           {aTargets: [1], mData: function(source) {
                               return source.domStringStato || "";
                           }},
                           {aTargets: [2], mData: function(source) {
                               return source.domStringSoggetto || "";
                           }},
                           {aTargets: [3], mData: function(source) {
                               return source.domStringProvvedimento || "";
                           }},
                           {aTargets: [4], mData: function(source) {
                                   return source.domStringDataScadenza || "";
                           }},
                           {aTargets: [5], mData: function(source) {
                                   return source.domStringImporto || "";
                               },
                               fnCreatedCell: function(nTd) {
                                   $(nTd).addClass("tab_Right");
                               }
                           }
                       ]
         };
         var opts = $.extend(true, {}, optionsBase, options);
         table.dataTable(opts);
    }

    function popolaTabellaModifiche(listaModifiche){
        var options = {
             oLanguage : {
                 sZeroRecords : "Non sono presenti modifiche associate"
             }
         };
         var opts = $.extend(true, {}, optionsBase, options);
         $("#tabellaModifiche").dataTable(opts);
    }
    
    $(function() {
        popolaTabellaSubimpegni();
        popolaTabellaModifiche();
    });


}(jQuery, this);