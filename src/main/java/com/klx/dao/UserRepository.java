package com.klx.dao;
import com.klx.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username, String password); //查找根据用户名密码在数据库查找
}
