package com.wfc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by wangfengchen on 2017/3/25.
 */
@SpringBootApplication
public class TestApplication {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(TestApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
