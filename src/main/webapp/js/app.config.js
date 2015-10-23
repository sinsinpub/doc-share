var libs = {
    "version" : {
        "requirejs" : "2.1.20",
        "require-css" : "0.1.8",
        "jquery" : "1.11.1",
        "bootstrap" : "3.3.5"
    },
    "webjars" : function(id, name) {
        return "webjars/" + id + "/" + libs.version[id] + "/" + name;
    },
    "js" : function(name) {
        return "js/" + name;
    },
    "css" : function(name) {
        return "css/" + name;
    }
};

requirejs.config({
    "baseUrl" : "/",
    "map" : {
        "*" : {
            "css" : libs.webjars("require-css", "css")
        }
    },
    "paths" : {
        "jquery" : libs.webjars("jquery", "jquery.min"),
        "bootstrap" : libs.webjars("bootstrap", "js/bootstrap.min"),
        "bootstrap-css" : libs.webjars("bootstrap", "css/bootstrap.min"),

        "global-css" : libs.css("global"),
        "module-css" : libs.css("module"),
        "header" : libs.js("header"),
        "index" : libs.js("index")
    },
    "shim" : {
        "bootstrap" : [ "jquery", "css!bootstrap-css" ],
        "header" : [ "jquery", "css!global-css" ],
        "index" : [ "bootstrap", "header", "css!module-css" ]
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
