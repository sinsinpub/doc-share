package com.github.sinsinpub.doc.web.model;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sinsinpub.doc.web.exception.RuntimeSqlException;
import com.github.sinsinpub.doc.web.service.UserService;
import com.github.sinsinpub.doc.web.service.UserServiceImpl;
import com.google.common.base.Preconditions;
import com.jfinal.aop.Enhancer;
import com.jfinal.ext.plugin.activerecord.TableEntity;
import com.jfinal.ext.plugin.guice.GuicePlugin;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 上传下载文件的操作主体，注册用户.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
// 这是我们自定义的表映射注解
@TableEntity(User.TABLE)
// 这是JSR-250注解
@Resource
// 这个不是我们的hint，是JSR-330注解
@Singleton
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

    /** 由Model提供的处理本值对象专用服务方法的静态快捷方式，适合调用线程安全的那些方法时用 */
    public static final User REPO = new User();
    /** 简单静态实例，没有利用到DI容器管理依赖。而且要求它无状态，里面所有方法实现必需线程安全 */
    public static final UserService SERVICE = Enhancer.enhance(new UserServiceImpl());

    /**
     * 用Guice DI注入实现，然后再添加拦截增强.
     * <p>
     * 绑定方法除了按Guice标准，还利用了JSR-330注解@Singleton以及扫描JSR-250注解@Resouirce。
     * 
     * @see GuicePlugin 详细可以参考GuicePlugin扩展
     */
    @Inject
    private UserService userService;

    public UserService getUserService() {
        return Enhancer.enhance(userService);
    }

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

    public User addDistinctUser(User user) {
        saveOrUpdateDistinctUser(user);
        return user;
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
        return findFirst(String.format("select * from %s where %s = ? limit 1", TABLE, EMAIL), email);
    }

    public List<User> fetchAll() {
        List<User> list = find(String.format("select * from %s order by %s asc", TABLE, ID));
        return list;
    }

}
