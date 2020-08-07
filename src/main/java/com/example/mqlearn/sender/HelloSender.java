package com.example.mqlearn.sender;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    /**
     * 1:通过实现ConfirmCallBack接口，消息发送到交换器Exchange后触发回调。
     */

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String exchange, String routingKey) {
        String context = "你好现在是 " + new Date();
        System.out.println("send content = " + context);
        //this.rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.convertAndSend(exchange, routingKey, context);
    }


    public void noRoutingKey(String exchange, String routingKey) {
        String context = "你好现在是 " + new Date();
        System.out.println("send content = " + context);
        //this.rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(new Date().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, context,correlationData);
    }



    public void testDead(String exchange, String routingKey) {
        String context = "你好现在是 " + new Date();
        System.out.println("send content = " + context);
        //this.rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.convertAndSend(exchange, routingKey, context);
    }

    /**
     * 失败后return回调：
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("send fail return-message = " + new String(message.getBody()) + ", replyCode: " + replyCode
            + ", replyText: " + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey);
    }



   //只要消息成功发送到交换器Exchange，此方法就会执行，不管有没有到队列中去
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("消息唯一标识"+correlationData);
        System.out.println(b);
        if (!b) {
            System.out.println("send ack fail, cause = " + s);
        } else {
            System.out.println("send ack success");
        }
    }
}
