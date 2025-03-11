package com.jocata.oms.inventory.service;


import com.jocata.oms.bean.InventoryBean;

import java.util.List;

public interface InventoryService {

    List<InventoryBean> getAll( );

    InventoryBean saveInventory(InventoryBean inventoryBean);

    List<InventoryBean> reserveInventory(Integer productId, Integer quantity );

    List<InventoryBean> releaseInventory(Integer productId, Integer quantity );

    List<InventoryBean> updateInventory(Integer productId, Integer quantity );

}
