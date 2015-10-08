package com.github.sinsinpub.doc.web;

import com.github.sinsinpub.doc.web.controller.HomeController;
import com.github.sinsinpub.doc.web.controller.LogViewController;
import com.github.sinsinpub.doc.web.controller.UserController;
import com.jfinal.config.Routes;

/**
 * Definitions of controller routes for HTTP request URI mapping.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class RoutesDefines extends Routes {

    @Override
    public void config() {
        add("/", HomeController.class);
        add("/api/user", UserController.class);
        add("/api/log", LogViewController.class);
    }

}
