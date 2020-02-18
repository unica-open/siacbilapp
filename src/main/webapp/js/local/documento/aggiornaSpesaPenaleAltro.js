/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($, doc) {
    var exports = {};

    function popolaTabella (lista) {
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
            aaData: lista,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti penali / altro associate",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [ 0 ], mData: "documento"},
                {aTargets: [ 1 ], mData: "data"},
                {
                    aTargets: [ 2 ],
                    mData: function(source) {
                        if(!source.ancoraStatoOperativo) {
                            source.ancoraStatoOperativo = "<a href='#'>" + source.statoOperativoDocumentoCode + "</a>";
                        }
                        return source.ancoraStatoOperativo;
                    },
                    fnCreatedCell: function(nTd, sData, oData) {
                         var settings = {
                            content: oData.statoOperativoDocumentoDesc,
                            title: "Stato",
                            trigger: "hover",
                            placement: "left"
                        };
                        $("a", nTd).eventPreventDefault("click")
                            .popover(settings);
                    }
                },
                {aTargets: [ 3 ], mData: "soggetto"},
                {
                    aTargets: [ 4 ],
                    mData: function(source) {
                        return source.importo.formatMoney();
                    },
                    fnCreatedCell: function(nTd) {
                        $(nTd).addClass("tab_Right");
                    }
                }
            ]
        };
        $("#tabellaPenaleAltroDocumento").dataTable(options);
    }

    exports.ottieniLista = function() {
        $.postJSON(
            "aggiornamentoDocumentoSpesa_ottieniListaPenaleAltro.do",
            function(data) {
                popolaTabella(data.listaDocumentoEntrata);
            }
        );
    };

    doc.PenaleAltro = exports;
    return doc;
}(jQuery, Documento || {}));

$(function() {
    Documento.PenaleAltro.ottieniLista();
});