var index = {
    hideAllUploadPanels : function() {
        $('.text-editor').hide();
        $('.pic-picker').hide();
        $('.file-picker').hide();
    },
    toggleTextEditor : function() {
        index.hideAllUploadPanels();
        $('.text-editor').show();
        $('.upload-panel').show();
    },
    togglePicPicker : function() {
        index.hideAllUploadPanels();
        $('.pic-picker').show();
        $('.upload-panel').show();
    },
    toggleFilePicker : function() {
        index.hideAllUploadPanels();
        $('.file-picker').show();
        $('.upload-panel').show();
    },
    readServiceStatus : function() {
        // Test mbean reads, usage:
        // https://jolokia.org/reference/html/clients.html
        /*
        var j4p = require("jolokia")({ url : "/console"});
        var req = {
            type : "read",
            mbean : "java.lang:type=Memory",
            attribute : "HeapMemoryUsage"
        };
        var response = j4p.request(req);
        console.info(response);
        $('.public-files').text(JSON.stringify(response.value));
        */
    },
    bindEvents : function() {
        $('.text-file>a').click(index.toggleTextEditor);
        $('.image-file>a').click(index.togglePicPicker);
        $('.any-file>a').click(index.toggleFilePicker);
    }
};

$(function() {
    index.bindEvents();
    index.readServiceStatus();
});
