/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($) {

    var alertErrori = $("#ERRORI");
    var alertMessaggi = $("#MESSAGGI");


    function mostraONascondiDatiAggiuntivi(){
        var divCampiAggiuntivi = $("#campiAggiuntivi");
        var livello = parseInt($("#HIDDEN_livelloPadre").val(), 10)+1;
        var livelloDiLegge = parseInt($("#HIDDEN_livelloDiLegge").val(), 10);
        if($("#contoFoglia").prop("checked") || livello === livelloDiLegge){
            divCampiAggiuntivi.slideDown();
            mostraTipoCespitiNellaLista();
        }else{
            divCampiAggiuntivi.slideUp();
            nascondiTipoCespitiDallaListaESelezionaGenerico();
        }
    }

    function mostraTipoCespitiNellaLista(){
        mostraONascondiTipoCespitiDallaLista("removeClass");
    }

    function nascondiTipoCespitiDallaListaESelezionaGenerico(){
        var uidGenerico = $("#uidTipoGenerico").val();
        var select = $("#tipoConto");
        if(select.val() === $("#uidTipoCespiti").val()){
        	select.find("option[value='"+uidGenerico+"']").attr("selected", "selected");
        }
        mostraONascondiTipoCespitiDallaLista("addClass");

    }

    function mostraONascondiTipoCespitiDallaLista(azione){
        var uidCespiti = $("#uidTipoCespiti").val();
        var select = $("#tipoConto");
        select.find("option[value='"+uidCespiti+"']")[azione]("hide");
    }

    function gestioneCategoriaCespiti(){

        var divCategoriaCespiti = $("#divCategoriaCespiti");
        var tipoSelezionato = $("#tipoConto").val();

        if(!tipoSelezionato) {
            divCategoriaCespiti.slideUp();
            $("#categoriaCespiti").val(0);
            return;
        }
        if(tipoSelezionato === $("#uidTipoCespiti").val()){
            divCategoriaCespiti.slideDown();
            $("#HIDDEN_Ammortamento").val("true");
            popolaListaClassi("inserisciFiglioPianoDeiContiGSA_ottieniListaClassiAmmortamento.do");
        }else{
            divCategoriaCespiti.slideUp();
            $("#categoriaCespiti").val(0);
            $("#HIDDEN_Ammortamento").val("");
            popolaListaClassi("inserisciFiglioPianoDeiContiGSA_ottieniListaClassiCompleta.do");
        }

    }

    function popolaListaClassi(url){
        $.postJSON(url, function (data) {
            if(impostaDatiNegliAlert(data.errori, $("#ERRORI_modaleRicercaConto"))) {
                return;
            }
            // popolo la select con la nuova lista
            var select = $("#classePianoDeiConti_modale");
            select.empty();
            $.each(data.listaClassi, function () {
                select.append(
                    $("<option />").val(this.uid).text(this.codice + ' - ' + this.descrizione)
                );
            });
        });
    }

    function caricaDettaglioSoggetto(){
        var campoCodiceSoggetto = $("#codiceSoggetto");
        var codiceSoggetto = campoCodiceSoggetto.val();
        var spanSoggetto = $("#datiRiferimentoSoggettoSpan");

         if(!codiceSoggetto) {
             spanSoggetto.html("");
             return;
         }
         // Inizializzo l'overlay
         campoCodiceSoggetto.overlay("show");
         // Chiamata alla action
         $.postJSON("ricercaSoggettoPerChiave.do", {"codiceSoggetto": codiceSoggetto}, function(data) {
             if(impostaDatiNegliAlert(data.errori, alertErrori, true, false)) {
                 spanSoggetto.html("");
                 return;
             }
             var descrizione =  data.soggetto.codiceSoggetto;
                descrizione +=  data.soggetto.denominazione ? " - " + data.soggetto.denominazione : "";
                descrizione +=  data.soggetto.codiceFiscale ?  " - " + data.soggetto.codiceFiscale : "";
             spanSoggetto.html(descrizione);
         }).always(function() {
             campoCodiceSoggetto.overlay("hide");
         });

    }

    function impostaAlberoCodificaDiBilancio(){

         var pulsante = $("#bottoneCodBilancio");
         var spinner = $("#SPINNER_CodiceBilancio");

         /* Non permettere di accedere al modale finché il caricamento non è avvenuto */
         pulsante.removeAttr("href");
         /* Attiva lo spinner */
         spinner.addClass("activated");

         $.postJSON("inserisciFiglioPianoDeiContiGSA_ottieniListaCodiceBilancio.do", function (data) {
             if(impostaDatiNegliAlert(data.errori, alertErrori, true, false)) {
                 return;
             }
             impostaDatiNegliAlert(data.messaggi, alertMessaggi, true, false);
             var listaCodificaBilancio = (data.listaCodiceBilancio);
             ZTree.imposta("treeCodBilancio", ZTree.SettingsBase, listaCodificaBilancio);
             // Ripristina l'apertura del modale
             pulsante.attr("href", "#codBilancio");
         }).always(function() {
             spinner.removeClass("activated");
         });
    }

    $(function() {
        // controllo eliminato in V03
        var livello = parseInt($("#HIDDEN_livelloPadre").val(), 10)+1;
        var livelloDiLegge = parseInt($("#HIDDEN_livelloDiLegge").val(), 10);
        var campoCodiceSoggetto = $("#codiceSoggetto");
        mostraONascondiDatiAggiuntivi();

        if(livello<livelloDiLegge){
            $("#contoFoglia").attr("disabled", true);
        }
        if(livello !== livelloDiLegge+1){
             $("#codiceSoggetto").val("");
             $("#datiSoggetto").addClass("hide");
        }else{
             $("#datiSoggetto").removeClass("hide");
        }

        // Inizializzazione dei modali
        Soggetto.inizializza("#codiceSoggetto", "#HIDDEN_soggettoCodiceFiscale", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan","#pulsanteCompilazioneGuidataSoggetto");
        Conto.inizializza(undefined, undefined, "#contoCollegato", '#descrizioneContoCollegato', "#pulsanteCompilazioneGuidataConto", false);
        impostaAlberoCodificaDiBilancio();

        $("#contoFoglia").on("change", mostraONascondiDatiAggiuntivi);
        $("#tipoConto").on("change", gestioneCategoriaCespiti);
        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	    var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, $(), $(), $('#datiRiferimentoSoggettoSpan'),undefined,undefined, undefined,true);
        	    if(!params || !params.doNotCloseAlerts) {
        	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
            			impostaDatiNegliAlert(errori, alertErrori);
            			return errori;
            		});
        	    }
        		
            })
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});
    });

}(jQuery);