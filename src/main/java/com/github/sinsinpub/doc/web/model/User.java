package com.github.sinsinpub.doc.web.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 上传下载文件的操作主体，注册用户.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class User extends Model<User> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String TABLE = "User";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String NICKNAME = "nickname";
    public static final String SPACE_URL = "space_url";
    public static final String PW_HASH = "hash";
    public static final String PW_SALT = "salt";
    public static final String ROLE = "role";
    public static final String AVATAR_URL = "avatar_url";
    public static final String IS_BLOCKED = "blocked";
    public static final String SIGNUP_AT = "signup_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String LAST_SIGNIN_AT = "last_signin_at";

    public static final User REPO = new User();

    public User() {
    }

}
