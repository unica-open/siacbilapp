/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
// Amplio il capitolo
var Capitolo = (function(global, exports) {

    'use strict';
    var alertErrori = $('#ERRORI');
    var cruscottinoLikeTables = {};
    
    exports.cercaCapitoliNelVincolo = cercaCapitoliNelVincolo;
    exports.ricercaVariazioniEquivalente = ricercaVariazioniEquivalente;
    exports.delayCaricamentoTab = delayCaricamentoTab;
    exports.caricaVincoli = caricaVincoli;
    exports.postCaricaVincoli = postCaricaVincoli;
    exports.caricaVariazioniCodifiche = caricaVariazioniCodifiche;
    exports.postCaricaVariazioniCodifiche = postCaricaVariazioniCodifiche;
    exports.caricaTabellaCruscottinoLike = caricaTabellaCruscottinoLike;
    exports.esportaRisultatiCruscottinoLike = esportaRisultatiCruscottinoLike;
    exports.handleReloadTabellaCruscottinoLike = handleReloadTabellaCruscottinoLike;

    global.endDownloadCruscottinoLike = endDownloadCruscottinoLike;

    /**
     * Imposta il dataTable delle relazioni tra Atto di Legge e Capitolo.
     */
    function impostaDataTableRelazioni() {
        var opzioni = {
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Nessuna relazione con atti di legge valida per questo capitolo.",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };

        // Inizializzazione del dataTable
        $("#tabellaRelazioneAttoDiLegge").dataTable(opzioni);
    }

    /**
     * Caricamento via Ajax della tabella dei vincoli e visualizzazione.
     *
     * @param idTabella l'id della tabella da renderizzare
     */
    function visualizzaTabellaCapitoliNelVincolo(idTabella) {
        var options = {
            bServerSide: false,
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ capitoli",
                sInfoEmpty: "0 capitoli",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti capitoli associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };

        $("#" + idTabella).dataTable(options);
    }

    /**
     * Ricerca le relazioni tra il capitolo e gli atti di legge ad esso collegati.
     */
    function cercaRelazioniAttoDiLegge() {
        var uidCapitolo = $("#HIDDEN_uidCapitolo").val();
        var uidBilancio = $("#HIDDEN_uidBilancio").val();

        // Chiamata AJAX per la ricerca delle relazioni
        return $.post("attoDiLegge/associaAttoDiLeggeAlCapitoloAction_ricercaConsultazione.do", {uidCapitolo: uidCapitolo, uidBilancio: uidBilancio})
        .then(returnHtmlOrReject);
    }
    
    /**
     * Ricerca i capitoli associati ad un vincolo con dato uid.
     *
     * @param uid l'uid del vincolo
     */
    function cercaCapitoliNelVincolo(elt) {
        var uidVincolo = parseInt(elt.siblings("input[type='hidden']").val(), 10);
        var uidElement = elt.attr("href").substring(1);
        var collapse = $("#" + uidElement);

        elt.overlay("show");
        collapse.children("div")
          .load("consultaVincoloPerCapitolo.do", {"uidDaConsultare": uidVincolo}, function() {
            // Attivo i popover
            $(this).find("a[rel='popover']")
                .eventPreventDefault("click")
                .popover();

            // Visualizzo le tabelle
            visualizzaTabellaCapitoliNelVincolo("tabellaCapitoliEntrata_" + uidElement);
            visualizzaTabellaCapitoliNelVincolo("tabellaCapitoliSpesa_" + uidElement);

            // Rimuovo le classi di controllo e apro il collapse
            elt.overlay("hide");
            collapse.collapse("show");
        });
    }

    /**
     * Ricerca delle variazioni del capitolo equivalente.
     *
     * @param uid (String) l'uid del capitolo equivalente
     * @param url (String) l'url da invocare
     * 
     * @returns (Promise) la promise associata al caricamento AJAX
     */
    function ricercaVariazioniEquivalente(uid) {
        var table = $("#tabellaMovimentiGestione").overlay("show");
        return $.post('ricercaMovimentiCapitoloAjax_ottieniVariazioniCapitoloEquivalente.do', {uidCapitolo: uid})
        .then(returnHtmlOrReject)
        .then(function(html) {
            table.find('tbody').html(html);
            return html;
        }).always(table.overlay.bind(table, "hide"));
    }
    
    /**
     * Carica il tab tramite la funzione selezionata al primo click
     * @param tabLabel (String)   il label del tab
     * @param tab      (String)   il tab
     * @param fnc      (Function) la funzione da invocare (deve ritornare una Promise(Object{html, data}))
     * @param thenFnc  (Function) la funzione da invocare al termine dell'invocazione (deve ritornare una promise)
     */
    function delayCaricamentoTab(tabLabel, tab, fnc, thenFnc) {
        var $tabLabel = $(tabLabel);
        $tabLabel.one('click', function() {
            var $overlay = $tabLabel.find('a').overlay('show');
            var $tab = $(tab);
            fnc()
                .then($tab.html.bind($tab))
                .then(thenFnc || $.noop)
                .always($overlay.overlay.bind($overlay, 'hide'));
        });
    }
    
    /**
     * Caricamento della pagina dei vincoli.
     * @param tipoCapitolo (String) il tipo di capitolo
     * @returns (Promise) la promise del caricamento dei dati
     */
    function caricaVincoli(tipoCapitolo) {
        var uid = $('#HIDDEN_uidCapitolo').val();
        return $.post('ricercaMovimentiCapitoloAjax_ottieniVincoliCapitolo' + tipoCapitolo + '.do', {uidCapitolo: uid})
        .then(returnHtmlOrReject);
    }
    
    /**
     * Termine del caricamento dei vincoli
     */
    function postCaricaVincoli() {
        $(".accordion-toggle[data-vincolo='true']").each(function() {
            var self = $(this);
            self.one("click", function(e) {
                e.preventDefault();
                e.stopPropagation();
                Capitolo.cercaCapitoliNelVincolo(self);
            });
        });
    }
    
    /**
     * Caricamento della pagina dei movimenti.
     * @param tipoCapitolo (String) il tipo di capitolo
     * @returns (Promise) la promise del caricamento dei dati
     */
    function caricaMovimenti() {
        var uid = $('#HIDDEN_uidCapitolo').val();
        return $.post('ricercaMovimentiCapitoloAjax_ottieniVariazioniCapitolo.do', {uidCapitolo: uid})
        .then(returnHtmlOrReject);
    }
    
    /**
     * Termine del caricamento dei movimenti
     */
    function postCaricaMovimenti() {
        disableEventOnAncoraVariazioni('Aumento');
        disableEventOnAncoraVariazioni('Diminuzione');
    }
    
    /**
     * Caricamento della pagina dei movimenti.
     * @param tipoCapitolo (String) il tipo di capitolo
     * @returns (Promise) la promise del caricamento dei dati
     */
    function caricaVariazioniCodifiche() {
        var uid = $('#HIDDEN_uidCapitolo').val();
        $('#tabellaVariazioniCodifiche').overlay('show');
        return $.post('ricercaMovimentiCapitoloAjax_ottieniVariazioniCodificheCapitolo.do', {uidCapitolo: uid})
        .then(returnHtmlOrReject);
    }
    
    /**
     * Termine del caricamento dei movimenti
     */
    function postCaricaVariazioniCodifiche() {
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);
        var tabella = $('#tabellaVariazioniCodifiche');
        var opzioni = {
            bServerSide: true,
            sServerMethod: "POST",
            sAjaxSource: "risultatiRicercaMovimentiCapitoloVariazioniCodificheAjax.do",
            bPaginate: true,
            bLengthChange: false,
            iDisplayLength: 10,
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
                sZeroRecords: "Nessuna variazione di codifiche per questo capitolo. ",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData : defaultPerDataTable('numeroVariazione')},
                {aTargets: [1], mData : defaultPerDataTable('dataVariazione')},
                {aTargets: [2], mData : defaultPerDataTable('provvedimentoVariazione')},
                {aTargets: [3], mData : defaultPerDataTable('strutturaAmministrativaContabileCapitoloPreVariazione')},
                {aTargets: [4], mData : defaultPerDataTable('descrizioneCapitoloPreVariazione')},
                {aTargets: [5], mData : defaultPerDataTable('descrizioneArticoloPreVariazione')}
            ],
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                tabella.parent().find('div.dataTables_processing').parent("div").show();
                tabella.overlay('hide');
                $('#tabellaVariazioniCodifiche_wrapper').overlay('show');
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function () {
                tabella.parent().find('div.dataTables_processing').parent("div").hide();
                $('#tabellaVariazioniCodifiche_wrapper').overlay('hide');
            }
        };
        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            tabella.dataTable().fnClearTable(false);
            tabella.dataTable().fnDestroy();
        }
        tabella.dataTable(opzioni);
    }
    
    /**
     * Disabilita l'evento sulla maschera delle variazioni
     * @param tipo il tipo di variazione
     */
    function disableEventOnAncoraVariazioni(tipo) {
        var nonZero = [0, 1, 2].reduce(function(acc, val) {
            return acc && $('#variazioniIn' + tipo + 'TotaleNum' + val).text() === '0';
        }, true);
        if(nonZero) {
            $('#ancoraVariazioni' + tipo).eventPreventDefault('click');
        }
    }
    
    /**
     * Restituisce l'HTML presente nel parametro o una Promise rigettata
     * @param data (Object/String) i dati della chiamata
     * @returns (Promise/String) una promise rigettata nel caso vi siano errori; l'HTML nel caso sia presente
     */
    function returnHtmlOrReject(data) {
        if(impostaDatiNegliAlert(data.errori, alertErrori)) {
            return $.Deferred().reject(data).promise();
        }
        return data;
    }
    
    // SIAC-5254
    /**
     * Caricamento della tabella tipo-cruscottino
     * @param idTabella                   (string) l'id della tabella
     * @param tipoEntitaConsultabilePadre (string) il tipo del padre
     * @param tipoEntitaConsultabile      (string) il tipo dell'entita' da consultare
     * @param arrFilters                  (string[]) i filtri ulteriori (optional, default [])
     * @returns (Promise) la promise corripondente al caricamento dell'intestazione della tabella
     */
    function caricaTabellaCruscottinoLike(idTabella, tipoEntitaConsultabilePadre, tipoEntitaConsultabile, arrFilters) {
        return $.postJSON('ricercaIntestazioniCampiTabellaEntitaConsultabileAjax.do', {tipoEntitaConsultabile: tipoEntitaConsultabile})
        .then(function(data) {
            var arrIntestazioni = data.listaIntestazioniCampiTabella;
            var intestazioni;
            var footer;
            var table;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            intestazioni = arrIntestazioni.reduce(function(acc, el) {
                return acc + '<th>' + el.name + '</th>';
            }, '<tr>') + '</tr>';
            footer = arrIntestazioni.reduce(function(acc, el) {
                return acc + (el.summable ? '<th data-summable class="text-right"></th>' : '<th></th>');
            }, '<tr>') + '</tr>';
            table = $(idTabella);
            table.find('thead').html(intestazioni);
            table.find('tfoot').html(footer);
            generaDataTableCruscottinoLike(table, tipoEntitaConsultabilePadre, tipoEntitaConsultabile, arrIntestazioni, arrFilters || []);
        });
    }

    /**
     * Generazione del dataTable tipo-cruscottino
     * @param table                       (jQuery)   la tabella
     * @param tipoEntitaConsultabilePadre (string)   il tipo del padre
     * @param tipoEntitaConsultabile      (string)   il tipo dell'entita' da consultare
     * @param arrIntestazioni             (string[]) le intestazioni
     * @param arrFilters                  (string[]) i filtri ulteriori
     */
    function generaDataTableCruscottinoLike(table, tipoEntitaConsultabilePadre, tipoEntitaConsultabile, arrIntestazioni, arrFilters) {
        var tableId = table.attr('id');
        var colonne = generaColonne(arrIntestazioni);
        var uidCapitolo = $('#HIDDEN_uidCapitolo').val();
        var askImporti = true;
        var opts = {
            bServerSide: true,
            sServerMethod: 'POST',
            sAjaxSource: 'ricercaEntitaConsultabileAjax.do',
            bPaginate: true,
            bLengthChange: false,
            bAutoWidth:false,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bDestroy:true,
            bFilter: false,
            fnServerParams: function(aoData) {
                var tmp;
                aoData.push({name: 'tipoEntitaConsultabile', value: tipoEntitaConsultabile});
                aoData.push({name: 'tipoEntitaConsultabilePadre', value: tipoEntitaConsultabilePadre});
                aoData.push({name: 'uidEntitaConsultabilePadre', value: uidCapitolo});
                
                // SIAC-6193
                if(cruscottinoLikeTables[tableId] && cruscottinoLikeTables[tableId].refresh) {
                    // Force page 1
                    this.fnSettings()._iDisplayStart = 0;
                    tmp = aoData.filter(function(el) {
                        return el.name === 'iDisplayStart';
                    });
                    if(tmp.length) {
                        tmp[0].value = 0;
                    }
                    cruscottinoLikeTables[tableId].refresh = false;
                }
                // Richiede l'importo solo la prima invocazione
                aoData.push({name: 'requestImporto', value: askImporti});
                // Aggiungo i filtri generici
                arrFilters.forEach(function(el, idx) {
                    var value = $(el).val();
                    aoData.push({name: 'listaFiltroGenerico[' + idx + ']', value: value === undefined ? '' : value});
                });
            },
            fnServerData: function (sSource, aoData, fnCallback, oSettings) {
                var newFnCallback = function(data) {
                    if(askImporti) {
                        // da capire come gestire  
                    	//askImporti = false;
                        table.find('th[data-summable]').html("Totale:&nbsp;" + formatMoney(data.importoEntitaConsultabili));
                    }
                    fnCallback.apply(this, arguments);
                }
                oSettings.jqXHR = $.ajax({
                    dataType: 'json',
                    type: 'POST',
                    'url': sSource,
                    'data': aoData,
                    'success': newFnCallback
                });
            },
            bProcessing: true,
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
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $('#' + tableId + '_processing').parent('div').show();
            },
            aoColumnDefs: colonne
        };
        var dt = table.dataTable(opts);
        
        // Save
        cruscottinoLikeTables[tableId] = {dataTable: dt, refresh: false};
    }
    
    /**
     * Formats the values as money
     * @param val (any) the value
     * @returns (string) the value as money, if numeric; nothing otherwise 
     */
    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return '';
    }
    
    /**
     * Generazione delle colonne
     * @param arrIntestazioni (string[]) le intestazioni delle colonne
     * @returns (any[]) le colonne
     */
    function generaColonne(arrIntestazioni) {
        return arrIntestazioni.map(function(el, idx) {
            return {aTargets: [idx], sTitle: arrIntestazioni[idx].name, mData: function(source) {
                return source.campiColonne[arrIntestazioni[idx].name] || '';
            }, fnCreatedCell: function(nTd){
                $('a', nTd).popover();
            }};
        });
    }
    
    /**
     * Esportazione dei risultati tipo-cruscottino
     * @param tipoPadre (string) il tipo del padre
     * @param e         (Event)  l'evento scatenante
     */
    function esportaRisultatiCruscottinoLike(tipoPadre, e){
        var $target = $(e.target);
        var $container = $('#iframeContainer');
        var figlio = $target.data('figlio');
        var xlsx = $target.data('xlsx');
        var uidCapitolo = $('#HIDDEN_uidCapitolo').val();
        
        // SIAC-6193
        var filters = ($target.data('filters') || '')
            .split(';')
            .filter(notEmptyString);
        
        var params = 'tipoEntitaConsultabile=' + encodeURIComponent(figlio)
            + '&tipoEntitaConsultabilePadre=' + encodeURIComponent(tipoPadre)
            + '&uidEntitaConsultabilePadre=' + encodeURIComponent(uidCapitolo)
            + '&isXlsx=' + encodeURIComponent(xlsx)
            // SIAC-6193
            + filters.reduce(function(acc, el, idx) {
                var value = $(el).val();
                return acc + '&listaFiltroGenerico[' + idx + ']=' + encodeURIComponent(value === undefined ? '' : value);
            }, '');
        
        var url = 'ricercaEntitaConsultabileAjax_download.do?' + params;
        var $fieldset = $target.closest('fieldset');
        var fieldset = $fieldset.attr('id');
        var html = '<iframe src="' + url + '" onload="endDownloadCruscottinoLike(\'' + fieldset + '\')"></iframe>';

        // Attivo il form-submitted
        $fieldset.addClass('form-submitted');

        $container.html(html);
    }
    
    /**
     * Checks if the string is not empty
     * @param str (string) the string
     * @returns (boolean) whether the string is not empty
     */
    function notEmptyString(str) {
        return str && str !== '';
    }

    /**
     * Callback per la gestione del download.
     * @param fieldset (string) l'id del fieldset da gestire
     */
    function endDownloadCruscottinoLike(fieldset) {
        var cnt = $('#iframeContainer').find('iframe').contents();
        var html = cnt.find('body').html();
        $("#" + fieldset).removeClass('form-submitted');
        if(!html) {
            //Download completato correttamente. Esco.
            return;
        }
        if(/^<pre/.test(html) && /errori/.test(html)){
            //Errore applicativo.
            var parsed;
            try {
                parsed = JSON.parse(html.replace(/<\/?pre.*?>/g, ''));
            } catch (syntaxError){
                return bootboxAlert(html);
            }
            parsed.errori = parsed.errori || [];
            return bootboxAlert('<ul><li>' + parsed.errori.map(function(el) {
                return el.codice + ' - ' + el.descrizione;
            }).join('</li><li>') + '</li></ul>');
        }
        
        //ERROR 500 o 404 o altro.
        return bootboxAlert(html);
    }
    
    /**
     * Gestione del reload della tabella tipo-cruscottino
     * @param container (jQuery) il contenitore
     */
    function handleReloadTabellaCruscottinoLike(container) {
        container.substituteHandler('change', reloadTabellaCruscottinoLike.bind(undefined, container.data('reloadDatatable')));
    }
    
    /**
     * Caricamento della tabella
     */
    function reloadTabellaCruscottinoLike(reference) {
        var dt = cruscottinoLikeTables[reference];
        if(dt === undefined) {
            // Nulla da fare
            return;
        }
        dt.refresh = true;
        // Ridisegna
        dt.dataTable.fnDraw();
    }

    $(function () {
        var openTab = $('#HIDDEN_openTab').val();
        // Cerco le relazioni con gli atti di legge
        delayCaricamentoTab('#tabAtti', '#atti', cercaRelazioniAttoDiLegge, impostaDataTableRelazioni);
        delayCaricamentoTab('#tabMovimenti', '#movimenti', caricaMovimenti, postCaricaMovimenti);
        delayCaricamentoTab('#tabVariazioniCodifiche', '#variazioniCodifiche', caricaVariazioniCodifiche, postCaricaVariazioniCodifiche);
        // SIAC-6305
        if(openTab !== '' && openTab !== undefined) {
            $('li#' + openTab + ' a').click();
        }

        // Impostazione dei link per le ricerche
        $(document).on("shown", function() {
            $("[data-overlay]").overlay("refresh");
        });
    });
    return exports;
}(this, Capitolo || {}));
