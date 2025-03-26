package com.jocata.oms.order.publisher;

import com.jocata.oms.bean.InventoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryEventPublisher {

    private final KafkaTemplate<String, List<InventoryBean>> kafkaTemplate;

    @Autowired
    public InventoryEventPublisher(KafkaTemplate<String, List<InventoryBean>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReserveInventoryEvent(List<InventoryBean> inventoryBeanList) {
        kafkaTemplate.send("reserve-inventory", inventoryBeanList);
    }

    public void publishReleaseInventoryEvent(List<InventoryBean> inventoryBeanList) {
        kafkaTemplate.send("release-inventory", inventoryBeanList);
    }

    public void publishUpdateInventoryEvent(List<InventoryBean> inventoryBeanList) {
        kafkaTemplate.send("update-inventory", inventoryBeanList);
    }
}
