package com.company.scm.service;

import com.company.scm.model.Warehouse;
import com.company.scm.exception.DuplicateEntityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.repository.WarehouseRepository;
import com.company.scm.util.CLIPrinter;

import java.util.List;

public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }
    public void addWarehouse(Warehouse warehouse) throws DuplicateEntityException {
        CLIPrinter.printHeader("Adding Warehouse");
        if (warehouseRepository.findById(warehouse.getWarehouseId()) != null) {
            throw new DuplicateEntityException("Warehouse already exists: " + warehouse.getWarehouseId());
        }
        warehouseRepository.addWarehouse(warehouse.getWarehouseId(), warehouse.getLocation(), warehouse.getCapacity());
    }
    public Warehouse getWarehouse(String warehouseId) throws EntityNotFoundException {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse == null) {
            throw new EntityNotFoundException(warehouseId);
        }
        return warehouse;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }
}
