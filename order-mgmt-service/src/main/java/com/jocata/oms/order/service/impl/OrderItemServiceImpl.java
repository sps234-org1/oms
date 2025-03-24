package com.jocata.oms.order.service.impl;

import com.jocata.oms.bean.order.OrderItemBean;
import com.jocata.oms.entity.order.OrderDetails;
import com.jocata.oms.entity.order.OrderItemDetails;
import com.jocata.oms.order.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Override
    public List<OrderItemDetails> convertToOrderItemDetails(OrderDetails order, List<OrderItemBean> orderItemRequests) {

        List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
        for (OrderItemBean orderItemRequest : orderItemRequests) {
            OrderItemDetails orderItemDetails = convertToEntity(order, orderItemRequest);
            orderItemDetailsList.add(orderItemDetails);
        }
        return orderItemDetailsList;
    }

    @Override
    public List<OrderItemBean> convertToOrderItemBean(OrderDetails order) {

        List<OrderItemBean> orderItemBeans = new ArrayList<>();
        for (OrderItemDetails orderItemDetail : order.getOrderItems()) {
            OrderItemBean orderItemBean = convertToBean(orderItemDetail);
            orderItemBeans.add(orderItemBean);
        }
        return orderItemBeans;
    }

    private OrderItemDetails convertToEntity(OrderDetails order, OrderItemBean orderItemBean) {

        OrderItemDetails orderItemDetails = new OrderItemDetails();
        orderItemDetails.setOrder(order);
        orderItemDetails.setProductId(orderItemBean.getProductId());
        orderItemDetails.setQuantity(orderItemBean.getQuantity());
        orderItemDetails.setPrice(orderItemBean.getPrice());
        orderItemDetails.setSubTotal(orderItemBean.getSubTotal());
        if (orderItemBean.getCreatedAt() == null) {
            orderItemDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            orderItemDetails.setCreatedAt(orderItemBean.getCreatedAt());
        }
        orderItemDetails.setUpdatedAt(orderItemBean.getUpdatedAt());
        return orderItemDetails;
    }

    private OrderItemBean convertToBean(OrderItemDetails orderItem) {

        OrderItemBean orderItemBean = new OrderItemBean();
        orderItemBean.setOrderItemId(orderItem.getOrderItemId());
        orderItemBean.setProductId(orderItem.getProductId());
        orderItemBean.setQuantity(orderItem.getQuantity());
        orderItemBean.setPrice(orderItem.getPrice());
        orderItemBean.setSubTotal(orderItem.getSubTotal());
        orderItemBean.setCreatedAt(orderItem.getCreatedAt());
        orderItemBean.setUpdatedAt(orderItem.getUpdatedAt());
        orderItemBean.setOrderId(orderItem.getOrder().getOrderId());
        return orderItemBean;
    }
}
