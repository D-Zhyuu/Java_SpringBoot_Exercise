package com.cy.store.service;

import com.cy.store.entity.User;

/* 用户模块业务层接口 */
public interface IUserService {
    /**
     * 用户注册方法
     * @param user 用户的数据对象
     */
    void reg(User user);

    /**
     * 用户登录功能
     * @param username 用户名
     * @param password 用户的密码
     * @return 当前匹配的用户数据，如果没有则返回null值
     */
    User login(String username, String password);

    /**
     * 用户修改密码功能
     * @param uid 用户uid
     * @param username 执行修改操作的用户名（一般就是用户本身）
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Integer uid, String username, String oldPassword, String newPassword);

    /**
     * 通过uid查询用户数据
     * @param uid 用户uid
     * @return 用户数据
     */
    User getByUid(Integer uid);

    /**
     * 修改用户个人信息
     * @param uid 用户uid
     * @param username 修改人
     * @param user 用户信息
     */
    void changeInfo(Integer uid, String username, User user);
}
