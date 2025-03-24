package com.jocata.apis.inventory;

import com.jocata.oms.bean.InventoryBean;
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
public class InventoryApiClient {

    private final String INVENTORY_PATH = "/inventory-mgmt-service/api/v1/inventory";

    private static final Logger logger = LoggerFactory.getLogger(InventoryApiClient.class);

    @Autowired
    private final WebClient webClient;

    private final ReactiveCircuitBreaker reactiveCircuitBreakerForInventory;

    public InventoryApiClient(WebClient.Builder webClientBuilder,
                              ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = webClientBuilder.build();
        this.reactiveCircuitBreakerForInventory = circuitBreakerFactory.create("inventoryService");
    }

    public Mono<List<InventoryBean>> getInventory() {
        logger.info("Fetching inventory list");

        return reactiveCircuitBreakerForInventory.run(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8083)
                                .path(INVENTORY_PATH + "/get")
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response -> {
                            logger.error("HTTP error in Inventory Service: {}", response.statusCode());
                            return Mono.error(new RuntimeException("Inventory Service Error: " + response.statusCode()));
                        })
                        .bodyToFlux(InventoryBean.class)
                        .collectList()
                        .doOnSuccess(inventoryBeans -> logger.info("Inventory list fetched successfully : {}", inventoryBeans.size()))
                        .doOnError(error -> logger.error("WebClient error in Inventory Service: {}", error.getMessage())),
                this::fallbackGetInventory
        );
    }

    public Mono<List<InventoryBean>> fallbackGetInventory(Throwable throwable) {
        logger.error("Fallback triggered for Inventory Service: {}", throwable.getMessage());
        InventoryBean errorInventory = new InventoryBean();
        errorInventory.setInventoryId(-1);
        return Mono.just(List.of(errorInventory));
    }

    public Mono<List<InventoryBean>> reserveInventory(List<InventoryBean> inventoryRequest) {

        return reactiveCircuitBreakerForInventory.run(
                webClient.patch()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8083)
                                .path(INVENTORY_PATH + "/reserve")
                                .build())
                        .bodyValue(inventoryRequest)
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response -> {
                            logger.error("HTTP error in Inventory Service: {}", response.statusCode());
                            return Mono.error(new RuntimeException("Inventory Service Error: " + response.statusCode()));
                        })
                        .bodyToFlux(InventoryBean.class)
                        .collectList()
                        .doOnError(error -> logger.error("WebClient error in Inventory Service: {}", error.getMessage())),
                this::fallbackReserveInventory
        );
    }

    public Mono<List<InventoryBean>> fallbackReserveInventory(Throwable throwable) {
        logger.error("Fallback triggered for Inventory Service: {}", throwable.getMessage());
        InventoryBean errorInventory = new InventoryBean();
        errorInventory.setInventoryId(-1);
        return Mono.just(List.of(errorInventory));
    }

    public Mono<InventoryBean> saveInventoryInfo(InventoryBean inventoryBean) {

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8083)
                        .path(INVENTORY_PATH + "/save")
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
