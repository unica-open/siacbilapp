/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ConsultaMassiva = (function() {
    var exports = {};

    /**
     * Annulla il capitolo.
     *
     * @params url {String} l'url da invocare
     */
    exports.annulla = function(url) {
        var uidDaAnnullare = $("#HIDDEN_UidDaAnnullare").val();
        var tabella = $("#risultatiricerca");
        var modale = $("#msgAnnulla");
        var alertErrori = $("#ERRORI");
        var alertMessaggi = $("#MESSAGGI");
        var alertInformazioni = $("#INFORMAZIONI");

        alertErrori.slideUp();
        alertMessaggi.slideUp();
        alertInformazioni.slideUp();

        $.post(
            url,
            {"uidDaAnnullare" : uidDaAnnullare},
            function(data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;

                // Controllo l'assenza di errori, messaggi e informazioni
                if(errori !== null && errori !== undefined && errori.length > 0) {
                    alertErrori.children("ul").find("li").remove().end();
                    // Mostro l'alert di errore
                    alertErrori.slideDown();
                    // Aggiungo gli errori alla lista
                    $.each(errori, function() {
                        alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    // Ritorno indietro
                    return;
                }
                if(messaggi !== null && messaggi !== undefined && messaggi.length > 0) {
                    alertMessaggi.children("ul").find("li").remove().end();
                    alertMessaggi.slideDown();
                    $.each(messaggi, function() {
                        alertMessaggi.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    return;
                }
                if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                    alertInformazioni.children("ul").find("li").remove().end();
                    alertInformazioni.slideDown();
                    $.each(informazioni, function() {
                        alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    return;
                }
                // Effettuo un refresh della tabella
                tabella.fnReloadAjax();
            }
        ).always(
            function() {
                modale.modal("hide");
            }
        );
    };

    /**
     * Elimina il capitolo.
     *
     * @param url {String} l'url da invocare
     */
    exports.elimina = function(url) {
        var uidDaEliminare = $("#HIDDEN_UidDaEliminare").val();
        var modale = $("#msgElimina");
        var alertErrori = $("#ERRORI");
        var alertMessaggi = $("#MESSAGGI");
        var alertInformazioni = $("#INFORMAZIONI");

        alertErrori.slideUp();
        alertMessaggi.slideUp();
        alertInformazioni.slideUp();

        // Chiamata al servizio
        $.postJSON(
            url,
            {"uidDaEliminare" : uidDaEliminare},
            function(data) {
                var errori = data.errori,
                messaggi = data.messaggi,
                informazioni = data.informazioni;

                // Controllo l'assenza di errori, messaggi e informazioni
                if(errori !== null && errori !== undefined && errori.length > 0) {
                    alertErrori.children("ul").find("li").remove().end();
                    // Mostro l'alert di errore
                    alertErrori.slideDown();
                    // Aggiungo gli errori alla lista
                    $.each(errori, function() {
                        alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    // Ritorno indietro
                    return;
                }
                if(messaggi !== null && messaggi !== undefined && messaggi.length > 0) {
                    alertMessaggi.children("ul").find("li").remove().end();
                    alertMessaggi.slideDown();
                    $.each(messaggi, function() {
                        alertMessaggi.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    return;
                }
                if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                    alertInformazioni.children("ul").find("li").remove().end();
                    alertInformazioni.slideDown();
                    $.each(informazioni, function() {
                        alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                    });
                    return;
                }
                // Effettuo un refresh della tabella
                ConsultaMassiva.dataTable.fnReloadAjax();
            }
        ).always(
            function() {
                modale.modal("hide");
            }
        );
    };

    /** Per ottenere il dataTable */
    exports.dataTable = undefined;

    return exports;
}());