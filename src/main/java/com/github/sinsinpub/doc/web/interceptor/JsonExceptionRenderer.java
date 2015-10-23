package com.github.sinsinpub.doc.web.interceptor;

import javax.servlet.http.HttpServletResponse;

import com.github.sinsinpub.doc.web.exception.DataNotFoundException;
import com.github.sinsinpub.doc.web.exception.RuntimeSqlException;
import com.google.common.base.Throwables;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.ActionException;
import com.jfinal.core.Controller;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.render.RenderFactory;

/**
 * 拦截未处理Unchecked异常，将异常转化为JSON作为HTTP响应Payload返回.
 * 
 * @author sin_sin
 * @version $Date: Oct 9, 2015 $
 */
public class JsonExceptionRenderer implements Interceptor {

    public static final String ERROR_JSON_KEY = "error";
    private static final Logger LOG = Logger.getLogger(JsonExceptionRenderer.class);

    private boolean retrievingRootCauseMessage = true;

    @Override
    public void intercept(Invocation inv) {
        if (inv.isActionInvocation()) {
            try {
                inv.invoke();
            } catch (ActionException e) {
                // 已经定好渲染方式的ActionException无需处理
                throw e;
            } catch (RuntimeException e) {
                handleException(inv, e);
            }
        } else {
            inv.invoke();
        }
    }

    /**
     * 针对不同的异常类型，定制你自己的响应代码和内容吧.
     * 
     * @param inv
     * @param e
     */
    protected void handleException(Invocation inv, RuntimeException e) {
        int statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        String message = isRetrievingRootCauseMessage() ? getMessageIfPossible(Throwables.getRootCause(e))
                : getMessageIfPossible(e);
        boolean treatedAsError = true;
        try {
            throw e;
        } catch (IllegalArgumentException iae) {
            // 400 Bad Request
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
            treatedAsError = false;
        } catch (DataNotFoundException dnfe) {
            // 404 Not Found
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            treatedAsError = false;
        } catch (NullPointerException npe) {
            // 500 Internal Server Error
        } catch (IllegalStateException ise) {
            // 500 Internal Server Error
        } catch (ActiveRecordException are) {
            // 500 Internal Server Error
        } catch (RuntimeSqlException rse) {
            // 500 Internal Server Error
        } finally {
            Controller ctl = inv.getController();
            if (treatedAsError) {
                LOG.error(String.format(
                        "Unhandled exception catched from controller, will response with: %d, %s",
                        statusCode, message), e);
                ctl.renderError(statusCode, RenderFactory.me()
                        .getJsonRender(ERROR_JSON_KEY, message));
            } else {
                // 不使用renderError则不抛出ActionException，算是严重度较低的异常
                ctl.getResponse().setStatus(statusCode);
                ctl.renderJson(ERROR_JSON_KEY, message);
                LOG.info(String.format(
                        "Unhandled exception catched from controller, will response with: %d, %s",
                        statusCode, message), e);
            }
        }
    }

    protected String getMessageIfPossible(Throwable t) {
        return t.getMessage() == null ? t.toString() : t.getMessage();
    }

    public boolean isRetrievingRootCauseMessage() {
        return retrievingRootCauseMessage;
    }

    public void setRetrievingRootCauseMessage(boolean retrievingRootCauseMessage) {
        this.retrievingRootCauseMessage = retrievingRootCauseMessage;
    }

}
