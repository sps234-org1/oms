package com.jocata.oms.inventory.service;


import com.jocata.oms.bean.InventoryBean;

import java.util.List;

public interface InventoryService {

    InventoryBean saveInventory(InventoryBean inventoryBean);

    List<InventoryBean> getAll();

    List<InventoryBean> getInventoryByProductIds(List<InventoryBean> inventoryRequests);

    List<InventoryBean> updateInventory(List<InventoryBean> inventoryRequests);

    List<InventoryBean> reserveInventory(List<InventoryBean> inventoryRequests);

    List<InventoryBean> releaseInventory(List<InventoryBean> inventoryRequests);

}
