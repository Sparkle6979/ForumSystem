package com.example.forumsystem.service;

import com.example.forumsystem.dao.LoginTicketMapper;
import com.example.forumsystem.dao.UserMapper;
import com.example.forumsystem.pojo.LoginTicket;
import com.example.forumsystem.pojo.User;
import com.example.forumsystem.utils.ForumConstant;
import com.example.forumsystem.utils.ForumUtil;
import com.example.forumsystem.utils.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 15:04
 */
@Configuration
@Service
public class UserService implements ForumConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    LoginTicketMapper loginTicketMapper;

    @Autowired(required = false)
    private TemplateEngine templateEngine;

    @Value("${forum.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String, Object> register(User user) {
//        System.out.println(user);
//        返回结果处理
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }
//        System.out.println(user);

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }
//        System.out.println(user);

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));
        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));
        // 普通用户
        user.setType(0);
        // 未激活
        user.setStatus(0);
        user.setActivationCode(ForumUtil.generateUUID());

        // String.format可以用来填充字符串
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        user.setCreateTime(new Date());
//        System.out.println(user);
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/activation/101/code
        String url = domain + contextPath + "activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
//        System.out.println(url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    /**
     * 注册逻辑，判断用户id和激活码是否对应
     * @param userID
     * @param code
     * @return
     */
    public int activate(int userID,String code){
        User user = userMapper.selectById(userID);
        if(user.getStatus() != 0){
            return ACTIVATION_REAPEAT;
        } else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userID,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String,Object> login(String username,String password,long expiredSeconds){
        Map<String,Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = ForumUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(ForumUtil.generateUUID());
        // 状态0代表登陆成功
        loginTicket.setStatus(0);
        // 过期时间设置为当前时间 + expiredSeconds
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;

    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId,String headerUrl){
        return userMapper.updateHeader(userId,headerUrl);
    }

    public int updatePassword(int userId,String password){
        return userMapper.updatePassword(userId,password);
    }
}
