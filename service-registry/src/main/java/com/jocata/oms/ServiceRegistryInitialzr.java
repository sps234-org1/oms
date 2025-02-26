package com.jocata.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryInitialzr {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryInitialzr.class, args);
        System.out.println("Hello, World!");

    }
}