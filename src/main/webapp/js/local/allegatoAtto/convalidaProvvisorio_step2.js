/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    var annoEsercizio = $("#HIDDEN_annoEsercizio").val();

    /**
     * Calcola le colonne.
     *
     * @param tipo         (String) il tipo del documento (S/E)
     * @param movimento    (String) il campo del movimento (impegno/accertamento)
     * @param submovimento (String) il campo del submovimento (subImpegno/subAccertamento)
     * @param capitolo     (String) il campo del capitolo (capitoloUscitaGestione/capitoloEntrataGestione)
     */
    function cols (movimento, submovimento, capitolo, tipo) {
        var isEntrata = (tipo === "E");
        var posDistinta = isEntrata ? 5 : 6;
        var posContoTesoriere = isEntrata ? 6 : 7;
        var posImportoEntrata = isEntrata ? 7 : 8;
        var posImportoSpesa = isEntrata ? 8 : 9;

        var baseCols = [
            {aTargets: [0], mData: function(source) {
                var res = "";
                // Atto amministrativo
                if(source.attoAmministrativo) {
                    res = source.attoAmministrativo.anno;
                    if(source.attoAmministrativo.strutturaAmmContabile) {
                        res += source.attoAmministrativo.strutturaAmmContabile.codice;
                    }
                    res += source.attoAmministrativo.numero;
                }
                return res;
            }},
            {aTargets: [1], mData: function(source) {
                var res = "";
                // Soggetto
                if(source.documento && source.documento.soggetto) {
                    res = "<a data-original-title='Denominazione' href='#' data-trigger='hover' rel='popover' data-content='"
                        + escapeHtml(source.documento.soggetto.denominazione) + "'>" + source.documento.soggetto.codiceSoggetto + "</a>";
                }
                return res;
            }, fnCreatedCell: function(nTd) {
                $("a", nTd).popover();
            }},
            {aTargets: [2], mData: function(source) {
                var res = "";
                var strDescrizione;
                var strAnnoCodiceNumero;
                // Documento
                if(source.documento) {
                    // Dati del documento
                    strAnnoCodiceNumero = tipo + "-" + source.documento.anno;
                    if(source.documento.tipoDocumento) {
                        strAnnoCodiceNumero += "/" + source.documento.tipoDocumento.codice;
                    }
                    strAnnoCodiceNumero += "/" + source.documento.numero + "-" + source.numero;
                    strDescrizione = source.descrizione;
                    res = "<a data-original-title='Descrizione' href='#' data-trigger='hover' rel='popover' data-content='"
                        + escapeHtml(strDescrizione) + "'>" + strAnnoCodiceNumero + "</a>";
                }
                return res;
            }, fnCreatedCell: function(nTd) {
                $("a", nTd).popover();
            }},
            {aTargets: [3], mData: function(source) {
                var res = "";
                var cap;
                // Capitolo
                if(source[movimento] && source[movimento][capitolo]) {
                    cap = source[movimento][capitolo];
                    res = cap.numeroCapitolo + "/" + cap.numeroArticolo;
                }
                return res;
            }},
            {aTargets: [4], mData: function(source) {
                var res = "";
                var mov;
                // Capitolo
                if(source[movimento]) {
                    mov = source[movimento];
                    res = annoEsercizio + "/" + mov.annoMovimento + "/" + mov.numero;
                    if(source[submovimento]) {
                        res += "/" + source[submovimento].numero;
                    }
                }
                return res;
            }},
            {aTargets: [posDistinta], mData: function(source) {
                // TODO: distinta
                return "";
            }},
            {aTargets: [posContoTesoriere], mData: function(source) {
                var res = "";
                // noteTesoriere
                if(source.noteTesoriere) {
                    res = source.noteTesoriere.codice + " - " + source.noteTesoriere.descrizione;
                }
                return res;
            }},
            {aTargets: [posImportoEntrata], mData: function(source) {
                var res = "-";
                if(isEntrata && source.importo !== undefined && source.importo !== null) {
                    res = source.importo.formatMoney();
                }
                return res;
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets: [posImportoSpesa], mData: function(source) {
                var res = "-";
                if(!isEntrata && source.importo !== undefined && source.importo !== null) {
                    res = source.importo.formatMoney();
                }
                return res;
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }}
        ];
        if(!isEntrata) {
            baseCols.push(
                {aTargets: [5], mData: function(source) {
                    var res = "";
                    // Liquidazione
                    if(source.liquidazione) {
                        res = source.liquidazione.annoLiquidazione + "/" + source.liquidazione.numeroLiquidazione;
                    }
                    return res;
                }}
            );
        }
        return baseCols;
    }

    /**
     * Popola la tabella dei documenti.
     *
     * @param tabella      (jQuery) la tabella da popolare
     * @param url          (String) l'URL da invocare
     * @param tipo         (String) il tipo del documento (S/E)
     * @param movimento    (String) il campo del movimento (impegno/accertamento)
     * @param submovimento (String) il campo del submovimento (subImpegno/subAccertamento)
     * @param capitolo     (String) il campo del capitolo (capitoloUscitaGestione/capitoloEntrataGestione)
     */
    function popolaTabella(tabella, url, tipo, movimento, submovimento, capitolo) {
        var isEntrata = (tipo === "E");
        var zeroRecords = "Non sono presenti quote di " + (isEntrata ? "entrata" : "spesa") + " corrispondenti ai parametri inseriti";
        var baseOpts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : url,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: zeroRecords,
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: cols(movimento, submovimento, capitolo, tipo)
        };
        tabella.dataTable(baseOpts);
    }

    /**
     * Invio del form
     */
    function inviaForm() {
        $("form").submit();
    }

    $(function() {
        var isEntrata = $("#HIDDEN_provvisorioDiEntrata").val() === "true";
        var tabella = $("#tabellaDocumentiProvvisorio");

        if(isEntrata) {
            // Entrata -> popolo la tabella con i dati di entrata
            popolaTabella(tabella, "risultatiRicercaQuoteEntrataAjax.do", "E", "accertamento", "subAccertamento", "capitoloEntrataGestione");
        } else {
            // Spesa -> popolo la tabella con i dati di spesa
            popolaTabella(tabella, "risultatiRicercaQuoteSpesaAjax.do", "S", "impegno", "subImpegno", "capitoloUscitaGestione");
        }
        // Lego il submit al pulsante di invio
        $("#pulsanteConfermaModaleTipoConvalida").substituteHandler("click", inviaForm);
    });
}(jQuery);