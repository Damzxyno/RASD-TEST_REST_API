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

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        var resp = customerService.getCustomers();
        return ResponseEntity.ok(resp);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PutMapping
    public ResponseEntity<Customer> modifyCustomers(@RequestBody Customer customer) {
        var resp = customerService.updateCustomer(customer);
        return ResponseEntity.ok(resp);
    }


}