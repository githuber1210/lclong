package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@TableName("sys_menu")
public class Menu implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long pid;
    private String name;
    private String icon;
    private String path;
    private String component;
    private String num;
    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    @TableField(exist = false)
    private List<Menu> children;

}
