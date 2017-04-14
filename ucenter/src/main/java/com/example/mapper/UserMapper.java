package com.example.mapper;

import com.example.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by mazhenhua on 2017/3/29.
 */

public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    UserInfo findById(@Param("id") Long id);

    @Select("SELECT * FROM t_user WHERE username = #{username} AND password = #{password}")
    UserInfo checkUser(@Param("username") String username, @Param("password") String password);


    @Insert("INSERT INTO t_user(username, password, nickname) VALUES(#{username}, #{password}, #{nickname})")
    int insert(@Param("username") String name, @Param("password") String password,
               @Param("nickname") String nickname);
}
