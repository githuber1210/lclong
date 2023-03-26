package com.example.models.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal payPrice;
    private String username;
    private String createTime;
    private String payTime;
    private Integer state;
    private String orderNo;
    private Integer productId;


}