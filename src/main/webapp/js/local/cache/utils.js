/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $) {
    'use strict';
    var exports = {};

    exports.bootboxAlert = bootboxAlert;
    exports.defaultPerDataTable = defaultPerDataTable;
    exports.defaultPreDraw = defaultPreDraw;
    exports.doLog = doLog;
    exports.doPost = doPost;
    exports.eventPreventDefault = eventPreventDefault;
    exports.impostaDatiNegliAlert = impostaDatiNegliAlert;
    exports.onerror = onerror;
    exports.passThrough = passThrough;
    exports.reject = reject;

    global.Utils = exports;

    $.fn.serializeObject = serializeObject;

    function bootboxAlert(msg) {
        bootbox.dialog(msg, [{
            label: 'Ok',
            'class': 'btn-primary',
            callback: $.noop
        }], {
            animate: true,
            classes: 'dialogAlert',
            header: 'Attenzione',
            backdrop: 'static'
        });
    }
    function defaultPerDataTable(sel, defaultValue, operation) {
        var split = sel.split('.');
        var length = split.length;
        var defVal = defaultValue !== undefined ? defaultValue : '';
        var oper = operation !== undefined && typeof operation === 'function' ? operation : passThrough;

        return function(source) {
            var res = source;
            var i;
            for(i = 0 ; i < length; i++) {
                if(res[split[i]] === undefined) {
                    return oper(defVal);
                }
                res = res[split[i]];
            }
            return oper(res);
        };
    }
    function defaultPreDraw(opts) {
        $('#' + opts.nTable.id + '_wrapper').children().first().hide();
    }
    function doLog(msg, level) {
        var innerLevel = level || 'log';
        if(global.console && typeof global.console[innerLevel] === 'function') {
            global.console[innerLevel](msg);
        }
    }
    function doPost(url, data) {
        var res;
        try {
            res = $.post(url, data)
            .fail(function(jqXHR) {
                var stringaErrore = 'Errore nella chiamata al servizio: ' + jqXHR.status + ' - ' + jqXHR.statusText;
                doLog(stringaErrore, 'error');
                if (jqXHR.status !== 0) {
                    bootboxAlert(stringaErrore);
                }
            });
        } catch(e) {
            doLog(e, 'error');
            res = reject(e);
        }
        return res;
    }
    function eventPreventDefault(event) {
        if(event && event.preventDefault && typeof event.preventDefault === 'function') {
            event.preventDefault();
        }
    }
    function impostaDatiNegliAlert(arrayDaInjettare, alertDaPopolare) {
        var html;
        if (!arrayDaInjettare || !arrayDaInjettare.length || !alertDaPopolare.length) {
            return false;
        }

        // Chiudo i vari alert aperti
        $('.alert').not('.alert-persistent').slideUp();
        // Aggiungo gli errori alla lista
        html = arrayDaInjettare.reduce(function(acc, el) {
            if(el.codice !== undefined) {
                return acc + '<li>' + el.codice + ' - ' + el.descrizione + '</li>';
            }
            return acc + '<li>' + el + '</li>';
        }, '');
        alertDaPopolare.find('ul').html(html);
        alertDaPopolare.slideDown();
        return true;
    }
    function onerror(msg, url, line, colNo, err) {
        var stringaErrore = 'Error: ' + msg + '\nURL: ' + url + '\nLine #: ' + line;
        if (colNo) {
            stringaErrore += '\nColumn #: ' + colNo;
        }
        bootboxAlert('<pre>' + stringaErrore + '</pre>');
        doLog(stringaErrore);
        if (err && err.stack) {
            doLog(err.stack, 'error');
        }
        return true;
    }
    function passThrough(e) {
        return e;
    }
    function reject(params) {
        return $.Deferred().reject(params).promise();
    }

    function serializeObject() {
        var o = {};
        var a = this.serializeArray();
        if (a.length === 0) {
            // Workaround for jQuery bug #8883
            // see http://bugs.jquery.com/ticket/8883
            a = this.filter(":input").add(this.find(":input")).serializeArray();
        }
        jQuery.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });

        // Puts also the unchecked checkboxes into the object
        this.find("input[type='checkbox']").each(function() {
            var self = $(this);
            var name = self.attr("name");
            var checked = self.prop("checked");
            if (!o[name]) {
                o[name] = checked;
            }
        });

        return o;
    }

}(this, jQuery);
