/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    // Variabili pseudo-globali
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModaleSospensioneSoggetto = $("#ERRORI_modaleSospensioneSoggettoElenco");
    var datiNonUnivociModaleSospensioneSoggetto = $("#modaleSospensioneSoggettoElenco_datiNonUnivoci");
    var regexStrip = /<[^>]+>([^<]*)<[^>]+>|<[^\/]+\/>/ig;
    var containerAggiornaSpezza = $('#containerModaleAggiornamentoQuotaElenco, #containerSlideSpezzaQuotaElenco').slideUp();
    var pdcIndexSpezza;
    var pdcIndexAggiorna;

    var moreCols = [
        {aTargets: [10], mData: defaultPerDataTable('domStringAzioni'), fnCreatedCell: function(nTd, sData, oData) {
            var $nTd = $(nTd).addClass("tab_Right");
            $nTd.find(".aggiornaQuotaElenco")
                .substituteHandler("click", aggiornaQuotaElenco.bind(undefined, nTd, oData, "aggiornaAllegatoAtto_aggiornaSubdocumentoElenco.do"));
            $nTd.find(".eliminaQuotaElenco")
                .substituteHandler("click", eliminaQuotaElenco.bind(undefined, oData, "aggiornaAllegatoAtto_eliminaSubdocumentoElenco.do"));
            $nTd.find(".sospendiPagamentoSoggetto")
                .substituteHandler("click", sospendiPagamentoSoggetto.bind(undefined, oData, "aggiornaAllegatoAtto_sospendiPagamentoSoggetto.do"));
            $nTd.find(".riattivaPagamentoSoggetto")
                .substituteHandler("click", riattivaPagamentoSoggetto.bind(undefined, oData, "aggiornaAllegatoAtto_riattivaSoggettoSospeso.do"));
            $nTd.find(".spezzaQuotaElenco")
                .substituteHandler("click", spezzaQuotaElenco.bind(undefined, nTd, oData, "aggiornaAllegatoAtto_spezzaSubdocumentoElenco.do"));
        }}
    ];

    /**
     * Imposta i dati negli alert, e ricarica l'elenco.
     *
     * @param data               (Object) i dati dell'invocazione asincrona
     * @param elemento           (Object) l'elemento originale
     * @param modal              (jQuery) il modale da chiudere (Optional, default: undefined)
     * @param alertErroriDaUsare (jQuery) l'alert degli errori da utilizzare (Optional, default: $("#ERRORI"))
     * @return (any) un oggetto che specifica se ci sono errori
     */
    function impostaDatiERicaricaElenco(data, elemento, modal, alertErroriDaUsare) {
        var evt;
        var alt = alertErroriDaUsare || alertErrori;
        if(impostaDatiNegliAlert(data.errori, alt)) {
            return {errori: true};
        }
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        evt = $.Event("elencoSelected");
        evt.elenco = {uid: elemento.uidElencoDocumentiAllegato};
        evt.startPage = getPageTabellaCollegati();
        $("#tabellaElencoDocumentiAllegato").trigger(evt);
        modal && modal.modal("hide");
        return {errori: false};
    }

    /**
     * Aggiorna la quota nell'elenco.
     *
     * @param td       (Node)   il td su cui e' avvenuta l'invocazione
     * @param elemento (any)    la quota da aggiornare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function aggiornaQuotaElenco(td, elemento, url, e) {
        var row = $(td).closest('tr').overlay('show');
        // SIAC-5348: chiudo i collapse di aggiorna e spezza
        containerAggiornaSpezza.slideUp();
        e.preventDefault();
        destroyJsAggiorna();
        $.post('aggiornaAllegatoAtto_caricaDatiAggiornamentoSubdocumentoElenco.do', {uidSubdocumento: elemento.uid})
        .then(function(data) {
            var container = $("#containerModaleAggiornamentoQuotaElenco");
            if(typeof data !== 'string' && impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Imposto l'html nel contenitore
            container.html(data);
            // Lego le azioni al pulsante
            initJsAggiorna(elemento, url);
            // SIAC-5043: Apro lo slide
            container.slideDown()
                .scrollToSelf();

        }).always(row.overlay.bind(row, 'hide'));
    }

    /**
     * Distruzione delle funzionalita' JS di aggiornamento quota
     */
    function destroyJsAggiorna() {
        // Distruggo la gestione del pdc prima di togliere l'elemento dal DOM
        $("#pulsanteConfermaModaleAggiornamentoQuotaElenco").off("click");
        $("#pulsanteAnnullaModaleAggiornamentoQuotaElenco").off("click");
        ProvvisorioDiCassa.destroy(pdcIndexAggiorna);
    }

    /**
     * Inizializzazione delle funzionalita' JS di aggiornamento quota
     * @param elemento (any)    la quota da aggiornare
     * @param url      (string) l'URL da invocare
     */
    function initJsAggiorna(elemento, url) {
        var container = $('#containerModaleAggiornamentoQuotaElenco');

        // Copio il tipo di provvisorio
        $("#modale_hidden_tipoProvvisorioDiCassa").val($("#tipoProvvisorioDiCassaModale").val());
        pdcIndexAggiorna = ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassaModaleAggiornamentoQuota", "", "#annoProvvisorioCassaModale", "#numeroProvvisorioCassaModale");

        container.find("input.soloNumeri").allowedChars({numeric: true});
        container.find("input.decimale").gestioneDeiDecimali();
        $("#pulsanteConfermaModaleAggiornamentoQuotaElenco").substituteHandler("click", handleConfermaAggiornamento.bind(undefined, url, elemento));
        $("#pulsanteAnnullaModaleAggiornamentoQuotaElenco").substituteHandler("click", container.slideUp.bind(container));
    }

    /**
     * Spezza la quota dell'elenco.
     *
     * @param td       (Node)   il td su cui e' avvenuta l'invocazione
     * @param elemento (any)    la quota da aggiornare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function spezzaQuotaElenco(td, elemento, url, e) {
        var row = $(td).closest('tr').overlay('show');
        // SIAC-5348: chiudo i collapse di aggiorna e spezza
        containerAggiornaSpezza.slideUp();
        e.preventDefault();
        destroyJsSpezza();
        $.post('aggiornaAllegatoAtto_caricaDatiSpezzaSubdocumentoElenco.do', {uidSubdocumento: elemento.uid})
        .then(function(data) {
           var container = $("#containerSlideSpezzaQuotaElenco");
            if(typeof data !== 'string' && impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Imposto l'html nel contenitore
            $('#containerSlideSpezzaQuotaElenco').html(data);
            // Attivazione funzionalita' javascript
            initJsSpezza(elemento, url);
            // Apro lo slide
            container.slideDown()
                .scrollToSelf();

        }).always(row.overlay.bind(row, 'hide'));
    }

    /**
     * Distruzione delle funzionalita' JS di spezzamento quota
     */
    function destroyJsSpezza() {
        // Distruggo la gestione del pdc prima di togliere l'elemento dal DOM
        $("#pulsanteConfermaSpezzaQuotaElenco").off("click");
        $("#pulsanteAnnullaSpezzaQuotaElenco").off("click");
        ProvvisorioDiCassa.destroy(pdcIndexSpezza);
    }

    /**
     * Inizializzazione delle funzionalita' JS di spezzamento quota
     * @param elemento (any)    la quota da aggiornare
     * @param url      (string) l'URL da invocare
     */
    function initJsSpezza(elemento, url) {
        var container = $('#containerSlideSpezzaQuotaElenco');
        var datepicker = container.find("input.datepicker");
        var dataOdierna = new Date();
        var annoDatepicker = parseInt($("#HIDDEN_anno_datepicker").val(), 10);

        // Copio il tipo di provvisorio
        $("#modale_hidden_tipoProvvisorioDiCassa").val($("#slide_hidden_tipoProvvisorioDiCassa").val());
        pdcIndexSpezza = ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassa", "", "#nuovoAnnoPcSlide", "#nuovoNumeroPcSlide");

        container.find("input.soloNumeri").allowedChars({numeric: true});
        container.find("input.decimale").gestioneDeiDecimali();
        $("#pulsanteConfermaSpezzaQuotaElenco").substituteHandler("click", handleConfermaSpezza.bind(undefined, url, elemento));
        $("#pulsanteAnnullaSpezzaQuotaElenco").substituteHandler("click", container.slideUp.bind(container));
        
        // Gestione datepicker
        if(!isNaN(annoDatepicker)) {
            dataOdierna.setFullYear(annoDatepicker);
        }

        datepicker.each(function() {
            var self = $(this).datepicker({
                    weekStart: 1,
                    language: "it",
                    startDate: "01/01/1901",
                    autoclose: true
                }).attr("tabindex", -1);
            var originalDate = self.val();

            if(dataOdierna) {
                self.datepicker("update", dataOdierna)
                    .val(originalDate);
            }
        });
    }

    /**
     * Gestione della conferma dell'aggiornamento
     * @param url      (string) l'url da invocare
     * @param elemento (any)    il wrapper dell'elemento
     * @param e        (Event)  l'evento scatenante
     * @returns
     */
    function handleConfermaAggiornamento(url, elemento, e) {
        // Popolo l'oggetto per la chiamata AJAX
        var obj = $("#fieldsetModaleAggiornamentoQuotaElenco").serializeObject();
        // Attivo lo spinner
        var spinner = $("#SPINNER_pulsanteConfermaModaleAggiornamentoQuotaElenco").addClass("activated");

        $.postJSON(url, obj)
        .then(function(data) {
            var container = $("#containerModaleAggiornamentoQuotaElenco");
            var ret = impostaDatiERicaricaElenco(data, elemento);
            $(document).trigger('refreshElenchi');
            if(!ret.errori) {
                container.slideUp();
            }
        }).always(spinner.removeClass.bind(spinner, "activated"));
    }

    /**
     * Gestione della conferma dello spezzamento
     * @param url      (string) l'url da invocare
     * @param elemento (any)    il wrapper dell'elemento
     * @param e        (Event)  l'evento scatenante
     */
    function handleConfermaSpezza(url, elemento, e) {
        // Popolo l'oggetto per la chiamata AJAX
        var obj = $("#fieldsetSlideSpezzaQuotaElenco").serialize();
        // Attivo lo spinner
        var spinner = $("#SPINNER_pulsanteConfermaSpezzaQuotaElenco").addClass("activated");
        
        $.postJSON(url, obj)
        .then(function(data) {
            var ret = impostaDatiERicaricaElenco(data, elemento);
            $(document).trigger('refreshElenchi');
            
            if (!ret.errori) {
               $('#containerSlideSpezzaQuotaElenco').slideUp();
            }
        }).always(spinner.removeClass.bind(spinner, "activated"));
    }

    /**
     * Elimina la quota dall'elenco.
     *
     * @param elemento (any)    la quota da eliminare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function eliminaQuotaElenco(elemento, url, e) {
        var modal = $("#modaleEliminazione");
        var str = elemento.domStringDocumento || "";
        // Blocco l'evento
        e.preventDefault();
        // Strip per i tag HTML
        $("#spanElementoSelezionatoModaleEliminazione").html(str.replace(regexStrip, "$1"));
        $("#pulsanteSiModaleEliminazione").substituteHandler("click", function(e) {
            var spinner = $("#SPINNER_pulsanteSiModaleEliminazione").addClass("activated");
            var obj = {};

            e.preventDefault();
            // Popolo l'oggetto per l'invocazione
            obj["elencoDocumentiAllegato.uid"] = elemento.uidElencoDocumentiAllegato;
            obj[(elemento.isSubdocumentoSpesa ? "subdocumentoSpesa" : "subdocumentoEntrata") + ".uid"] = elemento.uid;

            $.postJSON(url, obj, function(data) {
                impostaDatiERicaricaElenco(data, elemento, modal);
                $(document).trigger('refreshElenchi');
            }).always(spinner.removeClass.bind(spinner, "activated"));
        });
        // Apro il modale
        modal.modal("show");
    }

    /**
     * Gestisce il soggetto.
     *
     * @param elemento (any)    la quota da adoperare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function gestioneSoggetto(elemento, url, e) {
        var modal = $("#modaleSospensioneSoggettoElenco");
        // Blocco l'evento
        e.preventDefault();
        // Copio i dati in input
        $("#datiSoggettoModaleSospensioneSoggettoElenco").html(elemento.codiceSoggetto && (': ' + elemento.codiceSoggetto) || '');
        $("#uidDatiSoggettoAllegato").val(elemento.uidDatiSoggetto || "");
        $("#uidSoggettoDatiSoggettoAllegato").val(elemento.uidSoggetto || '');
        $("#dataSospensioneDatiSoggettoAllegato").val(elemento.dataSospensioneSoggetto ? formatDate(elemento.dataSospensioneSoggetto) : "");
        $("#causaleSospensioneDatiSoggettoAllegato").val(elemento.causaleSospensioneSoggetto);
        $("#dataRiattivazioneDatiSoggettoAllegato").val(elemento.dataRiattivazioneSoggetto ? formatDate(elemento.dataRiattivazioneSoggetto) : "");
        // Chiudo gli alert
        alertErroriModaleSospensioneSoggetto.slideUp();
        datiNonUnivociModaleSospensioneSoggetto.slideUp();

        // Gestione dei datepicker
        $(".datepicker", modal).each(function() {
            var $this = $(this);
            var value = $this.val();
            $this.datepicker("update", value);
        });

        $("#pulsanteConfermaModaleSospensioneSoggettoElenco").substituteHandler("click", function(e) {
            var obj = $("#fieldsetModaleSospensioneSoggettoElenco").serializeObject();
            var spinner = $("#SPINNER_pulsanteConfermaModaleSospensioneSoggettoElenco").addClass("activated");
            obj["elencoDocumentiAllegato.uid"] = elemento.uidElencoDocumentiAllegato;
            e.preventDefault();
            $.postJSON(url, obj)
            .then(function(data) {
                impostaDatiERicaricaElenco(data, elemento, modal, alertErroriModaleSospensioneSoggetto);
            }).always(spinner.removeClass.bind(spinner, "activated"));
        });
        // Apro il modale
        modal.modal("show");
    }


    // SIAC-5172
    /**
     * Gestione soggetto per elenco
     * @param e (Event) l'evento scatenante
     */
    function gestioneSoggettoElenco(e) {
        var modal = $("#modaleSospensioneSoggettoElenco");
        var overlay = $(e.target).closest('tr').overlay('show');
        // Blocco l'evento
        e.preventDefault();
        // Pulisco i dati
        $("#datiSoggettoModaleSospensioneSoggettoElenco").html('');
        modal.find('input').val('');
        // Chiudo gli alert
        alertErroriModaleSospensioneSoggetto.slideUp();
        alertErroriModaleSospensioneSoggetto.slideUp();

        $.postJSON('aggiornaAllegatoAtto_ricercaDatiSospensioneElenco.do', {'elencoDocumentiAllegato.uid': e.uidElenco})
        .then(handleRicercaDatiSoggettoElenco.bind(undefined, e.uidElenco))
        .always(overlay.overlay.bind(overlay, 'hide'));

    }

    /**
     * Gestione dei dati soggetto elenco
     * @param uidElenco l'uid dell'elenco
     * @param data (any) i parametri del servizio
     */
    function handleRicercaDatiSoggettoElenco(uidElenco, data) {
        var modal = $("#modaleSospensioneSoggettoElenco");

        // Prepopolo i dati se applicabile
        if(data.datiSoggettoAllegatoDeterminatiUnivocamente && data.datiSoggettoAllegato) {
            // Ripeto pe sicurezza
            datiNonUnivociModaleSospensioneSoggetto.slideUp();
            // TODO: dinamicizzare
            $('#dataSospensioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.dataSospensione);
            $('#causaleSospensioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.causaleSospensione);
            $('#dataRiattivazioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.dataRiattivazione);
        } else {
            // Mostro il div di dati non univoci
            datiNonUnivociModaleSospensioneSoggetto.slideDown();
        }

        $(".datepicker", modal).each(function() {
            var $this = $(this);
            var value = $this.val();
            $this.datepicker("update", value);
        });

        $("#pulsanteConfermaModaleSospensioneSoggettoElenco").substituteHandler("click", handleConfermaSospensioneSoggetto.bind(undefined, uidElenco));
        // Apro il modale
        modal.modal("show");
    }
    
    /**
     * Gestione della conferma della sospensione del soggetto
     * @param uidElenco (Number) l'uid dell'elenco
     * @param evt       (Event)  l'evento scatenante
     */
    function handleConfermaSospensioneSoggetto(uidElenco, evt) {
        var modal = $("#modaleSospensioneSoggettoElenco");
        var obj = $("#fieldsetModaleSospensioneSoggettoElenco").serializeObject();
        var spinner = $("#SPINNER_pulsanteConfermaModaleSospensioneSoggettoElenco").addClass("activated");

        obj["elencoDocumentiAllegato.uid"] = uidElenco;
        evt.preventDefault();
        $.postJSON('aggiornaAllegatoAtto_sospendiTuttoElenco.do', obj)
        .then(sospendiTuttoCallback.bind(undefined, uidElenco, modal))
        .always(spinner.removeClass.bind(spinner, "activated"));
    }

    /**
     * Callback di sospensione di tutti i dati
     * @param uidElenco (Number) l'uid dell'elenco
     * @param modal     (jQuery) la modale
     * @param data      (any)    i dati del servizio
     */
    function sospendiTuttoCallback(uidElenco, modal, data) {
        var $dettagli = $('#dettaglioElementiCollegati');
        var evt;
        if(impostaDatiNegliAlert(data.errori, alertErroriModaleSospensioneSoggetto)) {
            return $.Deferred().reject().promise();
        }
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        if(!$dettagli.is(':hidden')) {
            evt = $.Event("elencoSelected");
            evt.elenco = {uid: uidElenco};
            evt.startPage = getPageTabellaCollegati();

            $("#tabellaElencoDocumentiAllegato").trigger(evt);
        }
        modal.modal('hide');
    }
    
    /**
     * Ottiene la pagina dei collegati
     * @return (number) la pagina iniziale
     */
    function getPageTabellaCollegati() {
        var $tabellaDettagli = $('#tabellaDettaglioElementiCollegati');
        if (!$.fn.DataTable.fnIsDataTable($tabellaDettagli.get(0)) || !$tabellaDettagli.is(':visible')) {
            return undefined;
        }
        return $tabellaDettagli.dataTable().fnSettings()._iDisplayStart;
    }

    /**
     * Sospende il pagamento del soggetto.
     *
     * @param elemento (any)    la quota da adoperare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function sospendiPagamentoSoggetto(elemento, url, e) {
        gestioneSoggetto(elemento, url, e);
    }

    /**
     * Riattiva il pagamento al soggetto.
     *
     * @param elemento (any)    la quota da adoperare
     * @param url      (string) l'URL da invocare
     * @param e        (Event)  l'evento scatenante
     */
    function riattivaPagamentoSoggetto(elemento, url, e) {
        gestioneSoggetto(elemento, url, e);
    }

    /**
     * Ricarica gli elenchi dell'allegato
     */
    function ricaricaElenchi() {
        var table = $("#tabellaElencoDocumentiAllegato_wrapper").overlay("show");
        ElencoDocumentiAllegato.caricaElenchiAggiornamento("aggiornaAllegatoAtto_ricalcolaListaElenchi.do", "#tabellaElencoDocumentiAllegato", "aggiornaAllegatoAtto_eliminaElenco.do")
        .then(impostaTotaliTabellaElenchi)
        .always(table.overlay.bind(table, "hide"));
    }
    
    function impostaTotaliTabellaElenchi(data) {
        var totaleEntrataListaElencoDocumentiAllegato = data.totaleEntrataListaElencoDocumentiAllegato || 0;
        var totaleSpesaListaElencoDocumentiAllegato = data.totaleSpesaListaElencoDocumentiAllegato || 0;
        var totaleNettoListaElencoDocumentiAllegato = data.totaleNettoListaElencoDocumentiAllegato || 0;

        $("#numeroElenchiCollegatiAllegatoAtto").html(data.listaElencoDocumentiAllegato && data.listaElencoDocumentiAllegato.length || 0);
        $("#totaleEntrataAllegatoAtto").html(totaleEntrataListaElencoDocumentiAllegato.formatMoney());
        $("#totaleSpesaAllegatoAtto").html(totaleSpesaListaElencoDocumentiAllegato.formatMoney());
        $("#totaleNettoAllegatoAtto").html(totaleNettoListaElencoDocumentiAllegato.formatMoney());
    }

    $(function() {
        // Caricamento elenchi
        ElencoDocumentiAllegato.caricaElenchiAggiornamento("aggiornaAllegatoAtto_ottieniListaElenchi.do", "#tabellaElencoDocumentiAllegato",
            "aggiornaAllegatoAtto_eliminaElenco.do");
        // Inizializzo la gestione dell'elenco
        ElencoDocumentiAllegato.inizializza("#pulsanteApriModaleAssociaElencoDocumentiAllegato", "aggiornaAllegatoAtto_associaElenco.do",
            undefined, undefined, undefined, true, false, "#formInserimentoAllegatoAttoStep2", "#tabellaElencoDocumentiAllegato", true);

        // Lego l'azione relativa al caricamento del dettaglio dell'elenco
        $(document).on("elencoSelected", "#tabellaElencoDocumentiAllegato", function(e) {
            ElencoDocumentiAllegato.caricaDettaglioElenco(e.elenco, "ricercaDettaglioElencoDocumentiAllegato.do", "#tabellaDettaglioElementiCollegati",
                "#divElenchi", "#dettaglioElementiCollegati", moreCols, undefined, e.startPage);
        });
        $(document).on("refreshElenchi", ricaricaElenchi)
        	.on('elencoEliminato', impostaTotaliTabellaElenchi)
        	.on('sospendiTuttoElenco', gestioneSoggettoElenco);
        // SIAC-5348: chiudo i collapse di aggiorna e spezza al cambio tab
        $('a[data-toggle="tab"]').on('show', containerAggiornaSpezza.slideUp.bind(containerAggiornaSpezza));
    });
}(jQuery);