package com.company.scm.repository;

import com.company.scm.model.SalesOrder;
import com.company.scm.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalesOrderRepository {
    private final Map<String, SalesOrder> store = new HashMap<>();

    public void save(SalesOrder order) {
        store.put(order.getOrderId(), order);
        LoggerUtil.info("SalesOrder saved: " + order.getOrderId());
    }

    public SalesOrder findById(String orderId) {
        return store.get(orderId);
    }

    public List<SalesOrder> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " sales order details.");
        return new ArrayList<>(store.values());
    }

    public void update(SalesOrder order) {
        LoggerUtil.info("Updating the sales order " + order.getOrderId());
        store.put(order.getOrderId(), order);
    }

    public void delete(String orderId) {
        LoggerUtil.info("Deleting the sales order " + orderId);
        store.remove(orderId);
    }
    public void clear() {
        store.clear();
        LoggerUtil.debug("SalesOrder repository cleared.");
    }
}
