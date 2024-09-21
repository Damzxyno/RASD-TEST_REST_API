package com.damzxyno.salesportaltest.repository;

import com.damzxyno.salesportaltest.model.Sale;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalesRepositoryImpl implements  SalesRepository{
    private final List<Sale> sales = new ArrayList<>(List.of(
            Sale.builder().id(1L).description("Mr. Jones' sale").build(),
            Sale.builder().id(2L).description("Dr. Kaleem's sale").build(),
            Sale.builder().id(3L).description("Dr. Ling's sale").build(),
            Sale.builder().id(4L).description("Mr. Oluwole's sale").build(),
            Sale.builder().id(5L).description("Prof. Adams' sale").build(),
            Sale.builder().id(6L).description("Mr. Rachael's sale").build(),
            Sale.builder().id(7L).description("Mrs. Lee's sale").build(),
            Sale.builder().id(8L).description("Dr. Kabir's sale").build()
    ));

    @Override
    public List<Sale> findAll() {
        return new ArrayList<>(sales);
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return sales.stream()
                .filter(sale -> sale.getId().equals(id))
                .findFirst();
    }

    @Override
    public Sale save(Sale sale) {
        Optional<Sale> existingSale = findById(sale.getId());
        if (existingSale.isPresent()) {
            // Update existing sale
            sales.remove(existingSale.get());
        }
        sales.add(sale);
        return sale;
    }

    @Override
    public void deleteById(Long id) {
        sales.removeIf(sale -> sale.getId().equals(id));
    }

    @Override
    public List<Sale> findByDescriptionContaining(String keyword) {
        return sales.stream()
                .filter(sale -> sale.getDescription().contains(keyword))
                .collect(Collectors.toList());
    }
}
