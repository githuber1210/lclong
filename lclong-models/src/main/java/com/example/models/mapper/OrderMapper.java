package com.example.models.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.models.entity.Order;
import org.apache.ibatis.annotations.Update;


public interface OrderMapper extends BaseMapper<Order> {

    @Update("update orders set state = #{state},pay_time = #{payTime} where order_no = #{tradeNo}")
    void updateState(String tradeNo, int state, String payTime);

}
