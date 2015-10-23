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
    toggleFilePicker : function() {
        index.hideAllUploadPanels();
        $('.file-picker').show();
        $('.upload-panel').show();
    },
    bindEvents : function() {
        $('.text-file>a').click(index.toggleTextEditor);
        $('.any-file>a').click(index.toggleFilePicker);
    }
};

$(function() {
    index.bindEvents();
});
