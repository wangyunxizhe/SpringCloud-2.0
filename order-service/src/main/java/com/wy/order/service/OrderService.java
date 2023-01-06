package com.wy.order.service;

import com.wy.order.feignService.UserClient;
import com.wy.order.mapper.OrderMapper;
import com.wy.order.pojo.Order;
import com.wy.order.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(Long orderId) {
        // 查询订单
        Order order = orderMapper.findById(orderId);

        //不使用Feign，通过restTemplate调用provider
        /*String url="http://user-service/user/"+order.getUserId();
        User user = restTemplate.getForObject(url, User.class);*/

        //使用Feign，代替restTemplate调用provider
        User user = userClient.findById(order.getUserId());

        order.setUser(user);
        // 返回
        return order;
    }
}
