package com.github.sinsinpub.doc.web.controller;

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
public class JsonExceptionRenderer extends Controller implements Interceptor {

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
                LOG.warn("Unhandled exception catched from action controller", e);
                handleException(e);
            }
        } else {
            inv.invoke();
        }
    }

    protected void handleException(RuntimeException e) {
        int statusCode = 500;
        String message = isRetrievingRootCauseMessage() ? getMessageIfPossible(Throwables.getRootCause(e))
                : getMessageIfPossible(e);
        try {
            throw e;
        } catch (IllegalArgumentException iae) {
            statusCode = 400; // Bad Request
        } catch (DataNotFoundException dnfe) {
            statusCode = 404; // Not Found
        } catch (NullPointerException npe) {
            statusCode = 500;
        } catch (IllegalStateException ise) {
            statusCode = 500;
        } catch (ActiveRecordException are) {
            statusCode = 500;
        } catch (RuntimeSqlException rse) {
            statusCode = 500;
        } finally {
            LOG.warn(String.format("Response with: %d, %s", statusCode, message));
            renderError(statusCode, RenderFactory.me().getJsonRender(ERROR_JSON_KEY, message));
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
