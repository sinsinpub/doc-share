/**
 * Main module for page index.html.
 */
define([ 'jquery', 'header', 'jquery.fileupload' ], function($, header, jqfu) {
    'use strict';
    var thisApi = {
        configFileUpload : function() {
            // Change this to the location of your server-side upload handler:
            var url = 'api/upload/any';
            var uploadButton = $('<button/>').addClass('btn btn-primary').prop('disabled', true)
                    .text('Processing...').on('click', function() {
                        var $this = $(this), data = $this.data();
                        $this.off('click').text('Abort').on('click', function() {
                            $this.remove();
                            data.abort();
                        });
                        data.submit().always(function() {
                            $this.remove();
                        });
                    });
            $('#fileupload').fileupload({
                url : url,
                dataType : 'json',
                autoUpload : false,
                // acceptFileTypes :
                // /(\.|\/)(gif|jpe?g|png|txt|md|docx?|pptx?|xlsx?)$/i,
                maxFileSize : 9999000, // 10Mo
                maxChunkSize : 1000000, // 1Mo
                // Enable image resizing, except for Android and Opera,
                // which actually support image resizing, but fail to
                // send Blob objects via XHR requests:
                disableImageResize : /Android(?!.*Chrome)|Opera/.test(window.navigator.userAgent),
                previewMaxWidth : 100,
                previewMaxHeight : 100,
                previewCrop : true
            }).on('fileuploadadd', function(e, data) {
                data.context = $('<div/>').appendTo('#files');
                $.each(data.files, function(index, file) {
                    var node = $('<p/>').append($('<span/>').text(file.name));
                    if (!index) {
                        node.append('<br>').append(uploadButton.clone(true).data(data));
                    }
                    node.appendTo(data.context);
                });
            }).on(
                    'fileuploadprocessalways',
                    function(e, data) {
                        var index = data.index, file = data.files[index], node = $(data.context
                                .children()[index]);
                        if (file.preview) {
                            node.prepend('<br>').prepend(file.preview);
                        }
                        if (file.error) {
                            node.append('<br>').append(
                                    $('<span class="text-danger"/>').text(file.error));
                        }
                        if (index + 1 === data.files.length) {
                            data.context.find('button').text('Upload').prop('disabled',
                                    !!data.files.error);
                        }
                    }).on('fileuploadprogressall', function(e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress .progress-bar').css('width', progress + '%');
            }).on('fileuploaddone', function(e, data) {
                $.each(data.result.files, function(index, file) {
                    if (file.url) {
                        var link = $('<a>').attr('target', '_blank').prop('href', file.url);
                        $(data.context.children()[index]).wrap(link);
                    } else if (file.error) {
                        var error = $('<span class="text-danger"/>').text(file.error);
                        $(data.context.children()[index]).append('<br>').append(error);
                    }
                });
            }).on('fileuploadfail', function(e, data) {
                $.each(data.files, function(index) {
                    var error = $('<span class="text-danger"/>').text('File upload failed.');
                    $(data.context.children()[index]).append('<br>').append(error);
                });
            }).prop('disabled', !$.support.fileInput).parent().addClass(
                    $.support.fileInput ? undefined : 'disabled');
        },
        readServiceStatus : function() {
            // Load and test mbean reads, usage:
            // https://jolokia.org/reference/html/clients.html
            require([ "jolokia" ], function(jolokia) {
                var j4p = new jolokia({
                    url : "/console"
                });
                var req = {
                    type : "read",
                    mbean : "java.lang:type=Memory",
                    attribute : "HeapMemoryUsage"
                };
                var response = j4p.request(req);
                console.info(response);
                $('#my-recent-files>.panel-body>p').text(JSON.stringify(response.value));
            });
            console.info("Header is strict mode: " + header.isStrictMode());
        },
        onLoad : function() {
            thisApi.configFileUpload();
            $('#body').show();
            $('#my-recent-files>.panel-heading').click(thisApi.readServiceStatus);
        }
    };

    $(function() {
        thisApi.onLoad();
    });

    return thisApi;
});
