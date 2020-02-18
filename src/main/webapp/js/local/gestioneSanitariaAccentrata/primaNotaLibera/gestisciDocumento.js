/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var baseUrl = $("#HIDDEN_baseUrl").val();
    var baseUrlSubdocumento = $("#HIDDEN_baseUrlSubdocumento").val();
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");

    /**
     * Popolamento della tabella.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function popolaTabella(list) {
        var tableId = "tabellaQuote";
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
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "<a data-toggle=\"modal\"><p class=\"pagination-centered\"><i class=\"icon-search\">&nbsp;</i></p></a>";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("a", nTd).click([oData], apriModaleDatiDocumento);
                }},
                {aTargets: [1], mData: "numeroQuotaString"},
                {aTargets: [2], mData: "causaleString"},
                {aTargets: [3], mData: "statoRichiestaString"},
                {aTargets: [4], mData: "dataRegistrazioneString"},
                {aTargets: [5], mData: "contoFinanziarioString"},
                {aTargets: [6], mData: "contoEconomicoPatrimonialeString"},
                {aTargets: [7], mData: function(source) {
                    if(!source.subdocumento) {
                        return "";
                    }
                    return "<a href=\"" + baseUrlSubdocumento + "_dettaglio.do?subdocumento.uid=" + source.subdocumento.uid + "\" class=\"btn btn-secondary\">Dettaglio</a>";
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Apertura del modale dei dati documento.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function apriModaleDatiDocumento(e) {}































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
        $("input[type='radio'][data-" + dareAvere + "]").prop('checked', true);
        //diabilito i radio se è da causale

        $("input[type='radio'][data-DARE]").prop('disabled', $("#contiCausale").val() === "true");
        $("input[type='radio'][data-AVERE]").prop('disabled', $("#contiCausale").val() === "true");

        $("#segnoConto").val(elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name);
        $("#importoModale").val(elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.importo || 0);

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

        if(elementoStruttura.movimentoDettalgio && elementoStruttura.movimentoDettaglio.conto && elementoStruttura.movimentoDettaglio.conto.descrizione) {
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
     * Apertura del collapse dei conti.
     */
    function aperturaCollapseConti() {
        $(":input", "#fieldsetCollapseDatiStruttura").val("");
        $("#collapseDatiStruttura").collapse("show");
    }
    /*
    * imposta importo proposto
    */
    function impostaImporto() {
        var obj = $("#importoDaRegistrare").serializeObject();
        obj.listaElementoScritturaPerElaborazione = listaMovimentiPerTabella;

        $.postJSON(baseUrl + "_impostaImportoDaRegistrare.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            aggiornaDatiScritture (data);

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

        $.postJSON(baseUrl + "_aggiornaConto.do", obj, function(data) {
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
        $.postJSON(baseUrl + "_inserisciConto.do", obj, function(data) {
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

    function aggiornaDatiScritture (data) {
        var listaMovimentiPerTabella;
        if (data.listaElementoScritturaPerElaborazione.length >=0 ) {
            listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
            popolaTabella(listaMovimentiPerTabella);
            $("#totaleDare").html(data.totaleDare);
            $("#totaleAvere").html(data.totaleAvere);
            $("#daRegistrare").html(data.daRegistrare);
            if (data.contiCausale) {
                $("#pulsanteInserimentoDati").addClass("disabled")
                .off("click");
            }

        }
    }

    /**
     * Popolamento della select.
     *
     * @param select  (jQuery) la select da popolare
     * @param options (Array)  l'array delle opzioni
     * @param addEmptyOption (boolean) inserisce l'elemento vuoto (opzionale, default = false)
     * @param emptyOptionText (string) inserisce il testo per l'elemento vuoto (opzionale, default = "")
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
        $.postJSON("ricercaCausaleEPByEvento.do", {"evento.uid": evento}, function(data) {
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

    function ricaricaContiPerCausale(){

        $.postJSON(baseUrl + "_ottieniListaConti.do", {}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            aggiornaDatiScritture (data);

        });
    }

    $(function () {

        $("#pulsanteAnnullaStep1").click(puliziaForm);
        $("select[data-evento]").change(ricaricaCausaliPerEvento);
        $("select[data-causaleEP]").change(caricaelencoScritture);

        $("#pulsanteInserimentoDati").click(aperturaCollapseConti);
        $("#importoDaRegistrare").change(impostaImporto);
        $("#pulsanteSalvaAggiornamentoConto").click(aggiornamentoContoDaModale);
        $("#pulsanteSalvaInserimentoConto").click(inserisciConto);
        caricaelencoScritture();
    });
}(jQuery);