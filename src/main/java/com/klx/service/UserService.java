package com.klx.service;

import com.klx.po.User;

public interface UserService {  //用户登陆接口
    User checkUser(String username, String password);
}
