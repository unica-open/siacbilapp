/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function($, global) {
    "use strict";

    var exports = {};
    var alertErrori = $("#ERRORI_modaleRicercaMatricola");
    var Matricola;

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
     * Caricamento della tabella delle matricole da visualizzare.
     *
     * @param lista (Array)  la lista delle matricole da utilizzare per la creazione della tabella
     * @param table (jQuery) la tabella da popolare
     */
    function tabellaMatricole(lista, table) {
        var options = {
            bSort : true,
            aaData : lista,
            oLanguage : {
                sZeroRecords : "Non sono presenti risultati di ricerca secondo i parametri inseriti"
            },
            aoColumnDefs : [
                {aTargets : [ 0 ], mData : function() {
                    return "<input type='radio' name='checkMatricola'/>";
                }, bSortable: false, fnCreatedCell: function(nTd, sData, oData) {
                    $("input", nTd).data("originalMatricola", oData);
                }},
                {aTargets : [1], mData : function(source) {
                    return source.matricola || "";
                }},
                {aTargets : [2], mData : function(source) {
                    return source.denominazione || "";
                }},
                {aTargets : [3], mData : function(source) {
                    return source.statoOperativo || "";
                }, bSortable: false}
            ]
        };
        var opts = $.extend(true, {}, baseOpts, options);
        table.dataTable(opts);
    }

    /**
     * Calcola la descrizione della matricola fornita in input.
     *
     * @param m (Object) la matricola la cui descrizione deve essere calcolata
     * @returns la descrizione della matricola
     */
    function calcolaDescrizioneMatricola(m,maySearchHR) {
        var res = "";
        if(m) {
            if(!maySearchHR && (m.cognome || m.nome)) {
                res = ": " + (m.cognome != null ? m.cognome : "") + " " + (m.nome != null ? m.nome : "");
            } else if(m.denominazione) {
                res = ": " + m.denominazione;
            }
        }
        return res;
    }
    
    /**
     * Calcola la descrizione della matricola fornita in input.
     *
     * @param m (Object) la matricola la cui descrizione deve essere calcolata
     * @returns la descrizione della matricola
     */
    function calcolaUnitaOrganizzativaMatricola(m,maySearchHR,initialVal) {
        var res = initialVal;
        if(m && !maySearchHR && m.note) {
            res =  m.note != null ? m.note : "" ;
        }
        return res;
    }

    /**
     * Costruttore per la matricola.
     *
     * @param matricolaSelector   (String) il selettore CSS della matricola della matricola (deve rappresentare un campo di tipo input)
     * @param descrizioneSelector (String) il selettore CSS della descrizione della matricola (deve rappresentare un campo che ammette discendenti)
     * @param maySearchHR         (String) indica se puo' essere o meno usata la rcerca su HR
     * @param codiceAmbito        (String) il codice dell'ambito
     */
    Matricola = function(matricolaSelector, descrizioneSelector, codiceAmbito, maySearchHR) {
        // Il codice ambito
        this.codiceAmbito = codiceAmbito;
        
        // Campi della pagina
        this.$matricolaMatricola = $(matricolaSelector);
        this.$descrizioneMatricola = $(descrizioneSelector);
        this.$strutturaDiAppartenenzaRichiestaEconomale = $("#strutturaDiAppartenenzaRichiestaEconomale");
        if ( $("#HIDDEN_strutturaDiAppartenenzaRichiestaEconomale").length) {
        	this.$strutturaDiAppartenenzaRichiestaEconomale.val($("#HIDDEN_strutturaDiAppartenenzaRichiestaEconomale").val());
        } 
      //se deve puo' provare a cercare su HR(dipende poi dall'ente) 
        this.maySearchHR = maySearchHR === 'true';

        // I campi del modale
        this.$matricolaMatricolaModale = $("#matricolaSoggettoModaleRicercaMatricola");
        this.$denominazioneMatricolaModale = $("#denominazioneSoggettoModaleRicercaMatricola");
        this.$fieldsetRicerca = $("#fieldsetModaleRicercaMatricola");
        this.$divTabella = $("#risultatiRicercaModaleRicercaMatricola");
        this.$spinner = $("#spinnerModaleRicercaMatricola");
        this.$table = $("#tabellaModaleRicercaMatricola");

        this.$modal = $("#modaleRicercaMatricola");

        this.init();
    };

    /** Costruttore */
    Matricola.prototype.constructor = Matricola;

    /**
     * Inizializzazione interna delle funzionalita'.
     */
    Matricola.prototype.init = function() {
        $("#bottoneCercaModaleRicercaMatricola").click($.proxy(Matricola.prototype.cercaMatricola, this));
        $("#pulsanteConfermaModaleRicercaMatricola").click($.proxy(Matricola.prototype.impostaMatricola, this));
    };

    /**
     * Richiama l'esecuzione della ricerca matricola.
     *
     * @returns (Matricola) l'oggetto su cui e' atata effettuata l'invocazione
     */
    Matricola.prototype.cercaMatricola = function() {
        var self = this;
        var oggettoPerChiamataAjax = unqualify(self.$fieldsetRicerca.serializeObject(), 1);

        self.$divTabella.slideUp();
        self.$spinner.addClass("activated");
        // Chiudo l'alert
        alertErrori.slideUp();

        oggettoPerChiamataAjax.codiceAmbito = self.codiceAmbito;
        oggettoPerChiamataAjax.maySearchHR = self.maySearchHR;

        $("#tabellaModaleRicercaMatricola").dataTable().fnDestroy();

        $.postJSON("ricercaSinteticaSoggetto.do", oggettoPerChiamataAjax, function(data) {
            // Se ho errori, esco
            if(impostaDatiNegliAlert(data.errori, alertErrori, false)) {
                return;
            }

            // Carico i dati in tabella
            tabellaMatricole(data.listaSoggetti, self.$table);
            self.$divTabella.slideDown();
        }).always(function() {
            // Disattivo lo spinner
            self.$spinner.removeClass("activated");
        });

        return this;
    };

    /**
     * Imposta i dati della matricola all'interno dei campi selezionati.
     *
     * @returns (Matricola) l'oggetto su cui e' atata effettuata l'invocazione
     */
    Matricola.prototype.impostaMatricola = function() {
        var checkedMatricola = this.$table.find("[name='checkMatricola']:checked");

        // I campi da popolare
        var matricola;
        var matricolaMatricola;
        var descrizione;

        // Se non ho selezionato nulla, esco subito
        if(checkedMatricola.length === 0) {
            impostaDatiNegliAlert(["Necessario selezionare una matricola"], alertErrori, false);
            return;
        }

        // Ottengo i dati del soggetto salvati
        matricola = checkedMatricola.data("originalMatricola");

        // Imposto i valori dei campi
        matricolaMatricola = matricola.matricola;

        descrizione = calcolaDescrizioneMatricola(matricola, this.maySearchHR);
        this.$descrizioneMatricola.html(descrizione);
        this.$strutturaDiAppartenenzaRichiestaEconomale.val(calcolaUnitaOrganizzativaMatricola(matricola, this.maySearchHR,  this.$strutturaDiAppartenenzaRichiestaEconomale.val()));
        // Copio i campi ove adeguato e lancio le azioni
        this.$matricolaMatricola.val(matricolaMatricola).change();

        // Chiudo il modale e distruggo il datatable
        this.$modal.modal("hide");
    };

    /**
     * Apertura del modale
     *
     * @returns (Matricola) l'oggetto su cui e' stata effettuata l'invocazione
     */
    Matricola.prototype.apriModale = function() {
        // Copio i valori
        var matricolaSullaPagina = this.$matricolaMatricola.val();
        this.$matricolaMatricolaModale.val(matricolaSullaPagina);
        this.$denominazioneMatricolaModale.val("");

        // Nascondo i campi
        alertErrori.slideUp();
        this.$divTabella.slideUp();
        this.$modal.modal("show");
    };

    /**
     * Carica il dettaglio del soggetto e appone i dati nella pagina.
     *
     * @returns (Matricola) l'oggetto su cui e' atata effettuata l'invocazione
     */
    Matricola.prototype.caricaDettaglio = function() {
        var self = this;
        var value = self.$matricolaMatricola.val();
        var codiceAmbito = self.codiceAmbito;
        var maySearchHR = self.maySearchHR;
        // Se non ho il dato, esco
        if(!value) {
            self.$descrizioneMatricola.html("");
            return;
        }
        // Inizializzo l'overlay
        self.$matricolaMatricola.overlay("show");
        // Chiamata alla action
        $.postJSON("ricercaSoggettoPerMatricola.do", {"soggetto.matricola": value, "codiceAmbito": codiceAmbito, "maySearchHR": maySearchHR}, function(data) {
            var descrizione;
            if(impostaDatiNegliAlert(data.errori, alertErrori, true, false)) {
                self.$descrizioneMatricola.html("");
                return;
            }
            descrizione = calcolaDescrizioneMatricola(data.soggetto,self.maySearchHR);
            self.$descrizioneMatricola.html(descrizione);
            self.$strutturaDiAppartenenzaRichiestaEconomale.val(calcolaUnitaOrganizzativaMatricola(data.soggetto, this.maySearchHR,self.$strutturaDiAppartenenzaRichiestaEconomale.val()));
        }).always(function() {
            self.$matricolaMatricola.overlay("hide");
        });

        return this;
    };

    /**
     * Inizializzazione della gestione della matricola.
     */

    /**
     * Inizializza la gestione.
     *
     * @param selectorMatricola   (String)  il selettore del campo ove impostare la matricola del soggetto
     * @param selectorDescrizione (String)  il selettore del campo ove impostare la descrizione completa del soggetto
     * @param selectorPulsante    (String)  il selettore del pulsante di apertura modale
     * @param selectorAmbito      (String)  il selettore del campo da cui reperire l'ambito
     * @param selectorMaySearchHR (String)  il selettore della ricerca da HR (Optional - default: '#HIDDEN_maySearchHR')
     * @param searchByChange      (Boolean) se sia necessario ricercare il soggetto a seguito del cambiamento del codice (Optional - default: true)
     */
    exports.inizializza = function(selectorMatricola, selectorDescrizione, selectorPulsante, selectorAmbito, selectorMaySearchHR, /* Optional */ searchByChange) {
        var ambito = $(selectorAmbito).val() || "";
        var maySearchHR =  $(selectorMaySearchHR || '#HIDDEN_maySearchHR').val() || "false";
        var matricola = new Matricola(selectorMatricola, selectorDescrizione, ambito, maySearchHR);

        $(selectorPulsante).click($.proxy(Matricola.prototype.apriModale, matricola));
        if(searchByChange === undefined || searchByChange) {
            $(selectorMatricola).on("change codiceMatricolaCaricato", $.proxy(Matricola.prototype.caricaDettaglio, matricola))
                .change();
        }
    };

    // Esportazione delle funzionalita'
    global.Matricola = exports;
}(jQuery, this);