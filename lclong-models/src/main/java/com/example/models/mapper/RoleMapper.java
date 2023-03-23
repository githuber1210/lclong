package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where role_key = #{flag}")
    Long selectByFlag(@Param("flag") String flag);
}
