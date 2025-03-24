package com.jocata.apis.product;

import com.jocata.oms.bean.ProductBean;
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
public class ProductApiClient {

    private final String PRODUCT_PATH = "/product-mgmt-service/api/v1/product/";

    private static final Logger logger = LoggerFactory.getLogger(ProductApiClient.class);

    @Autowired
    private final WebClient webClient;

    private final ReactiveCircuitBreaker reactiveCircuitBreakerForProduct;

    public ProductApiClient(WebClient.Builder webClientBuilder,
                            ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.webClient = webClientBuilder.build();
        this.reactiveCircuitBreakerForProduct = circuitBreakerFactory.create("productService");
    }

    public Mono<List<ProductBean>> getProducts() {

        logger.info("Fetching product list");

        return reactiveCircuitBreakerForProduct.run(
                webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .scheme("http")
                                .host("localhost")
                                .port(8082)
                                .path(PRODUCT_PATH + "/get")
                                .build())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response -> {
                            logger.error("HTTP error in Product Service: {}", response.statusCode());
                            return Mono.error(new RuntimeException("Product Service Error: " + response.statusCode()));
                        })
                        .bodyToFlux(ProductBean.class)
                        .collectList()
                        .doOnSuccess(productBeans -> logger.info("Product list fetched successfully : {}", productBeans.size()))
                        .doOnError(error -> logger.error("WebClient error in Product Service: {}", error.getMessage())),
                this::fallbackGetProducts
        );
    }

    public Mono<List<ProductBean>> fallbackGetProducts(Throwable throwable) {
        logger.error("Fallback triggered for Product Service: {}", throwable.getMessage());
        ProductBean errorProduct = new ProductBean();
        errorProduct.setProductId(-1);
        errorProduct.setDescription("The Product Service is currently down. Please try again later.");
        return Mono.just(List.of(errorProduct));
    }

    public Mono<List<ProductBean>> getProductsByIds(List<ProductBean> productBeans) {

        logger.info("Fetching product list by IDs");
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8082)
                        .path(PRODUCT_PATH + "/getByIds")
                        .build()
                )
                .bodyValue(productBeans)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    logger.error("HTTP error in Product Service: {}", response.statusCode());
                    return Mono.error(new RuntimeException("Product Service Error: " + response.statusCode()));
                })
                .bodyToFlux(ProductBean.class)
                .collectList()
                .doOnError(error -> logger.error("WebClient error in Product Service: {}", error.getMessage()));
    }

}
