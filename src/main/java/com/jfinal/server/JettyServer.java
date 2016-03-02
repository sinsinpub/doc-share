/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.server;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.GzipHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.server.ssl.SslConnector;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

import com.jfinal.core.Const;
import com.jfinal.kit.FileKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;

/**
 * JettyServer is used to config and start jetty web server.<br>
 * Jetty version 8.1.8
 */
class JettyServer implements IServer {

    public static final String SSL_PORT_PROPERTY = "org.eclipse.jetty.ssl.port";
    public static final String KEYSTORE_PATH_PROPERTY = "org.eclipse.jetty.ssl.keystorePath";
    public static final String KEYSTORE_TYPE_PROPERTY = "org.eclipse.jetty.ssl.keystoreType";
    public static final String DEFAULT_KEYSTORE_PATH = "conf/keystore.jks";
    /**
     * Just used with default demo keystore.jks. Set yours via
     * <code>org.eclipse.jetty.ssl.keypassword</code>
     */
    private static final String DEFAULT_KEYPASSWORD = "doc-share";
    public static final String[] UNSAFE_PROTOCOLS = { "SSL", "SSLv2", "SSLv2Hello", "SSLv3" };
    public static final String[] UNSAFE_CIPHER_SUITES = { "SSL_RSA_WITH_DES_CBC_SHA",
            "SSL_DHE_RSA_WITH_DES_CBC_SHA", "SSL_DHE_DSS_WITH_DES_CBC_SHA",
            "SSL_RSA_EXPORT_WITH_RC4_40_MD5", "SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
            "SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA", "SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA",
            "SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
            "TLS_DHE_DSS_WITH_AES_256_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
            "TLS_DHE_DSS_WITH_AES_128_CBC_SHA", "SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA" };

    private String webAppDir;
    private int port;
    private int sslPort;
    private String context;
    private int scanIntervalSeconds;
    private boolean enableGzip = false;
    private boolean enableJmx = true;
    private boolean running = false;
    private Server server;
    private WebAppContext webApp;

    JettyServer(String webAppDir, int port, String context, int scanIntervalSeconds) {
        if (webAppDir == null)
            throw new IllegalStateException("Invalid webAppDir of web server: " + webAppDir);
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port of web server: " + port);
        if (StrKit.isBlank(context))
            throw new IllegalStateException("Invalid context of web server: " + context);

        this.webAppDir = webAppDir;
        this.port = port;
        this.context = context;
        this.scanIntervalSeconds = scanIntervalSeconds;
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

        deleteSessionData();

        System.out.println("Starting JFinal " + Const.JFINAL_VERSION);
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);

        if (getSslPort() > 0) {
            configureSsl(getSslPort(), server);
        }

        if (isEnableJmx()) {
            registerJettyMbeans(server);
        }

        webApp = new WebAppContext();
        webApp.setThrowUnavailableOnStartupException(true); // 在启动过程中允许抛出异常终止启动并退出 JVM
        webApp.setContextPath(context);
        webApp.setResourceBase(webAppDir);
        // webApp.setWar(JettyWarServer.getWarLocation());
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        webApp.setInitParameter("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", "false");
        // webApp.setInitParams(Collections.singletonMap("org.mortbay.jetty.servlet.Default.useFileMappedBuffer",
        // "false"));
        // webApp.setAttribute(WebInfConfiguration.WEBINF_JAR_PATTERN, ".*\\.jar$");
        // 允许暴露jar中的META-INF/resources资源
        webApp.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*\\.jar$");
        ((AbstractSessionManager) webApp.getSessionHandler().getSessionManager()).setHttpOnly(true);

        persistSession(webApp);

        if (isEnableGzip()) {
            GzipHandler gzipHandler = new GzipHandler();
            gzipHandler.setHandler(webApp);
            server.setHandler(gzipHandler);
        } else {
            server.setHandler(webApp);
        }

        changeClassLoader(webApp);

        // configureScanner
        if (scanIntervalSeconds > 0) {
            Scanner scanner = new Scanner(PathKit.getRootClassPath(), scanIntervalSeconds) {
                public void onChange() {
                    try {
                        System.err.println("\nLoading changes ......");
                        webApp.stop();
                        JFinalClassLoader loader = new JFinalClassLoader(webApp, getClassPath());
                        webApp.setClassLoader(loader);
                        webApp.start();
                        System.err.println("Loading complete.");
                    } catch (Exception e) {
                        System.err.println("Error reconfiguring/restarting webapp after change in watched files");
                        e.printStackTrace();
                    }
                }
            };
            System.out.println("Starting scanner at interval of " + scanIntervalSeconds
                    + " seconds.");
            scanner.start();
        }

        try {
            System.out.println("Starting web server on port: " + port);
            server.start();
            System.out.println("Starting Complete. Welcome To The JFinal World :P");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
        return;
    }

    static void configureSsl(int sslPort, Server server) {
        SslContextFactory sslContextFactory = new SslContextFactory(false);
        sslContextFactory.setKeyStorePath(System.getProperty(KEYSTORE_PATH_PROPERTY,
                DEFAULT_KEYSTORE_PATH));
        if (System.getProperty(KEYSTORE_TYPE_PROPERTY) != null) {
            sslContextFactory.setKeyStoreType(System.getProperty(KEYSTORE_TYPE_PROPERTY));
        }
        if (System.getProperty(SslContextFactory.KEYPASSWORD_PROPERTY) == null) {
            sslContextFactory.setKeyManagerPassword(DEFAULT_KEYPASSWORD);
        } else {
            sslContextFactory.setKeyManagerPassword(null);
        }
        sslContextFactory.setAllowRenegotiate(false);
        sslContextFactory.setNeedClientAuth(false);
        sslContextFactory.setWantClientAuth(false);
        sslContextFactory.setExcludeProtocols(UNSAFE_PROTOCOLS);
        sslContextFactory.setExcludeCipherSuites(UNSAFE_CIPHER_SUITES);
        SslConnector sslConnector = new SslSelectChannelConnector(sslContextFactory);
        sslConnector.setPort(sslPort);
        server.addConnector(sslConnector);
    }

    static void registerJettyMbeans(Server server) {
        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.getContainer().addEventListener(mbContainer);
        server.addBean(mbContainer);
        mbContainer.addBean(Log.getRootLogger());
    }

    static void changeClassLoader(WebAppContext webApp) {
        try {
            String classPath = getClassPath();
            JFinalClassLoader wacl = new JFinalClassLoader(webApp, classPath);
            wacl.addClassPath(classPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getClassPath() {
        return System.getProperty("java.class.path");
    }

    void deleteSessionData() {
        try {
            FileKit.delete(new File(getStoreDir()));
        } catch (Exception e) {
        }
    }

    String getStoreDir() {
        String storeDir = PathKit.getWebRootPath() + "/../../session_data" + context;
        if ("\\".equals(File.separator))
            storeDir = storeDir.replaceAll("/", "\\\\");
        return storeDir;
    }

    void persistSession(WebAppContext webApp) {
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

    static boolean available(int port) {
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

    public int getSslPort() {
        String portProp = System.getProperty(SSL_PORT_PROPERTY);
        if (sslPort == 0 && portProp != null) {
            sslPort = Integer.valueOf(portProp);
        }
        if (sslPort < 0 || sslPort > 65535 || sslPort == port)
            throw new IllegalArgumentException("Invalid port of secured server: " + getSslPort());
        return sslPort;
    }

    public boolean isEnableGzip() {
        return enableGzip;
    }

    public void setEnableGzip(boolean enableGzip) {
        this.enableGzip = enableGzip;
    }

    public boolean isEnableJmx() {
        return enableJmx;
    }

    public void setEnableJmx(boolean enableJmx) {
        this.enableJmx = enableJmx;
    }

}
