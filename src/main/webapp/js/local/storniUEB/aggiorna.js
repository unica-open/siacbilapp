/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Document onReady
$(
    function() {
       Provvedimento.svuotaFormRicerca();
       Provvedimento.inizializza();
       $("form").on("reset", function() {
           // NATIVO
           this.reset();
       });
    }
);

