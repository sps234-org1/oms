package com.jocata.oms.bean.order;

import com.jocata.oms.bean.AddressBean;
import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.enums.OrderStatus;
import com.jocata.oms.enums.PaymentMethod;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OrderRequestBean {

    private Integer orderId;
    private Integer customerId;
    private Timestamp orderDate;
    private AddressBean shippingAddress;
    private ProductBean products;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
    private Boolean isPaid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private UserBean customerDetails;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserBean getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(UserBean customerDetails) {
        this.customerDetails = customerDetails;
    }
}
