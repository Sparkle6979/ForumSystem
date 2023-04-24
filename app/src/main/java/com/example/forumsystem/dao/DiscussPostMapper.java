package com.example.forumsystem.dao;

import com.example.forumsystem.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 11:31
 */
@Mapper
public interface DiscussPostMapper {

    // 根据UserID来查找帖子，有偏移量，且支持分页（每页有几行）
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // 统计某个user的帖子总数
    int selectDiscussPostRows(@Param("userId") int userId);

    // 插入一条帖子
    int insertDiscussPost(DiscussPost discussPost);

    // 查询一条帖子
    DiscussPost selectDiscussPostById(int id);

    // 更新评论数量
    int updateCommentCount(int id,int commentCount);
}
