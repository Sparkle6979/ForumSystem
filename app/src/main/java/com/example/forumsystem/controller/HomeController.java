package com.example.forumsystem.controller;

import com.example.forumsystem.pojo.DiscussPost;
import com.example.forumsystem.pojo.Page;
import com.example.forumsystem.pojo.User;
import com.example.forumsystem.service.DiscussPostService;
import com.example.forumsystem.service.LikeService;
import com.example.forumsystem.service.UserService;
import com.example.forumsystem.utils.ForumConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 15:06
 */
@Controller
public class HomeController implements ForumConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;

    @GetMapping("/index")
    public String getIndexPage(Page page, Model model){
        // 设置总数
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        page.setGap(3);

        //查询10条数据
        List<DiscussPost> list=discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        //用Map把post和user装到一起
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        //遍历post，用post里的id查询user
        if(list!=null){
            for (DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User user=userService.findUserById(post.getUserId());
                map.put("user",user);

                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                // 显示点赞数
                discussPosts.add(map);
            }
        }
//        System.out.println(discussPosts);
        //把我们准备好的数据传给model
        model.addAttribute("discussPosts",discussPosts);

        // 返回模板
        return "/index";
    }

}
