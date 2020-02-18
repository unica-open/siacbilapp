/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var RisultatiRicercaPrimeNoteGSA = (function($) {
    var exports = {};
    "use strict";
    var alertErrori = $("#ERRORI");
    var alertErroriModale = $("#ERRORI_modale");
    var alertInformazioniModale = $("#INFORMAZIONI_modale");
    var baseUrl = $("#HIDDEN_baseUrl").val();
    
    /**
     * Impostazione della tabella.
     */
    exports.impostaTabellaPrimaNota = function() {
        var tableId = "#tabellaRisultatiRicerca";
        var startPosition = parseInt($('#HIDDEN_startPosition').val());
        var def = $.Deferred();
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaPrimaNotaIntegrataGSAAjax.do",
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
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                $("a[rel='popover']", tableId).popover();
                $("a.tooltip-test", tableId).tooltip();
                
                $('body').overlay('hide');
                
                if(def) {
                	def.resolve();
                	def = null;
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: "numeroMovimento"},
                {aTargets: [1], mData: "isResiduo"},
                {aTargets: [2], mData: "infoENumero", fnCreatedCell: function(nTd, sData, oData) {
                    $('a', nTd).click([oData, nTd], apriModaleDettagli);
                }},
                {aTargets: [3], mData: "descrizione"},
                {aTargets: [4], mData: "stato"},
                {aTargets: [5], mData: "numeroLibroGiornale"},
                {aTargets: [6], mData: "dataRegistrazioneDefinitiva"},
                {aTargets: [7], mData: "causaleEPWithPopover"},
                {aTargets: [8], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.validaPrimaNota")
                            .click([oData], validazionePrimaNota)
                            .end()
                        .find("a.aggiornaPrimaNota")
                            .click([oData], aggiornamentoPrimaNota)
                            .end()
                        .find("a.consultaPrimaNota")
                            .click([oData], consultazionePrimaNota)
                            .end()
                        .find("a.annullaPrimaNota")
                            .click([oData], annullamentoPrimaNota)
                    		.end()
	                    .find("a.collegaPrimaNota")
	            			.click([oData], collegamentoPrimaNota)
	            			.end()
                    	.find("a.ratei")
                    		.click([oData], RateiRiscontiGSA.inserimentoRateo)
                    		.end()
                    	.find("a.rateiAggiornamento")
                    		.click([oData], RateiRiscontiGSA.aggiornamentoRateo)
                    		.end()
                    	.find("a.gestioneRisconti")
                    		.click([oData], RateiRiscontiGSA.gestioneRisconti);
                }}
            ]
        };
        if(startPosition) {
            opts.iDisplayStart = startPosition;
        }
        $(tableId).dataTable(opts);
        return def;
    };
    
    /**
     * Apertura del collapse delle prime note
     */
	exports.aperturaCollapseCollegamentoPrimaNota = function() {
        $(":input", "#fieldsetCollegamentoPrimaNota")
                .val("")
                .end()
            .removeProp("checked");
        $("#collapseCollegamentoPrimaNota").collapse("show");
    };
    
    /**
     * 
     */
	exports.collegaPrimaNota = function() {
        var fieldset = $("#fieldsetCollegamentoPrimaNota");
        var obj = fieldset.serializeObject();
        var collapse = $("#collapseCollegamentoPrimaNota");

        alertErroriModale.slideUp();
        alertInformazioniModale.slideUp();

        // Attivo l'overlay
        collapse.overlay("show");
        $.postJSON(baseUrl + "_collegaPrimaNota.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioniModale);
            // Ricarico la tabella
            impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteCollegate);
        }).always(function() {
        	collapse.collapse("hide");
            collapse.overlay("hide");
        });
    };

    /**
     * Validazione dellla prima nota.
     */
    function validazionePrimaNota(e){
        var primaNota = e.data[0];
        var href = $(this).data("href");
        $("#modaleValidazioneElementoSelezionato").html(primaNota.numero + " - " + primaNota.descrizione);
        $("#modaleValidazionePulsanteSalvataggio").substituteHandler("click", function() {
            // SIAC-5336
            var classificatoreGSA = $('#HIDDEN_classificatoreGSAUid').val();
            // SIAC-5853
            var dataRegistrazione = $('#dataRegistrazionePrimaNotaLibera').val();
            // Aggiungo l'uid specificato nell'URL
            href += '&classificatoreGSA.uid=' + encodeURIComponent(classificatoreGSA)
                + '&primaNota.dataRegistrazioneLibroGiornale=' + encodeURIComponent(dataRegistrazione);
            $("#SPINNER_modaleValidazionePulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        // SIAC-5741
        GSAClassifZtree.cleanZTree(exports.classifGSATreeId);
        $('#modaleValidazione').one('shown', GSAClassifZtree.closeClassificatoreGSACollapse);
        $("#modaleValidazione").modal("show");
    }

    /**
     * Aggiornamento della prima nota.
     */
    function aggiornamentoPrimaNota(){
         var href = $(this).data("href");
         document.location = href;
    }

    /**
     * Consultazione della prima nota.
     */
    function consultazionePrimaNota(){
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Annullamento della prima nota.
     */
    function annullamentoPrimaNota(e){
        var primaNota = e.data[0];
        var href = $(this).data("href");
        $("#modaleAnnullamentoElementoSelezionato").html(primaNota.numero + " - " + primaNota.descrizione);
        $("#modaleAnnullamentoPulsanteSalvataggio").substituteHandler("click", function() {
            $("#SPINNER_modaleAnnullamentoPulsanteSalvataggio").addClass("activated");
            document.location = href;
        });
        $("#modaleAnnullamento").modal("show");
    }

    /**
     * Apertura del modale dei dettagli.
     */
    function apriModaleDettagli (e) {
        var primaNota = e.data[0];
        var td = e.data[1];
        var row = $(td).closest("tr").overlay("show");

        $.postJSON("dettaglioMovimentiPrimaNotaIntegrataAction_caricaMovimenti.do", {"primaNota.uid": primaNota.uid}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            impostaTabellaModale(data.listaMovimentoDettaglio);
            // Imposto i campi aggiuntivi
            $("#modaleDettaglioMovimentiSpan").html(primaNota.numero);
            $("#modaleDettaglioMovimentiDescrizione").html("Descrizione: " + primaNota.descrizione);
            $("#modaleDettaglioMovimentiTotaleDare").html(data.totaleDare.formatMoney());
            $("#modaleDettaglioMovimentiTotaleAvere").html(data.totaleAvere.formatMoney());

            $("#modaleDettaglioMovimenti").modal("show");
        }).always(function() {
            row.overlay("hide");
        });
    }

    /**
     * Impostazione della tabella della modale.
     *
     * @param list (Array) la lista da impostare
     *
     * @returns (jQuery) il wrapper della tabella impostata
     */
    function impostaTabellaModale(list) {
        var tableId = "#modaleDettaglioMovimentiTabella";
        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: false,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti movimenti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun movimento disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return source.conto && source.conto.codice || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.conto && source.conto.descrizione || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.segno && "DARE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [3], mData: function(source) {
                    return source.segno && "AVERE" === source.segno._name && source.importo != undefined && source.importo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        return $(tableId).dataTable(opts);
    }
    
    /**
     * Collegamento prime note - apertura modale
     */
    function collegamentoPrimaNota(e){
    	alertErroriModale.slideUp();
        alertInformazioniModale.slideUp();
    	caricaTabellaPrimeNote(e.data[0]);
    }
    
    /**
     * Popolamento della tabella delle prime note collegate.
     *
     * @param list (Array) la lista tramite cui popolare la tabella
     */
    function impostaTabellaPrimeNoteCollegate(list) {
        var tableId = "tabellaPrimeNoteCollegate";
        var opts = {
            bServerSide: false,
            aaData: list,
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
                sZeroRecords: "Nessuna prima nota collegata",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source){
                	return source.tipoCausale.descrizione;
                }},
                {aTargets: [1], mData: function(source){
                	return source.bilancio.anno;
                }},
                {aTargets: [2], mData: function(source){
                	return source.numeroRegistrazioneLibroGiornale;
                }},
                {aTargets: [3], mData: function(source){
                	return (source.tipoRelazionePrimaNota != null && source.tipoRelazionePrimaNota.descrizione != null) ? source.tipoRelazionePrimaNota.descrizione : "";
                }},
                {aTargets: [4], mData: function(source){
                	return (source.statoOperativoPrimaNota != null && source.statoOperativoPrimaNota.descrizione != null)? source.statoOperativoPrimaNota.descrizione : "";
                }},
                //Azioni
                {aTargets: [5], mData: function(source) {
                    return "<div class=\"btn-group\">" +
                               "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                               "<ul class=\"dropdown-menu pull-right\">" +
                                   "<li><a class=\"eliminaCollegamento\">elimina</a></li>" +
                               "</ul>" +
                           "</div>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.eliminaCollegamento")
                            .click([oData, iRow], eliminaCollegamento);
                }}
            ]
        };
        $("#" + tableId).dataTable(opts);
        if(list.length > 0){
        	$("#spanTotaleNoteCollegate").html(": totale " + list.length);
        }
    }

    /**
	 * Carica la lista delle prime note collegate
	 */
	function caricaTabellaPrimeNote(primaNota) {
		$("#tabellaRisultatiRicerca").overlay("show");
	    $.postJSON(baseUrl + "_ottieniListaPrimeNoteCollegate.do", {"primaNota.uid": primaNota.uid} , function(data) {
	        if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
	        	$("#tabellaRisultatiRicerca").overlay("hide");
	            return;
	        }
	        impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteCollegate);
	        	$("#tabellaRisultatiRicerca").overlay("hide");
	        	$("#modaleCollegaPrimaNota").modal("show");
	    });
	}

    /**
     * Eliminazione del collegamento.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaCollegamento(e) {
        var primaNota = e.data[0];
        var idx = e.data[1];
        var msg = " <Strong>Elemento selezionato:</Strong> collegamento con la prima nota " + primaNota.tipoCausale.descrizione + " numero " +  primaNota.numeroRegistrazioneLibroGiornale + ".<br>" +
        		" Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?" ;
        var btns =  [{'label': 'no, indietro', 
        			'class': 'btn', 
        			'callback': $.noop
        			},
        			{'label': 'si, prosegui ', 
        			'class': 'btn btn-primary', 
        			'callback': eliminazioneCollegamento.bind(null, idx)
        			}];
        
        bootboxAlert(msg, '', '', btns);

//        $("#modaleEliminazioneElementoSelezionato").html(str);
//        // Apro il modale
//        $("#modaleEliminazione").modal("show");
        // Lego l'azione di cancellazione
//        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", function() {
//        	eliminazioneCollegamento(idx);
//        });
    }

    /**
     * Eliminazione del collegamento.
     *
     * @param index l'indice del collegamento da eliminare
     */
    function eliminazioneCollegamento(index) {

    	alertErroriModale.slideUp();
        alertInformazioniModale.slideUp();
        
        $("#tabellaPrimeNoteCollegate").overlay("show");
        $.postJSON(baseUrl + "_eliminaCollegamento.do", {indicePrimaNota: index}, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                $("#modaleEliminazioneConto").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioniModale);
            impostaTabellaPrimeNoteCollegate(data.listaPrimeNoteCollegate);
            // Chiudo il modale
        }).always(function() {
        	$("#tabellaPrimeNoteCollegate").overlay("hide");
        });
    }
    
    return exports;
    
}(jQuery));  
    
$(function() {
    // Inizializzazione del datatable
	RisultatiRicercaPrimeNoteGSA.impostaTabellaPrimaNota();
	// prima nota collegata
    PrimaNota.inizializza("#annoPrimaNota", "#numeroPrimaNota", "#selectTipoPrimaNota","#pulsanteCompilazioneGuidataPrimaNota", "#HIDDEN_ambito", "#modaleCollegaPrimaNota");
    $("#pulsanteCollegamentoPrimeNote").click(RisultatiRicercaPrimeNoteGSA.aperturaCollapseCollegamentoPrimaNota);
    $("#pulsanteSalvaCollegamento").click(RisultatiRicercaPrimeNoteGSA.collegaPrimaNota);
    $(document).on('rateoSalvato', function(){
    	$('body').overlay('show');
    	RisultatiRicercaPrimeNoteGSA.impostaTabellaPrimaNota();
    });
    
    // SIAC-5336
    // SIAC-5741
    RisultatiRicercaPrimeNoteGSA.classifGSATreeId = GSAClassifZtree.initClassificatoreGSAZtree();

});
