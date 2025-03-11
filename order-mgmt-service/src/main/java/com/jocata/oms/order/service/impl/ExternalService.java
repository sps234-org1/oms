package com.jocata.oms.order.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.bean.ProductBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ExternalService {

    private final String INVENTORY_GET_URL = "http://localhost:8083/inventory-mgmt-service/api/v1/inventory/get";
    private final String PRODUCT_GET_URL = "http://localhost:8082/product-mgmt-service/api/v1/product/get";

    @Autowired
    private WebClient webClient;

    @Autowired
    ExternalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    Mono<List<InventoryBean>> getInventory() {
        return webClient.get()
                .uri(INVENTORY_GET_URL)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                )
                .bodyToFlux(InventoryBean.class)
                .collectList()
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
    }

    Mono<List<ProductBean>> getProducts() {
        return webClient.get()
                .uri(PRODUCT_GET_URL)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                )
                .bodyToFlux(ProductBean.class)
                .collectList()
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
    }

}
