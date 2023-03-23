package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("sys_log")
public class Log extends Model<Log> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String content;
    private String time;
    private String user;
    private String ip;

}
