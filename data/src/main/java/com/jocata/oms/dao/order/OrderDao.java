package com.jocata.oms.dao.order;

import com.jocata.oms.entity.order.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<OrderDetails,Integer> {
}
