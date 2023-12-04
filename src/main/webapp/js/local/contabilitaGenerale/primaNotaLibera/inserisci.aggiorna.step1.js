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
            //SIAC-8495 aggiungo l'attributo se già presente un vecchio valore
            $('#uidCausaleEP').data('old-value') === el.uid ?
                //  vecchio valore
                opts += "<option value='" + el.uid + "' selected >" + el.codice + " - " + el.descrizione + "</option>" :
                //  giro normale
                opts += "<option value='" + el.uid + "' >" + el.codice + " - " + el.descrizione + "</option>";
        });
        select.html(opts);
    }
    
    /**
     * SIAC-8495
     * @returns true se ci troviamo in inserimento o false se ci troviamo in aggiornamento
     */
    function determinaInserimentoAggiornamento(){
        return $('form[id^="form"]').attr('action').indexOf('/siacbilapp/inserisci') >= 0;
    }

    /**
     * Ricarica le causali per evento.
     */
    function ricaricaCausaliPerEvento(){
        var selectEvento = $("#uidEvento");
        var selectCausaleEP = $("#uidCausaleEP");
        var oldValueSelectCausaleEP = selectCausaleEP.val();
        var overlayElements = $("#uidCausaleEP, #ms-uidCausaleEP").overlay("show");
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

            popolaSelect(selectCausaleEP, data.listaCausaleEP, determinaInserimentoAggiornamento());
            selectCausaleEP.val(oldValueSelectCausaleEP);
        }).always(overlayElements.overlay.bind(overlayElements, "hide"));
    }

    $(function () {
        $("select[data-evento]").change(ricaricaCausaliPerEvento).change();
    });
}(jQuery);