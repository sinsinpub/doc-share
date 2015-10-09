package com.github.sinsinpub.doc.web.controller;

import java.util.List;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.model.AuditLog;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.plugin.activerecord.Page;

/**
 * 映射到`/api/log`处理审计日志查询请求的控制器.
 * 
 * @see RoutesDefines
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
@Before(JsonExceptionRenderer.class)
public class LogViewController extends Controller {

    public void index() {
        renderJson("status", "ok");
    }

    @Before(GET.class)
    public void access() {
        if (getPara() == null) {
            List<AuditLog> list = AuditLog.REPO.fetchAll();
            renderJson(list);
        } else {
            int pageNumber = getParaToInt(0);
            int pageSize = getParaToInt(1, 10);
            Page<AuditLog> page = AuditLog.REPO.fetchPage(pageNumber, pageSize, null,
                    (Object[]) null);
            renderJson(page);
        }
    }

}
