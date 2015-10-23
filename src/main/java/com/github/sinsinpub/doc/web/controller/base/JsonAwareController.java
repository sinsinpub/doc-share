package com.github.sinsinpub.doc.web.controller.base;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.sinsinpub.doc.utils.WebUtils;
import com.github.sinsinpub.doc.web.i18n.MessageResources;
import com.github.sinsinpub.doc.web.interceptor.JsonExceptionRenderer;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;

/**
 * 继承原Controller的抽象类，加点处理AJAX请求和JSON相关的方便方法.
 * <p>
 * 注意Controller实例生命周期由jfinal创建、管理，并不是单例模式，所以不能够作为MBean注册。
 * 
 * @see Controller
 * @see JsonExceptionRenderer
 * @author sin_sin
 * @version $Date: Oct 12, 2015 $
 */
@Before(JsonExceptionRenderer.class)
public abstract class JsonAwareController extends Controller {

    protected final Logger logger = Logger.getLogger(getClass());

    protected void status(int status) {
        getResponse().setStatus(status);
    }

    protected void statusCreated() {
        status(HttpServletResponse.SC_CREATED);
    }

    protected void statusAccepted() {
        status(HttpServletResponse.SC_ACCEPTED);
    }

    protected void statusNoContent() {
        status(HttpServletResponse.SC_NO_CONTENT);
    }

    protected void renderJsonEmpty() {
        renderJson("");
        statusNoContent();
    }

    protected String getMessage(String key) {
        return MessageResources.get(getLocale(), key);
    }

    protected String formatMessage(String key, Object... args) {
        return MessageResources.format(getLocale(), key, args);
    }

    protected Locale getLocale() {
        return getRequest().getLocale();
    }

    /**
     * 获取当前请求的客户端地址.
     * 
     * @return <code>request.getRemoteAddr()</code>
     */
    protected String getRemoteAddr() {
        return getRequest().getRemoteAddr();
    }

    /**
     * 尝试获取当前请求的真实客户端地址.
     * 
     * @return 默认从<code>request.getRemoteAddr()</code>，也会尝试从一些常见HTTP Header中识别
     */
    protected String getRealRemoteAddr() {
        return WebUtils.determineRemoteIp(getRequest(), true);
    }

    protected String getHeaderUserAgent() {
        return getRequest().getHeader("User-Agent");
    }

    protected String getHeaderReferer() {
        return getRequest().getHeader("Referer");
    }

    protected String getHeaderOrigin() {
        return getRequest().getHeader("Origin");
    }

    protected String getHeaderRequestedWith() {
        return getRequest().getHeader("X-Requested-With");
    }

    protected boolean isAjaxRequest() {
        return WebUtils.isAjaxRequest(getRequest());
    }

    /**
     * 取得当前请求的HTTP协议首行内容.
     * <p>
     * 并非实际客户端发来的首行，而是从请求上下文中取出HTTP<code>METHOD+URI+PROTOCOL</code>拼接模拟出来的。
     * 
     * @return 方法+空格+URI+空格+协议版本
     */
    protected String getHttpFirstLine() {
        HttpServletRequest req = getRequest();
        return String.format("%s %s %s", req.getMethod(), req.getRequestURI(), req.getProtocol());
    }

    /**
     * 强制令响应成为no cache方式的方便方法.
     */
    protected void forceResponseNoCache() {
        HttpServletResponse response = getResponse();
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,max-age=0");
        response.setDateHeader("Expires", 0);
    }

}
