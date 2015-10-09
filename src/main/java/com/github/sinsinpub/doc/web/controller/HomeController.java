package com.github.sinsinpub.doc.web.controller;

import java.util.Properties;

import com.github.sinsinpub.doc.web.model.AuditLog;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

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

    public void sysinfo() {
        Properties props = (Properties) System.getProperties().clone();
        props.put("jfinal.webRootPath", PathKit.getWebRootPath());
        props.put("jfinal.rootClassPath", PathKit.getRootClassPath());
        renderJson(props);
    }

}
