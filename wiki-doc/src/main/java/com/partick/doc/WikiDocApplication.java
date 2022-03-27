package com.partick.doc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author partick_peng
 */
@MapperScan("com.partick.database.mapper")
@ComponentScan(basePackages = {"com.partick"})
@SpringBootApplication
public class WikiDocApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiDocApplication.class, args);
    }

}
