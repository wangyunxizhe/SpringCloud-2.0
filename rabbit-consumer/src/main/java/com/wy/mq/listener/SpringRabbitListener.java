package com.wy.mq.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

/**
 * Spring接收消息的类 -- Consumer
 */
@Component
public class SpringRabbitListener {

     /*@RabbitListener(queues = "simple.queue")
     public void listenSimpleQueue(String msg) {
         System.out.println("消费者接收到simple.queue的消息：【" + msg + "】");
     }*/

    //start - 默认情况下，rabbit会让消费者1、2进行消息预取，即每人拿25条（因为生产者发了50条），不会因为消费者1处理的快就多给
    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    //end - 如果不想让这样的情况发生，可以通过application的配置来控制消息预取的上线
    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    //start - 监听队列1消息
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者接收到fanout.queue1的消息：【" + msg + "】");
    }

    //end - 监听队列2消息，队列1和2都在FanoutConfig中绑定到了同一个广播模式的交换机中，所以都收到了消息
    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者接收到fanout.queue2的消息：【" + msg + "】");
    }

    //start - 队列1的bindingKey是"red", "blue"
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "wy.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg) {
        System.out.println("消费者接收到direct.queue1的消息：【" + msg + "】");
    }

    //end - 队列2的bindingKey是"red", "yellow"
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "wy.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者接收到direct.queue2的消息：【" + msg + "】");
    }

    //start - Topic模式跟DIRECT模式的区别：前者支持通配符。# 代表0个或多个单词
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(name = "wy.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"
    ))
    public void listenTopicQueue1(String msg) {
        System.out.println("消费者接收到topic.queue1的消息：【" + msg + "】");
    }

    //end - * 代表一个单词
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "topic.queue2"),
            exchange = @Exchange(name = "wy.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"
    ))
    public void listenTopicQueue2(String msg) {
        System.out.println("消费者接收到topic.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> msg) {
        System.out.println("接收到object.queue的消息：" + msg);
    }
}
