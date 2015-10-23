package com.github.sinsinpub.doc.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.github.sinsinpub.doc.hint.Utilities;

/**
 * Web常用操作工具集.
 * 
 * @author sin_sin
 * @version $Date: Oct 23, 2015 $
 */
@Utilities
public abstract class AjaxWebUtils {

    private AjaxWebUtils() {
    }

    /**
     * 判断请求是否用AJAX方式发起.
     * 
     * @param request HTTP请求上下文
     * @return true if HTTP header contains 'X-Requested-With: XMLHttpRequest'
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String headerName = "X-Requested-With";
        String headerValue = StringUtils.defaultIfEmpty(request.getHeader(headerName),
                request.getHeader(headerName.toLowerCase()));
        return StringUtils.equalsIgnoreCase(headerValue, "XMLHttpRequest");
    }

    /**
     * 从当前HTTP请求中识别客户端IP地址。相对于服务端来说，“远程地址”就是指客户端的地址。
     * <p>
     * 除了访问CGI基本属性REMOTE_ADDR，还可能会根据HTTP Header中的特殊属性来识别客户端的“真实”地址。
     * 
     * @param request HTTP请求上下文
     * @param tryHeaders 不仅仅只获取远程地址，也尝试从Header中查找常见转发、代理会使用的客户端IP标识
     * @return 客户端地址。如果没有传入request则直接返回UNK
     */
    public static String determineRemoteIp(HttpServletRequest request, boolean tryHeaders) {
        if (request == null) {
            return "UNK";
        }
        String clientIp = request.getRemoteAddr();
        if (tryHeaders) {
            if (StringUtils.isNotEmpty(request.getHeader("$WSRH"))) {
                // 这是使用WebSphere PlugIn转发时才会使用的特殊Header名
                clientIp = request.getHeader("$WSRH");
                // 以下是一些常见HTTP代理(反向代理)转发时使用的Header名
            } else if (StringUtils.isNotEmpty(request.getHeader("X-FORWARDED-FOR"))) {
                clientIp = request.getHeader("X-FORWARDED-FOR");
            } else if (StringUtils.isNotEmpty(request.getHeader("X-REAL-IP"))) {
                clientIp = request.getHeader("X-REAL-IP");
            } else if (StringUtils.isNotEmpty(request.getHeader("HTTP_CLIENT_IP"))) {
                clientIp = request.getHeader("HTTP_CLIENT_IP");
            } else if (StringUtils.isNotEmpty(request.getHeader("HTTP_X_FORWARDED_FOR"))) {
                clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
            } else if (StringUtils.isNotEmpty(request.getHeader("HTTP_X_FORWARDED"))) {
                clientIp = request.getHeader("HTTP_X_FORWARDED");
            } else if (StringUtils.isNotEmpty(request.getHeader("HTTP_FORWARDED_FOR"))) {
                clientIp = request.getHeader("HTTP_FORWARDED_FOR");
            } else if (StringUtils.isNotEmpty(request.getHeader("HTTP_FORWARDED"))) {
                clientIp = request.getHeader("HTTP_FORWARDED");
            }
        }
        return clientIp;
    }

}
