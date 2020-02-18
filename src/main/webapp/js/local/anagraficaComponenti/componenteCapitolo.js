/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ComponenteCapitolo = (function() {
    var exports = {};
/*
    /**
     * Validazione del form. <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare
     * a livello client l'inserimento dei dati all'interno del form.
     * 
     * @returns <code>true</code> qualora il form sia valido, ovvero tutti i
     *          dati obbligator&icirc; siano stati inseriti; <code>false</code>
     *          altrimenti
     */
    exports.validaForm = function () {
        // Ottengo gli errori dei campi comuni
        var errori = ComponenteCapitolo.validaForm(true);

        // Definisco i campi specifici
        var macroTipo = $("#macroTipo").val();
        var sottoTipo = $("#sottoTipo").val();
       
        
        var tipoCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        
        var macroTipoFPVStandard = macroTipo === "FPV" ;
        
        var macroTipoFRESCOStandard = macroTipo === "FRESCO" ;
        
        var macroTipoAVANZOStandard = macroTipo === "AVANZO" ;

        
    };

    /* ***** Funzioni per le chiamate AJAX **** */

    /**
     * Carica i dati nelle select dei classificatori gerarchici da chiamata
     * AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
 
    
    
    function pulisciDisabilitaCampi(useDefaultTitoloSpesa){
        $("#annoCapitolo").val("").attr("disabled", "disabled");
       
        
        if(!useDefaultTitoloSpesa) {
            $("#momento").val("").attr("disabled", "disabled");
        }
    }
    /**
     * Carica i dati nella select del SottoTipo da chiamata AJAX.
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaSottoTipo = function () {
        /* Ottiene l'UID dalla select */

        var macroTipo = $("#macroTipo").val();
        var selects;
        if(macroTipo==="FPV"){  
            selects = $("#momento, #sottoTipo, #fonteFinanziamento").overlay("show");            
        }else if(macroTipo==="FRESCO"){            
            selects = $("#ambito").overlay("show");
        }else if(macroTipo==="AVANZO"){            
            selects = $("#fonteFinanziamento").overlay("show");
        }else{           
            selects = $("#momento, #sottoTipo, #fonteFinanziamento, #ambito");
        }


        
        return $.postJSON("inserisciComponenteCapitoloAction_caricaClassificatori.do", {"componenteCapitolo.macrotipoComponenteImportiCapitolo" : macroTipo})
        .then(function(data) {
            if(macroTipo==="FPV"){
                caricaSelectCodifiche($("#sottoTipo"), data.listaSottoTipo, undefined, '_name').change();
                caricaSelectCodifiche($("#momento"), data.listaMomento, undefined, '_name').change();
                //caricaSelectCodifiche($("#ambito"), data.listaAmbito, undefined, '_name').change();
                caricaSelectCodifiche($("#fonteFinanziamento"), data.listaFonteFinanziamento, undefined, '_name').change();

            }else if(macroTipo==="FRESCO"){
                caricaSelectCodifiche($("#ambito"), data.listaAmbito, undefined, '_name').change();
            }else if(macroTipo==="AVANZO"){
                caricaSelectCodifiche($("#fonteFinanziamento"), data.listaFonteFinanziamento, undefined, '_name').change();
            }
        }).always(selects.overlay.bind(selects, "hide"));
    };

 
    return exports;
}());