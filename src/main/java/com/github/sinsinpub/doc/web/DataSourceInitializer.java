package com.github.sinsinpub.doc.web;

import com.github.sinsinpub.doc.web.model.AuditLog;
import com.github.sinsinpub.doc.web.model.DocFile;
import com.github.sinsinpub.doc.web.model.User;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.Sqls;
import com.jfinal.plugin.activerecord.dialect.AnsiSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * RDBMS data-source init sequence process implementation supported by JFinal.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public abstract class DataSourceInitializer {

    private DataSourceInitializer() {
    }

    public synchronized static IDataSourceProvider initDataSourcePool() {
        Prop jdbcProps = PropKit.use(WebAppConfig.getProps().get("jfinal.dataSource",
                "jdbc.properties"));

        // 经典c3p0支持，不过现在用起来就各种小问题
        // C3p0Plugin dataSource = new C3p0Plugin(jdbcProps.getProperties());

        // Druid配置虽然麻烦些，而且jar有点大，但是可以正确被close
        DruidPlugin dataSource = new DruidPlugin(jdbcProps.get("jdbcUrl"), jdbcProps.get("user"),
                jdbcProps.get("password"), jdbcProps.get("driverClass"));
        dataSource.setInitialSize(jdbcProps.getInt("initialSize"));
        dataSource.setMinIdle(jdbcProps.getInt("minIdle"));
        dataSource.setMaxActive(jdbcProps.getInt("maxActive"));
        dataSource.setRemoveAbandoned(jdbcProps.getBoolean("removeAbandoned"));
        dataSource.setRemoveAbandonedTimeoutMillis(jdbcProps.getInt("removeAbandonedTimeoutMillis"));
        dataSource.setValidationQuery(jdbcProps.get("validationQuery"));

        return dataSource;
    }

    public synchronized static ActiveRecordPlugin initActiveRecordPlugin(
            IDataSourceProvider dataSource) {
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dataSource);
        arp.setDevMode(WebAppConfig.getProps().getBoolean("jfinal.devMode", Boolean.FALSE));
        arp.setShowSql(WebAppConfig.getProps().getBoolean("jfinal.devMode", Boolean.FALSE));
        // 统一让数据库字段都不区分大小写，不然操作值对象属性时都得被迫用全大写
        arp.setContainerFactory(new CaseInsensitiveContainerFactory());
        // 默认是MySQL方言，我们用H2/HSQLDB时要换ANSI标准
        arp.setDialect(new AnsiSqlDialect());
        return arp;
    }

    public static void mappingTablesToEntityClasses(ActiveRecordPlugin arp) {
        arp.addMapping(AuditLog.TABLE, AuditLog.class);
        arp.addMapping(User.TABLE, User.class);
        arp.addMapping(DocFile.TABLE, DocFile.class);
    }

    public static boolean checkIfTablesNotInited() {
        Record r = Db.findFirst("select 1 from AppStatus where inited=1");
        return r == null;
    }

    public static void sendShutdown() {
        // 用In-memory/file的H2/HSQLDB，如果不显式发关闭则会导致WebApp重载后数据文件仍然lock着不能使用
        Db.update("SHUTDOWN;");
    }

    public static void initTablesAndData() {
        Sqls.load(WebAppConfig.getProps().get("jfinal.initDbScript", "init-db.properties"));
        Db.update(Sqls.get("AppStatus"));
        Db.update(Sqls.get(AuditLog.TABLE));
        Db.update(Sqls.get(User.TABLE));
        Db.update(Sqls.get(DocFile.TABLE));

        Db.update("insert into AppStatus values(1, 1);");
        if (checkIfTablesNotInited()) {
            throw new IllegalStateException("Initializing failed.");
        }
        // JFinal的蛋疼初始化设计决定了无法做到启动时发现不对立即建表再继续启动并映射，只好建好结构先
        throw new IllegalStateException("Initialized, restart app plz.");
    }

}
