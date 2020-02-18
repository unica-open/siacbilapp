/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function (w, $) {
    'use strict';

    var exports = {};
    var alertErroriModale = $('#ERRORI_IMPEGNO_MODALE');
    var isGestioneUEB = $('#HIDDEN_gestioneUEB').val() === 'true';
    var baseOpts = {
        bServerSide: false,
        bPaginate: true,
        bLengthChange: false,
        iDisplayLength: 5,
        bSort: false,
        bInfo: true,
        bFilter: false,
        bAutoWidth: true,
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
    var instance;
    var $document = $(document);
    
    function extractSiopeAssenzaMotivazione(obj) {
        return obj && obj.siopeAssenzaMotivazione && obj.siopeAssenzaMotivazione.uid || 0;
    }

    var Impegno = function(annoMovimento, numeroMovimento, numeroSubMovimento, descrizione, cig, cup, mutuo, disponibilita, pulsanteApertura, siopeAssenza) {
        this.$annoMovimento = $(annoMovimento);
        this.$numeroMovimento = $(numeroMovimento);
        this.$numeroSubMovimento = $(numeroSubMovimento);
        this.$descrizione = $(descrizione);
        this.$cig = $(cig);
        this.$cup = $(cup);
        this.$mutuo = $(mutuo);
        this.$disponibilita = $(disponibilita);
        this.$pulsanteApertura = $(pulsanteApertura);
        this.$siopeAssenza = $(siopeAssenza);

        this.$ricercaEffettuataConSuccesso = $('#hidden_ricercaEffettuataConSuccessoModaleImpegno');
        this.$tabellaMutui = $('#tabellaMutuiModale');
        this.$tabellaMovimenti = $('#tabellaImpegniModale');
        this.$tdCapitolo = $('#tabellaImpegno_tdCapitolo');
        this.$tdProvvedimento = $('#tabellaImpegno_tdProvvedimento');
        this.$tdSoggetto = $('#tabellaImpegno_tdSoggetto');
        this.$tdImporto = $('#tabellaImpegno_tdImporto');
        this.$tdDisponibile = $('#tabellaImpegno_tdDisponibile');

        this.$annoModale = $('#annoImpegnoModale');
        this.$numeroModale = $('#numeroImpegnoModale');
        this.$spinner = $('#SPINNER_pulsanteRicercaImpegnoModale');
        this.$fieldset = $('#FIELDSET_modaleImpegno');
        this.$divMovimenti = $('#divImpegniTrovati');
        this.$divMutui = $('#divMutui').overlay();
        this.$conferma = $('#pulsanteConfermaModaleImpegno');
        this.$ricerca = $('#pulsanteRicercaImpegnoModale');
        this.$impegnoModale = $('#tabellaImpegnoModale');

        this.$modale = $('#modaleImpegno');
    };

    Impegno.prototype.impostaMutuiNellaTabella = impostaMutuiNellaTabella;
    Impegno.prototype.impostaImpegnoNellaTabella = impostaImpegnoNellaTabella;
    Impegno.prototype.ricercaImpegno = ricercaImpegno;
    Impegno.prototype.ricercaImpegnoCallback = ricercaImpegnoCallback;
    Impegno.prototype.confermaImpegno = confermaImpegno;
    Impegno.prototype.ricercaImpegnoPerChiaveOttimizzato = ricercaImpegnoPerChiaveOttimizzato;
    Impegno.prototype.ricercaImpegnoPerChiaveOttimizzatoCallback = ricercaImpegnoPerChiaveOttimizzatoCallback;
    Impegno.prototype.impostaSubimpegniPaginatiNellaTabella = impostaSubimpegniPaginatiNellaTabella;
    Impegno.prototype.impostaEApriCollapseImpegniTrovati = impostaEApriCollapseImpegniTrovati;
    Impegno.prototype.initDataTable = initDataTable;
    Impegno.prototype.dataTableOverlay = dataTableOverlay;
    Impegno.prototype.apriModale = apriModale;

    exports.inizializza = inizializza;
    exports.destroy = destroy;
    exports.computeStringCapitolo = computeStringCapitolo;
    exports.computeStringProvvedimento = computeStringProvvedimento;
    exports.computeStringSoggetto = computeStringSoggetto;
    exports.computeStringImporto = computeStringImporto;
    w.Impegno = exports;

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
     * Imposta la tabella dei mutui.
     */
    function impostaMutuiNellaTabella(lista) {
        var opts = {
            aaData: lista,
            oLanguage: {
                sZeroRecords : 'Non sono presenti mutui associati',
                oPaginate : {
                    sEmptyTable : 'Nessun mutuo disponibile'
                }
            },
            aoColumnDefs : [
                {aTargets: [0], mData: function() {
                    return '<input type="radio" name="radio_mutuo_modale_impegno" />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalMutuo', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numeroMutuo')},
                {aTargets: [2], mData: defaultPerDataTable('descrizioneMutuo')},
                {aTargets: [3], mData: defaultPerDataTable('istitutoMutuante.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttualeVoceMutuo', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('importoDisponibileLiquidareVoceMutuo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        this.initDataTable(this.$tabellaMutui, options);
    }

    /**
     * Imposta i dati dell'impegno nella tabella
     * @param impegno (any) l'impegno
     */
    function impostaImpegnoNellaTabella(impegno) {
        var disponibile = impegno.disponibilitaLiquidare || 0;

        this.$tdCapitolo.html(computeStringCapitolo(impegno.capitoloUscitaGestione));
        this.$tdProvvedimento.html(computeStringProvvedimento(impegno.attoAmministrativo));
        this.$tdSoggetto.html(computeStringSoggetto(impegno.soggetto, impegno.classeSoggetto));
        this.$tdImporto.html(computeStringImporto(impegno.importoAttuale, impegno.importoIniziale));
        this.$tdDisponibile.html(disponibile.formatMoney());
        // SIAC-6019
        this.$impegnoModale[impegno && impegno.elencoSubImpegni.length === 0 ? 'data' : 'removeData']('originalImpegno', impegno);
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
     * Richiama l'esecuzione della ricerca Impegno.
     * @returns (Promise) la promise dell'invocazione AJAX
     */
    function ricercaImpegno() {
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

        return $.postJSON('ricercaImpegnoPerChiaveOttimizzato.do', oggettoPerChiamataAjax)
        .then(this.ricercaImpegnoCallback.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, 'activated'));
    }

    /**
     * Callback per la ricerca impegni
     * @param data (any) i dati del servizio
     * @returns (any) i dati del servizio
     */
    function ricercaImpegnoCallback(data) {
        if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
            return;
        }
        this.impostaImpegnoNellaTabella(data.impegno);
        this.impostaSubimpegniPaginatiNellaTabella(data.impegno);
        this.impostaMutuiNellaTabella(data.impegno.listaVociMutuo);
        this.$ricercaEffettuataConSuccesso.val('true');

        this.$divMovimenti.slideDown();
        this.$divMutui.slideDown();
        return data;
    }

    /**
     * Conferma l'impegno selezionato.
     *
     * @param e (Event) l'evento scatenante
     */
    function confermaImpegno(e) {
        var checkedRadio = this.$tabellaMovimenti.find('input[name="radio_modale_impegno"]:checked');
        var checkedMutuoRadio = this.$tabellaMutui.find('input[name="radio_mutuo_modale_impegno"]:checked');
        var impegno;
        var subimpegno;
        var mutuo;
        var numeroSub;
        var disponibilita;
        var str;
        var event;
        var impegnoSenzaSub = this.$impegnoModale.data('originalImpegno');
        var tipoDebitoSIOPE;

        // Prevengo l'azione did efault
        e.preventDefault();

        if(!this.$ricercaEffettuataConSuccesso.val() || (checkedRadio.length === 0 && !impegnoSenzaSub)) {
            impostaDatiNegliAlert(['Necessario selezionare un impegno'], alertErroriModale, false);
            return;
        }
        
        impegno = impegnoSenzaSub ? impegnoSenzaSub : checkedRadio.data('originalImpegno');
        subimpegno = checkedRadio.data('originalSubImpegno') ? checkedRadio.data('originalSubImpegno') : undefined;
        mutuo = checkedMutuoRadio.data('originalMutuo') ? checkedMutuoRadio.data('originalMutuo') : undefined;
       
        // SIAC-5410
        tipoDebitoSIOPE = subimpegno
            ? subimpegno.siopeTipoDebito
            : impegno
                ? impegno.siopeTipoDebito
                : undefined;
        
        // Campi d'appoggio
        numeroSub = !!subimpegno ? subimpegno.numero : '';
        disponibilita = (!!subimpegno ? subimpegno.disponibilitaLiquidare : impegno.disponibilitaLiquidare) || 0;
        str = ': ' + impegno.annoMovimento + ' / ' + impegno.numero;
        if(subimpegno) {
            str += ' - ' + numeroSub;
        }
        // SIAC-5410
        if(tipoDebitoSIOPE && tipoDebitoSIOPE.codice && tipoDebitoSIOPE.descrizione) {
            str += ' (' + tipoDebitoSIOPE.codice + ' - ' + tipoDebitoSIOPE.descrizione + ')';
        }

        this.$annoMovimento.val(impegno.annoMovimento).attr('readOnly', true);
        this.$numeroMovimento.val(impegno.numero).attr('readOnly', true);
        this.$numeroSubMovimento.val(numeroSub).attr('readOnly', true);

        this.$cig.val(subimpegno ? subimpegno.cig : impegno.cig);
        this.$cup.val(subimpegno ? subimpegno.cup : impegno.cup);
        this.$siopeAssenza.val(subimpegno ? extractSiopeAssenzaMotivazione(subimpegno) : extractSiopeAssenzaMotivazione(impegno));
        this.$mutuo.val(mutuo && mutuo.numeroMutuo || '');
        this.$disponibilita.html(disponibilita.formatMoney());
        this.$descrizione.html(str);

        event = $.Event('impegnoCaricato', {'impegno': impegno, 'subimpegno': subimpegno});
        $document.trigger(event);
        this.$modale.modal('hide');
    }

    /**
     * Chiama la ricerca impegno per chiave (con subimpegni paginati)
     * @returns (Promise) la promise dell'invocazione AJAX
     */
    function ricercaImpegnoPerChiaveOttimizzato(){
        var oggettoPerChiamataAjax = unqualify(this.$fieldset.serializeObject(), 1);
        alertErroriModale.slideUp();
        this.$spinner.addClass('activated');

        return $.postJSON('ricercaImpegnoPerChiaveOttimizzato.do', oggettoPerChiamataAjax)
        .then(this.ricercaImpegnoPerChiaveOttimizzatoCallback.bind(this))
        .always(this.$spinner.removeClass.bind(this.$spinner, 'activated'));
    }

    /**
     * Callback per la ricerca ottimizzata
     * @param data (any) la risposta del servizio
     * @returns (any) i dati del servizio
     */
    function ricercaImpegnoPerChiaveOttimizzatoCallback(data) {
        if(impostaDatiNegliAlert(data.errori, alertErroriModale, false)) {
            return;
        }
        this.impostaImpegnoNellaTabella(data.impegno);
        this.impostaSubimpegniPaginatiNellaTabella(data.impegno);
        this.impostaMutuiNellaTabella(data.impegno.listaVociMutuo);
        this.impostaEApriCollapseImpegniTrovati();
        return data;
    }

    /**
     * Imposta la tabella dei subimpegni.
     * @param impegno (any) l'impegno da cui ottenere i dati per il popolamento della tabella
     */
    function impostaSubimpegniPaginatiNellaTabella(impegno) {
        var opts = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSubImpegniAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti subimpegni associati',
                oPaginate: {
                    sEmptyTable: 'Nessun subimpegno disponibile'
                }
            },
            fnPreDrawCallback: this.dataTableOverlay('$tabellaMovimentiWrapper', '#tabellaImpegniModale_wrapper', 'show', defaultPreDraw),
            fnDrawCallback: this.dataTableOverlay('$tabellaMovimentiWrapper', '#tabellaImpegniModale_wrapper', 'hide', defaultDrawCallback),
            aoColumnDefs: [
                {aTargets: [0], mData: function() {
                    return '<input type="radio" name="radio_modale_impegno" />';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $('input', nTd).data('originalImpegno', impegno)
                        .data('originalSubImpegno', oData);
                }},
                {aTargets: [1], mData: defaultPerDataTable('numero')},
                {aTargets: [2], mData: defaultPerDataTable('descrizione')},
                {aTargets: [3], mData: defaultPerDataTable('soggetto.denominazione')},
                {aTargets: [4], mData: defaultPerDataTable('importoAttuale', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('disponibilitaLiquidare', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var options = $.extend(true, {}, baseOpts, opts);
        this.initDataTable(this.$tabellaMovimenti, options, '$tabellaMovimentiWrapper');
    }

    /**
     * Popola i campi hidden e lega gli handler di risultati di ricerca della ricerca dell'impegno.
     * Una volta terminato, mostra i risultati.
     */
    function impostaEApriCollapseImpegniTrovati(){
        this.$ricercaEffettuataConSuccesso.val('true');
        this.$conferma.substituteHandler('click', this.confermaImpegno);
        this.$divMovimenti.slideDown();
    }

    /**
     * Effettua uno slideup dei div
     * @returns (function) la funzione che effettua lo slideup dei div
     */
    function slideUpDivs() {
        var $acc = $();
        Array.prototype.slice.call(arguments, 0).forEach(function(el) {
            $acc.add(el);
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
     * Gestione del change dei radio button
     * @param impegno (any) il gestore dell'impegno
     * @returns (function) la funzione da scatenare all'evento
     */
    function onChangeRadio(impegno) {
        return function() {
            var si = $(this).data('originalSubImpegno');
            impegno.$divMutui.overlay('show');
            impegno.impostaMutuiNellaTabella(si && si.listaVociMutuo || []);
            impegno.$divMutui.overlay('hide');
        };
    }

    /**
     * Apertura della modale
     */
    function apriModale() {
        this.$annoModale.val(this.$annoMovimento.val());
        this.$numeroModale.val(this.$numeroMovimento.val());
        // SIAC-5409: Chiudo i collapse
        this.$divMovimenti.slideUp();
        this.$divMutui.slideUp();
        this.$modale.modal('show');
    }

    /**
     * Inizializza la gestione.
     *
     * @param campoAnnoMovimento      (String) il campo ove impostare l'anno del movimento (Optional - default: #annoMovimentoMovimentoGestione)
     * @param campoNumeroMovimento    (String) il campo ove impostare il numero del movimento (Optional - default: #numeroMovimentoGestione)
     * @param campoNumeroSubMovimento (String) il campo ove impostare la denominazione (Optional - default: #numeroSubMovimentoGestione)
     * @param campoDescrizione        (String) il campo ove impostare la descrizione completa del soggetto (Optional - default: #datiRiferimentoImpegnoSpan)
     *
     * @param campoCig                (String) il campo ove impostare il CIG (Optional - default: '')
     * @param campoCup                (String) il campo ove impostare il CUP (Optional - default: '')
     * @param campoMutuo              (String) il campo ove impostare il mutuo (Optional - default: '')
     * @param spanDisponibilita       (String) il campo ove impostare la disponibilita (Optional - default: '')
     * @param pulsanteApertura        (String) il pulsante di apertura (Optional - default: '')
     * @param campoSiopeAssenza       (String) il campo ove impostare il tipo di assenza CIG (Optional - default: '')
     */
    function inizializza (campoAnnoMovimento, campoNumeroMovimento, campoNumeroSubMovimento, campoDescrizione, campoCig, campoCup, campoMutuo, spanDisponibilita, pulsanteApertura, campoSiopeAssenza) {
        var impegno = new Impegno(campoAnnoMovimento || '#annoMovimentoMovimentoGestione',
            campoNumeroMovimento || '#numeroMovimentoGestione',
            campoNumeroSubMovimento || '#numeroSubMovimentoGestione',
            campoDescrizione || '#datiRiferimentoImpegnoSpan',
            campoCig || '',
            campoCup || '',
            campoMutuo || '',
            spanDisponibilita || '',
            pulsanteApertura || '',
            campoSiopeAssenza || '');
        
        impegno._fncRicercaImpegno = impegno.ricercaImpegno.bind(impegno);
        impegno._fncConfermaImpegno = impegno.confermaImpegno.bind(impegno);
        impegno._fncApriModale = impegno.apriModale.bind(impegno);
        
        impegno._fncRicercaEffettuataConSuccessoVal = impegno.$ricercaEffettuataConSuccesso.val.bind(impegno.$ricercaEffettuataConSuccesso, '');
        impegno._fncSlideUpDivs = slideUpDivs(impegno.$divMovimenti, alertErroriModale);
        impegno._fncOnChangeRadio = onChangeRadio(impegno);

        impegno.$ricerca.substituteHandler('click', impegno._fncRicercaImpegno);
        impegno.$conferma.substituteHandler('click', impegno._fncConfermaImpegno);
        impegno.$pulsanteApertura.substituteHandler('click', impegno._fncApriModale);
        
        $('#divMutui, #divImpegniTrovati').on('shown hidden', eventStopPropagation);

        $(document).on('shown', '#modaleImpegno', impegno._fncRicercaEffettuataConSuccessoVal)
        .on('hidden', '#modaleImpegno', impegno._fncSlideUpDivs)
        .on('change', '#modaleImpegno input[name="radio_modale_impegno"]', impegno._fncOnChangeRadio);
        
        instance = impegno;
    }
    
    function destroy() {
    	if(!instance) {
    		return;
    	}
        instance.$ricerca.off('click', instance._fncRicercaImpegno);
        instance.$conferma.off('click', instance._fncConfermaImpegno);
        instance.$pulsanteApertura.off('click');
           
        $(document).off('shown', '#modaleImpegno', instance._fncRicercaEffettuataConSuccessoVal)
            .off('hidden', '#modaleImpegno', instance._fncSlideUpDivs)
            .off('change', '#modaleImpegno input[name="radio_modale_impegno"]', instance._fncOnChangeRadio);
    	instance = undefined;
    }

}(this, jQuery));