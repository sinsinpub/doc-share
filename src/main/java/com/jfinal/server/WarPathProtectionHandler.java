package com.jfinal.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.HandlerKit;

/**
 * 由于WAR打包时不得不将jetty, servlet-api, jfinal等类放到根目录，有必要在运行时限制它们的直接访问.
 * 
 * @author sin_sin
 * @version $Date: Oct 12, 2015 $
 */
public class WarPathProtectionHandler extends Handler {

    /** 当前嵌入的组件会引入com, org, javax这几个目录名开始的包。其余非必要文件已经在打包时被排除。 */
    protected static final String[] DEFAULT_IGNORE_PATH = { "/com/", "/org/", "/javax/" };
    private String[] ignorePathPrefixes = DEFAULT_IGNORE_PATH;

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
        if (isTargetStartsWithPrefix(target)) {
            // 直接返回404响应，以免被探测
            HandlerKit.renderError404(request, response, isHandled);
            return;
        }
        nextHandler.handle(target, request, response, isHandled);
    }

    protected boolean isTargetStartsWithPrefix(String target) {
        for (String prefix : getIgnorePathPrefixes()) {
            if (target.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public String[] getIgnorePathPrefixes() {
        return ignorePathPrefixes;
    }

    public void setIgnorePathPrefixes(String[] ignorePathPrefixes) {
        this.ignorePathPrefixes = ignorePathPrefixes;
    }

}
