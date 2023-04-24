package com.example.forumsystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/23 15:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int id;
//    发布评论/回复的userid
    private int userId;
//    评论 or 回复
    private int entityType;
//    评论的帖子id
    private int entityId;
//    回复的人的id
    private int targetId;
    private String content;

    // 回复状态，0代表有效
    private int status;
    private Date createTime;
}
