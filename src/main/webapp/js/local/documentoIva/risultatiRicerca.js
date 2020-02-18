/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {

    "use strict";

    var tipoDocumento = $('#HIDDEN_tipoDocumento').val();

    /**
     * Caricamento via Ajax della tabella dei preDocumenti e visualizzazione della stessa.
     */
    function visualizzaTabellaDocumenti() {
        var url = 'risultatiRicercaDocumentoIva' + tipoDocumento + 'Ajax.do';

        var options = {
            bServerSide : true,
            sAjaxSource : url,
            sServerMethod : "POST",
            bPaginate : true,
            bLengthChange : false,
            iDisplayLength : 10,
            bSort : false,
            bInfo : true,
            bAutoWidth : true,
            bFilter : false,
            bProcessing : true,
            oLanguage : {
                sInfo : "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty : "0 risultati",
                sProcessing : "Attendere prego...",
                sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate : {
                    sFirst : "inizio",
                    sLast : "fine",
                    sNext : "succ.",
                    sPrevious : "prec.",
                    sEmptyTable : "Nessun dato disponibile"
                }
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback : function() {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
            },
            aoColumnDefs : [
                {
                    aTargets : [ 0 ],
                    mData : function(source) {
                        return source.descrizioneDocumento
                            ? "<a rel='popover' href='#'>" + source.descrizioneDocumento + "</a>"
                            : "";
                    },
                    fnCreatedCell : function(nTd, sData, oData) {
                        var settings = {
                            content : formatDate(oData.dataEmissioneDocumento),
                            title : "Data",
                            trigger : "hover"
                        };
                        $("a", nTd).eventPreventDefault("click")
                            .popover(settings);
                    }
                },
                {
                    aTargets : [ 1 ],
                    mData : function(source) {
                        return source.flagIntracomunitario ? 'Si' : 'No';
                    }
                },
                {
                    aTargets : [ 2 ],
                    mData : function(source) {
                        return source.descrizioneSoggetto || "";
                    }
                },
                {
                    aTargets : [ 3 ],
                    mData : function(source) {
                        return source.annoEsercizio + '-' + source.progressivoIVA;
                    }
                },
                {
                    aTargets : [ 4 ],
                    mData : function(source) {
                        return source.tipoRegistrazioneIva
                            ? source.tipoRegistrazioneIva.codice + " - " + source.tipoRegistrazioneIva.descrizione
                            : "";
                    }
                },
                {
                    aTargets : [ 5 ],
                    mData : function(source) {
                        return source.attivitaIva
                            ? source.attivitaIva.codice + "-" + source.attivitaIva.descrizione
                            : "";
                    }
                },
                {
                    aTargets : [ 6 ],
                    mData : function(source) {
                        return source.registroIva
                            ? source.registroIva.codice + "-" + source.registroIva.descrizione
                            : "";
                    }

                },
                {
                    aTargets : [ 7 ],
                    mData : function(source) {
                        return source.numeroProtocolloProvvisorio
                            ? "<a rel='popover' href='#'>" + source.numeroProtocolloProvvisorio + "</a>"
                            : "";
                    },
                    fnCreatedCell : function(nTd, sData, oData) {
                        var settings;
                        if (oData.dataProtocolloProvvisorio) {
                            settings = {
                                content : formatDate(oData.dataProtocolloProvvisorio),
                                title : "Data Prot. Provv.",
                                trigger : "hover"
                            };
                            $("a", nTd).eventPreventDefault("click")
                                    .popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 8 ],
                    mData : function(source) {
                        return source.numeroProtocolloDefinitivo
                            ? "<a rel='popover' href='#'>" + source.numeroProtocolloDefinitivo + "</a>"
                            : "";
                    },
                    fnCreatedCell : function(nTd, sData, oData) {
                        var settings;
                        if (oData.dataProtocolloDefinitivo) {
                            settings = {
                                content : formatDate(oData.dataProtocolloDefinitivo),
                                title : "Data Prot. Def.",
                                trigger : "hover"
                            };
                            $("a", nTd).eventPreventDefault("click")
                                    .popover(settings);
                        }
                    }
                },
                {
                    aTargets : [ 9 ],
                    mData : "azioni",
                    fnCreatedCell : function(nTd, sData, oData) {
                        $(nTd).find(".aggiornaDocumentoIva" + tipoDocumento)
                                .substituteHandler("click", function(e) {
                                    e.preventDefault();
                                    document.location = 'risultatiRicercaDocumentoIva' + tipoDocumento + 'Aggiorna.do?uidDaAggiornare=' + oData.uid;
                                 })
                                 .end()
                             .find(".consultaDocumentoIva" + tipoDocumento)
                                 .substituteHandler("click", function(e) {
                                     e.preventDefault();
                                     document.location = 'risultatiRicercaDocumentoIva' + tipoDocumento + 'Consulta.do?uidDaConsultare=' + oData.uid;
                                 });
                    }
                }
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if (!isNaN(startPos)) {
            $.extend(true, options, {"iDisplayStart" : startPos});
        }

        $("#risultatiRicercaDocumentoIva").dataTable(options);
    }

    $(function() {
        visualizzaTabellaDocumenti();
    });
}(jQuery));