package com.company.scm;

import com.company.scm.model.*;
import com.company.scm.service.*;
import com.company.scm.repository.*;
import com.company.scm.exception.*;
import com.company.scm.util.CLIPrinter;
import com.company.scm.util.LoggerUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class App {
    public static void main(String[] args) {
        InventoryRepository inventoryRepository = new InventoryRepository();
        SupplierRepository supplierRepository = new SupplierRepository();
        WarehouseRepository warehouseRepository = new WarehouseRepository();
        ProductRepository productRepository = new ProductRepository();
        SalesOrderRepository salesOrderRepository = new SalesOrderRepository();
        PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
        ShipmentRepository shipmentRepository = new ShipmentRepository();

        InventoryService inventoryService = new InventoryService(inventoryRepository, warehouseRepository);
        SupplierService supplierService = new SupplierService(supplierRepository);
        WarehouseService warehouseService = new WarehouseService(warehouseRepository);
        OrderService orderService = new OrderService(purchaseOrderRepository, salesOrderRepository, inventoryService, shipmentRepository);
        ReportService reportService = new ReportService(inventoryRepository, productRepository);

        try {
            Product laptop = new Product("P001", "Laptop", "Business laptop", "Electronics", new BigDecimal("75000.00"));
            Product mouse = new Product("P002", "Wireless Mouse", "Standard mouse", "Accessories", new BigDecimal("1500.00"));
            productRepository.save(laptop);
            productRepository.save(mouse);

            Supplier supplier = new Supplier("S001", "Global Supplies", "contact@globalsupplies.com", 4.5);
            supplierService.addSupplier(supplier);

            Warehouse warehouse1 = new Warehouse("W001", "Hyderabad", 1000);
            warehouseService.addWarehouse(warehouse1);
            Warehouse warehouse2 = new Warehouse("W002", "Bangalore", 1500);
            warehouseService.addWarehouse(warehouse2);

            inventoryService.addStock("P001", "W001", 10);
            inventoryService.addStock("P002", "W001", 50);
            inventoryService.addStock("P001", "W002", 5);

            PurchaseOrder po = new PurchaseOrder("PO001", "S001", "P001", 5, LocalDate.now(), PurchaseOrder.Status.PENDING);
            purchaseOrderRepository.save(po);

            SalesOrder so = orderService.createSalesOrder("P001", 3, "Amazon Enterprises");

            LoggerUtil.info("Attempting to fulfill Sales Order " + so.getOrderId() + "...");
            Shipment shipment = orderService.fulfillSalesOrder(so.getOrderId(), "W001");
            LoggerUtil.info("SUCCESS: Order fulfilled and Shipment " + shipment.getShipmentId() + " created.");

            inventoryService.transferStock("P002", "W001", "W002", 10);
            LoggerUtil.info("SUCCESS: Transferred 10 units of P002 from W001 to W002.");

            CLIPrinter.printHeader("GENERATING REPORTS");
            reportService.printInventoryReport();
            reportService.printLowStockReport(5);

            CLIPrinter.printDivider();
            LoggerUtil.info("\nSales Order Status (After Fulfillment): " + orderService.findSalesOrder("SO-xxxx").getStatus());

        } catch (InsufficientStockException e) {
            LoggerUtil.error("BUSINESS ERROR: " + e.getMessage());
        } catch (EntityNotFoundException | DuplicateEntityException | InvalidQuantityException e) {
            LoggerUtil.error("SYSTEM ERROR: " + e.getMessage());
        } catch (Exception e) {
            LoggerUtil.error("UNEXPECTED ERROR: " + e.getMessage());
        }
    }
}
