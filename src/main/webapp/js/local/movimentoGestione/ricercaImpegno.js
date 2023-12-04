/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Impegno = (function () {
    "use strict";

    var exports = {};

    var alertErroriModale = $("#ERRORI_IMPEGNO_MODALE");

    var campoInCuiApplicareAnnoMovimento;
    var campoInCuiApplicareNumeroMovimento;
    var campoInCuiApplicareNumeroSubMovimento;
    var campoInCuiApplicareLaDescrizione;
    var campoInCuiApplicareCig;
    var campoInCuiApplicareCup;
    var campoInCuiApplicareDisponibilita;

    var campoRicercaEffettuataConSuccesso = $("#hidden_ricercaEffettuataConSuccessoModaleImpegno");
    var isGestioneUEB = $("#HIDDEN_gestioneUEB").val() === "true";

    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bFilter: false,
        bProcessing: true,
        bDestroy: true,
        oLanguage: {
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty : "0 risultati",
            sProcessing : "Attendere prego...",
            oPaginate: {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec."
            }
        }
    };

    /**
     * Imposta la tabella dei subimpegni.
     *
     * @param impegno (Object) l'impegno da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubimpegniNellaTabella(impegno) {
        var opts = {
            aaData: impegno.elencoSubImpegni,
            oLanguage: {
                sZeroRecords : "Non sono presenti subimpegni associati",
                oPaginate : {
                    sEmptyTable : "Nessun subimpegno disponibile"
                }
            },
            aoColumnDefs : [
                {aTargets: [0], mData: function(source) {
                    if(!source.stringaRadio) {
                        source.stringaRadio = "<input type='radio' name='radio_modale_impegno' />";
                    }
                    return source.stringaRadio;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalSubImpegno", oData);
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
                    return source.disponibilitaLiquidare ? source.disponibilitaLiquidare.formatMoney() : "";
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        $("#tabellaImpegniModale").data("originalImpegno", impegno)
            .dataTable(options);
    }


    /**
     * Imposta i dati dell'impegno nella tabella
     */
    function impostaImpegnoNellaTabella(impegno) {
        var capitolo = "";
        var provvedimento = "";
        var soggetto = "";
        var importo;
        var importoAttuale = impegno.importoAttuale || 0;
        var disponibile = impegno.disponibilitaLiquidare || 0;

        importo = importoAttuale.formatMoney();
        if(impegno.importoIniziale){
            importo += " (iniziale: " + impegno.importoIniziale.formatMoney() + ")";
        }

        if(impegno.capitoloUscitaGestione && impegno.capitoloUscitaGestione.uid !== 0){
            capitolo = impegno.capitoloUscitaGestione.annoCapitolo
                + "/" + impegno.capitoloUscitaGestione.numeroCapitolo
                + "/" + impegno.capitoloUscitaGestione.numeroArticolo;
            capitolo += isGestioneUEB ? "/" + impegno.capitoloUscitaGestione.numeroUEB : "";
            capitolo += " - " + impegno.capitoloUscitaGestione.descrizione;
            capitolo += impegno.capitoloUscitaGestione.strutturaAmministrativoContabile
                ? impegno.capitoloUscitaGestione.strutturaAmministrativoContabile.codice
                : "";
            capitolo += impegno.capitoloUscitaGestione.tipoFinanziamento
                ? " - Tipo finanziamento: " + impegno.capitoloUscitaGestione.tipoFinanziamento.codice
                : "";
        }

        if(impegno.attoAmministrativo){
            provvedimento = impegno.attoAmministrativo.anno + "/"+ impegno.attoAmministrativo.numero;
            provvedimento += impegno.attoAmministrativo.tipoAtto ? " - " + impegno.attoAmministrativo.tipoAtto.descrizione : "";
            provvedimento += " - " + impegno.attoAmministrativo.oggetto;
            provvedimento += impegno.attoAmministrativo.strutturaAmmContabile
                ? " - " + impegno.attoAmministrativo.strutturaAmmContabile.codice
                : "";
        }

        if(impegno.soggetto && impegno.soggetto.uid){
            soggetto = impegno.soggetto.codiceSoggetto + " - " +  impegno.soggetto.denominazione;
            soggetto = impegno.soggetto.codiceFiscale ? soggetto + " - CF: " + impegno.soggetto.codiceFiscale : soggetto;
            soggetto = impegno.soggetto.partitaIva ? soggetto + " - P.IVA: " + impegno.soggetto.partitaIva : soggetto;
        } else if (impegno.classeSoggetto){
            soggetto = "Classe: " + impegno.classeSoggetto.codice + " - " + impegno.classeSoggetto.descrizione;
        }

        $("#tabellaImpegno_tdCapitolo").html(capitolo);
        $("#tabellaImpegno_tdProvvedimento").html(provvedimento);
        $("#tabellaImpegno_tdSoggetto").html(soggetto);
        $("#tabellaImpegno_tdImporto").html(importo);
        $("#tabellaImpegno_tdDisponibile").html(disponibile.formatMoney());
    }

    /**
     * Richiama l'esecuzione della ricerca Soggetto.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function ricercaImpegno() {
        var anno = $("#annoImpegnoModale").val();
        var numero = $("#numeroImpegnoModale").val();
        var oggettoPerChiamataAjax;
        var spinner = $("#SPINNER_pulsanteRicercaImpegnoModale");
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

        oggettoPerChiamataAjax = unqualify($("#FIELDSET_modaleImpegno").serializeObject(), 1);
        spinner.addClass("activated");
        alertErroriModale.slideUp();
        $("#divImpegniTrovati").slideUp();

        $.postJSON(
            "ricercaImpegnoPerChiave.do",
            oggettoPerChiamataAjax,
            function(data){
                if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
                    return;
                }
                impostaImpegnoNellaTabella(data.impegno);
                impostaSubimpegniNellaTabella(data.impegno);
                // Copia dei campi dell'impegno
                $("#hidden_cigImpegno").val(data.impegno.cig);
                $("#hidden_cupImpegno").val(data.impegno.cup);
                $("#hidden_disponibileImpegno").val(data.impegno.disponibilitaLiquidare || 0);
                data.impegno.capitoloUscitaGestione && $("#hidden_flagRilevanteIva").val(data.impegno.capitoloUscitaGestione.flagRilevanteIva);
                campoRicercaEffettuataConSuccesso.val("true");

                $("#divImpegniTrovati").slideDown();
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Conferma l'impegno selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaImpegno(e) {
        var checkedRadio = $("#tabellaImpegniModale").find("input[name='radio_modale_impegno']:checked");
        var impegno;
        var subimpegno;
        var annoImpegno;
        var numeroImpegno;
        var numeroSubimpegno;
        var str;
        var zero = 0;


        var cig;
        var cup;

        var disponibilita;
        var tipoDebitoSIOPE;
        var event;

        e.preventDefault();

        if(!campoRicercaEffettuataConSuccesso.val()) {
            impostaDatiNegliAlert(["Necessario selezionare un impegno"], alertErroriModale, false);
            return;
        }

        subimpegno = checkedRadio.length > 0 && checkedRadio.data("originalSubImpegno");
        impegno = $("#tabellaImpegniModale").data("originalImpegno");
        
        // SIAC-5410
        tipoDebitoSIOPE = (subimpegno && subimpegno.siopeTipoDebito) || (impegno && impegno.siopeTipoDebito) || undefined;

        annoImpegno = $("#annoImpegnoModale").val();
        numeroImpegno = $("#numeroImpegnoModale").val();
        numeroSubimpegno = !!subimpegno ? subimpegno.numero : "";

       
        cig = $("#hidden_cigImpegno").val();
        cup = $("#hidden_cupImpegno").val();

        disponibilita = ((!!subimpegno ? subimpegno.disponibilitaLiquidare : parseFloat($("#hidden_disponibileImpegno").val())) || zero).formatMoney();

        campoInCuiApplicareAnnoMovimento.val(annoImpegno).attr("readOnly", true);
        campoInCuiApplicareNumeroMovimento.val(numeroImpegno).attr("readOnly", true);
        campoInCuiApplicareNumeroSubMovimento.val(numeroSubimpegno).attr("readOnly", true);

        campoInCuiApplicareCig.val(cig);
        campoInCuiApplicareCup.val(cup);
        campoInCuiApplicareDisponibilita.html(disponibilita);

        str = ": " + annoImpegno + " / " + numeroImpegno;
        if(subimpegno) {
            str += " - " + numeroSubimpegno;
        }
        // SIAC-5410
        if(tipoDebitoSIOPE && tipoDebitoSIOPE.codice && tipoDebitoSIOPE.descrizione) {
            str += ' (' + tipoDebitoSIOPE.codice + ' - ' + tipoDebitoSiope.descrizione + ')';
        }

        campoInCuiApplicareLaDescrizione.html(str);

        event = $.Event("impegnoCaricato", {'impegno': impegno, 'subimpegno': subimpegno});
        $(document).trigger(event);
        $("#modaleImpegno").modal("hide");
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoAnnoMovimento      (String) il campo ove impostare l'anno del movimento (Optional - default: #annoMovimentoMovimentoGestione)
     * @param campoNumeroMovimento    (String) il campo ove impostare il numero del movimento (Optional - default: #numeroMovimentoGestione)
     * @param campoNumeroSubMovimento (String) il campo ove impostare la denominazione (Optional - default: #numeroSubMovimentoGestione)
     * @param campoDescrizione        (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #datiRiferimentoImpegnoSpan)
     *
     * @param campoCig                (String) il campo ove impostare il CIG (Optional - default: '')
     * @param campoCup                (String) il campo ove impostare il CUP (Optional - default: '')
     * @param spanDisponibilita       (String) il campo ove impostare la disponibilita (Optional - default: '')
     */
    exports.inizializza = function(campoAnnoMovimento, campoNumeroMovimento, campoNumeroSubMovimento, campoDescrizione, campoCig, campoCup, spanDisponibilita) {
        campoInCuiApplicareAnnoMovimento = $(campoAnnoMovimento || "#annoMovimentoMovimentoGestione");
        campoInCuiApplicareNumeroMovimento = $(campoNumeroMovimento || "#numeroMovimentoGestione");
        campoInCuiApplicareNumeroSubMovimento = $(campoNumeroSubMovimento || "#numeroSubMovimentoGestione");
        campoInCuiApplicareLaDescrizione = $(campoDescrizione || "#datiRiferimentoImpegnoSpan");

        campoInCuiApplicareCig = $(campoCig || "");
        campoInCuiApplicareCup = $(campoCup || "");
        campoInCuiApplicareDisponibilita = $(spanDisponibilita || "");

        $("#pulsanteRicercaImpegnoModale").substituteHandler("click", ricercaImpegno);
        $("#pulsanteConfermaModaleImpegno").substituteHandler("click", confermaImpegno);
        $("#divImpegniTrovati").on("shown hidden", function(e) {
            e.stopPropagation();
        });

        $(document).on("shown", "#modaleImpegno", function() {
            campoRicercaEffettuataConSuccesso.val("");
        }).on("hidden", "#modaleImpegno", function() {
            $("#divImpegniTrovati").slideUp();
            alertErroriModale.slideUp();
        }).on("change", "#modaleImpegno input[name='radio_modale_impegno']", function() {
            var subimpegno = $(this).data("originalSubImpegno");
        });
    };

    return exports;
}());