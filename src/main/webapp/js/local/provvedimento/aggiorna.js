/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**
 * Carica i dati nello zTree della Struttura Amministrativo Contabile da
 * chiamata AJAX.
 */
$(
    function () {
        /* Pulsante per il modale della Struttura Amministrativo Contabile */
        var pulsante = $("#bottoneSAC");
        var spinner = $("#SPINNER_StrutturaAmministrativoContabile");
        /* Non permettere di accedere al modale finché il caricamento non è avvenuto */
        pulsante.removeAttr("href");
        /* Attiva lo spinner */
        spinner.addClass("activated");

        $.postJSON("ajax/strutturaAmministrativoContabileAjax.do", {})
        .then(function (data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
            ZTree.imposta("treeStruttAmm", ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
            /* Ripristina l'apertura del modale */
            pulsante.attr("href", "#struttAmm");
        }).always(function() {
            // Disattiva lo spinner anche in caso di errore
            spinner.removeClass("activated");
        });

        // Aggiorno le modifiche sul cambiamento del tipo di atto
        $("#tipoAtto").change(function () {
            if ($("#tipoAtto option:selected").html() === 'movimento interno') {
                $("#movimentoInternoHidden").val("true");
            } else {
                $("#movimentoInternoHidden").val("");
            }
        });

        $("form").substituteHandler("reset", function() {
            // NATIVO
            this.reset();
        });
    }
);