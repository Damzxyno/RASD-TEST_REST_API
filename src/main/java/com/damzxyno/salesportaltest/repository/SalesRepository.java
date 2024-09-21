package com.damzxyno.salesportaltest.repository;

import com.damzxyno.salesportaltest.model.Sale;

import java.util.List;
import java.util.Optional;

public interface SalesRepository {
    List<Sale> findAll();
    Optional<Sale> findById(Long id);
    Sale save(Sale sale);
    void deleteById(Long id);
    List<Sale> findByDescriptionContaining(String keyword);
}
