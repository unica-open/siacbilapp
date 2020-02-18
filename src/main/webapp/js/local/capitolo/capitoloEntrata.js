/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Creo uno scope privato
var CapitoloEntrata = (function() {
    // Gli exports della funzione
    var exports = {};

    /**
     * Validazione del form.
     * <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare a livello client l'inserimento dei dati all'interno del form.
     *
     * @returns <code>true</code> qualora il form sia valido, ovvero tutti i dati obbligator&icirc; siano stati inseriti; <code>false</code> altrimenti
     */
    exports.validaForm = function () {
        // Ottengo gli errori dei campi comuni
        var errori = Capitolo.validaForm();

        // Definisco i campi specifici
        var titolo = $("#titoloEntrata").val();
        var tipologia = $("#tipologiaTitolo").val();
        var categoria = $("#categoriaTipologiaTitolo").val();
        var tipoCapitoloStandard = $("option:selected", "#categoriaCapitolo").data("codice") === "STD";

        // Controllo che i dati siano inseriti
        if (tipoCapitoloStandard && titolo === "") {
            errori.push("Il campo Titolo deve essere compilato");
        }
        // I seguenti campi provengono da select che possono essere disattivate
        if (tipoCapitoloStandard && (tipologia === null || tipologia === "")) {
            errori.push("Il campo Tipologia deve essere compilato");
        }
        if (tipoCapitoloStandard && (categoria === null || categoria === "")) {
            errori.push("Il campo Categoria deve essere compilato");
        }

        // Gestione degli errori
        Capitolo.gestioneErrori(errori);
    };

    /* ***** Funzioni per le chiamate AJAX *****/
    
    /**
     * Carica i dati nelle select dei classificatori gerarchici da chiamata
     * AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaListaClassificatoriGerarchici = function (gestionePrevisione) {
        var selects = $("#titoloEntrata").overlay("show");

        return $.postJSON("capEntrata" + gestionePrevisione + "/caricaListaClassificatoriGerarchiciCapitoloEntrata" + gestionePrevisione + ".do")
        .then(function (data) {
            var errori = data.errori;
            // Se ci sono degli errori, esco subito
            if(errori.length > 0) {
                return;
            }
            caricaSelectCodifiche($("#titoloEntrata"), data.listaTitoloEntrata).change();
        }).always(selects.overlay.bind(selects, 'hide'));
    };
    
    /**
     * Carica i dati nelle select dei classificatori gerarchici da chiamata
     * AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaListaClassificatoriGenerici = function (gestionePrevisione) {
        var selector = "#tipoFinanziamento,#tipoFondo,#perimetroSanitario,#transazioneUnioneEuropea,#divRicorrenteEntrata";
        var i;
        var selects;
        for(i = 1; i<=15; i++){
            selector += ",#classificatoreGenerico" + i;
        }
        selects = $(selector).overlay("show");
        return $.postJSON("capEntrata" + gestionePrevisione + "/caricaListaClassificatoriGenericiCapitoloEntrata" + gestionePrevisione + ".do")
        .then(function (data) {
            var errori = data.errori;
            var fieldsetAltriDati;
            var j;
            var selectClassificatore;
            var html;
            var divRicorrenteEntrata;
            var checkedFirst;
            var reduced;
            // Se ci sono degli errori, esco subito
            if(errori.length > 0) {
                return;
            }
            caricaSelectCodifiche($("#tipoFinanziamento"), data.listaTipoFinanziamento);
            caricaSelectCodifiche($("#tipoFondo"), data.listaTipoFondo);
            caricaSelectCodifiche($("#perimetroSanitario"), data.listaPerimetroSanitarioEntrata);
            caricaSelectCodifiche($("#transazioneUnioneEuropea"), data.listaTransazioneUnioneEuropeaEntrata);

            fieldsetAltriDati = $("#fieldsetAltriDati");
            for(j = 1; j <= 15; j++){
                selectClassificatore = $("#classificatoreGenerico"+i);
                if(data['labelClassificatoreGenerico' + j] && selectClassificatore.length === 0){
                    html = '';
                    html += '<div class="control-group">';
                    html += '  <label for="classificatoreGenerico' + j + '" class="control-label">' + data['labelClassificatoreGenerico' + j] + '</label>';
                    html += '  <div class="controls">';
                    html += '    <select id="classificatoreGenerico' + j + '" class="span10" name="classificatoreGenerico' + j + '.uid" />';
                    html += '  </div>';
                    html += '</div>';
                    fieldsetAltriDati.append(html);

                    caricaSelectCodifiche($("#classificatoreGenerico" + j), data['listaClassificatoreGenerico' + j]);
                }
            }

            divRicorrenteEntrata = $("#divRicorrenteEntrata");
            checkedFirst = divRicorrenteEntrata.find('label input').attr('checked');

            if(data.listaRicorrenteEntrata && checkedFirst === 'checked'){
                html = '';
                html += '<label class="radio inline">';
                html += '<input type="radio" name="ricorrenteEntrata.uid" value="" checked="checked" ';
                html += '>&nbsp;Non si applica </input>';
                html += '</label>';
                reduced = data.listaRicorrenteEntrata.reduce(function(acc, val){
                    acc += '<label class="radio inline">';
                    acc += '<input type="radio" name="ricorrenteEntrata.uid" value="' + val.uid + '" ';
                    acc += '>&nbsp;' + val.descrizione + '</input>';
                    acc += '</label>';
                    return acc;
                }, '');
                html += reduced;
                divRicorrenteEntrata.html(html);
            }
        }).always(selects.overlay.bind(selects, 'hide'));
    };
    
    /**
     * Carica i dati nella select della Tipologia da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaTipologia = function () {
        var idTitoloEntrata = $("#titoloEntrata").val();
        var codiceTitolo = $("#titoloEntrata option:selected").html().split("-")[0];
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
        //Pulisco la select della categoria
        $("#categoriaTipologiaTitolo").val("");
        
        // Gestione del flag accertato per cassa
        handleFlagAccertatoPerCassa(false);
        

        // Imposto il codice nel campo hidden
        $("#HIDDEN_codiceTitolo").val(codiceTitolo);

        return $.postJSON(
            "ajax/tipologiaTitoloAjax.do",
            {"id" : idTitoloEntrata},
            function (data) {
                var listaTipologiaTitolo = (data.listaTipologiaTitolo);
                var errori = data.errori;
                var options = $("#tipologiaTitolo");
                var selectCategoria = $("#categoriaTipologiaTitolo");
                var bottonePdC = $("#bottonePdC");

                options.find('option').remove().end();

                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    selectCategoria.attr("disabled", "disabled");
                    bottonePdC.attr("disabled", "disabled");
                    return;
                }
                
                if (selectCategoria.attr("disabled") !== "disabled") {
                    selectCategoria.attr("disabled", "disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }
                
                caricaSelectCodifiche(options, listaTipologiaTitolo).change();
            }
        );
    };
    
    function handleFlagAccertatoPerCassa(mayOpen) {
        var checkbox = $('#flagAccertatoPerCassa');
        var codiceTitolo = $("#titoloEntrata option:selected").html().split("-")[0];
        var tipologiaOptionText = $("#tipologiaTitolo option:selected").html() || '';
        var codiceTipologia = tipologiaOptionText.split("-")[0];
        var correctTitolo = '1';
        var correctTipologia = ['1010100', '1010200', '1010300'];
        
        if(!mayOpen || codiceTitolo !== correctTitolo || correctTipologia.indexOf(codiceTipologia) === -1) {
            checkbox.closest('.control-group')
                .slideUp()
                .promise()
                .then(checkbox.prop.bind(checkbox, 'checked', false));
        } else {
            checkbox.closest('.control-group').slideDown();
        }
    }

    /**
     * Carica i dati nella select della Categoria da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaCategoria = function () {
        var idTipologiaTitolo = $("#tipologiaTitolo").val();

        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUid").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
        $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
        handleFlagAccertatoPerCassa(true);

        return $.postJSON(
            "ajax/categoriaTipologiaTitoloAjax.do",
            {"id" : idTipologiaTitolo},
            function (data) {
                var listaCategoriaTipologiaTitolo = (data.listaCategoriaTipologiaTitolo);
                var errori = data.errori;
                var options = $("#categoriaTipologiaTitolo");
                var bottonePdC = $("#bottonePdC");

                options.find('option').remove().end();

                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    return;
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }
                
                caricaSelectCodifiche(options, listaCategoriaTipologiaTitolo).change();
            }
        );
    };

    return exports;
}());