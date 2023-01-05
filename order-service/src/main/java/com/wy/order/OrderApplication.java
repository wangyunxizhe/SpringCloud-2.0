package com.wy.order;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@MapperScan("com.wy.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /* 改变负载规则方案一：全局改变。无论是user-service，还是pay-service/xxx-service都会遵循这里的负载规则（方案二在配置文件中） */
    /*@Bean
    public IRule myRule() {
        //Ribbon共有7种负载策略，默认是轮询。
        //此处将ribbon默认使用的 轮询策略 改为 随机策略
        return new RandomRule();
    }*/

}