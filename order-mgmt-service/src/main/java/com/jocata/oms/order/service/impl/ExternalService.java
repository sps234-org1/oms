package com.jocata.oms.order.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.bean.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ExternalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
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

    Mono<List<ProductBean>> getProducts() {
        return webClient.get()
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
                .collectList()
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
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
