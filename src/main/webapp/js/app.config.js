var libs = {
    "version" : {
        "requirejs" : "2.1.20",
        "require-css" : "0.1.8",
        "jquery" : "2.1.1",
        "jquery-ui" : "1.9.0",
        "bootstrap" : "3.3.5",
        "jquery-file-upload" : "9.10.1",
        "jolokia.js" : "1.3.2"
    },
    "webjars" : function(id, name) {
        return "/webjars/" + id + "/" + libs.version[id] + "/" + name;
    },
    "js" : function(name) {
        return "/js/" + name;
    },
    "css" : function(name) {
        return "/css/" + name;
    }
};

requirejs.config({
    "waitSeconds" : 20,
    "baseUrl" : "/js",
    "map" : {
        "*" : {
            "css" : libs.webjars("require-css", "css.js")
        }
    },
    "paths" : {
        "jquery" : libs.webjars("jquery", "jquery.min"),
        "jquery-ui" : libs.webjars("jquery-ui", "js/jquery-ui-1.9.0.custom.min"),
        "jquery.ui.widget" : libs.webjars("jquery-ui", "js/jquery-ui-1.9.0.custom.min"),
        "bootstrap" : libs.webjars("bootstrap", "js/bootstrap.min"),
        "bootstrap-css" : libs.webjars("bootstrap", "css/bootstrap.min"),
        "jquery.fileupload" : libs.webjars("jquery-file-upload", "js/jquery.fileupload"),
        "jquery.fileupload-css" : libs.webjars("jquery-file-upload", "css/jquery.fileupload"),
        "jquery.fileupload-ui-css" : libs.webjars("jquery-file-upload", "css/jquery.fileupload-ui"),
        "jolokia" : libs.webjars("jolokia.js", "jolokia"),

        "style-css" : libs.css("style"),
        "index-css" : libs.css("index"),
        "index" : libs.js("index")
    },
    "shim" : {
        "jquery-ui" : [ "jquery" ],
        "jquery.fileupload" : [ "css!jquery.fileupload-css", "css!jquery.fileupload-ui-css" ],
        "bootstrap" : [ "jquery", "css!bootstrap-css" ],
        "header" : [ "jquery", "css!style-css" ],
        "jolokia" : [ "jquery" ],
        "index" : [ "bootstrap", "header", "css!index-css", "jquery.fileupload" ]
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
};

var mainModuleName = detectMainRequireModuleName();
if (!!mainModuleName) {
    require([ mainModuleName ], function(module) {
        console.debug("Module '" + mainModuleName + "' has been started");
    });
} else {
    console.warn("No module specified to be started!");
}
