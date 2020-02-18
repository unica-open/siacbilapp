/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    // Per lo sviluppo
    "use strict";

    /**
     * Filtra i tipi di documento a partire dalla famiglia.
     */
    function filterTipoDocumento() {
        var famiglia = $(this);
        // Attivo l'overlay
        var select = $("#tipoDocumento").overlay("show");
        var selectedValue = parseInt(select.data("savedValue"), 10);

        return $.postJSON("ajax/tipoDocumentoAjax_filter.do", {tipoFamigliaDocumento: famiglia.val()}, function(data) {
            var html = "<option value=''></option>";
            // Creo l'HTML da impostare nella select
            $.each(data.listaTipoDocumento, function(idx, el) {
                html += "<option value='" + el.uid + "'";
                if(el.uid === selectedValue) {
                    // Non ho problemi di NaN grazie al triplo uguale
                    html += " selected ";
                }
                html += ">" + el.codice + " - " + el.descrizione + "</option>";
            });
            // Popolo la select
            select.html(html);
        }).always(function() {
            // Chiudo l'overlay
            select.overlay("hide");
        });
    }

    /**
     * Salva il valore selezionato del tipo di documento.
     */
    function saveTipoDocumento() {
        var $this = $(this);
        // Salvo il valore selezionato della select
        return $this.data("savedValue", $this.val());
    }

    /**
     * Imposta l'anno per il datepicker della data di emissione in maniera coerente con l'anno del documento
     */
    function impostaAnnoPerDataEmissione() {
        var anno = parseInt($(this).val(), 10);
        var dataOdierna;
        var datepicker;
        var oldValue;
        if(isNaN(anno)) {
            // L'anno non e' valido: esco
            return;
        }
        // Imposto la data
        dataOdierna = new Date();
        // Imposto la data iniziale al giorno corrente dell'anno selezionato
        dataOdierna.setFullYear(anno);

        // Imposto i dati del datepicker
        datepicker = $("#dataEmissioneDocumento");
        oldValue = datepicker.val();

        // Imposto la data nel datepicker
        datepicker.datepicker("update", formatDate(dataOdierna));
        if(!oldValue) {
            datepicker.val(oldValue);
        }
    }

    /**
     * Apre il modale dell'atto amministrativo copiando i dati forniti dall'utente.
     */
    function apriModaleAttoAmministrativo() {
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm_modale");

        $("#annoProvvedimento_modale").val($("#annoAttoAmministrativo").val());
        $("#numeroProvvedimento_modale").val($("#numeroAttoAmministrativo").val());
        $("#tipoAttoProvvedimento_modale").val($("#tipoAtto").val());
        // Seleziono dallo zTree
        ZTreePreDocumento.selezionaNodoSeApplicabile(tree.innerZTree, $("#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid").val());
        // Apro il modale
        $("#modaleGuidaProvvedimento").modal("show");
    }

    /**
     * Apre il modale del soggetto copiando i dati forniti dall'utente.
     */
    function apriModaleSoggetto() {
        $("#codiceSoggetto_modale").val($("#codiceSoggettoSoggetto").val());
        $("#modaleGuidaSoggetto").modal("show");
    }




    $(function() {
        // Campi del soggetto (caricati un'unica volta)
        var campoCodiceSoggetto = $("#codiceSoggettoSoggetto");
        var accordionSedeSecondaria = $("#accordionSedeSecondariaSoggetto");
        var accordionModalitaPagamento = $("#accordionModalitaPagamentoSoggetto");
        var spanDescrizione = $("#datiRiferimentoSoggettoSpan");

        // Al cambio del tipo famiglia, filtro il tipo di documento
        $("input[name='tipoFamigliaDocumento']").change(filterTipoDocumento);
        // Al cambio di tipo documento, lo salvo
        $("#tipoDocumento").change(saveTipoDocumento);
        // Al cambio dell'anno del documento, imposto l'anno della data di emissione
        $("#annoDocumento").on("change blur", impostaAnnoPerDataEmissione);
        // Carica il dettaglio del soggetto e apre gli accordion
        campoCodiceSoggetto.change(function(e, params) {
        	var alertErrori = $('#ERRORI');
        	    var def = Soggetto.caricaDettaglioSoggetto(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento, spanDescrizione,undefined,undefined, undefined,true);
        	    if(!params || !params.doNotCloseAlerts) {
        	    	def.then(alertErrori.slideUp.bind(alertErrori), function(errori){        		
            			impostaDatiNegliAlert(errori, alertErrori);
            			return errori;
            		});
        	    }
        		
            })
            // Forzo la chiamata al metodo
            .trigger('change', {doNotCloseAlerts: true});
        // Filtro delle modalita' di pagamento rispetto alla sede secondaria
        accordionSedeSecondaria.on("change", "input[name='sedeSecondariaSoggetto.uid']", function() {
            var tabella = accordionModalitaPagamento.find("table");
            var uid = $(this).filter(":checked").val();
            Soggetto.filterModalitaPagamentoSoggetto(uid, tabella);
        });

        // Attivo l'apertura del modale dell'atto amministrativo
        $("#pulsanteApriModaleProvvedimento").click(apriModaleAttoAmministrativo);
        $("#pulsanteAperturaCompilazioneGuidataSoggetto").click(apriModaleSoggetto);

        // Inizializzazione dei modali
        Soggetto.inizializza("#codiceSoggettoSoggetto", "#HIDDEN_codiceFiscaleSoggetto", "#HIDDEN_denominazioneSoggetto", "#datiRiferimentoSoggettoSpan");
        Provvedimento.inizializzazione("", "#annoAttoAmministrativo", "#numeroAttoAmministrativo", "#tipoAtto", "#treeStruttAmmAttoAmministrativo",
            "#HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid", "#statoOperativoAttoAmministrativo", "#datiRiferimentoAttoAmministrativoSpan",
            "AttoAmministrativo");
    });
}(jQuery));