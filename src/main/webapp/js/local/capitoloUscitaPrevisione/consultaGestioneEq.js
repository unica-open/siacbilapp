/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    


    var listTypeComponentCG=[];
    var rowsCG = $("tbody#componentiCompetenzaConsGest").find("tr");
    function getDescriptionsCG(){
        var rows = $("tbody#componentiCompetenzaConsGest").find("tr");
        for (var index = 0; index < rows.length; index++) {
            var typeComponent = {};
            var tableCell = {};
            var item = rows[index];
            var description = ($(item).find('td:first-child').text());
            var type = ($(item).find('td:eq(1)').text());
            typeComponent[description.trim()]=type.trim();
            tableCell[item] = typeComponent;
            listTypeComponentCG.push(tableCell);
        }
        getRowSpanCG();
    }

    function getValuesFromObjectCG(item){
        var values;
        for(var k in item){
            values = item[k];
        }
        return values;
    }
    function getKeyFromObjectCG(item){
        var key;
        for(var k in item){
            key = k;
        }
        return key;
    }

    function getRowSpanCG(){
        var i=0;
        while(i<listTypeComponentCG.length){
            
            if(i==listTypeComponentCG.length){
                //set del rowspan a 1
                break;
            }
            
            //oggettoni contenenti i td
            var obj=listTypeComponentCG[i]; 
            var obj2 = listTypeComponentCG[i+1];
            //oggetti contententi descrizione - tipo
            var keyObj1=getKeyFromObjectCG(obj); //td
            var keyObj2=getKeyFromObjectCG(obj2) //td
            var valuesObj1 = getValuesFromObjectCG(obj); //{}
            var valuesObj2 = getValuesFromObjectCG(obj2);//{}
            
            //confronto le chiavi e i valori (descr - type)
            var key1 = getKeyFromObjectCG(valuesObj1) //fresco1
            var key2 = getKeyFromObjectCG(valuesObj2) //fresco1
            var value1 = getValuesFromObjectCG(valuesObj1);
            var value2 = getValuesFromObjectCG(valuesObj2);
            
            if(key1===key2 && value1!==value2){
                $(rowsCG[i]).find('.componenti-competenza').attr("rowspan", "2");
                $(rowsCG[i+1]).find('.componenti-competenza').remove();
                i+=2;
            }else if(key1!==key2){
                i+=1;
            }else{
                i+=1;
            }
            

        }
    }


    

    $(function() {

        
        
         $("#componentiCompetenzaConsGest").hide();

         $("#competenzaTotaleConsGest").click(function(){
             $("#componentiCompetenzaConsGest").slideToggle();
         });

         /*$("td").each(function(){
             if($(this).html()==="" || $(this).html()==="&nbsp;") $(this).css("background-color", "#f4f4f4");
                
         })*/

         //Per uniformare le descrizioni con rowspan settando il rowspan 2
         getDescriptionsCG();

    });
}(jQuery);