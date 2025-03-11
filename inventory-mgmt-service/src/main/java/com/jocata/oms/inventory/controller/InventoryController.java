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
    InventoryBean saveInventory(@RequestBody InventoryBean inventoryBean){
        return inventoryService.saveInventory(inventoryBean);
    }

    @GetMapping("/get")
    List<InventoryBean> getAll( ){
        return inventoryService.getAll();
    }

    @PatchMapping( "/reserve/{productId}/{quantity}")
    List<InventoryBean> reserveInventory(@PathVariable Integer productId,@PathVariable Integer quantity ) {
        return inventoryService.reserveInventory( productId, quantity );
    }

    @PatchMapping( "/release/{productId}/{quantity}")
    List<InventoryBean> releaseInventory(@PathVariable Integer productId,@PathVariable Integer quantity) {
        return inventoryService.releaseInventory( productId, quantity );
    }

    @PatchMapping( "/update/{productId}/{quantity}")
    List<InventoryBean> updateInventory(@PathVariable Integer productId,@PathVariable Integer quantity ){
        return inventoryService.updateInventory( productId, quantity );
    }




}
