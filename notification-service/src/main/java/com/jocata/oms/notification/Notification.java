package com.jocata.oms.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Notification {
    public static void main(String[] args) {
        SpringApplication.run(Notification.class, args);
        System.out.println("Hello, World!");
    }
}