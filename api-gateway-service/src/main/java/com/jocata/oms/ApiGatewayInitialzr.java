package com.jocata.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayInitialzr {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayInitialzr.class, args);
        System.out.println("Hello, World!");
    }
}