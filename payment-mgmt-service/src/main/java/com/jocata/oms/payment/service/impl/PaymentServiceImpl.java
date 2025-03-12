package com.jocata.oms.payment.service.impl;

import com.jocata.oms.bean.PaymentBean;
import com.jocata.oms.dao.payment.PaymentDao;
import com.jocata.oms.entity.payment.PaymentDetails;
import com.jocata.oms.enums.PaymentMethod;
import com.jocata.oms.enums.PaymentStatus;
import com.jocata.oms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDao paymentDao;

    @Override
    public PaymentBean createPayment(PaymentBean paymentBean) {

        PaymentDetails paymentDetails = convertToEntity(paymentBean);
        paymentDetails.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        paymentDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentDetails.setTransactionId(UUID.randomUUID().toString());
        paymentDetails.setPaymentStatus(PaymentStatus.COMPLETED);
        PaymentDetails paymentDetailsDB = paymentDao.save(paymentDetails);
        return convertToBean(paymentDetailsDB);
    }

    @Override
    public List<PaymentBean> getPaymentsByOrderId(Integer orderId) {
        return List.of();
    }

    @Override
    public PaymentBean updatePayment(PaymentBean paymentBean) {
        return null;
    }

    private PaymentDetails convertToEntity(PaymentBean paymentBean) {

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setPaymentId(paymentBean.getPaymentId());
        paymentDetails.setOrderId(paymentBean.getOrderId());
        paymentDetails.setPaymentDate(paymentBean.getPaymentDate());
        paymentDetails.setPaymentMethod(paymentBean.getPaymentMethod());
        paymentDetails.setPaymentStatus(paymentBean.getPaymentStatus());
        paymentDetails.setTransactionId(paymentBean.getTransactionId());
        paymentDetails.setAmount(paymentBean.getAmount());
        paymentDetails.setCreatedAt(paymentBean.getCreatedAt());
        paymentDetails.setUpdatedAt(paymentBean.getUpdatedAt());
        return paymentDetails;
    }

    private PaymentBean convertToBean(PaymentDetails paymentDetails) {

        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setPaymentId(paymentDetails.getPaymentId());
        paymentBean.setOrderId(paymentDetails.getOrderId());
        paymentBean.setPaymentDate(paymentDetails.getPaymentDate());
        paymentBean.setTransactionId(paymentDetails.getTransactionId());
        paymentBean.setAmount(paymentDetails.getAmount());
        paymentBean.setCreatedAt(paymentDetails.getCreatedAt());
        paymentBean.setUpdatedAt(paymentDetails.getUpdatedAt());
        paymentBean.setPaymentMethod(paymentDetails.getPaymentMethod());
        paymentBean.setPaymentStatus(paymentDetails.getPaymentStatus());
        return paymentBean;
    }

}
