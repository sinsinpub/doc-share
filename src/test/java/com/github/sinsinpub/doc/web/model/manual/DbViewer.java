package com.github.sinsinpub.doc.web.model.manual;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

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

/**
 * 不想借助其它工具查看数据表时的简易方法，只能在应用停止(没有锁住数据文件)时用.
 */
public class DbViewer {

    private static final Logger logger = Logger.getLogger(DbViewer.class);
    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        logger.info(String.format("/**** TESTING METHOD [%s] */", testName.getMethodName()));
    }

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

    @Test
    public void joinTables() {
        List<DocFile> list = DocFile.REPO.find("select f.*,u.id as uid,u.nickname from DocFile f left join User u on f.user=u.email");
        for (DocFile r : list) {
            logger.info(r.toJson());
            logger.info(r.getStr(User.NICKNAME));
        }
    }

    // @Test
    public void listAppStatus() {
        List<Record> list = Db.find("select * from AppStatus");
        for (Record r : list) {
            logger.info(r.toString());
        }
    }

    // @Test
    public void executeSql() {
        logger.info("" + Db.update("update DocFile set user=?", "admin@yourdomain"));
    }

    @BeforeClass
    public static void setUp() {
        IDataSourceProvider pool = DataSourceInitializer.initDataSourcePool();
        ActiveRecordPlugin arp = DataSourceInitializer.initActiveRecordPlugin(pool);
        // DataSourceInitializer.mappingTablesToEntityClasses(arp);
        ((IPlugin) pool).start();
        arp.start();
    }

}
