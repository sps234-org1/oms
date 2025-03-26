package com.jocata.oms.order.service.impl;


import com.jocata.apis.inventory.InventoryApiClient;
import com.jocata.apis.payment.PaymentApiClient;
import com.jocata.apis.product.ProductApiClient;
import com.jocata.apis.user.UserApiClient;
import com.jocata.oms.bean.InventoryBean;
import com.jocata.oms.bean.PaymentBean;
import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.bean.order.OrderBean;
import com.jocata.oms.bean.order.OrderItemBean;
import com.jocata.oms.dao.order.OrderDao;
import com.jocata.oms.entity.order.OrderDetails;
import com.jocata.oms.enums.OrderStatus;
import com.jocata.oms.enums.PaymentStatus;
import com.jocata.oms.order.publisher.InventoryEventPublisher;
import com.jocata.oms.order.publisher.OrderEventPublisher;
import com.jocata.oms.order.service.OrderItemService;
import com.jocata.oms.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private InventoryEventPublisher inventoryEventPublisher;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private PaymentApiClient paymentApiClient;

    @Autowired
    private InventoryApiClient inventoryApiClient;

    @Autowired
    private ProductApiClient productApiClient;

    @Autowired
    private UserApiClient userApiClient;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemService orderItemService;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderBean saveOrder(OrderBean orderBean) {

        reserveInventory(orderBean.getOrderItems());

        OrderDetails orderDetails = convertToEntity(orderBean);
        orderDetails.setOrderStatus(OrderStatus.PENDING);
        orderDetails.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        orderDetails.setPaid(false);
        OrderDetails orderDetailsDB = orderDao.save(orderDetails);

        PaymentBean paymentRequest = new PaymentBean();
        paymentRequest.setPaymentMethod(orderBean.getPaymentMethod());
        paymentRequest.setOrderId(orderDetailsDB.getOrderId());
        PaymentBean paymentResponse = paymentApiClient.createPayment(paymentRequest).block();

        if (paymentResponse == null || !Objects.equals(paymentResponse.getOrderId(), orderDetailsDB.getOrderId())
                || paymentResponse.getPaymentId() == -1 || paymentResponse.getPaymentStatus().equals(PaymentStatus.FAILED)) {
            throw new IllegalArgumentException("Payment failed for order id : " + orderDetailsDB.getOrderId());
        }
        if (paymentResponse.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
            orderDetailsDB.setPaid(true);
            orderDetails.setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
            orderDetailsDB.setOrderStatus(OrderStatus.SHIPPED);
            orderDetailsDB.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            orderDetailsDB = orderDao.save(orderDetailsDB);
            releaseInventory(orderBean.getOrderItems());
        }
        OrderBean response = getOrder(orderDetailsDB.getOrderId());
        if (orderDetailsDB.getPaid()) {
            updateInventory(orderBean.getOrderItems());
            orderEventPublisher.publishOrderEvent(response);
            logger.info("Order event published for order id : {}", orderDetailsDB.getOrderId());
        }
        return response;
    }

    private void reserveInventory(List<OrderItemBean> orderItems) {

        List<InventoryBean> inventoryRequests = new ArrayList<>();
        for (OrderItemBean orderItem : orderItems) {
            InventoryBean inventoryRequest = new InventoryBean();
            inventoryRequest.setProductId(orderItem.getProductId());
            inventoryRequest.setReservedStock(orderItem.getQuantity());
            inventoryRequests.add(inventoryRequest);
        }
        inventoryEventPublisher.publishReserveInventoryEvent(inventoryRequests);
    }

    private void releaseInventory(List<OrderItemBean> orderItems) {

        List<InventoryBean> inventoryRequests = new ArrayList<>();
        for (OrderItemBean orderItem : orderItems) {
            InventoryBean inventoryRequest = new InventoryBean();
            inventoryRequest.setProductId(orderItem.getProductId());
            inventoryRequests.add(inventoryRequest);
        }
        inventoryEventPublisher.publishReleaseInventoryEvent(inventoryRequests);
    }

    private void updateInventory(List<OrderItemBean> orderItems) {

        List<InventoryBean> inventoryRequests = new ArrayList<>();
        for (OrderItemBean orderItem : orderItems) {
            InventoryBean inventoryRequest = new InventoryBean();
            inventoryRequest.setProductId(orderItem.getProductId());
            inventoryRequest.setStockQuantity(orderItem.getQuantity());
            logger.info("Inventory quantity : {}", orderItem.getQuantity());
            inventoryRequest.setLastUpdated(Timestamp.valueOf(LocalDateTime.now()));
            inventoryRequests.add(inventoryRequest);
        }
        inventoryEventPublisher.publishUpdateInventoryEvent(inventoryRequests);
    }

    @Override
    public OrderBean getOrder(Integer orderId) {

        OrderDetails orderDetails = orderDao.findById(orderId).orElse(null);
        if (orderDetails == null) {
            logger.error("Order not found");
            return null;
        }
        OrderBean response = convertToBean(orderDetails);
        return getOrderBeanResponse(response);
    }

    private Mono<FetchResult> fetchAsync(OrderBean orderBean) {

        Mono<UserBean> userMono = userApiClient.getUser(orderBean.getCustomerId());
        Mono<List<ProductBean>> productsMono = productApiClient.getProducts();
        Mono<List<InventoryBean>> inventoryMono = inventoryApiClient.getInventory();

        return Mono.zip(userMono, productsMono, inventoryMono)
                .map(tuple -> {
                    FetchResult fetchResult = new FetchResult();
                    fetchResult.user = tuple.getT1();
                    fetchResult.products = tuple.getT2();
                    fetchResult.inventoryList = tuple.getT3();
                    return fetchResult;
                })
                .doOnError(throwable -> logger.error("Error occurred while fetching data", throwable));
    }

    private OrderBean getOrderBeanResponse(OrderBean orderBean) {

        FetchResult fetchResult = fetchAsync(orderBean).block();
        if (fetchResult == null) {
            logger.error("Failed to fetch data");
            return orderBean;
        }
        List<InventoryBean> inventoryList = fetchResult.inventoryList;
        if (inventoryList == null || inventoryList.isEmpty()) {
            logger.error("No inventory found");
        }
        List<ProductBean> products = fetchResult.products;
        if (products == null || products.isEmpty()) {
            logger.error("No products found");
        }
        UserBean user = fetchResult.user;
        if (user == null) {
            logger.error("No user found");
        }
        orderBean.setCustomerDetails(user);

        for (OrderItemBean orderItem : orderBean.getOrderItems()) {
            Integer reqProductId = orderItem.getProductId();
            if (inventoryList == null || inventoryList.isEmpty()) {
                logger.error("No inventory found");
                continue;
            }
            for (InventoryBean inventory : inventoryList) {
                if (inventory == null) {
                    continue;
                }
                Integer productIdDB = inventory.getProductId();
                if (reqProductId.equals(productIdDB)) {
                    if (products == null || products.isEmpty()) {
                        logger.error("No products found");
                        continue;
                    }
                    for (ProductBean product : products) {
                        if (product == null) {
                            continue;
                        }
                        if (productIdDB.equals(product.getProductId())) {
                            orderItem.setProduct(product);
                            product.setInventoryInfo(inventory);
                            break;
                        }
                    }
                }
            }
        }
        orderBean.setOrderItems(orderBean.getOrderItems());
        return orderBean;
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
        orderDetails.setOrderItems(orderItemService.convertToOrderItemDetails(orderDetails, orderBean.getOrderItems()));
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
        orderBean.setOrderItems(orderItemService.convertToOrderItemBean(orderDetails));
        return orderBean;
    }

    public OrderBean processOrder(Integer orderId) {

//        inventoryListDB = externalService.getInventory().block();
//        if (inventoryListDB == null || inventoryListDB.isEmpty()) {
//            logger.error("No inventory found");
//        }
//        logger.info("Inventory response : {}", inventoryListDB.size());
//
//        productsDB = externalService.getProducts().block();
//        if (productsDB == null || productsDB.isEmpty()) {
//            logger.error("No products found");
//        }
//        logger.info("Product response : {}", productsDB.size());
//
//        List<InventoryBean> inventoryRequests = new ArrayList<>();
//        List<OrderItemBean> orderItems = orderRequest.getOrderItems();
//
//        for (OrderItemBean orderItem : orderRequest.getOrderItems()) {
//            int productId = orderItem.getProductId();
//            int requiredQuantity = orderItem.getQuantity();
//            double subTotal = 0;
//
//            for (ProductBean product : productsDB) {
//                if (product.getProductId() == productId) {
//                    BigDecimal price = product.getPrice();
//
//                    for (InventoryBean inventory : inventoryListDB) {
//                        if (inventory.getProductId() == productId) {
//                            int availableStock = inventory.getStockQuantity();
//                            int requiredStock = orderItem.getQuantity();
//                            if (availableStock >= requiredStock) {
//                                InventoryBean inventoryRequest = new InventoryBean();
//                                inventoryRequest.setProductId(productId);
//                                inventoryRequest.setReservedStock(requiredStock);
//                                inventoryRequests.add(inventoryRequest);
//                                subTotal += price.doubleValue() * requiredQuantity;
//                                orderItem.setPrice(price);
//                                orderItem.setSubTotal(BigDecimal.valueOf(subTotal));
//                                orderItem.setQuantity(requiredQuantity);
//                                orderItems.add(orderItem);
//                            } else {
//                                logger.error("Insufficient stock for product id : {}", productId);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        inventoryRes = reserveInventory(inventoryRequests);
//        if (inventoryRes == null || inventoryRes.isEmpty()) {
//            logger.error("Inventory reservation failed");
//            return null;
//        }
//
//        orderRequest.setOrderItems(orderItems);

        return null;
    }

}

class FetchResult {
    UserBean user;
    List<ProductBean> products;
    List<InventoryBean> inventoryList;
}
