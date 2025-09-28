package com.company.scm.service;

import com.company.scm.model.InventoryItem;
import com.company.scm.model.Product;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.ProductRepository;
import com.company.scm.util.CLIPrinter;
import com.company.scm.util.LoggerUtil;

public class ReportService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ReportService(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public void printInventoryReport() {
        CLIPrinter.printHeader("Inventory Report");
        for (InventoryItem item : inventoryRepository.findAll().values()) {
            Product product = productRepository.findById(item.getProductId());
            String productName = (product != null) ? product.getName() : "Unknown Product";

            LoggerUtil.info("Product Id: " + item.getProductId() + "\nProduct: " + productName + "\nQuantity: " + item.getQuantity() + "\nWarehouse: " + item.getWarehouseId());
        }
        CLIPrinter.printDivider();
    }

    public void printLowStockReport(int threshold) {
        CLIPrinter.printHeader("Low Stock Report (Threshold: " + threshold + ")");
        for (InventoryItem item : inventoryRepository.findAll().values()) {
            if (item.getQuantity() <= threshold) {
                Product product = productRepository.findById(item.getProductId());
                String productName;
                if (product != null) {
                    productName = product.getName();
                } else {
                    productName = "Unknown Product";
                }
                LoggerUtil.info("Product Id: " + item.getProductId() + "\nProduct: " + productName + "\nQuantity: " + item.getQuantity() + "\nWarehouse: " + item.getWarehouseId());
            }
        }
        CLIPrinter.printDivider();
    }
}
