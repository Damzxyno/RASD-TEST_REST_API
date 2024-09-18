package com.damzxyno.salesportaltest.service;

import com.damzxyno.salesportaltest.model.Product;
import com.damzxyno.salesportaltest.service.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public List<Product> getProducts() {
        return null;
    }

    @Override
    public Product getProduct(long id) {
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Product product) {
        return null;
    }

    @Override
    public Product deleteProduct(long id) {
        return null;
    }
}
