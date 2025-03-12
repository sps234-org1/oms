package com.jocata.oms.order.service.impl;


import com.jocata.oms.bean.*;
import com.jocata.oms.dao.order.OrderDao;
import com.jocata.oms.entity.order.OrderDetails;
import com.jocata.oms.entity.order.OrderItemDetails;
import com.jocata.oms.enums.OrderStatus;
import com.jocata.oms.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private ExternalService externalService;

    @Autowired
    private OrderDao orderDao;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderBean saveOrder(OrderBean orderBean) {

        List<InventoryBean> inventoryBeanList = externalService.getInventory().block();
        logger.info("Inventory fetched.");

        OrderDetails orderDetails = convertToEntity(orderBean);
        orderDetails.setOrderStatus(OrderStatus.PENDING);
        orderDetails.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
        orderDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        orderDetails.setPaid(false);
        OrderDetails orderDetailsDB = orderDao.save(orderDetails);
        return convertToBean(orderDetailsDB);
    }

    @Override
    public OrderBean getOrder(Integer orderId) {

        OrderDetails orderDetails = orderDao.findById(orderId).orElse(null);
        if (orderDetails == null) {
            return null;
        }
        UserBean userDB = externalService.getUser(orderDetails.getCustomerId()).block();
        logger.info("User details fetched");

        if (userDB == null) {
            return null;
        }

        List<ProductBean> products = externalService.getProducts().block();
        logger.info("Product response : {}", products.size());

        if (products == null || products.isEmpty()) {
            logger.error("No products found");
        }

        OrderBean response = convertToBean(orderDetails);
        response.setCustomerDetails(userDB);

        for (OrderItemBean orderItem : response.getOrderItems()) {
            for (ProductBean product : products) {
                if (orderItem.getProductId().equals(product.getProductId())) {
                    orderItem.setProduct(product);
                }
                if (product.getProductId() == -1) {
                    logger.error("Product service is down");
                    orderItem.setProduct(product);
                }
            }
        }
        response.setOrderItems(response.getOrderItems());
        return response;
    }

    private OrderDetails convertToEntity(OrderBean orderBean) {

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerId(orderBean.getCustomerId());
        orderDetails.setOrderStatus(orderBean.getOrderStatus());
        orderDetails.setOrderDate(orderBean.getOrderDate());
        orderDetails.setTotalAmount(orderBean.getTotalAmount());
        orderDetails.setPaid(orderBean.getPaid());
        orderDetails.setCreatedAt(orderBean.getCreatedAt());
        orderDetails.setUpdatedAt(orderBean.getUpdatedAt());
        orderDetails.setOrderItems(convertToOrderDetails(orderDetails, orderBean.getOrderItems()));
        return orderDetails;
    }

    private OrderBean convertToBean(OrderDetails orderDetails) {

        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(orderDetails.getOrderId());
        orderBean.setCustomerId(orderDetails.getCustomerId());
        orderBean.setOrderStatus(orderDetails.getOrderStatus());
        orderBean.setOrderDate(orderDetails.getOrderDate());
        orderBean.setTotalAmount(orderDetails.getTotalAmount());
        orderBean.setPaid(orderDetails.getPaid());
        orderBean.setCreatedAt(orderDetails.getCreatedAt());
        orderBean.setUpdatedAt(orderDetails.getUpdatedAt());
        orderBean.setOrderItems(convertToOrderBean(orderDetails.getOrderItems()));
        return orderBean;
    }

    private List<OrderItemBean> convertToOrderBean(List<OrderItemDetails> orderItemDetails) {

        List<OrderItemBean> orderItemBeanList = new ArrayList<>();
        for (OrderItemDetails orderItem : orderItemDetails) {
            OrderItemBean orderItemBean = new OrderItemBean();
            orderItemBean.setOrderItemId(orderItem.getOrderItemId());
            orderItemBean.setProductId(orderItem.getProductId());
            orderItemBean.setQuantity(orderItem.getQuantity());
            orderItemBean.setPrice(orderItem.getPrice());
            orderItemBean.setSubTotal(orderItem.getSubTotal());
            orderItemBean.setCreatedAt(orderItem.getCreatedAt());
            orderItemBean.setUpdatedAt(orderItem.getUpdatedAt());
            orderItemBeanList.add(orderItemBean);
        }
        return orderItemBeanList;
    }

    private List<OrderItemDetails> convertToOrderDetails(OrderDetails order, List<OrderItemBean> orderItemBeans) {

        List<OrderItemDetails> orderItemDetailsList = new ArrayList<>();
        for (OrderItemBean orderItemBean : orderItemBeans) {
            OrderItemDetails orderItemDetails = new OrderItemDetails();
            orderItemDetails.setOrder(order);
            orderItemDetails.setProductId(orderItemBean.getProductId());
            orderItemDetails.setQuantity(orderItemBean.getQuantity());
            orderItemDetails.setPrice(orderItemBean.getPrice());
            orderItemDetails.setSubTotal(orderItemBean.getSubTotal());
            orderItemDetails.setCreatedAt(orderItemBean.getCreatedAt());
            orderItemDetails.setUpdatedAt(orderItemBean.getUpdatedAt());
            orderItemDetailsList.add(orderItemDetails);
        }
        return orderItemDetailsList;
    }
}
