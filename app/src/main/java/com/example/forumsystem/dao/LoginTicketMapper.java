package com.example.forumsystem.dao;

import com.example.forumsystem.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/21 18:15
 */
@Mapper
public interface LoginTicketMapper {

    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
             "values (#{userId},#{ticket},#{status},#{expired})"
    })
//    这个注解可以让主键字段自动生成
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


//    bfd88c857fc14acfb76aa3bd488ce4c8
//    bfd88c857fc14acfb76aa3bd488ce4c8
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({"update login_ticket set status = #{status} where ticket = #{ticket}"})
    int updateStatus(String ticket, int status);
}
