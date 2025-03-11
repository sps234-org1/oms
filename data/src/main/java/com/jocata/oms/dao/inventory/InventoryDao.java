package com.jocata.oms.dao.inventory;

import com.jocata.oms.entity.inventory.InventoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryDao extends JpaRepository<InventoryDetails,Integer> {

}
