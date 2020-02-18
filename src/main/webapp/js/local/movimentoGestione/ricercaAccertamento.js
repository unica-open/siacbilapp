/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Accertamento = (function () {
    "use strict";

    var exports = {};

    var campoInCuiApplicareAnnoMovimento;
    var campoInCuiApplicareNumeroMovimento;
    var campoInCuiApplicareNumeroSubMovimento;
    var campoInCuiApplicareLaDescrizione;
    var campoInCuiApplicareDisponibilita;

    var modale = $("#modaleAccertamento");
    var alertErroriModale = $("#ERRORI_ACCERTAMENTO_MODALE");
    var campoRicercaEffettuataConSuccesso = $("#hidden_ricercaEffettuataConSuccessoModaleAccertamento");
    var isGestioneUEB = $("#HIDDEN_gestioneUEB").val() === "true";

    /**
     * Imposta la tabella dei subaccertamenti.
     *
     * @param accertamento (Object) l'accertamento da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubaccertamentiNellaTabella(accertamento) {
        var options = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            aaData: accertamento.elencoSubAccertamenti,
            oLanguage: {
                sInfo : "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty : "0 risultati",
                sProcessing : "Attendere prego...",
                sZeroRecords : "Non sono presenti subaccertamenti associati",
                oPaginate : {
                    sFirst : "inizio",
                    sLast : "fine",
                    sNext : "succ.",
                    sPrevious : "prec.",
                    sEmptyTable : "Nessun dato disponibile"
                }
            },
            aoColumnDefs : [
                {aTargets: [0], mData: function(source) {
                    if(!source.stringaRadio) {
                        source.stringaRadio = "<input type='radio' name='radio_modale_accertamento' />";
                    }
                    return source.stringaRadio;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd)
                        .data("originalSubAccertamento", oData);
                }},
                {aTargets: [1], mData: function(source) {
                    return source.numero || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.descrizione || "";
                }},
                {aTargets: [3], mData: function(source) {
                    var result = "";
                    if(source.soggetto && source.soggetto.uid) {
                        result = source.soggetto.codiceSoggetto + " - " + source.soggetto.denominazione;
                    }
                    return result;
                }}, // LIQUIDAZIONE
                {aTargets: [4], mData: function(source) {
                    return source.importoAttuale.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [5], mData: function(source) {
                    return source.disponibilitaIncassare ? source.disponibilitaIncassare.formatMoney() : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };

        $("#tabellaAccertamentiModale").data("originalAccertamento", accertamento)
            .dataTable(options);
    }

    function impostaAccertamentoNellaTabella(accertamento) {
        var capitolo="";
        var provvedimento="";
        var soggetto="";
        var importo;
        var importoAttuale = accertamento.importoAttuale || 0;

        var disponibile = accertamento.disponibilitaIncassare || 0;

        importo = importoAttuale.formatMoney();
        if (accertamento.importoIniziale) {
            importo += " (iniziale: " + accertamento.importoIniziale.formatMoney() + ")";
        }

        if(accertamento.capitoloEntrataGestione && accertamento.capitoloEntrataGestione.uid !== 0){
            capitolo = accertamento.capitoloEntrataGestione.annoCapitolo
                + "/" + accertamento.capitoloEntrataGestione.numeroCapitolo
                + "/" + accertamento.capitoloEntrataGestione.numeroArticolo;
            capitolo += isGestioneUEB ? accertamento.capitoloEntrataGestione.numeroUEB : "";
            capitolo += " - " + accertamento.capitoloEntrataGestione.descrizione;
            capitolo = accertamento.capitoloEntrataGestione.strutturaAmministrativoContabile ? capitolo + " - " +  accertamento.capitoloEntrataGestione.strutturaAmministrativoContabile.codice : capitolo;
            capitolo = accertamento.capitoloEntrataGestione.tipoFinanziamento   ? capitolo + " - Tipo finanziamento: " + accertamento.capitoloEntrataGestione.tipoFinanziamento.codice : capitolo;
        }

        if(accertamento.attoAmministrativo){
            provvedimento = accertamento.attoAmministrativo.anno + "/"+ accertamento.attoAmministrativo.numero;
            provvedimento = accertamento.attoAmministrativo.tipoAtto ? provvedimento + " - " + accertamento.attoAmministrativo.tipoAtto.descrizione : provvedimento;
            provvedimento = provvedimento + " - " + accertamento.attoAmministrativo.oggetto;
            provvedimento = accertamento.attoAmministrativo.strutturaAmmContabile ? provvedimento + " - " + accertamento.attoAmministrativo.strutturaAmmContabile.codice : provvedimento;
        }

        if(accertamento.soggetto && accertamento.soggetto.uid){
            soggetto = accertamento.soggetto.codiceSoggetto + " - " +  accertamento.soggetto.denominazione;
            soggetto = accertamento.soggetto.codiceFiscale ? soggetto + " - CF: " + accertamento.soggetto.codiceFiscale : soggetto;
            soggetto = accertamento.soggetto.partitaIva ? soggetto + " - P.IVA: " + accertamento.soggetto.partitaIva : soggetto;
        }else if (accertamento.classeSoggetto){
            soggetto = "Classe: " + accertamento.classeSoggetto.codice + " - " + accertamento.classeSoggetto.descrizione;
        }

        $("#tabellaAccertamento_tdCapitolo").html(capitolo);
        $("#tabellaAccertamento_tdProvvedimento").html(provvedimento);
        $("#tabellaAccertamento_tdSoggetto").html(soggetto);
        $("#tabellaAccertamento_tdImporto").html(importo);
        $("#tabellaAccertamento_tdDisponibile").html(disponibile.formatMoney());
    }

    /**
     * Richiama l'esecuzione della ricerca Accertamento.
     */
    function ricercaAccertamento() {
        var anno = $("#annoAccertamentoModale").val();
        var numero = $("#numeroAccertamentoModale").val();
        var oggettoPerChiamataAjax;
        var spinner = $("#SPINNER_pulsanteRicercaAccertamentoModale");
        var arrayErrori = [];

        if(isNaN(parseInt(anno, 10))) {
            arrayErrori.push("COR_ERR_0002 - Dato obbligatorio omesso: Anno");
        }
        if(isNaN(parseInt(numero, 10))) {
            arrayErrori.push("COR_ERR_0002 - Dato obbligatorio omesso: Numero");
        }

        if(impostaDatiNegliAlert(arrayErrori, alertErroriModale, false)) {
            return;
        }

        oggettoPerChiamataAjax = unqualify($("#FIELDSET_modaleAccertamento").serializeObject(), 1);
        spinner.addClass("activated");
        alertErroriModale.slideUp();
        $("#divAccertamentiTrovati").slideUp();

        $.postJSON(
            "ricercaAccertamentoPerChiave.do",
            oggettoPerChiamataAjax,
            function(data){
                if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                    return;
                }
                impostaAccertamentoNellaTabella(data.accertamento);
                impostaSubaccertamentiNellaTabella(data.accertamento);
                campoRicercaEffettuataConSuccesso.val("true");
                $("#hidden_disponibileAccertamento").val(data.accertamento.disponibilitaIncassare);
                $("#divAccertamentiTrovati").slideDown();
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Conferma l'accertamento selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaAccertamento(e) {
        var checkedRadio = $("#tabellaAccertamentiModale").find("input[name='radio_modale_accertamento']:checked");
        var accertamento;
        var subaccertamento;
        var annoAccertamento;
        var numeroAccertamento;
        var numeroSubaccertamento;
        var str;
        var zero = 0;

        var disponibilita;
        var event;

        e.preventDefault();

        if(!campoRicercaEffettuataConSuccesso.val()) {
            impostaDatiNegliAlert(["Necessario selezionare un accertamento"], alertErroriModale, false);
            return;
        }

        accertamento = $("#tabellaAccertamentiModale").data("originalAccertamento");
        subaccertamento = checkedRadio.length > 0 && checkedRadio.data("originalSubAccertamento");

        annoAccertamento = $("#annoAccertamentoModale").val();
        numeroAccertamento = $("#numeroAccertamentoModale").val();
        numeroSubaccertamento = !!subaccertamento ? subaccertamento.numero : "";

        disponibilita = ((!!subaccertamento ? subaccertamento.disponibilitaIncassare : parseFloat($("#hidden_disponibileAccertamento").val())) || zero).formatMoney();

        campoInCuiApplicareAnnoMovimento.val(annoAccertamento).attr("readOnly", true);
        campoInCuiApplicareNumeroMovimento.val(numeroAccertamento).attr("readOnly", true);
        campoInCuiApplicareNumeroSubMovimento.val(numeroSubaccertamento).attr("readOnly", true);

        campoInCuiApplicareDisponibilita.html(disponibilita);

        str = ": " + annoAccertamento + " / " + numeroAccertamento;
        if(subaccertamento) {
            str += " - " + numeroSubaccertamento;
        }

        campoInCuiApplicareLaDescrizione.html(str);

        event = $.Event("accertamentoCaricato", {'accertamento': accertamento, 'subaccertamento': subaccertamento});
        $(document).trigger(event);
        modale.modal("hide");
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoAnnoMovimento      (String) il campo ove impostare l'anno del movimento (Optional - default: #annoMovimentoMovimentoGestione)
     * @param campoNumeroMovimento    (String) il campo ove impostare il numero del movimento (Optional - default: #numeroMovimentoGestione)
     * @param campoNumeroSubMovimento (String) il campo ove impostare la denominazione (Optional - default: #numeroSubMovimentoGestione)
     * @param campoDescrizione        (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #datiRiferimentoImpegnoSpan)
     *
     * @param spanDisponibilita       (String) il campo ove impostare la disponibilita (Optional - default: '')
     */
    exports.inizializza = function(campoAnnoMovimento, campoNumeroMovimento, campoNumeroSubMovimento, campoDescrizione, spanDisponibilita) {
        campoInCuiApplicareAnnoMovimento = $(campoAnnoMovimento || "#annoMovimentoMovimentoGestione");
        campoInCuiApplicareNumeroMovimento = $(campoNumeroMovimento || "#numeroMovimentoGestione");
        campoInCuiApplicareNumeroSubMovimento = $(campoNumeroSubMovimento || "#numeroSubMovimentoGestione");
        campoInCuiApplicareLaDescrizione = $(campoDescrizione || "#datiRiferimentoImpegnoSpan");

        campoInCuiApplicareDisponibilita = $(spanDisponibilita || "");

        $("#pulsanteRicercaAccertamentoModale").substituteHandler("click", ricercaAccertamento);
        $("#pulsanteConfermaModaleAccertamento").substituteHandler("click", confermaAccertamento);

        modale.on("shown", function() {
            $("#divAccertamentiTrovati").slideUp();
            campoRicercaEffettuataConSuccesso.val("");
        }).on("hidden", function() {
            alertErroriModale.slideUp();
        });
    };

    return exports;
}());