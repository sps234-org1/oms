package com.jocata.oms.order;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan(basePackages = "com.jocata.oms.entity.order")
@EnableJpaRepositories(basePackages = "com.jocata.oms.dao.order")
@ComponentScan(basePackages = {"com.jocata.oms.order", "com.jocata.apis"})
public class OrderMgmtService {
    public static void main(String[] args) {
        SpringApplication.run(OrderMgmtService.class, args);
        System.out.println("Hello, World!");
    }
}