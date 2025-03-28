package com.jocata.oms.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.jocata.oms")
@EnableJpaRepositories(basePackages = "com.jocata.oms.dao")
@EntityScan(basePackages = "com.jocata.oms.entity")
@EnableDiscoveryClient
public class UserMgmtService {
    public static void main(String[] args) {

        SpringApplication.run(UserMgmtService.class, args);

        System.out.println("Hello, World!");
    }
}