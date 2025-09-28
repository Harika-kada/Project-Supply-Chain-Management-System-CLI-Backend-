package com.company.scm.repository;

import com.company.scm.model.Supplier;
import com.company.scm.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierRepository {
    private final Map<String, Supplier> store = new HashMap<>();

    public void save(Supplier supplier) {
        store.put(supplier.getSupplierId(), supplier);
        LoggerUtil.info("Supplier saved: " + supplier.getSupplierId() + " - " + supplier.getName());
    }

    public Supplier findById(String supplierId) {
        return store.get(supplierId);
    }

    public List<Supplier> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " supplier details.");
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
        LoggerUtil.debug("Supplier repository cleared.");
    }
}
