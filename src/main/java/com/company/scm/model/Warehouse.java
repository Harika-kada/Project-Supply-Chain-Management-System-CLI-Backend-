package com.company.scm.model;

public class Warehouse {
    private String warehouseId;
    private String location;
    private int capacity;

    public Warehouse(String warehouseId, String location, int capacity) {
        this.warehouseId = warehouseId;
        this.location = location;
        this.capacity = capacity;
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public String getLocation() {
        return location;
    }
    public int getCapacity()
    {
        return capacity;
    }
}
