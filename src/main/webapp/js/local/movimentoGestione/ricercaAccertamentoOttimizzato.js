/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function (w, $) {
    'use strict';
    var exports = {};
    var alertErroriModale = $('#ERRORI_ACCERTAMENTO_MODALE');
    var isGestioneUEB = $('#HIDDEN_gestioneUEB').val() === 'true';
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bFilter: false,
        bProcessing: true,
        bDestroy: true,
        oLanguage: {
            sInfo : '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty : '0 risultati',
            sProcessing : 'Attendere prego...',
            oPaginate: {
                sFirst : 'inizio',
                sLast : 'fine',
                sNext : 'succ.',
                sPrevious : 'prec.'
            }
        }
    };
    var $document = $(document);
    var instance;
    
    var Accertamento = function(annoMovimento, numeroMovimento, numeroSubMovimento, descrizione, disponibilita, pulsanteApertura) {
        this.$annoMovimento = $(annoMovimento);
        this.$numeroMovimento = $(numeroMovimento);
        this.$numeroSubMovimento = $(numeroSubMovimento);
        this.$descrizione = $(descrizione);
        this.$disponibilita = $(disponibilita);
        this.$pulsanteApertura = $(pulsanteApertura);

        this.$ricercaEffettuataConSuccesso = $('#hidden_ricercaEffettuataConSuccessoModaleAccertamento');
        this.$tabellaMovimenti = $('#tabellaAccertamentiModale');
        this.$tdCapitolo = $('#tabellaAccertamento_tdCapitolo');
        this.$tdProvvedimento = $('#tabellaAccertamento_tdProvvedimento');
        this.$tdSoggetto = $('#tabellaAccertamento_tdSoggetto');
        this.$tdImporto = $('#tabellaAccertamento_tdImporto');
        this.$tdDisponibile = $('#tabellaAccertamento_tdDisponibile');

        this.$annoModale = $('#annoAccertamentoModale');
        this.$numeroModale = $('#numeroAccertamentoModale');
        this.$spinner = $('#SPINNER_pulsanteRicercaAccertamentoModale');
        this.$fieldset = $('#FIELDSET_modaleAccertamento');
        this.$divMovimenti = $('#divAccertamentiTrovati');
        this.$conferma = $('#pulsanteConfermaModaleAccertamento');
        this.$ricerca = $('#pulsanteRicercaAccertamentoModale');

        this.$modale = $('#modaleAccertamento');
    };

    Accertamento.prototype.impostaAccertamentoNellaTabella = impostaAccertamentoNellaTabella;
    Accertamento.prototype.ricercaAccertamento = ricercaAccertamento;
    Accertamento.prototype.ricercaAccertamentoCallback = ricercaAccertamentoCallback;
    Accertamento.prototype.confermaAccertamento = confermaAccertamento;
    Accertamento.prototype.impostaSubaccertamentiPaginatiNellaTabella = impostaSubaccertamentiPaginatiNellaTabella;
    Accertamento.prototype.impostaEApriCollapseAccertamentiTrovati = impostaEApriCollapseAccertamentiTrovati;
    Accertamento.prototype.initDataTable = initDataTable;
    Accertamento.prototype.dataTableOverlay = dataTableOverlay;
    Accertamento.prototype.apriModale = apriModale;

    exports.inizializza = inizializza;
    exports.destroy = destroy;
    
    w.Accertamento = exports;

    /**
     * Formattazione degli importi
     * @param val (any) il valore da formattare
     * @returns (string) il valore formattato, se esiste; una stringa vuota altrimenti
     */
    function formatMoney(val) {
        return val !== undefined && val.formatMoney && typeof val.formatMoney === 'function' ? val.formatMoney() : '';
    }
    /**
     * Aggiunge la classse tab_Right all'elemento
     * @param el (Node) il nodo su cui impostare la classe
     * @returns (jQuery) il wrapper dell'elemento
     */
    function tabRight(el) {
        return $(el).addClass('tab_Right');
    }

    /**
     * Inizializza il dataTable, distrugendo il pre-esistente
     * @param $table      (jQuery) la tabella da inizializzare
     * @param opts        (any)    le opzioni
     * @param overlayName (string) il nome dell'overlay (opzionale)
     * @returns (jQuery) la tabella inizializzata
     */
    function initDataTable($table, opts, overlayName) {
        if($.fn.DataTable.fnIsDataTable($table[0])) {
            $table.dataTable().fnDestroy();
        }
        if(overlayName) {
            this[overlayName] = undefined;
        }
        return $table.dataTable(opts);
    }

    /**
     * Carica l'overlay sul data table
     * @param wrapperName     (string)   il nome del wrapper
     * @param wrapperSelector (string)   il selettore del wrapper
     * @param operation       (string)   l'operazione da invocare
     * @param otherFunction   (function) l'eventuale ulteriore operazione da invocare
     * @returns  (function) una funzione per l'utilizzo da parte di dataTables
     */
    function dataTableOverlay(wrapperName, wrapperSelector, operation, otherFunction) {
        var self = this;
        return function(opts) {
            if(otherFunction && typeof otherFunction === 'function') {
                otherFunction(opts);
            }
            if(!self[wrapperName]) {
                self[wrapperName] = $(wrapperSelector).overlay({rebind: true, loader: true});
                self[wrapperName + 'InUse'] = false;
            }
            if(operation === 'show' && self[wrapperName + 'InUse']) {
                return;
            }
            self[wrapperName].overlay(operation);
            self[wrapperName + 'InUse'] = operation === 'show';
        };
    }
    
    /**
     * Imposta i dati dell'accertamento nella tabella
     * @param accertamento (any) l'accertamento
     */
    function impostaAccertamentoNellaTabella(accertamento) {
        var disponibile = accertamento.disponibilitaIncassare || 0;
        this.$tdCapitolo.html(computeStringCapitolo(accertamento.capitoloEntrataGestione));
        this.$tdCapitolo.data('originalAccertamento', accertamento);
        this.$tdProvvedimento.html(computeStringProvvedimento(accertamento.attoAmministrativo));
        this.$tdSoggetto.html(computeStringSoggetto(accertamento.soggetto, accertamento.classeSoggetto));
        this.$tdImporto.html(computeStringImporto(accertamento.importoAttuale, accertamento.importoIniziale));
        this.$tdDisponibile.html(disponibile.formatMoney());
    }

    /**
     * Computa la stringa del capitolo
     * @param capitolo (any) il capitolo da cui ottenere la stringa
     * @returns (string) la stringa con i dati del capitolo
     */
    function computeStringCapitolo(capitolo) {
        var res = '';
        if(!capitolo || !capitolo.uid) {
            return res;
        }
        res = capitolo.annoCapitolo + '/' + capitolo.numeroCapitolo + '/' + capitolo.numeroArticolo;
        if(isGestioneUEB) {
            res += '/' + capitolo.numeroUEB;
        }
        res += ' - ' + capitolo.descrizione;
        if(capitolo.strutturaAmministrativoContabile && capitolo.strutturaAmministrativoContabile.codice) {
            res += ' - SAC: ' + capitolo.strutturaAmministrativoContabile.codice;
        }
        if(capitolo.tipoFinanziamento && capitolo.tipoFinanziamento.codice) {
            res += ' - Tipo finanziamento: ' + capitolo.tipoFinanziamento.codice;
        }
        return res;
    }

    /**
     * Computa la stringa del provvedimento
     * @param atto (any) l'atto amministrativo da cui ottenere la stringa
     * @returns (string) la stringa con i dati del provvedimento
     */
    function computeStringProvvedimento(atto) {
        var res = '';
        if(!atto || !atto.uid) {
            return res;
        }
        res += atto.anno + '/' + atto.numero;
        if(atto.tipoAtto && atto.tipoAtto.descrizione) {
            res += ' - ' + atto.tipoAtto.descrizione;
        }
        res += ' - ' + atto.oggetto;
        if(atto.strutturaAmmContabile && atto.strutturaAmmContabile.codice) {
            res += ' - ' + atto.strutturaAmmContabile.codice;
        }
        return res;
    }

    /**
     * Computa la stringa del soggetto
     * @param soggetto (any) il soggetto da cui ottenere la stringa
     * @param classeSoggetto (any) la classe soggetto da cui ottenere la stringa
     * @returns (string) la stringa con i dati del soggetto
     */
    function computeStringSoggetto(soggetto, classeSoggetto) {
        var res = '';
        if(soggetto && soggetto.uid) {
            res = soggetto.codiceSoggetto + ' - ' + soggetto.denominazione;
            if(soggetto.codiceFiscale) {
                res += ' - CF: ' + soggetto.codiceFiscale;
            }
            if(soggetto.partitaIva) {
                res += ' - P.IVA: ' + soggetto.partitaIva;
            }
            return res;
        }
        if(classeSoggetto) {
            res = 'Classe: ' + classeSoggetto.codice + ' - ' + classeSoggetto.descrizione;
            return res;
        }
        return res;
    }

    /**
     * Computa la stringa dell'importo
     * @param importoAttuale (number) l'importo attuale da cui ottenere la stringa
     * @param importoIniziale (number) l'importo iniziale da cui ottenere la stringa
     * @returns (string) la stringa con i dati del soggetto
     */
    function computeStringImporto(importoAttuale, importoIniziale) {
        var impAtt = importoAttuale || 0;
        var res = impAtt.formatMoney();
        if(importoIniziale){
            res += ' (iniziale: ' + importoIniziale.formatMoney() + ')';
        }
        return res;
    }

    /**
     * Richiama l'esecuzione della ricerca Accertamento.
     * @returns (Promise) la promise dell'invocazione AJAX
     */
    function ricercaAccertamento() {
        var oggettoPerChiamataAjax;
        var arrayErrori = [];

        if(isNaN(parseInt(this.$annoModale.val(), 10))) {
            arrayErrori.push('COR_ERR_0002 - Dato obbligatorio omesso: Anno');
        }
        if(isNaN(parseInt(this.$numeroModale.val(), 10))) {
            arrayErrori.push('COR_ERR_0002 - Dato obbligatorio omesso: Numero');
        }
        if(impostaDatiNegliAlert(arrayErrori, alertErroriModale, false)) {
            return;
        }

        oggettoPerChiamataAjax = unqualify(this.$fieldset.serializeObject(), 1);
        alertErroriModale.slideUp();
        this.$divMovimenti.slideUp();
        this.$spinner.addClass('activated');

        return $.postJSON('ricercaAccertamentoPerChiaveOttimizzato.do', oggettoPerChiamataAjax)
        .then(this.ricercaAccertamentoCallback.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, 'activated'));
    }

    /**
     * Callback per la ricerca accertamenti
     * @param data (any) i dati del servizio
     * @returns (any) i dati del servizio
     */
    function ricercaAccertamentoCallback(data) {
        if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
            return;
        }
        this.impostaAccertamentoNellaTabella(data.accertamento);
        this.impostaSubaccertamentiPaginatiNellaTabella(data.accertamento);
        this.$ricercaEffettuataConSuccesso.val('true');

        this.$divMovimenti.slideDown();
        return data;
    }

    /**
     * Conferma l'accertamento selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaAccertamento(e) {
        var checkedRadio = this.$tabellaMovimenti.find('input[name="radio_modale_accertamento"]:checked');
        
        var accertamento;
        var subaccertamento;
        var numeroSub;
        var disponibilita;
        var str;
        var event;

        e.preventDefault();

        if(!this.$ricercaEffettuataConSuccesso.val()) {
            impostaDatiNegliAlert(['Necessario selezionare un accertamento'], alertErroriModale, false);
            return;
        }
        accertamento = this.$tdCapitolo.data('originalAccertamento');
        subaccertamento = checkedRadio.data('originalSubAccertamento');
        numeroSub = !!subaccertamento ? subaccertamento.numero : '';
        disponibilita = (!!subaccertamento ? subaccertamento.disponibilitaIncassare : accertamento.disponibilitaIncassare) || 0;
        str = ': ' + accertamento.annoMovimento + ' / ' + accertamento.numero;
        if(subaccertamento) {
            str += ' - ' + numeroSub;
        }

        this.$annoMovimento.val(accertamento.annoMovimento).attr('readOnly', true);
        this.$numeroMovimento.val(accertamento.numero).attr('readOnly', true);
        this.$numeroSubMovimento.val(numeroSub).attr('readOnly', true);
        this.$disponibilita.html(disponibilita.formatMoney());
        this.$descrizione.html(str);

        event = $.Event('accertamentoCaricato', {'accertamento': accertamento, 'subaccertamento': subaccertamento});
        $document.trigger(event);
        this.$modale.modal('hide');
    }

    /**
     * Imposta la tabella dei subaccertamenti.
     * @param accertamento (Object) l'accertamento da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubaccertamentiPaginatiNellaTabella(accertamento) {
        var opts = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSubAccertamentiAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti subaccertamenti associati',
                oPaginate: {
                    sEmptyTable: 'Nessun subaccertamento disponibile'
                }
            },
            fnPreDrawCallback: this.dataTableOverlay('$tabellaMovimentiWrapper', '#tabellaAccertamentiModale_wrapper', 'show', defaultPreDraw),
            fnDrawCallback: this.dataTableOverlay('$tabellaMovimentiWrapper', '#tabellaAccertamentiModale_wrapper', 'hide', defaultDrawCallback),
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return '<input type="radio" name="radio_modale_accertamento" />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd)
                        .data('originalSubAccertamento', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numero')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('soggetto.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('disponibilitaIncassare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        this.initDataTable(this.$tabellaMovimenti, options, '$tabellaMovimentiWrapper');
    }

    /**
     * Popola i campi hidden e lega gli handler di risultati di ricerca della ricerca dell'accertamento.
     * Una volta terminato, mostra i risultati.
     */
    function impostaEApriCollapseAccertamentiTrovati(){
        this.$ricercaEffettuataConSuccesso.val('true');
        this.$conferma.substituteHandler('click', this.confermaAccertamento);
        this.$divMovimenti.slideDown();
    }

    /**
     * Effettua uno slideup dei div
     * @returns (function) la funzione che effettua lo slideup dei div
     */
    function slideUpDivs() {
        var $acc = $();
        Array.prototype.slice.call(arguments, 0).forEach(function(el) {
        	$acc = $acc.add(el);
        });
        return function() {
            $acc.slideUp();
        };
    }

    /**
     * Ferma la propagazione dell'evento
     * @param e (event) l'evento da bloccare
     */
    function eventStopPropagation(e) {
        e.stopPropagation();
    }
    
    /**
     * Apertura della modale
     */
    function apriModale() {
        this.$annoModale.val(this.$annoMovimento.val());
        this.$numeroModale.val(this.$numeroMovimento.val());
        // SIAC-5409
        this.$divMovimenti.slideUp();
        this.$modale.modal('show');
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoAnnoMovimento      (String) il campo ove impostare l'anno del movimento (Optional - default: #annoMovimentoMovimentoGestione)
     * @param campoNumeroMovimento    (String) il campo ove impostare il numero del movimento (Optional - default: #numeroMovimentoGestione)
     * @param campoNumeroSubMovimento (String) il campo ove impostare la denominazione (Optional - default: #numeroSubMovimentoGestione)
     * @param campoDescrizione        (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #datiRiferimentoImpegnoSpan)
     * @param spanDisponibilita       (String) il campo ove impostare la disponibilita (Optional - default: '')
     * @param alertErrori             (Jquery) oggetto jQuery che specifica quale alertErroriModale utilizzare
     * @param pulsanteApertura        (String) il pulsante di apertura (Optional - default: '')
     */
    function inizializza(campoAnnoMovimento, campoNumeroMovimento, campoNumeroSubMovimento, campoDescrizione, spanDisponibilita, /*Optional*/ alertErrori, pulsanteApertura) {
        var accertamento = new Accertamento(campoAnnoMovimento || '#annoMovimentoMovimentoGestione',
            campoNumeroMovimento || '#numeroMovimentoGestione',
            campoNumeroSubMovimento || '#numeroSubMovimentoGestione',
            campoDescrizione || '#datiRiferimentoImpegnoSpan',
            spanDisponibilita || '',
            pulsanteApertura || '');
        var alertDaSettare = alertErrori || alertErroriModale;
        
        accertamento._fncRicercaAccertamento = accertamento.ricercaAccertamento.bind(accertamento);
        accertamento._fncConfermaAccertamento = accertamento.confermaAccertamento.bind(accertamento);
        accertamento._fncRicercaEffettuataConSuccessoVal = accertamento.$ricercaEffettuataConSuccesso.val.bind(accertamento.$ricercaEffettuataConSuccesso, '');
        accertamento._fncApriModale = accertamento.apriModale.bind(accertamento);
        accertamento._fncSlideUpDivs = slideUpDivs(accertamento.$divMovimenti, alertErroriModale);
        
        alertErroriModale = alertDaSettare; 
        accertamento.$ricerca.substituteHandler('click', accertamento._fncRicercaAccertamento);
        accertamento.$conferma.substituteHandler('click', accertamento._fncConfermaAccertamento);
        accertamento.$pulsanteApertura.substituteHandler('click', accertamento._fncApriModale);

        $(document).on('shown', '#modaleAccertamento', accertamento._fncRicercaEffettuataConSuccessoVal)
        .on('hidden', '#modaleAccertamento', accertamento._fncSlideUpDivs);
        instance = accertamento;
    }
    /**
     * Pulisce gli handler precedentemente settati per l'aggiornamento
     * */
    function destroy(){
    	if(!instance) {
    		return;
    	}
    	instance.$ricerca.off('click', instance._fncRicercaAccertamento);
        instance.$conferma.off('click', instance._fncConfermaAccertamento);
        instance.$pulsanteApertura.off('click', instance._fncApriModale);
           
        $(document).off('shown', '#modaleAccertamento', instance._fncRicercaEffettuataConSuccessoVal)
            .off('hidden', '#modaleAccertamento', instance._fncSlideUpDivs);
    	instance = undefined;
    }
    return exports;
}(this, $));