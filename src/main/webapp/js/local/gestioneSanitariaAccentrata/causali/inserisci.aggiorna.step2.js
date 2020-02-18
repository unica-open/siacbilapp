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
    var isLibera = $("#HIDDEN_tipoCausaleDiRaccordo").val() == "false" ;

    /**
     * Popolamento della tabella.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function popolaTabella(data) {
        var tableId = "tabellaConti";
        var opts = {
            bServerSide: false,
            aaData: data.listaContoTipoOperazione,
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
                {aTargets: [0], mData: function(source) {
                    var res = "";
                    if(source.conto && source.conto.pianoDeiConti && source.conto.pianoDeiConti.classePiano && source.conto.pianoDeiConti.classePiano.uid != 0) {
                        res = source.conto.pianoDeiConti.classePiano.descrizione + " - " + source.conto.livello;
                    } else if(source.classeDiConciliazione){
                        res = source.classeDiConciliazione.descrizione;
                    }
                    return res;
                }},
                {aTargets: [1], mData: function(source) {
                    var res = "";
                    // Controllare i dati
                    if(source.conto) {
                        res = "<a data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-content=\"" + escapeHtml(source.conto.descrizione) + "\">" + source.conto.codice + "</a>";
                    }
                    return res;
                }},
                {aTargets: [2], mData: function(source) {
                    return source.operazioneSegnoConto && source.operazioneSegnoConto.descrizione || "";
                }},
                {aTargets: [3], bVisible:!isLibera, mData: function(source) {
                    return source.operazioneUtilizzoConto && source.operazioneUtilizzoConto.descrizione || "";
                }},
                {aTargets: [4], bVisible:!isLibera, mData: function(source) {
                    return source.operazioneUtilizzoImporto && source.operazioneUtilizzoImporto.descrizione || "";
                }},
                {aTargets: [5], mData: function(source) {
                    return source.operazioneTipoImporto && source.operazioneTipoImporto.descrizione || "";
                }},
                //Aggiorna
                {aTargets: [6], mData: function() {
                    return "<div class=\"btn-group\">" +
                               "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                               "<ul class=\"dropdown-menu pull-right\">" +
                                   "<li><a class=\"aggiornaConto\">aggiorna</a></li>" +
                                   "<li><a class=\"eliminaConto\">elimina</a></li>" +
                               "</ul>" +
                           "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaConto")
                            .click([oData, iRow], aggiornaConto)
                            .end()
                        .find("a.eliminaConto")
                            .click([oData, iRow], eliminaConto);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Aggiornamento del conto.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaConto(e) {
        // Popolo i dati
        var contoTipoOperazione = e.data[0];
        var idx = e.data[1];

        // Popolo i dati del conto
        $("#uidClassePianoPianoDeiContiContoContoTipoOperazioneModale").val(contoTipoOperazione.conto && contoTipoOperazione.conto.pianoDeiConti && contoTipoOperazione.conto.pianoDeiConti.classePiano && contoTipoOperazione.conto.pianoDeiConti.classePiano.uid || "");
        $("#codiceContoContoTipoOperazioneModale").val(contoTipoOperazione.conto && contoTipoOperazione.conto.codice || "");
        $("#operazioneSegnoContoContoTipoOperazioneModale").val(contoTipoOperazione.operazioneSegnoConto && contoTipoOperazione.operazioneSegnoConto._name || "");
        $("#operazioneUtilizzoContoContoTipoOperazioneModale").val(contoTipoOperazione.operazioneUtilizzoConto && contoTipoOperazione.operazioneUtilizzoConto._name || "");
        $("#operazioneUtilizzoImportoContoTipoOperazioneModale").val(contoTipoOperazione.operazioneUtilizzoImporto && contoTipoOperazione.operazioneUtilizzoImporto._name || "");
        $("#operazioneTipoImportoContoTipoOperazioneModale").val(contoTipoOperazione.operazioneTipoImporto && contoTipoOperazione.operazioneTipoImporto._name || "");
        //CR-4596
        if(contoTipoOperazione.classeDiConciliazione){
        $("#classeConciliazioneContoTipoOperazioneModale").val(contoTipoOperazione.classeDiConciliazione._name);
        }
        // Imposto l'indice
        $("#indiceContoModale").val(idx);
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
        var contoTipoOperazione = e.data[0];
        var idx = e.data[1];
        var str = "";
        if(contoTipoOperazione.conto && contoTipoOperazione.conto.pianoDeiConti && contoTipoOperazione.conto.pianoDeiConti.classePiano) {
            str = " " + contoTipoOperazione.conto.pianoDeiConti.classePiano.descrizione + " - " + contoTipoOperazione.conto.livello;
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
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_eliminaConto.do", {indiceConto: index}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            popolaTabella(data);//data.listaContoTipoOperazione);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaListaContoTipoOperazione() {
        $.postJSON(baseUrl + "_ottieniListaConti.do", {}, function(data) {
            popolaTabella(data);//data.listaContoTipoOperazione);
        });
    }

    /**
     * Apertura del collapse dei conti.
     */
    function aperturaCollapseConti() {
        $(":input", "#fieldsetCollapseInserimentoConto").val("");
        gestisciClassePiano();
        gestisciClasseDiConciliazione();
        $("#collapseInserimentoConto").collapse("show");
    }

    /**
     * Inserimento del conto.
     */
    function inserisciConto() {
        var obj = $("#fieldsetCollapseInserimentoConto").serializeObject();
        var collapse = $("#collapseInserimentoConto");

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
            $("#collapseInserimentoConto").collapse("hide");
            // Ricarico la tabella
            popolaTabella(data);//data.listaContoTipoOperazione);
        }).always(function() {
            collapse.overlay("hide");
        });
    }

    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoConto() {
        var obj = unqualify($("#fieldsetModaleAggiornamentoConto").serializeObject(), 1);
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

        alertErroriModale.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        $.postJSON(baseUrl + "_aggiornaConto.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#modaleAggiornamentoConto").modal("hide");
            // Ricarico la tabella
            popolaTabella(data);//data.listaContoTipoOperazione);
        }).always(function() {
            spinner.removeClass("activated");
        });
    }
    
    function gestisciClasseDiConciliazione(){
    	var selectConciliazione = $("#classeDiConciliazione");
    	var selectClassePiano = $("#uidClassePianoPianoDeiContiContoContoTipoOperazione");
    	var codiceConto = $("#codiceContoContoTipoOperazione");
    	
    	if(selectConciliazione.val()){
    		selectClassePiano.val(0);
    		codiceConto.val("");
    		selectClassePiano.attr("disabled", true);
    		codiceConto.attr("disabled", true);
    		$("#contoObbligatorio").html("");
    		$("#classeObbligatoria").html("");
    	}
    	if(!selectConciliazione.val()){
    		selectClassePiano.removeAttr("disabled");
    		codiceConto.removeAttr("disabled");
    		$("#contoObbligatorio").html("*");
    		$("#classeObbligatoria").html("*");
    	}
    }	
    	
	function gestisciClassePiano(){
    	var selectConciliazione = $("#classeDiConciliazione");
    	var selectClassePiano = $("#uidClassePianoPianoDeiContiContoContoTipoOperazione");
    	var codiceConto = $("#codiceContoContoTipoOperazione");
    	
    	if(selectClassePiano.val()){
    		selectConciliazione.val(0);
    		selectConciliazione.attr("disabled", true);
    	}
    	if(!selectClassePiano.val()){
    		selectConciliazione.removeAttr("disabled");
    		codiceConto.val("");
    	}
    	
    }

    $(function() {
        Conto.inizializza("#uidClassePianoPianoDeiContiContoContoTipoOperazione", undefined, "#codiceContoContoTipoOperazione", '#descrizioneContoContoTipoOperazione', "#pulsanteCompilazioneGuidataConto");
        // Lego le azioni
        $("#pulsanteApriCollapseConti").click(aperturaCollapseConti);
        $("#pulsanteSalvaInserimentoConto").click(inserisciConto);
        $("#pulsanteSalvaAggiornamentoConto").click(aggiornamentoConto);

        caricaListaContoTipoOperazione();
        $("#uidClassePianoPianoDeiContiContoContoTipoOperazione").on("change", gestisciClassePiano);
        $("#classeDiConciliazione").on("change", gestisciClasseDiConciliazione);
        
    });
}(jQuery);