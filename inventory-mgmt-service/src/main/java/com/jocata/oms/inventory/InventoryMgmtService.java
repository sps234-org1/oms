package com.jocata.oms.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan( basePackages = "com.jocata.oms.entity.inventory" )
@EnableJpaRepositories( basePackages = "com.jocata.oms.dao.inventory" )
public class InventoryMgmtService {
    public static void main(String[] args) {
        SpringApplication.run(InventoryMgmtService.class, args);
        System.out.println("Hello, World!");
    }
}