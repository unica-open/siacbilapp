/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($, doc) {
    var exports = {};
    var fieldsToKeep = $("*:input:not(.modal *:input)", "#tabRitenute").not("button");
    var nameNaturaEsenti = "ESENTI";

    var divsMayHideNatura = $('[data-hidden-natura]', '#fieldsetDatiOnere');
    var divsMayHideSplitReverse = $('[data-hidden-split-reverse]', '#fieldsetDatiOnere');
    var divSelectSplitReverse = $("#tipoIvaSplitReverseDiv", "#fieldsetDatiOnere");
    var codiceEsenzione = $('#hidden_codiceEsenzione').val();
    var codiceSplitReverse = $('#hidden_codiceSplitReverse').val();
    var selectNaturaOnere = $('#naturaOnereTipoOnereDettaglioOnere');

    /**
     * Carica la tabella delle ritenute a partire dalla lista fornita.
     *
     * @param lista (Array)
     */
    function caricaTabellaRitenute(lista, flagEditabilitaRitenute) {
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
                sZeroRecords: "Non sono presenti oneri associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source){
                    return source.tipoOnere && source.tipoOnere.naturaOnere && source.tipoOnere.naturaOnere.codice + " - " + source.tipoOnere.naturaOnere.descrizione || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.tipoOnere && source.tipoOnere.codice + " - " + source.tipoOnere.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.ordinativo && source.ordinativo.codice || "";
                }},
                {aTargets: [3], mData: function(source) {
                    var importo = source.importoImponibile || 0;
                    return importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], mData: function(source) {
                    var aliquota = source.tipoOnere && source.tipoOnere.aliquotaCaricoSoggetto || 0;
                    return aliquota.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [5], mData: function(source) {
                    var importo = source.importoCaricoSoggetto || 0;
                    return importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [6], mData: function(source) {
                    var aliquota = source.tipoOnere && source.tipoOnere.aliquotaCaricoEnte || 0;
                    return aliquota.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [7], mData: function(source) {
                    var importo = source.importoCaricoEnte || 0;
                    return importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: function(source) {
                    var importo = source.sommaNonSoggetta || 0;
                    return importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };

        // Se posso editare le ritenute, allora aggiungo le opzioni
        if(flagEditabilitaRitenute) {
            options.aoColumnDefs[9] = {aTargets: [9], mData: function(source) {
                if(!source.azioni) {
                    source.azioni = "<div class='btn-group'>" +
                            "<button data-toggle='dropdown' class='btn dropdown-toggle'>Azioni<span class='caret'></span></button>" +
                            "<ul class='dropdown-menu pull-right'>" +
                                "<li><a href='#' class='aggiornaRitenuta'>aggiorna</a></li>" +
                                "<li><a href='#' class='eliminaRitenuta'>elimina</a></li>" +
                            "</ul>" +
                        "</div>";
                }
                return source.azioni;
            }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                $(nTd).find(".aggiornaRitenuta")
                        .substituteHandler("click", function(e) {
                            apriModaleAggiornaRitenuta(e, oData, nTd);
                        })
                        .end()
                    .find(".eliminaRitenuta")
                        .substituteHandler("click", function(e) {
                            apriModaleEliminaRitenuta(e, iRow, oData);
                        })
                        .end()
                    .addClass("tab_Right");
            }};
        }else{
          	 options.aoColumnDefs[9] = {aTargets:[9], mData: function() {
                   return "";
               }};
        }
        options.aoColumnDefs[10] = {aTargets:[10], mData: function() {
            return "<td><i class=\"icon icon-spin icon-refresh spinner icon-2x\"></i>";
        }};
        
       

        $("#tabellaRitenute").dataTable(options);
        fieldsToKeep.keepOriginalValues();
        var campiInTesta = $("#importoCassaPensioniRitenuteDocumentoDocumento, #importoRivalsaRitenuteDocumentoDocumento, #importoIVARitenuteDocumentoDocumento");
        if(flagEditabilitaRitenute){
        	$("#pulsanteRitenute").removeClass("hide");
        	$("#divInserimentoOnere").removeClass("hide");
        	$("#modaleAggiornamentoRitenuteContainer").removeClass("hide");
        	campiInTesta.attr("readOnly", false);
        }else{
        	$("#pulsanteRitenute").addClass("hide");
        	$("#divInserimentoOnere").addClass("hide");
        	$("#modaleAggiornamentoRitenuteContainer").addClass("hide");
        	campiInTesta.attr("readOnly", true);
        }
    }

    /**
     * Apre il modale di aggiornamento della ritenuta e lega le azioni corrette.
     *
     * @param e     (Event)  l'evento scatenante
     * @param onere (Object) l'onere selezionato
     * @param td    (Node)   il nodo corrispondente al td contenente l'invocazione
     */
    function apriModaleAggiornaRitenuta(e, onere, td) {
        var modal = $("#modaleAggiornamentoRitenuteContainer");
        var oggettoPerChiamataAjax = qualify({'dettaglioOnere.uid': onere.uid});
        var spinner = $(td).closest("tr").find("i.spinner").addClass("activated");
        e.preventDefault();

        modal.empty()
            .load("aggiornamentoDocumentoSpesa_inizioAggiornaOnere.do", oggettoPerChiamataAjax, function() {
                var aliquotaSoggettoStr = $("#aliquotaCaricoSoggettoTipoOnereDettaglioOnereModale").val() || "";
                var aliquotaSoggetto = parseFloat(parseLocalNum(aliquotaSoggettoStr));
                var aliquotaEnteStr = $("#aliquotaCaricoEnteTipoOnereDettaglioOnereModale").val() || "";
                var aliquotaEnte = parseFloat(parseLocalNum(aliquotaEnteStr));

                // Lego le azioni
                $("#importoImponibileDettaglioOnereModale, #aliquotaCaricoSoggettoTipoOnereDettaglioOnereModale").on("change", function(e) {
                    e.preventDefault();
                    exports.gestioneImportiAliquote.call(this, e, "Modale");
                });

                // Controllo la modificabilita degli importi
                !isNaN(aliquotaSoggetto) && aliquotaSoggetto <= 0 && $("#importoCaricoSoggettoDettaglioOnereModale").attr("readOnly", true);
                !isNaN(aliquotaEnte) && aliquotaEnte <= 0 && $("#importoCaricoEnteDettaglioOnereModale").attr("readOnly", true);

                $("#pulsanteConfermaAggiornamentoOnere").substituteHandler("click", aggiornaRitenuta);

                modal.children("#modaleAggiornaRitenute")
                    .find('.decimale')
                        .gestioneDeiDecimali()
                        .end()
                    .find(".soloNumeri")
                        .allowedChars({numeric : true})
                        .end()
                    .modal("show");
                spinner.removeClass("activated");
            });
    }

    /**
     * Aggiorna la ritenuta.
     */
    function aggiornaRitenuta(e) {
        var oggettoPerChiamataAjax = $("#fieldsetAggiornamentoRitenutaModale").serializeObject();
        var spinner = $("#SPINNER_pulsanteConfermaAggiornamentoOnere").addClass("activated");
        e.preventDefault();

        $.postJSON("aggiornamentoDocumentoSpesa_aggiornaOnere.do", oggettoPerChiamataAjax, function(data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_ModaleAggiornamentoRitenuta"), false)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
            caricaTabellaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
            caricaImportoEsente(data.importoEsenteProposto);
            impostaNuoviImporti(data.netto, data.importoDaAttribuire);
            $("#modaleAggiornaRitenute").modal("hide");
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Caricamento dell'importo esente.
     *
     * @param importoEsente (Number) l'importo esente da impostare
     */
    function caricaImportoEsente(importoEsente) {
        var importo = importoEsente || 0;
        $("#importoEsenteRitenuteDocumentoDocumento").val(importo.formatMoney()).change();
    }

    /**
     * Apre il modale di eliminazione della ritenuta e lega le azioni corrette ai pulsanti.
     *
     * @param e    (Event)  l'evento scatenante
     * @param row  (Number) la riga selezionata
     * @param data (Object) il dato da eliminare
     */
    function apriModaleEliminaRitenuta(e, row, data) {
        var modal = $("#modaleEliminaRitenute");
        var str = data.tipoOnere.naturaOnere.codice + " - " + data.tipoOnere.codice + " - " + data.tipoOnere.descrizione;
        e.preventDefault();
        // Popolo lo span con i dati della ritenuta
        $("#SPAN_elementoSelezionatoRitenuteEliminazione").html(str);
        // Lego le azioni ai pulsanti
        $("#pulsanteNoEliminaRitenute").substituteHandler("click", function(e) {
            e.preventDefault();
            modal.modal("hide");
        });

        $("#pulsanteSiEliminaRitenute").substituteHandler("click", function(e) {
            e.preventDefault();
            eliminaRitenuta(row);
        });

        modal.modal("show");
    }

    /**
     * Elimina la ritenuta e aggiorna la tabella.
     *
     * @param row (Number) la riga selezionata
     */
    function eliminaRitenuta(row) {
        var spinner = $("#SPINNER_pulsanteSiEliminaRitenute").addClass("activated");
        var modal = $("#modaleEliminaRitenute");

        doc.alertErrori.slideUp();
        doc.alertInformazioni.slideUp();

        $.postJSON("aggiornamentoDocumentoSpesa_eliminaOnere.do", {rigaDaEliminare: row}, function(data) {
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                modal.modal("hide");
                return;
            }

            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
            // Ricarico la lista
            caricaTabellaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
            caricaImportoEsente(data.importoEsenteProposto);
            impostaNuoviImporti(data.netto, data.importoDaAttribuire);
            modal.modal("hide");
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Carica la select con le options specificate.
     *
     * @param select  (jQuery) la select da popolare
     * @param options (Array)  l'array delle options
     *
     * @return (jQuery) la select popolata
     */
    function caricaSelect(select, options) {
        select.empty();

        select.append($("<option />").val("0").text(""));
        $.each(options, function () {
            select.append(
                $("<option />")
                    .data("innerObject", this)
                    .val(this.uid)
                    .text(this.codice + ' - ' + this.descrizione)
            );
        });
        return select;
    }
    
//    /**
//     * Carica la select con le options specificate.
//     *
//     * @param select  (jQuery) la select da popolare
//     * @param options (Array)  l'array delle options
//     *
//     * @return (jQuery) la select popolata
//     */
//    function caricaSelectEnum(select, options) {
//        select.empty();
//
//        select.append($("<option />").val("").text(""));
//        $.each(options, function () {
//            select.append(
//                $("<option />")
//                    .data("innerObject", this)
//                    .val(this._name)
//                    .text(this.codice + ' - ' + this.descrizione)
//            );
//        });
//        return select;
//    }

    function impostaNuoviImporti(netto, importoDaAttribuire){
    	$("#nettoDocumento").val(netto.formatMoney()).keepOriginalValues();
    	$("#HIDDEN_oldNetto").val(netto.formatMoney()).keepOriginalValues();
    	$("#SPAN_nettoDocumento").html(netto.formatMoney());
    	$("#SPAN_importoDaAttribuireDocumento").html(importoDaAttribuire.formatMoney());
    	
    }
    /**
     * Carica la lista delle ritenute nella tabella.
     *
     * @param listaRitenute (Array) la lista delle ritenute
     */
    exports.caricaListaRitenute = function(listaRitenute, flagEditabilitaRitenute) {
        caricaTabellaRitenute(listaRitenute, flagEditabilitaRitenute);
    };

    /**
     * Apre il modale per l'inserimento di una nuova ritenuta.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.aperturaInserimentoNuovaRitenuta = function(e) {
        e.preventDefault();

        // Chiamo il servizio di pulizia e aspetto il risultato
        $.ajax({
            async: false,
            url: "aggiornamentoDocumentoSpesa_inizioInserisciOnere.do"
        })/*done(function(){})*/;

        // Sbianco la natura onere
        $("#naturaOnereTipoOnereDettaglioOnere").val("");
        
        // Ripopolo i campi
        $("label[for='aliquotaCaricoSoggettoTipoOnereDettaglioOnere']").html("Aliquota a carico soggetto");
        $("label[for='importoImponibileDettaglioOnere']").html("Imponibile *");
        $("label[for='importoCaricoSoggettoDettaglioOnere']").html("Importo a carico del soggetto");

        // svuoto e disabilito le altre select; svuoto i campi; rendo editabili gli importi
        $("#collapseInserisciNuovoOnere").find("select")
                .not("#naturaOnereTipoOnereDettaglioOnere")
                    .empty()
                    .attr("disabled", "disabled")
                    .end()
                .end()
            .find(":input")
                .val("")
                .filter("[readOnly]")
                    .removeAttr("readOnly")
                    .end()
                .end()
            .find("[data-hidden-natura], [data-hidden-split-reverse]")
                .slideDown()
                .end()
            .find('#tipoIvaSplitReverseDiv')
                .slideUp()
                .end()
            .find('[data-enabled-split-reverse]')
                .attr('disabled', true)
                .end()
            .collapse("show");
    };

    /**
     * Gestisce gli importi modificando il totale.
     */
    exports.gestioneImporti = function() {
        var totale = 0;
        $("#importoEsenteRitenuteDocumentoDocumento, #importoCassaPensioniRitenuteDocumentoDocumento, #importoRivalsaRitenuteDocumentoDocumento, #importoIVARitenuteDocumentoDocumento")
            .map(function() {
                return parseFloat(parseLocalNum($(this).val()));
            })
            .filter(function() {
                return !isNaN(this);
            })
            .each(function() {
                totale += this;
            });
        $("#totaleRitenute").html(totale.formatMoney());
    };

    /**
     * Carica la select dei tipi di onere a partire dalla select della natura onere
     */
    exports.caricaTipoOnere = function() {
        var uid = parseInt($(this).val(), 10);
        var selectTipoOnere = $("#tipoOnereDettaglioOnere").overlay("show");
        if(!uid) {
            selectTipoOnere.empty()
                .attr("disabled", "disabled")
                .overlay("hide");
            return;
        }

        $.postJSON("ajax/tipoOnereAjax.do", {id: uid}, function(data) {
            caricaSelect(selectTipoOnere, data.listaTipoOnere)
                .removeAttr("disabled");
        }).always(function() {
            selectTipoOnere.overlay("hide");
        });

    };

    /**
     * Carica l'attività dell'onere e la causale
     */
    exports.caricaAttivitaOnereCausaleESommeNonSoggette = function() {
        var self = $(this);
        var uid = parseInt(self.val(), 10);
        var selectAttivita = $("#attivitaOnereDettaglioOnere").overlay("show");
        var selectCausale = $("#causale770DettaglioOnere").overlay("show");
        var selectSomme = $("#tipoSommaNonSoggetta").overlay("show");

        var inputAliquotaCaricoEnte = $("#aliquotaCaricoEnteTipoOnereDettaglioOnere");
        var inputAliquotaCaricoSoggetto = $("#aliquotaCaricoSoggettoTipoOnereDettaglioOnere");

        var innerData;

        $("#importoCaricoSoggettoDettaglioOnere").attr("readOnly", false);
        $("#importoCaricoEnteDettaglioOnere").attr("readOnly", false);

        if(!uid) {
            selectAttivita.empty()
                .attr("disabled", "disabled")
                .overlay("hide");
            selectCausale.empty()
                .attr("disabled", "disabled")
                .overlay("hide");
            selectSomme.empty()
            	.attr("disabled", "disabled")
            	.overlay("hide");

            inputAliquotaCaricoEnte.val("");
            inputAliquotaCaricoSoggetto.val("");
            $("#importoImponibileDettaglioOnere").blur();
            return;
        }

        innerData = self.find(":selected")
            .data("innerObject");

        var aliquotaCaricoEnte = innerData.aliquotaCaricoEnte != null ? innerData.aliquotaCaricoEnte : 0;
        var aliquotaCaricoSoggetto = innerData.aliquotaCaricoSoggetto != null ? innerData.aliquotaCaricoSoggetto : 0;

        inputAliquotaCaricoEnte.val(aliquotaCaricoEnte.formatMoney());
        inputAliquotaCaricoSoggetto.val(aliquotaCaricoSoggetto.formatMoney());
        $("#quadro770TipoOnereDettaglioOnere").val(innerData.quadro770);

        var selectedOptionNatura = $("option:selected", selectNaturaOnere);
        
        // Importo non  digitabile se le aliquote sono uguali a zero e natura onere diversa da Split-reverse
        if ((aliquotaCaricoSoggetto <= 0 ) && (selectedOptionNatura.data('codice') !== codiceSplitReverse)) {
            $("#importoCaricoSoggettoDettaglioOnere").attr("readOnly", true).val("");
        }
        // Importo non  digitabile  se le aliquote sono uguali a zero
        if(aliquotaCaricoEnte <= 0){
            $("#importoCaricoEnteDettaglioOnere").attr("readOnly", true).val("");
        }

        // Effettuo il trigger dell'evento di change, si' che vengano calcolati subito gli imponibili
        $("#importoImponibileDettaglioOnere").change();

        // Effettuo le due chiamate AJAX
        $.when(
            $.postJSON("ajax/attivitaOnereAjax.do", {id: uid}),
            $.postJSON("ajax/causale770Ajax.do", {id: uid}),
            $.postJSON("ajax/sommaNonSoggettaAjax.do", {id: uid})
        ).then(function(response1, response2, response3) {
            var data1 = response1[0];
            var data2 = response2[0];
            var data3 = response3[0];

            // Se ho almeno un errore, mi blocco
            if(impostaDatiNegliAlert(data1.errori.concat(data2.errori).concat(data3.errori), doc.alertErrori, false, false)) {
                selectAttivita.overlay("hide");
                selectCausale.overlay("hide");
                selectSomme.overlay("hide");
                return;
            }

            caricaSelect(selectAttivita, data1.listaAttivitaOnere)
                .removeAttr("disabled")
                .overlay("hide");
            caricaSelect(selectCausale, data2.listaCausale770)
                .removeAttr("disabled")
                .overlay("hide");
            caricaSelect(selectSomme, data3.codiciSommaNonSoggetta)
	            .removeAttr("disabled")
	            .overlay("hide");
        });
    };

    /**
     * Gestione degli importi a carico di ente e soggetto sulla base delle aliquote.
     *
     * @param e         (Event)  l'evento scatenante
     * @param qualifica (String) la qualifica per i campi (Optional - default: "")
     */
    exports.gestioneImportiAliquote = function(e, qualifica) {
        // Per comodità
        var obj = {};
        var importo;
        qualifica = qualifica || "";
        importo = parseFloat(parseLocalNum($("#importoImponibileDettaglioOnere" + qualifica).val() || ""));

        obj.aliquotaEnte = parseFloat(parseLocalNum($("#aliquotaCaricoEnteTipoOnereDettaglioOnere" + qualifica).val() || ""));
        obj.aliquotaSoggetto = parseFloat(parseLocalNum($("#aliquotaCaricoSoggettoTipoOnereDettaglioOnere" + qualifica).val() || ""));

        obj.campoImportoEnte = $("#importoCaricoEnteDettaglioOnere" + qualifica);
        obj.campoImportoSoggetto = $("#importoCaricoSoggettoDettaglioOnere" + qualifica);

        // Se le aliquote sono NaN, non posso fare i calcoli
        if(isNaN(importo)) {
            return;
        }

        $.each(["Ente", "Soggetto"], function() {
            var importoDaImpostare;
            if(!isNaN(obj["aliquota" + this])) {
                importoDaImpostare = importo * obj["aliquota" + this] / 100;
                obj["campoImporto" + this].val(importoDaImpostare.formatMoney());
            }
        });
    };

    /**
     * Salva la testata del form delle ritenute.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.salvaTestaFormRitenute = function(e) {
        var oggettoPerChiamataAjax = $("#fieldsetTesta").serializeObject();
        var spinner = $("#SPINNER_pulsanteSalvaRitenute").addClass("activated");
        e.preventDefault();

        doc.alertErrori.slideUp();
        doc.alertInformazioni.slideUp();

        $.postJSON("aggiornamentoDocumentoSpesa_salvaRitenute.do", oggettoPerChiamataAjax, function(data) {
            impostaDatiNegliAlert(data.errori, doc.alertErrori);
            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);
        }).always(function(){
            spinner.removeClass("activated");
        });

        fieldsToKeep.keepOriginalValues();
    };

    /**
     * Gestione del pulsante di reset
     */
    exports.gestioneReset = function() {
        var inputFieldsetTesta = $("#fieldsetTesta").find("input")
            .each(function() {
                var self = $(this);
                self.data("fieldsetInitialValue", self.val());
            });

        $("#pulsanteResetRitenute").on("click", function(e) {
            e.preventDefault();
            // Ripristino i valori iniziali
            inputFieldsetTesta.each(function() {
                var self = $(this);
                self.val(self.data("fieldsetInitialValue"));
            });
        });
    };

    /**
     * Inserisce il nuovo onere.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.inserimentoNuovoOnere = function(e) {
        var oggettoPerChiamataAjax = $("#fieldsetDatiOnere").serializeObject();
        var spinner = $("#SPINNER_pulsanteInserisciOnere");
        e.preventDefault();

        spinner.addClass("activated");

        doc.alertErrori.slideUp();
        doc.alertInformazioni.slideUp();

        $.postJSON("aggiornamentoDocumentoSpesa_inserisciOnere.do", oggettoPerChiamataAjax, function(data) {
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                return;
            }

            impostaDatiNegliAlert(data.informazioni, doc.alertInformazioni);

            // Ricarico la lista delle ritenute
            caricaTabellaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
            caricaImportoEsente(data.importoEsenteProposto);
            impostaNuoviImporti(data.netto, data.importoDaAttribuire);
            // Chiudo il collapse
            $("#collapseInserisciNuovoOnere").collapse("hide");
        }).always(function() {
            spinner.removeClass("activated");
        });
    };

    // Lotto L
    /**
     * Caricamento dei dati della fattura. Il caricamento viene effettuato una sola volta
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    exports.caricaDatiFatture = function(e) {
        var pulsante = $("#datiFatturePulsante").overlay("show");
        e.preventDefault();

        $.postJSON('aggiornamentoDocumentoSpesa_caricaFatturaFEL.do', {}, function(data) {
            var totaleImponibile;
            var totaleImposta;
            var totaleEsente;
            if(impostaDatiNegliAlert(data.errori, doc.alertErrori)) {
                // Ho errori: esco
                return;
            }
            // Carico la tabella dei dati della fattura
            populateTableFattura(data.fatturaFEL && data.fatturaFEL.riepiloghiBeni || []);

            // Imposto i totali
            totaleImponibile = data.totaleImponibileFEL || 0;
            totaleImposta = data.totaleImpostaFEL || 0;
            totaleEsente = data.totaleEsenteFEL || 0;
            $("th[data-totale-imponibile]", "#tabellaDatiFatture").html(totaleImponibile.formatMoney());
            $("th[data-totale-imposta]", "#tabellaDatiFatture").html(totaleImposta.formatMoney());
            $("th[data-totale-esente]", "#tabellaDatiFatture").html(totaleEsente.formatMoney());

            // Lego l'apertura normale dell'accordion
            pulsante.substituteHandler("click", function(e) {
                e.preventDefault();
                $("#datiFattureAccordion").collapse("toggle");
            }).click();
        }).always(function() {
            pulsante.overlay("hide");
        });
    };

    /**
     * Popolamento della tabella delle fatture.
     * 
     * @param list (Array) la lista tramite cui caricare la tabella
     */
    function populateTableFattura(list) {
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
            aaData: list,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti riepiloghi beni associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source){
                    return source.progressivo || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.naturaFEL && source.naturaFEL.codice || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.aliquotaIvaNotNull.formatMoney();
                }, fnCreatedCell: addClassTabRight},
                {aTargets: [3], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? "" : source.imponibileImportoNotNull.formatMoney();
                }, fnCreatedCell: addClassTabRight},
                {aTargets: [4], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? "" : source.impostaNotNull.formatMoney();
                }, fnCreatedCell: addClassTabRight},
                {aTargets: [5], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? source.imponibileImportoNotNull.formatMoney() : "";
                }, fnCreatedCell: addClassTabRight}
            ]
        };
        $("#tabellaDatiFatture").dataTable(opts);
    }

    /**
     * Controlla se la natura FEL sia 'ESENZIONE'.
     *
     * @param naturaFEL (Object) la natura da controllare
     * @returns (Boolean) true se la natura e' ?ESENZIONE', false altrimenti
     */
    function isNaturaFELEsente(naturaFEL) {
        return naturaFEL && naturaFEL._name === nameNaturaEsenti;
    }

    /**
     * Aggiunge la classe 'tab_Right' al nodo.
     * 
     * @param nTd (Node) il nodo 
     */
    function addClassTabRight(nTd) {
        $(nTd).addClass("tab_Right");
    }

    /**
     * Effettua il cambio dei dati della natura, controllando quali ragonamenti debba effettuare lo split/reverse
     */
    exports.cambioNaturaSplitReverse = function() {
        var selectedOption = $('option:selected', selectNaturaOnere);
        var codice;
        var divsToHide;
        if(!selectedOption.length || !selectedOption.val()) {
            // Nulla da effettuare: esco
            return;
        }

        codice = selectedOption.data('codice');
        divsToHide = divsMayHideNatura.filter('[data-hidden-natura~="' + codice + '"]');
        divsMayHideSplitReverse.slideDown();
        showDivs(divsMayHideNatura.not(divsToHide));
        hideDivs(divsToHide);
        $("label[for='importoImponibileDettaglioOnere']").html(codice === codiceEsenzione ? "Importo *" : "Imponibile *");
        $("label[for='importoCaricoSoggettoDettaglioOnere']").html(codice === codiceSplitReverse ? "Imposta" : "Importo a carico del soggetto");
        $("input[data-enabled-split-reverse]").attr("disabled", true);

        // Gestione della select
        if(codice === codiceSplitReverse) {
            divSelectSplitReverse.slideDown();
            $("#tipoIvaSplitReverse").val("");
            return;
        }
        divSelectSplitReverse.slideUp();
        $("label[for='aliquotaCaricoSoggettoTipoOnereDettaglioOnere']").html("Aliquota a carico soggetto");
        $('#aliquotaCaricoEnteTipoOnereDettaglioOnere').attr('disabled', true);
    };

    /**
     * Effettua il cambio dello split/reverse con la gestione dei campi
     */
    exports.cambioSplitReverse = function() {
        var selectedOption = $('option:selected', '#tipoOnereDettaglioOnere');
        var selectedOptionNatura = $('option:selected', selectNaturaOnere);
        var innerData;
        var codice;
        var divsToHide;
        var inputsToEnable;
        var inputsDataEnabled;
        if(!selectedOption.length || !selectedOption.val() || !selectedOptionNatura.length || selectedOptionNatura.data('codice') !== codiceSplitReverse) {
            // Nulla da effettuare: esco
            return;
        }
        innerData = selectedOption.data("innerObject");
        if(!innerData || !innerData.tipoIvaSplitReverse) {
            showDivs(divsMayHideSplitReverse);
            $("#tipoIvaSplitReverse").val("");
            $("input[data-enabled-split-reverse]").attr("disabled", true);
            $("label[for='aliquotaCaricoSoggettoTipoOnereDettaglioOnere']").html("Aliquota a carico soggetto");
            // Nessun dato caricato
            return;
        }

        codice = innerData.tipoIvaSplitReverse.codice;
        divsToHide = divsMayHideSplitReverse.filter('[data-hidden-split-reverse~="' + codice + '"]');
        inputsDataEnabled = $("input[data-enabled-split-reverse]");
        inputsToEnable = inputsDataEnabled.filter('[data-enabled-split-reverse~="' + codice + '"]');

        $("label[for='aliquotaCaricoSoggettoTipoOnereDettaglioOnere']").html(codice === 'SI' || codice === 'SC' ? "Aliquota" : "Aliquota a carico soggetto");

        // data-enabled-split-reverse
        showDivs(divsMayHideSplitReverse.not(divsToHide));
        hideDivs(divsToHide);
        // Enable fields
        inputsDataEnabled.not(inputsToEnable).attr('disabled', true);
        inputsToEnable.removeAttr('disabled');
        $("#tipoIvaSplitReverse").val(innerData.tipoIvaSplitReverse.codice + ' - ' + innerData.tipoIvaSplitReverse.descrizione);
        $('#aliquotaCaricoEnteTipoOnereDettaglioOnere').attr('disabled', true);
    };

    /**
     * Mostra i div forniti.
     *
     * @param divs (jQuery) i div da mostrare
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function showDivs(divs) {
        return applyOnDivs(divs, 'slideDown', 'removeAttr');
    }

    /**
     * Nasconde i div forniti.
     *
     * @param divs (jQuery) i div da nascondere
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function hideDivs(divs) {
        return applyOnDivs(divs, 'slideUp', 'attr');
    }

    /**
     * Applica le funzioni fornite ai div.
     *
     * @param divs     (jQuery) i div su cui applicare le funzioni
     * @param slideFnc (String) il nome della funzione di slide da applicare (slideUp / slideDown)
     * @param attrFnc  (String) il nome della funzione di attributi da applicare (attr / removeAttr)
     *
     * @returns (JQuery) i div su cui e' stata effettuata l'invocazione
     */
    function applyOnDivs(divs, slideFnc, attrFnc) {
        return divs[slideFnc]()
            .filter(':input')[attrFnc]('disabled', true).end()
            .find(':input')[attrFnc]('disabled', true).end().end()
            .find('.chosen-select').trigger('chosen:updated').end();
    }

    exports.fieldsToKeep = fieldsToKeep;

    doc.Ritenute = exports;
    return doc;
}(jQuery, Documento || {}));

$(function() {
    var campiInTesta = $("#importoEsenteRitenuteDocumentoDocumento, #importoCassaPensioniRitenuteDocumentoDocumento, #importoRivalsaRitenuteDocumentoDocumento, #importoIVARitenuteDocumentoDocumento");
    Documento.Ritenute.gestioneReset();
    
    // Gestione dei pulsanti
    $("#pulsanteInserisciNuovoOnere").on("click", Documento.Ritenute.aperturaInserimentoNuovaRitenuta);
    $("#pulsanteSalvaRitenute").on("click", Documento.Ritenute.salvaTestaFormRitenute);
    $("#pulsanteInserisciOnere").on("click", Documento.Ritenute.inserimentoNuovoOnere);
    $("#datiFatturePulsante").one("click", Documento.Ritenute.caricaDatiFatture);

    // Gestione campi in testa
    campiInTesta.on("change", Documento.Ritenute.gestioneImporti)
        .change();

    // Gestione dei select
    $("#naturaOnereTipoOnereDettaglioOnere").on("change", function() {
        $.proxy(Documento.Ritenute.caricaTipoOnere, this)();
        Documento.Ritenute.cambioNaturaSplitReverse();
    });
    $("#tipoOnereDettaglioOnere").on("change", function() {
        $.proxy(Documento.Ritenute.caricaAttivitaOnereCausaleESommeNonSoggette, this)();
        Documento.Ritenute.cambioSplitReverse();
    });

    $("#importoImponibileDettaglioOnere, #aliquotaCaricoSoggettoTipoOnereDettaglioOnere").on("change", Documento.Ritenute.gestioneImportiAliquote);

    $.postJSON("aggiornamentoDocumentoSpesa_ottieniListaRitenute.do", function(data) {
        Documento.Ritenute.caricaListaRitenute(data.listaDettaglioOnere, data.flagEditabilitaRitenute);
    });

    Documento.Ritenute.fieldsToKeep.keepOriginalValues();
});