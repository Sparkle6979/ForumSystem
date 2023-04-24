package com.example.forumsystem.controller;

import com.example.forumsystem.pojo.User;
import com.example.forumsystem.service.LikeService;
import com.example.forumsystem.utils.ForumUtil;
import com.example.forumsystem.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/23 22:27
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(int entityType,int entityId){

        User user = hostHolder.getUser();
        // 进行点赞
        likeService.like(user.getId(),entityType,entityId);

        // 数量
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        // 状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
        // 返回的结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return ForumUtil.getJSONString(0,null,map);
    }
}
