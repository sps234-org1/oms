package com.jocata.oms.inventory.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.dao.inventory.InventoryDao;
import com.jocata.oms.entity.inventory.InventoryDetails;
import com.jocata.oms.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryDao inventoryDao;

    @Override
    public List<InventoryBean> getAll() {

        List<InventoryDetails> inventoryDetailsDB = inventoryDao.findAll();
        List<InventoryBean> inventoryBeans = new ArrayList<>();
        for (InventoryDetails inventoryDetails : inventoryDetailsDB) {
            InventoryBean inventoryBean = convertToBean(inventoryDetails);
            inventoryBeans.add(inventoryBean);
        }
        return inventoryBeans;
    }

    @Override
    public InventoryBean saveInventory(InventoryBean inventoryBean) {

        InventoryDetails inventoryDetails = convertToEntity(inventoryBean);
        inventoryDetails.setReservedStock(0);
        inventoryDetails.setLastUpdated(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return convertToBean(inventoryDao.save(inventoryDetails));
    }

    @Override
    public List<InventoryBean> reserveInventory(Integer productId, Integer quantity) {
        return List.of();
    }

    @Override
    public List<InventoryBean> releaseInventory(Integer productId, Integer quantity) {
        return List.of();
    }

    @Override
    public List<InventoryBean> updateInventory(Integer productId, Integer quantity) {
        return List.of();
    }

    private InventoryDetails convertToEntity(InventoryBean inventoryBean) {

        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setProductId(inventoryBean.getProductId());
        inventoryDetails.setStockQuantity(inventoryBean.getStockQuantity());
        inventoryDetails.setReservedStock(inventoryBean.getReservedStock());
        return inventoryDetails;
    }

    private InventoryBean convertToBean(InventoryDetails inventoryDetails) {

        InventoryBean inventoryBean = new InventoryBean();
        inventoryBean.setInventoryId(inventoryDetails.getInventoryId());
        inventoryBean.setProductId(inventoryDetails.getProductId());
        inventoryBean.setStockQuantity(inventoryDetails.getStockQuantity());
        inventoryBean.setReservedStock(inventoryDetails.getReservedStock());
        inventoryBean.setLastUpdated(inventoryDetails.getLastUpdated());
        return inventoryBean;
    }


}
