package com.jocata.oms.payment.service;

import com.jocata.oms.bean.PaymentBean;

import java.util.List;

public interface PaymentService {

    PaymentBean createPayment(PaymentBean paymentBean );

    List<PaymentBean> getPaymentsByOrderId(Integer orderId );

    PaymentBean updatePayment( PaymentBean paymentBean );

}
