package com.damzxyno.salesportaltest.controller;


import com.damzxyno.salesportaltest.config.TimeRestricted;
import com.damzxyno.salesportaltest.model.Sale;
import com.damzxyno.salesportaltest.service.interfaces.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/sales"})
@PreAuthorize(
        "hasRole('ROLE_PERMISSION') or " +
                "(hasRole('ADMIN') and hasAuthority('VIEW_SALES')) or " +
                "hasRole('SALES_MANAGER')"
)
public class SalesController {
    private final SalesService salesService;


    @GetMapping
    public ResponseEntity<List<Sale>> getSale(){
        var resp =  salesService.getSales();
        return ResponseEntity.ok(resp);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable long id){
        var resp =  salesService.getSale(id);
        return ResponseEntity.ok(resp);
    }

    @TimeRestricted(
            startTime = "08:00",
            endTime = "21:00",
            timeZone = "GMT",
            restrictedDays = {DayOfWeek.SATURDAY, DayOfWeek.SUNDAY}
    )
    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale){
        var resp =  salesService.addSale(sale);
        return ResponseEntity.ok(resp);
    }

    @TimeRestricted(
            startTime = "08:00",
            endTime = "21:00",
            timeZone = "GMT",
            restrictedDays = {DayOfWeek.SATURDAY, DayOfWeek.SUNDAY}
    )
    @PutMapping
    public ResponseEntity<Sale> modifySale(@RequestBody Sale sale){
        var resp =  salesService.updateSale(sale);
        return ResponseEntity.ok(resp);
    }
}
