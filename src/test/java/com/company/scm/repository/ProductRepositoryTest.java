package com.company.scm.repository;

import com.company.scm.model.Product;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.*;

@Test(groups = "repository")
public class ProductRepositoryTest {

    private ProductRepository repository;
    private final String PRODUCT_ID = "P001";
    private final Product testProduct = new Product(PRODUCT_ID, "Laptop", "High-end", "Electronics", new BigDecimal("1200.00"));

    @BeforeMethod
    public void setup() {
        repository = new ProductRepository();
        repository.save(testProduct);
    }

    @AfterMethod
    public void cleanup() {
        repository.clear();
    }

    @Test(testName = "Find by ID should retrieve the correct product")
    public void testFindById_Success() {
        Product found = repository.findById(PRODUCT_ID);
        assertNotNull(found);
        assertEquals(found.getName(), "Laptop");
    }

    @Test(testName = "Find all should return all saved products")
    public void testFindAll() {
        repository.save(new Product("P002", "Mouse", "Standard", "Accessories", new BigDecimal("20.00")));
        List<Product> products = repository.findAll();
        assertEquals(products.size(), 2);
    }

    @Test(testName = "Find by ID for a non-existent product should return null")
    public void testFindById_NotFound() {
        Product found = repository.findById("P999");
        assertNull(found);
    }
}
