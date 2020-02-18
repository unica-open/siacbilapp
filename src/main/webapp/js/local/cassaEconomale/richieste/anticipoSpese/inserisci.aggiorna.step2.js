/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var alertErrori = $("#ERRORI");
    var alertErroriIban = $("#ERRORI_modaleSelezioneIban");
    var baseUrl = $("#HIDDEN_baseUrl").val();

    /**
     * Gestione del caricamento dell'impegno sulla pagina.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function gestioneImpegnoCaricato(e) {
        var imp = e.subimpegno ? e.subimpegno : e.impegno;
        var impPerCapitolo = e.impegno;
        var capitolo = impPerCapitolo.capitoloUscitaGestione;
        var provvedimento = imp.attoAmministrativo;

        // Ottengo la disponibilita
        var disponibilita = imp.disponibilitaLiquidare || 0;
        // Carico i dati
        // Disponibilita
        $("#SPAN_disponibileMovimentoGestione").html(disponibilita.formatMoney());
        // SIAC-5623: flag cassa economale
        $('#SPAN_flagCassaEconomaleMovimentoGestione').html(imp.flagCassaEconomale ? "SI" : "NO");

        // Pulisco i campi prima di riscriverli
        $("#datiCapitoloStrutturaMovimentoGestione dl").html("");
        // Capitolo
        if(capitolo) {
            $("#numeroCapitoloCapitolo").html(createStringForHtml(capitolo.numeroCapitolo));
            $("#numeroArticoloCapitolo").html(createStringForHtml(capitolo.numeroArticolo));
            $("#descrizioneCapitolo").html(createStringForHtml(capitolo.descrizione));
            $("#numeroUEBCapitolo").html(createStringForHtml(capitolo.numeroUEB));
            $("#strutturaAmministrativoContabileCapitolo").html(createStringForHtml(capitolo.strutturaAmministrativoContabile && capitolo.strutturaAmministrativoContabile.codice || ""));
            $("#tipoFinanziamentoCapitolo").html(createStringForHtml(capitolo.tipoFinanziamento && capitolo.tipoFinanziamento.codice || ""));
        }
        // Atto Amministrativo
        if(provvedimento) {
            $("#tipoAttoAttoAmministrativo").html(createStringForHtml(provvedimento.tipoAtto && provvedimento.tipoAtto.codice || ""));
            $("#annoAttoAmministrativo").html(createStringForHtml(provvedimento.anno));
            $("#numeroAttoAmministrativo").html(createStringForHtml(provvedimento.numero));
            $("#strutturaAmmContabileAttoAmministrativo").html(createStringForHtml(provvedimento.strutturaAmmContabile && provvedimento.strutturaAmmContabile.codice || ""));
            $("#oggettoAttoAmministrativo").html(createStringForHtml(provvedimento.oggetto));
            $("#statoOperativoAttoAmministrativo").html(createStringForHtml(provvedimento.statoOperativo));
        }

        // Mostro i dati
        $("#containerDisponibileMovimentoGestione").removeClass("hide");
        $("#datiCapitoloStrutturaMovimentoGestione").removeClass("hide");
        $('#containerFlagCassaEconomale').removeClass('hide');
    }

    /**
     * Crea una stringa per l'injezione nell'HTML.
     *
     * @param obj (Object) l'oggetto da wrappare
     * @returns (String) la stringa da usare
     */
    function createStringForHtml(obj) {
    	return obj !== undefined ? obj + '&nbsp;' : '';
    }

    /**
     * Valuta se si debba effettuare il caricamento dell'impegno.
     */
    function valutaCaricamentoImpegno() {
        var annoMovimento = $("#annoMovimentoMovimentoGestione");
        var numeroMovimento = $("#numeroMovimentoGestione");
        var numeroSubMovimento = $("#numeroSubMovimentoGestione");
        var numeroSubMovimentoString = numeroSubMovimento.val();
        var numeroSubmovimentoNumber = numeroSubMovimentoString !== "" ? +numeroSubMovimentoString : "";
        // L'unione dei tre campi
        var joined = annoMovimento.add(numeroMovimento).add(numeroSubMovimento);
        var obj;

        // Se non ho almeno anno + numeroMovimento, esco
        if(!annoMovimento.val() || !numeroMovimento.val()) {
            return;
        }

        // Popolo l'oggetto per la chiamata AJAX
        obj = {"impegno.annoMovimento": annoMovimento.val(), "impegno.numero": numeroMovimento.val()};
        // Attivo gli overlay
        joined.overlay("show");

        $.postJSON("ricercaImpegnoPerChiaveOttimizzato.do", obj, function(data) {
            var event;
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                $("#containerDisponibileMovimentoGestione").addClass("hide");
                $("#datiCapitoloStrutturaMovimentoGestione").addClass("hide");
                $('#containerFlagCassaEconomale').addClass('hide');
                return;
            }
            event = $.Event("impegnoCaricato", {'impegno': data.impegno});
            if(data.impegno.elencoSubImpegni) {
                // Imposto anche il subimpegno
                $.each(data.impegno.elencoSubImpegni, function() {
                    if(this.numero === numeroSubmovimentoNumber) {
                        event.subimpegno = this;
                    }
                });
            }
            $(document).trigger(event);
        }).always(function() {
            joined.overlay("hide");
        });
    }

    /**
     * Apertura del modale dell'impegno.
     */
    function aperturaModaleImpegno() {
        // Copia dei campi
        $("#annoImpegnoModale").val($("#annoMovimentoMovimentoGestione").val());
        $("#numeroImpegnoModale").val($("#numeroMovimentoGestione").val());

        // Chiudo il div degli impegni trovati
        $("#ERRORI_IMPEGNO_MODALE").slideUp();
        $("#divImpegniTrovati").slideUp();
        $("#modaleImpegno").modal("show")
            .find("input[id^='hidden']")
                .val("");
    }

    /**
     * Caricamento dati  mod pagamento cassa
     * */
    function impostaModalitaPagamentoDipendenteMovimento(){
    	var campoModalitaPagamentoCassa = $("#modalitaPagamentoCassaMovimento");
    	var campoModalitaPagamentoDipendente = $("#modalitaPagamentoDipendenteMovimento");
    	 var obj;
         var url;
         if(!campoModalitaPagamentoCassa.val()) {
             // svupoto il campo dipendente 
        	 campoModalitaPagamentoDipendente.val("");
             return;
         }
         url = baseUrl + "_caricaModalitaPagamentoDipendenteDaCassa.do";
         obj = campoModalitaPagamentoCassa.serializeObject();
         campoModalitaPagamentoCassa.overlay("show");
      // Invocazione del servizio
         $.postJSON(url, obj, function(data) {
             var movimento;
             if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 // Errori presenti. Esco
                 return;
             }
             if(data.messaggi.length && data.listaModalitaPagamentoSoggettoDifferenteIban && data.listaModalitaPagamentoSoggettoDifferenteIban.length) {
                 // Devo far selezionare l'IBAN all'utente
                 selezioneIban(data.listaModalitaPagamentoSoggettoDifferenteIban);
                 return;
             }
             movimento = data.richiestaEconomale && data.richiestaEconomale.movimento;
             // Popolo i dati
             $("#modalitaPagamentoDipendenteMovimento").val(data.richiestaEconomale.movimento.modalitaPagamentoDipendente && data.richiestaEconomale.movimento.modalitaPagamentoDipendente.uid || "");
             $("#modalitaPagamentoDipendenteMovimento").change();
              }).always(function() {
        	 campoModalitaPagamentoCassa.overlay("hide");
         });
    }
    /**
     * Caricamento dei dati della modalita' di pagamento.
     */
    function caricaDatiModalitaPagamentoSoggetto() {
        var campoModalitaPagamento = $("#modalitaPagamentoDipendenteMovimento");
        var modalitaPagamentoSoggetto = $("#modalitaPagamentoSoggettoMovimento");
        var obj;
        var url;
        if(!campoModalitaPagamento.val()) {
            // Non ho nulla da fare
            return;
        }
        url = baseUrl + "_caricaDettaglioPagamento.do";
        obj = campoModalitaPagamento.serializeObject();
        modalitaPagamentoSoggetto.val("");
        campoModalitaPagamento.overlay("show");

        // Invocazione del servizio
        $.postJSON(url, obj, function(data) {
            var movimento;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Errori presenti. Esco
                return;
            }
            if(data.messaggi.length && data.listaModalitaPagamentoSoggettoDifferenteIban && data.listaModalitaPagamentoSoggettoDifferenteIban.length) {
                // Devo far selezionare l'IBAN all'utente
                selezioneIban(data.listaModalitaPagamentoSoggettoDifferenteIban);
                return;
            }
            movimento = data.richiestaEconomale && data.richiestaEconomale.movimento;
            // Popolo i dati
            $("#dettaglioPagamentoMovimento").val(movimento && movimento.dettaglioPagamento || "");
            $("#bicMovimento").val(movimento && movimento.bic || "");
            $("#contoCorrenteMovimento").val(movimento && movimento.contoCorrente || "");
            modalitaPagamentoSoggetto.val(data.richiestaEconomale.movimento.modalitaPagamentoSoggetto && data.richiestaEconomale.movimento.modalitaPagamentoSoggetto.uid || "");
        }).always(function() {
            campoModalitaPagamento.overlay("hide");
        });
    }

    /**
     * Selezione degli IBAN.
     */
    function selezioneIban(list) {
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
                sZeroRecords: "Nessun IBAN associato",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnPreDrawCallback: function() {
                $("#tabellaModaleSelezioneIban_processing").closest(".row-fluid.span12").removeClass("hide");
            },
            fnDrawCallback: function() {
                $("#tabellaModaleSelezioneIban_processing").closest(".row-fluid.span12").addClass("hide");
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return "<input type='radio' name='modalitaPagamentoSoggetto.uid' value='" + source.uid + "'/>";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.iban || "";
                }}
            ]
        };
        $("#tabellaModaleSelezioneIban").dataTable(opts);
        // Apro il modale come statico
        $("#modaleSelezioneIban").modal({
            backdrop: "static",
            keyboard: false,
            show:     true
        });
    }

    /**
     * Callback per la selezione dell'IBAN.
     */
    function ibanSelezionato() {
        var checked = $("input[type='radio'][name='modalitaPagamentoSoggetto.uid']:checked", "#tabellaModaleSelezioneIban");
        var spinner = $("#spinnerModaleSelezioneIban");
        var modalitaPagamentoSoggetto = $("#modalitaPagamentoSoggettoMovimento");
        var obj;
        if(!checked.length) {
            impostaDatiNegliAlert(["Necessario selezionare un IBAN", alertErroriIban]);
            return;
        }

        // Attivo lo spinner
        spinner.addClass("activated");
        obj = checked.serializeObject();
        modalitaPagamentoSoggetto.val("");
        $.postJSON(baseUrl + "_selezionaIban.do", obj, function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErroriIban)) {
                return;
            }
            // Altro da fare?
            $("#dettaglioPagamentoMovimento").val(data.richiestaEconomale.movimento.dettaglioPagamento);
            $("#bicMovimento").val(data.richiestaEconomale.movimento.bic);
            modalitaPagamentoSoggetto.val(data.richiestaEconomale.movimento.modalitaPagamentoSoggetto && data.richiestaEconomale.movimento.modalitaPagamentoSoggetto.uid || "");
            // Chiusura del modale
            $("#modaleSelezioneIban").modal("hide");
        }).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Ripristina tutti i campi del form
     */
    function puliziaForm() {
        var action = $(this).data("href");
        // Invio il form
        $("#formRichiestaEconomale").attr("action", action)
            .submit();
    }

    $(function() {
        $(document).on("impegnoCaricato", gestioneImpegnoCaricato);
        $("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione, #numeroSubMovimentoGestione").change(valutaCaricamentoImpegno)
            // Invoco subito il change sul primo
            .first()
                .change();
        $("#modalitaPagamentoCassaMovimento").change(impostaModalitaPagamentoDipendenteMovimento);
        $("#modalitaPagamentoDipendenteMovimento").change(caricaDatiModalitaPagamentoSoggetto);
        $("#pulsanteCompilazioneGuidataMovimentoGestione").click(aperturaModaleImpegno);
        $("#pulsanteConfermaModaleSelezioneIban").click(ibanSelezionato);

        $("#pulsanteAnnullaStep2").click(puliziaForm);

        // Inizializzazione dell'impegno
        Impegno.inizializza("#annoMovimentoMovimentoGestione", "#numeroMovimentoGestione", "#numeroSubMovimentoGestione", "#datiRiferimentoImpegnoSpan");
    });
}(jQuery);