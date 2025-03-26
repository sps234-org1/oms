package com.jocata.oms.inventory.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.dao.inventory.InventoryDao;
import com.jocata.oms.entity.inventory.InventoryDetails;
import com.jocata.oms.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryDao inventoryDao;

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Override
    public InventoryBean saveInventory(InventoryBean inventoryBean) {

        InventoryDetails inventoryDetails = convertToEntity(inventoryBean);
        inventoryDetails.setReservedStock(0);
        inventoryDetails.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
        return convertToBean(inventoryDao.save(inventoryDetails));
    }

    @Override
    public List<InventoryBean> getAll() {
        return convertEntityListtoBeanList(inventoryDao.findAll());
    }

    private List<InventoryDetails> findAllByProductId(List<InventoryBean> inventoryRequests) {
        List<Integer> productIds = new ArrayList<>();
        for (InventoryBean inventoryRequest : inventoryRequests) {
            productIds.add(inventoryRequest.getProductId());
        }
        return inventoryDao.findAllByProductId(productIds);
    }

    public List<InventoryBean> getInventoryByProductIds(List<InventoryBean> inventoryRequests) {

        List<Integer> productIds = new ArrayList<>();
        for (InventoryBean inventoryRequest : inventoryRequests) {
            productIds.add(inventoryRequest.getProductId());
        }
        List<InventoryDetails> inventoryDetailsListDB = inventoryDao.findAllByProductId(productIds);
        return convertEntityListtoBeanList(inventoryDetailsListDB);
    }

    private List<InventoryBean> saveAll(List<InventoryDetails> inventoryDetailsList) {
        List<InventoryDetails> inventoryDetailsListDB = inventoryDao.saveAll(inventoryDetailsList);
        return convertEntityListtoBeanList(inventoryDetailsListDB);
    }

    @Override
    public List<InventoryBean> updateInventory(List<InventoryBean> inventoryRequests) {
        return saveAll(convertBeanListtoEntityList(inventoryRequests));
    }

    @Override
    public List<InventoryBean> reserveInventory(List<InventoryBean> inventoryRequests) {

        List<InventoryDetails> inventoryListDB = findAllByProductId(inventoryRequests);

        for (InventoryBean inventoryRequest : inventoryRequests) {
            Integer productIdReq = inventoryRequest.getProductId();
            for (InventoryDetails inventoryDB : inventoryListDB) {
                if (inventoryDB.getProductId().equals(productIdReq)) {
                    Integer quantityDB = inventoryDB.getStockQuantity();
                    Integer stockDB = inventoryDB.getReservedStock();
                    Integer stockReq = inventoryRequest.getReservedStock();
                    if (stockDB == 0 && quantityDB >= stockReq) {
                        inventoryDB.setStockQuantity(quantityDB - stockReq);
                        inventoryDB.setReservedStock(stockReq);
                        inventoryDB.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
                    } else {
                        logger.info("Stock not available for product id: {}", inventoryRequest.getProductId());
                    }
                }

            }
        }
        return saveAll(inventoryListDB);
    }

    @Override
    public List<InventoryBean> releaseInventory(List<InventoryBean> inventoryRequests) {

        List<InventoryDetails> inventoryListDB = findAllByProductId(inventoryRequests);
        List<InventoryDetails> resList = getDeepCopy(inventoryListDB);
        for (InventoryBean inventoryRequest : inventoryRequests) {
            Integer productIdReq = inventoryRequest.getProductId();
            for (InventoryDetails inventoryDB : inventoryListDB) {
                Integer quantityDB = inventoryDB.getStockQuantity();
                Integer reservedStock = inventoryDB.getReservedStock();
                if (inventoryDB.getProductId().equals(productIdReq)) {
                    if (reservedStock <= quantityDB) {
                        inventoryDB.setReservedStock(0);
                        inventoryDB.setStockQuantity(quantityDB + reservedStock);
                        inventoryDB.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
                    } else {
                        logger.info("Reserve stock more than quantity: {}", quantityDB - reservedStock);
                    }
                }
            }
        }
        saveAll(inventoryListDB);
        return convertEntityListtoBeanList(resList);
    }

    private List<InventoryDetails> getDeepCopy(List<InventoryDetails> inventoryListDB) {

        List<InventoryDetails> resList = inventoryListDB.stream().map(details -> {
            InventoryDetails clonedDetails = new InventoryDetails();
            clonedDetails.setInventoryId(details.getInventoryId());
            clonedDetails.setProductId(details.getProductId());
            clonedDetails.setStockQuantity(details.getStockQuantity());
            clonedDetails.setReservedStock(details.getReservedStock());
            clonedDetails.setLastUpdated(details.getLastUpdated());
            return clonedDetails;
        }).toList();
        return resList;
    }

    private List<InventoryDetails> convertBeanListtoEntityList(List<InventoryBean> inventoryRequests) {
        List<InventoryDetails> resList = new ArrayList<>();
        for (InventoryBean inventoryBean : inventoryRequests) {
            resList.add(convertToEntity(inventoryBean));
        }
        return resList;
    }

    private List<InventoryBean> convertEntityListtoBeanList(List<InventoryDetails> inventoryDetailsListDB) {
        List<InventoryBean> resList = new ArrayList<>();
        for (InventoryDetails inventoryDetails : inventoryDetailsListDB) {
            resList.add(convertToBean(inventoryDetails));
        }
        return resList;
    }

    private InventoryDetails convertToEntity(InventoryBean inventoryBean) {

        InventoryDetails inventoryDetails = new InventoryDetails();
        inventoryDetails.setInventoryId(inventoryBean.getInventoryId());
        inventoryDetails.setProductId(inventoryBean.getProductId());
        inventoryDetails.setStockQuantity(inventoryBean.getStockQuantity());
        inventoryDetails.setReservedStock(inventoryBean.getReservedStock());
        inventoryDetails.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
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
