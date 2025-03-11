package com.jocata.oms.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan( basePackages = "com.jocata.oms.entity.payment" )
@EnableJpaRepositories( basePackages = "com.jocata.oms.dao.payment" )
public class PaymentMgmtService {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMgmtService.class, args);
        System.out.println("Hello, World!");
    }
}