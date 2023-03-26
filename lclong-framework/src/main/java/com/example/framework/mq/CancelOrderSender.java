package com.example.framework.mq;

import com.example.models.enums.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 取消订单消息的发出者
 */
@Component
public class CancelOrderSender {
    private static Logger LOGGER =LoggerFactory.getLogger(CancelOrderSender.class);
    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendMessage(String tradeNo, final long delayTimes){
        //给延迟队列发送消息
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),
                                    QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), tradeNo, message -> {
                    //给消息设置延迟毫秒值
                    message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                    return message;
                });
        LOGGER.info("send delay message orderId:{}",tradeNo);
    }
}
