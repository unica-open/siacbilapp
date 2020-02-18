/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';
    var idx = 0;
    var $table = $("#tabellaDettaglioVariazioni").overlay({rebind: true, loader: true, usePosition: true});
    var isOverlayIn = false;
    var replaceRegex = /%%CURRENT_IDX%%/g;
    //GESC014
    var listTypeComponent=[];
    var gRows="";

    var setRows = function(row){
        gRows=row;
    }

    var getDescriptions = function(rows){
        setRows(rows);
        for (var index = 0; index < rows.length; index++) {
            var typeComponent = {};
            var tableCell = {};
            var item = rows[index];
            var description = ($(item).find('td:first-child').text());
            var type = ($(item).find('td:eq(1)').text());
            typeComponent[description.trim()]=type.trim();
            tableCell[item] = typeComponent;
            listTypeComponent.push(tableCell);
        }
    }
    function getValuesFromObject(item){
        var values;
        for(var k in item){
            values = item[k];
        }
        return values;
    }
    function getKeyFromObject(item){
        var key;
        for(var k in item){
            key = k;
        }
        return key;
    }

    var getRowSpan = function(){
        var i=0;
        while(i<listTypeComponent.length){
            
            if(i==listTypeComponent.length){
                //set del rowspan a 1
                break;
            }
            
            //oggettoni contenenti i td
            var obj=listTypeComponent[i]; 
            var obj2 = listTypeComponent[i+1];
            //oggetti contententi descrizione - tipo
            var keyObj1=getKeyFromObject(obj); //td
            var keyObj2=getKeyFromObject(obj2) //td
            var valuesObj1 = getValuesFromObject(obj); //{}
            var valuesObj2 = getValuesFromObject(obj2);//{}
            
            //confronto le chiavi e i valori (descr - type)
            var key1 = getKeyFromObject(valuesObj1) //fresco1
            var key2 = getKeyFromObject(valuesObj2) //fresco1
            var value1 = getValuesFromObject(valuesObj1);
            var value2 = getValuesFromObject(valuesObj2);
            
            if(key1===key2 && value1!==value2){
                $(gRows[i]).find('.componenti-competenza-dett').attr("rowspan", "2");
                $(gRows[i+1]).find('.componenti-competenza-dett').remove();
                i+=2;
            }else if(key1!==key2){
                i+=1;
            }
            
        }
    }

    function initializeDataTable() {
        var opts = {
            bPaginate: true,
            bLengthChange: false,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bServerSide: true,
            sAjaxSource: 'dettaglioVariazioniCapitoloAjax.do',
            sServerMethod: "POST",
            iDisplayLength: 5,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: $('#testoZeroVariazioni').val(),
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function(opts) {
                defaultPreDraw(opts);
                if(!isOverlayIn){
                    $table.overlay('show');
                    isOverlayIn = true;
                }
            },
            fnDrawCallback: function(opts) {
                defaultDrawCallback(opts);
                $table.overlay('hide');
                isOverlayIn = false;
                //toggle componenti competenza
                $(".componentiCompetenzaDettVar").hide();
                $(".competenzaTotaleVariazione").click(function(e){
                    e.preventDefault();
                    $(".componentiCompetenzaDettVar", $(this).closest('table')).toggle();
                });
            },
            aoColumnDefs: [
                {aTargets: [0], mData: computeAccordion}
            ]
        };
        $table.dataTable(opts);
    }
    
    function computeAccordion(source) {
        return source.accordionHtml ? source.accordionHtml.replace(replaceRegex, idx++) : '';
    }

    $(function() {
        initializeDataTable();
        
    });
}(jQuery));
