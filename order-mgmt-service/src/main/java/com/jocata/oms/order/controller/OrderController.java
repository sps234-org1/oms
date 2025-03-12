package com.jocata.oms.order.controller;

import com.jocata.oms.bean.OrderBean;
import com.jocata.oms.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    OrderBean saveOrder(@RequestBody OrderBean orderBean) {
        return orderService.saveOrder(orderBean);
    }

    @GetMapping("/get/{orderId}")
    OrderBean getOrder(@PathVariable Integer orderId) {
        return orderService.getOrder(orderId);
    }


}
