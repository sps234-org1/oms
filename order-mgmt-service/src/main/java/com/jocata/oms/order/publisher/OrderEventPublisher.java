package com.jocata.oms.order.publisher;

import com.jocata.oms.bean.order.OrderBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderEvent(OrderBean orderBean) {
        kafkaTemplate.send("order-topic", orderBean);
    }

}
