/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Storni = (function(exports) {

    /**
     * Gestione del controllo sull'apertura dei modali.
     */
    exports.controllaModale = function () {
        var tipoUEB = $("input[name=tipoCapitolo]:checked").val();
            // Modali
        var modaleSorgente = $("#ModaleSorgente");
        var modaleDestinazione = $("#ModaleDestinazione");
            // Alert
        var alertErrori = $("#ERRORI");
            // Lista degli errori
        var erroriList = alertErrori.children("ul");

        if (tipoUEB === undefined || tipoUEB === null) {
            // Vi sono degli errori. Impedire la prosecuzione

            // Disabilita l'href dell'apertura del modale
            if (modaleSorgente.attr("href") !== undefined || modaleDestinazione.attr("href") !== undefined) {
                modaleSorgente.removeAttr("href");
                modaleDestinazione.removeAttr("href");
            }
            /* Rimuove gli eventuali <li> presenti */
            erroriList.find("li").remove().end();
            erroriList.append($("<li/>").html("Selezionare prima il tipo di UEB"));
            // Mostra l'alert
            alertErrori.removeClass("hide");

        } else {
            // Se il modale è disattivato, lo riattiva
            if (modaleSorgente.attr("href") === undefined || modaleDestinazione.attr("href") === undefined  ) {
                modaleSorgente.attr("href", "#collapseb");
                modaleDestinazione.attr("href", "#collapsedest");
            }
            // Nasconde l'alert degli errori
            alertErrori.addClass("hide");
        }
    };

    /**
     * Effettua la chiamata al servizio di ricerca Capitolo come dato aggiuntivo.
     *
     * @param qualificaCapitolo definisce se il capitolo sia sorgente o destinazione
     */
    exports.cercaCapitoloQualificato = function(qualificaCapitolo) {
        var annoCapitolo = $("#annoCapitolo" + qualificaCapitolo).val();
        var numeroCapitolo = $("#numeroCapitolo" + qualificaCapitolo).val();
        var numeroArticolo = $("#numeroArticolo" + qualificaCapitolo).val();
        var numeroUEB = $("#numeroUEB" + qualificaCapitolo);
        var statoCapitolo = $("#statoCapitolo" + qualificaCapitolo).val();
            // Tipo di capitolo
        var tipoCapitolo = ($('input[name=tipoCapitolo]:checked').val() === "CAPITOLO_USCITA_GESTIONE" ? "Uscita" : "Entrata");
            // Tabelle
        var tabella = $("#risultatiRicercaCapitolo" + qualificaCapitolo);
            // Dati del capitolo
        var informazioniCapitolo = $("#informazioniCapitolo" + qualificaCapitolo);
            // variabili per l'oggetto AJAX
        var oggettoAjaxAnnoCapitolo = ("capitolo" + tipoCapitolo + "Gestione.annoCapitolo");
        var oggettoAjaxNumeroCapitolo = ("capitolo" + tipoCapitolo + "Gestione.numeroCapitolo");
        var oggettoAjaxNumeroArticolo = ("capitolo" + tipoCapitolo + "Gestione.numeroArticolo");
        var oggettoAjaxNumeroUEB = ("capitolo" + tipoCapitolo + "Gestione.numeroUEB");
        var oggettoAjaxStatoOperativo = ("capitolo" + tipoCapitolo + "Gestione.statoOperativoElementoDiBilancio");
            // Oggetto per la chiamata AJAX, comprendente i valori da inviare al server
        var oggettoPerChiamataAjax = {};
            // Nome del link AJAX
        var ajaxSource = "effettuaRicercaComeDatoAggiuntivoCap" + tipoCapitolo + "Gestione.do";
            // Spinner
        var spinner = $("#SPINNER_Capitolo" + qualificaCapitolo);

        oggettoPerChiamataAjax[oggettoAjaxAnnoCapitolo] = annoCapitolo;
        oggettoPerChiamataAjax[oggettoAjaxNumeroCapitolo] = numeroCapitolo;
        oggettoPerChiamataAjax[oggettoAjaxNumeroArticolo] = numeroArticolo;
        oggettoPerChiamataAjax[oggettoAjaxNumeroUEB] = numeroUEB.val();
        oggettoPerChiamataAjax[oggettoAjaxStatoOperativo] = statoCapitolo;

        spinner.addClass("activated");

        // Chiamata AJAX
        $.postJSON(
            ajaxSource,
            oggettoPerChiamataAjax,
            function (data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;
                var listaCapitoli = data.listaCapitoli;
                    // Alerts
                var alertErrori = $("#ERRORI");
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                    // Tabella già in dataTable
                var tabellaGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter(tabella);

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

                // Non vi sono errori, né messaggi, né informazioni. Proseguire con la creazione del dataTable
                informazioniCapitolo.html("Elenco Ueb capitolo " + numeroCapitolo + "/" + numeroArticolo);

                // Pulisco il dataTable prima di inizializzarne uno nuovo
                if(tabellaGiaInDataTable.length > 0) {
                    // Distruggo la dataTable
                    tabellaGiaInDataTable.dataTable().fnDestroy();
                }
                Storni.impostaDataTableCapitolo(listaCapitoli, tabella, qualificaCapitolo);
                // Mostro il div della tabella
                $("#tabellaCapitolo" + qualificaCapitolo).slideDown();
            }
        ).always(
            function() {
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Imposta l'uid nel campo hidden.
     *
     * @param tipo {String} il tipo di oggetto
     */
    exports.impostaCapitoloQualificato = function(tipo) {
        var alertErrori = $("#ERRORI");
        var tipoDato = "UEB " + tipo;
        var radioButton = $("input[name=uidCapitolo" + tipo + "Radio]:checked");
        var valoreDelRadio = radioButton.val();
        var numeroUEB = radioButton.parent().next().children("a").html();
        var datiDelCapitoloDivisi = numeroUEB.split("/");
        var idDivDaCollassare = (tipo === "Sorgente" ? "collapseb" : "collapsedest");

        // Controllo che non vi siano errori
        if(valoreDelRadio === undefined || valoreDelRadio === "") {
            alertErrori.children("ul").find("li").remove().end();
            // Aggiungo gli errori alla lista
            alertErrori.children("ul").append($("<li/>").html("Necessario selezionare " + tipoDato));
            // Mostro l'alert di errore
            alertErrori.slideDown();
            // Ritorno indietro
            return;
        }

        $("#HIDDEN_uidCapitolo" + tipo).val(valoreDelRadio);
        $("#informazioniUEB" + tipo).html(datiDelCapitoloDivisi.join(" / "));
        // Imposto il numeroUEB
        $("#numeroUEB" + tipo).val(datiDelCapitoloDivisi[datiDelCapitoloDivisi.length - 1]);
        // Nascondo il collapse
        $("#" + idDivDaCollassare).collapse("hide");
    };

    return exports;
}(Storni || {}));

/* Document onReady */

$(
    function() {

        var numeroCapitoloSorgente = $("#numeroCapitoloSorgente").val();
        var numeroArticoloSorgente = $("#numeroArticoloSorgente").val();
        var numeroCapitoloDestinazione = $("#numeroCapitoloDestinazione").val();
        var numeroArticoloDestinazione = $("#numeroArticoloDestinazione").val();

        // Imposto le azioni
        $("#ModaleSorgente").on("click", Storni.controllaModale);
        $("#ModaleDestinazione").on("click", Storni.controllaModale);
        $("#pulsanteRicercaCapitoloQualificatoSorgente").on("click", function() {
            Storni.cercaCapitoloQualificato("Sorgente");
        });
        $("#pulsanteRicercaCapitoloQualificatoDestinazione").on("click", function() {
            Storni.cercaCapitoloQualificato("Destinazione");
        });
        $("#pulsanteImpostaCapitoloQualificatoSorgente").on("click", function() {
            Storni.impostaCapitoloQualificato("Sorgente");
        });
        $("#pulsanteImpostaCapitoloQualificatoDestinazione").on("click", function() {
            Storni.impostaCapitoloQualificato("Destinazione");
        });


        // Attivo i capitoli sorgente e destinazione
        if(numeroCapitoloSorgente !== "" && numeroArticoloSorgente !== "") {
            Storni.cercaCapitoloQualificato("Sorgente");
        }
        if(numeroCapitoloDestinazione !== "" && numeroArticoloDestinazione !== "") {
            Storni.cercaCapitoloQualificato("Destinazione");
        }

        Provvedimento.inizializza();
        // Nasconde le tabelle del form durante il reset
        $("form").on("reset", function() {
            $("#tabellaCapitoloSorgente").slideUp();
            $("#tabellaCapitoloDestinazione").slideUp();
        });
    }
);