package com.company.scm.model;

import java.math.BigDecimal;

public class Product {
    private final String productId;
    private final String name;
    private final String description;
    private final String category;
    private final BigDecimal unitPrice;

    public Product(String productId, String name, String description, String category, BigDecimal unitPrice) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.unitPrice = unitPrice;
    }
    public String getProductId() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getCategory() {
        return category;
    }
    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }
}
