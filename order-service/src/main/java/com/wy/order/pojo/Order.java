package com.wy.order.pojo;

import com.wy.feign.pojo.User;
import lombok.Data;

@Data
public class Order {
    private Long id;
    private Long price;
    private String name;
    private Integer num;
    private Long userId;
    private User user;
}