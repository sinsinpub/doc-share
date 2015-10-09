package com.github.sinsinpub.doc.web.controller;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.model.User;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jfinal.core.Controller;

/**
 * 映射到`/api/user`处理用户认证相关操作的控制器.
 * 
 * @see RoutesDefines
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class UserController extends Controller {

    public void index() {
        renderJson("status", "ok");
    }

    public void get() {
        String cond = getPara(0);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(cond), "Required parameter missing");
        User user = User.REPO.findById(cond);
        if (user == null) {
            user = User.REPO.findByEmail(cond);
        }
        renderJson(user);
    }

    public void create() {
        String adminEmail = "admin@yourdomain";
        User user = User.REPO.addDistinctUser(adminEmail, "Administrator", "admin", "hmac-sha256",
                "public-random-salt", "admin");
        renderJson(user);
    }

    public void all() {
        renderJson(User.REPO.fetchAll());
    }

}
