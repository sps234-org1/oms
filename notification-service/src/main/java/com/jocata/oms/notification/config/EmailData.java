package com.jocata.oms.notification.config;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "emailData")
public class EmailData {

    private String customerName;
    private String orderId;
    private String productName;
    private String price;
    private String deliveryStatus;
    private String deliveryDate;

    public String getCustomerName() {
        return customerName;
    }

    @XmlElement
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderId() {
        return orderId;
    }

    @XmlElement
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    @XmlElement
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    @XmlElement
    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    @XmlElement
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    @XmlElement
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

}
