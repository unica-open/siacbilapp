/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var baseUrl = $("#HIDDEN_baseUrl").val();
    var alertErrori = $("#ERRORI");
    var alertErroriModale = $("#ERRORI_modale");
    var alertInformazioni = $("#INFORMAZIONI");
    var pulsanteInserimento = $("#pulsanteInserimentoDati");
    var listaMovimentiPerTabella;
	//SIAC-7388
	var classiConciliazioneDigitazioneTestuale=["CONTI"];
	var classiConciliazioneSelezioneEDigitazioneTestuale=["CREDITI", "DEBITI"];

    /**
     * Svuota tutti i campi del form
     */
    function puliziaForm() {
        var action = $(this).data("href");
        // Invio il form
        $("#formInserisciPrimaNotaLibera").attr("action", action)
            .submit();
    }

    /**
     * Popolamento della tabella.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     * @param importoDaReg (Numeric) l'importo da registrare se i conti sono a zero (optionale default = 0)
     */
    function popolaTabella(list) {
        var tableId = "tabellaScritture";
        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessun conto associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "codiceConto"},
                {aTargets: [1], mData: function(source) {
                    return source.movimentoDettaglio.conto && source.movimentoDettaglio.conto.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    if(source.movimentoDettaglio.segno && source.movimentoDettaglio.segno._name === "DARE") {
                        var res = source.movimentoDettaglio.importo || 0;
                        return res.formatMoney();
                    }
                    return "";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");

                }},
                {aTargets: [3], mData: function(source) {
                    if(source.movimentoDettaglio.segno && source.movimentoDettaglio.segno._name === "AVERE") {
                        var res = source.movimentoDettaglio.importo || 0;
                        return res.formatMoney();
                    }
                    return "";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], mData: function(source) {
                    var temp = "";
                    var res = "";
                    if(!source.utilizzoImportoNonModificabile) {
                        // L'importo e' modificabile dall'operatore
                        temp += "<li><a class=\"aggiornaConto\">aggiorna</a></li>";
                    }
                    if(!source.utilizzoContoObbligatorio) {
                        // Posso eliminare il conto
                        temp += "<li><a class=\"eliminaConto\">elimina</a></li>";
                    }
                    if(source.daClasseConciliazione === true) {
                        temp += "<li><a class=\"dettaglio\">dettaglio</a></li>";
                    }
                    if(temp) {
                        res = "<div class=\"btn-group\">" +
                                "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                                "<ul class=\"dropdown-menu pull-right\">" +
                                    temp +
                                "</ul>" +
                            "</div>";
                    }
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaConto")
                            .click([oData, iRow], aggiornaConto)
                            .end()
                        .find("a.eliminaConto")
                            .click([oData, iRow], eliminaConto)
                            .end()
		                .find("a.dettaglio")
		                	.click([oData, iRow], apriModaleDettaglioConti);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Aggiornamento del conto modale.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaConto(e) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Popolo i dati
        var elementoStruttura = e.data[0];
        var idx = e.data[1];

        // Popolo i dati del conto
        var dareAvere = elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name;
        var importo = elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.importo || 0;
        $("input[type='radio'][data-" + dareAvere + "]").prop('checked', true);
        // Disabilito i radio se e' da causale
        $("input[type='radio'][data-DARE], input[type='radio'][data-AVERE]").prop('disabled', $("#HIDDEN_contiCausale").val() === "true");

        $("#segnoConto").val(elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name);
        $("#importoModale").val(importo.formatMoney());

        // Imposto l'indice
        $("#indiceContoModale").val(idx);
        // Apro il modale
        alertErroriModale.slideUp();
        $("#modaleAggiornamentoConto").modal("show");
    }

    /**
     * Eliminazione del conto da riga.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaConto(e) {
        var elementoStruttura = e.data[0];
        var idx = e.data[1];
        var str = "";

        if(elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.conto && elementoStruttura.movimentoDettaglio.conto.descrizione) {
            str = " " + elementoStruttura.movimentoDettaglio.conto.descrizione;
        }
        $("#modaleEliminazioneElementoSelezionato").html(str);
        // Apro il modale
        $("#modaleEliminazione").modal("show");
        // Lego l'azione di cancellazione
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", function() {
            eliminazioneConto(idx);
        });
    }

    /**
     * Eliminazione del conto.
     *
     * @param index l'indice del conto da eliminare
     */
    function eliminazioneConto(index) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_eliminaConto.do", {indiceConto: index})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 $("#modaleEliminazione").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            aggiornaDatiScritture(data);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        });
    }

    /**
     * Carica l'elenco delle scritture dalla action.
     *
     * @param urlSuffix (String) il suffisso dell'URL da invocare
     * @param obj       (Object) l'oggetto da passare al chiamato
     */
    function caricaElencoScrittureFromAction(urlSuffix, obj) {
        var overlay = $("#tabellaScritture").overlay("show");

        $.postJSON(baseUrl + urlSuffix + ".do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                return;
            }
            aggiornaDatiScritture(data);
        }).always(function() {
            overlay.overlay("hide");
        });
    }

    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaElencoScritture() {
        var uidCausale = $("#uidCausaleEP").val() != "" ? $("#uidCausaleEP").val() : 0;
        caricaElencoScrittureFromAction("_ottieniListaConti", {"causaleEP.uid": uidCausale});
    }

    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaElencoScrittureIniziale() {
        caricaElencoScrittureFromAction("_ottieniListaContiIniziale");
    }
    
    function popolaElencoContiConciliazione(classeDiConciliazione,index){
    	
    	if(index === undefined){
    		return;
    	}
    	var select = $("#contiConciliazionePerTitoloModale");
    	var obj = {'indiceConto': index};
    	$.postJSON(baseUrl + "_ottieniContiDaClasseDiConciliazione.do", obj, function(data) {
			 if(impostaDatiNegliAlert(data.errori, alertErrori)) {
	             return;
	         }
	         popolaSelect(select, data.listaContoDaClasseConciliazione, true);
        });
    }
 

    /**
     * Apertura del collapse dei conti.
     */
    function aperturaCollapseConti() {
        $(":input", "#fieldsetCollapseDatiStruttura").not("[data-maintain]").val("");
        $("#collapseDatiStruttura").collapse("show");
    }

    /**
    * Imposta importo proposto
    */
    function impostaImporto() {
        var obj = $("#importoDaRegistrare").serializeObject();
        obj.listaElementoScritturaPerElaborazione = listaMovimentiPerTabella;

        $.postJSON(baseUrl + "_impostaImportoDaRegistrare.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                return;
            }
            aggiornaDatiScritture(data);
        });

    }

    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoContoDaModale() {
        $("#segnoModale").val($("input[type='radio'][data-AVERE]").prop('checked'));
        var obj = unqualify($("#fieldsetModaleAggiornamentoConto").serializeObject(), 1);
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

        alertErroriModale.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_aggiornaConto.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                 $("#modaleAggiornamentoConto").modal("hide");
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#modaleAggiornamentoConto").modal("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);
        }).always(function() {
            spinner.removeClass("activated");
        });
    }
    
    /**
     * Chiama il servizio per selezionare il conto da classe di conciliazione
     * @param obj l'oggetto da inviare
     * @param url l'url a cui inviare l'oggetto
     * */
    function chiamaAggiornamentoContoClasseConciliazione(obj, url){
    	var spinner = $("#SPINNER_pulsanteSelezionaConto").addClass("activated");
        if(!url){
        	return;
        }
	    return $.postJSON(url, obj)
	        .then(function(data) {
	            if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleDettaglio'))) {
	                // Errore: esco
	                return;
	            }
	            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
	            $("#modaleDettaglioConti").modal("hide");
	            // Ricarico la tabella
	            aggiornaDatiScritture (data);
	        }).always(function() {
	            spinner.removeClass("activated");
	        });

    }
    
    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoContoClasseDaDigitazione(codiceContoInput, classeDiConciliazione) {
    	var index = $("#HIDDEN_indiceModale").val();
		//se viene passato il conto, prendo quello, altrimenti lo prendo dal dom
    	var codiceConto = codiceContoInput ? codiceContoInput :
					$('#contoConciliazione').val()? $('#contoConciliazione').val() : "";
		var conciliazione = classeDiConciliazione || '';
    	var obj = {'conto.codice' : codiceConto, 'indiceConto': index, 'classeDiConciliazioneContoDigitato': conciliazione };
    	var url = baseUrl + "_aggiornaContoDaClasseDiConciliazioneConDigitazione.do";
    	chiamaAggiornamentoContoClasseConciliazione(obj, url);
    }
    

	function aggiornamentoContoClasseDaSelezioneEDigitazione(classeDiConciliazione){
		var index = $("#HIDDEN_indiceModale").val();
		var codiceConto = $('#contoConciliazione').val()? $('#contoConciliazione').val() : "";
		var uisContoSelezionato = ($("#contiConciliazionePerTitoloModale").val() != null && $("#contiConciliazionePerTitoloModale").val() != "" )
    		? $("#contiConciliazionePerTitoloModale").val() : 0;
	    var valorizzatoCodiceConto =  codiceConto && codiceConto !== "";
		if( valorizzatoCodiceConto && uisContoSelezionato && uisContoSelezionato !== 0){
			if(impostaDatiNegliAlert(["COR_ERR_0029","Esiste un'incongruenza tra i parametri di input:il conto deve essere digitato o selezionato, non &egrave; possibile fare entrambe le operazioni."], $('#ERRORI_modaleDettaglio')))
			return;
		}
		if(!valorizzatoCodiceConto){
			var obj = {'contoDaSostituire.uid' : uisContoSelezionato, 'indiceConto': index};
	        var url = baseUrl + "_aggiornaContoDaClasseDiConciliazione.do";
	        chiamaAggiornamentoContoClasseConciliazione(obj, url);
			return;
		}
		var contoAssenteInClasse = $("#contiConciliazionePerTitoloModale option[data-codice='" + codiceConto + "']").length === 0;
		if(contoAssenteInClasse){
			impostaDatiNegliAlert([]);
		}
		
		aggiornamentoContoClasseDaDigitazione(codiceConto,classeDiConciliazione);
		
		
	}
    
    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoContoClasseDaSelezione() {
    	var index = $("#HIDDEN_indiceModale").val();
    	var uisContoSelezionato = ($("#contiConciliazionePerTitoloModale").val() != null && $("#contiConciliazionePerTitoloModale").val() != "" )
    		? $("#contiConciliazionePerTitoloModale").val() : 0;
        var obj = {'contoDaSostituire.uid' : uisContoSelezionato, 'indiceConto': index};
        
        var url = baseUrl + "_aggiornaContoDaClasseDiConciliazione.do";
        chiamaAggiornamentoContoClasseConciliazione(obj, url);
    }

    /**
     * Inserimento del conto da collapse
     */
    function inserisciConto() {
        $("#segnoCollapse").val($("input[type='radio'][data-collapse-AVERE]").prop('checked'));
        var obj = $("#fieldsetCollapseDatiStruttura").serializeObject();
        var collapse = $("#collapseDatiStruttura");


        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Attivo l'overlay
        collapse.overlay("show");
        $.postJSON(baseUrl + "_inserisciConto.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#collapseDatiStruttura").collapse("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);
        }).always(function() {
            collapse.overlay("hide");
        });
    }
    
    /**
     * imposta la modale in modo tale che sia predisposta per la classe conti.
     * */
    function impostaModaleClassePerDigitazioneConto(isUnicaOpzioneDigitazione){
		if(isUnicaOpzioneDigitazione){
			$("#selezioneContoGuidata").hide();
		}
    	$("#selezioneContoTestuale").show();
		$('#contoConciliazione').val("");
		if(isUnicaOpzioneDigitazione){
			$("#pulsanteSelezionaConto").substituteHandler('click', aggiornamentoContoClasseDaDigitazione.bind(undefined, undefined, undefined));	
		}
    	
    }

	function impostaModaleClassePerSelezioneConto(classeDiconciliazione,index, handlerSelezioneConto, isUnicaOpzioneDigitazione){
		if(index === undefined){
    		return;
    	}
		popolaElencoContiConciliazione(classeDiconciliazione,index);
		if(handlerSelezioneConto && typeof handlerSelezioneConto === "function"){
			$("#pulsanteSelezionaConto").substituteHandler('click',handlerSelezioneConto);
		}
		
	   if(isUnicaOpzioneDigitazione){
			$("#selezioneContoTestuale").hide();
	   }
		$("#selezioneContoGuidata").show();
    	
	}
    
    /**
     * Gestisce le diverse impostazioni della modale a seconda del tipo di classe di conciliazione
     * */
    function gestisciClassiConciliazione(classeDiconciliazione,index){
		//SIAC-7388
		$('.info-aggiuntive-digitazione-selezione').hide();
    	if(classiConciliazioneDigitazioneTestuale.includes(classeDiconciliazione)){	
    		return impostaModaleClassePerDigitazioneConto(true);
    	}
		var permettiSiaDigitazioneCheSelezione = classiConciliazioneSelezioneEDigitazioneTestuale.includes(classeDiconciliazione);
		if(permettiSiaDigitazioneCheSelezione){
			$('.info-aggiuntive-digitazione-selezione').show();
    		impostaModaleClassePerDigitazioneConto(false);
			impostaModaleClassePerSelezioneConto(classeDiconciliazione,index, aggiornamentoContoClasseDaSelezioneEDigitazione.bind(undefined,classeDiconciliazione), false);
			return;
    	}
		impostaModaleClassePerSelezioneConto(classeDiconciliazione,index, aggiornamentoContoClasseDaSelezione.bind(undefined), true);
    }
    
    /**
     * Apre la modale con i conti disponibili per sostituire la classe di conciliazione
     */
    function apriModaleDettaglioConti(e) {
    	var index = e.data[1];
    	var classeDiconciliazione = e.data[0] && e.data[0].classeDiConcilazione._name;
    	$("#HIDDEN_indiceModale").val(index);
    	gestisciClassiConciliazione(classeDiconciliazione,index);
    	$('#ERRORI_modaleDettaglio').slideUp();
    	$("#modaleDettaglioConti").modal("show");
    }
    
    function aggiornaDatiScritture (data) {
        listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
        popolaTabella(listaMovimentiPerTabella);
        $("#totaleDare").html(data.totaleDare.formatMoney());
        $("#totaleAvere").html(data.totaleAvere.formatMoney());
        $("#spanDaRegistrare").html(data.daRegistrare.formatMoney());
        $("#HIDDEN_contiCausale").val(data.contiCausale);
        if(data.contiCausale) {
            pulsanteInserimento.addClass("disabled")
            .off("click");
        }
       
    }

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
            opts += "<option >" + (emptyOptionText || "") + "</option>";
        }
        $.each(options, function() {
            opts += "<option value='" + this.uid + "'>" + this.codice + " - " + this.descrizione + "</option>";
        });
        select.html(opts);
    }
    
    function ricaricaCausaliPerEvento(){
        var selectEvento = $("#uidEvento");
        var selectCausaleEP = $("#uidCausaleEP");
         var overlayElements = $("#uidCausaleEP, #ms-uidCausaleEP");
         var evento = selectEvento.val();
         // Chiamata AJAX
        $.postJSON("ricercaCausaleEPByEvento.do", {"evento.uid": evento})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectCausaleEP, []);
                return;
            }
            popolaSelect(selectCausaleEP, data.listaCausaleEP, true,"Seleziona");
        }).always(function() {
            // Nasconfdo gli overlay
            overlayElements.overlay("hide");
        });
    }

    /**
     * Ricaricamento dei conti filtrati per causale.
     */
    function ricaricaContiPerCausale(){
        var selectCausaleEP = $("#uidCausaleEP");
        $.postJSON(baseUrl + "_ottieniListaConti.do", {"causaleEP.uid": selectCausaleEP.val()})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            aggiornaDatiScritture(data);
        });
    }

    $(function () {
        var selectCausale= $("select[data-causale-EP]");
        Conto.inizializza(undefined, undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
        $("#pulsanteAnnullaStep1").click(puliziaForm);

        selectCausale.change(caricaElencoScritture);

        pulsanteInserimento.click(aperturaCollapseConti);

        $("#pulsanteSalvaAggiornamentoConto").click(aggiornamentoContoDaModale);
        $("#pulsanteSalvaInserimentoConto").click(inserisciConto);

        if (selectCausale.val()) {
        	caricaElencoScritture();
        } else {
            caricaElencoScrittureIniziale();
        }
        // SIAC-5336
        GSAClassifZtree.initClassificatoreGSAZtree();
    });
}(jQuery);