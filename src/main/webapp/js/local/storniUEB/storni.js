/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *******************************
 **** Funzioni dello storno ****
 *******************************
 */

var Storni = (function(exports) {

    // Opzioni base per il dataTable
    var dataTableBaseOptions = {
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
        "bDestroy" : true,
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
        }
    };

    // Opzioni DataTables per le ricerche del Capitolo
    var dataTableCapitoloOptions = {
        // Gestione della paginazione
        "bPaginate" : true,
        // Impostazione del numero di righe
        "bLengthChange" : false,
        // Numero base di righe
        "iDisplayLength" : 3,
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
        "bDestroy" : true,
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
        }
    };

    /**
     * Imposta il dataTable per gli storniUEB.
     */
    exports.visualizzaTabellaStorni = function() {
        // Le opzioni base del dataTable
        var optionsBase = {
            // Configurazione per il processing server-side dei dati
            "bServerSide" : true,
            // Sorgente AJAX dei dati
            "sAjaxSource" : "risultatiRicercaStornoUEBAjax.do",
            // Metodo HTTP per la chiamata AJAX
            "sServerMethod" : "POST",

            // Chiamata al termine della creazione della tabella
            "fnDrawCallback" : function () {
                $('#id_num_result').html(" " + this.fnSettings().fnRecordsTotal() + " ");
            },
            // Definizione delle colonne
            "aoColumnDefs" : [
                {
                    "aTargets" : [ 0 ],
                    "mData" : function (source) {
                        return "<input type='radio' name='uidDaAggiornare' value='" + source.uid + "'/>";
                    }
                },
                {"aTargets" : [ 1 ], "mData" : "numeroStorno"},
                {"aTargets" : [ 2 ], "mData" : "capitoloSorgente"},
                {"aTargets" : [ 3 ], "mData" : "capitoloDestinazione"},
                {"aTargets" : [ 4 ], "mData" : "provvedimento"}
            ]
        };
        var options = $.extend(true, {}, dataTableBaseOptions, optionsBase);
        var startPos = $("#HIDDEN_startPosition").val();
        var posizioneStart = parseInt(startPos, 10);

        // Se è presente la posizione di start, la inserisco nelle opzioni
        if(!isNaN(posizioneStart)){
            posizioneStart = parseInt(startPos, 10);
            $.extend(true, options, {"iDisplayStart" : posizioneStart});
        }

        $("#risultatiRicercaStorniUEB").dataTable(options);
    };

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per il Capitolo.
     *
     * @param aaData  {Array}  i dati da impostare nel dataTable
     * @param tabella {String} la tabella in cui apporre i dati
     * @param campo   {String} il nome del radio button
     */
    exports.impostaDataTableCapitolo = function(aaData, tabella, campo) {
        // Le opzioni nuove del dataTable
        var nuoveOptions = {
            "aaData" : aaData,
            // Definizione delle colonne
            "aoColumnDefs" : [
                {
                    "aTargets" : [ 0 ],
                    "mData" : function(source, type, val) {
                        if(type === "set") {
                            // Definizione del radio
                            source.uid = "<input type='radio' name='uidCapitolo" + campo + "Radio' value='" + source.uid + "'";
                            if($("#HIDDEN_uidCapitolo" + campo).val() !== undefined && parseInt($("#HIDDEN_uidCapitolo" + campo).val(), 10) === val) {
                                source.uid += " checked='checked'";
                            }
                            source.uid += "/>";
                            return;
                        }
                        return source.uid;
                    },"fnCreatedCell" : function (nTd, sData, oData) {
                        $("input", nTd).data("capitolo",oData);
                    }
                },
                {
                    "aTargets" : [ 1 ],
                    "mData" : function (source) {
                        return "<a rel='popover' href='#'>" + source.capitolo + "</a>";
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.descrizione,
                            "title" : "Descrizione",
                            "trigger" : "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).popover(settings);
                    }
                },
                {"aTargets" : [ 2 ], "mData" : "stato"},
                {"aTargets" : [ 3 ], "mData" : "classificazione"},
                {
                    "aTargets" : [ 4 ],
                    "mData" : function(source) {
                        return parseFloat(source.stanziamentoCompetenza).formatMoney();
                    }
                },
                {
                    "aTargets" : [ 5 ],
                    "mData" : function(source) {
                        return parseFloat(source.stanziamentoResiduo).formatMoney();
                    }
                },
                {
                    "aTargets" : [ 6 ],
                    "mData" : function(source) {
                        return parseFloat(source.stanziamentoCassa).formatMoney();
                    }
                },
                {"aTargets" : [ 7 ], "mData" : "struttAmmResp"},
                {
                    "aTargets" : [ 8 ],
                    "mData" : function (source) {
                        return "<a rel='popover' href='#'>" + source.pdcFinanziario + "</a>";
                    },
                    "fnCreatedCell" : function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            "content" : oData.pdcVoce,
                            "title" : "Voce",
                            "trigger" : "hover",
                            "placement" : "left"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).popover(settings);
                    }
                }
            ]
        },
            cloneOptionsCapitolo = $.extend(true, {}, dataTableCapitoloOptions),
            options = $.extend(true, cloneOptionsCapitolo, nuoveOptions);

        // Inizializzazione del dataTable
        tabella.dataTable(options);
    };

    /**
     * Imposta l'uid nel campo hidden.
     *
     * @param tipo           {String} il tipo di oggetto
     * @param valoreDelRadio {String} il valore del radio
     *
     * @returns true se non vi sono stati errori, false altrimenti
     */
    exports.impostaUid = function(tipo, valoreDelRadio) {
        var alertErrori = $("#ERRORI"),
            tipoDato = (tipo === "Provvedimento" ? tipo :
                (tipo === "CapitoloSorgente" ? "UEB sorgente" : "UEB destinazione" ));

        // Il tipo sarà 'CapitoloSorgente', 'CapitoloDestinazione' o 'Provvedimento'
        if(valoreDelRadio === null || valoreDelRadio === undefined) {
            valoreDelRadio = $("input[name='uid" + tipo + "Radio']:checked").val();
        }

        // Controllo che non vi siano errori
        if(valoreDelRadio === undefined || valoreDelRadio === "") {
            alertErrori.children("ul").find("li").remove().end();
            // Aggiungo gli errori alla lista
            alertErrori.children("ul").append($("<li/>").html("Necessario selezionare " + tipoDato));
            // Mostro l'alert di errore
            alertErrori.slideDown();
            // Ritorno indietro
            return false;
        }

        $("#HIDDEN_uid" + tipo).val(valoreDelRadio);

        return true;
    };

    return exports;
}(Storni || {}));