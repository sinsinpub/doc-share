package com.github.sinsinpub.doc.web.controller;

import com.github.sinsinpub.doc.web.model.AuditLog;
import com.jfinal.core.Controller;

/**
 * 主页控制器.
 *
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class HomeController extends Controller {

    public void index() {
        AuditLog.REPO.addAccessLog(getRequest().getMethod(), getRequest().getRequestURI(),
                getRequest().getRemoteAddr(), getRequest().getHeader("User-Agent"),
                getRequest().getRemoteUser(), null);
        render("index.html");
    }

}
