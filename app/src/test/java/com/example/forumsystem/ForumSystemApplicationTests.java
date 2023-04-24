package com.example.forumsystem;

import com.example.forumsystem.dao.DiscussPostMapper;
import com.example.forumsystem.dao.UserMapper;
import com.example.forumsystem.pojo.DiscussPost;
import com.example.forumsystem.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class ForumSystemApplicationTests {

    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void DiscussPostTest(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
        }
    }

    @Test
    void selectDiscussPostRowsTest(){
        int discussPosts = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(discussPosts);
//        for (DiscussPost discussPost : discussPosts) {
//            System.out.println(discussPost);
//        }
    }

    @Test
    void UserID(){
        User user = userMapper.selectById(11);
        System.out.println(user);
//        for (DiscussPost discussPost : discussPosts) {
//            System.out.println(discussPost);
//        }
    }

    @Test
    void UserName(){
        User user = userMapper.selectByEmail("1249@123.com");
        System.out.println(user);
//        for (DiscussPost discussPost : discussPosts) {
//            System.out.println(discussPost);
//        }
    }


    @Test
    void InsertUser(){
        User user = new User(155,"ttt2","123456","12345","test@163.com",0,1,"acacac","http://images.nowcoder.com/head/138t.png",new Date());
        userMapper.insertUser(user);
    }


}
