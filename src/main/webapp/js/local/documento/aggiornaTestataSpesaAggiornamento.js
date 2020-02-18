/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    "use strict";

    /**
     * Apre il modale del soggetto e copia il valore del codice.
     *
     * @param e (Event) l'evento scatenante
     */
    function apriModaleSoggetto(e) {
        e.preventDefault();
        $("#codiceSoggetto_modale").val($("#codiceSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    }
    
    /**
     * Gestione del tipo SIOPE
     * @param e (Event) l'evento scatenante
     */
    function handleTipoDocumentoSiope(e) {
        var $siopeTipoDocumento = $(e.target).find('option:selected');
        var codice = $siopeTipoDocumento.data('codice');
        var $analogico = $('div[data-siope-analogico]');
        
        if(codice === 'A') {
            $analogico.slideDown();
        } else {
            $analogico.slideUp();
            $analogico.find('select')
                .val(0);
        }
    }

    $(function(){
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(apriModaleSoggetto);
        
        // SIAC-5311 SIOPE+
        $('#siopeDocumentoTipo').substituteHandler('change', handleTipoDocumentoSiope)
            .change();
    });

}(jQuery));