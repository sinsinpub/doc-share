package com.github.sinsinpub.doc.web;

import java.io.File;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.render.ViewType;

/**
 * Customer configuration for JFinal filter.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class WebAppConfig extends JFinalConfig {

    private static final Logger LOG = Logger.getLogger(WebAppConfig.class);
    private static Prop appCfg = PropKit.use("META-INF/application.properties");

    public static Prop getProps() {
        return appCfg;
    }

    public void configConstant(Constants me) {
        try {
            Prop runtimeCfg = PropKit.use(new File("./conf/runtime.properties"));
            appCfg = runtimeCfg;
        } catch (IllegalArgumentException e) {
            LOG.info("No runtime configuration found, using default properties.");
        }
        me.setDevMode(getProps().getBoolean("jfinal.devMode", Boolean.FALSE));
        me.setViewType(ViewType.JSP);
    }

    public void configRoute(Routes me) {
        me.add(new RoutesDefines());
    }

    public void configPlugin(Plugins me) {
        IDataSourceProvider dataSource = DataSourceInitializer.initDataSourcePool();
        me.add((IPlugin) dataSource);
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(dataSource);
        me.add(arp);
        DataSourceInitializer.mappingTablesToEntityClasses(arp);
    }

    public void configInterceptor(Interceptors me) {

    }

    public void configHandler(Handlers me) {

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

}
