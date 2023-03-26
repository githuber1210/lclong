package com.example.framework.mq;

import com.example.framework.service.IOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 取消订单消息的处理者
 */
@Component
public class CancelOrderReceiver {
    @Resource
    private IOrderService orderService;

    @RabbitHandler
    @RabbitListener(queues = "lclong.order.cancel")
    public void handle(String tradeNo){
        orderService.cancelOrder(tradeNo);
    }
}
