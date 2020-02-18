/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($) {
    var exports = {};

    /**
     * Deseleziona la struttura.
     *
     * @param e (Event) l'evento scatenante
     */
    exports.deselezionaStrutturaAmministrativoContabile = function(e) {
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm");
        var nodo = tree && tree.getCheckedNodes(true)[0];
        e.preventDefault();

        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    };

    return exports;
} (jQuery));

$(function () {
    Provvedimento.inizializzazione();
    $("#pulsanteDelesezionaStrutturaAmministrativoContabile").on("click", Documento.deselezionaStrutturaAmministrativoContabile);
});