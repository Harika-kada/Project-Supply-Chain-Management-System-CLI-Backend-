package com.company.scm.model;

import java.time.LocalDateTime;

public class SalesOrder {
    public enum Status { PROCESSING, FULFILLED, CANCELLED }

    private final String orderId;
    private final String productId;
    private final int quantity;
    private final LocalDateTime orderDate;
    private Status status;
    private final String customerDetails;

    public SalesOrder(String orderId, String productId, int quantity, LocalDateTime orderDate, Status status, String customerDetails) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
        this.customerDetails = customerDetails;
    }
    public String getOrderId()
    {
        return orderId;
    }
    public String getProductId()
    {
        return productId;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public Status getStatus()
    {
        return status;
    }
    public String getCustomerDetails()
    {
        return customerDetails;
    }
    public LocalDateTime getOrderDate()
    {
        return orderDate;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
