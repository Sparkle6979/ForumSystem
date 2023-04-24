package com.example.forumsystem.controller;

import com.example.forumsystem.pojo.Comment;
import com.example.forumsystem.pojo.DiscussPost;
import com.example.forumsystem.pojo.Page;
import com.example.forumsystem.pojo.User;
import com.example.forumsystem.service.CommentService;
import com.example.forumsystem.service.DiscussPostService;
import com.example.forumsystem.service.LikeService;
import com.example.forumsystem.service.UserService;
import com.example.forumsystem.utils.ForumConstant;
import com.example.forumsystem.utils.ForumUtil;
import com.example.forumsystem.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/22 22:05
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements ForumConstant {
    @Autowired
    DiscussPostService discussPostService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user == null){
            return ForumUtil.getJSONString(403,"您还未登陆！");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        return ForumUtil.getJSONString(0,"发布成功！");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDisscussPost(@PathVariable("discussPostId") int discussPostId, Page page, Model model){
        // 帖子本身
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);

        // 作者信息
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);

        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 评论信息
        page.setLimit(5);
        page.setRows(discussPost.getCommentCount());
        page.setPath("/discuss/detail/" + discussPostId);

        // 查询评论
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());

        List<Map<String,Object>> commentVoList = new ArrayList<>();

        if(commentList != null){
            for (Comment comment : commentList) {
                Map<String,Object> commetVo = new HashMap<>();
                // 评论vo
                commetVo.put("comment",comment);
                // 评论user
                commetVo.put("user",userService.findUserById(comment.getUserId()));
                // 评论数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commetVo.put("replyCount",replyCount);

                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commetVo.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commetVo.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                
                List<Map<String,Object>> replyVoList = new ArrayList<>();

                for (Comment reply : replyList) {
                    Map<String,Object> replyVo = new HashMap<>();
                    // 回复vo
                    replyVo.put("reply",reply);
                    // 评论user
                    replyVo.put("user",userService.findUserById(reply.getUserId()));
                    // 回复的目标
                    User target = reply.getTargetId() == 0 ? null:userService.findUserById(reply.getTargetId());
                    replyVo.put("target",target);

                    // 点赞数量
                    likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                    replyVo.put("likeCount", likeCount);
                    // 点赞状态
                    likeStatus = hostHolder.getUser() == null ? 0 :
                            likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                    replyVo.put("likeStatus", likeStatus);

                    replyVoList.add(replyVo);
                }
                commetVo.put("replys",replyVoList);
                commentVoList.add(commetVo);
            }
        }

        model.addAttribute("comments",commentVoList);

        return "/site/discuss-detail";
    }

}
