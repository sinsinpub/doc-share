package com.github.sinsinpub.doc.web.model.manual;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sinsinpub.doc.web.DataSourceInitializer;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

/**
 * JFinal作者表示没想过表结构事先不存在的场景:(
 */
public class InitDbTables {

    @Test
    public void initTables() {
        try {
            DataSourceInitializer.initTablesAndData();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    @BeforeClass
    public static void setUp() {
        IDataSourceProvider pool = DataSourceInitializer.initDataSourcePool();
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(pool, false);
        ((IPlugin) pool).start();
        arp.start();
    }

}
