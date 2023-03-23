package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_role_permission")
@Data
public class RolePermission {
    private Long roleId;
    private Long permissionId;

}
