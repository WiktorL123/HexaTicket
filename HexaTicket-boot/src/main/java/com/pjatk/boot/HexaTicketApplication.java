package com.pjatk.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.pjatk")
@EnableMongoRepositories(basePackages = "com.pjatk")
public class HexaTicketApplication {
    public static void main(String[] args){
        SpringApplication.run(HexaTicketApplication.class, args);
    }
}
