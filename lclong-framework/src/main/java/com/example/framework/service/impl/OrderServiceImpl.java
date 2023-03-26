package com.example.framework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.framework.mq.CancelOrderSender;
import com.example.framework.service.IOrderService;
import com.example.models.entity.Order;
import com.example.models.mapper.OrderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements IOrderService {

    @Resource
    private CancelOrderSender cancelOrderSender;

    @Resource
    private OrderMapper orderMapper;

    @Override
    public int updateState(String tradeNo, int state, String payTime) {
        orderMapper.updateState(tradeNo, state, payTime);
        return 1;
    }

    @Override
    public void cancelOrder(String tradeNo) {
        orderMapper.updateState(tradeNo, 2, null);
    }

    @Override
    public void sendDelayMessageCancelOrder(String tradeNo) {
        long delayTimes = 10 * 1000;
        cancelOrderSender.sendMessage(tradeNo, delayTimes);
    }


}
