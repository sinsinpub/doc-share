package com.jfinal.server;

import java.lang.reflect.Field;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Set;

import org.apache.jasper.runtime.TldScanner;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.GzipHandler;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

import com.jfinal.core.Const;
import com.jfinal.kit.StrKit;

/**
 * 原版的JettyServer不适应直接从WAR启动，只好另外写这个.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class JettyWarServer implements IServer {

    private int port;
    private String context;
    private boolean enableGzip = true;
    private boolean running = false;
    private Server server;
    private WebAppContext webApp;

    public JettyWarServer(int port, String context) {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        if (StrKit.isBlank(context))
            throw new IllegalStateException("Invalid context of web server: " + context);

        this.port = port;
        this.context = context;
    }

    public JettyWarServer(int port, String context, boolean gzip) {
        this(port, context);
        this.enableGzip = gzip;
    }

    public void start() {
        if (!running) {
            try {
                doStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
            running = true;
        }
    }

    public void stop() {
        if (running) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            running = false;
        }
    }

    private void doStart() {
        if (!JettyServer.available(port))
            throw new IllegalStateException("port: " + port + " already in use!");

        System.out.println("Starting JFinal " + Const.JFINAL_VERSION);
        server = new Server(port);
        webApp = new WebAppContext(getWarLocation(), context);
        // 不要返回版本号
        server.setSendServerVersion(false);
        if (isEnableGzip()) {
            // 对应响应内容启用GZIP压缩
            GzipHandler gzipHandler = new GzipHandler();
            gzipHandler.setHandler(webApp);
            server.setHandler(gzipHandler);
        } else {
            server.setHandler(webApp);
        }
        // 在启动过程中允许抛出异常终止启动并退出 JVM
        webApp.setThrowUnavailableOnStartupException(true);
        // 不允许列文件目录
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        // 允许暴露jar中的META-INF/resources资源
        webApp.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*\\.jar$");
        // 添加HttpOnly到cookies
        ((AbstractSessionManager) webApp.getSessionHandler().getSessionManager()).setHttpOnly(true);

        JettyServer.changeClassLoader(webApp);
        clearTldSystemUri();

        try {
            System.out.println("Starting web server on port: " + port);
            server.start();
            System.out.println("Starting Complete. Welcome To The JFinal World :)");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        return;
    }

    /**
     * Returns the location of the war (a trick, which is necessary for executable wars please see:
     * <a target="_blank" href=
     * "http://uguptablog.blogspot.de/2012/09/embedded-jetty-executable-war-with.html" >Embedded
     * Jetty with executable WAR</a>).
     * 
     * @return The war location.
     */
    static String getWarLocation() {
        ProtectionDomain protectionDomain = JettyWarServer.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        return location.toExternalForm();
    }

    @SuppressWarnings("unchecked")
    static void clearTldSystemUri() {
        try {
            Field f = TldScanner.class.getDeclaredField("systemUris");
            f.setAccessible(true);
            ((Set<String>) f.get(null)).clear();
        } catch (Exception e) {
            // ignored
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isEnableGzip() {
        return enableGzip;
    }

    public void setEnableGzip(boolean enableGzip) {
        this.enableGzip = enableGzip;
    }

    public boolean isRunning() {
        return running;
    }

}
