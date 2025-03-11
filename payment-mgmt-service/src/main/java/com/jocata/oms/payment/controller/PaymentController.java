package com.jocata.oms.payment.controller;

import com.jocata.oms.bean.PaymentBean;
import com.jocata.oms.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/create")
    PaymentBean createPayment(@RequestBody PaymentBean paymentBean ){
        return paymentService.createPayment( paymentBean );
    }

    @GetMapping("/get/{orderId}")
    List<PaymentBean> getPaymentsByOrderId(@PathVariable Integer orderId ) {
        return paymentService.getPaymentsByOrderId( orderId );
    }

    @PutMapping("/update")
    PaymentBean updatePayment(@RequestBody PaymentBean paymentBean ){
        return paymentService.updatePayment( paymentBean );
    }
}
