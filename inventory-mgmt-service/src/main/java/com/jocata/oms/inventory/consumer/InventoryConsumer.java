package com.jocata.oms.inventory.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryConsumer {

    @Autowired
    private InventoryService inventoryService;

    private List<InventoryBean> convertToListOfBean(List<?> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        return data.stream()
                .map(obj -> objectMapper.convertValue(obj, InventoryBean.class))
                .toList();

    }

    @KafkaListener(topics = "reserve-inventory", groupId = "inventory-group")
    private void reserve(List<?> data) {
        inventoryService.reserveInventory(convertToListOfBean(data));
    }

    @KafkaListener(topics = "release-inventory", groupId = "inventory-group")
    private void release(List<?> data) {
        inventoryService.releaseInventory(convertToListOfBean(data));
    }

    @KafkaListener(topics = "update-inventory", groupId = "inventory-group")
    private void update(List<?> data) {
        inventoryService.updateInventory(convertToListOfBean(data));
    }

}
