package com.jocata.apis.payment;

import com.jocata.oms.bean.PaymentBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentApiClient {

    private final String PAYMENT_PATH = "/payment-mgmt-service/api/v1/payment/";

    private static final Logger logger = LoggerFactory.getLogger(PaymentApiClient.class);

    @Autowired
    private final WebClient webClient;

    public PaymentApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<PaymentBean> createPayment(PaymentBean paymentBean) {

        logger.info("Creating payment");

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8085)
                        .path(PAYMENT_PATH + "/create")
                        .build())
                .body(Mono.just(paymentBean), PaymentBean.class)
                .retrieve()
                .bodyToMono(PaymentBean.class)
                .doOnSuccess(paymentBeanResponse -> logger.info("Payment created successfully : {}", paymentBeanResponse.getTransactionId()))
                .doOnError(error -> logger.error("WebClient error in Payment Service: {}", error.getMessage()));
    }


}
