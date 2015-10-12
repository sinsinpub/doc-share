package com.github.sinsinpub.doc.web.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import com.github.sinsinpub.doc.web.i18n.MessageResources;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * 加点处理AJAX请求和JSON相关的方便方法.
 * 
 * @author sin_sin
 * @version $Date: Oct 12, 2015 $
 */
@Before(JsonExceptionRenderer.class)
public abstract class JsonAwareController extends Controller {

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

    protected String getRemoteAddr() {
        return getRequest().getRemoteAddr();
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

}
