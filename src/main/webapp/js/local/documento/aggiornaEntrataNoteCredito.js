/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($, doc) {
    var exports = {};

    /**
     * Distrugge il collapse della quota.
     *
     * @param e (Event) l'evento scatenante
     */
    function distruggiCollapseNotaCredito(e) {
        e.preventDefault();
        $("#accordionInserisciNotaCreditoEntrata").collapse("hide");
    }

    /**
     * Salva la nuova quota all'interno del documento.
     *
     * @param e (Event) l'evento scatenante
     */
    function salvaNuovaNotaCredito(e) {
        var oggettoPerChiamataAjax = $("#fieldsetInserisciNotaCredito").serializeObject();
        var spinner = $("#SPINNER_pulsanteNotaCreditoSalva");
        e.preventDefault();

        spinner.addClass("activated");

        doc.alertErrori.slideUp();

        $.postJSON(
            "aggiornamentoDocumentoEntrata_inserimentoNuovaNotaCredito.do",
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
                caricaDataTableNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito, data.totaleImportoDaDedurreSuFattura);
                $("#nettoDocumento").val(data.netto.formatMoney());
                $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
                $("#noteCreditoDocumento").val(data.totaleImportoDaDedurreSuFattura.formatMoney());
                $("#SPAN_importoDaAttribuireDocumento").html(data.importoDaAttribuire.formatMoney());
                $("#statoDocumento").html(data.stato);
                $("#stato_tabNote").html(data.stato);
                $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
                // Effettuare il keepOriginalValues
                $("#accordionInserisciNotaCreditoEntrata").find("*:input")
                    .keepOriginalValues();
                $("#formAggiornamentoDatiDocumento").find(":input")
                    .keepOriginalValues();
                $("#accordionInserisciNotaCreditoEntrata").collapse("hide");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Aggiorna la nota.
     *
     * @param e (Event)  l'evento scatenante
     */
    function aggiornaNotaCredito(e) {
        var oggettoPerChiamataAjax = $("#fieldsetInserisciNotaCredito").serializeObject();
        var spinner = $("#SPINNER_pulsanteNotaCreditoSalva");
        e.preventDefault();

        spinner.addClass("activated");

        doc.alertErrori.slideUp();

        $.postJSON(
            "aggiornamentoDocumentoEntrata_aggiornamentoNotaCredito.do",
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
                caricaDataTableNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito, data.totaleImportoDaDedurreSuFattura);
                $("#nettoDocumento").val(data.netto.formatMoney());
                $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
                $("#noteCreditoDocumento").val(data.totaleImportoDaDedurreSuFattura.formatMoney());
                $("#SPAN_importoDaAttribuireDocumento").html(data.importoDaAttribuire.formatMoney());
                $("#statoDocumento").html(data.stato);
                $("#stato_tabNote").html(data.stato);
                $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
                // Chiamare il keepOriginalValues
                $("#accordionInserisciNotaCreditoEntrata").find("*:input")
                    .keepOriginalValues();
                $("#formAggiornamentoDatiDocumento").find(":input")
                    .keepOriginalValues();
                $("#accordionInserisciNotaCreditoEntrata").collapse("hide");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }


    /**
     * Permette la visualizzazione del dettaglio quote relativo alla nota di credito.
     *
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param obj (Object) l'oggetto contenuto nella tabella
     */
    function dettaglioQuote(e, uid) {
        var modaleDaPopolare = $("#modaleDettaglioQuote");
        var spinner = $("#SPINNER_pulsanteDettaglioQuoteNotaCredito").addClass("activated");

        e.preventDefault();

        $.postJSON(
            "aggiornamentoDocumentoEntrata_ottieniListaQuoteNotaCredito.do",
            {uidDaConsultare : uid},
            function(data) {
                var fieldset = $("#fieldsetModaleDettaglioQuoteNoteCredito");
                var netto = data.netto || 0;
                if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_QUOTA"), false)) {
                    return;
                }
                Documento.NoteCredito.popolaTabellaQuote("tabella_modaleDettaglioQuote", data.listaSubdocumentoEntrata);
                // SPAN dei totali
                $("#totaleNoteCreditoModale").html(data.totaleImportoDaDedurreSuFattura.formatMoney());
                $("#totaleQuoteNotaCreditoModale").html(data.totaleQuoteNotaCredito.formatMoney());
                $("#totaleImportoDaDedurreNotaCreditoModale").html(data.totaleImportoDaDedurre.formatMoney());
                $("#nettoDocumento").val(netto.formatMoney());

                $("#pulsanteApplicaNoteCreditoModale").substituteHandler("click", applicaImportiDaDedurre);
                $("#pulsanteConfermaNoteCreditoModale").substituteHandler("click", confermaImportiDaDedurre);

                modaleDaPopolare.find(".decimale")
                        .gestioneDeiDecimali()
                        .end()
                    .find(".soloNumeri").allowedChars({numeric: true});

                fieldset.find("*:input")
                    .keepOriginalValues();

                $("#formAggiornamentoDatiDocumento").find(":input")
                .keepOriginalValues();

                modaleDaPopolare.modal("show");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    function applicaImportiDaDedurre(e) {
        var fieldset = $("#fieldsetModaleDettaglioQuoteNoteCredito");
        var oggettoPerChiamataAjax = fieldset.serializeObject();
        var spinner = $("#SPINNER_pulsanteApplicaNoteCreditoModale").addClass("activated");

        e.preventDefault();

        $.postJSON(
            "aggiornamentoDocumentoEntrata_applicaImportiDaDedurre.do",
            unqualify(oggettoPerChiamataAjax, 2),
            function(data) {
                if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_QUOTA"), false)) {
                    return;
                }

                impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI_MODALE_QUOTA"));

                Documento.NoteCredito.popolaTabellaQuote("tabella_modaleDettaglioQuote", data.listaSubdocumentoEntrata);
                // SPAN dei totali
                $("#totaleNoteCreditoModale").html(data.totaleImportoDaDedurreSuFattura.formatMoney());
                $("#totaleQuoteNotaCreditoModale").html(data.totaleQuoteNotaCredito.formatMoney());
                $("#totaleImportoDaDedurreNotaCreditoModale").html(data.totaleImportoDaDedurre.formatMoney());
                $("#nettoDocumento").val(data.netto.formatMoney());
                $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
                $("#SPAN_totaleDaPagareQuoteIntestazione").html(data.totaleDaPagareQuote.formatMoney());
                $("#thTotaleDaPagareQuote").html(data.totaleDaPagareQuote.formatMoney());
                $("#SPAN_importoDaAttribuireDocumento").html(data.importoDaAttribuire.formatMoney());

                fieldset.find("*:input")
                    .keepOriginalValues();
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    function confermaImportiDaDedurre(e) {
        var modaleDaPopolare = $("#modaleDettaglioQuote");
        var fieldset = $("#fieldsetModaleDettaglioQuoteNoteCredito");
        var changedValues = fieldset.find("*:input:not(.modal *:input)")
            .findChangedValues();

        e.preventDefault();

        if(changedValues.length > 0) {
            impostaDatiNegliAlert(["Esistono dati non salvati"], $("#ERRORI_MODALE_QUOTA"), false);
            return;
        }

        modaleDaPopolare.modal("hide");
    }

    /**
     * Caricamento via Ajax della tabella delle quote
     *
     * @param idTabella {String} l'id della tabella
     * @param lista     {Array}  la lista da impostare
     */
    exports.popolaTabellaQuote = function(idTabella, lista) {
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
                sZeroRecords: "Non sono presenti capitoli associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs : [
                {aTargets: [ 0 ], mData: "numero"},
                {
                    aTargets: [1],
                    mData: function(source) {
                        if(!source.stringaAccertamento && source.accertamento) {
                            // Solo se l'accertamento è presente
                            source.stringaAccertamento = "" +source.accertamento.annoMovimento + "/" + source.accertamento.numero;
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
                            source.stringaProvvedimento = "<a rel='popover' href='#'>" + source.attoAmministrativo.anno + "/" + source.attoAmministrativo.numero + "/" +
                                source.attoAmministrativo.tipoAtto.codice.toUpperCase();
                            if(source.attoAmministrativo.strutturaAmmContabile) {
                                source.stringaProvvedimento += "/" + source.attoAmministrativo.strutturaAmmContabile.codice;
                            }
                            source.stringaProvvedimento += "</a>";
                        }
                        return source.stringaProvvedimento || "";
                    },
                    fnCreatedCell: function(nTd, sData, oData) {
                        var settings;
                        var anchor = $("a", nTd);
                        if(oData.attoAmministrativo) {
                            settings = {
                                content: oData.attoAmministrativo.oggetto,
                                title: "Oggetto",
                                trigger: "hover"
                            };
                            anchor.popover(settings);
                        }
                        anchor.on("click", function(e) {
                            e.preventDefault();
                        });
                    }
                },
                {
                    aTargets: [3], mData: function(source) {
                        return source.ordinativo ? source.ordinativo.anno + "/" + source.ordinativo.numero : "";
                    }
                },
                {
                    aTargets: [4], mData: function(source) {
                        return source.provvisorioCassa
                        ? (source.provvisorioCassa.dataEmissione && formatDate(source.provvisorioCassa.dataEmissione) || "")
                            + " - " + source.provvisorioCassa.numero
                        : "";
                    }
                },
                {
                    aTargets: [ 5 ],
                    mData: function(source) {
                        var value = source.importoDaDedurre ? source.importoDaDedurre : 0;
                        var importoDaDedurre = '<input id="impDed' + source.uid + '" class="lbTextSmall soloNumeri decimale span12 text-right" type="text" value="'+value.formatMoney()+'"';
                        if(source.ordinativo){
                            importoDaDedurre += 'disabled = "true" ';
                        }
                        importoDaDedurre += '/>';
                        return importoDaDedurre;
                    },
                    fnCreatedCell: function(nTd, sData, oData, iRow) {
                        $("input", nTd).attr("name", "noteCredito.quote.listaSubdocumentoEntrata[" + iRow + "].importoDaDedurre");

                    }
                },
                {
                    aTargets: [ 6 ],
                    mData: function(source) {
                        return source.importo.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#" + idTabella).dataTable(options);
    };


    /**
     * Carica il dataTable delle noteCredito (documentiCollegati).
     *
     * @param lista (Array) la lista tramite cui popolare la tabella
     */
    function caricaDataTableNoteCredito(lista, totaleNoteCredito, totaleImportoDaDedurreSuFattura) {
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
                sZeroRecords: "Non sono presenti noteCredito associate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {"aTargets" : [ 0 ], "mData" : "documento"},
                {"aTargets" : [ 1 ], "mData" : "data"},
                {
                    "aTargets" : [ 2 ],
                    "mData" : function(source) {
                        return "<a rel='popover' href='#'>" + source.statoOperativoDocumentoCode + "</a>";
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.statoOperativoDocumentoDesc,
                            "title" : "Stato",
                            "trigger" : "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).substituteHandler("click", function(event) {
                                event.preventDefault();
                            }).popover(settings);
                    }
                },
                {"aTargets" : [ 3 ], "mData" : "soggetto"},
                {
                    "aTargets" : [ 4 ],
                    "mData" : function(source) {
                        return source.importoDaDedurreSuFattura.formatMoney();
                    },
                    "fnCreatedCell" : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    "aTargets" : [ 5 ],
                    "mData" : function(source) {
                        return source.importo.formatMoney();
                    },
                    "fnCreatedCell" : function(nTd) {
                        $(nTd).addClass("text-right");
                    }
                },
                {
                    aTargets: [6],
                    mData: function(source) {
                        if(source.gestioneStatoNota) {
                            if(!source.azioni) {
                            	if(source.gestioneStatoSDINota) {   //SIAC-6988
                            		source.azioni = "<div class='btn-group'>" +
                                    "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                                    "<ul class='dropdown-menu pull-right'>" +  
                                        "<li><a href='#' class='consultaNotaCredito'>consulta</a></li>" +
                                    "</ul>" +
                                "</div>";
                            	}else{
                            		source.azioni = "<div class='btn-group'>" +
                                    "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                                    "<ul class='dropdown-menu pull-right'>" +  
                                        "<li><a href='#' class='consultaNotaCredito'>consulta</a></li>" +
                                        "<li><a href='#' class='scollegaNotaCredito'>scollega</a></li>" +
                                        "<li><a href='#' class='aggiornaNotaCredito'>aggiorna</a></li>" +
                                        "<li><a href='#' class='annullaNotaCredito'>annulla</a></li>" +
                                    "</ul>" +
                                "</div>";	
                            	}
                            }
                        } else {
                            source.azioni = "";
                        }
                        return source.azioni;
                    },
                    fnCreatedCell: function(nTd, sData, oData, iRow) {
                        $(nTd).addClass("tabRight")
                            .find(".consultaNotaCredito")
                                .substituteHandler("click", function(e) {
                                    consultaNotaCreditoDocumento(e, oData);
                                })
                                .end()
                            .find(".aggiornaNotaCredito")
                                .substituteHandler("click", function(e) {
                                    aggiornaNotaCreditoDocumento(e, oData);
                                })
                                .end()
                            .find(".scollegaNotaCredito")
                                .substituteHandler("click", function(e) {
                                	apriModaleScollegaNotaCredito(e, oData);
                                })
                                .end()
                             .find(".annullaNotaCredito")
                                .substituteHandler("click", function(e) {
                                    apriModaleAnnullaNotaCredito(e, iRow, oData);
                                })
                                .end();
                    }
                }
            ]
        };
        $("#tabellaNoteCreditoDocumento").dataTable(options);
        $("#thtotaleNoteCredito").html(totaleNoteCredito.formatMoney());
        $("#thtotaleDaDedurreSuFattura").html(totaleImportoDaDedurreSuFattura.formatMoney());
        $("#noteCreditoDocumento").val(totaleImportoDaDedurreSuFattura.formatMoney()).keepOriginalValues();

    }

    /**
     * Aggiorna la nota di credito.
     *
     * @param e            (Event)  l'evento scatenante
     * @param documento (Object) il documento da aggiornare
     */
    function aggiornaNotaCreditoDocumento(e, documento) {
        caricaEApriCollapseNotaCredito("aggiornamentoDocumentoEntrata_inizioAggiornamentoNuovaNotaCredito.do", {uidNotaCredito: documento.uid}, e, aggiornaNotaCredito);
    }

    /**
     * Consulta la nota di credito.
     *
     * @param e            (Event)  l'evento scatenante
     * @param documento (Object) il documento da consultare
     */
    function consultaNotaCreditoDocumento(e, documento) {
        e.preventDefault();
        document.location = "consultaDocumentoEntrata.do?uidDocumento="+documento.uid;
    }


    /**
     * Apre il modale di eliminazione della notaCredito e lega le azioni corrette ai pulsanti.
     *
     * @param e   (Event)  l'evento scatenante
     * @param row (Number) la riga selezionata
     */
    function apriModaleAnnullaNotaCredito(e, row, nota) {
        var modal = $("#modaleConfermaAnnullamentoNotaCredito");
        e.preventDefault();

        $("SPAN_elementoSelezionatoAnnullamentoNotaCredito").html(nota.documento);

        $("#pulsanteNoEliminazioneNotaCredito").substituteHandler("click", function(e) {
            e.preventDefault();
            modal.modal("hide");
        });

        $("#pulsanteSiEliminazioneNotaCredito").substituteHandler("click", function(e) {
            e.preventDefault();
            annullaNotaCredito(row);
        });

        modal.modal("show");
    }
    
    /**
     * Apre il modale di scollega della notaCredito e lega le azioni corrette ai pulsanti.
     *
     * @param e   (Event)  l'evento scatenante
     * @param row (Number) la riga selezionata
     */
    function apriModaleScollegaNotaCredito(e, documento) {
        var modal = $("#modaleConfermaScollegaNotaCredito");
        e.preventDefault();

        $("#pulsanteNoScollegaNotaCredito").substituteHandler("click", function(e) {
            e.preventDefault();
            modal.modal("hide");
        });

        $("#pulsanteSiScollegaNotaCredito").substituteHandler("click", function(e) {
            e.preventDefault();
            scollegaNotaCreditoDocumento(documento);
        });
        $("#SPAN_elementoSelezionatoScollegaNotaCredito").html(documento.documento);
        modal.modal("show");
    }
    /**
     * Scollega la nota di credito.
     *
     * @param documento (Object) il documento da scollegare
     */
    function scollegaNotaCreditoDocumento(documento) {
        var spinner = $("#SPINNER_pulsanteScollegaNotaCredito").addClass("activated");
        var modal = $("#modaleConfermaScollegaNotaCredito");
        
        $.postJSON("aggiornamentoDocumentoEntrata_scollegaNotaCreditoEsistente.do", {uidDocumentoDaScollegare : documento.uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                return;
            }
            // SIAC-4433
            $("#totaleNoteCreditoModale").html(data.totaleImportoDaDedurreSuFattura.formatMoney());
            $("#totaleQuoteNotaCreditoModale").html(data.totaleQuoteNotaCredito.formatMoney());
            $("#totaleImportoDaDedurreNotaCreditoModale").html(data.totaleImportoDaDedurre.formatMoney());
            $("#nettoDocumento").val(data.netto.formatMoney());
            $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
            $("#SPAN_totaleDaPagareQuoteIntestazione").html(data.totaleDaPagareQuote.formatMoney());
            $("#thTotaleDaPagareQuote").html(data.totaleDaPagareQuote.formatMoney());
            $("#SPAN_importoDaAttribuireDocumento").html(data.importoDaAttribuire.formatMoney());
            $("#statoDocumento").html(data.stato);
            $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);
            $("#HIDDEN_flagQuotaConImportoDaDedurre").val(data.totaleImportoDaDedurre > 0 ? 'true':'false');
            $("#formAggiornamentoDatiDocumento").find(":input").keepOriginalValues();

            ricalcolaListaDocumentiCollegati();
        }).always(function() {
        	 modal.modal("hide");
			 spinner.removeClass("activated");
         });
    }

    
    function ricalcolaListaDocumentiCollegati (){
    	$("#tabNoteCredito").overlay("show");
    	$.postJSON("aggiornamentoDocumentoEntrata_ricalcolaListaDocumentiCollegati.do", function(data) {
       	 if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
            	
                return;
            }
            impostaDatiNegliAlert(data.informazioni, Documento.alertInformazioni);
            caricaDataTableNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito, data.totaleImportoDaDedurreSuFattura);
           
		 	}).always(function() {
		 		$("#tabNoteCredito").overlay("hide");
          
		 	});
    	
    }
    /**
     * Annulla la  nota di credito e aggiorna la tabella.
     *
     * @param row (Number) la riga selezionata
     */
    function annullaNotaCredito(row) {
        var spinner = $("#SPINNER_pulsanteEliminaNotaCredito").addClass("activated");
        var modal = $("#modaleConfermaAnnullamentoNotaCredito");


        doc.alertErrori.slideUp();

        $.postJSON(
            "aggiornamentoDocumentoEntrata_annullaNotaCredito.do",
            {rigaDaEliminare: row},
            function(data) {
                if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                    modal.modal("hide");
                    return;
                }

                impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
                $("#nettoDocumento").val(data.netto.formatMoney());
                $("#SPAN_nettoDocumento").html(data.netto.formatMoney());
                $("#SPAN_importoDaAttribuireDocumento").html(data.importoDaAttribuire.formatMoney());
                // Ricarico la lista
                caricaDataTableNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito, data.totaleImportoDaDedurreSuFattura);
                $("#noteCreditoDocumento").val(data.totaleImportoDaDedurreSuFattura.formatMoney());
                $("#statoDocumento").html(data.stato);
                $("#stato_tabNote").html(data.stato);
                $("#HIDDEN_statoOperativoDocumentoCompleto").val(data.statoOperativoDocumentoCompleto);

                $("#formAggiornamentoDatiDocumento").find(":input")
                    .keepOriginalValues();
                modal.modal("hide");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Carica dal servizio il collapse della nota di credito e lo apre.
     *
     * @param url           (String)   l'URL da invocare
     * @param obj           (Object)   l'oggetto, eventualemente vuoto, da passare all'invocazione
     * @param e             (Event)    l'evento scatenante l'invocazione
     * @param functionSalva (Function) la funzione da invocare al salvataggio
     */
    function caricaEApriCollapseNotaCredito(url, obj, e, functionSalva) {
        var spinner = $("#SPINNER_pulsanteInserisciNuovaNotaCredito");
        var collapse = $("#accordionInserisciNotaCreditoEntrata");
        e.preventDefault();

        spinner.addClass("activated");

        collapse.load(
            url,
            obj,
            function() {
                var dataOdierna = new Date();
                var annoDatepicker = parseInt($("#HIDDEN_anno_datepicker").val(), 10);
                if(!isNaN(annoDatepicker)) {
                    dataOdierna.setFullYear(annoDatepicker);
                }

                // Lego le azioni ai pulsanti
                $("#pulsanteAnnullaInserisciNotaCredito").on("click", distruggiCollapseNotaCredito);
                $("#pulsanteSalvaInserisciNotaCredito").on("click", functionSalva);
                $("#importoNotaDocumento").on("change", function(){
                	$("#importoDaDedurreSuFattura").val($("#importoNotaDocumento").val());
                	$("#importoDaDedurreSuFattura").blur();
                });

                $("input.datepicker", collapse).each(function() {
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

                Provvedimento.inizializzazione("_NOTECREDITO");
                preSelezionaSeUnicaOpzione($('#tipoDocumento'));
                collapse.collapse("show")
                    .scrollToSelf();
                spinner.removeClass("activated");
            }
        );
    }

    /**
     * Apre il collapse di inserimento per una nuova quota
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.aperturaInserimentoNuovaNotaCredito = function(e) {
        caricaEApriCollapseNotaCredito("aggiornamentoDocumentoEntrata_inizioInserimentoNuovaNotaCredito.do", {}, e, salvaNuovaNotaCredito);
    };

    /**
     * Apre il modale per la visualizzazione del dettaglio quote
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.aperturaDettaglioQuoteNotaCredito = function(e) {
        dettaglioQuote(e, $("#HIDDEN_documentoPerNOTECREDITO_Uid").val());
    };

    /**
     * Carica la tabella delle noteCredito.
     *
     * @param lista (Array) la lista delle noteCredito
     */
    exports.caricaListaNoteCredito = function(lista, totaleNoteCredito, totaleImportoDaDedurreSuFattura) {
        caricaDataTableNoteCredito(lista, totaleNoteCredito, totaleImportoDaDedurreSuFattura);
    };
    /**
     * Ricalcola i dati prima di ricaricare la  la tabella delle noteCredito.
     *
     * @param lista (Array) la lista delle noteCredito
     */
    exports.ricalcolaListaDocCollegati = function() {
    	ricalcolaListaDocumentiCollegati();
    };
    doc.NoteCredito = exports;
    return doc;
}(jQuery, Documento || {}));

$(function() {

    // Gestione dei pulsanti
    $("#pulsanteInserimentoNuovaNotaCredito").on("click", Documento.NoteCredito.aperturaInserimentoNuovaNotaCredito);
    $("#pulsanteDettaglioQuoteNotaCredito").on("click", Documento.NoteCredito.aperturaDettaglioQuoteNotaCredito);
    NotaCreditoEntrata.inizializza("#pulsanteCollegaEntrataNotaCreditoEsistente");
    $.postJSON(
        "aggiornamentoDocumentoEntrata_ottieniListaNoteCredito.do",
        function(data) {
            Documento.NoteCredito.caricaListaNoteCredito(data.listaDocumentoEntrata, data.totaleNoteCredito, data.totaleImportoDaDedurreSuFattura);
        }
    );

});