package com.wde.eventplanner.models.services;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueService {
    private UUID serviceId;
    private String name;
    private Double price;
    private Double salePercentage;

    public CatalogueService(CatalogueService old) {
        serviceId = old.serviceId;
        name = old.name;
        price = old.price;
        salePercentage = old.salePercentage;
    }
}
