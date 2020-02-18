/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var alertErroriModale = $("#ERRORI_modale");
    var alertInformazioni = $("#INFORMAZIONI");
    var baseUrl = $("#HIDDEN_baseUrl").val();
    var pulsanteInserimento = $("#pulsanteInserimentoDati");
    var listaMovimentiPerTabella;
    var zero = 0;

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
                {aTargets: [0], mData: defaultPerDataTable("codiceConto")},
                {aTargets: [1], mData: defaultPerDataTable("descrizioneConto")},
                {aTargets: [2], mData: defaultPerDataTable("domStringMissione")},
                {aTargets: [3], mData: defaultPerDataTable("domStringProgramma")},
                {aTargets: [4], mData: defaultPerDataTable("dare"), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable("avere"), fnCreatedCell: tabRight},
                //Azioni
                {aTargets: [6], mData: function(source) {
                    return "<div class=\"btn-group\">" +
                               "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                               "<ul class=\"dropdown-menu pull-right\">" +
                                   "<li><a class=\"aggiornaConto\">aggiorna</a></li>" +
                                   "<li><a class=\"eliminaConto\">elimina</a></li>" +
                               "</ul>" +
                           "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    var $td = $(nTd).addClass("tab_Right");
                    $td.find("a.aggiornaConto")
                            .click([oData, iRow], aggiornaConto);
                    $td.find("a.eliminaConto")
                            .click([oData, iRow], eliminaConto);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }
    
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
    }
    
    /**
     * Aggiornamento del conto.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaConto(e) {
        var elementoStruttura = e.data[0];
        var idx = e.data[1];
        var importo = elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.importo || 0;
        var dareAvere = elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name;
        var fncName = $("#contiCausale").val() === "true" ? "prop" : "removeProp";
        var $missione = $('#missioneModale');
        var codiceClassePiano = elementoStruttura.movimentoDettaglio
            && elementoStruttura.movimentoDettaglio.conto
            && elementoStruttura.movimentoDettaglio.conto.pianoDeiConti
            && elementoStruttura.movimentoDettaglio.conto.pianoDeiConti.classePiano
            && elementoStruttura.movimentoDettaglio.conto.pianoDeiConti.classePiano.codice
            || '';

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Popolo i dati del conto
        $("input[type='radio'][data-" + dareAvere + "]").prop('checked', true);
        // Diabilito i radio se e'è da causale
        $("input[type='radio'][data-DARE]")[fncName]('disabled', true);
        $("input[type='radio'][data-AVERE]")[fncName]('disabled', true);

        $("#segnoConto").val(elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name);
        $("#importoModale").val(importo.formatMoney());
        
        // SIAC-5281
        gestioneMissioneProgramma('#missioneModale', '#programmaModale', 'modale', null, elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.conto || undefined);
        $missione.val(elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.missione && elementoStruttura.movimentoDettaglio.missione.uid || 0);
        $('#programmaModale').data('oldValue', elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.programma && elementoStruttura.movimentoDettaglio.programma.uid || 0);
        $missione.change();

        // Imposto l'indice
        $("#indiceContoModale").val(idx);
        // SIAC-5356
        $('#codiceClassePianoModale').val(codiceClassePiano);
        // Apro il modale
        alertErroriModale.slideUp();
        $("#modaleAggiornamentoConto").modal("show");
    }

    /**
     * Eliminazione del conto.
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
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", eliminazioneConto.bind(undefined, idx));
    }

    /**
     * Eliminazione del conto.
     *
     * @param index l'indice del conto da eliminare
     */
    function eliminazioneConto(index) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_eliminaConto.do", {indiceConto: index}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                $("#modaleEliminazione").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            aggiornaDatiScritture (data);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        });
    }

    /**
     * Aggiornamento dei dati delle scritture
     */
    function aggiornaDatiScritture (data) {
        $("#totaleDare").html(data.totaleDare.formatMoney());
        $("#totaleAvere").html(data.totaleAvere.formatMoney());
        $("#daRegistrare").val(data.daRegistrare.formatMoney());

        listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
        popolaTabella(listaMovimentiPerTabella);
        if (!data.inserisciNuoviContiAbilitato) {
            pulsanteInserimento.addClass("disabled").off("click");
        }
    }

    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaElencoScritture() {
        return $.postJSON(baseUrl + "_ottieniListaConti.do", {})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            aggiornaDatiScritture (data);
        });
    }

    /**
     * Apertura del collapse dei conti.
     */
    function aperturaCollapseConti() {
        $(":input", "#fieldsetCollapseDatiStruttura")
            .not("input[type='radio']")
                .val("")
                .end()
            .removeProp("checked");
        $('#descrizioneConto').html('');
        $('div[data-classificazione]').slideUp();
        $("#collapseDatiStruttura").collapse("show");
    }
    
    /**
     * Impostazione dell'importo
     */
    function impostaImporto() {
        var obj = $("#importoDaRegistrare").serializeObject();

        $.postJSON(baseUrl + "_impostaImportoDaRegistrare.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto il campo
                $("#importoDaRegistrare").val(zero.formatMoney());
                return;
            }
            aggiornaDatiScritture (data);
        });
    }

    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoContoDaModale() {
        var fieldset = $("#fieldsetModaleAggiornamentoConto");
        var radio = fieldset.find("input[type='radio']:checked");
        var serialized = fieldset.serializeObject();
        var obj;
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

        if(radio.is(":disabled")) {
            // Il radio e' disabilitato. Prendo comunque il valore
            serialized[radio.attr("name")] = radio.val();
        }

        obj = unqualify(serialized, 1);
        $("#segnoModale").val($("input[type='radio'][data-AVERE]").prop('checked'));

        alertErroriModale.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_aggiornaConto.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#modaleAggiornamentoConto").modal("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);

        }).always(spinner.removeClass.bind(spinner, "activated"));
    }
    /**
     * Inserimento del conto.
     */
    function inserisciConto() {
        var fieldset = $("#fieldsetCollapseDatiStruttura");
        var obj = fieldset.serializeObject();
        var collapse = $("#collapseDatiStruttura");

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Attivo l'overlay
        collapse.overlay("show");
        $.postJSON(baseUrl + "_inserisciConto.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#collapseDatiStruttura").collapse("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);
        }).always(collapse.overlay.bind(collapse, "hide"));
    }
    
  //-----prime note collegate
    
    /**
     * Popolamento della tabella delle prime note collegate.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function impostaTabellaPrimeNoteCollegate(list) {
        var tableId = "tabellaPrimeNoteCollegate";

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
                sZeroRecords: "Nessuna prima nota collegata",
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
                {aTargets: [0], mData: defaultPerDataTable('tipoCausale.descrizione')},
                {aTargets: [1], mData: defaultPerDataTable('bilancio.anno')},
                {aTargets: [2], mData: defaultPerDataTable('numeroRegistrazioneLibroGiornale')},
                {aTargets: [3], mData: defaultPerDataTable('tipoRelazionePrimaNota.descrizione')},
                {aTargets: [4], mData: defaultPerDataTable('statoOperativoPrimaNota.descrizione')},
                //Azioni
                {aTargets: [5], mData: function(source) {
                    return "<div class=\"btn-group\">" +
                               "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                               "<ul class=\"dropdown-menu pull-right\">" +
                                   "<li><a class=\"eliminaCollegamento\">elimina</a></li>" +
                               "</ul>" +
                           "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.eliminaCollegamento")
                            .click([oData, iRow], eliminaCollegamento);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
        if(list.length > 0){
        	$("#spanTotaleNoteCollegate").html(": totale " + list.length);
        }
    }

	/**
	 * Carica la lista del conto tipo operazione.
	 */
	function caricaTabellaPrimeNote() {
	    $.postJSON(baseUrl + "_ottieniListaPrimeNoteCollegate.do", {}, function(data) {
	        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
	            return;
	        }
	        impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteDaCollegare);
	    });
	}

	 /**
     * Apertura del collapse delle prime note
     */
    function aperturaCollapseCollegamentoPrimaNota() {
        $(":input", "#fieldsetCollegamentoPrimaNota")
                .val("")
                .end()
            .removeProp("checked");
        $("#collapseCollegamentoPrimaNota").collapse("show");
    }
    
    /**
     * 
     */
    function collegaPrimaNota() {
        var fieldset = $("#fieldsetCollegamentoPrimaNota");
        var obj = fieldset.serializeObject();
        var collapse = $("#collapseCollegamentoPrimaNota");

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Attivo l'overlay
        collapse.overlay("show");
        $.postJSON(baseUrl + "_collegaPrimaNota.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico la tabella
            impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteDaCollegare);
        }).always(function() {
        	collapse.collapse("hide");
            collapse.overlay("hide");
        });
    }
    
    /**
     * Eliminazione del conto.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaCollegamento(e) {
        var primaNota = e.data[0];
        var idx = e.data[1];
        var str = " legame con prima nota " + primaNota.tipoCausale.descrizione + " numero " +  primaNota.numeroRegistrazioneLibroGiornale;

        $("#modaleEliminazioneElementoSelezionato").html(str);
        // Apro il modale
        $("#modaleEliminazione").modal("show");
        // Lego l'azione di cancellazione
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", eliminazioneCollegamento.bind(undefined, idx));
    }

    /**
     * Eliminazione del conto.
     *
     * @param index l'indice del conto da eliminare
     */
    function eliminazioneCollegamento(index) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_eliminaCollegamento.do", {indicePrimaNota: index}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                $("#modaleEliminazioneConto").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteDaCollegare);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        });
    }

    function clearMissioneProgramma(missioneField, programmaField) {
        $(missioneField).val(0);
        // Svuoto anche le options
        $(programmaField).val(0).empty();
        // Nascondo i campi
        $('div[data-classificazione]').slideUp();
    }
    
    function gestioneMissioneProgramma(missioneField, programmaField, tipoClassificazione, e, conto) {
        var isCE = conto
            && conto.pianoDeiConti
            && conto.pianoDeiConti.classePiano
            && conto.pianoDeiConti.classePiano.codice === 'CE';
        if(isCE) {
            $(missioneField).val(0);
            $(programmaField).val(0).empty().attr('disabled', true);
            // Mostro i campi
            $('div[data-classificazione="' + tipoClassificazione + '"]').slideDown();
        } else {
            clearMissioneProgramma(missioneField, programmaField);
        }
    }
    
    function loadProgramma(missioneField, programmaField) {
        var idMissione = $(missioneField).val();
        var $programma = $(programmaField);
        var currentProgrammaValue = $programma.data('oldValue');
        
        // Pulisco l'old value
        $programma.data('oldValue', '');
        if(idMissione === '0') {
            $programma.val(0).empty().attr('disabled', true);
            return;
        }
        $programma.overlay('show');
        return $.postJSON('ajax/programmaAjax.do', {'id': idMissione})
        .then(function(data) {
            var str;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Rigetto la promise
                $programma.val(0).empty().attr('disabled', true);
                return $.Deferred().reject().promise();
            }
            str = data.listaProgramma.reduce(function(acc, el) {
                var tmp = '<option value="' + el.uid + '"';
                if(el.uid === currentProgrammaValue) {
                    tmp += ' selected';
                }
                
                return acc + tmp + '>' + el.codice + ' - ' + el.descrizione + '</option>';
            }, '<option value="0"></option>');
            if(str.indexOf('selected>') === -1) {
                $programma.val(0);
            }
            $programma.html(str);
            $programma.removeAttr('disabled');
        }).always($programma.overlay.bind($programma, 'hide'));
    }
    
    $(function () {
        Conto.inizializza(undefined, undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
        pulsanteInserimento.click(aperturaCollapseConti);
        $("#importoDaRegistrare").change(impostaImporto);
        $("#pulsanteSalvaAggiornamentoConto").click(aggiornamentoContoDaModale);
        $("#pulsanteSalvaInserimentoConto").click(inserisciConto);
        
        PrimaNota.inizializza("#annoPrimaNota", "#numeroPrimaNota", "#selectTipoPrimaNota","#pulsanteCompilazioneGuidataPrimaNota", "#HIDDEN_ambito");
        $("#pulsanteCollegamentoPrimeNote").click(aperturaCollapseCollegamentoPrimaNota);
        $("#pulsanteSalvaCollegamento").click(collegaPrimaNota);
        
        $('#codiceConto')
            .substituteHandler('change', clearMissioneProgramma.bind(undefined, '#missione', '#programma'))
            .substituteHandler('contoCaricato', gestioneMissioneProgramma.bind(undefined, '#missione', '#programma', 'collapse'));
        
        $('#missione').substituteHandler('change', loadProgramma.bind(undefined, '#missione', '#programma'));
        $('#missioneModale').substituteHandler('change', loadProgramma.bind(undefined, '#missioneModale', '#programmaModale'));
        
        caricaElencoScritture();
        caricaTabellaPrimeNote();
    });
}(jQuery);