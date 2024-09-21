package com.damzxyno.salesportaltest.service;

import com.damzxyno.salesportaltest.model.Sale;
import com.damzxyno.salesportaltest.service.interfaces.SalesService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {
    private final List<Sale> sales = new ArrayList<>(List.of(
            Sale.builder().id(1L).description("Mr. Jones' sale").build(),
            Sale.builder().id(2L).description("Dr. Kareem's sale").build(),
            Sale.builder().id(3L).description("Rev. Martins' sale").build()
    ));

    @Override
    public Sale addSale(Sale sale) {
        sale.setId((long) sales.size());
        sales.add(sale);
        return sale;
    }

    @Override
    public List<Sale> getSales() {
        return sales;
    }

    @Override
    public Sale getSale(long id) {
        for (Sale sale : sales){
            if (sale.getId() == id){
                return sale;
            }
        }
        return null;
    }

    @Override
    public Sale updateSale(Sale sale) {
        return null;
    }
}
