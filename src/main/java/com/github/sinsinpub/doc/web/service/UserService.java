package com.github.sinsinpub.doc.web.service;

import java.util.Locale;

import javax.annotation.Resource;

import com.github.sinsinpub.doc.web.model.User;

/**
 * 从基本DB操作抽象、扩展后的专门业务服务层.
 */
@Resource(type = UserServiceImpl.class)
public interface UserService {

    /**
     * 演示事务的伪改名业务
     * 
     * @param oldEmail
     * @param newEmail
     * @param remoteAddr
     * @param locale
     * @return Updated User entity
     */
    User rename(String oldEmail, String newEmail, String remoteAddr, Locale locale);

    /**
     * 演示删除业务
     * 
     * @param email
     * @param locale
     * @return true if succeeds
     */
    boolean delete(String email, Locale locale);

}
