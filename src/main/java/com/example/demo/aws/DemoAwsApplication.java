package com.example.demo.aws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DemoAwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoAwsApplication.class, args);
    }

}
