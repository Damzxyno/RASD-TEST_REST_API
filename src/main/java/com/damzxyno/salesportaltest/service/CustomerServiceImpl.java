package com.damzxyno.salesportaltest.service;

import com.damzxyno.salesportaltest.model.Customer;
import com.damzxyno.salesportaltest.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public List<Customer> getCustomers() {
        return null;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }
}
