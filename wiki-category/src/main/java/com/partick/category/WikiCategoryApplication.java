package com.partick.category;

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
public class WikiCategoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiCategoryApplication.class, args);
    }

}
