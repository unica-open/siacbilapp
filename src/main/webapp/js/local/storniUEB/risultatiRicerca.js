/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(
    function() {
        // Effettuo una validazione per la redirezione del form
        $("#formRedirezioneAggiornamento").submit(
            function(event) {
                // Radio button selezionato
                var radioConUid = $("input[name='uidDaAggiornare']:checked").val(),
                    alertErrori = $("#ERRORI");
                if(radioConUid === undefined || radioConUid === null || radioConUid === "") {
                    // Blocco la propagazione dell'evento di sottomissione del form
                    event.preventDefault();
                    // Popolo l'alert degli errori e lo mostro
                    alertErrori.children("ul").find("li").remove().end();
                    // Aggiungo gli errori alla lista
                    alertErrori.children("ul").append($("<li/>").html("Selezionare uno storno da aggiornare"));
                    // Mostro l'alert
                    alertErrori.slideDown();
                    $(document.body).overlay("hide");
                }
            }
        );

        // Carico la dataTable
        Storni.visualizzaTabellaStorni();
    }
);