/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var msgImpostatoDalSistema = "Impostato dal sistema";
    var selectStatoOperativo = $("#statoOperativo");
    var fieldNumero = $("#numero");
    var fieldAnno = $("#anno");
    var fieldTipoAtto = $("#tipoAtti");
    var pulsanteSAC = $("#bottoneSAC");
    var spinnerSAC = $("#SPINNER_StrutturaAmministrativoContabile");
    
    // SIAC-5259: gestione della validazione del form come pre-submit
    global.preSubmit = validazioneForm;

    function isProgressivoAutomatico () {
        return fieldTipoAtto.find("option:selected").data("progressivoAutomatico") !== undefined;
    }

    /**
     * Gestione del change del tipoAtto
     */
    function gestioneChangeTipoAtto () {
        var progressivoAutomatico = isProgressivoAutomatico();
        if (progressivoAutomatico) {
            fieldNumero.attr("disabled", "disabled");
            fieldNumero.val(msgImpostatoDalSistema);
            selectStatoOperativo.val('DEFINITIVO');
            selectStatoOperativo.find("option[value='PROVVISORIO']").remove();
        } else {
            fieldNumero.removeAttr("disabled");
            if (fieldNumero.val().toUpperCase() === msgImpostatoDalSistema.toUpperCase()) {
                fieldNumero.val("");
            }
            if(selectStatoOperativo.find("option[value='PROVVISORIO']").length === 0) {
                selectStatoOperativo.find("option:first").after('<option value="PROVVISORIO">PROVVISORIO</option>');
            }
            selectStatoOperativo.val('PROVVISORIO');
        }
    }

    /**
     * Validazione del form
     */
    function validazioneForm(e) {
        var anno = fieldAnno.val();
        var numero = fieldNumero.val();
        var isTipoAttoProgressivoAutomatico = isProgressivoAutomatico();
        var errori = [];

        // Controllo per l'anno
        if (anno === "") {
            errori.push("Il campo Anno deve essere compilato");
        } else if (isNaN(anno)) {
            errori.push("Il campo Anno deve essere un numero");
        }
        // Controllo per il numero
        if(!isTipoAttoProgressivoAutomatico){
            if(numero === "") {
                errori.push("Il campo Numero deve essere compilato");
            } else if(isNaN(numero)){
                 errori.push("Il campo Numero deve essere un numero");
            }
        }
        // Controllo per il tipo di atto
        if (!fieldTipoAtto.val()) {
            errori.push("Il campo Tipo Atto deve essere compilato");
        }
        if (errori.length === 0) {
            return true;
        }
        
        // Reimposto il dato del numero provvedimento interno, qualora si sia in tale caso
        if(isTipoAttoProgressivoAutomatico) {
            fieldNumero.val('Impostato dal sistema.');
        }
        // Popolo l'alert degli errori
        alertErrori.children("ul").find("li").remove().end();
        $.each(errori, function(key, value) {
            alertErrori.children("ul").append($("<li/>").html(value));
        });
        alertErrori.slideDown();
        e.preventDefault();
        return false;
    }
    
    $(function() {
        fieldTipoAtto.change(gestioneChangeTipoAtto);
        //$("#editProvvedimento").submit(validazioneForm);

        // Se il tipoAtto e' con numerazione automatica
        if (isProgressivoAutomatico()) {
            fieldNumero.attr("disabled", "disabled");
            fieldNumero.val(msgImpostatoDalSistema);
            selectStatoOperativo.val('DEFINITIVO');
            selectStatoOperativo.find("option[value='PROVVISORIO']").remove();
        }

        // Ricerco le strutture amministrativo contabili
        pulsanteSAC.removeAttr("href");
        spinnerSAC.addClass("activated");

        $.postJSON("ajax/strutturaAmministrativoContabileAjax.do", {}, function (data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
            ZTree.imposta("treeStruttAmm", ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
            // Ripristina l'apertura del modale
            pulsanteSAC.attr("href", "#struttAmm");
        }).always(function() {
            spinnerSAC.removeClass("activated");
        });
    });
}(this, jQuery);