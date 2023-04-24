package com.example.forumsystem.dao;

import com.example.forumsystem.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 10:59
 */
// 有了Mapper注解，可以自动生成相应的映射文件
@Mapper
public interface UserMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
