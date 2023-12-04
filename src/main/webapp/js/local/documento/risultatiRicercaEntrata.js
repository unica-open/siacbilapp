/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function () {
	var baseOpts = {
        bServerSide: false,
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
            oPaginate : {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }};
    var collapseOrdine = $("#divOrdine_modaleDettaglioOrdini");
    var tabellaOrdini = $("#tabella_modaleDettaglioOrdini");
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var dataTable;
    
    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Gestione del click sul pulsante attiva registrazioni contabili.
     *
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param td  (Node)   la cella su cui e' stato scatenato l'evento
     * @param doc (Object) l'oggetto contenuto nella tabella
     */
    function clickOnAttivaRegContabili(e, td, doc) {
        e.preventDefault();
        bootbox.dialog(
            // SIAC-4504
            'Si stanno per attivare le registrazioni GEN e PCC sul documento ' + doc.documento + '. Proseguire?',
         [
             {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
             {"label" : "Si, prosegui", "class" : "btn btn-primary", "callback": effettuaAttivazioneGENPCC.bind(undefined, td, doc.uid)}
         ],
         {
             animate: true, 
             classes: 'dialogWarn', 
             header: 'Attenzione', 
             backdrop: 'static'
        });
    }

    //SIAC-7557
    /**
     * Gestione del click sul pulsante gestione Ordini.
     *
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param td  (Node)   la cella su cui e' stato scatenato l'evento
     * @param uid (Object) uid dell'oggetto contenuto nella tabella
     */
    function clickOnGestioneOrdini(e, td, uid) {
        var overlay = $(td).closest("tr").overlay("show");
        $("#HIDDEN_uidDocumento_modaleDettaglioOrdini").val(uid);

        caricaTabellaOrdini(uid, alertErrori)
        .then(function() {
            // Nascondo il collapse
            collapseOrdine.slideUp();
            // Apro il modale
            $("#modaleDettaglioOrdini").modal("show");
        }).always(function() {
            overlay.overlay("hide");
        });
    }
    
    //SIAC-7557
    /**
     * Caricamento della tabella degli ordini.
     *
     * @param uid        (Number) l'uid da ricercare
     * @param alertToUse (jQuery) l'alert da popolare con gli errori
     * @returns (Promise) la promise di esecuzione della ricerca
     */
    function caricaTabellaOrdini(uid, alertToUse) {
        return $.postJSON("risultatiRicercaDocumentoEntrata_ottieniListaOrdine.do", {uidDaConsultare: uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertToUse)) {
                return $.Deferred().reject(data.errori).promise();
            }
            popolaTabellaOrdini(data.listaOrdine);
            return data;
        });
    }
    
    /**
     * Popolamento della tabella degli ordini.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function popolaTabellaOrdini(list) {
        var options = {
            aaData: list,
            bAutoWidth: false,
            oLanguage: {
                sZeroRecords: "Non sono presenti ordini associati"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "numeroOrdine"},
                {aTargets: [1], mData: function(source) {
                        return "<div class=\"btn-group\"><button href=\"#\" data-toggle=\"dropdown\" data-placement=\"left\" class=\"btn dropdown-toggle\">Azioni <span class=\"caret\"></span></button>"
                            + "<ul class=\"dropdown-menu pull-right\">"
                                + "<li><a href=\"#\" class=\"aggiornaOrdine\">aggiorna</a></li>"
                                + "<li><a href=\"#\" class=\"eliminaOrdine\">elimina</a></li>"
                            + "</ul></div>";
                    }, fnCreatedCell: function(nTd, sData, oData) {
                            $(nTd).data("originalOrdine", oData)
                                .find("a.aggiornaOrdine")
                                    .substituteHandler("click", function(e) {
                                        caricaDatiAggiornamentoOrdine(e, oData);
                                    })
                                    .end()
                                .find("a.eliminaOrdine")
                                    .substituteHandler("click", function(e) {
                                        eliminaOrdine(e, oData, nTd);
                                    });
                    }
                }
            ]};
        var opts = $.extend(true, {}, baseOpts, options);

        // Distruggo la tabella se gia' presente
        if($.fn.DataTable.fnIsDataTable(tabellaOrdini[0])) {
            tabellaOrdini.dataTable().fnDestroy();
        }
        tabellaOrdini.dataTable(opts);
    }
    
    /**
     * Caricamento dei dati per l'aggiornamento dell'ordine.
     *
     * @param e      (Event)  l'evento scatenante l'invocazione
     * @param ordine (Object) l'onere da aggiornare
     */
    function caricaDatiAggiornamentoOrdine(e, ordine) {
        $("#uidDocumentoOrdine_modaleDettaglioOrdini").val(ordine && ordine.documento && ordine.documento.uid || 0);
        $("#uidOrdine_modaleDettaglioOrdini").val(ordine && ordine.uid || 0);
        $("#numeroOrdineOrdine_modaleDettaglioOrdini").val(ordine && ordine.numeroOrdine || "")
            .data("initialNumero", ordine && ordine.numeroOrdine || "");

        $("#buttonSalva_modaleDettaglioOrdini").data("op", "aggiorna");

        collapseOrdine.slideDown();
    }
    
    /**
     * Pulisce l'ordine per l'invio a Struts.
     *
     * @param ordine (Object) l'ordine da ripulire
     * @returns (Object) l'ordine ripulito
     */
    function pruneOrdine(ordine){
        var obj = {};
        cloneProperties(ordine, obj, ['uid', 'documento.uid']);
        return obj;
    }
    
    /**
     * Elimina un ordine ordine.
     *
     * @param e      (Event)  l'evento scatenante l'invocazione
     * @param ordine (Object) l'ordine da eliminare
     */
    function eliminaOrdine(e, ordine, nTd) {
        var obj = qualify(pruneOrdine(ordine), "ordine");
        var overlayRow = $(nTd).closest('tr').overlay("show").data("overlay");
        var overlay;

        $.postJSON("risultatiRicercaDocumentoEntrata_eliminaOrdine.do", obj)
        .then(function(data) {
            // Se ho errori esco
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_ORDINI"))) {
                return;
            }

            overlay = $("#tabella_modaleDettaglioOrdini_wrapper").overlay("show").data("overlay");
            // Imposto l'informazione di successo
            impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI_MODALE_ORDINI"));
        }).always(function() {
            overlayRow && overlayRow.hide().promise();
        }).then(function() {
            return caricaTabellaOrdini($("#HIDDEN_uidDocumento_modaleDettaglioOrdini").val(), $("#ERRORI_MODALE_ORDINI"));
        }).always(function() {
            return overlay && overlay.hide().promise();
        }).then(function() {
            return overlay && overlay.remove();
        });
    }
    
    /**
     * Effettua la chiamata per l'attivazione della registrazione GEN/PCC.
     *
     * @param td  (Node)   la cella su cui e' stato scatenato l'evento
     * @param uid (Number) l'uid del documento
     */
    function effettuaAttivazioneGENPCC(td, uid) {
        var tr = $(td).closest('tr').overlay('show');

        // SIAC-6768
        var tabella = $('#risultatiRicercaDocumento').DataTable();

        $.postJSON("risultatiRicercaDocumentoEntrata_attivaRegistrazioniContabili.do", {uidDaAggiornare: uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // E' andato a buon fine: carico l'informazione di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico la tabella
            tabella && tabella.fnDraw();
        }).always(function() {
            tr.overlay("hide");
        });
    }

    /**
     * Permette la visualizzazione del dettaglio quote.
     *
     * @param td  (Node)   il td della tabella
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param obj (Object) l'oggetto contenuto nella tabella
     */
    function dettaglioQuote(td, e, uid) {
        var tr = $(td).closest('tr').overlay('show');
        e.preventDefault();
        $.postJSON("risultatiRicercaDocumentoEntrataDettaglioQuote.do", {uidDaConsultare : uid})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_QUOTA"))) {
                return;
            }
            popolaTabellaQuote();
            // SPAN dei totali
            $("#totaleQuote").html(data.totaleQuote.formatMoney());
            $("#modaleQuotaEntrata").modal("show");
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Caricamento via Ajax della tabella delle quote
     */
    function popolaTabellaQuote() {
        var isOverlay = false;
        var table = $("#tabellaDettaglioQuoteDocumentoEntrata").overlay({rebind: true, loader: true, usePosition: true});
        var opts = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            bServerSide: true,
            sAjaxSource: "risultatiRicercaSinteticaQuoteDocumentoEntrataAjax.do",
            sServerMethod: "POST",
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti quote associate",
                oPaginate : {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: defaultPerDataTable('movimento')},
                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
                {aTargets: [3], mData: defaultPerDataTable('ordinativo')},
                {aTargets: [4], mData: defaultPerDataTable('dataEmissione')},
                {aTargets: [5], mData: defaultPerDataTable('provvisorio')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }}
            ]
        };

        dataTable = table.dataTable(opts);
    }
    
    /**
     * Annullamento dell'inserimento/aggiornamento dell'ordine.
     */
    function annullaInserimentoOrdine() {
        $(":input", "#fieldset_modaleDettaglioOrdini").val("");
        collapseOrdine.slideUp();
    }

    /**
     * Salvataggio dell'onere.
     */
    function salvaInserimentoOrdine() {
        var obj = $("#fieldset_modaleDettaglioOrdini").serializeObject();
        var op = $("#buttonSalva_modaleDettaglioOrdini").data("op");
        var overlay;
        obj = unqualify(obj, 1);

        collapseOrdine.overlay("show");
        $.postJSON("risultatiRicercaDocumentoEntrata_" + op + "Ordine.do", obj)
        .then(function(data) {
            // Se ho errori esco
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_MODALE_ORDINI"))) {
                return;
            }
            // Imposto l'informazione di successo
            impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI_MODALE_ORDINI"));

            // Comunico il ricalcolo della tabella
            overlay = $("#tabella_modaleDettaglioOrdini_wrapper").overlay("show").data("overlay");
        }).then(function() {
            collapseOrdine.slideUp();
        }).always(function() {
            collapseOrdine.overlay("hide");
        }).then(function() {
            // Ricalcolo la tabella
            return caricaTabellaOrdini($("#HIDDEN_uidDocumento_modaleDettaglioOrdini").val(), $("#ERRORI_MODALE_ORDINI"));
        }).always(function() {
            return overlay && overlay.hide().promise();
        }).then(function() {
            return overlay && overlay.remove();
        });
    }

    /**
     * nuovo inserimento ordine.
     */
    function nuovoInserimentoOrdine() {
        var uidDocumentoOrdine = $("#HIDDEN_uidDocumento_modaleDettaglioOrdini").val();
        $(":input", "#fieldset_modaleDettaglioOrdini").val("");
        $("#buttonSalva_modaleDettaglioOrdini").data("op", "inserisce");

        $("#uidDocumentoOrdine_modaleDettaglioOrdini").val(uidDocumentoOrdine);
        collapseOrdine.slideDown();
    }
    
    /**
     * Valuta se il modale degli ordini possa essere chiuso
     */
    function valutaChiusuraModaleOrdini() {
        var collapse = $("#divOrdine_modaleDettaglioOrdini");
        var inputNumero = $("#numeroOrdineOrdine_modaleDettaglioOrdini");
        if(collapse.is(":visible") && inputNumero.val() !== inputNumero.data("initialNumero")) {
            return bootbox.dialog(
                "Il numero dell'ordine e' stato aggiornato ma non persistito: il dato verr&agrave; perso. Continuare?",
                [
                    {"label" : "No, indietro", "class" : "btn", "callback": $.noop},
                    {"label" : "Si, prosegui", "class" : "btn", "callback": $.proxy(chiudiModale, null, "#modaleDettaglioOrdini")}
                ],
                {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'}
            );
        }
        chiudiModale("#modaleDettaglioOrdini");
    }
    
    /**
     * Chiusura del modale degli ordini.
     *
     * @param id (String) l'ide del modale da chiudere
     */
    function chiudiModale(id) {
        $(id).modal("hide");
    }

    /**
     * Caricamento via Ajax della tabella dei documenti e visualizzazione.
     */
    function visualizzaTabellaDocumenti() {
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaDocumentoEntrataAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            // SIAC-6768 bDestroy: true
            bDestroy: true,
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
            fnServerData: function (sSource, aoData, fnCallback, oSettings) {
            	oSettings.jqXHR = $.ajax({
            		dataType: 'json',
            		type: 'POST',
            		url: sSource,
            		data: aoData,
            		success: function(data) {
            			if(data.moreData.importoTotale !== undefined) {
            				$('#importoTotale').html(data.moreData.importoTotale.formatMoney());
            			}
            			return fnCallback.apply(this, arguments);
            		}
            	});
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable("documento")},
                {aTargets: [1], mData: defaultPerDataTable("data")},
                {aTargets: [2], mData: function(source) {
                    return source.statoOperativoDocumentoCode + " / "+ source.statoOperativoDocumentoDesc;
                }},
                {aTargets: [3], mData: "decodeStatoSDI",
                	mRender : function(data, type) {
	            		if(type === 'display') {
                            var val = data.split("|");

                            //SIAC-7158
                            if(val[1] === "null"){
                                return '<div>'+val[0]+'</div>';
                            }
                            
                            val[1] = val[1].replace(/["]/g, "'");
                            return '<div title=\"'+val[1]+'\">'+val[0]+'</div>';
	            		}
	            		return data;
                	}
                },
                {aTargets: [4], mData: defaultPerDataTable("soggetto")},
                {aTargets: [5], mData: defaultPerDataTable("contabilizzato")},
                {aTargets: [6], mData: function(source) {
                    return source.importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [7], mData: "azioni", fnCreatedCell: function (nTd, sData, oData) {
                    $(nTd).find("a[href='#msgAnnulla']")
                            .substituteHandler("click", function() {
                                clickOnAnnulla(oData.uid);
                            })
                            .end()
                        .find("a.attivaRegistrazioniContabili")
                            .substituteHandler("click", function(e) {
                                clickOnAttivaRegContabili(e, nTd, oData);
                            })
                            //SIAC-7947 aggiuno la funzione end() per non creare conflitti tra i vari handler
                            .end()
                        //SIAC-7557
                        .find("a.gestioneOrdini")
                            .substituteHandler("click", function(e) {
                                clickOnGestioneOrdini(e, nTd, oData.uid);
                            })
                            //SIAC-7947 aggiuno la funzione end() per non creare conflitti tra i vari handler
                            .end()
                        .find("a.dettaglioQuoteDocumento")
                            .substituteHandler("click", function(e) {
                                dettaglioQuote(nTd, e, oData.uid);
                            });
                    $('.dropdown-toggle', nTd).dropdown();
                }}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }

        $("#risultatiRicercaDocumento").dataTable(options);
    }

    $(function() {
        // Carico la dataTable
        visualizzaTabellaDocumenti();
        
     // Lego le azioni ai pulsanti degli ordini
        $("#buttonAnnulla_modaleDettaglioOrdini").substituteHandler("click", annullaInserimentoOrdine);
        $("#buttonSalva_modaleDettaglioOrdini").substituteHandler("click", salvaInserimentoOrdine);
        $("#buttonNuovo_modaleDettaglioOrdini").substituteHandler("click", nuovoInserimentoOrdine);

        $("#pulsanteChiusura_modaleDettaglioOrdini").substituteHandler("click", valutaChiusuraModaleOrdini);
    });
}();

