/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function($, global) {
    "use strict";
    var alertErroriSoggetto = $("#ERRORI_MODALE_SOGGETTO");

    /**
     * Crea un form a partire da un oggetto: i campi dell'oggetto saranno i campi del form, utilizzando la chiave come name e il valore come value.
     *
     * @param formAction (String) l'azione del form
     * @param obj        (Object) l'oggetto di popolamento del form
     *
     * @returns (JQuery) l'oggetto jQuery corrispondente al form
     */
    function createFormFromObject(formAction, obj) {
        var str = "<form action=\"" + formAction + "\" method=\"POST\" novalidate=\"novalidate\">";
        $.each(obj, function(idx, val) {
            str += "<input type=\"hidden\" name=\"" + idx + "\" value=\"" + val + "\" />";
        });
        str += "</form>";
        return $(str);
    }

    /**
     * Impostazione della tabella.
     */
    function impostaTabellaFatturaElettronica() {
        var tableId = "#tabellaRisultatiRicerca";
        var opts = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource : "risultatiRicercaFatturaElettronicaAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti risultati di ricerca secondo i parametri inseriti",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessuna fattura elettronica disponibile"
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $(tableId + "_processing").parent("div")
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#num_result').html(testo);
                // Nascondo il div del processing
                $(tableId + "_processing").parent("div")
                    .hide();
                $("a[rel='popover']", tableId).popover();
                $("a.tooltip-test", tableId).tooltip();
            },
            aoColumnDefs: [
                {aTargets: [0], sWidth: "20%", mData: "fornitore"},
                {aTargets: [1], mData: "dataEmissione"},
                {aTargets: [2], mData: "dataRicezione"},
                {aTargets: [3], mData: "numeroDocumento"},
                {aTargets: [4], mData: "tipoDocumentoFEL"},
                {aTargets: [5], mData: "dataAcquisizione"},
                {aTargets: [6], mData: "statoAcquisizione"},
                {aTargets: [7], mData: "importoLordo", fnCreatedCell: function(nTd) {
                    $(nTd).addClass("tab_Right");
                }},
                {aTargets: [8], mData: "azioni", fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass("tab_Right")
                        .find("a.consultaFatturaElettronica")
                            .click([oData], consultazioneFatturaElettronica)
                            .end()
                        .find("a.importaFatturaElettronica")
                            .click([oData], importazioneFatturaElettronica)
                            .end()
                        .find("a.sospendiFatturaElettronica")
                            .click([oData], sospensioneFatturaElettronica);
                }}
            ]
        };
        $(tableId).dataTable(opts);
    }


    /**
     * Consultazione della fattura elettronica.
     */
    function consultazioneFatturaElettronica(){
        var href = $(this).data("href");
        document.location = href;
    }

    /**
     * Importazione della fattura elettronica.
     */
    function importazioneFatturaElettronica(e){
        var obj = e.data[0] || {};
        var href = $(this).data("href");
        var formAction = href.split("?")[0];
        var table = $("#tabellaRisultatiRicerca").overlay("show");
        //ricercaSinteticaSoggetto
        var oggettoPerChiamataAjax = {"soggetto.codiceFiscale": obj.codiceFiscale || "", "soggetto.partitaIva": obj.partitaIva || ""};
        alertErroriSoggetto.slideUp();
        $.postJSON("ricercaSinteticaSoggetto.do", oggettoPerChiamataAjax, function(data) {
        	if(impostaDatiNegliAlert(data.errori, alertErroriSoggetto)) {
        		 proseguiImportazioneConModaleSoggetto(obj, formAction);
            }
        	if(data.listaSoggetti.length === 1){
        		 var soggetto = data.listaSoggetti[0];
        		 var frm = createFormFromObject(formAction, {"fatturaFEL.idFattura": obj.uid, "soggetto.uid": soggetto.uid, "soggetto.codiceSoggetto": soggetto.codiceSoggetto});
                 frm.appendTo("body")
                     .submit();
        	}else{
        		proseguiImportazioneConModaleSoggetto(obj, formAction); 
        	}
        	
    		table.overlay("hide");
        });
        
        
        
    }
    
    function proseguiImportazioneConModaleSoggetto(obj, formAction){
    	// Svuoto il form di ricerca soggetto
        $("#fieldsetRicercaGuidateSoggetto").find(":input")
            .val("");
        // Imposto codice fiscale e partita iva
        $("#codiceFiscaleSoggetto_modale").val(obj.codiceFiscale || "");
        $("#partitaIvaSoggetto_modale").val(obj.partitaIva || "");

        // Imposto l'azione per il submit
        $("#pulsanteConfermaSoggetto").substituteHandler("click", function() {
            var checkedSoggetto = $("#risultatiRicercaSoggetti").find("[name='checkSoggetto']:checked");
            var soggetto;
            var frm;
            // Controllo di aver impostato il soggetto
            if(!checkedSoggetto.length) {
                impostaDatiNegliAlert(["COR_ERR_0002 - Dato obbligatorio omesso: soggetto"], alertErroriSoggetto, false);
                return;
            }
            // Ho il soggetto e l'id della fattura. Invio i dati
            soggetto = checkedSoggetto.data("originalSoggetto") || {};
            // Creazione del form di invios
            frm = createFormFromObject(formAction, {"fatturaFEL.idFattura": obj.uid, "soggetto.uid": soggetto.uid, "soggetto.codiceSoggetto": soggetto.codiceSoggetto});
            // Invio del form
            frm.appendTo("body")
                .submit();
        });
        // Apro il modale
        $("#modaleGuidaSoggetto").modal("show");
    }

    /**
     * Sospensione della fattura elettronica
     */
    function sospensioneFatturaElettronica(e){
        var obj = e.data[0] || {};
        // Imposto i dati nella modale
        $("#spanModaleSospensione").html(obj.numeroDocumento || "");
        $("#idFatturaFatturaFELModaleSospensione").val(obj.uid || "");
        $("#noteFatturaFELModaleSospensione").val(obj.note || "");
        $("#modaleSospensione").modal("show");
    }

    $(function() {
        // Inizializzazione del datatable
        impostaTabellaFatturaElettronica();
    });
}(jQuery, this);