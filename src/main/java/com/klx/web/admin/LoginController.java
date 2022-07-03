package com.klx.web.admin;

import com.klx.po.User;
import com.klx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
@Controller
@RequestMapping("/admin")   //登陆目录 /admin
public class LoginController {
    @Autowired
    private UserService userService;
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }     //登陆页面 /admin/login.html
    @PostMapping("/login")   // admin/login.html登陆页面
    //login登陆方法接收 username和password
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password); //调用userService接口下的checkUser方法验证
        if (user != null) {   //根据接口返回user查找的用户，如果没有该用户
            user.setPassword(null);  //置空，避免密码显示出来
            session.setAttribute("user",user);
            return "admin/index";  //返回登陆成功首页面
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/admin";  //重新返回登陆页面
        }
    }
    @GetMapping("/logout") //注销
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";  //跳转登录页面
    }
}
