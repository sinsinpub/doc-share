package com.github.sinsinpub.doc.web.controller;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.controller.base.JsonAwareController;
import com.github.sinsinpub.doc.web.controller.base.JsonResponseValidator;
import com.github.sinsinpub.doc.web.exception.DataNotFoundException;
import com.github.sinsinpub.doc.web.model.AuditLog;
import com.github.sinsinpub.doc.web.model.User;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.kit.StrKit;

/**
 * 映射到`/api/user`处理用户认证相关操作的控制器.
 * 
 * @see RoutesDefines
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class UserController extends JsonAwareController {

    @Before(NoUrlPara.class)
    public void index() {
        renderJson("status", "ok");
    }

    public void get() {
        String cond = getPara(0);
        if (cond == null) {
            cond = getPara("user");
        }
        Preconditions.checkArgument(!Strings.isNullOrEmpty(cond), "Required parameter missing");
        User user = User.REPO.findById(cond);
        if (user == null) {
            user = User.REPO.findByEmail(cond);
        }
        if (user == null) {
            throw new DataNotFoundException(formatMessage("service.exception.userNotFound", cond));
        } else {
            renderJson(user);
        }
    }

    @Before(UserFormValidator.class)
    public void create() {
        User form = getModel(User.class);
        User user = null;
        if (form == null || StrKit.isBlank(form.getStr(User.EMAIL))) {
            String adminEmail = "admin@yourdomain";
            user = User.REPO.addDistinctUser(adminEmail, "Administrator", "admin", "hmac-sha256",
                    "public-random-salt", "admin");
            renderJson(user);
        } else {
            // form.set(User.NICKNAME, form.getStr(User.EMAIL));
            form.set(User.SPACE_URL, form.getStr(User.NICKNAME));
            form.set(User.PW_HASH, "233");
            form.set(User.PW_SALT, "874");
            user = User.REPO.addDistinctUser(form);
            statusCreated();
            renderJson(user);
        }
        AuditLog.REPO.addOpLog(AuditLog.LEVEL_INFO, getClass().getSimpleName(), "CreateUser",
                user.toJson(), getRemoteAddr(), null, null);
    }

    public void all() {
        renderJson(User.REPO.fetchAll());
    }

    public void rename() {
        String sourceEmail = getPara("src", "admin1");
        String targetEmail = getPara("tar", "admin2");
        try {
            User user = User.SERVICE.rename(sourceEmail, targetEmail, getRemoteAddr(), getLocale());
            AuditLog.REPO.addOpLog(AuditLog.LEVEL_INFO, getClass().getSimpleName(), "RenameUser",
                    user.toJson(), getRemoteAddr(), null, null);
            renderJsonEmpty();
            statusAccepted();
        } catch (RuntimeException e) {
            // 操作成功和操作失败时记的日志不一样，而且记日志操作不能在业务操作的数据事务当中
            AuditLog.REPO.addOpLog(AuditLog.LEVEL_ERROR, getClass().getSimpleName(), "RenameUser",
                    getPara(), getRemoteAddr(), null, e.toString());
            throw e;
        }
    }

    public void delete() {
        String email = getPara(0);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(email), "Required parameter missing");
        try {
            User.SERVICE.delete(email, getLocale());
            AuditLog.REPO.addOpLog(AuditLog.LEVEL_INFO, getClass().getSimpleName(), "DeleteUser",
                    email, getRemoteAddr(), null, null);
            renderJsonEmpty();
        } catch (RuntimeException e) {
            AuditLog.REPO.addOpLog(AuditLog.LEVEL_ERROR, getClass().getSimpleName(), "DeleteUser",
                    email, getRemoteAddr(), null, e.toString());
            throw e;
        }
    }

    public static class UserFormValidator extends JsonResponseValidator {

        @Override
        protected void validate(Controller c) {
            User form = c.getModel(User.class);
            if (form.getStr(User.EMAIL) != null) {
                validateRequiredStringValue(form.getStr(User.NICKNAME), ERROR_KEY_PREFIX
                        + User.NICKNAME, "User name must not be empty");
            }
        }

        @Override
        protected boolean isResponseStatusOk() {
            return true;
        }

    }

}
