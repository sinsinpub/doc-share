var index = {
    readServiceStatus : function() {
        // Test mbean reads, usage:
        // https://jolokia.org/reference/html/clients.html
        /*
         * var j4p = require("jolokia")({ url : "/console"}); var req = { type :
         * "read", mbean : "java.lang:type=Memory", attribute :
         * "HeapMemoryUsage" }; var response = j4p.request(req);
         * console.info(response);
         * $('.public-files').text(JSON.stringify(response.value));
         */
    },
    initFileUpload : function() {
        $('#fileupload').fileupload({
        // Uncomment the following to send cross-domain cookies:
        // xhrFields: {withCredentials: true}
        });
        // Load existing files:
        $('#fileupload').addClass('fileupload-processing');
        $.ajax({
            // Uncomment the following to send cross-domain cookies:
            // xhrFields: {withCredentials: true},
            url : "api/upload/progress",
            dataType : 'json',
            context : $('#fileupload')[0]
        }).always(function() {
            $(this).removeClass('fileupload-processing');
        }).done(function(result) {
            /*
             * $(this).fileupload('option', 'done').call(this, $.Event('done'), {
             * result : result });
             */
        });
    }
};

$(function() {
    $('#body').show();
    index.initFileUpload();
});
