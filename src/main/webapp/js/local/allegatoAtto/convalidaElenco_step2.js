/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    var selectedDatas = {};
    
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
    var form = $("#formConvalidaAllegatoAttoStep2_unused");

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
            {aTargets: [startIdx + 4], mData: function(source) {
                return source.hasImpegnoConfermaDurc ? "Si" : "No";
            }},
            {aTargets: [startIdx + 5], mData: function(source) {
            	if(!source.hasImpegnoConfermaDurc){
            		return "";
            	}
                return source.dataFineValiditaDurc? formatDate(source.dataFineValiditaDurc) : "Durc da richiedere";
            }},
            {aTargets:[startIdx + 6], mData: function(source) {
                // Totale spese
                var res = 0;
                if(source.totaleQuoteSpese) {
                    res = source.totaleQuoteSpese;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 7], mData: function(source) {
                // Totale entrate
                var res = 0;
                if(source.totaleQuoteEntrate) {
                    res = source.totaleQuoteEntrate;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 8], mData: function(source) {
                // Da convalidare spese
                var res = 0;
                if(source.totaleDaConvalidareSpesa) {
                    res = source.totaleDaConvalidareSpesa;
                }
                return res.formatMoney();
            }, fnCreatedCell: function(nTd) {
                $(nTd).addClass("tab_Right");
            }},
            {aTargets:[startIdx + 9], mData: function(source) {
                // Da convalidare entarte
                var res = 0;
                if(source.totaleDaConvalidareEntrata) {
                    res = source.totaleDaConvalidareEntrata;
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
        var i;
        var data;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                data = selectedDatas[i].data;
                totaleSpese += (data.totaleQuoteSpese || 0);
                totaleEntrate += (data.totaleQuoteEntrate || 0);
                nonValidatoSpese += (data.totaleDaConvalidareSpesa || 0);
                nonValidatoEntrate += (data.totaleDaConvalidareEntrata || 0);
            }
        }
        $('#totaleSpeseConvalidabili').html(totaleSpese.formatMoney());
        $('#totaleEntrateConvalidabili').html(totaleEntrate.formatMoney());
        $('#nonValidatoSpeseConvalidabili').html(nonValidatoSpese.formatMoney());
        $('#nonValidatoEntrateConvalidabili').html(nonValidatoEntrate.formatMoney());
    }
    
    function isElencoDaDisabilitarePerDurc(source){
    	var now = new Date();
    	return source.hasImpegnoConfermaDurc && (!source.dataFineValiditaDurc || ( now > source.dataFineValiditaDurc))
    }

    /**
     * Popola la lista degli allegati convalidabili.
     *
     * @param list (Array) la lista dei risultati convalidabili
     */
    function popolaTabellaConvalidabili(list) {
        var columns = cols(1).slice();
        var moreOpts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Non sono presenti elenchi convalidabili"
            },
            fnDrawCallback: drawCallback,
            aoColumnDefs: columns
        };
        var optionsToUse;
        // Aggiungo le ulteriori colonne
        columns.push({aTargets: [0], mData: function(source) {
        	var res ="<input type='checkbox' class='checkboxElenco' name='listaUid'";
        	if(isElencoDaDisabilitarePerDurc(source)){
        		res += " disabled ";
        	}
        	res +=  " value='" + source.uid + "'/>"
            return res;
        }, fnCreatedCell: function(nTd, sData, oData) {
            $('input', nTd).data('originalData', oData);
        }});

        columns.push({aTargets: [11], mData: function(source) {
            return "<button type='button' class='btn' data-uid='" + source.uid + "'>dettaglio</a>";
        }, fnCreatedCell: function(nTd) {
            $("button", nTd).substituteHandler("click", function() {
                var self = $(this);
                $("#HIDDEN_uidElenco").val(self.data("uid"));
                // Impostare l'azione nel form e inviarlo
                form.attr("action", "convalidaAllegatoAtto_ottieniDettaglioElenco.do")
                    .submit();
            });
        }});
        optionsToUse = $.extend(true, {}, opts, moreOpts);
        $("#tabellaConvalidabili").dataTable(optionsToUse);
    }

    /**
     * Popola la lista degli allegati non convalidabili.
     *
     * @param list (Array) la lista dei risultati non convalidabili
     */
    function popolaTabellaNonConvalidabili(list) {
        var columns = cols().slice();
        var moreOpts = {
            aaData: list,
            oLanguage: {
                sZeroRecords: "Non sono presenti elenchi non convalidabili"
            },
            aoColumnDefs: columns
        };
        var optionsToUse = $.extend(true, {}, opts, moreOpts);
        $("#tabellaNonConvalidabili").dataTable(optionsToUse);
    }

    /**
     * Seleziona tutti i checkbox della stessa tabella
     */
    function selezionaTutti() {
        var $this = $(this);
        var table = $this.closest("table");
        var checkboxes = table.find("tbody").find("input[type='checkbox']:not([disabled])");
        var toCheck = $this.prop("checked");

        checkboxes.prop("checked", toCheck).each(function(idx, el) {
            selectedDatas[+el.value] = {isSelected: toCheck, data: $(el).data('originalData')};
        });
        updateTotali();
    }

    /**
     * Convalidazione degli elenchi selezionati.
     *
     * @param e (Event) l'evento scatenante
     */
    function convalidaElenco(e) {
        var formToSubmit = $('#formConvalidaAllegatoAttoStep2');
        var isConvalidaManuale = $('#convalidaManuale_modal').prop('checked');
        var html = '<input type="hidden" name="convalidaManuale" value="' + isConvalidaManuale + '"/>';
        $('#formConvalidaAllegatoAttoStep2_unused').addClass('form-submitted');
        writeSelectedToForm(formToSubmit, html);
       
        // Blocco l'evento
        e.preventDefault();
        
        // Invio il form
        formToSubmit.attr("action", "convalidaAllegatoAtto_convalidaElenco.do")
            .submit();
    }
    
    function writeSelectedToForm($form, htmlDiPartenza) {
        var html = htmlDiPartenza || '';
        var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                html += '<input type="hidden" name="listaUid" value="' + i + '" />';
            }
        }
        $form.html(html);
    }

    /**
     * Rifiuto degli elenchi selezionati.
     */
    function rifiutaElenco() {
        var formToSubmit = $('#formConvalidaAllegatoAttoStep2');
        $('#formConvalidaAllegatoAttoStep2_unused').addClass('form-submitted');
        writeSelectedToForm(formToSubmit, '');
        // Invio il form
        formToSubmit.attr("action", "convalidaAllegatoAtto_rifiutaElenco.do")
            .submit();
    }

    /**
     * Rifiuto degli elenchi per l'annullamento dell'allegato atto.
     */
    function rifiutaPerAnnullamentoAtto() {
        // Invio il form
        form.attr("action", "convalidaAllegatoAtto_rifiutaAtto.do")
            .submit();
    }
    
    function saveElenchi() {
        var $this = $(this);
        var isChecked;
        if($this.attr('id') === 'checkboxSelezionaTutti') {
            return;
        }
        isChecked = $this.prop('checked');
        selectedDatas[+$this.val()] = {isSelected: isChecked, data: $this.data('originalData')};
        updateTotali();
    }
    
    function drawCallback(options) {
        var unselectedCheckboxes = $(options.nTBody).find('input[type="checkbox"]').not(':checked');
        $("#checkboxSelezionaTutti")[unselectedCheckboxes.length ? 'removeProp' : 'prop']('checked', true);
        $("a[rel='popover']", options.nTBody).eventPreventDefault("click") .popover();
    }
    
    function popolaTotaleElenchi(totaleElenchi) {
    	return $("#totaleElenchi").html(totaleElenchi.formatMoney());
    }

    $(function() {
        $.postJSON("convalidaAllegatoAtto_ottieniListeElenchi.do", {}, function(data) {
            popolaTabellaConvalidabili(data.listaConvalidabili);
            popolaTabellaNonConvalidabili(data.listaNonConvalidabili);
            popolaTotaleElenchi(data.totaleElenchi || 0);
        });

        $("#checkboxSelezionaTutti").substituteHandler("change", selezionaTutti);
        $("#pulsanteConfermaModaleTipoConvalida").substituteHandler("click", convalidaElenco);
        $("#pulsanteSiModaleRifiutaElenco").substituteHandler("click", rifiutaElenco);
        $("#pulsanteSiModaleRifiutaAllegato").substituteHandler("click", rifiutaPerAnnullamentoAtto);
        form.on('change', 'input[type="checkbox"]', saveElenchi);
        updateTotali();
    });
}(jQuery);