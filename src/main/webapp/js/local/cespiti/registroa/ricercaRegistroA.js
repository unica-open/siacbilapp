/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global) {
    'use strict';
    var alertErrori = $('#ERRORI');
    var mapTipoElencoEntrataSpesa = {E: 'entrata', S: 'spesa'};
    var mapDataCapitoloTipo = {
        spesa: {
            tipo: 'UscitaGestione',
            suffix: '_cug'
        },
        entrata: {
            tipo: 'EntrataGestione',
            suffix: '_ceg'
        }
    };
    var ambito = $('#ambito').val();
    var capId = -1;
    $(init);
    global.preSubmit = preSubmit;
    
    function init() {
        // Funzionalita' legate in testa
        var $radioTipoCausale = $('input[type="radio"][name="primaNota.tipoCausale"]');
        var $radioTipoElenco = $('input[type="radio"][name="tipoElenco"]');
        var $selectTipoEventoIntegrata = $('#INT_tipoEvento');
        
        $radioTipoCausale.substituteHandler('change', handleChangeTipoCausale)
        $radioTipoCausale.filter(':checked').trigger('change');
        
        // Prima nota libera
        $('#LIB_evento').substituteHandler('change', ricaricaCausaliPerEvento).change();
        Conto.inizializza(undefined, undefined, '#LIB_contoCodice', '#LIB_descrizioneConto', '#LIB_pulsanteCompilazioneGuidataConto');
        
        // Prima nota integrata
        $radioTipoElenco.change(filtraTipoEvento.bind(undefined, false));
        $selectTipoEventoIntegrata.change(changeTipoEvento);
        Conto.inizializza(undefined, undefined, '#INT_contoCodice', '#INT_descrizioneConto', '#INT_pulsanteCompilazioneGuidataConto');
        ContoFIN.inizializza('#INT_registrazioneMovFinElementoPianoDeiContiAggiornatoConto', '#INT_pulsanteCompilazioneGuidataContoFinanziario');
        Provvedimento.inizializzazione('', '#annoProvvedimento', '#numeroProvvedimento', '#tipoAttoProvvedimento', '#treeStruttAmm',
            '#HIDDEN_StrutturaAmministrativoContabileUid', '#HIDDEN_statoProvvedimento', '#SPAN_InformazioniProvvedimento');
        $('.inputProvvedimento').change(handleChangeProvvedimento);
        $('#codiceSoggetto').change(handleChangeCodiceSoggetto);
        
        // Onload
        caricaListaClasse();
        filtraTipoEvento(false)
            .then(changeTipoEvento);
        $(document).one('sacCaricata', caricaStrutturaAmministrativoContabile);
        $('button[data-uncheck-ztree]').substituteHandler('click', uncheckZTree);
    }
    /**
     * Gestione del submit del form
     * @param e (Event) l'evento scatenante
     * @returns (boolean) se il submit puo' proseguire
     */
    function preSubmit(e) {
        var $tipoCausale = $('input[type="radio"][name="primaNota.tipoCausale"]').filter(':checked');
        var primaNotaSelector;
        if(!$tipoCausale.length) {
            e.preventDefault();
            return false;
        }
        primaNotaSelector = $tipoCausale.data('pnSelector');
        // Remove from DOM the block we did not use (to prevent Struts2 errors)
        $('div[data-pn]').not('[data-pn="' + primaNotaSelector + '"]').remove();
        return true;
    }
    /**
     * Gestione del change per il tipo causale
     * @param e (Event) l'evento scatenante
     */
    function handleChangeTipoCausale(e) {
        var $target = $(e.target);
        var primaNotaSelector = $target.data('pnSelector');
        var dataAttribute = '[data-pn="' + primaNotaSelector + '"]';
        var $dataPn = $('div[data-pn]');
        // Close different
        $dataPn.not(dataAttribute).slideUp();
        // Open same
        $dataPn.filter(dataAttribute).slideDown();
    }
    // PRIMA NOTA LIBERA
    /**
     * Ricarica le causali
     */
    function ricaricaCausaliPerEvento(e){
        var $selectEvento = $(e.target);
        var $selectCausaleEP = $('#LIB_causaleEP').overlay('show');
        var evento = $selectEvento.val();
        var objToSend = {'evento.uid': evento, 'ambito': ambito};
        // Chiamata AJAX
        $.postJSON('ricercaCausaleEPByEvento_ricercaModulare.do', objToSend)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Svuoto la select ed esco
                caricaSelectCodifiche($selectCausaleEP, []);
                return;
            }
            caricaSelectCodifiche($selectCausaleEP, data.listaCausaleEP || []);
        }).always($selectCausaleEP.overlay.bind($selectCausaleEP, 'hide'));
    }
    // PRIMA NOTA INTEGRATA
    /**
     * Filtra i tipi evento sulla base del tipo di elenco (Entrata/Spesa)
     */
    function filtraTipoEvento(keepOldValues) {
        var tipoElenco = $('input[type="radio"][name="tipoElenco"]:checked').val();
        var obj = {};
        var $select = $('#INT_tipoEvento');
        var oldValue = $select.val();
        
        if(tipoElenco) {
            obj[mapTipoElencoEntrataSpesa[tipoElenco]] = true;
        }
        obj.tipoCausale = 'Integrata';
        obj.filtroTipiEventoCogeInv = true;

        // Scateno l'overlay
        $select.overlay('show');
        return $.postJSON('ricercaTipoEventoByEntrataSpesa.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Non ho errori: popolo la select
            caricaSelect($select, data.listaTipoEvento, true, setDataAttributesTipoEvento, keepOldValues ? oldValue : undefined);
            // Lancio l'evento di change
            if(!keepOldValues) {
                $select.change();
            }
        }).always($select.overlay.bind($select, 'hide'));
    }
    /**
     * Caricamento della select tramite la lista fornita.
     * @param select           (jQuery)   la select da popolare
     * @param list             (Array)    la lista tramite cui popolare la select
     * @param addEmptyOption   (Boolean)  se aggiungere una option vuota (optional - default - false)
     * @param operation        (Function) l'operazione da operare sull'elemento per comporre dati aggiuntivi sulla option (optional - default - $.noop)
     * @param valueToSelect    (number)   il valore da selezionare (optional - default - undefined)
     * @return (jQuery) la select su cui si e' agito
     */
    function caricaSelect($select, list, addEmptyOption, operation, valueToSelect) {
        var defaultOperation = operation && typeof operation === 'function' ? operation : $.noop;
        var initialValue = !!addEmptyOption
            ? '<option value=""></option>'
            : '';
        var str = list.reduce(function(acc, val) {
            var tmp = '<option value="' + val.uid + '"';
            tmp += defaultOperation(val);
            // Forzo il cast a numero per sicurezza
            if(+valueToSelect === +val.uid) {
                tmp += ' selected';
            }
            tmp += '>' + val.codice + ' - ' + val.descrizione + '</option>';
            return acc + tmp;
        }, initialValue);
        return $select.html(str);
    }
    /**
     * Aggiunge il data-attribute relativo all'entrata/spesa
     * @param tipoEvento (object) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributeEntrataSpesa(tipoEvento) {
        return tipoEvento.tipoSpesa
            ? 'data-tipo="spesa"'
            : tipoEvento.tipoEntrata
                ? 'data-tipo="entrata"'
                : '';
    }
    /**
     * Aggiunge il data-attribute relativo al documento
     * @param tipoEvento (object) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributeDocumento(tipoEvento) {
        return tipoEvento.codice === 'DE' || tipoEvento.codice === 'DS' ? 'data-documento="true"' : '';
    }
    /**
     * Aggiunge il data-attribute relativo alla liquidazione
     * @param tipoEvento (object) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributeLiquidazione(tipoEvento) {
        return tipoEvento.codice === 'L' ? 'data-liquidazione="true"' : '';
    }
    /**
     * Aggiunge i data attributes del tipo evento
     * @param tipoEvento (any) il tipo di evento
     * @return (string) la stringa da aggiungere nella option se necessario
     */
    function setDataAttributesTipoEvento(tipoEvento) {
        var tmp = [];
        if(!tipoEvento) {
            return '';
        }
        tmp.push(setDataAttributeDocumento(tipoEvento));
        tmp.push(setDataAttributeLiquidazione(tipoEvento));
        tmp.push(setDataAttributeEntrataSpesa(tipoEvento));
        return ' ' + tmp.join(' ');
    }
    /**
     * Gestione del change del tipo evento
     */
    function changeTipoEvento() {
        var $selectedOption = $('option:selected', '#INT_tipoEvento');
        preselezionaDatiDerivatiTipoElenco($selectedOption);
        handleSelectedOption($selectedOption);
    }
    /**
     * Preselezione dei dati derivati per il tipo di elenco
     * @param $selectedOption (jQuery) la option selezionata
     */
    function preselezionaDatiDerivatiTipoElenco($selectedOption) {
        var tipoDocumento = !!$selectedOption.data('documento');
        var tipoEntrataSpesa = $selectedOption.data('tipo');
        var $capitoloContainer = $('#capitoloContainer');
        var isRateoRisconto = $selectedOption.data('rateo-risconto');
        $('#asteriskDataRegistrazione')[$selectedOption.data('documento') ? 'show' : 'hide']();
        $('#codiceSoggetto, #annoProvvedimento, #numeroProvvedimento, #tipoAttoProvvedimento, #pulsanteDelesezionaStrutturaAmministrativoContabile')
            [$selectedOption.val() === '' ? 'prop' : 'removeProp']('disabled', true);
        // SIAC-4644, modifica
        $('#INT_importoDocumentoDa, #INT_importoDocumentoA')[tipoDocumento ? 'removeProp' : 'prop']('disabled', true);
        $('#INT_containerImportoDocumento')[tipoDocumento ? 'slideDown' : 'slideUp']();
        $('#pulsanteApriModaleProvvedimento, #pulsanteApriModaleSoggetto')[$selectedOption.val() === '' ? 'addClass' : 'removeClass']('hide');
        
        // SIAC-5292
        Capitolo.destroy(capId);
        if(!tipoEntrataSpesa || isRateoRisconto) {
            $capitoloContainer.slideUp();
            $capitoloContainer.find('input').not('[data-maintain]').val('');
            $('#containerAccertamento, #containerImpegno').slideUp();
            return;
        }
        $capitoloContainer.slideDown();
        capId = Capitolo.inizializza(
            mapDataCapitoloTipo[tipoEntrataSpesa].tipo,
            '#annoCapitolo',
            '#numeroCapitolo',
            '#numeroArticolo',
            '#numeroUEB',
            '#datiRiferimentoCapitoloSpan',
            '#pulsanteApriCompilazioneGuidataCapitolo',
            mapDataCapitoloTipo[tipoEntrataSpesa].suffix);

        //SIAC-5799
        impostaFiltroMovimentoGestione($selectedOption);
    }
    /**
     * Impostazione del filtro per il movimento di gestione
     * @param $selectedOption la option selezionata
     */
    function impostaFiltroMovimentoGestione($selectedOption){
        var isSpesa = $selectedOption.data('tipo') === 'spesa';
        var isDocumento = $selectedOption.data('documento');
        var isLiquidazione = $selectedOption.data('liquidazione');
        var $containerAccertamento = $('#containerAccertamento').slideUp();
        var $containerImpegno = $('#containerImpegno').slideUp();

        Accertamento.destroy();
        Impegno.destroy();
        
        $containerAccertamento.find('input').val('').removeAttr('readonly');
        $containerImpegno.find('input').val('').removeAttr('readonly');
        
        if(!isDocumento && !isLiquidazione){
            return;
        }
        if(!isSpesa){
            $containerAccertamento.slideDown();
            Accertamento.inizializza('#annoAccertamento', '#numeroAccertamento', '#numeroSubAccertamento', 'SPAN_accertamentoH4', undefined, undefined, '#pulsanteAperturaCompilazioneGuidataAccertamento');
            return;
        }
        $containerImpegno.slideDown();
        Impegno.inizializza('#annoImpegno', '#numeroImpegno', '#numeroSubimpegno', '#SPAN_impegnoH4', undefined, undefined, undefined, undefined, '#pulsanteAperturaCompilazioneGuidataImpegno', undefined);
    }
    /**
     * Gestione della option selezionata
     * @param $opt (jQuery) la option selezionata
     */
    function handleSelectedOption($opt) {
        var txt = $opt.text();
        var $fieldMovimento = $('#INT_annoMovimento, #INT_numeroMovimento, #INT_numeroSubmovimento');
        disableProvvedimento(/^RS -/.test(txt) || /^RT -/.test(txt));
        // SIAC-6502
        $fieldMovimento[$opt.val() === '' ? 'attr' : 'removeAttr']('disabled', true);
        if($opt.val() === '') {
            $fieldMovimento.val('');
        }
    }
    /**
     * Disabilitazione del provvedimento
     * @param disabled (boolean) se disabilitare il campo
     */
    function disableProvvedimento(disabled) {
        var $container = $('#provvedimentoContainer');
        var $input = $('input, select', $container);
        
        $container[disabled ? 'slideUp' : 'slideDown']();
        
        return $input[disabled ? 'attr' : 'removeAttr']('disabled', 'disabled');
    }
    function handleChangeProvvedimento() {
        var $selectedOption = $('option:selected', '#INT_tipoEvento');
        var notEmptyProvv = false;
        if ($selectedOption.data('documento')){
            $('.inputProvvedimento').each(function() {
                notEmptyProvv |= $(this).val().length > 0;
            });
            $('#asteriskDataRegistrazione').toggle(! notEmptyProvv);
        }
    }
    /**
     * Gestione del change per il codice soggetto
     * @returns (Promise) la promise relativa al caricamento del soggetto
     */
    function handleChangeCodiceSoggetto() {
        var $campoCodiceSoggetto = $('#codiceSoggetto');
        var $accordionSedeSecondaria = $();
        var $accordionModalitaPagamento = $();
        var $spanDescrizione = $('#descrizioneCompletaSoggetto');
        return Soggetto.caricaDettaglioSoggetto($campoCodiceSoggetto, $accordionSedeSecondaria, $accordionModalitaPagamento, $spanDescrizione, undefined, undefined, undefined, true);
    }
    /**
     * Caricamento delle liste della classe.
     */
    function caricaListaClasse() {
        // classePianoDeiConti_modale
        var $select = $('#classePianoDeiConti_modale');
        // Se sono gia' popolate, non ricarico
        if(selectHasOptions($select, 1)) {
            return;
        }
        $select.overlay('show');
        $.postJSON('ricercaPrimaNotaIntegrataFIN_caricaListaClassi.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            // Caricamento della select
            caricaSelect($select, data.listaClassi, true);
        }).always($select.overlay.bind($select, 'hide'));
    }
    /**
     * Controlla se la select ha piu' di un dato numero di opzioni.
     *
     * @param $select   (jQuery) la select da controllare
     * @param threshold (Number) la soglia da superare
     * @returns true se la select ha pi' du threshold opzioni; false altrimenti
     */
    function selectHasOptions($select, threshold) {
        return $select.find('option').length > threshold;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Caricamento della SAC
     */
    function caricaStrutturaAmministrativoContabile(e){
        var listaSAC = e.listaStrutturaAmministrativoContabile;
        var idSuffix = 'Cap';
        ZTreeDocumento.imposta('treeStruttAmm' + idSuffix, ZTreeDocumento.SettingsBase, listaSAC, 'HIDDEN_StrutturaAmministrativoContabile' + idSuffix + 'Uid', idSuffix);
    }
    /**
     * Deselezione dello ztree
     */
    function uncheckZTree() {
        var zTreeId = $(this).data('uncheckZtree');
        var tree = $.fn.zTree.getZTreeObj(zTreeId);
        var nodo = tree && tree.getCheckedNodes(true)[0];
        if(nodo) {
            tree.checkNode(nodo, false, true, true);
        }
    }
}(this);