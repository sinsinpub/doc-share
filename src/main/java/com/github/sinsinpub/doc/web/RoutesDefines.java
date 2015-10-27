package com.github.sinsinpub.doc.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.gescobar.jmx.annotation.Description;
import net.gescobar.jmx.annotation.ManagedAttribute;

import com.github.sinsinpub.doc.web.controller.FileUploadController;
import com.github.sinsinpub.doc.web.controller.HomeController;
import com.github.sinsinpub.doc.web.controller.LogViewController;
import com.github.sinsinpub.doc.web.controller.UserController;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

/**
 * Definitions of controller routes for HTTP request URI mapping.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
@Description("定义JFinal委派Controller用的URI映射配置")
public class RoutesDefines extends Routes {

    public static final String OBJECT_NAME = "doc-share:type=Config,name=RoutesDefines";

    @Override
    public void config() {
        add("/", HomeController.class);
        add("/api/upload", FileUploadController.class);
        add("/api/user", UserController.class);
        add("/api/log", LogViewController.class);
    }

    @ManagedAttribute(writable = false, description = "当前映射")
    public Map<String, String> getRoutesMap() {
        Map<String, String> map = new HashMap<String, String>(getEntrySet().size());
        for (Entry<String, Class<? extends Controller>> e : getEntrySet()) {
            map.put(e.getKey(), e.getValue().getName());
        }
        return map;
    }

}
