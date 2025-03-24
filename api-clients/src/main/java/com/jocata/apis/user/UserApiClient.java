package com.jocata.apis.user;

import com.jocata.oms.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class UserApiClient {

    private final String USER_PATH = "/user-mgmt-service/api/v1/user/";

    @Autowired
    private final WebClient webClient;

    private final ReactiveCircuitBreaker reactiveCircuitBreakerForUser;

    private static final Logger logger = LoggerFactory.getLogger(UserApiClient.class);


    public UserApiClient(WebClient.Builder webClientBuilder,
                         ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = webClientBuilder.build();
        this.reactiveCircuitBreakerForUser = circuitBreakerFactory.create("userService");
    }


    public Mono<UserBean> getUser(Integer userId) {

        logger.info("Fetching user by ID: {}", userId);
        return reactiveCircuitBreakerForUser.run(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8081)
                                .path(USER_PATH + "/get")
                                .queryParam("userId", userId)
                                .build()
                        )
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                        )
                        .bodyToMono(UserBean.class)
                        .doOnSuccess(user -> logger.info("User fetched: {}", user.getUserId()))
                        .doOnError(error -> logger.error("WebClient error in User Service: {}", error.getMessage()))
                        .switchIfEmpty(Mono.error(new RuntimeException("Error"))),
                this::fallbackGetUser
        );
    }

    public Mono<UserBean> fallbackGetUser(Throwable throwable) {
        logger.error("Fallback triggered: {}", throwable.getMessage());
        throwable.printStackTrace();
        UserBean errorUser = new UserBean();
        errorUser.setUserId(-1);
        errorUser.setFullName("The User Service is currently down. Please try again later.");
        return Mono.just(errorUser);
    }


}
