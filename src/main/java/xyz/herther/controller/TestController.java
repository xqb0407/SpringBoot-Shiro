package xyz.herther.controller;

import com.sun.net.httpserver.HttpsServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试页面
 * @Herther
 * 时间2020/7/5
 */
@Controller
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public String _Hello(){
        return "hello word";
    }

//    测试thymeleaf页面
    @GetMapping("/")
    public String testThymeleaf(Model model){
        model.addAttribute("username","Herther");
        return "index";
    }
    @GetMapping("/add")
    public String addUser( ){
        return "user/AddUser";
    }
    @GetMapping("/update")
    public String updateUser( ){
        return "user/UpdateUser";
    }
    @GetMapping("/tologin")
    public String login(){
        return "/login";
    }
}
