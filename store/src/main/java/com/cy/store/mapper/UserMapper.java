package com.cy.store.mapper;

import com.cy.store.entity.User;

import java.util.Date;

/* 用户模块的持久层接口 */
public interface UserMapper {
    /**
     * 插入用户的数据
     * @param user 用户的数据
     * @return 受影响的行数(增、删、改，都会有受影响的行数作为返回值，可以根据返回值来判断是否执行成功)
     */
    Integer insert(User user);

    /**
     * 根据用户名来查询用户的数据
     * @param username 用户名
     * @return 如果找到对应的用户则返回这个用户的数据，如果没有找到则返回null值
     */
    User findByUsername(String username);

    /**
     * 根据uid修改密码
     * @param uid 用户uid
     * @param password 新密码
     * @param modifiedUser 修改人
     * @param modifiedTime 修改时间
     * @return 受影响的行数
     */
    Integer updatePasswordByUid(Integer uid,
                                String password,
                                String modifiedUser,
                                Date modifiedTime);

    /**
     * 根据uid查询用户的数据
     * @param uid 用户uid
     * @return 用户的数据，没有找到对应用户返回null
     */
    User findByUid(Integer uid);

    /**
     * 根据uid修改用户个人资料
     * @param user 用户对象
     * @return 受影响的行数
     */
    Integer updateInfoByUid(User user);
}
