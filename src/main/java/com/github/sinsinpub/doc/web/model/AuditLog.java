package com.github.sinsinpub.doc.web.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.github.sinsinpub.doc.utils.DatetimeFormatUtils;
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
        saveNewAuditLog(addr, user, method, uri, agent, more);
    }

    public void addOpLog(String action, String args, String addr, String user, String memo) {
        saveNewAuditLog(addr, user, action, args, "Internal Operation", memo);
    }

    private boolean saveNewAuditLog(String addr, String user, String method, String uri,
            String agent, String ext) {
        AuditLog log = new AuditLog();
        UUID id = UUID.randomUUID();
        long time = System.currentTimeMillis();
        log.set(ID, id.toString());
        log.set(TIMESTAMP, time);
        log.set(ADDR, StringUtils.abbreviate(addr, 50));
        log.set(USER, StringUtils.abbreviate(user, 130));
        log.set(TIME, DatetimeFormatUtils.formatIso(new Date(time)));
        log.set(METHOD, StringUtils.abbreviate(method, 60));
        log.set(URI, StringUtils.abbreviate(uri, 255));
        log.set(AGENT, StringUtils.abbreviate(agent, 255));
        log.set(EXT, StringUtils.abbreviate(ext, 255));
        return log.save();
    }

    public List<AuditLog> fetchAll() {
        return find(String.format("select * from %s order by %s desc", TABLE, TIMESTAMP));
    }

    public Page<AuditLog> fetchPage(int pageNumber, int pageSize, String whereSql, Object... paras) {
        String querySql = null;
        if (StringUtils.isBlank(whereSql)) {
            querySql = String.format("from %s order by %s desc", TABLE, TIMESTAMP);
        } else {
            querySql = String.format("from %s where %s order by %s desc", TABLE, whereSql, TIMESTAMP);
        }
        return paras == null ? paginate(pageNumber, pageSize, "select *", querySql) : paginate(
                pageNumber, pageSize, "select *", querySql, paras);
    }

}
