package com.example.forumsystem.service;

import com.example.forumsystem.dao.CommentMapper;
import com.example.forumsystem.dao.DiscussPostMapper;
import com.example.forumsystem.pojo.Comment;
import com.example.forumsystem.utils.ForumConstant;
import com.example.forumsystem.utils.ForumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.beans.Transient;
import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/23 16:11
 */
@Service
public class CommentService implements ForumConstant {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;


    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    // 事务管理注解，涉及到多表操作
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空!");
        }

        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

        // 插入评论
        int rows = commentMapper.insertComment(comment);

        // 更新帖子的回复数量
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostMapper.updateCommentCount(comment.getEntityId(),count);
        }
        return rows;

    }

}
