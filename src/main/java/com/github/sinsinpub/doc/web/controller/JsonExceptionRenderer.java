package com.github.sinsinpub.doc.web.controller;

import org.eclipse.jetty.http.HttpStatus;

import com.github.sinsinpub.doc.web.exception.DataNotFoundException;
import com.github.sinsinpub.doc.web.exception.RuntimeSqlException;
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
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
        String message = e.getMessage();
        try {
            throw e;
        } catch (IllegalArgumentException iae) {
            statusCode = HttpStatus.BAD_REQUEST_400;
        } catch (DataNotFoundException dnfe) {
            statusCode = HttpStatus.NOT_FOUND_404;
        } catch (NullPointerException npe) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
        } catch (IllegalStateException ise) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
        } catch (ActiveRecordException are) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
        } catch (RuntimeSqlException rse) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR_500;
        } finally {
            LOG.warn(String.format("Response with: %d, %s", statusCode, message));
            renderError(statusCode, RenderFactory.me().getJsonRender(ERROR_JSON_KEY, message));
        }
    }

}
