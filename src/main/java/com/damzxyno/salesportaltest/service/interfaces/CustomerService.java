package com.damzxyno.salesportaltest.service.interfaces;


import com.damzxyno.salesportaltest.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCustomers();

    Customer updateCustomer(Long id, Customer customer);
}
