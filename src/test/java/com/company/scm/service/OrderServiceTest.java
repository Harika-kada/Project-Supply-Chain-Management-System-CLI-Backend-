package com.company.scm.service;

import com.company.scm.exception.InsufficientStockException;
import com.company.scm.model.SalesOrder;
import com.company.scm.model.Shipment;
import com.company.scm.repository.*;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Test(groups = "orders")
public class OrderServiceTest {

    private OrderService orderService;
    private InventoryService inventoryService;
    private SalesOrderRepository salesOrderRepository;
    private ShipmentRepository shipmentRepository;

    private final String PRODUCT_ID = "P001";
    private final String WAREHOUSE_ID = "W001";
    private final int INITIAL_STOCK = 50;

    @BeforeMethod
    public void setup() throws Exception {
        PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
        InventoryRepository inventoryRepository = new InventoryRepository();
        WarehouseRepository warehouseRepository = new WarehouseRepository();
        salesOrderRepository = new SalesOrderRepository();
        shipmentRepository = new ShipmentRepository();

        inventoryService = new InventoryService(inventoryRepository, warehouseRepository);
        orderService = new OrderService(purchaseOrderRepository, salesOrderRepository, inventoryService, shipmentRepository);

        warehouseRepository.addWarehouse(WAREHOUSE_ID, "Test Location", 1000);
        inventoryService.addStock(PRODUCT_ID, WAREHOUSE_ID, INITIAL_STOCK);
    }

    @AfterMethod
    public void cleanup() {
        salesOrderRepository.clear();
        shipmentRepository.clear();
    }

    @Test(priority = 1, testName = "Successful fulfillment updates state and creates shipment")
    public void testFulfillSalesOrder_Success() throws Exception {

        SalesOrder order = orderService.createSalesOrder(PRODUCT_ID, 10, "Customer B");
        String orderId = order.getOrderId();

        Shipment shipment = orderService.fulfillSalesOrder(orderId, WAREHOUSE_ID);

        SalesOrder fulfilledOrder = orderService.findSalesOrder(orderId);
        assertEquals(fulfilledOrder.getStatus(), SalesOrder.Status.FULFILLED, "Order status should be FULFILLED.");
        assertEquals(inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_ID),
                INITIAL_STOCK - order.getQuantity(),
                "Inventory level should be reduced correctly.");
        assertNotNull(shipment.getShipmentId(), "A shipment record must be created.");
        assertEquals(shipmentRepository.findAll().size(), 1, "Shipment repository must contain one new record.");
    }

    @Test(priority = 2, testName = "Fulfilling an order with insufficient stock should throw InsufficientStockException",
            expectedExceptions = InsufficientStockException.class)
    public void testFulfillSalesOrder_InsufficientStock_ThrowsException() throws Exception {
        SalesOrder order = orderService.createSalesOrder(PRODUCT_ID, INITIAL_STOCK + 1, "Customer C");

        orderService.fulfillSalesOrder(order.getOrderId(), WAREHOUSE_ID);

        assertEquals(inventoryService.getStockLevel(PRODUCT_ID, WAREHOUSE_ID), INITIAL_STOCK, "Stock should not be modified if fulfillment fails.");
    }
}
