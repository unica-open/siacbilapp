/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Document onReady
/* Document ready */
!function($) {
    'use strict';

    $(function () {

        var macroTipo=$("#macroTipo").val();
        
        
        var collegatoACapitoli=$("#editComponenteCapitolo_componenteCapitolo_collegatoACapitoli").val();
        
        $("#fonteFinanziamento").removeAttr("disabled");
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
        
        
        var disableDescrizione = function(){
            $("#descrizioneCapitolo").attr("disabled", 'disabled');
            $("#descrizioneCapitolo").css("background-color", "#f4f4f4");
            $('label[for="descrizioneCapitolo"]').html("Descrizione");
        }
        
        var disableAnno = function(){
            $("#annoCapitolo").attr("disabled", 'disabled');
            $("#annoCapitolo").css("background-color", "#f4f4f4");
            $('label[for="annoCapitolo"]').html("Anno");
        }
        
        
        var disablePrevisione = function(){
            $("#previsione").attr("disabled", 'disabled');
            $("#previsione").css("background-color", "#f4f4f4");
            $('label[for="previsione"]').html("Default Previsione");
        }
        //SIAC--7349
//        var disableGestione = function(){
//            $("#gestione").attr("disabled", 'disabled');
//            $("#gestione").css("background-color", "#f4f4f4");
//            $('label[for="gestione"]').html("Tipo Gestione");
//        }
                
        var disableImpegnabile = function(){
            $("#impegnabile").attr("disabled", 'disabled');
            $("#impegnabile").css("background-color", "#f4f4f4");
            $('label[for="impegnabile"]').html("Impegnabile");
        }
        
        
        var disablDataInizioValiditaCapitolo = function(){
            $("#dataInizioValiditaCapitolo").attr("disabled", 'disabled');
            $("#dataInizioValiditaCapitolo").css("background-color", "#f4f4f4");
            $('label[for="dataInizioValiditaCapitolo"]').html("Data Inizio Validit&agrave;");
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
        

  
        if (collegatoACapitoli  === "true"){
        	
        	$("#fonteFinanziamento").attr("disabled", 'disabled');
            $("#fonteFinanziamento").css("background-color", "#f4f4f4");
            
            $("#momento").attr("disabled", 'disabled');
            $("#momento").css("background-color", "#f4f4f4");
        	        
            $("#sottoTipo").attr("disabled", 'disabled');
            $("#sottoTipo").css("background-color", "#f4f4f4");
            
            $("#ambito").attr("disabled", 'disabled');
            $("#ambito").css("background-color", "#f4f4f4");
            
            
             disableDescrizione();
             disableAnno();
             disablePrevisione();
             //SIAC-7349
             //disableGestione();
             disableImpegnabile();
             disablDataInizioValiditaCapitolo();
        }else{
    		
        	$("#editComponenteCapitolo_componenteCapitolo_sottotipoComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_ambitoComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_momentoComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_descrizione").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_fonteFinanziariaComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_anno").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_propostaDefaultComponenteImportiCapitolo").attr("disabled", 'disabled');
        	//SIAC-7349
        	//$("#editComponenteCapitolo_componenteCapitolo_tipoGestioneComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_impegnabileComponenteImportiCapitolo").attr("disabled", 'disabled');
        	$("#editComponenteCapitolo_componenteCapitolo_dataInizioValidita").attr("disabled", 'disabled');
        	
        }
        
        

    });
}(jQuery);

