package com.github.sinsinpub.doc.web.controller;

import java.util.UUID;

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
        String id = getPara(0);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Required id missing");
        User user = User.REPO.findById(id);
        renderJson(user);
    }

    public void create() {

        User user = new User();
        user.set(User.ID, UUID.randomUUID().toString());
        user.set(User.EMAIL, "admin@yourdomain");
        user.set(User.NICKNAME, "Administrator");
        user.set(User.SPACE_URL, "admin");
        user.set(User.PW_HASH, "hmac-sha256");
        user.set(User.PW_SALT, "public-random-salt");
        user.set(User.ROLE, "admin");

        user.save();

        renderJson(user);
    }

}
