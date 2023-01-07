package com.wy.user.web;

import com.wy.user.config.ConfigProperty;
import com.wy.user.pojo.User;
import com.wy.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user")
//可实现nacos配置项--@Value("${pattern.dateformat}")的热更新。
@RefreshScope//Tips：如果使用的是@ConfigurationProperties读取配置方式，那么不使用@RefreshScope一样可以热更新
public class UserController {

    @Autowired
    private UserService userService;

    //nacos配置中心里的配置项
    @Value("${pattern.dateformat}")
    private String dateformat;

    @Autowired
    private ConfigProperty prop;

    /**
     * 路径： /user/110
     *
     * @param id   用户id
     * @param talk 在gateway模块中，通过配置文件配置的过滤器增加的请求头
     * @return 用户
     */
    @GetMapping("/{id}")
    public User queryById(@PathVariable("id") Long id, @RequestHeader(value = "Talk", required = false) String talk) {
        System.out.println("talk：" + talk);
        return userService.queryById(id);
    }

    @GetMapping("now")
    public String now() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateformat));
    }

    @GetMapping("prop")
    public ConfigProperty prop() {
        return prop;
    }

}
