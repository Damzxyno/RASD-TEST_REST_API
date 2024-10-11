package com.damzxyno.salesportaltest.controller;

import com.damzxyno.salesportaltest.model.Customer;
import com.damzxyno.salesportaltest.service.interfaces.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/customers"})
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        var resp = customerService.getCustomers();
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> modifyCustomers(@PathVariable Long id, @RequestBody Customer customer) {
        var resp = customerService.updateCustomer(id, customer);
        return ResponseEntity.ok(resp);
    }
}