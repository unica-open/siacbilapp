/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Dismissione = function($,global) {
	'use strict';
	var exports = {};
	
	exports.ricaricaCausaliPerEvento = ricaricaCausaliPerEvento;
	
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
            opts += "<option value=''>" + (emptyOptionText || "") + "</option>";
        }
        options.forEach(function(el) {
            opts += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + "</option>";
        });
        select.html(opts);
    }
	
	
	function ricaricaCausaliPerEvento(e){
		var target = e && e.target;
		var selectEvento = $(target);
        var selectCausaleEP = $('#uidCausaleEPDismissione').overlay("show");
        var evento = selectEvento.val();
        var ambito = $("#ambito").val();
        var objToSend = {"evento.uid": evento, "ambito": ambito};
        // Chiamata AJAX
        $.postJSON("ricercaCausaleEPByEvento_ricercaModulare.do", objToSend)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                // Svuoto la select ed esco
                popolaSelect(selectCausaleEP, []);
                return;
            }

            popolaSelect(selectCausaleEP, data.listaCausaleEP, true,"");
        }).always(selectCausaleEP.overlay.bind(selectCausaleEP, "hide"));
	}
	
	return exports;
	
	
}(jQuery, this);

$(function() {
	// Inizializzazione delle funzionalita' della ricerca provvedimento
    Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
        "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
        "AttoAmministrativo");
    $("select[data-evento]").change(Dismissione.ricaricaCausaliPerEvento).change();
});