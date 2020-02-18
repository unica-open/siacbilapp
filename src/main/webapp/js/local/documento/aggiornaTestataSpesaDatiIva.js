/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, w, doc) {

    "use strict";

    var exports = {};
    // Documento
    var d = w.document;
    // Legami con i dati iva: sono dati statici della pagina, dunque li calcolo un'unica volta
    var legameConIvaPresente = "true" === $("#HIDDEN_legameConIvaPresente").val();
    var documentoIvaLegatoDocumentoPresente = "true" === $("#HIDDEN_documentoIvaLegatoDocumentoPresente").val();

    /**
     * Ottiene le quote rilevanti IVA dalla action e popola con esse la tabella corrispondente
     */
    function ottieniQuoteIva() {
        var spinner = $("#SPINNER_elencoQuoteRilevantiIva").addClass("activated");
        var tabella = $("#tabellaQuoteRilevantiIva").slideUp();

        // Inizio con il nascondere la tabella se visibile
        tabella.is(":visible") && tabella.slideUp();

        $.postJSON("aggiornamentoTestataDocumentoSpesa_ottieniQuoteRilevantiIva.do", function(data) {
            // Se ho degli errori, esco subito
            if (impostaDatiNegliAlert(data.errori, doc.alertErrori, false, false)) {
                return;
            }
            // Popolo la tabella
            popolaTabellaQuoteRilevantiIva(data.listaQuoteRilevantiIva);
         }).always(function() {
            spinner.removeClass("activated");
            // Mostro la tabella
            tabella.slideDown();
         });
    }

    /**
     * Popola la tabella delle quote rilevanti iva a partire dalla lista delle stesse.
     *
     * @param lista (Array) la lista delle quote tramite cui popolare la tabella
     */
    function popolaTabellaQuoteRilevantiIva(lista) {
        var opts = {
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
                sZeroRecords: "Non sono presenti quote rilevanti iva associate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                    {
                        aTargets: [ 0 ],
                        mData: "numero"
                    },
                    {
                        aTargets: [ 1 ],
                        mData: "movimento"
                    },
                    {
                        aTargets: [ 2 ],
                        mData: "capitolo"
                    },
                    {
                        aTargets: [ 3 ],
                        mData: "attivitaIva"
                    },
                    {
                        aTargets: [ 4 ],
                        mData: "registrazioneIva"
                    },
                    {
                        aTargets: [ 5 ],
                        mData: function(source) {
                            return source.importo.formatMoney();
                        },
                        fnCreatedCell: function(nTd) {
                            $(nTd).addClass("tab_Right");
                        }
                    },
                    {
                        aTargets: [ 6 ],
                        mData: function(source) {
                            return source.importoTotaleMovimentiIva
                                    .formatMoney();
                        },
                        fnCreatedCell: function(nTd) {
                            $(nTd).addClass("tab_Right");
                        }
                    },
                    {
                        aTargets: [ 7 ],
                        mData: function(source) {
                            return "<div class='btn-group'>" +
                                    "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                                        "<ul class='dropdown-menu pull-right'>" +
                                            "<li><a href='#'>" + (source.subdocumentoIva ? "Aggiorna" : "Inserisci") + "</a></li>" +
                                        "</ul>" +
                                    "</div>";
                        },
                        fnCreatedCell: function(nTd, sData, oData) {
                            $("a", nTd).substituteHandler("click", function(e) {
                                var url = "aggiornamentoTestataDocumentoSpesa_redirezione$DocumentoIvaSpesa_quota.do";
                                if (oData.subdocumentoIva && oData.subdocumentoIva.uid) {
                                    url = url.replace("$", "Aggiornamento") + "?uidSubdocumentoIva=" + oData.subdocumentoIva.uid;
                                } else {
                                    url = url.replace("$", "Inserimento") + "?uidQuota=" + oData.uid;
                                }
                                e.preventDefault();
                                d.location = url;
                            });
                        }
                    }
                ]
            };
        $("#tabellaQuoteRilevantiIva").dataTable(opts);
    }

    /**
     * Gestisce la visalizzazione dei div del documento iva.
     *
     * @param e    (Event)  l'evento scatenante l'invocazione
     */
    function gestioneVisualizzazioneDiv(e) {
        var divs = $("#inserimentoSubdocumentoIvaDocumento, #inserimentoSubdocumentoIvaQuota");
        var selectedRadio = $("input[name='tipoInserimentoDatiIva']").filter(":checked");
        var div;
        var first = !!e.firstTime;

        if(selectedRadio.val() === "Documento" && legameConIvaPresente && !documentoIvaLegatoDocumentoPresente) {
            // Ho un documento iva legato ad una nota: impedisco l'apertura e la selezione del documento iva legato alle quote
            // Se non e' la prima volta che chiamo la funzione, mostro anche un messaggio di errore
            if(!first) {
                impostaDatiNegliAlert(["Dati iva gi&agrave; collegati ad una quota rilevante iva: impossibile legare i dati iva all'intero documento"], Documento.alertErrori);
            }
            selectedRadio.removeAttr("checked");
            // Se avevo già impostato il checkbox delle quote, lo reimposto
            if($("#inserimentoSubdocumentoIvaQuota").is(":visible")) {
                $("input[name='tipoInserimentoDatiIva'][value='Quota']").attr("checked", true);
            }
            // Impedisco la continuazione
            return;
        }

        if(selectedRadio.val() === "Quota" && legameConIvaPresente && documentoIvaLegatoDocumentoPresente) {
            // Ho un documento iva legato al doc
            if(!first) {
                impostaDatiNegliAlert(["Dati iva gi&agrave; collegati al documento: impossibile legare i dati iva ad una quota"], Documento.alertErrori);
            }
            selectedRadio.removeAttr("checked");
            // Se avevo già impostato il checkbox delle quote, lo reimposto
            if($("#inserimentoSubdocumentoIvaDocumento").is(":visible")) {
                $("input[name='tipoInserimentoDatiIva'][value='Documento']").attr("checked", true);
            }
            // Impedisco la continuazione
            return;
        }

        if (selectedRadio.length) {
            div = divs.filter("#inserimentoSubdocumentoIva" + selectedRadio.val());
            if (!div.is(":visible")) {
                divs.slideUp();
                div.slideDown();
            }
        } else if (!selectedRadio.length) {
            divs.slideUp();
        }
    }

    // Export dei dati per il documento iva, nel caso possa essere necessario
    doc.Iva = exports;

    $(function() {
        $(d).on("click", "[name='tipoInserimentoDatiIva']", function(e) {
            // Al click sul radio del tipo di documento iva, scateno un evento differente
            var event = $.Event("inserimentoIva");
            event.originalEvent = e;
            $(this).trigger(event);
        })
            // All'azione sulla modifica del radio del documento IVA, gestisco la visualizzazione dei div
            .on("inserimentoIva", gestioneVisualizzazioneDiv)
            // All'aggiornamento delle quote, ricalcolo la tabella delle quote rilevanti iva
            .on("quoteAggiornate", ottieniQuoteIva)
            // Invoco gli eventi
            .trigger({
                type: "inserimentoIva",
                firstTime: true
            })
            .trigger("quoteAggiornate");
    });
}(jQuery, window, Documento || {}));