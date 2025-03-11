package com.jocata.oms.product.service.impl;

import com.jocata.oms.bean.InventoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalService {

    @Autowired
    private WebClient webClient;

    @Autowired
    ExternalService ( WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder.build();
    }

    private final String URL = "http://localhost:8083/inventory-mgmt-service/api/v1/inventory/save";

    public Mono<InventoryBean> saveInventoryInfo(InventoryBean inventoryBean) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8083)
                        .path("/inventory-mgmt-service/api/v1/inventory/save")
                        .build()
                )
                .bodyValue(inventoryBean)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError, response ->
                                Mono.error(new RuntimeException("Error ..." + response.statusCode()))
                )
                .bodyToMono(InventoryBean.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Error")));
    }


}
