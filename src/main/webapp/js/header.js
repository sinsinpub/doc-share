var loadHeader = function(base) {

    $('#header').load((base || '.') + '/header.html', 'header');
};

$(function() {
    loadHeader();
});

