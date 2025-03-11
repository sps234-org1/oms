package com.jocata.oms.dao.product;

import com.jocata.oms.entity.product.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDao extends JpaRepository<ProductDetails,Integer> {
}
