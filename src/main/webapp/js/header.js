/**
 * Module for loading common header & footer section for every pages.
 */
define([ 'jquery' ], function($) {
    'use strict';
    $.ajaxSetup({
        cache : true
    });
    var api = {
        /** Indicates if this module is using strict mode. */
        isStrictMode : function() {
            // no use for other module, just for test
            var result = true;
            try {
                result = !(arguments.callee);
            } catch (e) {
            }
            return result;
        },
        loadHeader : function(base) {
            if ($('#header').children().length == 0) {
                $('#header').load((base || '.') + '/header.html #header_inner');
            }
        },
        loadFooter : function(base) {
            if ($('#footer').children().length == 0) {
                $('#footer').load((base || '.') + '/header.html #footer_inner');
            }
        },
        onLoad : function() {
            api.loadHeader(window.contextPath);
            api.loadFooter(window.contextPath);
        }
    };
    $(function() {
        api.onLoad();
    });
    return api;
});
