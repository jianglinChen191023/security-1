package com.spring.security1.mapper;

import com.spring.security1.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户的信息
     *
     * @param username
     * @return
     */
    @Select(value = "SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(String username);

}
