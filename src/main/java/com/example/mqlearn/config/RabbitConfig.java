package com.example.mqlearn.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public final static String queueName = "COMMON_QUEUE";
    public final static String queueName3 = "COMMON_QUEUE_3";
    public final static String queueName4 = "HELLO_QUEUE";

    /**
     * 普通队列：
     */

    public final static String commonQueueName = "COMMON_QUEUE";
    public final static String commonRoutingKey = "COMMON_ROUTING_KEY";

    /**
     * 普通队列2：
     */

    public final static String commonQueueName2 = "COMMON_QUEUE_2";
    public final static String commonRoutingKey2 = "COMMON_ROUTING_KEY_2";




    /**
     * 死信队列：
     */

    public final static String deadQueueName = "DEAD_QUEUE";
    public final static String deadRoutingKey = "DEAD_ROUTING_KEY";

    /**
     * 死信交换机
     */

    public final static String deadExchangeName = "DEAD_EXCHANGE";


    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";



    @Bean
    public Queue helloQueue() {
        //将普通队列绑定到私信交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(DEAD_LETTER_QUEUE_KEY, deadExchangeName);
        args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKey);
        Queue queue = new Queue(queueName4, true, false, false, args);
        return queue;
    }


    /**
     * 普通交换机
     */

    public final static String commonExchangeName = "COMMON_EXCHANGE";


    /**
     * 普通交换机2
     */

    public final static String commonExchangeName2 = "COMMON_EXCHANGE_2";


    /**
     * 生成普通队列
     */


    @Bean
    public Queue commonQueue() {
        Queue queue = new Queue(commonQueueName, true);
        return queue;
    }

    /**
     * 生成普通队列2
     */


    @Bean
    public Queue commonQueue2() {
        Queue queue = new Queue(commonQueueName2, true);
        return queue;
    }


    /**
     * 生成普通直接交换机,策略为Direct 直接路由转发,
     * 消息中的routing-key与exchange中的binding-key匹配，相同则发送到这个queue
     * 策略种类介绍见mq.md
     */

    @Bean
    public DirectExchange commonExchange() {
        return new DirectExchange(commonExchangeName);
    }


    //此交换机queue绑定 但是routingKey不匹配
    @Bean
    public DirectExchange commonExchange2() {
        return new DirectExchange(commonExchangeName2);
    }

    //此bean在测试noRoutingKey3时需要关闭，其余需要打开
    @Bean
    public Binding bindingCommonExchange2(Queue commonQueue2, DirectExchange commonExchange2) {
        return BindingBuilder.bind(commonQueue2).to(commonExchange2).with(commonRoutingKey2);
    }



    /**
     * 将上述创建的普通对列与上面创建的普通直接交换机绑定
     * 这里就不用在写业务代码是再去绑定
     * @param commonQueue
     * @param commonExchange
     * @return
     */

    @Bean
    public Binding bindingDeadExchange(Queue commonQueue, DirectExchange commonExchange) {
        return BindingBuilder.bind(commonQueue).to(commonExchange).with(commonRoutingKey);
    }





    /**
     * 死信队列：
     */


    @Bean
    public Queue deadQueue2() {
        Queue deadQueue2 = new Queue(deadQueueName, true);
        return deadQueue2;
    }



    /**
     * 死信交换机
     * @return
     */


    @Bean
    public DirectExchange deadExchange2() {
        DirectExchange deadExchange2 = new DirectExchange(deadExchangeName);
        return deadExchange2;
    }


    /**
     *
     * @param deadQueue2
     * @param deadExchange2
     * @return
     */

    @Bean
    public Binding bindingDeadExchange2(Queue deadQueue2, DirectExchange deadExchange2) {
        return BindingBuilder.bind(deadQueue2).to(deadExchange2).with(deadRoutingKey);
    }



    /*@Bean(name = "helloQueue3")
    public Queue helloQueue3() {
        //将一个普通队列绑定到死信交换机上 ，当这个普通队列中消息变成死信时，只要配置参数正确，他就会这个死信交换机非诉讼发送到死信队列中去
        Map<String, Object> args = new HashMap<>(2);
        args.put(DEAD_LETTER_QUEUE_KEY, deadExchangeName);
        args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKey);
        Queue helloQueue3 = new Queue(queueName3, true, false, false, args);
        return helloQueue3;
    }*/

}

