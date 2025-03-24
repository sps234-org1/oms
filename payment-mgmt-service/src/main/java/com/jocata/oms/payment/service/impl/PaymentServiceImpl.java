package com.jocata.oms.payment.service.impl;

import com.jocata.oms.bean.PaymentBean;
import com.jocata.oms.bean.order.OrderBean;
import com.jocata.oms.dao.payment.PaymentDao;
import com.jocata.oms.entity.payment.PaymentDetails;
import com.jocata.oms.enums.PaymentStatus;
import com.jocata.apis.order.OrderApiClient;
import com.jocata.oms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentDao paymentDao;

    @Autowired
    private OrderApiClient orderApiClient;

    @Override
    public PaymentBean createPayment(PaymentBean paymentBean) {

        PaymentDetails paymentDetails = convertToEntity(paymentBean);
        paymentDetails.setPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
        paymentDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        paymentDetails.setTransactionId(UUID.randomUUID().toString());
        paymentDetails.setPaymentStatus(PaymentStatus.PENDING);

        OrderBean processedOrderResponse = orderApiClient.processOrder(paymentBean.getOrderId()).block();
        if (processedOrderResponse == null || processedOrderResponse.getOrderId() == -1) {
            throw new IllegalArgumentException("Order processing failed for order id: " + paymentBean.getOrderId());
        } else {
            paymentDetails.setPaymentStatus(PaymentStatus.COMPLETED);
        }

        PaymentDetails paymentDetailsDB = paymentDao.save(paymentDetails);
        return convertToBean(paymentDetailsDB);
    }

    @Override
    public List<PaymentBean> getPaymentsByOrderId(Integer orderId) {
        List<PaymentDetails> paymentDetailsList = paymentDao.findByOrderId(orderId);
        List<PaymentBean> paymentBeanList = new ArrayList<>();
        for (PaymentDetails paymentDetails : paymentDetailsList) {
            paymentBeanList.add(convertToBean(paymentDetails));
        }
        return paymentBeanList;
    }

    private PaymentBean getPaymentById(Integer paymentId) {

        PaymentDetails paymentDetails = paymentDao.findById(paymentId).orElse(null);
        if (paymentDetails != null) {
            return convertToBean(paymentDetails);
        }
        return null;
    }

    @Override
    public PaymentBean updatePayment(PaymentBean paymentBean) {

        if (paymentBean.getPaymentId() == null) {
            throw new IllegalArgumentException("Payment Id is required to update the payment");
        }
        PaymentBean paymentDB = getPaymentById(paymentBean.getPaymentId());
        if (paymentBean.getPaymentStatus() != null) {
            paymentDB.setPaymentStatus(paymentBean.getPaymentStatus());
        }
        paymentDB.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        PaymentDetails paymentResponse = paymentDao.save(convertToEntity(paymentDB));
        return convertToBean(paymentResponse);
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
