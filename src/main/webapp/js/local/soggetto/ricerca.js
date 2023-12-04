/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
var Soggetto = (function () {
    var exports = {};

    var alertErrori = $("#ERRORI_MODALE_SOGGETTO");

    var campoInCuiApplicareIlCodice;
    var campoInCuiApplicareIlCodiceFiscale;
    var campoInCuiApplicareLaDenominazione;
    var campoInCuiApplicareLaDescrizione;
    var campoInCuiApplicareUid;
    var campoPulsanteApertura;
    var ambito;
    var uidSoggetto;

    var baseOpts = {
        bServerSide : false,
        bPaginate : true,
        bLengthChange : false,
        iDisplayLength : 5,
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
            oPaginate : {
                sFirst : "inizio",
                sLast : "fine",
                sNext : "succ.",
                sPrevious : "prec.",
                sEmptyTable : "Nessun dato disponibile"
            }
        }
    };

    /**
     * Caricamento via Ajax della tabella dei soggetti e visualizzazione.
     *
     * @param lista (Array) la lista dei soggetti da utilizzare per la creazione della tabella
     */
    function tabellaSoggetti(lista) {
        var options = {
            bSort : true,
            aaData : lista,
            oLanguage : {
                sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            aoColumnDefs : [
                {aTargets : [ 0 ], mData : function() {
                    return "<input type='radio' name='checkSoggetto'/>";
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalSoggetto", oData);
                }},
                {aTargets : [ 1 ], mData : defaultPerDataTable("codiceSoggetto")},
                {aTargets : [ 2 ], mData : defaultPerDataTable("codiceFiscale"), bSortable: false},
                {aTargets : [ 3 ], mData : defaultPerDataTable("partitaIva"), bSortable: false},
                {aTargets : [ 4 ], mData : defaultPerDataTable("denominazione")},
                {aTargets : [ 5 ], mData : defaultPerDataTable("statoOperativo"), bSortable: false}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);

        $("#risultatiRicercaSoggetti").dataTable(opts);
    }

    /**
     * Imposta la tabella delle sedi secondarie del soggetto.
     *
     * @param lista                    (Array)  la lista delle sedi
     * @param tabella                  (jQuery) la tabella da popolare
     * @param tabellaModalitaPagamento (jQuery) la tabella delle modalita
     */
    function impostaTabellaSedeSecondariaSoggetto(lista, tabella, tabellaModalitaPagamento) {
        var oldUid = parseInt($("#HIDDEN_sedeSecondariaSoggettoUid").val(), 10);
        var options = {
            aaData: lista,
            oLanguage: {
                sZeroRecords: "Non sono presenti sedi associate"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var campo = "<input type='radio' name='sedeSecondariaSoggetto.uid' value='" + source.uid + "'";
                    if(source.uid === oldUid) {
                        campo += " checked";
                    }
                    campo += " />";
                    return campo;
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalData", oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('denominazione')},
                {aTargets: [2], mData: function(source) {
                    var indirizzo = source.indirizzoSoggettoPrincipale;
                    var result = "";
                    if(indirizzo) {
                        result = indirizzo.sedime + " " + indirizzo.denominazione + ", " + indirizzo.numeroCivico;
                    }
                    return result;
                }},
                {aTargets: [3], mData: defaultPerDataTable('indirizzoSoggettoPrincipale.comune')},
                {aTargets: [4], mData: defaultPerDataTable('statoOperativoSedeSecondaria')}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        tabella.dataTable(opts);
    }

    /**
     * Crea il campo radio per la modalita di pagamento del soggetto per l'uid fornito, fornendo il check del campo nel caso in cui sia pari al precedente valore.
     *
     * @param uid    (Number) l'uid per cui creare il campo
     * @param oldUid (Number) il precedente uid
     * @param codSog (String) il codice del soggetto di cessione (Optional - default: undefined)
     */
    function creaRadioModalitaPagamentoSoggettoByUid(uid, oldUid, codSog) {
        var res = "<input type='radio' name='modalitaPagamentoSoggetto.uid' value='" + uid + "' ";
        if(uid === oldUid) {
            res += "checked ";
        }
        // Accetto anche la stringa vuota
        if(codSog != undefined) {
            res += "data-cessione-cod-soggetto=\"" + codSog + "\" ";
        }
        res += "/>";
        return res;
    }

    /**
     * Imposta la cessione del codiceSoggetto
     */
    function impostaCessioneCodSoggetto() {
        var modalitaSelezionata = $("input[name='modalitaPagamentoSoggetto.uid']").find(":selected");
        var cessioneCodSoggetto;
        var hiddenField;

        // Rimuovo il campo
        $("#__cessioneCodSoggetto__modalitaPagamentoSoggetto__").remove();
        if(!modalitaSelezionata.length || modalitaSelezionata.data('cessioneCodSoggetto') == undefined) {
            // Esco
            return;
        }
        // Creo il campo
        cessioneCodSoggetto = modalitaSelezionata.data('cessioneCodSoggetto');
        hiddenField = $("<input />", {type: "hidden", value: cessioneCodSoggetto, id: "__cessioneCodSoggetto__modalitaPagamentoSoggetto__"});
        hiddenField.insertAfter(modalitaSelezionata);
    }

    /**
     * Imposta la tabella delle modalità di pagamento del soggetto.
     *
     * @param lista   (Array)  la lista delle modalità
     * @param tabella (jQuery) la tabella da popolare
     */
    function impostaTabellaModalitaPagamentoSoggetto(lista, tabella) {
        var oldUid = parseInt($("#HIDDEN_modalitaPagamentoSoggettoUid").val(), 10);
        var options = {
            aaData: lista,
            oLanguage: {
                sZeroRecords: "Non sono presenti modalit&agrave; associate"
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    return creaRadioModalitaPagamentoSoggettoByUid(source.modalitaPagamentoSoggettoCessione2 ? source.modalitaPagamentoSoggettoCessione2.uid : source.uid, oldUid, source.cessioneCodSoggetto);
                }, fnCreatedCell: function(nTd, sData, oData) {
                    var self = $("input", nTd).data("originalData", oData);
                    if(oData.modalitaAccreditoSoggetto) {
                        self.data("codiceModalitaPagamento", oData.modalitaAccreditoSoggetto.codice);
                    }
                }},
                {aTargets: [1], mData: defaultPerDataTable('codiceModalitaPagamento')},
                {aTargets: [2], mData: defaultPerDataTable('descrizioneInfo.descrizioneArricchita')},
                {aTargets: [3], mData: defaultPerDataTable('associatoA')},
                {aTargets: [4], mData: defaultPerDataTable('descrizioneStatoModalitaPagamento')}
            ]
        };

        var opts = $.extend(true, {}, baseOpts, options);
        tabella.dataTable(opts);
    }

    /**
     * Richiama l'esecuzione della ricerca Soggetto.
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function cercaSoggetti(e) {
        var oggettoPerChiamataAjax = unqualify($("#fieldsetRicercaGuidateSoggetto").serializeObject(), 1);
        var divTabella = $("#divTabellaSoggetti").slideUp();
        var spinner = $("#SPINNER_RicercaSoggetto");
        e.preventDefault();
        alertErrori.slideUp();
        oggettoPerChiamataAjax.codiceAmbito = ambito;

        spinner.addClass("activated");
        $.postJSON(
            "ricercaSinteticaSoggetto.do",
            oggettoPerChiamataAjax,
            function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori, false)) {
                    return;
                }

                // Carico i dati in tabella
                tabellaSoggetti(data.listaSoggetti);
                divTabella.slideDown();
            }
        ).always(function() {
            spinner.removeClass("activated");
        });
    }

    /**
     * Imposta i dati del soggetto all'interno dei campi hidden.
     *
     * @param e (Event) l'evento scatenante la funzione
     */
    function impostaSoggetto(e) {
        e.preventDefault();
        var checkedSoggetto = $("#risultatiRicercaSoggetti").find("[name='checkSoggetto']:checked");
        var soggetto;
        var codiceSoggetto;
        var codiceFiscaleSoggetto;
        var denominazioneSoggetto;
        if(checkedSoggetto.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare un soggetto"], alertErrori, false);
            return;
        }

        soggetto = checkedSoggetto.data("originalSoggetto");

        codiceSoggetto = soggetto.codiceSoggetto;
        codiceFiscaleSoggetto = soggetto.codiceFiscale;
        denominazioneSoggetto = soggetto.denominazione;

        campoInCuiApplicareIlCodice.val(codiceSoggetto).attr("readonly", "readonly");
        campoInCuiApplicareIlCodiceFiscale.val(codiceFiscaleSoggetto);
        campoInCuiApplicareLaDenominazione.val(denominazioneSoggetto);
        campoInCuiApplicareUid.val(soggetto.uid);
        campoInCuiApplicareLaDescrizione.html(": " + codiceSoggetto + " - " + denominazioneSoggetto + (codiceFiscaleSoggetto ? " - " + codiceFiscaleSoggetto : ""));

        $("#modaleGuidaSoggetto").modal("hide");
        campoInCuiApplicareIlCodice.trigger("codiceSoggettoCaricato");
    }

    /**
     * Calcola il codice fiscale.
     *
     * @param elt (Element) l'elemento in cui è apposto il codice fiscale
     */
    function calcoloCodiceFiscale(elt) {
        var codiceFiscale = $(elt).val().toUpperCase();
        var codiceFiscaleValido = true;
        if(codiceFiscale.length === 16) {
            codiceFiscaleValido = checkCodiceFiscale(codiceFiscale);
        }

        $(elt).val(codiceFiscale);

        if(codiceFiscaleValido) {
            alertErrori.slideUp();
        } else {
            impostaDatiNegliAlert(["Il codice fiscale inserito non è sintatticamente corretto"], alertErrori, false);
        }
    }


    /**
     * Apre il modale del soggetto
     *
     * @param e (Event) l'evento invocante la funzione
     */
    function apriModaleSoggetto(e) {
        e.preventDefault();
        alertErrori.slideUp();
        
        
        // Apro il modale
        $("#modaleGuidaSoggetto").modal("show");
    }

    /**
     * Imposta la descrizione del soggetto.
     *
     * @param soggetto (Object) il soggetto da impostare
     * @param element  (jQuery) l'elemetno in cui impostare la descrizione
     */
    function impostaDescrizioneSoggetto(soggetto, element) {
        var denominazione;
        // Se non ho il soggetto, esco
        if(!soggetto) {
            return;
        }
        denominazione = ": " + soggetto.codiceSoggetto + " - " + ( soggetto.denominazione != null ? soggetto.denominazione : "" ) + " - " + ( soggetto.codiceFiscale != null ? soggetto.codiceFiscale : "" );
        // Imposto i dati nello span
        element.html(denominazione);
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoCodiceSoggetto (String) il campo ove impostare il codice del soggetto (Optional - default: #codiceSoggetto)
     * @param campoCodiceFiscale  (String) il campo ove impostare il codice fiscale (Optional - default: #HIDDEN_soggettoCodiceFiscale)
     * @param campoDenominazione  (String) il campo ove impostare la denominazione (Optional - default: #HIDDEN_soggettoDenominazione)
     * @param campoDescrizione    (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #descrizioneCompletaSoggetto)
     * @param pulsanteApertura    (String) il pulsante di apertura modale (Optional - default: #pulsanteApriModaleSoggetto)
     * @param campoAmbito         (String) il campo da cui reperire l'ambito (Optional - default: '')
     * @param campoUid            (String) il campo da cui reperire uid (Optional - default: '')
     */
    exports.inizializza = function(campoCodiceSoggetto, campoCodiceFiscale, campoDenominazione, campoDescrizione, pulsanteApertura, campoAmbito, campoUid) {
        campoCodiceSoggetto = campoCodiceSoggetto || "#codiceSoggetto";
        campoCodiceFiscale = campoCodiceFiscale || "#HIDDEN_soggettoCodiceFiscale";
        campoDenominazione = campoDenominazione || "#HIDDEN_soggettoDenominazione";
        campoDescrizione = campoDescrizione || "#descrizioneCompletaSoggetto";
        campoUid = campoUid || "#HIDDEN_soggettoUid";
        pulsanteApertura = pulsanteApertura || "#pulsanteApriModaleSoggetto";

        campoInCuiApplicareIlCodice = $(campoCodiceSoggetto);
        campoInCuiApplicareIlCodiceFiscale = $(campoCodiceFiscale);
        campoInCuiApplicareLaDenominazione = $(campoDenominazione);
        campoInCuiApplicareLaDescrizione = $(campoDescrizione);
        campoInCuiApplicareUid = $(campoUid);
        campoPulsanteApertura = $(pulsanteApertura);

        $("#pulsanteCercaSoggetto").substituteHandler("click", cercaSoggetti);
        $("#pulsanteConfermaSoggetto").substituteHandler("click", impostaSoggetto);

        // L'ambito. E' facoltatito, forse
        ambito = $(campoAmbito).val() || "";

        // L'uid è facoltativo
        uidSoggetto = $(campoUid).val() || "";

        campoPulsanteApertura.substituteHandler("click", apriModaleSoggetto);

        // Check di validità formale del codice fiscale
        $("#codiceFiscaleSoggetto_modale").on("keypress", function() {
            var self = this;
            setTimeout(function() {
                calcoloCodiceFiscale(self);
            }, 1);
        });
    };
    
    exports.destroy = function() {
        $('#codiceFiscaleSoggetto_modale').off("keypress");
        $('#pulsanteCercaSoggetto').off('click');
        $('#pulsanteConfermaSoggetto').off('click');
        campoPulsanteApertura.off('click');
        
        campoInCuiApplicareIlCodice = undefined;
        campoInCuiApplicareIlCodiceFiscale = undefined;
        campoInCuiApplicareLaDenominazione = undefined;
        campoInCuiApplicareLaDescrizione = undefined;
        campoInCuiApplicareUid = undefined;
        campoPulsanteApertura = undefined;
    };

    /**
     * Carica il dettaglio del soggetto e appone i dati nella pagina.
     *
     * @param campoCodiceSoggetto         (jQuery)  il campo ove &eacute; presente il codice del soggetto
     * @param accordionSedeSecondaria     (jQuery)  l'accordion delle sedi secondarie
     * @param accordionModalitaPagamento  (jQuery)  l'accordion delle modalita di pagamento
     * @param elementoDescrizioneSoggetto (jQuery)  l'elemento ove impostare la descrizione
     * @param doNotCloseModals            (boolean) se i modali non debbano essere chiusi (optional - default: false)
     * @param campoUidSoggetto            (jQuery)  il campo ove &eacute; presente l'uid (optional - default: '')
     * @param alert                       (jQuery)  l'alert (optional - default: '')
     * @param delegateAlert               (Boolean) se delegare la gestione degli errori
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione del servizio
     */
    exports.caricaDettaglioSoggetto = function(campoCodiceSoggetto, accordionSedeSecondaria, accordionModalitaPagamento,
            elementoDescrizioneSoggetto, /* Optional */ doNotCloseModals, /* Optional */campoUidSoggetto, /* Optional */alert, /* Optional */delegateAlert) {

        var alertErroriSoggetto;
        // Inizializzo l'Overlay
        campoCodiceSoggetto.overlay("show");
        if(!campoCodiceSoggetto.val()) {
            // Nascondere i div degli accordion
            accordionSedeSecondaria.slideUp();
            accordionModalitaPagamento.slideUp();
            // Svuotare la descrizione
            elementoDescrizioneSoggetto.html("");
            campoCodiceSoggetto.overlay("hide");
            campoUidSoggetto && campoUidSoggetto.val && typeof campoUidSoggetto.val === 'function' && campoUidSoggetto.val('');
            return $.Deferred().resolve('Nessun soggetto selezionato').promise();
        }
        alertErroriSoggetto = alert || $("#ERRORI");
        // Inizializzo gli altri Overlay
        accordionSedeSecondaria.slideDown()
            .overlay("show");
        accordionModalitaPagamento.slideDown()
            .overlay("show");
        //setto semaforo soggetto a true (=chiamata in corso)
        window.semaphore.soggetto=true;
        return $.postJSON("ricercaSoggettoPerChiave.do", {"codiceSoggetto": campoCodiceSoggetto.val()})
        .then(function(data) {
            var tabellaSedeSecondaria;
            var tabellaModalitaPagamento;
            if((delegateAlert && data.errori && data.errori.length) || impostaDatiNegliAlert(data.errori, alertErroriSoggetto, true, !doNotCloseModals)) {
                // Se ho errori, esco
                // Svuoto la descrizione del soggetto
                elementoDescrizioneSoggetto.html("");
                campoUidSoggetto && campoUidSoggetto.val && typeof campoUidSoggetto.val === 'function' && campoUidSoggetto.val('');                
                // Chiudo gli accordion
                accordionSedeSecondaria.slideUp();
                accordionModalitaPagamento.slideUp();
                return $.Deferred().reject(data.errori).promise();
            }
            // Carico il soggetto
            impostaDescrizioneSoggetto(data.soggetto, elementoDescrizioneSoggetto);
            if (campoUidSoggetto) {
                campoUidSoggetto.val(data.soggetto.uid);
            }
            // Carico gli accordion
            if(accordionSedeSecondaria.length || accordionModalitaPagamento.length) {
                tabellaSedeSecondaria = accordionSedeSecondaria.find("table");
                tabellaModalitaPagamento = accordionModalitaPagamento.find("table");
                impostaTabellaSedeSecondariaSoggetto(data.listaSedeSecondariaSoggettoValide, tabellaSedeSecondaria, tabellaModalitaPagamento);
                impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggettoValide, tabellaModalitaPagamento);
            }
        }).always(function() {
            campoCodiceSoggetto.overlay("hide");
            accordionSedeSecondaria.overlay("hide");
            accordionModalitaPagamento.overlay("hide");
            //setto semaforo soggetto a false
            window.semaphore.soggetto=false;
        });
    };

    /**
     * Filtra la modalita di pagamento.
     *
     * @param uid       (Number) l'uid per cui filtrare
     * @param tabella   (jQuery) la tabella da popolare
     * @param accordion (jQuery) l'accordion su cui fare overlay (Optional, default: undefined)
     *
     * @returns (Deferred) l'oggetto deferred corrispondente all'invocazione del servizio
     */
    exports.filterModalitaPagamentoSoggetto = function(uid, tabella, accordion) {
        accordion && accordion.length && accordion.overlay("show");
        return $.postJSON("filtraModalitaPagamentoRispettoSedeSecondaria.do", {uidSedeSecondariaSoggetto: uid}, function(data) {
            impostaTabellaModalitaPagamentoSoggetto(data.listaModalitaPagamentoSoggettoValide, tabella);
        }).always(function() {
            accordion && accordion.length && accordion.overlay("hide");
        });
    };

    /**
     * Carica la vecchia sede secondaria.
     *
     * @param accordion (jQuery) l'accordion
     * @param uid       (Number) l'uid per cui filtrare
     */
    exports.caricaOldSedeSecondaria = function(accordion, uid) {
        if(!uid) {
            return;
        }
        accordion.find("table")
            .dataTable()
                .$("input[type='radio'][name='sedeSecondariaSoggetto.uid'][value='" + uid + "']")
                    .prop("checked", true)
                    .trigger("change");
    };

    /**
     * Carica la vecchia modalita di pagamento.
     *
     * @param accordion (jQuery) l'accordion
     * @param uid       (Number) l'uid per cui filtrare
     */
    exports.caricaOldModalitaPagamentoSoggetto = function(accordion, uid) {
        if(!uid) {
            return;
        }
        accordion.find("table")
            .dataTable()
                .$("input[type='radio'][name='modalitaPagamentoSoggetto.uid'][value='" + uid + "']")
                    .prop("checked", true);
    };

    return exports;
}());

$(
    function() {
        // Imposto le funzionalita' dei pulsanti
        Soggetto.inizializza();

        //SIAC-6780
        var ricercaPerCollegaDocumento = $('HIDDEN_collegaDocumento').val();
        if( ricercaPerCollegaDocumento && ricercaPerCollegaDocumento === 'ricercaPerCollegaDocumento'){
            Soggetto.inizializza('#codiceSoggettoDocumentoEntrata','#HIDDEN_soggettoCodiceFiscaleDocumentoEntrata',
                '#HIDDEN_documentoDenominazioneDocumentoEntrata','#descrizioneCompletaSoggettoDocumentoEntrata',
                '#HIDDEN_uidSoggettoDocumentoEntrata','#pulsanteApriModaleSoggettoDocumentoEntrata');
        }
        //
        
        $("form").on("reset", function() {
            $("#modaleGuidaSoggetto").find(":input")
                    .val("")
                    .end()
                .find("#divTabellaSoggetti")
                    .slideUp()
                    .end()
                .find("#ERRORI_MODALE_SOGGETTO")
                    .slideUp();
        });
    }
);