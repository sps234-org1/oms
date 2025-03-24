package com.jocata.oms.order.service;

import com.jocata.oms.bean.order.OrderItemBean;
import com.jocata.oms.entity.order.OrderDetails;
import com.jocata.oms.entity.order.OrderItemDetails;

import java.util.List;

public interface OrderItemService {

    List<OrderItemDetails> convertToOrderItemDetails(OrderDetails order, List<OrderItemBean> orderItemBeans);

    List<OrderItemBean> convertToOrderItemBean(OrderDetails orderDetails);


}
