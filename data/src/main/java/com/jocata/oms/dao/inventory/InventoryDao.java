package com.jocata.oms.dao.inventory;

import com.jocata.oms.entity.inventory.InventoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryDao extends JpaRepository<InventoryDetails,Integer> {


    @Query("SELECT i FROM InventoryDetails i WHERE i.productId IN :productIds")
    List<InventoryDetails> findAllByProductId( List<Integer> productIds );

}
