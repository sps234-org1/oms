package com.jocata.oms.entity.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import org.springframework.lang.NonNull;

import java.sql.Timestamp;

@Entity
@Table(name = "inventory")
public class InventoryDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryId;
    @Column(nullable = false)
    private Integer productId;
    @Min(0)
    private Integer stockQuantity;
    @Min(0)
    private Integer reservedStock;
    private Timestamp lastUpdated;

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(Integer reservedStock) {
        this.reservedStock = reservedStock;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
