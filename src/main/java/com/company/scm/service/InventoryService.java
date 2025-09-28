package com.company.scm.service;

import com.company.scm.exception.InsufficientStockException;
import com.company.scm.exception.InvalidQuantityException;
import com.company.scm.exception.EntityNotFoundException;
import com.company.scm.model.InventoryItem;
import com.company.scm.repository.InventoryRepository;
import com.company.scm.repository.WarehouseRepository;
import com.company.scm.util.LoggerUtil;

import java.time.LocalDate;

public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;

    public InventoryService(InventoryRepository inventoryRepository, WarehouseRepository warehouseRepository) {
        this.inventoryRepository = inventoryRepository;
        this.warehouseRepository = warehouseRepository;
    }
    private void validateQuantity(int quantity) throws InvalidQuantityException
    {
        if (quantity <= 0)
        {
            throw new InvalidQuantityException("Quantity must be positive.");
        }
    }
    public void addStock(String productId, String warehouseId, int quantity) throws InvalidQuantityException, EntityNotFoundException
    {
        LoggerUtil.info("Adding stock to inventory");
        validateQuantity(quantity);

        if (warehouseRepository.findById(warehouseId) == null)
        {
            throw new EntityNotFoundException(warehouseId);
        }

        InventoryItem item = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);

        if (item == null)
        {
            item = new InventoryItem(productId, warehouseId, quantity, LocalDate.now());
        }
        else
        {
            item.setQuantity(item.getQuantity() + quantity);
            item.setLastRestockedDate(LocalDate.now());
        }
        inventoryRepository.save(item);
    }
    public void removeStock(String productId, String warehouseId, int quantity)
            throws InvalidQuantityException, InsufficientStockException, EntityNotFoundException
    {
        LoggerUtil.info("Removing stock from inventory");
        validateQuantity(quantity);

        InventoryItem item = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);

        if (item == null)
        {
            throw new EntityNotFoundException(
                    String.format("Inventory item for product %s in warehouse %s not found.", productId, warehouseId));
        }

        int currentQuantity = item.getQuantity();

        if (currentQuantity < quantity) {
            throw new InsufficientStockException(
                    String.format("Insufficient stock for product %s. Requested: %d, Available: %d",
                            productId, quantity, currentQuantity));
        }
        item.setQuantity(currentQuantity - quantity);
        inventoryRepository.save(item);
    }
    public int getStockLevel(String productId, String warehouseId)
    {
        InventoryItem item = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId);

        if (item != null)
        {
            LoggerUtil.info("Quantity of stock in inventory: ");
            return item.getQuantity();
        }
        else
        {
            LoggerUtil.info("Quantity of stock in inventory: ");
            return 0;
        }
    }
    public void transferStock(String productId, String fromWarehouseId, String toWarehouseId, int quantity)
            throws InsufficientStockException, InvalidQuantityException, EntityNotFoundException
    {
        removeStock(productId, fromWarehouseId, quantity);
        try
        {
            addStock(productId, toWarehouseId, quantity);
            LoggerUtil.info("Transferring of stock from warehouse " + fromWarehouseId + " to warehouse " + toWarehouseId);
        }
        catch (EntityNotFoundException e) {
            throw e;
        }
    }
}
