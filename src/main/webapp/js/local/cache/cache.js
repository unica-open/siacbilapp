/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function(global, $){
    'use strict';
    var Utils = global.Utils;
    var CacheManager = function() {
        this.statistics = $('#statistics');
        this.keysTable = $('#keys');
        this.howToDecode = $('#howToDecode');
        this.alertErrori = $('#ERRORI');
        this.alertInformazioni = $('#INFORMAZIONI');

        // Inits the overlay
        this.statistics.overlay({rebind: true});
        this.keysTable.overlay({rebind: true});
    };

    CacheManager.prototype.constructor = CacheManager;
    CacheManager.prototype.clear = clear;
    CacheManager.prototype.clearKey = clearKey;
    CacheManager.prototype.info = info;
    CacheManager.prototype.keys = keys;
    CacheManager.prototype.refresh = refresh;
    CacheManager.prototype.toggleHowToDecode = toggleHowToDecode;

    CacheManager.prototype.handleClear = handleClear;
    CacheManager.prototype.handleClearKey = handleClearKey;
    CacheManager.prototype.handleInfo = handleInfo;
    CacheManager.prototype.handleKeys = handleKeys;

    $(init);

    function clear() {
        var pwd = prompt('Please insert password:', '');
        if(pwd === null) {
            // Click on 'Cancel'
            return;
        }
        return Utils.doPost('serviceCacheUtility_clear.do', {password: btoa(pwd)})
            .then(this.handleClear.bind(this));
    }
    function clearKey(event, key) {
        var pwd;
        Utils.eventPreventDefault(event);
        pwd = prompt('Please insert password:', '');
        if(pwd === null) {
            // Click on 'Cancel'
            return;
        }
        return Utils.doPost('serviceCacheUtility_clearKey.do', {password: btoa(pwd), key: key})
            .then(this.handleClearKey.bind(this));
    }
    function info(accurate) {
        this.statistics.overlay('show');
        return Utils.doPost('serviceCacheUtility_info.do', {accurate: !!accurate})
            .then(this.handleInfo.bind(this))
            .always(this.statistics.overlay.bind(this.statistics, 'hide'));
    }
    function keys(accurate) {
        this.keysTable.overlay('show');
        return Utils.doPost('serviceCacheUtility_keys.do', {accurate: !!accurate})
            .then(this.handleKeys.bind(this))
            .always(this.keysTable.overlay.bind(this.keysTable, 'hide'));
    }
    function refresh(accurate) {
        return $.when(this.info(accurate), this.keys(accurate));
    }
    function toggleHowToDecode() {
        this.howToDecode.toggleClass('in');
    }

    function handleClear(data) {
        if(Utils.impostaDatiNegliAlert(data.errori, this.alertErrori)) {
            return Utils.reject(data.errori);
        }
        Utils.impostaDatiNegliAlert(data.informazioni, this.alertInformazioni);
        return this.refresh(false);
    }
    function handleClearKey(data) {
        if(Utils.impostaDatiNegliAlert(data.errori, this.alertErrori)) {
            return Utils.reject(data.errori);
        }
        Utils.impostaDatiNegliAlert(data.informazioni, this.alertInformazioni);
        return this.refresh(false);
    }
    function handleInfo(data) {
        this.statistics.html(data.statistics);
    }
    function handleKeys(data) {
        // Workarpund for dataTables
        var arr = data.keys.map(function(el) {
            return {value: el};
        });
        var opts = {
            bPaginate: true,
            aaData: arr,
            bLengthChange: false,
            bSort: false,
            bInfo: true,
            bAutoWidth: false,
            bFilter: false,
            bProcessing: false,
            bServerSide: false,
            oLanguage: {
                sInfo: '_START_ - _END_ of _MAX_ results',
                sInfoEmpty: '0 results',
                sProcessing: 'Please wait...',
                sZeroRecords: 'No keys in cache',
                oPaginate: {
                    sFirst: 'first',
                    sLast: 'last',
                    sNext: 'next',
                    sPrevious: 'prev.',
                    sEmptyTable: 'No data'
                }
            },
            fnDrawCallback: Utils.defaultPreDraw,
            aoColumnDefs: [
                {aTargets: [0], mData: Utils.defaultPerDataTable('value')},
                {aTargets: [1], mData: composeDeleteLink, fnCreatedCell: centerCell}
            ]
        };
        if($.fn.DataTable.fnIsDataTable(this.keysTable[0])) {
            this.keysTable.dataTable().fnDestroy();
        }
        this.keysTable.dataTable(opts);
    }

    /**
     * Initialization
     */
    function init() {
        global.cacheManager = new CacheManager();
        global.cacheManager.refresh(false);

        $('[data-tooltip]').tooltip();
    }

    // Utilities
    function composeDeleteLink(source) {
        return '<a onclick="cacheManager.clearKey(event, \'' + source.value + '\')"><i class="icon icon-trash icon-2x"></i></a>';
    }
    function centerCell(nTd) {
        nTd.classList = 'text-center';
    }
}(this, jQuery);