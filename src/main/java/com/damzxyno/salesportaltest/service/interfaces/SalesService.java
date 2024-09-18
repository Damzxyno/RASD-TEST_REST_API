package com.damzxyno.salesportaltest.service.interfaces;



import com.damzxyno.salesportaltest.model.Sale;

import java.util.List;

public interface SalesService {
    Sale addSale(Sale sale);

    List<Sale> getSales();

    Sale getSale(long id);

    Sale updateSale(Sale sale);
}
