package com.github.sinsinpub.doc.web.model;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.github.sinsinpub.doc.web.exception.RuntimeSqlException;
import com.google.common.base.Preconditions;
import com.jfinal.kit.StrKit;
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
    public static final String LAST_SIGNIN_FROM = "last_signin_from";
    public static final String LAST_SIGNIN_AT = "last_signin_at";

    public static final User REPO = new User();

    public User() {
    }

    public User addDistinctUser(String email, String nickname, String spaceUrl, String pwHash,
            String pwSalt, String role) {
        User user = new User();
        user.set(User.EMAIL, email);
        user.set(User.NICKNAME, nickname);
        user.set(User.SPACE_URL, spaceUrl);
        user.set(User.PW_HASH, pwHash);
        user.set(User.PW_SALT, pwSalt);
        user.set(User.ROLE, role);
        addDistinctUser(user);
        return user;
    }

    public boolean addDistinctUser(User user) {
        return saveOrUpdateDistinctUser(user);
    }

    public boolean saveOrUpdateDistinctUser(User user) {
        String email = user.getStr(EMAIL);
        Preconditions.checkArgument(!StrKit.isBlank(email), "User.email must not been empty");

        boolean isUpdate = false;
        if (user.getStr(ID) == null) {
            user.set(ID, UUID.randomUUID().toString());
        }
        User existsUser = findByEmail(user.getStr(EMAIL));
        if (existsUser != null) {
            if (user.getStr(ID).equals(existsUser.getStr(ID))) {
                isUpdate = true;
            } else {
                throw new RuntimeSqlException(new SQLIntegrityConstraintViolationException(
                        "User.email has been occupied"));
            }
        }
        if (isUpdate) {
            user.set(UPDATED_AT, new Timestamp(System.currentTimeMillis()));
        }
        return isUpdate ? user.update() : user.save();
    }

    public User findByEmail(String email) {
        return findFirst(String.format("select * from %s where %s=?", TABLE, EMAIL), email);
    }

    public List<User> fetchAll() {
        List<User> list = find(String.format("select * from %s order by %s asc", TABLE, ID));
        return list;
    }

}
