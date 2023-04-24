package com.example.forumsystem.utils;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/22 11:19
 */

import com.example.forumsystem.pojo.User;
import org.springframework.stereotype.Component;

/**
 * 线程容器，用于代替Session对象
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void SetUser(User user){
        users.set(user);
    }
    public User getUser(){
        return users.get();
    }
    // 线程清理
    public void clear(){
        users.remove();
    }
}


