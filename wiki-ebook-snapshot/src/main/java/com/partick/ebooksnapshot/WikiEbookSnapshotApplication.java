package com.partick.ebooksnapshot;

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
public class WikiEbookSnapshotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikiEbookSnapshotApplication.class, args);
    }

}
