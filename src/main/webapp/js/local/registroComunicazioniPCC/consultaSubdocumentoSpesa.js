/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertInformazioni = $("#INFORMAZIONI");
    var documentoAnnullato = $("#documentoAnnullato").val() === "true";
    var presentiComunicazioniPagamento = $("#presentiComunicazioniPagamento").val() === "true";
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 3,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: false,
        bDestroy: true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec."
            }
        },
        // Chiamata al termine della creazione della tabella
        fnDrawCallback: function () {
            var records = this.fnSettings().fnRecordsTotal();
            var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
            $('#num_result').html(testo);
        }
    };

    /**
     * Attivazione del dataTable sulla tabella.
     *
     * @param tableSelector (String) l'id della tabella
     * @param extraOpts     (Object) le opzioni aggiuntive (opzionale, default = {})
     */
    function activateDataTableOnTable(tableSelector, extraOpts) {
        var opts = $.extend(true, {}, baseOpts, extraOpts || {});
        var popoverOpts = {
            title: "Descrizione Esito",
            trigger: "hover"
        };
        $(tableSelector).dataTable(opts);
        $("a.visualizzaDescrizioneEsito", "td").eventPreventDefault("click").popover(popoverOpts);
    }

    /**
     * Attivazione del dataTable delle contabilizzazioni
     *
     * @param list (Array) la lista delle contabilizzazioni
     */
    function activateDataTableContabilizzazioni(list) {
        var extraOpts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Non sono presenti contabilizzazioni",
                oPaginate: {
                    sEmptyTable: "Nessuna contabilizzazione disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "statoDebito"},
                {aTargets: [1], mData: "causalePCC"},
                {aTargets: [2], mData: "trasmesso"},
                {aTargets: [3], mData: "dataInvio"},
                {aTargets: [4], mData: function(source) {
                    return "<a href=\"#\" class=\"visualizzaDescrizioneEsito\" rel=\"popover\" data-trigger=\"hover\"  data-placement=\"left\" title=\"Descrizione esito\" data-content=\""
                        + escapeHtml(source.descrizioneEsito) + "\">" + source.codiceEsito + "</a>";
                }},
                {aTargets: [5], mData: "dataEsito"},
                {aTargets: [6], mData: function(source) {
                    if(source.dataInvio || documentoAnnullato || presentiComunicazioniPagamento) {
                        return "";
                    }
                    return "<div class=\"btn-group\">"
                            + "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
                            + "<ul class=\"dropdown-menu pull-right\">"
                                + "<li><a href=\"#\" class=\"aggiornaComunicazione\">aggiorna</a></li>"
                                + "<li><a href=\"#\" class=\"eliminaComunicazione\">elimina</a></li>"
                            + "</ul>"
                        + "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find(".aggiornaComunicazione")
                            .click([oData, iRow], aggiornaComunicazione)
                            .end()
                        .find(".eliminaComunicazione")
                            .click([oData, iRow], eliminaComunicazione);
                }}
            ]
        };
        activateDataTableOnTable("#tabellaContabilizzazioni", extraOpts);
    }

    /**
     * Aggiornamento della comunicazione.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaComunicazione(e) {
        // Caricamento dati nella modale e apertura
        var registro = e.data[0] || {};
        var row = e.data[1];
        var uidStatoDebito = registro.uidStatoDebito || 0;
        var uidCausalePCC = registro.uidCausalePCC || 0;
        var uidRegistro = registro.uid || 0;
        var methodSuffix="AggiornaContabilizzazione";

        //seleziono la select dello stato debito
        var $statoDebito = $("#statoDebitoRegistroComunicazioniPCCModaleAggiornaContabilizzazione");
        var ev = new $.Event('change');
        ev.oldUid = uidCausalePCC;

        // Blocco l'azione di default
        e.preventDefault();

        // Imposto i precedenti dati

        $statoDebito.change(popolaSelectCausaliByStatoDebito.bind($statoDebito, methodSuffix))
            .val(uidStatoDebito)
            .trigger(ev);

        $("#registroComunicazioniPCCModaleAggiornaContabilizzazione").val(uidRegistro);
        $("#confermaModaleAggiornaContabilizzazione").substituteHandler("click", function() {
            if($statoDebito.val()==="") {
                impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: Stato del debito"], $("#modaleAggiornaContabilizzazione").find(".alert.alert-error"));
                return;
            }
            aggiornaContabilizzazione.call(this, row);
        });
        $("#modaleAggiornaContabilizzazione").modal("show");
    }

    /**
     * Eliminazionedella comunicazione.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaComunicazione(e) {
        // Caricamento dati nella modale e apertura
        var registro = e.data[0] || {};
        var row = e.data[1];
        var codiceStatoDebito = registro.statoDebito;

        // Blocco l'azione di default
        e.preventDefault();
        $("#spanModaleEliminaContabilizzazione").text(codiceStatoDebito);
        $("#confermaModaleEliminaContabilizzazione").substituteHandler("click", function() {
            eliminaContabilizzazione.call(this, row, registro);
        });
        $("#modaleEliminaContabilizzazione").modal("show");
    }

    /**
     * Caricamento delle contabilizzazioni.
     */
    function caricaContabilizzazioni() {
        return $.postJSON("consultaRegistroComunicazioniPCCSubdocumentoSpesa_ottieniListaContabilizzazioni.do", {}, function(data) {
            activateDataTableContabilizzazioni(data.listaContabilizzazioni);
        });
    }

    /**
     * Apertura del modale per la nuova contabilizzazione.
     */
    function aperturaModaleInserimentoNuovaContabilizzazione() {
        // Pulisco i campi e apro il modale
        $(":input", "#fieldsetModaleNuovaContabilizzazione").val("");
        $("#modaleNuovaContabilizzazione").modal("show");
        var $statoDebito = $(" #modaleNuovaContabilizzazione #statoDebitoRegistroComunicazioniPCCModaleNuovaContabilizzazione");
        var methodSuffix = "NuovaContabilizzazione";
        $statoDebito.change( popolaSelectCausaliByStatoDebito.bind($statoDebito, methodSuffix));
    }

    /**
     * Popola la select delle causali in base allo stato del debito selezionato
     *
     * @param methodSuffix la stringa che definisce da quale modale &egrave stato lanciato l'evento (pu&ograve essere "NuovaContabilizzazione" o "Aggiornacontabilizzazione")
     * @param e l'evento scatenante
     */
     function popolaSelectCausaliByStatoDebito(methodSuffix, e ){
        // Blocco l'evento
        var idStatoDebito = $(this).val();
        var $target = $("#causalePCCRegistroComunicazioniPCCModale" + methodSuffix );
        $target.overlay('show');
        $.postJSON(
            "consultaRegistroComunicazioniPCCSubdocumentoSpesa_ottieniCausaliPCCByStatoDebito.do",
            {"statoDebito.uid" : idStatoDebito},
            function (data) {
                /* La lista dei programmi ottenuta dalla chiamata */
                var listaCausali = (data.listaCausalePCC);
                var str = '';

                /* Pulisci l'eventuale lista presente */
                $target.find('option').remove().end();

                 /* Se la lista da popolare è disabilitata, la abilita */
                if ($target.attr("disabled") === "disabled") {
                    $target.removeAttr("disabled");
                }

                /* Appendere i vari risultati */
                str = listaCausali.reduce(function(acc, val) {
                    var innerStr = '<option value="' + val.uid + '"';
                    if(e.oldUid && +val.uid === +e.oldUid) {
                        innerStr += ' selected';
                    }
                    innerStr += '>' + val.codice + ' - ' + val.descrizione + '</option>';
                    return acc + innerStr;
                }, '<option value=""></option>');
                $target.html(str);
                $target.overlay("hide");
            });
        }

    /**
     * Aggiornamento della contabilizzazione.
     *
     * @param row (Number) il numero di riga
     */
    function aggiornaContabilizzazione(row) {
        var obj = $("#fieldsetModaleAggiornaContabilizzazione").serializeObject();
        var spinner = $(this).find(".spinner");

        // Aggiungo il numero di riga
        obj.riga = row;
        callbackContabilizzazione(obj, spinner, "consultaRegistroComunicazioniPCCSubdocumentoSpesa_aggiornaContabilizzazione.do", "#modaleAggiornaContabilizzazione");
    }

    /**
     * Inserimento contabilizzazione
     */
    function inserisciContabilizzazione() {
        var $statoDebito = $('#statoDebitoRegistroComunicazioniPCCModaleNuovaContabilizzazione');
        var obj;
        var spinner;
        if($statoDebito.val() === "") {
            impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: Stato del debito"], $("#modaleNuovaContabilizzazione").find(".alert.alert-error"));
            return;
        }
        obj = $("#fieldsetModaleNuovaContabilizzazione").serializeObject();
        spinner = $(this).find(".spinner");

        callbackContabilizzazione(obj, spinner, "consultaRegistroComunicazioniPCCSubdocumentoSpesa_inserisciContabilizzazione.do", "#modaleNuovaContabilizzazione");
    }

    /**
     * Eliminazione della contabilizzazione.
     *
     * @param row (Number) il numero di riga
     * @param reg (Object) il registro da eliminare
     */
    function eliminaContabilizzazione(row, reg) {
        var obj = {riga: row, "registroComunicazioniPCC.uid": reg.uid};
        var spinner = $(this).find(".spinner");

        callbackContabilizzazione(obj, spinner, "consultaRegistroComunicazioniPCCSubdocumentoSpesa_eliminaContabilizzazione.do", "#modaleEliminaContabilizzazione");
    }

    /**
     * Callback per le contabilizzazioni
     *
     * @param objToSend (Object) l'oggetto da inviare
     * @param spinner   (jQuery) l'oggetto jQuery corrispondente allo spinner
     * @param url       (String) l'URL da invocare
     * @param modalSel  (String) il selettore del modale
     */
    function callbackContabilizzazione(objToSend, spinner, url, modalSel) {
        spinner.addClass("activated");
        $.postJSON(url, objToSend, function(data) {
            var modal = $(modalSel);
            if(impostaDatiNegliAlert(data.errori, modal.find(".alert.alert-error"))) {
                return;
            }
            // Inserimento andatao a buon fine
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Chiudo il modale
            modal.modal("hide");
            // Ricalcolo le contabilizzazioni
            caricaContabilizzazioni();
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    $(function() {
        // Attivo il dataTable sulle tabelle statiche
        activateDataTableOnTable("#tabellaComunicazioniDataScadenza", {iDisplayLength: 5, oLanguage: {sZeroRecords: "Non sono presenti comunicazioni per la data di scadenza", oPaginate: {sEmptyTable: "Nessuna comunicazione per data di scadenza disponibile"}}});
        activateDataTableOnTable("#tabellaComunicazioniPagamento", {oLanguage: {sZeroRecords: "Non sono presenti comunicazioni per il pagamento", oPaginate: {sEmptyTable: "Nessuna comunicazione per pagamento disponibile"}}});

        // Caricamento dati contabilizzazioni
        caricaContabilizzazioni();

        $("#pulsanteInserisciNuovaContabilizzazione").substituteHandler("click", aperturaModaleInserimentoNuovaContabilizzazione);
        $("#confermaModaleNuovaContabilizzazione").substituteHandler("click", inserisciContabilizzazione);
    });
}(jQuery);