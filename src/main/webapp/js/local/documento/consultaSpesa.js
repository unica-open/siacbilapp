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
    var flagRitenuteAccessibile = $('#hidden_flagRitenuteAccessibile').val() === 'true';
    var registrazioneSuSingolaQuota = $('#hidden_registrazioneSuSingolaQuota').val() === 'true';
    var nameNaturaEsenti = "ESENTI";
    
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
     * Carica il corpo della tabella 'Elenco Oneri' del tab 'Ritenute'
     */
    function caricaOneriNellaTabella(listaOneri){
        var options = {
            aaData: listaOneri,
            oLanguage: {
                sZeroRecords: 'Non sono presenti ritenute associate'
            },
            aoColumnDefs: [
                {aTargets: [0], mData: defaultPerDataTable('tipoOnere.naturaOnere', '', printFields(['codice', 'descrizione'], ' - '))},
                {aTargets: [1], mData: defaultPerDataTable('tipoOnere', '', printFields(['codice', 'descrizione'], ' - '))},
                {aTargets: [2], mData: defaultPerDataTable('importoImponibile', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [3], mData: defaultPerDataTable('tipoOnere.aliquotaCaricoSoggetto', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [4], mData: defaultPerDataTable('importoCaricoSoggetto', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [5], mData: defaultPerDataTable('tipoOnere.aliquotaCaricoEnte', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [6], mData: defaultPerDataTable('importoCaricoEnte', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [7], mData: defaultPerDataTable('sommaNonSoggetta', 0, formatMoney), fnCreatedCell: tabRight},
                {aTargets: [8], mData: function(source) {
                    return '<div class="btn-group">' +
                        '<button data-toggle="dropdown" class="btn dropdown-toggle">Azioni<span class="caret"></span></button>' +
                        '<ul class="dropdown-menu pull-right">' +
                            '<li><a href="#" class="dettaglioOnere">dettaglio</a></li>' +
                        '</ul>' +
                    '</div>';
                }, fnCreatedCell: function(nTd, sData, oData) {
                    $(nTd).find('.dettaglioOnere')
                            .substituteHandler('click', function(e) {
                                apriModaleDettaglio(e, oData);
                            })
                            .end()
                        .find('.dropdown-toggle')
                            .dropdown()
                            .end()
                        .addClass('tab_Right');
                }}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        $('#tabellaRitenute').dataTable(opts);
    }

    /**
     *  Carica il corpo della tabella 'Documenti collegati' del tab 'Documenti collegati'
     */
    function caricaDocumentiNellaTabella(listaDocumentiCollegati){
        var table = $('#tabellaDocumentiCollegati');
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
                    return '<a rel="popover" href="#" data-trigger="hover" data-original-title="Stato" data-content="' + escapeHtml(source.statoOperativoDocumentoDesc) + '">' + source.statoOperativoDocumentoCode + '</a>';
                }},
                {aTargets: [4], mData: defaultPerDataTable('soggetto')},
                {aTargets: [5], mData: defaultPerDataTable('loginModifica')},
                {aTargets: [6], mData: defaultPerDataTable('importo', 0, formatMoney), fnCreatedCell: tabRight},
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
     *  Carica il corpo della tabella 'Dettaglio quote' della modale 'dettaglio quote con importo da dedurre'
     *  (richiamata solo se il documento selezionato e' una nota di credito)
     */
    function caricaDettaglioQuoteNoteCredito(){
        var isOverlay = false;
        var table = $('#tabellaDettaglioQuoteNoteCredito').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoSpesaPerNotaCreditoAjax.do',
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
                {aTargets: [3], mData: defaultPerDataTable('liquidazione')},
                {aTargets: [4], mData: defaultPerDataTable('ordinativo')},
                {aTargets: [5], mData: defaultPerDataTable('importoDaDedurre', 0, formatMoney), fnCreatedCell: tabRight},
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
    function caricaDettaglioQuoteSpesa(){
        
        var options = {
                bServerSide: true,
                sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoSpesaCollegatoAjax.do',
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

    /**
     *  Carica il corpo della tabella 'Dettaglio quote' della modale 'dettaglio quote'
     *  Richiamata se il documento selezionato NON e' una nota di credito ed e' un documento di entrata
     */
    function caricaDettaglioQuoteEntrata(){
        var isOverlay = false;
        var table = $('#tabellaDettaglioQuoteDocumentoEntrata').overlay({rebind: true, loader: true, usePosition: true});
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

    function consultaDocumento(uid, tipo, event) {
        event.preventDefault();
        if(tipo === 'S'){
            document.location = 'consultaDocumentoSpesa.do?uidDocumento=' + uid;
        } else if (tipo === 'E'){
            document.location = 'consultaDocumentoEntrata.do?uidDocumento=' + uid;
        }
    }

    function caricaQuoteCollegato(td, uid, tipo, tipoDocumento, event) {
        var tr = $(td).closest('tr').overlay('show');
        var uidPadre = $('#hidden_uidDocumento').val();
        event.preventDefault();

        // Se e' un documento di spesa
        if( tipo === 'S') {
        	if (tipoDocumento === 'NCD') {
            // E' una nota credito
        		caricaTabellaDettaglioQuoteNoteCredito(uidPadre).always(tr.overlay.bind(tr, 'hide'));
        	} else {
        		caricaTabellaDettaglioQuoteDocumentoSpesa(uid).always(tr.overlay.bind(tr, 'hide'));
        	}
        } else if(tipo === 'E') {
            // Se e' documento di entrata
            caricaTabellaDettaglioQuoteDocumentoEntrata(uid).always(tr.overlay.bind(tr, 'hide'));
        } else {
            tr.overlay('hide');
        }
    }

    /**
    * carica i campi della modale 'dettaglio ritenute'
    *
    **/
    function apriModaleDettaglio(event, dettaglioOnere) {
        var modale = $('#dettRitenute');
        var naturaOnere = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.naturaOnere
            && dettaglioOnere.tipoOnere.naturaOnere.codice + ' - ' + dettaglioOnere.tipoOnere.naturaOnere.descrizione
            || '';
        var tipoOnere = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.codice || '';
        var importoImponibile = dettaglioOnere && dettaglioOnere.importoImponibile || 0;
        var aliquotaCaricoSoggetto = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.aliquotaCaricoSoggetto || 0;
        var aliquotaCaricoEnte = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.aliquotaCaricoEnte || 0;
        var importoCaricoSoggetto = dettaglioOnere && dettaglioOnere.importoCaricoSoggetto || 0;
        var importoCaricoEnte = dettaglioOnere && dettaglioOnere.importoCaricoEnte || 0;
        var sommaNonSoggetta = dettaglioOnere && dettaglioOnere.sommaNonSoggetta || 0;
        var quadro770 = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.quadro770 || '';
        var tipoIvaSplitReverse = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.tipoIvaSplitReverse
            && dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice + ' - ' + dettaglioOnere.tipoOnere.tipoIvaSplitReverse.descrizione
            || '';
        var reversale = '';
        if(dettaglioOnere != null && dettaglioOnere.subordinativiIncasso != null) {
            reversale = dettaglioOnere.subordinativiIncasso
                .filter(function(val) {
                    return !val.statoOperativoOrdinativo || !val.statoOperativoOrdinativo._name || val.statoOperativoOrdinativo._name !== 'ANNULLATO';
                }).map(function(val) {
                    return val.anno + '/' + val.numeroOrdinativo + '/' + val.numero;
                }).join(', ');
        }

        var codiceNaturaOnere = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.naturaOnere && dettaglioOnere.tipoOnere.naturaOnere.codice || '';
        var codiceSplitReverse = dettaglioOnere && dettaglioOnere.tipoOnere && dettaglioOnere.tipoOnere.tipoIvaSplitReverse && dettaglioOnere.tipoOnere.tipoIvaSplitReverse.codice || '';

        var campiNatura = modale.find('[data-hidden-natura]');
        var campiNaturaHide = campiNatura.filter('[data-hidden-natura~="' + codiceNaturaOnere + '"]');
        var campiSplitReverse = modale.find('[data-hidden-split-reverse]');
        var campiSplitReverseHide = campiSplitReverse.filter('[data-hidden-split-reverse~="' + codiceSplitReverse + '"]');

        event.preventDefault();

        $('#naturaOnereModaleRitenute').html(naturaOnere);
        $('#codiceTributoModaleRitenute').html(tipoOnere);
        $('#imponibileModaleRitenute').html(importoImponibile.formatMoney());
        $('#aliquotaSoggettoModaleRitenute').html(aliquotaCaricoSoggetto.formatMoney());
        $('#aliquotaEnteModaleRitenute').html(aliquotaCaricoEnte.formatMoney());
        $('#importoSoggettoModaleRitenute').html(importoCaricoSoggetto.formatMoney());
        $('#importoEnteModaleRitenute').html(importoCaricoEnte.formatMoney());
        $('#sommaNonModaleRitenute').html(sommaNonSoggetta.formatMoney());
        $('#quadro770ModaleRitenute').html(quadro770);
        $('#reversaleModaleRitenute').html(reversale);

        if(dettaglioOnere && dettaglioOnere.attivitaOnere) {
            $('#attivitaModaleRitenute').html(dettaglioOnere.attivitaOnere.codice + ' - ' + dettaglioOnere.attivitaOnere.codice);
            $('#periodoDalModaleRitenute').html(formatDate(dettaglioOnere.attivitaInizio));
            $('#periodoAlModaleRitenute').html(formatDate(dettaglioOnere.attivitaFine));
        } else {
            $('#spanPeriodoRitenute').hide();
        }
        if(dettaglioOnere && dettaglioOnere.causale770) {
            $('#causale770ModaleRitenute').html(dettaglioOnere.causale770.codice + ' - ' + dettaglioOnere.causale770.descrizione);
        } else {
            $('#causale770ModaleRitenute').html('');
        }
        if(dettaglioOnere && dettaglioOnere.tipoSommaNonSoggetta) {
            $('#codiceSommaNonModaleRitenute').html(dettaglioOnere.tipoSommaNonSoggetta.codice + ' - ' + dettaglioOnere.tipoSommaNonSoggetta.descrizione);
        } else {
            $('#codiceSommaNonModaleRitenute').html('');
        }

        // Lotto L
        $('#tipoSplitReverseModaleRitenute').html(tipoIvaSplitReverse);
        $('#dfnImponibileModaleRitenute').html(codiceNaturaOnere === 'ES' ? 'Importo' : 'Imponibile');
        $('#dfnAliquotaSoggettoModaleRitenute').html(codiceSplitReverse === 'SI' || codiceSplitReverse === 'SC' ? 'Aliquota' : 'Aliquota a carico soggetto');
        $('#dfnImportoSoggettoModaleRitenute').html(codiceNaturaOnere === 'SP' ? 'Imposta' : 'Importo a carico del soggetto');

        // Nascondi i campi necessari
        modale.find('[data-show-natura-onere]').hide()
            .filter('[data-show-natura-onere~="' + codiceNaturaOnere + '"]').show();
        campiNatura.not(campiNaturaHide).show();
        campiNaturaHide.hide();
        campiSplitReverse.not(campiSplitReverseHide).show();
        campiSplitReverseHide.hide();
        modale.modal('show');
    }

    /**
     * modale 'dettaglio quote con importo da dedurre'
     */
    function caricaTabellaDettaglioQuoteNoteCredito(uid){
        return $.postJSON('consultaDocumentoSpesa_caricaNotaCredito.do', {uidDocumentoPerRicerche: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#modaleNoteCredito_totaleNoteCredito').html(formatMoney(data.notaCredito.importoTotaleNoteCollegate));
            $('#modaleNoteCredito_totaleImportoDaDedurreSuFattura').html(formatMoney(data.notaCredito.importoTotaleDaDedurreSuFatturaNoteCollegate));
            $('#modaleNoteCredito_totaleImportoDaDedurre').html(formatMoney(data.notaCredito.totaleImportoDaDedurreQuote));
            $('#modaleNoteCredito_totaleImporto').html(formatMoney(data.notaCredito.totaleImportoQuote));
            return $.postJSON('consultaDocumentoSpesa_caricamentoQuoteNotaCredito.do');
        }).then(function(data) {
            caricaDettaglioQuoteNoteCredito();
            $('#dettQuoteNoteCredito').modal('show');
        });
    }

    // JIRA 5154
    function caricaTabellaDettaglioQuoteDocumentoSpesa(uid){
        return $.postJSON('consultaDocumentoSpesa_caricamentoQuoteSpesa.do', {uidDocumento: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#totaleQuote').html(formatMoney(data.totaleQuote));
            caricaDettaglioQuoteSpesa();
            $('#modaleDettaglioQuote').modal('show');
        });
    }
    
    
    function caricaTabellaDettaglioQuoteDocumentoEntrata(uid){
        return $.postJSON('consultaDocumentoSpesa_caricamentoQuoteEntrata.do', {uidDocumentoEntrata: uid})
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            $('#quoteEntrata_totaleImporto').html(formatMoney(data.totaleQuoteEntrata));
            caricaDettaglioQuoteEntrata();
            $('#modaleQuotaEntrata').modal('show');
        });
    }

    /**
     *
     *
     */
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

    /**
    *
    **/
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
        $('#tabellaQuoteDatiIvaSpesa').dataTable(opts);
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
        $.post('consultaDocumentoSpesa_caricaSubdocumento.do', {'subdocumentoSpesa.uid': uid, 'subdocIndex': subdocIndex})
        .then(function(data) {
            if(typeof data !== 'string' && data.errori && impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            $target.html(data);
            // Riscrivere
            $target.find('.visualizzaComunicazioniPCC').one('click', caricaPCC);
            // SIAC-5115
            attivaDataTableSospensioni($target);
            $this.data('loaded', true);
            $target.collapse('toggle');
        }).always($this.overlay.bind($this, 'hide'));
    }

    function attivaDataTableSospensioni($target) {
        var opts = {
            bServerSide: false,
            bDestroy: true,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti sospensioni collegate alla quota",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            }
        };
        $target.find('table[data-table-sospensione]').dataTable(opts);
    }

    /**
     * Carica il dettaglio della registrazione e mostra la modale
     * @param e (Event) l'evento scatenante
     */
    function caricaDettaglioRegistrazione(e) {
        var $this = $(this);
        var uid = $this.data('uid');
        var tr = $this.closest('tr').overlay('show');
        e.preventDefault();

        $.postJSON('consultaDocumentoSpesa_caricaDettaglioRegistrazioneIva.do', {uidQuotaIvaDifferita: uid})
        .then(function(data){
             if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                 return;
             }
            caricaDettaglioQuotaIvaDifferita(data.quotaIvaDifferita);
            $('#quotaIvaDifferita').modal('show');
        }).always(tr.overlay.bind(tr, 'hide'));
    }

    /**
     * Caricamento degli ordini.
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function caricaOrdini(e) {
        var overlay = $('#accordionOrdini').overlay('show');

        e.preventDefault();
        e.stopPropagation();

        $.post('consultaDocumentoSpesa_caricaOrdini.do')
        .then(function(data) {
            var opts = $.extend(true, {}, optionsBase, {oLanguage: {sZeroRecords: 'Non sono presenti ordini associati'}});
            $('#collapseOrdini').html(data);
            $('#tabellaOrdini').dataTable(opts);
            $('#collapseOrdini').collapse('show');
        }).always(overlay.overlay.bind(overlay, 'hide'));
    }

    // Lotto O

    /**
     * Carica la visualizzazione PCC.
     *
     * @param e (Event) l'evento scatenante
     */
    function caricaPCC(e) {
        var $this = $(this);
        var $target = $($this.data('target'));
        var uid = $this.data('uid');

        // Blocco l'evento
        e.preventDefault();
        e.stopPropagation();

        // Overlay del target
        $this.overlay('show');
        $.post('consultaRegistroComunicazioniPCCSubdocumentoSpesaInVisualizzazione.do', {'subdocumentoSpesa.uid': uid})
        .then(function(data) {
            $target.html(data);
            $target.collapse('show'); 
            //workaround dovuto al fatto che le tabelle sono all'interno di una tabella per cui vale .table-accordion thead {display: none;}
            $target.find('thead').each(function(){
            	$(this).show();
            });
            RegistroComunicazioniPCC.activateDataTableOnTable('table[data-table="contabilizzazioni"]', {oLanguage: {sZeroRecords: 'Non sono presenti contabilizzazioni', oPaginate: {sEmptyTable: 'Nessuna contabilizzazione disponibile'}}});
            RegistroComunicazioniPCC.activateDataTableOnTable('table[data-table="comunicazioniDataScadenza"]', {iDisplayLength: 5, oLanguage: {sZeroRecords: 'Non sono presenti comunicazioni per la data di scadenza', oPaginate: {sEmptyTable: 'Nessuna comunicazione per data di scadenza disponibile'}}});
            RegistroComunicazioniPCC.activateDataTableOnTable('table[data-table="comunicazioniPagamento"]', {oLanguage: {sZeroRecords: 'Non sono presenti comunicazioni per il pagamento', oPaginate: {sEmptyTable: 'Nessuna comunicazione per pagamento disponibile'}}});
        }).always($this.overlay.bind($this, 'hide'));
    }

    // SIAC-3965
    /**
     * Caricamento delle quote
     * @returns (Promise) la promise collegata al caricamento delle quote
     */
    function caricamentoQuote() {
        var tabKey = $('a[data-toggle="tab"][href="#quote"]').overlay('show');
        var uid = $('#hidden_uidDocumento').val();
        var idWorkaround = '_' + uid + '_';

        return $.postJSON('consultaDocumentoSpesa_caricamentoQuote.do')
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
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoSpesaAjax.do',
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
                    + '- Imp: ' + source.movimento + '&nbsp;'
                    + '- Provv: ' + source.provvedimento + '&nbsp;'
                    + '- Liq: ' + source.liquidazione + '&nbsp;'
                    + '- Ord: ' + source.ordinativo + '&nbsp;'
                    + '- Importo: ' + source.importo.formatMoney() + '&nbsp;'
                    + '<span class="icon"></span>'
                + '</a>'
            + '</div>'
            + '<div id="quota' + idWorkaround + '_' + source.uid + '" class="accordion-body collapse"></div>'
        + '</div>';
    }
    
    function caricaTabellaOneri(){
        if(!flagRitenuteAccessibile) {
            return $.Deferred().resolve().promise();
        }
        var tabKey = $('a[data-toggle="tab"][href="#ritenute"]').overlay('show');

        return $.postJSON('consultaDocumentoSpesa_caricamentoRitenuteDocumento.do')
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaOneriNellaTabella(data.listaOnere);
        }).always(tabKey.overlay.bind(tabKey, 'hide'));
    }

    function caricaTabellaDocumentiCollegati(){
        var tabKey = $('a[data-toggle="tab"][href="#documentiCollegati"]').overlay('show');

        return $.postJSON('consultaDocumentoSpesaOttieniListaDocumentiCollegati.do')
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
        var tabKey = $('a[data-toggle="tab"][href="#datiIva"]').overlay('show');
        
        return $.postJSON('consultaDocumentoSpesa_caricamentoQuoteIva.do')
        .then(function(data){
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return;
            }
            caricaQuoteIvaNellaTabella();
        }).always(tabKey.overlay.bind(tabKey, 'hide'));
    }
    
    function caricaQuoteIvaNellaTabella() {
        var isOverlay = false;
        var table = $('#tabellaQuoteDatiIvaSpesa').overlay({rebind: true, loader: true, usePosition: true});
        var options = {
            bServerSide: true,
            sAjaxSource: 'risultatiRicercaSinteticaQuoteDocumentoSpesaPerIvaAjax.do',
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
                            + '<li><a href="consultaDocumentoIvaSpesa.do?uidDocumentoIvaDaConsultare=' + source.uidSubdocumentoIva + '" >consulta</a></li>'
                        + '</ul>'
                    + '</div>';
                }}
            ]
        };
        var opts = $.extend(true, {}, optionsBase, options);
        table.dataTable(opts);
        return table;
    }
    
    /**
     * Caricamento dei dati della fattura. Il caricamento viene effettuato una sola volta
     *
     * @param e (Event) l'evento scatenante l'invocazione
     */
    function caricaDatiFatture(e) {
        var pulsante = $("#datiFatturePulsante").overlay("show");
        e.preventDefault();

        $.postJSON('consultaDocumentoSpesa_caricaFatturaFEL.do', {}, function(data) {
            var totaleImponibile;
            var totaleImposta;
            var totaleEsente;
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                // Ho errori: esco
                return;
            }
            // Carico la tabella dei dati della fattura
            populateTableFattura(data.fatturaFEL && data.fatturaFEL.riepiloghiBeni || []);

            // Imposto i totali
            totaleImponibile = data.totaleImponibileFEL || 0;
            totaleImposta = data.totaleImpostaFEL || 0;
            totaleEsente = data.totaleEsenteFEL || 0;
            $("th[data-totale-imponibile]", "#tabellaDatiFatture").html(totaleImponibile.formatMoney());
            $("th[data-totale-imposta]", "#tabellaDatiFatture").html(totaleImposta.formatMoney());
            $("th[data-totale-esente]", "#tabellaDatiFatture").html(totaleEsente.formatMoney());

            // Lego l'apertura normale dell'accordion
            pulsante.substituteHandler("click", function(e) {
                e.preventDefault();
                $("#datiFattureAccordion").collapse("toggle");
            }).click();
        }).always(function() {
            pulsante.overlay("hide");
        });
    }
    /**
     * Popolamento della tabella delle fatture.
     * 
     * @param list (Array) la lista tramite cui caricare la tabella
     */
    function populateTableFattura(list) {
        var opts = {
            bServerSide: false,
            bPaginate: false,
            bLengthChange: false,
            iDisplayLength: 5,
            bSort: false,
            bInfo: true,
            bAutoWidth: true,
            bFilter: false,
            bProcessing: true,
            aaData: list,
            bDestroy: true,
            oLanguage: {
                sInfo: "_START_ - _END_ di _MAX_ risultati",
                sInfoEmpty: "0 risultati",
                sProcessing: "Attendere prego...",
                sZeroRecords: "Non sono presenti riepiloghi beni associati",
                oPaginate: {
                    sFirst: "inizio",
                    sLast: "fine",
                    sNext: "succ.",
                    sPrevious: "prec.",
                    sEmptyTable: "Nessun dato disponibile"
                }
            },
            aoColumnDefs: [
                {aTargets: [0], mData: function(source){
                    return source.progressivo || "";
                }},
                {aTargets: [1], mData: function(source) {
                    return source.naturaFEL && source.naturaFEL.codice || "";
                }},
                {aTargets: [2], mData: function(source) {
                    return source.aliquotaIvaNotNull.formatMoney();
                }, fnCreatedCell: tabRight},
                {aTargets: [3], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? "" : source.imponibileImportoNotNull.formatMoney();
                }, fnCreatedCell: tabRight},
                {aTargets: [4], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? "" : source.impostaNotNull.formatMoney();
                }, fnCreatedCell: tabRight},
                {aTargets: [5], mData: function(source) {
                    return isNaturaFELEsente(source.naturaFEL) ? source.imponibileImportoNotNull.formatMoney() : "";
                }, fnCreatedCell: tabRight}
            ]
        };
        $("#tabellaDatiFatture").dataTable(opts);
    }
    /**
     * Controlla se la natura FEL sia 'ESENZIONE'.
     *
     * @param naturaFEL (Object) la natura da controllare
     * @returns (Boolean) true se la natura e' ?ESENZIONE', false altrimenti
     */
    function isNaturaFELEsente(naturaFEL) {
        return naturaFEL && naturaFEL._name === nameNaturaEsenti;
    }

    $(function(){
        caricamentoQuote();
        caricaTabellaOneri();
        caricaTabellaDocumentiCollegati();
        caricaTabellaQuoteIva();
        impostaTabellaDatiIva();

        $('div[id^="accordionQuote"]').on('click', '.accordion-toggle', caricaQuota);
        $('a.accordion-toggle', '#accordionOrdini').one('click', caricaOrdini);
        $('#quotePagateTable').on('click', '.dettaglioRegistrazione', caricaDettaglioRegistrazione);
        $("#datiFatturePulsante").one("click", caricaDatiFatture);
    });
}(this, jQuery));
