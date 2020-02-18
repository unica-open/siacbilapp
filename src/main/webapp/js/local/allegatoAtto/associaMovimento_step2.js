/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";

    // Campi di default
    var pulsanteAggiungiMovimento = $("#pulsanteAggiungiMovimento");
    var pulsanteCompilazioneGuidata = $("#pulsanteCompilazioneGuidataMovimentoGestione");
    var pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento = $('#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento');
    var radioTipoMovimento = $("input[name='tipoMovimento']");
    var pulsanteConferma = $("#buttonConfermaAggiungiMovimento");
    var collapse = $("#collapseAggiungiMovimento");
    var annoMovimento = $("#annoMovimento");
    var numeroMovimento = $("#numeroMovimentoGestione");
    var numeroSubmovimento = $("#numeroSubmovimentoGestione");
    var divCigCup = $("div[data-cig-cup]");
    var assenzaCig = $('*[data-assenza-cig]');
    var spanMutuo = $("#spanMutuo");
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var isGestioneUEB = $("#HIDDEN_gestioneUEB").val() === "true";
    var pulsanteSiEliminazione = $("#pulsanteSiModaleEliminazione");
    var modaleEliminazione = $("#modaleEliminazione");
    var spanElementoEliminazione = $("#spanElementoSelezionatoModaleEliminazione");
    var spinnerEliminazione = $("#pulsanteSiModaleEliminazione");

    // Funzioni su impegno e accertamento (per evitare l'eval)
    var functions = {
        "Impegno": {
            apriModale: apriModaleImpegno,
            conferma: confermaImpegno,
            hasCigCupMutuo: true,
            gestisciCigCup: "slideDown",
            gestisciMutuo: "removeClass",
            campoDisponibilita: "disponibilitaLiquidare",
            campoModel: "subdocumentoSpesa",
            urlInserimento: 'associaMovimentoAllegatoAtto_addImpegno.do',
            listaSub: 'elencoSubImpegni'
        },
        "Accertamento": {
            apriModale: apriModaleAccertamento,
            conferma: confermaAccertamento,
            hasCigCupMutuo: false,
            gestisciCigCup: "slideUp",
            gestisciMutuo: "addClass",
            campoDisponibilita: "disponibilitaIncassare",
            campoModel: "subdocumentoEntrata",
            urlInserimento: "associaMovimentoAllegatoAtto_addAccertamento.do",
            listaSub: 'elencoSubAccertamenti'
        }

    };
    // Opzioni base per il dataTable
    var baseOpts = {
        bServerSide: false,
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
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    /**
     * Apre il modale dell'impegno.
     */
    function apriModaleImpegno() {
        // Copio i dati
        $("#annoImpegnoModale").val(annoMovimento.val());
        $("#numeroImpegnoModale").val(numeroMovimento.val());
        $("#modaleImpegno").modal("show");

        // SIAC-5409
        $('#divImpegniTrovati').slideUp();
        $('#divMutui').slideUp();
    }

    /**
     * Apre il modale dell'accertamento.
     */
    function apriModaleAccertamento() {
        $("#annoAccertamentoModale").val(annoMovimento.val());
        $("#numeroAccertamentoModale").val(numeroMovimento.val());
        $("#modaleAccertamento").modal("show");
        // SIAC-5409
        $('#divAccertamentiTrovati').slideUp();
    }
    
    
    function computeEtichettaSpesa(subdoc){
    	 var result = "";
         if(subdoc.impegno) {
             result = "S-" + subdoc.impegno.annoMovimento + "/" + subdoc.impegno.numero;
             if(subdoc.subImpegno && subdoc.subImpegno.numero) {
                 result += "-" + subdoc.subImpegno.numero;
             }
         }
         return result;
    }


    /**
     * Conferma la selezione dell'impegno.
     */
    function confermaImpegno(data) {
        // Segnalo il messaggio di successo
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        // Ricarico la tabella con i dati fornitimi
        impostaTabellaSpesa(data.listaSubdocumentoSpesa);
        // Reimposto il totale
        $("#spanTotaleImpegni").html(data.totaleSpesa.formatMoney());
        pulsanteAggiungiMovimento.click();
    }

    /**
     * Conferma la selezione dell'accertamento.
     */
    function confermaAccertamento(data) {
        // Segnalo il messaggio di successo
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        // Ricarico la tabella con i dati fornitimi
        impostaTabellaEntrata(data.listaSubdocumentoEntrata);
        // Reimposto il totale
        $("#spanTotaleAccertamenti").html(data.totaleEntrata.formatMoney());
        pulsanteAggiungiMovimento.click();
    }

    /**
     * Apre o chiude il collapse del nuovo movimento.
     */
    function toggleNuovoMovimento() {
        collapse.is(".in") ? closeNuovoMovimento(pulsanteAggiungiMovimento, collapse) : openNuovoMovimento(pulsanteAggiungiMovimento, collapse);
    }

    /**
     * Chiude il collapse del nuovo movimento.
     *
     * @param button   (jQuery) il bottone da modificare
     * @param collapse (jQuery) il collapse da chiudere
     */
    function closeNuovoMovimento(button, collapse) {
        // Modifico la scritta nel pulsante
        button.html("aggiungi movimento");
        // Mostro il pulsante della compilazione guidata come disabilitato
        pulsanteCompilazioneGuidata.addClass("disabled")
            .off("click");
        pulsanteConferma.addClass("disabled")
            .off("click");
        $("#divDisponibilitaMovimentoGestione").addClass("hide");
        spanMutuo.addClass("hide");
        divCigCup.slideUp();
        assenzaCig.slideUp();
        collapse.collapse("hide")
        // Pulizia del collapse
            .find(":input")
                .removeAttr("readOnly")
                .removeAttr("disabled")
                .prop("checked", false)
                .filter(":not(input[type='radio'])")
                    .val("");
        $('#movimentoDati').html('');
        //lo disabilito
        $('#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento').attr("disabled","disabled");

    }

    /**
     * Apre il collapse del nuovo movimento.
     *
     * @param button   (jQuery) il bottone da modificare
     * @param collapse (jQuery) il collapse da aprire
     */
    function openNuovoMovimento(button, collapse) {
        // Modifico la scritta nel pulsante
        button.html("chiudi");
        $('#movimentoDati').html('');
        collapse.collapse("show")
        	.find('input[type="radio"][name="tipoMovimento"]:checked')
        		.change();
    }

    /**
     * Imposta il tipo di movimento selezionato dall'utente.
     */
    function impostaTipoMovimento() {
        var tipoMovimento = $("input[name='tipoMovimento']:checked").val();
        pulsanteCompilazioneGuidata.removeClass("disabled")
            .substituteHandler("click", functions[tipoMovimento].apriModale);
        pulsanteConferma.removeClass("disabled")
            .substituteHandler("click", function() {
                aggiungiMovimentoGestione(functions[tipoMovimento].conferma);
            });
        //inizializzo provvisorio in base al tipo
        inizializzaProvvisorioDiCassa(tipoMovimento);
        pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento.removeAttr("disabled").removeClass("disabled");
        // Mostro/nascondo il div dei CIG e CUP
        divCigCup[functions[tipoMovimento].gestisciCigCup]();
        assenzaCig.slideUp();
        spanMutuo[functions[tipoMovimento].gestisciMutuo]("hide");
    }

    /**
     * IN BASE al tipo movimento se spesa o entrata inizializzo il provvisorio
     */
    function  inizializzaProvvisorioDiCassa(tipoMovimento) {
    	var tipoForModale = tipoMovimento === "Impegno" ? "S" : "E";
        $("#modale_hidden_tipoProvvisorioDiCassa").val(tipoForModale);
        ProvvisorioDiCassa.inizializzazione("#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento", "", "#annoProvvisorioCassaSubdocumento", "#numeroProvvisorioCassaSubdocumento","#causaleProvvisorioCassaSubdocumento");
    }
    
    function gestisciSubMovimenti(){
        // valutare se mettere
        // obj['sub' + lowercaseTipoMovimento] = {};
        
        var found = false;
        var subs;
        var i;
        var disponibilita;
        if(numeroSubmovimentoValue) {
            // Se ho il valore del numero submovimento, allora ne prendo la disponibilita
            subs = data[lowercaseTipoMovimento][functions[tipoMovimento.val()].listaSub] || [];
            for(i = 0; i < subs.length && !found; i++) {
                // Uso il == in vece del === in quanto uno dei due campi e' numerico e l'altro e' una stringa
                if(subs[i] && subs[i].numero == numeroSubmovimentoValue) {
                	obj['sub' + lowercaseTipoMovimento] = subs[i];
                    disponibilita = subs[i][functions[tipoMovimento.val()].campoDisponibilita] || 0;
                    found = true;
                }
            }
            if(!found) {
                impostaDatiNegliAlert(["COR_ERR_0010 - Il valore del parametro sub" + tipoMovimento.val()
                    + " non e' consentito: non e' presente nell'elenco dei submovimenti"], alertErrori);
                numeroSubmovimento.val("");
                return;
            }
        }
    }
    
    function popolaDatiTestataMovimento(data, lowercaseTipoMovimento, sub){
    	//loweCaseTipoMovimento = impegno
        var obj = data['sub' + lowercaseTipoMovimento] ? data['sub' + lowercaseTipoMovimento] : data[lowercaseTipoMovimento];
        
    	var cig = obj.cig;
        var cup = obj.cup;
        // SIAC-5311 SIOPE+
        var assenza = obj.siopeAssenzaMotivazione && obj.siopeAssenzaMotivazione.uid
            ? obj.siopeAssenzaMotivazione.uid
            : 0;

        // Imposto i campi
        $("#cigMovimentoGestione").val(cig);
        $("#cupMovimentoGestione").val(cup);
        $('#siopeAssenzaMotivazione').val(assenza);
    }
    
    function computeTestataMovimentoGestione(){
    	var tipoMovimento = radioTipoMovimento.filter(":checked");
    	// Metto il tipo di movimento in lowercase per pigrizia
        var lowercaseTipoMovimento = tipoMovimento.val().toLowerCase();
        var url = "ricerca" + tipoMovimento.val() + "PerChiaveOttimizzato.do";
        // Attivo l'overlay
        var overlayer = collapse.find("[data-overlay]")
            .overlay("show");
        var obj = {};
        
        // Creo l'oggetto per inviare i dati
        obj[lowercaseTipoMovimento + ".annoMovimento"] = annoMovimento.val();
        obj[lowercaseTipoMovimento + ".numero"] = numeroMovimento.val();
        obj[lowercaseTipoMovimento + ".caricaSub"] = false;
        $.postJSON(url, obj)
        .then(function(data) {
        	var event;
            var obj = {};
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
            	return;
            }
            // Popolamento dei dati
            obj[lowercaseTipoMovimento] = data[lowercaseTipoMovimento];
            popolaDatiTestataMovimento(obj, lowercaseTipoMovimento);

            // Lancio l'evento di caricamento
            event = $.Event(lowercaseTipoMovimento + "Caricato", obj);
            $(document).trigger(event);
        })
        .always(function() {
            // Chiudo l'overlay
            overlayer.overlay("hide");
        });
    }
    
    function computeSubMovimentoGestione(){
        var tm = radioTipoMovimento.filter(":checked");
        // Metto il tipo di movimento in lowercase per pigrizia
        var lowercaseTipoMovimento = tm.val().toLowerCase();
        //ricercaMovimentoGestione_cercaImpegnoSubImpegno
        var url = 'ricercaMovimentoGestione_cerca' + tm.val() + 'Sub' + tm.val() + '.do';
        var i ; 
        // Attivo l'overlay
        var obj = {};
        var subs;
        var numeroSubmovimentoValue = numeroSubmovimento.val();
        var overlayer = collapse.find("[data-overlay]")
            .overlay("show");

        // Creo l'oggetto per inviare i dati
        obj['sub' + lowercaseTipoMovimento] = {};
        obj[lowercaseTipoMovimento + ".annoMovimento"] = annoMovimento.val();
        obj[lowercaseTipoMovimento + ".numero"] = numeroMovimento.val();
        obj["numeroSubmovimentoGestione"] =  numeroSubmovimentoValue;
        $.postJSON(url, obj)
        .then(function(data) {
        	var event;
            var obj = {};
            var found = false;
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))){
            	return;
            }
        	subs = data[lowercaseTipoMovimento][functions[tm.val()].listaSub] || [];
            for(i = 0; i < subs.length && !found; i++) {
                // Uso il == in vece del === in quanto uno dei due campi e' numerico e l'altro e' una stringa
                // TODO: usare il + su entrambi i campi per forzare il cast?
                if(subs[i] && subs[i].numero == numeroSubmovimentoValue) {
                    obj['sub' + lowercaseTipoMovimento] = subs[i];
                    found = true;
                }
            }
            if(!found) {
                impostaDatiNegliAlert(["COR_ERR_0010 - Il valore del parametro sub" + tm.val()
                    + " non e' consentito: non e' presente nell'elenco dei submovimenti"], alertErrori);
                numeroSubmovimento.val("");
                return;
            }
            obj[lowercaseTipoMovimento] = data[lowercaseTipoMovimento];
            // Popolamento dei dati
            popolaDatiTestataMovimento(obj, lowercaseTipoMovimento);
        	
        	

            // Lancio l'evento di caricamento
            event = $.Event(lowercaseTipoMovimento + "Caricato", obj);
            $(document).trigger(event);
        })
        .always(function() {
            // Chiudo l'overlay
            overlayer.overlay("hide");
        });
    }
    
    /**
     * Calcola il movimento di gestione se sono stati forniti sufficienti dati.
     */
    function computeMovimentoGestione() {
    	
    	if(!radioTipoMovimento.filter(":checked").val() || !annoMovimento.val() || !numeroMovimento.val()) {
            return;
        }
    	$('#ERRORI').slideUp();
    	
    	return numeroSubmovimento.val() ? computeSubMovimentoGestione() : computeTestataMovimentoGestione();
        
    }

    /**
     * Ottiene le tabelle e ne calcola il dataTable
     */
    function ottieniTabelle() {
        $.postJSON("associaMovimentoAllegatoAtto_obtainLists.do", {})
        .then(function(data) {
            impostaTabellaSpesa(data.listaSubdocumentoSpesa);
            impostaTabellaEntrata(data.listaSubdocumentoEntrata);
        });
    }

    /**
     * Imposta la tabella dei movimenti di spesa.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function impostaTabellaSpesa(list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun impegno"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                	return computeEtichettaSpesa(source);
                }},
                {aTargets:[1], mData: function(source) {
                	return source.cig || '';
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("input", nTd).data("row", iRow);
                }},
                {aTargets:[2], mData: function(source) {
                	return (source.cup || "");
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("input", nTd).data("row", iRow);
                }},
                {aTargets: [3], mData: function(source) {
                    // Costruisco la select in maniera quasi-dinamica
                    var tipoDebitoSIOPE = computeTipoDebitoSIOPE(source.impegno, source.subImpegno);
                    var siopeAssenzaMotivazione = source.siopeAssenzaMotivazione;
                    if(!tipoDebitoSIOPE || 'CO' !== tipoDebitoSIOPE.codice || !siopeAssenzaMotivazione) {
                        return '';
                    }
                    return siopeAssenzaMotivazione.codice + ' - ' + siopeAssenzaMotivazione.descrizione;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("select", nTd).data("row", iRow);
                }},
                {aTargets: [4], mData: function(source) {
                    return source.impegno && source.impegno.listaVociMutuo && source.impegno.listaVociMutuo.length &&
                        source.impegno.listaVociMutuo[0] && source.impegno.listaVociMutuo[0].numeroMutuo || "";
                }},
                {aTargets: [5], mData: function(source) {
                    var result = "";
                    if(source.impegno && source.impegno.capitoloUscitaGestione && source.impegno.capitoloUscitaGestione.numeroCapitolo) {
                        result = source.impegno.capitoloUscitaGestione.numeroCapitolo + "/" + source.impegno.capitoloUscitaGestione.numeroArticolo;
                        if(isGestioneUEB) {
                            result += "-" + source.impegno.capitoloUscitaGestione.numeroUEB;
                        }
                    }
                    return result;
                }},
                {aTargets: [6], mData: function(source) {
                    var result = "";
                    if(source.impegno && source.impegno.attoAmministrativo) {
                        result = "<a rel='popover' href='#'>" + source.impegno.attoAmministrativo.anno + "/" + source.impegno.attoAmministrativo.numero;
                        if(source.impegno.attoAmministrativo.tipoAtto) {
                            result += "/" + source.impegno.attoAmministrativo.tipoAtto.codice;
                        }
                        if(source.impegno.attoAmministrativo.strutturaAmmContabile) {
                            result += "/" + source.impegno.attoAmministrativo.strutturaAmmContabile.codice;
                        }
                        result += "</a>";
                    }
                    return result;
                }, fnCreatedCell: function(nTd, sData, oData){
                    var settings;
                    if(oData.impegno && oData.impegno.attoAmministrativo) {
                        settings = {
                            content: oData.impegno.attoAmministrativo.oggetto,
                            title: "Oggetto",
                            trigger: "hover"
                        };
                        $("a", nTd).popover(settings);
                    }
                }},
                {aTargets: [7], mData: function(source) {
                    var result = "";
                    if(source.provvisorioCassa && source.provvisorioCassa.anno && source.provvisorioCassa.numero) {
                        result = source.provvisorioCassa.anno + "/" + source.provvisorioCassa.numero;
                    }
                    return result;
                }},
                {aTargets:[8], mData: function(source) {
                    return source.importo.formatMoney();
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("input")
                            .data("row", iRow);
                }},
                {aTargets:[9], mData: function() {
                    return "<a href='#'><i class='icon-trash icon-2x'></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).data("row", iRow).substituteHandler("click", function(e) {
                        e.preventDefault();
                        eliminaImpegno(iRow, oData);
                    });
                }},
                {aTargets:[10], mData: function() {
                    return "<a href='#'><i class='icon-pencil icon-2x'></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).data("row", iRow).substituteHandler("click", function(e) {
                        e.preventDefault();
                        apriModaleAggiornaImpegno(iRow, oData);
                    });
                }}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        var table = $("#tabellaImpegni");
        var datatable;
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        datatable = table.dataTable(options);
        datatable.$(".soloNumeri").allowedChars({numeric: true});
        datatable.$(".decimale").gestioneDeiDecimali();
    }

    /**
     * Imposta la tabella dei movimenti di entrata.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function impostaTabellaEntrata(list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun accertamento"
            },
            aoColumnDefs: [
               {aTargets: [0], mData: function(source) {
                   var result = "";
                   if(source.accertamento) {
                       result = "E-" + source.accertamento.annoMovimento + "/" + source.accertamento.numero;
                       if(source.subAccertamento&& source.subAccertamento.numero) {
                           result += "-" + source.subAccertamento.numero;
                       }
                   }
                   return result;
           }},
           {aTargets: [1], mData: function(source) {
               var result = "";
               if(source.accertamento && source.accertamento.capitoloEntrataGestione && source.accertamento.capitoloEntrataGestione.numeroCapitolo) {
                   result = source.accertamento.capitoloEntrataGestione.numeroCapitolo + "/" + source.accertamento.capitoloEntrataGestione.numeroArticolo;
                   if(isGestioneUEB) {
                       result += "-" + source.accertamento.capitoloEntrataGestione.numeroUEB;
                   }
               }
               return result;
           }},
           {aTargets: [2], mData: function(source) {
               var result = "";
               if(source.accertamento && source.accertamento.attoAmministrativo) {
                   result = "<a rel='popover' href='#'>" + source.accertamento.attoAmministrativo.anno + "/" + source.accertamento.attoAmministrativo.numero;
                   if(source.accertamento.attoAmministrativo.tipoAtto) {
                       result += "/" + source.accertamento.attoAmministrativo.tipoAtto.codice;
                   }
                   if(source.accertamento.attoAmministrativo.strutturaAmmContabile) {
                       result += "/" + source.accertamento.attoAmministrativo.strutturaAmmContabile.codice;
                   }
                   result += "</a>";
               }
               return result;
           }, fnCreatedCell: function(nTd, sData, oData){
               var settings;
               if(oData.accertamento && oData.accertamento.attoAmministrativo) {
                   settings = {
                       content: oData.accertamento.attoAmministrativo.oggetto,
                       title: "Oggetto",
                       trigger: "hover"
                   };
                   $("a", nTd).popover(settings);
               }
           }},
           {aTargets: [3], mData: function(source) {
               var result = "";
               if(source.provvisorioCassa && source.provvisorioCassa.anno && source.provvisorioCassa.numero) {
                   result = source.provvisorioCassa.anno + "/" + source.provvisorioCassa.numero;
               }
               return result;
           }},
           {aTargets:[4], mData: function(source) {
               return "<input type='text' class='span12 soloNumeri decimale' name='importoInAtto' value='" + source.importo.formatMoney() + "' placeholder='Importo'/>";
           }, fnCreatedCell: function(nTd, sData, oData, iRow) {
               $(nTd).addClass("tab_Right")
                   .find("input")
                       .data("row", iRow);
           }},
           {aTargets:[5], mData: function() {
               return "<a href='#'><i class='icon-trash icon-2x'></i></a>";
           }, fnCreatedCell: function(nTd, sData, oData, iRow) {
               $("a", nTd).data("row", iRow).substituteHandler("click", function(e) {
                   e.preventDefault();
                   eliminaAccertamento(iRow, oData);
               });
           }}
       ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        var table = $("#tabellaAccertamenti");
        var datatable;
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        datatable = table.dataTable(options);
        datatable.$(".soloNumeri").allowedChars({numeric: true});
        datatable.$(".decimale").gestioneDeiDecimali();
    }
    
    function impegnoCaricatoCallback(e) {
        showDatiImpegno(e.impegno, e.subimpegno);
        showMotivoAssenzaCig(e.impegno, e.subimpegno);
        showDisponibileImpegno(e.impegno, e.subimpegno);
    }
    function accertamentoCaricatoCallback(e) {
        showDatiAccertamento(e.accertamento, e.subaccertamento);
        showDisponibileAccertamento(e.accertamento, e.subaccertamento);
    }
    function showDatiImpegno(impegno, subimpegno) {
        var tipoDebitoSIOPE = computeTipoDebitoSIOPE(impegno, subimpegno);
        var str = ': ' + impegno.annoMovimento + ' / ' + impegno.numero;
        if(subimpegno) {
            str += ' - ' + subimpegno.numero;
        }
        // SIAC-5410
        if(tipoDebitoSIOPE && tipoDebitoSIOPE.codice && tipoDebitoSIOPE.descrizione) {
            str += ' (' + tipoDebitoSIOPE.codice + ' - ' + tipoDebitoSIOPE.descrizione + ')';
        }
        $('#movimentoDati').html(str);
    }
    function showDatiAccertamento(accertamento, subaccertamento) {
        var str = ': ' + accertamento.annoMovimento + ' / ' + accertamento.numero;
        if(subaccertamento) {
            str += ' - ' + subaccertamento.numero;
        }
        $('#movimentoDati').html(str);
    }
    function showMotivoAssenzaCig(impegno, subimpegno) {
        var tipoDebitoSIOPE = computeTipoDebitoSIOPE(impegno, subimpegno);
        var isCO = tipoDebitoSIOPE && tipoDebitoSIOPE.codice === 'CO';
        assenzaCig[isCO ? 'slideDown' : 'slideUp']();
    }
    function computeTipoDebitoSIOPE(impegno, subimpegno) {
        return (subimpegno && subimpegno.siopeTipoDebito) || (impegno && impegno.siopeTipoDebito) || undefined;
    }

    function showDisponibileImpegno(impegno, subimpegno) {
    	var url = "associaMovimentoAllegatoAtto_calcolaDisponibilitaGiaImpegnataImpegno.do";
    	var disponibile = subimpegno && subimpegno.numero ? subimpegno.disponibilitaLiquidare : impegno.disponibilitaLiquidare;
    	var obj = {uidMovimento:impegno.uid, numeroSubmovimento: subimpegno ? subimpegno.numero : null};
    	showDisponibile(url,obj,disponibile);
    }
    
    function showDisponibileAccertamento(accertamento, subaccertamento) {
    	var url = "associaMovimentoAllegatoAtto_calcolaDisponibilitaGiaImpegnataAccertamento.do";
    	var disponibile = subaccertamento && subaccertamento.numero ? subaccertamento.disponibilitaIncassare : accertamento.disponibilitaIncassare;
    	var obj = {uidMovimento:accertamento.uid, numeroSubmovimento: subaccertamento ? subaccertamento.numero : null};
    	showDisponibile(url,obj,disponibile);
    }
    
    /**
     * Visualizza il campo relativo al disponibile.
     */
    function showDisponibile(url, obj, disponibile) {
    	var disponibilita = disponibile || 0;
    	$.postJSON(url, obj)
    	.then(function(data){
    		disponibilita = disponibilita-data.totaleGiaImpegnato;
    		$("#disponibilitaMovimentoGestione").html(disponibilita.formatMoney());
    	});
        
        // Impedisco di cambiare il tipo
        $("input[name='tipoMovimento']").attr("disabled", true);
        $("#divDisponibilitaMovimentoGestione").removeClass("hide");
    }

    /**
     * Aggiunge il movimento di gestione.
     *
     * @param callback (Function) la funzione di callback per il termine dell'invocazione
     *
     * @returns (Deferred) l'oggetto corrispondente alla chiamata AJAX
     */
    function aggiungiMovimentoGestione(callback) {
        var tipoMovimento = radioTipoMovimento.filter(":checked").val();
        var url;
        var obj;
        var spinner;
        // Se non ho selezionato il tipo di movimento, esco
        if(!tipoMovimento) {
            return;
        }
        url = functions[tipoMovimento].urlInserimento;
        // Creo l'oggetto per la chiamata AJAX
        obj = creazioneOggettoPerChiamataAjax(tipoMovimento);
        // Attivo lo spinner
        spinner = $("#SPINNER_buttonConfermaAggiungiMovimento").addClass("activated");
        // Effettuo la chiamata
        return  $.postJSON(url, obj)
        .then(function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Eventuale messaggi: carico la richiesta all'utente
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, obj, $.noop, callback);
                return;
            }
            callback(data);
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Gestione della richiesta di prosecuzione.
     *
     * @param array        (Array) l'array di messaggi richiedenti conferma
     * @param url          (String) l'url da invocare
     * @param data         (Object) l'oggetto da fornire come parametro
     * @param failCallback (Function) la funzione di callback in caso di scelta negativa dell'utente
     * @param endCallback  (Function) la funzione di callback in caso di scelta positiva dell'utente
     */
    function gestioneRichiestaProsecuzione(array, url, data, failCallback, endCallback) {
        var str = '';
        var btnNo = {label: 'No, indietro', 'class': 'btn', callback: failCallback};
        var btnOk = {label: 'Si, prosegui', 'class': 'btn', callback: function() {
            var div = $('.bootbox');
            var btns = div.find('a[data-handler]').overlay('show');
            data.proseguireConElaborazione = true;
            $.postJSON(url, data)
            .then(endCallback)
            .always(function() {
                btns.overlay('hide');
                div.modal('hide');
            });
            return false;
        }};
        if(array) {
            str = array.reduce(function(acc, el) {
                if(el.testo) {
                    return acc + '<li>' + el.testo + '</li>';
                }
                return acc + '<li>' + el.codice + ' - ' + el.descrizione + '</li>';
            }, '<ul>') + '</ul>';
        }
        $('#collapseAggiungiMovimento').find('input').removeAttr('disabled');
        bootbox.dialog(str, [btnNo, btnOk], {animate: true, classes: 'dialogWarn', header: 'Attenzione', backdrop: 'static'});
    }

    /**
     * Crea l'oggetto per la chiamata AJAX.
     *
     * @param tipo (String) il tipo di movimento di gestione (Impegno / Accertamento)
     *
     * @returns (Object) l'oggetto tramite cui effettuare la chiamata AJAX
     */
    function creazioneOggettoPerChiamataAjax(tipo) {
        var obj = {};
        var lowerCaseTipo = tipo.toLowerCase();
        var campoModel = functions[tipo].campoModel;
        var hasCigCupMutuo = functions[tipo].hasCigCupMutuo;

        obj[campoModel + "." + lowerCaseTipo + ".annoMovimento"] = $("#annoMovimento").val();
        obj[campoModel + "." + lowerCaseTipo + ".numero"] = $("#numeroMovimentoGestione").val();
        obj[campoModel + "." + "sub" + tipo + ".numero"] = $("#numeroSubmovimentoGestione").val();
        //aggiungo quello del provvisorio se e' presente 
        obj[campoModel + ".provvisorioCassa"+".anno"] = $("#annoProvvisorioCassaSubdocumento").val();
        obj[campoModel + ".provvisorioCassa"+".numero"] = $("#numeroProvvisorioCassaSubdocumento").val();
        
        if(hasCigCupMutuo) {
            obj["voceMutuo.numeroMutuo"] = $("#numeroMutuoVoceMutuo").val();
            obj[campoModel + ".cig"] = $("#cigMovimentoGestione").val();
            obj[campoModel + ".cup"] = $("#cupMovimentoGestione").val();
            obj[campoModel + '.siopeAssenzaMotivazione.uid'] = $('#siopeAssenzaMotivazione').val();
        }
        obj[campoModel + ".importo"] = $("#importoInAtto").val();

        return obj;
    }

    /**
     * Elimina l'impegno selezionato.
     *
     * @param row  (Integer) la riga da eliminare
     * @param data (Object)  l'oggetto da eliminare
     */
    function eliminaImpegno(row, data) {
        var str = "S-" + data.impegno.annoMovimento + "/" + data.impegno.numero;
        if(data.subImpegno && data.subImpegno.numero) {
            str += "-" + data.subImpegno.numero;
        }
        pulsanteSiEliminazione.substituteHandler("click", function() {
            spinnerEliminazione.addClass("activated");
            $.postJSON("associaMovimentoAllegatoAtto_removeImpegno.do", {row: row})
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                // Segnalo il messaggio di successo
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                // Ricarico la tabella con i dati fornitimi
                impostaTabellaSpesa(data.listaSubdocumentoSpesa);
                // Reimposto il totale
                $("#spanTotaleImpegni").html(data.totaleSpesa.formatMoney());
                modaleEliminazione.modal("hide");
            }).always(function() {
                spinnerEliminazione.removeClass("activated");
            });
        });
        spanElementoEliminazione.html(str);
        modaleEliminazione.modal("show");
    }

    /**
     * Elimina l'accertamento selezionato.
     *
     * @param row  (Integer) la riga da eliminare
     * @param data (Object)  l'oggetto da eliminare
     */
    function eliminaAccertamento(row, data) {
        var str = "E-" + data.accertamento.annoMovimento + "/" + data.accertamento.numero;
        if(data.subAccertamento && data.subAccertamento.numero) {
            str += "-" + data.subAccertamento.numero;
        }
        pulsanteSiEliminazione.substituteHandler("click", function() {
            spinnerEliminazione.addClass("activated");
            $.postJSON("associaMovimentoAllegatoAtto_removeAccertamento.do", {row: row})
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                // Segnalo il messaggio di successo
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                // Ricarico la tabella con i dati fornitimi
                impostaTabellaEntrata(data.listaSubdocumentoEntrata);
                // Reimposto il totale
                $("#spanTotaleAccertamenti").html(data.totaleEntrata.formatMoney());
                modaleEliminazione.modal("hide");
            }).always(function() {
                spinnerEliminazione.removeClass("activated");
            });
        });
        spanElementoEliminazione.html(str);
        modaleEliminazione.modal("show");
    }

    /**
     * Aggiorna l'impegno selezionato.
     */
    function aggiornaImpegno(row, subdoc) {
    	
        var obj = {};
        var url = "associaMovimentoAllegatoAtto_alterImpegno.do";
        
        obj.row = row;
        obj["subdocumentoSpesa.siopeAssenzaMotivazione.uid"] =  $('#siopeAssenzaMotivazioneModale').val();
        obj["subdocumentoSpesa.importo"] = $('#importoInAttoModale').val();
        obj["subdocumentoSpesa.cig"] = $('#cigMovimentoGestioneModale').val();
        obj["subdocumentoSpesa.cup"] = $('#cupMovimentoGestioneModale').val();

        $.postJSON("associaMovimentoAllegatoAtto_alterImpegno.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modaleEditMovimento'))) {
                // Reimposto i dati precedenti
                return;
            }
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, obj, function() {
                    // Ripristino i valori precedenti
                    impostaTabellaSpesa(data.listaSubdocumentoSpesa);
                } ,function(data2) {
                    // Imposto il messaggio di successo
                    impostaDatiNegliAlert(data2.informazioni, alertInformazioni);
                    // Ricarico le tabelle
                    impostaTabellaSpesa(data2.listaSubdocumentoSpesa);
                    // Reimposto il totale
                    $("#spanTotaleImpegni").html(data2.totaleSpesa.formatMoney());
                });
                return;
            }
            // Imposto il messaggio di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico le tabelle
            impostaTabellaSpesa(data.listaSubdocumentoSpesa);
            // Reimposto il totale
            $("#spanTotaleImpegni").html(data.totaleSpesa.formatMoney());
            $('#editMovimento').modal('hide');
        }).always(function() {
        });
    }

    /**
     * Aggiorna l'accertamento selezionato.
     */
    function aggiornaAccertamento() {
        var $this = $(this);
        var row = $this.data("row");
        var tr = $this.closest("tr")
            .overlay("show");
        var obj = {};
        var url = "associaMovimentoAllegatoAtto_alterAccertamento.do";

        obj.row = row;
        obj["subdocumentoEntrata.importo"] = tr.find("input[name='importoInAtto']").val();

        $.postJSON(url, obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Reimposto i dati precedenti
                impostaTabellaEntrata(data.listaSubdocumentoEntrata);
                return;
            }
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi, url, obj, function() {
                    // Reimporto i dati originali
                    impostaTabellaEntrata(data.listaSubdocumentoEntrata);
                }, function(data2) {
                    // Imposto il messaggio di successo
                    impostaDatiNegliAlert(data2.informazioni, alertInformazioni);
                    // Ricarico le tabelle
                    impostaTabellaEntrata(data2.listaSubdocumentoEntrata);
                    // Reimposto il totale
                    $("#spanTotaleAccertamenti").html(data2.totaleEntrata.formatMoney());
                });
            }
            // Imposto il messaggio di successo
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricarico le tabelle
            impostaTabellaEntrata(data.listaSubdocumentoEntrata);
            // Reimposto il totale
            $("#spanTotaleAccertamenti").html(data.totaleEntrata.formatMoney());
        }).always(function() {
            tr.overlay("hide");
        });
    }
    
    function apriModaleAggiornaImpegno(row, subdoc){
	     var $modal = $('#editMovimento');
	     var isCO = subdoc.siopeTipoDebito && subdoc.siopeTipoDebito.codice === 'CO';
	     var $pulsanteConferma = $('#confermaAggiornaMovimentoModale');
	     $('#ERRORI_modaleEditMovimento').slideUp();
	     
	     $('#titoloModaleMovimento').html(computeEtichettaSpesa(subdoc));
	     $('#cigMovimentoGestioneModale').val(subdoc.cig ? subdoc.cig : '');
	     $('#cupMovimentoGestioneModale').val(subdoc.cup ? subdoc.cup : '');
	     assenzaCig[isCO ? 'slideDown' : 'slideUp']();
    	 $('#siopeAssenzaMotivazioneModale').val((isCO && subdoc.siopeAssenzaMotivazione ?
	    			 subdoc.siopeAssenzaMotivazione.uid
	    			 : 0));
    	 $('#importoInAttoModale').val(subdoc.importo.formatMoney());
	     $modal.modal('show');
	     $pulsanteConferma.substituteHandler('click', aggiornaImpegno.bind(undefined, row, subdoc));
    }
    
    

    $(function() {
        pulsanteAggiungiMovimento.click(toggleNuovoMovimento);
        $("#buttonAnnullaAggiungiMovimento").click(function() {
            pulsanteAggiungiMovimento.trigger("click");
        });

        $(document).on("change", "input[name='tipoMovimento']", impostaTipoMovimento);
        $(document).on("impegnoCaricato", impegnoCaricatoCallback);
        $(document).on("accertamentoCaricato", accertamentoCaricatoCallback);
        Accertamento.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined, "#disponibilitaMovimentoGestione");
        Impegno.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined,
            "#cigMovimentoGestione", "#cupMovimentoGestione", "#numeroMutuoVoceMutuo", "#disponibilitaMovimentoGestione",
            undefined, '#siopeAssenzaMotivazione');

        // Alla modifica dei campi del tipo movimento, anno, numero e numero subdoc, se valorizzati, ricalcolo i dati del movimenti di gestione
        $("input[name='tipoMovimento'], #annoMovimento, #numeroMovimentoGestione, #numeroSubmovimentoGestione").on("change", computeMovimentoGestione);
        ottieniTabelle();

        //$("#tabellaImpegni").on("change", "input[type='text'], select", aggiornaImpegno);
        $("#tabellaAccertamenti").on("change", "input[type='text']", aggiornaAccertamento);
        $('#pulsanteCompilazioneGuidataProvvisorioCassaSubdocumento').attr("disabled","disabled");
    });
}(jQuery));