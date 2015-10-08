package com.github.sinsinpub.doc.web.model.manual;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sinsinpub.doc.web.DataSourceInitializer;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;

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
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(pool);
        ((IPlugin) pool).start();
        arp.start();
    }

}
