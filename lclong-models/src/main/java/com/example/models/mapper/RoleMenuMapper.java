package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Long> selectMidsByRid(@Param("roleId") Long roleId);
}
