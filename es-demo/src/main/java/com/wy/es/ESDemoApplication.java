package com.wy.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wy.es.mapper")
@SpringBootApplication
public class ESDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESDemoApplication.class, args);
    }

}
