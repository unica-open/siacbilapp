/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ZTreePreDocumento = (function($) {
    var exports = {};
    var deselezionaNodi;

    var settingsBase = {
        check : {
            enable : true,
            chkStyle : "radio",
            radioType : "all"
        },
        data : {
            key : {
                name : "testo",
                children : "sottoElementi"
            },
            simpleData : {
                enable : true
            }
        },
        callback : {
            onCheck : impostaValueStrutturaAmministrativoContabile
        }
    };

    /**
     * Crea una stringa gerarchica per il nodo.
     *
     * @param treeNode       (Object) il nodo
     * @param messaggioVuoto (String) il messaggio nel caso in cui non sia stato selezionato nulla
     * @param regressione    (Boolean) se sia necessaria una regressione sui nodi
     *
     * @returns (String) la stringa gerarchica ottenuta
     */
    function creaStringaGerarchica(treeNode, messaggioVuoto, regressione) {
        // Il nodo corrente
        var nodes = treeNode;
        // Il nodo padre
        var parent;
        // La stringa da comporre
        var string;
        if (!nodes) {
            throw new Error( "Nessun nodo su cui effettuare la creazione della stringa");
        }
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
     * Imposta il valore della Struttura.
     *
     * @param event    (Event) l'evento scatenante
     * @param treeId   (Number) l'uid dello zTree
     * @param treeNode (Object) il nodo
     */
    function impostaValueStrutturaAmministrativoContabile(event, treeId, treeNode, isFirst) {
        var stringa = creaStringaGerarchica(treeNode, "Nessuna Struttura Amministrativa Responsabile selezionata", true);
        var tree = this.getZTreeObj(treeId);
        var evt = $.Event("selezionataStrutturaAmministrativaContabile");
        valorizzaCampi(tree.innerZTree, treeNode, stringa);
        // Lancio un evento sul DOM
        evt.treeId = treeId;
        evt.treeNode = treeNode;
        evt.isFirst = !!isFirst;
        $(document).trigger(evt);
    }

    /**
     * Valorizza i campi hidden, pulendo il campo dell'uid nel caso in cui il nodo sia stato deselezionato.
     *
     * @param tree     (ZTree) l'albero
     * @param treeNode (Object) il nodo
     * @param stringa  (String) la stringa da apporre nei campi testuali
     */
    function valorizzaCampi(tree, treeNode, stringa) {
        var valoreUid = treeNode.checked ? treeNode.uid : "";

        tree.$hiddenUid.val(valoreUid);
        tree.$hiddenStringa.val(stringa);
        tree.$spanStringa.html(stringa);
    }

    /**
     * Costruttore per lo ZTree.
     *
     * @param campoUidHidden          (String) il campo hidden in cui impostare l'uid (Optional, default: #HIDDEN_StrutturaAmministrativaContabileUid)
     * @param campoStringaHidden      (String) il campo hidden in cui impostare la stringa composta dal nodo (Optional, default: #HIDDEN_StrutturaAmministrativaContabileStringa)
     * @param campoStringaSpan        (String) lo span in cui impostare la stringa composta dal nodo (Optional, default: #SPAN_StrutturaAmministrativaContabile)
     * @param campoStringaDeseleziona (String) il campo relativo al pulsante adibito alla deselezione dello zTree (Optional, default: #BUTTON_deselezionaStrutturaAmministrativoContabile)
     * @param listSelector            (String) il selettore della lista in cui apporre lo ZTree
     * @param settings                (Object) i settings per la creazione dell'oggetto (Optional, default: {})
     * @param json                    (JSON) l'oggetto JSON contenente i dati tramite cui popolare lo ZTree
     * @param suffix                  (String) il suffisso da apporre agli id (Optional, default: "")
     */
    var ZTree = function(campoUidHidden, campoStringaHidden, campoStringaSpan, campoStringaDeseleziona, listSelector, settings, json, suffix) {
        var compiledSuffix = suffix || "";
        var uidHidden = campoUidHidden || ("#HIDDEN_StrutturaAmministrativaContabileUid" + compiledSuffix);
        var stringaHidden = campoStringaHidden || ("#HIDDEN_StrutturaAmministrativaContabileStringa" + compiledSuffix);
        var stringaSpan = campoStringaSpan || ("#SPAN_StrutturaAmministrativaContabile" + compiledSuffix);
        var stringaDeseleziona = campoStringaDeseleziona || ("#BUTTON_deselezionaStrutturaAmministrativoContabile" + compiledSuffix);
        var self;
        var closestForm;
        var list = $(listSelector);

        this.suffix = compiledSuffix;
        this.$hiddenUid = $(uidHidden);
        this.$hiddenStringa = $(stringaHidden);
        this.$spanStringa = $(stringaSpan);
        this.$buttonDeseleziona = $(stringaDeseleziona);

        this.zTree = $.fn.zTree.init(list, $.extend(true, {}, settingsBase, settings), json);
        closestForm = list.closest("form");
        this.$form = closestForm.length && closestForm || $("form");

        $.extend(true, ZTree.prototype, $.fn.zTree);
        this.zTree.innerZTree = this;
        self = this;

        this.$buttonDeseleziona.on("click", function() {
            deselezionaNodi(self);
        });
    };

    /** Costruttore */
    ZTree.prototype.constructor = ZTree;

    /**
     * Imposta lo ZTree e lo restituisce al chiamante.
     *
     * @param listTree                (String) il selettore dell'elemento in cui apporre lo ZTree
     * @param settings                (Object) le impostazioni da utilizzare (Optional, default: {})
     * @param jsonVariable            (Array) la lista da impostare
     * @param idCampoHidden           (String) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     * @param campoStringaHidden      (String) il campo hidden in cui impostare la stringa composta dal nodo
     * @param campoStringaSpan        (String) lo span in cui impostare la stringa composta dal nodo
     * @param campoStringaDeseleziona (String) il campo relativo al pulsante adibito alla deselezione dello zTree
     * @param suffisso                (String) l'eventuale suffisso (Optional - default: "")
     *
     * @returns (ZTree) l'oggetto creato
     */
    exports.imposta = function(listTree, settings, jsonVariable, campoUidHidden, campoStringaHidden, campoStringaSpan, campoStringaDeseleziona, suffisso) {
        var inner = new ZTree(campoUidHidden, campoStringaHidden, campoStringaSpan, campoStringaDeseleziona,  listTree, settings, jsonVariable, suffisso);
        var uid = parseInt(inner.$hiddenUid.val(), 10);
        // Selezione subito il nodo, nel caso in cui ciò sia possibile
        exports.selezionaNodoSeApplicabile(inner, uid);

        // Lego la funzionalita' al reset
        inner.$form.on("reset", function() {
            exports.deselezionaNodiEAbilitaAlberatura(inner);
        });

        return inner;
    };

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     *
     * @param tree (ZTree) lo ZTree
     * @param uid  (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.selezionaNodoSeApplicabile = function(tree, uid) {
        var node;
        var alb = tree.zTree || tree;
        if (isNaN(parseInt(uid, 10)) || !tree) {
            return;
        }

        node = alb.getNodeByParam("uid", uid);
        // Evito il check nel caso in cui il nodo sia null
        if(!!node) {
            alb.checkNode(node, true, true, false);
            impostaValueStrutturaAmministrativoContabile.bind($.fn.zTree)(null, tree.zTree.setting.treeId, node, true);
        }
    };

    /**
     * Deseleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile.
     *
     * @param tree (ZTree) lo ZTree
     * @param uid  (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.deselezionaNodoSeApplicabile = function(tree, uid) {
        var node;
        var alb = tree.zTree || tree;
        if (isNaN(parseInt(uid, 10)) || !tree) {
            return;
        }

        node = alb.getNodeByParam("uid", uid);
        // Evito il check nel caso in cui il nodo sia null
        if(node) {
            alb.refresh();
            alb.checkNode(node, false, true, true);
        }
    };

    /**
     * Seleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile. <br>
     * Inoltre, disabilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     * @param uid  (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.selezionaNodoSeApplicabileEDisabilitaAlberatura = function(tree, uid) {
        var nodeArray;
        var node;
        var alb = tree.zTree || tree;
        if(!alb) {
            return;
        }

        // Abilito temporaneamente i nodi e li dechecko
        nodeArray = alb.getNodes();
        for(node in nodeArray) {
            if(nodeArray.hasOwnProperty(node)) {
                alb.setChkDisabled(nodeArray[node], false, true, true);
                alb.checkNode(nodeArray[node], false, true, true);
            }
        }

        exports.selezionaNodoSeApplicabile(tree, uid);

        // Disabilito temporaneamente i nodi
        nodeArray = alb.getNodes();
        for(node in nodeArray) {
            if(nodeArray.hasOwnProperty(node)) {
                alb.setChkDisabled(nodeArray[node], true, true, true);
            }
        }
    };

    /**
     * Deseleziona il nodo all'interno dello zTree nel caso in cui tale selezione sia possibile. <br>
     * Inoltre, abilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     * @param uid  (Number) l'id del campo hidden in cui potrebbe essere presente l'uid gi&agrave; selezionato
     */
    exports.deselezionaNodoSeApplicabileEAbilitaAlberatura = function(tree, uid) {
        var nodeArray;
        var node;
        var alb = tree.zTree || tree;

        exports.deselezionaNodoSeApplicabile(tree, uid);

        nodeArray = alb.getNodes();
        for (node in nodeArray) {
            if (nodeArray.hasOwnProperty(node)) {
                alb.setChkDisabled(nodeArray[node], false, true, true);
            }
        }
    };

    /**
     * Deseleziona i nodi all'interno dello zTree. <br>
     * Inoltre, abilita l'intera alberatura.
     *
     * @param tree (ZTree) lo ZTree
     */
    exports.deselezionaNodiEAbilitaAlberatura = function(tree) {
        var nodeArray;
        var node;
        var index;
        var alb = tree.zTree || tree;

        nodeArray = alb.getNodes();

        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                alb.setChkDisabled(node, false, true, true);
            }
        }
        // Deseleziono i nodi checkati
        nodeArray = alb.getCheckedNodes(true);
        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                alb.checkNode(node, false, true, true);
            }
        }
    };

    /**
     * Deseleziona i nodi all'interno dello zTree.
     *
     * @param tree (ZTree) lo ZTree
     */
    deselezionaNodi = exports.deselezionaNodi = function(tree) {
        var nodeArray;
        var node;
        var index;
        var alb = tree.zTree || tree;

        nodeArray = alb.getCheckedNodes(true);

        for (index in nodeArray) {
            if (nodeArray.hasOwnProperty(index)) {
                node = nodeArray[index];
                alb.checkNode(node, false, true, true);
            }
        }
    };

    return exports;
}(jQuery));