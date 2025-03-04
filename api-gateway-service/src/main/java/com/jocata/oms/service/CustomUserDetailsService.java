package com.jocata.oms.service;

import com.jocata.oms.response.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CustomUserDetailsService {

    @Autowired
    private WebClient webClient;


    UserBean getUserByEmail(String url ) {

        UserBean userBeanResponse = webClient.get().uri( url ).retrieve().bodyToMono(UserBean.class).block();

        return userBeanResponse;
    }

}



