/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    'use strict';
    var exports = {};
    var msgImpostatoDalSistema = 'Impostato dal sistema';
    var instance;

    var ProvvedimentoInserimento = function(prefix, suffix, ztree) {
        this.prefix = prefix || '';
        this.suffix = suffix || '';
        this.ztree = ztree;

        this.underscorePrefix = this.prefix ? '_' + this.prefix : '';
        this.hrefPulsanteSAC = '#' + this.prefix + '_struttAmm';
        this.treeSAC = this.prefix + '_treeStruttAmm';
        this.fields = [];
        // Brutto, ma non voglio riscrivere lo ztree
        this.spanSAC = $('#SPAN_StrutturaAmministrativoContabile' + this.underscorePrefix);
        this.strutturaAmministrativoContabileUid = $('#HIDDEN_StrutturaAmministrativoContabile' + this.underscorePrefix + 'Uid');
        this.fields.push(this.strutturaAmministrativoContabileUid);

        this.modale = $('#' + this.prefix);
        this.alertErrori = $('#' + this.prefix + '_ERRORI');
        this.pulsanteApertura = $('#' + this.prefix + '_pulsanteApertura');
        this.pulsanteConferma = $('#' + this.prefix + '_pulsanteConferma');

        // Add fields
        this.addField('errori', false, true);
        this.addField('fieldset');
        this.addField('anno', true);
        this.addField('numero', true);
        this.addField('tipoAtto', true);
        this.addField('oggetto', true);
        this.addField('statoOperativo', true);
        this.addField('note', true);

        this.originalFieldAnno = $('#annoProvvedimento' + this.suffix);
        this.originalFieldNumero = $('#numeroProvvedimento' + this.suffix);
        this.originalFieldTipo = $('#tipoAtto' + this.suffix);
        this.originalFieldStato = $('#HIDDEN_statoProvvedimento' + this.suffix);
        this.originalFieldStrutturaAmm = $('#HIDDEN_StrutturaAmministrativoContabile' + this.suffix + 'Uid');
        this.originalFieldInformazioni = $("#SPAN_InformazioniProvvedimento" + this.suffix);

        this.listaSAC = undefined;
    };

    ProvvedimentoInserimento.prototype.constructor = ProvvedimentoInserimento;
    ProvvedimentoInserimento.prototype.addField = addField;
    ProvvedimentoInserimento.prototype.init = init;
    ProvvedimentoInserimento.prototype.open = open;
    ProvvedimentoInserimento.prototype.destroy = destroy;
    ProvvedimentoInserimento.prototype.isProgressivoAutomatico = isProgressivoAutomatico;
    ProvvedimentoInserimento.prototype.handleChangeTipoAtto = handleChangeTipoAtto;
    ProvvedimentoInserimento.prototype.loadSAC = loadSAC;
    ProvvedimentoInserimento.prototype.reinsertStatoOperativoProvvisorio = reinsertStatoOperativoProvvisorio;
    ProvvedimentoInserimento.prototype.confirm = confirm;
    ProvvedimentoInserimento.prototype.setProvvedimento = setProvvedimento;

    exports.inizializzazione = inizializzazione;
    global.ProvvedimentoInserimento = exports;

    function addField(fieldName, addToFields, upperCase) {
        var field = $('#' + this.prefix + '_' + (upperCase ? fieldName.toUpperCase() : fieldName));
        this[fieldName] = field;
        if(addToFields) {
            this.fields.push(field);
        }
    }
    function init() {
        this.pulsanteApertura.eventPreventDefault('click', this.open.bind(this));
        this.pulsanteConferma.eventPreventDefault('click', this.confirm.bind(this));
        this.loadSAC();
    }
    function open() {
        this.destroy(false);
        this.spanSAC.html('Seleziona la Struttura amministrativa');
        // Pulizia dei campi
        this.fields.forEach(function(el) {
            el.val('');
            el.removeAttr('disabled');
        });

        this.tipoAtto.substituteHandler('change', this.handleChangeTipoAtto.bind(this));
        this.loadSAC();
        this.modale.modal('show');
    }
    function destroy(full) {
        this.tipoAtto.off('change');
        if(full) {
            this.pulsanteApertura.off('click');
            this.pulsanteConferma.off('click');
        }
    }
    function isProgressivoAutomatico () {
        return this.tipoAtto.find('option:selected').data('progressivoAutomatico') !== undefined;
    }
    function handleChangeTipoAtto () {
        if(this.isProgressivoAutomatico()) {
            this.numero.attr('disabled', 'disabled');
            this.numero.val(msgImpostatoDalSistema);
            this.statoOperativo.val('DEFINITIVO');
            this.statoOperativo.find('option[value="PROVVISORIO"]').remove();
            return;
        }
        this.numero.removeAttr('disabled');
        if (this.numero.val().toUpperCase() === msgImpostatoDalSistema.toUpperCase()) {
            this.numero.val('');
        }
        this.reinsertStatoOperativoProvvisorio();
        this.statoOperativo.val('PROVVISORIO');
    }
    function reinsertStatoOperativoProvvisorio() {
        if(this.statoOperativo.find('option[value="PROVVISORIO"]').length === 0) {
            this.statoOperativo.find('option:first').after('<option value="PROVVISORIO">PROVVISORIO</option>');
        }
    }
    function loadSAC() {
        var that = this;
        var promise;
        if(this.listaSAC === undefined) {
            promise = $.postJSON('ajax/strutturaAmministrativoContabileAjax.do')
                .then(function (data) {
                    that.listaSAC = data.listaElementoCodifica;
                    return data.listaElementoCodifica;
                });
        } else {
            promise = $.Deferred().resolve(this.listaSAC).promise();
        }
        promise.then(this.ztree.imposta.bind(this.ztree, this.treeSAC, this.ztree.SettingsBase, this.listaSAC, '', this.underscorePrefix));
    }
    function confirm() {
        var obj = unqualify(this.fieldset.serializeObject(), 1);
        var alertErrori = this.alertErrori;
        this.fieldset.overlay('show');
        $.postJSON('inserisciProvvedimento_inserimento.do', obj)
        .then(function(data) {
            if(impostaDatiNegliAlert(data.errori, alertErrori)) {
                return $.Deferred().reject().promise();
            }
            return data.attoAmministrativo;
        }).then(this.setProvvedimento.bind(this))
        .always(this.fieldset.overlay.bind(this.fieldset, 'hide'));
    }
    function setProvvedimento(provvedimento) {
        // Tolgo l'eventuale hidden del tipo
        var uidTipoAtto = provvedimento.tipoAtto && provvedimento.tipoAtto.uid || '';
        var descTipo = provvedimento.tipoAtto && provvedimento.tipoAtto.descrizione || '';
        var descSAC = provvedimento.strutturaAmmContabile && provvedimento.strutturaAmmContabile.codice + '-' + provvedimento.strutturaAmmContabile.descrizione || '';
        var uidSAC = provvedimento.strutturaAmmContabile && provvedimento.strutturaAmmContabile.uid || '';
        var hiddenStr = '<input type="hidden" name="' + this.originalFieldTipo.attr('name') + '" value="' + uidTipoAtto + '"/>';
        var informazioni = [provvedimento.anno + ' / ' + provvedimento.numero, descTipo, provvedimento.oggetto, descSAC, 'Stato: ' + provvedimento.statoOperativo];

        $('input[type="hidden"][name="' + this.originalFieldTipo.attr('name') + '"]').remove();

        this.originalFieldAnno.val(provvedimento.anno).attr('readonly', 'readonly');
        this.originalFieldNumero.val(provvedimento.numero).attr('readonly', 'readonly');
        this.originalFieldTipo.val(uidTipoAtto)
            .attr("disabled", "disabled")
            .after(hiddenStr);
        this.originalFieldStrutturaAmm.val(uidSAC);
        this.originalFieldStato.val(provvedimento.statoOperativo);
        this.originalFieldInformazioni.html(': ' + informazioni.join(' - '))
            .trigger('change');

        this.ztree.selezionaNodoSeApplicabileEDisabilitaAlberatura('treeStruttAmm' + this.suffix, uidSAC);
        this.modale.modal('hide');
    }

    function inizializzazione(prefix, suffix, ztree) {
        if(instance) {
            instance.destroy(true);
        }
        instance = new ProvvedimentoInserimento(prefix, suffix, ztree);
        global.provvedimentoInserimentoInstance = instance;
        instance.init();
    }
}(this, jQuery);