/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
    var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
    var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
    var spanDescrizione = $("#datiRiferimentoSoggettoSpan");
    var selectSplitReverse = $('#tipoIvaSplitReverseTipoOnere');
    var divsMayHideSplitReverse = $('[data-hidden-split-reverse]', 'form');
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
     * Effettua il cambio dello split/reverse con la gestione dei campi
     */
    function cambioSplitReverse() {
        var selectedOption = $('option:selected', selectSplitReverse);
        var codice;
        var divsToHide;
        if(!selectedOption.length || !selectedOption.val()) {
            // Nulla da effettuare: esco
            return;
        }

        codice = selectedOption.data('codice');
        divsToHide = divsMayHideSplitReverse.filter('[data-hidden-split-reverse~="' + codice + '"]');
        
        $("label[for='aliquotaCaricoSoggetto']").html(codice === "SI" || codice === "SC" ? "Aliquota" : "Aliquota a carico soggetto");
        showDivs(divsMayHideSplitReverse.not(divsToHide));
        hideDivs(divsToHide);
    }

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
            .find(':input')[attrFnc]('disabled', true).end()
            .find('.chosen-select').trigger('chosen:updated').end();
    }
    
    /**
     * Calcola la stringa del movimento di gestione
     * @param movgestName  (any) il movimento di gestione
     * @param subName      (any) il submovimento
     * @param capitoloName (any) il capitolo
     * @param source       (any) i dati sorgente
     * @return (string) la stringa del movimento di gestione
     */
    function computeStringMovimentoGestione(movgestName, subName, capitoloName, source) {
        var res = '';
        var movgest = source[movgestName];
        var sub = source[subName];
        var capitolo = source[capitoloName];

        if(capitolo) {
            res += (capitolo.bilancio ? capitolo.bilancio.anno : capitolo.annoCapitolo) + '/';
        }
        if(movgest) {
            res += movgest.annoMovimento + '/' + movgest.numero;
        }
        if(sub) {
            res += '-' + sub.numero;
        }
        return res;
    }

    /**
     * Formattazione della valuta
     * @param value (any) il valore da formattare
     * @return (string) il valore formattato
     */
    function formatMoney(value) {
        if(typeof value === 'number') {
            return value.formatMoney();
        }
        return '';
    }

    /**
     * Legge i dati da possibili valori distinti
     * @param possibleFields      (string[]) i possibili campi
     * @param fieldToRead         (string) il campo da leggere
     * @param defaultValueToApply (any) il valore di default
     * @param fncToApply          (function) la funzione da applicare
     * @return (function) al function di lettura del dato
     */
    function readData(possibleFields, fieldToRead, defaultValueToApply, fncToApply) {
        var fnc = fncToApply || passThrough;
        var defaultValue = defaultValueToApply || '';
        return function(source) {
            var el = possibleFields.reduce(function(acc, val) {
                return acc !== undefined ? acc : source[val];
            }, undefined);
            var field = el && el[fieldToRead] || defaultValue;
            return fnc(field);
        };
    }
    
    /**
     * Aggiunge il tab_Right al nodo
     * @param nTd (Node) il nodo
     * @return (Node) il nodo originale
     */
    function tabRight(nTd) {
        $(nTd).addClass("tab_Right");
        return nTd;
    }

    /**
     * Impostazione delle causali di entrata.
     *
     * @param list (Array) la lista delle causali
     */
    var impostaCausaliEntrata = function (list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun accertamento associato"
            },
            aoColumnDefs: [
            	{aTargets: [0], mData: defaultPerDataTable('distinta.descrizione')},
                {aTargets: [1], mData: computeStringMovimentoGestione.bind(undefined, 'accertamento', 'subAccertamento', 'capitoloEntrataGestione'), fnCreatedCell: function(nTd, sData) {
                    $(nTd).data("string", sData).addClass("firstCell");
                }},
                {aTargets: [2], mData: readData(['subAccertamento', 'accertamento'], 'descrizione')},
                {aTargets: [3], mData: readData(['subAccertamento', 'accertamento'], 'importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [4], mData: readData(['subAccertamento', 'accertamento'], 'disponibilitaIncassare', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: function() {
                    return "<a class='btn btn-secondary'>Elimina &nbsp;<i class='icon-trash icon-1x'></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    var $nTd = $(nTd);
                    var str = $nTd.closest("tr")
                        .find(".firstCell")
                            .data("string");
                    $nTd.addClass("tab_Right span2")
                        .find("a")
                            .substituteHandler("click", eliminaCausale.bind(undefined, iRow, str, "entrata"));
                }}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        $("#tabellaMovimentiEntrata").dataTable(options);
    };

    /**
     * Impostazione delle causali di spesa.
     *
     * @param list (Array) la lista delle causali
     */
    var impostaCausaliSpesa = function(list) {
        var opts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Nessun impegno associato"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: computeStringMovimentoGestione.bind(undefined, 'impegno', 'subImpegno', 'capitoloUscitaGestione')},
                {aTargets: [1], mData: readData(['subImpegno', 'impegno'], 'descrizione')},
                {aTargets: [2], mData: readData(['subImpegno', 'impegno'], 'importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [3], mData: readData(['subImpegno', 'impegno'], 'disponibilitaPagare', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [4], mData: function() {
                    return "<a class='btn btn-secondary'>Elimina &nbsp;<i class='icon-trash icon-1x'></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $(nTd).addClass("tab_Right span2")
                        .find("a")
                            .substituteHandler("click", eliminaCausale.bind(undefined, iRow, "", "spesa"));
                }}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        $("#tabellaMovimentiSpesa").dataTable(options);
    };

    var causaleDataByType = {
            spesa: {
                list: "listaCausaleSpesa",
                capitolo: {
                    property: "capitoloUscitaGestione",
                    fieldAnno: "#annoCapitoloSpesa",
                    fieldNumeroCapitolo: "#numeroCapitoloSpesa",
                    fieldNumeroArticolo: "#numeroArticoloSpesa",
                    fieldNumeroUEB: "#numeroUEBSpesa"
                },
                tableLoading: impostaCausaliSpesa,
                urlInserimento: "aggiornaTipoOnereSpesa_inserisciCausale.do",
                urlEliminazione: "aggiornaTipoOnereSpesa_eliminaCausale.do",
                fieldsetCapitolo: "#fieldsetCapitoloSpesa",
                fieldsetMovimento: "#fieldsetImpegno"
            },
            entrata: {
                list: "listaCausaleEntrata",
                capitolo: {
                    property: "capitoloEntrataGestione",
                    fieldAnno: "#annoCapitoloEntrata",
                    fieldNumeroCapitolo: "#numeroCapitoloEntrata",
                    fieldNumeroArticolo: "#numeroArticoloEntrata",
                    fieldNumeroUEB: "#numeroUEBEntrata"
                },
                tableLoading: impostaCausaliEntrata,
                urlInserimento: "aggiornaTipoOnereEntrata_inserisciCausale.do",
                urlEliminazione: "aggiornaTipoOnereEntrata_eliminaCausale.do",
                fieldsetCapitolo: "#fieldsetCapitoloEntrata",
                fieldsetMovimento: "#fieldsetAccertamento"
            }
        };

    /**
     * Apertura del modele di eliminazione della causale e legame con l'azione di eliminazione della stessa.
     *
     * @param row     (Number) la riga da eliminare
     * @param element (String) la descrizione dell'elemento
     * @param type    (String) il tipo da utilizzare (spesa / entrata)
     */
    function eliminaCausale(row, element, type) {
        $("#spanElementoSelezionatoModaleEliminazione").html(element);
        $("#pulsanteSiModaleEliminazione").substituteHandler("click", function() {
            processaEliminazioneCausale(row, type);
        });
        $("#modaleEliminazione").modal("show");
    }

    /**
     * Processa l'eliminazione della causale
     *
     * @param row  (Number) la riga
     * @param type (String) il tipo da utilizzare
     */
    function processaEliminazioneCausale(row, type) {
        var spinner = $("#SPINNER_pulsanteSiModaleEliminazione").addClass("activated");
        $.postJSON(causaleDataByType[type].urlEliminazione, {row: row})
        .then(function(data) {
            var list;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Ho errori: esco
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricalcolo la tabella
            list = data[causaleDataByType[type].list] || [];
            causaleDataByType[type].tableLoading(list);
            if(list.length === 0) {
                // Non ho piu' causali. Ripristino l'editabilita' del capitolo
                ripristinaEditabilitaCapitolo(type);
            }
            // Se non ho piu' causali, ripristino l'editabilita' del soggetto
            if(data.totaleCausaliCollegate === 0) {
                ripristinaEditabilitaSoggetto();
            }
        }).always(function() {
            spinner.removeClass("activated");
            $("#modaleEliminazione").modal("hide");
        });
    }

    /**
     * Ripristina l'editabilita' del capitolo.
     *
     * @param type (String) il tipo
     */
    function ripristinaEditabilitaCapitolo(type) {
        var capData = causaleDataByType[type].capitolo;
        $(capData.fieldAnno).prop("readonly", false);
        $(capData.fieldNumeroCapitolo).prop("readonly", false);
        $(capData.fieldNumeroArticolo).prop("readonly", false);
        $(capData.fieldNumeroUEB).prop("readonly", false);

        pulsanteCompilazioneGuidataNonEditabile(type, false);
    }

    /**
     * Ripristina l'editabilita' del soggetto.
     */
    function ripristinaEditabilitaSoggetto() {
        $("#codiceSoggettoSoggetto").prop("readonly", false);
        $("#compilazioneGuidataSoggetto").removeClass("hide");
    }
    
    
    function pulsanteCompilazioneGuidataNonEditabile(type, nonEditabile){
        $("#compilazioneGuidata_" + type) [nonEditabile ? 'addClass' : 'removeClass']('hide');
    }

    /**
     * Caricamento delle causali.
     */
    function caricaCausali() {
        $.postJSON("aggiornaTipoOnere_ottieniListaCausali.do", {})
        .then(function(data) {
            impostaCausaliSpesa(data.listaCausaleSpesa || []);
            impostaCausaliEntrata(data.listaCausaleEntrata || []);
        });
    }

    /**
     * Inserimento della causale.
     *
     * @param type (String) il tipo (Entrata / Spesa)
     */
    function inserisciCausale(type) {
        var obj = $(causaleDataByType[type].fieldsetMovimento + ", " + causaleDataByType[type].fieldsetCapitolo + ", #fieldsetSoggetto").serializeObject();
        var overlay = $(causaleDataByType[type].fieldsetMovimento).overlay("show");

        $.postJSON(causaleDataByType[type].urlInserimento, obj)
        .then(function(data) {
            var list;
            var capitolo;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Ho errori: ripulisco i campi e esco
                ripulisciCampiMovimentoGestione();
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricalcolo la tabella
            list = data[causaleDataByType[type].list] || [];
            causaleDataByType[type].tableLoading(list);
            // Imposto i dati del capitolo
            capitolo = data[causaleDataByType[type].capitolo.property];
            if(capitolo) {
                impostaCapitolo(type, capitolo);
            }
            if(data.soggetto) {
                impostaSoggetto(data.soggetto);
            }
        }).always(overlay.overlay.bind(overlay, "hide"));
    }

    /**
     * Ripulisce i campi relativi al movimento gestione in caso di errore
     */
    function ripulisciCampiMovimentoGestione(){
        $("#annoMovimentoEntrata, #numeroMovimentoEntrata, #numeroSubMovimentoEntrata, #annoMovimentoSpesa, #numeroMovimentoSpesa, #numeroSubMovimentoSpesa").val("");
    }

    /**
     * Impostazione dei dati del capitolo.
     *
     * @param type (String) il tipo
     * @param cap  (Object) il capitolo
     */
    function impostaCapitolo(type, cap) {
        var capData = causaleDataByType[type].capitolo;
        $(capData.fieldAnno).val(cap.annoCapitolo).prop("readonly", true);
        $(capData.fieldNumeroCapitolo).val(cap.numeroCapitolo).prop("readonly", true);
        $(capData.fieldNumeroArticolo).val(cap.numeroArticolo).prop("readonly", true);
        $(capData.fieldNumeroUEB).val(cap.numeroUEB).prop("readonly", true);
        pulsanteCompilazioneGuidataNonEditabile(type, true);
    }

    /**
     * Impostazione dei dati del soggetto.
     *
     * @param sog (Object) il soggetto
     */
    function impostaSoggetto(sog) {
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        if(!campoCodiceSoggetto.val()) {
            campoCodiceSoggetto.val(sog.codiceSoggetto)
                .trigger("change");
        }
        campoCodiceSoggetto.prop("readonly", true);
        $("#compilazioneGuidataSoggetto").addClass("hide");
    }

    /**
     * Inserimento della causale di spesa.
     */
    function inserisciCausaleSpesa() {
        inserisciCausale("spesa");
    }

    /**
     * Inserimento della causale di entrata.
     */
    function inserisciCausaleEntrata() {
        inserisciCausale("entrata");
    }

    /**
     * Apertura del modale del soggetto
     */
    function apriModaleSoggetto() {
        // Copio i dati
        $("#codiceSoggetto_modale").val(campoCodiceSoggetto.val());
        // Chiudo gli errori
        $("#ERRORI_MODALE_SOGGETTO").slideUp();
        // Apro il modale
        $("#modaleGuidaSoggetto").modal("show");
    }

    $(function() {
        var oldModalitaPagamentoSoggetto = $("#oldModalitaPagamentoSoggetto").val();

        selectSplitReverse.substituteHandler('change', cambioSplitReverse).change();

        // Inizializzazione delle funzionalita' della ricerca capitolo
        Capitolo.inizializza('EntrataGestione', "#annoCapitoloEntrata", "#numeroCapitoloEntrata", "#numeroArticoloEntrata", "#numeroUEBEntrata",
            "#datiRiferimentoCapitoloEntrataSpan", "#pulsanteApriCompilazioneGuidataEntrata", "_ceg");
        Capitolo.inizializza('UscitaGestione', "#annoCapitoloSpesa", "#numeroCapitoloSpesa", "#numeroArticoloSpesa", "#numeroUEBSpesa",
                "#datiRiferimentoCapitoloSpesaSpan", "#pulsanteApriCompilazioneGuidataSpesa", "_cug");

        // Legame azioni inserimento movimento gestione
        $("#pulsanteAggiungiSpesa").substituteHandler("click", inserisciCausaleSpesa);
        $("#pulsanteAggiungiEntrata").substituteHandler("click", inserisciCausaleEntrata);
        
        pulsanteCompilazioneGuidataNonEditabile("entrata", $("#capitoloEntrataNonEditabile").val() === 'true');
        pulsanteCompilazioneGuidataNonEditabile("spesa", $("#capitoloUscitaNonEditabile").val() === 'true');
        if($("#soggettoNonEditabile").val() === "true"){
        	$("#compilazioneGuidataSoggetto").addClass("hide");
        }

        // Reset del form
        $("form").substituteHandler("reset", function(e) {
            e.preventDefault();
            $("body").overlay("show");
            document.location.reload(true);
        });

        // Caricamento causali
        caricaCausali();

        // Gestione soggetto
        // Inizializzazione dei dati del soggetto
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");

        // Apertura del modale di compilazione guidata
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(apriModaleSoggetto);
        // Caricamento dettaglio soggetto
        campoCodiceSoggetto.on("change codiceSoggettoCaricato", function(e, doNotCleanModal, doNotCloseAlert) {
            var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione, !!doNotCleanModal,undefined, undefined, true)
                .always(function() {
                    // Caricamento vecchi valori
                    Soggetto.caricaOldSedeSecondaria(accordionSedeSecondaria, $("#oldSedeSecondariaSoggetto").val());
                    Soggetto.caricaOldModalitaPagamentoSoggetto(accordionModalitaPagamento, $("#oldModalitaPagamentoSoggetto").val());
                });
            if(!doNotCloseAlert){
            	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){ 
                	impostaDatiNegliAlert(errori, alertErrori);
                	return errori;
                });
            }
        })
            // Forzo la chiamata al metodo
            .trigger("change", [true, true]);
        // Filtro delle modalita di pagamento
        accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
            var tabella = accordionModalitaPagamento.find("table");
            var uid = $(this).filter(":checked").val();
            Soggetto.filterModalitaPagamentoSoggetto(uid, tabella, accordionModalitaPagamento)
                .always(function() {
                    if(oldModalitaPagamentoSoggetto) {
                        Soggetto.caricaOldModalitaPagamentoSoggetto(accordionModalitaPagamento, oldModalitaPagamentoSoggetto);
                        oldModalitaPagamentoSoggetto = undefined;
                    }
                });
        });
    });
}(jQuery);