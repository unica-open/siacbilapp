/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($, doc) {
    var exports = {};
    var campoRicercaEffettuataConSuccesso;
    var gestioneUEB = $("#HIDDEN_gestioneUEB").val() === "true";
    var mapCodiceModalitaPagamentoNoteCertificazione = {
        PI: "contenzioso in corso",
        FA: "fallimento in corso",
        CSI: "cesione di credito",
        CSC: "cessione di credito"
    };
    // SIAC-2765: creo un default con operazione vuota per evitare di modificare troppo codice
    var Ritenute = Documento.Ritenute || {caricaListaRitenute: $.noop};
    // SIAC-5115
    var modaleSospensioneQuota = $('#modaleDatiSospensioneQuota');
    var currentDataScadenza;

    /**
     * Lancia l'informazione sull'aggiornamento delle quote
     *
     * @param flagIvaDisponibile (Boolean) se l'iva sia disponibile
     */
    function triggerQuoteAggiornate(flagIvaDisponibile) {
        var li = $("a[href='#tabDatiIva']").parent("li");
        var div = $("#tabDatiIva");
        $(document).trigger("quoteAggiornate");
        if(flagIvaDisponibile) {
            li.removeClass("hide");
            div.removeClass("hide");
        } else {
            li.addClass("hide");
            div.addClass("hide");
        }
    }

    /**
     * Distrugge il collapse della quota.
     *
     * @param e (Event) l'evento scatenante
     */
    function distruggiCollapseQuota(e) {
        e.preventDefault();
        $("#accordionInserisciQuotaSpesa").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
    }

    /**
     * disabilita i campi relativi a soggetto -->codice
     *                               nasconde --> compilazione guidata
     */
    function disabilitaCampiSoggetto() {
        //parte aggiunta il 26_03_2015 jira 1905

        $("#codiceSoggetto").removeAttr('required');
        $("#codiceSoggetto").attr('disabled', true);
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").addClass('hide');
    }
    
    /**
     * Gestione della richiesta di prosecuzione.
     *
     * @param array       (Array) l'array di messaggi richiedenti conferma
     * @param url         (String) l'url da invocare
     * @param data        (Object) l'oggetto da fornire come parametro
     * @param endCallback (Function) la funzione di callback
     */
    function gestioneRichiestaProsecuzione(array, url, data, endCallback) {
        var alertModale = $("#alertModaleConfermaProsecuzioneSuAzione");
        alertModale.find("ul")
            .empty();

        impostaDatiNegliAlert(array, $("#alertModaleConfermaProsecuzioneSuAzione"), undefined, true);

        $("#modaleConfermaProsecuzioneSuAzionePulsanteSi").substituteHandler("click", function() {
            var spinner = $("#SPINNER_modaleConfermaProsecuzioneSuAzione").addClass("activated");
            var conferma = $("#modaleConfermaProsecuzioneSuAzionePulsanteSi");
            conferma.addClass("disabled");
            conferma.attr("disabled", true);
            conferma.prop("disabled", true);
            data.proseguireConElaborazione = true;
            data.proseguireConElaborazioneAttoAmministrativo = true;
            $.postJSON(url, data, endCallback)
            .always(function() {
                spinner.removeClass("activated");
                conferma.removeClass("disabled");
                conferma.removeAttr("disabled");
                conferma.removeProp("disabled");
                $("#modaleConfermaProsecuzioneSuAzione").modal("hide");
            });
        });

        $("#modaleConfermaProsecuzioneSuAzione").modal("show");
    }

    /**
     * Salva la nuova quota all'interno del documento.
     *
     * @param e (Event) l'evento scatenante
     */
    function salvaNuovaQuota(e) {
        var fieldset = $("#fieldsetInserisciQuota");
        var oggettoPerChiamataAjax = fieldset.serializeObject();
        var spinner = $("#SPINNER_pulsanteQuotaSalva");
        var pulsanteSalva = $("#pulsanteSalvaInserisciSubdocumento");
        var url = "aggiornamentoDocumentoSpesa_inserimentoNuovaQuota.do";
        e.preventDefault();

        spinner.addClass("activated");
        pulsanteSalva.addClass("disabled");
        pulsanteSalva.attr("disabled", true);
        pulsanteSalva.prop("disabled", true);

        doc.alertErrori.slideUp();
        doc.alertMessaggi.slideUp();
        doc.alertInformazioni.slideUp();
        fieldset.addClass('form-submitted');

        $.postJSON(url, oggettoPerChiamataAjax)
        .then(function(data) {
            var array;
            var index;
            // Eventuali errori
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                array = data.errori;
                for(index = 0; index < array.length; index++) {
                    if(array[index].codice === "FIN_ERR_0269"){
                        $("#mutuoMovimentoGestione").val("");
                    }
                }
                return;
            }
            // Eventuale messaggi: carico la richiesta all'utente
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, oggettoPerChiamataAjax, completaInserimentoQuota);
                return;
            }
            //se il documento non e' piu incompleto disabilito l'editabilità dei campi soggetto
            if(data.documentoIncompleto===false) {
                disabilitaCampiSoggetto();
            }
            completaInserimentoQuota(data);
        }).always(function() {
            spinner.removeClass("activated");
            pulsanteSalva.removeClass("disabled");
            pulsanteSalva.removeAttr("disabled");
            pulsanteSalva.removeProp("disabled");
            fieldset.removeClass('form-submitted');
        });
    }

    /**
     * Completamento dell'inserimento della quota.
     *
     * @param data i dati ottenuti dal servizio
     */
    function completaInserimentoQuota(data) {
        // Nascondo il modale se ho finito
        if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
            impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);
            var array = data.errori;
            var index;
            for(index = 0; index < array.length; index++) {
                if(array[index].codice === "FIN_ERR_0269"){
                    $("#mutuoMovimentoGestione").val("");
                }
            }
            return;
        }
        
        data.messaggi = data.messaggi || [];
        
        impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni, undefined, true);
        
        impostaDatiNegliAlert([], doc.alertMessaggi, undefined, true);
        impostaDatiNegliAlert(data.messaggi.concat(data.messaggiSenzaRichiestaConferma), doc.alertMessaggi, undefined, false);
        
        caricaDataTableQuote(data.listaSubdocumentoSpesa, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire, data.contabilizza);
        $("#accordionInserisciQuotaSpesa").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
        popolaTabellaDettaglioQuote("tabella_modaleDettaglioQuoteQuote", data.listaSubdocumentoSpesa);
        $("#HIDDEN_flagSplitQuotePresente").val(data.flagSplitQuotePresente);
        gestisciVisibilitaCalcoloSplitAuto(data.flagAutoCalcoloImportoSplitQuote);
        // Lancio l'evento di aggiornamento delle quote, nel caso qualcuno stia ascoltando
        triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
        $("#nettoDocumento").val(data.netto.formatMoney());
        $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
        $("#statoDocumento").html(data.stato);
        $("#stato_tabNote").html(data.stato);
        $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
        Ritenute.caricaListaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
        
        $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
        // SIAC-5072
        $('div[data-div-sospensione]')[data.flagDatiSospensioneEditabili ? 'removeClass' : 'addClass']('hide');
    }

    /**
     * Aggiorna la quota.
     *
     * @param e (Event)  l'evento scatenante
     */
    function aggiornaQuota(e) {
        var fieldset = $("#fieldsetInserisciQuota");
        var oggettoPerChiamataAjax = fieldset.serializeObject();
        var spinner = $("#SPINNER_pulsanteQuotaSalva");
        var url = "aggiornamentoDocumentoSpesa_aggiornamentoQuota.do";
        var pulsanteSalva = $("#pulsanteSalvaInserisciSubdocumento");
        e.preventDefault();

        spinner.addClass("activated");
        pulsanteSalva.addClass("disabled");
        pulsanteSalva.attr("disabled", true);
        pulsanteSalva.prop("disabled", true);

        doc.alertErrori.slideUp();
        doc.alertInformazioni.slideUp();
        fieldset.addClass('form-submitted');

        $.postJSON(url, oggettoPerChiamataAjax)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
               // impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, true);
                var array = data.errori;
                var index;
                for(index = 0; index < array.length; index++) {
                    if(array[index].codice === "FIN_ERR_0269"){
                        $("#mutuoMovimentoGestione").val("");
                    }
                }
                return;
            }

            // Eventuale messaggi: carico la richiesta all'utente
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, oggettoPerChiamataAjax, completaAggiornamentoQuota);
                return;
            }
            
            //se il documento non e' piu incompleto disabilito l'editabilità dei campi soggetto
            if(data.documentoIncompleto===false) {
                disabilitaCampiSoggetto();
            }

            completaAggiornamentoQuota(data);
        }).always(function() {
            spinner.removeClass("activated");
            pulsanteSalva.removeClass("disabled");
            pulsanteSalva.removeAttr("disabled");
            pulsanteSalva.removeProp("disabled");
            fieldset.removeClass('form-submitted');
        });
    }

    /**
     * Completa l'aggiornamento della quota.
     *
     * @param data i dati forniti dal servizio
     */
    function completaAggiornamentoQuota(data) {
        if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
            impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);
            var array = data.errori;
            var index;
            for(index = 0; index < array.length; index++) {
                if(array[index].codice === "FIN_ERR_0269"){
                    $("#mutuoMovimentoGestione").val("");
                }
            }
            return;
        }

        // Eventuale errore sui totali
        impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
        caricaDataTableQuote(data.listaSubdocumentoSpesa, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire, data.contabilizza);
        $("#accordionInserisciQuotaSpesa").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
        popolaTabellaDettaglioQuote("tabella_modaleDettaglioQuoteQuote", data.listaSubdocumentoSpesa);
        $("#HIDDEN_flagSplitQuotePresente").val(data.flagSplitQuotePresente);
        gestisciVisibilitaCalcoloSplitAuto(data.flagAutoCalcoloImportoSplitQuote);
        triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
        $("#nettoDocumento").val(data.netto.formatMoney());
        $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
        $("#statoDocumento").html(data.stato);
        $("#stato_tabNote").html(data.stato);
        $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
        Ritenute.caricaListaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
        
        impostaDatiNegliAlert(data.messaggiSenzaRichiestaConferma, $('#MESSAGGI'));
        
        $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
        // SIAC-5072
        $('div[data-div-sospensione]')[data.flagDatiSospensioneEditabili ? 'removeClass' : 'addClass']('hide');
    }

    /**
     * Imposta la tabella dei subimpegni.
     *
     * @param impegno (Object) l'impegno da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubimpegniNellaTabella(impegno) {
        var options = {
            bServerSide: false,
            bPaginate: false,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            aaData: impegno.elencoSubImpegni,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti subimpegni associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun subimpegno disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return "<input type='radio' name='radio_modale_impegno' />";
                },
                fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalImpegno", impegno)
                    .data("originalSubImpegno", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numero')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('soggetto.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('disponibilitaLiquidare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        $("#tabellaImpegniModale").dataTable(options);
    }
    
    function formatMoney(val) {
        return typeof val === 'number' ? val.formatMoney() : val;
    }
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
    }
    
    /**
     * Imposta la tabella dei subimpegni.
     *
     * @param impegno (Object) l'impegno da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubimpegniPaginatiNellaTabella(impegno) {
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaSubImpegniAjax.do",
            sServerMethod: "POST",
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
                sZeroRecords: "Non sono presenti subimpegni associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun subimpegno disponibile"
                }
            },
            fnServerData: function ( sSource, aoData, fnCallback, oSettings ) {
            	var table =  $('#tabellaImpegniModale_wrapper');
                oSettings.jqXHR = $.ajax({
                	dataType: 'json',
                	type: "POST",
                	url: sSource,
                	data: aoData,
                	beforeSend: table.overlay.bind(table, 'show'),
                	success: fnCallback
                }).always(table.overlay.bind(table, 'hide'));
        	},
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return "<input type='radio' name='radio_modale_impegno' />";
                },
                fnCreatedCell: function(nTd, sData, oData) {
                	//TODO: questo lo devo settare prima!! ora non ho i dati?
                    $("input", nTd).data("originalImpegno", impegno)
                    .data("originalSubImpegno", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numero')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('soggetto.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('disponibilitaLiquidare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };        
        $("#tabellaImpegniModale").dataTable(options);
    }

    /**
     * Imposta la tabella dei mutui.
     */
    function impostaMutuiNellaTabella(lista) {
        var opts = {
            bServerSide: false,
            bPaginate: false,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            aaData: lista,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords : "Non sono presenti mutui associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun mutuo disponibile"
                }
            },
            aoColumnDefs : [
                {aTargets: [0], mData: function(source) {
                    return "<input type='radio' name='radio_mutuo_modale_impegno' />";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalMutuo", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numeroMutuo')},
                {aTargets: [2], mData: defaultPerDataTable('descrizioneMutuo')},
                {aTargets: [3], mData: defaultPerDataTable('istitutoMutuante.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttualeVoceMutuo', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('importoDisponibileLiquidareVoceMutuo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        $("#tabellaMutuiModale").dataTable(opts);
    }

    function impostaImpegnoNellaTabella(impegno) {
        var capitolo = "";
        var provvedimento = "";
        var soggetto = "";
        var zero = 0;
        var importo = (impegno.importoAttuale || zero).formatMoney() + " (iniziale: " + (impegno.importoIniziale || zero).formatMoney() + ")";
        var disponibile = (impegno.disponibilitaLiquidare || zero).formatMoney();

        if(impegno.capitoloUscitaGestione && impegno.capitoloUscitaGestione.uid !== 0){
            capitolo = calcolaStringaCapitolo(impegno.capitoloUscitaGestione);
        }

        if(impegno.attoAmministrativo){
            provvedimento = calcolaStringaProvvedimento(impegno.attoAmministrativo);
        }

        if(impegno.soggetto){
            soggetto = calcolaStringaSoggetto(impegno.soggetto);
        } else if (impegno.classeSoggetto){
            soggetto = "Classe: " + impegno.classeSoggetto.codice + " - " + impegno.classeSoggetto.descrizione;
        }

        $("#tabellaImpegnoModale").data('originalImpegno', impegno);
        $("#tabellaImpegno_tdCapitolo").html(capitolo);
        $("#tabellaImpegno_tdProvvedimento").html(provvedimento);
        $("#tabellaImpegno_tdSoggetto").html(soggetto);
        $("#tabellaImpegno_tdImporto").html(importo);
        $("#tabellaImpegno_tdDisponibile").html(disponibile);
        
        if( impegno && impegno.elencoSubImpegni.length === 0){
        	//vuole dire ho soltanto un impegno senza sub 
        	$('#tabellaImpegnoModale').data('originalImpegno', impegno);
        }
    }

    /**
     * Calcolo della stringa del capitolo.
     *
     * @param cap (Object) il capitolo
     */
    function calcolaStringaCapitolo(cap) {
        var res = cap.annoCapitolo + "/" + cap.numeroCapitolo + "/" + cap.numeroArticolo;
        // Gestione UEB
        res += gestioneUEB ? cap.numeroUEB : "";

        res += " - " + cap.descrizione;
        // Struttura Amministratico Contabile
        res = cap.strutturaAmministrativoContabile ? res + " - " +  cap.strutturaAmministrativoContabile.codice : res;
        // Tipo Finanziamento
        res = cap.tipoFinanziamento ? res + " - Tipo finanziamento: " + cap.tipoFinanziamento.codice : res;
        return res;
    }
    
    /**
     * Calcolo della stringa del capitolo .
     *
     * @param cap (Object) il capitolo
     */
    function calcolaStringaCap(cap) {
        var res = cap.annoCapitolo + "/" + cap.numeroCapitolo + "/" + cap.numeroArticolo;
        // Gestione UEB scommentare se c'e bisogno vedere la ueb
        //res += gestioneUEB ? cap.numeroUEB : "";
        return res;
    }

    /**
     * Calcolo della stringa del provvedimento.
     *
     * @param provv (Object) il provvedimento
     */
    function calcolaStringaProvvedimento(provv) {
        var res = provv.anno + "/"+ provv.numero;
        res = provv.tipoAtto ? res + " - " + provv.tipoAtto.descrizione : res;
        res = res + " - " + provv.oggetto;
        res = provv.strutturaAmmContabile ? res + " - " + provv.strutturaAmmContabile.codice : res;
        return res;
    }
    /**
     * Calcolo della stringa del provvedimento con meno dettagli 
     *
     * @param provv (Object) il provvedimento
     */
    function calcolaStringaProvv(provv) {
        var res = provv.anno + "/"+ provv.numero;
        res = provv.tipoAtto ? res + "/" + provv.tipoAtto.codice : res;
        return res;
    }
    /**
     * Calcolo della stringa del soggetto.
     *
     * @param sog (Object) il soggetto
     */
    function calcolaStringaSoggetto(sog) {
        var res = sog.codiceSoggetto || "";
        res = sog.denominazione ? res + " - " +  sog.denominazione : res;
        res = sog.codiceFiscale ? res + " - CF: " + sog.codiceFiscale : res;
        res = sog.partitaIva ? res + " - P.IVA: " + sog.partitaIva : res;
        return res;
    }

    /**
     * Carica il dataTable delle quote (subdocumento).
     *
     * @param lista               (Array)  la lista tramite cui popolare la tabella
     * @param totaleQuote         (Number) il totale delle quote
     * @param totaleDaPagareQuote (Number) il totale da pagare delle quote
     * @param importoDaAttribuire (Number) l'importo da attribuire
     */
    function caricaDataTableQuote(lista, totaleQuote, totaleDaPagareQuote, importoDaAttribuire, contabilizza) {
        var options = {
            bServerSide: false,
            bPaginate: false,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            aaData: lista || [],
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti quote associate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: function(source) {
                    var result = "";
                    if(source.impegno && source.impegno.numero !== undefined && source.impegno.numero !== null) {
                        // Solo se l'impegno e' presente
                        result = "" + source.impegno.annoMovimento + " / " + source.impegno.numero;
                        if(source.subImpegno) {
                            result += " - " + source.subImpegno.numero;
                            }
                        }
                    return result;
                }},
                {aTargets: [2], mData: function(source) {
                    var result = "";
                    if(source.attoAmministrativo) {
                        // Solo se l'impegno e' presente
                        result = "<a rel='popover' data-original-title=\"oggetto\" data-trigger=\"hover\" data-placement=\"left\" data-content=\"" + escapeHtml(source.attoAmministrativo.oggetto) + "\">"
                            + source.attoAmministrativo.anno + " / "
                            + source.attoAmministrativo.numero + " / "
                            + source.attoAmministrativo.tipoAtto.codice;
                        if(source.attoAmministrativo.strutturaAmmContabile) {
                            result += " / " + source.attoAmministrativo.strutturaAmmContabile.codice + "</a>";
                        }
                    }
                    return result;
                }, fnCreatedCell : function(nTd) {
                    $("a", nTd).popover();
                }},
                {aTargets: [3], mData: function(source) {
                    return source.liquidazione ? source.liquidazione.annoLiquidazione + "/" + source.liquidazione.numeroLiquidazione: "";
                }},
                {aTargets: [4], mData: function(source) {
                    var result = "";
                    var ordinativo = source.ordinativo;
                    if(ordinativo) {
                        result = ordinativo.anno + "/" + ordinativo.numero;
                    }
                    return result;
                }},
                {aTargets: [5], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: function(source) {
                    var hasLiquidazione = source.liquidazione;
                    var numRegIVANonValorizzato = (source.numeroRegistrazioneIVA == null || source.numeroRegistrazioneIVA.length === 0);
                    var quotaPagatoCEC = !!source.pagatoInCEC;
                    
                    var azioni = "<div class='btn-group'>" +
                        "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                        "<ul class='dropdown-menu pull-right'>";
                    if((!hasLiquidazione || source.liquidazione.statoOperativoLiquidazione === 'PROVVISORIO') /*&& numRegIVANonValorizzato*/ && !quotaPagatoCEC){
                        azioni +=    "<li><a href='#' class='aggiornaQuota'>aggiorna</a></li>";
                    }
                    azioni +=        "<li><a href='#' class='ripetiQuota'>ripeti</a></li>" ;
                    if(!hasLiquidazione && numRegIVANonValorizzato && !quotaPagatoCEC){
                        azioni +=    "<li><a href='#' class='eliminaQuota'>elimina</a></li>";
                    }
                    if(contabilizza){
                        azioni += "<li><a href='#' class='contabilizzaQuota'>contabilizza</a></li>";
                    }
                    azioni +=     "</ul>" +
                        "</div>";
                    return azioni;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    var $nTd = $(nTd).addClass("tab_Right");
                    $nTd.find(".aggiornaQuota")
                        .substituteHandler("click", aggiornaQuotaDocumento.bind(undefined, oData));
                    $nTd.find(".ripetiQuota")
                        .substituteHandler("click", ripetiQuotaDocumento.bind(undefined, oData));
                    $nTd.find(".eliminaQuota")
                        .substituteHandler("click", apriModaleEliminaQuotaDocumento.bind(undefined, iRow, oData));
                    $nTd.find(".contabilizzaQuota")
                        .substituteHandler("click", contabilizzaQuotaDocumento.bind(undefined, oData));
                }}
            ]
        };
        var table = $("#tabellaQuoteDocumento");
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        table.dataTable(options);

        // Imposto i totali
        $("#SPAN_importoDaAttribuireDocumento").html(importoDaAttribuire.formatMoney());
        $("#SPAN_totaleDaPagareQuoteIntestazione").html(totaleDaPagareQuote.formatMoney());
        $("#thTotaliQuote").html(totaleQuote.formatMoney());
        $("#thTotaleDaPagareQuote").html(totaleDaPagareQuote.formatMoney());
        $("#totaleQuote_tabNote").html(totaleQuote.formatMoney());
    }

    /**
     * Contabilizzazione della quota del documento.
     *
     * @param subdoc (Object) il subdocumento di riferimento
     * @param e      (Event)  l'evento scatenante l'invocazione
     */
    function contabilizzaQuotaDocumento(subdoc, e) {
        e.preventDefault();
        // Redirigo verso la action
        document.location = "aggiornamentoDocumentoSpesa_contabilizzaQuota.do?subdocumento.uid=" + subdoc.uid;
    }

    /**
     * Caricamento della tabella del dettaglio delle quote
     *
     * @param idTabella {String} l'id della tabella
     * @param lista     {Array}  la lista da impostare
     */
    function popolaTabellaDettaglioQuote(idTabella, lista) {
        var options = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            aaData: lista,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti quote associate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs : [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: function(source) {
                    if(!source.stringaImpegno && source.impegno) {
                        // Solo se l'impegno e' presente
                        source.stringaImpegno = "" +source.impegno.annoMovimento + "/" + source.impegno.numero;
                        if(source.subImpegno) {
                            source.stringaImpegno += " - " + source.subImpegno.numero;
                        }
                    }
                    return source.stringaImpegno || "";
                }},
                {aTargets: [2], mData: function(source) {
                    if(!source.stringaProvvedimento && source.attoAmministrativo) {
                        // Solo se l'impegno e' presente
                        source.stringaProvvedimento = "<a rel='popover' href='#' data-trigger=\"hover\" data-original-title=\"Oggetto\" data-content=\"" + escapeHtml(source.attoAmministrativo.oggetto) + "\">"
                            + source.attoAmministrativo.anno + "/"
                            + source.attoAmministrativo.numero + "/"
                            + source.attoAmministrativo.tipoAtto.codice.toUpperCase();
                        if(source.attoAmministrativo.strutturaAmmContabile) {
                            source.stringaProvvedimento += "/" + source.attoAmministrativo.strutturaAmmContabile.codice;
                        }
                        source.stringaProvvedimento += "</a>";
                    }
                    return source.stringaProvvedimento || "";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    var anchor = $("a", nTd);
                    anchor.on("click", function(e) {
                        e.preventDefault();
                    });
                    // No-wrap
                    $(nTd).addClass("whitespace-pre");
                }},
                {aTargets: [3], mData: function(source) {
                    return source.liquidazione ? source.liquidazione.annoLiquidazione + "/" + source.liquidazione.numeroLiquidazione: "";
                }},
                {aTargets: [4], mData: function(source) {
                    return source.ordinativo ? source.ordinativo.anno + "/" + source.ordinativo.numero: "";
                }},
                {aTargets: [5], mData: defaultPerDataTable('importoDaDedurre', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };

        $("#" + idTabella).dataTable(options);
    }

    /**
     * Apre il modale per l'eliminazione della quota del documento.
     *
     * @param row   (Number) la riga della quota da cancellare
     * @param quota (object) la quota da eliminare
     * @param ev    (Event)  l'evento scatenante
     */
    function apriModaleEliminaQuotaDocumento(row, quota, ev) {
        var modal = $("#modaleConfermaEliminazioneQuota");

        ev.preventDefault();

        $("#SPAN_elementoSelezionatoEliminazioneQuota").html(quota.numero);
        // Se clicco su no, chiudo il modale
        $("#pulsanteNoEliminazioneQuota").substituteHandler("click", function(e) {
            e.preventDefault();
            modal.modal("hide");
        });
        // Se clicco su si', allora elimino la quota
        $("#pulsanteSiEliminazioneQuota").substituteHandler("click", function(e) {
            eliminaQuotaDocumento(e, row);
        });

        modal.modal("show");
    }

    /**
     * Aggiorna la quota del documento.
     *
     * @param subdocumento (Object) il subdocumento da aggiornare
     * @param e            (Event)  l'evento scatenante
     */
    function aggiornaQuotaDocumento(subdocumento, e) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoSpesa_inizioAggiornamentoNuovaQuota.do", {uidQuota: subdocumento.uid}, e, aggiornaQuota);
    }

    /**
     * Ripeti l'inserimento della quota del documento.
     *
     * @param subdocumento (Object) il subdocumento di cui ripetere l'inserimento
     * @param e            (Event)  l'evento scatenante
     */
    function ripetiQuotaDocumento(subdocumento, e) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoSpesa_inizioRipetiNuovaQuota.do", {uidQuota: subdocumento.uid}, e, salvaNuovaQuota);
    }

    /**
     * Elimina la quota del documento.
     *
     * @param e   (Event)  l'evento scatenante
     * @param row (Number) la riga del subdocumento da eliminare
     */
    function eliminaQuotaDocumento(e, row) {
        var spinner = $("#SPINNER_pulsanteEliminaQuota");
        var modal = $("#modaleConfermaEliminazioneQuota");
        e.preventDefault();
        spinner.addClass("activated");

        doc.alertErrori.slideUp();
        doc.alertInformazioni.slideUp();

        $.postJSON("aggiornamentoDocumentoSpesa_eliminaQuota.do", {rigaDaEliminare: row})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);
                return;
            }
            // Eventuale errore sui totali
            impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);
            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni, undefined, true);
            caricaDataTableQuote(data.listaSubdocumentoSpesa, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire, data.contabilizza);

            $(".tab-pane.active").find("*:input")
                .keepOriginalValues();
            popolaTabellaDettaglioQuote("tabella_modaleDettaglioQuoteQuote", data.listaSubdocumentoSpesa);
            triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
            $("#statoDocumento").html(data.stato);
            $("#stato_tabNote").html(data.stato);
            $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
            $("#HIDDEN_flagSplitQuotePresente").val(data.flagSplitQuotePresente);
            gestisciVisibilitaCalcoloSplitAuto(data.flagAutoCalcoloImportoSplitQuote);
            $("#nettoDocumento").val(data.netto.formatMoney());
            $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
            Ritenute.caricaListaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
            $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
            // SIAC-5072
            $('div[data-div-sospensione]')[data.flagDatiSospensioneEditabili ? 'removeClass' : 'addClass']('hide');

            //se il documento non e' piu incompleto disabilito l'editabilità dei campi soggetto
            if(data.documentoIncompleto===false) {
                disabilitaCampiSoggetto();
            }
        }).always(function() {
            spinner.removeClass("activated");
            modal.modal("hide");
        });
    }

    /**
     * Carica dal servizio il collapse della quota e lo apre.
     *
     * @param url           (String)   l'URL da invocare
     * @param obj           (Object)   l'oggetto, eventualemente vuoto, da passare all'invocazione
     * @param e             (Event)    l'evento scatenante l'invocazione
     * @param functionSalva (Function) la funzione da invocare al salvataggio
     */
    function caricaEApriCollapseQuota(url, obj, e, functionSalva) {
        var spinner = $("#SPINNER_pulsanteNuovaQuota");
        var collapse = $("#accordionInserisciQuotaSpesa");
        e.preventDefault();

        doc.alerts.slideUp();

        spinner.addClass("activated");

        collapse.load(
            url,
            obj,
            function() {
                // Lego le azioni ai pulsanti
                var datepicker = $("input.datepicker", collapse);
                var dataOdierna = new Date();
                var annoDatepicker = parseInt($("#HIDDEN_anno_datepicker").val(), 10);
                
                if($("#azioneProvvedimentoConsentita").val() === "true"){
                	$("#divProvvedimento").removeClass("hide");
                }else{
                	$("#divProvvedimento").addClass("hide");
                }
                
                $(".tooltip-test").tooltip();
                $("#pulsanteAnnullaInserisciSubdocumento").on("click", distruggiCollapseQuota);
                $("#pulsanteSalvaInserisciSubdocumento").on("click", functionSalva);
                $("#pulsanteAperturaCompilazioneGuidataImpegno").on("click", apriCompilazioneGuidataImpegno);
                $("#noteTesoriereSubdocumento").on("change", impostaCampoNote);
                
                if($("#noteSubdocumento").val() === ""){
                	impostaCampoNote();
                }

                $("#divMutui, #divImpegniTrovati").on("shown hidden", function(e) {
                    e.stopPropagation();
                });

                $("#pulsanteRicercaImpegnoModale").on("click", ricercaImpegno);
                collapse.on("change", "input[name='radio_modale_impegno']", function() {
                    var subimpegno = $(this).data("originalSubImpegno");
                    var div = $("#divMutui").overlay("show");
                    impostaMutuiNellaTabella(subimpegno && subimpegno.listaVociMutuo || []);
                    div.overlay("hide");
                });
                $("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione, #numeroSubmovimento").substituteHandler("change", ricercaImpegnoPerChiaveOttimizzatoOnChange);
                $("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione").on("change", valutaVisualizzazioneDatiSospensione);
                valutaVisualizzazioneDatiSospensione();

                $("#tabellaModalitaPagamento").on("click", "input", controlloModalitaAccredito);
                $("#divSediSecondarie").on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
                    filtraModalitaPagamentoSoggetto();
                });
                $("#pulsanteDeselezionaSedeSecondaria").on("click", impostaModalitaPagamentoSoggettoNonFiltrate);

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

                $(".soloNumeri", collapse).allowedChars({numeric: true});
                $(".decimale", collapse).gestioneDeiDecimali();

                currentDataScadenza = $("#dataScadenzaSubdocumento").val();
                $("#flagRilevanteIvaSubdocumento").substituteHandler("click", gestioneClickFlagIva);
                $("#dataScadenzaSubdocumento").substituteHandler("change changeDate", gestioneDataScadenzaDopoSospensione);

                Provvedimento.inizializzazione(Ztree, {}, "_QUOTE");
                ProvvedimentoInserimento.inizializzazione('modaleInserimentoProvvedimento', '_QUOTE', ZTreeDocumento);
                
                
                
                ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento", "", "#annoProvvisorioCassaSubdocumento", "#numeroProvvisorioCassaSubdocumento",
                    "#causaleProvvisorioCassaSubdocumento");
                $("#risultatiRicercaImpegni").on("change", "input[type='radio'][name='radio_impegno_modale_impegno']",mostraTabelleImpegnoSelezionato);
             
                $('#annoProvvedimento_QUOTE, #numeroProvvedimento_QUOTE, #tipoAttoProvvedimento_QUOTE').substituteHandler('change', gestisciFlagProseguireConElaborazioneAttoAmministrativo);
                
                $('#SPAN_InformazioniProvvedimento_QUOTE').on('change', gestisciFlagProseguireConElaborazioneAttoAmministrativo);

                $('#treeStruttAmm_QUOTE').substituteHandler('ztreecheck', gestisciFlagProseguireConElaborazioneAttoAmministrativo);
                // SIAC-5115
                ottieniSospensioniQuota();
                $('#aggiungiDatiSospensioneQuota').eventPreventDefault('click', insertSospensioneQuota);
                $('#pulsanteConfermaModaleDatiSospensioneQuota').eventPreventDefault('click', confermaSospensione);
                
                // SIAC-5504 
                $('input[data-allowed-chars]', collapse).allowedChars().forzaMaiuscole();
                
                //$('*[data-assenza-cig]').slideUp()
                
                collapse.collapse("show")
                    .scrollToSelf();
                spinner.removeClass("activated");
            }
        );
    }
    function ottieniSospensioniQuota() {
        $.postJSON('aggiornamentoDocumentoSpesa_ottieniSospensioniQuota.do')
        .then(function(data) {
            gestisciTabellaSospensioneQuota(data.listaSospensioneSubdocumento);
        });
    }
    function insertSospensioneQuota(e) {
        // Pulisco i campi
        $('#fieldsetModaleDatiSospensioneQuota').find(':input').val('');
        $('#pulsanteConfermaModaleDatiSospensioneQuota').data('url', 'aggiornamentoDocumentoSpesa_addSospensioneQuota.do');
        modaleSospensioneQuota.modal('show');
    }
    function updateSospensioneQuota(uid, index, e) {
        // Leggo i campi
        var $inputs = $('#fieldsetModaleDatiSospensioneQuota').find(':input');
        var $target = $(e.currentTarget);
        var $tds = $target.closest('tr').find('td');
        var obj = {
            'sospensioneSubdocumento.uid': uid,
            'indexSospensioneQuota': index,
            'sospensioneSubdocumento.dataSospensione': $tds[0].innerHTML,
            'sospensioneSubdocumento.causaleSospensione': $tds[1].innerHTML,
            'sospensioneSubdocumento.dataRiattivazione': $tds[2].innerHTML
        };

        // Pulisco i campi
        $inputs.val('');
        $('#pulsanteConfermaModaleDatiSospensioneQuota').data('url', 'aggiornamentoDocumentoSpesa_updateSospensioneQuota.do');
        // Imposto i dati
        $inputs.toArray().forEach(function(el) {
            var $el = $(el);
            var name = $(el).attr('name');
            $el.val(obj[name]);
        });
        // Apro la modale
        modaleSospensioneQuota.modal('show');
    }
    function deleteSospensioneQuota(index, e) {
        return bootboxAlert('Stai per eliminare la sospensione selezionata. Proseguire?', undefined, undefined, [
            {label: 'S&iacute;, prosegui', 'class': 'btn-primary', callback: doDeleteSospensioneQuota.bind(undefined, index)},
            {label: 'No, indietro', 'class': 'btn-secondary', callback: $.noop}
        ]);
    }
    function doDeleteSospensioneQuota(index) {
        $.postJSON('aggiornamentoDocumentoSpesa_removeSospensioneQuota.do', {indexSospensioneQuota: index})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                return $.Deferred().reject().promise();
            }
            return gestisciTabellaSospensioneQuota(data.listaSospensioneSubdocumento);
        })
        .then(gestioneDataScadenzaDopoSospensione.bind(undefined, undefined, true));
    }
    function confermaSospensione() {
        var url = $('#pulsanteConfermaModaleDatiSospensioneQuota').data('url');
        var obj = $('#fieldsetModaleDatiSospensioneQuota').serializeObject();
        var spinner = $('#SPINNER_pulsanteConfermaModaleDatiSospensioneQuota').addClass('activated');
        return $.postJSON(url, obj)
            .then(callbackGestioneSospensioneSubdocumento)
            .then(modaleSospensioneQuota.modal.bind(modaleSospensioneQuota, 'hide'))
            .then(gestioneDataScadenzaDopoSospensione.bind(undefined, undefined, true))
            .always(spinner.removeClass.bind(spinner, 'activated'));
    }
    function callbackGestioneSospensioneSubdocumento(data) {
        if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleDatiSospensioneQuota'))) {
            return $.Deferred().reject().promise();
        }
        return gestisciTabellaSospensioneQuota(data.listaSospensioneSubdocumento);
    }

    function gestisciTabellaSospensioneQuota(list) {
        var $table = $('#tabellaDatiSospensioneQuota');
        var datiSospensioneQuotaEditabili = $('#HIDDEN_datiSospensioneQuotaEditabili').val() === 'true';
        var opts = {
            bServerSide: false,
            bDestroy: true,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti sospensioni collegate alla quota",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('dataSospensione', '', formatDate)},
                {aTargets: [1], mData: defaultPerDataTable('causaleSospensione')},
                {aTargets: [2], mData: defaultPerDataTable('dataRiattivazione', '', formatDate)},
                {aTargets: [3], mData: function(source) {
                    if(!datiSospensioneQuotaEditabili) {
                        return '';
                    }
                    return '<a href="#" data-update-sospensione><i class="icon-pencil icon-2x">&nbsp;</i></a>'
                        + '<a href="#" data-delete-sospensione><i class="icon-trash icon-2x">&nbsp;</i></a>';
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    var $nTd = $(nTd);
                    $nTd.find('a[data-update-sospensione]').eventPreventDefault('click', updateSospensioneQuota.bind(undefined, oData.uid, iRow));
                    $nTd.find('a[data-delete-sospensione]').eventPreventDefault('click', deleteSospensioneQuota.bind(undefined, iRow));
                }}
            ]
        };
        if(list) {
            opts.aaData = list;
        }
        if($.fn.dataTable.fnIsDataTable($table[0])) {
            $table.dataTable().fnDestroy();
        }
        $table.dataTable(opts);
    }

    /**
     * Prepopolamento della causale di sospensione
     * @param select (string) il selettore della select
     * @param input  (string) il selettore dell'input
     */
    function prepopolaCausaleSospensione(select, input) {
        var $option = $('option:selected', select);
        var template;
        if($option.val() === '0') {
            // Non faccio alcunche'
            return;
        }
        template = $option.data('template');
        $(input).val(template);
    }

    /**
     * GESTIONE DEL Flag elaborazione Atto Amministrativo
     */
    function gestisciFlagProseguireConElaborazioneAttoAmministrativo (){
        var flag_proseguireConElaborazioneAttoAmministrativo = $('#HIDDEN_proseguireConElaborazioneAttoAmministrativo');
    	flag_proseguireConElaborazioneAttoAmministrativo.val('false');
    }
    /**
     * Gestione del click sul checkbox del flag IVA. Non deve essere modificabile nel caso in cui il numero iva sia valorizzato.
     *
     * @param e (Event) l'evento scatenante
     */
    function gestioneClickFlagIva(e) {
        $("#numeroRegistrazioneIVASubdocumento").val() !== "" && e.preventDefault();
    }

    /**
     * Controlla la modalita di accredito per il popolamento dei campi derivati.
     */
    function controlloModalitaAccredito() {
        var codice = $(this).data("codiceModalitaPagamento");
        var stringaNoteCertificazioneCredito = "";
        var campoNote = $("#noteCertificazioneDatiCertificazioneCreditiSubdocumento");

        if("PI" === codice) {
            stringaNoteCertificazioneCredito = "contenzioso in corso";
        } else if("FA" === codice) {
            stringaNoteCertificazioneCredito = "fallimento in corso";
        } else if("CSI" === codice || "CSC" === codice) {
            stringaNoteCertificazioneCredito = "cessione di credito";
        }

        campoNote.val(stringaNoteCertificazioneCredito);
    }

    /**
     * Effettua una ricerca dell'impegno 
     * controlla i campi popolati e richiama :
     * 1-ricerca impegno per chiave quando anno e numero impegno sono popolati
     * 2-ricerca sintetica impegni subimpegni quando viene popolato uno dei campi del provvedimento
     */
    function ricercaImpegno() {
        var anno = $("#annoImpegnoModale").val();
        var numero = $("#numeroImpegnoModale").val();
        var annoProvv = $("#annoProvvedimento_modaleImpegno").val();
        var numeroProvv = $("#numeroProvvedimento_modaleImpegno").val();
        var uidTipoProvv = $('#tipoAttoProvvedimento_modaleImpegno').find('option:selected').val();
        var uidStrutturaAmm = $("#HIDDEN_StrutturaAmministrativoContabile_modaleImpegnoUid").val(); 
        var alertErroriModale = $("#ERRORI_IMPEGNO_MODALE").slideUp();
        var arrayErrori = [];
        
        var isRicercaPerChiave = !isNaN(parseInt(anno, 10))
            && !isNaN(parseInt(numero, 10))
            && isNaN(parseInt(annoProvv, 10))
            && isNaN(parseInt(numeroProvv, 10))
            && isNaN(parseInt(uidTipoProvv, 10))
            && isNaN(parseInt(uidStrutturaAmm, 10));
        var isAlMenoUnCampoPopolato = !isNaN(parseInt(anno, 10))
                || !isNaN(parseInt(numero, 10))
                || !isNaN(parseInt(annoProvv, 10))
                || !isNaN(parseInt(numeroProvv, 10))
                || !isNaN(parseInt(uidTipoProvv, 10))
                || !isNaN(parseInt(uidStrutturaAmm, 10));

        if(!isAlMenoUnCampoPopolato) {
            arrayErrori.push("COR_ERR_0018 - Indicare almeno un criterio di ricerca");
        }
        $("#divListaImpegniTrovati").slideUp();
        $("#divImpegniTrovati").slideUp();

        if(impostaDatiNegliAlert(arrayErrori, alertErroriModale, false)) {
            return;
        }

        if(isRicercaPerChiave){
        	resetCampiProvvedimento();
        	return ricercaImpegnoPerChiaveOttimizzato();
        }
        ricercaSinteticaImpegno();
    }

    /**
     * resetta i campi del provvedimento 
     */
    function resetCampiProvvedimento(){
    	$("#annoProvvedimento_modaleImpegno").val('');
    	$("#numeroProvvedimento_modaleImpegno").val('');
    	$('#tipoAttoProvvedimento_modaleImpegno').find('option:selected').val('');
    	$("#HIDDEN_StrutturaAmministrativoContabileUid_modaleImpegno").val('');
    }
    
    /**
     * Richiama il servizio ricercaSintetica degli impegni 
     * e popola le tabelle relative alla ricerca
     */
    function ricercaSinteticaImpegno(){
        var spinner = $("#SPINNER_pulsanteRicercaImpegnoModale");
        var alertErroriModale = $("#ERRORI_IMPEGNO_MODALE").slideUp();

        var oggettoPerChiamataAjax = unqualify($("#FIELDSET_modaleImpegno").serializeObject(), 1);
        spinner.addClass("activated");

        $.postJSON( "ricercaSinteticaImpegniSubImpegni.do", oggettoPerChiamataAjax)
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }
            impostaImpegniNellaTabella(data);
            $("#divListaImpegniTrovati").slideDown();
        }).always(function() {
            spinner.removeClass("activated");
        });
    }
   
    /**
     * Mostra :
     * 1-la tabella con gli impegni trovati
     * 2-le tabelle relative ai singoli impegni selezionati 
     */
    function mostraTabelleImpegnoSelezionato(){
        var impegno = $("input[type='radio'][name='radio_impegno_modale_impegno']:checked", "#risultatiRicercaImpegni").data("originalImpegnoFromImpegni");
        if(impegno !== undefined){
            // prendo i valori inseriti per effettuare la ricerca e le valorizzo con l'impegno scelto dall'utente 
            $("#annoImpegnoModale").val(impegno.annoMovimento);
            $("#numeroImpegnoModale").val(impegno.numero);
            // chiamo la ricercaImpegnoPerChiave per ottenere i dati dei subimpegni e mutui
            ricercaImpegnoPerChiaveOttimizzato();
            return;
        }
        
        $('#divImpegniTrovati').html('');
        $('#divListaImpegniTrovati').html('');
    }
    
    /**
     * Chiamate ajax per il caricamento degli impegni in base ai parametri di ricerca
     */
    function impostaImpegniNellaTabella(listaImpegni) {
        var options = {
            bServerSide: true,
            bDestroy: true,
            sAjaxSource: "risultatiRicercaImpegniAjax.do",
            sServerMethod: "POST",
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
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
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "<input type='radio' name='radio_impegno_modale_impegno' />";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalImpegnoFromImpegni", oData);
                }},
                {aTargets: [1], mData: function(source) {
                    if(source){
                        return source.annoMovimento + "/"+ source.numero;
                    }
                }},
                {aTargets: [2], mData: function(source){
                    if(source.capitoloUscitaGestione && source.capitoloUscitaGestione.uid !== 0){
                        return calcolaStringaCap(source.capitoloUscitaGestione);
                    }
                }},
                {aTargets: [3],  mData: function(source){
                    if(source.attoAmministrativo){
                        return calcolaStringaProvv(source.attoAmministrativo);
                    }
                }},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('disponibilitaLiquidare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        $("#risultatiRicercaImpegni").dataTable(options);
    }
    
    /**
     * Chiama la ricerca impegno per chiave (con subimpegni paginati) 
     * @return  
     */
    function ricercaImpegnoPerChiaveOttimizzato(){
        var spinner = $("#SPINNER_pulsanteRicercaImpegnoModale");
        var alertErroriModale = $("#ERRORI_IMPEGNO_MODALE").slideUp();
        var oggettoPerChiamataAjax = unqualify($("#FIELDSET_modaleImpegno").serializeObject(), 1);
        spinner.addClass("activated");

        return $.postJSON("ricercaImpegnoPerChiaveOttimizzato.do", oggettoPerChiamataAjax)
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                return;
            }
            impostaImpegnoNellaTabella(data.impegno);
            impostaSubimpegniPaginatiNellaTabella(data.impegno);
            impostaMutuiNellaTabella(data.impegno.listaVociMutuo);
            impostaEApriCollapseImpegniTrovati(data.impegno);
        }).always(spinner.removeClass.bind(spinner, "activated"));
    }
    
    /**
     * Popola i campi hidden e lega gli handler di risultati di ricerca della ricerca dell'impegno.
     * Una volta terminato, mostra i risultati.
     * 
     * */
    function impostaEApriCollapseImpegniTrovati(impegno){
    	// Imposto anche CIG e CUP nei campi hidden
        $("#hidden_cigImpegno").val(impegno.cig);
        $("#hidden_cupImpegno").val(impegno.cup);
        impegno.capitoloUscitaGestione && $("#hidden_flagRilevanteIva").val(impegno.capitoloUscitaGestione.flagRilevanteIva);
        campoRicercaEffettuataConSuccesso.val("true");                
        $("#pulsanteConfermaModaleImpegno").substituteHandler("click", confermaImpegno);
        $("#divImpegniTrovati").slideDown();
    }
    /**
     * Conferma l'impegno selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaImpegno(e) {
        var checkedRadio = $("#tabellaImpegniModale").find("input[name='radio_modale_impegno']:checked");
        var impegno;
        var subimpegno;
        var annoImpegno;
        var numeroImpegno;
        var cigImpegno;
        var cupImpegno;
        var checkedMutuo;
        var mutuo = "";
        var flagRilevanteIva;
        var numeroSubimpegno;
        var siopeAssenzaMotivazione;
        var str;

        e.preventDefault();

        if(!campoRicercaEffettuataConSuccesso.val()) {
            impostaDatiNegliAlert(["Necessario selezionare un impegno"], $("#ERRORI_IMPEGNO_MODALE"), false);
            return;
        }

        subimpegno = checkedRadio.length > 0 && checkedRadio.data("originalSubImpegno");
        impegno = checkedRadio.length ? checkedRadio.data('originalImpegno') : $('#tabellaImpegnoModale').data('originalImpegno');

        annoImpegno = impegno.annoMovimento;
        numeroImpegno = impegno.numero;
        numeroSubimpegno = !!subimpegno ? subimpegno.numero : "";
        cigImpegno = subimpegno ? subimpegno.cig : impegno.cig;
        cupImpegno = subimpegno ? subimpegno.cup : impegno.cup;
        checkedMutuo = $("input[name='radio_mutuo_modale_impegno']:checked");
        flagRilevanteIva = $("#hidden_flagRilevanteIva").val();
        siopeAssenzaMotivazione = subimpegno ? subimpegno.siopeAssenzaMotivazione : impegno.siopeAssenzaMotivazione;

        if(checkedMutuo.length) {
            mutuo = checkedMutuo.data("originalMutuo").numeroMutuo;
        }
        str = ": " + annoImpegno + " / " + numeroImpegno;
        if(subimpegno) {
            str += " - " + numeroSubimpegno;
        }
        

        $("#annoMovimentoMovimentoGestione").val(annoImpegno);
        $("#numeroMovimentoGestione").val(numeroImpegno);
        $("#numeroSubmovimento").val(numeroSubimpegno);
        $("#cigMovimentoGestione").val(subimpegno ? subimpegno.cig : cigImpegno);
        $("#cupMovimentoGestione").val(subimpegno ? subimpegno.cup : cupImpegno);
        $("#mutuoMovimentoGestione").val(mutuo);
        $('#siopeAssenzaMotivazione').val(siopeAssenzaMotivazione ? siopeAssenzaMotivazione.uid : 0);

        $("#SPAN_impegnoH4").html(str);

        $("#modaleImpegno").modal("hide");

        
        impegno = $('#tabellaImpegnoModale').data('originalImpegno');
        // Mostro lo span del rilevante Iva (solo in inserimento)
        valutaVisualizzazioneRilevanteIva();
        
        showMotivoAssenzaCig(impegno, subimpegno);
        
    }

    /**
     * Valutazione della visualizzazione del rilevante Iva.
     *
     * @param e (Event) l'evento scatenante (Optional, default: undefined)
     */
    function valutaVisualizzazioneRilevanteIva(e) {
         // Sono nel caso di caricamento a partire dalla compilazione guidata: imposto subito il campo
        showRilevanteIva($("#hidden_flagRilevanteIva").val());
        
    }
    function creaOggettoRicercaImpegnoPerChiaveOttimizzato(fieldAnno, fieldNumero, numeroSub){
    	
    	var obj = {};
    	obj['subimpegno'] = {};
        obj['impegno.annoMovimento'] = fieldAnno.val();
        obj['impegno.numero'] = fieldNumero.val();
        
        if(!numeroSub){
        	obj["caricaSub"] = false;
        	return obj;
        }
        obj['numeroSubmovimentoGestione'] =  numeroSub;
        obj["caricaSub"] = true;
        return obj;
    }
    
    function isSubimpegnoValido(numeroSub, subs){
    	var i;
    	if(!numeroSub){
    		//non ho ricercato un subimpegno. esco.
    		return true;
    	}
        //ciclo sui sub per  controllare che quello ricercato esista (dal servizio dovrebbe arrivare una lista con un solo elemneto, quello richiesto)
        for(i = 0; i < subs.length; i++) {
            // Uso il == in vece del === in quanto uno dei due campi e' numerico e l'altro e' una stringa
            // TODO: usare il + su entrambi i campi per forzare il cast?
            if(subs[i] && subs[i].numero == numeroSub) {
                return true;
            }
        }
        impostaDatiNegliAlert(["COR_ERR_0010 - Il valore del parametro sub impegno non e' consentito: non e' presente nell'elenco dei submovimenti"], doc.alertErrori);
        $('#numeroSubmovimento').val('');
        return false;
    }
    
    /***
     *  Effettua la ricerca dell'impegno quando vengono popolati i campi anno e numero movimento.
     *  @return l'oggetto deferred
     * */
    function ricercaImpegnoPerChiaveOttimizzatoOnChange(){
    	var fieldAnno;
        var fieldNumero;
        var fields = $('#fieldsetImpegnoQuota');
        var numeroSub = $('#numeroSubmovimento').val();
        var url = numeroSub? 'ricercaMovimentoGestione_cercaImpegnoSubImpegno.do' : 'ricercaImpegnoPerChiaveOttimizzato.do';
        var obj = {};
        var isInserimento = $("#uidSubdocumento").val() === '0';
        // Ricerco il movimento
        fieldAnno = $("#annoMovimentoMovimentoGestione");
        fieldNumero = $("#numeroMovimentoGestione");
        if(!fieldAnno.val() || !fieldNumero.val()) {
            // Non ho impostato tutti i dati necessarii
            return;
        }
        
        obj = creaOggettoRicercaImpegnoPerChiaveOttimizzato(fieldAnno, fieldNumero, numeroSub);
        
        $("#hidden_cigImpegno").val("");
        $("#hidden_cupImpegno").val("");
        $('#cigMovimentoGestione').val("");
        $('#cupMovimentoGestione').val("");
        $('#SPAN_impegnoH4').val("");
        
        fields.overlay("show");
        // Devo ricercare l'impegno e vederne i dati
        return $.postJSON(url, obj)
        .then(function(data) {
        	var subs = (data.impegno && data.impegno.elencoSubImpegni) || [];
        	//si potrebbero verificare degli errori da servizio, oppure potrebbe non esistere il subimpegno ricercato.
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori) || ! isSubimpegnoValido(numeroSub, subs)) {
                // sono in uno dei due casi precedenti, esco.
                return;
            }
            // Prendo il campo del rilevante dal capitolo se presente
            valorizzaCampiPostRicerca(data);
            if(isInserimento) {
                showRilevanteIva(data.impegno && data.impegno.capitoloUscitaGestione && data.impegno.capitoloUscitaGestione.flagRilevanteIva);
            }
        }).always(function() {
            fields.overlay("hide");
        });
    }
    
    /**
     * Valorizza i campi cig e cup e la descrizione con impegno e numero 
     * SIAC-3658
     */
    function valorizzaCampiPostRicerca(data){
        var subimpegnoNumero = $("#numeroSubmovimento").val();
        var numeroSubimpegnoNumber = subimpegnoNumero !== "" ? +subimpegnoNumero : "";
        var cig;
        var cup;
        var str;
        var assenzaMotivazione;
        var subimpegno = {};
        
        // impegno a questo livello e' sempre presente
        cig = data.impegno.cig;
        cup = data.impegno.cup;
        assenzaMotivazione = data.impegno.siopeAssenzaMotivazione ? data.impegno.siopeAssenzaMotivazione.uid : 0;
        str = ": " + data.impegno.annoMovimento + " / " + data.impegno.numero;
        // controllo il subimpegno
        if (data.impegno.elencoSubImpegni) {
            data.impegno.elencoSubImpegni.forEach(function(el) {
                if (el.numero === numeroSubimpegnoNumber) {
                    cig = el.cig;
                    cup = el.cup;
                    assenzaMotivazione = el.siopeAssenzaMotivazione ? el.siopeAssenzaMotivazione.uid : 0;
                    subimpegno = el;
                }
            });
            str += numeroSubimpegnoNumber ? " / " + numeroSubimpegnoNumber:"";
        }
        
        $("#hidden_cigImpegno").val(cig);
        $("#hidden_cupImpegno").val(cup);
        $('#cigMovimentoGestione').val(cig);
        $('#cupMovimentoGestione').val(cup);
        $('#siopeAssenzaMotivazione').val(assenzaMotivazione);
        $("#SPAN_impegnoH4").html(str);
        showMotivoAssenzaCig(data.impegno, subimpegno);
    }

    /**
     * Mostra o nasconde lo span del rilevante iva.
     * @param toShow (boolean) se il campo sia da mostrare
     */
    function showRilevanteIva(toShow) {
        $("#spanCapitoloRilevanteIvaQuote")[toShow ? "show" : "hide"]();
    }

    /**
     * Carica le strutture amministrative contabili per la modale di compilazione guidata degli impegni  
     */
    function caricaStrutture(){
        $.postJSON("ajax/strutturaAmministrativoContabileAjax.do")
        .then(function (data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
            ZTreeDocumento.imposta("treeStruttAmm_modaleImpegno", ZTreeDocumento.SettingsBase, listaStrutturaAmministrativoContabile,"HIDDEN_StrutturaAmministrativoContabile_modaleImpegno_Uid", "_modaleImpegno");
        });
    }

    /**
     * Valutazione della visualizzazione dei dati di sospensione
     *
     */
    function valutaVisualizzazioneDatiSospensione() {
        var fieldAnno;
        var fieldNumero;

        // Ricerco il movimento
        fieldAnno = $("#annoMovimentoMovimentoGestione");
        fieldNumero = $("#numeroMovimentoGestione");
        if(!fieldAnno || !fieldNumero){
        	return;
        }
        if(!fieldAnno.val() || !fieldNumero.val()) {
           $("#datiSospensione").addClass("hide");
        }else{
        	$("#datiSospensione").removeClass("hide");
        }
       
    }
    /**
     * Apre il modale per la compilazione guidata dell'impegno.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriCompilazioneGuidataImpegno(e) {
        e.preventDefault();
      
        caricaStrutture();
        $("#annoImpegnoModale").val($("#annoMovimentoMovimentoGestione").val());
        $("#numeroImpegnoModale").val($("#numeroMovimentoGestione").val());

        campoRicercaEffettuataConSuccesso = $("#hidden_ricercaEffettuataConSuccessoModaleImpegno");
        campoRicercaEffettuataConSuccesso.val("");

        // Svuoto i campi
        $("#hidden_cigImpegno").val("");
        $("#hidden_cupImpegno").val("");
        $("#hidden_flagRilevanteIva").val("");
        // Nascondo i div
        $("#divImpegniTrovati").slideUp();
        $("#ERRORI_IMPEGNO_MODALE").slideUp();

        // Apro il modale
        $("#modaleImpegno").modal("show");
        
        
    }

    /**
     * Filtra le modalita di pagamento del soggetto.
     */
    function filtraModalitaPagamentoSoggetto() {
        var uidSede = $("input[type='radio'][name='sedeSecondariaSoggetto.uid']").filter(":checked")
            .val();
        var oggettoPerChiamataAjax = {"sedeSecondariaSoggetto.uid": uidSede || 0};
        var collapse = $("#collapseModalitaPagamento").overlay("show");

        $.post("aggiornamentoDocumentoSpesa_filtraModalitaPagamentoSoggetto.do", oggettoPerChiamataAjax)
        .then(function(data) {
            collapse.html(data);
        }).always(function() {
            collapse.overlay("hide");
        });
    }

    /**
     * Imposta le modalita' di pagamento non filtrate
     * */
    function impostaModalitaPagamentoSoggettoNonFiltrate(){
         var collapse = $("#collapseModalitaPagamento").overlay("show");

         $.post("aggiornamentoDocumentoSpesa_impostaModalitaPagamentoSoggettoNonFiltrate.do")
         .then(function(data) {
             collapse.html(data);
         }).always(function() {
             collapse.overlay("hide");
         });
    }

    /**
     * Gestisce la data di scadenza del documento dopo la sospensione.
     * @param e     (Event)   l'evento scatenante
     * @param force (boolean) se forzare l'invocazione.
     */
    function gestioneDataScadenzaDopoSospensione(e, force) {
        var $dataScadenza = $('#dataScadenzaSubdocumento');
        var $dataScadenzaDopoSospensione = $('#dataScadenzaDopoSospensioneSubdocumento');
        var dataScadenza = $dataScadenza.val();
        var dataScadenzaDopoSospensione = $dataScadenzaDopoSospensione.val();
        var obj = {
            'subdocumento.dataScadenza': dataScadenza,
            'subdocumento.dataScadenzaDopoSospensione': dataScadenzaDopoSospensione
        };
        var overlay;
        if(window.semaphore.gestioneDataScadenzaDopoSospensione) {
            // Gia' in esecuzione
            return;
        }
        if(currentDataScadenza === dataScadenza && !force) {
            // Non forzo la riscrittura
            return;
        }
        currentDataScadenza = dataScadenza;
        window.semaphore.gestioneDataScadenzaDopoSospensione = true;
        overlay = $('#dataScadenzaSubdocumento, #dataScadenzaDopoSospensioneSubdocumento').overlay('show');
        $.postJSON('aggiornamentoDocumentoSpesa_calcolaDataScadenzaDopoSospensione.do', obj)
        .then(function(data) {
            var dataDaServizio = data.subdocumento && data.subdocumento.dataScadenzaDopoSospensione || '';
            $dataScadenzaDopoSospensione.val(formatDate(dataDaServizio));
            delete window.semaphore.gestioneDataScadenzaDopoSospensione;
        }).always(overlay.overlay.bind(overlay, 'hide'));
    }
    
    /**
     * Imposta il campo note con il valore selezionato in note del tesoriere.
     */
   function impostaCampoNote(){
       var optionSelezionata = $("option:selected", '#noteTesoriereSubdocumento').html() || '';
       var note = optionSelezionata.split(' - ').slice(1).join(' - ');
       $("#noteSubdocumento").val(note);
    }

   /**
    * Calcolo  in automatico se possibile degli importi split di tutte le quote documento 
    *
    */
   function calcoloAutomaticoImportiIvaSplit () {
       var spinner = $("#SPINNER_pulsanteCalcolaImportiIva");
       var url = "aggiornamentoDocumentoSpesa_calcoloAutomaticoImportiIvaSplitQuote.do";
       doc.alertErrori.slideUp();
       doc.alertMessaggi.slideUp();
       doc.alertInformazioni.slideUp();
       spinner.addClass("activated");
       $.postJSON(url)
       .then(function(data) {
          
           // Eventuali errori 
           if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
               
               return;
           }
           impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi);
           impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
           $("#HIDDEN_flagSplitQuotePresente").val(data.flagSplitQuotePresente);
           gestisciVisibilitaCalcoloSplitAuto(data.flagAutoCalcoloImportoSplitQuote);
       }).always(function() {
           spinner.removeClass("activated");
       });

   }
   
   
   
   /**
    * Mostra/Nasconde il tasto x Calcolo  in automatico 
    *
    */
   function gestisciVisibilitaCalcoloSplitAuto (abilitazioneTasto) {
   	
	   $('#pulsanteCalcolaImportiIva')[abilitazioneTasto ? 'removeClass' : 'addClass']('hide');
   }
    /**
     * Apre il modale di inserimento per una nuova quota
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.aperturaInserimentoNuovaQuota = function(e) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoSpesa_inizioInserimentoNuovaQuota.do", {}, e, salvaNuovaQuota);
    };

    /**
     * Carica la tabella delle quote.
     */
    exports.caricaListaQuote = function() {
        $.postJSON("aggiornamentoDocumentoSpesa_ottieniListaQuote.do")
        .then(function(data) {
            var lista = data.listaSubdocumentoSpesa;
            var totaleQuote = data.totaleQuote;
            var totaleDaPagareQuote = data.totaleDaPagareQuote;
            var importoDaAttribuire = data.importoDaAttribuire;
            $("#HIDDEN_flagSplitQuotePresente").val(data.flagSplitQuotePresente);
            gestisciVisibilitaCalcoloSplitAuto(data.flagAutoCalcoloImportoSplitQuote);
            caricaDataTableQuote(lista, totaleQuote, totaleDaPagareQuote, importoDaAttribuire, data.contabilizza);
            popolaTabellaDettaglioQuote("tabella_modaleDettaglioQuoteQuote", lista);
            
            if($("#HIDDEN_flagRitenuteAccessibile").val() === "true"){
                Ritenute.caricaListaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
            }
        });
    };

    
    /**
     * Calcolo  in automatico se possibile degli importi split di tutte le quote documento 
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.verificaCalcoloAutoSplitQuote = function() {
    	var flagSplitQuotePresente = $("#HIDDEN_flagSplitQuotePresente");
    	if (flagSplitQuotePresente.val()==="true") {
		    var modal = $("#modaleConfermaCalcoloAutoSplitQuote");
		
		    // Se clicco su no, chiudo il modale
		    $("#pulsanteNoCalcoloAutoSplitQuote").substituteHandler("click", function() {
		        modal.modal("hide");
		    });
		    // Se clicco su si' allora elimino la quota
		    $("#pulsanteSiCalcoloAutoSplitQuote").substituteHandler("click", function() {
		    	modal.modal("hide");
		    	calcoloAutomaticoImportiIvaSplit();
		    });
		
		    modal.modal("show");
    	} else {
    		calcoloAutomaticoImportiIvaSplit();
    	}
    };
    
    
    /**
     * Preimpostazione delle note di certificazione del credito sulla base della modalita di pagamento.
     */
    exports.preimpostaNoteCertificazioneCredito = function() {
        var checked = $("input[name='modalitaPagamentoSoggetto.uid']", "#accordionInserisciQuotaSpesa").filter(":checked");
        var mps;
        var msg;
        if(!checked.length) {
            // Nessuna opzione selezionata: esco
            return;
        }

        mps = checked.data("codiceModalitaPagamento") || "";
        msg = mapCodiceModalitaPagamentoNoteCertificazione[mps];
        // Se ho il messaggio, allora lo imposto
        if(msg) {
            $("#noteCertificazioneDatiCertificazioneCreditiSubdocumento").val(msg);
        }
    };
    
    //SIAC-5413
    function computeTipoDebitoSIOPE(impegno, subimpegno) {
        return (subimpegno && subimpegno.siopeTipoDebito) || (impegno && impegno.siopeTipoDebito) || undefined;
    }

    function showMotivoAssenzaCig(impegno, subimpegno) {
        var tipoDebitoSIOPE = computeTipoDebitoSIOPE(impegno, subimpegno);
        var isCO = tipoDebitoSIOPE && tipoDebitoSIOPE.codice === 'CO';
        $('*[data-assenza-cig]')[isCO ? 'slideDown' : 'slideUp']();
    }
    

    exports.prepopolaCausaleSospensione = prepopolaCausaleSospensione;
    doc.Quote = exports;
    return doc;
}(jQuery, Documento || {}));

$(function() {

    // Gestione dei pulsanti
    $("#pulsanteInserimentoNuovaQuota").on("click", Documento.Quote.aperturaInserimentoNuovaQuota);
    
    $("#pulsanteCalcolaImportiIva").on("click", Documento.Quote.verificaCalcoloAutoSplitQuote);
    $("#accordionInserisciQuotaSpesa").on("change", "input[name='modalitaPagamentoSoggetto.uid']", Documento.Quote.preimpostaNoteCertificazioneCredito);

    $('#modaleSospensioneQuota_causaleSospensioneSubdoc').substituteHandler('change',
            Documento.Quote.prepopolaCausaleSospensione.bind(undefined, '#modaleSospensioneQuota_causaleSospensioneSubdoc', '#modaleSospensioneQuota_causaleSospensioneSubdocumento'));

    Documento.Quote.caricaListaQuote();
    
    // SIAC-5438
    $('#causaleSospensioneSelectModaleDatiSospensioneQuota')
    .substituteHandler('change', Documento.Quote.prepopolaCausaleSospensione.bind(undefined, '#causaleSospensioneSelectModaleDatiSospensioneQuota', '#causaleSospensioneModaleDatiSospensioneQuota'));

    // CR-2541
    $("#modale_hidden_tipoProvvisorioDiCassa").val("S");
    
});