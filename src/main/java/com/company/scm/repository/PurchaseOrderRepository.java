package com.company.scm.repository;

import com.company.scm.model.PurchaseOrder;
import com.company.scm.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseOrderRepository {

    private final Map<String, PurchaseOrder> store = new HashMap<>();

    public void save(PurchaseOrder order) {
        store.put(order.getOrderId(), order);
        LoggerUtil.info("Product saved: " + order.getOrderId());
    }
    public PurchaseOrder findById(String orderId) {
        return store.get(orderId);
    }
    public List<PurchaseOrder> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " purchase order details.");
        return new ArrayList<>(store.values());
    }
    public void update(PurchaseOrder order) {
        LoggerUtil.info("Updating the purchase order " + order.getOrderId());
        store.put(order.getOrderId(), order);
    }
    public void delete(String orderId) {
        LoggerUtil.info("Deleting the purchase order " + orderId);
        store.remove(orderId);
    }
    public void clear() {
        store.clear();
        LoggerUtil.info("Purchase Order repository cleared.");
    }
}
