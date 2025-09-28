package com.company.scm.repository;

import com.company.scm.model.InventoryItem;
import com.company.scm.util.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

public class InventoryRepository {

    private final Map<String, InventoryItem> inventoryStore;

    public InventoryRepository() {
        this.inventoryStore = new HashMap<>();
    }

    public void save(InventoryItem item) {
        inventoryStore.put(item.getItemId(), item);
        LoggerUtil.info("Inventory item saved/updated: " + item.getItemId());
    }

    public InventoryItem findByProductIdAndWarehouseId(String productId, String warehouseId) {
        String key = InventoryItem.generateCompositeKey(productId, warehouseId);
        return inventoryStore.get(key);
    }

    public Map<String, InventoryItem> findAll() {
        LoggerUtil.info("Fetching all " + inventoryStore.size() + " inventory items.");
        return inventoryStore;
    }

    public void clear() {
        inventoryStore.clear();
        LoggerUtil.info("Inventory repository cleared.");
    }
}
