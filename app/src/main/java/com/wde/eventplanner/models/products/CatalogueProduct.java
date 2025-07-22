package com.wde.eventplanner.models.products;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogueProduct {
    private UUID productId;
    private String name;
    private Double price;
    private Double salePercentage;

    public CatalogueProduct(CatalogueProduct old) {
        productId = old.productId;
        name = old.name;
        price = old.price;
        salePercentage = old.salePercentage;
    }

    public Boolean equals(CatalogueProduct cp) {
        return productId == cp.productId && name.equals(cp.name) && price.equals(cp.price) && salePercentage.equals(cp.salePercentage);
    }
}
