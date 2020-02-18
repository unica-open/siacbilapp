/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var euroUid = parseInt($("#HIDDEN_uidValutaEuro").val());
    var tipoGiustificativoClone = $("#tipoGiustificativoGiustificativo").clone().removeAttr("name").removeAttr("id").attr("data-name", "giustificativo.tipoGiustificativo.uid").removeClass().addClass("span12");
    var importoGiustificativoClone = $("<input>", {type: "text", "class": "span12 soloNumeri decimale", "data-name": "giustificativo.importoGiustificativo"});
    var importoSpettanteGiustificativoClone = $("<input>", {type: "text", "class": "span12 soloNumeri decimale", "data-name": "giustificativo.importoSpettanteGiustificativo"});

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModale = $("#ERRORI_modaleGiustificativo");

    /**
     * Apertura del modale del nuovo giustificativo.
     */
    function apriModaleNuovoGiustificativo() {
        var modale = $("#modaleGiustificativo");
        // Chiudo l'alert di errore
        alertErroriModale.slideUp();
        // Cancello tutti i campi
        modale.find(":input").val("");
        // Disabilito i campi
        $("#cambioGiustificativo, #importoGiustificativoInValutaGiustificativo").attr("disabled", true);
        // Imposto l'euro
        $("#valutaGiustificativo").val(euroUid);
        // Apro il modale
        modale.modal("show");
    }

    /**
     * Caricamento della lista dei giustificativi.
     */
    function caricaListaGiustificativi() {
        return $.postJSON("inserisciAnticipoPerTrasfertaDipendentiCassaEconomale_ottieniListaGiustificativi.do", {}, function(data) {
            // Impostazione totale
            $("#totaleGiustificativi").html(data.totaleImportiGiustificativi.formatMoney());
            // Impostazione dei giustificativi
            impostaTabellaGiustificativi(data.listaGiustificativo);
        });
    }

    /**
     * Impostazione della tabella dei giustificativi.
     *
     * @param list (Array) la lista da impostare
     */
    function impostaTabellaGiustificativi(list) {
        var opts = {
            bServerSide: false,
            aaData: list,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessun giustificativo associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#tabellaGiustificativi_processing").closest(".row-fluid.span12").addClass("hide");
            },
            aoColumnDefs: [

                //Tipo
                {aTargets: [0], mData: function(source) {
                    var res = "";
                    // Ciclare sulle option per selezionare quella corretta
                    selectCorrectOption(tipoGiustificativoClone, source.tipoGiustificativo.uid);
                    res = tipoGiustificativoClone[0].outerHTML;
                    // Aggiungo l'uid del giustificativo
                    res += "<input type='hidden' style='display:none' data-name='giustificativo.uid' value='" + source.uid + "'/>";
                    // Aggiungo la data di emissione
                    return res;
                }},

                //Importo
                {aTargets: [1], mData: function(source) {
                    var importo = source.importoGiustificativo !== undefined && source.importoGiustificativo.formatMoney() || "";
                    importoGiustificativoClone.attr("value", importo);
                    return importoGiustificativoClone[0].outerHTML;
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},

                //Importo spettante
                {aTargets: [2], mData: function(source) {
                    var importo = source.importoSpettanteGiustificativo !== undefined && source.importoSpettanteGiustificativo.formatMoney() || "";
                    importoSpettanteGiustificativoClone.attr("value", importo);
                    return importoSpettanteGiustificativoClone[0].outerHTML;
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},

                //Aggiorna
                {aTargets: [3], mData: function() {
                    return "<a title='aggiorna'><i class='icon-pencil icon-2x'><span class='nascosto'>aggiorna</span></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).click([oData, iRow], aggiornaGiustificativo);
                }},

                //Elimina
                {aTargets: [4], mData: function() {
                    return "<a title='elimina'><i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).click([oData, iRow], eliminaGiustificativo);
                }}


            ]
        };
        $("#tabellaGiustificativi").dataTable(opts);
    }

    /**
     * Selezione della option nella lista corispondente al valore fornito. L'uguaglianza tra i termini sara' debole.
     *
     * @param select (jQuery) la select da analizzare
     * @param value  (Object) il valore da considerare per la selezione
     *
     * @param (jQuery) la select modificata
     */
    function selectCorrectOption(select, value) {
        return select.find("option")
            .each(function() {
                var $this = $(this);
                var fnc = $this.val() == value ? "attr" : "removeAttr";
                $this[fnc]("selected", true);
            });
    }

    /**
     * Aggiornamento del giustificativo.
     *
     * @param e (Event) l'eveno scatenante l'invocazione della funzione
     */
    function aggiornaGiustificativo(e) {
        var tr = $(e.currentTarget).closest("tr");
        var clone = tr.clone();
        var inputs = clone.find(":input[data-name]");
        var obj;
        var row = e.data[1];

        // Imposto i name
        inputs.each(function() {
            var $this = $(this);
            var name = $this.data("name");
            $this.attr("name", name);
        });
        obj = clone.serializeObject();
        obj.rowNumber = row;

        tr.overlay("show");
        // Invocazione del servizio
        $.postJSON("inserisciAnticipoPerTrasfertaDipendentiCassaEconomale_updateGiustificativo.do", obj, function(data) {
            impostaDatiNegliAlert(data.errori, alertErrori) || impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricaricamento tabella
            caricaListaGiustificativi();
        }).always(function() {
            tr.overlay("hide");
        });
    }

    /**
     * Inizio dell'eliminazione del giustificativo.
     *
     * @param e (Event) l'evento scatenante l'invocazione della funzione
     */
    function eliminaGiustificativo(e) {
        // TODO: diversificare le stringhe
        var str = e.data[0].numeroGiustificativo
            ? 'giustificativo numero ' + e.data[0].numeroGiustificativo
            : 'giustificativo numero ' + (e.data[1] + 1);
        $("#elementoSelezionatoModaleAnnullamentoElemento").html(str);
        $("#confermaModaleAnnullamentoElemento").off("click").click(e.data, eliminazioneGiustificativo);
        $("#modaleAnnullamentoElemento").modal("show");
    }

    /**
     * Eliminazione del giustificativo.
     *
     * @param e (Event) l'eveno scatenante l'invocazione della funzione
     */
    function eliminazioneGiustificativo(e) {
        var row = e.data[1];
        var spinner = $("#SPINNER_confermaModaleAnnullamentoElemento").addClass("activated");
        $.postJSON("inserisciAnticipoPerTrasfertaDipendentiCassaEconomale_removeGiustificativo.do", {rowNumber: row}, function(data) {
            impostaDatiNegliAlert(data.errori, alertErrori) || impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Ricaricamento tabella
            caricaListaGiustificativi();
        }).always(function() {
            $("#modaleAnnullamentoElemento").modal("hide");
            spinner.removeClass("activated");
        });
    }

    /**
     * Inserimento del giustificativo
     */
    function inserisciGiustificativo() {
        var obj = $("#fieldsetModaleGiustificativo").serializeObject();

        // Chiudo l'alert degli errori
        alertErroriModale.slideUp();

        // Chiamata al servizio
        $.postJSON("inserisciAnticipoPerTrasfertaDipendentiCassaEconomale_addGiustificativo.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                return;
            }
            impostaDatiNegliAlert(data.informazioni, alertInformazioni);
            // Chiusura modale
            $("#modaleGiustificativo").modal("hide");
            // Ricaricamento tabella
            caricaListaGiustificativi();
        });
    }

    /**
     * Abilitazione e disabilitazione dei campi valuta
     */
    function abilitazioneCampiValuta() {
        // Prendo il contenitore
        var $this = $(this);
        var container = $this.closest("#fieldsetModaleGiustificativo, tr");
        var cambiDaAbilitareDisabilitare = container.find("input[data-euro]");
        // Doppio uguale in quanto possono insorgere problemi di cast (jQuery.fn.val ritorna una stringa)
        var isEuro = $this.val() == euroUid;
        cambiDaAbilitareDisabilitare[isEuro ? "attr" : "removeAttr"]("disabled", true);
    }

    /**
     * Copia della richiesta economale.
     */
    function copiaRichiestaEconomale() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }

    $(function() {
        $("#pulsanteCopiaRichiestaEconomale").click(copiaRichiestaEconomale);
        $("#pulsanteInserimentoDati").click(apriModaleNuovoGiustificativo);
        $("#confermaModaleGiustificativo").click(inserisciGiustificativo);
        $(document).on("change", "select[data-valuta]", abilitazioneCampiValuta);

        // Carica la lista dei giustificativi dalla action
        caricaListaGiustificativi();
        // Inizializzazione della gestione della matricola
        Matricola.inizializza("#codiceSoggettoSoggettoRichiestaEconomale", "#descrizioneMatricola", "#pulsanteCompilazioneGuidataMatricola", "#HIDDEN_codiceAmbito");
    });
}(jQuery);