package com.github.sinsinpub.doc.web.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.ext.kit.DateKit;
import com.jfinal.ext.plugin.activerecord.TableEntity;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Log entity for auditing kinds of operations.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
@TableEntity(AuditLog.TABLE)
public class AuditLog extends Model<AuditLog> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String TABLE = "AuditLog";
    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String ADDR = "addr";
    public static final String USER = "user";
    public static final String TIME = "time";
    public static final String LEVEL = "level";
    public static final String MODULE = "module";
    public static final String METHOD = "method";
    public static final String ARGUMENTS = "args";
    public static final String URI = "uri";
    public static final String AGENT = "agent";
    public static final String OTHERS = "others";

    public static final String LEVEL_TRACE = "TRACE";
    public static final String LEVEL_DEBUG = "DEBUG";
    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_WARN = "WARN";
    public static final String LEVEL_ERROR = "ERROR";
    public static final String LEVEL_FATAL = "FATAL";

    public static final String MODULE_ACCESSLOG = "ActionHandler";

    public static final AuditLog REPO = new AuditLog();

    public AuditLog() {
    }

    public void addAccessLog(String method, String uri, String addr, String agent, String user) {
        saveNewAuditLog(null, addr, user, LEVEL_TRACE, MODULE_ACCESSLOG, method, null, uri, agent,
                null);
    }

    public void addOpLog(String level, String module, String action, String args, String addr,
            String user, String memo) {
        saveNewAuditLog(null, addr, user, level, module, action, args, null, null, memo);
    }

    /**
     * @param timestamp 动作发生时间戳，如果传入null则自动使用当前系统时间
     * @param addr 动作来源地址，例如客户端IP
     * @param user 动作来源用户标识，如果有的话
     * @param level 动作严重等级，例如TRACE、DEBUG、INFO、ERROR、FATAL
     * @param module 动作发生模块标识
     * @param method 动作发生方法名
     * @param args 动作提供的参数字符串化
     * @param uri 动作来源浏览器URI
     * @param agent 动作来源浏览器User-Agent
     * @param others 其它自定义信息
     * @return 成功插入记录时true，无需或无效时false
     */
    private boolean saveNewAuditLog(Long timestamp, String addr, String user, String level,
            String module, String method, String args, String uri, String agent, String others) {
        AuditLog log = new AuditLog();
        UUID id = UUID.randomUUID();
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
        log.set(ID, id.toString());
        log.set(TIMESTAMP, timestamp);
        log.set(ADDR, StringUtils.abbreviate(addr, 50));
        log.set(USER, StringUtils.abbreviate(user, 130));
        log.set(TIME, DateKit.formatIso(new Date(timestamp)));
        log.set(LEVEL, StringUtils.abbreviate(level, 10));
        log.set(MODULE, StringUtils.abbreviate(module, 255));
        log.set(METHOD, StringUtils.abbreviate(method, 255));
        log.set(ARGUMENTS, StringUtils.abbreviate(args, 255));
        log.set(URI, StringUtils.abbreviate(uri, 255));
        log.set(AGENT, StringUtils.abbreviate(agent, 255));
        log.set(OTHERS, StringUtils.abbreviate(others, 255));
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
