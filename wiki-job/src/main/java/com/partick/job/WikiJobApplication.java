package com.partick.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author partick_peng
 */
@MapperScan("com.partick.database.mapper")
@ComponentScan(basePackages = {"com.partick"})
@EnableFeignClients
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class WikiJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiJobApplication.class, args);
    }

}
