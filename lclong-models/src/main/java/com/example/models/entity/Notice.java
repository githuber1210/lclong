package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("sys_notice")
public class Notice {
      @TableId(value = "id",type = IdType.AUTO)
      private Integer id;
      private String title;
      private String content;
      private String author;
      private String createTime;


}
