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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
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
        logger.info("Inventory List: {}", inventoryBeanList.size());

        orderBean.setOrderStatus(OrderStatus.PENDING);
        orderBean.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderBean.setCreatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderBean.setPaid(false);

        List<OrderItemBean> orderItems = orderBean.getOrderItems();


        double totalAmount = 0.0;



        OrderDetails orderDetailsDB = orderDao.save(convertToEntity(orderBean));
        return convertToBean(orderDetailsDB);
    }

    private OrderBean processOrder( OrderBean orderBean ) {

        List<ProductBean> productBeanList = externalService.getProducts().block();
        logger.info("Product List: {}", productBeanList.size());

        List<InventoryBean> inventoryBeanList = externalService.getInventory().block();
        logger.info("Inventory List: {}", inventoryBeanList.size());

        orderBean.setOrderStatus(OrderStatus.PENDING);
        orderBean.setOrderDate(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderBean.setCreatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        orderBean.setPaid(false);

        List<OrderItemBean> orderItems = orderBean.getOrderItems();


        double totalAmount = 0.0;

        for (OrderItemBean orderItem : orderItems) {
            for (ProductBean productBean : productBeanList) {
                if (orderItem.getProductId().equals(productBean.getProductId())) {
                    double subTotal = productBean.getPrice().multiply(new BigDecimal(orderItem.getQuantity())).doubleValue();
                    orderItem.setPrice(productBean.getPrice());
                    orderItem.setSubTotal(new BigDecimal(subTotal));
                    orderItem.setCreatedAt(Timestamp.valueOf(LocalDate.now().atStartOfDay()));
                    totalAmount += subTotal;
                    break;
                }
            }
        }
        orderBean.setTotalAmount(new BigDecimal(totalAmount));
        orderBean.setOrderItems(orderItems);

        OrderDetails orderDetailsDB = orderDao.save(convertToEntity(orderBean));
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
        logger.info("Product fetched");

        if (products == null || products.isEmpty()) {
            return null;
        }

        OrderBean response = convertToBean(orderDetails);
        response.setCustomerDetails(userDB);

        for (OrderItemBean orderItem : response.getOrderItems()) {
            for (ProductBean product : products) {
                if (orderItem.getProductId().equals(product.getProductId())) {
                    orderItem.setProduct(product);
                }
            }
        }
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
