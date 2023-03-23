package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_user")
@ToString
public class User implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Long id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "电话号码")
    private String telephone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    @ApiModelProperty(value = "用户角色")
    private String role;
    @ApiModelProperty(value = "用户创建方式 注册/创建")
    private String createBy;
    @ApiModelProperty(value = "用户创建时间")
    private String createTime;
    @ApiModelProperty(value = "用户最后登录时间")
    private String loginTime;
    @ApiModelProperty(value = "用户最后登入IP")
    private String loginIp;
    @ApiModelProperty(value = "用户是否启用 1启用 0停用")
    private boolean enabled;
    @ApiModelProperty(value = "用户是否删除 1删除")
    private boolean remove;
    @ApiModelProperty(value = "性别")
    private String sex;
    @TableField(exist = false)
    private String browser;
    @TableField(exist = false)
    private String Ipaddr;
    @TableField(exist = false)
    private String loginLocation;
    @TableField(exist = false)
    private String Os;
    @TableField(exist = false)
    private List<Menu> menus;
    @TableField(exist = false)
    private List<Permission> permissionList;


}
