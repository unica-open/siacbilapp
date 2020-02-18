/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 ************************************
 **** Funzioni del provvedimento ****
 ************************************
 */

var Provvedimento = (function(exports) {

    var optionsBase = {
        // Gestione della paginazione
        "bPaginate" : true,
        // Impostazione del numero di righe
        "bLengthChange" : false,
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
     * Impostazione di un nuovo dataTable a partire dai dati, per il Provvedimento. Caso della ricerca senza operazioni.
     *
     * @param aaData {Array} i dati da impostare nel dataTable
     */
    function impostaDataTableProvvedimentoSenzaOperazioni(aaData) {
        // Le opzioni base del dataTable
        var optionsNuove = {
            // Dati da intabellare
            "aaData" : aaData,
            // Numero base di righe
            "iDisplayLength" : 3,
            // Disattiva il sorting delle colonne
            "bSort" : false,
            // Definizione delle colonne
            "aoColumnDefs" : [
                {
                    "aTargets" : [ 0 ],
                    "mData" : function(source, type, val) {
                        if(type === "set") {
                            // Definizione del radio
                            source.uid = "<input type='radio' name='uidProvvedimentoRadio' value='" + source.uid + "'";
                            if($("#HIDDEN_uidProvvedimento").val() !== undefined && parseInt($("#HIDDEN_uidProvvedimento").val(), 10) === val) {
                                source.uid += " checked='checked'";
                            }
                            source.uid += "/>";
                            return;
                        }
                        return source.uid;
                    }
                },
                {"aTargets" : [ 1 ], "mData" : "anno"},
                {"aTargets" : [ 2 ], "mData" : "numero"},
                {"aTargets" : [ 3 ], "mData" : "tipo"},
                {"aTargets" : [ 4 ], "mData" : "oggetto"},
                {"aTargets" : [ 5 ], "mData" : "strutturaAmministrativoContabile"},
                {"aTargets" : [ 6 ], "mData" : "stato"}
            ]
        };
        var options = $.extend(true, {}, optionsBase, optionsNuove);
        var tabellaGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($("#risultatiRicercaProvvedimento"));
        // Pulisco il dataTable prima di inizializzarne uno nuovo
        if(tabellaGiaInDataTable.length > 0) {
            // Distruggo la dataTable
            tabellaGiaInDataTable.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        $("#risultatiRicercaProvvedimento").dataTable(options);
    }

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     *
     * @param doNotPopulateErrori (Boolean) se gli errori siano da popolare o meno (Optional, default: false)
     */
    exports.cercaProvvedimento = function(doNotPopulateErrori) {
        // Workaround per la gestione del fieldset su Explorer
        var formDiRicerca = $("#formRicercaProvvedimento :input");
            // Valori per il check javascript
        var annoProvvedimento = $("#annoProvvedimento").val();
        var numeroProvvedimento = $("#numeroProvvedimento").val();
        var tipoProvvedimento =$("#tipoAttoProvvedimento").val();
            // Errori
        var erroriArray = [];
            // Alert per gli errori
        var alertErrori = $("#ERRORI");
            // Spinner
        var spinner = $("#SPINNER_Provvedimento");
        var innerDoNotPopulateErrori = !!doNotPopulateErrori;

        // Controllo che non vi siano errori
        if(annoProvvedimento === "") {
            erroriArray.push("Il campo Anno deve essere selezionato");
        }
        if(numeroProvvedimento === "" && tipoProvvedimento === "") {
            erroriArray.push("Almeno uno tra i campi Numero e Tipo deve essere compilato");
        }

        // Controllo l'esistenza degli errori
        if (erroriArray.length && !innerDoNotPopulateErrori) {
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

        spinner.addClass("activated");

        // Chiamata AJAX
        $.postJSON(
            // Dovrebbe andare bene
            "effettuaRicercaSenzaOperazioniProvvedimento.do",
            formDiRicerca.serializeArray(),
            function (data) {
                var errori = data.errori;
                var messaggi = data.messaggi;
                var informazioni = data.informazioni;
                var listaProvvedimenti = data.listaElementoProvvedimento;
                    // Alerts
                var alertMessaggi = $("#MESSAGGI");
                var alertInformazioni = $("#INFORMAZIONI");
                    // Tabella già in dataTable
                var tabellaGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($("#risultatiRicercaProvvedimento"));
                var invocazioneValida = true;

                if(!innerDoNotPopulateErrori) {
                    // Controllo l'assenza di errori, messaggi e informazioni
                    if(errori !== null && errori !== undefined && errori.length > 0 && alertErrori.length > 0) {
                        alertErrori.children("ul").find("li").remove().end();
                        // Aggiungo gli errori alla lista
                        $.each(errori, function() {
                            alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                        });
                        // Mostro l'alert di errore
                        alertErrori.slideDown();
                        // Ritorno indietro
                        invocazioneValida = false;
                    } else if(messaggi !== null && messaggi !== undefined && messaggi.length > 0 && alertMessaggi.length > 0) {
                        alertMessaggi.children("ul").find("li").remove().end();
                        $.each(messaggi, function() {
                            alertMessaggi.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                        });
                        alertMessaggi.slideDown();
                        invocazioneValida = false;
                    } else if(informazioni !== null && informazioni !== undefined &&
                            informazioni.length > 0 && alertInformazioni.length > 0) {
                        alertInformazioni.children("ul").find("li").remove().end();
                        $.each(informazioni, function() {
                            alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                        });
                        alertInformazioni.slideDown();
                        invocazioneValida = false;
                    }
                }

                if(!invocazioneValida) {
                    // L'invocazione è invalida
                    // Pulisco la dataTable, se presente; la inizializzo vuota in caso contrario
                    if(tabellaGiaInDataTable.length > 0) {
                        tabellaGiaInDataTable.dataTable().fnClearTable();
                    } else {
                        impostaDataTableProvvedimentoSenzaOperazioni([]);
                    }
                    return;
                }

                if(!innerDoNotPopulateErrori) {
                    alertErrori.slideUp();
                    alertMessaggi.slideUp();
                    alertMessaggi.slideUp();
                }

                // Pulisco il dataTable prima di inizializzarne uno nuovo
                if(tabellaGiaInDataTable.length > 0) {
                    // Distruggo la dataTable
                    tabellaGiaInDataTable.dataTable().fnDestroy();
                }
                // Non vi sono errori, né messaggi, né informazioni. Proseguire con la creazione del dataTable
                impostaDataTableProvvedimentoSenzaOperazioni(listaProvvedimenti);

                $("#tabellaProvvedimento").slideDown();
                // Imposto come selezionato un provvedimento, qualora si abbia il ripopolamento della pagina
                if($("input[name='uidProvvedimentoRadio']:checked").length > 0) {
                    exports.impostaUid(true, innerDoNotPopulateErrori);
                }
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
     * @param dallaRicerca        (Boolean) se l'impostazione del valore proviene dalla ricerca
     * @param doNotPopulateErrori (Boolean) se gli errori non siano da popolare
     *
     */
    exports.impostaUid = function(dallaRicerca, doNotPopulateErrori) {
        var alertErrori = $("#ERRORI");
        var radioButton = $("input[name='uidProvvedimentoRadio']:checked");
        var valoreDelRadio = radioButton.val();
            // Il collapse, se presente
        var divCollapse = $("#collapseProvvedimento");
        var spanInformazioni = $("#SPAN_InformazioniProvvedimento");
            // Dati del radio
        var annoProvvedimento;
        var numeroProvvedimento;
        var statoProvvedimento;

        // Controllo che non vi siano errori
        if(valoreDelRadio === undefined && !doNotPopulateErrori) {
            alertErrori.children("ul").find("li").remove().end();
            // Aggiungo gli errori alla lista
            alertErrori.children("ul").append($("<li/>").html("Necessario selezionare Provvedimento"));
            // Mostro l'alert di errore
            alertErrori.slideDown();
            // Ritorno indietro
            return;
        }

        // Determino anno e numero del Provvedimento
        annoProvvedimento = radioButton.parent().next().html();
        numeroProvvedimento = radioButton.parent().next().next().html();
        statoProvvedimento = radioButton.parent().siblings(":last").html();

        if(!doNotPopulateErrori) {
            alertErrori.slideUp();
        }
        $("#HIDDEN_uidProvvedimento").val(valoreDelRadio);
        $("#HIDDEN_uidProvvedimentoInjettato").val(valoreDelRadio);
        $("#HIDDEN_numeroProvvedimento").val(numeroProvvedimento);
        $("#HIDDEN_annoProvvedimento").val(annoProvvedimento);
        $("#HIDDEN_statoProvvedimento").val(statoProvvedimento);
        // Chiudo il collapse, qualora presente
        if(divCollapse.length > 0 && !dallaRicerca) {
            divCollapse.collapse("hide");
        }
        // Imposto le informazioni del provvedimento, qualora possibile
        if(spanInformazioni.length > 0) {
            spanInformazioni.html(": " + annoProvvedimento + " / " + numeroProvvedimento);
        }
    };

    /**
     * Deseleziona il provvedimento.
     */
    exports.deseleziona = function() {
        exports.svuotaFormRicerca();
        $("#SPAN_InformazioniProvvedimento").empty();
        $("#HIDDEN_uidProvvedimento").val("0");
        $("#HIDDEN_uidProvvedimentoInjettato").val("0");
    };

    /**
     * Svuota il form di ricerca del Provvedimento.
     */
    exports.svuotaFormRicerca = function() {
    	var tabelleInDataTable =$.fn.dataTable.fnTables(true);
    	var tabellaProvvedimento = $('#risultatiRicercaProvvedimento');
    	var tabelleDaIgnorare = $(tabelleInDataTable).not(tabellaProvvedimento);
    	var arrayDaIgnorare = [];
    	for(var i=0; i<tabelleDaIgnorare.size(); i++){
    	  arrayDaIgnorare.push(tabelleDaIgnorare.get(i));
    	}
        cleanDataTables(arrayDaIgnorare);
        $("#tabellaProvvedimento").slideUp();
        $("#HIDDEN_numeroProvvedimento").val("");
        $("#HIDDEN_annoProvvedimento").val("");
        $("#HIDDEN_statoProvvedimento").val("");
        $("#HIDDEN_StrutturaAmministrativoContabileUid").val("");
        $("#HIDDEN_StrutturaAmministrativoContabileCodice").val("");
        $("#HIDDEN_StrutturaAmministrativoContabileDescrizione").val("");
        $("#SPAN_StrutturaAmministrativoContabile").text("Nessuna Struttura Amministrativa Responsabile selezionata");
        $('#formRicercaProvvedimento').find(':input').val("");
        if($("#collapseProvvedimento").hasClass("in")){
            $("#collapseProvvedimento").collapse("hide");
        }
    };

    return exports;
} (Provvedimento || {}));

/* Document onReady */

$(
    function () {

        /* Pulsante per il modale della Struttura Amministrativo Contabile */
        var pulsante = $("#bottoneSAC");
        var spinner = $("#SPINNER_StrutturaAmministrativoContabile");

        // Lego le azioni ai pulsanti
        $("#pulsanteRicercaProvvedimento").on("click", Provvedimento.cercaProvvedimento);
        $("#pulsanteSelezionaProvvedimento").on("click", function() {
            Provvedimento.impostaUid();
        });
        $("#pulsanteDeselezionaProvvedimento").on("click", Provvedimento.deseleziona);

        /* Non permettere di accedere al modale finché il caricamento non è avvenuto */
        pulsante.removeAttr("href");
        /* Attiva lo spinner */
        spinner.addClass("activated");

        $.postJSON(
            "ajax/strutturaAmministrativoContabileAjax.do",
            {},
            function (data) {
                var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
                ZTree.imposta("treeStruttAmm", ZTree.SettingsBase, listaStrutturaAmministrativoContabile);
                /* Ripristina l'apertura del modale */
                pulsante.attr("href", "#struttAmm");
            }
        ).always(
            function() {
                // Disattiva lo spinner anche in caso di errore
                spinner.removeClass("activated");
            }
        );

        // Carico il provvedimento se i dati sono presenti
        var annoProvvedimento = $("#annoProvvedimento").val(),
            numeroProvvedimento = $("#numeroProvvedimento").val(),
            tipoProvvedimento =$("#tipoAttoProvvedimento").val();

        if(parseInt(annoProvvedimento, 10) && (parseInt(numeroProvvedimento, 10) || parseInt(tipoProvvedimento, 10))) {
            // I dati sono valorizzati: effettuare la ricerca
            Provvedimento.cercaProvvedimento(true);
        }

        submitFormOnEnterPress('#formRicercaConOperazioniProvvedimento');
        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $("form").on("reset", function() {
            $("#tabellaProvvedimento").slideUp();
            Provvedimento.deseleziona();

            if($("#collapseProvvedimento").hasClass("in")){
            	$('[data-toggle="collapse"][href="#collapseProvvedimento"]').trigger('click');                
            }
        });
    }
);