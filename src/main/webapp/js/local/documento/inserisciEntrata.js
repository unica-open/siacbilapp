/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    'use strict';
    global.preSubmit = Documento.preSubmit;

    //SIAC-7567    
    function toStep3WithCheckPA(e){
        var $overlay = $(document.body);
        var $alertWarning = $("#modaleConfermaProsecuzioneSuAzione");
        var $form = $('#formInserimentoDocumentoEntrata');
        //cerco i fieldset che hanno un id che contenga all'inizio 'fieldset_'
        var $fieldset = $("fieldset[id^='fieldset_']");
        var $pulsanteSalva = $("#salvaDocumento");
        var $checkCanale = $('#HIDDEN_checkCanale').val();
        var url = $form.attr('action').slice(0, -3) + 'Asincrono.do';
        var oggettoPerChiamataAjax = $fieldset.serializeObject();

        e && e.preventDefault();
        
        $pulsanteSalva.addClass("disabled");
        $pulsanteSalva.attr("disabled", true);
        $pulsanteSalva.prop("disabled", true);
        
        $overlay.overlay('show');
        $alertWarning.slideUp();
        $fieldset.addClass('form-submitted');

        if(typeof($checkCanale) == 'undefined'){
            //il soggetto non e' una pa 
            //pertanto posso procedere in maniera sincrona
            $form.submit();
            return;
        }
        
        $.postJSON(url, oggettoPerChiamataAjax, function(data) {
            if(data.errori && data.errori.length) {
                //se l'overlay è ancora mostrato lo rimuovo
                $(document.body).overlay('hide');
                impostaDatiNegliAlert(data.errori, $('#ERRORI'));
                return;
            }
            if(impostaDatiNegliAlert(data.errori, $alertWarning)) {
                //se l'overlay è ancora mostrato lo rimuovo
                $(document.body).overlay('hide');
                return;
            }
            if(data.messaggi && data.messaggi.length) {
                //tolgo l'overlay per mostrare la modale del messaggio
                $(document.body).overlay('hide');
                //costruisco il messaggio in caso di messaggi
                var messaggioConfermaPA = '<li>Il debitore è una Pubblica Amministrazione';
                data.messaggi.length > 1 ? messaggioConfermaPA += ', i seguenti campi: ' : messaggioConfermaPA += ' ed il seguente campo: ';
                for(var i = 0; i < data.messaggi.length; i++){
                    messaggioConfermaPA += (data.messaggi[i].descrizione.substring('Dato obbligatorio omesso: '.length) + (data.messaggi.length-1 === i ? '' : ', '));
                }
                messaggioConfermaPA += data.messaggi.length > 1 ? ' non sono valorizzati,' : ' non &eacute; valorizzato,';
                messaggioConfermaPA += ' proseguire comunque con l\'inserimento del documento?</li>';
                //
                impostaRichiestaConfermaUtente(messaggioConfermaPA, function() {
                    //passo la conferma
                    $fieldset.find('input[name="proseguireConElaborazioneCheckPA"]').val(true);
                    //submit del form
                    $form.submit();
                }, undefined, "Si, prosegui", "no, indietro");
                return;
            }
            //eseguo il submit del form solo se non ho avuto errori o messagi in risposta
            if(!(data.errori && data.errori.length)) {
                $form.submit();
            }
        }).always(function() {
            //NON tolgo l'overlay poiche' ci pensa il ricaricamento della pagina
            $pulsanteSalva.removeClass("disabled");
            $pulsanteSalva.removeAttr("disabled");
            $pulsanteSalva.removeProp("disabled");
            $fieldset.removeClass('form-submitted');
        });
    }
    
    $(function() {
        $("#importoDocumento, #arrotondamentoDocumento").on("change", Documento.valorizzaNetto);
        $("#terminePagamentoDocumento, #dataRepertorioDocumento").on("change", Documento.gestioneDataScadenza)
            .change();
        $("#annoDocumento").on("change blur", Documento.impostaAnnoPerDataEmissione);

        $("#formInserimentoDocumentoSpesa").on("reset", Documento.resetForm);

        //SIAC-7567
        $('#salvaDocumento').on('click', toStep3WithCheckPA);
        
        //SIAC 6677
        $("#codAvvisoPagoPA").change(function(){
            if($(this).val().length > 0 && $(this).val().length < 18 ){
             	var diff = 18 - $(this).val().length;
                var codice = '';
                for(var i=0; i<diff; i++){
                	codice = codice + '0';
                }
                var newcodice = codice + $(this).val();
             	$(this).val(newcodice)
            }
        });
    });
}(this, jQuery);
