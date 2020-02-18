/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    'use strict';

    var exports = {};
    var counter = 0;
    var instances = {};
    var nomeAzioneDecentrata = $('#nomeAzioneDecentrata').val();

    var Provvedimento = function(ztree, ztreeSettings, suffix) {
        this.suffix = suffix || '';

        this.ztreeConstructor = ztree;
        this.ztree = new ztree(this.suffix, 'treeStruttAmm', ztreeSettings);
        this.ztreeModale = new ztree(this.suffix + '_Modale', 'treeStruttAmm', ztreeSettings);

        this.$alertErrori = $('#ERRORI_MODALE_PROVVEDIMENTO' + this.suffix);
        this.$hiddenUid = $('#HIDDEN_uidProvvedimento' + this.suffix);
        this.$table = $('#risultatiRicercaProvvedimento' + this.suffix);

        this.$divTabella =  $('#divContenitoreTabellaProvvedimento' + this.suffix);
        this.$formDiRicerca = $('#fieldsetRicercaGuidateProvvedimento' + this.suffix);

        this.$annoProvvedimento = $('#annoProvvedimento' + this.suffix);
        this.$numeroProvvedimento = $('#numeroProvvedimento' + this.suffix);
        this.$tipoProvvedimento = $('#tipoAtto' + this.suffix);
        this.$statoProvvedimento = $('#statoOperativoProvvedimento' + this.suffix);
        this.$strutturaAmministrativoContabileUid = $('#HIDDEN_StrutturaAmministrativoContabileUid' + this.suffix);
        this.$strutturaAmministrativoContabileCodice = $('#HIDDEN_StrutturaAmministrativoContabileCodice' + this.suffix);
        this.$strutturaAmministrativoContabileDesc = $('#HIDDEN_StrutturaAmministrativoContabileDescrizione' + this.suffix);

        this.$annoProvvedimentoModale = $('#annoProvvedimento_modale' + this.suffix);
        this.$numeroProvvedimentoModale = $('#numeroProvvedimento_modale' + this.suffix);
        this.$tipoProvvedimentoModale = $('#tipoAttoProvvedimento_modale' + this.suffix);
        this.$oggettoProvvedimentoModale = $('#oggettoProvvedimento_modale' + this.suffix);
        this.$strutturaAmministrativoContabileUidModale = $('#HIDDEN_StrutturaAmministrativoContabileUid' + this.suffix + '_Modale');
        this.$strutturaAmministrativoContabileCodiceModale = $('#HIDDEN_StrutturaAmministrativoContabileCodice' + this.suffix + '_Modale');
        this.$strutturaAmministrativoContabileDescModale = $('#HIDDEN_StrutturaAmministrativoContabileDescrizione' + this.suffix + '_Modale');

        this.$spanInformazioni = $('#SPAN_InformazioniProvvedimento' + this.suffix);
        this.$modale = $('#modaleGuidaProvvedimento' + this.suffix);
        this.$accordionModale = $('#accordionPadreStrutturaAmministrativa_modale' + this.suffix);

        this.$spinner = $('#SPINNER_Provvedimento' + this.suffix);
    };

    Provvedimento.prototype.constructor = Provvedimento;
    Provvedimento.prototype.impostaDataTableProvvedimentoSenzaOperazioni = impostaDataTableProvvedimentoSenzaOperazioni;
    Provvedimento.prototype.composeCheckbox = composeCheckbox;
    Provvedimento.prototype.cercaProvvedimento = cercaProvvedimento;
    Provvedimento.prototype.cercaProvvedimentoCallback = cercaProvvedimentoCallback;
    Provvedimento.prototype.deseleziona = deseleziona;
    Provvedimento.prototype.caricaStrutture = caricaStrutture;
    Provvedimento.prototype.caricaStruttureCallback = caricaStruttureCallback;
    Provvedimento.prototype.confermaProvvedimento = confermaProvvedimento;
    Provvedimento.prototype.apriModaleProvvedimento = apriModaleProvvedimento;
    Provvedimento.prototype.onClickAccordion = onClickAccordion;
    Provvedimento.prototype.annullaCampiRicerca = annullaCampiRicerca;
    Provvedimento.prototype.doReset = doReset;
    Provvedimento.prototype.apriModaleAttoAmministrativo = apriModaleAttoAmministrativo;

    exports.inizializzazione = inizializzazione;
    exports.destroy = destroy;
    exports.bindApriModaleAttoAmministrativo = bindApriModaleAttoAmministrativo;
    global.Provvedimento = exports;

    /**
     * Impostazione di un nuovo dataTable a partire dai dati, per il Provvedimento. Caso della ricerca senza operazioni.
     *
     * @param arr (any[]) i dati da impostare nel dataTable
     */
    function impostaDataTableProvvedimentoSenzaOperazioni(arr) {
        var opts = {
            aaData: arr,
            bPaginate: true,
            bLengthChange: false,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            bDestroy: true,
            iDisplayLength: 3,
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
            aoColumnDefs: [
                {aTargets: [0], mData: this.composeCheckbox.bind(this), fnCreatedCell: setSourceIntoData('sourceProvvedimento')},
                {aTargets: [1], mData : defaultPerDataTable('anno')},
                {aTargets: [2], mData : defaultPerDataTable('numero')},
                {aTargets: [3], mData : defaultPerDataTable('tipo')},
                {aTargets: [4], mData : defaultPerDataTable('oggetto')},
                {aTargets: [5], mData : defaultPerDataTable('strutturaAmministrativoContabile')},
                {aTargets: [6], mData : defaultPerDataTable('stato')}
            ]
        };
        if($.fn.DataTable.fnIsDataTable(this.$table[0])) {
            this.$table.dataTable().fnDestroy();
        }
        // Inizializzazione del dataTable
        this.$table.dataTable(opts);
    }

    /**
     * Compone il checkbox a partire dai dati in arrivo
     * @param source (any) il sorgente
     * @returns (string) il ckeckbox
     */
    function composeCheckbox(source) {
        var str = '<input type="radio" name="uidProvvedimentoRadio" value="' + source.uid + '"';
        if(this.$hiddenUid.val() !== undefined && +this.$hiddenUid.val() === +source.uid) {
            str += ' checked';
        }
        return str + '/>';
    }

    /**
     * Effettua la chiamata al servizio di ricerca Provvedimento
     */
    function cercaProvvedimento() {
        var err = [];

        this.$divTabella.slideUp();

        // Controllo che non vi siano errori
        pushError(err, !isNaN(+this.$annoProvvedimentoModale.val()), 'Il campo Anno deve essere selezionato');
        pushError(err, !isNaN(+this.$numeroProvvedimentoModale.val()) || !isNaN(+this.$tipoProvvedimentoModale.val()), 'Almeno uno tra i campi Numero e Tipo deve essere compilato');
        if(impostaDatiNegliAlert(err, this.$alertErrori)) {
            return;
        }
        this.$spinner.addClass('activated');
        $.postJSON('effettuaRicercaSenzaOperazioniProvvedimento.do', unqualify(this.$formDiRicerca.serializeObject(), 1))
        .then(this.cercaProvvedimentoCallback.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, 'activated'));
    }

    /**
     * Callback della ricerca provvedimento
     * @param data (any) i dati in ingresso
     */
    function cercaProvvedimentoCallback(data) {
        if(impostaDatiNegliAlert(data.errori, this.$alertErrori)) {
            return;
        }
        this.$alertErrori.slideUp();
        this.impostaDataTableProvvedimentoSenzaOperazioni(data.listaElementoProvvedimento);

        this.$divTabella.slideDown();
        $('input[name="uidProvvedimentoRadio"]:checked', this.$table)
            .removeAttr('selected')
            .removeAttr('checked');
    }
    /**
     * Deseleziona il provvedimento.
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function deseleziona(e) {
        // Se ho un evento, lo annullo
        e && e.preventDefault();

        this.$table
            .find('input[type="radio"]')
            .prop('checked', false);
    }
    /**
     * Caricamento delle SAC
     * @returns (Promise) la promise corrispondente al caricamento dei dati
     */
    function caricaStrutture() {
        var obj = {};
        if(nomeAzioneDecentrata) {
            obj.nomeAzioneDecentrata = nomeAzioneDecentrata;
        }

        return $.postJSON('ajax/strutturaAmministrativoContabileAjax.do', obj)
        .then(this.caricaStruttureCallback.bind(this));
    }
    /**
     * Callback di caricamento delle strutture
     * @param data (any) i dati in ingresso
     */
    function caricaStruttureCallback(data) {
        var list = data.listaElementoCodifica;
        this.ztree.inizializza(list);
        this.ztreeModale.inizializza(list);
        
        $(document).trigger({
            type: 'struttureAmministrativeCaricate',
            lista: list
        });
    }
    /**
     * Conferma provvedimento : imposta i dati nei campi della pagina che apre il modale
     * @param e (Event) l'evento scatenante
     */
    function confermaProvvedimento(e) {
        var $checkedProvvedimento = $('[name="uidProvvedimentoRadio"]:checked', this.$table);
        var provvedimento;
        e.preventDefault();

        if($checkedProvvedimento.length === 0) {
            impostaDatiNegliAlert(['Necessario selezionare un provvedimento'], this.$alertErrori, false);
            return;
        }

        provvedimento = $checkedProvvedimento.data('sourceProvvedimento');

        $('input[type="hidden"][name="' + this.$tipoProvvedimento.attr('name') + '"]').remove();
        this.$annoProvvedimento
            .val(provvedimento.anno)
            .attr('readonly', true);
        this.$numeroProvvedimento
            .val(provvedimento.numero)
            .attr('readonly', true);
        this.$tipoProvvedimento
            .val(provvedimento.uidTipo)
            .attr('disabled', true)
            .after('<input type="hidden" name="' + this.$tipoProvvedimento.attr('name') + '" value="' + provvedimento.uidTipo + '" />');
        this.$strutturaAmministrativoContabileUid
            .val(provvedimento.uidStrutturaAmministrativoContabile);
        this.ztreeConstructor.selezionaNodoSeApplicabileEDisabilitaAlberatura('treeStruttAmm' + this.suffix, provvedimento.uidStrutturaAmministrativoContabile);

        this.$statoProvvedimento
            .val(provvedimento.stato);
        this.$spanInformazioni
            .html(componiInformazioniProvvedimento(provvedimento))
            .trigger('change');
        this.$modale.modal('hide');
    }
    /**
     * Apre il modale del provvedimento
     * @param e (Event) l'evento invocante la funzione
     */
    function apriModaleProvvedimento(e) {
        e.preventDefault();
        this.$alertErrori.slideUp();
        if(this.$accordionModale.length) {
            this.$accordionModale.substituteHandler('click', this.onClickAccordion(this.suffix));
        }

        // Apro il modale
        this.$annoProvvedimentoModale.val(this.$annoProvvedimento.val());
        this.$numeroProvvedimentoModale.val(this.$numeroProvvedimento.val());
        this.$tipoProvvedimentoModale.val(this.$tipoProvvedimento.val());

        // Pulisco lo ztree
        this.ztreeConstructor.deselezionaNodiEAbilitaAlberatura(this.ztreeModale);
        this.$modale.modal('show');

    }
    /**
     * Annullamento dei campi di ricerca
     */
    function annullaCampiRicerca() {
        var uid = +this.$strutturaAmministrativoContabileUidModale.val();
        // Pulizia dei campi
        this.$annoProvvedimentoModale.val('');
        this.$numeroProvvedimentoModale.val('');
        this.$tipoProvvedimentoModale.val('');
        this.$oggettoProvvedimentoModale.val('');
        this.$strutturaAmministrativoContabileUidModale.val('');
        this.$strutturaAmministrativoContabileCodiceModale.val('');
        this.$strutturaAmministrativoContabileDescModale.val('');
        this.$divTabella.slideUp();
        if(!isNaN(uid)) {
            this.ztreeConstructor.deselezionaNodoSeApplicabileEAbilitaAlberatura(this.ztreeModale.innerTree, uid);
        }
    }
    // Utilities
    /**
     * Imposta il sorgente nel data-attribute.
     * @param dataName (string) il nome del data-attribute da usare
     * @returns ((Node, string, any) => void) la funzione che applica i dati nel nodo
     */
    function setSourceIntoData(dataName) {
        return function(nTd, sData, oData) {
            $('input', nTd).data(dataName, oData);
        };
    }
    /**
     * Aggiunge l'errore nell'array
     * @param arr       (string[]) l'array degli errori
     * @param condition (boolean)  la condizione da verificare
     * @param msg       (string)   il messaggio da aggiungere
     */
    function pushError(arr, condition, msg) {
        if(!condition) {
            arr.push(msg);
        }
    }
    /**
     * Composizione delle informazioni del provvedimento
     * @param provvedimento (any) il provvedimento
     * @returns (string) le informazioni
     */
    function componiInformazioniProvvedimento(provvedimento) {
        var arr = [];
        pushInfoPiece(arr, provvedimento.anno, '');
        pushInfoPiece(arr, provvedimento.numero, ' / ');
        pushInfoPiece(arr, provvedimento.tipo, ' - ');
        pushInfoPiece(arr, provvedimento.oggetto, ' - ');
        pushInfoPiece(arr, provvedimento.strutturaAmministrativoContabile, ' - ');
        pushInfoPiece(arr, provvedimento.stato, ' - Stato: ');

        return ': ' + arr.join('');
    }
    /**
     * Aggiunge nell'array fornito il campo con il separatore se valorizzato
     * @param arr   (string[]) l'array da popolare
     * @param field (string)   il campo da apporre
     * @param sep   (string)   il separatore
     */
    function pushInfoPiece(arr, field, sep) {
        if(field) {
            arr.push(sep + field);
        }
    }
    /**
     * Gestione del click dell'accordion
     */
    function onClickAccordion(suffix) {
        $('#struttAmm_modale' + suffix).collapse('toggle');
        $(this)
            .closest('.struttAmm')
            .toggleClass('span11 span9');
    }

    /**
     * Effettua il reset del form
     */
    function doReset() {
        cleanDataTables();
        this.$divTabella.slideUp();
        this.deseleziona();

        // Sarebbe da chiudersi anche il collapse della SAC (ma collapse funge molto male)
//        $('#struttAmm_modale' + this.suffix).filter('.in').collapse('hide');
        
        // Ripristino l'editabilita' dei campi
        this.$annoProvvedimento.removeAttr('disabled');
        this.$numeroProvvedimento.removeAttr('disabled');
        this.$tipoProvvedimento.removeAttr('disabled');
    }

    /**
     * Inizializzazione del JavaScript.
     * @param ztree         (any)    la configurazione dello ztree
     * @param ztreeSettings (any)    le opzioni per lo ztree
     * @param suffix        (string) il suffisso per gli id
     * @returns (number) l'id dell'istanza
     */
    function inizializzazione(ztree, ztreeSettings, suffix) {
        var inner = new Provvedimento(ztree, ztreeSettings, suffix);
        var idSuffix = inner.suffix;
        var id = counter++;

        // Ha senso metterlo nel costruttore?
        inner.doResetBound = inner.doReset.bind(inner);

        $('#pulsanteRicercaProvvedimento' + idSuffix).substituteHandler('click', inner.cercaProvvedimento.bind(inner));
        $('#pulsanteDeselezionaProvvedimento' + idSuffix).substituteHandler('click', inner.deseleziona.bind(inner));
        $('#pulsanteConfermaProvvedimento' + idSuffix).substituteHandler('click', inner.confermaProvvedimento.bind(inner));
        $('#pulsanteApriModaleProvvedimento' + idSuffix).substituteHandler('click', inner.apriModaleProvvedimento.bind(inner));
        $('#pulsanteAnnullaRicercaProvvedimento' + idSuffix).substituteHandler('click', inner.annullaCampiRicerca.bind(inner));
        
        // Cancella la tabella dei provvedimenti quando si effettua un reset del form
        $('form').on('reset', inner.doResetBound);

        inner.caricaStrutture();

        $('#accordionPadreStrutturaAmministrativa' + idSuffix).substituteHandler('click', function() {
            $('#struttAmm' + idSuffix).collapse('toggle');
        });
        instances[id] = inner;
        return id;
    }
    /**
     * Distruzione del gestore.
     * @param id (number) l'id del gestore
     * @returns (boolean) se la distruzione e' andata a buon fine
     */
    function destroy(id) {
        var instance = instances[id];
        if(!instance) {
            return false;
        }
        
        $('#pulsanteRicercaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteDeselezionaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteConfermaProvvedimento' + instance.idSuffix).off('click');
        $('#pulsanteCompilazione' + instance.idSuffix).off('click');
        $('#pulsanteAnnullaRicercaProvvedimento' + instance.idSuffix).off('click');

        $('form').off('reset', instance.doResetBound);

        instances[id] = undefined;
        instance = undefined;
        return true;
    }

    /**
     * Legame della funzionalita' di apertura modale per l'atto amministrativo
     * @param id (number) l'id dell'istanza
     */
    function bindApriModaleAttoAmministrativo(id) {
        var instance = instances[id];
        if(!instance) {
            return;
        }
        $('#pulsanteCompilazione' + instance.suffix).substituteHandler('click', instance.apriModaleAttoAmministrativo.bind(instance));
    }
    
    /**
     * Apertura della modale dell'atto amministrativo
     */
    function apriModaleAttoAmministrativo() {
        var anno = this.$annoProvvedimento.val();
        var numero = this.$numeroProvvedimento.val();
        var tipo = this.$tipoProvvedimento.val();
        var sac = this.$strutturaAmministrativoContabileUid.val();

        this.$annoProvvedimentoModale.val(anno);
        this.$numeroProvvedimentoModale.val(numero);
        this.$tipoProvvedimentoModale.val(tipo);
        this.ztreeConstructor.selezionaNodoSeApplicabile('treeStruttAmm' + this.suffix + '_Modale', sac);

        this.$modale.modal('show');
    }
}(this, jQuery);
