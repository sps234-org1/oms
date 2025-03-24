package com.jocata.oms.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan(basePackages = "com.jocata.oms.entity.product")
@EnableJpaRepositories(basePackages = "com.jocata.oms.dao.product")
@ComponentScan(basePackages = {"com.jocata.oms.product", "com.jocata.apis"})
public class ProductMgmtService {
    public static void main(String[] args) {
        SpringApplication.run(ProductMgmtService.class, args);
        System.out.println("Hi, from product service");
    }
}