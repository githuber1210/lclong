package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Select("select permission_id from sys_role_permission where role_id = #{roleId}")
    List<Long> selectPidsByRid(@Param("roleId") Long roleId);

}
