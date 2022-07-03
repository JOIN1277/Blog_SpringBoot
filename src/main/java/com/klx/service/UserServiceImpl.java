package com.klx.service;

import com.klx.po.User;
import com.klx.dao.UserRepository;
import com.klx.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl implements UserService {   //实现了userService接口中的方法
    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUser(String username, String password) {  //checkUser登陆验证
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));  //使用dao层下的userRepository中的方法查询数据库
        return user;  //查询结构返回为user
    }
}
