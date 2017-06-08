package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by LZF on 2017/5/31.
 * 注册页面post提交后所做的处理
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String register(Model model,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "rememberme" ,defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next") String next,
                        HttpServletResponse response) {

        try{
            Map<String,String> map = userService.register(username,password);
            if(map.containsKey("ticket")){//说明注册成功
                //将ticket下发到cookie
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);//设置有效期
                }
                response.addCookie(cookie);

                if(StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "rememberme" ,defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next") String next,
                        HttpServletResponse response) {
        try{
            Map<String,String> map = userService.login(username,password);
            if(map.containsKey("ticket")){//说明登录成功
                //将ticket下发到cookie
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");//可在同一应用服务器内共享cookie的方法：设置cookie.setPath("/");
                if(rememberme){
                    cookie.setMaxAge(3600*24*5);//设置有效期
                }
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }

        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model,
                           @RequestParam(value = "next", defaultValue = "") String next) {
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
