/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($) {
    'use strict';
    // VARIABILI GENERICHE UTILIZZATE NELLO SCOPE
    var capitoliModaleSelezionati = [];
    var capitoliInTemp = [];
    var accantonamentiSelezionati = [];
    var countAccantonamentiSelezionati = 0;
    var baseOptionsDataTable = {
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
            sInfo: '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty: '0 risultati',
            sProcessing: 'Attendere prego...',
            sZeroRecords: 'Nessun elemento disponibile',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun dato disponibile'
            }
        }
    };
    var opzioniPaginazioneServerSide={
        bServerSide:true,
        sServerMethod: 'POST'
    };
    var alertErrori = $('#ERRORI');
    var alertInformazioni = $('#INFORMAZIONI');
    var erroriModaleCapitolo = $('#ERRORI_MODALE_CAPITOLO');
    var erroriModaleAggiornamento = $('#ERRORI_modaleAggiornamentoAccantonamentoDubbiaEsigibilita');

    // FUNZIONI UTILITA
    /**
     * Apre la modale di selezione del capitolo
     * @returns (jQuery) la modale
     */
    function apriModaleCapitoloDubbiaEsigibilita() {
        var $modale = $('#modaleGuidaCapitolo');
        $('#fieldsetRicercaGuidataCapitolo').find('select').val('').change();
        $('#HIDDEN_ElementoPianoDeiContiUid').val('');
        $('#HIDDEN_StrutturaAmministrativoContabileUid').val('');
        $('#numeroCapitolo_modale').val('');
        $('#numeroArticolo_modale').val('');
        if($('#HIDDEN_gestioneUEB').val() === 'true'){
            $('#numeroUEB_modale').val('');
        }
        $('#divRisultatiRicercaCapitolo').slideUp();
        erroriModaleCapitolo.slideUp();
        $modale.modal('show');
        return $modale;
    }
    /**
     * Seleziona tutti i checkbox nella tabella indicata dall'attributo data-referred-table del pulsante chiamante.
     * @returns (jQuery) i checkbox su cui si &eacute; agito.
     */
    function checkAllInTable() {
        var $this = $(this);
        var tableId = $this.data('referred-table');
        var isChecked = $this.prop('checked');
        // Seleziono tutti i checkboxes e li rendo come il globale
        return $(tableId).find('tbody')
            .find('input[type="checkbox"]').not(':disabled')
                .prop('checked', isChecked)
                .each(function(idx, el){
                    $(el).change();
                });
    }
    /**
     * Pulisce la tabella indicata
     * @param $tabella (jQuery) l'oggetto jQuery che wrappa la tabella
     */
    function cleanDataTable($tabella){
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);
        if($(tabelleGiaInDataTable).filter($tabella).length > 0) {
            $tabella.dataTable().fnClearTable(false);
            $tabella.dataTable().fnDestroy();
        }
    }
    /**
     * Ottiene il valore della percentuale di accantonamento correttamente formattato in modo null-safe
     * @param $tr       (jQuery) la colonna
     * @param selettore (string) il selettore da usare
     * @returns (number) il valore della percentuale
     */
    function ottieniValorePercentualeAccantonamento(tr, selettore){
        var valore;
        if(!tr || !selettore){
            return 0;
        }
        valore = tr.find(selettore).val();
        if(!valore){
            return;
        }

        return +parseLocalNum(valore);
    }
    /**
     * Definisce quale tipo di media utilizzare (semplice o ponderata) e applica la funzione corrispondente
     * @param e (Event) l'evento scatanante l'invocazione
     */
    function calcolaMedia(e) {
        var container = $(this).closest('tr, fieldset');
        var fnc = $('#mediaApplicataSemplice').is(':checked') ? calcolaMediaSemplice : calcolaMediaPonderata;
        var avg;

        if ($('#riscossioneVirtuosaTrue').is(':checked')) {
            avg = fnc(container, true, 'input[data-uba=3], input[data-uba=2], input[data-uba=1]');
        }
        else {
            avg = fnc(container, true, 'input[data-uba]');
        }

        container.find('input[data-media]').val(formatMoney(avg.toString()));

        if(e){
            container.find('input[data-delta]').val(formatMoney(avg.toString()));
        }
    }
    /**
     * Definisce quale tipo di media utilizzare (semplice o ponderata) e applica la funzione corrispondente
     */
    function calcolaMediaTable(tr) {
        var container = $(tr);
        var fnc = $('#mediaApplicataSemplice').is(':checked') ? calcolaMediaSemplice : calcolaMediaPonderata;
        var avg;

        if ($('#riscossioneVirtuosaTrue').is(':checked')) {
            avg = fnc(container, false, '[data-uba=3], [data-uba=2], [data-uba=1]');
        }
        else {
            avg = fnc(container, false, '[data-uba]');
        }

        container.find('[data-media]').html(formatMoney(avg.toString()));
    }

    /**
     * Gestione del checkbox di selezione globale
     * @param $tabella (jQuery) la tabella da considerare
     */
    function gestisciCheckedSelezionaTutti(tabella){
        var $checkboxes = tabella.find('tbody').find('input[type="checkbox"]').not(':disabled');
        var $checkedCheckboxes = $checkboxes.filter(':checked');
        var $selezionaTutti = tabella.find('.check-all');
        var method = ($checkboxes.length > 0 &&($checkboxes.length === $checkedCheckboxes.length)? 'attr' : 'removeAttr');
        $selezionaTutti[method]('checked', 'checked');
    }
    /**
     * Calcola la media semplice degli uba nella tr
     * @param $tr         (jQuery) la riga
     * @param emptyIsZero (boolean) se considerare il valore vuoto di un campo pari a zero
     * @param ubaSel      (string) l'UBA da selezionare
     * @returns (BigNumber) la media
     */
    function calcolaMediaSemplice($tr, emptyIsZero, ubaSel) {
        var uba = $tr.find(ubaSel);
        var reducer = mediaReductor('0.2', emptyIsZero);
        return uba.get().reduce(reducer, new BigNumber(0));
    }
    /**
     * Calcola la media ponderata degli uba presenti nella tr
     * @param $tr         (jQuery) la riga
     * @param emptyIsZero (boolean) se considerare il valore vuoto di un campo pari a zero
     * @returns (BigNumber) la media
     */
    function calcolaMediaPonderata(tr, emptyIsZero) {
        var reducer10 = mediaReductor('0.35', emptyIsZero);
        var reducer432 = mediaReductor('0.1', emptyIsZero);
        var uba432 = tr.find('[data-uba=4], [data-uba=3], [data-uba=2]');
        var uba10 = tr.find('[data-uba=1], [data-uba=0]');

        var sum432 = uba432.get().reduce(reducer432, new BigNumber(0));
        var sum10 = uba10.get().reduce(reducer10, new BigNumber(0));
        if(sum432 === '' || sum10 === '') {
            return '';
        }

        return sum432.plus(sum10);
    }
    /**
     * Riduttore per la media
     * @param multiplier  (string) il moltiplicatore
     * @param emptyIsZero (boolean) se considerare il valore vuoto di un campo pari a zero
     * @returns (function(BigNumber, HTMLInputElement) => BigNumber) il riduttore
     */
    function mediaReductor(multiplier, emptyIsZero) {
        var mult = new BigNumber(multiplier);
        return function(acc, el) {
            var nodeType = el.nodeName.toUpperCase();
            var value = nodeType === 'INPUT' ? el.value : el.innerHTML;
            var num;
            
            if(acc === '') {
                // Fallback
                return '';
            }
            
            if(!emptyIsZero && !value) {
                return '';
            }
            if(value) {
                num = parseLocalNum(value) || 0;
                acc = acc.plus(new BigNumber(num).times(mult));
            }
            return acc;
        };
    }
    /**
     * Applica la funzione di formattazione del numero se e solo se il type of dell'elemento di di tipo numero
     * @param el (any) l'elemento da formattare
     * @returns (string) l'elemento formattato
     */
    function doFormatMoney(el) {
        if(typeof el === 'number') {
            return el.formatMoney();
        }
        return '';
    }
    /**
     * Crea la funzione che dataTable utilizza per creare un input di tipo testo in cui inserire gli uba e, se possibile, lo prepopola con i dati presenti nel source utilizzato da datatable
     * @param fieldname (string)  il nome del campo
     * @param data      (string)  i data-attribute
     * @param disabled  (boolean) se il campo sia disabilitato
     * @returns (function(any) => string) la funzione che genera il campo
     */
    function creaInput(fieldname, data, disabled) {
        return function(source) {
            return '<input type="text" class="input-small soloNumeri decimale text-right" name="' + fieldname + '" value="'  + doFormatMoney(source[fieldname]) + '" '
                + data + ' ' + (disabled ? 'disabled' : '') + '/>';
        };
    }
    /**
     *  Crea la funzione che dataTable utilizza per creare un input di tipo testo in cui inserire gli uba e, se possibile, lo prepopola con i dati presenti nella variabile accantonamentiSelezionati
     *  @param fieldname (string)  il nome del campo
     * @param data      (string)  i data-attribute
     * @param disabled  (boolean) se il campo sia disabilitato
     * @returns (function(any) => string) la funzione che genera il campo
     */
    function creaInputTemp(fieldname, data, disabled) {
        return function(source) {
            var datum = accantonamentiSelezionati[source.capitolo.uid];
            var valoreDaImpostare = datum && datum[fieldname] ? datum[fieldname] : '';
            return '<input type="text" class="input-small soloNumeri decimale text-right" name="' + fieldname + '" value="'  + valoreDaImpostare + '" '
                + data + ' ' + (disabled ? 'disabled' : '') + '/>';
        };
    }

    /**
     * Aggiorna la tabella di ricerca dopo avere effettuato il salvataggio di alcuni accantonamenti
     * @param $tabella (jQuery) la tabella da ricaricare
     * @param url      (string) l'URL da invocare
     * @returns (Promise) la promise relativa al calcolo della tabella
     */
    function ricaricaTabella(tabella, url){
        var datatable, settings, obj;
        var tabelleGiaInDataTable = $.fn.dataTable.fnTables(true);

        if($(tabelleGiaInDataTable).filter(tabella).length > 0) {
            return $.Deferred().resolve().promise();
        }
        datatable = tabella.dataTable();
        settings = dataTable.fnSettings();
        obj = {
            forceRefresh: true,
            iTotalRecords: settings._iRecordsTotal,
            iTotalDisplayRecords: settings._iRecordsDisplay,
            iDisplayStart: settings._iDisplayStart,
            iDisplayLength: settings._iDisplayLength
        };
        return $.postJSON(url, obj).then(dataTable.fnDraw.bind(dataTable));
   }


    // MODALE CAPITOLI

    /**
     * Ricerca il capitolo Entrata Previsione
     * @param event (Event) l'evento scatenante l'invocazione
     * @returns (Promise) la Promise collegata alla ricerca del capitolo
     */
    function ricercaCapitoliModale(event) {
        var $form, arrayToSend;
        var $spinner = $('#SPINNER_RicercaCapitolo');
        // per prima cosa, disabilito il div
        var divRisultatiRicerca = $('#divRisultatiRicercaCapitolo').slideUp();
        event.stopPropagation();
        event.stopImmediatePropagation();
        event.preventDefault();

        $form = $(event.target).parents('form');
        arrayToSend = unqualify($form.serializeObject(), 1);
        $spinner.addClass('activated');

        return $.postJSON('selezionaCapitolo_capitoloEntrataPrevisioneFondiDubbiaEsigibilita.do', arrayToSend)
        .then(function(data) {
            var errori = data.errori;

            if(impostaDatiNegliAlert(errori, erroriModaleCapitolo, true, true)){
                return;
            }
            gestioneTabellaCapitoliModale();
            divRisultatiRicerca.slideDown();

            $('.modal-body').scrollTop(divRisultatiRicerca.position().top);
        }).always($spinner.removeClass.bind($spinner,'activated'));
    }

    /**
     * Crea e gestisce le chiamate ajax per la tabella di risultati
     */
    function gestioneTabellaCapitoliModale() {
        var opts = {
            sAjaxSource : 'risultatiRicercaCapitoloEntrataPrevisioneModaleAjax.do',
            bAutoWidth:false,
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $("#divRisultatiRicercaCapitolo").addClass("hide");
                $("#risultatiRicercaCapitoloEntrataPrevisione_processing").parent("div")
                    .show();
            },
            fnDrawCallback: function () {
                // Mostro il div del processing
                $("#risultatiRicercaCapitoloEntrataPrevisione_processing").parent("div")
                    .hide();
                $("#divRisultatiRicercaCapitolo").removeClass("hide");
                gestisciCheckedSelezionaTutti($("#divRisultatiRicercaCapitolo"));
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(src){
                    return '<input type="checkbox" name="capitolo.uid" value="' + src.uid + '" '
                        + (capitoliInTemp[src.uid] ? 'disabled="disabled"' : '') + (capitoliModaleSelezionati[src.uid] ? 'checked' : '') +'  />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('capitolo', oData).substituteHandler('change', function(){
                        gestisciSelezioneCapitoliModale($(this), oData);
                    });
                } },
                {aTargets: [1], mData: defaultPerDataTable('capitolo'), fnCreatedCell: setCssClass('tab_Right')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('classificazione')},
                {aTargets: [4], mData: defaultPerDataTable('struttAmmResp')},
                {aTargets: [5], mData: defaultPerDataTable('pdcFinanziario')}
            ]
        };

        var tabella = $('#risultatiRicercaCapitolo');
        var opzioniTabellaCapitoli = $.extend(true, {},baseOptionsDataTable, opzioniPaginazioneServerSide, opts );
        if ( $.fn.DataTable.fnIsDataTable( tabella.get(0) ) ) {
           tabella.dataTable().fnDestroy();
        }
        tabella.dataTable(opzioniTabellaCapitoli);
    }
    /**
     * Crea una funzione che imposta una classe CSS
     * @param cssClass (string) la classe CSS da impostare
     * @returns (function(Node) => any) la funzione che imposta la classe CSS
     */
    function setCssClass(cssClass) {
        return function(nTd) {
            nTd.className = cssClass;
        };
    }

    /**
     * Gestione della selezione del capitolo nella modale
     * @param $checkbox (jQuery) il checkbox
     * @param capitolo  (any)    il capitolo
     */
    function gestisciSelezioneCapitoliModale(checkbox, capitolo){
        if(!checkbox.is(':checked')) {
            capitoliModaleSelezionati[capitolo.uid] = undefined;
            return;
        }
        capitoliModaleSelezionati[capitolo.uid] = {
            capitolo: capitolo
        };
    }

    /**
     * Conferma dei capitoli nella modale
     * @returns (Promise) la promise collegata alla conferma dei capitoli
     */
    function confermaCapitoliModale(){
        var listaCapitoli = capitoliModaleSelezionati.filter(filterOutUndefined).map(mapToObject);
        var obj = {listaCapitoloEntrataPrevisione: listaCapitoli};

        $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_confermaCapitoli.do', projectToString(obj))
        .then(function(data) {
            if (impostaDatiNegliAlert(data.errori, erroriModaleCapitolo)) {
                return;
            }
            capitoliModaleSelezionati = [];
            cleanDataTable($('#risultatiRicercaCapitolo'));
            popolaTabellaTempCapitoli(data.listaAccantonamentoFondiDubbiaEsigibilitaTemp);
            $('#divRisultatiRicercaCapitolo').addClass('hide');
            $('#modaleGuidaCapitolo').modal('hide');
        });
    }
    /**
     * Filtra gli elementi undefined
     * @param el (any) l'elemento da controllare
     * @returns (boolean) se l'elemento non e' undefined
     */
    function filterOutUndefined(el) {
        return el !== undefined;
    }
    /**
     * Mappa l'elemento in un altro elemento
     * @param elem (any) l'elemento da mappare
     * @return (any) l'elemento wrappato
     */
    function mapToObject(elem) {
        return {
            uid: elem.capitolo.uid,
            annoCapitolo: elem.capitolo.annoCapitolo,
            numeroCapitolo: elem.capitolo.numeroCapitolo,
            numeroArticolo: elem.capitolo.numeroArticolo,
            numeroUEB: elem.capitolo.numeroUEB || 1
        };
    }

    // CAPITOLI -> ACCANTONAMENTI: tabella temporanea
    /**
     * Chiama la Action per ottenere la lista di capitoli selezionati nella modale
     * @returns (Promise) la promise legata al caricamento della lista
     */
    function leggiFondiDubbiaEsigibilita() {
        var wrapper = $('#riepilogoCapitoliGiaAssociati_wrapper');
        return $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_caricaListaAccantonamentoFondi.do', {})
        .then(function(data){
            popolaDataTableRiepilogoCapitoli();
        }).always(wrapper.overlay.bind(wrapper, 'hide'));
    }

    /**
     * Riporta i dati sui capitoli
     * @returns (any) il risultato
     */
    function riportaSuCapitoli() {
        var srcCap = $('#tempCapitoli tbody input:checked').first().closest('tr');
        var trCap = $('#tempCapitoli tbody tr').not(srcCap);
        var arrUba = [];
        var u;
        if(!srcCap.length){
            return;
        }
        for (u = 0; u <= 4; u++) {
            arrUba[u] = srcCap.find('[data-uba=' + u + ']').val();
        }

        trCap.each(function(idx, el) {
            var $el = $(el);
            var u;
            for (u = 0; u <= 4; u++) {
                $el.find('[data-uba=' + u + ']').val(arrUba[u]).blur();
            }
        });

        trCap.find('[data-uba=0]').trigger('change');
        return false;
    }

    /**
     * Salvataggio dei capitoli
     * @returns (boolean) false per indicare di bloccare l'evento
     */
    function salvaCapitoli() {
        var $salva = $('#salvaCapitoli');
        if (!$salva.hasClass('disabled')) {
            var listaAccantonamenti = accantonamentiSelezionati.filter(filterOutUndefined);
            var obj = {listaAccantonamentoFondiDubbiaEsigibilitaSelezionati: listaAccantonamenti};

            $salva.overlay('show');
            $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_salvaCapitoli.do', projectToString(obj))
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, alertErrori)){
                    return;
                }
                impostaDatiNegliAlert(data.informazioni, alertInformazioni);
                accantonamentiSelezionati = [];
                $('#riepilogoCapitoliGiaAssociati_wrapper').overlay('show');
                leggiFondiDubbiaEsigibilita();
                return leggiAccantonamentiTempNelModel();
            }).always(function() {
                $('#msgElimina').modal('hide');
                $salva.overlay('hide');
                $('#riepilogoCapitoliGiaAssociati_wrapper').overlay('hide');
            });
        }

        return false;
    }
    /**
     * Impostazione dell'informazione di successo
     */
    function impostaInformazioneSuccesso() {
        impostaDatiNegliAlert(['COR_INF_0006 - Operazione effettuata correttamente'], $('#INFORMAZIONI'));
    }

    /**
     * Popola opportunamente l'oggetto accantonamenti selezionati
     * @param nTd   (Node)   il td da considerare
     * @param sData (string) il contenuto del td come stringa
     * @param oData (any)    l'oggetto tramite cui si &eacute; popolato il td
     */
    function gestisciAccantonamentiSelezionati(nTd, sData, oData){
        var uidCapitoloEntrataPrevisione = oData.capitolo.uid || 0;
        var tr = $(nTd).closest('tr');

        var checkbox = $(nTd).find('input[type="checkbox"]');
        if (checkbox.length > 0) {
            countAccantonamentiSelezionati = countAccantonamentiSelezionati + (checkbox.is(':checked') ? +1 : -1);
        }
        $('#salvaCapitoli').toggleClass('disabled', countAccantonamentiSelezionati === 0);
        $('#pulsanteRiportaSuCapitoli').attr('disabled', countAccantonamentiSelezionati === 0);
        if(!tr.find('input[type="checkbox"]').is(':checked')) {
            accantonamentiSelezionati[uidCapitoloEntrataPrevisione] = undefined;
            return;
        }

        accantonamentiSelezionati[uidCapitoloEntrataPrevisione] = {
            capitolo: oData.capitolo,
            percentualeAccantonamentoFondi: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeAccantonamentoFondi"]'),
            percentualeAccantonamentoFondi1: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeAccantonamentoFondi1"]'),
            percentualeAccantonamentoFondi2: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeAccantonamentoFondi2"]'),
            percentualeAccantonamentoFondi3: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeAccantonamentoFondi3"]'),
            percentualeAccantonamentoFondi4: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeAccantonamentoFondi4"]'),
            percentualeMediaAccantonamento: +parseLocalNum(tr.find('input[name="percentualeMediaAccantonamento"]').val()),
            percentualeDelta: ottieniValorePercentualeAccantonamento(tr, 'input[name="percentualeDelta"]')
        };
    }
    /**
     * associa l'handler gestisci accantonamenti (tabella temporanea)
     * @return (function(Node, string, any) => any) la function da utilizzare per l'associazione dell'handler
     */
    function associaHandlerInputAccantonamenti() {
        return function (nTd, sData, oData) {
            //TODO: migliorare
            if($(nTd).find('input[type="checkbox"]').length === 0){
                $(nTd).addClass('text-right');
            }
            $(nTd).find('input')
                .on('change', function() {
                    gestisciAccantonamentiSelezionati(nTd, sData, oData);
                });
        };
    }

    /**
     * Legge gli accantonamenti temporanei nel model
     * @returns (Promise) la promise legata alla lettura dei temporanei
     */
    function leggiAccantonamentiTempNelModel(){
        return $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_leggiListaTemp.do',{})
        .then(function(data) {
            capitoliInTemp = {};
            popolaTabellaTempCapitoli(data.listaAccantonamentoFondiDubbiaEsigibilitaTemp);
        });

    }

    /**
     * Popolamento della tabella dei temporanei
     * @param list (any[]) la lista dei temporanei
     */
    function popolaTabellaTempCapitoli(listaAccantonamentoFondiDubbiaEsigibilitaTemp) {
        var riscossioneVirtuosaChecked = $('#riscossioneVirtuosaTrue').is(':checked');
        var tabella = $('#tabellaTempCapitoli');
        var datatableResult;

        var colonne = [
            {aTargets: [ 0 ], mData: function(source) {
                var datum = accantonamentiSelezionati[source.capitolo.uid];
                capitoliInTemp[source.capitolo.uid] = true;
                var checked = datum ? 'checked' : '';
                return '<input type="checkbox" name="capitolo.uid" value="'+ source.capitolo.uid+'" ' + checked + '"/>';
            }, fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 1 ], mData : function(source) {
                var capitolo = source.capitolo;
                var result = '';
                var stringaUEB = ($('HIDDEN_gestioneUEB').val() && capitolo.numeroUEB)? ('/' + capitolo.numeroUEB) : '';
                if(capitolo && capitolo.annoCapitolo && capitolo.numeroCapitolo && capitolo.numeroArticolo != undefined){
                    result = capitolo.annoCapitolo + '/' + capitolo.numeroCapitolo + '/' + capitolo.numeroArticolo + stringaUEB;
                }
                return result;
            }},
            {aTargets : [ 2 ], mData : creaInputTemp('percentualeAccantonamentoFondi4', 'data-uba="4"', riscossioneVirtuosaChecked), fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 3 ], mData : creaInputTemp('percentualeAccantonamentoFondi3', 'data-uba="3"' ) ,fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 4 ], mData : creaInputTemp('percentualeAccantonamentoFondi2', 'data-uba="2"'), fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 5 ], mData : creaInputTemp('percentualeAccantonamentoFondi1', 'data-uba="1"', riscossioneVirtuosaChecked), fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 6 ], mData : creaInputTemp('percentualeAccantonamentoFondi', 'data-uba="0"' ), fnCreatedCell : associaHandlerInputAccantonamenti()},
            {aTargets : [ 7 ], mData : creaInputTemp('percentualeMediaAccantonamento', 'data-media', true ),fnCreatedCell : impostaClasseCssEHanldlerSullaCella('text-right')},
            {aTargets : [ 8 ], mData : creaInputTemp('percentualeDelta', 'data-delta' ), fnCreatedCell : associaHandlerInputAccantonamenti()}
        ];
        var opzioniTabellaTemp = {
            aaData: listaAccantonamentoFondiDubbiaEsigibilitaTemp,
            fnDrawCallback: function(oSettings) {
                gestisciCheckedSelezionaTutti(tabella);
                dataTableDrawCallback(oSettings);
            },
            aoColumnDefs: colonne
        };
        var opzioniDataTable = $.extend(true,{},baseOptionsDataTable, opzioniTabellaTemp);


        cleanDataTable(tabella);
        datatableResult = tabella.dataTable(opzioniDataTable);
        datatableResult.$('.soloNumeri').allowedChars({numeric: true});
        datatableResult.$('.decimale').gestioneDeiDecimali();
        $('#tempCapitoli').removeClass('hide');
        $('#pulsanteRiportaSuCapitoli').prop('disabled', listaAccantonamentoFondiDubbiaEsigibilitaTemp.length <= 1);
    }

    // ACCANTONAMENTI: FUNZIONALITA' DI RIEPILOGO
    /**
     * Chiama il servizio che aggiorna l'accantonamento
     * @param oData (any)  i dati
     * @param nTd   (Node) il td
     * @returns (boolean) false per indicare il blocco dell'esecuzione dell'evento
     */
    function modificaAccantonamentoFondiDubbiaEsigibilita() {
        var data = $('#fieldset_modaleAggiornamentoAccantonamentoDubbiaEsigibilita').serializeObject();
        var obj = qualify(data, 'accantonamento');

        $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_aggiornaAccantonamento.do', projectToString(obj))
            .then(function(data) {
                if(impostaDatiNegliAlert(data.errori, erroriModaleAggiornamento)){
                    return;
                }
                impostaDatiNegliAlert(['COR_INF_0006 - Operazione effettuata correttamente'],$('#INFORMAZIONI'));
                $('#riepilogoCapitoliGiaAssociati_wrapper').overlay('show');
                leggiFondiDubbiaEsigibilita();
                $('#modaleAggiornamentoAccantonamentoDubbiaEsigibilita').modal('hide');
        });

        return false;
    }

    /**
     * Chiama il servizio che elimina un certo accantonamento
     * @param td               (Node) il td su cui &eacute; stata effettuata l'invocazione
     * @param oggettoOriginale (any) l'oggetto da eliminare
     */
    function eliminaAccantonamentoFondiDubbiaEsigibilita(oggettoOriginale){
        var oggettoDaEliminare = qualify({uid: oggettoOriginale.uid}, 'accantonamento');

        $.postJSON('inserisciConfigurazioneStampaDubbiaEsigibilita_eliminaAccantonamento.do', oggettoDaEliminare)
        .then(function(data) {
            impostaDatiNegliAlert(['COR_INF_0006 - Operazione effettuata correttamente'],$('#INFORMAZIONI'));

            $('#riepilogoCapitoliGiaAssociati_wrapper').overlay('show');
            leggiFondiDubbiaEsigibilita();
            //ricaricaTabella($('#riepilogoCapitoliGiaAssociati'), 'risultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjax.do');
            $('#msgElimina').modal('hide');
        });
    }

    /**
     * Apre la modale di conferma eliminazione di un accantonamento
     * @param oggettoOriginale (any)  l'oggetto originale
     * @param td               (Node) il td su cui &eacute; stata effettuata l'invocazione
     */
    function apriModaleEliminazioneAccantonamentoFondiDubbiaEsigibilita(oggettoOriginale, td){
        var $pulsanteEliminazione = $('#pulsanteSiModaleEliminazione');
        // Clone dell'oggetto originale, per effettuare la chiamata
        var oggettoDaEliminare = $.extend(true, {}, oggettoOriginale);

        var $elementoSelezionato = $('#spanElementoSelezionatoModaleEliminazione');
        var descrizioneElementoSelezionato = calcolaStringaCapitolo(oggettoOriginale);

        $elementoSelezionato.html(descrizioneElementoSelezionato);
        $('#modaleEliminazione').modal('show');
        // Eliminazione
        $pulsanteEliminazione.substituteHandler('click', function() {
            $('#modaleEliminazione').modal('hide');
            eliminaAccantonamentoFondiDubbiaEsigibilita(oggettoDaEliminare);
        });
    }
    /**
     * Apre la modale di aggiornamento di un accantonamento
     * @param oggettoOriginale (any)  l'oggetto originale
     */
    function apriModaleAggiornamentoAccantonamentoFondiDubbiaEsigibilita(oggettoOriginale){
        var fieldset = $('#fieldset_modaleAggiornamentoAccantonamentoDubbiaEsigibilita');
        fieldset.find('input[name]')
            .toArray()
            .forEach(function(el) {
                var name = el.name;
                var value = resolve(name, oggettoOriginale);
                if(el.dataset.formatMoney !== undefined) {
                    value = doFormatMoney(value);
                }
                el.value = value;
            });
        $('#infoCapitolo_modaleAggiornamentoAccantonamentoDubbiaEsigibilita').html(calcolaStringaCapitolo(oggettoOriginale));
        calcolaMedia.bind(fieldset.find('input:first'))();
        erroriModaleAggiornamento.slideUp();
        $('#modaleAggiornamentoAccantonamentoDubbiaEsigibilita').modal('show');
    }
    function resolve(path, obj) {
        return path.split('.').reduce(function(prev, curr) {
            return prev ? prev[curr] : null;
        }, obj || self);
    }
    /**
     * Calcola la stringa del capitolo
     * @param obj (any) l'oggetto contenente il capitolo
     * @returns (string) la stringa del capitolo
     */
    function calcolaStringaCapitolo(obj) {
        var capitolo = obj.capitolo;
        var descrizioneElementoSelezionato = '';

        if(capitolo && capitolo.annoCapitolo && capitolo.numeroCapitolo && capitolo.numeroArticolo !== undefined){
            descrizioneElementoSelezionato = capitolo.annoCapitolo + '/' + capitolo.numeroCapitolo + '/' + capitolo.numeroArticolo;
            if($('HIDDEN_gestioneUEB').val() === 'true' && capitolo.numeroUEB) {
                descrizioneElementoSelezionato += '/' + capitolo.numeroUEB;
            }
        }
        return descrizioneElementoSelezionato;
    }

    /**
     * Rende visibile il pulsante di modifica dell'accantonamento solo se il valore digitato e' diverso da quello presente su db
     * @param $input (jQuery) l'input
     * @param oData  (any)    i dati
     * @param nTd    (Node)   il td
     */
    function gestisciPulsanteModifica($input, oData, nTd){
        var $tr = $(nTd).closest('tr');
        var $inputs = $tr.find('input').not(':disabled');
        var modificaAbilitata = false;
        var pulsanteModifica = $tr.find('a.modifica-accantonamento');

        $inputs.each(function(index, element){
            var $element = $(element);
            var fieldName = $element.attr('name');
            var newFieldValue = $element.val();
            var savedFieldValue = doFormatMoney(oData[fieldName]);

            if(newFieldValue !== savedFieldValue){
                //ci sono delle modifiche non salvate su db: abilito la modifica ed esco subito
                modificaAbilitata = true;
                return false;
            }
        });

        if(!modificaAbilitata){
            //il valore inserito e' lo stesso salvato su db:
            pulsanteModifica.removeClass('in');
            pulsanteModifica.off('click');
            return;
        }
        pulsanteModifica.addClass('in');
        pulsanteModifica.substituteHandler('click', modificaAccantonamentoFondiDubbiaEsigibilita);
    }
    /**
     * imposta la classe css sulla cella e associa all'input un handler per la gestione del pulsante di modifica
     * @param classeCss (string) la classe CSS da impostare
     * @returns (function(Node, string, any) => any) la funzione da utilizzare per legare le azioni
     */
    function impostaClasseCssEHanldlerSullaCella(classeCss){
        return function(nTd, sData, oData){
            var $input = $(nTd).addClass(classeCss).find('input');

            $input.on('change', function(e){
                calcolaMedia.bind(nTd, e)();
                gestisciPulsanteModifica($input, oData, nTd);
            });
        };
    }
    /**
     * imposta la classe css sulla cella
     * @param classeCss (string) la classe CSS da impostare
     * @param datas (<key, value>[]) i dati da apporre
     * @returns (function(Node, string, any) => any) la funzione da utilizzare per legare le azioni
     */
    function impostaClasseCssAndData(classeCss, datas){
        return function(nTd, sData, oData){
            var $nTd = $(nTd).addClass(classeCss);
            if(datas) {
                datas.forEach(function(el) {
                    $nTd.attr(el.key, el.value);
                });
            }
        };
    }

    /***
     * Popola la tabella di riepilogo dei capitoli
     */
    function popolaDataTableRiepilogoCapitoli() {
        var opzioniRiepilogo ={
            sAjaxSource:'risultatiRicercaAccantonamentoFondiDubbiaEsigibilitaAjax.do',
            bAutoWidth : true,
            aoColumnDefs: [

                {aTargets: [0], mData: function(source) {
                    var capitolo = source.capitolo;
                    var result = '';
                    var stringaUEB = ($('HIDDEN_gestioneUEB').val()==='true' && capitolo.numeroUEB)? ('/' + capitolo.numeroUEB) : '';
                    if(capitolo && capitolo.annoCapitolo && capitolo.numeroCapitolo && capitolo.numeroArticolo!= undefined){
                        result = capitolo.annoCapitolo + '/' + capitolo.numeroCapitolo + '/' + capitolo.numeroArticolo + stringaUEB;
                    }
                    return result;
                }},
                {aTargets: [1], mData: defaultPerDataTable('percentualeAccantonamentoFondi4', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-uba', value: 4}])},
                {aTargets: [2], mData: defaultPerDataTable('percentualeAccantonamentoFondi3', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-uba', value: 3}])},
                {aTargets: [3], mData: defaultPerDataTable('percentualeAccantonamentoFondi2', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-uba', value: 2}])},
                {aTargets: [4], mData: defaultPerDataTable('percentualeAccantonamentoFondi1', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-uba', value: 1}])},
                {aTargets: [5], mData: defaultPerDataTable('percentualeAccantonamentoFondi',  '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-uba', value: 0}])},
                {aTargets: [6], mData: defaultPerDataTable('percentualeMediaAccantonamentoFondi', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-media', value: ''}])},
                {aTargets: [7], mData: defaultPerDataTable('percentualeDelta', '', doFormatMoney), fnCreatedCell: impostaClasseCssAndData('text-right', [{key: 'data-delta', value: ''}])},
                {aTargets: [8], mData: creaPulsanteModificaAccantonamento, fnCreatedCell: bindOnLink(apriModaleAggiornamentoAccantonamentoFondiDubbiaEsigibilita)},
                {aTargets: [9], mData: creaPulsanteEliminaAccantonamento, fnCreatedCell: bindOnLink(apriModaleEliminazioneAccantonamentoFondiDubbiaEsigibilita)}
            ],
            fnCreatedRow: calcolaMediaTable,
            fnDrawCallback: dataTableDrawCallback
        };
        var opzioniDataTableRiepilogo = $.extend(true, {}, baseOptionsDataTable, opzioniPaginazioneServerSide, opzioniRiepilogo);

        cleanDataTable($('#riepilogoCapitoliGiaAssociati'));
        $('#riepilogoCapitoliGiaAssociati').dataTable(opzioniDataTableRiepilogo);
    }
    /**
     * Lega l'azione al click del pulsante
     * @param fnc (function) la funzione da legare
     * @returns (function(Node, string, any) => any) la funzione da utilizzare per legare le azioni
     */
    function bindOnLink(fnc) {
        return function(nTd, sData, oData) {
            $(nTd).addClass('text-center')
            .find('a')
            .eventPreventDefault('click', fnc.bind(undefined, oData, nTd));
        };
    }
    /**
     * Creazione del pulsante di modifica accantonamento
     * @param source (any) l'oggetto da cui ricavare i dati
     * @returns (string) l'HTML del pulsante
     */
    function creaPulsanteModificaAccantonamento(source) {
        return '<a data-accantonamento-uid="' + source.uid + '" href="" title="modifica le percentuali" role="button" class="modifica-accantonamento" data-toggle="modal">' +
            '<i class="icon-pencil icon-2x"><span class="nascosto">modifica</span></i>' +
            '</a>';
    }
    /**
     * Creazione del pulsante di eliminazione accantonamento
     * @param source (any) l'oggetto da cui ricavare i dati
     * @returns (string) l'HTML del pulsante
     */
    function creaPulsanteEliminaAccantonamento(source) {
        return '<a data-accantonamento-uid="' + source.uid + '" href="#msgElimina" title="elimina" role="button" data-toggle="modal">' +
            '<i class="icon-trash icon-2x"><span class="nascosto">elimina</span></i>' +
            '</a>';
    }

    /**
     * Callback al termine della visualizzazione della tabella
     * @param oSettings (any) i settings della tabella
     */
    function dataTableDrawCallback(oSettings) {
        $('.soloNumeri', oSettings.nTable).allowedChars({numeric: true});
        $('.decimale', oSettings.nTable).gestioneDeiDecimali();
    }

    /**
     * Popolamento dall'anno precedente
     */
    function popolaDaAnnoPrecedente() {
        var $button = $('button[data-copia-precedente]').overlay('show');
        var async = new Async('inserisciConfigurazioneStampaDubbiaEsigibilita_popolaDaAnnoPrecedente.do', {}, popolaDaAnnoPrecedenteAsyncCallback($button),
                'async/polling.do', popolaDaAnnoPrecedentePolling);
        async.execute()
            .fail(popolaDaAnnoPrecedenteFailCallback);
    }
    
    /**
     * Popolamento dall'anno precedente gestione
     */
    function popolaDaAnnoPrecedenteGestione() {
        var $button = $('button[data-copia-precedente]').overlay('show');
        var async = new Async('inserisciConfigurazioneStampaDubbiaEsigibilita_popolaDaAnnoPrecedenteGestione.do', {}, popolaDaAnnoPrecedenteAsyncCallback($button),
                'async/polling.do', popolaDaAnnoPrecedentePolling);
        async.execute()
            .fail(popolaDaAnnoPrecedenteFailCallback);
    }

    /**
     * Callback di fallimento nel popolamento da anno precedente
     * @param error (any) gli errori
     * @returns (Promise) una promise rigettata
     */
    function popolaDaAnnoPrecedenteFailCallback(error) {
        var jserror = error.errori !== undefined && error.errori.filter(function(el) {
            return el instanceof Error;
        })[0];
        if(jserror) {
            throw jserror;
        }
        impostaDatiNegliAlert(error.errori, alertErrori);
        return $.Deferred().reject(error).promise();
    }

    /**
     * Callback del popolamento da anno precedente
     * @param $button (jQuery) il pulsante
     * @returns (function(any) => any) la funzione da invocare come callback dell'esecuzione asincrona
     */
    function popolaDaAnnoPrecedenteAsyncCallback($button) {
        return function(data) {
            $button.overlay('hide');
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                throw Error('Errore nell\'invocazione del servizio');
            }
            // Disabilito il pulsante
            $button.attr('disabled', 'disabled');
            return {idOperazioneAsync: data.idOperazioneAsincrona};
        };
    }

    /**
     * Callback di polling del popolamento da anno precedente
     * @param data (any) i dati dell'invocazione
     * @returns boolean se proseguire con il popolamento
     */
    function popolaDaAnnoPrecedentePolling(data) {
        data.stato && impostaDatiNegliAlert([data.messaggio], alertInformazioni);
        return Async.checkConclusaOErrore(data.stato);
    }

    /**
     * Completamento del polling
     */
    function popolaDaAnnoPrecedentePollingDone() {
        $('button[data-copia-precedente]').removeAttr('disabled');
        leggiFondiDubbiaEsigibilita();
    }
    /**
     * Timeout del polling
     */
    function popolaDaAnnoPrecedentePollingTimeout() {
        impostaDatiNegliAlert(['Il risultato dell\'invocazione non &eacute; disponibile al termine del timeout del polling. Sar&agrave; disponibile in un prossimo caricamento della pagina'], alertInformazioni);
    }

    /**
     * Inizializzazione
     */
    function init() {
        $('#pulsanteApriModaleCapitoloDubbiaEsigibilita').on('click', apriModaleCapitoloDubbiaEsigibilita);

        $('#titoloEntrata').on('change', CapitoloEntrata.caricaTipologia);
        $('#tipologiaTitolo').on('change', CapitoloEntrata.caricaCategoria);
        $('#categoriaTipologiaTitolo').on('change', function() {
            $('#bottonePdC').attr('href','#PDCfin');
            Capitolo.caricaPianoDeiConti(this, true);
        });
        $('#bottonePdC').on('click', Capitolo.controllaPdC);

        $('#pulsanteRicercaCapitolo').substituteHandler('click', ricercaCapitoliModale);

        $('#pulsanteConfermaCapitoli').substituteHandler('click', confermaCapitoliModale);
        $('#pulsanteRiportaSuCapitoli').substituteHandler('click', riportaSuCapitoli);
        $('#salvaCapitoli').substituteHandler('click', salvaCapitoli);

        // Carica lo zTree relativo alla Struttura Amministrativo Contabile
        Capitolo.caricaStrutturaAmministrativoContabile();

        leggiFondiDubbiaEsigibilita();
        leggiAccantonamentiTempNelModel();

        // Creo la funzionalita' di selezione multipla
        $('.check-all').change(checkAllInTable);

        $('#tabellaTempCapitoli').on('change', 'input[data-uba]', calcolaMedia);
        $('#fieldset_modaleAggiornamentoAccantonamentoDubbiaEsigibilita').on('change', 'input[data-uba]', calcolaMedia);
        // SIAC-4469
        $('#pulsantePopolaDaAnnoPrecedente').removeAttr('disabled').substituteHandler('click', popolaDaAnnoPrecedente);
        $('#pulsantePopolaDaAnnoPrecedenteGestione').removeAttr('disabled').substituteHandler('click', popolaDaAnnoPrecedenteGestione);
        $('#pulsanteConferma_modaleAggiornamentoAccantonamentoDubbiaEsigibilita').substituteHandler('click', modificaAccantonamentoFondiDubbiaEsigibilita);
        $(document).substituteHandler('pollingDone.async', popolaDaAnnoPrecedentePollingDone)
            .substituteHandler('pollingTimeout.async', popolaDaAnnoPrecedentePollingTimeout);
    }

    $(init);
})(jQuery);
