/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Progetto = (function(){
    var exports = {};


    /**
     * Consulta i totali dei dettagli.
     *
     * @param url     (String) l'URL da invocare
     * @param header  (String) il testo da apporre nel titolo
     * @param spinner (jQuery) lo spinner da azionare
     * @param idCrono (String) l'id del cronoprogramma
     */
    function consultaTotali(url, header, spinner, idCrono) {
        var oggettoPerChiamataAjax = {};
        var id = parseInt(idCrono, 10);

        oggettoPerChiamataAjax.idCronoprogramma = id;

        spinner.addClass("activated");
        $.postJSON(
            url,
            oggettoPerChiamataAjax,
            function(data) {
                var anno;
                var tableHead = $("#tabellaTotali").find("thead").empty();
                var row;
                $("#titoloTotali").html(header);
                for(anno in data.mappaTotali) {
                    if(data.mappaTotali.hasOwnProperty(anno)) {
                        row = "<tr class='borderBottomLight'>" +
                                "<th scope='col'>Totale</th>" +
                                "<th scope='col'>" + anno + "</th>" +
                                "<th scope='col'>&nbsp;</th>" +
                                "<th scope='col'>&nbsp;</th>" +
                                "<th class='tab_Right text-right' scope='col'>" + data.mappaTotali[anno].formatMoney() + "</th>" +
                                "<th scope='col'>&nbsp;</th>" +
                            "</tr>";
                        tableHead.append(row);
                    }
                }
                $("#modaleConsultaTotali").modal("show");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Consulta il totale dei dettagli di entrata.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    exports.consultaTotaliEntrata = function(e) {
        var self = $(this);
        var idCrono = self.closest("fieldset")
            .find(".uidDelCronoprogramma")
                .val();
        var spinner = self.find(".spinner");
        e.preventDefault();
        consultaTotali("consultaProgettoCronoprogrammaTotaliEntrata.do", "Totali Entrata", spinner, idCrono);
    };

    /**
     * Consulta il totale dei dettagli di uscita.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    exports.consultaTotaliUscita = function(e) {
        var self = $(this);
        var idCrono = self.closest("fieldset")
            .find(".uidDelCronoprogramma")
                .val();
        var spinner = self.find(".spinner");
        e.preventDefault();
        consultaTotali("consultaProgettoCronoprogrammaTotaliUscita.do", "Totali Spesa", spinner, idCrono);
    };

    /**
     * Caricamento via Ajax della tabella dei cronoprogrammi e visualizzazione.
     *
     * @param idTabella {String} l'id della tabella
     */
    exports.visualizzaTabellaCronoProgrammi = function(idTabella) {
        var options = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : false,
            // Gestione della paginazione
            "bPaginate" : true,
            // Impostazione del numero di righe
            "bLengthChange" : false,
            // Numero base di righe
            "iDisplayLength" : 5,
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
                "sInfo" : "_START_ - _END_ di _MAX_ capitoli",
                // Informazioni per quando la tabella è vuota
                "sInfoEmpty" : "0 capitoli",
                // Testo mostrato quando la tabella sta processando i dati
                "sProcessing" : "Attendere prego...",
                // Testo quando non vi sono dati
                "sZeroRecords" : "Non sono presenti cronoprogrammi associati",
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
            }
        };

        $("#" + idTabella).dataTable(options);
        $("#" + idTabella + "_wrapper").children(":first").hide();
    };

    /**
     * Permette di consultare il cronoprogramma di gestione ahmad.
     *
     */
    exports.consultaCronoprogrammaGestione=function() {
        var url="consultaProgettoConsultaCronoprogrammaGestione.do";

        $.postJSON(url, function(data) {
            var tabella = $("#consultazioneCronoprogrammaDaGestioneTabella tbody");
            // Svuoto la tabella
            tabella.empty();

            // Carico i totali in tabella
            if(data.listaProspettoRiassuntivoCronoprogramma != null) {
                $.each(data.listaProspettoRiassuntivoCronoprogramma, function(index) {
                    var tr = $("<tr>");
                    // Creo le celle della tabella
                    var td1 = $("<td>").html(this.anno).addClass("colored");
                    var td2 = $("<td>").html(this.totaliSpese.formatMoney()).addClass("tab_Right");
                    var td3 = $("<td>").html(this.totaliEntrate.formatMoney()).addClass("tab_Right");
                    // Appendo le celle alla riga
                    tr.append(td1).append(td2).append(td3);
                    // Appendo la riga al corpo della tabella
                    tabella.append(tr);
                });
            }
        });
    };

    return exports;
}());

$(function() {
    var numeroTotaleCollapses = $(".accordion-group").length;
    var i;

    var impostaClickSuPulsante = function() {
        $("#pulsanteApriConsultaTotali" + this + i).on("click", Progetto["consultaTotali" + this]);
    };

    for(i = 0; i < numeroTotaleCollapses; i++) {
        $.each(["Entrata", "Uscita"], impostaClickSuPulsante);

        Progetto.visualizzaTabellaCronoProgrammi("tabellaEntrata" + i);
        Progetto.visualizzaTabellaCronoProgrammi("tabellaUscita" + i);
    }

    //aggiunta 24_03_2015 ahmad,per consultare il cronoprogramma di gestione accordion
    Progetto.consultaCronoprogrammaGestione();
});