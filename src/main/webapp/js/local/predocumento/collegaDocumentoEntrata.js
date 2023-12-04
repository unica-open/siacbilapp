/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    'use strict';
    var $form = $('#associaQuote');
    var table = $('#risultatiRicercaCollegaDocumento');
    var associaButton = $('#associaQuoteAPreDocumento');
    var importoPreDoc = $('#importoPreDocumento').val();
    var alertErrori = $('#ERRORI');
    var selectedDatas = {};
    var arrSelected = [];

    /**
     * Gestione della selezione di tutti gli elementi. Preseleziona il checkbox all'apertura della pagina
     * @param settings (any) i settings del dataTable
     */
    function handleSelezionaTutti(settings) {
        var inputNonChecked = $('input', settings.nTBody).not(':checked');
        $('#checkboxSelezionaTutti').prop('checked', inputNonChecked.length === 0);
    }

    /**
     * Handling della variazione del checkbox
     * @param nTd    (Node) il td
     * @param predoc (any)  il predoc
     */
    function handlePredocCheckboxChange(nTd, predoc) {
        var input = $('input', nTd);
        selectedDatas[+input.val()] = {isSelected: input.prop('checked'), data: predoc};
    }

    /**
     * Ottiene i preDocumenti selezionati.
     * @returns (any[]) l'array dei dati selezionati
     */
    function getPreDocumentiSelezionati() {
        var res = [];
        var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                res.push(selectedDatas[i].data);
            }
        }
        return res;
    }

    /**
     * Caricamento via Ajax della tabella dei documenti e visualizzazione.
     */
    function visualizzaTabellaQuoteDaAssociareToPreDoc() {
        var options = {
            bServerSide: true,
            sAjaxSource: "risultatiRicercaQuoteDaAssociareAjax.do",
            sServerMethod: "POST",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            // SIAC-6768 bDestroy: true
            bDestroy: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            fnServerData: function (sSource, aoData, fnCallback, oSettings) {
            	oSettings.jqXHR = $.ajax({
            		dataType: 'json',
            		type: 'POST',
            		url: sSource,
            		data: aoData,
            		success: function(data) {
            			if(data.moreData.importoTotale !== undefined) {
            				$('#importoTotale').html(data.moreData.importoTotale.formatMoney());
            			}
            			return fnCallback.apply(this, arguments);
            		}
            	});
            },
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#risultatiRicercaPreDocumento_processing').parent('div')
                    .hide();
                // Attivo i popover
                table.find('a[rel="popover"]')
                    .popover()
                    .eventPreventDefault('click');
                handleSelezionaTutti(settings);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source, type, val) {
                    var res = '<input type="checkbox" name="uidDocumento" value="' + source.uid + '"';
                    if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
                        res += ' checked';
                    }
                    return res + '/>';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalPreDocumento', oData)
                        .substituteHandler('change', handlePredocCheckboxChange.bind(undefined, nTd, oData));
                }},
                {aTargets: [1], mData: defaultPerDataTable("documentoPadre")},
                {aTargets: [2], mData: defaultPerDataTable("dataEmissione")},
                {aTargets: [3], mData: function(source) {
                    return source.codiceStato + ' / ' + source.descrizioneStato;
                }},
                {aTargets: [4], mData: defaultPerDataTable("soggetto")},
                {aTargets: [5], mData: defaultPerDataTable("numero")},
                {aTargets: [6], mData: defaultPerDataTable("movimento")},
                {aTargets: [7], mData: defaultPerDataTable("ivaSplitReverse")},
                {aTargets: [8], mData: defaultPerDataTable("provvisorioCassa")},
                {aTargets: [9], mData: function(source) {
                    return source.importo.formatMoney();
                }, fnCreatedCell: function(nTd) {
                    $(nTd).addClass("text-right");
                }},
                {aTargets: [10],bVisible:false, mData: function(source) {
                    return ' gg';
                }}
            ]
        };

        var startPos = parseInt($("#HIDDEN_startPosition").val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {iDisplayStart: startPos});
        }

        $("#risultatiRicercaCollegaDocumento").dataTable(options);

    }
    
    function changeFormAction(){
        $form.append('<input type="hidden" name="uidSubDocumentoDaAssociare" value="' + arrSelected[0].uid + '" />');
        $form.attr('action', 'ritornoCollegaDocumentoToAggiornaPreDoc.do');

        return $form.submit();
    }

    function associaQuote(){
        arrSelected = getPreDocumentiSelezionati();
        if(arrSelected.length == 0){
            alertErrori.append( "<li>Selezionare una quota da associare alla predisposizione d'incasso</li>" );
            alertErrori.show();
            return;
        }
        if(arrSelected.length > 1){
            alertErrori.append( "<li>Non &eacute; possibile associare pi&ugrave;  di una quota alla predisposizione d'incasso</li>" );
            alertErrori.show();
            return;
        }
        if(arrSelected[0].importo === importoPreDoc && arrSelected[0].importo > importoPreDoc){
            alertErrori.append( "<li>Impossibile selezionare una quota con importo maggiore a quello della predisposizione d'incasso</li>" );
            alertErrori.show();
            return;
        }

        var spinner = $('#SPINNER_modaleConfermaProsecuzioneSuAzione');
        var conferma = $('#modaleConfermaProsecuzioneSuAzionePulsanteSi');
        var url = "proseguiConWarning.do";
        var objectToSend = arrSelected[0];
        spinner.addClass("activated");
        conferma.addClass("disabled");
        conferma.attr("disabled", true);
        conferma.prop("disabled", true);

        $.postJSON(url, objectToSend).then(function(data){
            if(data.messaggi && data.messaggi.length) {
                var stringaMessaggioConfermaProcedimento = '';
                stringaMessaggioConfermaProcedimento = data.messaggi.reduce(function(acc, el) {
                    return acc + '<li>' + el.codice + ' - ' + el.descrizione + '<li>';
                }, '<ul>') + '</ul>';
                impostaRichiestaConfermaUtente(stringaMessaggioConfermaProcedimento, function() {
                    // objectToSend.proseguireConElaborazione = true;+
                    $('#HIDDEN_proseguireConElaborazione').val(true);
                    $form.append();
                    changeFormAction();
                }, undefined, "Si, prosegui", "no, indietro");
                return;
            }
        }).always(function() {
            spinner.removeClass("activated");
            conferma.removeClass("disabled");
            conferma.removeAttr("disabled");
            conferma.removeProp("disabled");
            $("#modaleConfermaProsecuzioneSuAzione").modal("hide");
        });

    }

    $(function() {

        visualizzaTabellaQuoteDaAssociareToPreDoc();
        associaButton.substituteHandler('click', associaQuote);
        
    });
    
})(jQuery, this);