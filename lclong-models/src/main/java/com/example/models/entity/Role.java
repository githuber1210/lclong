package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;



@TableName("sys_role")
@Data
public class Role implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String roleKey;

    private String createTime;
    private String createBy;
    private String updateTime;
    private String updateBy;
    private Integer status;

}
