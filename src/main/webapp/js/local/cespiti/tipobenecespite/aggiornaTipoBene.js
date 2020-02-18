/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var TipoBene = function($,global) {
	"use strict";
	var exports = {};
	var arrayConti = ['Patrimoniale','Ammortamento', 'FondoAmmortamento', 'Plusvalenza', 'MinusValenza', 'Incremento', 'Decremento', 'Alienazione', 'Donazione'];
	var arrayOggettiConto = [];
	var alertErrori = $('#ERRORI');
	
	exports.inizializzaCompilazioniGuidateConti = inizializzaCompilazioniGuidateConti;
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
	
	
	function inizializzaCompilazioniGuidateConti(){		
		arrayConti.forEach(function(value){
			var obj = ContoBase.inizializza('', '', '#codiceConto' + value, '#descrizioneConto' + value, '#pulsanteCompilazioneGuidataConto' + value);
			arrayOggettiConto.push(obj);
		});		
	}
	
	function ricaricaCausaliPerEvento(e){
		var target = e && e.target;
		var selectEvento = $(target);
        var selectCausaleEP = selectEvento.parent().parent().parent().find('select[name^="tipoBeneCespite.causale"]').overlay("show");
        var evento = selectEvento.val();
        var ambito = $("#ambitoCausaleInventario").val();
        var objToSend = {"evento.uid": evento, "ambito": ambito};
        var oldValue = selectCausaleEP.val();
        // Chiamata AJAX
        $.postJSON("ricercaCausaleEPByEvento_ricercaModulare.do", objToSend)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectCausaleEP, []);
                return;
            }

            popolaSelect(selectCausaleEP, data.listaCausaleEP, true,"");
            selectCausaleEP.val(oldValue);
        }).always(selectCausaleEP.overlay.bind(selectCausaleEP, "hide"));
	}
	
	return exports;

	
}(jQuery, this);

$(function() {
	TipoBene.inizializzaCompilazioniGuidateConti();
	$("select[data-evento]").change(TipoBene.ricaricaCausaliPerEvento).change();
	
});