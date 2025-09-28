package com.company.scm.repository;

import com.company.scm.model.Shipment;
import com.company.scm.util.LoggerUtil;

import java.util.*;

public class ShipmentRepository {
    private final Map<String, Shipment> store = new HashMap<>();

    public void save(Shipment shipment) {
        store.put(shipment.getShipmentId(), shipment);
        LoggerUtil.info("Shipment saved: " + shipment.getShipmentId());
    }

    public Shipment findById(String shipmentId) {
        return store.get(shipmentId);
    }

    public List<Shipment> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " shipment details.");
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
        LoggerUtil.debug("Shipment repository cleared.");
    }
}
