package com.github.sinsinpub.doc.web.model.manual;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.sinsinpub.doc.web.DataSourceInitializer;
import com.github.sinsinpub.doc.web.model.AuditLog;
import com.github.sinsinpub.doc.web.model.DocFile;
import com.github.sinsinpub.doc.web.model.User;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Record;

public class DbViewer {

    private static final Logger logger = Logger.getLogger(DbViewer.class);

    @Test
    public void listAccessLog() {
        List<AuditLog> list = AuditLog.REPO.fetchAll();
        for (AuditLog r : list) {
            logger.info(r.toJson());
        }
    }

    @Test
    public void listUser() {
        List<User> list = User.REPO.fetchAll();
        for (User r : list) {
            logger.info(r.toJson());
        }
    }

    @Test
    public void listDocFile() {
        List<DocFile> list = DocFile.REPO.fetchAll();
        for (DocFile r : list) {
            logger.info(r.toJson());
        }
    }

    // @Test
    public void listAppStatus() {
        List<Record> list = Db.find("select * from AppStatus");
        for (Record r : list) {
            logger.info(r.toString());
        }
    }

    @BeforeClass
    public static void setUp() {
        IDataSourceProvider pool = DataSourceInitializer.initDataSourcePool();
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(pool);
        DataSourceInitializer.mappingTablesToEntityClasses(arp);
        ((IPlugin) pool).start();
        arp.start();
    }

}
