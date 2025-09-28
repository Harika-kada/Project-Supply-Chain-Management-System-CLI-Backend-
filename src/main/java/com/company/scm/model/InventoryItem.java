package com.company.scm.model;

public class InventoryItem {
    private final String itemId;
    private final String productId;
    private final String warehouseId;
    private int quantity;
    private LocalDate lastRestockedDate;

    public InventoryItem(String productId, String warehouseId, int quantity, LocalDate lastRestockedDate) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.itemId = productId + "-" + warehouseId;
        this.quantity = quantity;
        this.lastRestockedDate = lastRestockedDate;
    }

    public String getItemId()
    {
        return itemId;
    }
    public static String generateCompositeKey(String productId, String warehouseId) {
        return productId + "_" + warehouseId;
    }
    public String getProductId() {
        return productId;
    }
    public String getWarehouseId() {
        return warehouseId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setLastRestockedDate(LocalDate lastRestockedDate) {
        this.lastRestockedDate = lastRestockedDate;
    }
}
