/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function($, global) {
    'use strict';
    
    var oldSlideUp = $.fn.slideUp;
    $.fn.slideUp = function() {
    	try {throw new Error('wawa')} catch(e) {console.error(this, arguments, e)}
    	return oldSlideUp.apply(this, arguments);
    }

    var selectedDatas = {};
    var $alertErrori = $('#ERRORI');
    var $form = $('#riepilogoCompletaDefinisciForm');
    var table = $('#riepilogoRisultatiRicercaPreDocumentoCompletaDefinisci');
    var $sacUid = $('#HIDDEN_StrutturaAmministrativoContabileUidAttoAmministrativo');

    function formatMoney(val) {
        if(typeof val === 'number') {
            return val.formatMoney();
        }
        return val;
    }

    function caricaTotali() {
        var $fieldsetRicerca = $('#fieldsetRicercaPredisposizione');
        var obj = $fieldsetRicerca.serializeObject();
        var $divTotali = $('#totaliElencoPredocumenti').overlay('show');
        //var $overlayDivs = $('#fieldsetRicercaPredisposizione, #totaliElencoPredocumenti');
        $divTotali.find(':input').val('');
     
        return $.postJSON('riepilogoCompletaDefinisciPreDocumentoEntrataAction_cercaTotaliForRiepilogo.do', obj)
            .then(function(data) {
               if(impostaDatiNegliAlert(data.errori, $alertErrori, undefined, false)) {
                   return $.Deferred().reject().promise();
               }
               // Impostazione dati nei campi
               $('#numeroPredisposizioniTotale').val(data.numeroPreDocumentiTotale);
               $('#numeroPredisposizioniIncomplete').val(data.numeroPreDocumentiIncompleti);
               $('#numeroPredisposizioniAnnullateDefinite').val(data.numeroPreDocumentiAnnullatiDefiniti);
               $('#numeroPredisposizioniComplete').val(data.numeroPreDocumentiCompleti);
               
               $('#importoPredisposizioniTotale').val(formatMoney(data.importoPreDocumentiTotale));
               $('#importoPredisposizioniIncomplete').val(formatMoney(data.importoPreDocumentiIncompleti));
               $('#importoPredisposizioniAnnullateDefinite').val(formatMoney(data.importoPreDocumentiAnnullatiDefiniti));
               $('#importoPredisposizioniComplete').val(formatMoney(data.importoPreDocumentiCompleti));

               //SIAC-6780
               $('#numeroPredisposizioniNoCassaTotale').val(data.numeroPreDocumentiNoCassaTotale);
               $('#numeroPredisposizioniNoCassaIncomplete').val(data.numeroPreDocumentiNoCassaIncompleti);
               $('#numeroPredisposizioniNoCassaAnnullateDefinite').val(data.numeroPreDocumentiNoCassaAnnullatiDefiniti);
               $('#numeroPredisposizioniNoCassaComplete').val(data.numeroPreDocumentiNoCassaCompleti);
               
               $('#importoPredisposizioniNoCassaTotale').val(formatMoney(data.importoPreDocumentiNoCassaTotale));
               $('#importoPredisposizioniNoCassaIncomplete').val(formatMoney(data.importoPreDocumentiNoCassaIncompleti));
               $('#importoPredisposizioniNoCassaAnnullateDefinite').val(formatMoney(data.importoPreDocumentiNoCassaAnnullatiDefiniti));
               $('#importoPredisposizioniNoCassaComplete').val(formatMoney(data.importoPreDocumentiNoCassaCompleti));
               //
           }).always($divTotali.overlay.bind($divTotali, 'hide'));
    }

    /**
     * Caricamento via Ajax della tabella dei preDocumenti e visualizzazione della stessa.
     */
    function visualizzaTabellaDocumenti() {
        
        var options = {
            bServerSide: true,
            sAjaxSource: 'riepilogoCompletaDefinisciPreDocumentoEntrataAjax.do',
            sServerMethod: 'POST',
            bPaginate: true,
            bLengthChange: true,
            iDisplayLength: 10,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            sScrollY: '300px',
            bScrollCollapse: true,
            oLanguage: {
                sInfo: '_START_ - _END_ di _MAX_ risultati',
                sInfoEmpty: '0 risultati',
                sProcessing: 'Attendere prego...',
                sZeroRecords: 'Non sono presenti risultati di ricerca secondo i parametri inseriti',
                oPaginate: {
                    sFirst: 'inizio',
                    sLast: 'fine',
                    sNext: 'succ.',
                    sPrevious: 'prec.',
                    sEmptyTable: 'Nessun dato disponibile'
                }
            },
            fnPreDrawCallback: function () {
                // Mostro il div del processing
                $('#risultatiRicercaPreDocumento_processing').parent('div')
                    .show();
            },
            // Chiamata al termine della creazione della tabella
            fnDrawCallback: function (settings) {
                var records = settings.fnRecordsTotal();
                var testo = (records === 0 || records > 1) ? (records + ' Risultati trovati') : ('1 Risultato trovato');
                $('#id_num_result').html(testo);
                // Nascondo il div del processing
                $('#risultatiRicercaPreDocumento_processing').parent('div')
                    .hide();
                // Attivo i popover
                table.find('a[rel="popover"]')
                    .popover()
                    .eventPreventDefault('click');
                handleSelezionaTutti(settings);
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source) {
                    var res = '<input type="checkbox" style="display:none;" name="uidPreDocumento" value="' + source.uid + '"';
                    if(selectedDatas[+source.uid] && selectedDatas[+source.uid].isSelected) {
                        res += ' checked';
                    }
                    return res + '/>';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalPreDocumento', oData)
                        .substituteHandler('change', handlePredocCheckboxChange.bind(undefined, nTd, oData));
                }},
                {aTargets: [1], mData: function(source) {
                    return source.descrizione
                        ? '<a data-original-title="Causale estesa" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.descrizione) + '" href="#">' + source.numero + '</a>'
                        : source.numero;
                }},
                {aTargets: [2], mData: function(source) {
                    var sac = source.strutturaAmministrativoContabile;
                    return sac
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(sac.descrizione) + '">' + sac.codice + '</a>'
                        : '';
                }},
                {aTargets: [3], mData: function(source) {
                    var causale = source.causale;
                    var tipoCausale = causale && causale.tipoCausale;
                    return causale
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(causale.descrizione) + '">' + (tipoCausale ? tipoCausale.codice + ' - ' : '') + causale.codice + '</a>'
                        : '';
                }},
                {aTargets: [4], mData: function(source) {
                    return source.contoCorrente
                        ? '<a data-original-title="Descrizione" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.contoCorrente.descrizione) + '">' + source.contoCorrente.codice + '</a>'
                        : '';
                }},
                {aTargets: [5], mData: defaultPerDataTable('dataCompetenza', '', formatDate)},
                {aTargets: [6], mData: function(source) {
                    return source.statoOperativoDocumento
                        ? '<a data-original-title="Documento" href="#" data-trigger="hover" rel="popover" data-content="' + escapeHtml(source.documento) + '">' + source.statoOperativoDocumento.codice + '</a>'
                        : '';
                }},
                {aTargets: [7], mData: defaultPerDataTable('ragioneSocialeCognomeNome')},
                {aTargets: [8], mData: defaultPerDataTable('soggetto')},
                {aTargets: [9], mData: defaultPerDataTable('statoOperativoPreDocumento.codice')},
                {aTargets: [10], mData: defaultPerDataTable('dataDocumento', '', formatDate)},
                {aTargets: [11], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: function(nTd) {
                    $(nTd).addClass('tab_Right');
                }},
                {aTargets: [12], mData: 'ragioneSocialeCognomeNome', bVisible: false
                // fnCreatedCell: function(nTd, sData, oData) {
                //     // $('.annullaPreDocumento', nTd).eventPreventDefault('click', clickOnAnnulla.bind(undefined, oData));
                // }
            }
            ]
        };

        var startPos = parseInt($('#HIDDEN_startPosition').val(), 10);
        if(!isNaN(startPos)) {
            $.extend(true, options, {'iDisplayStart' : startPos});
        }

        table.dataTable(options);
    }

    /**
     * Handling della variazione del checkbox
     * @param nTd    (Node) il td
     * @param predoc (any)  il predoc
     */
    function handlePredocCheckboxChange(nTd, predoc) {
        var input = $('input', nTd);
        selectedDatas[+input.val()] = {isSelected: input.prop('checked'), data: predoc};
    }

    /**
     * Gestione della selezione di tutti gli elementi. Preseleziona il checkbox all'apertura della pagina
     * @param settings (any) i settings del dataTable
     */
    function handleSelezionaTutti(settings) {
        var inputNonChecked = $('input', settings.nTBody).not(':checked');
        $('#checkboxSelezionaTutti').prop('checked', inputNonChecked.length === 0);
    }

    /**
     * Gestione del click sul pulsante annulla.
     *
     * @param preDoc (Object) il preDocumento da annullare
     */
    function clickOnAnnulla(preDoc) {
        $('#HIDDEN_UidDaAnnullare').val(preDoc.uid);
        $('#elementoSelezionatoAnnullamento').html(preDoc.numero);
        $('#modaleAnnullaPreDocumento');
    }
    
    function caricaTabellaPredocumenti(){
        var url='riepilogoCompletaDefinisciPreDocumentoEntrata_caricaPredocumentiSelezionati.do';
        return $.postJSON(url,{}).then(function(data){
            if(impostaDatiNegliAlert(data.errori, $('#ERRORI'), undefined, false)){
                return;
            }
            $('#importoTotaleSpan').html(formatMoney(data.importoTotale));
            visualizzaTabellaDocumenti();
        }).always($.noop);
    }

    function validateForm(){

        var b = accertamentoAssente() && soggettoAssente();
        if(b){
            $alertErrori.find('li').remove();
            $alertErrori.append( "<li>COR_ERR_0002 - Dato obbligatorio omesso: Inserire almeno un soggetto o un accertamento</li>" );
            $alertErrori.show();
            return;
        }

        changeFormAction();
    }

    function accertamentoAssente() {
        var $numeroMovimentoGestione = $('#numeroMovimentoGestione').val();
        var $numeroSubMovimentoGestione = $('#numeroSubMovimentoGestione').val();
        var $annoMovimentoMovimentoGestione = $('#annoMovimentoMovimentoGestione').val();

        return ($numeroMovimentoGestione === undefined || $numeroMovimentoGestione === '')
        || ($numeroSubMovimentoGestione === undefined || $numeroSubMovimentoGestione === '')
        || ($annoMovimentoMovimentoGestione === undefined || $annoMovimentoMovimentoGestione === '');
    }

    function soggettoAssente() {
        var $codiceSoggetto = $('#codiceSoggetto').val();
        return ($codiceSoggetto === undefined || $codiceSoggetto === '');
    }

    function changeFormAction(){
        $form.attr('action','riepilogoCompletaDefinisciPreDocumentoEntrataAction_completaDefinisci.do');
        $form.submit();
    }
    
    /**
     * Caricamento delle SAC
     * @param e (Event) l'evento scatenante
     */
    function caricaSAC(e) {
        PreDocumento.caricaStruttureAmministrativoContabili(e);
        Ztree.selezionaNodoSeApplicabile('treeStruttAmmAttoAmministrativo', $sacUid.val());
    }

    $(function() {
    	var $document = $(document);
    	var idProvvedimento;
        var idSoggetto;
    	
    	caricaTabellaPredocumenti();
        caricaTotali();
        
        $('#tipoCausale').substituteHandler('change', PreDocumento.caricaListaCausaleEntrata);

        $document.substituteHandler('struttureAmministrativeCaricate', caricaSAC);

        // Impegno
        $('#pulsanteCompilazioneGuidataMovimentoGestione').substituteHandler('click', PreDocumento.apriModaleAccertamento);
        Accertamento.inizializza();

        // Soggetto
        idSoggetto = Soggetto.inizializza('#codiceSoggettoSoggetto', '#HIDDEN_codiceFiscaleSoggetto', '#HIDDEN_denominazioneSoggetto', '#datiRiferimentoSoggettoSpan', '#pulsanteCompilazioneSoggetto');
        Soggetto.bindCaricaDettaglioSoggetto(idSoggetto, false);

        // Atto Amministrativo
        idProvvedimento = Provvedimento.inizializzazione(Ztree, {}, 'AttoAmministrativo');
        Provvedimento.bindApriModaleAttoAmministrativo(idProvvedimento);

        // Provvisorio
        $('#HIDDEN_TipoProvvisorioCassa').val('E');
        ProvvisorioDiCassa.inizializzazione('#pulsanteCompilazioneGuidataProvvisorioCassa','#HIDDEN_TipoProvvisorioCassa','#annoProvvisorioCassa','#numeroProvvisorioCassa','#HIDDEN_CausaleProvvisorioCassa');

        // Workaround per Struts2
        if($sacUid.val() === '') {
            $sacUid.val(0);
        }

        //SIAC-6780
        $('#completaDefinisciSubmit').substituteHandler('click', changeFormAction);

        $('form').on('reset', PreDocumento.puliziaReset);

        $('#riepilogoCompletaDefinisciSubmit').substituteHandler('click', function(event){
            event.preventDefault();
            validateForm();
        });

    });
    
})(jQuery, this);
