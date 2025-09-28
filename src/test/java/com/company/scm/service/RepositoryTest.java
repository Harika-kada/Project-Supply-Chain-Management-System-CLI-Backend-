package com.company.scm.service;

import com.company.scm.model.InventoryItem;
import com.company.scm.model.Product;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.ProductRepository;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.assertTrue;

@Test(groups = "report")
public class ReportServiceTest {

    private InventoryRepository inventoryRepository;
    private ProductRepository productRepository;
    private ReportService reportService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeMethod
    public void setup() {
        inventoryRepository = new InventoryRepository();
        productRepository = new ProductRepository();
        reportService = new ReportService(inventoryRepository, productRepository);

        System.setOut(new PrintStream(outContent));

        productRepository.save(new Product("P001", "Laptop", "Desc", "E", new BigDecimal("1000")));
        productRepository.save(new Product("P002", "Monitor", "Desc", "E", new BigDecimal("200")));

        inventoryRepository.save(new InventoryItem("P001", "W001", 10, LocalDate.now())); // High Stock
        inventoryRepository.save(new InventoryItem("P002", "W001", 3, LocalDate.now()));  // Low Stock
    }

    @AfterMethod
    public void cleanup() {
        System.setOut(originalOut);
        inventoryRepository.clear();
        productRepository.clear();
    }

    @Test(testName = "Inventory Report should list all items correctly")
    public void testPrintInventoryReport() {
        reportService.printInventoryReport();
        String reportOutput = outContent.toString();

        assertTrue(reportOutput.contains("Laptop (P001) | Qty: 10"));
        assertTrue(reportOutput.contains("Monitor (P002) | Qty: 3"));
    }

    @Test(testName = "Low Stock Report should only show items below threshold")
    public void testPrintLowStockReport() {
        int threshold = 5;
        reportService.printLowStockReport(threshold);
        String reportOutput = outContent.toString();

        assertTrue(reportOutput.contains("LOW STOCK: Monitor (P002) | Qty: 3"));
        assertTrue(reportOutput.contains("Threshold: 5"));
        assertTrue(!reportOutput.contains("Laptop (P001)"));
    }
}
