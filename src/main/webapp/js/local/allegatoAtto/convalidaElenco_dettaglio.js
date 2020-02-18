/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    // Per lo sviluppo
    "use strict";
    var selectedDatas = {};

    var tabellaDaConvalidare = $("#tabellaSubdocumentiDaConvalidare");
    var opts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 50,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
        bFilter: false,
        bProcessing: true,
        bDestroy: true,
        sScrollY: "300px",
        bScrollCollapse: true,
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
        }
    };
    
    // $("#pulsanteSelezionaTuttiDaConvalidare")

    /**
     * Seleziona tutti i checkbox.
     */
    function selezionaTutti() {
        var isChecked = $(this).prop("checked");

        tabellaDaConvalidare.find("tbody")
            .find("input[type='checkbox']:not([disabled])")
            .prop("checked", isChecked)
            .each(function(idx, el) {
                selectedDatas[+el.value] = {isSelected: isChecked, row: $(this).closest("tr").clone()};
            });
        modificaTotali();
    }
    
    function checkAll() {
        tabellaDaConvalidare.find('input[type="checkbox"]:checked').each(function(idx, el) {
            var $this = $(el);
            selectedDatas[+$this.val()] = {isSelected: $this.prop('checked'), row: $this.closest('tr').clone()};
        });
        modificaTotali();
    }
    
    function checkSingle() {
        var $this = $(this);
        selectedDatas[+$this.val()] = {isSelected: $this.prop("checked"), row: $this.closest("tr").clone()};
        
        modificaTotali();
    }

    /**
     * Aggiorna i totali
     */
    function modificaTotali() {
        var totSpese = 0;
        var totEntrate = 0;
        var defaultImporto = "0,00";
        var numeroElementiSelezionati = 0;
        var messaggio;
        var i;
        var row;
        var spesa;
        var entrata;
        
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                row = selectedDatas[i].row;
                spesa = row.find(".importoSpesa").html() || "-";
                entrata = row.find(".importoEntrata").html() || "-";
                spesa = spesa.trim() === "-" ? defaultImporto : spesa.trim();
                entrata = entrata.trim() === "-" ? defaultImporto : entrata.trim();

                // Converto in numero
                spesa = parseFloat(parseLocalNum(spesa));
                entrata = parseFloat(parseLocalNum(entrata));
                // Aggiungo al totale
                totSpese += spesa;
                totEntrate += entrata;
                numeroElementiSelezionati += 1;
            }
        }

        $('[data-totale-in-convalida="entrata"]').html(totEntrate.formatMoney());
        $('[data-totale-in-convalida="spesa"]').html(totSpese.formatMoney());
        messaggio = numeroElementiSelezionati === 0
            ? "In Convalida"
            : numeroElementiSelezionati === 1
                ? "In Convalida (1 elemento selezionato)"
                : "In Convalida (" + numeroElementiSelezionati + " elementi selezionati)";
        $("[data-numero-elementi-selezionati]").html(messaggio);
        
        // Refresho la tabella
        if($.fn.dataTable.fnIsDataTable(tabellaDaConvalidare[0])) {
            tabellaDaConvalidare.dataTable().fnDraw();
        }
    }

    function submitForm() {
        var formToSubmit = $('#formConvalidaAllegatoAttoStep2');
        var isConvalidaManuale = $('#convalidaManuale_modal').prop('checked');
        var html = '<input type="hidden" name="convalidaManuale" value="' + isConvalidaManuale + '"/>';
        $('#formConvalidaAllegatoAttoStep2_unused').addClass('form-submitted');
        writeSelectedToForm(formToSubmit, html);
        
        // Invio il form
        formToSubmit.submit();
    }
    
    function writeSelectedToForm($form, initialHtmlValue) {
        var html = initialHtmlValue || '';
        var i;
        for(i in selectedDatas) {
            if(Object.prototype.hasOwnProperty.call(selectedDatas, i) && selectedDatas[i] && selectedDatas[i].isSelected === true) {
                html += '<input type="hidden" name="listaUid" value="' + i + '" />';
            }
        }
        $form.html(html);
    }

    function drawCallback(options) {
        var unselectedCheckboxes = $(options.nTBody).find('input[type="checkbox"]').not(':checked');
        $("#pulsanteSelezionaTuttiDaConvalidare")[unselectedCheckboxes.length ? 'removeProp' : 'prop']('checked', true);
    }

    $(function() {
        checkAll();
        tabellaDaConvalidare.on("change", "input[type='checkbox']", checkSingle)
            .dataTable($.extend(true, {}, opts, {oLanguage: {sZeroRecords: "Nessuna quota da convalidare disponibile"}, fnDrawCallback: drawCallback}));
        $("#tabellaSubdocumentiNonConvalidabili").dataTable($.extend(true, {}, opts, {oLanguage: {sZeroRecords: "Nessuna quota non convalidabile"}}));

        $("#pulsanteSelezionaTuttiDaConvalidare").substituteHandler("change", selezionaTutti);
        $("#pulsanteConfermaModaleTipoConvalida").substituteHandler("click", submitForm);
    });
}(jQuery);