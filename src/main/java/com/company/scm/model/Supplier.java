package com.company.scm.model;

public class Supplier {
    private String supplierId;
    private String name;
    private String contactInfo;
    private double rating;

    public Supplier(String supplierId, String name, String contactInfo, double rating) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.rating = rating;
    }
    public String getSupplierId()
    {
        return supplierId;
    }
    public String getName()
    {
        return name;
    }
    public String getContactInfo()
    {
        return contactInfo;
    }
    public double getRating()
    {
        return rating;
    }
}
