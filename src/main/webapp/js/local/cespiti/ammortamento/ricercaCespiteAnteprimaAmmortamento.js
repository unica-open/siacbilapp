/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var CespiteDettaglioanteprima = function($,global) {
	
	var exports = {};
	var $alertErrori = $('#ERRORI');
	exports.ricaricaTipoBeneByCausale = ricaricaTipoBeneByCausale;
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
	
	function ricaricaTipoBeneByCausale(e){
		var target = e && e.target;
		var selectCategoria = $(target);
	    var selectTipoBene = $('#cespiteTipoBene').overlay("show");
	    var categoriaUid = selectCategoria.val();
	    var objToSend = {"categoriaCespiti.uid": categoriaUid};
	    return $.postJSON('ricercaCespiteAnteprimaAmmortamento_caricaListaTipoBeneFiltrato.do',qualify(objToSend))
		.then(function(data){
			if(impostaDatiNegliAlert(data.errori, $alertErrori)){
				return;
			}
			popolaSelect(selectTipoBene,data.listaTipoBeneFiltrata, true);
		}).always(selectTipoBene.overlay.bind(selectTipoBene, 'hide'));
	}
	
	return exports;
}(jQuery, this);
$(function() {
   $('#categoriaCespiti').substituteHandler('change', CespiteDettaglioanteprima.ricaricaTipoBeneByCausale);
});