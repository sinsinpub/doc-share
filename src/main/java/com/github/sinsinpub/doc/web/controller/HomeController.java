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
     * 跳到首页个人仪表盘.
     */
    public void index() {
        render("index.html");
    }

    /**
     * 测试下JSP渲染.
     * <p>
     * JSP/FreeMarker这些视图文件为了安全建议放在WEB-INF下，在配置Routes时设置viewPath参数来告诉JFinal.
     */
    public void test() {
        renderJsp("jsp/test.jsp");
    }

    /**
     * 跳到指南.
     */
    public void guide() {
        render("guide.html");
    }

    public void signin() {
        render("guide.html");
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
