/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var CapitoloUscita = (function() {
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
        var errori = Capitolo.validaForm(true);

        // Definisco i campi specifici
        var missione = $("#missione").val();
        var programma = $("#programma").val();
        var titolo = $("#titoloSpesa").val();
        var macroaggregato = $("#macroaggregato").val();
        
        var tipoCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        
        var tipoCapitoloStandard = tipoCapitolo === "STD" || tipoCapitolo === 'FPV';

        // Controllo che i dati siano inseriti
        if (tipoCapitoloStandard && (missione === "" || missione === '0')) {
            errori.push("Il campo Missione deve essere selezionato");
        }
        if (tipoCapitoloStandard && (titolo === "" || titolo === "0")) {
            errori.push("Il campo Titolo deve essere selezionato");
        }
        // I seguenti campi provengono da select che possono essere disattivate
        if (tipoCapitoloStandard && (programma === null || programma === "" || programma === "0")) {
            errori.push("Il campo Programma deve essere selezionato");
        }
        if (tipoCapitoloStandard && (macroaggregato === null || macroaggregato === "" || macroaggregato === "0")) {
            errori.push("Il campo Macroaggregato deve essere selezionato");
        }

        // Gestione degli errori
        Capitolo.gestioneErrori(errori);
    };

    /* ***** Funzioni per le chiamate AJAX **** */

    /**
     * Carica i dati nelle select dei classificatori gerarchici da chiamata
     * AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaListaClassificatoriGerarchici = function (gestionePrevisione, loadTitoloSpesa) {
        var selects = $("#missione").overlay("show");
        return $.postJSON("capUscita" + gestionePrevisione + "/caricaListaClassificatoriGerarchiciCapitoloUscita" + gestionePrevisione + ".do")
        .then(function (data) {
            var errori = data.errori;
            // Se ci sono degli errori, esco subito
            if(errori.length > 0) {
                return;
            }

            caricaSelectCodifiche($("#missione"), data.listaMissione).change();
            if(!!loadTitoloSpesa) {
                caricaSelectCodifiche($("#titoloSpesa"), data.listaTitoloSpesa).change();
            }
        }).always(selects.overlay.bind(selects, "hide"));
    };
    
    /**
     * Carica i dati nelle select dei classificatori gerarchici da chiamata
     * AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaListaClassificatoriGenerici = function (gestionePrevisione) {
        var selector = "#politicheRegionaliUnitarie,#tipoFinanziamento,#tipoFondo,#perimetroSanitario,#transazioneUnioneEuropea,#divRicorrenteSpesa";
        var i = 1;
        var selects;
        for(i = 1; i <= 15; i++){
            selector += ",#classificatoreGenerico"+i;
        }
        selects = $(selector).overlay("show");
        return $.postJSON("capUscita" + gestionePrevisione + "/caricaListaClassificatoriGenericiCapitoloUscita" + gestionePrevisione + ".do")
        .then(function (data) {
            var errori = data.errori;
            var fieldsetAltriDati;
            var j;
            var selectClassificatore;
            var html;
            var divRicorrenteSpesa;
            var checkedFirst;
            var reduced;
            // Se ci sono degli errori, esco subito
            if(errori.length > 0) {
                return;
            }
            caricaSelectCodifiche($("#politicheRegionaliUnitarie"), data.listaPoliticheRegionaliUnitarie);
            caricaSelectCodifiche($("#tipoFinanziamento"), data.listaTipoFinanziamento);
            caricaSelectCodifiche($("#tipoFondo"), data.listaTipoFondo);
            caricaSelectCodifiche($("#perimetroSanitario"), data.listaPerimetroSanitarioSpesa);
            caricaSelectCodifiche($("#transazioneUnioneEuropea"), data.listaTransazioneUnioneEuropeaSpesa);

            fieldsetAltriDati = $("#fieldsetAltriDati");
            for(j = 1; j <= 15; j++){
                selectClassificatore = $("#classificatoreGenerico" + j);
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

            divRicorrenteSpesa = $("#divRicorrenteSpesa");
            checkedFirst = divRicorrenteSpesa.find('label input').attr('checked');

            if(data.listaRicorrenteSpesa && checkedFirst === 'checked'){
                html = '';
                html += '<label class="radio inline">';
                html += '<input type="radio" name="ricorrenteSpesa.uid" value="" checked="checked" ';
                html += '>&nbsp;Non si applica </input>';
                html += '</label>';
                reduced = data.listaRicorrenteSpesa.reduce(function(acc, val){
                    acc += '<label class="radio inline">';
                    acc += '<input type="radio" name="ricorrenteSpesa.uid" value="' + val.uid + '" ';
                    acc += '>&nbsp;' + val.descrizione + '</input>';
                    acc += '</label>';
                    return acc;
                }, '');
                html += reduced;
                divRicorrenteSpesa.html(html);
            }
        }).always(selects.overlay.bind(selects, 'hide'));
    };
    
    
    function pulisciDisabilitaCampi(useDefaultTitoloSpesa){
        $("#classificazioneCofog").val("").attr("disabled", "disabled");
        $("#macroaggregato").val("").attr("disabled", "disabled");
        $("#bottonePdC").attr("disabled", "disabled");
        
        if(!useDefaultTitoloSpesa) {
            $("#titoloSpesa").val("").attr("disabled", "disabled");
        }
    }
    exports.gestisciRisorsaAccantonata = function(saltaGestioneFlagImpegnabile){
    	var codiceMissione = $('#missione option:selected').data('codice');
    	var selectRisorsaAccantonata = $('#risorsaAccantonata');
    	var containerRisorsaAccantonata =$('#containerRisorsaAccantonata'); 
    	
    	if(!saltaGestioneFlagImpegnabile){
			Capitolo.gestioneFlagImpegnabile();
		}
    	
    	if(!codiceMissione || codiceMissione != '20'){
    		selectRisorsaAccantonata.val(0);
    		containerRisorsaAccantonata.slideUp();
    		selectRisorsaAccantonata.attr('disabled', true);
    		return;
    	}
    	containerRisorsaAccantonata.slideDown();
    	selectRisorsaAccantonata.removeAttr('disabled');
    	
    	if(saltaGestioneFlagImpegnabile){
    		$("#flagImpegnabile").attr("disabled", true);
    	}
    	
    	
    }
    
    /**
     * Carica i dati nella select del Programma da chiamata AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaProgramma = function (useDefaultTitoloSpesa) {
		/* Ottiene l'UID dalla select */
		var idMissione = $("#missione").val();
		// Pulisco i campi
		pulisciDisabilitaCampi(useDefaultTitoloSpesa);
		var selectProgramma = $("#programma").overlay("show");
		
		return $.postJSON("ajax/programmaAjax.do", {
			"id" : idMissione
		}).then(function(data) {
			/* La lista dei programmi ottenuta dalla chiamata */
			var listaProgramma = (data.listaProgramma);
			/* La lista degli errori */
			var errori = (data.errori);
			/* La select in cui dovranno essere posti i dati */
			var selectProgramma = $("#programma");
			var titoloSpesa = $("#titoloSpesa");
			var macroaggregato = $("#macroaggregato"); 
			/*
			 * La select per il classificatore Cofog. Serve per un controllo
			 * sulla disabilitazione
			 */
			var cofog = $("#classificazioneCofog");

			/* Pulisci l'eventuale lista presente */
			selectProgramma.find('option').remove().end();
			
			//task-55
			exports.capitoloDaNonInserire(useDefaultTitoloSpesa);

			// Se vi sono degli errori, esco subito
			if (errori.length > 0) {
				selectProgramma.attr("disabled", "disabled");
				return;
			}
			/*
			 * Controlla che i select Cofog,titolo.macroaggregato sia
			 * disabilitato. In caso contrario, li disabilito
			 */
			if (cofog.attr("disabled") !== "disabled") {
				cofog.attr("disabled", "disabled");
			}

			if (titoloSpesa.attr("disabled") !== "disabled" && !useDefaultTitoloSpesa) {
				titoloSpesa.attr("disabled", "disabled");
			}

			if (macroaggregato.attr("disabled") !== "disabled") {
				macroaggregato.attr("disabled", "disabled").off("click");
			}
			caricaSelectCodifiche(selectProgramma, listaProgramma).change();
		
		}).always(selectProgramma.overlay.bind(selectProgramma, "hide"));
		
	};

	//task-55
	exports.capitoloDaNonInserire = function (useDefaultTitoloSpesa) {
		var codiceMissione = $('#missione option:selected').data('codice');
    	var capitoloDaNonInserire =$('#containerFlagCapitoloDaNonInserireA1'); 
    	
    	if(!codiceMissione || codiceMissione != '20'){
    		capitoloDaNonInserire.slideUp();
    		return;
    	}
    	capitoloDaNonInserire.slideDown();
	};
	
	exports.gestisciChangeMissione = function(useDefaultTitoloSpesa){
    	//SIAC-7192
    	exports.gestisciRisorsaAccantonata();
    	
    	exports.caricaProgramma(useDefaultTitoloSpesa);
    	
    	//task-55
		exports.capitoloDaNonInserire(useDefaultTitoloSpesa);
    }
    /**
	 * Carica i dati nella select del Cofog da chiamata AJAX.
	 * 
	 * @param useDefaults (Boolean) se utilizzare i valori di default
	 * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
	 */
    exports.caricaCofogTitolo = function (useDefaults) {
        return exports.caricaTitoloCofog(useDefaults);
    };

    /**
	 * Carica il codice del tipo di Cofog nel campo hidden corrispondente
	 */
    exports.caricaCodiceCofog = function() {
        var codice = $(this).find("option:selected")
                .data("codiceTipo");
        var hiddenField = $("#codiceTipoClassificatoreClassificazioneCofog");
        if(hiddenField.length) {
            // Se ho il campo, allora scrivo il valore
            hiddenField.val(codice);
        }
    };
    
    /**
	 * Carica il codice del tipo di TitoloSpesa nel campo hidden corrispondente
	 */
    exports.caricaCodiceTitoloSpesa = function() {
        var codice = $(this).find("option:selected")
                .data("codiceTipo");
        var hiddenField = $("#codiceTipoClassificatoreTitoloSpesa");
        if(hiddenField.length) {
            // Se ho il campo, allora scrivo il valore
            hiddenField.val(codice);
        }
    };
  
    /**
     * Carica i dati nella select del Cofog e del titolo in unica chiamata AJAX.
     * 
     * @param useDefaults (Boolean) se utilizzare i valori di default
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaTitoloCofog = function (useDefaults) {
        var idProgramma = $("#programma").val();
        var selectTitolo = $('#titoloSpesa').overlay("show");
        var selectCofog = $('#classificazioneCofog').overlay("show");
        var url = !!useDefaults ? 'ajax/titoloSpesaCofogByProgrammaAjaxWithDefaults.do' : 'ajax/titoloSpesaCofogByProgrammaAjax.do';
		$("#macroaggregato").val("").attr("disabled", "disabled");
		$("#bottonePdC").attr("disabled", "disabled");
		// non funge l'overlay se inserisco piu di una select ...

		return $.postJSON(url, {"id" : idProgramma})
		    .then(function(data) {
    			// TITOLO SPESA
    			var listaTitoloSpesa = data.listaTitoloSpesa;
    			var errori = data.errori;
    			var selectTitolo = $("#titoloSpesa");
    
    			// COFOG
    			var listaClassificazioneCofog = data.listaClassificazioneCofog;
    			var selectCofog = $("#classificazioneCofog");
    
    			selectCofog.find('option').remove().end();
    			selectTitolo.find('option').remove().end();
    
    			if (errori.length > 0) {
    				selectCofog.attr("disabled", "disabled");
    				// Ho comunque i valori originali da rimettere
    				if(!!useDefaults) {
    				    caricaSelectCodifiche(selectTitolo,listaTitoloSpesa).change();
    				} else {
    				    selectTitolo.attr("disabled", "disabled");
    				}
    				return;
    			}

    			selectCofog.removeAttr('disabled');
    			// carica la select dei cofog
    			caricaSelectCodifiche(selectCofog, listaClassificazioneCofog, function(val) {
			        return ' data-codice-tipo="' + val.tipoClassificatore.codice + '"';
    			}).change();
    			caricaSelectCodifiche(selectTitolo,listaTitoloSpesa).change();
    		}).always(function() {
    		    selectTitolo.overlay("hide");
    	        selectCofog.overlay("hide");
	        });
    };

    /**
	 * Carica i dati nella select del Cofog da chiamata AJAX.
	 * 
	 * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
	 */
    exports.caricaCofog = function () {
        var idProgramma = $("#programma").val();
        return $.postJSON(
            "ajax/cofogAjax.do",
            {"id" : idProgramma},
            function (data) {
                var listaClassificazioneCofog = data.listaClassificazioneCofog;
                var errori = data.errori;
                var selectCofog = $("#classificazioneCofog");
                selectCofog.find('option').remove().end();

                if(errori.length > 0) {
                    selectCofog.attr("disabled", "disabled");
                    return;
                }
                
                caricaSelectCodifiche(selectCofog, listaClassificazioneCofog,  function (val) {
                    return ' data-codice-tipo="' + val.tipoClassificatore.codice + '"';
                }).change();
            }
        );
    };
    
    /**
	 * Carica i dati nella select del TitoloSpesa da chiamata AJAX.
	 * 
	 * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
	 */
    exports.caricaTitoloSpesa = function () {
        var idProgramma = $("#programma").val();
        return $.postJSON(
            "ajax/titoloSpesaAjax.do",
            {"id" : idProgramma},
            function (data) {
                var listaClassificazioneTitoloSpesa = data.listaClassificazioneTitoloSpesa;
                var errori = data.errori;
                var selectTitoloSpesa = $("#titoloSpesa");
                selectTitoloSpesa.find('option').remove().end();

                if(errori.length > 0) {
                    selectTitoloSpesa.attr("disabled", "disabled");
                    return;
                }
                
                caricaSelectCodifiche(selectTitoloSpesa, listaClassificazioneTitoloSpesa,  function (val) {
                    return ' data-codice-tipo="' + val.tipoClassificatore.codice + '"';
                }).change();
            }
        );
    };


    /**
     * Carica i dati nella select del Macroaggregato da chiamata AJAX.
     * 
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaMacroaggregato = function () {
        var idTitoloSpesa = $("#titoloSpesa").val();

		        // Pulisco il valore dei campi riferentesi all'elemento del piano dei
		// conti
		$("#HIDDEN_ElementoPianoDeiContiUid").val("");
		$("#HIDDEN_ElementoPianoDeiContiStringa").val("");
		$("#SPAN_ElementoPianoDeiConti").html(
				"Nessun P.d.C. finanziario selezionato");
		$("#bottonePdC").attr("disabled", "disabled");
		var select = $("#macroaggregato").overlay("show");
		// Effettuo la chiamata JSON
		return $.postJSON("ajax/macroaggregatoAjax.do", {
			"id" : idTitoloSpesa
		}).then(
				function(data) {
					var listaMacroaggregato = (data.listaMacroaggregato);
					var errori = data.errori;
					var selectMacroaggregato = $("#macroaggregato");
					var bottonePdC = $("#bottonePdC");

					selectMacroaggregato.find('option').remove().end();
					if (errori.length > 0) {
						selectMacroaggregato.attr("disabled", "disabled");
						return;
					}
					if (bottonePdC.attr("disabled") !== "disabled") {
						bottonePdC.attr("disabled", "disabled");
					}

					caricaSelectCodifiche(selectMacroaggregato,
							listaMacroaggregato).change();
				}).always(select.overlay.bind(select, "hide"));
	};

    return exports;
}());