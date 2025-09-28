package com.company.scm.repository;

import com.company.scm.model.Product;
import com.company.scm.model.Supplier;
import com.company.scm.model.Warehouse;
import com.company.scm.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseRepository {
    private final Map<String, Warehouse> store = new HashMap<>();

    private String failOnFindId = null;

    public void save(Warehouse warehouse)
    {
        store.put(warehouse.getWarehouseId(), warehouse);
        LoggerUtil.info("Warehouse saved: " + warehouse.getWarehouseId());
    }
    public Warehouse findById(String warehouseId) {
        if (warehouseId.equals(failOnFindId)) {
            LoggerUtil.warn("Warehouse not found for the id: " + warehouseId);
            return null;
        }
        return store.get(warehouseId);
    }
    public List<Warehouse> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " warehouse details.");
        return new ArrayList<>(store.values());
    }
    public void setFailOnFind(String warehouseId) {
        this.failOnFindId = warehouseId;
    }

    public void addWarehouse(String id, String location, int capacity) {
        store.put(id, new Warehouse(id, location, capacity));
    }
}
