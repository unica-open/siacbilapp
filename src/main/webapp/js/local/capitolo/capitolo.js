/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *******************************
 **** Funzioni del capitolo ****
 *******************************
 */

//Creo uno scope privato
var Capitolo = (function() {
    // Variabile di esportazione
    var exports = {};
    // Settings base per gli zTree
    var zTreeSettingsBase = {
            check: {enable: true, chkStyle: "radio", radioType: "all"},
            data: {
                key: {name: "testo", children: "sottoElementi"},
                simpleData: {enable: true}
            }
    };
    // Settings per la Struttura Amministrativo Contabile
    var zTreeSettingStrutturaAmministrativoContabile;
    // Settings per il SIOPE
    var zTreeSettingSIOPE;
    // Settings per l'Elemento del Piano dei Conti
    var zTreeSettingElementoPianoDeiConti;
    // Settings per l'Elemento del Piano dei Conti per la ricerca
    var zTreeSettingElementoPianoDeiContiRicerca;
    // Document wrappato in jQuery
    var $d = $(document);

    /**
     * Validazione del form.
     * <br>
     * La funzione sar&agrave; richiamata quando sar&agrave; necessario validare a livello client l'inserimento dei dati all'interno del form.
     * Questa funzione contiene i campi presenti globalmente.
     *
     * @returns l'array degli errori comuni
     */
    exports.validaForm = function (isSpesa) {
        // Definizione dei campi da controllare
        var capitolo = $("#numeroCapitolo").val();
        var articolo = $("#numeroArticolo").val();
        var ueb = $("#numeroUEB").val();
        var descrizione = $("#descrizioneCapitolo").val();
        var pianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiUid").val();
        var strutturaAmministrativoContabile = $("#HIDDEN_StrutturaAmministrativoContabileUid").val();
        var tipoCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        // CR 2204
        var tipoCapitoloStandard = tipoCapitolo === "STD";
        if(!!isSpesa) {
        	tipoCapitoloStandard = tipoCapitoloStandard || tipoCapitolo === 'FPV';
        }

        // definizione del vettore di errori
        var errori = [];

        // Controllo che il dato sia stato inserito
        if (capitolo === "") {
            errori.push("Il campo Capitolo deve essere compilato");
        }
        if (articolo === "") {
            errori.push("Il campo Articolo deve essere compilato");
        }
        if (ueb === "") {
            errori.push("Il campo UEB deve essere compilato");
        }
        if ($.trim(descrizione) === "") {
            errori.push("Il campo Descrizione deve essere compilato");
        }
        if (!tipoCapitoloStandard && (strutturaAmministrativoContabile === "" || strutturaAmministrativoContabile === '0')) {
            errori.push("Il campo Struttura Amministrativo Contabile deve essere compilato");
        }
        if (tipoCapitoloStandard && (pianoDeiConti === "" || pianoDeiConti === '0')) {
            errori.push("Il campo P.d.C. deve essere selezionato");
        }

        // Restituisco il vettore di errori
        return errori;
    };

    /**
     * Gestione degli errori di validazione del form.
     *
     * @param errori {Array} l'array degli errori
     */
    exports.gestioneErrori = function (errori) {
        // Definizione dei campi da popolare
        var modal = $("#ModalAltriDati");
        var alertErrori = $("#alertErrori");
        var erroriList = $("#errori");

        if (errori.length !== 0) {
            // Vi sono degli errori. Impedire la prosecuzione

            // Disabilita l'href dell'apertura del modale
            if ($.type(modal.attr("href")) !== "undefined") {
                modal.removeAttr("href");
            }
            /* Rimuove gli eventuali <li> presenti */
            erroriList.find("li").remove().end();
            $.each(
                    errori,
                    function (key, value) {
                        erroriList.append($("<li/>").html(value));
                    }
            );

            // Mostro l'alert
            if(alertErrori.hasClass("hide")) {
                // Se ho la classe 'hide' la tolgo, sì da visualizzarlo immediatamente
                alertErrori.removeClass("hide").slideDown();
            } else {
                // Se non ho la classe 'hide', sono arrivato qui dal data-hide, dunque ho invocato un $().hide(). Ne chiamo la funzione inversa.
                alertErrori.slideDown();
            }
            setTimeout(function() {
                modal.addClass("collapsed");
            }, 50);

        } else {
            // Se il modale è disattivato, lo riattiva
            if ($.type(modal.attr("href")) === "undefined") {
                modal.attr("href", "#collapseOne");
            }
            // Nasconde l'alert degli errori
            alertErrori.slideUp();
            setTimeout(function() {
                modal.removeClass("collapsed");
            }, 50);
        }
    };

    /**
     * Calcolo dell'Importo di Cassa.
     * <br>
     * Calcolo dell'Importo di Cassa a partire dall'Importo di Stanziamento e dall'Importo residuo secondo la regola
     * <pre>
     * cassa = stanziamento + residuo
     * </pre>
     */
    exports.calcolaImportoCassa = function () {
        var competenza = parseLocalNum($("#stanziamento0").val()) || 0;
        var residui = parseLocalNum($("#residuo0").val()) || 0;
        var cassa = $("#cassa0");
        var valore = (parseFloat(competenza) + parseFloat(residui));
        cassa.val(valore.formatMoney());
    };

    /**
     * Controlla se il modale selezionato sia accessibile.
     *
     * @param idPulsante {String} l'id del pulsante da controllare
     * @param hrefModale {String} l'href del pulsante riferentesi al modale
     */
    function controllaModale(idPulsante, hrefModale) {
        var pulsante = $("#" + idPulsante);

        if ($.type(pulsante.attr("disabled")) !== "undefined" && $.type(pulsante.attr("href")) !== "undefined") {
            pulsante.removeAttr("href");
        } else if ($.type(pulsante.attr("disabled")) === "undefined" && $.type(pulsante.attr("href")) === "undefined") {
            pulsante.attr("href", hrefModale);
        }
    }

    /**
     * Controlla se il modale del PdC sia accessibile.
     * <br>
     * Controlla se il pulsante base per l'apertura del modale per la selezione dell'Elemento del Piano dei Conti sia disabilitato o meno.
     * Nel primo caso, impedisce l'apertura del modale; nel secondo, ripristina (eventualmente) l'apertura del modale.
     */
    exports.controllaPdC = function () {
        controllaModale("bottonePdC", "#myModal");
    };

    /**
     * Controlla se il modale del SIOPE sia accessibile.
     * <br>
     * Controlla se il pulsante base per l'apertura del modale per la selezione del SIOPE sia disabilitato o meno.
     * Nel primo caso, impedisce l'apertura del modale; nel secondo, ripristina (eventualmente) l'apertura del modale.
     */
    exports.controllaSIOPE = function () {
        controllaModale("bottoneSIOPE", "#modaleSIOPE");
    };

    /**
     * Visualizza lo stanziamento per l'anno precedente.
     */
    exports.stanziamentoAnnoPrecedente = function () {
        // Stanziamento
        $("#stanziamentoM1").removeClass("invisible");
        // Residuo
        $("#residuoM1").removeClass("invisible");
        // Cassa
        $("#cassaM1").removeClass("invisible");
        // FondoPluriennaleVincolato
        $("#fpvM1").removeClass("invisible");
    };

    /**
     * Effettua una pulizia completa dei campi al reset.
     */
    exports.puliziaReset = function(e) {
        // Pulisco i field
        e.campi.add("input[type='hidden']").not("[data-maintain]").val("");

        deselezionaEDisattivaPulsante("treePDC");
        deselezionaEDisattivaPulsante("treeSIOPE");
        deseleziona("treeStruttAmm");
    };

    /**
     * Effettua la pulizia del reset per l'aggiornamento. Fallback alla funzionalit&agrave; nativa.
     */
    exports.puliziaResetAggiornamento = function() {
        // NATIVO! Ripristina i campi originali
        this.reset();
        // Ripopolare gli alberi
    };



    /* ***** Chiamate AJAX *****/

    /**
     * Carica i dati nello zTree della Struttura Amministrativo Contabile da chiamata AJAX.
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaStrutturaAmministrativoContabile = function () {
        /* Pulsante per il modale della Struttura Amministrativo Contabile */
        var pulsante = $("#bottoneSAC");
        var spinner = $("#SPINNER_StrutturaAmministrativoContabile");
        /* Non permettere di accedere al modale finché il caricamento non è avvenuto */
        pulsante.removeAttr("href");
        /* Attiva lo spinner */
        spinner.addClass("activated");

        return $.postJSON(
                "ajax/strutturaAmministrativoContabileAjax.do",
                {},
                function (data) {
                    var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
                    impostaZTree("treeStruttAmm", zTreeSettingStrutturaAmministrativoContabile, listaStrutturaAmministrativoContabile);
                    /* Ripristina l'apertura del modale */
                    pulsante.attr("href", "#struttAmm");
                }
        ).always(
                function() {
                    // Disattiva lo spinner anche in caso di errore
                    spinner.removeClass("activated");
                }
        );
    };

    /**
     * Carica i dati nello zTree dell'Elemento del Piano Dei Conti da chiamata AJAX.
     *
     * @param obj             {Object}  l'oggetto chiamante
     * @param daRicerca       {Boolean} definisce se la richiesta dello zTree deriva da una pagina di ricerca
     * @param daDocumentReady {Boolean} definisce se la richiesta arriva dal document.ready
     *
     * @returns (jQuery.Deferred) l'oggetto deferred corrispondente alla chiamata AJAX
     */
    exports.caricaPianoDeiConti = function (obj, daRicerca, daDocumentReady) {
        var id = $(obj).val();
        /* Settings dello zTree */
        var zTreeSettings = (daRicerca ? zTreeSettingElementoPianoDeiContiRicerca : zTreeSettingElementoPianoDeiConti);
        /* Spinner */
        var spinner = $("#SPINNER_ElementoPianoDeiConti");
        /* Caso d'uso */
        // Firefox è l'unico browser (27/11/2013) che abbia una funzione del tipo 'String.contains'. Si replica qui il comportamento
        // Cfr. https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/contains
        var casoDUsoEntrata = (-1 !== String.prototype.indexOf.call($("form").attr("id"), "Entrata"));

        if(daDocumentReady === undefined) {
            // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
            $("#HIDDEN_ElementoPianoDeiContiUid").val("");
            $("#HIDDEN_ElementoPianoDeiContiStringa").val("");
            $("#SPAN_ElementoPianoDeiConti").html("Nessun P.d.C. finanziario selezionato");
        }
        if(+id === 0) {
            //return;
            return $.Deferred().reject().promise();
        }
        /* Attiva lo spinner */
        spinner.addClass("activated");

        return $.postJSON("ajax/elementoPianoDeiContiAjax.do", {"id" : id})
        .then(function (data) {
            var listaElementoPianoDeiConti = (data.listaElementoCodifica);
            var options = $("#bottonePdC");
            var elementoPianoDeiContiGiaSelezionato = $("#HIDDEN_ElementoPianoDeiContiUid").val();
            var codiceTitolo;
            var albero;
            var isCodiceTitoloImpostato;
            var isListaElementoPianoDeiContiNonVuota = listaElementoPianoDeiConti && listaElementoPianoDeiConti.length > 0;

            if(casoDUsoEntrata) {
                codiceTitolo = $("#HIDDEN_codiceTitolo").val();
                isCodiceTitoloImpostato = !!codiceTitolo && codiceTitolo <= 2;
            }

            impostaZTree("treePDC", zTreeSettings, listaElementoPianoDeiConti);
            // Se il bottone è disabilitato, lo si riabiliti
            if (options.attr("disabled") === "disabled" && isListaElementoPianoDeiContiNonVuota) {
                options.removeAttr("disabled");
            }

            // Controllo se sono nel caso d'uso di entrata: in tal caso, effettuo il check del primo elemento nel caso in cui non vi sia un altro elemento selezionato
            if(isCodiceTitoloImpostato && elementoPianoDeiContiGiaSelezionato === "" && isListaElementoPianoDeiContiNonVuota) {
                albero = $.fn.zTree.getZTreeObj("treePDC");
                albero && albero.checkNode(albero.getNodes()[0], true, true, !daDocumentReady);
            }
        }).always(
                function() {
                    // Disattiva lo spinner anche in caso di errore
                    spinner.removeClass("activated");
                }
        );
    };

    /**
     * Carica i dati nello zTree del SIOPE da chiamata AJAX.
     *
     * @param id              {Number}  l'id dell'Elemento del Piano dei Conti padre
     * @param daDocumentReady {boolean} se la chiamata viene effettuata dal Document.ready
     *
     * @returns (jQuery.Deferred) l'oggetto Deferred relativo all'invocazione
     */
    exports.caricaSIOPE = function (id, daDocumentReady) {
        // CR SIAC-2559
        return $.Deferred().resolve().promise();

//        var spinner = $("#SPINNER_SIOPE");
//        var casoDUsoEntrata = (-1 !== String.prototype.indexOf.call($("form").attr("id"), "Entrata"));
//        var urlDaChiamare = casoDUsoEntrata ? "ajax/siopeEntrataAjax.do" : "ajax/siopeSpesaAjax.do";
//        var hiddenSiopeUid = $("#HIDDEN_SIOPEUid");
//
//        /* Attiva lo spinner */
//        spinner.addClass("activated");
//
//        if(!!daDocumentReady) {
//            hiddenSiopeUid.val(hiddenSiopeUid.data("originalUid"));
//        } else {
//         // Pulisco il valore dei campi riferentesi all'elemento del piano dei conti
//            hiddenSiopeUid.val("");
//            $("#HIDDEN_SIOPEStringa").val("");
//            $("#SPAN_SIOPE").html("Nessun SIOPE selezionato");
//        }
//
//        return $.postJSON(
//                urlDaChiamare,
//                {"id" : id},
//                function (data) {
//                    var listaSIOPE = (data.listaElementoCodifica);
//                    var options = $("#bottoneSIOPE");
//                    var zTreeSettings = zTreeSettingSIOPE;
//
//                    impostaZTree("treeSIOPE", zTreeSettings, listaSIOPE);
//                    // Se il bottone è disabilitato, lo si riabiliti
//                    if (options.attr("disabled") === "disabled") {
//                        options.removeAttr("disabled");
//                    }
//                }
//        ).always(
//                function() {
//                    // Disattiva lo spinner anche in caso di errore
//                    spinner.removeClass("activated");
//                }
//        );
    };

    /**
     * Carica il SIOPE a partire dal campo hidden ove si trova l'uid.
     */
    exports.caricaSIOPEDaHidden = function() {
        var tree = $.fn.zTree.getZTreeObj("treeSIOPE");
        var hiddenSiopeUid = $("#HIDDEN_SIOPEUid");
        var uid = parseInt(hiddenSiopeUid.data("originalUid"), 10);
        var node;

        if(!isNaN(uid)) {
            hiddenSiopeUid.val(uid);
            if(tree) {
                node = tree.getNodeByParam("uid", uid);
                // Evito il check nel caso in cui il nodo sia null
                !!node && tree.checkNode(node, true, true, true);
            }
        }
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
        var idCampo = (idList === "treePDC") ? "ElementoPianoDeiConti" :
            ((idList === "treeStruttAmm") ? "StrutturaAmministrativoContabile" : "SIOPE");
        var uid = parseInt($("#HIDDEN_" + idCampo + "Uid").val(), 10);
        var node;
        var idPulsante = "#deseleziona" + idCampo;

        // Lego l'evento di deselezione al pulsante
        $(idPulsante).on("click", function(event) {
            event.preventDefault();
            deseleziona(idList);
        });

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
            $("#HIDDEN_" + idCampoHidden + "Uid").val(treeNode.uid);
        } else {
            $("#HIDDEN_" + idCampoHidden + "Uid").val("");
        }
        $("#HIDDEN_" + idCampoHidden + "Stringa").val(stringa);
        $("#HIDDEN_" + idCampoHidden + "CodiceTipoClassificatore").val(treeNode.codiceTipo);
        $("#SPAN_" + idCampoHidden).html(stringa);
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
        if (livello <= 3) {
            bootboxAlert("Selezionare almeno il quarto livello del Piano dei Conti. Livello selezionato: " + livello);
        }
        return livello > 3;
    }

    /**
     * Imposta il valore della Struttura Amministrativo Contabile in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        var pulsante = $("#deselezionaStrutturaAmministrativaContabile");
        valorizzaCampi(treeNode, "StrutturaAmministrativoContabile", stringa);
        if(treeNode.checked) {
            pulsante.removeAttr("disabled");
        } else {
            pulsante.attr("disabled", "disabled");
        }
    }

    /**
     * Imposta il valore del SIOPE in un campo hidden per la gestione server-side.
     *
     * @param event    {Object} l'evento generato
     * @param treeId   {String} l'id univoco dello zTree
     * @param treeNode {Object} l'oggetto JSON corrispondente alla selezione
     */
    function impostaValueSIOPE(event, treeId, treeNode) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna SIOPE selezionato", true);
        var pulsante = $("#deselezionaSIOPE");
        valorizzaCampi(treeNode, "SIOPE", stringa);
        if(treeNode.checked) {
            pulsante.removeAttr("disabled");
        } else {
            pulsante.attr("disabled", "disabled");
        }
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
        var pulsante = $("#deselezionaElementoPianoDeiConti");
        valorizzaCampi(treeNode, "ElementoPianoDeiConti", stringa);
        if(treeNode.checked) {
            pulsante.removeAttr("disabled");
            // Abilito il pulsante per il SIOPE
            $("#bottoneSIOPE").removeAttr("disabled").attr("href", "#modaleSIOPE");
            // Chiamata per il caricamento del SIOPE
            exports.caricaSIOPE(treeNode.uid);
        } else {
            pulsante.attr("disabled", "disabled");
            // Disabilito il pulsante per il SIOPE
            $("#bottoneSIOPE").removeAttr("href").attr("disabled", "disabled");
        }
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
     * Deseleziona l'elemento, se selezionato.
     *
     * @param zTreeId {String} l'id univoco dello zTree
     */
    function deselezionaEDisattivaPulsante(zTreeId) {
        var ul = $("#" + zTreeId);
        var modal = ul.parents(".modal");
        var anchor = $("a[href='#" + modal.attr("id") + "']");
        deseleziona(zTreeId);
        // Disattivazione del pulsante
        anchor.attr("disabled", true);
    }

    // Per l'aggiornamento: ricaricare le select

    /**
     * Ricarica le select di base (Missione, TitoloSpesa, TitoloEntrata)
     *
     * @param selectId  (String) l'id della select
     * @param evtEmesso (String) il nome dell'evento emesso
     */
    exports.ricaricaSelectBase = function(selectId, evtEmesso) {
        var select = $("#" + selectId);
        if(select.length && !select.is(":disabled")) {
            // Ricarico il valore
            select.val(select.data("originalUid"));
        }
        // Emetto l'evento
        $d.trigger(evtEmesso);
    };

    /**
     * Ricarica la select presente nella gerarchia (Missione -> Programma -> Cofog; TitoloSpesa -> Macroaggregato; TitoloEntrata -> TipologiaTitolo -> Categoria)
     *
     * @param selectId           (String) l'id della select
     * @param selectPadreId      (String) l'id della select padre
     * @param functionDaInvocare (String) il nome della funzione da invocare
     * @param evtAscolto         (String) il nome dell'evento da ascoltare
     * @param evtEmesso          (String) il nome dell'evento emesso
     * @param cap                (Object) il namespace del capitolo da utilizzare (CapitoloUscita / CapitoloEntrata)
     */
    exports.ricaricaSelectGerarchica = function(selectId, selectPadreId, functionDaInvocare, evtAscolto, evtEmesso, cap) {
        // Ascolto l'evento
        $d.on(evtAscolto, function() {
            var select = $("#" + selectId);
            var selectPadre = $("#" + selectPadreId);
            var triggerImmediato = true;
            if(selectPadre.is(":disabled")) {
                // Il padre non era modificabile: ricarico subito il dato
                caricamentoDato();
            } else {
                triggerImmediato = false;
                // Il padre era modificabile: ricarico la select
                cap[functionDaInvocare]()
                    .then(function() {
                        caricamentoDato();
                        $d.trigger(evtEmesso);
                    });
            }

            $d.off(evtAscolto);
            // Se non ho fatto nulla di AJAX, emetto l'evento
            if(triggerImmediato) {
                $d.trigger(evtEmesso);
            }

            function caricamentoDato() {
                select.val(select.data("originalUid"));
            }
        });
    };

    /**
     * Ricarica lo zTree del PianoDeiConti.
     *
     * @param selectId (String) l'id della select padre (Macroaggregato o CategoriaTipologiaTitolo)
     */
    exports.ricaricaTreeElementoPianoDeiConti = function(selectId) {
        $d.on("macroaggregatoCaricato categoriaTipologiaTitoloCaricato", function() {
            var select = $("#" + selectId);
            var editabile = $("#HIDDEN_ElementoPianoDeiContiEditabile").val() === "true";
            var triggerImmediato = true;
            if(editabile) {
                // Se devo caricare via AJAX, non faccio emettere subito l'evento
                triggerImmediato = false;
                caricaDato();
            }

            $d.off("macroaggregatoCaricato categoriaTipologiaTitoloCaricato");
            if(triggerImmediato) {
                $d.trigger("elementoPianoDeiContiCaricato");
            }

            function caricaDato() {
                exports.caricaPianoDeiConti(select, false, true)
                    .then(function() {
                        var albero = $.fn.zTree.getZTreeObj("treePDC");
                        var hiddenField = $("#HIDDEN_ElementoPianoDeiContiUid");
                        var uid = hiddenField.data("originalUid");
                        var node;

                        // Popolo il value del campo hidden
                        hiddenField.val(uid);
                        if(albero) {
                            node = albero.getNodeByParam("uid", uid);
                            node && !node.checked && albero.checkNode(node, true, true, true);
                        }
                        $d.trigger("elementoPianoDeiContiCaricato");
                    });
            }
        });
    };

    /**
     * Ricarica lo zTree del SIOPE.
     */
    exports.ricaricaTreeSiope = function() {
        $d.on("elementoPianoDeiContiCaricato", function() {
            var editabile = $("#HIDDEN_SIOPEEditabile").val() === "true";
            var triggerImmediato = true;
            if(editabile) {
                triggerImmediato = false;
                caricaDato();
            }

            $d.off("elementoPianoDeiContiCaricato");
            if(triggerImmediato) {
                $d.trigger("siopeCaricato");
            }
        });

        function caricaDato() {
            var uidPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiUid").val();
            exports.caricaSIOPE(uidPianoDeiConti, true)
                .then(function() {
                    var albero = $.fn.zTree.getZTreeObj("treeSIOPE");
                    var hiddenField = $("#HIDDEN_SIOPEUid");
                    var uid = hiddenField.data("originalUid");
                    var node;

                    hiddenField.val(uid);
                    if(albero) {
                        node = albero.getNodeByParam("uid", uid);
                        node && !node.checked && albero.checkNode(node, true, true, true);
                    }
                    $d.trigger("siopeCaricato");
                });
        }
    };

    /**
     * Ricarica lo zTree della Struttura Amministrativo Contabile.
     */
    exports.ricaricaTreeStrutturaAmministrativoContabile = function() {
        var editabile = $("#HIDDEN_StrutturaAmministrativoContabileEditabile").val() === "true";
        if(editabile) {
            exports.caricaStrutturaAmministrativoContabile()
                .then(function() {
                    var hiddenField = $("#HIDDEN_StrutturaAmministrativoContabileUid");
                    var uid = hiddenField.data("originalUid");
                    var albero = $.fn.zTree.getZTreeObj("treeStruttAmm");
                    var node;

                    hiddenField.val(uid);
                    if(albero) {
                        node = albero.getNodeByParam("uid", uid);
                        node && !node.checked && albero.checkNode(node, true, true, true);
                    }
                    $d.trigger("strutturaAmministrativoContabileCaricato");
                });
        }
    };

    /** Gestione del flag impegnabile a seconda del tipo di capitolo */
    exports.gestioneFlagImpegnabile = function() {
        var checkbox = $("#flagImpegnabile");
        
        var codiceCategoriaCapitolo;
        var tipoCapitoloFPV;
        var tipoCapitoloSTD;
        if(checkbox.data('editabile') !== undefined && checkbox.data('editabile') !== true) {
            return;
        }
        codiceCategoriaCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        tipoCapitoloFPV = codiceCategoriaCapitolo === "FPV";
        tipoCapitoloSTD = codiceCategoriaCapitolo === "STD";

        checkbox[tipoCapitoloFPV ? "attr" : "removeAttr"]("disabled", true);
        if(tipoCapitoloSTD) {
            checkbox.prop('checked', true);
        }
        if(tipoCapitoloFPV) {
            checkbox.removeProp('checked');
        }
    };

    //START -----> SIAC-6884-VariazioneDecentrate
    /**
     *
     * @param checkboxImpegnabile       (Boolean) valore checkbox "Impegnabile"
     * @param codiceCategoriaCapitolo   (String) Codice categoria capitolo
     * @param classificatoreFondino     (String) il selettore per la combo Capitolo FOndino (classificatoreGenerico3)
     * @param tipoCapitoloSTD           (Boolean) True se Standard, False altrimenti
     *
     */
    //Controlli al cambio della categoria capitolo
    exports.gestioneFondinoFromCategoria = function() {
        var checkboxImpegnabile = $("#flagImpegnabile").prop("checked");
        var codiceCategoriaCapitolo;
        var classificatoreFondino;
        var tipoCapitoloSTD;
        //La categoria viene gestita tramite uid. Alla load non devo fare la clear della select se è stato scelto un valore
        //dunque devo inventarmi questa funzione.
        var tipoCapitoloLoad = $.trim($("#categoriaCapitolo option:selected").text());
        var res = tipoCapitoloLoad.split("-");
        var isStandard = ($.trim(res[0])=="STD") ? true : false;
        //END 
        codiceCategoriaCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        
        classificatoreFondino = $("#classificatoreGenerico3");
        tipoCapitoloSTD = (codiceCategoriaCapitolo === "STD") ? true : false;

        if(!isStandard && !tipoCapitoloSTD){
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        }
        else if(tipoCapitoloSTD && !checkboxImpegnabile) {
            classificatoreFondino.removeAttr("disabled");
        }
        else if (isStandard && !checkboxImpegnabile){
            return;
            
        }else{
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        }
    };
    
    /**
     *
     * @param checkboxImpegnabile       (Boolean) valore checkbox "Impegnabile"
     * @param codiceCategoriaCapitolo   (String) Codice categoria capitolo
     * @param classificatoreFondino     (String) il selettore per la combo Capitolo FOndino (classificatoreGenerico3)
     * @param tipoCapitoloSTD           (Boolean) True se Standard, False altrimenti
     *
     */
    //Controlli al cambio del flag impegnabile
    exports.gestioneFondinoFromCheckImpegnabile = function() {
        
        
        //La categoria viene gestita tramite uid. Alla load non devo fare la clear della select se è stato scelto un valore
        //dunque devo inventarmi questa funzione.
        var tipoCapitoloLoad = $.trim($("#categoriaCapitolo option:selected").text());
        var res = tipoCapitoloLoad.split("-");
        var isStandard = ($.trim(res[0])=="STD") ? true : false;
        var checkboxImpegnabile = $("#flagImpegnabile").prop("checked");
        var codiceCategoriaCapitolo;
        var classificatoreFondino;
        var tipoCapitoloSTD;
        codiceCategoriaCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        classificatoreFondino = $("#classificatoreGenerico3");
        tipoCapitoloSTD = (codiceCategoriaCapitolo === "STD") ? true : false;

        if(!isStandard && !tipoCapitoloSTD){
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        }
        else if(tipoCapitoloSTD && checkboxImpegnabile) {
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        } else {
            classificatoreFondino.removeAttr("disabled");
        }
    };

    //END -----> SIAC-6884-VariazioneDecentrate
    

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
    exports.gestioneSIOPEPuntuale = function (selectorCodice, selectorDescrizione, selectorHiddenUid, selectorHiddenDescrizione, selectorHiddenTipo, url) {
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
    };

    /**
     * Effettua la copia dei dati
     */
    exports.effettuaCopia = function() {
        var $form = $('form');
        var url = $('#buttonCopia').data('url');
        $form.attr('action', url)
            .submit();
    };


    // Definizione dei settings
    zTreeSettingStrutturaAmministrativoContabile = $.extend(true, {}, zTreeSettingsBase, {callback: {onCheck: impostaValueStrutturaAmministrativoContabile}});
    zTreeSettingSIOPE = $.extend(true, {}, zTreeSettingsBase, {callback: {onCheck: impostaValueSIOPE}});
    zTreeSettingElementoPianoDeiConti = $.extend(true, {}, zTreeSettingsBase, {callback: {beforeCheck: controllaLivelloPianoDeiConti, onCheck: impostaValueElementoPianoDeiConti}});
    zTreeSettingElementoPianoDeiContiRicerca = $.extend(true, {}, zTreeSettingsBase,{callback: {onCheck: impostaValueElementoPianoDeiConti}});

    return exports;
}());

/* Document ready */
$(
    function() {
        // Controllo se il pulsante di deselezione debba essere disabilitato o meno
        var uidStrutturaAmministrativoContabile = $("#HIDDEN_StrutturaAmministrativoContabileUid").val();
        var uidElementoPianoDeiConti = $("#HIDDEN_ElementoPianoDeiContiUid").val();
        var uidSIOPE = $("#HIDDEN_SIOPEUid").val();
        if(!uidStrutturaAmministrativoContabile) {
            $("#deselezionaStrutturaAmministrativaContabile").attr("disabled", "disabled");
        }
        if(!uidElementoPianoDeiConti) {
            $("#deselezionaElementoPianoDeiConti").attr("disabled", "disabled");
        }
        if(!uidSIOPE) {
            $("#deselezionaSIOPE").attr("disabled", "disabled");
        }
        
        //SIAC-6884-VariazioneDecentrate
        $("#categoriaCapitolo").click(Capitolo.gestioneFlagImpegnabile);
        $("#categoriaCapitolo").click(Capitolo.gestioneFondinoFromCategoria);
        $("#flagImpegnabile").click(Capitolo.gestioneFondinoFromCheckImpegnabile);

        
        $('#buttonCopia').click(Capitolo.effettuaCopia);
        
        //alla load della pagina
        var tipoCapitoloLoad = $.trim($("#categoriaCapitolo option:selected").text());
        var res = tipoCapitoloLoad.split("-");
        var isStandard = ($.trim(res[0])=="STD") ? true : false;
        
        var checkboxImpegnabile = $("#flagImpegnabile").prop("checked");
        var codiceCategoriaCapitolo;
        var classificatoreFondino;
        var tipoCapitoloSTD;
        codiceCategoriaCapitolo = $("option:selected", "#categoriaCapitolo").data("codice");
        classificatoreFondino = $("#classificatoreGenerico3");
        tipoCapitoloSTD = (codiceCategoriaCapitolo === "STD") ? true : false;

        if(!isStandard && !tipoCapitoloSTD){
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        }
        else if(tipoCapitoloSTD && checkboxImpegnabile) {
            classificatoreFondino.val("0"); //clear della select. 
            classificatoreFondino.attr("disabled", true);
        } else{
            return;
        }


    }
);