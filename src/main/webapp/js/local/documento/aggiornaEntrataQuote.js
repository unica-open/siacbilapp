/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($, doc) {
    var exports = {};
    var campoRicercaEffettuataConSuccesso;
    var gestioneUEB = $("#HIDDEN_gestioneUEB").val() === "true";

    /**
     * Lancia l'informazione sull'aggiornamento delle quote
     *
     * @param flagIvaDisponibile (Boolean) se l'iva sia disponibile
     */
    function triggerQuoteAggiornate(flagIvaDisponibile) {
        var li = $("a[href='#tabDatiIva']").parent("li");
        var div =$("#tabDatiIva");
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
        $("#accordionInserisciQuotaEntrata").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
    }
    
    function impostaSfondamentoAccertamento(fncToCall){
    	var innerFnc = fncToCall && typeof fncToCall == 'function'? fncToCall : $.noop;
    	$('input[name="forzaDisponibilitaAccertamento"]').val(true);
    	fncToCall();
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
        var url = "aggiornamentoDocumentoEntrata_inserimentoNuovaQuota.do";
        var pulsanteSalva = $("#pulsanteSalvaInserisciSubdocumento");
        e && e.preventDefault();

        spinner.addClass("activated");
        pulsanteSalva.addClass("disabled");
        pulsanteSalva.attr("disabled", true);
        pulsanteSalva.prop("disabled", true);

        doc.alertErrori.slideUp();
        fieldset.addClass('form-submitted');

        $.postJSON(
            url,
            oggettoPerChiamataAjax,
            function(data) {
            	var stringaMessaggioConfermaSfondaDisponibilita = '';
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    return;
                }
                if(data.messaggi && data.messaggi.length) {
                	//SIAC-6732
                    //gestioneRichiestaProsecuzione(data.messaggi, url, oggettoPerChiamataAjax, completaInserimentoQuota);
                	
                	stringaMessaggioConfermaSfondaDisponibilita = data.messaggi.reduce(function(acc, el) {
                    	return acc + '<li>' + el.codice + ' - ' + el.descrizione + '<li>';
                    }, '<ul>') + '</ul>';
                    impostaRichiestaConfermaUtente(stringaMessaggioConfermaSfondaDisponibilita, function() {
                    	fieldset.find('input[name="proseguireConElaborazione"]').val(true);
                    	fieldset.find('input[name="proseguireConElaborazioneAttoAmministrativo"]').val(true);
                    	salvaNuovaQuota();
                    }, undefined, "Si, prosegui", "no, indietro");
                    return;
                }
                //SIAC-6645
                if(data.messaggioConfermaSfondamentoAccertamento && data.messaggioConfermaSfondamentoAccertamento.codice ){
                	stringaMessaggioConfermaSfondaDisponibilita = data.messaggioConfermaSfondamentoAccertamento.codice  + ' - ' + data.messaggioConfermaSfondamentoAccertamento.descrizione;  
                	impostaRichiestaConfermaUtente(stringaMessaggioConfermaSfondaDisponibilita, impostaSfondamentoAccertamento.bind(undefined, salvaNuovaQuota), undefined, "Si, inserisci una modifica di importo", "no, indietro");
                	return;
                }
                
                 //se il documento non e' piu incompleto disabilito l'editabilità dei campi soggetto
                if(data.documentoIncompleto === false) {
                    disabilitaCampiSoggetto();
                }
                completaInserimentoQuota(data);
            }
        ).always(function() {
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
        impostaDatiNegliAlert(data.errori, doc.alertErrori, undefined, false);
        impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);
        impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni, undefined, true);

        caricaDataTableQuote(data.listaSubdocumentoEntrata, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire);
        $("#accordionInserisciQuotaEntrata").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
        // Lancio l'evento di aggiornamento delle quote, nel caso qualcuno stia ascoltando
        triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
        $("#nettoDocumento").val(data.netto.formatMoney());
        $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
        $("#statoDocumento").html(data.stato);
        $("#stato_tabNote").html(data.stato);
        $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
        $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
    }

    /**
     * Completa l'aggiornamento della quota.
     *
     * @param data i dati forniti dal servizio
     */
    function completaAggiornamentoQuota(data) {
    	if(impostaDatiNegliAlert(data.errori, doc.alertErrori, undefined, false)) {
    		impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, false);    		
    		return;
    	}
        impostaDatiNegliAlert(data.messaggi, doc.alertMessaggi, undefined, true);
        // siac 2746
        impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni, undefined, true);

        caricaDataTableQuote(data.listaSubdocumentoEntrata, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire);
        $("#accordionInserisciQuotaEntrata").collapse("hide");

        $(".tab-pane.active").find("*:input")
            .keepOriginalValues();
        triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
        $("#nettoDocumento").val(data.netto.formatMoney());
        $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
        $("#statoDocumento").html(data.stato);
        $("#stato_tabNote").html(data.stato);
        $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
        $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
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
        var pulsanteSalva = $("#pulsanteSalvaInserisciSubdocumento");
        var url = "aggiornamentoDocumentoEntrata_aggiornamentoQuota.do";
        e && e.preventDefault();

        spinner.addClass("activated");
        pulsanteSalva.addClass("disabled");
        pulsanteSalva.attr("disabled", true);
        pulsanteSalva.prop("disabled", true);

        doc.alertErrori.slideUp();
        fieldset.addClass('form-submitted');
        
        $.postJSON(
            url,
            oggettoPerChiamataAjax,
            function(data) {
                var stringaMessaggioConfermaSfondaDisponibilita = '';
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    return;
                }

                // Eventuale messaggi: carico la richiesta all'utente
                if(data.messaggi && data.messaggi.length) {
                	//SIAC-6732
//                    gestioneRichiestaProsecuzione(data.messaggi, url, oggettoPerChiamataAjax, completaAggiornamentoQuota);
                    
                    stringaMessaggioConfermaSfondaDisponibilita = data.messaggi.reduce(function(acc, el) {
                    	return acc + '<li>' + el.codice + ' - ' + el.descrizione + '<li>';
                    }, '<ul>') + '</ul>';
                    impostaRichiestaConfermaUtente(stringaMessaggioConfermaSfondaDisponibilita, function() {
                    	fieldset.find('input[name="proseguireConElaborazione"]').val(true);
                    	fieldset.find('input[name="proseguireConElaborazioneAttoAmministrativo"]').val(true);
                        aggiornaQuota();
                    }, undefined, "Si, prosegui", "no, indietro");
                    return;
                }
                
                //SIAC-6645
                if(data.messaggioConfermaSfondamentoAccertamento && data.messaggioConfermaSfondamentoAccertamento.codice ){
                	stringaMessaggioConfermaSfondaDisponibilita = data.messaggioConfermaSfondamentoAccertamento.codice  + ' - ' + data.messaggioConfermaSfondamentoAccertamento.descrizione;  
                	impostaRichiestaConfermaUtente(stringaMessaggioConfermaSfondaDisponibilita, impostaSfondamentoAccertamento.bind(undefined, aggiornaQuota), undefined, "Si, inserisci una modifica di importo", "no, indietro");
                	return;
                }
                
                //se il documento non e' piu incompleto disabilito l'editabilità dei campi soggetto
                if(data.documentoIncompleto===false) {
                    disabilitaCampiSoggetto();
                }

                completaAggiornamentoQuota(data);
            }
        ).always(function() {
            spinner.removeClass("activated");
            pulsanteSalva.removeClass("disabled");
            pulsanteSalva.removeAttr("disabled");
            pulsanteSalva.removeProp("disabled");
            fieldset.removeClass('form-submitted');
        });
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
        var alertModale = $("#alertModaleConfermaProsecuzioneSuAzione").find("ul")
            .empty()
            .end();
        impostaDatiNegliAlert(array, alertModale, undefined, false);

        $("#modaleConfermaProsecuzioneSuAzionePulsanteSi").substituteHandler("click", function() {
            var spinner = $("#SPINNER_modaleConfermaProsecuzioneSuAzione").addClass("activated");
            var conferma = $("#modaleConfermaProsecuzioneSuAzionePulsanteSi");
            conferma.addClass("disabled");
            conferma.attr("disabled", true);
            conferma.prop("disabled", true);
            data.proseguireConElaborazione = true;
            data.proseguireConElaborazioneAttoAmministrativo = true;

            $.postJSON(url, data)
            .then(endCallback)
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
     * Carica il dataTable delle quote (subdocumento).
     *
     * @param lista               (Array)  la lista tramite cui popolare la tabella
     * @param totaleQuote         (Number) il totale delle quote
     * @param totaleDaPagareQuote (Number) il totale da pagare delle quote
     * @param importoDaAttribuire (Number) l'importo da attribuire
     */
    function caricaDataTableQuote(lista, totaleQuote, totaleDaPagareQuote, importoDaAttribuire) {
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
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.numero || "";
                }},
                {
                    aTargets: [1],
                    mData: function(source) {
                        if(!source.stringaAccertamento && source.accertamento) {
                            // Solo se l'accertamento è presente
                            source.stringaAccertamento = "" + source.accertamento.annoMovimento + " / " + source.accertamento.numero;
                            if(source.subAccertamento) {
                                source.stringaAccertamento += " - " + source.subAccertamento.numero;
                            }
                        }
                        return source.stringaAccertamento || "";
                    }
                },
                {
                    aTargets: [2],
                    mData: function(source) {
                        if(!source.stringaProvvedimento && source.attoAmministrativo) {
                            // Solo se l'accertamento è presente
                            source.stringaProvvedimento = "<a rel='popover'>" + source.attoAmministrativo.anno + " / " + source.attoAmministrativo.numero + " / " +
                                source.attoAmministrativo.tipoAtto.codice;
                            if(source.attoAmministrativo.strutturaAmmContabile) {
                                source.stringaProvvedimento += " / " + source.attoAmministrativo.strutturaAmmContabile.codice + "</a>";
                            }
                        }
                        return source.stringaProvvedimento || "";
                    },
                    fnCreatedCell : function(nTd, sData, oData) {
                        if(oData.attoAmministrativo) {
                            var settings = {
                                content: oData.attoAmministrativo.oggetto,
                                title: "oggetto",
                                trigger: "hover",
                                placement: "left"
                            };
                            $("a", nTd).popover(settings);
                        }
                    }
                },
                {aTargets: [3], mData: function(source) {
                    return source.ordinativo ? source.ordinativo.anno + "/" + source.ordinativo.numero : "";
                }},
                {aTargets: [4], mData: function(source) {
                    var provvisorio = "";
                    if(source.provvisorioCassa){
                        provvisorio += source.provvisorioCassa.dataEmissione ? formatDate(source.provvisorioCassa.dataEmissione) + " - " : "";
                        provvisorio += source.provvisorioCassa.numero ? source.provvisorioCassa.numero : "";
                    }
                    return provvisorio;
                }},
                {
                    aTargets: [5],
                    mData: function(source) {
                        return source.importo.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {
                    aTargets: [6],
                    mData: function(source) {
                        if(!source.azioni) {
                            source.azioni ="<div class='btn-group'>" +
                            "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                                "<ul class='dropdown-menu pull-right'>";
                            if(source.ordinativo == null /*&& (source.numeroRegistrazioneIVA == null || source.numeroRegistrazioneIVA.length === 0)*/){
                                source.azioni += "<li><a href='#' class='aggiornaQuota'>aggiorna</a></li>";
                            }
                            source.azioni += "<li><a href='#' class='ripetiQuota'>ripeti</a></li>";
                            if(source.ordinativo == null && (source.numeroRegistrazioneIVA == null || source.numeroRegistrazioneIVA.length === 0)){
                                source.azioni += "<li><a href='#' class='eliminaQuota'>elimina</a></li>";
                            }
                            source.azioni += "</ul>" +
                            "</div>";
                         }
                         return source.azioni;
                    },
                    fnCreatedCell: function(nTd, sData, oData, iRow) {
                        $(nTd).addClass("tab_Right")
                            .find(".aggiornaQuota")
                                .substituteHandler("click", function(e) {
                                    aggiornaQuotaDocumento(e, oData);
                                })
                                .end()
                            .find(".ripetiQuota")
                                .substituteHandler("click", function(e) {
                                    ripetiQuotaDocumento(e, oData);
                                })
                                .end()
                            .find(".eliminaQuota")
                                .substituteHandler("click", function(e) {
                                    apriModaleEliminaQuotaDocumento(e, iRow, oData);
                                });
                    }
                }
            ]
        };
        $("#tabellaQuoteDocumento").dataTable(options);

        // Imposto i totali
        $("#SPAN_importoDaAttribuireDocumento").html(importoDaAttribuire.formatMoney());
        $("#SPAN_totaleDaPagareQuoteIntestazione").html(totaleDaPagareQuote.formatMoney());
        $("#thTotaliQuote").html(totaleQuote.formatMoney());
        $("#thTotaleDaPagareQuote").html(totaleDaPagareQuote.formatMoney());
        $("#totaleQuote_tabNote").html(totaleQuote.formatMoney());
    }

    /**
     * Formattazione degli importi
     * @param value (any) il valore da formattare
     * @returns (any) il valore formattato
     */
    function doFormatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return value;
    }
    /**
     * Crea una funzione che imposta la classe sul nodo
     * @param className (string) la classe
     * @returns (function(Node) => void) la funzione che imposta la classe sul nodo
     */
    function doAddClass(className) {
        return function(nTd) {
            nTd.classList = className;
        };
    }

    /**
     * Caricamento via Ajax della tabella dei capitoli e visualizzazione.
     *
     * @param lista (Array) la lista dei capitoli da utilizzare per la creazione della tabella
     */
    function tabellaCapitoli(lista) {
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaCapitoloEntrataGestioneModaleAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
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
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: '"prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnDrawCallback: function(oSettings) {
                $('a[rel="popover"]', oSettings.nTable).popover().eventPreventDefault('click');
            },
            aoColumnDefs : [
                {aTargets: [0], mData: function() {
                    return '<input type="radio" name="checkCapitolo"/>';
                }, fnCreatedCell: function(nTd, sData, oData){
                    $(nTd).find('input').data('sourceCapitolo', oData);
                }},
                {aTargets: [1], mData: function(source) {
                    return '<a rel="popover" data-original-title="Descrizione" data-trigger="hover" data-placement="right" data-content="' + escapeHtml(source.descrizione) + '" href="#">' + source.capitolo + '</a>';
                }},
                {aTargets: [2], mData: defaultPerDataTable('classificazione')},
                {aTargets: [3], mData: defaultPerDataTable('disponibileAnno0', 0, doFormatMoney), fnCreatedCell: doAddClass('text-right')},
                {aTargets: [4], mData: defaultPerDataTable('disponibileAnno1', 0, doFormatMoney), fnCreatedCell: doAddClass('text-right')},
                {aTargets: [5], mData: defaultPerDataTable('disponibileAnno2', 0, doFormatMoney), fnCreatedCell: doAddClass('text-right')},
                {aTargets: [6], mData: function(source) {
                    var str = '';
                    var n;
                    if(source.struttAmmResp) {
                        str = source.struttAmmResp;
                        n = str.indexOf('-');
                        str = str.substring(0, n);
                    }
                    return '<a rel="popover" data-original-title="Descrizione" data-trigger="hover" data-placement="left" data-content="' + escapeHtml(source.struttAmmResp) + '" href="#">' + str + '</a>';
                }},
                {aTargets: [7], mData: function(source) {
                    return '<a rel="popover" data-original-title="Voce" data-trigger="hover" data-placement="left" data-content="' + escapeHtml(source.pdcVoce) + '" href="#">' + source.pdcFinanziario + '</a>';
                }},
                {aTargets: [8], mData: function(source) {
                    if(!source.categoriaCapitolo) {
                        return '';
                    }
                    return '<a rel="popover" data-original-title="Descrizione" data-trigger="hover" data-placement="left" data-content="' + escapeHtml(source.categoriaCapitolo.descrizione) + '" href="#">' + source.categoriaCapitolo.codice + '</a>';
                }}
            ]
        };

        $("#risultatiRicercaCapitolo").dataTable(options);
    }


    /**
     * Apre il modale per l'eliminazione della quota del documento.
     *
     * @param ev    (Event)  l'evento scatenante
     * @param row   (Number) la riga della quota da cancellare
     * @param quota (object) la quota da eliminare
     */
    function apriModaleEliminaQuotaDocumento(ev, row, quota) {
        var modal = $("#modaleConfermaEliminazioneQuota");

        ev.preventDefault();

        $("#SPAN_elementoSelezionatoEliminazioneQuota").html(quota.numero);
        // Se clicco su no, chiudo il modale
        $("#pulsanteNoEliminazioneQuota").substituteHandler("click", function(e) {
            e.preventDefault();
            modal.modal("hide");
        });
        // Se clicco su sì, allora elimino la quota
        $("#pulsanteSiEliminazioneQuota").substituteHandler("click", function(e) {
            eliminaQuotaDocumento(e, row);
        });

        modal.modal("show");
    }

    /**
     * Aggiorna la quota del documento.
     *
     * @param e            (Event)  l'evento scatenante
     * @param subdocumento (Object) il subdocumento da aggiornare
     */
    function aggiornaQuotaDocumento(e, subdocumento) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoEntrata_inizioAggiornamentoNuovaQuota.do", {uidQuota: subdocumento.uid}, e, aggiornaQuota);
    }

    /**
     * Ripeti l'inserimento della quota del documento.
     *
     * @param e            (Event)  l'evento scatenante
     * @param subdocumento (Object) il subdocumento di cui ripetere l'inserimento
     */
    function ripetiQuotaDocumento(e, subdocumento) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoEntrata_inizioRipetiNuovaQuota.do", {uidQuota: subdocumento.uid}, e, salvaNuovaQuota);
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

        $.postJSON(
            "aggiornamentoDocumentoEntrata_eliminaQuota.do",
            {rigaDaEliminare: row},
            function(data) {
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
                caricaDataTableQuote(data.listaSubdocumentoEntrata, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire);

                $(".tab-pane.active").find("*:input")
                    .keepOriginalValues();
                triggerQuoteAggiornate(data.flagDatiIvaAccessibile);
                $("#statoDocumento").html(data.stato);
                $("#stato_tabNote").html(data.stato);
                $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
                $("#nettoDocumento").val(data.netto.formatMoney());
                $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
                $('#pulsanteAttivaRegistrazioniContabili')[data.attivaRegistrazioniContabiliVisible ? 'removeClass' : 'addClass']('hide');
                // Se il documento non e' piu incompleto disabilito l'editabilita' dei campi soggetto
                if(data.documentoIncompleto === false) {
                    disabilitaCampiSoggetto();
                }
            }
        ).always(function() {
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
        var collapse = $("#accordionInserisciQuotaEntrata");
        e.preventDefault();

        spinner.addClass("activated");

        doc.alerts.slideUp();

        collapse.load(
            url,
            obj,
            function() {
                var datepicker = $("input.datepicker", collapse);
                var dataOdierna = new Date();
                var annoDatepicker = parseInt($("#HIDDEN_anno_datepicker").val(), 10);
                var initialEditabilita = $('#annoProvvedimento_QUOTE').is('[readonly]');

                $(".tooltip-test").tooltip();
                
                // Lego le azioni ai pulsanti
                $("#pulsanteAnnullaInserisciSubdocumento").on("click", distruggiCollapseQuota);
                $("#pulsanteSalvaInserisciSubdocumento").on("click", functionSalva);                
                $("#pulsanteApriModaleCapitolo").on("click", apriCompilazioneGuidataCapitolo);
                
                $("#pulsanteAperturaCompilazioneGuidataAccertamento").on("click", apriCompilazioneGuidataAccertamento);
                //inizializzo utilizzando tutti i parametri di default eccetto il modale degli errori che e' stato creato solo con il caricamento del collapse
                Accertamento.destroy();
                Accertamento.inizializza(undefined, undefined, undefined, $('#SPAN_accertamentoH4'), undefined, $('#ERRORI_ACCERTAMENTO_MODALE'));
                $(document).substituteHandler('accertamentoCaricato', valutaVisualizzazioneRilevanteIva);
                $("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione").substituteHandler("change", ricercaAccertamentoPerChiaveOttimizzatoOnChange).trigger('change');
                $('#numeroCapitolo_QUOTE, #numeroArticolo_QUOTE, #numeroUEB_QUOTE').on('change', valutaEditabilitaAttoAmministrativo.bind(undefined, initialEditabilita));
                
                $("#pulsanteRicercaCapitolo").on("click", cercaCapitoli);
                $("#noteTesoriereSubdocumento").on("change", impostaCampoNote);
                
                if($("#noteSubdocumento").val() === ""){
                	impostaCampoNote();
                }

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
                
                 $(".trim").on('paste', function(event) { 
                    var text = (event.originalEvent.clipboardData || window.clipboardData).getData("text");
                    $(this).val(text.trim());
                	event.preventDefault();
                });
                

                $(".decimale", collapse).gestioneDeiDecimali();

                Provvedimento.inizializzazione("_QUOTE");
                ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento", "", "#annoProvvisorioCassaSubdocumento", "#numeroProvvisorioCassaSubdocumento",
                    "#causaleProvvisorioCassaSubdocumento");
                
                $('#annoProvvedimento_QUOTE, #numeroProvvedimento_QUOTE, #tipoAttoProvvedimento_QUOTE').substituteHandler('change', gestisciFlagProseguireConElaborazioneAttoAmministrativo);
                
                $('#SPAN_InformazioniProvvedimento_QUOTE').on('change', gestisciFlagProseguireConElaborazioneAttoAmministrativo);

                $('#treeStruttAmm_QUOTE').substituteHandler('ztreecheck', gestisciFlagProseguireConElaborazioneAttoAmministrativo);
                collapse.collapse("show");
                spinner.removeClass("activated");
 
                collapse.scrollToSelf();
            }
        );
    }
    
    function valutaEditabilitaAttoAmministrativo(initialEditabilita) {
        var campiAtto = $('#annoProvvedimento_QUOTE, #numeroProvvedimento_QUOTE, #tipoAttoProvvedimento_QUOTE');
        var compGuidata = $('#pulsanteApriModaleProvvedimento_QUOTE').closest('span.guidata');
        if(!!$('#numeroCapitolo_QUOTE').val() && !!$('#numeroArticolo_QUOTE').val()) {
            campiAtto.removeAttr('readonly');
            compGuidata.removeClass('hide');
        } else {
            campiAtto[initialEditabilita ? 'attr' : 'removeAttr']('readonly', true);
            compGuidata[initialEditabilita ? 'addClass' : 'removeClass']('hide');
        }
    }

    function gestisciFlagProseguireConElaborazioneAttoAmministrativo (){
        var flag_proseguireConElaborazioneAttoAmministrativo = $('#HIDDEN_proseguireConElaborazioneAttoAmministrativo');
    	flag_proseguireConElaborazioneAttoAmministrativo.val('false');
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
     * disabilita i campi relativi a soggetto -->codice
     *                               nasconde --> compilazione guidata
     */
    function disabilitaCampiSoggetto(){
        //parte aggiunta il 26_03_2015 jira 1905

        $("#codiceSoggetto").removeAttr('required');
        $("#codiceSoggetto").attr('disabled', true);
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").addClass('hide');
    }

    exports.abilitaDisabilitaAccertamentoOCapitolo = function(){
        var annoCapitolo = $("#annoCapitolo_QUOTE");
        var numeroCapitolo = $("#numeroCapitolo_QUOTE");
        var numeroArticolo = $("#numeroArticolo_QUOTE");
        var numeroUEB = $("#numeroUEB_QUOTE");
        var annoMov = $("#annoMovimentoMovimentoGestione");
        var numeroMov = $("#numeroMovimentoGestione");
        var numeroSubMov = $("#numeroSubmovimentoGestione");

        if(annoCapitolo.val() && numeroCapitolo.val() && numeroArticolo.val()){
            annoMov.prop("readonly", true).val("");
            numeroMov.prop("readonly", true).val("");
            numeroSubMov.prop("readonly", true).val("");
            $("#SPAN_pulsanteAperturaCompilazioneGuidataAccertamento").addClass("hide");
        }else if(annoMov.val() && numeroMov.val()){
            annoCapitolo.prop("readonly", true);
            numeroCapitolo.prop("readonly", true).val("");
            numeroArticolo.prop("readonly", true).val("");
            numeroUEB.prop("readonly", true).val("");
            $("#SPAN_pulsanteApriModaleCapitolo").addClass("hide");
        }else{
            annoMov.removeProp("readonly");
            numeroMov.removeProp("readonly");
            numeroSubMov.removeProp("readonly");
            $("#SPAN_pulsanteAperturaCompilazioneGuidataAccertamento").removeClass("hide");
            annoCapitolo.removeProp("readonly");
            numeroCapitolo.removeProp("readonly");
            numeroArticolo.removeProp("readonly");
            numeroUEB.removeProp("readonly");
            $("#SPAN_pulsanteApriModaleCapitolo").removeClass("hide");
        }

    };


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
     * Richiama l'esecuzione della ricerca Capitoli.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function cercaCapitoli(e) {
        var alertErrori = $("#ERRORI_MODALE_CAPITOLO");
        var oggettoPerChiamataAjax = unqualify($("#fieldsetRicercaGuidataCapitolo").serializeObject(), 1);
        var divTabella = $("#divTabellaCapitoli").slideUp();
        var spinner = $("#SPINNER_RicercaCapitolo");
        e.preventDefault();
        alertErrori.slideUp();

        spinner.addClass("activated");
        $.postJSON(
            "selezionaCapitoloEntrataGestione.do",
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori, false)) {
                    return;
                }

                // Carico i dati in tabella
                tabellaCapitoli(data.listaCapitoli);
                divTabella.slideDown();
                $("#divVisualizzaDettaglio").slideDown();
                $("#pulsanteVisualizzaDettaglio").substituteHandler("click", visualizzaDettaglioCapitolo);
                $("#pulsanteConfermaCapitolo").substituteHandler("click", confermaCapitolo);
                $("#collapseDettaglioCapitolo").removeClass("in");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }
  
    /**
     * Valutazione della visualizzazione del rilevante Iva.
     *
     * @param e (Event) l'evento scatenante (Optional, default: undefined)
     */
    function valutaVisualizzazioneRilevanteIva(e) {
        var accertamento;
        
        if($("#uidSubdocumento").val() !== "0") {
            // Non sono in inserimento: non faccio alcunche'
            return;
        }
     	// nel caricamento a partire dalla compilazione guidata: imposto subito il campo
        accertamento = e && e.accertamento;        
        showRilevanteIva(accertamento && accertamento.capitoloEntrataGestione && accertamento.capitoloEntrataGestione.flagRilevanteIva);            

        return;

    }
    
    // Jira SIAC-7001 Alessandro T.
    function inserisciValoriCapitolo(capitolo) {
        var strCapitolo;
        strCapitolo = ' - Capitolo: ' +  capitolo.annoCapitolo + ' / ' + capitolo.numeroCapitolo + ' / ' + capitolo.numeroArticolo;
        if(capitolo.numeroUEB === '' || capitolo.numeroUEB === undefined || !gestioneUEB){
            return strCapitolo;
        }
        strCapitolo += ' / ' + capitolo.numeroUEB;
        return strCapitolo;
    }

    /***
     * Ricerca l'accertamento se digitato direttamente nei campi anno e numero. 
     * 
     * */
    function ricercaAccertamentoPerChiaveOttimizzatoOnChange(){
    	var fieldAnno;
        var fieldNumero;
        var fields;
    	
    	// Ricerco il movimento
        fieldAnno = $("#annoMovimentoMovimentoGestione");
        fieldNumero = $("#numeroMovimentoGestione");
        if(!fieldAnno.val() || !fieldNumero.val()) {
            // Non ho impostato tutti i dati necessarii
            return;
        }
        fields = fieldAnno.add(fieldNumero);
        fields.overlay("show");
        // Devo ricercare l'impegno e vederne i dati
        //task-232
        return $.postJSON("ricercaAccertamentoPerChiaveOttimizzato.do", {"accertamento.annoMovimento": fieldAnno.val(), "accertamento.numero": fieldNumero.val().trim(), "caricaSub":false})
	        .then(function(data) {
	        	var str;
	        	var accertamento;
	        	var capitolo;
	        	
	            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
	                // Se ho errori esco
	                return;
	            }
	            
	            accertamento = data.accertamento;
	            // Prendo il campo del rilevante dal capitolo se presente
	            showRilevanteIva(data.accertamento && data.accertamento.capitoloEntrataGestione && data.accertamento.capitoloEntrataGestione.flagRilevanteIva);
                
                str = ': ' + accertamento.annoMovimento + ' / ' + accertamento.numero;
                
                // Jira SIAC-7001 Alessandro T.
                capitolo = accertamento.capitoloEntrataGestione;
                str = str + '' + inserisciValoriCapitolo(capitolo);
                
                $('#SPAN_accertamentoH4').html(str);

            }).always(function() {
	            fields.overlay("hide");
	        });
    }

    /**
     * Mostra o nasconde lo span del rilevante iva.
     *
     * @param toShow (boolean) se il campo sia da mostrare
     */
    function showRilevanteIva(toShow) {
        $("#spanCapitoloRilevanteIvaQuote")[toShow ? "show" : "hide"]();
    }

    /**
     * Conferma il capitolo selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaCapitolo(e) {
        var checkedRadio = $("#risultatiRicercaCapitolo").find("input[name='checkCapitolo']:checked");
        var capitolo;

        e.preventDefault();

        if(checkedRadio.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un capitolo"], $("#ERRORI_MODALE_CAPITOLO"), false);
            return;
        }

        capitolo = checkedRadio.data("sourceCapitolo");

        $("#SPAN_InformazioniCapitolo_QUOTE").html(capitolo.annoCapitolo + "/" + capitolo.capitolo);
        $("#annoCapitolo_QUOTE").val(capitolo.annoCapitolo).attr("readonly", true);
        $("#numeroCapitolo_QUOTE").val(capitolo.numeroCapitolo).attr("readonly", true);
        $("#numeroArticolo_QUOTE").val(capitolo.numeroArticolo).attr("readonly", true);
        $("#numeroUEB_QUOTE").val(capitolo.numeroUEB).attr("readonly", true);

        $("#modaleGuidaCapitolo").modal("hide");
    }



    /**
     * Conferma il capitolo selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function visualizzaDettaglioCapitolo(e) {
        var checkedRadio = $("#risultatiRicercaCapitolo").find("input[name='checkCapitolo']:checked");
        var collapse = $("#collapseDettaglioCapitolo");
        var capitoloSelezionato;
        e.preventDefault();

        if(checkedRadio.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un capitolo"], $("#ERRORI_MODALE_CAPITOLO"), false);
            return;
        }

        capitoloSelezionato = checkedRadio.data("sourceCapitolo");

        $.each(["Competenza", "Cassa", "Residuo"], function() {
            var i = this;
            var j;
            var k;
            for(j = 0; j < 3; j++) {
                k = j || "";
                $("#modaleElementoSelezionato" + i + "Anno" + j).html(capitoloSelezionato["stanziamento" + i + k].formatMoney());
            }
        });

        $("#informazioniCapitoloModale").html("Capitolo " + capitoloSelezionato.capitolo);

        collapse.addClass("in");
    }

    /**
     * Apre il modale per la compilazione guidata dell'accertamento.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriCompilazioneGuidataAccertamento(e) {
        e.preventDefault();

        $("#annoAccertamentoModale").val($("#annoMovimentoMovimentoGestione").val());
        $("#numeroAccertamentoModale").val($("#numeroMovimentoGestione").val());

        // Jira SIAC-7001 Alessandro T.
        // cambio i parametri 
        $("#pulsanteConfermaModaleAccertamento").click(e,ricercaAccertamentoPerChiaveOttimizzatoOnChange);
        
        campoRicercaEffettuataConSuccesso = $("#hidden_ricercaEffettuataConSuccessoModaleAccertamento");
        campoRicercaEffettuataConSuccesso.val("");
        $("#hidden_flagRilevanteIva").val("");
        $("#modaleAccertamento").modal("show");
    }

    /**
     * Apre il modale per la compilazione guidata del capitolo
     *
     * @param e (Event) l'evento scatenante
     */
    function apriCompilazioneGuidataCapitolo(e) {
        e.preventDefault();
        $("#annoCapitolo_modale").val($("#annoCapitolo_QUOTE").val());
        $("#numeroCapitolo_modale").val($("#numeroCapitolo_QUOTE").val());
        $("#numeroArticolo_modale").val($("#numeroArticolo_QUOTE").val());
        $("#numeroUEB_modale").val($("#numeroUEB_QUOTE").val());

        $("#ERRORI_MODALE_CAPITOLO").slideUp();
        $("#divTabellaCapitoli").slideUp();
        $("#divVisualizzaDettaglio").slideUp();
        $("#collapseDettaglioCapitolo").removeClass("in");

        $("#modaleGuidaCapitolo").modal("show");

    }



    /**
     * Apre il modale di inserimento per una nuova quota
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.aperturaInserimentoNuovaQuota = function(e) {
        caricaEApriCollapseQuota("aggiornamentoDocumentoEntrata_inizioInserimentoNuovaQuota.do", {}, e, salvaNuovaQuota);
    };

    /**
     * Carica la tabella delle quote.
     *
     * @param lista               (Array)  la lista delle quote
     * @param totaleQuote         (Number) il totale delle quote
     * @param totaleDaPagareQuote (Number) il totale da pagare delle quote
     * @param importoDaAttribuire (Number) l'importo da attribuire
     */
    exports.caricaListaQuote = function(lista, totaleQuote, totaleDaPagareQuote, importoDaAttribuire) {
        caricaDataTableQuote(lista, totaleQuote, totaleDaPagareQuote, importoDaAttribuire);
    };

    doc.Quote = exports;
    return doc;
}(jQuery, Documento || {}));

$(function() {

    // Gestione dei pulsanti
    $("#pulsanteInserimentoNuovaQuota").on("click", Documento.Quote.aperturaInserimentoNuovaQuota);

    $.postJSON("aggiornamentoDocumentoEntrata_ottieniListaQuote.do", function(data) {
        Documento.Quote.caricaListaQuote(data.listaSubdocumentoEntrata, data.totaleQuote, data.totaleDaPagareQuote, data.importoDaAttribuire);
    });

    $.postJSON("aggiornamentoDocumentoEntrata_campiDisabilitati.do", function(data) {
        if(!data.capitoloQuotaDisabilitato && !data.impegnoQuotaDisabilitato){
            $(document).on("blur", "#annoCapitolo_QUOTE, #numeroCapitolo_QUOTE, #numeroArticolo_QUOTE, #numeroUEB_QUOTE, #annoMovimentoMovimentoGestione, #numeroMovimentoGestione, #numeroSubmovimentoGestione",
                    Documento.Quote.abilitaDisabilitaAccertamentoOCapitolo);
        }
    });

    // CR-2541
    $("#modale_hidden_tipoProvvisorioDiCassa").val("E");
});