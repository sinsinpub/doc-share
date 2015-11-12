package com.github.sinsinpub.doc.web.service;

import java.sql.Timestamp;
import java.util.Locale;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sinsinpub.doc.hint.ThreadSafe;
import com.github.sinsinpub.doc.web.exception.DataNotFoundException;
import com.github.sinsinpub.doc.web.i18n.MessageResources;
import com.github.sinsinpub.doc.web.model.User;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 从基本DB操作抽象、扩展后的专门业务服务层.
 * <p>
 * 这里同时演示不止一个原子DB操作时的事务回滚，以及类似DDD设计风格的从属于Domain Model下的Repository和Service.
 * 
 * @author sin_sin
 * @version $Date: Oct 9, 2015 $
 */
@ThreadSafe
@Singleton
public class UserServiceImpl implements UserService {

    @Inject
    @Resource
    private User userRepo;

    public UserServiceImpl() {
    }

    /*
     * 演示事务的伪改名业务，其实根本不需要这些操作233
     * @param oldEmail
     * @param newEmail
     * @param remoteAddr
     * @param locale
     * @return Updated User entity
     */
    @Override
    @Before(Tx.class)
    public User rename(String oldEmail, String newEmail, String remoteAddr, Locale locale) {
        User user = getUserRepo().findByEmail(oldEmail);
        if (user == null) {
            throw new DataNotFoundException(MessageResources.format(locale,
                    "service.exception.userNotFound", oldEmail));
        }
        user.set(User.LAST_SIGNIN_AT, new Timestamp(System.currentTimeMillis()));
        user.set(User.LAST_SIGNIN_FROM, remoteAddr);
        user.update();

        user.set(User.EMAIL, newEmail);
        user.update();

        return user;
    }

    @Override
    @Before(Tx.class)
    public boolean delete(String email, Locale locale) {
        User user = getUserRepo().findByEmail(email);
        if (user == null) {
            throw new DataNotFoundException(MessageResources.format(locale,
                    "service.exception.userNotFound", email));
        }
        return user.delete();
    }

    public User getUserRepo() {
        // 如果没启用DI则只能取静态实例
        return userRepo == null ? User.REPO : userRepo;
    }

    public void setUserRepo(User userRepo) {
        this.userRepo = userRepo;
    }

}
