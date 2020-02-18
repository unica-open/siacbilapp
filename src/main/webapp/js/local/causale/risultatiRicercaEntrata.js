/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Causale = (function ($) {

    "use strict";

    var exports = {};

    var alertErrori = $("#ERRORI");

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param uid l'uid da impostare
     */
    function clickOnAnnulla(uid) {
        $("#HIDDEN_UidDaAnnullare").val(uid);
    }

    /**
     * Permette la visualizzazione del dettaglio dello storico causali.
     *
     * @param e   (Event)  l'evento corrispondente alla chiamata della funzione
     * @param obj (Object) l'oggetto contenuto nella tabella
     */
    function dettaglioStorico(e, obj) {
        var uid = obj.uid;
        var spinner = $("#SPINNER_ConsultaCausale" + uid).addClass("activated");
        var modaleDaPopolare = $("#msgConsulta").modal("hide");

        e.preventDefault();

        $.postJSON(
            "consultaCausaleEntrata.do",
            {uidDaConsultare : uid},
            function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                popolaTabella("tabellaStoricoCausali", data.listaStoricoCausale);

                $("#MODALE_codiceCausale").html(obj.causaleCode);
                $("#MODALE_codiceTipoCausale").html(obj.tipoCausale);
                $("#MODALE_statoCausale").html(obj.statoOperativoCausaleDesc);

                modaleDaPopolare.modal("show");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Caricamento via Ajax dello storico delle causali
     *
     * @param idTabella {String} l'id della tabella
     * @param lista     {Array}  la lista da impostare
     */
    function popolaTabella(idTabella, lista) {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : false,
            "bPaginate" : true,
            "bLengthChange" : false,
            "iDisplayLength" : 5,
            "bSort" : false,
             "bInfo" : true,
            "bAutoWidth" : true,
            "bFilter" : false,
            "bProcessing" : true,
            "aaData" : lista,
            "bDestroy" : true,
             "oLanguage" : {
                "sInfo" : "_START_ - _END_ di _MAX_ risultati",
                "sInfoEmpty" : "0 risultati",
                "sProcessing" : "Attendere prego...",
                "sZeroRecords" : "Non sono presenti quote associate",
                 "oPaginate" : {
                     "sFirst" : "inizio",
                    "sLast" : "fine",
                    "sNext" : "succ.",
                    "sPrevious" : "prec.",
                    // Quando la tabella è vuota
                    "sEmptyTable" : "Nessun dato disponibile"
                }
            },
            // Definizione delle colonne
            "aoColumnDefs" : [
                {"aTargets" : [ 0 ], "mData" : "dataDecorrenza"},
                {"aTargets" : [ 1 ], "mData" : "capitolo"},
                {"aTargets" : [ 2 ], "mData" : "movimentoGestione"},
                {"aTargets" : [ 3 ], "mData" : "soggetto"},
                {"aTargets" : [ 4 ], "mData" : "provvedimento"}
            ]
        };

        $("#" + idTabella).dataTable(options);
    }


    /**
     * Caricamento via Ajax della tabella delle causali e visualizzazione.
     */
    exports.visualizzaTabellaCausali = function() {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaCausaleEntrataAjax.do",
            // Metodo HTTP per la chiamata AJAX
            "sServerMethod" : "POST",
            // Gestione della paginazione
            "bPaginate" : true,
            // Impostazione del numero di righe
            "bLengthChange" : false,
            // Numero base di righe
            "iDisplayLength" : 10,
            // Sorting delle colonne
            "bSort" : false,
            // Display delle informazioni
            "bInfo" : true,
            // Calcolo automatico della larghezza delle colonne
            "bAutoWidth" : true,
            // Filtro dei dati
            "bFilter" : false,
            // Abilita la visualizzazione di 'Processing'
            "bProcessing" : true,
            // Internazionalizzazione
            "oLanguage" : {
                // Informazioni su quanto mostrato nella pagina
                "sInfo" : "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                "sInfoEmpty" : "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                "sProcessing" : "Attendere prego...",
                // Testo quando non vi sono dati
                "sZeroRecords" : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                // Definizione del linguaggio per la paginazione
                "oPaginate" : {
                    // Link alla prima pagina
                    "sFirst" : "inizio",
                    // Link all'ultima pagina
                    "sLast" : "fine",
                    // Link alla pagina successiva
                    "sNext" : "succ.",
                    // Link alla pagina precedente
                    "sPrevious" : "prec.",
                    // Quando la tabella è vuota
                    "sEmptyTable" : "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            "fnDrawCallback" : function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            "aoColumnDefs" : [
                {"aTargets" : [ 0 ], "mData" : "causaleCode"},
                {"aTargets" : [ 1 ], "mData" : "tipoCausale"},
                {
                    "aTargets" : [ 2 ],
                    "mData" : function(source, type) {
                        if(!source.statoOperativo) {
                            source.statoOperativo = "";
                        }
                        if (type === 'set') {
                            source.statoOperativo = "<a rel='popover' href='#'>" + source.statoOperativoCausaleDesc + "</a>";
                            return;
                        }
                        return source.statoOperativo;
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.dataDecorrenza,
                            "title" : "Data decorrenza",
                            "trigger" : "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).off("click")
                            .on("click", function(event) {event.preventDefault();})
                            .popover(settings);
                    }
                },
                {"aTargets" : [ 3 ], "mData" : "strutturaAmministrativa"},
                {"aTargets" : [ 4 ], "mData" : "capitolo"},
                {"aTargets" : [ 5 ], "mData" : "movimentoGestione"},
                {"aTargets" : [ 6 ], "mData" : "soggetto"},
                {"aTargets" : [ 7 ], "mData" : "provvedimento"},
                {
                    "aTargets" : [ 8 ],
                    "mData" : "azioni",
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        $(nTd).find("a[href='#msgAnnulla']")
                                .substituteHandler("click", function() {
                                    clickOnAnnulla(oData.uid);
                                })
                                .end()
                            .find(".consultazioneCausale")
                                .substituteHandler("click", function(e) {
                                    dettaglioStorico(e, oData);
                                })
                                .end()
                            .find('.dropdown-toggle')
                                .dropdown();
                    }
                },
                {"aTargets" : [ 9 ],
                    "mData" : function(source) {
                        if(!source.colonnaPerSpinner) {
                            source.colonnaPerSpinner = "<i class='icon-spin icon-refresh spinner' id='SPINNER_ConsultaCausale" + source.uid + "'></i>";
                        }
                        return source.colonnaPerSpinner;
                    }
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#risultatiRicercaCausale").dataTable(options);
    };

    return exports;
}(jQuery));

$(function() {
    // Carico la dataTable
    Causale.visualizzaTabellaCausali();
});