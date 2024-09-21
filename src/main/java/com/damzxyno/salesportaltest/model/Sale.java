package com.damzxyno.salesportaltest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private Long id;
    private String description;
    private Long customerId;
}
