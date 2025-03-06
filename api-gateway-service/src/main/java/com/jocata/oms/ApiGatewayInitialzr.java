package com.jocata.oms;

import com.jocata.oms.response.UserBean;
import com.jocata.oms.service.CustomUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayInitialzr {
    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(ApiGatewayInitialzr.class, args);
//        CustomUserDetailsService customUserDetailsService = context.getBean(CustomUserDetailsService.class);
//
//        String url = "http://localhost:8081/user-mgmt-service/api/v1/user/getByEmail?email=johndoe@example.com";
//        UserBean userBean = customUserDetailsService.getUserByEmail(url).block();
//        System.out.println(userBean.getEmail());

        System.out.println("Hello, World!");
    }
}