;(function($) {
    'use strict';
    
    // Set the defaults for DataTables initialisation
    $.extend(true, $.fn.dataTable.defaults, {
        sDom: "<'row-fluid span12'r>t<'row-fluid'<'span4'i><'span8'p>>",
        sPaginationType: 'bootstrapFL',
        oLanguage: {
            sLengthMenu: '_MENU_ records per page'
        }
    });

    // Default class modification
    $.extend($.fn.dataTableExt.oStdClasses, {
        sWrapper: 'dataTables_wrapper form-inline'
    });

    // API method to get paging information
    $.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
		if(oSettings === null || oSettings === undefined) {
			return {
				iStart: 0,
				iEnd: 0,
				iLength: 0,
				iTotal: 0,
				iFilteredTotal: 0,
				iPage: 0,
				iTotalPages: 0
			};
		}
        return {
            iStart: oSettings._iDisplayStart,
            iEnd: oSettings.fnDisplayEnd(),
            iLength: oSettings._iDisplayLength,
            iTotal: oSettings.fnRecordsTotal(),
            iFilteredTotal: oSettings.fnRecordsDisplay(),
            iPage: oSettings._iDisplayLength === -1 ? 0 : Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
            iTotalPages: oSettings._iDisplayLength === -1 ? 0 : Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
        };
    };

    // Bootstrap style pagination control
    $.extend($.fn.dataTableExt.oPagination, {
        bootstrapFL: {
            fnInit: function (oSettings, nPaging, fnDraw) {
                var oLang = oSettings.oLanguage.oPaginate;
                var fnClickHandler = function (e) {
                    e.preventDefault();
                    if (oSettings.oApi._fnPageChange(oSettings, e.data.action)) {
                        fnDraw(oSettings);
                    }
                };
                var fnGotoPage = function (e) {
                    var sPageSelected = $('input', nPaging).val();
                    var iPageSelected = +sPageSelected;
                    var iNewStart;
                    var iDisplayStart;
                    if(sPageSelected == '' || isNaN(iPageSelected)) {
                        // Handle empty string
                        $('input', nPaging).val('');
                        return;
                    }
                    if(iPageSelected <= 0) {
                        iPageSelected = 1;
                    }
                    iNewStart = oSettings._iDisplayLength * (iPageSelected - 1);
                    if ( iNewStart > oSettings.fnRecordsDisplay() ) {
                        // Display overrun
                        iDisplayStart = (Math.ceil((oSettings.fnRecordsDisplay() - 1) /
                                oSettings._iDisplayLength) - 1) * oSettings._iDisplayLength;
                    } else {
                        iDisplayStart = iNewStart;
                    }
                    oSettings._iDisplayStart = iDisplayStart;
                    $(oSettings.oInstance).trigger('page', oSettings);
                    fnDraw( oSettings );
                    sPageSelected = iDisplayStart / oSettings._iDisplayLength + 1;
                    $('input', nPaging).val(sPageSelected);
                };
                var pagination = [];
                var els;
                var oldFnc;
                
                if(oSettings.fnServerData) {
                    oldFnc = oSettings.fnServerData;
                    oSettings.fnServerData = function(sSource, aoData, fnCallback) {
                        var fnc = function(res) {
                            // Do something
                            $(nPaging).find('.gotopage')[res.iTotalDisplayRecords > oSettings._iDisplayLength ? 'removeClass' : 'addClass']('hide');
                            fnCallback.apply(this, arguments);
                        }
                        return oldFnc(sSource, aoData, fnc, oSettings);
                    }
                } else {
                    oSettings.fnServerData = function(sSource, aoData, fnCallback, oSettings) {
                        var fnc = function(res) {
                            // Do something
                            $(nPaging).find('.gotopage')[res.iTotalDisplayRecords > oSettings._iDisplayLength ? 'removeClass' : 'addClass']('hide');
                            fnCallback.apply(this, arguments);
                        }
                        // Based on DataTables source code
                        oSettings.jqXHR = $.ajax({
                            url:  sSource,
                            data: aoData,
                            success: function (json) {
                                if(json.sError) {
                                    oSettings.oApi._fnLog(oSettings, 0, json.sError);
                                }
                                $(oSettings.oInstance).trigger('xhr', [oSettings, json]);
                                fnc.apply(this, arguments);
                            },
                            dataType: 'json',
                            cache: false,
                            type: oSettings.sServerMethod,
                            error: function (xhr, error, thrown) {
                                if (error === "parsererror") {
                                    oSettings.oApi._fnLog(oSettings, 0, "DataTables warning: JSON data from " +
                                        "server could not be parsed. This is caused by a JSON formatting error." );
                                }
                            }
                        });
                    }
                }
                
                pagination.push('<ul>');
                if(oSettings.fnRecordsTotal() > oSettings._iDisplayLength) {
                    pagination.push('<li class="gotopage"><input type="text" maxlength="3"/><button type="button" data-paginator>&ldsh;</button></li>');
                }
                pagination.push('<li class="first disabled"><a href="#">&laquo;&nbsp;' + oLang.sFirst + '</a></li>');
                pagination.push('<li class="prev  disabled"><a href="#">&larr;&nbsp;' + oLang.sPrevious + '</a></li>');
                pagination.push('<li class="next  disabled"><a href="#">' + oLang.sNext + '&nbsp;&rarr;</a></li>');
                pagination.push('<li class="last  disabled"><a href="#">' + oLang.sLast + '&nbsp;&raquo;</a></li>');
                pagination.push('</ul>');
                
                $(nPaging)
                    .addClass('pagination')
                    .append(pagination.join(''));
                els = $('a', nPaging);
                $(els[0]).bind('click.DT', {action: 'first'},    fnClickHandler);
                $(els[1]).bind('click.DT', {action: 'previous'}, fnClickHandler);
                $(els[2]).bind('click.DT', {action: 'next'},     fnClickHandler);
                $(els[3]).bind('click.DT', {action: 'last'},     fnClickHandler);
                $('button[data-paginator]', nPaging).bind('click.DT', fnGotoPage);
            },
            fnUpdate: function(oSettings, fnDraw) {
                var iListLength = 5;
                var oPaging = oSettings.oInstance.fnPagingInfo();
                var an = oSettings.aanFeatures.p;
                var iHalf = Math.floor(iListLength / 2);
                var i, j, iLen, sClass, iStart, iEnd;

                if (oPaging.iTotalPages < iListLength) {
                    iStart = 1;
                    iEnd = oPaging.iTotalPages;
                } else if (oPaging.iPage <= iHalf) {
                    iStart = 1;
                    iEnd = iListLength;
                } else if (oPaging.iPage >= (oPaging.iTotalPages - iHalf)) {
                    iStart = oPaging.iTotalPages - iListLength + 1;
                    iEnd = oPaging.iTotalPages;
                } else {
                    iStart = oPaging.iPage - iHalf + 1;
                    iEnd = iStart + iListLength - 1;
                }
                for (i = 0, iLen = an.length; i < iLen; i++) {
                    // Remove the middle elements
                    $('li:gt(1)', an[i]).filter(':not(.next,.last)').remove();

                    // Add the new list items and their event handlers
                    for (j = iStart; j <= iEnd; j++) {
                        sClass = (j == oPaging.iPage + 1) ? 'class="active"' : '';
                        $('<li ' + sClass + '><a href="#">' + j + '</a></li>')
                            .insertBefore($('.next,.last', an[i])[0])
                            .bind('click', function(e) {
                                e.preventDefault();
                                oSettings._iDisplayStart = (parseInt($('a', this).text(), 10) - 1) * oPaging.iLength;
                                fnDraw(oSettings);
                                $(oSettings.oInstance).trigger('page', oSettings);
                            }
                        );
                    }

                    // Add / remove disabled classes from the
                    // static elements
                    if (oPaging.iPage === 0) {
                        $('.first,.prev', an[i]).addClass('disabled');
                    } else {
                        $('.first,.prev', an[i]).removeClass('disabled');
                    }

                    if (oPaging.iPage === oPaging.iTotalPages - 1 || oPaging.iTotalPages === 0) {
                        $('.next,.last', an[i]).addClass('disabled');
                    } else {
                        $('.next,.last', an[i]).removeClass('disabled');
                    }
                }
            }
        }
    });

    /*
     * TableTools Bootstrap compatibility
     * Required TableTools 2.1+
     */
    if ($.fn.DataTable.TableTools) {
        // Set the classes that TableTools uses to something suitable for Bootstrap
        $.extend(true, $.fn.DataTable.TableTools.classes, {
            container: 'DTTT btn-group',
            buttons: {
                normal: 'btn',
                disabled: '"disabled'
            },
            collection: {
                container: 'DTTT_dropdown dropdown-menu',
                buttons: {
                    normal: '',
                    disabled: 'disabled'
                }
            },
            print: {
                info: 'DTTT_print_info modal'
            },
            select: {
                row: 'active'
            }
        });

        // Have the collection use a bootstrap compatible dropdown
        $.extend(true, $.fn.DataTable.TableTools.DEFAULTS.oTags, {
            collection: {
                container: 'ul',
                button: 'li',
                liner: 'a'
            }
        });
    }

})(jQuery);