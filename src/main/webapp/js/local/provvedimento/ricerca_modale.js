/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 *************************************************************
 **** Funzioni del provvedimento: versione FIN col modale ****
 *************************************************************
 */

var Provvedimento = (function(w, exports) {

    var ZTreeClazz = w.ZTreePreDocumento;

    var optionsBase = {
        bPaginate : true,
        bLengthChange : false,
        bSort : false,
        bInfo : true,
        bAutoWidth : true,
        bFilter : false,
        bProcessing : true,
        bDestroy : true,
        oLanguage : {
            sInfo : "_START_ - _END_ di _MAX_ risultati",
            sInfoEmpty : "0 risultati",
            sProcessing : "Attendere prego...",
            sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti",
            oPaginate : {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec.",
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };
    var nomeAzioneDecentrata = $("#nomeAzioneDecentrata").val();
    var counter = 0;
    var instances = {};

    /**
     * Costruttore per il Provvedimento.
     *
     * @param suffix                 (String) il suffisso da apporre agli id
     * @param campoAnno              (String) il campo in cui impostare l'anno
     * @param campoNumero            (String) il campo in cui impostare il numero
     * @param campoTipoAtto          (String) il campo in cui impostare il tipo atto
     * @param treeSAC                (String) il campo dell'alberatura della Struttura Amministrativa Contabile
     * @param campoSAC               (String) il campo in cui impostare la Struttura Amministrativa Contabile
     * @param campoStato             (String) il campo in cui impostare lo stato
     * @param campoDescrizione       (String) il campo in cui impostare la descrizione
     * @param infix                  (String) l'id da immettere negli id
     * @param pulsanteApertura       (String) l'id del pulsante di apertura
     * @param allowSearchWithoutAnno (boolean) se permettere la ricerca senza impostare l'anno (Optional - default: false)
     */
    var InnerProvvedimento = function(suffix, campoAnno, campoNumero, campoTipoAtto, treeSAC, campoSAC, campoStato, campoDescrizione, infix, pulsanteApertura, allowSearchWithoutAnno) {
        this.idSuffix = suffix;
        this.idInfix = infix;
        this.$alertErrori = $("#ERRORI_MODALE_PROVVEDIMENTO" + this.idSuffix);
        this.$campoUid = $("#uidAttoAmministrativo" + this.idSuffix);
        this.$campoAnno = $(campoAnno + this.idSuffix);
        this.$campoNumero = $(campoNumero + this.idSuffix);
        this.$campoTipoAtto = $(campoTipoAtto + this.idSuffix);
        this.$treeSAC = $(treeSAC + this.idSuffix);
        this.$campoSAC = $(campoSAC + this.idSuffix);
        this.$campoStato = $(campoStato + this.idSuffix);
        this.$campoDescrizione = $(campoDescrizione + this.idSuffix);
        this.$pulsanteApertura = $(pulsanteApertura + this.idSuffix);
        this.allowSearchWithoutAnno = allowSearchWithoutAnno;

        this.$table = $("#risultatiRicercaProvvedimento" + this.idSuffix);
        this.$hiddenUidProvvedimento = $("#HIDDEN_uidProvvedimento" + this.idSuffix);
        this.$divTabella = $("#divContenitoreTabellaProvvedimento" + this.idSuffix);
        this.$fieldsetRicerca = $("#fieldsetRicercaGuidateProvvedimento" + this.idSuffix);
        this.$annoProvvedimentoModale = $("#annoProvvedimento_modale" + this.idSuffix);
        this.$numeroProvvedimentoModale = $("#numeroProvvedimento_modale" + this.idSuffix);
        this.$tipoAttoModale = $("#tipoAttoProvvedimento_modale" + this.idSuffix);
        this.$modal = $("#modaleGuidaProvvedimento" + this.idSuffix);
    };

    /**
     * Costruttore
     */
    InnerProvvedimento.prototype.constructor = InnerProvvedimento;

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per il Provvedimento. Caso della ricerca senza operazioni.
     *
     * @param aaData (Array) i dati da impostare nel dataTable
     */
    InnerProvvedimento.prototype.impostaDataTableProvvedimentoSenzaOperazioni = function(aaData) {
        var self = this;
        var optionsNuove = {
            aaData : aaData,
            iDisplayLength : 3,
            bSort : false,
            aoColumnDefs : [
                {aTargets : [ 0 ], mData : function() {
                    var radio = "<input type='radio' name='uidProvvedimentoRadio' value=''";
                    if (self.$hiddenUidProvvedimento.val() !== undefined && parseInt(self.$hiddenUidProvvedimento.val(), 10) === val) {
                        radio += " checked='checked'";
                    }
                    radio += "/>";
                    return radio;
                }, fnCreatedCell : function(nTd, sData, oData) {
                    $(nTd).find("input").data("sourceProvvedimento", oData);
                }},
                {aTargets : [ 1 ], mData : "anno"},
                {aTargets : [ 2 ], mData : "numero"},
                {aTargets : [ 3 ], mData : "tipo"},
                {aTargets : [ 4 ], mData : "oggetto"},
                {aTargets : [ 5 ], mData : "strutturaAmministrativoContabile"},
                {aTargets : [ 6 ], mData : "stato"}
            ]
        };
        var options = $.extend(true, {}, optionsBase, optionsNuove);
        // Devo pulire la vecchia tabella
        if($.fn.DataTable.fnIsDataTable(this.$table[0])) {
            this.$table.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        this.$table.dataTable(options);
    };

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     */
    InnerProvvedimento.prototype.cercaProvvedimento = function() {
        var idSuffix = this.idSuffix;
        var self = this;

        // Valori per il check javascript
        var annoProvvedimento = this.$annoProvvedimentoModale.val();
        var numeroProvvedimento = this.$numeroProvvedimentoModale.val();
        var tipoProvvedimento = this.$tipoAttoModale.val();
        // Errori
        var erroriArray = [];
        var obj = {};

        // Spinner
        var spinner = $("#SPINNER_Provvedimento" + idSuffix);

        this.$divTabella.slideUp();

        // Controllo che non vi siano errori
        if (!this.allowSearchWithoutAnno && annoProvvedimento === "") {
            erroriArray.push("Il campo Anno deve essere selezionato");
        }
        if (numeroProvvedimento === "" && tipoProvvedimento === "") {
            erroriArray.push("Almeno uno tra i campi Numero e Tipo deve essere compilato");
        }

        if (impostaDatiNegliAlert(erroriArray, this.$alertErrori)) {
            return;
        }
        spinner.addClass("activated");
        obj = unqualify(this.$fieldsetRicerca.serializeObject(), 1);
        self.$alertErrori.slideUp();

        // Chiamata AJAX
        $.postJSON("effettuaRicercaSenzaOperazioniProvvedimento.do", obj, function(data) {
            var errori = data.errori;
            var listaProvvedimenti = data.listaElementoProvvedimento;

            // Tabella gia' in dataTable
            if(impostaDatiNegliAlert(errori, self.$alertErrori)) {
                // Errori presenti: esco
                return;
            }

            // Non vi sono errori, ne' messaggi, ne' informazioni: proseguire con la creazione del dataTable
            self.impostaDataTableProvvedimentoSenzaOperazioni(listaProvvedimenti);
            self.$divTabella.slideDown();
            self.$table.find("input[name='uidProvvedimentoRadio']:checked")
                .removeAttr("selected")
                .removeAttr("checked");
        }).always(function() {
            spinner.removeClass("activated");
        });
    };

    /**
     * Deseleziona il provvedimento.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    InnerProvvedimento.prototype.deseleziona = function(e) {
        // Se ho un evento, lo annullo
        e && e.preventDefault();

        this.$table.find("input[type='radio']")
            .prop("checked", false);
    };

    /**
     * Carica struttura: carica la struttura amministrativa.
     */
    InnerProvvedimento.prototype.caricaStrutture = function() {
        var self = this;
        var oggettoPerChiamataAjax = {};

        if(nomeAzioneDecentrata) {
            oggettoPerChiamataAjax.nomeAzioneDecentrata = nomeAzioneDecentrata;
        }

        $.postJSON("ajax/strutturaAmministrativoContabileAjax.do", oggettoPerChiamataAjax, function(data) {
            var listaStrutturaAmministrativoContabile = (data.listaElementoCodifica);
            var idSuffix = self.idSuffix;

            ZTreeClazz.imposta("#" + self.$treeSAC.attr("id"), {}, listaStrutturaAmministrativoContabile,
                "#HIDDEN_StrutturaAmministrativoContabile" + self.idInfix + "Uid" + idSuffix,
                "#HIDDEN_StrutturaAmministrativoContabile" + self.idInfix + "Stringa" + idSuffix,
                "#SPAN_StrutturaAmministrativoContabile" + self.idInfix + idSuffix,
                "#BUTTON_deselezionaStrutturaAmministrativoContabile" + self.idInfix + idSuffix,
                idSuffix);
            ZTreeClazz.imposta("#treeStruttAmm_modale" + idSuffix, {}, listaStrutturaAmministrativoContabile,
                "#HIDDEN_StrutturaAmministrativoContabileUid_modale" + idSuffix,
                "#HIDDEN_StrutturaAmministrativoContabileStringa_modale" + idSuffix,
                "#SPAN_StrutturaAmministrativoContabile_modale" + idSuffix,
                "#BUTTON_deselezionaStrutturaAmministrativoContabile_modale" + self.idInfix + idSuffix,
            idSuffix);

            $(document).trigger({
                type : "struttureAmministrativeCaricate",
                lista : listaStrutturaAmministrativoContabile
            });
        });
    };

    /**
     * Conferma provvedimento: imposta i dati nei campi della pagina che apre il modale
     */
    InnerProvvedimento.prototype.confermaProvvedimento = function(e) {
        var self = this;
        var checkedProvvedimento = this.$table.find("[name='uidProvvedimentoRadio']:checked");
        var provvedimento;
        var tree = $.fn.zTree.getZTreeObj(this.$treeSAC.attr("id"));

        e.preventDefault();

        // Tolgo l'eventuale tipoAtto dal DOM, se presente
        $("#HIDDEN_tipoAttoUid").remove();

        if (checkedProvvedimento.length === 0) {
            impostaDatiNegliAlert([ "Necessario selezionare un provvedimento" ], this.$alertErrori, false);
            return;
        }

        provvedimento = checkedProvvedimento.data("sourceProvvedimento");

        this.$campoUid.val(provvedimento.uid);
        this.$campoAnno.val(provvedimento.anno).attr("readonly", "readonly");
        this.$campoNumero.val(provvedimento.numero)
                .attr("readonly", "readonly");
        this.$campoTipoAtto.val(provvedimento.uidTipo)
            .attr("disabled", "disabled")
                .after($("<input>").attr({
                    type: "hidden",
                    value: self.$campoTipoAtto.val(),
                    name: self.$campoTipoAtto.attr("name"),
                    id: "HIDDEN_tipoAttoUid"
                }));
        this.$campoSAC.val(provvedimento.uidStrutturaAmministrativoContabile);
        this.$campoStato.val(provvedimento.stato);

        tree && ZTreeClazz.deselezionaNodi(tree.innerZTree);
        tree && ZTreeClazz.selezionaNodoSeApplicabileEDisabilitaAlberatura(tree.innerZTree, provvedimento.uidStrutturaAmministrativoContabile);

        this.$campoDescrizione.html(getDescrizioneByProvvedimento(provvedimento));
        this.$modal.modal("hide");
    };

    /**
     * Ottiene la descrizione del provvedimento fornito.
     *
     * @param provvedimento (Object) il provvedimento la cui descrizione e' da ottenere
     *
     * @returns la descrizione del provvedimento
     */
    function getDescrizioneByProvvedimento(provvedimento) {
        return !!provvedimento
            ? (": " + provvedimento.anno + " / " + provvedimento.numero + " - " + provvedimento.tipo + " - " + provvedimento.oggetto + " - " + provvedimento.strutturaAmministrativoContabile + " - Stato:" + provvedimento.stato)
            : "";
    }

    /**
     * Apre il modale del provvedimento
     *
     * @param e (Event) l'evento invocante la funzione
     */
    InnerProvvedimento.prototype.apriModaleProvvedimento = function(e) {
        var idSuffix = this.idSuffix;
        var tree = $.fn.zTree.getZTreeObj("treeStruttAmm_modale");

        e.preventDefault();
        this.$alertErrori.slideUp();
        this.$divTabella.slideUp();
        this.$table.find("input[type=\"radio\"]").removeProp("checked").removeAttr("checked");

        $("#accordionPadreStrutturaAmministrativa_modale" + idSuffix).on("click", function() {
            $("#struttAmm_modale" + idSuffix).collapse("toggle");
            $(this).closest(".struttAmm").toggleClass("span11 span9");
        });

        // Apro il modale
        this.$annoProvvedimentoModale.val(this.$campoAnno.val());
        this.$numeroProvvedimentoModale.val(this.$campoNumero.val());
        this.$tipoAttoModale.val(this.$campoTipoAtto.val());
        // Seleziono dallo zTree
        tree && ZTreeClazz.deselezionaNodi(tree.innerZTree);
        tree && ZTreeClazz.selezionaNodoSeApplicabile(tree.innerZTree, this.$campoSAC.val());

        this.$modal.modal("show");
    };

    /**
     * Annulla i campi della ricerca.
     */
    InnerProvvedimento.prototype.annullaCampiRicerca = function() {
        var idSuffix = this.idSuffix;
        var uid = parseInt($("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid_modale").val(), 10);
        // Pulizia dei campi
        this.$annoProvvedimentoModale.val("");
        this.$numeroProvvedimentoModale.val("");
        this.$tipoAttoModale.val("");
        $("#oggettoProvvedimento_modale" + idSuffix).val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Uid_modale").val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Codice_modale").val("");
        $("#HIDDEN_StrutturaAmministrativoContabile" + idSuffix + "Descrizione_modale").val("");
        this.$divTabella.slideUp();
        if(!isNaN(uid)) {
            ZTreeClazz.deselezionaNodoSeApplicabileEAbilitaAlberatura($.fn.zTree.getZTreeObj("treeStruttAmm_modale" + idSuffix).innerZTree, uid);
        }
    };
    
    /**
     * Effettua il reset del form
     */
    InnerProvvedimento.prototype.doReset = function() {
        cleanDataTables();
        $("#tabellaProvvedimento" + this.idSuffix).slideUp();
        this.deseleziona();

        $("#collapseProvvedimento" + this.idSuffix).filter(".in")
            .collapse("hide");
        
        // Ripristino l'editabilita' dei campi
        this.$campoAnno.removeAttr("disabled");
        this.$campoNumero.removeAttr("disabled");
        this.$campoTipoAtto.removeAttr("disabled");
    };

    /**
     * Inizializzazione del JavaScript.
     *
     * @param suffix                 (String) il suffisso per gli id (Optional - default: "")
     * @param campoAnno              (String) il campo per popolare l'anno (Optional - default: #annoProvvedimento)
     * @param campoNumero            (String) il campo per popolare il numero (Optional - default: #numeroProvvedimento)
     * @param campoTipoAtto          (String) il campo per popolare il tipo atto (Optional - default: #tipoAttoProvvedimento)
     * @param treeSAC                (String) il campo per il tree della struttura amministrativo contabile (Optional - default: #treeStruttAmm)
     * @param campoSAC               (String) il campo per popolare la struttura amministrativo contabile (Optional - default: #HIDDEN_StrutturaAmministrativoContabile)
     * @param campoStato             (String) il campo per popolare lo stato (Optional - default: #HIDDEN_statoProvvedimento)
     * @param campoDescrizione       (String) il campo per popolare la descrizione (Optional - default: #SPAN_InformazioniProvvedimento)
     * @param infix                  (String) il valore intermedio degli uid (Optional - default: "")
     * @param pulsanteApertura       (String) il pulsante di apertura (Optional - default: #pulsanteApriModaleProvvedimento)
     * @param allowSearchWithoutAnno (boolean) se permettere la ricerca senza impostare l'anno (Optional - default: false)
     * @return (number) l'id del gestore
     */
    exports.inizializzazione = function(suffix, campoAnno, campoNumero, campoTipoAtto, treeSAC, campoSAC, campoStato, campoDescrizione, infix, pulsanteApertura, allowSearchWithoutAnno) {
        var inner = new InnerProvvedimento(suffix || "", campoAnno || "#annoProvvedimento", campoNumero || "#numeroProvvedimento",
                campoTipoAtto || "#tipoAttoProvvedimento", treeSAC || "#treeStruttAmm", campoSAC || "#HIDDEN_StrutturaAmministrativoContabile",
                campoStato || "#HIDDEN_statoProvvedimento", campoDescrizione || "#SPAN_InformazioniProvvedimento", infix || "", pulsanteApertura || '#pulsanteApriModaleProvvedimento',
                allowSearchWithoutAnno || false);
        var idSuffix = inner.idSuffix;
        var id = counter++;

        inner.doResetBound = inner.doReset.bind(inner);

        $("#pulsanteRicercaProvvedimento" + idSuffix).substituteHandler("click", inner.cercaProvvedimento.bind(inner));
        $("#pulsanteDeselezionaProvvedimento" + idSuffix).substituteHandler("click", inner.deseleziona.bind(inner));
        $("#pulsanteConfermaProvvedimento" + idSuffix).substituteHandler("click", inner.confermaProvvedimento.bind(inner));
        $(inner.$pulsanteApertura).substituteHandler("click", inner.apriModaleProvvedimento.bind(inner));
        $("#pulsanteAnnullaRicercaProvvedimento" + idSuffix).substituteHandler("click", inner.annullaCampiRicerca.bind(inner));

        inner.caricaStrutture();

        // Cancella la tabella dei provvedimenti quando si effettua un reset del
        // form
        $("form").on("reset", inner.doResetBound);

        $("#accordionPadreStrutturaAmministrativa" + idSuffix).on("click", function() {
            $("#struttAmm" + idSuffix).collapse("toggle");
        });
        instances[id] = inner;
        return id;
    };
    /**
     * Distruzione del gestore.
     * @param id (number) l'id del gestore
     */
    exports.destroy = function(id) {
        var instance = instances[id];
        if(!instance) {
            return;
        }
        
        $('#pulsanteRicercaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteDeselezionaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteConfermaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteAnnullaRicercaProvvedimento' + instance.idSuffix).off('click');
        $('#accordionPadreStrutturaAmministrativa' + instance.idSuffix).off('click');
        $(instance.$pulsanteApertura).off('click');
        $('form').off('reset', instance.doResetBound);

        instances[id] = undefined;
        instance = undefined;
    };

    return exports;
}(window, Provvedimento || {}));