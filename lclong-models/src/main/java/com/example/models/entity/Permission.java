package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_permission")
public class Permission {

    private Long id;
    private String name;
    private String url;
    private String description;
    private int category;
}
