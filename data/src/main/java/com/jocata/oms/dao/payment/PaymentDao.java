package com.jocata.oms.dao.payment;

import com.jocata.oms.entity.payment.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentDao extends JpaRepository<PaymentDetails,Integer> {

    @Query("SELECT p FROM PaymentDetails p WHERE p.orderId = ?1")
    List<PaymentDetails> findByOrderId(Integer orderId);

}
