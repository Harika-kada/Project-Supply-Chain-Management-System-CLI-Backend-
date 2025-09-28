package com.company.scm.repository;

import com.company.scm.model.Product;
import com.company.scm.util.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {
    private final Map<String, Product> store = new HashMap<>();

    public void save(Product product)
    {
        store.put(product.getProductId(), product);
        LoggerUtil.info("Product saved: " + product.getProductId());
    }
    public Product findById(String productId) {
        return store.get(productId);
    }
    public List<Product> findAll() {
        LoggerUtil.info("Fetching all " + store.size() + " product items.");
        return new ArrayList<>(store.values());
    }
    public void clear() {
        store.clear();
        LoggerUtil.info("Product repository cleared.");
    }
}
