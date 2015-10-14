package com.jfinal.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.sinsinpub.doc.ApplicationVersion;
import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

/**
 * 保护WAR包敏感地址的处理器.
 * <p>
 * 由于WAR打包时不得不将jetty, servlet-api, jfinal等类放到根目录，有必要在运行时限制它们的直接访问。<br>
 * 顺便限制直接访问.jsp文件，并添加一些安全Header。
 * 
 * @author sin_sin
 * @version $Date: Oct 12, 2015 $
 */
public class WarPathProtectionHandler extends Handler {

    /** 当前嵌入的组件会引入com, org, javax这几个目录名开始的包。其余非必要文件已经在打包时被排除。 */
    protected static final String[] DEFAULT_IGNORE_PATH_PREFIXES = { "/com/", "/org/", "/javax/" };
    /** 一般.jsp这些文件不建议直接通过URL访问。应由控制器指派渲染，访问的是映射过不含后缀的地址。 */
    protected static final String[] DEFAULT_IGNORE_PATH_SUFFIXES = { ".jsp", ".jspx" };
    private String[] ignorePathPrefixes = DEFAULT_IGNORE_PATH_PREFIXES;
    private String[] ignorePathSuffixes = DEFAULT_IGNORE_PATH_SUFFIXES;
    private boolean addCorsHeaders = true;

    public WarPathProtectionHandler() {
        super();
    }

    public WarPathProtectionHandler(String... ignorePrefixes) {
        this();
        this.ignorePathPrefixes = ignorePrefixes;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response,
            boolean[] isHandled) {
        // 如果没有屏蔽嵌入jetty响应的Server头，可以用我们自己应用的替换
        if (null == response.getHeader("Server")) {
            response.addHeader("Server", ApplicationVersion.getInstance().getApplicationName());
        }
        if (isAddCorsHeaders()) {
            addCorsHeaders(response);
        }
        if (isTargetStartsWithPrefix(target)) {
            // 直接返回404响应，以免被探测
            HandlerKit.renderError404(request, response, isHandled);
            return;
        }
        if (isTargetEndsWithSuffix(target)) {
            HandlerKit.renderError404(request, response, isHandled);
            return;
        }
        nextHandler.handle(target, request, response, isHandled);
    }

    protected boolean isTargetStartsWithPrefix(String target) {
        if (getIgnorePathPrefixes() != null) {
            for (String prefix : getIgnorePathPrefixes()) {
                if (target.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isTargetEndsWithSuffix(String target) {
        if (getIgnorePathSuffixes() != null) {
            for (String prefix : getIgnorePathSuffixes()) {
                if (target.endsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 并不是真的CORS，只是添加防止XSS常用的响应Header.
     * 
     * @param response
     */
    protected void addCorsHeaders(HttpServletResponse response) {
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "sameorigin");
        response.addHeader("X-XSS-Protection", "1; mode=block");
    }

    public String[] getIgnorePathPrefixes() {
        return ignorePathPrefixes;
    }

    public void setIgnorePathPrefixes(String[] ignorePathPrefixes) {
        this.ignorePathPrefixes = ignorePathPrefixes;
    }

    public String[] getIgnorePathSuffixes() {
        return ignorePathSuffixes;
    }

    public void setIgnorePathSuffixes(String[] ignorePathSuffixes) {
        this.ignorePathSuffixes = ignorePathSuffixes;
    }

    public boolean isAddCorsHeaders() {
        return addCorsHeaders;
    }

    public void setAddCorsHeaders(boolean addCorsHeaders) {
        this.addCorsHeaders = addCorsHeaders;
    }

}
