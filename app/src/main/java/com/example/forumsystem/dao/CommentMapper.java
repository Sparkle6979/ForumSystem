package com.example.forumsystem.dao;

import com.example.forumsystem.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/23 16:00
 */
@Mapper
public interface CommentMapper {
    // 按照回复时间进行分页
    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);
//
//    Comment selectCommentById(int id);
}
