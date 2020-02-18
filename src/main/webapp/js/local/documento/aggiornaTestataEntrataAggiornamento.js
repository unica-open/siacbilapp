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

    $(function(){
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").on("click", apriModaleSoggetto);
    });

}(jQuery));