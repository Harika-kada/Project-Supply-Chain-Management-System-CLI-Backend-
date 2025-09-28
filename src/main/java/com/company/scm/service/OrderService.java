package com.company.scm.service;

import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.model.PurchaseOrder;
import com.company.scm.model.SalesOrder;
import com.company.scm.model.Shipment;
import com.company.scm.repository.PurchaseOrderRepository;
import com.company.scm.repository.SalesOrderRepository;
import com.company.scm.repository.ShipmentRepository;
import com.company.scm.util.CLIPrinter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final InventoryService inventoryService;
    private final ShipmentRepository shipmentRepository;

    public OrderService(PurchaseOrderRepository purchaseOrderRepository, SalesOrderRepository salesOrderRepository, InventoryService inventoryService, ShipmentRepository shipmentRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.inventoryService = inventoryService;
        this.shipmentRepository = shipmentRepository;
    }

    public PurchaseOrder createPurchaseOrder(String supplierId, String productId, int quantity)
            throws InvalidQuantityException {
        CLIPrinter.printHeader("Creating a Purchase order(PO)");
        if (quantity <= 0) {
            throw new InvalidQuantityException("Order quantity must be positive.");
        }
        String orderId = "PO-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        PurchaseOrder order = new PurchaseOrder(orderId, supplierId, productId, quantity, LocalDate.now(), PurchaseOrder.Status.PENDING);
        purchaseOrderRepository.save(order);
        return order;
    }

    public SalesOrder createSalesOrder(String productId, int quantity, String customerDetails)
            throws InvalidQuantityException {
        CLIPrinter.printHeader("Creating a Sales order(SO)");
        if (quantity <= 0) {
            throw new InvalidQuantityException("Order quantity must be positive.");
        }
        String orderId = "SO-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        SalesOrder order = new SalesOrder(orderId, productId, quantity, LocalDateTime.now(), SalesOrder.Status.PROCESSING, customerDetails);
        salesOrderRepository.save(order);
        return order;
    }

    public Shipment fulfillSalesOrder(String orderId, String warehouseId)
            throws InsufficientStockException, EntityNotFoundException, InvalidQuantityException {

        SalesOrder order = salesOrderRepository.findById(orderId);

        if (order == null) {
            throw new EntityNotFoundException("Sales Order not found: " + orderId);
        }
        if (order.getStatus() != SalesOrder.Status.PROCESSING) {
            throw new IllegalStateException("Order must be in PROCESSING status to fulfill.");
        }

        String productId = order.getProductId();
        int quantity = order.getQuantity();

        inventoryService.removeStock(productId, warehouseId, quantity);

        order.setStatus(SalesOrder.Status.FULFILLED);
        salesOrderRepository.update(order);

        CLIPrinter.printHeader("Creating a Shipment id");
        String shipmentId = "SH-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        Shipment shipment = new Shipment(
                shipmentId,
                orderId,
                warehouseId,
                order.getCustomerDetails(),
                LocalDateTime.now(),
                Shipment.Status.IN_TRANSIT
        );
        shipmentRepository.save(shipment);
        return shipment;
    }
    public SalesOrder findSalesOrder(String orderId) {
        return salesOrderRepository.findById(orderId);
    }
}
