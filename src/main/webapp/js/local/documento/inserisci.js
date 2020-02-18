/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function() {
    var exports = {};

    // L'alert degli errori
    var alertErrori = $("#ERRORI");
    
    exports.preSubmit = preSubmit;

    /**
     * Valorizza il netto per l'importo.
     */
    exports.valorizzaNetto = function() {
        var importoStr = $("#importoDocumento").val();
        var arrotondamentoStr = $("#arrotondamentoDocumento").val();
        var importo = parseFloat(parseLocalNum(importoStr));
        var arrotondamento = parseFloat(parseLocalNum(arrotondamentoStr));
        var campoNetto = $("#nettoDocumento");

        if(isNaN(importo) || isNaN(arrotondamento)) {
            // Almeno una stringa non è corretta: ritorno
            return;
        }
        // Importo il netto
        campoNetto.val((importo + arrotondamento).formatMoney());
    };

    /**
     * Calcola il codice fiscale del soggetto pignorato.
     *
     * @param elt (jQuery) l'elemento da cui ottenere il codice fiscale
     */
    exports.calcoloCodiceFiscalePignorato = function(elt) {
        var codiceFiscale = elt.val().toUpperCase();
        var codiceFiscaleValido = true;
        if(codiceFiscale.length === 16) {
            codiceFiscaleValido = checkCodiceFiscale(codiceFiscale);
        }

        elt.val(codiceFiscale);

        if(codiceFiscaleValido) {
            alertErrori.slideUp();
        } else {
            impostaDatiNegliAlert(["Il codice fiscale inserito non è sintatticamente corretto"], alertErrori, false);
        }
    };

    /**
     * Gestisce l'attivazione o la disattivazione della data di sospensione del documento.
     */
    exports.gestioneDataRiattivazione = function() {
        var campoDataRiattivazione = $("#dataRiattivazioneDocumento");
        var campoDataSospensione = $("#dataSospensioneDocumento");
        if(campoDataSospensione.val()) {
            campoDataRiattivazione.removeAttr("disabled");
        } else {
            campoDataRiattivazione.val("")
                .attr("disabled", "disabled");
        }
    };

    /**
     * Gestisce la data di scadenza del documento.
     */
    exports.gestioneDataScadenza = function() {
        var terminePagamento = parseInt($("#terminePagamentoDocumento").val(), 10);
        var dataScadenzaField = $("#dataScadenzaDocumento");
        var dataDocumento = $("#HIDDEN_dataRicezioneDocumento").val() || $("#dataRepertorioDocumento").val() || $("#HIDDEN_dataDocumento").val() || $("#dataEmissioneDocumento").val();
        var dataEmissioneSplit = dataDocumento.split("/");
        var dataScadenzaDate;
        var newString;

        // Controllo se abbia sufficienti dati
        if(isNaN(terminePagamento) || dataEmissioneSplit.length !== 3) {
            return;
        }

        // Costruisco la data
        dataScadenzaDate = new Date(dataEmissioneSplit[2], dataEmissioneSplit[1] - 1, dataEmissioneSplit[0]);
        dataScadenzaDate.setDate(dataScadenzaDate.getDate() + terminePagamento);
        // Costruisco la nuova stringa
        newString = ('0' + dataScadenzaDate.getDate()).slice(-2) + '/'
                + ('0' + (dataScadenzaDate.getMonth() + 1)).slice(-2) + '/'
                + dataScadenzaDate.getFullYear();
        dataScadenzaField.val(newString);
    };

    /**
     * Imposta l'anno per il datepicker della data di emissione in maniera coerente con l'anno del documento
     */
    exports.impostaAnnoPerDataEmissione = function() {
        var anno = parseInt($(this).val(), 10);
        var dataEmissione = $("#dataEmissioneDocumento").not(':disabled');
        var currentValue = dataEmissione.val();
        var dataOdierna = new Date();
        if(isNaN(anno) || dataEmissione.length===0) {
            return;
        }

        // Imposto la data iniziale dell'anno di emissione
        dataOdierna.setFullYear(anno);

        dataEmissione.datepicker("update", dataOdierna)
            .val(currentValue);
    };

    /**
     * Resetta il form.
     * <br>
     * Pulisce il form e ripristina a read-only gli eventuali campi già compilati.
     */
    exports.resetForm = function() {
        // Per pura pigrizia
        var zero = 0;
        $(this).find(":input")
                .not(".btn")
                    .val("");
        $("#dataRiattivazioneDocumento").attr("disabled", "disabled");
        // Metto a zero gli importi
        $("#importoDocumento").val(zero.formatMoney());
        $("#arrotondamentoDocumento").val(zero.formatMoney());
        $("#nettoDocumento").val(zero.formatMoney());
    };

    /**
     * Gestisce il click sul campo di riattivazione, bloccando l'evento nel caso in cui il campo sia readonly.
     *
     * @param e (Event) l'evento scatenante
     *
     * @returns (Boolean) se l'evento debba propagarsi o meno
     */
    exports.gestioneClickRiattivazione = function(e) {
        if($(this).attr("disabled")) {
            e.stopPropagation();
            e.preventDefault();
            return false;
        }
        return true;
    };

    /**
     * Apre il modale del soggetto e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.apriModaleSoggetto = function(e) {
        e.preventDefault();
        $("#codiceSoggetto_modale").val($("#codiceSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    };

    /**
     * Pre-submit dei dati
     * @param e (Event) l'evento scatenante
     * @return (boolean) se proseguire con il submit
     */
    function preSubmit(e) {
        var fields = $(e.currentTarget)
            .find('#registroRepertorioDocumento, #annoRepertorioDocumento, #numeroRepertorioDocumento, #dataRepertorioDocumento');
        var campiValorizzati;
        if(fields.length === 0 || e.forceSubmit === true) {
            // Non ho i campi: permetto il submit
            return true;
        }
        campiValorizzati = fields.filter(function(idx, el) {
            return el.value !== undefined && el.value !== '';
        }).length;
        if(campiValorizzati === 0 || campiValorizzati === fields.length) {
            // Tutti i campi sono correttamente valorizzati
            return true;
        }
        // Chiedo di proseguire
        askProsecuzione();
        e.preventDefault();
        return false;
    }

    /**
     * Richiesta per la prosecuzione con il submit del form
     */
    function askProsecuzione() {
        var msg = 'COR_ERR_0009 - Il formato del parametro Dati repertorio/protocollo non &egrave; valido: Registro, Anno, Numero e Data devono essere tutti valorizzati o tutti non valorizzati. Proseguire?';
        bootboxAlert(msg, 'Attenzione', 'dialogWarn', [
            {label: 'No, indietro', 'class': 'btn', callback: $.noop},
            {label: 'S&igrave; prosegui', 'class': 'btn', callback: function(){
                // Forzo il submit
                var e = $.Event('submit');
                e.forceSubmit = true;
                $('form').trigger(e);
            }}
        ]);
    }

    return exports;
} ());

$(function() {
    // JIRA-1900 e' stato richiesto di  togliere la descrizione 'E' possibile associare all'impegno un soggetto o una classe di soggetti'
    $('#descrizioneSoggetto_modale_seleziona_soggetto').addClass('hide');
    // Preimposto la data scadenza
    var dataScadenzaField = $("#dataScadenzaDocumento");
    dataScadenzaField.length > 0 && dataScadenzaField.data("dataScadenza", dataScadenzaField.val());

    $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(Documento.apriModaleSoggetto);
});