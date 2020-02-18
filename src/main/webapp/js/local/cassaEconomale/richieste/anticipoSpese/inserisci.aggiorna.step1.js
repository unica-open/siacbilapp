/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var baseUrl = $("#HIDDEN_baseUrl").val();
    /**
     * Copia della richiesta economale.
     */
    function copiaRichiestaEconomale() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }

    /**
     * Svuota tutti i campi del form
     */
    function puliziaForm() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }
    
    /**
     * Visualizzazione degli importi di cassa.
     */
    function visualizzaImportiCassa() {
        var spinner = $("#SPINNER_pulsanteVisualizzaImporti").addClass("activated");
        $("#divImportiCassa").load(baseUrl +"_visualizzaImporti.do", {}, function() {
            spinner.removeClass("activated");
            $("#modaleImportiCassa").modal("show");
        });
    }
    
//    /**
//     * Visualizzazione degli importi di cassa.
//     */
//    function visualizzaImportiCassa() {
//      //  var spinner = $("#SPINNER_pulsanteVisualizzaImporti").addClass("activated");
//        var action = $(this).data("href");
//        $("#divImportiCassa").load(action, {}, function() {
//            spinner.removeClass("activated");
//            $("#modaleImportiCassa").modal("show");
//        });
//    }
    
    $(function() {
        $("#pulsanteCopiaRichiestaEconomale").click(copiaRichiestaEconomale);
        $("#pulsanteAnnullaStep1").click(puliziaForm);

        // Inizializzazione della gestione della matricola
        Matricola.inizializza("#matricolaSoggettoRichiestaEconomale", "#descrizioneMatricola", "#pulsanteCompilazioneGuidataMatricola", "#HIDDEN_codiceAmbito", "#HIDDEN_maySearchHR");
        $("#pulsanteVisualizzaImporti").substituteHandler("click", visualizzaImportiCassa);
      
    });

}(jQuery);