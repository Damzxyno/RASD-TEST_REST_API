package com.damzxyno.salesportaltest.controller;


import com.damzxyno.salesportaltest.config.LocationRestricted;
import com.damzxyno.salesportaltest.model.Product;
import com.damzxyno.salesportaltest.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/products"})
@LocationRestricted(locationAccepted = {"UK"})
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        var resp =  productService.getProducts();
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable long id){
        var resp =  productService.getProduct(id);
        return ResponseEntity.ok(resp);
    }


    @PreAuthorize(
            "(hasRole('ADMIN') and hasAuthority('MANAGE_PRODUCT')) or " +
                    "hasRole('SALES_MANAGER')"
    )
    @PostMapping
    public ResponseEntity<Product> createSale(@RequestBody Product product){
        var resp =  productService.addProduct(product);
        return ResponseEntity.ok(resp);
    }

    @PreAuthorize(
            "(hasRole('ADMIN') and hasAuthority('MANAGE_PRODUCT')) or " +
                    "hasRole('SALES_MANAGER') or " +
                    "hasRole('MARKETING_MANAGER')"
    )
    @PutMapping
    public ResponseEntity<Product> modifySale(@RequestBody Product product){
        var resp =  productService.updateProduct(product);
        return ResponseEntity.ok(resp);
    }


    @PreAuthorize("hasRole('ADMIN') and hasAuthority('MANAGE_PRODUCT')")

    @DeleteMapping
    public ResponseEntity<Product> modifySale(@RequestBody long id){
        var resp =  productService.deleteProduct(id);
        return ResponseEntity.ok(resp);
    }
}
