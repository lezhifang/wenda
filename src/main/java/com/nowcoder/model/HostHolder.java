package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by LZF on 2017/6/7.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users =  new ThreadLocal<User>();//后期解决为什么这么做

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}

