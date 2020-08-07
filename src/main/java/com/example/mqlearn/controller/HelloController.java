package com.example.mqlearn.controller;

import com.example.mqlearn.config.RabbitConfig;
import com.example.mqlearn.sender.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @Autowired
    private HelloSender helloSender;

    //这里交换机模式为Direct，只要routingKey匹配即可发送指定对列
    @GetMapping("/noPointExchange")
    public String noPointExchange() {
        //这里没有指定exchange ,只指定routingKey，是因为在配置时，就已经把这个routingKey与一个exchange绑定起来了
        helloSender.send(null, RabbitConfig.queueName);
        return "success";
    }

    /**
     * 这里交换机模式为Direct，只要routingKey匹配即可发送指定对列
     * 这里测试下routingKey不匹配 此时消息队列里是没有发送是消息的
     * @return
     */

    /*@GetMapping("/noRoutingKey")
    public String noRoutingKey() {
        //这里有个坑，这个"commonExchange2"与创建的交换机名字不对应  "COMMON_EXCHANGE_2"这个才是对的
        //这坑废了半小时
        //routingKey不匹配 后面修改后又匹配，那之前法发的消息时会丢失的
        helloSender.noRoutingKey("COMMON_EXCHANGE_2", RabbitConfig.commonRoutingKey2);
        return "success";
    }*/

    /**
     * 测试生产者消息确认机制
     *  1：通过事务机制实现(这个影响mq的吞吐量，一般不采用)
     *: 2：通过生产者消息确认机制（publisher confirm）实现
     *       publisher confirm 发送确认分为两步，一是确认是否到达交换器，二是确认是否到达队列
     * @return
     */

   /* @GetMapping("/noRoutingKey2")
    public String noRoutingKey2() {
       //此方法用来测试生产者发送消息的确认机制之一是确认是否到达交换器
        helloSender.noRoutingKey("COMMON_EXCHANGE_2", RabbitConfig.commonRoutingKey2);
        return "success";
    }*/


    /**
     * 测试结果
     *                  消息唯一标识CorrelationData [id=Thu Aug 06 20:25:24 CST 2020]
     *                  true
     *                  send ack success
     * 所以这里的true只能保证消息到了交换机，不能保证到了queue
     * 此时回调函数只有   confirm函数
     * @return
     */

    /*@GetMapping("/noRoutingKey3")
    public String noRoutingKey3() {
        //此方法用来测试生产者发送消息到了交换机，
        // 但是不到queue,只要交换机不与任何队列匹配正确，但是消息附带的routing_key匹配能匹配到当前的交换机
        //就可实现此测试
        helloSender.noRoutingKey("COMMON_EXCHANGE_2", RabbitConfig.commonRoutingKey2);
        return "success";
    }*/


    /**
     * 此方法用来测试生产者发送消息的确认机制之二是确认是否到达队列
     * 这个测试相对简单，只要交换机和queue绑定正确，但是消息中附带的routing_key不与交换机中的routing_key匹配，
     * 消息就不会到queue中去，此时回调函数是returnedMessage和confirm函数 这里要注意
     *
     */
    @GetMapping("/noRoutingKey4")
    public String noRoutingKey4() {
        helloSender.noRoutingKey("COMMON_EXCHANGE_2", "dfghjkl");
        return "success";
    }


    /**
     * 下面模拟生产者发送状态的集中场景：
     *//*

	//1、exchange, queue 都正确, confirm被回调, ack=true
	@RequestMapping("/send1")
	@ResponseBody
	public String send1() {
		helloSender.send(null, RabbitConfig.queueName);
		return "success";
	}

	//2、exchange 错误, queue 正确, confirm被回调, ack=false
	@RequestMapping("/send2")
	@ResponseBody
	public String send2() {
		helloSender.send("fail-exchange", RabbitConfig.queueName);
		return "success";
	}

	//3、exchange 正确, queue 错误, confirm被回调, ack=true; return被回调 replyText:NO_ROUTE
	@RequestMapping("/send3")
	@ResponseBody
	public String send3() {
		helloSender.send(null, "fail-queue");
		return "success";
	}

	//4、exchange 错误, queue 错误, confirm被回调, ack=false
	@RequestMapping("/send4")
	@ResponseBody
	public String send4() {
		helloSender.send("fail-exchange", "fail-queue");
		return "success";
	}*/

}
