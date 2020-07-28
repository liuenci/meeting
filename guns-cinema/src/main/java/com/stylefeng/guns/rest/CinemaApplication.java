package com.stylefeng.guns.rest;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@EnableDubboConfiguration
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
        log.info("=========== Cinema 完美启动 ==============");
    }
}
