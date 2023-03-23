package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.User;
import com.example.models.dto.Password;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password}")
    int updatePassword(Password password);
    @Select("select * from sys_user where username = #{username}")
    User findByUsername(String username);

}
