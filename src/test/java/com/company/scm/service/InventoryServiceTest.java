package com.company.scm.service;

import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.WarehouseRepository;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "inventory")
public class InventoryServiceTest {

    private InventoryRepository inventoryRepository;
    private WarehouseRepository warehouseRepository;
    private InventoryService inventoryService;

    private final String PRODUCT_ID = "P001";
    private final String WAREHOUSE_A_ID = "W001";
    private final String WAREHOUSE_B_ID = "W002";
    private final int INITIAL_STOCK = 10;

    @BeforeMethod
    public void setup() throws Exception {
        inventoryRepository = new InventoryRepository();
        warehouseRepository = new WarehouseRepository();
        inventoryService = new InventoryService(inventoryRepository, warehouseRepository);

        inventoryService.addStock(PRODUCT_ID, WAREHOUSE_A_ID, INITIAL_STOCK);
    }

    @AfterMethod
    public void cleanup() {
        inventoryRepository.clear();
    }

    @Test(testName = "Adding stock should increase inventory correctly")
    public void testAddStockSuccess() throws Exception {
        int quantityToAdd = 5;
        inventoryService.addStock(PRODUCT_ID, WAREHOUSE_A_ID, quantityToAdd);

        int newStock = inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_A_ID);
        assertEquals(newStock, INITIAL_STOCK + quantityToAdd, "Stock should increase by " + quantityToAdd);
    }

    @Test(testName = "Removing stock within available quantity should succeed")
    public void testRemoveStockSuccess() throws Exception {
        inventoryService.removeStock(PRODUCT_ID, WAREHOUSE_A_ID, 5);
        int newStock = inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_A_ID);
        assertEquals(newStock, 5);
    }

    @Test(testName = "Removing more stock than available should throw InsufficientStockException",
            expectedExceptions = InsufficientStockException.class, // Required: Use expectedExceptions
            expectedExceptionsMessageRegExp = ".*Insufficient stock.*") // Required: Use message regex
    public void testRemoveStockExceedingQuantity_ThrowsException() throws Exception {
        int quantityToRemove = INITIAL_STOCK + 1;
        inventoryService.removeStock(PRODUCT_ID, WAREHOUSE_A_ID, quantityToRemove);
    }

    @Test(testName = "Using zero quantity should throw InvalidQuantityException",
            expectedExceptions = InvalidQuantityException.class)
    public void testAddStock_ZeroQuantity_ThrowsException() throws Exception {
        inventoryService.addStock(PRODUCT_ID, WAREHOUSE_A_ID, 0);
    }

    @Test(testName = "Transferring to a non-existent warehouse should throw EntityNotFoundException",
            expectedExceptions = EntityNotFoundException.class)
    public void testTransferStock_InvalidDestination_ThrowsException() throws Exception {
        String nonExistentWarehouse = "W999";

        warehouseRepository.setFailOnFind(nonExistentWarehouse);
        inventoryService.transferStock(PRODUCT_ID, WAREHOUSE_A_ID, nonExistentWarehouse, 1);
    }

    @DataProvider(name = "transferData")
    public Object[][] provideTransferData() {

        return new Object[][] {
                {2, 8, 2},
                {5, 5, 5},
                {10, 0, 10}
        };
    }

    @Test(dataProvider = "transferData", testName = "Stock Transfer updates both warehouses correctly")
    public void testStockTransferUpdatesBothWarehouses(
            int qtyToTransfer, int expectedSourceQty, int expectedDestQty) throws Exception {

        inventoryService.addStock(PRODUCT_ID, WAREHOUSE_B_ID, 0);

        inventoryService.transferStock(PRODUCT_ID, WAREHOUSE_A_ID, WAREHOUSE_B_ID, qtyToTransfer);

        int sourceStock = inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_A_ID);
        int destStock = inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_B_ID);

        assertEquals(sourceStock, expectedSourceQty, "Source warehouse stock incorrect.");
        assertEquals(destStock, expectedDestQty, "Destination warehouse stock incorrect.");
    }
}
