/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *************************************************************
 **** Funzioni del provvedimento: versione FIN col modale ****
 *************************************************************
 */

var Provvedimento = (function(exports) {

    var optionsBase = {
        bPaginate: true,
        bLengthChange: false,
        bSort: false,
        bInfo: true,
        bAutoWidth: true,
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
                sEmptyTable: "Nessun dato disponibile"
            }
        }
    };

    /**
     * Costruttore per il Provvedimento.
     *
     * @param suffix (String) il suffisso da apporre agli id (Optional - default: "")
     */
    var InnerProvvedimento = function(/* Optional */ suffix) {
        this.idSuffix = suffix || "";
        this.$alertErrori = $("#ERRORI_MODALE_PROVVEDIMENTO" + this.idSuffix);
    };

    /**
     * Costruttore
     */
    InnerProvvedimento.prototype.constructor = InnerProvvedimento;

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per il Provvedimento. Caso della ricerca senza operazioni.
     *
     * @param aaData {Array} i dati da impostare nel dataTable
     */
    InnerProvvedimento.prototype.impostaDataTableProvvedimentoSenzaOperazioni = function(aaData) {
        var self = this;
        var optionsNuove = {
            "aaData": aaData,
            iDisplayLength: 3,
            bSort: false,
            aoColumnDefs: [
                {
                    aTargets: [ 0 ],
                    mData: function(source, type) {
                    	var tmp = "<input type='radio' name='uidProvvedimentoRadio' value=''";
                    	if($("#HIDDEN_uidProvvedimento" + self.idSuffix).val() !== undefined &&
                                parseInt($("#HIDDEN_uidProvvedimento" + self.idSuffix).val(), 10) === source.uid) {
                    		tmp += " checked='checked'";
                    	}
                    	return tmp + "/>";
                    },
                    fnCreatedCell : function(nTd, sData, oData){
                        $(nTd).find("input").data("sourceProvvedimento", oData);
                    }
                },
                {aTargets : [ 1 ], mData : "anno"},
                {aTargets : [ 2 ], mData : "numero"},
                {aTargets : [ 3 ], mData : "tipo"},
                {aTargets : [ 4 ], mData : "oggetto"},
                {aTargets : [ 5 ], mData : "strutturaAmministrativoContabile"},
                {aTargets : [ 6 ], mData : "stato"}
            ]
        };
        var options = $.extend(true, {}, optionsBase, optionsNuove);
        var tabellaGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($("#risultatiRicercaProvvedimento" + this.idSuffix));
        // Pulisco il dataTable prima di inizializzarne uno nuovo
        if(tabellaGiaInDataTable.length > 0) {
            // Distruggo la dataTable
            tabellaGiaInDataTable.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        $("#risultatiRicercaProvvedimento" + this.idSuffix).dataTable(options);
    };

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     */
    InnerProvvedimento.prototype.cercaProvvedimento = function() {
        var idSuffix = this.idSuffix;
        var alertErrori = this.$alertErrori;
        var self = this;

        var divTabella = $("#divContenitoreTabellaProvvedimento" + idSuffix).slideUp();
        // Workaround per la gestione del fieldset su Explorer
        var formDiRicerca = $("#fieldsetRicercaGuidateProvvedimento" + idSuffix);
            // Valori per il check javascript
        var annoProvvedimento = $("#annoProvvedimento_modale" + idSuffix).val();
        var numeroProvvedimento = $("#numeroProvvedimento_modale" + idSuffix).val();
        var tipoProvvedimento =$("#tipoAttoProvvedimento_modale" + idSuffix).val();
            // Errori
        var erroriArray = [];

            // Spinner
        var spinner = $("#SPINNER_Provvedimento" + idSuffix);

        // Controllo che non vi siano errori
        if(annoProvvedimento === "") {
            erroriArray.push("Il campo Anno deve essere selezionato");
        }
        if(numeroProvvedimento === "" && tipoProvvedimento === "") {
            erroriArray.push("Almeno uno tra i campi Numero e Tipo deve essere compilato");
        }

        if(impostaDatiNegliAlert(erroriArray, alertErrori)) {
            return;
        }
        spinner.addClass("activated");

        // Chiamata AJAX
        $.postJSON(
            // Dovrebbe andare bene
            "effettuaRicercaSenzaOperazioniProvvedimento.do",
            unqualify(formDiRicerca.serializeObject(), 1),
            function (data) {
                var errori = data.errori;
                var listaProvvedimenti = data.listaElementoProvvedimento;

                // Tabella già in dataTable
                var tabellaGiaInDataTable = $($.fn.dataTable.fnTables(true)).filter($("#risultatiRicercaProvvedimento" + idSuffix));
                var invocazioneValida = !impostaDatiNegliAlert(errori, alertErrori);

                if(!invocazioneValida) {
                    // L'invocazione è invalida
                    // Pulisco la dataTable, se presente; la inizializzo vuota in caso contrario
                    if(tabellaGiaInDataTable.length > 0) {
                        tabellaGiaInDataTable.dataTable().fnClearTable();
                    } else {
                        self.impostaDataTableProvvedimentoSenzaOperazioni([]);
                    }
                    return;
                }

                alertErrori.slideUp();

                // Non vi sono errori, né messaggi, né informazioni. Proseguire con la creazione del dataTable
                self.impostaDataTableProvvedimentoSenzaOperazioni(listaProvvedimenti);

                divTabella.slideDown();
                $("input[name='uidProvvedimentoRadio']:checked", "#risultatiRicercaProvvedimento" + idSuffix).removeAttr("selected")
                    .removeAttr("checked");
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    };

    /**
     * Deseleziona il provvedimento.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    InnerProvvedimento.prototype.deseleziona = function(e) {
        var idSuffix = this.idSuffix;
        var tabella = $("#risultatiRicercaProvvedimento" + idSuffix);
        // Se ho un evento, lo annullo
        e && e.preventDefault();

        tabella.find("input[type='radio']")
            .prop("checked", false);
    };

    /**
     * Carica struttura: carica la struttura amministrativa
     */
    InnerProvvedimento.prototype.caricaStrutture = function() {
        var self = this;
        $.postJSON(
            "ajax/strutturaAmministrativoContabileAjax.do",
            {},
            function (data) {
                var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
                var idSuffix = self.idSuffix;
                var event = $.Event('sacCaricata', {listaStrutturaAmministrativoContabile: listaStrutturaAmministrativoContabile});
                ZTreeDocumento.imposta("treeStruttAmm" + idSuffix, ZTreeDocumento.SettingsBase, listaStrutturaAmministrativoContabile, "HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid", idSuffix);
                ZTreeDocumento.imposta("treeStruttAmm_modale" + idSuffix, ZTreeDocumento.SettingsBase, listaStrutturaAmministrativoContabile,"HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid_modale", idSuffix);
                $(document).trigger(event);
            }
        );
    };

    /**
     * Conferma provvedimento : imposta i dati nei campi della pagina che apre il modale
     */
    InnerProvvedimento.prototype.confermaProvvedimento = function(e) {
        var idSuffix = this.idSuffix;

        var checkedProvvedimento = $("#risultatiRicercaProvvedimento" + idSuffix).find("[name='uidProvvedimentoRadio']:checked");

        var campoInCuiApplicareAnnoProvvedimento = $("#annoProvvedimento" + idSuffix);
        var campoInCuiApplicareNumeroProvvedimento = $("#numeroProvvedimento" + idSuffix);
        var campoInCuiApplicareTipoProvvedimento = $("#tipoAttoProvvedimento" + idSuffix);
        var campoInCuiApplicareStatoProvvedimento = $("#HIDDEN_statoProvvedimento" + idSuffix);
        var campoInCuiApplicareStrutturaAmm= $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Desc");
        var campoInCuiApplicareUidProvvedimento = $("#HIDDEN_uidProvvedimento" + idSuffix);
        var provvedimento;

        e.preventDefault();

        if(checkedProvvedimento.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un provvedimento"], this.$alertErrori, false);
            return;
        }

        provvedimento = checkedProvvedimento.data("sourceProvvedimento");

        $('input[type="hidden"][name="' + campoInCuiApplicareTipoProvvedimento.attr("name") + '"]').remove();
        campoInCuiApplicareUidProvvedimento.val(provvedimento.uid);
        campoInCuiApplicareAnnoProvvedimento.val(provvedimento.anno)
            .attr("readonly", "readonly");
        campoInCuiApplicareNumeroProvvedimento.val(provvedimento.numero)
            .attr("readonly", "readonly");
        campoInCuiApplicareTipoProvvedimento.val(provvedimento.uidTipo)
            .attr("disabled", "disabled")
            .after(
                $("<input>").attr({
                    type: "hidden",
                    value: campoInCuiApplicareTipoProvvedimento.val(),
                    name: campoInCuiApplicareTipoProvvedimento.attr("name")
                })
            );

        campoInCuiApplicareStrutturaAmm.val(provvedimento.uidStrutturaAmministrativoContabile);
        ZTreeDocumento.selezionaNodoSeApplicabileEDisabilitaAlberatura("treeStruttAmm" + idSuffix, provvedimento.uidStrutturaAmministrativoContabile);
        $("#treeStruttAmm" + idSuffix)
            .find("li")
            .find("input[type='radio']");

        campoInCuiApplicareStatoProvvedimento.val(provvedimento.stato);
        $("#SPAN_InformazioniProvvedimento" + idSuffix).html(": " + provvedimento.anno + " / " + provvedimento.numero + " - " + provvedimento.tipo
                        + " - " + provvedimento.oggetto + " - " + provvedimento.strutturaAmministrativoContabile + " - Stato:" + provvedimento.stato);
        $("#SPAN_InformazioniProvvedimento" + idSuffix).trigger('change'); 
        $("#modaleGuidaProvvedimento" + idSuffix).modal("hide");
    };

    /**
     * Apre il modale del provvedimento
     *
     * @param e (Event) l'evento invocante la funzione
     */
    InnerProvvedimento.prototype.apriModaleProvvedimento = function(e) {
        var idSuffix = this.idSuffix;

        e.preventDefault();
        this.$alertErrori.slideUp();

        $("#accordionPadreStrutturaAmministrativa_modale" + idSuffix).on("click", function() {
            $("#struttAmm_modale" + idSuffix).collapse("toggle");
            $(this).closest(".struttAmm")
                    .toggleClass("span11 span9");
        });

        // Apro il modale
        $("#annoProvvedimento_modale"+idSuffix).val($("#annoProvvedimento"+idSuffix).val());
        $("#numeroProvvedimento_modale"+idSuffix).val($("#numeroProvvedimento"+idSuffix).val());
        $("#tipoAttoProvvedimento_modale"+idSuffix).val($("#tipoAttoProvvedimento"+idSuffix).val());

        $("#modaleGuidaProvvedimento" + idSuffix).modal("show");

    };

    /**
     * Annulla i campi della ricerca.
     */
    InnerProvvedimento.prototype.annullaCampiRicerca = function() {
        var idSuffix = this.idSuffix;
        var uid = parseInt($("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid_modale").val(), 10);
        // Pulizia dei campi
        $("#annoProvvedimento_modale" + idSuffix).val("");
        $("#numeroProvvedimento_modale" + idSuffix).val("");
        $("#tipoAttoProvvedimento_modale" + idSuffix).val("");
        $("#oggettoProvvedimento_modale" + idSuffix).val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid_modale").val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Codice_modale").val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Descrizione_modale").val("");
        $("#divContenitoreTabellaProvvedimento" + idSuffix).slideUp();
        if(!isNaN(uid)) {
            ZTreeClazz.deselezionaNodoSeApplicabileEAbilitaAlberatura($.fn.zTree.getZTreeObj("treeStruttAmm_modale" + idSuffix).innerZTree, uid);
        }
    };

    /**
     * Inizializzazione del JavaScript.
     *
     * @param suffix (String) il suffisso per gli id
     */
    exports.inizializzazione = function(suffix) {
        var inner = new InnerProvvedimento(suffix);
        var idSuffix = inner.idSuffix;

        $("#pulsanteRicercaProvvedimento" + idSuffix).substituteHandler("click", function(e) {
            inner.cercaProvvedimento(e);
        });
        $("#pulsanteDeselezionaProvvedimento" + idSuffix).substituteHandler("click", function(e) {
            inner.deseleziona(e);
        });
        $("#pulsanteConfermaProvvedimento" + idSuffix).substituteHandler("click", function(e) {
            inner.confermaProvvedimento(e);
        });
        $("#pulsanteApriModaleProvvedimento" + idSuffix).substituteHandler("click", function(e) {
            inner.apriModaleProvvedimento(e);
        });
        $("#pulsanteAnnullaRicercaProvvedimento" + idSuffix).substituteHandler("click", function() {
            inner.annullaCampiRicerca();
        });

        inner.caricaStrutture();

        var annoProvvedimento = $("#annoProvvedimento_modale" + idSuffix).val(),
            numeroProvvedimento = $("#numeroProvvedimento_modale" + idSuffix).val(),
            tipoProvvedimento =$("#tipoAttoProvvedimento_modale" + idSuffix).val();

        if(parseInt(annoProvvedimento, 10) && (parseInt(numeroProvvedimento, 10) || parseInt(tipoProvvedimento, 10))) {
            // I dati sono valorizzati: effettuare la ricerca
            inner.cercaProvvedimento();
        }

        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $("form").on("reset", function() {
            cleanDataTables();
            $("#tabellaProvvedimento" + idSuffix).slideUp();
            $("#HIDDEN_uidProvvedimento" + idSuffix).val('');
            inner.deseleziona();

            $("#collapseProvvedimento" + idSuffix).filter(".in")
                .collapse("hide");
        });

        $("#accordionPadreStrutturaAmministrativa" + idSuffix).on("click", function() {
            $("#struttAmm" + idSuffix).collapse("toggle");
        });
    };

    return exports;
} (Provvedimento || {}));