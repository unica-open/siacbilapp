/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/**** Funzioni dell Atto di Legge ****
 ************************************
 */

var AttoDiLegge = (function() {

    var exports = {};

    // Opzioni DataTables per le ricerche del Atto di Legge
    var dataTableAttoDiLeggeOptions = {
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            sZeroRecords: "Nessun Atto di Legge trovato con in criteri di ricerca indicati.",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };
    // Opzioni DataTables per le ricerche del Atto di Legge */
    var dataTableRelazioniOptions = {
        bPaginate: false,
        bLengthChange: false,
        iDisplayLength: 99,
        bSort: false,
        bInfo: false,
        bAutoWidth: false,
        bFilter: false,
        bProcessing: true,
        oLanguage: {
            sInfo: "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty: "0 risultati",
            sProcessing: "Attendere prego...",
            sZeroRecords: "Nessuna relazione con atti di legge valida per questo capitolo.",
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    /**
     * Prepara Annulla Relazione.
     *
     * @param uidRelazione {Number} l'uid della relazione
     */
    function clickOnAnnullaRelazione(uidRelazione) {
        var alertAvviso = $("#divAvvisoAnnullamentoRel");
        var alertErrori = $("#ERRORI_ANNULLA_REL");
        var alertInformazioni = $("#INFORMAZIONI_ANNULLA_REL");
        var btnChiudi = $("#btnAnnullaRelChiudi");
        var btnIndietro = $("#btnAnnullaRelIndietro");
        var btnProcedi = $("#btnAnnullaRelProcedi");

        alertErrori.slideUp();
        alertInformazioni.slideUp();
        alertAvviso.slideDown();
        btnChiudi.hide();
        btnIndietro.show();
        btnProcedi.show();

        $("input[name='relazioneAttoDiLeggeCapitolo.uid']").val(uidRelazione);
    }

    /**
     * Prepara Aggiorna Relazione.
     *
     * @param uidRelazione   {Number} l'uid della relazione
     * @param uidAttoDiLegge {Number} l'uid dell'atto di legge
     */
    function clickOnAggiornaRelazione(uidRelazione, uidAttoDiLegge)  {
        var alertErrori = $("#ERRORI_AGGIORNA_REL");
        var alertInformazioni = $("#INFORMAZIONI_AGGIORNA_REL");
        var btnChiudi = $("#btnAggiornaRelChiudi");
        var btnAnnulla = $("#btnAggiornaRelAnnulla");
        var btnSalva = $("#btnAggiornaRelSalva");
        var btnAggiornaAtto = $("btnAggiornaAttoLegge");
        var objectDaInviare = {
            uidAttoDiLeggeCapitolo: uidRelazione
        };

        $("#divAggiornaRelazioneAttoDiLeggeCapitolo").slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        btnChiudi.hide();
        btnAnnulla.show();
        btnSalva.show();
        btnAggiornaAtto.show();

        $("input[name='attoDiLeggeCapitolo.uid']").val(uidRelazione);
        $("input[name='attoDiLeggeCapitolo.uidAttoDiLegge']").val(uidAttoDiLegge);

            // Chiamata AJAX
        $.postJSON ("attoDiLegge/ricercaPuntualeRelazioniAttoDiLeggeCapitolo.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;
            var listaRisultati = data.aaData;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                return;

            }

            if (listaRisultati.length > 0) {
                $("input[name='attoDiLeggeCapitolo.uid']").val(listaRisultati[0].uid);
                $("input[name='attoDiLeggeCapitolo.uidAttoDiLegge']").val(listaRisultati[0].uidAttoDiLegge);
                $("input[name='relazioneAttoDiLeggeCapitolo.uid']").val(listaRisultati[0].uid);
                $("input[name='relazioneAttoDiLeggeCapitolo.uidAttoDiLegge']").val(listaRisultati[0].uidAttoDiLegge);
                $("input[name='aggAttoDiLegge.uid']").val(listaRisultati[0].uidAttoDiLegge);
                $("input[name='relazioneAttoDiLeggeCapitolo.gerarchia']").val(listaRisultati[0].gerarchia);
                $("textarea[name='relazioneAttoDiLeggeCapitolo.descrizione']").val(listaRisultati[0].descrizione);
                $("input[name='relazioneAttoDiLeggeCapitolo.dataInizioFinanziamento']").val(listaRisultati[0].dataInizioFinanziamento);
                $("input[name='relazioneAttoDiLeggeCapitolo.dataFineFinanziamento']").val(listaRisultati[0].dataFineFinanziamento);
            }

            $("#divAggiornaRelazioneAttoDiLeggeCapitolo").slideDown();
            $('#aggiornaRelazioneAttoDiLeggeCapitolo').find('input, select').attr('disabled',false);
        });
    }

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per Atto di Legge.
     *
     * @param aaData {Array}  i dati da impostare nel dataTable
     * @param campo  {String} il nome del radio button
     */
    function impostaDataTableAttoDiLegge(aaData, campo) {
        // Le opzioni nuove del dataTable
        var tabella = $("#risultatiRicercaAttoDiLeggeSenzaOperazioni");

        var nuoveOptions = {
            aaData: aaData,
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "<input type='radio' name='" + campo + "Radio' value='" + source.uid + "'/>";
                }},
                {aTargets: [1], mData: function (source) {
                    return source.tipoAtto && (source.tipoAtto.codice + "-" + source.tipoAtto.descrizione) || "";
                }},
                {aTargets: [2], mData: defaultPerDataTable('anno')},
                {aTargets: [3], mData: defaultPerDataTable('numero')},
                {aTargets: [4], mData: defaultPerDataTable('articolo')},
                {aTargets: [5], mData: defaultPerDataTable('comma')},
                {aTargets: [6], mData: defaultPerDataTable('punto')}
            ]
        };

        var options = $.extend(true, {}, dataTableAttoDiLeggeOptions, nuoveOptions);

        // Inizializzazione del dataTable
        tabella.dataTable(options);
    }

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per Atto di Legge.
     *
     * @param aaData {Array} i dati da impostare nel dataTable
     */
    function impostaDataTableRelazioni(aaData) {
        // Le opzioni nuove del dataTable
        var tabella = $("#relazioniAttoDiLeggeCapitoloConOperazioni");
        var nuoveOptions = {
            aaData: aaData,
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('tipoAtto')},
                {aTargets: [1], mData: defaultPerDataTable('anno')},
                {aTargets: [2], mData: defaultPerDataTable('numero')},
                {aTargets: [3], mData: defaultPerDataTable('articolo')},
                {aTargets: [4], mData: defaultPerDataTable('comma')},
                {aTargets: [5], mData: defaultPerDataTable('punto')},
                {aTargets: [6], mData: defaultPerDataTable('gerarchia')},
                {aTargets: [7], mData: defaultPerDataTable('descrizione')},
                {aTargets: [8], mData: defaultPerDataTable('dataInizioFinanziamento')},
                {aTargets: [9], mData: defaultPerDataTable('dataFineFinanziamento')},
                {aTargets: [10], mData: defaultPerDataTable('azioni'), fnCreatedCell: function (nTd, sData, oData) {
                    // Importante : attivare il popover sull'elemento anchor
                    $("a[data-annulla]", nTd).substituteHandler("click", function(event) {
                        event.preventDefault();
                        clickOnAnnullaRelazione(oData.uid);
                    });
                    $("a[data-aggiorna]", nTd).substituteHandler("click", function(event) {
                        event.preventDefault();
                        clickOnAggiornaRelazione(oData.uid, oData.uidAttoDiLegge);
                    });
                }}
            ]
        };
        var options = $.extend(true, {}, dataTableRelazioniOptions, nuoveOptions);

        // Inizializzazione del dataTable
        tabella.dataTable(options);
    }

    /**
     * Effettua la chiamata Ajax al servizio di riceca atto di legge
     */
    function cercaRelazioniAttoDiLegge() {
        var alertErrori = $("#ERRORI_CERCA_REL");
        var alertInformazioni = $("#INFORMAZIONI_CERCA_REL");
        var tabelleGiaInDataTable =  $.fn.dataTable.fnTables(true);
        var uidCapitoloAggiornare = $("#uidCapitoloDaAggiornare").val();
        var uidBilancio = $("input[name='bilancio.uid']").val();
        var objectDaInviare = {
             uidCapitolo: uidCapitoloAggiornare,
             uidBilancio: uidBilancio
        };
        // nasconde alert errori e messaggi
        alertErrori.slideUp();
        alertInformazioni.slideUp();

            // Chiamata AJAX
        $.postJSON("attoDiLegge/ricercaRelazioniAttoDiLeggeCapitolo.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;
            var listaRisultati = data.aaData;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                return;
            }

            // Pulisco il dataTable prima di inizializzarne uno nuovo
            if(tabelleGiaInDataTable.length > 0) {

                $("#relazioniAttoDiLeggeCapitoloConOperazioni").dataTable().fnDestroy();
            }

            if (listaRisultati === null || listaRisultati === undefined) {
                listaRisultati = "";
            }
            impostaDataTableRelazioni(listaRisultati);
        });
    }

    /**
     * Effettua la chiamata al servizio di salvataggio atto di legge.
     */
    exports.salvaNuovoAttoDiLegge = function() {
        var formDiInserimento = $("#formInserisciAttoDiLegge");
        var alertErrori = $("#ERRORI_INSERISCI_ADL");
        var alertInformazioni = $("#INFORMAZIONI_INSERISCI_ADL");
        var btnChiudi = $("#btnModalInsAttoChiudi");
        var btnAnnulla = $("#btnModalInsAttoAnnulla");
        var btnSalva = $("#btnModalInsAttoSalva");

        alertErrori.slideUp();

        alertInformazioni.slideUp();

            // Chiamata AJAX
        $.postJSON("attoDiLegge/inserisciAttoDiLegge.do", formDiInserimento.serializeArray(), function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                $('#divInserisciAttoDiLegge').find('input').not(':button, :submit, :reset, :hidden').attr('disabled','disabled');
                $('#divInserisciAttoDiLegge').find('select').attr('disabled','disabled');

                btnChiudi.show();
                btnAnnulla.hide();
                btnSalva.hide();

                // Lancia anche una ricerca dell'atto di legge
                $("input[name='annoLegge']").val($("input[name='attoDiLegge.anno']").val());
                $("input[name='numeroLegge']").val($("input[name='attoDiLegge.numero']").val());
                $("select[name='tipoAtto.uid']").val($("select[name='attoDiLegge.tipoAtto.uid']").val());
                AttoDiLegge.cercaAttoDiLegge();
                return;
            }
        });
    };

    /**
     * Pulisce i campi di input del modale di inserimento nuovo atto di legge e imposta i pulsanti visibili.
     */
    exports.preparaInserimentoAttoDiLegge = function() {
        var formDiInserimento = $("#formInserisciAttoDiLegge");
        var alertErrori = $("#ERRORI_INSERISCI_ADL");
        var alertInformazioni = $("#INFORMAZIONI_INSERISCI_ADL");
        var btnChiudi = $("#btnModalInsAttoChiudi");
        var btnAnnulla = $("#btnModalInsAttoAnnulla");
        var btnSalva = $("#btnModalInsAttoSalva");

        alertErrori.slideUp();

        alertInformazioni.slideUp();

        btnChiudi.hide();
        btnAnnulla.show();
        btnSalva.show();

        formDiInserimento.find('input, select').attr('disabled', false);
        formDiInserimento.find(':input').not('button, hidden').val('');
    };

    /**
     * Predispone l'annullamento del atto di legge.
     */
    exports.preparaAnnullaAttoDiLegge = function() {
        var alertAvviso = $("#divAvvisoAnnullamento");
        var alertErr = $("#ERRORI_ANNULLA_ATTO");
        var alertErrori = $("#ERRORI_ANNULLA_ATTO");
        var alertInformazioni = $("#INFORMAZIONI_ANNULLA_ATTO");
        var btnChiudi = $("#btnAnnullaAttoChiudi");
        var btnIndietro = $("#btnAnnullaAttoIndietro");
        var btnProcedi = $("#btnAnnullaAttoProcedi");
        var radioButton = $("input[name=uidAttoDiLeggeRadio]:checked");
        var valoreDelRadio = radioButton.val();

        if (valoreDelRadio === null || valoreDelRadio === undefined) {
            alertErr.children("ul").find("li").remove().end();
            // Mostro l'alert di errore
            alertErr.children("ul").append($("<li/>").html("E' necessario selezionare un atto di legge per procedere!"));
            alertErr.slideDown();
            alertAvviso.slideUp();
            btnChiudi.show();
            btnIndietro.hide();
            btnProcedi.hide();
            return;
        }

        alertErr.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();
        alertAvviso.slideDown();
        btnChiudi.hide();
        btnIndietro.show();
        btnProcedi.show();
    };

    /**
     * Effettua la chiamata al servizio di salvataggio atto di legge.
     */
    exports.annullaAttoDiLegge = function() {
        // Imposta l'uid del atto di legge
        var radioButton = $("input[name=uidAttoDiLeggeRadio]:checked");
        var valoreDelRadio = radioButton.val();
        var alertAvviso = $("#divAvvisoAnnullamento");
        var alertErrori = $("#ERRORI_ANNULLA_ATTO");
        var alertInformazioni = $("#INFORMAZIONI_ANNULLA_ATTO");
        var btnChiudi = $("#btnAnnullaAttoChiudi");
        var btnIndietro = $("#btnAnnullaAttoIndietro");
        var btnProcedi = $("#btnAnnullaAttoProcedi");
        var objectDaInviare = {
                uidAttoDiLegge: valoreDelRadio
            };

        // Chiama il servizio per la ricerca della disponibilità
        $.postJSON ("attoDiLegge/annullaAttoDiLegge.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;

            if (errori !== null && errori !== undefined && errori.length > 0) {
                alertErrori.children("ul").find("li").remove().end();
                // Mostro l'alert di errore
                alertErrori.slideDown();
                // Aggiungo gli errori alla lista
                $.each(errori, function() {
                    alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                alertErrori.slideDown();
                alertInformazioni.slideUp();
                alertAvviso.slideUp();
                btnChiudi.show();
                btnIndietro.hide();
                btnProcedi.hide();
                // Ritorno indietro
                return;
            }

            if (informazioni !== null && informazioni !== undefined && informazioni.length > 0)  {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                $('#divInserisciAttoDiLegge').find('input, textarea, button, select').attr('disabled','disabled');

                btnChiudi.show();
                btnIndietro.hide();
                btnProcedi.hide();
                alertErrori.slideUp();
                alertAvviso.slideUp();

                AttoDiLegge.cercaAttoDiLegge();

                return;
            }
        });
    };

    /**
     * Effettua la chiamata Ajax al servizio di ricerca atto di legge.
     */
    exports.cercaAttoDiLegge = function() {
        var formDiRicerca = $("#formRicercaAttoDiLegge");
        var alertErrori = $("#ERRORI_CERCA_ATTO");
        var alertMessaggi = $("#MESSAGGI_CERCA_ATTO");
        var alertInformazioni = $("#INFORMAZIONI_CERCA_ATTO");
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);
        var spinner = $("#SPINNER_AttoDiLegge");

        // nasconde alert errori e messaggi
        alertErrori.slideUp();
        alertMessaggi.slideUp();
        alertInformazioni.slideUp();

        spinner.addClass("activated");

            // Chiamata AJAX
        $.postJSON("attoDiLegge/ricercaAttoDiLegge.do", formDiRicerca.serializeArray(), function (data) {
            var errori = data.errori;
            var messaggi = data.messaggi;
            var informazioni = data.informazioni;
            var listaAttoDiLegge = data.listaAttoDiLegge;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                return;
            }

            // Pulisco il dataTable prima di inizializzarne uno nuovo
            if(tabelleGiaInDataTable.length > 0) {
                $("#risultatiRicercaAttoDiLeggeSenzaOperazioni").dataTable().fnDestroy();
            }

            if (listaAttoDiLegge === null || listaAttoDiLegge === undefined) {
                listaAttoDiLegge = "";
            }
            impostaDataTableAttoDiLegge(listaAttoDiLegge, "uidAttoDiLegge");

        }).always(function() {
            spinner.removeClass("activated");
        });
    };

    // RELAZIONE ATTO DI LEGGE - CAPITOLO
    /**
     * Effettua la preparazione per l'inserimento di una nuova relazione.
     */
    exports.preparaInserimentoRelazione = function() {
        var tabelleGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter("risultatiRicercaAttoDiLeggeSenzaOperazioni");
        // Pulisco il dataTable prima di inizializzarne uno nuovo
        if(tabelleGiaInDataTable.length > 0) {
            $("#risultatiRicercaAttoDiLeggeSenzaOperazioni").dataTable().fnDestroy();
        }

        $("#formRicercaAttoDiLegge").find('input, select').attr('disabled',false);
        $("#formRicercaAttoDiLegge").find('input').not(':button, :submit, :reset, :hidden').val('');

        exports.preparaAssociaRelazione();
    };

    /**
     * Effettua la preparazione per l'inserimento di una nuova relazione.
     */
    exports.preparaAssociaRelazione = function() {
        var formDiInserimento = $("#inserisciRelazioneAttoDiLeggeCapitolo");
        var alertErrori = $("#ERRORI_INS_RELAZIONE");
        var alertInformazioni = $("#INFORMAZIONI_INS_RELAZIONE");
        var btnAnnulla = $("#btnModalInsRelazioneAnnulla");
        var btnSalva = $("#btnModalInsRelazioneSalva");

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        btnSalva.show();
        btnAnnulla.show();

        formDiInserimento.find('input, select').attr('disabled',false);
        formDiInserimento.find('input').not(':button, :submit, :reset, :hidden').val('');
    };

    /**
     * Effettua la chiamata al servizio di salvataggio atto di legge
     */
    exports.salvaRelazioneAttoDiLeggeCapitolo = function() {
        var alertErrori = $("#ERRORI_INS_RELAZIONE");
        var alertInformazioni = $("#INFORMAZIONI_INS_RELAZIONE");
        var radioButton = $("input[name=uidAttoDiLeggeRadio]:checked");
        var valoreDelRadio = radioButton.val();
        var formDiInserimento = $("#inserisciRelazioneAttoDiLeggeCapitolo");
        var btnAnnulla = $("#btnModalInsRelazioneAnnulla");
        var btnSalva = $("#btnModalInsRelazioneSalva");

        if (valoreDelRadio === null || valoreDelRadio === undefined) {
            alertErrori.children("ul").find("li").remove().end();
               // Mostro l'alert di errore
            alertErrori.children("ul").append($("<li/>").html("E' necessario selezionare un atto di legge per procedere!"));
            alertErrori.slideDown();
            return;
        }

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Imposta uidAttoDiLegge da radio button
        $("#uidAttoDiLegge").val(valoreDelRadio);

        // Imposta uid Capitolo da pagina principale aggiorna
        var uidCapitoloAggiornare = $("#uidCapitoloDaAggiornare").val();

        $("#uidCapitolo").val(uidCapitoloAggiornare);

            // Chiamata AJAX
        $.postJSON("attoDiLegge/associaAttoDiLeggeAlCapitolo.do", formDiInserimento.serializeArray(), function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                // Ricarica la lista delle Relazioni in essere
                cercaRelazioniAttoDiLegge();

                formDiInserimento.find('input, select').find('input').not(':button, :submit, :reset, :hidden').attr('disabled','disabled');

                btnAnnulla.hide();
                btnSalva.hide();

                // Pulisco il dataTable
                $($.fn.dataTable.fnTables(true)).filter("#risultatiRicercaAttoDiLeggeSenzaOperazioni").dataTable().fnClearTable();
                $($.fn.dataTable.fnTables(true)).filter("#risultatiRicercaAttoDiLeggeSenzaOperazioni").dataTable().fnDestroy();

                // Chiudo i modali
                $("#relazioneAttoDiLeggeCapitolo").collapse("hide");
                $("#divInsNuovaRelazione").collapse("hide");

                // Mostro l'alert di successo
                alertInformazioni.slideDown();
                return;
            }
        });
    };

    /**
     * Effettua la chiamata al servizio di salvataggio atto di legge
     */
    exports.annullaRelazioneAttoDiLegge = function() {
        // Imposta l'uid del atto di legge
        var alertAvviso = $("#divAvvisoAnnullamentoRel");
        var alertErrori = $("#ERRORI_ANNULLA_REL");
        var alertInformazioni = $("#INFORMAZIONI_ANNULLA_REL");
        var btnChiudi = $("#btnAnnullaRelChiudi");
        var btnIndietro = $("#btnAnnullaRelIndietro");
        var btnProcedi = $("#btnAnnullaRelProcedi");
        var uidRelazioneSelezionata = $("input[name='relazioneAttoDiLeggeCapitolo.uid']").val();
        var objectDaInviare = {
            uidAttoDiLeggeCapitolo: uidRelazioneSelezionata
        };

        alertAvviso.slideUp();
        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Chiama il servizio per la ricerca della disponibilità
        $.postJSON ("attoDiLegge/annullaRelazioneAttoDiLeggeCapitolo.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;

            if (errori !== null && errori !== undefined && errori.length > 0) {
                alertErrori.children("ul").find("li").remove().end();
                // Mostro l'alert di errore
                alertErrori.slideDown();
                // Aggiungo gli errori alla lista
                $.each(errori, function() {
                    alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                alertErrori.slideDown();
                alertInformazioni.slideUp();
                alertAvviso.slideUp();
                btnChiudi.show();
                btnIndietro.hide();
                btnProcedi.hide();
                // Ritorno indietro
                return;
            }

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                $('#divInserisciAttoDiLegge').find('input, textarea, button, select').attr('disabled','disabled');

                btnChiudi.show();
                btnIndietro.hide();
                btnProcedi.hide();

                // Ricarica la lista delle Relazioni in essere
                cercaRelazioniAttoDiLegge();

                return;
            }
        });
    };

    /**
     * Prepara Aggiorna Relazione.
     */
    exports.clickOnAnnullaAggiornaRelazione = function() {
        $("#divAggiornaRelazioneAttoDiLeggeCapitolo").slideUp();
        $("input[name='relazioneAttoDiLeggeCapitolo.uid']").val('');
        $("input[name='relazioneAttoDiLeggeCapitolo.uidAttoDiLegge']").val('');
        $("input[name='relazioneAttoDiLeggeCapitolo.gerarchia']").val('');
        $("textarea[name='relazioneAttoDiLeggeCapitolo.descrizione']").val('');
        $("input[name='relazioneAttoDiLeggeCapitolo.dataInizioFinanziamento']").val('');
        $("input[name='relazioneAttoDiLeggeCapitolo.dataFineFinanziamento']").val('');
    };

    /**
     *
     * aggiornaRelazioneAttoDiLegge
     *
     * Effettua la chiamata al servizio di salvataggio atto di legge
     */
    exports.aggiornaRelazioneAttoDiLegge = function() {
        // Imposta l'uid del atto di legge
        var alertErrori = $("#ERRORI_AGGIORNA_REL");
            // Utilizzo l'alert generico per permetterne la visualizzazione anche a seguito del collasso del div
        var alertInformazioni = $("#INFORMAZIONI_CERCA_REL");

        var btnChiudi = $("#btnAggiornaRelChiudi");
        var btnAnnulla = $("#btnAggiornaRelAnnulla");
        var btnSalva = $("#btnAggiornaRelSalva");
        var btnAggiornaAtto = $("btnAggiornaAttoLegge");

        var uidRelazioneSelezionata = $("input[name='relazioneAttoDiLeggeCapitolo.uid']").val();
        var uidAttoDiLegge = $("input[name='relazioneAttoDiLeggeCapitolo.uidAttoDiLegge']").val();
        var gerarchia = $("input[name='relazioneAttoDiLeggeCapitolo.gerarchia']").val();
        var descrizione = $("textarea[name='relazioneAttoDiLeggeCapitolo.descrizione']").val();
        var dataInizioFinanziamento = $("input[name='relazioneAttoDiLeggeCapitolo.dataInizioFinanziamento']").val();
        var dataFineFinanziamento = $("input[name='relazioneAttoDiLeggeCapitolo.dataFineFinanziamento']").val();
        var uidCapitolo = $("#uidCapitoloDaAggiornare").val();
        var uidBilancio = $("input[name='bilancio.uid']").val();

        var objectDaInviare = {
                uidAttoDiLeggeCapitolo: uidRelazioneSelezionata,
                uidAttoDiLegge: uidAttoDiLegge,
                uidCapitolo: uidCapitolo,
                uidBilancio: uidBilancio,
                gerarchia: gerarchia,
                descrizione: descrizione,
                dataInizioFinanziamento: dataInizioFinanziamento,
                dataFineFinanziamento: dataFineFinanziamento
            };

        alertErrori.slideUp();
        alertInformazioni.slideUp();

        // Chiama il servizio per la ricerca della disponibilità
        $.postJSON ("attoDiLegge/aggiornaRelazioneAttoDiLeggeCapitolo.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;


            if (errori !== null && errori !== undefined && errori.length > 0) {
                alertErrori.children("ul").find("li").remove().end();
                // Mostro l'alert di errore
                alertErrori.slideDown();
                // Aggiungo gli errori alla lista
                $.each(errori, function() {
                    alertErrori.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                alertErrori.slideDown();
                alertInformazioni.slideUp();

                btnChiudi.hide();
                btnAnnulla.show();
                btnSalva.show();

                return;
            }

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                $('#aggiornaRelazioneAttoDiLeggeCapitolo').find('input, select').attr('disabled','disabled');

                btnChiudi.hide();
                btnAnnulla.hide();
                btnSalva.hide();
                btnAggiornaAtto.show();

                // Aggiorna il datatable
                cercaRelazioniAttoDiLegge();
                // Collasso il div
                $("#divAggiornaRelazioneAttoDiLeggeCapitolo").slideUp();
                // Mostro l'alert di successo
                alertInformazioni.slideDown();
                return;
            }
        });
    };

    /**
     * Prepare l'aggiornamento dell'atto di legge.
     */
    exports.preparaAggiornaAttoDiLegge = function() {
        var formDiAggiornamento = $("#formAggiornaAttoDiLegge");
        var alertErrori = $("#ERRORI_AGG_ATTO");
        var alertInformazioni = $("#INFORMAZIONI_AGG_ATTO");
        var btnChiudi = $("#btnModalAggAttoChiudi");
        var btnAnnulla = $("#btnModalAggAttoAnnulla");
        var btnSalva = $("#btnModalAggAttoSalva");
        var uidAttoDiLegge = $("input[name='relazioneAttoDiLeggeCapitolo.uidAttoDiLegge']").val();
        var objectDaInviare = {uidAttoDiLegge: uidAttoDiLegge};

        alertErrori.slideUp();

        alertInformazioni.slideUp();

        btnChiudi.hide();
        btnAnnulla.show();
        btnSalva.show();

        // Chiamata AJAX
        $.postJSON ("attoDiLegge/ricercaPuntualeAttoDiLegge.do", objectDaInviare, function (data) {
           var errori = data.errori;
           var informazioni = data.informazioni;
           var listaRisultati = data.listaAttoDiLegge;

           if (errori !== null && errori !== undefined && errori.length > 0) {
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

           if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
               // Successo nell'inserimento
               alertInformazioni.children("ul").find("li").remove().end();
               alertInformazioni.slideDown();
               $.each(informazioni, function() {
                   alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
               });

               return;
           }

           if (listaRisultati.length > 0) {
               $("input[name='aggAttoDiLegge.uid']").val(listaRisultati[0].uid);
               $("input[name='aggAttoDiLegge.anno']").val(listaRisultati[0].anno);
               $("input[name='aggAttoDiLegge.numero']").val(listaRisultati[0].numero);
               $("input[name='aggAttoDiLegge.articolo']").val(listaRisultati[0].articolo);
               $("input[name='aggAttoDiLegge.comma']").val(listaRisultati[0].comma);
               $("input[name='aggAttoDiLegge.punto']").val(listaRisultati[0].punto);
               $("select[name='aggAttoDiLegge.tipoAtto.uid']").val(listaRisultati[0].tipoAtto.uid);
           }

           formDiAggiornamento.find('input, select').attr('disabled',false);
           $("input[name='aggAttoDiLegge.anno']").attr('disabled','disabled');
           $("input[name='aggAttoDiLegge.numero']").attr('disabled','disabled');
       });
    };

    /**
     * Effettua la chiamata al servizio di salvataggio atto di legge.
     */
    exports.aggiornaAttoDiLegge = function() {
        var alertErrori = $("#ERRORI_AGG_ATTO");
        var alertInformazioni = $("#INFORMAZIONI_AGG_ATTO");

        var btnChiudi = $("#btnModalAggAttoChiudi");
        var btnAnnulla = $("#btnModalAggAttoAnnulla");
        var btnSalva = $("#btnModalAggAttoSalva");

        var objectDaInviare = {
            "attoDiLegge.uid": $("input[name='aggAttoDiLegge.uid']").val(),
            "attoDiLegge.anno": $("input[name='aggAttoDiLegge.anno']").val(),
            "attoDiLegge.numero": $("input[name='aggAttoDiLegge.numero']").val(),
            "attoDiLegge.articolo": $("input[name='aggAttoDiLegge.articolo']").val(),
            "attoDiLegge.comma": $("input[name='aggAttoDiLegge.comma']").val(),
            "attoDiLegge.punto": $("input[name='aggAttoDiLegge.punto']").val(),
            "attoDiLegge.tipoAtto.uid": $("select[name='aggAttoDiLegge.tipoAtto.uid']").val()
        };

        alertErrori.slideUp();

        alertInformazioni.slideUp();

            // Chiamata AJAX
        $.postJSON("attoDiLegge/aggiornaAttoDiLegge.do", objectDaInviare, function (data) {
            var errori = data.errori;
            var informazioni = data.informazioni;

            if (errori !== null && errori !== undefined && errori.length > 0) {
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

            if(informazioni !== null && informazioni !== undefined && informazioni.length > 0) {
                // Successo nell'inserimento
                alertInformazioni.children("ul").find("li").remove().end();
                alertInformazioni.slideDown();
                $.each(informazioni, function() {
                    alertInformazioni.children("ul").append($("<li/>").html(this.codice + " - " + this.descrizione));
                });

                $('#divAggiornaAttoDiLegge').find('input').not(':button, :hidden').attr('disabled','disabled');

                btnChiudi.show();
                btnAnnulla.hide();
                btnSalva.hide();

                // Aggiorna il datatable
                cercaRelazioniAttoDiLegge();

                return;
            }

        });
    };

    // Invoco subito la funzione
    cercaRelazioniAttoDiLegge();

    return exports;
}());

$(function() {

    // Gestione delle azioni
    $("#btnModalInsAttoSalva").on("click", AttoDiLegge.salvaNuovoAttoDiLegge);
    $("#pulsantePreparaInserisciNuovoAttoDiLegge").on("click", AttoDiLegge.preparaInserimentoAttoDiLegge);
    $("#pulsantePreparaAnnullaAttoDiLegge").on("click", AttoDiLegge.preparaAnnullaAttoDiLegge);
    $("#btnAnnullaAttoProcedi").on("click", AttoDiLegge.annullaAttoDiLegge);
    $("#btnCercaAtto").on("click", AttoDiLegge.cercaAttoDiLegge);

    $("#pulsantePreparaInserimentoRelazione").on("click", AttoDiLegge.preparaInserimentoRelazione);
    $("#pulsantePreparaAssociaRelazione").on("click", AttoDiLegge.preparaAssociaRelazione);
    $("#btnModalInsRelazioneSalva").on("click", AttoDiLegge.salvaRelazioneAttoDiLeggeCapitolo);
    $("#btnAnnullaRelProcedi").on("click", AttoDiLegge.annullaRelazioneAttoDiLegge);
    $("#btnAggiornaRelAnnulla").on("click", AttoDiLegge.clickOnAnnullaAggiornaRelazione);
    $("#btnAggiornaRelSalva").on("click", AttoDiLegge.aggiornaRelazioneAttoDiLegge);
    $("#btnAggiornaAttoLegge").on("click", AttoDiLegge.preparaAggiornaAttoDiLegge);
    $("#btnModalAggAttoSalva").on("click", AttoDiLegge.aggiornaAttoDiLegge);
});