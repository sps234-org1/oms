package com.jocata.oms.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan( basePackages = "com.jocata.oms.entity.product" )
@EnableJpaRepositories( basePackages = "com.jocata.oms.dao.product" )
public class ProductMgmtService {
    public static void main(String[] args) {
        SpringApplication.run(ProductMgmtService.class, args);
        System.out.println("Hello, World!");
    }
}