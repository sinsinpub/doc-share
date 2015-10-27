$.ajaxSetup({
    cache : true
});

var loadHeader = function(base) {
    if ($('#header').children().length == 0) {
        $('#header').load((base || '.') + '/header.html #header_inner');
    }
};

var loadFooter = function(base) {
    if ($('#footer').children().length == 0) {
        $('#footer').load((base || '.') + '/header.html #footer_inner');
    }
};

$(function() {
    loadHeader(window.contextPath);
    loadFooter(window.contextPath);
});
