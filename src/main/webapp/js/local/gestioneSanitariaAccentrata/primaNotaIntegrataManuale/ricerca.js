/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErrori = $("#ERRORI");
    var gsaClassifZtreeId;

    /**
     * Popolamento della select.
     *
     * @param select          (jQuery)  la select da popolare
     * @param options         (Array)   l'array delle opzioni
     * @param addEmptyOption  (boolean) inserisce l'elemento vuoto (opzionale, default = false)
     * @param emptyOptionText (string)  inserisce il testo per l'elemento vuoto (opzionale, default = "")
     */
    function popolaSelect(select, options, /*opzionale*/ addEmptyOption, /*opzionale*/ emptyOptionText) {
        var opts = "";
        if (!!addEmptyOption) {
            opts += "<option value=''>" + (emptyOptionText || "") + "</option>";
        }
        options.forEach(function(el) {
            opts += "<option value='" + el.uid + "'>" + el.codice + " - " + el.descrizione + "</option>";
        });
        select.html(opts);
    }

    /**
     * Ricarica le causali
     */
    function ricaricaCausaliPerEvento(){
        var selectEvento = $("#uidEvento");
        var selectCausaleEP = $("#uidCausaleEP").overlay('show');
        var evento = selectEvento.val();
        var ambito = $("#ambito").val();
        var objToSend = {"evento.uid": evento, "ambito": ambito};
        // Chiamata AJAX
        $.postJSON("ricercaCausaleEPByEvento_ricercaModulare.do", objToSend)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                popolaSelect(selectCausaleEP, []);
                return;
            }

            popolaSelect(selectCausaleEP, data.listaCausaleEP, true,"");
        }).always(selectCausaleEP.overlay.bind(selectCausaleEP, "hide"));
    }

    /**
     * Ricerca il piano dei conti.
     */
    function ricercaPianoDeiConti() {
        var obj = $("#fieldsetRicerca").serializeObject();
        var spinner = $("#SPINNER_pulsanteRicercaPianoDeiConti").addClass("activated");

        divRisultati.slideUp();
        // Effettuo la ricerca
        $.postJSON("ricercaPianoDeiConti_effettuaRicerca.do", obj)
        .then(function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori. Mostro la tabella e calcolo i risultati
            popolaTabella(data.conto, data.figliPresenti, data.isGestioneConsentita, data.gerarchiaConti);
            divRisultati.slideDown();
        }).always(spinner.removeClass.bind(spinner, "activated"));

    }
    function loadProgramma(missioneField, programmaField) {
        var idMissione = $(missioneField).val();
        var $programma = $(programmaField);
        var currentProgrammaValue = $programma.data('oldValue');
        
        // Pulisco l'old value
        $programma.data('oldValue', '');
        if(idMissione === '0') {
            $programma.val(0).empty().attr('disabled', true);
            return;
        }
        $programma.overlay('show');
        return $.postJSON('ajax/programmaAjax.do', {'id': idMissione})
        .then(function(data) {
            var str;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Rigetto la promise
                $programma.val(0).empty().attr('disabled', true);
                return $.Deferred().reject().promise();
            }
            str = data.listaProgramma.reduce(function(acc, el) {
                var tmp = '<option value="' + el.uid + '"';
                if(el.uid === currentProgrammaValue) {
                    tmp += ' selected';
                }
                
                return acc + tmp + '>' + el.codice + ' - ' + el.descrizione + '</option>';
            }, '<option value="0"></option>');
            if(str.indexOf('selected>') === -1) {
                $programma.val(0);
            }
            $programma.html(str);
            $programma.removeAttr('disabled');
        }).always($programma.overlay.bind($programma, 'hide'));
    }
    
    /**
     * Gestione del change del tipo movimento
     */
    function handleChangeTipoMovimento() {
        var $checked = $('input[name="tipoMovimento"]').filter(':checked');
        var $fields = $('#annoMovimento, #numeroMovimentoGestione, #numeroSubmovimentoGestione');
        var $button = $('#pulsanteCompilazioneGuidataMovimentoGestione');
        if(!$checked.length) {
            $fields.attr('disabled', true);
            $button.addClass('disabled');
            return;
        }
        $fields.removeAttr('disabled');
        $button.removeClass('disabled');
        
        Accertamento.destroy();
        Impegno.destroy();
        if($checked.val() === 'Impegno') {
            Impegno.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined, undefined, undefined, undefined, undefined, '#pulsanteCompilazioneGuidataMovimentoGestione');
        } else if ($checked.val() === 'Accertamento') {
            Accertamento.inizializza("#annoMovimento", "#numeroMovimentoGestione", "#numeroSubmovimentoGestione", undefined, undefined, undefined, '#pulsanteCompilazioneGuidataMovimentoGestione');
        }
    }
    
    /**
     * Gestione del reset del form
     */
    function handleReset() {
        $('form').substituteHandler('reset', GSAClassifZtree.cleanZTree.bind(undefined, gsaClassifZtreeId));
    }
    
    $(function () {
        var $body = $(document.body);
        $("select[data-evento]").change(ricaricaCausaliPerEvento).change();
        Conto.inizializza(undefined, undefined, "#codiceConto", '#descrizioneConto', "#pulsanteCompilazioneGuidataConto");
        // SIAC-5281
        $('#missione')
            .substituteHandler('change', loadProgramma.bind(undefined, '#missione', '#programma'))
            .change();
        // SIAC-5334
        $('input[name="tipoMovimento"]').change(handleChangeTipoMovimento).change();
        // SIAC-5336
        gsaClassifZtreeId = GSAClassifZtree.initClassificatoreGSAZtree();
        // SIAC-5756
        handleReset();
        // SIAC-6425
        $('[data-overlay-body]').substituteHandler('click', $body.overlay.bind($body, 'show'));
    });

}(jQuery, this);