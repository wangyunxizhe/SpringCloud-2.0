package com.wy.user.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 负责读取user-service.yaml这个nacos中共享配置项
 */
@Component
@Data
@ConfigurationProperties(prefix = "pattern")
public class ConfigProperty {

    private String envSharedValue;

}
