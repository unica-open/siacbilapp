/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");

    /**
     * Popolamento della select.
     *
     * @param select          (jQuery)  la select da popolare
     * @param options         (Array)   l'array delle opzioni
     * @param addEmptyOption  (boolean) inserisce l'elemento vuoto (opzionale, default = false)
     * @param emptyOptionText (string)  inserisce il testo per l'elemento vuoto (opzionale, default = "")
     */
    function popolaSelect(select, options, /*opzionale*/ addEmptyOption, /*opzionale*/ emptyOptionText) {
        var opts = "";
        if (!!addEmptyOption) {
            opts += "<option>" + (emptyOptionText || "") + "</option>";
        }
        options.forEach(function(el) {
            opts += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + "</option>";
        });
        select.html(opts);
    }

    /**
     * Ricarica le causali per evento.
     */
    function ricaricaCausaliPerEvento(){
        var selectEvento = $("#uidEvento");
        var selectCausaleEP = $("#uidCausaleEP").overlay("show");
        var oldValueSelectCausaleEP = selectCausaleEP.val();
        var evento = selectEvento.val();
        var ambito = $("#ambito").val();
        var objToSend = {"evento.uid": evento, "ambito": ambito};
        // Chiamata AJAX
        $.postJSON("ricercaCausaleEPByEvento_ricercaModulare.do", objToSend)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectCausaleEP, []);
                selectCausaleEP.val(oldValueSelectCausaleEP);
                return;
            }

            popolaSelect(selectCausaleEP, data.listaCausaleEP, true);
            selectCausaleEP.val(oldValueSelectCausaleEP);
        }).always(function() {
            // Nasconfdo gli overlay
            selectCausaleEP.overlay("hide");
        });
    }

    $(function () {
        $("select[data-evento]").change(ricaricaCausaliPerEvento).change();
    });
}(jQuery);