package com.jocata.oms.order.service;

import com.jocata.oms.bean.order.OrderBean;

public interface OrderService {

    OrderBean saveOrder(OrderBean orderBean );

    OrderBean getOrder(Integer orderId );

    OrderBean processOrder(Integer orderId );

}
