/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
;(function(w, $){
    var alertErrori = $('#ERRORI');
    var optionsBase = {
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
            sInfo: '_START_ - _END_ di _MAX_ risultati',
            sInfoEmpty: '0 risultati',
            sProcessing: 'Attendere prego...',
            oPaginate: {
                sFirst: 'inizio',
                sLast: 'fine',
                sNext: 'succ.',
                sPrevious: 'prec.',
                sEmptyTable: 'Nessun dato disponibile'
            }
        }
    };
    var flagDatiIvaAccessibile = $('#hidden_flagDatiIvaAccessibile').val() === 'true';
    var registrazioneSuSingolaQuota = $('#hidden_registrazioneSuSingolaQuota').val() === 'true';

    function tabRight(td) {
        return $(td).addClass('tab_Right');
    }
    function printFields(fields, separator) {
        return function(val) {
            if(val === undefined || val === null) {
                return '';
            }
            return fields.map(function(field) {
                var tmp;
                var op;
                if(typeof field === 'string') {
                    return val[field];
                }
                if(typeof field === 'object') {
                    tmp = val[field.fieldname];
                    op = field.operation && typeof field.operation === 'function' ? field.operation : passThrough;
                    return op(val);
                }
                return '';
            }).filter(function(el) {
                return el !== undefined;
            }).join(separator);
        };
    }

    /**
     *  Carica il corpo della tabella 'Documenti collegati' del tab 'Documenti collegati'
     */
    function caricaDocumentiNellaTabella(listaDocumentiCollegati){
        var table = $("#tabellaDocumentiCollegatiEntrata");
        var options = {
            aaData: listaDocumentiCollegati,
            oLanguage: {
                sZeroRecords: 'Non sono presenti documenti associati'
            },
            fnDrawCallback: function() {
                table.find('a[rel="popover"]').popover();
                table.find('button.dropdown-toggle').dropdown();
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('tipo')},
                {aTargets: [1], mData: defaultPerDataTable('documento')},
                {aTargets: [2], mData: defaultPerDataTable('data')},
                {aTargets: [3], mData: function (source) {
                    return '<a rel="popover" href="#" data-trigger="hover" data-original-title="Stato" data-content="' + escapeHtml(source.statoOperativoDocumentoDesc) + '">' + source.statoOperativoDocumentoCode+ '</a>';
                }},
                {aTargets: [4], mData: defaultPerDataTable('soggetto')},
                {aTargets: [5], mData: defaultPerDataTable('loginModifica')},
                {aTargets: [6], mData: defaultPerDataTable('importo', '0', formatMoney), fnCreatedCell: tabRight},
                {aTargets: [7], mData: defaultPerDataTable('importoDaDedurreSuFattura', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [8], mData: function() {
                    return '<div class="btn-group">' +
                            '<button data-toggle="dropdown" class="btn dropdown-toggle">Azioni<span class="caret"></span></button>' +
                            '<ul class="dropdown-menu pull-right">' +
                                '<li><a href="#" class="consultaDocumentoCollegato">consulta</a></li>' +
                                '<li><a href="#" class="dettaglioDocumentoCollegato">dettaglio quote</a></li>' +
                            '</ul>' +
                        '</div>';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).addClass('tab_Right')
                        .find('.consultaDocumentoCollegato')
                            .substituteHandler('click', consultaDocumento.bind(undefined, oData.uid, oData.tipo))
                            .end()
                        .find('.dettaglioDocumentoCollegato')
                            .substituteHandler('click', caricaQuoteCollegato.bind(undefined, nTd, oData.uid, oData.tipo, oData.tipoDocumento));
                }}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);
    }
    
    function getStringMovimentoGestione(field, fieldSub) {
        return function(source) {
            var res = '';
            if(source[field]) {
                res += source[field].annoMovimento + ' / ' + source[field].numero;
                if(source[fieldSub]) {
                    res += ' - ' + source[fieldSub].numero;
                }
            }
            return res;
        };
    }
    
    function getStringAttoAmministrativo(source) {
        var res = '';
        if(source.attoAmministrativo) {
            res = source.attoAmministrativo.anno + ' / ' + source.attoAmministrativo.numero;
            if(source.attoAmministrativo.tipoAtto) {
                res += ' / ' + source.attoAmministrativo.tipoAtto.codice;
            }
            if(source.attoAmministrativo.strutturaAmmContabile){
                res += ' / ' + source.attoAmministrativo.strutturaAmmContabile.codice;
            }
        }
        return res;
    }

    /**
     *  Carica il corpo della tabella 'Dettaglio quote' della modale 'dettaglio quote'
     *  richiamata  se il documento selezionato NON e' una nota di credito ed è un documento di entrata
     */
    function caricaDettaglioQuoteEntrata(){
    	var isOverlay = false;
        var table = $('#tabellaDettaglioQuoteDocumentoEntrata').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoEntrataCollegatoAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote associate'
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: defaultPerDataTable('movimento')},
                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
                {aTargets: [3], mData: defaultPerDataTable('ordinativo')},
                {aTargets: [4], mData: defaultPerDataTable('dataEmissione')},
                {aTargets: [5], mData: defaultPerDataTable('provvisorio')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);    	
    }

    /**
     *  Carica il corpo della tabella 'Dettaglio quote' della modale 'dettaglio quote'
     *  richiamata  se il documento selezionato NON e' una nota di credito ed e' un documento di spesa
     */
    function caricaDettaglioQuoteSpesa(listaQuote){
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoSpesaAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote associate'
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: defaultPerDataTable('movimento')},
                {aTargets: [2], mData: defaultPerDataTable('provvedimento')},
                {aTargets: [3], mData: defaultPerDataTable('liquidazione')},
                {aTargets: [4], mData: defaultPerDataTable('ordinativo')},
                {aTargets: [5], mData: defaultPerDataTable('dataEmissione')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        $('#tabella_modaleDettaglioQuote').dataTable(opts);
    }

    function caricaDettaglioQuoteNoteCredito(){
        var isOverlay = false;
        var table = $('#tabellaDettaglioQuoteNoteCredito').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoEntrataPerNotaCreditoAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote associate'
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: getStringMovimentoGestione('accertamento', 'subAccertamento')},
                {aTargets: [2], mData: getStringAttoAmministrativo},
                {aTargets: [3], mData: defaultPerDataTable('ordinativo', '', printFields(['anno', 'numero'], '/'))},
                {aTargets: [4], mData: defaultPerDataTable('provvisorioCassa', '', printFields([{fieldname: 'dataEmissione', operation: formatDate}, 'numero'], '/'))},
                {aTargets: [5], mData: defaultPerDataTable('importoDaDedurre', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);
    }

    function consultaDocumento(uid, tipo, event) {
        event.preventDefault();
        if(tipo === 'S'){
            document.location = 'consultaDocumentoSpesa.do?uidDocumento=' + uid;
            // voglio richiamare 'consultaDocumentoSpesa.do'
        } else if (tipo === 'E'){
            // voglio richiamare 'consultaDocumentoEntrata.do'
            document.location = 'consultaDocumentoEntrata.do?uidDocumento=' + uid;
        }
    }


    function caricaQuoteCollegato(td, uid, tipo,tipoDocumento, event) {
        var tr = $(td).closest('tr').overlay('show');
        var uidPadre = $('#hidden_uidDocumento').val();
        event.preventDefault();
        if(tipo === 'E'){
        	if (tipoDocumento === 'NCD'){
        		//E' una nota credito
                caricaTabellaDettaglioQuoteNoteCredito(uidPadre).then(tr.overlay.bind(tr, 'hide'));
        	}
        	else{
        		caricaTabellaDettaglioQuoteDocumentoEntrata(uid).then(tr.overlay.bind(tr, 'hide'));
        	}

        } else if(tipo === 'S'){
            // Se e' documento di spesa
            caricaTabellaDettaglioQuoteDocumentoSpesa(uid).then(tr.overlay.bind(tr, 'hide'));
        } else {
            tr.overlay('hide');
        }
    }

    function caricaTabellaDettaglioQuoteNoteCredito(uid){
        return $.postJSON('consultaDocumentoEntrata_caricaNotaCredito.do', {uidDocumentoPerRicerche: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#modaleNoteCredito_totaleNoteCredito').html(formatMoney(data.notaCredito.importoTotaleNoteCollegate));
            $('#modaleNoteCredito_totaleImportoDaDedurreSuFattura').html(formatMoney(data.notaCredito.importoTotaleDaDedurreSuFatturaNoteCollegate));
            $('#modaleNoteCredito_totaleImportoDaDedurre').html(formatMoney(data.notaCredito.totaleImportoDaDedurreQuote));
            $('#modaleNoteCredito_totaleImporto').html(formatMoney(data.notaCredito.totaleImportoQuote));
            return $.postJSON('consultaDocumentoEntrata_caricamentoQuoteNotaCredito.do');
        }).then(function(data) {
            caricaDettaglioQuoteNoteCredito();
            $('#dettQuoteNoteCredito').modal('show');
        });
    }

    function caricaTabellaDettaglioQuoteDocumentoEntrata(uid){
        return $.postJSON('consultaDocumentoEntrata_caricamentoQuoteEntrata.do', {uidDocumento: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#quoteEntrata_totaleImporto').html(data.totaleQuote.formatMoney());
            caricaDettaglioQuoteEntrata();
            $('#modaleQuotaEntrata').modal('show');
        });
    }
    
    function caricaTabellaDettaglioQuoteDocumentoSpesa(uid){
        return $.postJSON('consultaDocumentoEntrata_caricamentoQuoteSpesa.do', {uidDocumentoSpesa: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#totaleQuote').html(data.totaleQuoteSpesa.formatMoney());
            caricaDettaglioQuoteSpesa();
            $('#modaleDettaglioQuote').modal('show');
        });
    }

    function caricaDettaglioQuotaIvaDifferita(quotaIva) {
        var tipoRegistroIva = quotaIva.registroIva && quotaIva.registroIva.tipoRegistroIva ? quotaIva.registroIva.tipoRegistroIva.descrizione : '';
        var tipoRegistrazioneIva = quotaIva.tipoRegistrazioneIva ? quotaIva.tipoRegistrazioneIva.codice +' - '+ quotaIva.tipoRegistrazioneIva.descrizione : '';
        var attivitaIva = quotaIva.attivitaIva != null ? quotaIva.attivitaIva.codice + ' - ' + quotaIva.attivitaIva.descrizione : '';
        var flagRilevanteIRAP = quotaIva.flagRilevanteIRAP === true ? 'S&iacute;' : 'No';
        var registroIva = quotaIva.registroIva ? quotaIva.registroIva.codice + ' - ' + quotaIva.registroIva.descrizione : '';
        
        $('#progressivoIVA').html(quotaIva.progressivoIVA);
        $('#annoEsercizio').html(quotaIva.annoEsercizio);
        $('#tipoRegistrazioneIva').html(tipoRegistrazioneIva);
        $('#tipoRegistroIva').html(tipoRegistroIva);
        $('#attivitaIva').html(attivitaIva);
        $('#flagRilevanteIRAP').html(flagRilevanteIRAP);
        $('#registroIva').html(registroIva);
        $('#numeroProtocolloDefinitivo').html(quotaIva.numeroProtocolloDefinitivo);
        $('#dataProtocolloDefinitivo').html(formatDate(quotaIva.dataProtocolloDefinitivo));
        $('#descrizioneIva').html(quotaIva.descrizioneIva);
        popolaTabellaMovimentiIva(quotaIva.listaAliquotaSubdocumentoIva);
        $('#totaleImponibileMovimentiIva').html(formatMoney(quotaIva.totaleImponibileMovimentiIva));
        $('#totaleImpostaMovimentiIva').html(formatMoney(quotaIva.totaleImpostaMovimentiIva));
        $('#totaleTotaleMovimentiIva').html(formatMoney(quotaIva.totaleTotaleMovimentiIva));
    }
    
    function popolaTabellaMovimentiIva(listaAliquote) {
        var options = {
            aaData: listaAliquote,
            oLanguage: {
                sZeroRecords: 'Non sono presenti aliquote'
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('aliquotaIva', '', printFields(['codice', 'descrizione'], ' - '))},
                {aTargets: [1], mData: defaultPerDataTable('aliquotaIva.percentualeAliquota')},
                {aTargets: [2], mData: defaultPerDataTable('imponibile', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [3], mData: defaultPerDataTable('imposta', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [4], mData: defaultPerDataTable('impostaDetraibile', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('impostaIndetraibile', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: defaultPerDataTable('totale', 0, formatMoney), fnCreatedCell: tabRight}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        $('#tabellaModaleMovimentiIva').dataTable(opts);
    }

    function impostaTabellaDatiIva(){
        if(!flagDatiIvaAccessibile || !registrazioneSuSingolaQuota) {
            return;
        }
        var options = {
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote associate'
            }
        };
        var opts = $.extend(true, {}, optionsBase, options);
        $('#tabellaQuoteDatiIvaEntrata').dataTable(opts);
    }

    /**
     * Carica la quota.
     *
     * @param e (Event) l'evento scatenante
     */
    function caricaQuota(e) {
        var $this = $(this);
        var href = $this.attr('href');
        var uid = $this.data('uid');
        var $target = $(href);
        var subdocIndex = $this.data('index');
        var loaded = $this.data('loaded');

        // Blocco l'evento
        if(loaded) {
            return;
        }
        e.preventDefault();
        e.stopPropagation();

        // Overlay del target
        $this.overlay('show');
        $.post('consultaDocumentoEntrata_caricaSubdocumento.do', {'subdocumentoEntrata.uid': uid, 'subdocIndex': subdocIndex})
        .then(function(data) {
            if(typeof data !== 'string' && data.errori && impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            
            $target.html(data);
            $this.data('loaded', true);
            $target.collapse('toggle');
        }).always($this.overlay.bind($this, 'hide'));
    }
    
    /**
     * Carica il dettaglio della registrazione e mostra la modale
     * @params e (Event) l'evento scatenante
     */
    function caricaDettaglioRegistrazione(e) {
        var $this = $(this);
        var uid = $this.data('uid');
        var tr = $this.closest('tr').overlay('show');
        e.preventDefault();
        $.postJSON('consultaDocumentoEntrata_caricaDettaglioRegistrazioneIva.do', {uidQuotaIvaDifferita: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaDettaglioQuotaIvaDifferita(data.quotaIvaDifferita);
            $('#quotaIvaDifferita').modal('show');
        }).always(tr.overlay.bind(tr, 'hide'));
        
    }

    // SIAC-3965
    /**
     * Caricamento delle quote
     * @returns (Promise) la promise collegata al caricamento delle quote
     */
    function caricamentoQuote() {
        var tabKey = $('a[data-toggle="tab"][href="#quoteDocumentoEntrata"]').overlay('show');
        var uid = $('#hidden_uidDocumento').val();
        var idWorkaround = '_' + uid + '_';

        return $.postJSON('consultaDocumentoEntrata_caricamentoQuote.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $('#numeroQuoteDocumento').html(data.numeroQuote);
            caricaTabellaQuote(idWorkaround);
        }).always(tabKey.overlay.bind(tabKey, 'hide'));
    }

    /**
     * Caricamento della tabella delle quote
     * @param idWorkaround (string) l'id workaround per gestire i collapse come accordion
     * @returns (jQuery) la tabella
     */
    function caricaTabellaQuote(idWorkaround) {
        var isOverlay = false;
        var table = $('#tabellaElencoQuote').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoEntrataAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote associate'
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: computeAccordionSubdocumento.bind(undefined, idWorkaround)}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);
        return table;
    }

    /**
     * Calcola la testata dell'accordion del subdocumento
     * @param idWorkaround (string) l'id workaround per gestire i collapse come accordion
     * @param source       (any)    l'oggetto ottenuto dall'invocazione
     * @returns (string) la stringa HTML rappresentante l'accordion
     */
    function computeAccordionSubdocumento(idWorkaround, source) {
        return '<div class="accordion-group">'
            + '<div class="accordion-heading ">'
                + '<a class="accordion-toggle collapsed"  data-parent="#accordionQuote' + idWorkaround + '" href="#quota' + idWorkaround + '_' + source.uid + '" data-toggle="collapse" data-uid="' + source.uid + '" data-loaded="false">'
                    + 'N. ' + source.numero + '&nbsp;'
                    + '- Acc: ' + source.movimento + '&nbsp;'
                    + '- Reversale: ' + source.ordinativo + '&nbsp;'
                    + '- Provv: ' + source.provvedimento + '&nbsp;'
                    + '- Importo: ' + source.importo.formatMoney() + '&nbsp;'
                    + '<span class="icon"></span>'
                + '</a>'
            + '</div>'
            + '<div id="quota' + idWorkaround + '_' + source.uid + '" class="accordion-body collapse"></div>'
        + '</div>';
    }

    function caricaTabellaDocumentiCollegati(){
        var tabKey = $('a[data-toggle="tab"][href="#documentiCollegatiDocumentoEntrata"]').overlay('show');

        return $.postJSON('consultaDocumentoEntrataOttieniListaDocumentiCollegati.do')
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaDocumentiNellaTabella(data.listaDocumentiCollegati);
        }).always(tabKey.overlay.bind(tabKey, 'hide'));
    }

    function caricaTabellaQuoteIva() {
        if(!flagDatiIvaAccessibile || !registrazioneSuSingolaQuota) {
            return $.Deferred().resolve().promise();
        }
        var tabKey = $('a[data-toggle="tab"][href="#datiIvaDocumentoEntrata"]').overlay('show');
        
        return $.postJSON('consultaDocumentoEntrata_caricamentoQuoteIva.do')
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaQuoteIvaNellaTabella();
        }).always(tabKey.overlay.bind(tabKey, 'hide'));
    }

    function caricaQuoteIvaNellaTabella() {
        var isOverlay = false;
        var table = $('#tabellaQuoteDatiIvaEntrata').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoEntrataPerIvaAjax.do',
            sServerMethod: 'POST',
            oLanguage: {
                sZeroRecords: 'Non sono presenti quote rilevanti'
            },
            fnPreDrawCallback: function() {
                if(isOverlay) {
                    return;
                }
                table.overlay('show');
                isOverlay = true;
            },
            fnDrawCallback: function () {
                table.overlay('hide');
                isOverlay = false;
                table.find('.dropdown-toggle').dropdown();
            },
            // Definizione delle colonne
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('numero')},
                {aTargets: [1], mData: defaultPerDataTable('movimento')},
                {aTargets: [2], mData: defaultPerDataTable('stringaCapitolo')},
                {aTargets: [3], mData: defaultPerDataTable('stringaAttivitaIva')},
                {aTargets: [4], mData: defaultPerDataTable('numeroRegistrazioneIva')},
                {aTargets: [5], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: defaultPerDataTable('totaleMovimentiIva', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [7], mData: function(source) {
                    if(!source || !source.uidSubdocumentoIva) {
                        return '';
                    }
                    return '<div class="btn-group">'
                        + '<button data-toggle="dropdown" class="btn dropdown-toggle">'
                            + 'Azioni<span class="caret"></span>'
                        + '</button>'
                        + '<ul class="dropdown-menu pull-right">'
                            + '<li><a href="consultaDocumentoIvaEntrata.do?uidDocumentoIvaDaConsultare=' + source.uidSubdocumentoIva + '" >consulta</a></li>'
                        + '</ul>'
                    + '</div>';
                }}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);
        return table;
    }

    $(function(){
        caricamentoQuote();
        caricaTabellaDocumentiCollegati();
        caricaTabellaQuoteIva();
        impostaTabellaDatiIva();
        
        $('div[id^="accordionQuote"]').on('click', '.accordion-toggle', caricaQuota);
        $('#accordionQuote').on('click', '.accordion-toggle', caricaQuota);
        $('#quotePagateTable').on('click', '.dettaglioRegistrazione', caricaDettaglioRegistrazione);
    });

}(this, jQuery));
