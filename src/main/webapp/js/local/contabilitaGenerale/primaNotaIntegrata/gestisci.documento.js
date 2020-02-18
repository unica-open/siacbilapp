/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    /**
     * Applicazione del dataTable.
     */
    function applyDataTable() {
        var opts = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti quote",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };
        $("#tabellaQuote").dataTable(opts);
    }

    /**
     * Controlla se i dati per la redirezione del dettaglio siano rispettati.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function controllaRedirezioneDettaglio(e) {
        var data = $("#dataRegistrazionePrimaNota").val();
        var descrizione = $("#descrizionePrimaNota").val();
        var errors = [];
        var url = $(this).attr("href");
        var err;
        var errString;

        e.preventDefault();

        if(!data) {
            errors.push("La data non &eacute; stata valorizzata");
        }
        if(!descrizione) {
            errors.push("La descrizione non &eacute; stata valorizzata");
        }

        if(!errors.length) {
            // Non ho errori. Sono a posto, e pertanto esco
            appendToFormAndSubmit(url);
            return;
        }

        // Ho degli errori: segnalo all'utente

        errString = "Sono stati riscontrati i seguenti errori:<br/><ul>";
        for(err in errors) {
            if(errors.hasOwnProperty(err)) {
                errString += "<li>" + errors[err] + "</li>";
            }
        }
        errString += "</ul>Non sar&agrave; possibile completare la scrittura nella prossima pagina. Si &eacute; certi di voler proseguire?";

        bootbox.dialog(errString, [{
            "label" : "Si"
            , "class" : "btn btn-primary"
            , "callback": function() {
                appendToFormAndSubmit(url);
                $("form").attr("action", url).submit();
            }
        }, {
            "label" : "No"
            , "class" : "btn btn-secondary"
        }], {
            animate: true
            , classes: "dialogWarn"
            , header: "Attenzione!"
            , backdrop: 'static'
        });
    }

    /**
     * Appende i dati dell'url al form e invia.
     *
     * @param url (String) l'url da invocare
     */
    function appendToFormAndSubmit(url) {
        var splitUrl = url.split("?");
        var location = splitUrl[0];
        var params = splitUrl[1] ? splitUrl[1].split("&") : [];
        var paramObjs;
        var paramIdx;
        var form = $("form");
        var str = "";

        form.attr("action", location);
        for(paramIdx in params) {
            if(params.hasOwnProperty(paramIdx)) {
                paramObjs = params[paramIdx].split("=");
                str += "<input type=\"hidden\" name=\"" + paramObjs[0] + "\" value=\"" + paramObjs[1] + "\" />";
            }
        }
        form.append(str);
        form.submit();
    }

    $(function () {
        applyDataTable();
        $("a[data-dettaglio]").click(controllaRedirezioneDettaglio);
    });
}(jQuery);