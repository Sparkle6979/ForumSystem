package com.example.forumsystem.controller.interceptor;

import com.example.forumsystem.pojo.LoginTicket;
import com.example.forumsystem.pojo.User;
import com.example.forumsystem.service.UserService;
import com.example.forumsystem.utils.CookieUtil;
import com.example.forumsystem.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/22 11:10
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String value = CookieUtil.getValue(request,"ticket");
//        System.out.println("-------------" + value);
        if(value != null){
            LoginTicket ticket = userService.findLoginTicket(value);
            if(ticket != null && ticket.getStatus() == 0 && ticket.getExpired().after(new Date())){
                User user = userService.findUserById(ticket.getUserId());
                // 保存在ThreadLocal里
                hostHolder.SetUser(user);
            }
        }
        return true;
    }

//    传入到模版引擎中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if(user != null && modelAndView != null){
            modelAndView.addObject("loginUser",user);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
