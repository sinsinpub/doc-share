package com.github.sinsinpub.doc.web.service;

import java.sql.Timestamp;

import com.github.sinsinpub.doc.web.exception.DataNotFoundException;
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
public class UserService {

    private static final User REPO = User.REPO;

    public UserService() {
    }

    /**
     * 演示事务的伪改名业务，其实根本不需要这些操作233
     * 
     * @param oldEmail
     * @param newEmail
     * @param remoteAddr
     * @return Updated User entity
     */
    @Before(Tx.class)
    public User rename(String oldEmail, String newEmail, String remoteAddr) {
        User user = REPO.findByEmail(oldEmail);
        if (user == null) {
            throw new DataNotFoundException("User not found for " + oldEmail);
        }
        user.set(User.LAST_SIGNIN_AT, new Timestamp(System.currentTimeMillis()));
        user.set(User.LAST_SIGNIN_FROM, remoteAddr);
        user.update();

        user.set(User.EMAIL, newEmail);
        user.update();

        return user;
    }

}
