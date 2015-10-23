package com.github.sinsinpub.doc.web;

import java.io.File;
import java.util.Properties;

import net.gescobar.jmx.annotation.Description;
import net.gescobar.jmx.annotation.ManagedAttribute;

import com.github.sinsinpub.doc.web.interceptor.AccessLoggingInterceptor;
import com.github.sinsinpub.doc.web.jmx.MBeanExporter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.render.ViewType;
import com.jfinal.server.WarPathProtectionHandler;

/**
 * Customer configuration for JFinal filter.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
@Description("应用自定义的JFinal核心Filter配置")
public class WebAppConfig extends JFinalConfig {

    public static final String OBJECT_NAME = "doc-share:type=Config,name=WebAppConfig";
    private static final Logger LOG = Logger.getLogger(WebAppConfig.class);
    private static Prop appCfg = PropKit.use("app-default.properties");

    public static Prop getProps() {
        return appCfg;
    }

    public void configConstant(Constants me) {
        // 尝试从文件系统特定位置读取配置文件来替换默认的
        try {
            Prop runtimeCfg = PropKit.use(new File("./conf/runtime.properties"));
            appCfg = runtimeCfg;
        } catch (IllegalArgumentException e) {
            LOG.info("No runtime configuration found, using default properties.");
        }
        me.setDevMode(getProps().getBoolean("jfinal.devMode", Boolean.FALSE));
        // 并不推荐用JSP，这里只是不想.html默认按FreeMarker渲染
        me.setViewType(ViewType.JSP);
    }

    public void configRoute(Routes me) {
        // 注册URL映射和控制器的映射定义
        RoutesDefines routes = new RoutesDefines();
        me.add(routes);
        MBeanExporter.INSTANCE.export(routes, RoutesDefines.OBJECT_NAME);
    }

    public void configPlugin(Plugins me) {
        // 注册默认数据源和AR插件
        IDataSourceProvider dataSource = DataSourceInitializer.initDataSourcePool();
        me.add((IPlugin) dataSource);
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(dataSource);
        me.add(arp);
        DataSourceInitializer.mappingTablesToEntityClasses(arp);
        // 注册MBean导出器插件
        me.add(MBeanExporter.INSTANCE);
        MBeanExporter.INSTANCE.export(this, OBJECT_NAME);
    }

    public void configInterceptor(Interceptors me) {
        // 注册访问日志拦截器
        AccessLoggingInterceptor ali = new AccessLoggingInterceptor();
        ali.setEnableAccessLog(WebAppConfig.getProps().getBoolean("app.controller.accessLog", true));
        ali.setEnableDbAccessLog(WebAppConfig.getProps().getBoolean("app.controller.dbAccessLog",
                false));
        me.addGlobalActionInterceptor(ali);
        MBeanExporter.INSTANCE.export(ali, AccessLoggingInterceptor.OBJECT_NAME);
    }

    public void configHandler(Handlers me) {
        boolean enableConsole = WebAppConfig.getProps().getBoolean("app.servlet.jolokiaConsole",
                true);
        if (enableConsole) {
            // 如果不启用Jolokia console就不添加跳过该地址的处理器
            // 要想彻底去除应该去web.xml中删除Agent Servlet
            me.add(new UrlSkipHandler("/console/.*", true));
        }

        // 注册特殊URL地址保护处理器
        WarPathProtectionHandler pph = new WarPathProtectionHandler();
        me.add(pph);
        MBeanExporter.INSTANCE.export(pph, WarPathProtectionHandler.OBJECT_NAME);
    }

    @Override
    public void beforeJFinalStop() {
        DataSourceInitializer.sendShutdown();
    }

    @Override
    public void afterJFinalStart() {
        if (DataSourceInitializer.checkIfTablesNotInited()) {
            LOG.error("Database tables has not been initialized yet.");
            DataSourceInitializer.initTablesAndData();
        }
    }

    @ManagedAttribute(writable = false, description = "当前配置属性")
    public Properties getProperties() {
        return getProps().getProperties();
    }

}
