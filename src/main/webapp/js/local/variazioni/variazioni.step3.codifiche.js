/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per lo step 3 delle variazioni - variazione di codifiche */

var Variazioni = (function(exports) {

    /** Mappatura nomi dei campi - nomi dei valori ottenuti dall'AJAX */
    var campiSelect = {
        MISSIONE: "missione",
        PROGRAMMA: "programma",
        TITOLO_SPESA: "titoloSpesa",
        MACROAGGREGATO: "macroaggregato",
        CLASSIFICAZIONE_COFOG: "classificazioneCofog",
        TITOLO_ENTRATA: "titoloEntrata",
        TIPOLOGIA: "tipologiaTitolo",
        CATEGORIA: "categoriaTipologiaTitolo",
        TIPO_FINANZIAMENTO: "tipoFinanziamento",
        TIPO_FONDO: "tipoFondo",
        PERIMETRO_SANITARIO_ENTRATA: "perimetroSanitarioEntrata",
        PERIMETRO_SANITARIO_SPESA: "perimetroSanitarioSpesa",
        TRANSAZIONE_UE_ENTRATA: "transazioneUnioneEuropeaEntrata",
        TRANSAZIONE_UE_SPESA: "transazioneUnioneEuropeaSpesa",
        POLITICHE_REGIONALI_UNITARIE: "politicheRegionaliUnitarie"
    };

    var classificatoriGenerici = {
        Uscita: {
            CLASSIFICATORE_1: "classificatoreGenerico1",
            CLASSIFICATORE_2: "classificatoreGenerico2",
            CLASSIFICATORE_3: "classificatoreGenerico3",
            CLASSIFICATORE_4: "classificatoreGenerico4",
            CLASSIFICATORE_5: "classificatoreGenerico5",
            CLASSIFICATORE_6: "classificatoreGenerico6",
            CLASSIFICATORE_7: "classificatoreGenerico7",
            CLASSIFICATORE_8: "classificatoreGenerico8",
            CLASSIFICATORE_9: "classificatoreGenerico9",
            CLASSIFICATORE_10: "classificatoreGenerico10",
            CLASSIFICATORE_31: "classificatoreGenerico11",
            CLASSIFICATORE_32: "classificatoreGenerico12",
            CLASSIFICATORE_33: "classificatoreGenerico13",
            CLASSIFICATORE_34: "classificatoreGenerico14",
            CLASSIFICATORE_35: "classificatoreGenerico15"
        },
        Entrata: {
            CLASSIFICATORE_36: "classificatoreGenerico1",
            CLASSIFICATORE_37: "classificatoreGenerico2",
            CLASSIFICATORE_38: "classificatoreGenerico3",
            CLASSIFICATORE_39: "classificatoreGenerico4",
            CLASSIFICATORE_40: "classificatoreGenerico5",
            CLASSIFICATORE_41: "classificatoreGenerico6",
            CLASSIFICATORE_42: "classificatoreGenerico7",
            CLASSIFICATORE_43: "classificatoreGenerico8",
            CLASSIFICATORE_44: "classificatoreGenerico9",
            CLASSIFICATORE_45: "classificatoreGenerico10",
            CLASSIFICATORE_46: "classificatoreGenerico11",
            CLASSIFICATORE_47: "classificatoreGenerico12",
            CLASSIFICATORE_48: "classificatoreGenerico13",
            CLASSIFICATORE_49: "classificatoreGenerico14",
            CLASSIFICATORE_50: "classificatoreGenerico15"
        }
    };

    var campiSelectCodifica = {
            CATEGORIA_CAPITOLO: "categoriaCapitolo"
    };

    var campiAttributi = {
        FLAG_ASSEGNABILE: "assegnabile",
        FLAG_FUNZIONI_DELEGATE: "funzioniDelegateRegione",
        FLAG_PER_MEMORIA: "corsivoPerMemoria",
        FLAG_RILEVANTE_IVA: "rilevanteIva",
        FLAG_TRASFERIMENTO_ORGANI_COMUNITARI: "trasferimentoOrganiComunitari",
        FLAG_ENTRATE_RICORRENTI: "entrateRicorrenti",
        NOTE: "note",
        FLAG_TRASFERIMENTI_VINCOLATI: "trasferimentiVincolati"
    };

    var campiDescrizioni = {
        DESCRIZIONE: "descrizioneCapitolo",
        DESCRIZIONE_ARTICOLO: "descrizioneArticolo"
    };

    var campiFlags = {
        flagCorsivoPerMemoria: "corsivoPerMemoria",
        flagFunzioniDelegateRegione: "funzioniDelegateRegione",
        flagRilevanteIva: "rilevanteIva",
        flagImpegnabile: "impegnabile"
    };

    var campiTrees = {
        elementoPianoDeiConti: ["PDC", "PDC_I", "PDC_II", "PDC_III", "PDC_IV", "PDC_V"],
        strutturaAmministrativoContabile: ["CDC", "CDR"],
        siopeSpesa: ["SIOPE_SPESA", "SIOPE_SPESA_I", "SIOPE_SPESA_II", "SIOPE_SPESA_III"],
        siopeEntrata: ["SIOPE_ENTRATA", "SIOPE_ENTRATA_I", "SIOPE_ENTRATA_II", "SIOPE_ENTRATA_III"]
    };

    /**
     * Carica gli attributi modificabili.
     *
     * @param mappaAttributiModificabili la mappatura degli attributi modificabili
     * @param tipoCapitolo               il tipo di capitolo
     */
    function caricamentoAttributiModificabili(mappaAttributiModificabili, tipoCapitolo) {
        var index;
        var elementoDOM;
        for(index in campiAttributi) {
            if(campiAttributi.hasOwnProperty(index) && (elementoDOM = $("#" + campiAttributi[index] + "Capitolo" + tipoCapitolo)).length > 0) {
                if(!mappaAttributiModificabili[index]) {
                    $("#" + campiAttributi[index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                        .find(":input")
                            .attr("disabled", "disabled");
                } else {
                    // Ripristino il campo
                    $("#" + campiAttributi[index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                        .find(":input")
                            .removeAttr("disabled");
                }
            }
        }
    }

    /**
     * Carica le codifiche per un capitolo.
     *
     * @param elementoCapitoloCodifiche       l'elemento trovato
     * @param mappaClassificatori             la mappa delle liste di classificatori
     * @param mappaClassificatoriModificabili la mappa rappresentante la modificabilit&agrave; dei classificatori
     * @param mappaDescrizioniModificabili    la mappa rappresentante la modificabilit&agrave; delle descrizioni
     * @param tipoCapitolo                    il tipo di capitolo
     * @param listaCategoriaCapitolo          la lista delle categorie
     */
    function caricamentoCodifiche(elementoCapitoloCodifiche, mappaClassificatori, mappaClassificatoriModificabili, mappaDescrizioniModificabili, tipoCapitolo, listaCategoriaCapitolo) {
        var index;
        var elementoDOM;
        var elementoCapitolo;
        var classificatoreValido;
        var i;
        var tipoClassificatore = (tipoCapitolo === "Uscita" ? "Spesa": "Entrata");
        var str;

        var siope = elementoCapitoloCodifiche['siope' + tipoClassificatore];
        $('#HIDDEN_idSiope'+tipoClassificatore).val(siope && siope.uid || '');
        $('#siope' + tipoClassificatore).val(siope && siope.codice || '');
        $('#descrizioneSiope'+tipoClassificatore).val(siope && siope.descrizione || '');
        $('#HIDDEN_descrizioneSiope'+tipoClassificatore).val(siope && siope.descrizione || '');

        var popolaSelect = function(idx, value) {
            var temp = "<option value='" + value.uid + "' ";
            if(elementoCapitolo && !elementoCapitolo.dataFineValidita && elementoCapitolo.uid === value.uid) {
                temp += "selected ";
            }
            temp += ">" + value.codice + "-" + value.descrizione + "</option>";
            str += temp;
        };

        for(index in campiSelect) {
            if(campiSelect.hasOwnProperty(index) && (elementoDOM = $("#" + campiSelect[index] + "Capitolo" + tipoCapitolo)).length > 0) {
                // Controllo se nascondere il campo
                if(!mappaClassificatoriModificabili[index]) {
                    $("#" + campiSelect[index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                        .find(":input").attr("disabled", "disabled");
                } else {
                    // Ripristino il campo
                    $("#" + campiSelect[index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                        .find(":input").removeAttr("disabled");
                }
                // Pulisco il select
                elementoDOM.find("option").remove().end();
                // Ottengo l'uid dell'elemento corrispondente
                elementoCapitolo = elementoCapitoloCodifiche[campiSelect[index]];
                str = "<option value='0'></option>";
                // Carico il select con i dati ottenuti dalla mappa dei classificatori
                $.each(mappaClassificatori[index] || [], popolaSelect);
                elementoDOM.html(str);
            }
        }

        for(index in campiSelectCodifica) {
            if(campiSelectCodifica.hasOwnProperty(index) && (elementoDOM = $("#" + campiSelectCodifica[index] + "Capitolo" + tipoCapitolo)).length > 0) {


                // Pulisco il select
                elementoDOM.find("option").remove().end();
                // Ottengo l'uid dell'elemento corrispondente
                elementoCapitolo = elementoCapitoloCodifiche[campiSelectCodifica[index]];
                str = "<option value='0'></option>";
                // Carico il select con i dati ottenuti dalla listacategoriaa(al momento messo di forza è l'unico)
                $.each(listaCategoriaCapitolo || [], popolaSelect);
                elementoDOM.html(str);
            }
        }
        // Carico i classificatori generici
        for(index in classificatoriGenerici[tipoCapitolo]) {
            if(classificatoriGenerici[tipoCapitolo].hasOwnProperty(index)
                    && (elementoDOM = $("#" + classificatoriGenerici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo)).length > 0) {
                // Controllo se nascondere il campo
                if(!mappaClassificatoriModificabili[index]) {
                    $("#" + classificatoriGenerici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                        .find(":input").attr("disabled", "disabled");
                } else {
                    // Ripristino il campo
                    $("#" + classificatoriGenerici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                        .find(":input").removeAttr("disabled");
                }
                // Pulisco il select
                elementoDOM.find("option").remove().end();
                // Ottengo l'uid dell'elemento corrispondente
                elementoCapitolo = elementoCapitoloCodifiche[classificatoriGenerici[tipoCapitolo][index]];
                str = "<option value='0'></option>";
                // Carico il select con i dati ottenuti dalla mappa dei classificatori
                $.each(mappaClassificatori[index] || [], popolaSelect);
                elementoDOM.html(str);

                // Popolo i label
                elementoCapitolo = mappaClassificatori[index];
                if(elementoCapitolo && elementoCapitolo.length > 0) {
                    // Se presente, prendo il primo elemento per ottenere il label
                    $("#" + classificatoriGenerici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Label").html(elementoCapitolo[0].tipoClassificatore.descrizione);
                } else {
                    // Se non presente, nascondo la combo
                    $("#" + classificatoriGenerici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide");
                }
            }
        }

        // Carico il ricorrente
        elementoCapitolo = elementoCapitoloCodifiche["ricorrente" + tipoClassificatore];
        elementoDOM = $("#ricorrente" + tipoClassificatore + "Capitolo" + tipoCapitolo);
        // Cancello i vecchi label
        elementoDOM.find("label").remove().end();
        $.each(mappaClassificatori["RICORRENTE_" + tipoClassificatore.toUpperCase()], function(idx, value) {
            var checkbox = $("<input/>").val(value.uid).attr("type", "radio").attr("name", "ricorrente" + tipoClassificatore + ".uid");
            var label = $("<label/>").addClass("radio inline").html(checkbox).append("&nbsp;").append(value.descrizione);
            if(elementoCapitolo && elementoCapitolo.uid === value.uid && !elementoCapitolo.dataFineValidita) {
                checkbox.attr("checked", "checked");
            }
            elementoDOM.append(label);
        });
        if(!mappaClassificatoriModificabili["RICORRENTE_" + tipoClassificatore.toUpperCase()]) {
            $("#ricorrente" + tipoClassificatore + "Capitolo" + tipoCapitolo + "Div").addClass("hide");
        } else {
            $("#ricorrente" + tipoClassificatore + "Capitolo" + tipoCapitolo + "Div").removeClass("hide");
        }

        // Carico i flag
        for(index in campiFlags) {
            if(campiFlags.hasOwnProperty(index) && (elementoDOM = $("#" + campiFlags[index] + "Capitolo" + tipoCapitolo)).length > 0) {
                if(elementoCapitoloCodifiche[index]) {
                    elementoDOM.attr("checked", "cheched");
                } else {
                    elementoDOM.removeAttr("checked");
                }
            }
        }

        // Carico i campi testuali
        for(index in campiDescrizioni) {
            if(campiDescrizioni.hasOwnProperty(index) && (elementoDOM = $("#" + campiDescrizioni[index] + "Capitolo" + tipoCapitolo)).length > 0) {
//                elementoDOM.html(elementoCapitoloCodifiche[campiDescrizioni[index]]);
                elementoDOM.val(elementoCapitoloCodifiche[campiDescrizioni[index]]);
                if(!mappaDescrizioniModificabili[index]) {
                    $("#" + campiDescrizioni[index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide");
                } else {
                    $("#" + campiDescrizioni[index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide");
                }
            }
        }

        $("#noteCapitolo" + tipoCapitolo).html(elementoCapitoloCodifiche.note);

        // Carico gli zTree (il SIOPE è caricato dall'elemento del piano dei conti)
        Variazioni.ZTree.caricaPianoDeiConti(elementoCapitoloCodifiche);
        Variazioni.ZTree.caricaStrutturaAmministrativoContabile(elementoCapitoloCodifiche, mappaClassificatori.CDC);

        // Nascondo i trees
        for(index in campiTrees) {
            if(campiTrees.hasOwnProperty(index)) {
                classificatoreValido = false;
                for(i = 0; i < campiTrees[index].length && !classificatoreValido; i++) {
                    // Ottengo il classificatore valido dalla lista dei classificatori modificabili
                    classificatoreValido = classificatoreValido || mappaClassificatoriModificabili[campiTrees[index][i]];
                }
                // Workaround? Pare non funzionare senza
                classificatoreValido = false || classificatoreValido;
                if(!classificatoreValido) {
                    $("#" + index + "Capitolo" + tipoCapitolo + "Pulsante").addClass("hide");
                } else {
                    $("#" + index + "Capitolo" + tipoCapitolo + "Pulsante").removeClass("hide");
                }
            }
        }
    }

    /**
     * Effettua una chiamata al servizio per il caricamento delle codifiche.
     *
     * @param elementoCapitoloCodifiche l'elemento rispetto al quale cercare le codifiche
     * @param tipoCapitolo              il tipo di capitolo
     * @param popolareRequest           se sia necessario popolare la request
     *
     * @return l'oggetto Deferred corrispondente all'invocazione
     */
    function caricaCodifiche(elementoCapitoloCodifiche, tipoCapitolo, popolareRequest) {
        var oggettoPerChiamataAjax = {};
        if(popolareRequest) {
            oggettoPerChiamataAjax = qualify($.extend(true, {}, elementoCapitoloCodifiche), "specificaCodifiche.elementoCapitoloCodifiche");
        }

        var ajaxSource = "controlloModificabilitaCaricamentoClassificatoriVariazioneCodifiche.do";
        var alertErrori = $("#ERRORI");
        var alertMessaggi = $("#MESSAGGI");
        var alertInformazioni = $("#INFORMAZIONI");

        // Controllo classificatori modificabili e caricamento liste
        return $.postJSON(
            ajaxSource,
            oggettoPerChiamataAjax,
            function(data) {
                // Controllo degli errori
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                if(impostaDatiNegliAlert(data.messaggi, alertMessaggi)) {
                    return;
                }
                if(impostaDatiNegliAlert(data.informazioni, alertInformazioni)) {
                    return;
                }
                // Carico gli attributi modificabili
                caricamentoAttributiModificabili(data.specificaCodifiche.mappaAttributiModificabili, tipoCapitolo);

                // Carico le liste delle codifiche sulla GUI
                caricamentoCodifiche(elementoCapitoloCodifiche, data.specificaCodifiche.mappaClassificatori,
                    data.specificaCodifiche.mappaClassificatoriModificabili, data.specificaCodifiche.mappaDescrizioniModificabili, tipoCapitolo,
                    data.specificaCodifiche.listaCategoriaCapitolo);

                // Mostro i div
                $("#fieldsetCapitolo" + tipoCapitolo).slideDown();
                $("#divRicercaCapitolo").slideDown();
            }
        );
    }

    /**
     * Popola il dataTable.
     *
     * @param lista {Array} la lista tramite cui popolare il dataTable
     */
    function popolaDataTable(lista) {
        var options = {
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
                sZeroRecords: "Non sono presenti variazioni",
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
                    mData: function (source) {
                        return "<input type='radio' name='uid' value='" + source.uid + "'/>";
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                    	return source.denominazioneCapitolo;
                        //return "<a rel='popover' href='#'>" + source.denominazioneCapitolo + "</a>";
                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        return source.descrizioneCapitolo;
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        return source.descrizioneArticolo;
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function(source) {
                    	             	
                        var res = "";
                        if(source.strutturaAmministrativoContabile && source.strutturaAmministrativoContabile.codice && source.strutturaAmministrativoContabile.descrizione) {
                            res = source.strutturaAmministrativoContabile.codice + " - " + source.strutturaAmministrativoContabile.descrizione;
                        }
                        return res;
                    }
                }
            ]
        };
        // Creo il dataTable
        $("#codificheNellaVariazione").dataTable(options);
    }

    /**
     * Inserisce la variazione.
     */
    function lavoraConVariazione(ajaxUrl) {
        var spinner = $("#inserisciModificaSpinner");
        var tipoCapitolo = $("input[name='specificaCodifiche.tipoCapitolo']:checked").val();
        var oggettoNelFieldset = $("#fieldsetCapitolo" + tipoCapitolo).serializeObject();
        var oggettoDaInserire = qualify(oggettoNelFieldset, "specificaCodifiche.elementoCapitoloCodifiche");
        spinner.addClass("activated");
        $.postJSON(
            ajaxUrl,
            oggettoDaInserire,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                    return;
                }
                if(impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"))) {
                    return;
                }
                if(impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI"))) {
                    return;
                }

                // Popolo il dataTable
                popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);
                // Disabilito il pulsante
                $("#bottoneInserisciModifica").off();
                // Chiudo il collapse
                //$("#collapseGestioneCodifiche").collapse("hide");
                $("#collapseGestioneCodifiche").is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');
                $("#divRicercaCapitolo").slideUp();
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Inserisce la variazione.
     */
    function inserisciVariazione() {
        lavoraConVariazione("inserisciVariazioneCodifica.do");
    }

    /**
     * Aggiorna la variazione.
     */
    function aggiornamentoVariazione() {
        lavoraConVariazione("aggiornamentoCapitoloVariazioneCodifica.do");
    }

    
 // CR SIAC-2559
    /**
     * Gestione del SIOPE.
     *
     * @param selectorCodice            (String) il selettore per il codide
     * @param selectorDescrizione       (String) il selettore per la descrizione
     * @param selectorHiddenUid         (String) il selettore per l'uid, campo hidden
     * @param selectorHiddenDescrizione (String) il selettore per la descrizione, campo hidden
     * @param selectorHiddenTipo        (String) il selettore per il tipo, campo hidden
     * @param url                       (String) l'url da invocare
     * @returns (Promise) la promise corrispondente all'invocazione AJAX
     */
    function gestioneSIOPEPuntuale(selectorCodice, selectorDescrizione, selectorHiddenUid, selectorHiddenDescrizione, selectorHiddenTipo, url) {
        var campoCodice = $(selectorCodice);
        var codice = campoCodice.val();
        var campoDescrizione = $(selectorDescrizione);
        var hiddenUid = $(selectorHiddenUid);
        var hiddenDescrizione = $(selectorHiddenDescrizione);
        var hiddenTipo = $(selectorHiddenTipo);
        if(!codice) {
            campoDescrizione.html('');
            hiddenDescrizione.val('');
            hiddenUid.val('');
            hiddenTipo.val('');
            return $.Deferred().reject().promise();
        }

        campoCodice.overlay('show');
        return $.postJSON(url, {'classificatore.codice': codice})
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'))) {
                campoDescrizione.html('');
                hiddenDescrizione.val('');
                hiddenUid.val('');
                hiddenTipo.val('');
                return;
            }
            campoDescrizione.html(data.classificatore && data.classificatore.descrizione);
            hiddenDescrizione.val(data.classificatore && data.classificatore.descrizione);
            hiddenUid.val(data.classificatore && data.classificatore.uid);
            hiddenTipo.val(data.classificatore && data.classificatore.tipoClassificatore && data.classificatore.tipoClassificatore.codice || '');
        }).always(campoCodice.overlay.bind(campoCodice, 'hide'));
    }
    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     */
    exports.cercaCapitolo = function() {
        var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
        var tipoCapitolo = $("input[name='specificaCodifiche.tipoCapitolo']:checked").val();
        var tipoCapitoloSuffix = (tipoCapitolo === "Uscita" ? "Spesa": "Entrata");
        var annoCapitolo = $("#annoCapitolo").val();
        var numeroCapitolo = $("#numeroCapitolo").val();
        var numeroArticolo = $("#numeroArticolo").val();
        var ricercaPuntualeUrl = $("#siope" + tipoCapitoloSuffix).data('ricercaUrl');
        // Vari alert
        var alertErrori = $("#ERRORI");
        var alertMessaggi = $("#MESSAGGI");
        var alertInformazioni = $("#INFORMAZIONI");
            // Array degli errori
        var erroriArray = [];
            // Wrapper per l'injezione di tipo capitolo e tipo applicazione
        var stringaTipoApplicazione = (tipoApplicazione === "PREVISIONE" ? "Previsione": "Gestione");
        var wrapTipoCapitoloApplicazione = tipoCapitolo + stringaTipoApplicazione;
            // Dati per la creazione della chiamata AJAX
        var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
        var tipologiaCapitolo;
            // Spinner
        var spinner = $("#SPINNER_CapitoloSorgente");

        var oggettoPerChiamataAjaxRicerca = {};

        var ajaxSourceRicerca = "ricercaMassivaVariazioneCodificheCap" + wrapTipoCapitoloApplicazione + ".do";

        // Imposto il tipo di capitolo per lo Ztree
        Variazioni.ZTree.tipoCapitolo = tipoCapitolo;

        // Nascondo il div e i fieldset
        $("#divRicercaCapitolo").slideUp();
        $("#fieldsetCapitoloUscita").slideUp();
        $("#fieldsetCapitoloEntrata").slideUp();

        // Controllo che i campi siano compilati correttamente
        if(annoCapitolo === "") {
            erroriArray.push("Il campo Anno deve essere compilato");
        }
        if(numeroCapitolo === "" || !$.isNumeric(numeroCapitolo)) {
            erroriArray.push("Il campo Capitolo deve essere compilato");
        }
        if(numeroArticolo === "" || !$.isNumeric(numeroArticolo)) {
            erroriArray.push("Il campo Articolo deve essere compilato");
        }
        if(tipoCapitolo === undefined) {
            erroriArray.push("Il tipo di capitolo deve essere selezionato");
        }

        // Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
        if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
            return;
        }
        // La validazione JavaScript è andata a buon fine. Proseguire
        alertErrori.slideUp();

        tipologiaCapitolo = "CAPITOLO_" + tipoCapitolo.toUpperCase() + "_" + stringaTipoApplicazione.toUpperCase();

        oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
        oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
        oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
        oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".tipoCapitolo"] = tipologiaCapitolo;
        oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";

        spinner.addClass("activated");

        /*
         * Le chiamate AJAX vengono effettuate il cascata secondo lo schema:
         *
         * 1. ricerca capitolo
         * 2. controllo modificabilita' campi e caricamento classificatori
         */

        // Chiamata AJAX per la ricerca
        $.postJSON(
            ajaxSourceRicerca,
            oggettoPerChiamataAjaxRicerca,
            function(data) {
                var elementoCapitoloCodifiche = data.elementoCapitoloCodifiche;

                // Controllo degli errori
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    spinner.removeClass("activated");
                    return;
                }
                if(impostaDatiNegliAlert(data.messaggi, alertMessaggi)) {
                    spinner.removeClass("activated");
                    return;
                }
                if(impostaDatiNegliAlert(data.informazioni, alertInformazioni)) {
                    spinner.removeClass("activated");
                    return;
                }

                if(!elementoCapitoloCodifiche || $.isEmptyObject(elementoCapitoloCodifiche)) {
                    erroriArray.push("Nessun elemento trovato");
                    impostaDatiNegliAlert(erroriArray, alertErrori);
                    spinner.removeClass("activated");
                    return;
                }
                // Popolo i dati per la visualizzazione
                $("#annoCapitoloTrovato").html(elementoCapitoloCodifiche.annoCapitolo);
                $("#numeroCapitoloArticoloTrovato").html(elementoCapitoloCodifiche.numeroCapitolo + " / " + elementoCapitoloCodifiche.numeroArticolo);
                $("#HIDDEN_uidElementoCapitoloCodifiche" + tipoCapitolo).val(elementoCapitoloCodifiche.uid);

                // Chiamo il servizio per il caricamento delle codifiche
                // NON passo l'elemento, altrimenti spacco il Tomcat
                caricaCodifiche(elementoCapitoloCodifiche, tipoCapitolo, true).always(function() {
                    // Chiudo lo spinner al termine di QUESTA chiamata AJAX
                    spinner.removeClass("activated");
                });
                
                //Imposto la ricerca della siope
                RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siope', '#descrizioneSiope', '#HIDDEN_idSiope','#HIDDEN_descrizioneSiope','#HIDDEN_SIOPECodiceTipoClassificatore',tipoCapitoloSuffix);
                
                $('#siope' + tipoCapitoloSuffix).substituteHandler('change', gestioneSIOPEPuntuale.bind(null, '#siope'+ tipoCapitoloSuffix, '#descrizioneSiope' + tipoCapitoloSuffix, '#HIDDEN_idSiope' + tipoCapitoloSuffix,
                        '#HIDDEN_descrizioneSiope' + tipoCapitoloSuffix, '', ricercaPuntualeUrl));
                
                // Imposto l'azione collegata al pulsante di salvataggio
                $("#bottoneInserisciModifica").off().on("click", function(event) {
                    event.preventDefault();
                    inserisciVariazione();
                });
            }
        );
    };
    
   /**
     * Elimina la variazione dalla lista.
     */
    exports.eliminaVariazione = function() {
        // Valore mantenuto nella chiusura
        var uidElementoDaEliminare = $("input[name='uid']:checked").val();
        var pulsanteCancella = $("#modaleEliminazionePulsanteCancella");
        var pulsanteProsegui = $("#modaleEliminazionePulsanteProsegui");
        var modaleEliminazione = $("#modaleEliminazione");

        // Chiudo eventualmente il collapse dei classificatori
        //$("#collapseGestioneCodifiche").collapse("hide");
        $("#collapseGestioneCodifiche").is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');

        // Controllo di aver effettivamente selezionato un elemento
        if(!uidElementoDaEliminare) {
            impostaDatiNegliAlert(["Necessario selezionare elemento da eliminare"], $("#ERRORI"));
            return;
        }
        // Imposto le azioni sui pulsanti
        pulsanteCancella.on("click", function() {
            // Cancello il valore selezionato
            uidElementoDaEliminare = undefined;
            pulsanteCancella.off("click");
            pulsanteProsegui.off("click");
        });
        pulsanteProsegui.on("click", function() {
            var spinner = $("#modaleEliminazioneSpinner");
            var oggettoPerChiamataAjax = {};

            oggettoPerChiamataAjax = qualify({uid: uidElementoDaEliminare}, "specificaCodifiche.elementoCapitoloCodifiche");

            spinner.addClass("activated");
            // Chiamata AJAX
            $.postJSON(
                "eliminaCapitoloVariazioneCodifiche.do",
                oggettoPerChiamataAjax,
                function(data) {
                    if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                        return;
                    }
                    if(impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"))) {
                        return;
                    }
                    if(impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI"))) {
                        return;
                    }
                    // L'eliminazione è stata effettuata: ricarico il dataTable
                    popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);

                    // Nascondo il modale
                    modaleEliminazione.modal("hide");
                    $("#divRicercaCapitolo").slideUp();
                }
            ).always(function() {
                spinner.removeClass("activated");
            });
            // Slego le funzioni ai pulsanti, sì che non si possa cercare di rieliminare l'elemento
            pulsanteCancella.off("click");
            pulsanteProsegui.off("click");
        });
    };

    /**
     * Effettua una nuova ricerca
     */
    exports.nuovaRicerca = function() {
        // Pulisco il form
        $("#divRicercaCapitolo")
            .find("fieldset")
                // Pulisco i campi input e torno al selettore padre
                .find(":input").val("").end()
            // Nascondo i fieldset e torno al selettore padre
            .slideUp().end()
        // Nascondo il div
        .slideUp();
        // Pulisco il radio del tipo di capitolo
        $("input[name='specificaCodifiche.tipoCapitolo']").removeProp("checked");
        // Pulisco i campi del capitolo
        $("#numeroCapitolo").val("");
        $("#numeroArticolo").val("");

        // Chiudo eventualmente il collapse dei classificatori
        //$("#collapseGestioneCodifiche").collapse("hide");
        $("#collapseGestioneCodifiche").is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');
    };

    /**
     * Aggiorna la variazione
     */
    exports.aggiornaVariazione = function() {
        // Valore mantenuto nella chiusura
        var uidElementoDaAggiornare = $("input[name='uid']:checked").val();
        var spinnerAggiorna = $("#pulsanteAggiornamentoSpinner");
        var oggettoPerChiamataAjax = {};
        var collapseGestioneCodifiche = $("#collapseGestioneCodifiche");

        // Chiudo eventualmente il collapse dei classificatori
        //collapseGestioneCodifiche.is(".in") && collapseGestioneCodifiche.collapse("hide");
        collapseGestioneCodifiche.is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');
        // Controllo di aver effettivamente selezionato un elemento
        if(!uidElementoDaAggiornare) {
            impostaDatiNegliAlert(["Necessario selezionare elemento da aggiornare"], $("#ERRORI"));
            return;
        }

        oggettoPerChiamataAjax = qualify({uid: uidElementoDaAggiornare}, "specificaCodifiche.elementoCapitoloCodifiche");

        spinnerAggiorna.addClass("activated");

        // Chiamo il servizio
        $.postJSON(
            "aggiornaCapitoloVariazioneCodifica.do",
            oggettoPerChiamataAjax,
            function(data) {
                var elementoCapitoloCodifiche = data.specificaCodifiche.elementoCapitoloCodifiche;
                var tipoCapitoloArray; 
                var tipoCapitolo; 
                
                // Nascondo i fieldset
                $("#fieldsetCapitoloUscita").slideUp();
                $("#fieldsetCapitoloEntrata").slideUp();

                if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                    spinnerAggiorna.removeClass("activated");
                    return;
                }
                if(impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"))) {
                    spinnerAggiorna.removeClass("activated");
                    return;
                }
                if(impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI"))) {
                    spinnerAggiorna.removeClass("activated");
                    return;
                }
                
                tipoCapitoloArray = elementoCapitoloCodifiche.tipoCapitolo.split("_");
                tipoCapitolo = tipoCapitoloArray[tipoCapitoloArray.length - 2].toLowerCase().capitalize();
                // Imposto i dati nella ricerca, ma non effettuo la ricerca
                $("#tipoCapitoloRadio" + tipoCapitolo).attr("checked", "checked");
                $("#numeroCapitolo").val(elementoCapitoloCodifiche.numeroCapitolo);
                $("#numeroArticolo").val(elementoCapitoloCodifiche.numeroArticolo);
                
                $("#HIDDEN_uidElementoCapitoloCodifiche" + tipoCapitolo).val(elementoCapitoloCodifiche.uid);
                
                $("#annoCapitoloTrovato").html(elementoCapitoloCodifiche.annoCapitolo);
                $("#numeroCapitoloArticoloTrovato").html(elementoCapitoloCodifiche.numeroCapitolo + " / " + elementoCapitoloCodifiche.numeroArticolo);

                Variazioni.ZTree.tipoCapitolo = tipoCapitolo;

                // Chiamo il servizio per il caricamento delle codifiche
                caricaCodifiche(elementoCapitoloCodifiche, tipoCapitolo, false).always(function() {
                    // Chiudo lo spinner al termine di QUESTA chiamata AJAX
                    spinnerAggiorna.removeClass("activated");
                    $("#divRicercaCapitolo").slideDown()
                        .promise()
                        .then(function() {
                            $("#linkGestioneCodifiche").click();
                        });
                });


                $("#bottoneInserisciModifica").off().on("click", function(event) {
                    event.preventDefault();
                    aggiornamentoVariazione();
                });
            }
        );
    };

    /**
     * Legge i capitoli presenti nella variazione.
     */
    exports.leggiCapitoliNellaVariazione = function() {
        $.postJSON(
            "leggiCapitoliNellaVariazioneCodifiche.do",
            {},
            function(data) {
                popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);
            }
        );
    };

    return exports;
} (Variazioni || {}));

$(
    function() {
        // Lego gli handler ai pulsanti
        $("#BUTTON_ricercaCapitolo").on("click", Variazioni.cercaCapitolo);
        $("#pulsanteEliminaCodifica").on("click", Variazioni.eliminaVariazione);
        $("#pulsanteNuovaCodifica").on("click", Variazioni.nuovaRicerca);
        $("#pulsanteAggiornaCodifica").on("click", Variazioni.aggiornaVariazione);

        // Lego gli handler per il cambiamento di selezione sulle select
        $("#missioneCapitoloUscita").on("change", Variazioni.Classificatori.caricaProgramma);
        $("#programmaCapitoloUscita").on("change", Variazioni.Classificatori.caricaClassificazioneCofog);
        $("#titoloSpesaCapitoloUscita").on("change", Variazioni.Classificatori.caricaMacroaggregato);
        $("#macroaggregatoCapitoloUscita").on("change", function() {
            // Se non si definisce la funzione anonima attorno, viene passato come parametro alla funzione interna l'evento
            Variazioni.ZTree.caricaPianoDeiConti();
        });

        $("#titoloEntrataCapitoloEntrata").on("change", Variazioni.Classificatori.caricaTipologiaTitolo);
        $("#tipologiaTitoloCapitoloEntrata").on("change", Variazioni.Classificatori.caricaCategoriaTipologiaTitolo);
        $("#categoriaTipologiaTitoloCapitoloEntrata").on("change", function() {
            // Se non si definisce la funzione anonima attorno, viene passato come parametro alla funzione interna l'evento
            Variazioni.ZTree.caricaPianoDeiConti();
        });
       
       // Carico i capitoli già presenti nella variazione
        Variazioni.leggiCapitoliNellaVariazione();
    }
);