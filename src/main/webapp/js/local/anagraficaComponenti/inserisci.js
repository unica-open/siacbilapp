/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
***********************************************
**** Inserisci Componente Capitolo ****
***********************************************
*/

/* Document ready */
!function($) {
    'use strict';
    
    //SIAC-7246
    function setDefaultDataInizioValiditaCapitolo(){
        $('#dataInizioValiditaCapitolo').val('01/01/'+$('#HIDDEN_annoBil').val());
    }

    $(function () {

        //SIAC-7246
        setDefaultDataInizioValiditaCapitolo();

        var disabledFonteFinanziamento = function(){
            $("#fonteFinanziamento option").remove();
            $("#fonteFinanziamento").attr("disabled", 'disabled');
            $("#fonteFinanziamento").css("background-color", "#f4f4f4");
            $('label[for="fonteFinanziamento"]').html("Fonte di finanziamento");
        }

        var enableFonteFinanziamento = function(){
            $("#fonteFinanziamento").removeAttr("disabled");
            $("#fonteFinanziamento").css("background-color", "white");
            $('label[for="fonteFinanziamento"]').html("Fonte di finanziamento");
        }

        var disabledSottoTipo = function(){
            $("#sottoTipo option").remove();
            $("#sottoTipo").attr("disabled", 'disabled');
            $("#sottoTipo").css("background-color", "#f4f4f4");
            $('label[for="sottoTipo"]').html("Sottotipo");
        }

        var enableSottoTipo = function(){
            $("#sottoTipo").removeAttr("disabled");
            $("#sottoTipo").css("background-color", "white");
            $('label[for="sottoTipo"]').html("Sottotipo *");
        }

        var disabledMomento = function(){
            $("#momento option").remove();
            $("#momento").attr("disabled", 'disabled');
            $("#momento").css("background-color", "#f4f4f4");
            $('label[for="momento"]').html("Momento");
        
        }

        var enableMomento = function(){
            $("#momento").removeAttr("disabled");
            $("#momento").css("background-color", "white");
            $('label[for="momento"]').html("Momento *");
        }

        var disableAmbito = function(){
            $("#ambito option").remove();
            $("#ambito").attr("disabled", 'disabled');
            $("#ambito").css("background-color", "#f4f4f4");
            $('label[for="ambito"]').html("Ambito");
        }

        var enableAmbito = function(){
            $("#ambito").removeAttr("disabled");
            $("#ambito").css("background-color", "white");
            $('label[for="ambito"]').html("Ambito *");
        }

        var changeRequiredLabel = function(macroTipo){
            if(macroTipo==="FPV"){
                enableFonteFinanziamento();
                enableSottoTipo();
                enableMomento();  
                disableAmbito();
                
                $('label[for="annoCapitolo"]').html("Anno");
            
            }else if(macroTipo==="AVANZO"){
                disabledMomento();         
                enableFonteFinanziamento();
                disabledSottoTipo();
                disableAmbito();          
            $('label[for="annoCapitolo"]').html("Anno");
            }else if(macroTipo==="FRESCO"){
                disabledFonteFinanziamento();
                disabledMomento();         
                disabledSottoTipo();         
                enableAmbito();
                $('label[for="annoCapitolo"]').html("Anno");
            }else{
                disabledFonteFinanziamento();
                disabledMomento();         
                disabledSottoTipo();
                disableAmbito();
                $('label[for="annoCapitolo"]').html("Anno");
            }

        }

        // Lego le azioni
        $("#macroTipo").on("change", ComponenteCapitolo.caricaSottoTipo).change();        
        $("#macroTipo").on("change", function(){
            var macroTipo=$(this).val();
            changeRequiredLabel(macroTipo);
            
        }).change(); 
    });
}(jQuery);
