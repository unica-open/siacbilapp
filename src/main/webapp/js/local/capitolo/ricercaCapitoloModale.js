/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Definisco il namespace in cui creare la funzione
!function ($, global) {
    "use strict";

    var exports = {},
        errorMessageNotSelected = "Necessario selezionare un capitolo",
        map = {},
        counter = 0,
        Searcher;

    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return val;
    }
    
    function tabRight(nTd) {
        $(nTd).addClass("text-right");
    }
    /**
     * Definition for the table and visualization.
     *
     * @param list  (Array)  the list to use to populate the table
     * @param table (jQuery) the table to populate
     */
    function visualizeTable(list, table,tipoCapitolo) {
    	var url="risultatiRicercaCapitolo" + tipoCapitolo + "ModaleAjax.do";
        var options = {
            bServerSide : true,
            sAjaxSource : url,
            sServerMethod : "POST",
            bPaginate : true,
            bLengthChange : false,
            iDisplayLength : 5,
            bSort : false,
            bInfo : true,
            bAutoWidth : true,
            bFilter : false,
            bProcessing : true,
            bDestroy : true,
            aaData : list,
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
            },
            fnDrawCallback: function () {
                var records = this.fnSettings().fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + " Risultati trovati") : ("1 Risultato trovato");
                $('#id_num_result').html(testo);
                table.find("a[rel='popover']").popover();
            },
            aoColumnDefs : [
                {aTargets : [ 0 ], mData : function () {
                    return "<input type='radio' name='checkCapitolo'/>";
                }, fnCreatedCell : function (nTd, sData, oData) {
                    $(nTd).find("input").data("sourceCapitolo", oData);
                }},
                {aTargets : [ 1 ], mData : function (source) {
                    return "<a data-original-title='Descrizione' data-trigger='hover' rel='popover' data-content='" + escapeHtml(source.descrizione) + "'>" + source.capitolo + "</a>";
                }},
                {aTargets : [ 2 ], mData : "classificazione"},
                {aTargets : [ 3 ], mData : defaultPerDataTable('disponibileAnno0', 0, formatMoney), fnCreatedCell : tabRight},
                {aTargets : [ 4 ], mData : defaultPerDataTable('disponibileAnno1', 0, formatMoney), fnCreatedCell : tabRight},
                {aTargets : [ 5 ], mData : defaultPerDataTable('disponibileAnno2', 0, formatMoney), fnCreatedCell : tabRight},
                {aTargets : [ 6 ], mData : function (source) {
                    var str = "",
                        n;
                    if (source.struttAmmResp) {
                        str = source.struttAmmResp;
                        n = str.indexOf("-");
                        str = str.substring(0, n);
                    }
                    return "<a data-original-title='Descrizione' data-trigger='hover' rel='popover' data-content='" + escapeHtml(source.struttAmmResp) + "'>" + str + "</a>";
                }},
                {aTargets : [ 7 ], mData: function (source) {
                    return "<a data-original-title='Voce' data-trigger='hover' rel='popover' data-content='" + escapeHtml(source.pdcVoce) + "'>" + source.pdcFinanziario + "</a>";
                }},
                {aTargets : [ 8 ], mData: defaultPerDataTable('categoriaCapitolo.codice')}
            ]
        };
        if($.fn.DataTable.fnIsDataTable(table[0])) {
            table.dataTable().fnDestroy();
        }
        table.dataTable(options);
    }

    /**
     * Constructor for searching of the Capitolo.
     *
     * @param type          (String) the type of the Capitolo
     * @param chapterYear   (String) the CSS selector for the chapter year
     * @param chapterNumber (String) the CSS selector for the chapter number
     * @param articleNumber (String) the CSS selector for the article number
     * @param uebNumber     (String) the CSS selector for the ueb number
     * @param description   (String) the CSS selector for the description
     * @param opening       (String) the CSS selector for the opening element
     * @param suffix        (String) the suffix to use
     */
    Searcher = function (type, chapterYear, chapterNumber, articleNumber, uebNumber, description, opening, suffix) {
        this.type = type;
        this.suffix = suffix;

        // Fields
        this.$description = $(description);
        this.$chapterYear = $(chapterYear);
        this.$chapterNumber = $(chapterNumber);
        this.$articleNumber = $(articleNumber);
        this.$uebNumber = $(uebNumber);
        this.$opening = $(opening);

        // Derived fields
        // Fieldsets
        this.$fieldset = $("#fieldsetRicercaGuidataCapitolo" + this.suffix);
        // Divs
        this.$tableDiv = $("#divTabellaCapitoli" + this.suffix);
        this.$detailDiv = $("#divVisualizzaDettaglio" + this.suffix);
        // Spinners
        this.$spinner = $("#SPINNER_RicercaCapitolo" + this.suffix);
        // Alerts
        this.$errorAlert = $("#ERRORI_MODALE_CAPITOLO" + this.suffix);
        // Collapses
        this.$collapse = $("#collapseDettaglioCapitolo" + this.suffix);
        // Tables
        this.$table = $("#risultatiRicercaCapitolo" + this.suffix);
        // Buttons
        this.$buttonSearch = $("#pulsanteRicercaCapitolo" + this.suffix);
        this.$buttonDetail = $("#pulsanteVisualizzaDettaglio" + this.suffix);
        this.$buttonConfirm = $("#pulsanteConfermaCapitolo" + this.suffix);
        // Modal
        this.$modal = $("#modaleGuidaCapitolo" + this.suffix);
        // Titles
        this.$infos = $("#informazioniCapitoloModale" + this.suffix);
        // Fields
        this.$fieldChapterYear = $("#annoCapitolo_modale" + this.suffix);
        this.$fieldChapterYearHidden = $("#annoCapitolo_modaleHidden" + this.suffix);
        this.$fieldChapterNumber = $("#numeroCapitolo_modale" + this.suffix);
        this.$fieldArticleNumber = $("#numeroArticolo_modale" + this.suffix);
        this.$fieldUebNumber = $("#numeroUEB_modale" + this.suffix);
        // Document
        this.$document = $(document);
        // Others
        this.shouldPreventDefault = this.$opening.is('a') || this.$opening.is('button');
    };

    /** Constructor */
    Searcher.prototype.constructor = Searcher;

    /**
     * Calls for the execution of the Search.
     */
    Searcher.prototype.search = function () {
        var self = this,
            obj = unqualify(self.$fieldset.serializeObject(), 1),
            url = "selezionaCapitolo" + self.type + ".do";

        // Closing the errors alert
        self.$errorAlert.slideUp();
        // Hiding the table div
        self.$tableDiv.slideUp();
        // Activation of the spinner
        self.$spinner.addClass("activated");

        // Starting the AJAX call
        $.postJSON(url, obj, function (data) {
            if (impostaDatiNegliAlert(data.errori, self.$errorAlert)) {
                return;
            }

            // Puts the data in the table
            visualizeTable(data.listaCapitoli, self.$table,self.type);
            // Hides the detail collapse
            self.$collapse.removeClass("in");
            // Shows the divs
            self.$tableDiv.slideDown();
            self.$detailDiv.slideDown();
            // Binds the events
            self.$buttonDetail.substituteHandler("click", $.proxy(self.showDetail, self));
            self.$buttonConfirm.substituteHandler("click", $.proxy(self.confirm, self));
        }).always(function () {
            self.$spinner.removeClass("activated");
        });
    };

    /**
     * Confirms the selected choice.
     */
    Searcher.prototype.confirm = function () {
        var checkedRadio = this.$table.find("input[name='checkCapitolo']").filter(":checked"),
            data;

        // If no radio was selected, exits
        if (!checkedRadio.length) {
            impostaDatiNegliAlert([errorMessageNotSelected], this.$errorAlert);
            return;
        }

        // Gets the data from the radio
        data = checkedRadio.data("sourceCapitolo");
        if (!data) {
            return;
        }

        // Sets the data in the fields
        this.$description.html(data.annoCapitolo + "/" + data.capitolo);
        this.$chapterYear.val(data.annoCapitolo).attr("readonly", true);
        this.$chapterNumber.val(data.numeroCapitolo).attr("readonly", true);
        this.$articleNumber.val(data.numeroArticolo).attr("readonly", true);
        this.$uebNumber.val(data.numeroUEB).attr("readonly", true);

        // Triggers an event on the document
        this.$document.trigger('loadedCapitolo' + this.suffix, {capitolo: data});

        // Closes the modal
        this.$modal.modal("hide");
    };

    /**
     * Shows the selected item's detail.
     */
    Searcher.prototype.showDetail = function () {
        var checkedRadio = this.$table.find("input[name='checkCapitolo']").filter(":checked"),
            data,
        // Only for the callback of $.fn.each
            self = this;

        // If no radio was selected, exit
        if (!checkedRadio.length) {
            return;
        }
        data = checkedRadio.data("sourceCapitolo");
        // If no data was found, exit
        if (!data) {
            return;
        }

        // Title
        this.$infos.html("Capitolo " + data.capitolo);
        // Setting the data
        $.each(["Competenza", "Residuo", "Cassa"], function () {
            var i = this,
                j,
                k;
            for (j = 0; j < 3; j++) {
                k = j || "";
                $("#modaleElementoSelezionato" + i + "Anno" + j + self.suffix).html(data["stanziamento" + i + k].formatMoney());
            }
        });
        // Opens the collapse (NOT by calling $.fn.collapse, since it shows some problems)
        this.$collapse.addClass("in");
    };

    /**
     * Opening of the modal.
     *
     * @param e (Event) the event
     */
    Searcher.prototype.open = function (e) {
        if(this.shouldPreventDefault) {
            // Blocks the default action of the event
            e.preventDefault();
            e.stopPropagation();
        }
        // Copy the data into the modal
        this.$fieldChapterYear.val(this.$chapterYear.val());
        this.$fieldChapterYearHidden.val(this.$chapterYear.val());
        this.$fieldChapterNumber.val(this.$chapterNumber.val());
        this.$fieldArticleNumber.val(this.$articleNumber.val());
        this.$fieldUebNumber.val(this.$uebNumber.val());
        this.$tableDiv.slideUp();
        this.$detailDiv.slideUp();

        this.$document.trigger('openedModalCapitolo' + this.suffix);
        // Open the modal
        this.$modal.modal("show");
    };

    /**
     * Inizializza la gestione.
     *
     * @param tipoCapitolo         (String) il tipo di capitolo (Uscita \ Entrata, Previsione \ Gestione)
     * @param campoAnnoCapitolo    (String) il campo ove impostare l'anno del capitolo (Optional - default: #annoCapitolo)
     * @param campoNumeroCapitolo  (String) il campo ove impostare il numero del capitolo (Optional - default: #numeroCapitolo)
     * @param campoNumeroArticolo  (String) il campo ove impostare il numero dell'articolo (Optional - default: #numeroArticolo)
     * @param campoNumeroUEB       (String) il campo ove impostare il numero dell'UEB (Optional - default: #numeroUEB)
     * @param campoDescrizione     (String) il campo ove impostare la descrizione completa del capitolo (Optional - default: #datiRiferimentoCapitoloSpan)
     * @param aperturaCompilazione (String) il pulsante di apertura compilazione (Optional - default: "");
     * @param suffisso             (String) il suffisso per i campi (Optional - default: "")
     * 
     * @returns (number) il contatore da utilizzare per revocare la ricerca
     */
    exports.inizializza = function (tipoCapitolo, campoAnnoCapitolo, campoNumeroCapitolo, campoNumeroArticolo, campoNumeroUEB, campoDescrizione, aperturaCompilazione, suffisso) {
        var ricerca = new Searcher(tipoCapitolo, campoAnnoCapitolo || "#annoCapitolo", campoNumeroCapitolo || "#numeroCapitolo",
            campoNumeroArticolo || "#numeroArticolo", campoNumeroUEB || "#numeroUEB", campoDescrizione || "#datiRiferimentoCapitoloSpan",
            aperturaCompilazione || "", suffisso || "");
        var id = counter++;

        // Lego le azioni ai pulsanti
        ricerca.$buttonSearch.substituteHandler("click", $.proxy(ricerca.search, ricerca));
        ricerca.$opening.substituteHandler("click", $.proxy(ricerca.open, ricerca));
        
        map[id] = ricerca;
        
        return id;
    };

    /**
     * Distruzione del gestore
     * @param id (number) l'identificativo del gestore
     */
    exports.destroy = function(id) {
        var instance = map[id];
        if(!instance) {
            return;
        }
        instance.$buttonSearch.off("click");
        instance.$opening.off("click");
        map[id] = undefined;
        instance = undefined;
    };

    global.Capitolo = exports;
}(jQuery, this);