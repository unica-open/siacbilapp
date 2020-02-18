/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Per l'aggiornamento delle variazioni - variazione di codifiche */

var Variazioni = Variazioni || {};

/** Il nome della funzione da invocare per il popolamento del dataTable */
Variazioni._nome_popolamento_dataTable = undefined;

/** Mappatura nomi dei campi - nomi dei valori ottenuti dall'AJAX */
Variazioni._campi_select = {
    "MISSIONE" : "missione",
    "PROGRAMMA" : "programma",
    "TITOLO_SPESA" : "titoloSpesa",
    "MACROAGGREGATO" : "macroaggregato",
    "CLASSIFICAZIONE_COFOG" : "classificazioneCofog",
    "TITOLO_ENTRATA" : "titoloEntrata",
    "TIPOLOGIA" : "tipologiaTitolo",
    "CATEGORIA" : "categoriaTipologiaTitolo",
    "TIPO_FINANZIAMENTO" : "tipoFinanziamento",
    "TIPO_FONDO" : "tipoFondo",
    "PERIMETRO_SANITARIO_ENTRATA" : "perimetroSanitarioEntrata",
    "PERIMETRO_SANITARIO_SPESA" : "perimetroSanitarioSpesa",
    "TRANSAZIONE_UE_ENTRATA" : "transazioneUnioneEuropeaEntrata",
    "TRANSAZIONE_UE_SPESA" : "transazioneUnioneEuropeaSpesa",
    "POLITICHE_REGIONALI_UNITARIE" : "politicheRegionaliUnitarie"
};

Variazioni._classificatori_generici = {
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

Variazioni._campi_select_enum = {
    //CATEGORIA_CAPITOLO: "categoriaCapitolo"
};

Variazioni._campi_select_codifica = {
        CATEGORIA_CAPITOLO: "categoriaCapitolo"
    };

Variazioni._attributi = {
    "FLAG_ASSEGNABILE" : "assegnabile",
    "FLAG_FONDO_SVALUTAZIONE_CREDITI" : "fondoSvalutazioneCrediti",
    "FLAG_FUNZIONI_DELEGATE" : "funzioniDelegateRegione",
    "FLAG_PER_MEMORIA" : "corsivoPerMemoria",
    "FLAG_RILEVANTE_IVA" : "rilevanteIva",
    "FLAG_TRASFERIMENTO_ORGANI_COMUNITARI" : "trasferimentoOrganiComunitari",
    "FLAG_ENTRATE_RICORRENTI" : "entrateRicorrenti",
    "NOTE" : "note",
    "FLAG_TRASFERIMENTI_VINCOLATI" : "trasferimentiVincolati"
};

Variazioni._descrizioni = {
    "DESCRIZIONE" : "descrizioneCapitolo",
    "DESCRIZIONE_ARTICOLO" : "descrizioneArticolo"
};

Variazioni._flags = {
    "flagCorsivoPerMemoria" : "corsivoPerMemoria",
    "flagFondoSvalutazioneCrediti" : "fondoSvalutazioneCrediti",
    "flagFunzioniDelegateRegione" : "funzioniDelegateRegione",
    "flagRilevanteIva" : "rilevanteIva",
    "flagImpegnabile": "impegnabile"
};

Variazioni._trees = {
    "elementoPianoDeiConti" : ["PDC", "PDC_I", "PDC_II", "PDC_III", "PDC_IV", "PDC_V"],
    "strutturaAmministrativoContabile" : ["CDC", "CDR"],
    "siopeSpesa" : ["SIOPE_SPESA", "SIOPE_SPESA_I", "SIOPE_SPESA_II", "SIOPE_SPESA_III"],
    "siopeEntrata" : ["SIOPE_ENTRATA", "SIOPE_ENTRATA_I", "SIOPE_ENTRATA_II", "SIOPE_ENTRATA_III"]
};

/**
 * Effettua la ricerca del capitolo per creare una variazione.
 */
Variazioni.cercaCapitolo = function() {
    var tipoApplicazione = $("#HIDDEN_tipoApplicazione").val();
    var tipoCapitolo = $("input[name='specificaCodifiche.tipoCapitolo']:checked").val();
    var annoCapitolo = $("#annoCapitolo").val();
    var numeroCapitolo = $("#numeroCapitolo").val();
    var numeroArticolo = $("#numeroArticolo").val();
        // Vari alert
    var alertErrori = $("#ERRORI");
    var alertMessaggi = $("#MESSAGGI");
    var alertInformazioni = $("#INFORMAZIONI");
        // Array degli errori
    var erroriArray = [];
        // Wrapper per l'injezione di tipo capitolo e tipo applicazione
    var stringaTipoApplicazione = (tipoApplicazione === "Previsione" || tipoApplicazione === "PREVISIONE" ? "Previsione" : "Gestione"); /* NUOVO */
    var wrapTipoCapitoloApplicazione = tipoCapitolo + stringaTipoApplicazione; /* NUOVO */
        // Dati per la creazione della chiamata AJAX
    var capitoloDaRichiamare = "capitolo" + wrapTipoCapitoloApplicazione;
    var tipologiaCapitolo; /* NUOVO */
        // Spinner
    var spinner = $("#SPINNER_CapitoloSorgente");

    var oggettoPerChiamataAjaxRicerca = {};

    var ajaxSourceRicerca = "ricercaMassivaVariazioneCodificheCap" + wrapTipoCapitoloApplicazione + ".do";
    
    var collapse = $('#collapseGestioneCodifiche');

    // Imposto il tipo di capitolo per lo Ztree
    Variazioni.ZTree.tipoCapitolo = tipoCapitolo;

    // Nascondo il div e i fieldset
    $("#divRicercaCapitolo").slideUp();
    $("#fieldsetCapitoloUscita").slideUp();
    $("#fieldsetCapitoloEntrata").slideUp();
    if(collapse.is('.in') && collapse.is(':visible')) {
    	$('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');
    }

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

    tipologiaCapitolo = "CAPITOLO_" + tipoCapitolo.toUpperCase() + "_" + stringaTipoApplicazione.toUpperCase(); /* NUOVO */

    oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
    oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
    oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
    oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".tipoCapitolo"] = tipologiaCapitolo; /* NUOVO */
    oggettoPerChiamataAjaxRicerca[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";

    spinner.addClass("activated");

    /*
     * Le chiamate AJAX vengono effettuate il cascata secondo lo schema:
     *
     * 1. ricerca capitolo
     * 2. contorllo modificabilità campi e caricamento classificatori
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
            Variazioni.caricaCodifiche(elementoCapitoloCodifiche, tipoCapitolo, true).always(function() {
                // Chiudo lo spinner al termine di QUESTA chiamata AJAX
                spinner.removeClass("activated");
            });
            
            //Compilazione guidata Siope
            var tipoCapitoloSuffix = (tipoCapitolo === "Uscita" ? "Spesa": "Entrata");
            var ricercaPuntualeUrl = $("#siope" + tipoCapitoloSuffix).data('ricercaUrl');
            RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siope', '#descrizioneSiope', '#HIDDEN_idSiope','#HIDDEN_descrizioneSiope','#HIDDEN_SIOPECodiceTipoClassificatore',tipoCapitoloSuffix);
            $('#siope' + tipoCapitoloSuffix).substituteHandler('change', gestioneSIOPEPuntuale.bind(null, '#siope'+ tipoCapitoloSuffix, '#descrizioneSiope' + tipoCapitoloSuffix, '#HIDDEN_idSiope' + tipoCapitoloSuffix,
                    '#HIDDEN_descrizioneSiope' + tipoCapitoloSuffix, '', ricercaPuntualeUrl));
           

            // Imposto l'azione collegata al pulsante di salvataggio
            $("#bottoneInserisciModifica").off().on("click", function(event) {
                event.preventDefault();
                Variazioni.inserisciVariazione();
            });
        }
    );
};

/**
 * Effettua una chiamata al servizio per il caricamento delle codifiche.
 *
 * @param elementoCapitoloCodifiche l'elemento rispetto al quale cercare le codifiche
 * @param tipoCapitolo              il tipo di capitolo
 *
 * @return l'oggetto Deferred corrispondente all'invocazione
 */
Variazioni.caricaCodifiche = function(elementoCapitoloCodifiche, tipoCapitolo, popolareRequest) {
    var oggettoPerChiamataAjax = {};
    if(popolareRequest) {
        oggettoPerChiamataAjax = qualify($.extend(true, {}, elementoCapitoloCodifiche), "specificaCodifiche.elementoCapitoloCodifiche");
    }
    var ajaxSource = "controlloModificabilitaCaricamentoClassificatoriVariazioneCodifiche_aggiornamento.do";
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

            Variazioni.caricamentoAttributiModificabili(data.specificaCodifiche.mappaAttributiModificabili, tipoCapitolo);

            // Carico le liste delle codifiche sulla GUI
            Variazioni.caricamentoCodifiche(elementoCapitoloCodifiche, data.specificaCodifiche.mappaClassificatori,
                data.specificaCodifiche.mappaClassificatoriModificabili, data.specificaCodifiche.mappaDescrizioniModificabili, tipoCapitolo,
                data.specificaCodifiche.listaCategoriaCapitolo);

            // Mostro i div
            $("#fieldsetCapitolo" + tipoCapitolo).slideDown();
            $("#divRicercaCapitolo").slideDown();
        }
    );
};

/**
 * Carica gli attributi modificabili.
 *
 * @param mappaAttributiModificabili la mappatura degli attributi modificabili
 * @param tipoCapitolo               il tipo di capitolo
 */
Variazioni.caricamentoAttributiModificabili = function(mappaAttributiModificabili, tipoCapitolo) {
    var index;
    var elementoDOM;
    for(index in Variazioni._attributi) {
        if(Variazioni._attributi.hasOwnProperty(index) && (elementoDOM = $("#" + Variazioni._attributi[index] + "Capitolo" + tipoCapitolo)).length > 0) {
            if(!mappaAttributiModificabili[index]) {
                $("#" + Variazioni._attributi[index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                    .find(":input")
                        .attr("disabled", "disabled");
            } else {
                // Ripristino il campo
                $("#" + Variazioni._attributi[index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                    .find(":input")
                        .removeAttr("disabled");
            }
        }
    }
};

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
Variazioni.caricamentoCodifiche = function(elementoCapitoloCodifiche, mappaClassificatori, mappaClassificatoriModificabili, mappaDescrizioniModificabili, tipoCapitolo, listaCategoriaCapitolo) {
    var index;
    var elementoDOM;
    var elementoCapitolo;
    var tipoClassificatore = (tipoCapitolo === "Uscita" ? "Spesa" : "Entrata");
    var classificatoreValido;
    var i;
    var str;

    var siope = elementoCapitoloCodifiche['siope' + tipoClassificatore];
    $('#HIDDEN_idSiope' + tipoClassificatore).val(siope && siope.uid || '');
    $('#siope' + tipoClassificatore).val(siope && siope.codice || '');
    $('#descrizioneSiope' + tipoClassificatore).val(siope && siope.descrizione || '');
    $('#HIDDEN_descrizioneSiope' + tipoClassificatore).val(siope && siope.descrizione || '');
    

    var popolaSelect = function(idx, value) {
        var temp = "<option value='" + value.uid + "' ";
        if(elementoCapitolo && !elementoCapitolo.dataFineValidita && elementoCapitolo.uid === value.uid) {
            temp += "selected ";
        }
        temp += ">" + value.codice + "-" + value.descrizione + "</option>";
        str += temp;
    };

    for(index in Variazioni._campi_select) {
        if(Variazioni._campi_select.hasOwnProperty(index) && (elementoDOM = $("#" + Variazioni._campi_select[index] + "Capitolo" + tipoCapitolo)).length > 0) {
            // Controllo se nascondere il campo
            if(!mappaClassificatoriModificabili[index]) {
                $("#" + Variazioni._campi_select[index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                    .find(":input").attr("disabled", "disabled");
            } else {
                // Ripristino il campo
                $("#" + Variazioni._campi_select[index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                    .find(":input").removeAttr("disabled");
            }
            // Pulisco il select
            elementoDOM.find("option").remove().end();
            // Ottengo l'uid dell'elemento corrispondente
            elementoCapitolo = elementoCapitoloCodifiche[Variazioni._campi_select[index]];
            str = "<option value='0'></option>";
            // Carico il select con i dati ottenuti dalla mappa dei classificatori
            $.each(mappaClassificatori[index], popolaSelect);
            elementoDOM.html(str);
        }
    }

    for(index in Variazioni._campi_select_codifica) {
        if(Variazioni._campi_select_codifica.hasOwnProperty(index) && (elementoDOM = $("#" + Variazioni._campi_select_codifica[index] + "Capitolo" + tipoCapitolo)).length > 0) {


            // Pulisco il select
            elementoDOM.find("option").remove().end();
            // Ottengo l'uid dell'elemento corrispondente
            elementoCapitolo = elementoCapitoloCodifiche[Variazioni._campi_select_codifica[index]];
            str = "<option value='0'></option>";
            // Carico il select con i dati ottenuti dalla listacategoriaa(al momento messo di forza è l'unico)
            $.each(listaCategoriaCapitolo || [], popolaSelect);
            elementoDOM.html(str);
        }
    }
    // Carico i classificatori generici
    for(index in Variazioni._classificatori_generici[tipoCapitolo]) {
        if(Variazioni._classificatori_generici[tipoCapitolo].hasOwnProperty(index)
                && (elementoDOM = $("#" + Variazioni._classificatori_generici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo)).length > 0) {
            // Controllo se nascondere il campo
            if(!mappaClassificatoriModificabili[index]) {
                $("#" + Variazioni._classificatori_generici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide")
                    .find(":input").attr("disabled", "disabled");
            } else {
                // Ripristino il campo
                $("#" + Variazioni._classificatori_generici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").removeClass("hide")
                    .find(":input").removeAttr("disabled");
            }
            // Pulisco il select
            elementoDOM.find("option").remove().end();
            // Ottengo l'uid dell'elemento corrispondente
            elementoCapitolo = elementoCapitoloCodifiche[Variazioni._classificatori_generici[tipoCapitolo][index]];
            str = "<option value='0'></option>";
            // Carico il select con i dati ottenuti dalla mappa dei classificatori
            $.each(mappaClassificatori[index] || [], popolaSelect);
            elementoDOM.html(str);

            // Popolo i label
            elementoCapitolo = mappaClassificatori[index];
            if(elementoCapitolo && elementoCapitolo.length > 0) {
                // Se presente, prendo il primo elemento per ottenere il label
                $("#" + Variazioni._classificatori_generici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Label")
                    .html(elementoCapitolo[0].tipoClassificatore.descrizione);
            } else {
                // Se non presente, nascondo la combo
                $("#" + Variazioni._classificatori_generici[tipoCapitolo][index] + "Capitolo" + tipoCapitolo + "Div").addClass("hide");
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
    for(index in Variazioni._flags) {
        if(Variazioni._flags.hasOwnProperty(index) && (elementoDOM = $("#" + Variazioni._flags[index] + "Capitolo" + tipoCapitolo)).length > 0) {
            if(elementoCapitoloCodifiche[index]) {
                elementoDOM.attr("checked", "cheched");
            } else {
                elementoDOM.removeAttr("checked");
            }
        }
    }

    // Carico i campi testuali
//    $("#descrizioneCapitoloCapitolo" + tipoCapitolo).html(elementoCapitoloCodifiche.descrizioneCapitolo);
    $("#descrizioneCapitoloCapitolo" + tipoCapitolo).val(elementoCapitoloCodifiche.descrizioneCapitolo);
    if(!mappaDescrizioniModificabili.DESCRIZIONE) {
        $("#descrizioneCapitoloCapitolo" + tipoCapitolo + "Div").addClass("hide");
    } else {
        $("#descrizioneCapitoloCapitolo" + tipoCapitolo + "Div").removeClass("hide");
    }

//    $("#descrizioneArticoloCapitolo" + tipoCapitolo).html(elementoCapitoloCodifiche.descrizioneArticolo);
    $("#descrizioneArticoloCapitolo" + tipoCapitolo).val(elementoCapitoloCodifiche.descrizioneArticolo);
    if(!mappaDescrizioniModificabili.DESCRIZIONE_ARTICOLO) {
        $("#descrizioneArticoloCapitolo" + tipoCapitolo + "Div").addClass("hide");
    } else {
        $("#descrizioneArticoloCapitolo" + tipoCapitolo + "Div").removeClass("hide");
    }

    $("#noteCapitolo" + tipoCapitolo).html(elementoCapitoloCodifiche.note);

    // Carico gli zTree
    Variazioni.ZTree.caricaPianoDeiConti(elementoCapitoloCodifiche);
    Variazioni.ZTree.caricaStrutturaAmministrativoContabile(elementoCapitoloCodifiche, mappaClassificatori.CDC);

    // Nascondo i trees
    for(index in Variazioni._trees) {
        if(Variazioni._trees.hasOwnProperty(index)) {
            classificatoreValido = false;
            for(i = 0; i < Variazioni._trees[index].length && !classificatoreValido; i++) {
                //classificatoreValido = classificatoreValido || Variazioni._trees[index][i];
            	classificatoreValido = classificatoreValido ||mappaClassificatoriModificabili[Variazioni._trees[index][i]];
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
};

/**
 * Inserisce la variazione.
 */
Variazioni.inserisciVariazione = function() {
    Variazioni.lavoraConVariazione("inserisciVariazioneCodifica_aggiornamento.do");
};

/**
 * Aggiorna la variazione.
 */
Variazioni.aggiornamentoVariazione = function() {
    Variazioni.lavoraConVariazione("aggiornamentoCapitoloVariazioneCodifica_aggiornamento.do");
};

/**
 * Inserisce la variazione.
 */
Variazioni.lavoraConVariazione = function(ajaxUrl) {
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
            Variazioni.popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);
            // Disabilito il pulsante
            $("#bottoneInserisciModifica").off();
        }
    ).always(function() {
    	$("#collapseGestioneCodifiche").is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');
        spinner.removeClass("activated");
        window.location.hash = "codificheNellaVariazione";
        $("#divRicercaCapitolo").slideUp();
        $("#pulsanteRicercaCapitolo").click();
    });
};

/**
 * Popola il dataTable a partire dalla lista fornita in input.
 *
 * @param lista {Array} la lista presente in input
 */
Variazioni.popolaDataTable = function(lista) {
	var rigaEditabile = !$('form#aggiornamentoVariazioneCodifiche').hasClass('form-disabilitato');
    var options = {
        bPaginate : true,
        bLengthChange : false,
        iDisplayLength : 5,
        bSort : false,
        bInfo : true,
        bAutoWidth : true,
        bFilter : false,
        bProcessing : true,
        aaData : lista,
        bDestroy : true,
        oLanguage : {
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty : "0 risultati",
            sProcessing : "Attendere prego...",
            sZeroRecords : "Non sono presenti variazioni",
            oPaginate : {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec.",
                sEmptyTable : "Nessun dato disponibile"
            }
        },
        aoColumnDefs : [
            {
                aTargets : [ 0 ],
                mData : function(source) {
                	return source.denominazioneCapitolo;                   
                }
              },
            {
                aTargets : [ 1 ],
                mData : function(source) {
                    return source.descrizioneCapitolo;
                }
            },
            {
                aTargets : [ 2 ],
                mData : function(source) {
                    return source.descrizioneArticolo;
                }
            },
            {
                aTargets : [ 3 ],
                mData : function(source) {               	
                	             	
                    var res = "";
                    if(source.strutturaAmministrativoContabile && source.strutturaAmministrativoContabile.codice && source.strutturaAmministrativoContabile.descrizione) {
                        res = source.strutturaAmministrativoContabile.codice + " - " + source.strutturaAmministrativoContabile.descrizione;
                    }
                    return res;
                }
            },            
            {	
            	bVisible: rigaEditabile,
                aTargets : [ 4 ],
                mData : function() {
                    return "<a title='modifica le codifiche' href='#'>" +
                               "<i class='icon-pencil icon-2x'><span class='nascosto'>modifica</span></i>" +
                           "</a>";
                },
                fnCreatedCell : function (nTd, sData, oData) {
                    $(nTd).addClass("text-center")
                        .find("a")
                            .off("click")
                            .on("click", function(event) {
                                event.preventDefault();
                                Variazioni.aggiornaVariazione(oData.uid, $(this));
                            });
                }
            },
            {	
            	bVisible: rigaEditabile,
                aTargets : [ 5 ],
                mData : function() {
                    return "<a title='elimina' href='#'>" +
                               "<i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i>" +
                           "</a>";
                },
                fnCreatedCell : function (nTd, sData, oData) {
                    $(nTd).addClass("text-center")
                        .find("a")
                            .off("click")
                            .on("click", function(event) {
                                event.preventDefault();
                                Variazioni.eliminaVariazione(oData.uid);
                            });
                }
            }
        ]
    };
    // Creo il dataTable
    $("#codificheNellaVariazione").dataTable(options);
};

/**
 * Elimina la variazione dalla lista.
 *
 * @param uid {String} l'uid dell'elemento da eliminare
 */
Variazioni.eliminaVariazione = function(uid) {
    // Valore mantenuto nella chiusura
    var pulsanteCancella = $("#modaleEliminazionePulsanteCancella");
    var pulsanteProsegui = $("#modaleEliminazionePulsanteProsegui");
    var modaleEliminazione = $("#modaleEliminazione");

    // Chiudo eventualmente il collapse dei classificatori
    $("#collapseGestioneCodifiche").is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');

    // Controllo di aver effettivamente selezionato un elemento
    if(!uid) {
        impostaDatiNegliAlert(["Necessario selezionare elemento da eliminare"], $("#ERRORI"));
        return;
    }
    // Imposto le azioni sui pulsanti
    pulsanteCancella.on("click", function() {
        // Cancello il valore selezionato
        pulsanteCancella.off("click");
        pulsanteProsegui.off("click");
    });
    pulsanteProsegui.on("click", function() {
        var spinner = $("#modaleEliminazioneSpinner");
        var oggettoPerChiamataAjax = {};

        oggettoPerChiamataAjax = qualify({"uid" : uid}, "specificaCodifiche.elementoCapitoloCodifiche");

        spinner.addClass("activated");
        // Chiamata AJAX
        $.postJSON(
            "eliminaCapitoloVariazioneCodifiche_aggiornamento.do",
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
                Variazioni.popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);

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

    // Apro il modale
    modaleEliminazione.modal("show");
};

/**
 * Aggiorna la variazione.
 *
 * @param uid {String} l'uid della variazione
 */
Variazioni.aggiornaVariazione = function(uid, pulsanteChiamante) {
    // Valore mantenuto nella chiusura
    var htmlOriginale = pulsanteChiamante.html();
    var oggettoPerChiamataAjax = {};
    var collapseGestione = $("#collapseGestioneCodifiche");

    // Chiudo eventualmente il collapse dei classificatori
    collapseGestione.is(".in") && $('#risultatiRicercaCapitolo a.btn-collapse').trigger('click');

    // Controllo di aver effettivamente selezionato un elemento
    if(!uid) {
        impostaDatiNegliAlert(["Necessario selezionare elemento da eliminare"], $("#ERRORI"));
        return;
    }

    oggettoPerChiamataAjax = qualify({"uid" : uid}, "specificaCodifiche.elementoCapitoloCodifiche");

    // Cambio l'html del pulsante chiamante
    pulsanteChiamante.html('<i class="icon-spin icon-refresh icon-2x spinner" id="spinnerPulsante' + uid + '"></i>');
    $("#spinnerPulsante" + uid).addClass("activated");

    // Chiamo il servizio
    $.postJSON(
        "aggiornaCapitoloVariazioneCodifica_aggiornamento.do",
        oggettoPerChiamataAjax,
        function(data) {
            var elementoCapitoloCodifiche = data.specificaCodifiche.elementoCapitoloCodifiche;
            var tipoCapitoloArray = elementoCapitoloCodifiche.tipoCapitolo.split("_");
            var tipoCapitolo = tipoCapitoloArray[tipoCapitoloArray.length - 2].toLowerCase().capitalize();

            // Imposto i dati nella ricerca, ma non effettuo la ricerca
            $("#tipoCapitoloRadio" + tipoCapitolo).attr("checked", "checked");
            $("#numeroCapitolo").val(elementoCapitoloCodifiche.numeroCapitolo);
            $("#numeroArticolo").val(elementoCapitoloCodifiche.numeroArticolo);

            // Nascondo i fieldset
            $("#fieldsetCapitoloUscita").slideUp();
            $("#fieldsetCapitoloEntrata").slideUp();

            if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                pulsanteChiamante.html(htmlOriginale);
                return;
            }
            if(impostaDatiNegliAlert(data.messaggi, $("#MESSAGGI"))) {
                pulsanteChiamante.html(htmlOriginale);
                return;
            }
            if(impostaDatiNegliAlert(data.informazioni, $("#INFORMAZIONI"))) {
                pulsanteChiamante.html(htmlOriginale);
                return;
            }
            $("#annoCapitoloTrovato").html(elementoCapitoloCodifiche.annoCapitolo);
            $("#numeroCapitoloArticoloTrovato").html(elementoCapitoloCodifiche.numeroCapitolo + " / " + elementoCapitoloCodifiche.numeroArticolo);
            $("#HIDDEN_uidElementoCapitoloCodifiche" + tipoCapitolo).val(elementoCapitoloCodifiche.uid);

            Variazioni.ZTree.tipoCapitolo = tipoCapitolo;

            // Chiamo il servizio per il caricamento delle codifiche
            // NON passo l'elemento, altrimenti spacco il Tomcat
            Variazioni.caricaCodifiche(elementoCapitoloCodifiche, tipoCapitolo, false).always(function() {
                // Chiudo lo spinner al termine di QUESTA chiamata AJAX
                pulsanteChiamante.html(htmlOriginale);
                $("#collapseGestioneCodifiche").collapse("show")
                    .scrollToSelf()
                    .promise()
                    .then(function() {
                        $("#linkGestioneCodifiche").click();
                    });
            });
            
            //Compilazione guidata Siope
            var tipoCapitoloSuffix = (tipoCapitolo === "Uscita" ? "Spesa": "Entrata");
            var ricercaPuntualeUrl = $("#siope" + tipoCapitoloSuffix).data('ricercaUrl');
            RicercaSiope.inizializzazione('#compilazioneGuidataSIOPE', '#siope', '#descrizioneSiope', '#HIDDEN_idSiope','#HIDDEN_descrizioneSiope','#HIDDEN_SIOPECodiceTipoClassificatore',tipoCapitoloSuffix);
            $('#siope' + tipoCapitoloSuffix).substituteHandler('change', gestioneSIOPEPuntuale.bind(null, '#siope'+ tipoCapitoloSuffix, '#descrizioneSiope' + tipoCapitoloSuffix, '#HIDDEN_idSiope' + tipoCapitoloSuffix,
                    '#HIDDEN_descrizioneSiope' + tipoCapitoloSuffix, '', ricercaPuntualeUrl));

            $("#bottoneInserisciModifica").off().on("click", function(event) {
                event.preventDefault();
                Variazioni.aggiornamentoVariazione();
            });
        }
    );
};


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
 * Legge i capitoli presenti nella variazione.
 */
Variazioni.leggiCapitoliNellaVariazione = function() {
    $.postJSON(
        "leggiCapitoliNellaVariazioneCodifiche_aggiornamento.do",
        {},
        function(data) {
            Variazioni.popolaDataTable(data.specificaCodifiche.listaElementoCapitoloCodifiche);
        }
    );
};

/**
 * Inizia una nuova ricerca per la variazione.
 */
Variazioni.nuovaRicerca = function() {
    // Pulisco i campi
    $("input[name='specificaCodifiche.tipoCapitolo']").removeAttr("checked");
    $("#numeroCapitolo").val("");
    $("#numeroArticolo").val("");
};

$(
    function() {

        Variazioni._nome_popolamento_dataTable = "popolaDataTable";

        if(!$('a[href="#collapseProvvedimento"]').hasClass('disabled') && Provvedimento) {
        	Provvedimento.svuotaFormRicerca();
        	Provvedimento.inizializza('Nessun provvedimento selezionato');
        }

        // Lego gli handler ai pulsanti
        $("#BUTTON_ricercaCapitolo").on("click", Variazioni.cercaCapitolo);
        $("#pulsanteRicercaCapitolo").on("click", Variazioni.nuovaRicerca);

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

        // Pulsanti di submit
        $("#pulsanteSalvaAggiornamentoVariazione").on("click", function() {
            $("#divRicercaCapitolo").find(":input").removeAttr("name");
            // Invio il form dopo aver settato correttamente l'azione
            $("#aggiornamentoVariazioneCodifiche").attr("action", "salvaAggiornamentoVariazioneCodifiche.do").submit();
        });
        $("#pulsanteConcludiAggiornamentoVariazione").on("click", function() {
            $("#divRicercaCapitolo").find(":input").removeAttr("name");
            // Invio il form dopo aver settato correttamente l'azione
            $("#aggiornamentoVariazioneCodifiche").attr("action", "concludiAggiornamentoVariazioneCodifiche.do").submit();
        });
        $("#EDIT_annulla").on("click", function() {
            $("#divRicercaCapitolo").find(":input").removeAttr("name");
            // Invio il form dopo aver settato correttamente l'azione
            $("#aggiornamentoVariazioneCodifiche").attr("action", "annullaAggiornamentoVariazioneCodifiche.do").submit();
        });

        // Carico i capitoli già presenti nella variazione
        Variazioni.leggiCapitoliNellaVariazione();

        $("form").on("reset", function() {
            // NATIVO
            this.reset();
        });
    }
);