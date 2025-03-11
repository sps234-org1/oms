package com.jocata.oms.dao.payment;

import com.jocata.oms.entity.payment.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDao extends JpaRepository<PaymentDetails,Integer> {
}
