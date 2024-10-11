package com.damzxyno.salesportaltest.controller;

import com.damzxyno.salesportaltest.config.LocationRestricted;
import com.damzxyno.salesportaltest.model.Product;
import com.damzxyno.salesportaltest.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing product-related operations.
 * -----------------------------------------------------------
 * This controller exposes endpoints under the "/api/v1/products" URI,
 * and all the requests are restricted to users accessing from accepted locations.
 * The location restriction for this controller allows access only from the "UK".
 * ---------------
 * It provides two primary endpoints:
 * 1. Fetching a list of all available products.
 * 2. Fetching details of a specific product by its ID.
 * -----------------------------------------------------------------
 * This controller leverages {@code ProductService} to handle business logic.
 * -----------------------
 * Annotations:
 * - {@code @RestController}: Indicates that this class is a REST controller.
 * - {@code @RequestMapping}: Maps requests to "/api/v1/products".
 * - {@code @RequiredArgsConstructor}: Automatically generates a constructor with required fields.
 * - {@code @LocationRestricted}: Restricts access to users from the "UK".
 *
 * @author damzxyno
 */
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
