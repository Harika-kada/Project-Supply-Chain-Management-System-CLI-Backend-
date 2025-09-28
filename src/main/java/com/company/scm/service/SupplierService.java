package com.company.scm.service;

import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.model.Supplier;
import com.company.scm.exception.DuplicateEntityException;
import com.company.scm.repository.SupplierRepository;

import java.util.List;

public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public void addSupplier(Supplier supplier) throws DuplicateEntityException {
        if (supplierRepository.findById(supplier.getSupplierId()) != null) {
            throw new DuplicateEntityException("Supplier already exists: " + supplier.getSupplierId());
        }
        supplierRepository.save(supplier);
    }
    public Supplier getSupplier(String supplierId) throws EntityNotFoundException {
        Supplier supplier = supplierRepository.findById(supplierId);
        if (supplier == null) {
            throw new EntityNotFoundException("Supplier not found: " + supplierId);
        }
        return supplier;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
}
