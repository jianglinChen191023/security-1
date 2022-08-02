package com.spring.security1.mapper;

import com.spring.security1.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    /**
     * 根据用户名查询用户信息
     *
     * @param loginAcct 用户名
     * @return
     */
    @Select("SELECT id, login_acct as loginAcct, user_pswd as userPswd, user_name as userName, email, create_time as createTime FROM t_admin WHERE login_acct = #{loginAcct}")
    Admin getAdminByLoginAcct(String loginAcct);

}
