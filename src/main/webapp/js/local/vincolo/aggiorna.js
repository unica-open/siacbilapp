/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Vincolo = (function() {
    "use strict";
    var exports = {};

    /**
     * Imposta i totali degli stanziamenti.
     *
     * @param tipoCapitolo {String} il tipo di capitolo
     * @param data         {Object} i dati con gli stanziamenti
     */
    function impostaTotali(tipoCapitolo, data) {
        var index;
        // Imposto i totali
        for(index = 0; index < 3; index++) {
            $("#totaleStanziamento" + tipoCapitolo + index).html(data["totaleStanziamento" + tipoCapitolo + index].formatMoney());
        }
    }

    /**
     * Imposta la dataTable per i Capitoli.
     *
     * @param tabella      {String} l'id della tabella
     * @param lista        {Array}  la lista da impostare
     * @param tipoCapitolo {String} il tipo di capitolo
     */
    function impostaDataTableCapitoli(tabella, lista, tipoCapitolo) {

        // Le opzioni base del dataTable
        var optionsBase = {
            // Gestione della paginazione
            bPaginate: true,
            // Impostazione del numero di righe
            bLengthChange: false,
            // Sorting delle colonne
            bSort: false,
            // Display delle informazioni
            bInfo: true,
            // Calcolo automatico della larghezza delle colonne
            bAutoWidth: true,
            // Filtro dei dati
            bFilter: false,
            // Abilita la visualizzazione di 'Processing'
            bProcessing: true,
            // Configurazione per il processing server-side dei dati
            bServerSide: false,
            // Lista dei risultati
            aaData: lista,
            // Distugge la tabella prima di ricrearla
            bDestroy: true,

            // Internazionalizzazione
            oLanguage: {
                // Informazioni su quanto mostrato nella pagina
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                sInfoEmpty: "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                sProcessing: "Attendere prego...",
                // Testo quando non vi sono dati
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                // Definizione del linguaggio per la paginazione
                oPaginate: {
                    // Link alla prima pagina
                    sFirst: "inizio",
                    // Link all'ultima pagina
                    sLast: "fine",
                    // Link alla pagina successiva
                    sNext: "succ.",
                    // Link alla pagina precedente
                    sPrevious: "prec.",
                    // Quando la tabella è vuota
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnDrawCallback: function () {
                $('a', "#" + tabella).eventPreventDefault("click");
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {
                    aTargets: [ 0 ],
                    mData: function (source) {
                        return "<input type='radio' name='" + tipoCapitolo + ".capitolo.uid' value='" + source.uid + "'/>";
                    }
                },
                {
                    aTargets: [ 1 ],
                    mData: function (source) {
                        return "<a rel='popover' href='#'>" + source.capitolo + "</a>";
                    },
                    fnCreatedCell: function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            content: oData.descrizione,
                            title: "Descrizione",
                            trigger: "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).popover(settings);
                    }
                },
                {aTargets: [ 2 ], mData: "stato"},
                {aTargets: [ 3 ], mData: "classificazione"},
                {
                    aTargets: [ 4 ],
                    mData: function(source) {
                        return parseFloat(source.stanziamentoCompetenza).formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {
                    aTargets: [ 5 ],
                    mData: function(source) {
                        return parseFloat(source.stanziamentoResiduo).formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {aTargets: [ 6 ],
                    mData: function(source) {
                        return parseFloat(source.stanziamentoCassa).formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {aTargets: [ 7 ], mData: "struttAmmResp"},
                {
                    aTargets: [ 8 ],
                    mData: function (source) {
                        return "<a rel='popover' href='#'>" + source.pdcFinanziario + "</a>";
                    },
                    fnCreatedCell: function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            content: oData.pdcVoce,
                            title: "Voce",
                            trigger: "hover",
                            placement: "left"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).popover(settings);
                    }
                }
            ]
        };

        // Chiamata alla funzione dataTable per la generazione della tabella
        $('#' + tabella).dataTable(optionsBase);
    }

    /**
     * Effettua la ricerca del capitolo per creare una variazione.
     *
     * @param tipoCapitolo {String} il tipo di capitolo da cercare
     */
    exports.cercaCapitolo = function(tipoCapitolo) {
        var tipoBilancio = $("#tipoBilancio").find("label").html();
        var annoCapitolo = $("#annoCapitolo" + tipoCapitolo).val();
        var numeroCapitolo = $("#numeroCapitolo" + tipoCapitolo).val();
        var numeroArticolo = $("#numeroArticolo" + tipoCapitolo).val();
        var numeroUEB = $("#numeroUEB" + tipoCapitolo).val();
            // alert degli errori
        var alertErrori = $("#ERRORI");
            // Array degli errori
        var erroriArray = [];
            // Wrapper per l'injezione di tipo capitolo e tipo applicazione
        var wrapTipoCapitolo = tipoCapitolo + tipoBilancio;
        var capitoloDaRichiamare = "capitolo" + wrapTipoCapitolo;
        var oggettoPerChiamataAjax = {};
        var ajaxSource = "effettuaRicercaComeDatoAggiuntivoCap" + wrapTipoCapitolo + ".do";
            // Spinner
        var spinner = $("#SPINNER_cercaCapitolo" + tipoCapitolo);

        // Nascondo il fieldset
        $("#fieldsetCapitolo" + tipoCapitolo).slideUp();

        // Controllo che i campi siano compilati correttamente
        if(annoCapitolo === "") {
            erroriArray.push("Il campo Anno deve essere compilato");
        }
        if(!$.isNumeric(numeroCapitolo)) {
            erroriArray.push("Il campo Capitolo deve essere compilato");
        }
        if(!$.isNumeric(numeroArticolo)) {
            erroriArray.push("Il campo Articolo deve essere compilato");
        }
        if(tipoCapitolo === undefined) {
            erroriArray.push("Il tipo di capitolo deve essere selezionato");
        }

        // Se i campi non sono compilati correttamente, imposta l'errore nell'alert e ritorna
        if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
            return;
        }
        // La validazione JavaScript è andata a buon fine. Proseguire
        alertErrori.hide();

        oggettoPerChiamataAjax[capitoloDaRichiamare + ".annoCapitolo"] = annoCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroCapitolo"] = numeroCapitolo;
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroArticolo"] = numeroArticolo;
        if($.isNumeric(numeroUEB)) {
            // Aggiungo il numero di UEB se e solo se esso è definito
            oggettoPerChiamataAjax[capitoloDaRichiamare + ".numeroUEB"] = numeroUEB;
        }
        oggettoPerChiamataAjax[capitoloDaRichiamare + ".statoOperativoElementoDiBilancio"] = "VALIDO";

        spinner.addClass("activated");

        $.postJSON(
            ajaxSource,
            oggettoPerChiamataAjax,
            function(data) {
                var errori = data.errori;

                // Controllo gli eventuali errori
                if(impostaDatiNegliAlert(errori, alertErrori)) {
                    return;
                }
                // Nascondo gli alert
                alertErrori.hide();

                impostaDataTableCapitoli("capitoli" + tipoCapitolo, data.listaCapitoli, tipoCapitolo);
                $("#fieldsetCapitolo" + tipoCapitolo).slideDown();
                // var idTabella = "capitoli" + tipoCapitolo;
                // $('#'+idTabella+' > tbody').attr('id',tipoCapitolo);
            }
        ).always(
            function() {
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Associa il capitolo.
     *
     * @param tipoCapitolo {String} il tipo di capitolo da associare
     */
    exports.associaCapitolo = function(tipoCapitolo) {
        var uidCapitolo = $("input[name='" + tipoCapitolo + ".capitolo.uid']:checked").val();
        var oggettoPerChiamataAjax = {};
        var spinner = $("#SPINNER_associaCapitolo" + tipoCapitolo);
        var ajaxUrl = "aggiornaVincoloAssociaCapitolo" + tipoCapitolo + ".do";
        var alertErrori = $("#ERRORI");

        if(uidCapitolo === undefined) {
            impostaDatiNegliAlert(["Necessario selezionare il capitolo da associare"], alertErrori);
            return;
        }

        oggettoPerChiamataAjax["capitolo.uid"] = uidCapitolo;

        spinner.addClass("activated");

        $.postJSON(
            ajaxUrl,
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }

                // Imposto la tabella
                Vincolo.tabellaCapitoliAssociati("capitoli" + tipoCapitolo + "NelVincolo", data["listaCapitoli" + tipoCapitolo]);
                // Imposto i totali
                impostaTotali(tipoCapitolo, data);
                // Nascondo il collapse di associazione
                $("#associaCapitolo" + tipoCapitolo).collapse("hide");

                // Nascondo la tabella
                $("#fieldsetCapitolo" + tipoCapitolo).slideUp();
                // Cancello il dataTable di associazione e sbianco i dati della ricerca
                var tabella = $("#capitoli" + tipoCapitolo).dataTable();
                tabella.fnClearTable();
                tabella.fnDestroy();

                $("#numeroCapitolo" + tipoCapitolo).val("");
                $("#numeroArticolo" + tipoCapitolo).val("");
                $("#numeroUEB" + tipoCapitolo).val("");
            }
        ).always(
            function() {
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Metodo di utilit&agrave; per l'impostazione del tipo di capitolo nel campo hidden relativo allo scollegamento della relazione con il vincolo.
     *
     * @param event        {Event} l'evento verificatosi
     * @param tipoCapitolo {String} il tipo di capitolo da impostare
     */
    exports.eliminaCapitolo = function(event, tipoCapitolo) {
        var $ojbCapitolo = $("input[name='capitolo.uid']:checked");
        var uidCapitolo = $ojbCapitolo.val();
        if(uidCapitolo === undefined) {
            impostaDatiNegliAlert(["Necessario selezionare il capitolo da disassociare"], $("#ERRORI"));
            event.preventDefault();
            event.stopPropagation();
            return;
        }

        //SIAC-7524
        var idTabella = "#".concat('','capitoli').concat('',tipoCapitolo).concat('','NelVincolo');
        var tabella = $(idTabella);
        var $capitoloSelezionato = $("input[name='capitolo.uid']:checked").closest('table', idTabella);
        var tipoCapitoloSelezionato = $capitoloSelezionato['0'].id === tabella['0'].id;

        if(!tipoCapitoloSelezionato){
            impostaDatiNegliAlert(["Nessun capitolo selezionato per la tipologia selezionata"], $("#ERRORI"));
            event.preventDefault();
            event.stopPropagation();
            return;
        }
        //

        $("#HIDDEN_tipoCapitolo").val(tipoCapitolo);
        $("#HIDDEN_uidCapitolo").val(uidCapitolo);
    };

    /**
     * Scollega il capitolo dal vincolo.
     */
    exports.scollegaCapitolo = function() {
        var oggettoPerChiamataAjax = {};
        var tipoCapitolo = $("#HIDDEN_tipoCapitolo").val();
        var uidCapitolo = $("#HIDDEN_uidCapitolo").val();
        var ajaxUrl = "aggiornaVincoloScollegaCapitolo" + tipoCapitolo + ".do";
        var spinner = $("#SPINNER_elimina");

        oggettoPerChiamataAjax["capitolo.uid"] = uidCapitolo;

        spinner.addClass("activated");

        $.postJSON(
            ajaxUrl,
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                    return;
                }

                // Imposto la tabella
                Vincolo.tabellaCapitoliAssociati("capitoli" + tipoCapitolo + "NelVincolo", data["listaCapitoli" + tipoCapitolo]);
                // Imposto i totali
                impostaTotali(tipoCapitolo, data);
                // Nascondo il collapse di associazione
                $("#msgElimina").modal("hide");
            }
        ).always(
            function() {
                spinner.removeClass("activated");
            }
        );
    };

    /**
     * Caricamento via Ajax della tabella dei vincoli e visualizzazione.
     *
     * @param idTabella {String} l'id della tabella
     * @param lista     {Array}  la lista da impostare
     */
    exports.tabellaCapitoliAssociati = function(idTabella, lista) {
        var options = {
            // Configurazione per il processing server-side dei dati
            bServerSide: false,
            // Gestione della paginazione
            bPaginate: true,
            // Impostazione del numero di righe
            bLengthChange: false,
            // Numero base di righe
            iDisplayLength: 5,
            // Sorting delle colonne
            bSort: false,
            // Display delle informazioni
            bInfo: true,
            // Calcolo automatico della larghezza delle colonne
            bAutoWidth: true,
            // Filtro dei dati
            bFilter: false,
            // Abilita la visualizzazione di 'Processing'
            bProcessing: true,
            // Dati nella tabella
            aaData: lista,
            // Distrugge la tabella prima di ricrearla
            bDestroy: true,
            // Internazionalizzazione
            oLanguage: {
                // Informazioni su quanto mostrato nella pagina
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                // Informazioni per quando la tabella è vuota
                sInfoEmpty: "0 risultati",
                // Testo mostrato quando la tabella sta processando i dati
                sProcessing: "Attendere prego...",
                // Testo quando non vi sono dati
                sZeroRecords: "Non sono presenti capitoli associati",
                // Definizione del linguaggio per la paginazione
                oPaginate: {
                    // Link alla prima pagina
                    sFirst: "inizio",
                    // Link all'ultima pagina
                    sLast: "fine",
                    // Link alla pagina successiva
                    sNext: "succ.",
                    // Link alla pagina precedente
                    sPrevious: "prec.",
                    // Quando la tabella è vuota
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnDrawCallback: function () {
                $('a', "#" + idTabella).eventPreventDefault("click");
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {
                    aTargets: [ 0 ],
                    mData: function (source) {
                        return "<input type='radio' name='capitolo.uid' value='" + source.uid + "'/>";
                    }
                },
                {
                    aTargets: [ 1 ],
                    mData: function (source) {
                        return "<a rel='popover' href='#'>" + source.capitolo + "</a>";
                    },
                    fnCreatedCell: function (nTd, sData, oData) {
                        // Settings del popover
                        var settings = {
                            content: oData.descrizione,
                            title: "Descrizione",
                            trigger: "hover"
                        };
                        // Importante : attivare il popover sull'elemento anchor
                        $("a", nTd).popover(settings);
                    }
                },
                {aTargets: [ 2 ], mData: "classificazione"},
                {
                    aTargets: [ 3 ],
                    mData: function(source) {
                        return source.competenzaAnno0.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {
                    aTargets: [ 4 ],
                    mData: function(source) {
                        return source.competenzaAnno1.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {
                    aTargets: [ 5 ],
                    mData: function(source) {
                        return source.competenzaAnno2.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                },
                {aTargets: [ 6 ], mData: "strutturaAmministrativoContabile"}
            ]
        };

        $("#" + idTabella).dataTable(options);
    };

    /**
     *
     */
    exports.chiudiRicercaCapitolo = function(event, tipoCapitolo) {
        event.preventDefault();

        $("#associaCapitolo" + tipoCapitolo)
            // Nascondo il collapse
            .collapse("hide")
            .find("#fieldsetAnagraficaCapitolo" + tipoCapitolo)
                .find("input:enabled")
                    // Sbianco gli input modificabili
                    .val("")
                    .end()
                .end()
            .find("#fieldsetCapitolo" + tipoCapitolo)
                // Nascondo il fieldset
                .slideUp()
                .find("#capitoli" + tipoCapitolo)
                    // Svuoto il dataTable
                    .dataTable()
                    .fnClearTable();
    };

    /**
     *  Controllo aggiuntivo per il bottone di indietro.
     *
     *  @param event (Event) l'evento che sto considerando
     */
    exports.controllaIndietro = function(event) {
        var ajaxUrl = "controllaCapitoliAssociatiAlVincolo.do";
        $.postJSON(
            ajaxUrl,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, $("#ERRORI"))) {
                    event.preventDefault();
                }
            },
            false
        );
    };

    return exports;
}());

$(
    function() {

        // Imposto le funzionalità dei pulsanti
        $("#pulsanteCercaEntrata").on("click", function() {
            Vincolo.cercaCapitolo("Entrata");
        });
        $("#pulsanteCercaUscita").on("click", function() {
            Vincolo.cercaCapitolo("Uscita");
        });

        $("#pulsanteAssociaCapitoloEntrata").on("click", function() {
            Vincolo.associaCapitolo("Entrata");
        });
        $("#pulsanteAssociaCapitoloUscita").on("click", function() {
            Vincolo.associaCapitolo("Uscita");
        });

        $("#pulsanteEliminaEntrata").on("click", function(e) {
            Vincolo.eliminaCapitolo(e, "Entrata");
        });
        $("#pulsanteEliminaUscita").on("click", function(e) {
            Vincolo.eliminaCapitolo(e, "Uscita");
        });

        $("#pulsanteAnnullaRicercaEntrata").on("click", function(e) {
            Vincolo.chiudiRicercaCapitolo(e, "Entrata");
        });
        $("#pulsanteAnnullaRicercaUscita").on("click", function(e) {
            Vincolo.chiudiRicercaCapitolo(e, "Uscita");
        });

        $("#eliminaCapitolo").on("click", Vincolo.scollegaCapitolo);

        $("#pulsanteRedirezioneIndietro").on("click", Vincolo.controllaIndietro);


        // Carico i dati per le tabelle dal servizio
        $.postJSON(
            "aggiornaVincoloLeggiCapitoliAssociati.do",
            {},
            function(data) {
                Vincolo.tabellaCapitoliAssociati("capitoliEntrataNelVincolo", data.listaCapitoliEntrata);
                Vincolo.tabellaCapitoliAssociati("capitoliUscitaNelVincolo", data.listaCapitoliUscita);
            }
        );

        $("form").substituteHandler("reset", function() {
            // NATIVO
            this.reset();
        });

    }
);