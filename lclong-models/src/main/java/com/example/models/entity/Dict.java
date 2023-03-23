package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_dict")
public class Dict {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String value;
    private String type;
    private String time;

}
