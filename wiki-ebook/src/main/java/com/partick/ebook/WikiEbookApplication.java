package com.partick.ebook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author partick_peng
 */
@MapperScan("com.partick.database.mapper")
@ComponentScan(basePackages = {"com.partick"})
@SpringBootApplication
@EnableFeignClients
public class WikiEbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiEbookApplication.class, args);
    }

}
