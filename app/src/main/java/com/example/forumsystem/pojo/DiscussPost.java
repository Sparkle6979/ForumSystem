package com.example.forumsystem.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 11:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DiscussPost {

    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
