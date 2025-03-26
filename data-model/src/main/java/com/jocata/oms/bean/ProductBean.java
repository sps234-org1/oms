package com.jocata.oms.bean;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductBean {

    private Integer productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private InventoryBean inventoryInfo;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public InventoryBean getInventoryInfo() {
        return inventoryInfo;
    }

    public void setInventoryInfo(InventoryBean inventoryInfo) {
        this.inventoryInfo = inventoryInfo;
    }
}
