/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

!function($) {
    'use strict';
    var $document = $(document);
    var first = true;

    $(init);
    
    function applyChangeOnSelects() {
        $('select').change();
    }
    /**
     * Callback al click del pulsante conferma della modale di richiesta conferma per Tipo Documento
     */
    function confermaTipoDoc(){
        // aggiungo il parametro che permette di aggiorna il tipodoc
        $('input[name="forzaTipoDocumento"]').val("true");
        // forzo il submit
        $('#formAggiornamentoTipoDocumento').submit();
    }
    
    function init() {
        var inputRichiestaConferma = $('#HIDDEN_richiediConfermaUtente'); 

        var richiestaProsecuzione = false;

        if(inputRichiestaConferma.length>0 ){
            richiestaProsecuzione = true;
            impostaRichiestaConfermaUtente(inputRichiestaConferma.data('messaggio-conferma'), confermaTipoDoc, applyChangeOnSelects);
        }
    }
  
}(jQuery);

