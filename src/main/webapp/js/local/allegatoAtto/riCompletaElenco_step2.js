/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    var opts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 10,
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
            oPaginate: {
                sFirst: "inizio",
                sLast: "fine",
                sNext: "succ.",
                sPrevious: "prec.",
                sEmptyTable: "Nessun dato disponibile"
            }
        },
        fnDrawCallback: function() {
            $("a[rel='popover']", this).eventPreventDefault("click")
                .popover();
        }
    };
    var form = $("#formRiCompletaAllegatoAttoStep2");

    /**
     * Costruisce le colonne base.
     *
     * @param baseIdx (Integer) l'indice di base
     */
    function cols(baseIdx) {
        var startIdx = baseIdx || 0;
        return [
            {aTargets: [startIdx], mData: function(source) {
                // Elenco
                return source.anno + "/" + source.numero;
            }},
            {aTargets: [startIdx + 1], mData: function(source) {
                // Stato
                var res = "";
                if(source.statoOperativoElencoDocumenti) {
                    res = "<a data-original-title='Stato' href='#' data-trigger='hover' rel='popover' data-content='"
                        + escapeHtml(source.statoOperativoElencoDocumenti.descrizione) + "'>" + source.statoOperativoElencoDocumenti.codice + "</a>";
                }
                return res;
            }},
            {aTargets: [startIdx + 2], mData: function(source) {
                // Quote a copertura
                var res = "";
                if(source.contieneQuoteACopertura) {
                    res = "<a data-original-title='Info' href='#' data-trigger='hover' rel='popover' data-content='Contiene quote a copertura'>"
                            + "<i class='icon-asterisk icon-2x'>&nbsp;</i>"
                        + "</a>";
                }
                return res;
            }},
            {aTargets: [startIdx + 3], mData: function(source) {
                // Trasmissione
                var res = "";
                if(source.dataTrasmissione) {
                	var fonte = "";
                	fonte += source.sysEsterno ? source.sysEsterno + " " : "";
                	fonte += source.annoSysEsterno && source.numeroSysEsterno ? source.annoSysEsterno + "/" + source.numeroSysEsterno : "";
                    res = "<a data-original-title='Fonte dati' href='#' data-trigger='hover' rel='popover' data-content='" + escapeHtml(fonte) + "'>" +
                        formatDate(source.dataTrasmissione) + "</a>";
                }
                return res;
            }},
            {aTargets:[startIdx + 4], mData: function(source) {
                // Totale spese
                var res = 0;
                if(source.totaleQuoteSpese) {
                    res = source.totaleQuoteSpese;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 5], mData: function(source) {
                // Totale entrate
                var res = 0;
                if(source.totaleQuoteEntrate) {
                    res = source.totaleQuoteEntrate;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 6], mData: function(source) {
                // Da convalidare spese
                var res = 0;
                if(source.totaleDaConvalidareSpesaNoCopertura) {
                    res = source.totaleDaConvalidareSpesaNoCopertura;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 7], mData: function(source) {
                // Da convalidare entarte
                var res = 0;
                if(source.totaleDaConvalidareEntrataNoCopertura) {
                    res = source.totaleDaConvalidareEntrataNoCopertura;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }}
        ];
    }

    /**
     * Aggiornamento dei totali
     */
    function updateTotali() {
        var totaleSpese = 0;
        var totaleEntrate = 0;
        var nonValidatoSpese = 0;
        var nonValidatoEntrate = 0;
        $('tbody', '#tabellaRiCompletabili').find('input[type="checkbox"]:checked').each(function(idx, el) {
            var data = $(el).data('originalData');
            totaleSpese += (data.totaleQuoteSpese || 0);
            totaleEntrate += (data.totaleQuoteEntrate || 0);
            nonValidatoSpese += (data.totaleDaConvalidareSpesaNoCopertura || 0);
            nonValidatoEntrate += (data.totaleDaConvalidareEntrataNoCopertura || 0);
        });
        $('#totaleSpeseRiCompletabili').html(totaleSpese.formatMoney());
        $('#totaleEntrateRiCompletabili').html(totaleEntrate.formatMoney());
        $('#nonValidatoSpeseRiCompletabili').html(nonValidatoSpese.formatMoney());
        $('#nonValidatoEntrateRiCompletabili').html(nonValidatoEntrate.formatMoney());
    }

    /**
     * Popola la lista degli allegati RiCompletabili.
     *
     * @param list (Array) la lista dei risultati RiCompletabili
     */
    function popolaTabellaRiCompletabili(list) {
        var columns = cols(1).slice();
        var moreOpts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Non sono presenti elenchi da riportare allo stato completato"
            },
            aoColumnDefs: columns
        };
        var optionsToUse;
        // Aggiungo le ulteriori colonne
        columns.push({aTargets: [0], mData: function(source) {
            return "<input type='checkbox' class='checkboxElenco' name='listaUid' value='" + source.uid + "'/>";
        }, fnCreatedCell: function(nTd, sData, oData) {
            $('input', nTd).data('originalData', oData).substituteHandler('change', updateTotali);
        }});

        columns.push({aTargets: [9], mData: function(source) {
            return "";
        }});
        optionsToUse = $.extend(true, {}, opts, moreOpts);
        $("#tabellaRiCompletabili").dataTable(optionsToUse);
    }

    /**
     * Popola la lista degli allegati non Elaborabili.
     *
     * @param list (Array) la lista dei risultati non ELaborabili
     */
    function popolaTabellaNonElaborabili(list) {
        var columns = cols().slice();
        var moreOpts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Non sono presenti elenchi non Elaborabili"
            },
            aoColumnDefs: columns
        };
        var optionsToUse = $.extend(true, {}, opts, moreOpts);
        $("#tabellaNonElaborabili").dataTable(optionsToUse);
    }

    /**
     * Seleziona tutti i checkbox della stessa tabella
     */
    function selezionaTutti() {
        var $this = $(this);
        var table = $this.closest("table");
        var checkboxes = table.find("tbody").find("input[type='checkbox']");

        checkboxes.prop("checked", $this.prop("checked"));
        updateTotali();
    }

    /**
     * Convalidazione degli elenchi selezionati.
     *
     * @param e (Event) l'evento scatenante
     */
    function riCompletaElenco(e) {
        // Blocco l'evento
        e.preventDefault();
        // Invio il form
        form.attr("action", "riCompletaAllegatoAtto_riCompletaElenco.do")
            .submit();
        $("#pulsantePortaAcompletatoElenco").addClass("disabled");

    }


    $(function() {
        $.postJSON("riCompletaAllegatoAtto_ottieniListeElenchi.do", {}, function(data) {
            popolaTabellaRiCompletabili(data.listaRiCompletabili);
            popolaTabellaNonElaborabili(data.listaNonElaborabili);
        });

        $("#checkboxSelezionaTutti").substituteHandler("change", selezionaTutti);
        $("#pulsantePortaAcompletatoElenco").substituteHandler("click", riCompletaElenco);

        updateTotali();
    });
}(jQuery);