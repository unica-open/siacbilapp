/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var Documento = (function($) {
    var exports = {};
    var _oldImpostaDatiNegliAlert = impostaDatiNegliAlert;

    /**
     * Sovrscrive i dati originali presenti nei campi di input e riprova l'invocazione dell'evento.
     *
     * @param anchor         (jQuery) l'ancora su cui ichiamare l'evento
     * @param inputContainer (jQuery) gli input presenti nella pagina
     * @param modale         (jQuery) il modale da chiudere
     */
    function sovrascriviModificheERiprova(anchor, inputContainer, modale) {
        // Sovrascrivo il valore originale
        inputContainer.keepOriginalValues();
        modale.modal("hide");
        anchor.trigger("click");
    }
    
    /**
     * SIAC-7567
     * Funzione di controllo per determinare il submit o la chiamata asincrona per 
     * il controllo del cig e del cup per le pubbliche assistenze.
     * In caso di mancata valorizzazione di uno o più campi, (cig e/o cup) per una PA, 
     * apro la modale e chiedo conferma all'utente.
     *
     * @param e     (jQuery) l'evento chiamante
     *
     * @returns     (jQuery) il modale aperto
     */
    exports.asyncCheckPA = function(e){
        var $overlay = $(document.body);
        var $alertWarning = $("#modaleConfermaProsecuzioneSuAzione");
        var $fieldset = $("#fieldset_AggiornaDocumentoEntrata");
        var oggettoPerChiamataAjax = $fieldset.serializeObject();
        var url = "aggiornamentoDocumentoEntrata_aggiornamentoAnagraficaAsincrono.do";
        var pulsanteSalva = $("#salvaAggiornamento");
        var $checkCanale = $('#HIDDEN_checkCanale').val();
        
        e && e.preventDefault();

        if(typeof($checkCanale) == 'undefined'){
            //se sono qui il soggetto non e' una PA 
            //pertanto posso procedere in maniera sincrona
            $('#formAggiornamentoDatiDocumento').submit();
            return;
        }
        //SIAC-7770: sposto qui, se faccio il submit la gestione deve essere quella precedente a SIAC-7516
        pulsanteSalva.addClass("disabled");
        pulsanteSalva.attr("disabled", true);
        pulsanteSalva.prop("disabled", true);
        
        $overlay.overlay('show');
        $alertWarning.slideUp();
        $fieldset.addClass('form-submitted');

        //chiamata asincrona di validazione per le PA
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
        }).done(function(data){
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
                    $('#formAggiornamentoDatiDocumento').submit();
                }, undefined, "Si, prosegui", "no, indietro");
                return;
            }
            //eseguo il submit del form solo se non ho avuto errori o messagi in risposta
            if(!(data.errori && data.errori.length)) {
                $('#formAggiornamentoDatiDocumento').submit();
            }
        }).fail(function(data){
            //tento di gestire il fallimento della validazione
            $('#ERRORI').val(data.error);
        }).always(function() {
            //NON tolgo l'overlay poiche' ci pensa il ricaricamento della pagina
            pulsanteSalva.removeClass("disabled");
            pulsanteSalva.removeAttr("disabled");
            pulsanteSalva.removeProp("disabled");
            $fieldset.removeClass('form-submitted');
        });
    }

    /**
     * Apre il modale per la conferma del cambiamento di tab.
     *
     * @param anchor         (jQuery) l'ancora su cui ichiamare l'evento
     * @param inputContainer (jQuery) gli input presenti nella pagina
     *
     * @returns (jQuery) il modale aperto
     */
    function apriModaleConfermaCambioTab(anchor, inputContainer) {
        var modale = $("#modaleConfermaProsecuzione");
        return modale
            .find("#modaleConfermaPulsanteSi")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    sovrascriviModificheERiprova(anchor, inputContainer, modale);
                })
                .end()
            .find("#modaleConfermaPulsanteNo")
                .substituteHandler("click", function(e) {
                    e.preventDefault();
                    modale.modal("hide");
                })
                .end()
            .find(".alert")
                .slideDown()
                .end()
            .modal("show");
    }

    /**
     * Gestisce il cambiamento del tab: blocca tale azione nel caso in cui vi siano delle modifiche non salvate.
     *
     * @param e (Event) l'evento scatenante, da bloccare nel caso in cui vi siano modifiche non salvate
     *
     * @returns (Boolean) <code>true</code> nel caso in cui l'invocazione possa proseguire; <code>false</code> in caso contrario
     */
    exports.gestioneCambioTab = function(e) {
        var inputContainer = $(".tab-content").children(".active")
            .find("*:input:not(.modal *:input)")
                .not("button");
        var changedInputs = inputContainer.findChangedValues();
        var result = true;
        if(changedInputs.length > 0) {
            e.preventDefault();
            // Apri modale conferma cambio, passandogli il this
            apriModaleConfermaCambioTab($(this), inputContainer);
            result = false;
        } else {
            // Posso cambiare il tab: chiudo i messaggi di errore
            $("#ERRORI, #INFORMAZIONI").slideUp();
        }
        return result;
    };

    /**
     * Gestisce il submit del form. Controlla che il checkbox sia deselezionato: in tal caso, FORZA il valore 'false'.
     */
    exports.gestioneSubmitPerCheckbox = function() {
        var checkbox = $("#debitoreMultiplo");
        if(!checkbox.is(":checked")) {
            checkbox.after(
                $("<input/>", {
                    "type": "hidden",
                    "name": checkbox.attr("name"),
                    "value": false
                })
            );
        }
    };

    /**
     * Salva i vecchi valori di netto ed arrotondamento.
     */
    exports.salvaVecchiNettoArrotondamento = function() {
        $("#HIDDEN_oldArrotondamento").val($("#arrotondamentoDocumento").val()).keepOriginalValues();
        $("#HIDDEN_oldNetto").val($("#nettoDocumento").val()).keepOriginalValues();
    };

    /**
     * Pulisce il modale del soggetto.
     */
    exports.puliziaMascheraSoggetto = function() {
        $("#fieldsetRicercaGuidateSoggetto").find(":input")
                .val("");
        $("#divTabellaSoggetti").slideUp();
    };

    exports.alertErrori = $("#ERRORI");
    exports.alertMessaggi = $("#MESSAGGI");
    exports.alertInformazioni = $("#INFORMAZIONI");
    exports.alerts = $("#ERRORI, #INFORMAZIONI");

    /**
     * Wrappo la funzionalità vecchia aggiungendo la chiusura dei vecchi alerts.
     */
    window.impostaDatiNegliAlert = function() {
        var chiudereGliAlerts = arguments[3];
        var innerChiusura = chiudereGliAlerts === undefined ? true : chiudereGliAlerts;
        if(!!innerChiusura) {
            exports.alertErrori.slideUp();
            exports.alertInformazioni.slideUp();
        }
        return _oldImpostaDatiNegliAlert.apply(this, arguments);
    };

    return exports;
}(jQuery));

$(function() {
    //jira 1900 e' stato richiesto di  togliere la descrizione ' È possibile associare all'impegno un soggetto o una classe di soggetti'
    $('#descrizioneSoggetto_modale_seleziona_soggetto').addClass('hide');

    // Gestione del cambio tab
    $("*:input:not(.modal *:input)").not("button")
        .keepOriginalValues();
    $(".nav.nav-tabs").find("a")
        .on("click", Documento.gestioneCambioTab);

    $("#formAggiornamentoDatiDocumento").submit(Documento.gestioneSubmitPerCheckbox);
    $("form").on("reset", function() {
        // NATIVO
        this.reset();
        Documento.puliziaMascheraSoggetto();
    });

    //SIAC-7567
    $('#salvaAggiornamento').on('click', Documento.asyncCheckPA);

    Documento.salvaVecchiNettoArrotondamento();
});