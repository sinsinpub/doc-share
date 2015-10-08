package com.jfinal.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.webapp.WebAppContext;

import com.jfinal.core.Const;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.PathKit;
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
    private boolean running = false;
    private Server server;
    private WebAppContext webApp;

    public JettyWarServer(int port, String context) {
        if (port < 0 || port > 65536)
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        if (StrKit.isBlank(context))
            throw new IllegalStateException("Invalid context of web server: " + context);

        this.port = port;
        this.context = context;
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
        if (!available(port))
            throw new IllegalStateException("port: " + port + " already in use!");

        // deleteSessionData();

        System.out.println("Starting JFinal " + Const.JFINAL_VERSION);
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
        webApp = new WebAppContext();
        // 在启动过程中允许抛出异常终止启动并退出 JVM
        webApp.setThrowUnavailableOnStartupException(true);
        webApp.setContextPath(context);
        // webApp.setResourceBase(webAppDir);
        webApp.setWar(getWarLocation());
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        // webApp.setInitParams(Collections.singletonMap(
        // "org.mortbay.jetty.servlet.Default.useFileMappedBuffer", "false"));

        // persistSession(webApp);

        server.setHandler(webApp);
        changeClassLoader(webApp);

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
    private String getWarLocation() {
        ProtectionDomain protectionDomain = JettyWarServer.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        return location.toExternalForm();
    }

    private void changeClassLoader(WebAppContext webApp) {
        try {
            String classPath = getClassPath();
            JFinalClassLoader wacl = new JFinalClassLoader(webApp, classPath);
            wacl.addClassPath(classPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getClassPath() {
        return System.getProperty("java.class.path");
    }

    @SuppressWarnings("unused")
    private void deleteSessionData() {
        try {
            FileKit.delete(new File(getStoreDir()));
        } catch (Exception e) {
        }
    }

    private String getStoreDir() {
        String storeDir = PathKit.getWebRootPath() + "/../../session_data" + context;
        if ("\\".equals(File.separator))
            storeDir = storeDir.replaceAll("/", "\\\\");
        return storeDir;
    }

    @SuppressWarnings("unused")
    private void persistSession(WebAppContext webApp) {
        String storeDir = getStoreDir();

        try {
            SessionManager sm = webApp.getSessionHandler().getSessionManager();
            if (sm instanceof HashSessionManager) {
                ((HashSessionManager) sm).setStoreDirectory(new File(storeDir));
                return;
            }

            HashSessionManager hsm = new HashSessionManager();
            hsm.setStoreDirectory(new File(storeDir));
            SessionHandler sh = new SessionHandler();
            sh.setSessionManager(hsm);
            webApp.setSessionHandler(sh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean available(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    // should not be thrown, just detect port available.
                }
            }
        }
        return false;
    }

}
