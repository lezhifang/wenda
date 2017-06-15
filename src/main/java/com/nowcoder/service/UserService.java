package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by LZF on 2017/5/30.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public Map<String, String> register(String username,String password){
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空！");
            return map;
        }
        //判断用户名是否存在
        User user = userDAO.selectByName(username);
        if(user != null){//存在相同用户名
            map.put("msg","用户名已经被注册！");
            return map;
        }

        //添加合法注册用户信息
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        String head = String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
        user.setHeadUrl(head);
        userDAO.addUser(user);

        //注册成功后，添加ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String, String> login(String username,String password){
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空！");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空！");
            return map;
        }
        //判断用户名是否存在
        User user = userDAO.selectByName(username);
        if(user == null){//用户不存在
            map.put("msg","用户不存在！");
            return map;
        }

        //登陆成功，添加ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(1, ticket);
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);//将ticket与用户进行关联
        loginTicket.setStatus(0);
        Date date = new Date();
        date.setTime(date.getTime() + 1000*3600*24);//一天时间 1000*3600*24   考虑删除
        loginTicket.setExpired(date);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User selectByName(String name){
        return userDAO.selectByName(name);
    }
}
