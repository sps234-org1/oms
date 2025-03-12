package com.jocata.oms.inventory.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.dao.inventory.InventoryDao;
import com.jocata.oms.entity.inventory.InventoryDetails;
import com.jocata.oms.inventory.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public InventoryBean saveInventory(InventoryBean inventoryBean) {

        inventoryBean.setReservedStock(0);
        inventoryBean.setLastUpdated(Timestamp.valueOf(LocalDate.now().toString()));
        InventoryDetails inventoryDetails = convertToEntity(inventoryBean);
        return convertToBean(inventoryDao.save(inventoryDetails));
    }

    @Override
    public List<InventoryBean> getAll() {

        List<InventoryDetails> inventoryDetailsDB = getAllInventory();
        List<InventoryBean> inventoryBeans = new ArrayList<>();
        for (InventoryDetails inventoryDetails : inventoryDetailsDB) {
            InventoryBean inventoryBean = convertToBean(inventoryDetails);
            inventoryBeans.add(inventoryBean);
        }
        return inventoryBeans;
    }

    private List<InventoryDetails> getAllInventory() {
        return inventoryDao.findAll();
    }

    @Override
    public List<InventoryBean> reserveInventory(List<InventoryBean> inventoryRequests) {

        List<InventoryDetails> inventoryListDB = getAllInventory();

        for (InventoryBean inventoryRequest : inventoryRequests) {

            Integer productIdReq = inventoryRequest.getProductId();

            for (InventoryDetails inventoryDB : inventoryListDB) {

                Integer quantityDB = inventoryDB.getStockQuantity();
                Integer stockDB = inventoryDB.getReservedStock();

                if (inventoryDB.getProductId().equals(productIdReq)) {

                    Integer stockReq = inventoryRequest.getReservedStock();
                    if (stockDB == 0 && quantityDB >= stockReq) {
                        inventoryDB.setReservedStock(stockReq);
                        inventoryDB.setStockQuantity(quantityDB - stockReq);
                        inventoryDB.setLastUpdated(Timestamp.valueOf(LocalDate.now().toString()));
                    } else {
                        logger.info("Stock not available for product id: {}", inventoryRequest.getProductId());
                    }
                }

            }
        }
        List<InventoryDetails> inventoryDetailsList = inventoryDao.saveAll(inventoryListDB);
        List<InventoryBean> resList = new ArrayList<>();
        for (InventoryDetails inventoryDetails : inventoryDetailsList) {
            resList.add(convertToBean(inventoryDetails));
        }
        return resList;
    }


    @Override
    public List<InventoryBean> releaseInventory(List<InventoryBean> inventoryRequests) {

        List<InventoryDetails> inventoryListDB = getAllInventory();

        for (InventoryBean inventoryRequest : inventoryRequests) {

            Integer productIdReq = inventoryRequest.getProductId();

            for (InventoryDetails inventoryDB : inventoryListDB) {

                Integer quantityDB = inventoryDB.getStockQuantity();
                Integer stockDB = inventoryDB.getReservedStock();

                if (inventoryDB.getProductId().equals(productIdReq)) {

                    Integer stockReq = inventoryRequest.getReservedStock();
                    if (stockDB >= stockReq) {
                        inventoryDB.setReservedStock(stockDB - stockReq);
                        inventoryDB.setStockQuantity(quantityDB + stockReq);
                        inventoryDB.setLastUpdated(Timestamp.valueOf(LocalDate.now().toString()));
                    } else {
                        logger.info("Stock not available for product id: {}", inventoryRequest.getProductId());
                    }
                }
            }
        }
        List<InventoryDetails> inventoryDetailsList = inventoryDao.saveAll(inventoryListDB);
        List<InventoryBean> resList = new ArrayList<>();
        for (InventoryDetails inventoryDetails : inventoryDetailsList) {
            resList.add(convertToBean(inventoryDetails));
        }
        return resList;
    }

    @Override
    public List<InventoryBean> updateInventory(List<InventoryBean> inventoryRequests) {
        return List.of();
    }

    private InventoryDetails convertToEntity(InventoryBean inventoryBean) {

        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setInventoryId(inventoryBean.getInventoryId());
        inventoryDetails.setProductId(inventoryBean.getProductId());
        inventoryDetails.setStockQuantity(inventoryBean.getStockQuantity());
        inventoryDetails.setReservedStock(inventoryBean.getReservedStock());
        inventoryDetails.setLastUpdated(inventoryBean.getLastUpdated());
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
