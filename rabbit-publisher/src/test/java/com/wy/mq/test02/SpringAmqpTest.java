package com.wy.mq.test02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //普通消息
    @Test
    public void testSendMessage2SimpleQueue() {
        //队列名称
        String queueName = "simple.queue";
        //消息
        String message = "hello, spring amqp!";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    //1秒内向队列中发送50条消息，让两个consumer来消费
    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "simple.queue";
        String message = "hello, message__";
        //1s内发完50条消息，让两个consumer监听
        for (int i = 1; i <= 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    //向指定交换机中发送消息
    @Test
    public void testSendFanoutExchange() {
        // 交换机名称
        String exchangeName = "wy.fanout";
        // 消息
        String message = "hello, every one!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    //向指定交换机中发送消息，并指定bindingKey，发送到绑定同样bindingKey的队列中
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "wy.direct";
        // 消息
        String message = "hello, red!";
        // 发送消息
//        rabbitTemplate.convertAndSend(exchangeName, "blue", message);
//        rabbitTemplate.convertAndSend(exchangeName, "yellow", message);
        rabbitTemplate.convertAndSend(exchangeName, "red", message);
    }

    //向topic模式的交换机发送消息
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "wy.topic";
        // 消息
        String message = "今天天气不错，我的心情好极了!";
        // 发送消息
//        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
        rabbitTemplate.convertAndSend(exchangeName, "china.weather", message);
    }

    //发送的消息体是个java对象
    @Test
    public void testSendObject2Queue() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "wyuan");
        map.put("age", 21);
        rabbitTemplate.convertAndSend("object.queue", map);
    }

}
