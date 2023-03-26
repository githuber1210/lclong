package com.example.framework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.models.entity.Order;

public interface IOrderService extends IService<Order>{

    int updateState(String tradeNo, int state, String payTime);

    void cancelOrder(String tradeNo);

    void sendDelayMessageCancelOrder(String orderId);
}
