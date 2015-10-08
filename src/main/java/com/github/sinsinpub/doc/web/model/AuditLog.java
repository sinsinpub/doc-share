package com.github.sinsinpub.doc.web.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.github.sinsinpub.doc.utils.DatetimeFormatUtils;
import com.google.common.base.Strings;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Log entity for auditing kinds of operations.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class AuditLog extends Model<AuditLog> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String TABLE = "AuditLog";
    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String ADDR = "addr";
    public static final String USER = "user";
    public static final String TIME = "time";
    public static final String METHOD = "method";
    public static final String URI = "uri";
    public static final String AGENT = "agent";
    public static final String EXT = "ext";

    public static final AuditLog REPO = new AuditLog();

    public AuditLog() {
    }

    public void addAccessLog(String method, String uri, String addr, String agent, String user,
            String more) {
        AuditLog log = new AuditLog();
        UUID id = UUID.randomUUID();
        long time = System.currentTimeMillis();

        log.set(ID, id.toString());
        log.set(TIMESTAMP, time);
        log.set(ADDR, addr);
        log.set(USER, user);
        log.set(TIME, DatetimeFormatUtils.formatIso(new Date(time)));
        log.set(METHOD, method);
        log.set(URI, uri);
        log.set(AGENT, agent);
        log.set(EXT, more);

        log.save();
    }

    public List<AuditLog> fetchAll() {
        return find(String.format("select * from %s order by %s desc", TABLE, TIMESTAMP));
    }

    public Page<AuditLog> fetchPage(int pageNumber, int pageSize, String whereSql, Object... paras) {
        String querySql = String.format("from %s where %s order by %s desc", TABLE, whereSql,
                TIMESTAMP);
        if (Strings.isNullOrEmpty(whereSql)) {
            querySql = String.format("from %s order by %s desc", TABLE, TIMESTAMP);
        }
        return paras == null ? paginate(pageNumber, pageSize, "select *", querySql) : paginate(
                pageNumber, pageSize, "select *", querySql, paras);
    }

}
