package com.lizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author  荔枝
 @EnableScheduling 开启任务调度
 */
@SpringBootApplication
@MapperScan("com.lizhi.mapper")
@EnableScheduling
public class FindFriendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindFriendApplication.class, args);
    }

}
