package com.damzxyno.salesportaltest.service.interfaces;

import com.damzxyno.salesportaltest.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    Product getProduct(long id);

    Product addProduct(Product product);

    Product updateProduct(Product product);

    Product deleteProduct(long id);
}
