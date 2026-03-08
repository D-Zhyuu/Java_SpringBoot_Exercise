package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/* 用户模块业务层的实现类 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        // 通过user参数来获取传递过来的username
        String username = user.getUsername();
        // 调用findByUsername(username)判断用户是否被注册过
        User result = userMapper.findByUsername(username);
        // 判断结果集是否为null
        if(result != null) {
            // 抛出用户名被占用的异常
            throw new UsernameDuplicatedException("用户名被占用");
        }

        // 补全数据：is_delete设置成0
        user.setIsDelete(0);
        // 补全数据：4个日志字段信息
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        // 执行注册业务功能的实现(rows == 1)
        Integer rows = userMapper.insert(user);
        if(rows != 1) {
            throw new InsertException("在用户注册过程中产生了未知的异常");
        }
    }
}
