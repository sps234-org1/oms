package com.jocata.oms.order.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.bean.UserBean;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ExternalService {

    private final String USER_GET_PATH = "/user-mgmt-service/api/v1/user/get";
    private final String PRODUCT_GET_PATH = "/product-mgmt-service/api/v1/product/get";
    private final String INVENTORY_GET_PATH = "/inventory-mgmt-service/api/v1/inventory/get";

    @Autowired
    private WebClient webClient;

    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);

    @Autowired
    ExternalService(WebClient.Builder webClientBuilder, ReactiveCircuitBreakerFactory circuitBreakerFactory) {
        this.webClient = webClientBuilder.build();
        this.reactiveCircuitBreaker = circuitBreakerFactory.create("productService");
    }

    Mono<UserBean> getUser(Integer userId) {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8081)
                                .path(USER_GET_PATH)
                                .queryParam("userId", userId)
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                )
                .bodyToMono(UserBean.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
    }

//    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackGetProducts")
    Mono<List<ProductBean>> getProducts() {
        return reactiveCircuitBreaker.run(
                webClient.get()
                        .uri(
                                uriBuilder -> uriBuilder
                                        .scheme("http")
                                        .host("localhost")
                                        .port(8082)
                                        .path(PRODUCT_GET_PATH)
                                        .build()

                        )
                        .retrieve()
                        .onStatus(
                                HttpStatusCode::isError, response ->
                                        Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                        )
                        .bodyToFlux(ProductBean.class)
                        .collectList(),
                this::fallbackGetProducts
        );
    }

    public Mono<List<ProductBean>> fallbackGetProducts(Throwable throwable) {
        logger.error("Error fetching products: {}", throwable.getMessage());
        ProductBean errorProduct = new ProductBean();
        errorProduct.setProductId(-1);
        errorProduct.setDescription("The Product Service is currently down. Please try again later.");

        return Mono.just(List.of(errorProduct));
    }

    Mono<List<InventoryBean>> getInventory() {
        return webClient.get()
                .uri(
                        uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8083)
                                .path(INVENTORY_GET_PATH)
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                )
                .bodyToFlux(InventoryBean.class)
                .collectList()
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
    }

}





