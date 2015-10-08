requirejs.config({
    "baseUrl" : "js",
    "shim" : {
//        "jquery.bpopup.min" : [ "jquery.min" ],
//        "jquery.dateFormat.min" : [ "jquery.min" ],
//        "jquery-ui.min" : [ "jquery.min" ],
//        "console" : [ "jquery.min" ],
//        "header" : [ "jquery.min", "jquery.flexslider.min" ],
//        "user" : [ "jquery.min", "console", "header" ],
        "index" : [ "jquery.min" ]
    }
});

var detectMainRequireModuleName = function() {
    var pathFileName = location.pathname.substr(location.pathname.lastIndexOf("/") + 1);
    var moduleName = pathFileName.substr(0, pathFileName.lastIndexOf("."))
    var scriptElement = document.querySelector("script[data-main][module]");
    if (!!scriptElement) {
        moduleName = scriptElement.getAttribute("module") || moduleName;
    }
    return moduleName;
}

var mainModuleName = detectMainRequireModuleName();
if (!!mainModuleName) {
    require([ mainModuleName ], function(module) {
        console.debug("Module '" + mainModuleName + "' has been started");
    });
} else {
    console.warn("No module specified to be started!");
}
