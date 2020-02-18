/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {
    "use strict";
    var inputDaControllare = $("*:input:not(.modal *:input)", "#tabDatiAllegato").not("button");
    var alertErrori = $("#ERRORI");
    var alertInformazioni = $("#INFORMAZIONI");
    var modaleConfermaProsecuzione = $("#modaleConfermaProsecuzione");
    var alertErroriModaleSospensioneSoggetto = $("#ERRORI_modaleSospensioneSoggettoElenco");
    var datiNonUnivociModaleSospensioneSoggetto = $("#modaleSospensioneSoggettoElenco_datiNonUnivoci");

    /**
     * Gestisce il cambiamento del tab: blocca tale azione nel caso in cui vi siano delle modifiche non salvate.
     *
     * @param e (Event) l'evento scatenante, da bloccare nel caso in cui vi siano modifiche non salvate
     *
     * @returns (Boolean) <code>true</code> nel caso in cui l'invocazione possa proseguire; <code>false</code> in caso contrario
     */
    function gestioneCambioTab(e) {
        var changedInputs = inputDaControllare.findChangedValues();
        var result = true;
        if(changedInputs.length > 0) {
            e.preventDefault();
            // Apri modale conferma cambio, passandogli il this
            apriModaleConfermaCambioTab($(this));
            result = false;
        } else {
            // Posso cambiare il tab: chiudo i messaggi di errore
            alertErrori.slideUp();
            alertInformazioni.slideUp();
        }
        return result;
    }

    /**
     * Apre il modale per la conferma del cambiamento di tab.
     *
     * @param anchor (jQuery) l'ancora su cui richiamare l'evento
     *
     * @returns (jQuery) il modale aperto
     */
    function apriModaleConfermaCambioTab(anchor) {
        return modaleConfermaProsecuzione
            .find("#modaleConfermaPulsanteSi")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    sovrascriviModificheERiprova(anchor);
                })
                .end()
            .find("#modaleConfermaPulsanteNo")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    modaleConfermaProsecuzione.modal("hide");
                })
                .end()
            .find(".alert")
                .slideDown()
                .end()
            .modal("show");
    }

    /**
     * Sovrscrive i dati originali presenti nei campi di input e riprova l'invocazione dell'evento.
     *
     * @param anchor (jQuery) l'ancora su cui richiamare l'evento
     */
    function sovrascriviModificheERiprova(anchor) {
        // Sovrascrivo il valore originale
        inputDaControllare.keepOriginalValues();
        modaleConfermaProsecuzione.modal("hide");
        anchor.trigger("click");
    }

    /**
     * Gestione della modifica del submit del form
     * @param e (Event) l'evento scatenante
     */
    function gestioneSubmitForm(e) {
        var $form = $('#formAggiornamentoAllegatoAtto');
        var button = $(e.target);
        var action = button.data('submitUrl');

        $form.attr('action', action)
            .submit();
    }

    /**
     * Gestione del soggetto per l'allegato
     */
    function gestioneSoggettoAllegato() {
        var overlay = $('#sospendiTuttoAllegatoAtto').overlay('show');
        // Pulisco i dati
        $("#datiSoggettoModaleSospensioneSoggettoElenco").html('');
        $("#modaleSospensioneSoggettoElenco").find('input').val('');
        // Nascondo gli alert
        datiNonUnivociModaleSospensioneSoggetto.slideUp();
        alertErroriModaleSospensioneSoggetto.slideUp();

        $.postJSON('aggiornaAllegatoAtto_ricercaDatiSospensioneAllegato.do')
        .then(handleRicercaDatiSoggettoAllegato)
        .always(overlay.overlay.bind(overlay, 'hide'));
    }

    /**
     * Gestione dei dati soggetto allegato
     * @param data (any) i parametri del servizio
     */
    function handleRicercaDatiSoggettoAllegato(data) {
        var modal = $("#modaleSospensioneSoggettoElenco");

        // Prepopolo i dati se applicabile
        if(data.datiSoggettoAllegatoDeterminatiUnivocamente && data.datiSoggettoAllegato) {
            // Ripeto pe sicurezza
            datiNonUnivociModaleSospensioneSoggetto.slideUp();
            // TODO: dinamicizzare
            $('#dataSospensioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.dataSospensione);
            $('#causaleSospensioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.causaleSospensione);
            $('#dataRiattivazioneDatiSoggettoAllegato').val(data.datiSoggettoAllegato.dataRiattivazione);
        } else {
            // Mostro il div di dati non univoci
            datiNonUnivociModaleSospensioneSoggetto.slideDown();
        }

        $(".datepicker", modal).each(function() {
            var $this = $(this);
            var value = $this.val();
            $this.datepicker("update", value);
        });

        $("#pulsanteConfermaModaleSospensioneSoggettoElenco").substituteHandler("click", handleConfermaSospensioneSoggetto);
        // Apro il modale
        modal.modal("show");
    }

    /**
     * Gestione della conferma della sospensione del soggetto
     * @param evt (Event) l'evento scatenante
     */
    function handleConfermaSospensioneSoggetto(evt) {
        var modal = $("#modaleSospensioneSoggettoElenco");
        var obj = $("#fieldsetModaleSospensioneSoggettoElenco").serializeObject();
        var spinner = $("#SPINNER_pulsanteConfermaModaleSospensioneSoggettoElenco").addClass("activated");
        evt.preventDefault();
        $.postJSON('aggiornaAllegatoAtto_sospendiTuttoAllegato.do', obj)
        .then(sospendiTuttoCallback.bind(undefined, modal))
        .always(spinner.removeClass.bind(spinner, "activated"));
    }

    /**
     * Callback di sospensione di tutti i dati
     * @param modal (jQuery) la modale
     * @param data  (any)    i dati del servizio
     */
    function sospendiTuttoCallback(modal, data) {
        if(impostaDatiNegliAlert(data.errori, alertErroriModaleSospensioneSoggetto)) {
            return $.Deferred().reject().promise();
        }
        impostaDatiNegliAlert(data.informazioni, alertInformazioni);
        modal.modal('hide');
    }

    $(function() {
        // Gestione tab dati
        inputDaControllare.keepOriginalValues();
        $("#navElenchiCollegati").on("click", gestioneCambioTab);
        $('button[data-submit-url]').substituteHandler('click', gestioneSubmitForm);
        $('#sospendiTuttoAllegatoAtto').substituteHandler('click', gestioneSoggettoAllegato);
    });
}(jQuery);