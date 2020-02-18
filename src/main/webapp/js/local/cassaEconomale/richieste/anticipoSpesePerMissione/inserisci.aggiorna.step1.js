/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";

    var euroUid = parseInt($("#HIDDEN_uidValutaEuro").val());
    var isAggiornamento = $("#HIDDEN_isAggiornamento").val() === "true";

    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var alertErroriModale = $("#ERRORI_modaleGiustificativo");
    var baseUrl = $("#HIDDEN_baseUrl").val();
    /**
     * Visualizzazione degli importi di cassa.
     */
    function visualizzaImportiCassa() {
        var spinner = $("#SPINNER_pulsanteVisualizzaImporti").addClass("activated");
        $("#divImportiCassa").load(baseUrl +"_visualizzaImporti.do", {}, function() {
            spinner.removeClass("activated");
            $("#modaleImportiCassa").modal("show");
        });
    }
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
        return $.postJSON(baseUrl + "_ottieniListaGiustificativi.do", {}, function(data) {
            // Impostazione totale
            $("#totaleGiustificativi").html(data.totaleImportiGiustificativi.formatMoney());
            $("#totaleSpettanteGiustificativi").html(data.totaleImportiSpettantiGiustificativi.formatMoney());
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
        var isFromMissioneEsterna = !!$('#idMissioneEsternaRichiestaEconomale').val();
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
                {aTargets: [0], mData: function(source) {
                    return source.tipoGiustificativo && (source.tipoGiustificativo.codice + " - " + source.tipoGiustificativo.descrizione) || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.valuta && (source.valuta.codice + " - " + source.valuta.descrizione) || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return  source.cambio !== undefined && source.cambio.formatMoney() || "";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [3], mData: function(source) {
                    return source.quantita || "";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [4], mData: function(source) {
                    return source.importoGiustificativoInValuta !== undefined && source.importoGiustificativoInValuta.formatMoney() || "";
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [5], mData: function(source) {
                    return source.importoGiustificativo !== undefined && source.importoGiustificativo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [6], mData: function(source) {
                    return source.importoSpettanteGiustificativo !== undefined && source.importoSpettanteGiustificativo.formatMoney() || "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };

        if(!isAggiornamento) {
            if(isFromMissioneEsterna) {
                opts.aoColumnDefs.push({aTargets: [7], mData: function() {
                    return '';
                }});
                opts.aoColumnDefs.push({aTargets: [8], mData: function() {
                    return '';
                }});
            } else {
                opts.aoColumnDefs.push({aTargets: [7], mData: function() {
                    return "<a title='aggiorna'><i class='icon-pencil icon-2x'><span class='nascosto'>aggiorna</span></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).click([oData, iRow], aggiornaGiustificativo);
                }});
                opts.aoColumnDefs.push({aTargets: [8], mData: function() {
                    return "<a title='elimina'><i class='icon-trash icon-2x'><span class='nascosto'>elimina</span></i></a>";
                }, fnCreatedCell: function(nTd, sData, oData, iRow) {
                    $("a", nTd).click([oData, iRow], eliminaGiustificativo);
                }});
            }
        }
        $("#tabellaGiustificativi").dataTable(opts);
    }

    /**
     * Aggiornamento del giustificativo.
     *
     * @param e (Event) l'eveno scatenante l'invocazione della funzione
     */
    function aggiornaGiustificativo(e) {
        var fieldset = $("#fieldsetModaleGiustificativo");
        var giustificativo = e.data[0];
        var row = +e.data[1];

        // Ciclo sui campi per impostarne i valori
        fieldset.find(":input[name^='giustificativo.']").each(function(idx, el) {
            var $el = $(el);
            var name = $el.attr("name").split("\.").slice(1);
            var value = giustificativo;
            // Ricorsivamente sull'oggetto
            for(var i in name) {
                if(name.hasOwnProperty(i)) {
                    value = value ? value[name[i]] : "";
                }
            }
            if($el.hasClass('decimale')) {
                value = formatMoney(value);
            } else if($el.data("date")) {
                value = formatDate(value);
            }
            $el.val(value).blur();
        });

        alertErroriModale.slideUp();
        $("#valutaGiustificativo").val(euroUid);
        $("#labelModaleGiustificativo").html("Aggiornamento giustificativo numero " + (row + 1));
        $("#confermaModaleGiustificativo").substituteHandler("click", function() {
            effettuaAggiornamentoGiustificativo(row);
        });
        $("#modaleGiustificativo").modal("show");
    }

    /**
     * Formattazione in valuta
     * @param el (any) il campo da formattare
     * @returns (any) il numero formattato, o il campo in input se non numerico
     */
    function formatMoney(el) {
        return typeof el === 'number' ? el.formatMoney() : el;
    }

    /**
     * Effettua l'aggiornamento del giustificativo.
     *
     * @param row (Number) il numero di riga
     */
    function effettuaAggiornamentoGiustificativo(row) {
        var obj = $("#fieldsetModaleGiustificativo").serializeObject();
        obj.rowNumber = row;

        // Chiudo l'alert degli errori
        alertErroriModale.slideUp();

        // Chiamata al servizio
        $.postJSON(baseUrl + "_updateGiustificativo.do", obj, function(data) {
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
        $.postJSON(baseUrl + "_removeGiustificativo.do", {rowNumber: row}, function(data) {
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
        $.postJSON(baseUrl + "_addGiustificativo.do", obj, function(data) {
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
     * Calcolo dell'importo spettante
     */
    function calcoloSpettante() {
        // Prendo il contenitore
        var $this = $(this);
        var container = $this.closest("#fieldsetModaleGiustificativo, tr");
        var percentuale = container.find("select[data-calcolo-spettante]").find("option:selected").data("spettante");
        var importo = container.find("input[data-calcolo-spettante]").val();
        var campoImportoSpettante = container.find("input[data-importo-spettante]");
        var importoSpettante;
        var percentualeNum;
        var importoNum;

        if(!percentuale || ! importo) {
            campoImportoSpettante.val("");
            return;
        }
        // Calcolo i dati
        percentualeNum = parseFloat(parseLocalNum(percentuale));
        importoNum = parseFloat(parseLocalNum(importo));

        // Importo spettante = importo * percentuale / 100
        importoSpettante = importoNum * percentualeNum / 100.0;

        // Imposto l'importo spettante
        campoImportoSpettante.val(importoSpettante.formatMoney());
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

    /**
     * Svuota tutti i campi del form
     */
    function puliziaForm() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }

    /**
     * Gestione della missione esterna.
     *
     * @param e      (Event)  l'evento scatenante l'invocazione
     * @param params (Object) i parametri dell'evento
     */
    function gestioneMissioneEsterna(e, params) {
        var idMissioneEsterna = params.idMissioneEsterna;
        var callback = params.callback;

        $.postJSON('inserisciAnticipoSpesePerMissioneCassaEconomale_caricaDettaglioRichiestaEconomaleEsterna.do', {'idMissioneEsterna': idMissioneEsterna})
        .then(popolaDaMissioneEsterna)
        .then(callback);
    }

    /**
     * Popolamento della pagina da missione esterna.
     */
    function popolaDaMissioneEsterna(data) {
        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            return;
        }
        var richiesta = qualify(data.richiestaEconomale, 'richiestaEconomale');

        $('input, textarea', '#formRichiestaEconomale').each(elaboraMissioneEsternaPerInput.bind(null, richiesta));
        $('button, select', '#formRichiestaEconomale').each(elaboraMissioneEsternaPerNonInput.bind(null, richiesta));

        // Elimino le compilazioni guidate
        $('#pulsanteCompilazioneGuidataMatricola, #pulsanteInserimentoDati').remove();
        $('#matricolaSoggettoRichiestaEconomale').change();
        // Carico via AJAX i giustificativi e aspetto la risposta
        return caricaListaGiustificativi();
    }

    /**
     * Elaborazione della missione esterna per i campi input.
     *
     * @param richiesta (Object)  la richiesta economale
     * @param index     (Number)  l'indice dell'elemento
     * @param element   (Element) l'elemento DOM
     */
    function elaboraMissioneEsternaPerInput(richiesta, index, element) {
        var name = element.name;
        var type = element.type;
        var $el;
        var value;
        // Ignoro a tutti i costi i submit
        if(type === 'submit') {
            return;
        }
        $el = $(element);
        // Mi proteggo dai problemi delle date
        value = $el.is('.datepicker') ? formatDate(richiesta[name]) : richiesta[name];

        if(type === 'radio') {
            $el.prop('checked', $el.val() === value.toString());
        } else {
            $el.val(value);
        }
        $el.attr('disabled', true);
    }

    /**
     * Elaborazione della missione esterna per i campi non-input.
     *
     * @param richiesta (Object)  la richiesta economale
     * @param index     (Number)  l'indice dell'elemento
     * @param element   (Element) l'elemento DOM
     */
    function elaboraMissioneEsternaPerNonInput(richiesta, index, element) {
        var $el = $(element);
        $el.attr('disabled', true);
        if($el.is('select.chosen-select')) {
            $el.trigger("chosen:updated");
        }
    }

    function gestioneQuantitaGiustificativo() {
        var tipo = $('#tipoGiustificativoGiustificativo').find('option:selected');
        var quantita = $('#quantitaGiustificativo').val();
        var importo = tipo.data('importo');
        var total;
        if(!tipo.length || !importo || +importo === 0 || !quantita) {
            // Non ho il tipo, l'importo o la quantita'
            return;
        }
        total = parseLocalNum(importo) * quantita || 0;
        $('#importoGiustificativoGiustificativo').val(total.formatMoney()).change();
    }

    $(function() {
        $("#pulsanteCopiaRichiestaEconomale").click(copiaRichiestaEconomale);
        $("#pulsanteInserimentoDati").click(apriModaleNuovoGiustificativo);
        $("#confermaModaleGiustificativo").click(inserisciGiustificativo);
        $("#pulsanteAnnullaStep1").click(puliziaForm);
        $(document).on("change", "select[data-valuta]", abilitazioneCampiValuta)
            .on("change", "*[data-calcolo-spettante]", calcoloSpettante)
            .on('selectedMissioneEsterna', gestioneMissioneEsterna);
        $('#tipoGiustificativoGiustificativo, #quantitaGiustificativo').change(gestioneQuantitaGiustificativo);

        // Carica la lista dei giustificativi dalla action
        caricaListaGiustificativi();
        // Inizializzazione della gestione della matricola
        global.Matricola.inizializza("#matricolaSoggettoRichiestaEconomale", "#descrizioneMatricola", "#pulsanteCompilazioneGuidataMatricola", "#HIDDEN_codiceAmbito", "#HIDDEN_maySearchHR");
        if(global.MissioneEsterna) {
            global.MissioneEsterna.inizializza('#idMissioneEsternaRichiestaEconomale', '#pulsanteInserisciRichiestaDaCaricamentoEsterno');
        }
        $("#pulsanteVisualizzaImporti").substituteHandler("click", visualizzaImportiCassa);
    });
}(jQuery, this);