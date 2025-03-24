package com.jocata.apis.order;

import com.jocata.oms.bean.order.OrderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderApiClient {

    public final String ORDER_PATH = "/order-mgmt-service/api/v1/order/";

    private static final Logger logger = LoggerFactory.getLogger(OrderApiClient.class);

    private final WebClient webClient;

    private final ReactiveCircuitBreaker reactiveCircuitBreakerForOrder;

    public OrderApiClient(WebClient.Builder webClientBuilder,
                          ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = webClientBuilder.build();
        this.reactiveCircuitBreakerForOrder = circuitBreakerFactory.create("orderService");
    }

    public Mono<OrderBean> getOrderById(int orderId) {
        logger.info("Fetching order by ID: {}", orderId);

        return reactiveCircuitBreakerForOrder.run(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8083)
                                .path(ORDER_PATH + "/get/")
                                .queryParam("orderId", orderId)
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response -> {
                            logger.error("HTTP error in Order Service: {}", response.statusCode());
                            return Mono.error(new RuntimeException("Order Service Error: " + response.statusCode()));
                        })
                        .bodyToMono(OrderBean.class)
                        .doOnSuccess(order -> logger.info("Order fetched: {}", order.getOrderId()))
                        .doOnError(error -> logger.error("WebClient error in Order Service: {}", error.getMessage())),
                this::fallbackGetOrder
        );
    }

    public Mono<OrderBean> fallbackGetOrder(Throwable throwable) {
        logger.error("Fallback triggered for Order Service: {}", throwable.getMessage());
        OrderBean errorOrder = new OrderBean();
        errorOrder.setOrderId(-1);
        return Mono.just(errorOrder);
    }

    public Mono<OrderBean> processOrder(Integer orderId) {

        logger.info("Processing order for order id : {}", orderId);
        return reactiveCircuitBreakerForOrder.run(
                webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8083)
                                .path(ORDER_PATH + "/process/" + orderId)
                                .build()
                        )
                        .retrieve()
                        .bodyToMono(OrderBean.class)
                        .doOnSuccess(order -> logger.info("Order processed: {}", order.getOrderId()))
                        .doOnError(error -> logger.error("Error processing order: {}", error.getMessage())),
                this::fallbackProcessOrder
        );
    }

    Mono<OrderBean> fallbackProcessOrder(Throwable throwable) {
        logger.error("Fallback triggered for Order Service: {}", throwable.getMessage());
        OrderBean errorOrder = new OrderBean();
        errorOrder.setOrderId(-1);
        return Mono.just(errorOrder);
    }


}
