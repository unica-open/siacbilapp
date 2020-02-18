/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/* Estensione delle funzionalità */
var Storni = (function(exports) {

    /**
     * Effettua la chiamata al servizio di ricerca Capitolo come dato aggiuntivo. Popola sia la tabella del sorgente che la tabella della destinazione.
     *
     * @param daDocumentReady {Boolean, Optional} indica se la ricerca proviene da un documentReady
     */
    exports.cercaCapitolo = function(/* Opzionale */daDocumentReady) {
        var annoCapitolo = $("#annoCapitoloSorgente").val();
        var numeroCapitolo = $("#numeroCapitoloSorgente").val();
        var numeroArticolo = $("#numeroArticoloSorgente").val();
            // Tipo di capitolo
        var tipoCapitolo = $('input[name=tipoCapitolo]:checked').val();
            // Tabelle
        var tabellaSorgente = $("#risultatiRicercaCapitoloSorgente");
        var tabellaDestinazione = $("#risultatiRicercaCapitoloDestinazione");
            // Dati del capitolo
        var informazioniSorgenti = $("#informazioniCapitoloSorgente");
        var informazioniDestinazione = $("#informazioniCapitoloDestinazione");
            // variabili per l'oggetto AJAX
        var oggettoAjaxAnnoCapitolo = ("capitolo" + tipoCapitolo + "Gestione.annoCapitolo");
        var oggettoAjaxNumeroCapitolo = ("capitolo" + tipoCapitolo + "Gestione.numeroCapitolo");
        var oggettoAjaxNumeroArticolo = ("capitolo" + tipoCapitolo + "Gestione.numeroArticolo");
            // Oggetto per la chiamata AJAX, comprendente i valori da inviare al server
        var oggettoPerChiamataAjax = {};
            // Nome del link AJAX
        var ajaxSource = "effettuaRicercaComeDatoAggiuntivoCap" + tipoCapitolo + "Gestione.do";
            // Array degli errori
        var erroriArray = [];
            // Alert degli errori
        var alertErrori = $("#ERRORI");
            // Spinner
        var spinner = $("#SPINNER_CapitoloSorgente");
            // Div per il sorgente e per la destinazione
        var divTabellaSorgente = $("#tabellaCapitoloSorgente");
        var divTabellaDestinazione = $("#tabellaCapitoloDestinazione");

        // Controllo che non vi siano errori
        if(tipoCapitolo === undefined) {
            erroriArray.push("Il tipo di capitolo deve essere selezionato");
        }
        if(numeroCapitolo === "") {
            erroriArray.push("Il campo Capitolo deve essere compilato");
        }
        if(numeroArticolo === "") {
            erroriArray.push("Il campo Articolo deve essere compilato");
        }

        // Controllo l'esistenza degli errori
        if (erroriArray.length !== 0) {
            // Pulisco la lista degli errori
            alertErrori.children("ul").find("li").remove().end();
            // Aggiungo gli errori alla lista
            $.each(erroriArray, function(key, value) {
                alertErrori.children("ul").append($("<li/>").html(value));
            });
            // Mostro l'alert di errore
            alertErrori.slideDown();
            // Ritorno indietro
            return;
        }

        oggettoPerChiamataAjax[oggettoAjaxAnnoCapitolo] = annoCapitolo;
        oggettoPerChiamataAjax[oggettoAjaxNumeroCapitolo] = numeroCapitolo;
        oggettoPerChiamataAjax[oggettoAjaxNumeroArticolo] = numeroArticolo;

        spinner.addClass("activated");
        divTabellaSorgente.slideUp();
        divTabellaDestinazione.slideUp();

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
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                    // Tabelle già in dataTable
                var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

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

                // Nascondo gli alert, nel caso fossero a schermo
                if(!daDocumentReady) {
                    alertErrori.slideUp();
                    alertMessaggi.slideUp();
                    alertInformazioni.slideUp();
                }

                // Non vi sono errori, né messaggi, né informazioni. Proseguire con la creazione del dataTable
                informazioniSorgenti.html("Elenco Ueb capitolo " + numeroCapitolo + "/" + numeroArticolo);
                informazioniDestinazione.html("Elenco Ueb capitolo " + numeroCapitolo + "/" + numeroArticolo);

                // Pulisco il dataTable prima di inizializzarne uno nuovo
                if($(tabelleGiaInDataTable).filter(tabellaSorgente).length > 0) {
                    $(tabelleGiaInDataTable).filter(tabellaSorgente).dataTable().fnDestroy();
                }
                if($(tabelleGiaInDataTable).filter(tabellaDestinazione).length > 0) {
                    $(tabelleGiaInDataTable).filter(tabellaDestinazione).dataTable().fnDestroy();
                }

                Storni.impostaDataTableCapitolo(listaCapitoli, tabellaSorgente, "Sorgente");
                Storni.impostaDataTableCapitolo(listaCapitoli, tabellaDestinazione, "Destinazione");

                divTabellaSorgente.slideDown();
                divTabellaDestinazione.slideDown();

                // Seleziono il capitolo nel caso si torni nella pagina con un ripopolamento del form
                if($("input[name='uidCapitoloSorgenteRadio']:checked").length > 0) {
                    exports.impostaCapitolo("CapitoloSorgente", daDocumentReady);
                }
                if($("input[name='uidCapitoloDestinazioneRadio']:checked").length) {
                    exports.impostaCapitolo("CapitoloDestinazione");
                }
            }
        ).always(
            function() {
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Imposta i campi del capitolo selezionato
     *
     * @param tipo            {String}            il tipo di oggetto
     * @param daDocumentReady {Boolean, Optional} se il capitolo proviene da un documentReady
     */
    exports.impostaCapitolo = function (tipo, /* Opzionale */daDocumentReady) {
        // Imposta l'uid del capitolo
        var radioButton = $("input[name=uid" + tipo + "Radio]:checked");
        var valoreDelRadio = radioButton.val();
        var numeroUEB = radioButton.parent().next().children("a").html();
        var objectDaInviare;
            // Dati per la request
        var annoCapitolo = $("#annoCapitoloSorgente").val();
        var numeroCapitolo = $("#numeroCapitoloSorgente").val();
        var numeroArticolo = $("#numeroArticoloSorgente").val();
        var faseBilancio = $("#HIDDEN_faseBilancio").val();
        var statoBilancio = $("#HIDDEN_statoBilancio").val();
        var capitolo = radioButton.data("capitolo");
        var tipoDisponibilitaRichiesta = "";
            // booleano della possibilità di continuare
        var continuazionePossibile = Storni.impostaUid(tipo, valoreDelRadio);

        if(!continuazionePossibile) {
            // Non posso continuare, pertanto torno indietro
            return;
        }
        var disponibilita = capitolo.disponibileVariareAnno0 || 0;
        var tipoCapitolo = (tipo === "CapitoloSorgente" ? "Sorgente" : "Destinazione");

        $("#disponibilita" + tipo).html(" UEB " + numeroUEB + " - Disponibilita: " + disponibilita.formatMoney());
        $("#divDisponibilita" + tipo).slideDown();
        if(tipo === "CapitoloSorgente" && !daDocumentReady) {
            // Cancello gli importi
            $("#competenzaSorgente0").val("0,00");
            $("#competenzaSorgente1").val("0,00");
            $("#competenzaSorgente2").val("0,00");
            $("#cassaSorgente0").val("0,00");
        }
        // Imposto le informazioni nello span
        $("#SPAN_Informazioni" + tipo).html(": " + numeroUEB);
        $("#HIDDEN_numeroUEB" + tipoCapitolo).val(numeroUEB.split("/")[2]);
        $("#HIDDEN_disponibilita" + tipoCapitolo).val(disponibilita);

       //mantengo l'object perchè forse è usato da altro
        objectDaInviare = {
            "annoCapitolo" : annoCapitolo,
            "numeroCapitolo" : numeroCapitolo,
            "numeroArticolo" : numeroArticolo,
            "faseBilancio" : faseBilancio,
            "statoBilancio" : statoBilancio,
            "tipoDisponibilitaRichiesta" : tipoDisponibilitaRichiesta
        };
    };

    /**
     * Svuota il form dello storno.
     */
    exports.svuotaFormStorno = function() {
        // Nasconde le tabelle del form
        $("#tabellaCapitoloSorgente").slideUp();
        $("#tabellaCapitoloDestinazione").slideUp();
        // Nasconde i div delle disponibilità
        $("#divDisponibilitaCapitoloSorgente").slideUp();
        $("#divDisponibilitaCapitoloDestinazione").slideUp();
        // Svuota le informazioni
        $("#SPAN_InformazioniCapitoloSorgente").html("");
        $("#SPAN_InformazioniCapitoloDestinazione").html("");
        $("#SPAN_InformazioniProvvedimento").html("");
        // Collassa la ricerca del provvedimento
        $("#collapsec").collapse("hide");
    };

    return exports;
}(Storni || {}));





/* Document onReady */

$(
    function() {

        var tipoCapitolo = $("input[name=tipoCapitolo]:checked").val();
        var numeroCapitolo = $("#numeroCapitoloSorgente").val();
        var numeroArticolo = $("#numeroArticoloSorgente").val();

        // Imposto le azioni
        $("#pulsanteRicercaCapitolo").on("click", Storni.cercaCapitolo);
        $("#pulsanteImpostaCapitoloSorgente").on("click", function() {
            Storni.impostaCapitolo("CapitoloSorgente");
        });
        $("#pulsanteImpostaCapitoloDestinazione").on("click", function() {
            Storni.impostaCapitolo("CapitoloDestinazione");
        });
        $("form").on("reset", Storni.svuotaFormStorno);

        Provvedimento.inizializza();
        
        // Caricamento delle liste dei capitoli
        if(tipoCapitolo !== undefined && numeroCapitolo !== "" && numeroArticolo !== "") {
            // Workaroud per errori nel caricamento delle tabelle in concorrenza
            Storni.cercaCapitolo(true);
        }
    }
);