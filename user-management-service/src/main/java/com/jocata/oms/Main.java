package com.jocata.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.jocata.oms")
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

        System.out.println("Hello, World!");
    }
}