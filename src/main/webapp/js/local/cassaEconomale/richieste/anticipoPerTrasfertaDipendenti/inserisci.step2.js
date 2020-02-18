/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";

    var alertErrori = $("#ERRORI");

    /**
     * Gestione del caricamento dell'impegno sulla pagina.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function gestioneImpegnoCaricato(e) {
        var imp = e.subimpegno ? e.subimpegno : e.impegno;
        var capitolo = imp.capitoloUscitaGestione;
        var provvedimento = imp.attoAmministrativo;

        // Ottengo la disponibilita
        var disponibilita = imp.disponibilitaLiquidare || 0;
        // Carico i dati
        // Disponibilita
        $("#SPAN_disponibileMovimentoGestione").html(disponibilita.formatMoney());

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
    }

    /**
     * Crea una stringa per l'injezione nell'HTML.
     *
     * @param obj (Object) l'oggetto da wrappare
     * @returns (String) la stringa da usare
     */
    function createStringForHtml(obj) {
        return (obj || "") + "&nbsp;";
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

    $(function() {
        $(document).on("impegnoCaricato", gestioneImpegnoCaricato);
        $("#annoMovimentoMovimentoGestione, #numeroMovimentoGestione, #numeroSubMovimentoGestione").change(valutaCaricamentoImpegno)
            // Invoco subito il change sul primo
            .first()
                .change();
        $("#pulsanteCompilazioneGuidataMovimentoGestione").click(aperturaModaleImpegno);

        // Inizializzazione dell'impegno
        Impegno.inizializza("#annoMovimentoMovimentoGestione", "#numeroMovimentoGestione", "#numeroSubMovimentoGestione", "#datiRiferimentoImpegnoSpan");
    });
}(jQuery);