/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var PrimaNota = function($,w){
    "use strict";
    var exports = {};
    var alertErrori = $('#ERRORI');
    var alertInformazioni = $('#INFORMAZIONI');

    var baseOptsDataTable = {
    		bPaginate: true,
            bLengthChange: false,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bDestroy: true,
            oLanguage: {
              	sInfo: "_START_ - _END_ di _MAX_ risultati",
              	sInfoEmpty: "0 risultati",
               	sProcessing: "Attendere prego...",
               	sZeroRecords: "Non sono presenti movimenti collegati",
               	oPaginate: {
               		sFirst: "inizio",
               		sLast: "fine",
               		sNext: "succ.",
               		sPrevious: "prec.",
               		sEmptyTable: "Nessuna richiesta disponibile"
               	}
            },
            aoColumnDefs : []
    };

    var optsComuniDatiFinanziari = {
        	bServerSide:true,
        	sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaDatiFinanziariPrimaNotaIntegrataGSAAjax.do",
            iDisplayLength: 10,
            fnDrawCallback: function () {
            	$('#divDatiFinanziari').overlay('hide');
            }
        };

    var optsComuniSenzaPaginazione= {
    		bServerSide: false,
            iDisplayLength: 4
        };

    var baseOpts = $.extend(true, {}, baseOptsDataTable, optsComuniSenzaPaginazione);

    var baseOptsDatiFinanziari =  $.extend(true, {}, baseOptsDataTable, optsComuniDatiFinanziari);

    exports.attivaDataTablePrimeNoteCollegate = attivaDataTablePrimeNoteCollegate;
    exports.ottieniDatiFinanziari = ottieniDatiFinanziari;
    exports.aperturaCollapseConti = aperturaCollapseConti;
    exports.caricaElencoScritture = caricaElencoScritture;
    exports.submitForm = submitForm;

    /**
     * Estrae l'importo di dato tipo (DARE O AVERE)
     * */
    function estraiImportoDareAvere(tipo) {
    	return function(source) {
    		if(source.movimentoDettaglio.segno && source.movimentoDettaglio.segno._name === tipo) {
                var res = source.movimentoDettaglio.importo || 0;
                return res.formatMoney();
            }
            return "";
    	};
    }

    /**
     * Impostazione della tabella.
     */
    function attivaDataTablePrimeNoteCollegate() {
        var tableId = "#tabellaPrimeNoteCollegate";
        var optsNoteCollegate = {
            fnDrawCallback: function () {
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div").hide();
            }
        };
        var opts = $.extend(true, {}, baseOpts, optsNoteCollegate);
        $(tableId).dataTable(opts);
    }

    function attivaDataTableSubdocSpesa() {
        var optsDocumentoSpesa = {
        	aoColumnDefs : [
        		{aTargets: [0], mData: defaultPerDataTable('numero')},
    			{aTargets: [1], mData: defaultPerDataTable('importo')},
    			{aTargets: [2], mData: defaultPerDataTable('movimentoGestione')},
    			{aTargets: [3], mData: defaultPerDataTable('pianoDeiConti')},
    			{aTargets: [4], mData: defaultPerDataTable('liquidazione')},
    			{aTargets: [5], mData: defaultPerDataTable('ordinativo')}
    			]
        };

        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoSpesa);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }


    function attivaDataTableSubdocEntrata() {
        var optsDocumentoEntrata = {
    		aoColumnDefs: [
    			{aTargets: [0], mData: defaultPerDataTable('numero')},
    			{aTargets: [1], mData: defaultPerDataTable('importo')},
    			{aTargets: [2], mData: defaultPerDataTable('movimentoGestione')},
    			{aTargets: [3], mData: defaultPerDataTable('pianoDeiConti')},
    			{aTargets: [4], mData: defaultPerDataTable('ordinativo')}
    		]
        };
        var opzioniTabella = $.extend(true, {}, baseOptsDatiFinanziari, optsDocumentoEntrata);
        $('#divDatiFinanziari').find('table').dataTable(opzioniTabella);
    }

    function attivaDataTableScritture(list) {
        var tableId = "tabellaScritture";
        var editabile = $('#aggiornaPrimaNotaIntegrata').length >0 && $('#pulsanteInserimentoDati').length >0;
        var optsScritture = {
        	aaData: list,
            fnPreDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#" + tableId + "_processing").closest(".row-fluid.span12").addClass("hide");
                $("a[rel='popover']", "#" + tableId).popover();
            },
            aoColumnDefs: [
            	{aTargets: [0], mData: defaultPerDataTable('movimentoDettaglio.conto.codice')},
            	{aTargets: [1], mData: defaultPerDataTable('movimentoDettaglio.conto.descrizione')},
                {aTargets: [2], mData: estraiImportoDareAvere('DARE'), fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");

                }},
                {aTargets: [3], mData: estraiImportoDareAvere('AVERE'),  fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], bVisible: editabile, mData: function(source) {
                    var temp = "";
                    var res = "";
                    if(!source.utilizzoImportoNonModificabile) {
                        // L'importo e' modificabile dall'operatore
                        temp += "<li><a class=\"aggiornaConto\">aggiorna</a></li>";
                    }
                    if(!source.utilizzoContoObbligatorio) {
                        // Posso eliminare il conto
                        temp += "<li><a class=\"eliminaConto\">elimina</a></li>";
                    }
                    if(temp) {
                        res = "<div class=\"btn-group\">" +
                                "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>" +
                                "<ul class=\"dropdown-menu pull-right\">" +
                                    temp +
                                "</ul>" +
                            "</div>";
                    }
                    return res;
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right")
                        .find("a.aggiornaConto")
                            .click([oData, iRow], aggiornaConto)
                            .end()
                        .find("a.eliminaConto")
                            .click([oData, iRow], eliminaConto);
                }}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, optsScritture);
        $("#" + tableId).dataTable(opts);
    }

    /**
     * Ottiene i dati finanziari
     */
    function ottieniDatiFinanziari(e) {
        var header = $('#headingAccordionDatiFinanziari');
        var div = $('#divDatiFinanziari');
        var obj;

        if(header.data('loaded')) {
            return;
        }
        e.preventDefault();
        e.stopPropagation();
        obj = {'primaNota.uid': $('#uidPrimaNotaIntegrata').val(), 'tipoCollegamento': $('#tipoCollegamentoDatiFinanziari').val()};
        header.overlay('show');

        return $.post('consultaPrimaNotaIntegrataGSA_ottieniDatiFinanziari.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                return;
            }
            div.html(data);
            header.data('loaded', true);
            div.collapse('show');
        }).always(header.overlay.bind(header, 'hide'));
    }

    //--------------------------------------------------------------------------------------------------
    //---------------- GESTIONE CONTI ------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    function aggiornaDatiScritture (data) {
        var listaMovimentiPerTabella = data.listaElementoScritturaPerElaborazione;
        attivaDataTableScritture(listaMovimentiPerTabella);
        $("#totaleDare").html(data.totaleDare.formatMoney());
        $("#totaleAvere").html(data.totaleAvere.formatMoney());
    }
    /**
     * Carica la lista del conto tipo operazione.
     */
    function caricaElencoScritture() {
    	 var overlay = $("#tabellaScritture").overlay("show");

         $.postJSON("aggiornaContoPrimaNotaIntegrataGSA_ottieniListaConti.do", {})
         .then(function(data) {
             if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                 // Svuoto la select ed esco
                 return;
             }
             aggiornaDatiScritture(data);
         }).always(function() {
             overlay.overlay("hide");
         });
    }


    /**
     * Apertura del collapse dei conti.
     */
    function aperturaCollapseConti() {
        $(":input", "#fieldsetCollapseDatiStruttura").not("[data-maintain]").val("");
        $("#collapseDatiStruttura").collapse("show");
        $("#pulsanteSalvaInserimentoConto").substituteHandler('click',inserisciConto);
    }

    /**
     * Aggiornamento del conto.
     */
    function aggiornamentoContoDaModale() {
        $("#segnoModale").val($("input[type='radio'][data-AVERE]").prop('checked'));
        var obj = unqualify($("#fieldsetModaleAggiornamentoConto").serializeObject(), 1);
        var spinner = $("#SPINNER_pulsanteSalvaAggiornamentoConto").addClass("activated");

//        $('#ERRORI')Modale.slideUp();
//        $('#ERRORI').slideUp();
//        alertInformazioni.slideUp();

        $.postJSON("aggiornaContoPrimaNotaIntegrataGSA_aggiornaConto.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI_modale'))) {
                 //$("#modaleAggiornamentoConto").modal("hide");
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
            $("#modaleAggiornamentoConto").modal("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);
        }).always(function() {
            spinner.removeClass("activated");
        });
    }


    /**
     * Aggiornamento del conto modale.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function aggiornaConto(e) {
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Popolo i dati
        var elementoStruttura = e.data[0];
        var idx = e.data[1];

        // Popolo i dati del conto
        var dareAvere = elementoStruttura.contoTipoOperazione.operazioneSegnoConto._name;
        var importo = elementoStruttura.movimentoDettaglio && elementoStruttura.movimentoDettaglio.importo || 0;
        $("input[type='radio'][data-" + dareAvere + "]").prop('checked', true);
        $('#operazioneSegnoContoModale').val(dareAvere);

        $("#importoModale").val(importo.formatMoney());

        // Imposto l'indice
        $("#indiceContoModale").val(idx);
        $("#pulsanteSalvaAggiornamentoConto").substituteHandler('click',aggiornamentoContoDaModale);
        // Apro il modale
        //$('#ERRORI')Modale.slideUp();
        $("#modaleAggiornamentoConto").modal("show");
    }

    /**
     * Eliminazione del conto da riga.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function eliminaConto(e) {
        var elementoStruttura = e.data[0];
        var idx = e.data[1];
        var str = "";

        if(elementoStruttura.movimentoDettalgio && elementoStruttura.movimentoDettaglio.conto && elementoStruttura.movimentoDettaglio.conto.descrizione) {
            str = " " + elementoStruttura.movimentoDettaglio.conto.descrizione;
        }
        $("#modaleEliminazioneElementoSelezionato").html(str);
        // Apro il modale
        $("#modaleEliminazione").modal("show");
        // Lego l'azione di cancellazione
        $("#modaleEliminazionePulsanteSalvataggio").substituteHandler("click", function() {
            eliminazioneConto(idx);
        });
    }

    /**
     * Eliminazione del conto.
     *
     * @param index l'indice del conto da eliminare
     */
    function eliminazioneConto(index) {
        $('#ERRORI').slideUp();
        //alertInformazioni.slideUp();

        $.postJSON("aggiornaContoPrimaNotaIntegrataGSA_eliminaConto.do", {indiceConto: index})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                 $("#modaleEliminazione").modal("hide");
                return;
            }
            impostaDatiNegliAlert(data.informazioni, $('#INFORMAZIONI'));
            aggiornaDatiScritture(data);
            // Chiudo il modale
            $("#modaleEliminazione").modal("hide");
        });
    }


    /**
     * Inserimento del conto da collapse
     */
    function inserisciConto() {
        $("#segnoCollapse").val($("input[type='radio'][data-collapse-AVERE]").prop('checked'));
        var obj = $("#fieldsetCollapseDatiStruttura").serializeObject();
        var collapse = $("#collapseDatiStruttura");


        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Attivo l'overlay
        collapse.overlay("show");
        $.postJSON("aggiornaContoPrimaNotaIntegrataGSA_inserisciConto.do", obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errore: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            $("#collapseDatiStruttura").collapse("hide");
            // Ricarico la tabella
            aggiornaDatiScritture (data);
        }).always(function() {
            collapse.overlay("hide");
        });
    }
    
    /**
     * Submit del form
     */
    function submitForm(e) {
        var $form = $('form');
        var obj = $form.serialize();
        var $body = $(document.body).overlay('show');
        e.preventDefault();
        
        $.postJSON('aggiornaPrimaNotaIntegrataGSA_aggiorna.do', obj)
        .then(function(data) {
            var loc = document.location.href.split('/');
            // Per sicurezza rimouvo eventuali campi hidden aggiunti
            $('input[name="proseguiImportiNonCongruenti"]').remove();
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            if(data.messaggi && data.messaggi.length) {
                gestioneRichiestaProsecuzione(data.messaggi);
                return;
            }
            loc[loc.length - 1] = 'aggiornaPrimaNotaIntegrataGSA_aggiornaSuccess.do';
            document.location = loc.join('/');
        }).always($body.overlay.bind($body, 'hide'));
        return false;
    }
    
    /**
     * Richiesta prosecuzione inserimento conto
     * @param messaggi (array) i mesaggi per l'utente
     */
    function gestioneRichiestaProsecuzione(messaggi) {
        var msg = messaggi.map(function(el) {
            return el.codice + ' - ' + el.descrizione;
        }).join('<br/>');
        bootboxAlert(msg, 'Attenzione', 'dialogWarn', [
            {label: 'No, indietro', 'class': 'btn', callback: $.noop},
            {label: 'S&igrave;, prosegui', 'class': 'btn', callback: function(){
                // Forzo il submit
                var $form = $('form');
                $form.append('<input type="hidden" name="proseguiImportiNonCongruenti" value="true"/>');
                $form.submit();
            }}
        ]);
    }
    return exports;
}(jQuery, this);

$(function() {
    // Inizializzazione del datatable
    PrimaNota.attivaDataTablePrimeNoteCollegate();
    $('#headingAccordionDatiFinanziari').on('click', PrimaNota.ottieniDatiFinanziari);
    $('#pulsanteInserimentoDati').click(PrimaNota.aperturaCollapseConti);

    Conto.inizializza(undefined, undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
    $('#fieldsetModaleAggiornamentoConto').find('input[name = "modale.operazioneSegnoConto"]').removeAttr('disabled');
    PrimaNota.caricaElencoScritture();

    // SIAC-5802
    $('form').substituteHandler('submit', PrimaNota.submitForm);
});
