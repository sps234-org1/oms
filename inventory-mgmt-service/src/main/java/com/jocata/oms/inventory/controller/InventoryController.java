package com.jocata.oms.inventory.controller;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/save")
    InventoryBean saveInventory(@RequestBody InventoryBean inventoryBean) {
        return inventoryService.saveInventory(inventoryBean);
    }

    @GetMapping("/get")
    List<InventoryBean> getAll() {
        return inventoryService.getAll();
    }

    @PatchMapping("/reserve")
    List<InventoryBean> reserveInventory(@RequestBody List<InventoryBean> inventoryRequest) {
        return inventoryService.reserveInventory(inventoryRequest);
    }

    @PatchMapping("/release")
    List<InventoryBean> releaseInventory(@RequestBody List<InventoryBean> inventoryRequest) {
        return inventoryService.releaseInventory(inventoryRequest);
    }

    @PatchMapping("/update")
    List<InventoryBean> updateInventory(@RequestBody List<InventoryBean> inventoryRequests) {
        return inventoryService.updateInventory(inventoryRequests);
    }

    @PostMapping("/getByProductIds")
    List<InventoryBean> getInventoryByProductIds(@RequestBody List<InventoryBean> inventoryRequests) {
        return inventoryService.getInventoryByProductIds(inventoryRequests);
    }

}
