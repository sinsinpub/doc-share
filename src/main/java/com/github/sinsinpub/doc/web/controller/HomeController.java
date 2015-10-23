package com.github.sinsinpub.doc.web.controller;

import java.util.Properties;

import com.github.sinsinpub.doc.ApplicationVersion;
import com.github.sinsinpub.doc.hint.Prototype;
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
@Prototype
public class HomeController extends Controller {

    /**
     * 跳到首页.
     */
    public void index() {
        render("index.html");
    }

    /**
     * 测试下JSP渲染.
     */
    public void test() {
        renderJsp("jsp/test.jsp");
    }

    public void dashboard() {
        render("dashboard.html");
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

    public void status() {
        renderJson("status", "online");
    }

}
