/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    var AttivitaIvaCapitolo = (function() {

        "use strict";

        var exports = {};

        var alertErrori = $("#ERRORI");
        var alertInformazioni = $("#INFORMAZIONI");
        var alertErroriModale = $("#ERRORI_MODALE");
        var zero = 0;
        var baseOpts = {
            iDisplayLength : 5,
            bPaginate : true,
            bLengthChange : false,
            bSort : false,
            bInfo : true,
            bAutoWidth : true,
            bFilter : false,
            bProcessing : true,
            bServerSide : false,
            bDestroy : true,
            oLanguage : {
                sInfo : "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty : "0 risultati",
                sProcessing : "Attendere prego...",
                sZeroRecords : "Non sono presenti risultati relativi ai parametri inseriti",
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
         * Chiude gli eventuali alerts aperti.
         */
        function chiudiAlerts() {
            $("#ERRORI, #INFORMAZIONI, #ERRORI_MODALE").slideUp();
        }

        /**
         * Effettua un toggle delle classi dello spinner, cambiando tra la lente
         * di ricerca e lo spinner vero e proprio.
         *
         * @param spinner (jQuery) l'oggetto spinner
         *
         * @returns (jQuery) lo spinner
         */
        function toggleSpinner(spinner) {
            return spinner.toggleClass("icon-search icon-spin icon-refresh spinner active");
        }

        /**
         * Popola la tabella delle UEB a partire dalla lista delle stesse.
         *
         * @param lista (Array) la lista delle UEB tramite cui popolare la tabella
         */
        function popolaTabellaUeb(lista) {
            var opts = {
                aaData : lista,
                fnDrawCallback : function() {
                    $('a', "#tabellaUEBCapitolo").eventPreventDefault("click");
                },
                aoColumnDefs : [
                    {
                        aTargets : [ 0 ],
                        mData : function(source) {
                            // Creo il radio
                            return "<input type='radio' name='capitolo.uid' value='" + source.uid + "'/>";
                        }
                    },
                    {
                        aTargets : [ 1 ],
                        mData : function(source) {
                            return "<a rel='popover' href='#'>" + source.capitolo + "</a>";
                        },
                        fnCreatedCell : function(nTd, sData, oData) {
                            var popoverContent = oData.descrizione;
                            var settings;
                            if(oData.descrizioneArticolo) {
                                popoverContent += " - " + oData.descrizioneArticolo;
                            }
                            // Settings del popover
                            settings = {
                                content : popoverContent,
                                title : "Descrizione",
                                trigger : "hover"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).popover(settings);
                        }
                    },
                    {aTargets : [ 2 ], mData : "stato"},
                    {aTargets : [ 3 ], mData : "classificazione"},
                    {
                        aTargets : [ 4 ],
                        mData : function(source) {
                            return source.stanziamentoCompetenza ? source.stanziamentoCompetenza.formatMoney() : zero.formatMoney();
                        }
                    },
                    {
                        aTargets : [ 5 ],
                        mData : function(source) {
                            return source.stanziamentoResiduo ? source.stanziamentoResiduo.formatMoney() : zero.formatMoney();
                        }
                    },
                    {
                        aTargets : [ 6 ],
                        mData : function(source) {
                            return source.stanziamentoCassa ? source.stanziamentoCassa.formatMoney() : zero.formatMoney();
                        }
                    },
                    {aTargets : [ 7 ], mData : "struttAmmResp"},
                    {
                        aTargets : [ 8 ],
                        mData : function(source) {
                            return "<a rel='popover' href='#'>" + source.pdcFinanziario + "</a>";
                        },
                        fnCreatedCell : function(nTd, sData, oData) {
                            // Settings del popover
                            var settings = {
                                content : oData.pdcVoce,
                                title : "Voce",
                                trigger : "hover",
                                placement : "left"
                            };
                            // Importante : attivare il popover sull'elemento anchor
                            $("a", nTd).popover(settings);
                        }
                    }
                ]
            };

            var options = $.extend(true, {}, baseOpts, opts);
            // Attivo il dataTable
            $("#tabellaUEBCapitolo").dataTable(options);
        }

        /**
         * Popola la tabella delle attivita collegate al capitolo a partire dalla lista delle stesse.
         *
         * @param lista (Array) la lista delle attivita tramite cui popolare la tabella
         */
        function popolaTabellaAttivitaCollegate(lista) {
            var opts = {
                aaData : lista,
                aoColumnDefs : [
                    {aTargets : [ 0 ], mData : "codice"},
                    {aTargets : [ 1 ], mData : "descrizione"},
                    {
                        aTargets : [ 2 ],
                        mData : function(source) {
                            return source.flagRilevanteIRAP ? "SI" : "NO";
                        }
                    },
                    {
                        aTargets : [ 3 ],
                        mData : function(source) {
                            return source.gruppoAttivitaIva ? (source.gruppoAttivitaIva.codice + " - " + source.gruppoAttivitaIva.descrizione) : "";
                        }
                    },
                    {
                        aTargets : [ 4 ],
                        mData : function() {
                            return "<a href='#'><i class='icon-trash icon-2x'></i></a>";
                        },
                        fnCreatedCell : function(nTd, sData, oData) {
                            $(nTd).addClass("tab_Right").find("a")
                                // Gestisco il click
                                .substituteHandler("click", function(e) {
                                    e.preventDefault();
                                    clickOnElimina(oData);
                                });
                        }
                    }
                ]
            };

            var options = $.extend(true, {}, baseOpts, opts);
            // Attivo il dataTable
            $("#tabellaAttivitaIvaAssociate").dataTable(options);
        }

        /**
         * Gestisce il click sul pulsante di eliminazione.
         *
         * @param attivita (Object) l'attivita da eliminare
         */
        function clickOnElimina(attivita) {
            $("#SPAN_elementoSelezionatoElimina").html(" " + attivita.codice);
            $("#BUTTON_siElimina").substituteHandler("click", function(e) {
                e.preventDefault();
                eliminaAssociazione(attivita.uid);
            });
            $("#modaleElimina").find(".alert").slideDown().end().modal("show");
        }

        /**
         * Elimina l'associazione tra capitolo e attivita.
         *
         * @param uidAttivita (Number) l'uid dell'attivita
         */
        function eliminaAssociazione(uidAttivita) {
            var uidCapitolo = $("input[name='capitolo.uid']").filter(":checked").val();
            var oggettoPerChiamataAjax = {};
            var spinner = $("#SPINNER_BUTTON_siElimina");

            // Chiudo gli alert
            chiudiAlerts();

            oggettoPerChiamataAjax['capitolo.uid'] = uidCapitolo;
            oggettoPerChiamataAjax['attivitaIva.uid'] = uidAttivita;

            spinner.addClass("activated");

            $.postJSON("associaAttivitaIvaCapitolo_eliminaAssociazioneAttivitaIvaAlCapitolo.do", oggettoPerChiamataAjax, function(data) {
                // Se ho degli errori esco subito
                if (impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                    return;
                }
                // Imposto il messaggio di successo
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                // Ripopolo la tabella delle attivitaAssociate
                popolaTabellaAttivitaCollegate(data.listaAttivitaIva);
                // Chiudo il modale
                $("#modaleElimina").modal("hide");
            }).always(function() {
                spinner.removeClass("activated");
            });
        }

        /**
         * Popola la tabella delle attivita a partire dalla lista delle stesse.
         *
         * @param lista (Array) la lista delle attivita tramite cui popolare la tabella
         */
        function popolaTabellaAttivita(lista) {
            var opts = {
                aaData : lista,
                aoColumnDefs : [
                    {
                        aTargets : [ 0 ],
                        mData : function(source) {
                            return "<input type='radio' name='attivitaIva.uid' value='" + source.uid + "'/>";
                        }
                    },
                    {aTargets : [ 1 ], mData : "codice"},
                    {aTargets : [ 2 ], mData : "descrizione"},
                    {
                        aTargets : [ 3 ],
                        mData : function(source) {
                            return source.flagRilevanteIRAP ? "SI" : "NO";
                        }
                    },
                    {
                        aTargets : [ 4 ],
                        mData : function(source) {
                            return source.gruppoAttivitaIva ? (source.gruppoAttivitaIva.codice + " - " + source.gruppoAttivitaIva.descrizione) : "";
                        }
                    }
                ]
            };

            var options = $.extend(true, {}, baseOpts, opts);
            // Attivo il dataTable
            $("#tabellaAttivitaIvaRicerca").dataTable(options);
        }

        /**
         * Effettua una ricerca del capitolo.
         *
         * @param e (Event) l'evento scatenante
         */
        exports.ricercaCapitolo = function(e) {
            var spinner = $("#pulsanteRicercaCapitolo").find("i");
            var tipoCapitolo = $("input[name='tipoCapitolo']:checked").val();
            var annoCapitolo = $("#annoCapitolo").val();
            var numeroCapitolo = $("#numeroCapitolo").val();
            var numeroArticolo = $("#numeroArticolo").val();
            var statoCapitolo = $("#statoOperativoElementoDiBilancioCapitolo").val();
            var errori = [];
            var oggettoPerChiamataAjax = {};
            var url = "effettuaRicercaComeDatoAggiuntivoCap" + tipoCapitolo + ".do";
            var collapse = $("#collapseCapitoloUEB");

            e.preventDefault();
            // Chiudo gli alert
            chiudiAlerts();

            if (!tipoCapitolo) {
                errori.push("Tipo capitolo obbligatorio");
            }
            if (!annoCapitolo) {
                errori.push("Anno obbligatorio");
            }
            if (!numeroCapitolo) {
                errori.push("Numero capitolo obbligatorio");
            }
            if (!numeroArticolo) {
                errori.push("Numero articolo obbligatorio");
            }
            // Se ho degli errori, li mostro ed esco
            if (impostaDatiNegliAlert(errori, alertErrori)) {
                return;
            }
            // Popolo l'oggetto per la chiamata AJAX
            oggettoPerChiamataAjax["capitolo" + tipoCapitolo + ".annoCapitolo"] = annoCapitolo;
            oggettoPerChiamataAjax["capitolo" + tipoCapitolo + ".numeroCapitolo"] = numeroCapitolo;
            oggettoPerChiamataAjax["capitolo" + tipoCapitolo + ".numeroArticolo"] = numeroArticolo;
            oggettoPerChiamataAjax["capitolo" + tipoCapitolo + ".statoOperativoElementoDiBilancio"] = statoCapitolo;

            // Scambio la classe originale con quella dello spinner
            toggleSpinner(spinner);
            collapse.is(".in") && collapse.collapse("hide");
            $.postJSON(url, oggettoPerChiamataAjax, function(data) {
                var cap;
                var capitoliRilevantiIva = [];

                if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                // Se il capitolo non è rilevante iva, esco
                for (cap in data.listaCapitoli) {
                    if (data.listaCapitoli.hasOwnProperty(cap) && data.listaCapitoli[cap].rilevanteIva) {
                        capitoliRilevantiIva.push(data.listaCapitoli[cap]);
                    }
                }

                if(capitoliRilevantiIva.length === 0) {
                    impostaDatiNegliAlert([ "FIN_ERR_0178 - Il Capitolo ricercato non e' rilevante ai fini IVA" ], alertErrori);
                    return;
                }

                $("#spanElencoUEBCapitolo").html(numeroCapitolo + "/" + numeroArticolo);
                popolaTabellaUeb(capitoliRilevantiIva);
                // Ripristino il pulsante
                $("#pulsanteSelezionaUeb").substituteHandler("click", exports.selezionaUeb)
                    .removeAttr("disabled");
                $("#collapseCapitoloUEB").collapse("show");
            }).always(function() {
                // Ripristino la classe originale
                toggleSpinner(spinner);
            });
        };

        /**
         * Seleziona l'ueb e ottiene la lista delle attivita iva collegate ad esso.
         *
         * @param e (Event) l'evento scatenante
         */
        exports.selezionaUeb = function(e) {
            var uidCapitolo = $("input[name='capitolo.uid']").filter(":checked").val();
            var spinner = $("#SPINNER_pulsanteRicercaAccertamentoModale");
            var oggettoPerChiamataAjax = {};
            e.preventDefault();
            // Chiudo gli alert
            chiudiAlerts();
            // Se non ho selezionato nulla, restituisco un errore
            if (!uidCapitolo) {
                impostaDatiNegliAlert([ "Necessario selezionare una UEB" ], alertErrori);
                return;
            }
            // Popolo l'oggetto per la chiamata
            oggettoPerChiamataAjax["capitolo.uid"] = uidCapitolo;

            spinner.addClass("activated");

            $.postJSON("associaAttivitaIvaCapitolo_ottieniAttivitaIvaAssociateCapitolo.do", oggettoPerChiamataAjax, function(data) {
                if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                // Popolo la tabella delle attivita
                popolaTabellaAttivitaCollegate(data.listaAttivitaIva);

                // Disattivo la tabella delle UEB e il pulsante
                $("#tabellaUEBCapitolo").find("input[type='radio']")
                    .attr("disabled", true);
                $("#pulsanteSelezionaUeb").off("click")
                    .attr("disabled", true);
                // Apro il collapse
                $("#attivitaIvaAssociate").collapse("show");
            }).always(function() {
                spinner.removeClass("activated");
            });
        };

        /**
         * Annulla la selezione della UEB.
         */
        exports.annullaSelezione = function() {
            $("#tabellaUEBCapitolo").find("input[type='radio']")
                .prop("checked", false)
                .removeAttr("disabled");
            $("#pulsanteSelezionaUeb").substituteHandler("click", exports.selezionaUeb)
                .removeAttr("disabled");

            // Chiudo i collapse
            $("#attivitaIvaAssociate, #collapseRicercaAttivitaIva").filter(".in")
                .collapse("hide");
        };

        /**
         * Apre l'inserimento di una nuova relazione tra capitolo e attivita.
         *
         * @param e (Event) l'evento scatenante
         */
        exports.inserisciNuovaRelazione = function(e) {
            e.preventDefault();
            // Svuoto gli input
            $("#collapseRicercaAttivitaIva").find("input")
                    .val("")
                    .end()
                // Apro il collapse
                .collapse("show");
        };

        /**
         * Ricerca l'attivita iva e apre il modale corrispondente.
         *
         * @param e (Event) l'evento scatenante
         */
        exports.ricercaAttivitaIva = function(e) {
            var codice = $("#codiceAttivitaIva").val();
            var descrizione = $("#descrizioneAttivitaIva").val();
            var oggettoPerChiamataAjax = {};
            var spinner = $("#pulsanteRicercaAttivitaIva").find("i");

            // Blocco l'evento
            e.preventDefault();

            // Chiudo gli alert
            chiudiAlerts();

            // Se non ho segnato alcun tipo di dato, esco fornendo l'errore
            if (!codice && !descrizione) {
                impostaDatiNegliAlert([ "COR_ERR_0018 - Indicare almeno un criterio di ricerca" ], alertErrori);
                return;
            }

            // Popolo l'oggetto per la chiamata AJAX
            oggettoPerChiamataAjax['attivitaIva.codice'] = codice;
            oggettoPerChiamataAjax['attivitaIva.descrizione'] = descrizione;

            // Attivo lo spinner
            toggleSpinner(spinner);

            $.postJSON("ajax/attivitaIvaAjax.do", oggettoPerChiamataAjax, function(data) {
                // Se ho degli errori esco subito
                if (impostaDatiNegliAlert(data.errori, alertErrori)) {
                    return;
                }
                // Popolo la tabella
                popolaTabellaAttivita(data.listaAttivitaIva);
                // Apro il modale
                $("#modaleRisultatiAttivitaIva").modal("show");
            }).always(function() {
                // Disattivo lo spinner
                toggleSpinner(spinner);
            });
        };

        /**
         * Associa l'attivita iva al capitolo.
         *
         * @param e (Event) l'evento scatenante
         */
        exports.associa = function(e) {
            var uidCapitolo = $("input[name='capitolo.uid']").filter(":checked").val();
            var uidAttivitaIva = $("input[name='attivitaIva.uid']").filter(":checked").val();
            var oggettoPerChiamataAjax = {};
            var spinner = $("#SPINNER_BUTTON_siAssocia");

            e.preventDefault();
            // Chiudo gli alert
            chiudiAlerts();

            if (!uidAttivitaIva) {
                impostaDatiNegliAlert([ "Necessario selezionare attivita'" ], alertErroriModale);
                return;
            }

            oggettoPerChiamataAjax['capitolo.uid'] = uidCapitolo;
            oggettoPerChiamataAjax['attivitaIva.uid'] = uidAttivitaIva;

            spinner.addClass("activated");

            $.postJSON("associaAttivitaIvaCapitolo_associaAttivitaIvaAlCapitolo.do", oggettoPerChiamataAjax, function(data) {
                // Se ho degli errori esco subito
                if (impostaDatiNegliAlert(data.errori, alertErroriModale)) {
                    return;
                }
                // Imposto il messaggio di successo
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);

                // Ripopolo la tabella delle attivitaAssociate
                popolaTabellaAttivitaCollegate(data.listaAttivitaIva);
                // Chiudo il collapse di ricerca
                $("#collapseRicercaAttivitaIva").collapse("hide");
                // Chiudo il modale
                $("#modaleRisultatiAttivitaIva").modal("hide");
            }).always(function() {
                spinner.removeClass("activated");
            });
        };

        /**
         * Gestisce il reset del form.
         */
        exports.resetForm = function() {
            var tables = $.fn.dataTable.fnTables();
            var index;
            var dtTable;
            // Pulisco i vari campi
            $("input[type='radio'][name='tipoCapitolo']").removeAttr("checked");
            $("#numeroCapitolo, #numeroArticolo").val("");

            // Chiudo i modali e i collapse
            $("#modaleRisultatiAttivitaIva").modal("hide");
            $("#collapseRicercaAttivitaIva, #attivitaIvaAssociate, #collapseCapitoloUEB").collapse("hide");

            // Pulisco e distruggo le tabelle
            for(index in tables) {
                if(tables.hasOwnProperty(index)) {
                    dtTable = $(tables[index]).dataTable();
                    dtTable.fnClearTable();
                    dtTable.fnDestroy();
                }
            }
        };

        return exports;
    }());

    $(function() {
        // Ripristino di default il pulsante delle attività associate
        $("#pulsanteSelezionaUeb").removeAttr("disabled");

        $("#pulsanteRicercaCapitolo").substituteHandler("click", AttivitaIvaCapitolo.ricercaCapitolo);
        $("#pulsanteSelezionaUeb").substituteHandler("click", AttivitaIvaCapitolo.selezionaUeb);
        $("#pulsanteAnnullaSelezione").substituteHandler("click", AttivitaIvaCapitolo.annullaSelezione);
        $("#pulsanteInserisciNuovaRelazione").substituteHandler("click", AttivitaIvaCapitolo.inserisciNuovaRelazione);
        $("#pulsanteRicercaAttivitaIva").substituteHandler("click", AttivitaIvaCapitolo.ricercaAttivitaIva);
        $("#BUTTON_siAssocia").substituteHandler("click", AttivitaIvaCapitolo.associa);

        $("form").substituteHandler("reset", AttivitaIvaCapitolo.resetForm);
    });
}(jQuery));