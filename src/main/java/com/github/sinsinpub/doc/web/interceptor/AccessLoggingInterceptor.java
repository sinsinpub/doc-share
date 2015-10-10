package com.github.sinsinpub.doc.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.github.sinsinpub.doc.utils.DatetimeFormatUtils;
import com.github.sinsinpub.doc.web.model.AuditLog;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.ActionException;
import com.jfinal.log.Logger;

/**
 * 记HTTP访问日志的拦截器，对所有Action Controller全局生效.
 * <p>
 * 然而拦截器并不是原型模式的，实例化由配置初始化过程进行一次，之后用于AOP代理。
 * 
 * @author sin_sin
 * @version $Date: Oct 10, 2015 $
 */
public class AccessLoggingInterceptor implements Interceptor {

    private static final Logger LOG = Logger.getLogger(AccessLoggingInterceptor.class);
    private boolean enableAccessLog = true;
    private boolean enableDbAccessLog = false;

    public AccessLoggingInterceptor() {
    }

    @Override
    public void intercept(Invocation inv) {
        long start = System.currentTimeMillis();
        RuntimeException exp = null;
        try {
            inv.invoke();
        } catch (RuntimeException e) {
            exp = e;
            throw e;
        } finally {
            if (isEnableAccessLog()) {
                doAccessLog(inv, start, exp);
            }
            if (isEnableDbAccessLog()) {
                doDbAccessLog(inv);
            }
        }
    }

    protected void doAccessLog(Invocation inv, long start, RuntimeException exp) {
        Logger logger = Logger.getLogger(inv.getController().getClass());
        long end = System.currentTimeMillis();
        if (logger.isInfoEnabled()) {
            HttpServletRequest request = inv.getController().getRequest();
            HttpServletResponse response = inv.getController().getResponse();

            // 模仿NCSA公共格式生成一行日志
            String startTime = DatetimeFormatUtils.formatIso(new Date(start));
            // 登入User获取方式需要实现，下同
            String remoteUser = StringUtils.defaultString(request.getRemoteUser(), "-");
            String requestUrl = request.getRequestURI();
            if (request.getQueryString() != null) {
                requestUrl += "?" + request.getQueryString();
            }
            int statusCode = response.getStatus();
            if (exp instanceof ActionException) {
                statusCode = ((ActionException) exp).getErrorCode();
            }
            // 响应大小不方便取，直接写消耗毫秒在上面好了
            long takenTime = end - start;
            String referer = StringUtils.defaultString(request.getHeader("Referer"), "-");
            String userAgent = StringUtils.defaultString(request.getHeader("User-Agent"), "-");

            // Address User user [Time] "Method URI HTTP/1.1" Status Taken "Referer" "User-agent"
            String logLine = String.format("%s %s %s [%s] \"%s %s HTTP/1.1\" %s %s \"%s\" \"%s\"",
                    request.getRemoteAddr(), remoteUser, /* 取当前登入用户 */"-", startTime,
                    request.getMethod(), requestUrl, statusCode, takenTime, referer, userAgent);
            logger.info(logLine);
        }
    }

    protected void doDbAccessLog(Invocation inv) {
        try {
            HttpServletRequest request = inv.getController().getRequest();
            AuditLog.REPO.addAccessLog(request.getMethod(), request.getRequestURI(),
                    request.getRemoteAddr(), request.getHeader("User-Agent"),
                    request.getRemoteUser());
        } catch (RuntimeException e) {
            // 理论上记日志不应该出岔子，不过世事难料
            LOG.warn(e.toString());
        }
    }

    public boolean isEnableAccessLog() {
        return enableAccessLog;
    }

    public void setEnableAccessLog(boolean enableAccessLog) {
        this.enableAccessLog = enableAccessLog;
    }

    public boolean isEnableDbAccessLog() {
        return enableDbAccessLog;
    }

    public void setEnableDbAccessLog(boolean enableDbAccessLog) {
        this.enableDbAccessLog = enableDbAccessLog;
    }

}
