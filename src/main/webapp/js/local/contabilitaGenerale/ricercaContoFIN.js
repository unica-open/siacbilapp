/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($,global) {
    "use strict";
    var exports = {};
    var ContoFIN;
    var alertErroriModale = $("#ERRORI_MODALE_CONTOFIN");

    var zTreeSettingsBase = {
        check: {
            enable: true,
            chkStyle: "radio",
            radioType: "all"
        },
        data: {
            key: {
                name: "testo",
                children: "sottoElementi"
            },
            simpleData: {
                enable: true
            }
        }
    };

    /**
     * Controlla se la select sia vuota.
     */
    function selectVuota (objectVal) {
        return !objectVal;
    }

    /**
     * Deseleziona l'elemento, se selezionato.
     *
     * @param zTreeId {String} l'id univoco dello zTree
     */
    function deseleziona(zTreeId) {
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree && tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }

    /**
     * Costruttore per il conto.

     * @param selectorcodiceConto (String) il selettore CSS del codice del conto
     */
     ContoFIN = function(selectorcodiceConto) {
        // Campi della pagina

        this.$codice = $(selectorcodiceConto);

        // I campi del modale
        this.$fieldsetRicerca = $("#fieldsetModaleRicercaContoFIN");
        this.$campiSpesa=$("#campiSpesaFIN");
        this.$campiEntrata=$("#campiEntrataFIN");
        this.$titoloSpesaModale = $("#titoloSpesaFIN");
        this.$macroaggregatoModale = $("#macroaggregatoFIN");
        this.$titoloEntrataModale = $("#titoloEntrataFIN");
        this.$tipologiaTitoloModale = $("#tipologiaTitoloFIN");
        this.$categoriaTipologiaTitoloModale = $("#categoriaTipologiaTitoloFIN");

        this.$spinnerRicerca = $("#spinnerModaleRicercaPDCFIN");
        this.$bottoneCercaModaleRicercaPDCModale = $("#bottoneCercaModaleRicercaPDCFIN");
        this.$pulsanteConfermaModaleRicercaConto = $("#pulsanteConfermaModaleRicercaContoFIN");
        this.$pulsanteDeselezionaModaleRicercaConto = $("#pulsanteDeselezionaModaleRicercaContoFIN");
        this.$divRisultatiContoFIN= $("#divRisultatiContoFIN");
        this.$treePDCFIN= $("#treePDCFIN");
        this.$modal = $("#comp-codContoFIN");

        this.init();
    };

    /**
     * Gestione degli errori per il conto finanziario
     */
    ContoFIN.prototype.gestioneErrori = function () {
        var errori = [];
        if ((!$("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (!$("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaContoFIN").is(":checked"))) {
            errori.push("Necessario selezionare il tipo di conto");
        }
        if (($("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (selectVuota(this.$categoriaTipologiaTitoloModale.val()))) {
            errori.push("Necessario selezionare la categoria");
        }
        if ( ($("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (selectVuota(this.$macroaggregatoModale.val()))) {
            errori.push("Necessario selezionare il macroaggregato");
        }
        impostaDatiNegliAlert(errori, alertErroriModale, false);
    };

    /**
     * Apertura del modale
     */
    ContoFIN.prototype.apriModale = function() {

        //svuoto i campi
        this.$fieldsetRicerca.find(":input").val("").removeAttr('checked');

        this.$titoloSpesaModale.val("");
        this.$macroaggregatoModale.val("");
        this.$titoloEntrataModale.val("");
        this.$tipologiaTitoloModale.val("");
        this.$categoriaTipologiaTitoloModale.val("");
        this.svuotaPDC();

        // Nascondo i campi
        alertErroriModale.slideUp();
        this.$campiEntrata.hide();
        this.$campiSpesa.hide();
        this.$divRisultatiContoFIN.hide();

        this.$modal.modal("show");
    };

    /**
     * Gestione del macroaggregato.
     */
    ContoFIN.prototype.gestisciMacroaggregato = function() {
        if (selectVuota(this.$titoloSpesaModale.val())) {
            this.$macroaggregatoModale.val("");
            this.$macroaggregatoModale.attr("disabled", true);
        } else {
            this.$macroaggregatoModale.removeAttr("disabled");
            this.caricaMacroAgregato();
        }
    };

    /**
     * Gestione della tipologia.
     */
    ContoFIN.prototype.gestisciTipologia = function() {
        if (selectVuota(this.$titoloEntrataModale.val())) {
            this.$tipologiaTitoloModale.val ("");
            this.$tipologiaTitoloModale.attr("disabled", true);
            this.$categoriaTipologiaTitoloModale.val("");
            this.$categoriaTipologiaTitoloModale.attr("disabled", true);
        } else {
            this.$tipologiaTitoloModale.removeAttr("disabled");
            this.caricaTipologia();
        }
    };

    /**
     * Gestione della categoria
     */
    ContoFIN.prototype.gestisciCategoria = function() {
        if (selectVuota(this.$tipologiaTitoloModale.val())) {
            this.$categoriaTipologiaTitoloModale.val("");
            this.$categoriaTipologiaTitoloModale.attr("disabled", true);
        } else {
            this.$categoriaTipologiaTitoloModale.removeAttr("disabled");
            this.caricaCategoria();
        }
    };

    /**
     * Gestione delle entrate e delle spese.
     */
    ContoFIN.prototype.gestioneEntrateSpese = function() {
        if ($("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaContoFIN").is(":checked")){
            this.$campiSpesa.show();
            this.$titoloSpesaModale.removeAttr("disabled");
            if (!selectVuota(this.$titoloSpesaModale.val())) {
                this.$macroaggregatoModale.removeAttr("disabled");
            }
            this.$campiEntrata.hide();
            $(":input[data-campoentrata]", "#fieldsetModaleRicercaContoFIN").attr("disabled", true);

            // Esco
            return;
        }
        if ($("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaContoFIN").is(":checked")){
            this.$campiSpesa.hide();
            $(":input[data-campospesa]", "#fieldsetModaleRicercaContoFIN").attr("disabled", true);
            this.$campiEntrata.show();
            this.$titoloEntrataModale.removeAttr("disabled");
            if (!selectVuota(this.$titoloEntrataModale.val())) {
                this.$tipologiaTitoloModale.removeAttr("disabled");
                if (!selectVuota(this.$tipologiaTitoloModale.val())) {
                    this.$categoriaTipologiaTitoloModale.removeAttr("disabled");
                }
            }

            // Esco
            return;
        }
    };

    /**
     * Svuoto il PDC.
     */
    ContoFIN.prototype.svuotaPDC = function () {
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
        $("#HIDDEN_ElementoPianoDeiContiUidFIN").val("");
        $("#HIDDEN_ElementoPianoDeiContiStringaFIN").val("");
        $("#HIDDEN_ElementoPianoDeiContiCodiceFIN").val("");
        $("#SPAN_ElementoPianoDeiContiFIN").html("Nessun P.d.C. finanziario selezionato");
    };

    /* ***** Funzioni per le chiamate AJAX *****/
    /**
     * Carica i dati nella select della Tipologia da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    ContoFIN.prototype.caricaTipologia = function () {
        var idTitoloEntrata = this.$titoloEntrataModale.val();
        // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti

        //Pulisco la select della categoria
        this.$categoriaTipologiaTitoloModale.val("");

        return $.postJSON(
            "ajax/tipologiaTitoloAjax.do",
            {"id" : idTitoloEntrata},
            function (data) {
                var listaTipologiaTitolo = (data.listaTipologiaTitolo);
                var errori = data.errori;
                var options = $("#tipologiaTitoloFIN");
                var selectCategoria = $("#categoriaTipologiaTitoloFIN");
                var bottonePdC = $("#bottonePdCFIN");

                options.find('option').remove().end();

                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    selectCategoria.attr("disabled", "disabled");
                    bottonePdC.attr("disabled", "disabled");
                    return;
                }
                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                if (selectCategoria.attr("disabled") !== "disabled") {
                    selectCategoria.attr("disabled", "disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }

                options.append($("<option />").val("").text(""));
                $.each(
                    listaTipologiaTitolo,
                    function () {
                        options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
                    }
                );
            }
        );
    };
    /**
     * Carica i dati nella select della Categoria da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    ContoFIN.prototype.caricaCategoria = function () {
        var idTipologiaTitolo = this.$tipologiaTitoloModale.val();

        this.svuotaPDC();

        return $.postJSON(
            "ajax/categoriaTipologiaTitoloAjax.do",
            {"id" : idTipologiaTitolo},
            function (data) {
                var listaCategoriaTipologiaTitolo = (data.listaCategoriaTipologiaTitolo);
                var errori = data.errori;
                var options = $("#categoriaTipologiaTitoloFIN");
                var bottonePdC = $("#bottonePdCFIN");

                options.find('option').remove().end();

                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    return;
                }

                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }

                options.append($("<option />").val("").text(""));
                $.each(
                    listaCategoriaTipologiaTitolo,
                    function () {
                        options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
                    }
                );
            }
        );
    };
    /**
     * Carica i dati nella select del Macroaggregato da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    ContoFIN.prototype.caricaMacroaggregato = function () {
        var idTitoloSpesa = this.$titoloSpesaModale.val();

        this.svuotaPDC();

        // Effettuo la chiamata JSON
        return $.postJSON(
            "ajax/macroaggregatoAjax.do",
            {"id" : idTitoloSpesa},
            function (data) {
                var listaMacroaggregato = (data.listaMacroaggregato);
                var errori = data.errori;
                var options = $("#macroaggregatoFIN");
                var bottonePdC = $("#bottonePdCFIN");

                options.find('option').remove().end();
                if(errori.length > 0) {
                    options.attr("disabled", "disabled");
                    return;
                }

                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                if (bottonePdC.attr("disabled") !== "disabled") {
                    bottonePdC.attr("disabled", "disabled");
                }

                options.append($("<option />").val("").text(""));
                $.each(
                    listaMacroaggregato,
                    function () {
                        options.append($("<option />").val(this.uid).text(this.codice + '-' + this.descrizione));
                    }
                );
            }
        );
    };


    /**
     * Prepara i parametri per la Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     *
     * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    ContoFIN.prototype.gestisciPDC = function () {
         alertErroriModale.slideUp();
         if (($("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (!selectVuota(this.$categoriaTipologiaTitoloModale.val()))) {
             this.caricaPianoDeiConti (this.$categoriaTipologiaTitoloModale);

         }
         if ( ($("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (!selectVuota(this.$macroaggregatoModale.val()))) {
             this.caricaPianoDeiConti (this.$macroaggregatoModale);

         }
         if (!(($("input[type='radio'][data-entrata]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (!selectVuota(this.$categoriaTipologiaTitoloModale.val()))) &&
         !( ($("input[type='radio'][data-spesa]", "#fieldsetModaleRicercaContoFIN").is(":checked")) && (!selectVuota(this.$macroaggregatoModale.val()))) ) {
            this.gestioneErrori();
         }
    };



    /**
     * Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     * @param obj             {Object}  l'oggetto chiamante
     *
     * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    ContoFIN.prototype.caricaPianoDeiConti = function (obj) {
        var id = obj.val();
        /* Settings dello zTree */
        var zTreeSettings = $.extend(true, {}, zTreeSettingsBase, {callback: {beforeCheck: controllaLivelloPianoDeiConti, onCheck: impostaValueElementoPianoDeiConti}});
        /* Spinner */
        var spinner = this.$spinnerRicerca;
        var self = this;

        /* Attiva lo spinner */
        spinner.addClass("activated");

        this.svuotaPDC();

        return $.postJSON(
            "ajax/elementoPianoDeiContiAjax.do",
            {"id" : id},
            function (data) {
                var listaElementoPianoDeiConti = (data.listaElementoCodifica);
                var options = $("#bottonePdCFIN");
                var codiceTitolo;
                var isCodiceTitoloImpostato;

                if (!selectVuota(self.$titoloEntrataModale.val())) {
                    codiceTitolo = self.$titoloEntrataModale.val();
                    isCodiceTitoloImpostato = !!codiceTitolo;
                }
                if (!selectVuota(self.$titoloSpesaModale.val())) {
                    codiceTitolo = self.$titoloEntrataModale.val();
                    isCodiceTitoloImpostato = !!codiceTitolo;
                }
                $("#divRisultatiContoFIN").show();
                impostaZTree("treePDCFIN", zTreeSettings, listaElementoPianoDeiConti);
                // Se il bottone è disabilitato, lo si riabiliti
                if (options.attr("disabled") === "disabled") {
                    options.removeAttr("disabled");
                }
                $("#PDCfinResult").collapse("show");

            }
        ).always(
                function() {
                    // Disattiva lo spinner anche in caso di errore
                    spinner.removeClass("activated");
                }
        );
    };

    /* ***** zTree *****/

    /**
     * Funzione per la creazione di una stringa contenente le informazioni gerarchiche della selezione. Genera solo il codice degli elementi non-foglia.
     *
     * @param treeNode       {Object}  il nodo selezionato
     * @param messaggioVuoto {String}  messaggio da impostare qualora non sia stato selezionato nulla
     * @param regressione    {Boolean} indica se effettuare una regressione sui vari codici
     *
     * @returns {String} la stringa gerarchica creata
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        var nodes = treeNode;
        var parent;
        var string;

        if (!nodes.checked) {
            return messaggioVuoto;
        }
        if (!regressione) {
            return nodes.testo;
        }

        string = nodes.testo;
        parent = nodes.getParentNode();
        while (parent !== null) {
            nodes = parent;
            string = nodes.codice + " - " + string;
            parent = nodes.getParentNode();
        }

        return string;
    }

    /**
     * Funzione per l'impostazione dello zTree.
     *
     * @param idList       {String} l'id dell'elemento in cui sar&agrave; posto lo zTree
     * @param setting      {Object} le impostazioni dello zTree
     * @param jsonVariable {Object} la variabile con i dati JSON per il popolamento dello zTree
     */
    function impostaZTree(idList, setting, jsonVariable) {
        var tree = $.fn.zTree.init($("#" + idList), setting, jsonVariable);
        var idCampo = "ElementoPianoDeiConti";
        var uid = parseInt($("#HIDDEN_" + idCampo + "UidFIN").val(), 10);
        var node;

        // Se l'uid è selezionato l'elemento corrispondente
        if(!isNaN(uid)) {
            node = tree.getNodeByParam("uid", uid);
            // Evito il check nel caso in cui il nodo sia null
            !!node && tree.checkNode(node, true, true, true);
        }
    }

    /**
     * Metodo di utilit&agrave; per l'impostazione dei dati da zTree a un campo hidden.
     *
     * @param treeNode      {Object} l'oggetto JSON corrispondente alla selezione
     * @param idCampoHidden {String} l'id del campo hidden, senza la sotto-stringa HIDDEN_
     * @param stringa       {String} la stringa contenente la descrizione estesa (ovvero comprendente la descrizione degli elementi superiori in gerarchia) della selezione
     */
    function valorizzaCampi(treeNode, idCampoHidden, stringa) {
        if(treeNode.checked) {
            $("#HIDDEN_" + idCampoHidden + "UidFIN").val(treeNode.uid);
            $("#HIDDEN_" + idCampoHidden + "CodiceFIN").val(treeNode.codice);
        } else {
            $("#HIDDEN_" + idCampoHidden + "UidFIN").val("");
            $("#HIDDEN_" + idCampoHidden + "CodiceFIN").val("");
        }

        $("#HIDDEN_" + idCampoHidden + "StringaFIN").val(stringa);
        $("#HIDDEN_" + idCampoHidden + "CodiceTipoClassificatoreFIN").val(treeNode.codiceTipo);
        $("#SPAN_" + idCampoHidden+"FIN").html(stringa);
    }

    /**
     * Calcola il livello del piano dei conti.
     *
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Integer} il livello del piano dei conti
     */
    function calcolaLivelloPianoDeiConti(treeNode) {
        var array = treeNode.codice.split(".");
        var index;
        for(index = 1; index < array.length && array[index] > 0; index++) {/* Empty */}
        return --index;
    }

    /**
     * Controllare che la selezione dell'elemento del Piano dei Conti corrisponda almeno al quarto livello dello stesso.
     *
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     *
     * @returns {Boolean} <code>true</code> qualora il livello selezionato sia valido, e scatena pertanto l'evento onClick;
     *                    <code>false</code> in caso contrario, e inibisce lo scatenarsi dell'evento onclick.
     */
    function controllaLivelloPianoDeiConti(treeId, treeNode) {
        var livello;
        if(treeId === undefined) {
            throw new ReferenceError("Nessun treeId fornito");
        }
        livello = calcolaLivelloPianoDeiConti(treeNode);

        // Se il livello non è almeno pari a 4, segnalo l'errore
        if (livello <= 4) {
            bootboxAlert("Selezionare almeno il quinto livello del Piano dei Conti. Livello selezionato: " + livello);
        }
        return livello > 4;
    }

    /**
     * Imposta il valore dell'Elemento del Piano Dei Conti in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueElementoPianoDeiConti(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessun P.d.C. finanziario selezionato", false);
        valorizzaCampi(treeNode, "ElementoPianoDeiConti", stringa);
    }

    /****fine z-tree ****/

    /**
     * Inizializzazione
     */
    ContoFIN.prototype.init = function() {
        var self = this;
        $("input[type='radio']", "#fieldsetModaleRicercaContoFIN").substituteHandler("change", $.proxy(self.gestioneEntrateSpese, self));

        this.$campiSpesa.hide();
        $(":input[data-campospesa]", "#fieldsetModaleRicercaContoFIN").attr("disabled", true);
        this.$campiEntrata.hide();
        $(":input[data-campoentrata]", "#fieldsetModaleRicercaContoFIN").attr("disabled", true);

        this.$titoloSpesaModale.substituteHandler("change", $.proxy(self.caricaMacroaggregato, self));
        this.$titoloEntrataModale.substituteHandler("change", $.proxy(self.gestisciTipologia, self));
        this.$tipologiaTitoloModale.substituteHandler("change", $.proxy(self.gestisciCategoria, self));
        this.$bottoneCercaModaleRicercaPDCModale.substituteHandler("click", $.proxy(self.gestisciPDC, self));
        this.$pulsanteConfermaModaleRicercaConto.click($.proxy(ContoFIN.prototype.impostaConto, this));
        this.$pulsanteDeselezionaModaleRicercaConto.click($.proxy(ContoFIN.prototype.deselezionaNodo, this));

    };

    /**
     * Deselezione del nodo
     */
    ContoFIN.prototype.deselezionaNodo = function() {
        deseleziona("treePDCFIN");
    };
    /**
     * Imposta i dati della conto all'interno dei campi selezionati.
     *
     * @returns (Conto) l'oggetto su cui e' atata effettuata l'invocazione
     */
    ContoFIN.prototype.impostaConto = function() {
        var codiceVal= $("#HIDDEN_ElementoPianoDeiContiCodiceFIN").val();

        // Se non ho selezionato nulla, esco subito
        if(codiceVal === "") {
            impostaDatiNegliAlert(["Necessario selezionare un conto"], alertErroriModale, false);
            return;
        }
        // Chiudo il modale e distruggo il datatable
        this.$modal.modal("hide");
        //copio il risultato
        this.$codice.val(codiceVal);

    };

    /**
     * Inizializzazione dei campi.
     *
     * @param selectorCodiceConto     (String) il selettore del codice conto
     * @param selectorPulsanteGuidata (String) il selettore del pulsante di compilazione guidata
     */
    exports.inizializza = function(selectorCodiceConto, selectorPulsanteGuidata) {
        var datiContoFIN = new ContoFIN(selectorCodiceConto);
        $(selectorPulsanteGuidata).click($.proxy(ContoFIN.prototype.apriModale, datiContoFIN));
    };

    // Esportazione delle funzionalita'
    global.ContoFIN = exports;

}(jQuery, this);