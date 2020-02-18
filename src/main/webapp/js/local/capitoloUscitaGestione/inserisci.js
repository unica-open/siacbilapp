/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
***********************************************
**** Inserisci Capitolo Di Uscita Gestione ****
***********************************************
*/

/* Document ready */
!function($) {
    'use strict';

    $(function () {
        var caricamentoPianoDeiConti = $.Deferred().resolve();
        var $macroaggregato = $("#macroaggregato");
        /**Funzioni per il rowspan dei componenti */
        var listTypeComponent=[];
        var rows = $("tbody#componentiCompetenzaAgg").find("tr");
        
    
    
        function getDescriptions(){
            var rows = $("tbody#componentiCompetenzaAgg").find("tr");
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
        /******************* */
        $("#categoriaCapitolo").change();

        // Lego le azioni
        $("#missione").on("change", CapitoloUscita.caricaProgramma.bind(null, false));
        $("#programma").on("change", CapitoloUscita.caricaCofogTitolo.bind(null, false));
        $("#titoloSpesa").on("change", CapitoloUscita.caricaMacroaggregato);
        $macroaggregato.on("change", Capitolo.caricaPianoDeiConti.bind(null, $macroaggregato, false, true));
        $("#bottonePdC").on("click", Capitolo.controllaPdC);
        // CR SIAC-2559
//        $("#bottoneSIOPE").on("click", Capitolo.controllaSIOPE);
        if(!$("form[data-disabled-form]").length) {
            $("#ModalAltriDati").on("click", CapitoloUscita.validaForm);
        }

        // Carica lo zTree relativo alla Struttura Amministrativo Contabile
        Capitolo.caricaStrutturaAmministrativoContabile();

        // Controlla se il campo FPV sia stato selezionato. In caso contrario, disabilita l'editazione dei campi corrispondenti agli importi.
        if ($("#FPV").attr("checked") !== "checked") {
            $("#fpv0").attr("disabled", "disabled");
            $("#fpv1").attr("disabled", "disabled");
            $("#fpv2").attr("disabled", "disabled");
        }
        // Carica, eventualmente, lo zTree relativo all'Elemento del Piano dei Conti
        if ($macroaggregato.val()) {
            caricamentoPianoDeiConti = Capitolo.caricaPianoDeiConti($macroaggregato, false, true);
        }
        // Carica, eventualmente, lo zTree relativo al SIOPE
        // CR SIAC-2559
//        var elementoPdC = parseInt($("#HIDDEN_ElementoPianoDeiContiUid").val(), 10);
//        if (!isNaN(elementoPdC) && elementoPdC) {
//            caricamentoPianoDeiConti.done(Capitolo.caricaSIOPEDaHidden);
//        }
        // CR SIAC-2559
        $('#siopeSpesa').substituteHandler('change', Capitolo.gestioneSIOPEPuntuale.bind(null, '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa',
            '#HIDDEN_descrizioneSiopeSpesa', '', 'ricercaClassificatoreGerarchicoByCodice_siopeSpesa.do'));
        RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siopeSpesa', '#descrizioneSiopeSpesa', '#HIDDEN_idSiopeSpesa','#HIDDEN_descrizioneSiopeSpesa','#HIDDEN_SIOPECodiceTipoClassificatore');
        
        $("form").on("reset", Capitolo.puliziaReset);

        $("#componentiCompetenzaAgg").hide();

        $("#competenzaTotale").click(function(){
            $("#componentiCompetenzaAgg").slideToggle();
        });

        getDescriptions();






    });
}(jQuery);
