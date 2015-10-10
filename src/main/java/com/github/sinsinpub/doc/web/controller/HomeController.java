package com.github.sinsinpub.doc.web.controller;

import java.util.Properties;

import com.github.sinsinpub.doc.ApplicationVersion;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

/**
 * 主页控制器.
 * <p>
 * 顺便JFinal的Controller并不是单例设计，所以不需要保障线程安全。
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class HomeController extends Controller {

    /**
     * 跳到首页.
     */
    public void index() {
        render("index.html");
    }

    /**
     * 显示服务端系统机密信息233
     */
    public void sysinfo() {
        Properties props = (Properties) System.getProperties().clone();
        props.put("jfinal.webRootPath", PathKit.getWebRootPath());
        props.put("jfinal.rootClassPath", PathKit.getRootClassPath());
        props.put("app.name", ApplicationVersion.getInstance().getApplicationName());
        props.put("app.version", ApplicationVersion.getInstance().getApplicationVersion());

        renderJson(props);
    }

}
