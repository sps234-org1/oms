package com.jocata.oms.product.service.impl;

import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.dao.product.ProductDao;
import com.jocata.oms.entity.product.ProductDetails;
import com.jocata.apis.inventory.InventoryApiClient;
import com.jocata.oms.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    InventoryApiClient inventoryApiClient;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductBean saveProduct(ProductBean productBean) {

        ProductDetails productDetails = convertToEntity(productBean);
        productDetails.setCreatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay()));

        ProductDetails savedProduct = productDao.save(productDetails);
        InventoryBean inventoryReq = productBean.getInventoryInfo();
        inventoryReq.setProductId(savedProduct.getProductId());

        ProductBean res = convertToBean(savedProduct);
        InventoryBean resInventory = inventoryApiClient.saveInventoryInfo(inventoryReq).block();
        logger.info("Inventory saved {} {} :", resInventory.getInventoryId(), resInventory.getStockQuantity());
        res.setInventoryInfo(resInventory);
        return res;
    }

    @Override
    @Cacheable(value = "products", key = "#productId")
    public ProductBean getProductById(Integer productId) {

        ProductDetails productDetailsDB = productDao.findById(productId).orElse(null);
        if (productDetailsDB == null) {
            logger.info("Product not found for id: " + productId);
            return null;
        }
        return convertToBean(productDetailsDB);
    }

    @Override
    public List<ProductBean> getAllProducts() {

        List<ProductDetails> productDetailsList = productDao.findAll();
        List<ProductBean> productBeanList = new ArrayList<>();
        for (ProductDetails productDetails : productDetailsList) {
            productBeanList.add(convertToBean(productDetails));
        }
        return productBeanList;
    }

    public List<ProductBean> getProductsByIds( List<ProductBean> productBeans) {

        List<Integer> productIds = new ArrayList<>();
        for (ProductBean productBean : productBeans) {
            productIds.add(productBean.getProductId());
            logger.info("Product Ids : {}", productBean.getProductId());
        }
        List<ProductDetails> productDetailsList = productDao.findAllById(productIds);
        List<ProductBean> productBeanList = new ArrayList<>();
        for (ProductDetails productDetails : productDetailsList) {
            productBeanList.add(convertToBean(productDetails));
        }
        return productBeanList;
    }

    @Override
    public ProductBean updateProduct(ProductBean productBean) {

        ProductDetails productDetailsDB = productDao.findById(productBean.getProductId()).orElse(null);
        if (productDetailsDB == null) {
            logger.info("Product could not be updated as it does not exist for id: " + productBean.getProductId());
            return null;
        }
        ProductDetails productDetails = convertToEntity(productBean);
        productDetails.setProductId(productBean.getProductId());
        productDetails.setCreatedAt(productDetailsDB.getCreatedAt());
        productDetails.setUpdatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return convertToBean(productDao.save(productDetails));
    }

    @Override
    public String deleteProduct(Integer productId) {

        ProductDetails productDetailsDB = productDao.findById(productId).orElse(null);
        if (productDetailsDB == null) {
            return "Product could not be deleted as it does not exist for id: " + productId;
        }
        productDao.deleteById(productId);
        return "Product deleted successfully for id: " + productId;
    }

    ProductDetails convertToEntity(ProductBean productBean) {

        ProductDetails productDetails = new ProductDetails();
        productDetails.setName(productBean.getName());
        productDetails.setDescription(productBean.getDescription());
        productDetails.setPrice(productBean.getPrice());
        return productDetails;
    }

    ProductBean convertToBean(ProductDetails productDetails) {

        ProductBean productBean = new ProductBean();
        productBean.setProductId(productDetails.getProductId());
        productBean.setName(productDetails.getName());
        productBean.setDescription(productDetails.getDescription());
        productBean.setPrice(productDetails.getPrice());
        productBean.setCreatedAt(productDetails.getCreatedAt());
        productBean.setUpdatedAt(productDetails.getUpdatedAt());
        return productBean;
    }

}
