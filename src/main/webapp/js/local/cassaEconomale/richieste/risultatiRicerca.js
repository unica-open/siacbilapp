/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, global) {
    'use strict';
    var totaleImporto = $("#totaleImporto");
    // SIAC-4618
    var hasNumeroRichiesta = $('#HIDDEN_hasNumeroRichiesta').val() === 'true';
    var hasData = $('#HIDDEN_hasData').val() === 'true';
    var hasStato = $('#HIDDEN_hasStato').val() === 'true';
    var hasNumeroSospeso = $('#HIDDEN_hasNumeroSospeso').val() === 'true';
    var hasNumeroMovimento = $('#HIDDEN_hasNumeroMovimento').val() === 'true';
    var hasRichiedente = $('#HIDDEN_hasRichiedente').val() === 'true';
    var hasImporto = $('#HIDDEN_hasImporto').val() === 'true';
    var hasNumeroMovimentoRendiconto = $('#HIDDEN_hasNumeroMovimentoRendiconto').val() === 'true';

    var exports = {};

    /**
     * Mostra la modale per la conferma della stampa
     * @param obj (any)    l'oggetto da mostrare
     * @param url (string) l'url da invocare
     */
    function mostraModaleConfermaStampaRicevuta(obj, url){
        bootbox.dialog('Si sta per elaborare la stampa Ricevuta, vuoi proseguire?', [
            {label: 'No, indietro', 'class': 'btn', callback: $.noop},
            {label: 'S&iacute;, prosegui', 'class': 'btn', callback: confermaStampaRicevuta.bind(undefined, obj, url, 'uid')}
        ], {animate: true, classes: 'dialogWarn', header: 'Conferma Stampa Ricevuta', backdrop: 'static'});
    }

    /**
     * Mostra la modale per la conferma della stampa del rendiconto
     * @param obj (any)   l'oggetto da mostrare
     * @param ev  (Event) l'evento scatenante l'invocazione
     */
    function mostraModaleConfermaStampaRicevutaRendiconto(obj, ev) {
        var url = $(ev.target).data('href');
        bootbox.dialog('Si sta per elaborare la stampa Ricevuta del rendiconto, vuoi proseguire?', [
            {label: 'No, indietro', 'class': 'btn', callback: $.noop},
            {label: 'S&iacute;, prosegui', 'class': 'btn', callback: confermaStampaRicevuta.bind(undefined, obj, url, 'uidRendiconto')}
        ], {animate: true, classes: 'dialogWarn', header: 'Conferma Stampa Ricevuta', backdrop: 'static'});
    }

    /**
     * Conferma della stampa della ricevuta
     * @param obj            (any)    l'oggetto da mostrare
     * @param url            (string) l'url da invocare
     * @param fieldToExtract (string) il campo da estrarre
     */
    function confermaStampaRicevuta(obj, url, fieldToExtract){
        var $form = $('<form>', {method: 'POST', action: url});
        var $input = $('<input>', {type:'hidden', name:'uidRichiestaDaStampare', value: obj[fieldToExtract]});
        $form.append($input);
        $('body').append($form);
        $form.submit();
    }

    /**
     * Impostazione della tabella.
     *
     * @param urlData         (String) l'URL da invocare per ottenere i dati
     * @param urlAnnullamento (String) l'URL da invocare per invocare l'annullamento
     */
    exports.impostaTabellaTipoOperazioniCassa = function(urlData, urlAnnullamento, urlAggiornamento, urlStampaRicevuta) {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : urlData,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna richiesta disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('.num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
            },
            aoColumnDefs: componiColonne(urlAnnullamento, urlAggiornamento, urlStampaRicevuta)
        };
        var startPage = +$('#HIDDEN_savedDisplayStart').val();
        if(!isNaN(startPage)) {
            opts.iDisplayStart = startPage;
        }

        $(tableId).substituteHandler("xhr", populateTotale);
        $(tableId).dataTable(opts);
    };

    /**
     * Formattazione del valore come danaro
     * @param value (any) il valore da formattare
     * @returns (string) il valore
     */
    function doFormatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return '';
    }
    /**
     * Aggiunge la classe tab_right
     * @param nTd (Node) il td
     */
    function tabRight(nTd) {
        nTd.className += 'tab_Right';
    }
    /**
     * Composizione delle colonne per il dataTable
     * @param urlAnnullamento   (String) l'URL da invocare per invocare l'annullamento
     * @param urlAggiornamento  (String) l'URL da invocare per invocare l'aggiornamento
     * @param urlStampaRicevuta (String) l'URL da invocare per invocare la stampa della ricevuta
     * @returns (any[]) l'array delle colonne
     */
    function componiColonne(urlAnnullamento, urlAggiornamento, urlStampaRicevuta) {
        var cols = [];
        var index = 0;

        if(hasNumeroRichiesta) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('numeroRichiesta')});
        }
        if(hasData) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('dataCreazione')});
        }
        if(hasStato) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('statoOperativoRichiestaEconomale')});
        }
        if(hasNumeroSospeso) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('numeroSospeso')});
        }
        if(hasNumeroMovimento) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('numeroMovimento')});
        }
        if(hasNumeroMovimentoRendiconto) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('numeroMovimentoRendiconto')});
        }
        if(hasRichiedente) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('richiedente')});
        }
        if(hasImporto) {
            cols.push({aTargets: [index++], mData: defaultPerDataTable('importoRichiesta', 0, doFormatMoney), fnCreatedCell: tabRight});
        }
        cols.push({aTargets: [index++], mData: defaultPerDataTable('azioni'), fnCreatedCell: handleAzioni(urlAnnullamento, urlAggiornamento, urlStampaRicevuta)});
        return cols;
    }

    /**
     * Gestione delle azioni
     * @param urlAnnullamento   (String) l'URL da invocare per invocare l'annullamento
     * @param urlAggiornamento  (String) l'URL da invocare per invocare l'aggiornamento
     * @param urlStampaRicevuta (String) l'URL da invocare per invocare la stampa della ricevuta
     * @returns (function (Node, string, any) => void) la funzione di legame sulle azioni
     */
    function handleAzioni(urlAnnullamento, urlAggiornamento, urlStampaRicevuta) {
        return function(nTd, sData, oData) {
            $(nTd).addClass('tab_Right')
                .find('a.annullaRichiestaEconomale')
                    .substituteHandler('click', gestioneAnnullamentoRichiestaEconomale.bind(undefined, oData, urlAnnullamento))
                    .end()
                .find('a.aggiornaRichiestaEconomale')
                    .substituteHandler('click', gestioneAggiornamentoRichiestaEconomale.bind(undefined, oData, urlAggiornamento))
                .end()
                .find('a.stampaRicevutaRichiesta')
                    .substituteHandler('click', mostraModaleConfermaStampaRicevuta.bind(undefined, oData, urlStampaRicevuta))
                .end()
                .find('a.stampaRicevutaRendiconto')
                    .substituteHandler('click', mostraModaleConfermaStampaRicevutaRendiconto.bind(undefined, oData));
        };
    }

    /**
     * Popola il totale.
     *
     * @param event    (Event)  l'evento scatenante l'invocazione
     * @param settings (Object) i settings del DataTable
     * @param json     (Object) la risposta della chiamata AJAX
     */
    function populateTotale(event, settings, json) {
        var totale = json.totale || 0;
        totaleImporto.html(totale.formatMoney());
    }

    /**
     * Gestione dell'annullamento della richiesta economale.
     *
     * @param obj (Object) l'oggetto da annullare
     * @param url (String) l'URL da invocare
     */
    function gestioneAnnullamentoRichiestaEconomale(obj, url) {
        // A seconda dello stato
        if(obj.statoOperativoDaRendicontare || obj.statoOperativoEvasa) {
            bootbox.dialog('Richiesta conclusa con il pagamento. Si desidera comunque proseguire?', [
                {label: 'No, indietro', 'class': 'btn', callback: $.noop},
                {label: 'S&iacute;, prosegui', 'class': 'btn', 'callback': aperturaModaleAnnullamentoRichiestaEconomale.bind(undefined, obj, url)}
            ], {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'});
        } else {
            aperturaModaleAnnullamentoRichiestaEconomale(obj, url);
        }
    }

    /**
     * Gestione dell'aggiornamento della richiesta economale.
     *
     * @param obj (Object) l'oggetto da aggiornare
     * @param url (String) l'URL da invocare
     */
    function gestioneAggiornamentoRichiestaEconomale(obj, url) {
        //se da rendicontare mostro il messaggio
        if(obj.statoOperativoDaRendicontare) {
            bootbox.dialog('CEC_INF_0016 - Aggiornamento di una richiesta su cui c\'&egrave; gi&agrave; stato un pagamento. Si desidera comunque proseguire?', [
                {label: 'No, indietro', 'class': 'btn', callback: $.noop},
                {label: 'S&iacute;, prosegui', 'class': 'btn', callback: aggiornaRichiestaEconomale.bind(undefined, obj, url)}
            ], {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'});
        } else if(obj.statoOperativoRendicontata || obj.statoOperativoAgliAtti || obj.statoOperativoEvasa) {
            bootbox.dialog('CEC_INF_0015 - Aggiornamento di una richiesta che &egrave; gi&agrave; stata rendicontata o evasa. Si desidera comunque proseguire?', [
                {label: 'No, indietro', 'class': 'btn', callback: $.noop},
                {label: 'S&iacute;, prosegui', 'class': 'btn', callback: aggiornaRichiestaEconomale.bind(undefined, obj, url)}
            ], {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'});
        } else {
            aggiornaRichiestaEconomale(obj, url);
        }
    }

    /**
     * Apertura del modale per l'annullamento della richiesta.
     *
     * @param obj (Object) l'oggetto da annullare
     * @param url (String) l'URL da invocare
     */
    function aperturaModaleAnnullamentoRichiestaEconomale(obj, url) {
        $('#elementoSelezionatoModaleAnnullamentoElemento').html(obj.numeroRichiesta);
        $('#modaleAnnullamentoElemento').modal('show');
        $('#confermaModaleAnnullamentoElemento').substituteHandler('click', annullaRichiestaEconomale.bind(undefined, obj, url));
    }

    /**
     * Annullamento della richiesta economale.
     *
     * @param obj (Object) l'oggetto da annullare
     * @param url (String) l'URL da invocare
     */
    function annullaRichiestaEconomale(obj, url) {
        $('form').attr('action', url)
            .append('<input type="hidden" name="uidRichiesta" value="' + obj.uid + '"/>')
            .submit();
    }

    /**
     * Aggiornamento della richiesta economale.
     *
     * @param obj (Object) l'oggetto da annullare
     * @param url (String) l'URL da invocare
     */
    function aggiornaRichiestaEconomale(obj, url) {
        $('form').attr('action', url)
            .append('<input type="hidden" name="uidRichiesta" value="' + obj.uid + '"/>')
            .submit();
    }

    // Esportazione delle funzionalita'
    global.RichiestaEconomale = exports;

}(jQuery, this);