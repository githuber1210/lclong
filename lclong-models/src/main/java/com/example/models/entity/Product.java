package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("product")
public class Product {
    private int id;

    private String title;

    private String author;

    private String date;

    private String press;

    private String abs;

    private String cover;

    @TableField(exist = false)
    private Category category;
}
