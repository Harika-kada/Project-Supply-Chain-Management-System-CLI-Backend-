package com.company.scm.model;

import java.time.LocalDate;

public class PurchaseOrder {
    public enum Status {PENDING, DELIVERED, CANCELLED }

    private String orderId;
    private String supplierId;
    private String productId;
    private int quantity;
    private LocalDate orderDate;
    private Status status;

    public PurchaseOrder(String orderId, String supplierId, String productId, int quantity, LocalDate orderDate, Status status) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.status = status;
    }
    public String getOrderId()
    {
        return orderId;
    }
    public String getSupplierId()
    {
        return supplierId;
    }
    public String getProductId()
    {
        return productId;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public LocalDate getOrderDate()
    {
        return orderDate;
    }
    public Status getStatus()
    {
        return status;
    }
}
