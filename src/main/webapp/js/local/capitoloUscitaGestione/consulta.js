/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    'use strict';
    var listTypeComponent=[];
    var rows = $("tbody#componentiCompetenza").find("tr");
    


    function getDescriptions(){
        var rows = $("tbody#componentiCompetenza").find("tr");
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
        getRowSpan();
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

    

    function getRowSpan(){
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
                $(rows[i]).find('.componenti-competenza').attr("rowspan", "2");
                $(rows[i+1]).find('.componenti-competenza').remove();
                i+=2;
            }else if(key1!==key2){
                i+=1;
            }else{
                i+=1;
            }
            

        }
    }

    $(function() {

        

        
        
        Capitolo.delayCaricamentoTab('#tabVincoli', '#vincoli', Capitolo.caricaVincoli.bind(null, 'UscitaGestione'), Capitolo.postCaricaVincoli);
        Capitolo.caricaTabellaCruscottinoLike('#tabellaImpegniCollegati', 'CAPITOLOSPESA', 'IMPEGNO', ['input[name="filtroImpegno"]:checked']);
        Capitolo.caricaTabellaCruscottinoLike('#tabellaLiquidazioniCollegate', 'CAPITOLOSPESA', 'LIQUIDAZIONE', ['input[name="filtroLiquidazione"]:checked']);
        Capitolo.caricaTabellaCruscottinoLike('#tabellaOrdinativiCollegati', 'CAPITOLOSPESA', 'MANDATO', ['input[name="filtroOrdinativo"]:checked']);
        $('button[data-esportazione]').substituteHandler('click', Capitolo.esportaRisultatiCruscottinoLike.bind(undefined, 'CAPITOLOSPESA'));
        
        $('[data-reload-datatable]').each(function(idx, el) {
            Capitolo.handleReloadTabellaCruscottinoLike($(el));
        });

         //SIAC-6881

         $("#componentiCompetenza").hide();

         $("#competenzaTotale").click(function(){
             $("#componentiCompetenza").slideToggle();
         });

        //  $("#componentiCompetenza").hide();

        //  $("#competenzaTotale").click(function(){
        //      $("#componentiCompetenza").slideToggle();
        //  });
        
        /*$('table#idStanziamentiTabella').each(function() {
            $(this).find('tr').each(function() {
              $(this).find('td').each(function(i) {
                if($(this).html()==="" || $(this).html()=== "&nbsp;" ) $(this).css("background-color", "#f4f4f4");
              });
            });
          });*/

          getDescriptions();

        

        

    });
}(jQuery);